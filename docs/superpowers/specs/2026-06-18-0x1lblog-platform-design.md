# 0x1lBlog — Social Blog Platform Design

> **Style:** CSDN + YouTube + Facebook lai
> **Target:** 10k–100k DAU, single monolith JAR
> **Stack:** Java 21 / Spring Boot 4.1 / PostgreSQL 16+ / Caffeine L1 + Redis 8 L2 / MinIO S3 / Vue 3

---

## 1. PHÂN LOẠI NGƯỜI DÙNG

| Role | Mô tả | Quyền tối thiểu |
|------|-------|----------------|
| **GUEST** | Chưa đăng nhập | Xem blog PUBLIC (hạn chế), search cơ bản |
| **USER** | Đã đăng nhập | Viết blog, comment, reaction, bookmark, follow, share |
| **USER (is_creator=true)** | Creator đã kích hoạt monetization | Như USER + wallet, tip, paid series, payout |
| **ADMIN** | Quản trị viên | Quản lý user, category, badge, hệ thống |

---

## 2. YÊU CẦU CHỨC NĂNG (Functional)

### Nhóm 1: Auth & User

| # | Chức năng | Mô tả | Roles |
|---|-----------|-------|-------|
| F1 | Đăng ký / Đăng nhập | Email + password, BCrypt | GUEST |
| F2 | OAuth2 Google | Đăng nhập qua Google | GUEST |
| F3 | JWT access token | Bearer token, 15 phút | All |
| F4 | Refresh token | HttpOnly cookie, 7 ngày | All |
| F5 | Logout | Blacklist token trong Redis | All |
| F6 | Quên / Reset mật khẩu | Email link | GUEST |
| F7 | Profile CRUD | Avatar, bio, website, social links, location... | All |
| F8 | User public profile | Username, nickname, badges, stats | All |

### Nhóm 2: Content (Blog)

| # | Chức năng | Mô tả | Roles |
|---|-----------|-------|-------|
| F9 | Tạo blog | Markdown editor, cover image, hashtag | USER |
| F10 | Blog workflow | DRAFT → PUBLISHED (scheduled optional) → DELETED | USER |
| F11 | Blog visibility | PUBLIC / MEMBERS_ONLY | USER |
| F12 | Blog detail | Xem blog + tags + category + reaction + view count | All |
| F13 | Blog list | Filter: category, hashtag, author, status | All |
| F14 | Blog CRUD | Edit, soft delete, cache evict | USER own |
| F15 | Đếm views | In-memory ConcurrentHashMap, batch flush DB 60s | — |
| F16 | Đếm words + read time | Tự động tính | — |
| F17 | Cover image | Ảnh bìa bài viết | USER |
| F18 | Scheduled publish | Hẹn giờ xuất bản | USER |

### Nhóm 3: Category & Hashtag

| # | Chức năng | Mô tả | Roles |
|---|-----------|-------|-------|
| F19 | Category | 1 cấp, admin tạo, 1 blog 1 category | ADMIN |
| F20 | Hashtag | User tự gắn `#tag`, tự sinh nếu chưa có | USER |
| F21 | Hashtag trending | Top hashtag theo usage_count | All |
| F22 | Khám phá theo hashtag | Click → xem blog trending cùng hashtag | All |

### Nhóm 4: Comment

| # | Chức năng | Mô tả | Roles |
|---|-----------|-------|-------|
| F23 | Tạo comment | 2 cấp (comment → reply), không reply tiếp | USER |
| F24 | Moderation | Anonymous notif → PENDING. Logged-in → auto APPROVED | ADMIN |
| F25 | Comment CRUD | Edit, soft delete | USER own |
| F26 | @Mention | Tag user trong comment → notification | USER |

### Nhóm 5: Social Interaction

| # | Chức năng | Mô tả | Roles |
|---|-----------|-------|-------|
| F27 | Reaction mở rộng | 6 types: LIKE, LOVE, HAHA, WOW, SAD, ANGRY | USER |
| F28 | 1 user 1 reaction/blog | Click lần 2 = chọn khác, click lại = bỏ | USER |
| F29 | Bookmark | Lưu blog vào bộ sưu tập, có note, collection | USER |
| F30 | Follow | Follow user → thấy bài trong feed | USER |
| F31 | Friend | Follow 2 chiều (A→B + B→A), badge bạn bè | USER |
| F32 | Share blog | Copy link + share_count counter | USER |
| F33 | Quote blog | Share kèm bình luận | USER |
| F34 | Series | Tác giả gom blog, sort_order, subscribe | USER |

### Nhóm 6: Discovery & Feed

| # | Chức năng | Mô tả | Roles |
|---|-----------|-------|-------|
| F35 | Feed "Following" | Blog từ users mình follow, mới nhất trước | USER |
| F36 | Feed "For You" | Gợi ý cá nhân hoá theo hành vi đọc + follow + trending | USER |
| F37 | Trending | Hot 24h / Weekly / Monthly — scoring theo engagement | All |
| F38 | Search ranking | Full-text + engagement signals (views, likes, freshness) | All |
| F39 | Related blogs | Gợi ý blog cùng hashtag/category, không trùng blog hiện tại | All |
| F40 | Infinite scroll | Cursor-based, không offset | All |

