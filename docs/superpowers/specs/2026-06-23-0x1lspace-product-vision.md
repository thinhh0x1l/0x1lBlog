# 0x1lSpace — Product Vision

> **Ngày:** 2026-06-23
> **Trạng thái:** Approved
> **Identity:** Social Creator Universe — không phải blog platform, không phải social network thuần. Là vũ trụ nơi mỗi người có một bản thể số để sáng tạo, kết nối, chơi và kiếm tiền.

---

## 1. Product Identity

### 0x1lSpace là gì?

```
0x1lSpace = CSDN × Facebook × YouTube × MySpace × Steam
```

Một nền tảng nơi người dùng đến để:

| Họ đến để... | Reward loop |
|---|---|
| **Thể hiện cá tính** | Soul Space (profile modular, items, decoration) — Social proof, khoe |
| **Viết dài** | Blog (multi-visibility) — Weekly meso-loop |
| **Cập nhật nhanh** | Status (thread + poll) — Daily micro-loop |
| **Khoảnh khắc 24h** | Story (ảnh/video/text, tự hủy) — Daily engagement |
| **Kết nối** | Follow, Friend, Circle, Guild, Duel — Social bonding |
| **Sáng tạo cùng nhau** | Canvas cộng đồng (vẽ chung), Playlist cộng đồng (nhạc share) — Co-creation loop |
| **Chơi & phá** | Mischief (profile prank), Duel, Battle, Quest, Gacha, Craft — Dopamine loop |
| **Sưu tầm** | Items, Badge, Stamp, Limited Edition — Collecting loop |
| **Khám phá** | Random Discovery ("Đi lạc"), Interaction Map, Blind Challenge — Curiosity loop |
| **Kiếm tiền** | Tip, Paywall, Membership, Item Design — Creator economy |
| **Hoài niệm** | Personal Timeline, Time Capsule, Sound Garden — Nostalgia loop |

### Đối tượng

- Dev + non-dev
- Nội dung đa dạng (tech, đời sống, review, sáng tác...)
- Scale target: 10k–100k DAU, single monolith JAR

### Cốt lõi: Blog là 1 feature, không phải sản phẩm

Không giống WordPress hay Medium — 0x1lSpace không bán "công cụ viết blog". Nó bán **một thế giới số** nơi blog chỉ là một trong nhiều cách để tương tác. Người dùng ở lại vì:
- Có mục tiêu mỗi ngày (quest)
- Có thứ để sưu tầm (item)
- Có địa vị để tranh (rank)
- Có bạn bè để ghẹo (mischief)
- Có tranh để vẽ cùng (canvas)
- Có nhạc để chia sẻ (playlist)
- Có bạn bè để chơi cùng (guild, duel)
- Có nơi để khám phá (đi lạc, interaction map)
- Có tiền để kiếm (creator economy)

---

## 2. Content Formats

| Format | Độ dài | Visibility | Tính năng đặc biệt |
|--------|--------|------------|-------------------|
| **Blog** | Dài (≥1000 chữ) | PUBLIC / FOLLOWERS / FRIENDS / PRIVATE / MEMBERS_ONLY | Search vector, reaction, bookmark, share, paid paywall |
| **Status** | Ngắn (≤500 chữ) | PUBLIC / FOLLOWERS / FRIENDS / PRIVATE | 2 dạng: Thread (nối tiếp) hoặc Poll (gắn vào thread) |
| **Story** | Ảnh/video/text | PUBLIC / FOLLOWERS / FRIENDS | 24h tự hủy, Facebook-style, reactions riêng |

### Visibility Model (chi tiết)

| Visibility | Ai xem? | Blog | Status | Story | Note |
|------------|---------|------|--------|-------|------|
| **PUBLIC** | Ai cũng xem được (kể cả chưa đăng nhập) | ✅ | ✅ | ✅ | Mặc định |
| **FOLLOWERS** | Follower của author | ✅ | ✅ | ✅ | Tăng động lực follow |
| **FRIENDS** | Mutual follow | ✅ | ✅ | ✅ | Nội dung riêng tư |
| **PRIVATE** | Chỉ author + admin | ✅ | ✅ | ❌ | Draft, note cá nhân |
| **MEMBERS_ONLY** | Member/subscriber | ✅ | ❌ | ❌ | Paid content |
| **PAID** | Trả phí per-content | ✅ | ❌ | ❌ | Tip/paywall cho blog dài |

---

## 3. Social Layer

| Tính năng | Mô tả | Phase |
|-----------|-------|-------|
| **Follow** | 1 chiều, không cần approve | P1 |
| **Friend** | Mutual follow = bạn bè (materialized view) | P1 |
| **Circle** | User-created group (subreddit style), có admin riêng | P2 |
| **Guild** | Team cạnh tranh theo mùa, guild shop, guild rank (chỉ khi ≥100 active users) | P2/P3 |
| **Duel** | 1v1 cùng chủ đề, có cọc, vote + comment bắt buộc | P3 |
| **Blog Battle** | Bracket tournament 16/32/64 người, format tăng dần, prize pool | Event feature |
| **Canvas cộng đồng** | Shared canvas, ai cũng vẽ được, vẽ ngu vẫn vui | P2 |
| **Community Playlist** | Shared music playlist, cộng đồng cùng add nhạc vote | P2 |
| **Mentor System** | Senior → junior, cả 2 có reward (chỉ enable khi có senior tự nguyện) | P3+ |

