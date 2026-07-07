# 0x1lBlog — Database Redesign v2

> **Target:** PostgreSQL 16+
> **Design date:** 2026-06-18
> **Status:** Approved

---

## 1. Design Decisions

| Decision | Solution | Lý do |
|----------|----------|-------|
| **Roles** | USER + ADMIN | Mọi user đều viết blog được như FB |
| **Creator mode** | `users.is_creator` flag | Monetization là tính năng, không phải role |
| **Visitor tracking** | `sessions` table (session-based) | Fingerprint không đủ unique |
| **Audit/Activity** | `activity_log` unified (gộp page_views + event_log + api_logs) | Tránh UNION, 1 nguồn thống kê |
| **Trace ID** | ULID (`trace_id`) | Gom nhóm events trong cùng 1 page view, tối ưu index |
| **IP ownership** | Chỉ `sessions` có IP | Các table khác JOIN sessions để lấy |
| **Source** | `activity_log.source` CHECK('USER','SYSTEM') | Phân biệt hành vi user vs system |
| **Dead tables removed** | moment, conversation, message, story, guest | Từ MySQL era, không dùng |
| **Full-text search** | `search_vector tsvector GENERATED ALWAYS AS` + GIN | PostgreSQL native FTS |
| **Reaction counters** | 6 denormalized columns on blogs | like, love, haha, wow, sad, angry |

---

## 2. Table Catalog

### Phase 1 (27 tables)

| # | Table | Rows/ngày (10k DAU) | Mục đích |
|---|-------|---------------------|----------|
| 1 | `users` | — | Tài khoản người dùng |
| 2 | `oauth2_accounts` | — | OAuth2 links |
| 3 | `refresh_tokens` | — | Refresh token hash |
| 4 | `roles` | — | Built-in: USER, ADMIN |
| 5 | `permissions` | — | Granular permissions |
| 6 | `role_permissions` | — | Junction |
| 7 | `categories` | — | Danh mục blog |
| 8 | `hashtags` | — | Hashtag |
| 9 | `blog_hashtags` | ~3k | Junction blog↔hashtag |
| 10 | `blogs` | ~200 | Bài viết |
| 11 | `blog_series` | — | Series |
| 12 | `series_blogs` | ~200 | Junction blog↔series |
| 13 | `series_subscribers` | — | Subscribe series |
| 14 | `comments` | ~2k | Bình luận 2 cấp |
| 15 | `comment_reactions` | ~1k | LIKE comment |
| 16 | `blog_reactions` | ~3k | 6 cảm xúc |
| 17 | `bookmarks` | ~500 | Bookmark |
| 18 | `follows` | ~2k | Follow |
| 19 | `shares` | ~300 | Share |
| 20 | `mentions` | ~200 | @mention |
| 21 | `badges` | — | Huy hiệu |
| 22 | `user_badges` | — | Huy hiệu đã nhận |
| 23 | `daily_checkins` | ~5k | Điểm danh |
| 24 | `user_exp_log` | ~10k | EXP history |
| 25 | `notifications` | ~10k | Thông báo |
| 26 | `sessions` | ~15k | Phiên truy cập |
| 27 | `activity_log` | ~80k | Activity stream unified |
| 28 | `site_settings` | — | Cấu hình site |
| 29 | `about_info` | — | About/Terms/Privacy |
| 30 | `friends` (MV) | — | Mutual follow |

### Phase 2+ (8 tables)

| # | Table | Mục đích |
|---|-------|----------|
| 31 | `wallet_transactions` | Wallet immutable log |
| 32 | `tips` | Tip giữa users |
| 33 | `content_purchases` | Mua blog/series |
| 34 | `memberships` | Subscribe creator |
| 35 | `payouts` | Rút tiền |
| 36 | `user_feed` | "For You" materialized |
| 37 | `user_affinity` | Affinity score |
| 38 | `recommended_content` | Collaborative filtering cache |

---

## 3. Full DDL

### Extensions

```sql
CREATE EXTENSION IF NOT EXISTS pgcrypto;
CREATE EXTENSION IF NOT EXISTS pg_trgm;
CREATE EXTENSION IF NOT EXISTS btree_gin;
```