### Nhóm 7: Gamification

| # | Chức năng | Mô tả | Roles |
|---|-----------|-------|-------|
| F41 | Badge | Auto award qua event (First Blog, Popular, 1 Year...) | All |
| F42 | 3 tiers badge | BRONZE / SILVER / GOLD | All |
| F43 | Daily check-in | Điểm danh → streak → bonus | USER |
| F44 | User level | Điểm từ blog, comment, tương tác | All |

### Nhóm 8: Notification

| # | Chức năng | Mô tả | Roles |
|---|-----------|-------|-------|
| F45 | 8 loại notification | NEW_COMMENT, NEW_REPLY, NEW_FOLLOWER, NEW_BLOG, LIKE_BLOG, BADGE_AWARD, SERIES_NEW_POST, MENTION | USER |
| F46 | Notification center | List notif, đánh dấu đã đọc, unread count | USER |
| F47 | Realtime | WebSocket cho notif instant (phase sau) | USER |

### Nhóm 9: Admin

| # | Chức năng | Mô tả | Roles |
|---|-----------|-------|-------|
| F48 | Dashboard | Thống kê: users, blogs, views, comments | ADMIN |
| F49 | User management | List, set role, block/unblock | ADMIN |
| F50 | Content moderation | Duyệt blog, duyệt/reject comment | ADMIN |
| F51 | Category CRUD | Tạo, sửa, xoá category | ADMIN |
| F52 | Badge management | Tạo badge, award thủ công | ADMIN |
| F53 | Site settings | Cấu hình chung | ADMIN |

### Nhóm 10: Creator Monetization

| # | Chức năng | Mô tả | Roles |
|---|-----------|-------|-------|
| F54 | Wallet | Số dư nội bộ của user, gồm: balance (có thể rút) + bonus (không rút) | All |
| F55 | Nạp tiền | Thanh toán qua gateway (Momo, VNPay, Bank) → cộng balance | USER |
| F56 | Tip author | Chuyển tiền từ wallet → author blog, kèm message (optional) | USER |
| F57 | Paid Series | Series có price, user mua để xem, author nhận doanh thu | USER (is_creator) |
| F58 | Content Paywall | Blog riêng lẻ có price, user mua 1 lần xem vĩnh viễn | USER (is_creator) |
| F59 | Membership | USER subscribe author hàng tháng → badge + content riêng | USER (is_creator) |
| F60 | Revenue Dashboard | Tổng quan: thu nhập (tháng), top blog, tip nhận được, membership | USER (is_creator) |
| F61 | Payout | Rút tiền: balance → bank/Momo, yêu cầu tối thiểu | USER (is_creator) |
| F62 | Payout history | Lịch sử rút tiền, trạng thái (pending/processing/done/rejected) | USER (is_creator) |
| F63 | Affiliate link | Author gắn link sản phẩm → track click → hoa hồng (phase sau) | USER (is_creator) |
| F64 | Bonus/Ads revenue | Chia sẻ doanh thu quảng cáo cho author theo views | ADMIN |

**Business rules:**
- Tip: người tip bị trừ balance, author được cộng balance (admin không thu phí hoặc thu % cố định)
- Paid Series: user mua = author nhận 100% (admin thu phí riêng)
- Membership: admin giữ % phí nền tảng, author nhận phần còn lại
- Wallet: balance KHÔNG lãi, bonus CÓ thể expire, balance CÓ thể rút
- Payout: yêu cầu tối thiểu 50.000 VND (hoặc tương đương), xử lý trong 3-7 ngày
- Content Paywall: blog PAID không index search, không share preview đầy đủ

### Nhóm 11: Analytics & Tracking

| # | Chức năng | Mô tả | Roles |
|---|-----------|-------|-------|
| F65 | Session tracking | ULID session cookie, IP, UA, GeoIP | — |
| F66 | Activity log unified | Gộp page_view + action + API call, trace_id ULID gom nhóm | — |
| F67 | Audit log | activity_log với source='SYSTEM' phân biệt hành vi hệ thống | ADMIN |
| F68 | Ranking cache | Trending hot/weekly/monthly | All |

---

## 3. THUẬT TOÁN & RECOMMENDATION

### 3.1 Trending Scoring

Công thức tính độ hot, refresh mỗi 5 phút qua `@Scheduled`:

```
Trending Score = 
    views * 0.3 +
    likes * 2 +
    comments * 3 +
    bookmarks * 4 +
    shares * 5 +
    freshness_bonus
```

**Freshness bonus:** Giảm dần theo thời gian, reset mỗi 24h.
```
freshness_bonus = max(0, 100 - hours_since_publish * 2)
```
→ Blog mới đăng: +100. Blog 50 giờ tuổi: +0.