### Đã loại bỏ (không phù hợp scale đầu)

| Tính năng | Lý do |
|-----------|-------|
| **Co-authoring** | Niche, ít dùng, phức tạp không tương xứng |
| **Debate Thread** | Rủi ro moderation quá cao |
| **Anonymous Mode** | Toxicity + moderation impossible ở scale nhỏ |
| **Quiz (standalone)** | Over-engineer cho phase đầu, poll trong Status đủ |
| **Knowledge Base** | Wiki-style quá nặng, khác xa blog |
| **Showcase** | Portfolio/gallery trùng với blog + Soul Space |
| **Remix / Fork** | Copyright unclear, moderation overhead |

---

## 4. Duel & Battle

### 4.1 Duel — 1v1

**Luồng:**

```
Bước 1: Challenge
  User A chọn chủ đề (từ danh sách hoặc tự nhập) — "Tech: Java vs Go"
  → Đặt cọc: 100 Coins hoặc 1 item (người thắng lấy cả)

Bước 2: Match
  System match User B cùng chủ đề (hoặc A mời bạn bè)
  → B chấp nhận → cả 2 đóng cọc

Bước 3: Viết (3-7 ngày)
  Mỗi người viết 1 bài (Blog / Status)
  → Cả 2 bài ẩn đến khi hết hạn — không ai thấy bài đối thủ

Bước 4: Vote (7 ngày)
  Cả 2 bài public cùng lúc
  → Reader vote "Bài nào hay hơn?" + để lại 1 comment ngắn (chống vote rỗng)
  → 1 user chỉ vote 1 lần

Bước 5: Kết quả
  Thắng: cọc ×2 + Duel Point + Season Points + item roll
  Thua: mất cọc (vào prize pool), bài vẫn giữ
  Hòa: cả 2 nhận lại cọc
```

**Cơ chế đặc biệt:**

| Cơ chế | Effect |
|--------|--------|
| **Cọc** (Coins/item) | Rủi ro thực — không vào nếu không dám mất |
| **Bài ẩn đến hết hạn** | Tò mò — không biết đối thủ viết gì |
| **Comment bắt buộc** | Anti-spam vote — phải đọc mới comment được |
| **Duel Point** | Tích lũy → rank riêng: Bronze → Silver → Gold → Diamond → 0x1l |
| **Revenge** | Nếu A thắng B, B có thể thách đấu lại free 1 lần trong 7 ngày |

### 4.2 Blog Battle — Tournament

**Format theo vòng (tăng dần độ khó):**

| Vòng | Format | Thời gian |
|------|--------|-----------|
| V1 (16→8) | Status | 3 ngày viết + 3 ngày vote |
| V2 (8→4) | Blog | 5 ngày viết + 5 ngày vote |
| V3 (4→2) | Blog + Status (poll) | 5 ngày viết + 5 ngày vote |
| Chung kết (2→1) | Status (thread, 3 part) | 7 ngày viết + 7 ngày vote |

**Prize pool:** Mỗi người đăng ký đóng 200 Coins → 16 × 200 = 3.200 Coins. Platform fee 10%.

| Hạng | Giải |
|------|------|
| 🥇 1st | 1.500 Coins + Legendary Battle badge + Exclusive item |
| 🥈 2nd | 800 Coins + Epic Battle badge |
| 🥉 3rd | 400 Coins + Rare Battle badge |
| 4th–8th | 100 Coins + Uncommon badge |
| 9th–16th | Participant stamp |

**Special rules:**
- Spectator vote = 50% điểm, Duel Point của người chơi = 50%
- Loser's Bracket: thua v1 → xuống nhánh thua, vẫn có cơ hội
- Live bracket page: countdown, bracket tree, bài đã public

---

## 5. Soul Space — Profile Modular

Mỗi user có một "không gian cá nhân" có thể trang trí, sắp xếp.

### Widget system

| Widget | Mô tả | Có sẵn? |
|--------|-------|---------|
| **Avatar + Border** | Avatar với item border | ✅ Free |
| **Bio** | Giới thiệu bản thân | ✅ Free |
| **Blog List** | Blog đã publish | ✅ Free |
| **Badge Wall** | Badge + Stamp đã đạt được | ✅ Free |
| **Stats** | View count, follower, reaction nhận được | ✅ Free |
| **Rolltext Banner** | Chữ chạy ngang profile | Item |
| **Music Box** | Auto-play nhạc khi vào profile | Item |
| **Theme** | Background, màu sắc, font | Item |
| **Garden Status** | "Đang hoạt động" / "Đang vắng" | ✅ Free |
| **Interaction Map** | Graph bạn bè + mức độ tương tác | P2 |
| **Sound Garden** | Khu vườn ảo — mỗi cây = 1 blog | P2 |
| **Personal Timeline** | Lịch sử mọi hoạt động trên nền tảng | P2 |
| **Game Mode Switch** | Toggle profile ↔ RPG character sheet | P2 |