### 3.1 users

```sql
CREATE TABLE users (
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(50) NOT NULL UNIQUE,
    email           VARCHAR(255) UNIQUE,
    password_hash   VARCHAR(255),               -- NULL nếu OAuth2 only
    display_name    VARCHAR(100) NOT NULL DEFAULT '',
    avatar_url      VARCHAR(500),
    bio             TEXT DEFAULT '',
    website         VARCHAR(500) DEFAULT '',
    location        VARCHAR(200) DEFAULT '',
    social_links    JSONB DEFAULT '{}',

    -- Role & status
    role            VARCHAR(20) NOT NULL DEFAULT 'USER'
                        CHECK (role IN ('USER', 'ADMIN')),
    is_creator      BOOLEAN NOT NULL DEFAULT FALSE,
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
                        CHECK (status IN ('ACTIVE', 'INACTIVE', 'BANNED')),
    locked_until    TIMESTAMPTZ,

    -- Denormalized counters
    blog_count      INT NOT NULL DEFAULT 0,
    follower_count  INT NOT NULL DEFAULT 0,
    following_count INT NOT NULL DEFAULT 0,

    -- Gamification
    level           INT NOT NULL DEFAULT 1,
    exp             BIGINT NOT NULL DEFAULT 0,
    checkin_streak  INT NOT NULL DEFAULT 0,
    last_checkin_at DATE,

    -- Wallet (Phase 2)
    balance         BIGINT NOT NULL DEFAULT 0,
    bonus           BIGINT NOT NULL DEFAULT 0,

    -- Timestamps
    last_active_at  TIMESTAMPTZ DEFAULT NOW(),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE INDEX idx_users_email ON users(email) WHERE deleted_at IS NULL;
CREATE INDEX idx_users_username ON users(username) WHERE deleted_at IS NULL;
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_created_at ON users(created_at DESC);
```

### 3.2 oauth2_accounts

```sql
CREATE TABLE oauth2_accounts (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    provider        VARCHAR(50) NOT NULL,
    provider_id     VARCHAR(255) NOT NULL,
    avatar_url      VARCHAR(500),
    raw_attributes  JSONB DEFAULT '{}',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(provider, provider_id)
);

CREATE INDEX idx_oauth2_user ON oauth2_accounts(user_id);
```

### 3.3 refresh_tokens

```sql
CREATE TABLE refresh_tokens (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    token_hash      VARCHAR(255) NOT NULL UNIQUE,
    device_info     VARCHAR(500),
    ip_address      INET,
    expires_at      TIMESTAMPTZ NOT NULL,
    revoked         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_refresh_tokens_user ON refresh_tokens(user_id);
CREATE INDEX idx_refresh_tokens_expires ON refresh_tokens(expires_at) WHERE revoked = FALSE;
```

### 3.4 roles + permissions + role_permissions

```sql
CREATE TABLE roles (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE,
    description     TEXT DEFAULT ''
);

CREATE TABLE permissions (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL UNIQUE,
    description     TEXT DEFAULT ''
);

CREATE TABLE role_permissions (
    role_id         BIGINT NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
    permission_id   BIGINT NOT NULL REFERENCES permissions(id) ON DELETE CASCADE,
    PRIMARY KEY (role_id, permission_id)
);

INSERT INTO roles (name, description) VALUES
    ('USER', 'Người dùng thông thường, có thể viết blog'),
    ('ADMIN', 'Quản trị viên hệ thống');
```

### 3.5 categories

```sql
CREATE TABLE categories (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL UNIQUE,
    slug            VARCHAR(120) NOT NULL UNIQUE,
    description     TEXT DEFAULT '',
    icon            VARCHAR(255),
    color           VARCHAR(7),
    sort_order      INT NOT NULL DEFAULT 0,
    blog_count      INT NOT NULL DEFAULT 0,
    is_visible      BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE INDEX idx_categories_slug ON categories(slug) WHERE deleted_at IS NULL;
CREATE INDEX idx_categories_sort ON categories(sort_order, name) WHERE deleted_at IS NULL;
```

### 3.6 hashtags