**Các period:**
| Bảng | Period | Cache key | TTL |
|------|--------|-----------|-----|
| `trending_hot` | 24h | `trending:hot` | 5 phút |
| `trending_weekly` | 7 ngày | `trending:weekly` | 15 phút |
| `trending_monthly` | 30 ngày | `trending:monthly` | 30 phút |
| `trending_alltime` | ∞ | `trending:alltime` | 1 giờ |

### 3.2 Feed "For You" — Personalized Ranking

Refresh mỗi 5 phút, materialize vào bảng `user_feed`.

**Input signals:**
| Signal | Weight | Nguồn |
|--------|--------|-------|
| Following | 0.40 | User follow ai? |
| Hashtag affinity | 0.25 | Hashtag của blog user từng đọc/like |
| Category affinity | 0.15 | Category ưa thích |
| Trending score | 0.10 | Blog đang hot |
| Recency | 0.10 | Blog càng mới càng cao |

**Hashtag affinity:** `count(reads) / total_reads * log(1 + interactions)` cho mỗi hashtag user từng đọc. Lưu trong Redis: `user:123:hashtag_affinity` → JSON.

**Category affinity:** Tương tự hashtag, tracking qua `user_behavior_log`.

**Cách build feed:**
```sql
-- Mỗi 5 phút, cho mỗi user active
INSERT INTO user_feed (user_id, blog_id, score, reason)
SELECT 
    u.id,
    b.id,
    CASE 
        WHEN f.user_id IS NOT NULL THEN 0.4     -- following
        ELSE 0
    END +
    COALESCE(hf.score, 0) * 0.25 +               -- hashtag affinity
    COALESCE(cf.score, 0) * 0.15 +               -- category affinity
    LEAST(b.views * 0.3 + b.like_count * 2, 0.1) + -- trending
    GREATEST(0, 0.1 - EXTRACT(EPOCH FROM NOW() - b.published_at) / 86400 * 0.001)  -- recency
    AS score,
    'mixed' AS reason
FROM users u
CROSS JOIN blog b
LEFT JOIN follow f ON f.following_id = b.author_id AND f.follower_id = u.id
LEFT JOIN hashtag_affinity hf ON hf.user_id = u.id AND hf.hashtag_id IN (...)
LEFT JOIN category_affinity cf ON cf.user_id = u.id AND cf.category_id = b.category_id
WHERE b.status = 'PUBLISHED' AND b.visibility = 'PUBLIC'
  AND NOT EXISTS (SELECT 1 FROM user_feed uf WHERE uf.user_id = u.id AND uf.blog_id = b.id)
ORDER BY score DESC
LIMIT 200;
```

**Giới hạn:** Chỉ build feed cho user active trong 7 ngày gần nhất (check `last_active_at`).

### 3.3 Related Blogs

Hiển thị dưới mỗi blog detail. SQL thuần, không cache:

```sql
SELECT b.id, b.title, b.views,
       ts_rank(b.search_vector, plainto_tsquery('simple', current_blog.title)) * 0.6 +
       CASE WHEN b.category_id = current_blog.category_id THEN 0.2 ELSE 0 END +
       COUNT(bh.hashtag_id) FILTER (WHERE bh.hashtag_id IN (current_blog_hashtags)) * 0.2
       AS relevance
FROM blog b
LEFT JOIN blog_hashtag bh ON bh.blog_id = b.id
WHERE b.id != current_blog_id
  AND b.status = 'PUBLISHED'
GROUP BY b.id
ORDER BY relevance DESC
LIMIT 6;
```

Cache kết quả 5 phút: `related:blog:{id}`.

### 3.4 Search Ranking

PostgreSQL full-text + engagement signals:

```sql
ranking = 
    ts_rank(search_vector, query, 32) * 0.5 +   -- độ khớp full-text
    log(1 + views) * 0.15 +                       -- độ phổ biến
    like_count / (like_count + 10) * 0.15 +       -- tỷ lệ like (tránh bias)
    (comment_count + bookmark_count) * 0.05 +     -- tương tác
    recency_boost * 0.15                           -- độ mới
```

**recency_boost:**
- < 1 ngày: 1.0
- < 7 ngày: 0.7
- < 30 ngày: 0.4
- < 90 ngày: 0.2
- > 90 ngày: 0.05

### 3.5 Similar Users (Collaborative Filtering — Phase 2)

Dùng cho: "Những người thích bài này cũng thích..."

**Cách tính:** Jaccard similarity trên hành vi (reaction + bookmark + follow chung):