### Game Mode (Profile dạng RPG)

Khi bật game mode, profile hiển thị như nhân vật game:
- **Level** + EXP bar (mana bar)
- **HP Bar** = Reputation Score
- **Equipment slots** = item đang equip (avatar border, theme, rolltext...)
- **Skill Tree** = các kỹ năng đã unlock
- **Achievement List** = badge + stamp
- **Title** hiển thị dưới tên

### Layout

- Fixed grid (Phase 1): các ô cố định, user chọn show/hide
- Free drag-drop (Phase 2): kéo thả widget, resizable (kiểu MySpace)

---

## 6. Mischief System — Phá nhau vui vẻ

User có thể mua item "xấu" để phá profile bạn bè — cả người phá lẫn người dọn đều tiêu Coins, tạo coin sink tự nhiên.

### Catalog

| Hành động | Effect | Thời gian | Giá mua (phá) | Giá dọn (gỡ) |
|-----------|--------|-----------|:-------------:|:------------:|
| **Release roach** | Con gián chạy quanh profile | 24h | 100 Coins | 50 Coins |
| **Trash dump** | Thùng rác ở góc profile | 24h | 100 Coins | 50 Coins |
| **Spray paint** | Chữ bậy trên background | 12h | 200 Coins | 80 Coins |
| **Rain cloud** | Mây đen mưa trên avatar | 6h | 200 Coins | 100 Coins |
| **Fart bubble** | Bong bóng thúi quanh tên | 12h | 200 Coins | 60 Coins |
| **Slime trail** | Chất nhờn dưới chân avatar | 24h | 200 Coins | 70 Coins |
| **Cobweb** | Mạng nhện phủ widget | 24h | 100 Coins | 40 Coins |
| **Ghost** | Hồn ma bay quanh profile | 48h | 500 Coins | 120 Coins |
| **Troll face popup** | Face khi click vào profile | 1 ngày | 500 Coins | 100 Coins |

### Rarity & giá

| Rarity | Ví dụ | Giá phá | Giá dọn |
|--------|-------|:-------:|:-------:|
| Common | Roach, Cobweb, Trash | 100 | 40-50 |
| Uncommon | Spray, Fart, Slime | 200 | 60-80 |
| Rare | Rain, Troll, Ghost | 500 | 100-120 |

### Anti-abuse

| Cơ chế | Mô tả |
|--------|-------|
| **Cooldown** | 1 user bị phá tối đa 3 lần/ngày |
| **Block** | User có thể block người khác vĩnh viễn |
| **Auto-expire** | Effect tự hết — không dính mãi |
| **Immunity** | Premium membership → chỉ bị effect 1/2 thời gian |
| **Revenge free** | Bị phá → trả đũa free 1 lần trong 24h |
| **Report** | Phá quá đà → admin mute mischief của user đó |

### Chaos Pack

| Gói | Giá |
|-----|:---:|
| 5 effect Common ngẫu nhiên | 400 Coins (thay vì 500) |
| 10 effect bất kỳ | 1.800 Coins (tiết kiệm 200) |
| Revenge Pass (1 tháng trả đũa free) | 300 Gems |
| Immunity Shield (1 tuần miễn nhiễm) | 500 Gems |

### Economy impact

```
User A kiếm 300 Coins từ quest
  → Mua Rain Cloud (200) phá User B
  → User B trả 100 Coins dọn
Platform thu: 200 + 100 = 300 Coins bị tiêu hủy (không vào túi ai)
→ Chống lạm phát
→ User cần kiếm Coins tiếp → vào app hàng ngày
```

---

## 7. Gamification — Content RPG

### 7.1 Reputation Score (đa chiều)

| Chiều | Cách tính | Hiển thị |
|-------|-----------|----------|
| **Writing Power** | Chất lượng blog (độ dài, readability, reactions, comments) | Điểm + title |
| **Community** | Comment hữu ích, mentor, giúp đỡ người khác | Điểm + badge |
| **Creativity** | Status (thread + poll), format mới | Điểm + title |
| **Influence** | Followers, shares, mentions, được repost | Điểm + rank |

### 7.2 Skill Tree

- Mỗi category (Tech, Đời sống, Review, Sáng tác...) là một nhánh kỹ năng
- Viết blog trong category → tích điểm nhánh đó
- Mở khóa: upload lớn hơn, embed video, scheduled post, analytics pro, custom domain

### 7.3 Level & Title

