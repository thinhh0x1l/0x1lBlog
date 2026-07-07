# 0x1lSpace Database Schema

> PostgreSQL 16 — 6 modules, 49 tables, 1 materialized view

---

## 1. Auth & User (6 tables)

```mermaid
erDiagram
    users {
        bigint id PK
        varchar username UK
        varchar email UK
        varchar password_hash
        varchar display_name
        varchar avatar_url
        text bio
        varchar website
        varchar location
        jsonb social_links
        varchar role "USER | ADMIN"
        boolean is_creator
        varchar status "ACTIVE | INACTIVE | BANNED"
        timestamptz locked_until
        bigint blog_count
        bigint follower_count
        bigint following_count
        int level
        bigint exp
        int checkin_streak
        date last_checkin_at
        bigint balance
        bigint bonus
        bigint reputation_writing
        bigint reputation_community
        bigint reputation_creativity
        bigint reputation_influence
        timestamptz last_active_at
        timestamptz created_at
        timestamptz updated_at
        timestamptz deleted_at
    }

    oauth2_accounts {
        bigint id PK
        bigint user_id FK
        varchar provider
        varchar provider_id UK
        varchar avatar_url
        jsonb raw_attributes
        varchar email
        timestamptz created_at
    }

    refresh_tokens {
        bigint id PK
        bigint user_id FK
        varchar token_hash UK
        varchar device_info
        inet ip_address
        timestamptz expires_at
        boolean revoked
        timestamptz created_at
    }

    roles {
        bigint id PK
        varchar name UK
        text description
    }

    permissions {
        bigint id PK
        varchar name UK
        text description
    }

    role_permissions {
        bigint role_id PK, FK
        bigint permission_id PK, FK
    }

    users ||--o{ oauth2_accounts : "user_id"
    users ||--o{ refresh_tokens : "user_id"
    roles ||--o{ role_permissions : "role_id"
    permissions ||--o{ role_permissions : "permission_id"
```

---

## 2. Content (11 tables)

```mermaid
erDiagram
    categories {
        bigint id PK
        varchar name UK
        varchar slug UK
        text description
        varchar icon
        varchar color
        int sort_order
        bigint blog_count
        boolean is_visible
        timestamptz created_at
        timestamptz deleted_at
    }

    hashtags {
        bigint id PK
        varchar name UK
        bigint usage_count
        timestamptz created_at
    }

    blog_series {
        bigint id PK
        varchar name
        text description
        varchar cover_image
        bigint author_id FK
        varchar status "ACTIVE | COMPLETED | ARCHIVED"
        int price
        bigint post_count
        bigint subscriber_count
        timestamptz created_at
        timestamptz updated_at
        timestamptz deleted_at
    }

    series_subscribers {
        bigint id PK
        bigint series_id FK
        bigint user_id FK
        timestamptz created_at
    }

    blogs {
        bigint id PK
        bigint author_id FK
        bigint category_id FK
        varchar title
        varchar slug UK
        text content
        varchar description
        varchar cover_image
        varchar content_type "MARKDOWN | HTML"
        varchar location_name
        decimal latitude
        decimal longitude
        varchar status "DRAFT | PUBLISHED | ARCHIVED | DELETED"
        varchar visibility "PUBLIC | FOLLOWERS | FRIENDS | PRIVATE | MEMBERS_ONLY | PAID"
        int price
        boolean is_top
        boolean is_recommend
        boolean allow_comments
        int words
        int read_time
        bigint views
        bigint like_count
        bigint love_count
        bigint haha_count
        bigint wow_count
        bigint sad_count
        bigint angry_count
        bigint comment_count
        bigint bookmark_count
        bigint share_count
        tsvector search_vector "GEN ALWAYS"
        timestamptz published_at
        timestamptz last_commented_at
        timestamptz created_at
        timestamptz updated_at
        timestamptz deleted_at
    }

    blog_hashtags {
        bigint blog_id PK, FK
        bigint hashtag_id PK, FK
    }

    series_blogs {
        bigint series_id PK, FK
        bigint blog_id PK, FK
        int sort_order
        text note
        timestamptz created_at
    }

    statuses {
        bigint id PK
        bigint user_id FK
        bigint thread_id FK "self-ref"
        smallint part_order
        varchar content
        varchar image_url
        varchar visibility "PUBLIC | FOLLOWERS | FRIENDS | PRIVATE"
        timestamptz created_at
        timestamptz updated_at
        timestamptz deleted_at
    }

    status_polls {
        bigint id PK
        bigint status_id FK
        varchar question
        jsonb options
        timestamptz ends_at
        timestamptz created_at
    }

    status_poll_votes {
        bigint id PK
        bigint poll_id FK
        bigint user_id FK
        smallint option_index
        timestamptz created_at
    }

    stories {
        bigint id PK
        bigint user_id FK
        varchar media_url
        varchar media_type "image | video | text"
        varchar text_content
        varchar visibility
        bigint view_count
        timestamptz expires_at
        timestamptz created_at
        timestamptz deleted_at
    }

    story_archives {
        bigint id PK
        bigint user_id FK
        bigint story_id FK, UK
        varchar media_url
        varchar media_type
        varchar text_content
        bigint view_count
        timestamptz created_at
        timestamptz archived_at
    }

    users ||--o{ blog_series : author_id
    users ||--o{ blogs : author_id
    categories ||--o{ blogs : category_id
    blogs ||--o{ blog_hashtags : blog_id
    hashtags ||--o{ blog_hashtags : hashtag_id
    blog_series ||--o{ series_blogs : series_id
    blogs ||--o{ series_blogs : blog_id
    blog_series ||--o{ series_subscribers : series_id
    users ||--o{ series_subscribers : user_id
    users ||--o{ statuses : user_id
    statuses ||--o{ statuses : thread_id "self-ref"
    statuses ||--o{ status_polls : status_id
    status_polls ||--o{ status_poll_votes : poll_id
    users ||--o{ status_poll_votes : user_id
    users ||--o{ stories : user_id
    users ||--o{ story_archives : user_id
    stories ||--o{ story_archives : story_id
```