```sql
WITH user_a AS (
    SELECT blog_id FROM blog_reaction WHERE user_id = :userId
    UNION
    SELECT blog_id FROM bookmark WHERE user_id = :userId
),
similar_users AS (
    SELECT r.user_id,
           COUNT(*)::float / (
               (SELECT COUNT(*) FROM user_a) + 
               (SELECT COUNT(*) FROM blog_reaction WHERE user_id = r.user_id)
               - COUNT(*)
           ) AS jaccard
    FROM blog_reaction r
    WHERE r.blog_id IN (SELECT blog_id FROM user_a)
      AND r.user_id != :userId
    GROUP BY r.user_id
    ORDER BY jaccard DESC
    LIMIT 20
)
SELECT b.id, b.title, COUNT(*) AS reasons
FROM similar_users su
JOIN blog_reaction r ON r.user_id = su.user_id
JOIN blog b ON b.id = r.blog_id
WHERE b.id NOT IN (SELECT blog_id FROM user_a)
  AND b.status = 'PUBLISHED'
GROUP BY b.id
ORDER BY COUNT(*) DESC
LIMIT 10;
```

Cache 1 giờ: `recommend:user:{id}:collaborative`.

### 3.6 User Affinity Tracking

Tracking hành vi user để nuôi thuật toán, lưu trong Redis:

**Redis keys:**
| Key | Type | TTL | Mục đích |
|-----|------|-----|----------|
| `user:{id}:hashtag_affinity` | Hash | 7 ngày | Hashtag user quan tâm |
| `user:{id}:category_affinity` | Hash | 7 ngày | Category ưa thích |
| `user:{id}:read_history` | List (100 gần nhất) | 30 ngày | Chống gợi ý trùng |
| `user:{id}:recent_interactions` | Sorted Set | 7 ngày | Đo mức độ active |

**Khi user:**
- Xem blog → increment hashtag_affinity (hash), push read_history (list)
- Like/blog → increment hashtag_affinity cao hơn
- Comment/bookmark → increment cao nhất
- Follow → update category_affinity theo blog của user được follow

### 3.7 Scheduled Tasks

| Task | Cron / Delay | Việc làm |
|------|-------------|----------|
| `refreshTrending` | `fixedDelay=300s` | Tính lại trending hot/weekly/monthly |
| `buildUserFeed` | `fixedDelay=300s` | Build feed cho user active |
| `flushViewCount` | `fixedDelay=60s` | Flush view count từ memory → DB |
| `refreshRelated` | `fixedDelay=300s` | Refresh related blogs cache |
| `cleanupExpiredAffinity` | `daily 3AM` | Xoá affinity cũ > 7 ngày |

**Không realtime:** Tất cả thuật toán chạy scheduled task, không ảnh hưởng response time API.

---

## 4. FILE STORAGE

### 4.1 Kiến trúc

```
Upload → API (multipart) → Image/File Processing → Object Storage → CDN → Client
                              ↓
                         Metadata: PostgreSQL (url, size, type, dimensions...)
```

### 4.2 Loại file

| Loại | Ví dụ | Max size | Format cho phép |
|------|-------|----------|----------------|
| **Image** | Blog cover, avatar, thumbnail | 10MB | JPEG, PNG, WebP, GIF |
| **Video** | Blog embed video | 100MB (phase sau) | MP4, WebM |
| **Attachment** | Code, tài liệu | 20MB | PDF, ZIP, txt, code files |
| **Avatar** | User ảnh đại diện | 2MB | JPEG, PNG, WebP |

### 4.3 Lựa chọn: MinIO (S3-Compatible) — 4 môi trường

**Chọn MinIO vì:** S3 API chuẩn, self-hosted free, không vendor lock-in, giống hệt nhau trên dev/test/staging/prod.

| Môi trường | Chạy ở đâu | CDN | Chi phí |
|-----------|-----------|-----|---------|
| **Dev** | Docker container trong docker-compose | ❌ | $0 |
| **Test (CI)** | GitHub Actions service container | ❌ | $0 |
| **Staging** | Docker container trên VPS | Cloudflare proxy | Disk VPS |
| **Production** | Docker container trên VPS | Cloudflare proxy | Disk VPS |

**Khi scale > 100GB:** MinIO vẫn chạy được cluster mode. Nếu cần migrate → sang Cloudflare R2 (S3 API tương thích, đổi endpoint là xong).

### 4.4 Free Storage Options

| Service | Free tier | CDN | Ghi chú |
|---------|-----------|-----|---------|
| **Cloudflare R2** | **10GB storage, $0 egress** | ✅ Cloudflare CDN | **Recommended.** Không mất phí băng thông. Resize ảnh = Cloudflare Images (tính phí riêng) |
| **Backblaze B2 + Cloudflare** | 10GB free, egress $0 qua Cloudflare | ✅ CDN qua Cloudflare | Phải cấu hình Cloudflare Bandwidth Alliance |
| **Cloudinary** | **25GB storage, 25GB bandwidth/tháng** | ✅ Built-in CDN | Có sẵn image transformation (resize, webp) |
| **MinIO (self-hosted)** | Unlimited (dùng disk VPS) | ❌ tự xử lý | Cần VPS có disk lớn, Nginx làm CDN |

