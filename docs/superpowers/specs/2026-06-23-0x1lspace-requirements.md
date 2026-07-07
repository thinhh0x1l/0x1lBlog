# 0x1lSpace — Requirements (Step 1)

> **Ngày:** 2026-06-23
> **Trạng thái:** Draft
> **Base trên:** Product Vision v2 (2026-06-23-0x1lspace-product-vision.md)

---

## Phase 0 — Foundation

Các requirement này phải hoàn thành trước khi phát triển tính năng mới.

---

### R001: Blog Visibility Model

**Mô tả:** Mở rộng `blog_visibility` enum từ `(PUBLIC, MEMBERS_ONLY, PAID)` thành `(PUBLIC, FOLLOWERS, FRIENDS, PRIVATE, MEMBERS_ONLY, PAID)`.

**Actors:** Admin (config), System (check), User (chọn khi viết)

**Pre-conditions:**
- DB migration đã chạy
- Code enum đã update

**Flow:**
1. User chọn visibility khi tạo/edit blog
2. System check quyền đọc ở mọi API trả blog:
   - PUBLIC → ai cũng xem
   - FOLLOWERS → chỉ follower của author
   - FRIENDS → chỉ mutual follow
   - PRIVATE → chỉ author + admin
   - MEMBERS_ONLY → member/subscriber
   - PAID → đã mua + member

**Business rules:**
- Người chưa đăng nhập chỉ thấy PUBLIC
- FOLLOWERS không xuất hiện trong public feed/search, chỉ trong following feed
- FRIENDS không xuất hiện trong feed/search nào — chỉ vào bằng direct link
- PRIVATE không xuất hiện ở bất kỳ đâu — chỉ author và admin
- MEMBERS_ONLY: Phase 1 chỉ giữ enum, chưa enforce (chưa có membership system)
- PAID: Phase 1 chỉ giữ enum, chưa enforce (chưa có payment)

**Error cases:**
- User không phải follower → xem FOLLOWERS blog → 403 FORBIDDEN
- Người chưa đăng nhập → xem FOLLOWERS/FRIENDS/PRIVATE → 401 UNAUTHORIZED

**DB changes:**
```sql
ALTER TYPE blog_visibility ADD VALUE 'FOLLOWERS' BEFORE 'MEMBERS_ONLY';
ALTER TYPE blog_visibility ADD VALUE 'FRIENDS' BEFORE 'MEMBERS_ONLY';
ALTER TYPE blog_visibility ADD VALUE 'PRIVATE' BEFORE 'MEMBERS_ONLY';
```

---

### R002: Cache Consolidation

**Mô tả:** Gộp 4 cache service (CacheService legacy, EntityCacheService, BlogCacheService, FeedCacheService) thành 2 service với trách nhiệm rõ ràng.

**Actors:** Developer

**Pre-conditions:**
- Code review toàn bộ cache usage

**Design:**

```
CacheService (generic — entity CRUD đơn giản)
├── get(key, type)       → L1 Caffeine → L2 Redis
├── put(key, value, ttl) → L1 + L2
├── evict(key)           → L1 + L2
└── dùng cho: User, Category, Hashtag, SiteSetting, AboutInfo

BlogCacheService (domain-specific — blog + feed)
├── getBlog(id)
├── getBlogBySlug(slug)
├── getFeed(userId, cursor)
├── getTrending(period)
├── getRelated(blogId)
├── putBlog(blog)
├── evictBlog(blogId)
├── evictFeed(userId)
└── evictTrending()
```

**Changes:**
1. Xoá `CacheService.java` (legacy)
2. Gộp `FeedCacheService` vào `BlogCacheService`
3. `EntityCacheService` giữ nguyên
4. `BlogOrchestrator` không gọi cache — chuyển cache call xuống `BlogService`

**Business rules:**
- Cache không sở hữu Repository (pure cache)
- BlogService sở hữu cả Repository + CacheService

---

### R003: Transaction Boundary

**Mô tả:** `@Transactional` chỉ ở Service layer, Orchestrator không quản lý transaction.

**Business rules:**
- `@Transactional` trên mọi Service write method (create/update/delete/publish/archive)
- Orchestrator không có `@Transactional`
- Nếu Orchestrator cần multi-service flow → dùng `@Transactional(propagation = REQUIRES_NEW)` explicit trong Service method riêng
- Transaction scope càng ngắn càng tốt — không network I/O (email, cache, event) trong transaction

---

### R004: Event Retry & Rollback

**Mô tả:** Event publish fail → có retry + dead-letter queue.

**Design:**

```
Service.create()
  → Transaction BEGIN
  → persist entity
  → eventPublisher.publishEvent(...)  — synchronous, trong transaction
  → Transaction COMMIT
  → Event listener chạy sau commit (TransactionPhase.AFTER_COMMIT)
  → Nếu listener fail → retry 3 lần (Spring Retry)
  → Nếu vẫn fail → ghi vào dead_letter_events table
  → Admin dashboard có tab "Failed Events" để review + replay
```

**DB changes:**
```sql
CREATE TABLE dead_letter_events (
    id              BIGSERIAL PRIMARY KEY,
    event_type      VARCHAR(100) NOT NULL,
    payload         JSONB NOT NULL,
    error_message   TEXT,
    retry_count     INT NOT NULL DEFAULT 0,
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    last_retry_at   TIMESTAMPTZ
);
```

---

### R005: Doc Sync

**Mô tả:** Đồng bộ toàn bộ 5 docs cũ + 2 docs mới để không mâu thuẫn.

**Scope:**
1. `2026-06-18-0x1lblog-platform-design.md` → update identity thành "Social Creator Space", thêm visibility mới, thêm reward system, item system, mischief
2. `2026-06-18-db-redesign.md` → update visibility enum, thêm counters cho item/inventory/mischief
3. `2026-06-21-ai-readable-architecture-layer-guide.md` → cache consolidation, transaction rule
4. `2026-06-21-backend-analytics-counter-strategy.md` → thêm mischief tracking, item purchase tracking
5. `2026-06-21-backend-business-rules.md` → thêm mischief rules, duel/battle rules, item rules

---

## Phase 1 — Core Features

---

### R101: Statuses (Thread + Poll)

**Mô tả:** User viết status ngắn ≤500 chữ, có 2 dạng: Thread (nối tiếp) hoặc Poll (gắn vào thread).

**Khai báo dạng Status:**
- **Status đơn**: 1 bài viết ngắn, hiển thị trên profile timeline + feed follower
- **Thread**: nhiều status nối tiếp (tối đa 10 part), part sau append vào thread gốc
- **Poll**: tùy chọn gắn vào thread — câu hỏi + 2-6 options, vote real-time

**Actors:** Verified User

**Pre-conditions:**
- User đã login + verify email
- User không bị banned/suspended

**Flow (Status đơn):**
1. User click "Status" trên compose bar
2. Nhập text (≤500 chữ)
3. Attach image (tối đa 1 ảnh, ≤5MB, JPEG/PNG/WebP)
4. Chọn visibility: PUBLIC / FOLLOWERS / FRIENDS / PRIVATE
5. Submit → status xuất hiện trên profile timeline + feed follower