| Level | Title | Yêu cầu |
|-------|-------|---------|
| 1–5 | **Newbie** | Mới đăng ký |
| 6–10 | **Writer** | Viết 5 blogs |
| 11–20 | **Creator** | Viết 20 blogs, 10 reactions |
| 21–30 | **Bậc thầy** | Viết 50 blogs, level 25+ |
| 31–40 | **Chuyên gia** | 100 blogs, reputation ≥500 |
| 41–49 | **Huyền thoại** | 200 blogs, có item Legendary |
| 50 | **0x1l Legend** | Prestige ít nhất 1 lần |

### 7.4 Prestige System

- Khi đạt level 50 → có thể Prestige (reset về level 1)
- Giữ lại: items, badge, title, reputation score
- Nhận: Prestige badge (theo số lần Prestige), +500 Gems, exclusive item
- Prestige càng nhiều → càng danh giá

### 7.5 Streak & Momentum

| Cơ chế | Mô tả |
|--------|-------|
| **Login Streak** | Ngày login liên tiếp → bonus Coins + Gems |
| **Publishing Streak** | Ngày publish liên tiếp → bonus EXP × multiplier |
| **Momentum Bar** | Visual bar: publish 3 blogs/tuần → bar đầy → boost content trong feed |
| **Streak Freeze** | Mua bằng Gems, giữ streak 1 ngày vắng |

---

## 8. Reward System

### 8.1 Currencies

| Currency | Ký hiệu | Nhận từ | Tiêu vào | Hao hụt? |
|----------|---------|---------|----------|----------|
| **EXP** | Level | Mọi hành động | Level up, skill tree | Không |
| **Reputation** | ★ | Đa chiều | Title, priority feed, social proof | Giảm nếu vi phạm |
| **Gems** | 💎 | Quest, rank, sự kiện, check-in | Item limited, gacha, fusion, immunity shield | Tiêu |
| **Coins** | 🪙 | Hàng ngày, login, đọc, reaction | Item common, mischief (phá + dọn), tip nhẹ | Tiêu |
| **Season Points** | 🏆 | Hành động trong mùa | Season rank, limited item | Reset mỗi mùa |
| **Guild Contribution** | 🛡️ | Hoạt động guild | Guild shop, guild rank | Reset theo mùa guild |

### 8.2 Reward Loops — 4 vòng lặp

#### Loop 1: Daily Micro-loop (≤5 phút/ngày)

```
Login        → +10 Coins +5 EXP
Read 1 blog  → +5 Coins +10 EXP +1 Stamp progress
Viết Status → +15 Coins +20 EXP +5 Creativity
Check-in     → streak × 2 Gems
Daily Quest  → +50 Gems + item roll
```

→ Lý do quay lại mỗi ngày: tối thiểu 5 phút → 6 loại reward.

#### Loop 2: Weekly Meso-loop (1–2 giờ/tuần)

```
Viết 3 blogs         → +100 Gems + skill tree unlock token
Momentum ≥80%        → x2 EXP tuần sau
Top Writer category  → badge + title
Weekly Quest         → +1 Gacha ticket
```

→ Lý do đầu tư cuối tuần: reward không thể kiếm daily.

#### Loop 3: Seasonal Macro-loop (1 tháng)

```
Tích Season Points cả tháng
Top 10 mỗi chuyên mục   → Legendary badge + Limited item
Top 100                 → Epic badge
Top 50%                 → Rare item
Guild top 3             → Exclusive border cả guild
Prestige                → Reset + Legendary item
```

→ Lý do gắn bó 1 tháng: exclusive reward không mua được.

#### Loop 4: Daily Engagement Loop (bất kỳ lúc nào)

```
Vào profile bạn → thấy con gián → cười
  → Mua item trả đũa → 200 Coins
  → Bạn kia dọn → 100 Coins
Cả 2 cùng vui → quay lại app

Hoặc:
  → "Đi lạc" → random profile → +1 Stamp
  → Blind Challenge → xem chủ đề mai
  → Live Leaderboard → thấy mình sắp rớt rank → viết 1 bài
```

→ Lý do mở app nhiều lần/ngày: luôn có thứ để làm dù không viết.

#### Loop 4 (cũ → đổi): Yearly Ultra-loop (1 năm)

```
Time Capsule: nhìn lại 1 năm
Anniversary badge
Top creator năm          → "0x1l Legend" + profile gold border
Lifetime writing ≥365    → "Bất tử" title
Retained 5+ năm          → Special role color
```

→ Lý do ở lại dài hạn: nostalgia + legacy.

### 8.3 Reward Ma trận