**So sánh chi phí (dự tính 100k DAU, ~10GB ảnh/tháng):**

| Service | Storage 10GB | BW 100GB | Image processing | Tổng/tháng |
|---------|-------------|----------|-----------------|-----------|
| Cloudflare R2 | $0.15 | $0 | Cloudflare Images $0.50/1000 req | ~$0.65 |
| Backblaze B2 | ~$0.10 | $0 (Bandwidth Alliance) | ❌ tự làm | ~$0.10 |
| Cloudinary | Miễn phí (25GB) | Miễn phí (25GB) | Miễn phí | **$0** |
| MinIO | Tiền VPS disk | Tiền VPS BW | ❌ tự làm | Tuỳ VPS |

**Chiến lược:**
- **Dev/Test:** MinIO Docker (giống hệt prod, không lệch môi trường)
- **Staging/Prod:** MinIO Docker + Cloudflare CDN proxy
- **Nếu cần cloud:** Đổi endpoint sang R2 — S3 API compatible, zero code change

### 4.5 Image Processing Pipeline

```
Upload → Validate (type, size, virus scan)
       → Generate variants:
           ├── original (giữ nguyên)
           ├── thumbnail (300x300, webp)
           ├── medium (800x, webp)
           └── large (1600x, webp)  — chỉ cho blog cover
       → Upload all variants to storage
       → Save metadata to PostgreSQL
       → Return URL
```

**Xử lý:** Server-side Java dùng Thumbnailator hoặc Imgscalr (Java thuần, không cần external service).

### 4.6 URL Structure

```
Development:  http://localhost:9000/0x1lblog/{type}/{variant}/{filename}.webp
Production:   https://cdn.0x1l.com/{type}/{variant}/{filename}.webp

{type} = avatars / covers / attachments / thumbnails
{variant} = original / thumbnail / medium / large
```

### 4.7 Signed URLs cho private file

Files từ blog PAID hoặc MEMBERS_ONLY → signed URL có TTL.

```
GET /api/files/{id}/download?token={signed}
→ Generate presigned URL (R2) hoặc serve qua API với auth check
→ URL có TTL 15 phút
```

---

## 5. MÔI TRƯỜNG & TECHNOLOGY STACK

### 5.1 Tổng quan 4 môi trường

| Môi trường | Mục đích | Ai dùng? | Chi phí/tháng |
|-----------|----------|----------|--------------|
| **Dev** | Phát triển local, code, debug | Developer | **$0** (máy cá nhân) |
| **Test** | CI/CD, integration test, E2E | CI pipeline + QA | **$0–$15** (GitHub Actions free + VPS mini) |
| **Staging** | Pre-production, giống prod thật | Developer + Stakeholder | **$15–$30** (1 VPS nhỏ) |
| **Production** | Live, user thật | Người dùng | **$30–$100** (VPS/CDN) |

### 5.2 Dev (Local)

**Công nghệ:** Chạy hoàn toàn trên máy developer, không cần internet.

| Thành phần | Tech | Cấu hình |
|-----------|------|---------|
| **Backend** | Spring Boot Maven | `mvnw spring-boot:run -Dspring.profiles.active=dev` |
| **DB** | PostgreSQL Docker | `docker compose up mysql redis minio` |
| **Cache** | Redis Docker | Cùng docker-compose |
| **Storage** | MinIO Docker (S3-compatible) | Cùng docker-compose |
| **Frontend blog-view** | Vite dev server | `npm run dev` (port 5173, hot reload) |
| **Frontend blog-cms** | Vite dev server | `npm run dev` (port 5174, hot reload) |
| **Debug** | IntelliJ debugger / VS Code | Remote JVM debug port 5005 |
| **Hot reload** | spring-boot-devtools | Auto restart khi code thay đổi |
| **Profiles** | `application-dev.yml` | Log debug, SQL show, cache disabled |

**docker-compose.dev.yml:**
```yaml
services:
  postgres:
    image: postgres:16-alpine
    ports: ["5432:5432"]
    environment:
      POSTGRES_DB: blog
      POSTGRES_USER: blog
      POSTGRES_PASSWORD: blog123
    volumes: ["./data/postgres:/var/lib/postgresql/data"]

  redis:
    image: redis:8-alpine
    ports: ["6379:6379"]
    command: redis-server --requirepass redis123

  minio:
    image: quay.io/minio/minio:RELEASE.2025-09-06T17-38-46Z
    ports:
      - "9000:9000"   # S3 API
      - "9001:9001"   # Console
    environment:
      MINIO_ROOT_USER: minioadmin
      MINIO_ROOT_PASSWORD: minioadmin123
    command: server /data --console-address ":9001"

  pgadmin:
    image: dpage/pgadmin4:latest
    ports: ["5050:80"]
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@blog.local
      PGADMIN_DEFAULT_PASSWORD: admin123
    depends_on: [postgres]
```