---

## 3. Comment (unified — `target_type` + `target_id`)

```mermaid
erDiagram
    comments {
        bigint id PK
        varchar target_type "BLOG | STATUS"
        bigint target_id
        bigint parent_id FK "self-ref"
        bigint user_id FK
        text content
        varchar status "APPROVED | PENDING | FLAGGED"
        timestamptz created_at
        timestamptz updated_at
        timestamptz deleted_at
    }

    comments ||--o{ comments : "parent_id (self-ref)"
    comments }o--|| users : user_id
```

---

## 4. Social (5 tables)

```mermaid
erDiagram
    reactions {
        bigint id PK
        bigint user_id FK
        varchar target_type "BLOG | STATUS | STORY | COMMENT"
        bigint target_id
        varchar type "LIKE | LOVE | HAHA | WOW | SAD | ANGRY"
        timestamptz created_at
    }

    bookmarks {
        bigint id PK
        bigint user_id FK
        bigint blog_id FK
        varchar collection
        text note
        boolean is_public
        timestamptz created_at
    }

    follows {
        bigint id PK
        bigint follower_id FK
        bigint following_id FK
        timestamptz created_at
    }

    shares {
        bigint id PK
        bigint user_id FK
        varchar target_type "BLOG | STATUS"
        bigint target_id
        text content
        timestamptz created_at
    }

    mentions {
        bigint id PK
        bigint target_user_id FK
        bigint mentioned_by FK
        varchar source_type "BLOG | STATUS | COMMENT"
        bigint source_id
        timestamptz created_at
    }

    users ||--o{ reactions : user_id
    users ||--o{ bookmarks : user_id
    blogs ||--o{ bookmarks : blog_id
    users ||--o{ follows : "follower_id"
    users ||--o{ follows : "following_id"
    users ||--o{ shares : user_id
    users ||--o{ mentions : target_user_id
    users ||--o{ mentions : mentioned_by
```

---

## 5. Gamification (4 tables)

```mermaid
erDiagram
    badges {
        bigint id PK
        varchar name UK
        varchar display_name
        text description
        varchar icon_url
        varchar tier "BRONZE | SILVER | GOLD"
        jsonb criteria
        timestamptz created_at
    }

    user_badges {
        bigint id PK
        bigint user_id FK
        bigint badge_id FK
        bigint awarded_by FK
        timestamptz awarded_at
    }

    daily_checkins {
        bigint id PK
        bigint user_id FK
        date checkin_date
        int streak_at_time
        int bonus_exp
        timestamptz created_at
    }

    user_exp_log {
        bigint id PK
        bigint user_id FK
        int amount
        varchar reason
        bigint ref_id
        timestamptz created_at
    }

    users ||--o{ user_badges : user_id
    badges ||--o{ user_badges : badge_id
    users ||--o{ daily_checkins : user_id
    users ||--o{ user_exp_log : user_id
```

---

## 6. Notification (1 table)