| Action | EXP | Rep | Coins | Gems | Season | Guild | Streak | Quest |
|--------|:---:|:---:|:-----:|:----:|:------:|:-----:|:------:|:-----:|
| Login | 5 | 0 | 10 | 0 | 0 | 0 | +1 day | — |
| Đọc blog | 10 | 0 | 5 | 0 | 0 | 0 | — | — |
| Reaction | 5 | +1 Comm | 3 | 0 | 1 | 0 | — | — |
| Comment | 15 | +3 Comm | 5 | 0 | 2 | 0 | — | Count |
| Viết Status | 20 | +5 Creat | 15 | 0 | 5 | +5 | — | Count |
| Vẽ canvas (stroke) | 1 | 0 | 0 | 0 | 0 | 0 | — | — |
| Add nhạc playlist | 3 | 0 | 5 | 0 | 0 | 0 | — | — |
| Nhạc được +10 votes | 10 | +5 Creat | 50 | 0 | 0 | 0 | — | — |
| Viết blog | 50 | +10 Write | 30 | 0 | 10 | +15 | +1 | Count |
| Blog được 10 reactions | 20 | +5 Infl | 10 | 0 | 3 | 0 | — | — |
| Check-in 7 days | 30 | 0 | 0 | 20 | 0 | 0 | Reset | — |
| Duel thắng | 40 | +10 Comm | 0 | 30 | 30 | +30 | — | — |
| Season top 10 | 500 | +50 Infl | 0 | 200 | Reset | +100 | — | — |
| Prestige | Reset | +100 All | 0 | 500 | 0 | 0 | — | — |
| Mischief (phá) | 0 | 0 | -100~500 | 0 | 0 | 0 | — | — |
| Mischief (dọn) | 5 | 0 | -40~120 | 0 | 0 | 0 | — | — |
| "Đi lạc" khám phá | 5 | 0 | 3 | 0 | 0 | 0 | — | — |

---

## 9. Item System

### 9.1 Item Categories

| Loại | Mục đích | Ví dụ |
|------|----------|-------|
| **Visual** | Trang trí profile/blog | Profile theme, avatar border, rolltext banner, music box, canvas brush theme, playlist cover, comment highlight color, animated background |
| **Effect** | Hiệu ứng khi tương tác | ❤️ mưa trái tim, comment pháo hoa, custom cursor, canvas paint splash, page transition |
| **Perk** | Mở khóa tính năng | Custom slug, no ads, scheduled post, analytics pro, upload 100MB |
| **Social** | Tương tác người khác | Gift item tặng bạn, custom emoji, special role trong Circle |
| **Mischief** | Phá profile bạn bè | Roach, trash, spray paint, rain cloud, ghost, troll face |
| **Collectible** | Sưu tầm thuần túy | Stamp kỷ niệm, limited edition có số serial, seasonal souvenir |

### 9.2 Rarity

| Rarity | Màu | Mua được? | Trade? | Số lượng |
|--------|-----|-----------|--------|----------|
| **Common** | Xám | Coins | ✅ | Unlimited |
| **Uncommon** | Xanh | Gems | ✅ | Unlimited |
| **Rare** | Tím | Gems / Gacha | ✅ | Unlimited |
| **Epic** | Đỏ | Gacha / Craft | ❌ Soulbound | Unlimited |
| **Legendary** | Vàng cam | Sự kiện / Top rank | ❌ Soulbound | 100–1000 cái |
| **Mythic** | Cầu vồng | Giải đặc biệt / Contest | ❌ Soulbound | ≤10 cái toàn nền tảng |

### 9.3 Duration

| Duration | Ví dụ |
|----------|-------|
| **Permanent** | Theme mua, badge đạt được |
| **Timed** (30/90/365 ngày) | Music box thuê, gói perk |
| **Consumable** (1 lần) | Gacha ticket, reroll scroll |
| **Seasonal** | Season border, mất khi hết mùa |

### 9.4 Item Sources

```
SHOP        → Coins / Gems / Real money
REWARD      → Quest / Rank / Event / Check-in
CRAFT       → Fusion 3 Common → 1 Rare / Upgrade
GACHA       → Mystery box, random item
TRADE       → Auction House P2P, platform fee
GIFT        → Mua tặng bạn bè
CREATE      → User thiết kế, bán lại 70/30
MILESTONE   → 100 blogs, 1 năm, Prestige
LIMITED     → 100 cái, có số serial
MISCHIEF    → Shop phá nhau (roach, trash, ghost...)
```

---

## 10. Challenge System

### 10.1 System Challenges

| Loại | Ví dụ | Frequency |
|------|-------|-----------|
| **Daily** | "Viết 1 Status hôm nay" | Hàng ngày |
| **Weekly** | "Nhận 30 reactions tuần này" | Hàng tuần |
| **Season** | "Top 10 Tech mùa này" | Hàng tháng |
| **Milestone** | "Viết blog thứ 100" | Một lần |
| **Cross** | "Đọc 5 blog + comment 3 bài" | Hàng tuần |
| **Event** | "Halloween: viết blog chủ đề ma" | Sự kiện |
| **Blind Challenge** | Chủ đề ẩn ngày mai, ai đoán đúng → bonus | Hàng ngày |

### 10.2 User Challenges

| Loại | Ví dụ | Giới hạn |
|------|-------|----------|
| **Creator** | "Viết blog chủ đề AI, top 3 được tôi tặng item" | Reward pool do creator bỏ ra |
| **Guild** | "Guild nào reactions cao nhất tuần → border" | Admin guild tạo |
| **Duel** | "1v1 cùng chủ đề, vote 7 ngày" | Tự do |
| **Open** | "Ai cũng tham gia được" | Moderation, giới hạn reward |