```sql
CREATE TABLE hashtags (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE,
    usage_count     INT NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_hashtags_name_trgm ON hashtags USING GIN (name gin_trgm_ops);
CREATE INDEX idx_hashtags_usage ON hashtags(usage_count DESC);
```

### 3.7 blog_hashtags

```sql
CREATE TABLE blog_hashtags (
    blog_id         BIGINT NOT NULL REFERENCES blogs(id) ON DELETE CASCADE,
    hashtag_id      BIGINT NOT NULL REFERENCES hashtags(id) ON DELETE CASCADE,
    PRIMARY KEY (blog_id, hashtag_id)
);

CREATE INDEX idx_blog_hashtags_hashtag ON blog_hashtags(hashtag_id);
```

### 3.8 blogs

```sql
CREATE TABLE blogs (
    id              BIGSERIAL PRIMARY KEY,
    author_id       BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id     BIGINT REFERENCES categories(id) ON DELETE SET NULL,

    -- Content
    title           VARCHAR(200) NOT NULL,
    slug            VARCHAR(255) NOT NULL UNIQUE,
    content         TEXT NOT NULL,
    description     VARCHAR(500) DEFAULT '',
    cover_image     VARCHAR(500),
    content_type    VARCHAR(20) NOT NULL DEFAULT 'MARKDOWN'
                        CHECK (content_type IN ('MARKDOWN', 'HTML')),

    -- Location (optional)
    location_name   VARCHAR(200),
    latitude        DECIMAL(10,7),
    longitude       DECIMAL(10,7),

    -- Status & visibility
    status          VARCHAR(20) NOT NULL DEFAULT 'DRAFT'
                        CHECK (status IN ('DRAFT', 'PUBLISHED', 'ARCHIVED', 'DELETED')),
    visibility      VARCHAR(20) NOT NULL DEFAULT 'PUBLIC'
                        CHECK (visibility IN ('PUBLIC', 'MEMBERS_ONLY', 'PAID')),
    price           INT,
    is_top          BOOLEAN NOT NULL DEFAULT FALSE,
    is_recommend    BOOLEAN NOT NULL DEFAULT FALSE,
    allow_comments  BOOLEAN NOT NULL DEFAULT TRUE,

    -- Denormalized stats
    words           INT NOT NULL DEFAULT 0,
    read_time       INT NOT NULL DEFAULT 0,
    views           INT NOT NULL DEFAULT 0,
    like_count      INT NOT NULL DEFAULT 0,
    love_count      INT NOT NULL DEFAULT 0,
    haha_count      INT NOT NULL DEFAULT 0,
    wow_count       INT NOT NULL DEFAULT 0,
    sad_count       INT NOT NULL DEFAULT 0,
    angry_count     INT NOT NULL DEFAULT 0,
    comment_count   INT NOT NULL DEFAULT 0,
    bookmark_count  INT NOT NULL DEFAULT 0,
    share_count     INT NOT NULL DEFAULT 0,

    -- Timestamps
    published_at            TIMESTAMPTZ,
    last_commented_at       TIMESTAMPTZ,
    created_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at              TIMESTAMPTZ
);

-- Full-text search
ALTER TABLE blogs ADD COLUMN search_vector tsvector
    GENERATED ALWAYS AS (
        setweight(to_tsvector('simple', coalesce(title, '')), 'A') ||
        setweight(to_tsvector('simple', coalesce(description, '')), 'B') ||
        setweight(to_tsvector('simple', coalesce(content, '')), 'C')
    ) STORED;

-- Indexes
CREATE INDEX idx_blogs_author ON blogs(author_id, published_at DESC)
    WHERE status = 'PUBLISHED' AND deleted_at IS NULL;
CREATE INDEX idx_blogs_category ON blogs(category_id, published_at DESC)
    WHERE status = 'PUBLISHED' AND visibility = 'PUBLIC' AND deleted_at IS NULL;
CREATE INDEX idx_blogs_published ON blogs(published_at DESC)
    WHERE status = 'PUBLISHED' AND deleted_at IS NULL;
CREATE INDEX idx_blogs_slug ON blogs(slug) WHERE deleted_at IS NULL;
CREATE INDEX idx_blogs_status ON blogs(status, deleted_at);
CREATE INDEX idx_blogs_search ON blogs USING GIN (search_vector)
    WHERE status = 'PUBLISHED' AND deleted_at IS NULL;
CREATE INDEX idx_blogs_trending ON blogs(
    (views * 0.3 + like_count * 2 + comment_count * 3 + bookmark_count * 4 + share_count * 5) DESC
) WHERE status = 'PUBLISHED' AND deleted_at IS NULL;
```

