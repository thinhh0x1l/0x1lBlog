# 0x1lSpace — Blog-View Layout Design

**Date:** 2026-06-23
**Author:** Design System Agent
**Status:** Draft

---

## 1. Overview

Redesign the blog-view page (`/blog/:id`) from the current 2-column layout to a **3-column layout** that integrates all P1 gamification/social features (R101–R110, R301–R303) while keeping the blog reading experience as the primary focus.

### Design Principle

Blog content is the star. Left sidebar builds author authority and trust. Right sidebar drives reader engagement and curiosity. Gamification elements are contextual — they appear only when relevant to the current blog/author/reader.

---

## 2. Layout Structure

```
┌──────────────────────────────────────────────────────────────────────┐
│  HEADER (sticky, h=64px)                                             │
│  Logo | Search | Trang chủ | Lưu trữ | Về tôi | Notif | User menu   │
├─────────────┬──────────────────────────────────┬────────────────────┤
│  LEFT       │  CENTER                          │  RIGHT             │
│  280px      │  flex: 1 (min-width: 0)          │  280px             │
│             │                                   │                    │
│  STICKY     │  ← scroll →                      │  STICKY            │
│  top: 88px  │                                   │  top: 88px         │
│             │                                   │                    │
│  Author     │  Blog Header                      │  Music Box (R303)  │
│  Card       │  (category, title, meta, cover)   │  (if equipped)    │
│  (expanded) │                                   │                    │
│             │  Blog Content                     │  Canvas Preview    │
│  Status     │  (markdown + typography)          │  (R302, 200×200)  │
│  (R101)     │                                   │                    │
│             │  Reaction Bar                     │  Daily Quest (R104)│
│  Story      │  (6 emoji + bookmark + share)     │  (if reader login) │
│  (R301)     │                                   │                    │
│             │  Author CTA                       │  Blind Challenge   │
│  TOC        │  (follow, more posts)             │  (R110, teaser)    │
│             │                                   │                    │
│  Reading    │  Comments                         │  Skill Tree (R103) │
│  Progress   │  (tree + form + replies)          │  (category-based)  │
│             │                                   │                    │
│             │  Related Posts (R106)             │  Trending (mini)   │
│             │  (grid 2-3 cards)                 │                    │
│             │                                   │  Tags (compact)    │
├─────────────┴──────────────────────────────────┴────────────────────┤
│  FOOTER                                                             │
└──────────────────────────────────────────────────────────────────────┘
```

### 2.1 Breakpoints

| Viewport | Layout | Behavior |
|----------|--------|----------|
| ≥1200px | 3 columns | Full experience |
| 768–1199px | 2 columns | Hide right sidebar (content collapses into left or below blog) |
| <768px | 1 column | Both sidebars hidden. Floating TOC + music buttons. |

### 2.2 Sticky Behavior

Both sidebars use `position: sticky; top: calc(var(--header-height) + 24px); height: fit-content`.

---

## 3. Left Sidebar Components

### 3.1 Author Card (expanded)

```
┌──────────────────────────┐
│  [======== cover gradient ========]  │  h=72px
│           ◉ avatar (72px)           │  margin-top: -36px
│       Tác giả (name)                │  h3, font-weight: 700
│       [Creator] (role badge)        │
│  ──────────────────────────          │
│   12 blogs    1.2k followers        │  stats row (3 items)
│  ──────────────────────────          │
│  Lv.7   ▓▓▓▓▓▓▓▓░░░░  2,340/5,000  │  EXP bar + level
│  ──────────────────────────          │
│  🔥 12-day streak     ★ 4.8 rep     │  R108 + R107
│  ──────────────────────────          │
│  🏆 ⭐ 💎 🔥  (badge mini row)      │  R105 (top 4)
│  ──────────────────────────          │
│  Giới thiệu: "Developer..."         │  collapsible
│  Website: thinh.dev                  │
│  Địa điểm: Hanoi, Vietnam           │
│  Tham gia: 06/2024                   │
│  ──────────────────────────          │
│  [  + Follow  ]  [  ✉ Message  ]    │  action buttons
└──────────────────────────┘
```

**States:**
- **Loading**: Skeleton placeholder matching card shape
- **Logged out**: Show all info except "Message" button
- **Own profile**: Hide Follow/Message, show "Edit profile"
- **Logged in + following**: Toggle to "✓ Following" / "Unfollow"