**Flow (Thread — dạng nối tiếp):**
1. User viết status đầu tiên của thread
2. Chọn "Đây là thread" → tạo thread ID
3. Part tiếp theo: click thread gốc → "Thêm part"
4. Tối đa 10 part, mỗi part ≤500 chữ
5. Follower nhận notif mỗi part mới (nếu follow thread)
6. Có thể gắn poll vào thread root hoặc bất kỳ part nào

**Flow (Poll — gắn vào thread):**
1. User tạo thread → bật "Gắn poll"
2. Nhập câu hỏi (≤200 chữ) + 2-6 options
3. Chọn thời gian poll (1-7 ngày)
4. Sau khi vote → hiển thị kết quả real-time
5. Mỗi user vote 1 lần/poll

**Business rules:**
- 1 user tối đa 20 Status/ngày (rate limit chung cho cả đơn + thread part)
- Thread tối đa 10 part, mỗi part ≤500 chữ
- Poll tối đa 7 ngày, 2-6 options
- Status đơn không có bookmark, share count riêng
- Không xuất hiện trong search result
- Visibility FOLLOWERS: chỉ follower thấy
- Visibility FRIENDS: chỉ mutual follow thấy
- Không edit status đã publish (phải delete + viết lại)
- Thread part có thể edit trong 5 phút đầu
- Soft delete (deleted_at) cho cả status + thread + poll

**Error cases:**
- Text >500 chữ → 400 BAD_REQUEST
- Thread >10 part → 400 BAD_REQUEST
- Poll sai option count → 400 BAD_REQUEST
- Rate limit exceeded → 429 TOO_MANY_REQUESTS
- User banned → 403 FORBIDDEN
- Người chưa đăng nhập → 401 UNAUTHORIZED

**Reward mapping:**
- Status đơn: EXP +15, Coins +10, Rep (Creativity) +3
- Thread part: EXP +10, Coins +8, Rep (Creativity) +2
- Poll tạo: EXP +10, Coins +5, Rep (Creativity) +5
- Quest progress: +1 nếu daily quest yêu cầu viết status

**DB entities cần:**
```sql
CREATE TABLE statuses (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    thread_id       BIGINT REFERENCES statuses(id) ON DELETE CASCADE,  -- NULL = status đơn
    part_order      SMALLINT DEFAULT 0,                                -- thứ tự trong thread
    content         VARCHAR(500) NOT NULL,
    image_url       VARCHAR(500),
    visibility      status_visibility NOT NULL DEFAULT 'PUBLIC',
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE TABLE status_polls (
    id              BIGSERIAL PRIMARY KEY,
    status_id       BIGINT NOT NULL REFERENCES statuses(id) ON DELETE CASCADE,
    question        VARCHAR(200) NOT NULL,
    options         JSONB NOT NULL,                -- ["option1", "option2", ...]
    ends_at         TIMESTAMPTZ NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE status_poll_votes (
    id              BIGSERIAL PRIMARY KEY,
    poll_id         BIGINT NOT NULL REFERENCES status_polls(id) ON DELETE CASCADE,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    option_index    SMALLINT NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(poll_id, user_id)
);

CREATE INDEX idx_statuses_user_id ON statuses(user_id);
CREATE INDEX idx_statuses_thread_id ON statuses(thread_id);
CREATE INDEX idx_statuses_created_at ON statuses(created_at DESC);
CREATE INDEX idx_status_polls_status_id ON status_polls(status_id);
```

---

### R102: Profile Widget System

**Mô tả:** Profile user gồm các widget có thể show/hide, arrange.

**Actors:** All users (xem), Verified User (cấu hình)

**Pre-conditions:**
- User tồn tại

**Flow (cấu hình):**
1. User vào "Edit Profile"
2. Chọn tab "Layout"
3. Bật/tắt các widget:
   - Avatar + Border (luôn bật)
   - Bio
   - Blog List
   - Badge Wall
   - Stats (view, follower, reaction)
   - Rolltext Banner (nếu có item)
   - Music Box (nếu có item)
   - Statuses (nếu có)
4. Drag-drop để sắp xếp (Phase 2)
5. Save

**Flow (xem):**
1. User A vào profile User B
2. Load danh sách widget đã bật
3. Render theo thứ tự đã lưu
4. Nếu B có item equipped → hiển thị (theme background, border, rolltext, music box)
5. Nếu B bật game mode → render profile dạng RPG character sheet

**Business rules:**
- Avatar + Border luôn hiển thị, không thể tắt
- Widget chỉ hiển thị nếu có dữ liệu (Blog List = 0 → ẩn tự động)
- Music box tự động play khi vào profile (có thể tắt bằng nút mute)
- Rolltext chạy ngang dưới avatar — tốc độ cố định
- Stats chỉ hiển thị số public (followers, reactions nhận được, views)
- Game mode: profile chuyển sang dạng character sheet (level, HP bar = rep, equipment slots)

**DB changes:**
```sql
CREATE TABLE profile_widgets (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    widget_type     VARCHAR(50) NOT NULL,
    is_visible      BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order      INT NOT NULL DEFAULT 0,
    config          JSONB DEFAULT '{}',
    UNIQUE(user_id, widget_type)
);

ALTER TABLE users ADD COLUMN profile_layout JSONB DEFAULT '[]';
ALTER TABLE users ADD COLUMN game_mode BOOLEAN NOT NULL DEFAULT FALSE;
```

---

### R103: Skill Tree System

**Mô tả:** Mỗi category là một nhánh kỹ năng, viết blog → tích điểm → unlock perk.

**Actors:** Verified User

**Pre-conditions:**
- User đã login

**Flow:**
1. User vào "Skill Tree" tab trên profile
2. Xem các nhánh: Tech, Đời sống, Review, Sáng tác...
3. Mỗi nhánh có các node:
   - Node 1: "Upload ảnh 10MB" (tốn 100 điểm nhánh)
   - Node 2: "Scheduled Post" (tốn 300 điểm)
   - Node 3: "Analytics Pro" (tốn 500 điểm)
   - Node 4: "Custom Domain" (tốn 1000 điểm)
4. Khi viết blog trong category → nhận điểm cho nhánh đó
5. Khi đủ điểm → click unlock → perk active

**Business rules:**
- Mỗi blog: +10 điểm cho nhánh tương ứng
- Mỗi Status (đơn/thread part/poll): +3 điểm
- Có thể reset skill tree mỗi 30 ngày (miễn phí) hoặc bất kỳ lúc nào (tốn 200 Gems)
- Perk có hiệu lực ngay sau khi unlock
- Perk vĩnh viễn (không mất khi reset)

**DB changes:**
```sql
CREATE TABLE skill_trees (
    id              BIGSERIAL PRIMARY KEY,
    category_id     BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    perk_type       VARCHAR(50) NOT NULL,
    perk_value      JSONB NOT NULL DEFAULT '{}',
    points_required INT NOT NULL,
    sort_order      INT NOT NULL DEFAULT 0
);

CREATE TABLE user_skill_progress (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    category_id     BIGINT NOT NULL REFERENCES categories(id) ON DELETE CASCADE,
    total_points    INT NOT NULL DEFAULT 0,
    UNIQUE(user_id, category_id)
);

CREATE TABLE user_skill_unlocks (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    skill_id        BIGINT NOT NULL REFERENCES skill_trees(id) ON DELETE CASCADE,
    unlocked_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, skill_id)
);
```