### 3.9 blog_series

```sql
CREATE TABLE blog_series (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(200) NOT NULL,
    description     TEXT DEFAULT '',
    cover_image     VARCHAR(500),
    author_id       BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status          VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
                        CHECK (status IN ('ACTIVE', 'COMPLETED', 'ARCHIVED')),
    price           INT,
    post_count      INT NOT NULL DEFAULT 0,
    subscriber_count INT NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE INDEX idx_blog_series_author ON blog_series(author_id, created_at DESC) WHERE deleted_at IS NULL;
```

### 3.10 series_blogs

```sql
CREATE TABLE series_blogs (
    series_id       BIGINT NOT NULL REFERENCES blog_series(id) ON DELETE CASCADE,
    blog_id         BIGINT NOT NULL REFERENCES blogs(id) ON DELETE CASCADE,
    sort_order      INT NOT NULL DEFAULT 0,
    note            TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (series_id, blog_id)
);

CREATE INDEX idx_series_blogs_blog ON series_blogs(blog_id);
```

### 3.11 series_subscribers

```sql
CREATE TABLE series_subscribers (
    id              BIGSERIAL PRIMARY KEY,
    series_id       BIGINT NOT NULL REFERENCES blog_series(id) ON DELETE CASCADE,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(series_id, user_id)
);

CREATE INDEX idx_series_subs_user ON series_subscribers(user_id);
```

### 3.12 comments

2 cấp: comment (parent_id NULL) → reply (parent_id = comment.id).

```sql
CREATE TABLE comments (
    id              BIGSERIAL PRIMARY KEY,
    blog_id         BIGINT NOT NULL REFERENCES blogs(id) ON DELETE CASCADE,
    parent_id       BIGINT REFERENCES comments(id) ON DELETE CASCADE,
    user_id         BIGINT REFERENCES users(id) ON DELETE SET NULL,
    guest_name      VARCHAR(100),
    content         TEXT NOT NULL,
    status          VARCHAR(20) NOT NULL DEFAULT 'APPROVED'
                        CHECK (status IN ('APPROVED', 'PENDING', 'REJECTED', 'FLAGGED', 'DELETED')),
    like_count      INT NOT NULL DEFAULT 0,
    reply_count     INT NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ,
    CHECK (user_id IS NOT NULL OR guest_name IS NOT NULL)
);

CREATE INDEX idx_comments_blog ON comments(blog_id, parent_id, created_at DESC)
    WHERE status = 'APPROVED' AND deleted_at IS NULL;
CREATE INDEX idx_comments_user ON comments(user_id) WHERE deleted_at IS NULL;
CREATE INDEX idx_comments_parent ON comments(parent_id) WHERE deleted_at IS NULL;
```

### 3.13 comment_reactions

```sql
CREATE TABLE comment_reactions (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    comment_id      BIGINT NOT NULL REFERENCES comments(id) ON DELETE CASCADE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, comment_id)
);

CREATE INDEX idx_comment_reactions_comment ON comment_reactions(comment_id);
```

### 3.14 blog_reactions

```sql
CREATE TABLE blog_reactions (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    blog_id         BIGINT NOT NULL REFERENCES blogs(id) ON DELETE CASCADE,
    type            VARCHAR(10) NOT NULL
                        CHECK (type IN ('LIKE','LOVE','HAHA','WOW','SAD','ANGRY')),
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, blog_id)
);

CREATE INDEX idx_blog_reactions_blog ON blog_reactions(blog_id);
CREATE INDEX idx_blog_reactions_user ON blog_reactions(user_id);
```

### 3.15 bookmarks