### 3.2 Recent Status (R101)

Shown if author has a status within last 7 days. Compact card below author card.

```
┌──────────────────────────┐
│  📝 Status               │
│  "Đang học Rust... mượt  │
│   mà!"                    │
│  🗳️ Rust vs Go?  [3 votes]│  (nếu là poll)
│  2h ago                   │
└──────────────────────────┘
```

### 3.3 Active Story (R301)

Shown if author has unexpired story (24h). Horizontal scrollable story rings.

```
┌──────────────────────────┐
│  📸 Story                 │
│  ○ ○ ○ ○ +               │
│  (story rings, 48px each) │
│  "12h còn lại"            │
└──────────────────────────┘
```

### 3.4 Table of Contents

Keep current implementation. Extract headings from blog content. Active heading highlighted on scroll.

### 3.5 Reading Progress

Keep current implementation. Progress bar tracks scroll position through `.blog-content`.

---

## 4. Center Content Components

### 4.1 Blog Header

```
Category badge (pill, primary color)
# Blog Title (h1, 2rem, font-weight: 800)

◎ Avatar (40px)  Tác giả · 12/03/2025 · 5 phút đọc
                                     👁️ 1.2k  💬 8
```

- Keep current styling
- Category links to `/category/:slug`

### 4.2 Cover Image

Keep current. Max height 400px, object-fit cover, hover zoom 1.01 scale.

### 4.3 Blog Content

Keep current. Markdown rendered via `marked` + `DOMPurify`. Prism.js syntax highlighting. Medium-zoom on images.

### 4.4 Reaction Bar

```
┌───────────────────────────────────────────┐
│  👍 42  ❤️ 15  😂 3  😮 1  😢 0  😡 0   │
│                               [★ Lưu] [↗] │
└───────────────────────────────────────────┘
```

- 6 reactions: LIKE, LOVE, HAHA, WOW, SAD, ANGRY
- Active state: filled background + border
- Bookmark + Share buttons on right
- Animate reaction count on click (micro-interaction)

### 4.5 Author CTA

```
┌───────────────────────────────────────────┐
│  ◎ Avatar  Tác giả                        │
│  "Nếu bạn thấy bài viết hay, hãy theo dõi │
│   để ủng hộ mình nhé!"                    │
│  [❤️ Follow]  [📝 Xem thêm bài viết]      │
└───────────────────────────────────────────┘
```

- Simple card below reaction bar
- Only shown if reader ≠ author
- "Xem thêm bài viết" links to `/profile/:userId`

### 4.6 Comments

Keep current structure:
- Comment form (if logged in) / Login prompt (if not)
- Comment tree with replies
- Reactions on comments (❤️ toggle)
- Pagination or infinite scroll

### 4.7 Related Posts (R106)

```
┌─ Bài viết liên quan ──────────────────────┐
│                                            │
│  ┌──────┐  ┌──────┐  ┌──────┐             │
│  │ cover │  │ cover │  │ cover │            │
│  │ Title │  │ Title │  │ Title │            │
│  │ date  │  │ date  │  │ date  │            │
│  └──────┘  └──────┘  └──────┘             │
└────────────────────────────────────────────┘
```

- 3-card grid (responsive: 2→1 on smaller screens)
- Same category + published
- Thumbnail card style

---

## 5. Right Sidebar Components

### 5.1 Music Box (R303)

Shown if current author has equipped a playlist. Mini music player.

```
┌──────────────────────────┐
│  🎵 Nhạc nền              │
│  ─────────────────        │
│  🎶 Tên bài hát            │
│  👤 Tên ca sĩ              │
│  ⏸️ ▓▓▓▓▓▓▓▓░░░░  3:45   │
│  [⏮] [▶⏸] [⏭]            │
│  🔊 ▓▓▓▓▓░░░░░░░          │
│  ─────────────────        │
│  📋 Danh sách phát         │
│  1. ◉ Bài A (đang phát)   │
│  2. ○ Bài B               │
│  3. ○ Bài C               │
│  ...                       │
└──────────────────────────┘
```

**States:**
- **No playlist**: Hidden entirely
- **Has playlist**: Show mini player with current track
- **Loading**: Skeleton