---

### R104: Quest System

**Mô tả:** Hệ thống nhiệm vụ hàng ngày/tuần/mùa có reward.

**Actors:** Verified User, Admin (tạo/config)

**Pre-conditions:**
- User đã login
- Quest đã active (có trong database)

**Flow:**
1. User mở "Quest" tab
2. Xem danh sách quest đang active:
   - Daily (reset mỗi ngày): "Viết 1 Status", "Đọc 3 blogs", "Reaction 5 bài"
   - Weekly (reset mỗi CN): "Viết 3 blogs tuần này", "Nhận 30 reactions"
   - Season (theo mùa): "Top 10 Tech"
   - Milestone (1 lần): "Viết blog thứ 100"
   - Cross: "Đọc 5 blogs + comment 3 bài"
3. Click quest → xem progress
4. Khi đủ điều kiện → click "Claim Reward"
5. Reward được add vào inventory/wallet

**Business rules:**
- Daily quest: tối đa 3 quest active cùng lúc
- Weekly quest: tối đa 5 quest
- Season quest: chỉ 1 (top rank)
- Milestone: tự động hoàn thành, không cần claim (auto reward)
- Không thể claim lại quest đã hoàn thành
- Quest không hoàn thành trước khi reset → mất (không rollover)

**DB changes:**
```sql
CREATE TABLE quests (
    id              BIGSERIAL PRIMARY KEY,
    type            VARCHAR(20) NOT NULL,        -- DAILY / WEEKLY / SEASON / MILESTONE / CROSS
    title           VARCHAR(200) NOT NULL,
    description     TEXT,
    conditions      JSONB NOT NULL,              -- {"action": "WRITE_BLOG", "count": 3}
    rewards         JSONB NOT NULL,              -- {"exp": 50, "coins": 30, "gems": 50, "item_id": null}
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE user_quests (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    quest_id        BIGINT NOT NULL REFERENCES quests(id) ON DELETE CASCADE,
    progress        INT NOT NULL DEFAULT 0,
    target          INT NOT NULL,
    status          VARCHAR(20) NOT NULL DEFAULT 'IN_PROGRESS',  -- IN_PROGRESS / COMPLETED / CLAIMED / EXPIRED
    claimed_at      TIMESTAMPTZ,
    expires_at      TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, quest_id, expires_at)
);
```

---

### R105: Mischief System (P1 cơ bản)

**Mô tả:** User mua item "xấu" để phá profile bạn bè.

**Actors:** Verified User (phá + dọn), System (auto-expire)

**Pre-conditions:**
- User có đủ Coins trong wallet
- Target user không block user phá
- Target chưa bị phá quá 3 lần trong ngày

**Flow (phá):**
1. User A vào profile User B
2. Click nút "Mischief" (cạnh nút Follow)
3. Chọn effect từ danh sách đã mua hoặc mua ngay từ shop
4. Xác nhận:
   - Effect common: confirm 1 click
   - Effect uncommon+: confirm + "Bạn có chắc?"
5. Effect xuất hiện trên profile B ngay lập tức
6. User B nhận notification: "A vừa thả gián vào profile bạn!"

**Flow (dọn):**
1. User B thấy effect trên profile mình
2. Click vào effect → "Dọn dẹp" button
3. Xác nhận tốn Coins
4. Effect biến mất
5. Hoặc đợi auto-expire (effect tự hết)

**Flow (trả đũa):**
1. User B bị phá
2. Trong 24h, B có 1 lần trả đũa free (không tốn Coins)
3. Chọn effect → A bị phá lại
4. A nhận notification

**Business rules:**
- 1 user bị phá tối đa 3 lần/ngày (không kể trả đũa)
- Effect auto-expire sau thời gian quy định
- Block: B block A → A không thể phá B nữa
- Immunity Shield (premium): effect chỉ kéo dài 1/2 thời gian
- Revenge free: 1 lần trong 24h sau khi bị phá

**DB changes:**
```sql
CREATE TABLE profile_effects (
    id              BIGSERIAL PRIMARY KEY,
    target_user_id  BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    actor_user_id   BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    effect_type     VARCHAR(50) NOT NULL,
    expires_at      TIMESTAMPTZ NOT NULL,
    is_revenge      BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_profile_effects_target ON profile_effects(target_user_id, expires_at);
```

---

### R106: Random Discovery ("Đi lạc")

**Mô tả:** 1 nút trên menu → đưa user đến profile/blog ngẫu nhiên, có reward nhẹ.

**Actors:** Verified User

**Flow:**
1. User click "Đi lạc" trên sidebar/header
2. System chọn ngẫu nhiên:
   - 50%: blog PUBLIC ngẫu nhiên
   - 30%: profile ngẫu nhiên
   - 20%: blog cũ (≥30 ngày) đang được content recycling
3. Redirect đến trang đó
4. Popup nhẹ: "Bạn vừa khám phá [title/blog] — +3 Coins +1 Stamp"

**Business rules:**
- Stamp: "Nhà thám hiểm" — đạt được sau 10/50/100/500 lần "Đi lạc"
- Có thể filter: "cùng sở thích", "người lạ", "bài hot cũ"
- Rate limit: 30 lần/ngày

**Reward mapping:**
- EXP: +5
- Coins: +3
- Stamp progress: +1

**DB changes:**
- Không cần table mới — chỉ cần ghi activity_log

---

---

### R107: Reputation Score

**Mô tả:** Hệ thống reputation đa chiều thay vì chỉ level.

**Actors:** System (tự động tính), User (xem)

**Flow:**
1. Mỗi hành động user → cộng reputation vào chiều tương ứng
2. Reputation được tính real-time (không batch)
3. User vào profile → thấy 4 chỉ số + tổng
4. Mỗi chiều có title riêng:

| Chiều | Tiêu chí | Title |
|-------|----------|-------|
| Writing Power | Blog chất lượng (reactions, comments, độ dài) | "Cây bút" / "Tác giả" / "Bậc thầy" |
| Community | Comment hữu ích, mentor | "Người bạn" / "Cố vấn" / "Huyền thoại" |
| Creativity | Status (thread + poll) | "Người mơ" / "Nhà sáng tạo" / "Thiên tài" |
| Influence | Followers, shares, mentions | "Ảnh hưởng" / "Ngôi sao" / "Huyền thoại sống" |

**Business rules:**
- Reputation không thể âm (min = 0)
- Vi phạm (blog bị report, comment toxic) → trừ Community + Writing
- Reputation giảm dần nếu không active (Gradual Decay — 5% mỗi tháng)
- Title tự động thay đổi khi đạt ngưỡng

**DB changes:**
```sql
ALTER TABLE users ADD COLUMN reputation_writing INT NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN reputation_community INT NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN reputation_creativity INT NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN reputation_influence INT NOT NULL DEFAULT 0;
```

---

### R108: Streak & Momentum

**Mô tả:** Hệ thống streak login + publishing + momentum bar.

**Actors:** Verified User, System

**Flow (login streak):**
1. User login → system check lần login cuối
2. Nếu hôm qua có login → streak +1
3. Nếu hôm nay đã login → bỏ qua (không double count)
4. Streak hiển thị trên profile + daily quest tab