### 10.3 Anti-abuse

- User challenge: giới hạn reward pool (max 500 Coins hoặc 50 Gems)
- Cooldown giữa các challenge (1/ngày)
- Flag nếu challenge có dấu hiệu exploit
- Admin có thể duyệt hoặc từ chối

---

## 11. Discovery — Khám phá

| Tính năng | Mô tả | Phase |
|-----------|-------|-------|
| **Feed For You** | Algo blend: following + trending + affinity | P1 basic, P2 full |
| **Trending** | Hot 24h / Weekly / Monthly | P1 |
| **Search** | Full-text PostgreSQL + engagement signals | P1 |
| **Related Blogs** | Cùng hashtag/category | P1 |
| **Hashtag Trending** | Top hashtag theo usage | P1 |
| **Random Discovery ("Đi lạc")** | 1 nút → random profile/blog, có thể lọc "cùng sở thích", mỗi lần +1 Stamp + có thể drop item | P1 |
| **Live Leaderboard** | "Ai đang hot hôm nay?", "Ai đang viết nhiều nhất tuần?", "Guild nào dẫn đầu?" — real-time | P2 |
| **Interaction Map** | Graph bạn bè: chấm = user, kích thước = tương tác, khoảng cách = thân thiết, click → xem activity | P2 |
| **Sound Garden** | Khu vườn ảo — mỗi cây = 1 blog/thought, cây lớn dần theo tương tác, có thể decorate, bạn bè vào thăm + để lại "light" | P2 |
| **Personal Timeline** | Tự động ghi lại mọi khoảnh khắc: "Ngày X bạn viết blog Y", "Bạn đạt level Z", "Bạn thắng duel", "Bạn kết bạn với A" | P2 |
| **Blind Challenge** | Chủ đề ẩn mỗi ngày — ai đoán đúng trước khi tiết lộ → bonus, viết đúng chủ đề → x2 EXP | P2 |
| **Topic Graph** | Map chủ đề user quan tâm | P2 |
| **Cross-pollination** | "User thích bài này cũng thích..." | P2 |
| **Personalized Digest** | Email top 10 trong tuần | P2 |
| **Reading Stats** | "Bạn đọc 50k chữ tuần này" | P2 |

---

## 12. Creator Economy

| Tính năng | Mô tả | Phase |
|-----------|-------|-------|
| **Wallet** | Balance (rút được) + Bonus (không rút được) | P3 |
| **Tip** | Reader → Creator, trừ balance người gửi | P3 |
| **Paid Series** | Series có price, mua 1 lần xem vĩnh viễn | P3 |
| **Content Paywall** | Blog PAID, không index search | P3 |
| **Membership** | 3 tiers: Basic/Pro/Elite, monthly subscription | P3 |
| **Item Design** | User thiết kế item, bán, platform 30% fee | P3 |
| **Revenue Dashboard** | Thống kê thu nhập, top blog, item sales | P3 |
| **Payout** | Rút tiền về bank/Momo, min 50k VND | P3 |

---

## 13. Anti-churn Mechanisms

| Cơ chế | Mô tả |
|--------|-------|
| **Streak Freeze** | Mua bằng Gems, không mất streak khi vắng 1 ngày |
| **Returning Bonus** | Vắng 7 ngày → quay lại x2 EXP 3 ngày |
| **"We Miss You"** | Email: blog hot trong tuần, bạn bè vừa đăng |
| **Gradual Decay** | Rep score giảm nhẹ nếu không active (không phạt nặng) |
| **Anti-burnout** | Nhắc nếu đăng >5 blogs/ngày: "Quality > Quantity" |
| **Content Recycling** | Blog cũ tự đề xuất lại sau 3/6/12 tháng |
| **Friend Trigger** | "Bạn của bạn vừa viết blog mới" |
| **Time Capsule** | "1 năm trước bạn viết..." → nostalgia |
| **Mischief Decay** | Effect tự hết — không dính mãi, kích thích quay lại xem profile |
| **"Đi lạc" Hook** | Không biết gì → click random → bất ngờ |

---

## 14. User Actions Catalog

### 14.1 Content — Tạo nội dung

| Hành động | Format | Input | Output |
|-----------|--------|-------|--------|
| Viết blog | Blog dài | Text, image, cover, tags, visibility | 1 blog |
| Viết Status | Status | Text, image ≤500 chữ | 1 status |
| Viết thread | Status (thread) | Multi-part, nối tiếp | 1 thread |
| Tạo poll | Status (poll) | Câu hỏi + options gắn vào thread | 1 poll gắn |
| Viết Story | Story | Ảnh/video/text, 24h tự hủy | 1 story |
| Đăng ký series | Subscribe | Series ID | 1 subscription |

### 14.2 Social — Kết nối