### 5.2 Canvas Preview (R302)

Mini preview of author's equipped canvas.

```
┌──────────────────────────┐
│  🎨 Canvas               │
│  ┌──────────────────┐    │
│  │                  │    │
│  │  [200×200 img]   │    │
│  │                  │    │
│  └──────────────────┘    │
│  "Click để xem full"     │
└──────────────────────────┘
```

- 200×200 preview (scaled to fit ~260px card)
- Click → open full canvas in lightbox
- If no canvas: show "Tác giả chưa có canvas"

### 5.3 Daily Quest (R104)

Shown only if current reader is logged in.

```
┌──────────────────────────┐
│  ⚔️ NHIỆM VỤ HÔM NAY      │
│  ─────────────────        │
│  📝 Viết 1 blog            │
│  ▓▓▓▓▓░░░░░░  1/1 ✅      │
│  📖 Đọc 3 blogs            │
│  ▓▓▓░░░░░░░░  1/3         │
│  👍 Reaction 5 bài         │
│  ░░░░░░░░░░░  0/5         │
│                            │
│  2/3 quest hoàn thành     │
└──────────────────────────┘
```

**States:**
- **Not logged in**: Hidden
- **Logged in, no active quests**: Show "Hôm nay không có nhiệm vụ mới. Quay lại sau!"
- **Logged in, has quests**: Show quest list with progress bars
- **All completed**: Show celebration state + "Đã hoàn thành tất cả!"

### 5.4 Blind Challenge (R110)

Teaser for today's blind challenge.

```
┌──────────────────────────┐
│  ❓ BLIND CHALLENGE        │
│  ─────────────────        │
│  "??? — Hãy đoán chủ đề   │
│   hôm nay!"               │
│                            │
│  Gợi ý: "Một ngôn ngữ     │
│   lập trình"               │
│                            │
│  [🎯 Đoán ngay]           │
│  ─────────────────        │
│  🔓 Tiết lộ lúc 20:00     │
│  (còn 5h 30p)             │
└──────────────────────────┘
```

**States:**
- **Before 20:00**: Show hint + options + guess button (if not guessed yet)
- **After 20:00 (revealed)**: Show topic + "Kết quả: Bạn đã đoán đúng/sai"
- **If reader guessed correctly**: Celebration state

### 5.5 Skill Tree (R103)

Category-based skill tree progress for the blog's category.

```
┌──────────────────────────┐
│  🌳 KỸ NĂNG — Lập trình   │
│  ─────────────────        │
│  Node 1: Upload 10MB      │
│  ▓▓▓▓▓▓▓▓▓░  90%         │
│  Node 2: Scheduled Post   │
│  ▓▓▓▓░░░░░░  40%          │
│  Node 3: Analytics Pro    │
│  ▓░░░░░░░░░  10%          │
│                            │
│  [Xem tất cả kỹ năng]     │
└──────────────────────────┘
```

**States:**
- **No skill tree for category**: Show "Danh mục này chưa có kỹ năng"
- **Reader not logged in**: Show "Đăng nhập để xem tiến trình kỹ năng của bạn"
- **Reader logged in**: Show progress bars
- **All unlocked**: Show "Đã mở khóa toàn bộ!" with link to skill tree page

### 5.6 Trending (compact)

Mini trending list from AppSidebar.

```
┌──────────────────────────┐
│  🔥 XU HƯỚNG              │
│  ─────────────────        │
│  1. Bài viết A           │
│  2. Bài viết B           │
│  3. Bài viết C           │
└──────────────────────────┘
```

- Only top 3, no rank colors (keep minimal)
- Click → `/blog/:id`

### 5.7 Tags (compact)

Compact tag cloud from current blog's tags.

```
┌──────────────────────────┐
│  #Tags: [Vue.js] [Rust]  │
│  [TypeScript] [System    │
│   Design]                 │
└──────────────────────────┘
```

- Only show tags from the current blog (not all tags)
- Not a full tag cloud — just the blog's tags as pills

---

## 6. Conditional Rendering Logic