**Flow (publishing streak):**
1. User publish blog/thought → system check lần publish cuối
2. Nếu hôm qua có publish → streak +1
3. Streak reset nếu bỏ 1 ngày

**Flow (momentum bar):**
1. Thanh momentum hiển thị trên profile (0-100%)
2. Mỗi blog publish trong tuần → +33%
3. 3 blogs/tuần → bar đầy 100% → boost content trong feed (tuần sau)
4. Đầu tuần mới → reset về 0

**Reward mapping:**
| Streak | Reward |
|:------:|--------|
| 7 days | +100 Coins + 20 Gems |
| 30 days | +500 Coins + 100 Gems + Uncommon item |
| 100 days | +2000 Coins + 500 Gems + Rare item |
| 365 days | +Legendary badge "Bất tử" |

**Business rules:**
- Streak freeze: mua bằng Gems (50 Gems/lần), giữ streak 1 ngày nghỉ
- Tối đa 7 freeze/tháng
- Momentum reset mỗi thứ 2 hàng tuần

**DB changes:**
```sql
ALTER TABLE users ADD COLUMN login_streak INT NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN publish_streak INT NOT NULL DEFAULT 0;
ALTER TABLE users ADD COLUMN last_login_date DATE;
ALTER TABLE users ADD COLUMN last_publish_date DATE;
ALTER TABLE users ADD COLUMN streak_freezes INT NOT NULL DEFAULT 7;
ALTER TABLE users ADD COLUMN momentum INT NOT NULL DEFAULT 0;
```

---

### R109: Virtual Shop

**Mô tả:** Item catalog + purchase + inventory.

**Actors:** Verified User (mua), Admin (thêm item), System (xử lý giao dịch)

**Pre-conditions:**
- User có đủ currency
- Item còn hàng (nếu limited)

**Flow:**
1. User vào "Shop" tab
2. Browse items theo category (Visual / Effect / Perk / Social / Mischief / Collectible)
3. Filter theo rarity (Common / Uncommon / Rare / Epic / Legendary / Mythic)
4. Click item → xem detail (giá, mô tả, preview, số lượng còn nếu limited)
5. Click "Mua"
6. Chọn currency (nếu item hỗ trợ nhiều loại)
7. Confirm
8. Trừ currency + thêm item vào inventory

**Business rules:**
- Item Common: mua bằng Coins
- Item Uncommon: mua bằng Gems
- Item Rare: mua bằng Gems hoặc Gacha
- Item Epic+: chỉ Gacha / Craft / Sự kiện
- Limited item: mua bằng Gems hoặc tiền thật
- Mischief item: mua bằng Coins
- Refund không được (trừ bug)
- Item timed: hiển thị ngày hết hạn trong inventory

**DB changes:**
```sql
CREATE TABLE item_catalog (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(100) NOT NULL,
    description     TEXT,
    category        VARCHAR(20) NOT NULL,      -- VISUAL / EFFECT / PERK / SOCIAL / MISCHIEF / COLLECTIBLE
    rarity          VARCHAR(20) NOT NULL,      -- COMMON / UNCOMMON / RARE / EPIC / LEGENDARY / MYTHIC
    duration_type   VARCHAR(20) NOT NULL DEFAULT 'PERMANENT',  -- PERMANENT / TIMED / CONSUMABLE / SEASONAL
    duration_days   INT,                       -- NULL nếu permanent
    price_coins     INT DEFAULT 0,
    price_gems      INT DEFAULT 0,
    price_usd       INT DEFAULT 0,             -- cents, NULL nếu không bán bằng tiền thật
    max_supply      INT,                       -- NULL nếu unlimited
    current_supply  INT,
    effect_config   JSONB NOT NULL DEFAULT '{}',
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE user_inventory (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    item_id         BIGINT NOT NULL REFERENCES item_catalog(id) ON DELETE CASCADE,
    serial_number   INT,
    source          VARCHAR(20) NOT NULL,      -- SHOP / QUEST / RANK / GACHA / CRAFT / TRADE / GIFT / EVENT
    acquired_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    expires_at      TIMESTAMPTZ,
    is_equipped     BOOLEAN NOT NULL DEFAULT FALSE,
    trade_count     INT NOT NULL DEFAULT 0
);

CREATE INDEX idx_inventory_user ON user_inventory(user_id);
CREATE INDEX idx_inventory_item ON user_inventory(item_id);
```

---

### R110: Blind Challenge

**Mô tả:** Mỗi ngày có 1 chủ đề ẩn — ai đoán đúng trước khi tiết lộ → bonus.

**Actors:** Verified User

**Flow:**
1. 00:00 hệ thống chọn 1 chủ đề ngẫu nhiên cho hôm nay
2. Chủ đề ẩn, chỉ hiển thị "??? — Hãy đoán chủ đề hôm nay"
3. User có thể đoán 1 lần/ngày — chọn từ danh sách 10 chủ đề gợi ý
4. 20:00 cùng ngày → chủ đề được tiết lộ
5. Nếu user đoán đúng → bonus
6. User viết blog/status đúng chủ đề trong ngày → x2 EXP

**Business rules:**
- Gợi ý: 10 chủ đề, gồm chủ đề thật + 9 chủ đề gần giống
- Bonus khi đoán đúng: +50 Coins + 10 Gems + Stamp
- Không đoán = không phạt

**DB changes:**
```sql
CREATE TABLE blind_challenges (
    id              BIGSERIAL PRIMARY KEY,
    date            DATE NOT NULL UNIQUE,
    topic_id        BIGINT NOT NULL,
    topic_hint      VARCHAR(100),              -- gợi ý mơ hồ
    options         JSONB NOT NULL,            -- 10 options
    revealed        BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE blind_challenge_guesses (
    id              BIGSERIAL PRIMARY KEY,
    challenge_id    BIGINT NOT NULL REFERENCES blind_challenges(id) ON DELETE CASCADE,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    guessed_topic_id BIGINT NOT NULL,
    is_correct      BOOLEAN,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(challenge_id, user_id)
);
```

---

---

## Core Platform — Nền tảng Blog & Hạ tầng

Các module này là cốt lõi để 0x1lSpace vận hành như một nền tảng blog thực thụ. Phải có trước bất kỳ tính năng game/social nào.

---

### R201: Content Editor

**Mô tả:** Trình soạn thảo blog với đầy đủ tính năng.

**Actors:** Verified User

**Flow:**
1. User click "Tạo blog" → mở editor
2. Editor hỗ trợ:
   - Markdown (có preview live)
   - Hoặc rich text toggle (Phase 2)
   - Syntax highlight code block (hỗ trợ 20+ ngôn ngữ)
   - Image upload (drag-drop, paste)
   - Embed: YouTube, Twitter, CodePen, GitHub Gist
   - File attachment (tối đa 20MB)
   - Gallery (multi-image layout)
3. Auto-save draft mỗi 30 giây (localStorage + server)
4. Word count + reading time hiển thị real-time
5. Table of Contents auto-generate từ heading
6. Set cover image, category, hashtags, visibility