```mermaid
erDiagram
    notifications {
        bigint id PK
        bigint user_id FK
        bigint actor_id FK
        varchar type "NEW_COMMENT | NEW_REPLY | NEW_FOLLOWER | NEW_BLOG | LIKE_BLOG | LIKE_COMMENT | BADGE_AWARD | SERIES_NEW_POST | MENTION | TIP_RECEIVED | PAYOUT_STATUS"
        varchar title
        text message
        varchar target_type
        bigint target_id
        boolean is_read
        timestamptz created_at
    }

    users ||--o{ notifications : user_id
    users ||--o{ notifications : actor_id
```

---

## 7. Analytics & Tracking (2 tables)

```mermaid
erDiagram
    sessions {
        bigint id PK
        varchar session_id UK
        bigint user_id FK
        inet ip_address
        text user_agent
        varchar device_type "DESKTOP | MOBILE | TABLET"
        char country_code
        varchar city
        timestamptz started_at
        timestamptz ended_at
        int duration_seconds
    }

    activity_log {
        bigint id PK
        varchar trace_id
        bigint session_id FK
        bigint user_id FK
        varchar category "PAGE_VIEW | ACTION | API | SYSTEM"
        varchar action
        varchar source "USER | SYSTEM"
        varchar target_type
        bigint target_id
        jsonb metadata
        timestamptz created_at
    }

    users ||--o{ sessions : user_id
    sessions ||--o{ activity_log : session_id
    users ||--o{ activity_log : user_id
```

---

## 8. System (3 tables)

```mermaid
erDiagram
    site_settings {
        bigint id PK
        varchar key UK
        text value
        varchar type "STRING | INT | BOOLEAN"
        text description
        timestamptz updated_at
    }

    about_info {
        bigint id PK
        text content
        varchar type "ABOUT | TERMS | PRIVACY"
        timestamptz created_at
        timestamptz updated_at
    }

    dead_letter_events {
        bigint id PK
        varchar event_type
        jsonb payload
        text error_message
        int retry_count
        varchar status
        timestamptz created_at
        timestamptz last_retry_at
    }
```

---

## 9. Canvas & Playlist (5 tables)

```mermaid
erDiagram
    canvases {
        bigint id PK
        varchar type "profile | community | event"
        varchar title
        int width
        int height
        bigint owner_id FK
        timestamptz starts_at
        timestamptz ends_at
        boolean is_active
        timestamptz created_at
    }

    canvas_strokes {
        bigint id PK
        bigint canvas_id FK
        bigint user_id FK
        int x
        int y
        varchar color
        smallint brush_size
        timestamptz created_at
    }

    playlists {
        bigint id PK
        bigint owner_id FK, UK
        varchar title
        boolean is_public
        int song_count
        timestamptz created_at
        timestamptz updated_at
    }

    playlist_songs {
        bigint id PK
        bigint playlist_id FK
        bigint added_by FK
        varchar title
        varchar artist
        varchar source "youtube | soundcloud | upload"
        varchar source_id
        varchar thumbnail_url
        int duration_sec
        int sort_order
        int vote_count
        timestamptz created_at
    }

    playlist_votes {
        bigint id PK
        bigint playlist_id FK
        bigint song_id FK
        bigint user_id FK
        smallint vote "1 | -1"
        timestamptz created_at
    }

    users ||--o{ canvases : owner_id
    canvases ||--o{ canvas_strokes : canvas_id
    users ||--o{ canvas_strokes : user_id
    users ||--o{ playlists : owner_id
    playlists ||--o{ playlist_songs : playlist_id
    users ||--o{ playlist_songs : added_by
    playlists ||--o{ playlist_votes : playlist_id
    playlist_songs ||--o{ playlist_votes : song_id
    users ||--o{ playlist_votes : user_id
```

---

## 10. Profile, Skill, Quest, Shop, Blind (10 tables)