| Component | Condition |
|-----------|-----------|
| Author Card | Always |
| Recent Status | Author has status < 7 days old |
| Active Story | Author has unexpired story |
| TOC | Blog content has ≥3 headings |
| Reading Progress | Always |
| Reaction Bar | Always |
| Author CTA | Reader ≠ author |
| Comments | Always |
| Related Posts | ≥3 blogs in same category |
| Music Box | Author has equipped playlist |
| Canvas Preview | Author has canvas |
| Daily Quest | Reader is logged in |
| Blind Challenge | Always |
| Skill Tree | Blog's category has skill tree |
| Trending | Always (top 3 trending blogs) |
| Tags | Blog has hashtags |

---

## 7. Data Flow

### API Endpoints Needed (new or updated)

| Endpoint | Purpose | New/Existing |
|----------|---------|-------------|
| `GET /api/blog/{id}` | Blog detail + author info + stats | Existing (extend) |
| `GET /api/blog/{id}/related` | Related posts (R106) | New |
| `GET /api/status/user/{userId}` | Author's recent status (R101) | New |
| `GET /api/story/user/{userId}` | Author's active story (R301) | New |
| `GET /api/playlist/user/{userId}` | Author's playlist (R303) | New |
| `GET /api/canvas/user/{userId}` | Author's canvas (R302) | New |
| `GET /api/user/{userId}/badges` | Author's badges (R105) | New |
| `GET /api/user/{userId}/reputation` | Rep + streak (R107, R108) | New |
| `GET /api/quests/my` | Reader's daily quests (R104) | New |
| `GET /api/blind/today` | Today's challenge (R110) | New |
| `GET /api/skill-trees/category/{id}` | Skill tree for category (R103) | New |

### Data Fetch Strategy

```typescript
// On mount — fetch blog detail
const blog = await blogApi.getById(id)

// Parallel fetch — everything else
const [
  author,
  status,
  story,
  playlist,
  canvas,
  badges,
  reputation,
  related,
  quests,
  blindChallenge,
  skillTree,
] = await Promise.all([
  profileApi.getPublic(blog.authorId),
  statusApi.getByUser(blog.authorId),
  storyApi.getByUser(blog.authorId),
  playlistApi.getByUser(blog.authorId),
  canvasApi.getByUser(blog.authorId),
  badgeApi.getByUser(blog.authorId),
  reputationApi.getByUser(blog.authorId),
  blogApi.getRelated(blog.id),
  isLoggedIn ? questApi.getMyQuests() : null,
  blindApi.getToday(),
  skillTreeApi.getByCategory(blog.categoryId),
])
```

---

## 8. Component Tree (Vue)

```
Blog.vue
├── BlogSidebar.vue (left)
│   ├── AuthorCard.vue (expanded)
│   │   ├── AuthorCover.vue
│   │   ├── AuthorAvatar.vue
│   │   ├── AuthorStats.vue
│   │   ├── ExpBar.vue (R107)
│   │   ├── StreakBadge.vue (R108)
│   │   ├── BadgeRow.vue (R105)
│   │   ├── AuthorBio.vue (collapsible)
│   │   └── FollowButton.vue
│   ├── AuthorStatus.vue (R101)
│   ├── AuthorStory.vue (R301)
│   ├── TableOfContents.vue
│   └── ReadingProgress.vue
│
├── BlogContent.vue (center)
│   ├── BlogHeader.vue
│   ├── BlogCover.vue
│   ├── BlogBody.vue (markdown render)
│   ├── ReactionBar.vue
│   ├── AuthorCta.vue
│   ├── CommentSection.vue
│   └── RelatedPosts.vue (R106)
│
└── BlogDiscoverySidebar.vue (right)
    ├── MusicBox.vue (R303)
    ├── CanvasPreview.vue (R302)
    ├── DailyQuestPanel.vue (R104)
    ├── BlindChallengeCard.vue (R110)
    ├── SkillTreeCard.vue (R103)
    ├── TrendingMini.vue
    └── BlogTags.vue
```

---

## 9. New/Major Components Detail

### 9.1 ExpBar.vue

```
Props: { level: number, currentExp: number, nextLevelExp: number }
Template:
  <div class="exp-bar">
    <span class="exp-level">Lv.{{ level }}</span>
    <div class="exp-track">
      <div class="exp-fill" :style="{ width: pct + '%' }"></div>
    </div>
    <span class="exp-text">{{ currentExp }}/{{ nextLevelExp }}</span>
  </div>
```