**Tính năng dev:**
- Cache L1+L2 tắt hoặc TTL = 0 (luôn miss → query DB thật)
- SQL log hiển thị trong console
- Debug endpoint `/api/h2-console` (nếu dùng H2)
- Upload file → lưu vào MinIO local
- Email dev → console log (không gửi thật)
- OAuth2 → test mode với mock token

### 5.3 Test (CI/CD)

**Công nghệ:** GitHub Actions (free 2000 phút/tháng).

| Thành phần | Tech | Cấu hình |
|-----------|------|---------|
| **CI platform** | GitHub Actions + Maven | `.github/workflows/ci.yml` |
| **DB test** | PostgreSQL service container | Actions built-in |
| **Redis test** | Redis service container | Actions built-in |
| **Storage test** | MinIO Docker hoặc mock | Actions service |
| **Frontend test** | Vitest (headless) | `npm run test:ci` |
| **Backend test** | JUnit 5 + Mockito + Testcontainers | `mvnw verify` |
| **E2E** | Playwright (optional) | `npx playwright test` |
| **Code quality** | SonarQube Cloud (free) | `mvnw sonar:sonar` |
| **Secrets** | GitHub Secrets | DB password, JWT secret, API keys |
| **Artifact** | Docker image build | `docker build -t blog-api:test` |

**CI Pipeline:**
```yaml
on: [push, pull_request]
jobs:
  test:
    runs-on: ubuntu-latest
    services:
      postgres:
        image: postgres:16-alpine
        env: { POSTGRES_DB: blog_test, POSTGRES_USER: blog, POSTGRES_PASSWORD: test }
      redis:
        image: redis:8-alpine
      minio:
        image: quay.io/minio/minio:RELEASE.2025-09-06T17-38-46Z
        command: server /data

    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with: { java-version: 21, distribution: temurin }
      - run: ./mvnw verify -Ptest
      - run: npm ci && npm run test:ci  # frontend tests
      - uses: sonarsource/sonarcloud-github-action@master  # code quality
```

**application-test.yml:**
- DB: PostgreSQL service container (ephemeral, xoá sau test)
- Cache: Redis mock (CacheManager disable)
- Storage: MinIO service hoặc mock S3
- Mail: GreenMail (mock SMTP)
- OAuth2: mock token
- Rate limit: disable

### 5.4 Staging

**Công nghệ:** 1 VPS mini, dùng Docker Compose.

| Thành phần | Tech | Giá |
|-----------|------|-----|
| **Server** | VPS 2 CPU / 4GB RAM | $10–15/tháng |
| **Containers** | Docker Compose | Free |
| **DB** | PostgreSQL Container | Chung VPS |
| **Cache** | Redis Container | Chung VPS |
| **Storage** | MinIO Container | Free |
| **Frontend** | Nginx serve static build | Chung VPS |
| **Backend** | Docker image từ CI | — |
| **Monitor** | Uptime Kuma (self-hosted) | Free |
| **Domain** | staging.0x1l.com | Free subdomain |
| **SSL** | Let's Encrypt + Certbot | Free |
| **Deploy** | GitHub Actions → SSH deploy | Free |

**Deployment flow:**
```
Push → GitHub Actions build & test → Docker image push → SSH vào VPS → docker compose pull & up
```

**application-staging.yml:**
- Profile `staging` — cache bật (TTL ngắn hơn prod)
- DB: PostgreSQL staging (WAL backup hằng ngày)
- Storage: MinIO Docker + Cloudflare proxy CDN
- Email: gửi thật (SendGrid free 100 emails/day)
- Log: INFO + gửi lỗi qua Telegram bot
- Rate limit: thấp hơn prod (test cho kỹ)

### 5.5 Production

**Công nghệ:** Cloud + CDN + Monitoring.

| Thành phần | Tech | Giá/tháng | Ghi chú |
|-----------|------|-----------|---------|
| **VPS** | 4 CPU / 8GB RAM (x2 instance) | $30–$50 | Scale ngang, load balancer |
| **DB** | PostgreSQL managed (Aiven free 5GB → Hetzner €5) | $0–$20 | Aiven free tier tốt cho giai đoạn đầu |
| **Cache** | Redis managed (Aiven free 250MB → Upstash) | $0–$10 | Upstash free 256MB |
| **Storage** | MinIO on VPS + Cloudflare proxy | Disk VPS | S3 API, đồng bộ với dev/test/staging, CDN qua Cloudflare |
| **CDN** | Cloudflare (free plan) | $0 | CDN, SSL, DDoS protection |
| **Load balancer** | Cloudflare Load Balancer (free) | $0 | Hoặc Nginx |
| **Email** | SendGrid free (100/day) → Resend | $0–$10 | |
| **Monitoring** | Grafana + Prometheus self-hosted | $0 | Hoặc Datadog free |
| **Logging** | Loki + Grafana | $0 | Self-hosted |
| **Sentinel** | Error tracking (Sentry free) | $0 | 5k events/month |
| **Domain** | blog.0x1l.com | $0–$10/năm | |
| **SSL** | Cloudflare (miễn phí) | $0 | |