| Hành động | Target | Effect |
|-----------|--------|--------|
| Follow | User | +1 follower, vào feed |
| Unfollow | User | -1 follower, khỏi feed |
| Accept follow | User | Thành friend nếu mutual |
| Invite friend | Email/link | Referral tracking |
| Tạo Circle | — | 1 group mới |
| Join Circle | Circle ID | Thành member |
| Rời Circle | Circle ID | Mất quyền |
| Mời vào Guild | User ID | Thành guild member |
| Rời Guild | Guild ID | Mất quyền |
| Duel challenge | User ID | Bắt đầu duel |
| Chấp nhận duel | Duel ID | Vào duel |
| Tạo battle | — | Tạo tournament |
| Đăng ký battle | Battle ID | Vào tournament |
| Vote duel/battle | Duel/Battle ID | 1 vote |
| Mentor request | User ID | Gửi yêu cầu mentor |
| Nhận mentee | User ID | Chấp nhận mentor |

### 14.3 Interaction — Tương tác

| Hành động | Target | Effect |
|-----------|--------|--------|
| Reaction LIKE | Blog / Comment | +1 like count |
| Reaction LOVE | Blog / Comment | +1 love count |
| Reaction HAHA | Blog / Comment | +1 haha count |
| Reaction WOW | Blog / Comment | +1 wow count |
| Reaction SAD | Blog / Comment | +1 sad count |
| Reaction ANGRY | Blog / Comment | +1 angry count |
| Bỏ reaction | Blog / Comment | -1 count |
| Comment | Blog | Tạo comment |
| Reply | Comment | Tạo reply |
| Edit comment | Comment của mình | Sửa |
| Delete comment | Comment của mình | Xóa mềm |
| Bookmark | Blog | Vào collection |
| Remove bookmark | Blog | Khỏi collection |
| Share internal | Blog | +1 share, vào feed |
| Share external | Blog | Copy link |
| Quote share | Blog | Share + comment |
| @Mention | User trong content | Tạo mention |
| Report | Content / User | Gửi report |

### 14.4 Economy — Kinh tế

| Hành động | Target | Effect |
|-----------|--------|--------|
| Mua item (Coins) | Item ID | Trừ Coins, thêm item |
| Mua item (Gems) | Item ID | Trừ Gems, thêm item |
| Mua item (tiền thật) | Item ID | Trừ wallet, thêm item |
| Equip item | Inventory ID | Item hiển thị |
| Unequip item | Inventory ID | Item ẩn |
| Gift item | User ID + Item | Trừ item mình, thêm cho người |
| Sell item | Item ID (Auction) | Lên sàn |
| Buy item (Auction) | Listing | Trả giá |
| Craft item | Fusion | 3 Common → 1 Rare |
| Gacha | Gacha box | Random item |
| Mua mischief item | Mischief ID | Phá profile người khác |
| Dọn mischief | Effect trên profile | Gỡ effect |
| Tip | User + Amount | Trừ balance → + balance author |
| Mua blog paywall | Blog ID | Mở khóa blog PAID |
| Subscribe membership | Creator ID | Monthly subscription |
| Unsubscribe | Creator ID | Hủy định kỳ |
| Yêu cầu payout | — | Wallet → bank |
| Nạp tiền | — | Bank → wallet |
| Mua Gems | Gói Gems | Tiền thật → Gems |

### 14.5 Gamification — Chơi

| Hành động | Target | Effect |
|-----------|--------|--------|
| Nhận daily quest | Quest ID | Quest active |
| Hoàn thành quest | Quest ID | Reward |
| Blind Challenge đoán | — | Nếu đúng → bonus |
| Blind Challenge viết | — | x2 EXP |
| Tạo user challenge | Config | Challenge public |
| Tham gia challenge | Challenge ID | Bắt đầu |
| Check-in | — | Streak + reward |
| Mở skill tree | Skill node | Unlock |
| Prestige | — | Reset level, giữ items |
| Reroll item | Item ID | Đổi item khác (consumable) |
| Mở rộng inventory slot | — | +10 slot (perk) |
| "Đi lạc" | — | Random profile/blog + Stamp |

### 14.6 Profile — Cá nhân hóa

| Hành động | Effect |
|-----------|--------|
| Sửa avatar | Đổi ảnh |
| Sửa bio | Đổi giới thiệu |
| Sửa social links | Thêm/link mạng xã hội |
| Sắp xếp widget | Drag-drop |
| Chọn theme | Đổi giao diện profile |
| Chọn rolltext | Set chữ chạy |
| Bật/tắt music box | Nhạc nền |
| Chọn playlist cho music box | Playlist ID |
| Set garden status | "Đang hoạt động" / "Đang vắng" |
| Toggle game mode | Profile ↔ RPG character sheet |
| Xem personal timeline | Lịch sử của mình |
| Xem interaction map | Graph bạn bè |
| Xem sound garden | Vườn của user |

### 14.7 Discovery — Khám phá

| Hành động | Effect |
|-----------|--------|
| Search | Kết quả search |
| Browse feed | Scroll |
| Filter feed | Theo category/tag |
| Click hashtag | Blog cùng tag |
| Click category | Blog cùng category |
| "Đi lạc" | Random content + Stamp |
| Xem trending | Top hot |
| Xem live leaderboard | Bảng xếp hạng real-time |
| Xem blind challenge | Chủ đề ẩn ngày mai |