**Business rules:**
- Auto-save draft: lưu vào localStorage ngay, sync lên server mỗi 30s
- Draft không bị mất nếu refresh/close tab
- Draft tối đa 50 bản/user (cũ nhất bị xóa nếu quá)

**DB changes:**
```sql
ALTER TABLE blogs ADD COLUMN auto_save_data JSONB;
ALTER TABLE blogs ADD COLUMN editor_version VARCHAR(10) DEFAULT 'markdown';

CREATE TABLE blog_drafts (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title           VARCHAR(200),
    content         TEXT,
    data            JSONB,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

---

### R202: Scheduled Publish

**Mô tả:** Hẹn giờ publish blog.

**Actors:** Verified User

**Flow:**
1. User viết blog xong
2. Chọn "Lên lịch" thay vì "Publish ngay"
3. Chọn thời gian (tối thiểu 15 phút sau, tối đa 1 năm)
4. System lưu blog với status SCHEDULED + scheduled_at
5. Đến thời điểm → system tự động publish (set status = PUBLISHED, set published_at = now)

**Business rules:**
- Scheduled blog có thể edit/hủy trước khi publish
- Hủy scheduled → về DRAFT
- Tối đa 10 scheduled blogs/user

**DB changes:**
```sql
ALTER TYPE blog_status ADD VALUE 'SCHEDULED' BEFORE 'PUBLISHED';
ALTER TABLE blogs ADD COLUMN scheduled_at TIMESTAMPTZ;
CREATE INDEX idx_blogs_scheduled ON blogs(scheduled_at) WHERE status = 'SCHEDULED' AND deleted_at IS NULL;
```

---

### R203: Version History

**Mô tả:** Lưu lịch sử chỉnh sửa blog.

**Actors:** Verified User (own blog)

**Flow:**
1. Mỗi lần user save blog → tạo version snapshot
2. User vào "Version History" → xem danh sách versions (thời gian, tóm tắt thay đổi)
3. Click vào version → preview nội dung cũ
4. Click "Restore" → thay thế nội dung hiện tại bằng version cũ

**Business rules:**
- Chỉ lưu tối đa 20 versions/blog (cũ nhất bị xóa)
- Auto-save draft không tạo version (chỉ manual save mới tạo)
- Version lưu: title + content + description + cover_image

**DB changes:**
```sql
CREATE TABLE blog_versions (
    id              BIGSERIAL PRIMARY KEY,
    blog_id         BIGINT NOT NULL REFERENCES blogs(id) ON DELETE CASCADE,
    title           VARCHAR(200) NOT NULL,
    content         TEXT NOT NULL,
    description     VARCHAR(500),
    cover_image     VARCHAR(500),
    version_number  INT NOT NULL,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(blog_id, version_number)
);
```

---

### R204: SEO

**Mô tả:** Blog có meta tags, Open Graph, sitemap, RSS.

**Actors:** System (auto-generate), User (tùy chỉnh meta)

**Flow:**
1. Mỗi blog publish → system auto-generate:
   - Title tag: {title} | 0x1lSpace
   - Meta description: blog.description (hoặc truncate content)
   - Open Graph: title, description, image (cover), url
   - Twitter Card: summary_large_image
   - Canonical URL
2. User có thể custom meta title + description riêng (advanced)
3. Sitemap.xml tự động cập nhật khi có blog mới
4. RSS feed: `/feed/rss`, `/feed/atom`, theo category

**Business rules:**
- Sitemap refresh mỗi 6h (cron)
- RSS feed cache 15 phút
- Canonical URL luôn trỏ đến slug chính

**DB changes:**
```sql
ALTER TABLE blogs ADD COLUMN meta_title VARCHAR(70);
ALTER TABLE blogs ADD COLUMN meta_description VARCHAR(160);
```

---

### R205: Email System

**Mô tả:** Gửi email cho user: verify, forgot password, notification, digest.

**Actors:** System

**Flow (verify email):**
1. User đăng ký → system gửi email chứa link verify (6h expiry)
2. User click link → email verified
3. Chưa verify → không viết blog, không comment, không reaction

**Flow (forgot password):**
1. User click "Quên mật khẩu"
2. Nhập email
3. System gửi link reset (30 phút expiry)
4. User click → nhập password mới

**Flow (notification email):**
1. User có notification unread > 6h không đọc
2. System gửi email gộp: "Bạn có 3 thông báo mới"
3. Tối đa 1 email/6h (tránh spam)

**Flow (weekly digest):**
1. Chủ nhật hàng tuần
2. Gửi email: top blogs trong tuần, unread notifications summary, streaks
3. User có thể unsubscribe digest

**Business rules:**
- Dùng SendGrid (Phase 1) hoặc Resend (Phase 2)
- Email template: transactional (verify, reset) khác với marketing (digest)
- Track bounce + complaint — nếu bounce >5% → flag
- Rate limit: max 5 emails/user/ngày (không tính transactional)

---

### R206: Moderation

**Mô tả:** Hệ thống kiểm duyệt nội dung + user.

**Actors:** Admin, System (auto-flag)

**Flow (report content):**
1. User click "Report" trên blog/comment/user
2. Chọn lý do: Spam / Toxic / Copyright / Khác
3. Optional: mô tả thêm
4. Report vào moderation queue

**Flow (moderation queue):**
1. Admin mở "Moderation" dashboard
2. Xem danh sách pending reports
3. Click vào từng report → xem content bị report
4. Action: Approve (bỏ qua) / Warning (gửi cảnh cáo) / Delete content / Ban user (tạm thời/vĩnh viễn)
5. Log audit

**Flow (auto-flag):**
1. System check content mới (blog, comment) chứa từ khóa nhạy cảm
2. Nếu match → tự động set status = PENDING, không public
3. Admin review trong moderation queue

**Business rules:**
- Spam detection: rate limit + từ khóa + IP reputation (Phase 1 basic)
- Toxic detection: từ khóa + regex (Phase 1 basic)
- User bị ban: không login được, content ẩn
- Report anonymous (người report không lộ danh tính)

**DB changes:**
```sql
CREATE TABLE moderation_reports (
    id              BIGSERIAL PRIMARY KEY,
    reporter_id     BIGINT REFERENCES users(id) ON DELETE SET NULL,
    target_type     VARCHAR(20) NOT NULL,     -- BLOG / COMMENT / USER
    target_id       BIGINT NOT NULL,
    reason          VARCHAR(50) NOT NULL,
    description     TEXT,
    status          VARCHAR(20) NOT NULL DEFAULT 'PENDING',  -- PENDING / APPROVED / REJECTED / ACTIONED
    handled_by      BIGINT REFERENCES users(id) ON DELETE SET NULL,
    handled_at      TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE moderation_actions (
    id              BIGSERIAL PRIMARY KEY,
    admin_id        BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    target_type     VARCHAR(20) NOT NULL,
    target_id       BIGINT NOT NULL,
    action          VARCHAR(50) NOT NULL,     -- WARNING / DELETE / BAN_TEMP / BAN_PERM
    reason          TEXT,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE banned_keywords (
    id              BIGSERIAL PRIMARY KEY,
    keyword         VARCHAR(200) NOT NULL UNIQUE,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
```

---

### R207: Security & Rate Limiting

**Mô tả:** Bảo mật nền tảng: rate limit, 2FA, session management, security headers.

**Actors:** System

**Flow (rate limiting):**
1. Mỗi request → check bucket (Bucket4j + Redis)
2. Nếu vượt quota → 429 Too Many Requests + Retry-After header
3. Quota theo:

| Endpoint | Chưa đăng nhập | User | Admin |
|----------|:-----:|:----:|:-----:|
| Login | 5/phút/IP | — | — |
| Register | 3/phút/IP | — | — |
| API read | 30/phút | 100/phút | 300/phút |
| API write | — | 30/phút | 100/phút |
| Comment | — | 10/phút | — |
| Search | 10/phút | 30/phút | — |

**Flow (session management):**
1. User login → tạo session (JWT access 15 phút + refresh token 7 ngày)
2. Refresh token lưu trong DB (hash) + HttpOnly cookie
3. User có thể xem danh sách session active (device, IP, last active)
4. User có thể revoke session từ xa
5. Password change → revoke tất cả sessions trừ session hiện tại

**Business rules:**
- Security headers: CSP (strict), HSTS (1 năm), X-Frame-Options (DENY), X-Content-Type-Options (nosniff)
- Password: BCrypt, min 8 ký tự, có uppercase + number
- JWT: HS256, không chứa sensitive data
- 2FA: Phase 2 (TOTP, email code)
- CORS: chỉ cho phép blog.0x1l.com, cms.0x1l.com

---

### R208: User Management & GDPR

**Mô tả:** Quản lý tài khoản, quyền riêng tư, tuân thủ GDPR.

**Actors:** User, Admin, System

**Flow (account deletion):**
1. User vào Settings → "Xóa tài khoản"
2. Confirm: nhập password + lý do
3. System soft-delete user + anonymize content (comment → "guest", blog → author removed)
4. 30-day grace period (có thể hủy bằng cách login lại)
5. Sau 30 ngày → hard delete dữ liệu cá nhân (email, password_hash, IP)

**Flow (data export):**
1. User yêu cầu export dữ liệu
2. System tạo archive JSON trong 24h: blogs, comments, profile, settings
3. Gửi link download (7 ngày expiry)
4. User download

**Flow (privacy settings):**
1. User vào Settings → Privacy
2. Toggle: "Ai có thể xem email của tôi" (nobody / followers / everyone)
3. Toggle: "Hiển thị last_active" (on/off)
4. Toggle: "Cho phép search engine index profile" (on/off)
5. Block list: quản lý user đã block

**Business rules:**
- Email mặc định là private (chỉ admin xem được)
- Last_active mặc định là public
- Block: user bị block không thể: follow, comment, reaction, send friend request
- Block không thông báo cho người bị block

**DB changes:**
```sql
ALTER TABLE users ADD COLUMN privacy_email VARCHAR(20) NOT NULL DEFAULT 'NOBODY';
ALTER TABLE users ADD COLUMN privacy_last_active BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE users ADD COLUMN privacy_search_index BOOLEAN NOT NULL DEFAULT TRUE;
ALTER TABLE users ADD COLUMN scheduled_deletion_at TIMESTAMPTZ;

CREATE TABLE user_blocks (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    blocked_user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(user_id, blocked_user_id)
);
```

---

### R209: Onboarding & UX

**Mô tả:** Trải nghiệm người dùng mới + error pages + empty states.

**Actors:** User, System

**Flow (onboarding):**
1. User đăng ký xong → redirect đến onboarding wizard
2. Step 1: Chọn interests (categories) → xây dựng feed
3. Step 2: Upload avatar + viết bio ngắn
4. Step 3: "Hãy viết blog đầu tiên của bạn!" — optional
5. Step 4: Daily quest intro "Mỗi ngày bạn có thể..."
6. Done → redirect đến feed

**Empty states:**
| Trang | Empty state |
|-------|-------------|
| Feed | "Chưa có blog nào. Hãy follow ai đó hoặc viết blog đầu tiên!" |
| Blog list | "Bạn chưa viết blog nào. Bắt đầu ngay?" |
| Notification | "Chưa có thông báo nào." |
| Bookmark | "Bạn chưa bookmark blog nào." |
| Following | "Bạn chưa follow ai. Khám phá người dùng!" |
| Search | "Không tìm thấy kết quả. Thử từ khóa khác?" |

**Error pages:**
| HTTP | Nội dung |
|:----:|----------|
| 404 | "Trang không tồn tại. Có thể blog đã bị xóa hoặc đường link sai." + Search bar |
| 403 | "Bạn không có quyền truy cập trang này." + Login button |
| 500 | "Có lỗi xảy ra. Chúng tôi đã được thông báo." + Retry button |
| 429 | "Bạn đang gửi yêu cầu quá nhanh. Vui lòng đợi 1 phút." |

---

### R210: Admin Dashboard

**Mô tả:** Dashboard quản trị toàn diện.

**Actors:** Admin

**Sections:**

**1. Overview:**
- Active users (24h) + new users (7 days)
- Total blogs + new blogs (7 days)
- Total views + comments + reactions
- Bảng mini real-time

**2. User Management:**
- List users (search, filter by role/status/date)
- Actions: set role (USER/ADMIN), ban/unban, verify email
- Xem chi tiết user (blogs, comments, login history)

**3. Content Moderation:**
- Moderation queue (pending reports)
- Blog management (list, search, delete, restore)
- Comment management

**4. Category & Badge Management:**
- CRUD categories
- CRUD badges
- Award badge manually

**5. Site Settings:**
- Site name, description, logo, favicon
- Maintenance mode (on/off + message)
- Registration: open/invite-only/closed

**6. Audit Log:**
- Admin actions log (ai làm gì, khi nào)
- Search + filter

**7. System Health:**
- Cache stats (hit rate, size)
- Queue depth (dead_letter_events count)
- Scheduled tasks status

---

### R211: Monitoring & Error Tracking

**Mô tả:** Hệ thống giám sát, log, error tracking.

**Actors:** Admin, System

**Components:**

```
1. Health Check
   GET /api/health
   → { status: "UP", db: "UP", redis: "UP", minio: "UP", uptime: "72h" }

2. Structured Logging (Logback)
   - JSON format
   - Fields: timestamp, level, logger, message, trace_id, user_id, request_id
   - Log file rotation (7 ngày)
   - Error → gửi Telegram webhook

3. Error Tracking (Sentry)
   - Capture unhandled exceptions
   - Capture 500 errors
   - User feedback: "Xảy ra lỗi, gửi báo cáo?"

4. Metrics (Micrometer + Prometheus)
   - JVM metrics (memory, threads, GC)
   - Request metrics (count, p50/p95/p99, error rate)
   - DB connection pool
   - Cache hit rate
   - Expose /actuator/prometheus

5. Scheduled Tasks Monitor
   - Task: trending refresh, feed build, view flush, email digest
   - Log: start time, duration, success/fail, rows affected
   - Alert nếu task fail 3 lần liên tiếp
```

---

### R212: Backup & Maintenance

**Mô tả:** Backup tự động DB + file + restore procedure.

**Actors:** Admin, System

**Schedule:**

| Task | Frequency | Retention |
|------|-----------|-----------|
| DB dump (full) | Daily 3AM | 30 days |
| DB WAL archiving | Continuous | 7 days |
| Redis snapshot (RDB) | Every 6h | 3 days |
| File backup (MinIO) | Daily 4AM | 7 days (incremental) |
| Dead letter events cleanup | Daily 5AM | 90 days |
| Activity log cleanup | Weekly | Theo retention policy |
| Refresh tokens cleanup | Daily 6AM | Expired + 7 days |

**Restore procedure (documentation):**
```md
1. Stop application
2. Restore PostgreSQL: pg_restore -d blog backup.sql
3. Restore Redis: copy RDB file to redis data dir
4. Restore MinIO: mc mirror backup/ minio/
5. Start application
6. Verify: check /api/health, check recent blogs
7. Run cache warm: ./warmup.sh
```

---

### R213: Compliance

**Mô tả:** Terms of Service, Privacy Policy, cookie consent.

**Actors:** User, Admin

**Pages:**

```
GET /about           → About 0x1lSpace
GET /terms           → Terms of Service
GET /privacy         → Privacy Policy
GET /cookie-policy   → Cookie Policy
```

**Cookie consent:**
1. User lần đầu vào site (kể cả chưa đăng nhập) → banner: "Trang web sử dụng cookie để cải thiện trải nghiệm."
2. Nút: "Chấp nhận" / "Tùy chỉnh" / "Từ chối"
3. Nếu từ chối → không set tracking cookies, chỉ session cookie (bắt buộc)
4. Lưu preference 1 năm

**ToS highlights:**
- User owns content
- Platform có quyền xóa nội dung vi phạm
- Không chịu trách nhiệm về nội dung user tạo
- Cấm: spam, toxic, illegal content

---

### R214: File & Storage

**Mô tả:** Hệ thống upload file, image processing, signed URLs.

**Actors:** User, System

**Flow:**
1. Upload → API multipart → validate (type, size, virus scan)
2. Generate variants:
   - original (giữ nguyên)
   - thumbnail (300x300, webp)
   - medium (800x, webp)
   - large (1600x, webp) — chỉ cho blog cover
3. Upload all variants to storage (MinIO S3)
4. Save metadata to PostgreSQL
5. Return URL

**Storage limits:**

| Loại | Max size | Formats | Variants |
|------|----------|---------|----------|
| Avatar | 2MB | JPEG, PNG, WebP, GIF | original + thumbnail |
| Blog cover | 10MB | JPEG, PNG, WebP | original + thumbnail + medium + large |
| Blog image | 10MB | JPEG, PNG, WebP, GIF | original + thumbnail + medium |
| Attachment | 20MB | PDF, ZIP, txt, code | original only |

**Signed URLs:**
- File từ blog PAID hoặc MEMBERS_ONLY → signed URL 15 phút
- Avatar, cover → public CDN URL

---

### R216: Session Tracking (Anonymous)

**Mô tả:** Theo dõi session cho analytics — không cần login, chỉ tracking technical.

**Actors:** System

**Flow:**
1. User (kể cả chưa đăng nhập) lần đầu vào site → tạo session cookie (ULID)
2. Session lưu: IP, user_agent, device_type, country_code, city
3. Mỗi page view → ghi activity_log với trace_id + session_id
4. Nếu user login → attach user_id vào session hiện tại
5. Session timeout 30 phút không hoạt động

**Business rules:**
- Session cookie không cần consent (essential cookie)
- IP chỉ lưu trong sessions table — các table khác JOIN để lấy
- Retention: 90 ngày

---

### R217: API Documentation

**Mô tả:** API docs cho developer.

**Actors:** Developer (external), Frontend (internal)

**Format:**
- SpringDoc OpenAPI 3.0 (Swagger UI)
- Endpoint: `/swagger-ui.html`
- Grouped by module: Auth, Blog, Comment, User, Admin...
- Authentication: Bearer token
- Rate limit headers: X-RateLimit-Remaining, X-RateLimit-Reset
- Response format chuẩn:

```json
{
  "success": true,
  "data": {},
  "error": null,
  "meta": {
    "page": 1,
    "size": 20,
    "total": 100
  }
}

// Error
{
  "success": false,
  "data": null,
  "error": {
    "code": "BLOG_NOT_FOUND",
    "message": "Blog không tồn tại",
    "details": null
  },
  "meta": null
}
```

---

### R218: RSS Feed

**Mô tả:** RSS/Atom feed cho blog.

**Actors:** External (RSS reader), System

**Endpoints:**
```
GET /feed/rss          → RSS 2.0 — tất cả blog PUBLIC mới nhất
GET /feed/atom         → Atom — tất cả blog PUBLIC mới nhất
GET /feed/rss?category=tech  → RSS theo category
GET /feed/rss?author=username → RSS theo author
```

**Business rules:**
- Cache 15 phút
- Max 50 items/feed
- Content: tiêu đề, description, link, author, published date, category, tags
- Full content hay summary? → Mặc định summary (200 chữ), có thể config lấy full

---

## Phase 2 — Community & Social

### R301: Story (24h tự hủy)

**Mô tả:** Facebook-style story — ảnh/video/text tự hủy sau 24 giờ. Hiển thị dạng horizontal strip trên đầu feed.

**Actors:** Verified User

**Pre-conditions:**
- User đã login + verify email
- User không bị banned/suspended

**Flow (tạo):**
1. User click "Story" trên compose bar hoặc + button ở đầu feed
2. Chọn ảnh/video từ device hoặc nhập text
3. Optional: thêm sticker, filter, text overlay
4. Chọn visibility: PUBLIC / FOLLOWERS / FRIENDS
5. Submit → story active trong 24h
6. Story xuất hiện trên horizontal strip đầu feed của follower

**Flow (xem):**
1. User thấy strip các story từ user đang follow
2. Click vào story → xem fullscreen, tap phải/trái để chuyển
3. Reaction nhanh (❤️/🔥/😂/😯/😢/🙏) — reaction riêng, không vào reaction system chung
4. Reply story → gửi tin nhắn riêng (Direct Message — Phase 3)
5. Story tự động hết hạn sau 24h → ẩn khỏi strip

**Business rules:**
- Tối đa 5 stories/ngày/user
- Story không có comment, không có public reaction
- Story không xuất hiện trong search, trending, feed (chỉ trong strip)
- Story không được bookmark/share
- Hết hạn sau 24h → soft delete hoặc move vào archive
- Archive: user có thể xem lại story của chính mình trong 7 ngày
- Không edit sau khi publish (phải delete + viết lại)

**Error cases:**
- >5 stories/ngày → 429 TOO_MANY_REQUESTS
- File >10MB (ảnh) / >50MB (video) → 413 PAYLOAD_TOO_LARGE
- Format không hỗ trợ → 400 BAD_REQUEST
- Người chưa đăng nhập → 401 UNAUTHORIZED

**Reward mapping:**
- EXP: +10
- Coins: +5
- Streak: +0.5 (mỗi story = nửa ngày streak)
- Quest progress: +1 nếu daily quest yêu cầu

**DB entities cần:**
```sql
CREATE TABLE stories (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    media_url       VARCHAR(500),                 -- NULL nếu chỉ text
    media_type      VARCHAR(10),                  -- 'image', 'video', 'text'
    text_content    VARCHAR(300),
    visibility      story_visibility NOT NULL DEFAULT 'PUBLIC',
    expires_at      TIMESTAMPTZ NOT NULL,         -- created_at + 24h
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE TABLE story_reactions (
    id              BIGSERIAL PRIMARY KEY,
    story_id        BIGINT NOT NULL REFERENCES stories(id) ON DELETE CASCADE,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    reaction_type   VARCHAR(10) NOT NULL,         -- love, fire, lol, wow, sad, pray
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(story_id, user_id)
);

CREATE TABLE story_archives (
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    story_id        BIGINT NOT NULL REFERENCES stories(id) ON DELETE CASCADE,
    archived_at     TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(story_id)
);

CREATE INDEX idx_stories_user_id ON stories(user_id);
CREATE INDEX idx_stories_expires_at ON stories(expires_at);
CREATE INDEX idx_story_reactions_story_id ON story_reactions(story_id);
```

**Visibility type cho Story:**
```sql
CREATE TYPE story_visibility AS ENUM ('PUBLIC', 'FOLLOWERS', 'FRIENDS');
```

---

### R302: Canvas cộng đồng

**Mô tả:** Shared digital canvas nơi mọi user có thể vẽ chung — mỗi nét vẽ là 1 action, vẽ ngu vẫn vui. Canvas có thể là vĩnh viễn (profile canvas) hoặc limited-time event (Tết, Halloween).

**Actors:** Verified User

**Pre-conditions:**
- User đã login

**Flow:**
1. User mở "Canvas" từ Soul Space hoặc event banner
2. Chọn màu (bảng màu cơ bản 16 màu) + brush size (1/3/5px)
3. Click/drag trên canvas → stroke được render real-time (WebSocket)
4. Stroke được broadcast cho tất cả user đang xem canvas đó
5. Canvas có thể replay — xem lại toàn bộ quá trình vẽ từ đầu

**Canvas types:**
| Type | Duration | Size | Ghi chú |
|------|----------|------|---------|
| **Profile Canvas** | Vĩnh viễn | 200×200 | Trang trí Soul Space, 1 user/canvas |
| **Community Canvas** | Vĩnh viễn | 500×500 | Chung toàn platform, global |
| **Event Canvas** | Limited-time | 500×500 | Tết, Halloween, anniversary |

**Business rules:**
- Rate limit: 1 stroke/giây/user (chống spam)
- Canvas lưu dưới dạng stroke log (không snapshot) — có thể replay
- Stroke log: {user_id, x, y, color, size, timestamp}
- Event canvas tự động archive sau khi kết thúc
- Canvas có thể reset bởi admin (nếu bị spam/vandalism)
- Report stroke: click vào stroke → report (nếu vẽ nội dung xấu)

**Reward mapping:**
- Mỗi stroke (verified user): +1 EXP (tối đa 50 EXP/ngày từ canvas)
- Event canvas top contributor: badge + item

**DB entities cần:**
```sql
CREATE TABLE canvases (
    id              BIGSERIAL PRIMARY KEY,
    type            VARCHAR(20) NOT NULL,          -- 'profile', 'community', 'event'
    title           VARCHAR(100),
    width           INT NOT NULL,
    height          INT NOT NULL,
    owner_id        BIGINT REFERENCES users(id) ON DELETE SET NULL,  -- NULL = community canvas
    starts_at       TIMESTAMPTZ,
    ends_at         TIMESTAMPTZ,                   -- NULL = vĩnh viễn
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE canvas_strokes (
    id              BIGSERIAL PRIMARY KEY,
    canvas_id       BIGINT NOT NULL REFERENCES canvases(id) ON DELETE CASCADE,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE
    x               INT NOT NULL,
    y               INT NOT NULL,
    color           VARCHAR(7) NOT NULL,            -- hex color
    brush_size      SMALLINT NOT NULL DEFAULT 3,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_canvas_strokes_canvas ON canvas_strokes(canvas_id, created_at);
```

---

### R303: Community Playlist

**Mô tả:** Collaborative music playlist cho Soul Space — cộng đồng cùng add nhạc, vote bài hát. Mỗi Soul Space/profile có 1 playlist riêng.

**Actors:** Verified User

**Pre-conditions:**
- User đã login
- Playlist đã được tạo (mặc định mỗi user có 1 playlist khi tạo Soul Space)

**Flow:**
1. User vào Soul Space → tab "Playlist"
2. Xem danh sách bài hát hiện tại (tối đa 50 bài)
3. Click "Add nhạc" → search bài hát (YouTube/SoundCloud API)
4. Chọn bài → thêm vào playlist (chờ duyệt nếu không phải chủ playlist)
5. User khác vote up/down bài hát trong playlist
6. Bài có vote cao nhất được ưu tiên phát first
7. Chủ playlist có thể xóa bài bất kỳ, reorder

**Business rules:**
- Mỗi playlist tối đa 50 bài
- Mỗi user tối đa add 5 bài/ngày vào 1 playlist
- Chủ playlist có toàn quyền (xóa, reorder, set default)
- Người chưa đăng nhập chỉ thấy widget playlist trên profile (view-only), không add/vote
- Vote: 1 user = 1 vote/bài (có thể đổi vote)
- Bài bị -5 votes → tự động remove (community moderation)
- Tích hợp YouTube / SoundCloud / tự upload (Phase 2+)
- Music box trong profile tự động phát playlist (shuffle mode)
- Nếu không có bài nào → music box tắt

**Reward mapping:**
- Add nhạc: +5 Coins +3 EXP (tối đa 25 Coins/ngày)
- Bài được +10 votes: +50 Coins +10 EXP badge "Nhạc công"

**DB entities cần:**
```sql
CREATE TABLE playlists (
    id              BIGSERIAL PRIMARY KEY,
    owner_id        BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title           VARCHAR(100) DEFAULT 'Playlist của tôi',
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE TABLE playlist_songs (
    id              BIGSERIAL PRIMARY KEY,
    playlist_id     BIGINT NOT NULL REFERENCES playlists(id) ON DELETE CASCADE,
    added_by        BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title           VARCHAR(200) NOT NULL,
    artist          VARCHAR(200),
    source          VARCHAR(20) NOT NULL,           -- 'youtube', 'soundcloud', 'upload'
    source_url      VARCHAR(500) NOT NULL,
    duration        INT,                            -- seconds
    votes           INT NOT NULL DEFAULT 0,
    is_approved     BOOLEAN NOT NULL DEFAULT TRUE,  -- FALSE nếu chưa duyệt
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    deleted_at      TIMESTAMPTZ
);

CREATE TABLE playlist_votes (
    id              BIGSERIAL PRIMARY KEY,
    song_id         BIGINT NOT NULL REFERENCES playlist_songs(id) ON DELETE CASCADE,
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    vote            SMALLINT NOT NULL,              -- 1 = up, -1 = down
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE(song_id, user_id)
);

CREATE INDEX idx_playlist_songs_playlist ON playlist_songs(playlist_id, votes DESC);
CREATE INDEX idx_playlist_votes_song ON playlist_votes(song_id);
```

(Các module Phase 2 khác sẽ được viết sau khi Phase 1 + Core Platform requirements hoàn thành)