**Kiến trúc Production:**

```
                         Cloudflare CDN
                              |
                    ┌─────────┴─────────┐
                    │   Load Balancer    │
                    └─────────┬─────────┘
                    ┌─────────┴─────────┐
                    │   API Instance 1   │──┐
                    ├────────────────────┤  │
                    │   API Instance 2   │──┤
                    └────────────────────┘  │
                                            │
               ┌────────────────────────────┤
               │                            │
        ┌──────┴──────┐            ┌────────┴───────┐
        │ PostgreSQL   │            │    Redis       │
        │ (Managed)    │            │  (Managed)     │
        └─────────────┘            └────────────────┘

        ┌───────────────────────────────────────────┐
        │    MinIO (Storage) + Cloudflare CDN        │
        │    S3 API, tương thích dev/test/staging    │
        └───────────────────────────────────────────┘
```

**application-prod.yml:**
- Cache FULL: L1 Caffeine + L2 Redis (TTL đầy đủ)
- DB connection pool: HikariCP (tối ưu)
- Rate limit: 100 req/phút (auth), 300 req/phút (read)
- Log: WARN + ERROR → Loki/Grafana
- Health check: Spring Actuator + Prometheus
- Graceful shutdown: 30s timeout

**Backup strategy:**
- DB: PostgreSQL WAL archiving + daily dump → R2
- Redis: snapshot (RDB) mỗi 6h → R2
- Docker image: self-hosted registry hoặc Docker Hub

### 5.6 So sánh công nghệ theo môi trường

| Công nghệ | Dev | Test | Staging | Production |
|-----------|:---:|:----:|:-------:|:----------:|
| **PostgreSQL** | Docker | Service container | Docker | Managed (Aiven/Hetzner) |
| **Redis** | Docker | Service container | Docker | Managed (Upstash) |
| **Storage** | MinIO Docker | MinIO Docker | MinIO Docker | MinIO Docker + Cloudflare CDN |
| **CDN** | ❌ | ❌ | Cloudflare | Cloudflare |
| **Backend** | Maven spring-boot:run | Maven verify | Docker Compose | Docker × 2 instance |
| **Frontend** | Vite dev server | Vitest | Nginx static | Nginx static |
| **Email** | Console log | GreenMail | SendGrid free | SendGrid/Resend |
| **Monitor** | ❌ | ❌ | Uptime Kuma | Grafana + Prometheus |
| **SSL** | ❌ | ❌ | Let's Encrypt | Cloudflare |
| **CI/CD** | ❌ | GitHub Actions | GitHub Actions | GitHub Actions |
| **Chi phí** | $0 | $0–$15 | $15–$30 | $30–$100 |

---

## 6. YÊU CẦU PHI CHỨC NĂNG (Non-Functional)

### Hiệu năng

| # | Yêu cầu | Mục tiêu |
|---|---------|----------|
| NF1 | Response time API (p95) | < 200ms với cache hit, < 500ms cache miss |
| NF2 | Cache L1 hit rate | > 80% (Caffeine) |
| NF3 | Cache L1 + L2 hit rate | > 95% (Caffeine → Redis) |
| NF4 | View count flush | Batch mỗi 60s, mất tối đa 60s data nếu crash |
| NF5 | Concurrent users | 1000+ concurrent |
| NF6 | Page load (frontend) | < 2s First Contentful Paint |
| NF7 | Feed materialize | Refresh mỗi 5 phút, không realtime |

### Bảo mật

| # | Yêu cầu | Mô tả |
|---|---------|-------|
| NF8 | Mật khẩu | BCrypt, không plaintext |
| NF9 | JWT | HS256, 15 phút expiry |
| NF10 | Refresh token | HttpOnly cookie, 7 ngày, có blacklist |
| NF11 | Rate limit | Bucket4j + Redis: 10 req/phút cho GUEST, 60 cho USER, 200 cho ADMIN |
| NF12 | Input validation | `@Valid`, sanitize HTML output (Jsoup) |
| NF13 | SQL injection | MyBatis parameter binding, không concatenation |
| NF14 | CORS | Chỉ cho phép origins: blog.0x1l.com, cms.0x1l.com |
| NF15 | Role check | `@PreAuthorize` trên mọi admin endpoint |

### Tính sẵn sàng

| # | Yêu cầu | Mô tả |
|---|---------|-------|
| NF16 | Uptime | 99.5% (cho phép ~3.5h downtime/tháng) |
| NF17 | Graceful shutdown | Xả cache, đợi request hiện tại xong |
| NF18 | Database backup | Daily snapshot, WAL archiving |
| NF19 | Stateless app | Scale ngang bằng thêm instance, session trong Redis |

### Khả năng mở rộng