```sql
CREATE TABLE bookmarks (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    blog_id         BIGINT NOT NULL REFERENCES blogs(id) ON DELETE CASCADE,
    collection      VARCHAR(200),
    note            TEXT,
    is_public       BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, blog_id)
);

CREATE INDEX idx_bookmarks_user ON bookmarks(user_id, collection);
CREATE INDEX idx_bookmarks_blog ON bookmarks(blog_id);
```

### 3.16 follows

```sql
CREATE TABLE follows (
    id              BIGSERIAL PRIMARY KEY,
    follower_id     BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    following_id    BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(follower_id, following_id),
    CHECK (follower_id != following_id)
);

CREATE INDEX idx_follows_follower ON follows(follower_id, created_at DESC);
CREATE INDEX idx_follows_following ON follows(following_id, created_at DESC);
```

### 3.17 shares

```sql
CREATE TABLE shares (
    id              BIGSERIAL PRIMARY KEY,
    blog_id         BIGINT NOT NULL REFERENCES blogs(id) ON DELETE CASCADE,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    quote_text      TEXT,
    platform        VARCHAR(50) DEFAULT 'INTERNAL',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_shares_blog ON shares(blog_id, created_at DESC);
CREATE INDEX idx_shares_user ON shares(user_id, created_at DESC);
```

### 3.18 mentions

```sql
CREATE TABLE mentions (
    id              BIGSERIAL PRIMARY KEY,
    target_user_id  BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    mentioned_by    BIGINT REFERENCES users(id) ON DELETE SET NULL,
    source_type     VARCHAR(20) NOT NULL CHECK (source_type IN ('BLOG', 'COMMENT')),
    source_id       BIGINT NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_mentions_user ON mentions(target_user_id, created_at DESC);
```

### 3.19 badges

```sql
CREATE TABLE badges (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE,
    display_name    VARCHAR(100) NOT NULL,
    description     TEXT DEFAULT '',
    icon_url        VARCHAR(500),
    tier            VARCHAR(10) NOT NULL CHECK (tier IN ('BRONZE', 'SILVER', 'GOLD')),
    criteria        JSONB NOT NULL DEFAULT '{}',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

### 3.20 user_badges

```sql
CREATE TABLE user_badges (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    badge_id        BIGINT NOT NULL REFERENCES badges(id) ON DELETE CASCADE,
    awarded_by      BIGINT REFERENCES users(id) ON DELETE SET NULL,
    awarded_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, badge_id)
);

CREATE INDEX idx_user_badges_user ON user_badges(user_id);
```

### 3.21 daily_checkins

```sql
CREATE TABLE daily_checkins (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    checkin_date    DATE NOT NULL DEFAULT CURRENT_DATE,
    streak_at_time  INT NOT NULL,
    bonus_exp       INT NOT NULL DEFAULT 0,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, checkin_date)
);