```mermaid
erDiagram
    profile_widgets {
        bigint id PK
        bigint user_id FK
        varchar widget_type
        boolean is_visible
        int sort_order
        jsonb config
        timestamptz created_at
        timestamptz updated_at
    }

    skill_trees {
        bigint id PK
        bigint category_id FK
        varchar name
        text description
        varchar perk_type
        jsonb perk_value
        int points_required
        int sort_order
        timestamptz updated_at
    }

    user_skill_progress {
        bigint id PK
        bigint user_id FK
        bigint category_id FK
        int total_points
    }

    user_skill_unlocks {
        bigint id PK
        bigint user_id FK
        bigint skill_id FK
        timestamptz unlocked_at
    }

    quests {
        bigint id PK
        varchar type "DAILY | WEEKLY | SEASON | MILESTONE | CROSS"
        varchar title
        text description
        jsonb conditions
        jsonb rewards
        boolean is_active
        timestamptz created_at
        timestamptz updated_at
    }

    user_quests {
        bigint id PK
        bigint user_id FK
        bigint quest_id FK
        int progress
        int target
        varchar status "IN_PROGRESS | COMPLETED | CLAIMED | EXPIRED"
        timestamptz claimed_at
        timestamptz expires_at
        timestamptz created_at
    }

    item_catalog {
        bigint id PK
        varchar name
        text description
        varchar category "VISUAL | EFFECT | PERK | SOCIAL | MISCHIEF | COLLECTIBLE"
        varchar rarity "COMMON | UNCOMMON | RARE | EPIC | LEGENDARY | MYTHIC"
        varchar duration_type "PERMANENT | TIMED | CONSUMABLE | SEASONAL"
        int duration_days
        int price_coins
        int price_gems
        int price_usd
        int max_supply
        int current_supply
        jsonb effect_config
        boolean is_active
        timestamptz created_at
        timestamptz updated_at
    }

    user_inventory {
        bigint id PK
        bigint user_id FK
        bigint item_id FK
        int serial_number
        varchar source "SHOP | QUEST | RANK | GACHA | CRAFT | TRADE | GIFT | EVENT"
        timestamptz acquired_at
        timestamptz expires_at
        boolean is_equipped
        int trade_count
    }

    blind_challenges {
        bigint id PK
        date date UK
        bigint topic_id FK
        varchar topic_hint
        jsonb options
        boolean revealed
        timestamptz created_at
    }

    blind_challenge_guesses {
        bigint id PK
        bigint challenge_id FK
        bigint user_id FK
        bigint guessed_topic_id
        boolean is_correct
        timestamptz created_at
    }

    users ||--o{ profile_widgets : user_id
    categories ||--o{ skill_trees : category_id
    users ||--o{ user_skill_progress : user_id
    categories ||--o{ user_skill_progress : category_id
    users ||--o{ user_skill_unlocks : user_id
    skill_trees ||--o{ user_skill_unlocks : skill_id
    users ||--o{ user_quests : user_id
    quests ||--o{ user_quests : quest_id
    users ||--o{ user_inventory : user_id
    item_catalog ||--o{ user_inventory : item_id
    categories ||--o{ blind_challenges : topic_id
    blind_challenges ||--o{ blind_challenge_guesses : challenge_id
    users ||--o{ blind_challenge_guesses : user_id
```

---

## 11. Materialized View

```mermaid
erDiagram
    friends {
        bigint user_id1
        bigint user_id2
        timestamptz friendship_started_at
    }
```

`friends` — mutual follow (user A follows B AND B follows A). REFRESH MATERIALIZED VIEW CONCURRENTLY on every follow/unfollow.

---

## Summary

| Module | Tables | Key Tables |
|--------|--------|------------|
| Auth & User | 6 | `users`, `oauth2_accounts`, `refresh_tokens` |
| Content | 11 | `blogs`, `categories`, `hashtags`, `statuses`, `stories` |
| Comment | 1 | `comments` (unified `target_type` + `target_id`) |
| Social | 5 | `reactions`, `bookmarks`, `follows`, `shares`, `mentions` |
| Gamification | 4 | `badges`, `daily_checkins`, `user_exp_log` |
| Notification | 1 | `notifications` |
| Analytics | 2 | `sessions`, `activity_log` |
| System | 3 | `site_settings`, `about_info`, `dead_letter_events` |
| Canvas & Playlist | 5 | `canvases`, `playlists`, `playlist_songs` |
| Extended Profile | 10 | `skill_trees`, `quests`, `item_catalog`, `blind_challenges` |
| **Total** | **48** | |

### Design patterns

- **Soft delete**: `deleted_at` on all entities — every query `WHERE deleted_at IS NULL`
- **Unified relations**: `comments`, `reactions`, `shares`, `mentions` use `target_type` + `target_id` (polymorphic FK)
- **Denormalized counters**: `blogs.views`, `users.follower_count` — updated via `syncCounters()` job
- **Triggers**: `update_updated_at_column()` on 7 tables, `refresh_friends_mv()` on follow changes
- **Search**: GIN index on `blogs.search_vector` (tsvector) with weighted title/description/content
- **Trending**: Index on `(views*3 + like_count*20 + comment_count*30 + bookmark_count*40 + share_count*50) DESC`