### 9.2 MusicBox.vue

Props: { playlist: PlaylistDTO }
- Uses aplayer library (already in dependencies)
- Mini player with play/pause, prev/next, volume, playlist toggle
- Auto-play disabled (user must click play)
- Remember play state across page navigation (Pinia store)

### 9.3 CanvasPreview.vue

Props: { canvas: CanvasDTO }
- Renders canvas image in 200×200 box
- Click → medium-zoom lightbox
- Placeholder if no canvas: "🎨 Tác giả chưa có canvas"

### 9.4 DailyQuestPanel.vue

Props: { quests: UserQuestDTO[] }
- 3 progress bars with labels
- "Claim" button on completed quests
- Animate progress update (CSS transition)

### 9.5 BlindChallengeCard.vue

Props: { challenge: BlindChallengeDTO, guess: BlindGuessDTO | null }
- Show hint before 20:00, reveal after
- If not guessed: show options list + guess button
- If guessed: show result (correct/wrong)
- Countdown timer to 20:00

---

## 10. Micro-interactions

| Interaction | Effect |
|-------------|--------|
| Reaction click | Scale 1.2 → 1.0, count increment animation |
| Bookmark toggle | Star icon fill/unfill with color transition |
| Follow button | Text change "Follow" → "✓ Following" with slide |
| Progress bar | Width transition 0.3s ease |
| TOC active | Left border + background color transition |
| Related post hover | Card lift + shadow |
| Quest progress | Bar fill animation on update |
| Guess (Blind) | Card flip animation on submit |

---

## 11. Error & Loading States

| Component | Loading | Empty | Error |
|-----------|---------|-------|-------|
| AuthorCard | Skeleton (avatar circle + lines) | N/A | Avatar fallback + "Không thể tải" |
| ExpBar | Skeleton bar | Show level 1 | Hide |
| BadgeRow | 4 shimmer circles | Hide | Hide |
| MusicBox | Skeleton player | Hide | Hide |
| CanvasPreview | Skeleton rectangle | Hide | Hide |
| DailyQuest | Skeleton bars | "Không có nhiệm vụ" | "Lỗi tải" |
| BlindChallenge | Skeleton card | "Hôm nay không có" | Hide |
| SkillTree | Skeleton bars | "Chưa có kỹ năng" | Hide |
| RelatedPosts | 3 skeleton cards | Hide | Hide |

---

## 12. Mobile Adaptations

### 768–1199px (Tablet)
- Right sidebar hidden
- Quest/Blind Challenge content moves to left sidebar or inline after comments
- Music Box → floating mini player (bottom-right, draggable)
- Canvas Preview → remove (too small on tablet)

### <768px (Mobile)
- Both sidebars hidden
- Floating bottom bar: reaction buttons (compact)
- Floating TOC button (bottom-left corner)
- Music Box → mini notification bar (collapsible)
- Author info inline in blog header (already works)
- Comments full-width

---

## 13. Design Tokens (additions to existing system)

```scss
// New tokens for blog-view
--sidebar-left-width: 280px;
--sidebar-right-width: 280px;
--blog-max-width: 720px; // max width of blog content in center
--exp-bar-height: 6px;
--story-ring-size: 48px;
--music-player-height: 200px;
```

---

## 14. Implementation Order

1. Refactor Blog.vue → 3-column grid layout
2. Split BlogSidebar.vue → AuthorCard components
3. Create new left sidebar components (AuthorStatus, AuthorStory)
4. Create right sidebar components (MusicBox, CanvasPreview, DailyQuestPanel, BlindChallengeCard, SkillTreeCard, TrendingMini, BlogTags)
5. Update data fetching layer (api/*.js)
6. Add micro-interactions
7. Mobile responsive
8. Test + QA

---

## 15. Decisions

- **MusicBox auto-play**: NO — user must click play. Auto-play is intrusive.
- **DailyQuest scope**: Show ALL daily quests for the reader (not filtered by category).
- **Canvas preview**: Static image. Animated canvas is Phase 2.
- **Guest users**: Right sidebar shows public content (MusicBox, Canvas, BlindChallenge teaser, Trending, Tags). Quest + SkillTree hidden. Guest sees a narrower right sidebar.