CREATE INDEX idx_checkins_user_date ON daily_checkins(user_id, checkin_date DESC);
```

### 3.22 user_exp_log

```sql
CREATE TABLE user_exp_log (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    amount          INT NOT NULL,
    reason          VARCHAR(50) NOT NULL,
    ref_id          BIGINT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_exp_log_user ON user_exp_log(user_id, created_at DESC);
```

### 3.23 notifications

```sql
CREATE TABLE notifications (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    actor_id        BIGINT REFERENCES users(id) ON DELETE SET NULL,
    type            VARCHAR(30) NOT NULL CHECK (type IN (
                        'NEW_COMMENT', 'NEW_REPLY', 'NEW_FOLLOWER',
                        'NEW_BLOG', 'LIKE_BLOG', 'LIKE_COMMENT',
                        'BADGE_AWARD', 'SERIES_NEW_POST', 'MENTION',
                        'TIP_RECEIVED', 'PAYOUT_STATUS'
                    )),
    title           VARCHAR(200) DEFAULT '',
    message         TEXT,
    target_type     VARCHAR(30),
    target_id       BIGINT,
    is_read         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_notifications_user ON notifications(user_id, created_at DESC);
CREATE INDEX idx_notifications_unread ON notifications(user_id, created_at DESC) WHERE is_read = FALSE;
```

### 3.24 sessions

Thay thế `visitors` + `visits` cũ. Mỗi phiên truy cập = 1 row. IP là chủ quyền duy nhất.

```sql
CREATE TABLE sessions (
    id              BIGSERIAL PRIMARY KEY,
    session_id      VARCHAR(26) NOT NULL UNIQUE,     -- ULID
    user_id         BIGINT REFERENCES users(id) ON DELETE SET NULL,
    ip_address      INET,                            -- source of truth duy nhất
    user_agent      TEXT,
    device_type     VARCHAR(20) CHECK (device_type IN ('DESKTOP', 'MOBILE', 'TABLET')),
    country_code    CHAR(2),
    city            VARCHAR(100),
    started_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    ended_at        TIMESTAMPTZ,
    duration_seconds INT
);

CREATE INDEX idx_sessions_user ON sessions(user_id, started_at DESC);
CREATE INDEX idx_sessions_session ON sessions(session_id);
```

### 3.25 activity_log

Unified activity stream. Gộp page_views + event_log + api_logs.

```sql
CREATE TABLE activity_log (
    id              BIGSERIAL PRIMARY KEY,
    trace_id        VARCHAR(26) NOT NULL,            -- ULID gom events cùng page view
    session_id      BIGINT REFERENCES sessions(id) ON DELETE SET NULL,
    user_id         BIGINT REFERENCES users(id) ON DELETE SET NULL,

    -- Classification
    category        VARCHAR(20) NOT NULL
                        CHECK (category IN ('PAGE_VIEW', 'ACTION', 'API', 'SYSTEM')),
    action          VARCHAR(50) NOT NULL,
    source          VARCHAR(10) NOT NULL DEFAULT 'USER'
                        CHECK (source IN ('USER', 'SYSTEM')),

    -- Target (polymorphic)
    target_type     VARCHAR(30),
    target_id       BIGINT,

    -- Rich metadata
    metadata        JSONB DEFAULT '{}',

    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_activity_trace ON activity_log(trace_id);
CREATE INDEX idx_activity_session ON activity_log(session_id, created_at DESC);
CREATE INDEX idx_activity_user ON activity_log(user_id, created_at DESC);
CREATE INDEX idx_activity_category ON activity_log(category, created_at DESC);
CREATE INDEX idx_activity_action ON activity_log(action, created_at DESC);
CREATE INDEX idx_activity_target ON activity_log(target_type, target_id, created_at DESC);
CREATE INDEX idx_activity_created ON activity_log(created_at DESC);
```

### 3.26 site_settings

```sql
CREATE TABLE site_settings (
    id              BIGSERIAL PRIMARY KEY,
    key             VARCHAR(100) NOT NULL UNIQUE,
    value           TEXT NOT NULL,
    type            VARCHAR(20) NOT NULL DEFAULT 'STRING',
    description     TEXT,
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

### 3.27 about_info

```sql
CREATE TABLE about_info (
    id              BIGSERIAL PRIMARY KEY,
    content         TEXT NOT NULL,
    type            VARCHAR(20) NOT NULL DEFAULT 'ABOUT',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

### 3.28 friends (materialized view)

```sql
CREATE MATERIALIZED VIEW friends AS
SELECT
    LEAST(f1.follower_id, f1.following_id) AS user_id1,
    GREATEST(f1.follower_id, f1.following_id) AS user_id2,
    f1.created_at AS friendship_started_at
FROM follows f1
JOIN follows f2
    ON f1.follower_id = f2.following_id
    AND f1.following_id = f2.follower_id;

CREATE UNIQUE INDEX idx_friends_pair ON friends(user_id1, user_id2);
```

---

## 4. Tables Removed

| Table cũ | Lý do |
|----------|-------|
| `visitors` | Fingerprint không đủ unique, chuyển sessions |
| `visits` | Gộp vào sessions |
| `page_views` | Gộp vào activity_log (category='PAGE_VIEW') |
| `event_log` | Gộp vào activity_log (category='ACTION'/'SYSTEM') |
| `api_logs` | Gộp vào activity_log (category='API') |
| `guest` | GUEST không có DB row, guest_name trong comments |
| `moment` | Tính năng Moment bỏ |
| `conversation` | Messaging dời Phase 2+ |
| `message` | Messaging dời Phase 2+ |
| `story` | Stories dời Phase 2+ |
| `tag` / `blog_tag` | Thay bằng hashtag system |

---

## 5. Tables Renamed

| Cũ | Mới | Ghi chú |
|----|-----|---------|
| `user` | `users` | Consistent số nhiều |
| `blog` | `blogs` | Consistent số nhiều |
| `category` | `categories` | Consistent số nhiều |
| `comment` | `comments` | Consistent số nhiều |
| `follow` | `follows` | Consistent số nhiều |
| `notification` | `notifications` | Consistent số nhiều |
| `badge` | `badges` | Consistent số nhiều |
| `role` | `roles` | Consistent số nhiều |
| `permission` | `permissions` | Consistent số nhiều |
| `about` | `about_info` | Rõ mục đích |

---

## 6. Index Strategy

| Query | Index |
|-------|-------|
| Homepage blog list | `blogs.published_at DESC WHERE status=PUBLISHED` |
| Blog by category | `blogs.category_id + published_at DESC` |
| Blog by author | `blogs.author_id + published_at DESC` |
| Blog by slug | `blogs.slug UNIQUE` |
| Full-text search | `blogs.search_vector GIN` |
| Trending | `blogs.(trending_expr) DESC` |
| Hashtag autocomplete | `hashtags.name gin_trgm_ops GIN` |
| Comments by blog | `comments.blog_id + parent_id + created_at DESC` |
| Feed by follow | `follows.follower_id + created_at DESC` |
| Notification list | `notifications.user_id + created_at DESC` |
| Unread count | `notifications.user_id WHERE is_read=FALSE` |
| Activity by trace | `activity_log.trace_id` |
| Activity by session | `activity_log.session_id + created_at DESC` |
| Activity by target | `activity_log.target_type + target_id + created_at DESC` |

---

## 7. Business Rules

1. **Comment depth 2**: application layer check `parent.parent != NULL → reject`
2. **Soft delete**: mọi query WHERE `deleted_at IS NULL`
3. **Paid content**: `visibility='PAID'` → `price > 0`
4. **Reaction toggle**: `INSERT ... ON CONFLICT (user_id, blog_id) DO UPDATE SET type = ...`
5. **Wallet immutable**: `wallet_transactions` chỉ INSERT, không UPDATE/DELETE
6. **Friend**: mutual follow = friends MV, refresh định kỳ
7. **Search vector**: auto-generated, không cần trigger
8. **Anonymous comment**: `status = 'PENDING'` khi user_id IS NULL
9. **Guest tracking**: session cookie (`session_id`), không cần DB row cho guest
10. **IP ownership**: chỉ sessions có IP, các table khác JOIN sessions

---

## 8. Retention Policy

| Table | Retention | Cleanup |
|-------|-----------|---------|
| `activity_log` (API category) | 7 ngày | `DELETE WHERE category='API' AND created_at < NOW() - 7d` |
| `activity_log` (PAGE_VIEW) | 30 ngày | `DELETE WHERE category='PAGE_VIEW' AND created_at < NOW() - 30d` |
| `activity_log` (ACTION/SYSTEM) | 90 ngày | `DELETE WHERE category IN ('ACTION','SYSTEM') AND created_at < NOW() - 90d` |
| `sessions` | 90 ngày | `DELETE WHERE created_at < NOW() - 90d` |
| `notifications` | 90 ngày | Archive hoặc delete |
| `refresh_tokens` | expired + 7 days | `DELETE WHERE revoked=TRUE OR expires_at < NOW() - 7d` |
| Others | Vĩnh viễn | Soft delete |

---

## 9. Migration Path

### Dev (clean slate)

```sql
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
-- Chạy V1__init.sql mới
```

### Production (có dữ liệu)

1. Backup MySQL data
2. Export sang CSV
3. Transform column names (snake_case, số nhiều)
4. Import vào PostgreSQL
5. Chạy script migrate counters + relationships