### 14.8 Co-creation — Sáng tạo cùng nhau

| Hành động | Target | Effect |
|-----------|--------|--------|
| Vẽ canvas | Canvas | 1 nét vẽ (pixel/stroke) |
| Xem canvas lịch sử | Canvas ID | Replay từng nét |
| Canvas event | Canvas ID | Canvas limited-time (Tết, Halloween) |
| Thêm nhạc playlist | Playlist ID | 1 bài hát vào playlist |
| Xóa nhạc playlist | Playlist ID | Xóa bài của mình |
| Vote nhạc | Song ID | Upvote/downvote |
| Bật playlist profile | Playlist ID | Nhạc nền Soul Space |
| Share playlist | Playlist ID | Copy link |

### 14.9 System — Hệ thống

| Hành động | Effect |
|-----------|--------|
| Đăng ký | Tạo account |
| Đăng nhập | Active session |
| Login OAuth2 | Google login |
| Logout | Revoke token |
| Xác thực email | Verify |
| Quên mật khẩu | Reset |
| Đổi mật khẩu | Security |
| Xóa tài khoản | Soft delete |
| Báo cáo lỗi | Gửi feedback |

---

## 15. Phased Rollout

```
Phase 0 — Foundation (bây giờ)
├── Fix schema visibility: PUBLIC/FOLLOWERS/FRIENDS/PRIVATE/MEMBERS_ONLY
├── Consolidate cache (2 services)
├── Fix transaction rule (Service layer)
├── Fix Orchestrator không gọi cache
├── Event retry/rollback strategy
├── Đồng bộ toàn bộ docs
└── Đọc lại codebase → đánh giá thực tế

Phase 1 — Hook & Identity (~2 tháng)
├── Soul Space cơ bản: profile widget (avatar, bio, blog list, badge wall, rolltext, garden status)
├── Content RPG: Skill tree, Daily Quest, Level + Title system
├── Reputation Score (đa chiều: Writing/Community/Creativity/Influence)
├── Streak & Momentum (login streak, publishing streak, momentum bar)
├── Virtual Shop cơ bản: item catalog, purchase (Coins/Gems), inventory, equip/unequip
├── Items: profile theme, avatar border, rolltext banner, custom slug
├── Status (thread + poll)
├── Mischief System cơ bản: 5-6 effect common/uncommon, revenge, block
├── Daily/Weekly/Season/Milestone challenges
├── Random Discovery ("Đi lạc")
├── Blind Challenge (chủ đề ẩn)
└── Anti-churn: returning bonus, streak freeze, we miss you email

Phase 2 — Community & Social (~3 tháng)
├── Circle (user-created group, admin riêng)
├── Guild/Clan system + guild shop + guild rank
├── Season Rank + Prestige System
├── Live Leaderboard (real-time)
├── Interaction Map (graph bạn bè)
├── Personal Timeline
├── Game Mode profile (RPG character sheet)
├── Story (24h tự hủy)
├── Canvas cộng đồng (shared pixel canvas)
├── Community Playlist (nhạc cộng đồng vote)
├── Feed For You (algo blend)
├── Cross-pollination + Topic Graph
├── Personalized Digest (email)
├── Reading Stats
├── User challenges (creator/guild/open)
└── Item Fusion + Gacha + Auction House

Phase 3 — Duel, Battle & Monetization (~4 tháng)
├── Duel 1v1 (cọc, hidden entry, vote + comment, duel rank)
├── Blog Battle (tournament event, bracket, prize pool)
├── Wallet: nạp, balance, bonus
├── Tip, Paid Series, Content Paywall
├── Membership (3 tiers: Basic/Pro/Elite)
├── Revenue Dashboard + Payout
├── Custom Item Creator (user design → sell, 70/30 split)
├── Sound Garden (khu vườn ảo)
├── Soul Space: free drag-drop layout
├── Time Capsule
├── Content AI Coach (readability, tag suggest, quality score)
├── Mentor System (chỉ khi có senior tự nguyện)
├── Mythic items + Limited edition auction
├── Cross-platform syndication
└── Chaos Pack (gói mischief, immunity shield, revenge pass)
```

---

## 16. Non-functional Requirements

| # | Yêu cầu | Mục tiêu |
|---|---------|----------|
| NF1 | Response time API (p95) | <200ms cache hit, <500ms cache miss |
| NF2 | Cache L1+L2 hit rate | >95% |
| NF3 | Concurrent users | 1000+ |
| NF4 | Page load (frontend) | <2s FCP |
| NF5 | Cache stampede protection | Per-key mutex |
| NF6 | View count flush | Batch 60s |
| NF7 | Uptime | 99.5% |
| NF8 | Counter không được âm | DB CHECK |
| NF9 | Role check | `@PreAuthorize` |
| NF10 | Rate limit | Bucket4j + Redis |