| # | Yêu cầu | Mô tả |
|---|---------|-------|
| NF20 | Monolith JAR | Single deploy cho đến 100k DAU |
| NF21 | DB indexing | Composite indexes cho mọi query pattern chính |
| NF22 | Denormalized counters | like_count, comment_count, share_count, bookmark_count trong blog table |
| NF23 | CQRS nhẹ | Read model riêng cho feed (user_feed table) |
| NF24 | Event-driven | Spring ApplicationEvent cho cross-cutting (badge, notif, cache) |

### Khả năng bảo trì

| # | Yêu cầu | Mô tả |
|---|---------|-------|
| NF25 | Layered architecture | Controller → Orchestrator → Service → Repository |
| NF26 | Cache ẩn trong Service | Orchestrator không biết cache tồn tại |
| NF27 | Provider pattern | SQL trong `*SqlProvider.java`, không XML, không annotation SQL |
| NF28 | API DTO in/out | Controller không trả Entity |
| NF29 | MapStruct | Map Entity ↔ Internal DTO ↔ API DTO |
| NF30 | Feature-based packages | `service/blog/`, `service/comment/` — không flat |

### Kiến trúc

| # | Yêu cầu | Mô tả |
|---|---------|-------|
| NF31 | Cache 2 tầng | Caffeine (L1, in-process) → Redis (L2, distributed) → DB |
| NF32 | DB: PostgreSQL | JSONB, tsvector full-text, TIMESTAMPTZ, GIN index |
| NF33 | Auth | JWT (Nimbus HS256) + OAuth2 Google |
| NF34 | ORM | MyBatis + Provider pattern |
| NF35 | Frontend | Vue 3 (blog-view: PrimeVue, blog-cms: Element Plus) |

---

## 7. MA TRẬN PHÂN QUYỀN

| Entity | Action | GUEST | USER | USER (is_creator) | ADMIN |
|--------|--------|:-----:|:----:|:-----------------:|:-----:|
| Blog | Xem | ✅ hạn chế | ✅ | ✅ | ✅ |
| Blog | Tạo | ❌ | ✅ | ✅ | ✅ |
| Blog | Sửa/Xoá | ❌ | ✅ own | ✅ own | ✅ |
| Comment | Xem | ❌ | ✅ | ✅ | ✅ |
| Comment | Tạo | ❌ | ✅ | ✅ | ✅ |
| Comment | Duyệt | ❌ | ❌ | ❌ | ✅ |
| Reaction | Toggle | ❌ | ✅ | ✅ | ✅ |
| Bookmark | CRUD | ❌ | ✅ | ✅ | ✅ |
| Follow | Toggle | ❌ | ✅ | ✅ | ✅ |
| Share | Tạo | ❌ | ✅ | ✅ | ✅ |
| Series | CRUD | ❌ | ✅ own | ✅ own | ✅ |
| Wallet | Xem | ❌ | ✅ | ✅ | ✅ |
| Wallet | Nạp tiền | ❌ | ✅ | ✅ | ✅ |
| Tip | Gửi/Nhận | ❌ | ✅ gửi | ✅ gửi/nhận | ✅ |
| Paid Series | Tạo | ❌ | ❌ | ✅ | ✅ |
| Paid Series | Mua | ❌ | ✅ | ✅ | ✅ |
| Content Paywall | Tạo | ❌ | ❌ | ✅ | ✅ |
| Membership | Tạo | ❌ | ❌ | ✅ | ✅ |
| Membership | Subscribe | ❌ | ✅ | ✅ | ✅ |
| Revenue Dashboard | Xem | ❌ | ❌ | ✅ own | ✅ |
| Payout | Yêu cầu | ❌ | ❌ | ✅ | ✅ |
| Monetization Settings | Quản lý | ❌ | ❌ | ❌ | ✅ |
| Admin | Management | ❌ | ❌ | ❌ | ✅ |

**Hạn chế GUEST (F0):**
- Rate limit 10 request/phút
- Không xem comment, reaction stats
- Search trả kết quả giới hạn
- Chỉ xem blog PUBLIC
- Ẩn reaction buttons, bookmark, follow

---

## 8. PHẠM VI

### Phase 1 — Core (hiện tại + sửa)
- Auth, Blog CRUD, Comment, Category, Hashtag, Search
- Reaction mở rộng, Bookmark, Follow, Share
- Feed Following + Trending
- Notification (REST poll)
- Badge, Daily check-in (cơ bản)
- Admin dashboard cơ bản

### Phase 2 — Social nâng cao
- Feed "For You" (thuật toán)
- Friend system (mutual follow)
- @Mention trong comment
- Notification realtime (WebSocket)
- Infinite scroll (cursor-based)

### Phase 3 — Monetization (khi có creator base)
- Wallet hệ thống: nạp, balance, bonus
- Tip author
- Paid Series + Content Paywall
- Membership subscription
- Revenue Dashboard + Payout

### Phase 4 — Mở rộng (khi có user base)
- Groups / Communities
- Messaging (real-time chat)
- Stories (24h)
- Affiliate link tracking
- Bonus/Ads revenue sharing
