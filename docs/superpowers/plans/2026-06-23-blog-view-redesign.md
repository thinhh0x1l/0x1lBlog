# Blog-View Redesign Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task.

**Goal:** Redesign blog-view page (`/blog/:id`) from 2-column to 3-column layout, integrating all P1 gamification features.

**Architecture:** Keep current mock-based data layer. Add mock data + API calls for new features (R101–R110, R301–R303). Refactor Blog.vue into child components. No backend changes — pure frontend work.

**Tech Stack:** Vue 3 (Composition API, `<script setup>`), Element Plus, SCSS design tokens, marked + DOMPurify, aplayer, Pinia

---

## File Structure

### New files (20)
| File | Responsibility |
|------|---------------|
| `src/api/status.js` | Status API calls |
| `src/api/story.js` | Story API calls |
| `src/api/playlist.js` | Playlist API calls |
| `src/api/canvas.js` | Canvas API calls |
| `src/api/reputation.js` | Reputation API calls |
| `src/api/quest.js` | Quest API calls |
| `src/api/blind.js` | Blind Challenge API calls |
| `src/api/skill.js` | Skill Tree API calls |
| `src/components/blog/ExpBar.vue` | EXP bar display |
| `src/components/blog/BadgeRow.vue` | Mini badge icons row |
| `src/components/blog/AuthorStatus.vue` | Author's recent status |
| `src/components/blog/AuthorStory.vue` | Author's active story |
| `src/components/blog/BlogDiscoverySidebar.vue` | Right sidebar container |
| `src/components/blog/MusicBox.vue` | Mini music player |
| `src/components/blog/CanvasPreview.vue` | Canvas preview |
| `src/components/blog/DailyQuestPanel.vue` | Daily quest progress |
| `src/components/blog/BlindChallengeCard.vue` | Blind challenge teaser |
| `src/components/blog/SkillTreeCard.vue` | Category skill tree |
| `src/components/blog/RelatedPosts.vue` | Related posts grid |
| `src/components/blog/AuthorCta.vue` | Author call-to-action |

### Modified files (5)
| File | Change |
|------|--------|
| `src/data/dummy.js` | Add 10 new mock datasets |
| `src/api/index.js` | Import + re-export new API modules, add to existing exports |
| `src/view/blog/Blog.vue` | 3-column grid, import new components |
| `src/components/blog/BlogSidebar.vue` | Add EXP bar, badges, streak (or delegate to children) |
| `src/router/index.js` | No changes needed |

---

## Task 1: Add mock data for new features

**File:** `src/data/dummy.js`

**Interfaces:** Produces exports consumed by all API modules.

Add at end of file (before existing `useCounter` to not break line numbers), or better: at the very end after `seriesSubscribers`:

```javascript
// ─── R101: Statuses ───────────────────────────────────────────────
export const statuses = Array.from({length: 200}, (_, i) => ({
  id: i + 1,
  userId: users[i % 100].id,
  content: ['Đang học Rust...', 'Hôm nay trời đẹp quá!', 'Vừa release feature mới 🚀', 'Có ai rảnh review PR giúp mình không?', 'Đang đọc sách "Clean Code"'][i % 5],
  type: ['TEXT', 'POLL', 'THREAD'][i % 3],
  pollOptions: i % 3 === 1 ? JSON.stringify([
    {id: 1, text: 'Rust', votes: 12},
    {id: 2, text: 'Go', votes: 8},
    {id: 3, text: 'TypeScript', votes: 5},
  ]) : null,
  totalVotes: i % 3 === 1 ? 25 : 0,
  visibility: 'PUBLIC',
  createdAt: new Date(Date.now() - i * 3600000).toISOString(),
  updatedAt: new Date(Date.now() - i * 3600000).toISOString(),
}))

// ─── R301: Stories ────────────────────────────────────────────────
export const stories = Array.from({length: 50}, (_, i) => ({
  id: i + 1,
  userId: users[i % 50].id,
  mediaUrl: `https://picsum.photos/seed/story${i}/400/600`,
  mediaType: 'IMAGE',
  caption: ['Đang code đây...', 'Cà phê sáng ☕', 'Bug mới, ngày mới!', 'Mặt mộc đi làm'][i % 4],
  expiresAt: new Date(Date.now() + (24 - i) * 3600000).toISOString(),
  viewCount: Math.floor(Math.random() * 100),
  createdAt: new Date(Date.now() - i * 7200000).toISOString(),
}))

// ─── R302: Canvases ───────────────────────────────────────────────
export const canvases = Array.from({length: 30}, (_, i) => ({
  id: i + 1,
  userId: users[i % 30].id,
  type: ['PROFILE', 'COMMUNITY'][i % 2],
  canvasData: JSON.stringify({elements: [{type: 'rectangle', x: 10, y: 10, w: 180, h: 180, color: '#0ea5e9'}]}),
  thumbnailUrl: `https://picsum.photos/seed/canvas${i}/200/200`,
  isEquipped: i < 10,
  createdAt: new Date(Date.now() - i * 86400000).toISOString(),
}))

// ─── R303: Playlists ──────────────────────────────────────────────
export const playlists = Array.from({length: 20}, (_, i) => ({
  id: i + 1,
  ownerId: users[i % 20].id,
  name: ['Code Flow', 'Chill Vibes', 'Focus Mode', 'Late Night Coding'][i % 4],
  isActive: i < 5,
  createdAt: new Date(Date.now() - i * 86400000).toISOString(),
}))

export const playlistSongs = Array.from({length: 100}, (_, i) => ({
  id: i + 1,
  playlistId: (i % 20) + 1,
  title: ['Song A', 'Song B', 'Song C', 'Song D', 'Song E'][i % 5],
  artist: ['Artist X', 'Artist Y', 'Artist Z'][i % 3],
  url: 'https://www.soundhelix.com/examples/mp3/SoundHelix-Song-1.mp3',
  coverUrl: `https://picsum.photos/seed/song${i}/100/100`,
  sortOrder: i % 5,
  duration: 180 + Math.floor(Math.random() * 120),
}))

// ─── R104: Quests ─────────────────────────────────────────────────
export const quests = [
  {id: 1, type: 'DAILY', title: 'Viết 1 blog', description: 'Viết một bài blog mới hôm nay', conditions: JSON.stringify({action: 'WRITE_BLOG', count: 1}), rewards: JSON.stringify({exp: 50, coins: 30}), isActive: true, createdAt: new Date().toISOString()},
  {id: 2, type: 'DAILY', title: 'Đọc 3 blogs', description: 'Đọc 3 bài viết của người khác', conditions: JSON.stringify({action: 'READ_BLOG', count: 3}), rewards: JSON.stringify({exp: 30, coins: 20}), isActive: true, createdAt: new Date().toISOString()},
  {id: 3, type: 'DAILY', title: 'Reaction 5 bài', description: 'Thả reaction 5 bài viết', conditions: JSON.stringify({action: 'REACT', count: 5}), rewards: JSON.stringify({exp: 40, coins: 25}), isActive: true, createdAt: new Date().toISOString()},
  {id: 4, type: 'DAILY', title: 'Viết 1 status', description: 'Đăng một status mới', conditions: JSON.stringify({action: 'WRITE_STATUS', count: 1}), rewards: JSON.stringify({exp: 20, coins: 15}), isActive: true, createdAt: new Date().toISOString()},
  {id: 5, type: 'WEEKLY', title: 'Viết 3 blogs', description: 'Viết 3 bài trong tuần', conditions: JSON.stringify({action: 'WRITE_BLOG', count: 3}), rewards: JSON.stringify({exp: 200, gems: 50}), isActive: true, createdAt: new Date().toISOString()},
]

export const userQuests = Array.from({length: 100}, (_, i) => ({
  id: i + 1,
  userId: users[i % 10].id,
  questId: (i % 5) + 1,
  progress: Math.floor(Math.random() * 3),
  target: [(i % 5) + 1, 3, 5, 1, 3][i % 5],
  status: ['IN_PROGRESS', 'COMPLETED', 'CLAIMED'][i % 3],
  claimedAt: i % 3 === 2 ? new Date().toISOString() : null,
  expiresAt: new Date(Date.now() + 86400000).toISOString(),
  createdAt: new Date().toISOString(),
}))

// ─── R110: Blind Challenge ────────────────────────────────────────
export const blindChallenges = [
  {id: 1, date: new Date().toISOString().split('T')[0], topicId: 1, topicHint: 'Một ngôn ngữ lập trình', options: JSON.stringify([{id: 1, name: 'Rust'}, {id: 2, name: 'Go'}, {id: 3, name: 'TypeScript'}, {id: 4, name: 'Python'}, {id: 5, name: 'Java'}, {id: 6, name: 'C++'}, {id: 7, name: 'Kotlin'}, {id: 8, name: 'Swift'}, {id: 9, name: 'Ruby'}, {id: 10, name: 'PHP'}]), revealed: false, createdAt: new Date().toISOString()},
]

export const blindChallengeGuesses = [
  {id: 1, challengeId: 1, userId: 1, guessedTopicId: 1, isCorrect: null, createdAt: new Date().toISOString()},
]

// ─── R103: Skill Trees ────────────────────────────────────────────
export const skillTrees = [
  {id: 1, categoryId: 1, name: 'Upload ảnh 10MB', description: 'Mở khóa khả năng upload ảnh dung lượng lớn', perkType: 'UPLOAD_LIMIT', perkValue: JSON.stringify({maxSize: 10}), pointsRequired: 100, sortOrder: 1, createdAt: new Date().toISOString()},
  {id: 2, categoryId: 1, name: 'Scheduled Post', description: 'Lên lịch đăng bài', perkType: 'SCHEDULE', perkValue: JSON.stringify({}), pointsRequired: 300, sortOrder: 2, createdAt: new Date().toISOString()},
  {id: 3, categoryId: 1, name: 'Analytics Pro', description: 'Xem thống kê chi tiết', perkType: 'ANALYTICS', perkValue: JSON.stringify({}), pointsRequired: 500, sortOrder: 3, createdAt: new Date().toISOString()},
  {id: 4, categoryId: 1, name: 'Custom Domain', description: 'Sử dụng domain riêng', perkType: 'CUSTOM_DOMAIN', perkValue: JSON.stringify({}), pointsRequired: 1000, sortOrder: 4, createdAt: new Date().toISOString()},
]

export const userSkillProgress = Array.from({length: 50}, (_, i) => ({
  id: i + 1,
  userId: users[i % 20].id,
  categoryId: (i % 5) + 1,
  totalPoints: Math.floor(Math.random() * 500),
}))

export const userSkillUnlocks = Array.from({length: 30}, (_, i) => ({
  id: i + 1,
  userId: users[i % 15].id,
  skillId: (i % 4) + 1,
  unlockedAt: new Date(Date.now() - i * 86400000).toISOString(),
}))
```

---

## Task 2: Create new API modules

**Files:** 8 new API files + modify `src/api/index.js`

### `src/api/status.js`
```javascript
import { statuses, users } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const statusApi = {
  getByUser: async (userId) => {
    await delay()
    return { data: statuses.filter(s => s.userId === Number(userId)).slice(0, 3) }
  },
}
```

### `src/api/story.js`
```javascript
import { stories } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const storyApi = {
  getByUser: async (userId) => {
    await delay()
    const now = new Date()
    return { data: stories.filter(s => s.userId === Number(userId) && new Date(s.expiresAt) > now).slice(0, 5) }
  },
}
```

### `src/api/playlist.js`
```javascript
import { playlists, playlistSongs } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const playlistApi = {
  getByUser: async (userId) => {
    await delay()
    const playlist = playlists.find(p => p.ownerId === Number(userId) && p.isActive)
    if (!playlist) return { data: null }
    const songs = playlistSongs.filter(s => s.playlistId === playlist.id).sort((a, b) => a.sortOrder - b.sortOrder)
    return { data: { ...playlist, songs } }
  },
}
```

### `src/api/canvas.js`
```javascript
import { canvases } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const canvasApi = {
  getByUser: async (userId) => {
    await delay()
    const canvas = canvases.find(c => c.userId === Number(userId) && c.isEquipped)
    return { data: canvas || null }
  },
}
```

### `src/api/reputation.js`
```javascript
import { users, userExpLog } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const reputationApi = {
  getByUser: async (userId) => {
    await delay()
    const u = users.find(u => u.id === Number(userId))
    if (!u) return { data: null }
    const totalExp = userExpLog.filter(l => l.userId === Number(userId)).reduce((s, l) => s + l.points, 0)
    const level = Math.floor(totalExp / 1000) + 1
    return { data: { userId: u.id, level, currentExp: totalExp % 1000, nextLevelExp: 1000, totalExp } }
  },
}
```

### `src/api/quest.js`
```javascript
import { quests, userQuests, users } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const questApi = {
  getActiveQuests: async () => {
    await delay()
    return { data: quests.filter(q => q.isActive) }
  },
  getMyQuests: async (userId) => {
    await delay()
    return { data: userQuests.filter(q => q.userId === Number(userId)) }
  },
  claim: async (userQuestId) => {
    await delay()
    return { data: { success: true } }
  },
}
```

### `src/api/blind.js`
```javascript
import { blindChallenges, blindChallengeGuesses, categories } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const blindApi = {
  getToday: async () => {
    await delay()
    const today = new Date().toISOString().split('T')[0]
    const challenge = blindChallenges.find(c => c.date === today) || blindChallenges[0]
    if (!challenge) return { data: null }
    const options = JSON.parse(challenge.options || '[]').map(o => ({
      ...o, name: categories.find(c => c.id === o.id)?.name || o.name
    }))
    return { data: { ...challenge, options } }
  },
  getMyGuess: async (userId) => {
    await delay()
    return { data: blindChallengeGuesses.find(g => g.userId === Number(userId)) || null }
  },
  submitGuess: async (challengeId, guessedTopicId) => {
    await delay()
    return { data: { id: 999, challengeId, guessedTopicId, isCorrect: null } }
  },
}
```

### `src/api/skill.js`
```javascript
import { skillTrees, userSkillProgress, userSkillUnlocks, categories } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const skillApi = {
  getByCategory: async (categoryId) => {
    await delay()
    return { data: skillTrees.filter(s => s.categoryId === Number(categoryId)).sort((a, b) => a.sortOrder - b.sortOrder) }
  },
  getMyProgress: async (userId, categoryId) => {
    await delay()
    const progress = userSkillProgress.find(p => p.userId === Number(userId) && p.categoryId === Number(categoryId))
    const unlocks = userSkillUnlocks.filter(u => u.userId === Number(userId))
    const unlockedSkillIds = unlocks.map(u => u.skillId)
    return { data: { progress: progress || { totalPoints: 0 }, unlockedSkillIds } }
  },
}
```

### modify `src/api/index.js`
Add imports at top:
```javascript
import { statusApi } from './status'
import { storyApi } from './story'
import { playlistApi } from './playlist'
import { canvasApi } from './canvas'
import { reputationApi } from './reputation'
import { questApi } from './quest'
import { blindApi } from './blind'
import { skillApi } from './skill'
```

Add to export:
```javascript
export { statusApi, storyApi, playlistApi, canvasApi, reputationApi, questApi, blindApi, skillApi }
```

---

## Task 3: EXP Bar component

**Create:** `src/components/blog/ExpBar.vue`

```vue
<template>
  <div class="exp-bar">
    <div class="exp-bar-header">
      <span class="exp-level">Lv.{{ level }}</span>
      <span class="exp-rep" v-if="rep !== undefined">★ {{ rep }}</span>
    </div>
    <div class="exp-track">
      <div class="exp-fill" :style="{ width: pct + '%' }"></div>
    </div>
    <span class="exp-text">{{ currentExp }}/{{ nextLevelExp }} EXP</span>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({
  level: { type: Number, default: 1 },
  currentExp: { type: Number, default: 0 },
  nextLevelExp: { type: Number, default: 1000 },
  rep: { type: Number, default: undefined },
})
const pct = computed(() => Math.min(100, Math.round((props.currentExp / props.nextLevelExp) * 100)))
</script>
<style scoped lang="scss">
.exp-bar { padding: 8px 0; }
.exp-bar-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 4px; }
.exp-level { font-size: 0.85rem; font-weight: 700; color: var(--primary); }
.exp-rep { font-size: 0.75rem; color: #f59e0b; }
.exp-track { height: 6px; background: var(--bg-secondary); border-radius: 99px; overflow: hidden; margin-bottom: 2px; }
.exp-fill { height: 100%; border-radius: 99px; background: linear-gradient(90deg, var(--primary), #8b5cf6); transition: width 0.5s ease; }
.exp-text { font-size: 0.68rem; color: var(--text-muted); }
</style>
```

---

## Task 4: Badge Row component

**Create:** `src/components/blog/BadgeRow.vue`

```vue
<template>
  <div class="badge-row" v-if="badges.length">
    <div v-for="b in badges.slice(0, max)" :key="b.id" class="badge-mini" :title="b.displayName">
      <span class="badge-icon">{{ b.icon || '🏆' }}</span>
    </div>
    <span class="badge-more" v-if="badges.length > max">+{{ badges.length - max }}</span>
  </div>
</template>
<script setup>
defineProps({
  badges: { type: Array, default: () => [] },
  max: { type: Number, default: 4 },
})
</script>
<style scoped lang="scss">
.badge-row { display: flex; align-items: center; gap: 4px; padding: 4px 0; }
.badge-mini { width: 24px; height: 24px; border-radius: 50%; display: flex; align-items: center; justify-content: center; background: var(--bg-secondary); cursor: help; }
.badge-icon { font-size: 0.8rem; }
.badge-more { font-size: 0.68rem; color: var(--text-muted); font-weight: 600; }
</style>
```

---

## Task 5: Author Status component

**Create:** `src/components/blog/AuthorStatus.vue`

Show author's most recent public status. If it's a poll, show mini poll results.

```vue
<template>
  <div class="author-status" v-if="status">
    <div class="status-header">
      <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M12 20h9"/><path d="M16.5 3.5a2.121 2.121 0 0 1 3 3L7 19l-4 1 1-4L16.5 3.5z"/></svg>
      <span>Status</span>
    </div>
    <p class="status-content">{{ status.content }}</p>
    <div class="status-poll" v-if="status.type === 'POLL' && status.pollOptions">
      <div v-for="opt in parsedOptions" :key="opt.id" class="poll-option">
        <div class="poll-bar" :style="{ width: pollPct(opt.votes) + '%' }"></div>
        <span class="poll-text">{{ opt.text }}</span>
        <span class="poll-votes">{{ opt.votes }}</span>
      </div>
    </div>
    <span class="status-time">{{ timeAgo(status.createdAt) }}</span>
  </div>
</template>
<script setup>
import { computed } from 'vue'
import dayjs from 'dayjs'
const props = defineProps({ status: Object })
const parsedOptions = computed(() => {
  if (!props.status?.pollOptions) return []
  try { return JSON.parse(props.status.pollOptions) } catch { return [] }
})
const pollPct = (votes) => {
  const total = parsedOptions.value.reduce((s, o) => s + o.votes, 0)
  return total ? (votes / total) * 100 : 0
}
const timeAgo = (d) => dayjs(d).fromNow()
</script>
<style scoped lang="scss">
.author-status { padding: 12px; background: var(--bg-secondary); border-radius: var(--radius-lg); }
.status-header { display: flex; align-items: center; gap: 6px; font-size: 0.75rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; margin-bottom: 8px; }
.status-header svg { width: 14px; height: 14px; }
.status-content { font-size: 0.85rem; color: var(--text-secondary); line-height: 1.5; margin-bottom: 8px; }
.status-poll { display: flex; flex-direction: column; gap: 4px; }
.poll-option { position: relative; display: flex; align-items: center; gap: 8px; padding: 4px 8px; }
.poll-bar { position: absolute; left: 0; top: 0; height: 100%; background: var(--primary-50); border-radius: var(--radius-sm); transition: width 0.5s ease; }
.poll-text { position: relative; font-size: 0.78rem; flex: 1; }
.poll-votes { position: relative; font-size: 0.7rem; color: var(--text-muted); }
.status-time { font-size: 0.7rem; color: var(--text-muted); }
</style>
```

---

## Task 6: Author Story component

**Create:** `src/components/blog/AuthorStory.vue`

Show horizontal story rings if author has active stories.

```vue
<template>
  <div class="author-story" v-if="stories.length">
    <div class="story-header">
      <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="2"/><path d="M2 12h4M18 12h4M12 2v4M12 18v4"/></svg>
      <span>Story</span>
      <span class="story-expiry">· {{ timeLeft }} còn lại</span>
    </div>
    <div class="story-rings">
      <div v-for="s in stories" :key="s.id" class="story-ring" @click="$emit('viewStory', s)">
        <div class="ring-border">
          <img v-if="s.mediaUrl" :src="s.mediaUrl" class="ring-thumb" />
          <div v-else class="ring-placeholder">📸</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
import dayjs from 'dayjs'
const props = defineProps({ stories: { type: Array, default: () => [] } })
defineEmits(['viewStory'])
const timeLeft = computed(() => {
  if (!props.stories.length) return ''
  const expires = dayjs(props.stories[0].expiresAt)
  const hours = expires.diff(dayjs(), 'hour')
  return hours > 0 ? `${hours}h` : 'Sắp hết hạn'
})
</script>
<style scoped lang="scss">
.author-story { padding: 12px; background: var(--bg-secondary); border-radius: var(--radius-lg); }
.story-header { display: flex; align-items: center; gap: 6px; font-size: 0.75rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; margin-bottom: 10px; }
.story-header svg { width: 14px; height: 14px; }
.story-expiry { text-transform: none; font-weight: 400; color: var(--accent); }
.story-rings { display: flex; gap: 8px; overflow-x: auto; padding-bottom: 4px; }
.story-ring { cursor: pointer; flex-shrink: 0; }
.ring-border { width: 48px; height: 48px; border-radius: 50%; padding: 2px; background: linear-gradient(135deg, #f97316, #ec4899, #8b5cf6); display: flex; align-items: center; justify-content: center; }
.ring-thumb { width: 42px; height: 42px; border-radius: 50%; object-fit: cover; border: 2px solid var(--surface); }
.ring-placeholder { width: 42px; height: 42px; border-radius: 50%; background: var(--surface); display: flex; align-items: center; justify-content: center; font-size: 1.2rem; }
</style>
```

---

## Task 7: Update BlogSidebar

**Modify:** `src/components/blog/BlogSidebar.vue`

Add new sections to the sidebar template (after author stats, before TOC):
- EXP bar (import ExpBar)
- Streak + rep row
- Badge row (import BadgeRow)
- AuthorStatus (import AuthorStatus) — conditionally
- AuthorStory (import AuthorStory) — conditionally

```vue
<template>
  <aside class="blog-sidebar">
    <!-- Author Card -->
    <div class="sidebar-card author-card">
      <div class="author-cover" :style="{ background: `linear-gradient(135deg, ${authorColors[0]}, ${authorColors[1]})` }"></div>
      <div class="author-content">
        <!-- Avatar + Name (keep) -->
        <router-link :to="`/profile/${author?.id}`" class="avatar-link">
          <img v-if="author?.avatarUrl" :src="author.avatarUrl" class="author-avatar" />
          <div v-else class="author-avatar-placeholder">{{ author?.displayName?.charAt(0) || 'U' }}</div>
        </router-link>
        <h3 class="author-name">{{ author?.displayName }}</h3>
        <span class="author-role" v-if="author?.isCreator">Creator</span>

        <!-- Stats (keep) -->
        <div class="author-stats">
          <div class="stat"><span class="stat-value">{{ author?.blogCount || 0 }}</span><span class="stat-label">Bài viết</span></div>
          <div class="stat"><span class="stat-value">{{ formatCount(author?.followerCount || 0) }}</span><span class="stat-label">Theo dõi</span></div>
          <div class="stat"><span class="stat-value">Lv.{{ reputation?.level || 1 }}</span><span class="stat-label">Cấp độ</span></div>
        </div>

        <!-- EXP Bar (NEW) -->
        <ExpBar v-if="reputation" :level="reputation.level" :current-exp="reputation.currentExp" :next-level-exp="reputation.nextLevelExp" :rep="author?.repScore" />

        <!-- Streak + Rep row (NEW) -->
        <div class="streak-row" v-if="streak">
          <span class="streak-item">🔥 <strong>{{ streak }}</strong> ngày</span>
          <span class="streak-item" v-if="author?.repScore">★ <strong>{{ author.repScore }}</strong> Rep</span>
        </div>

        <!-- Badge Row (NEW) -->
        <BadgeRow v-if="badges.length" :badges="badges" />

        <!-- Bio (keep collapsible) -->
        <transition name="expand">
          <div class="author-expanded" v-if="showDetail">
            <div class="info-row" v-if="author?.bio"><span class="info-label">Giới thiệu</span><span class="info-value">{{ author.bio }}</span></div>
            <div class="info-row" v-if="author?.website"><span class="info-label">Website</span><a :href="author.website" target="_blank" class="info-link">{{ author.website }}</a></div>
            <div class="info-row" v-if="author?.location"><span class="info-label">Địa điểm</span><span class="info-value">{{ author.location }}</span></div>
            <div class="info-row"><span class="info-label">Tham gia</span><span class="info-value">{{ formatDate(author?.createdAt) }}</span></div>
          </div>
        </transition>
        <button class="toggle-btn" @click="showDetail = !showDetail">{{ showDetail ? 'Thu gọn' : 'Xem thêm' }} <svg :class="{ rotated: showDetail }" width="12" height="12" viewBox="0 0 24 24"><polyline points="6 9 12 15 18 9"/></svg></button>

        <!-- Follow (keep) -->
        <button v-if="author?.id !== currentUserId" :class="['follow-btn', { following: isFollowing }]" @click="$emit('toggleFollow')">{{ isFollowing ? '✓ Đang follow' : 'Follow' }}</button>
      </div>
    </div>

    <!-- Author Status (NEW) -->
    <AuthorStatus v-if="authorStatus" :status="authorStatus" />

    <!-- Author Story (NEW) -->
    <AuthorStory v-if="authorStories.length" :stories="authorStories" @viewStory="handleViewStory" />

    <!-- TOC (keep) -->
    <div class="sidebar-card toc-card" v-if="headings.length">
      <div class="toc-header"><svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 6h16M4 12h12M4 18h8"/></svg><h3>Mục lục</h3></div>
      <nav class="toc-nav">
        <a v-for="(h, idx) in headings" :key="idx" :href="`#${h.id}`" :class="['toc-item', `level-${h.level}`, { active: activeHeading === h.id }]" @click.prevent="scrollToHeading(h.id)">
          <span class="toc-dot"></span><span class="toc-text">{{ h.text }}</span>
        </a>
      </nav>
    </div>

    <!-- Reading Progress (keep) -->
    <div class="sidebar-card progress-card">
      <div class="progress-info"><svg width="14" height="14" viewBox="0 0 24 24"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg><span>Tiến độ đọc</span><span class="progress-pct">{{ readProgress }}%</span></div>
      <div class="progress-track"><div class="progress-fill" :style="{ width: readProgress + '%', background: progressColor }"></div></div>
    </div>
  </aside>
</template>
```

Script changes:
- Import ExpBar, BadgeRow, AuthorStatus, AuthorStory
- Add props: reputation, streak, badges, authorStatus, authorStories
- Remove hardcoded level/stats from template (use props)
- Add handleViewStory emit

Style changes: add `.streak-row` styles.

---

## Task 8: BlogDiscoverySidebar (right sidebar)

**Create:** `src/components/blog/BlogDiscoverySidebar.vue`

```vue
<template>
  <aside class="discovery-sidebar">
    <MusicBox v-if="playlist" :playlist="playlist" />
    <CanvasPreview v-if="canvas" :canvas="canvas" />
    <DailyQuestPanel v-if="isLoggedIn && quests.length" :quests="quests" @claim="handleClaim" />
    <BlindChallengeCard v-if="blindChallenge" :challenge="blindChallenge" :my-guess="myGuess" @guess="handleGuess" />
    <SkillTreeCard v-if="skillTrees.length" :trees="skillTrees" :my-progress="skillProgress" :is-logged-in="isLoggedIn" />
    <TrendingMini :items="trending" />
    <BlogTags v-if="tags.length" :tags="tags" />
  </aside>
</template>
```

Props: playlist, canvas, quests, blindChallenge, myGuess, skillTrees, skillProgress, trending, tags, isLoggedIn
Emits: claim, guess, viewCanvas, viewTrending

---

## Task 9: Right sidebar child components

Create all 7 child components in `src/components/blog/`:

### MusicBox.vue
Mini aplayer wrapper. Shows playlist name, current song, play/pause/prev/next controls, volume slider. Uses aplayer npm package (already in deps). Auto-play = false.

### CanvasPreview.vue
Displays canvas thumbnail (200×200). Click → medium-zoom. If no canvas, show placeholder.

### DailyQuestPanel.vue
Lists active daily quests with progress bars. Shows "Nhận thưởng" button on completed. Claim button disabled on already-claimed.

### BlindChallengeCard.vue
Before 20:00: show hint + 10 options as clickable pills + "Đoán ngay" button. After 20:00: show result. Countdown timer to reveal time.

### SkillTreeCard.vue
Shows skill tree nodes for the blog's category as progress bars. If user logged in: show user's progress. If not: show public nodes without progress.

### TrendingMini.vue
Compact top 3 trending blogs. Just title + views. Clickable.

### BlogTags.vue
Blog's hashtags as pills. Clickable → `/tag/:id`.

---

## Task 10: RelatedPosts component

**Create:** `src/components/blog/RelatedPosts.vue`

Grid of 3 related blog cards (same category). Each card: thumbnail, title, date, read time. Empty state when no related posts.

---

## Task 11: AuthorCta component

**Create:** `src/components/blog/AuthorCta.vue`

Simple card: avatar + name + "Nếu bạn thấy bài viết hay..." + [Follow] [Xem thêm] buttons.

---

## Task 12: Refactor Blog.vue to 3-column layout

**Modify:** `src/view/blog/Blog.vue`

Key changes:
1. Template wrapper: `<div class="blog-layout">` → 3-column grid
2. Import and use BlogDiscoverySidebar
3. Import and use RelatedPosts, AuthorCta
4. Data layer: fetch all new data in parallel with existing data
5. Pass props to BlogSidebar and BlogDiscoverySidebar

```vue
<template>
  <div class="blog-layout" v-if="blog">
    <BlogSidebar
      :author="author"
      :content="blog.content"
      :isFollowing="isFollowing"
      :reputation="reputation"
      :streak="streak"
      :badges="authorBadges"
      :authorStatus="authorStatus"
      :authorStories="authorStories"
      @toggleFollow="toggleFollow"
      @viewStory="viewStory"
    />

    <div class="blog-main">
      <!-- Existing content -->
      <div class="blog-header">...</div>
      <div class="blog-cover" v-if="blog.coverImage">...</div>
      <div class="blog-content typo" v-html="renderedContent" ref="contentRef"></div>
      <div class="reaction-bar">...</div>

      <!-- Author CTA (NEW) -->
      <AuthorCta v-if="author && currentUserId !== author.id" :author="author" :is-following="isFollowing" @follow="toggleFollow" />

      <!-- Comments (keep) -->
      <div id="comments" class="comments-section">...</div>

      <!-- Related Posts (NEW) -->
      <RelatedPosts v-if="relatedPosts.length" :posts="relatedPosts" />
    </div>

    <BlogDiscoverySidebar
      v-if="!isMobile"
      :playlist="authorPlaylist"
      :canvas="authorCanvas"
      :quests="myQuests"
      :blindChallenge="todayBlind"
      :myGuess="myBlindGuess"
      :skillTrees="categorySkills"
      :skillProgress="mySkillProgress"
      :trending="trendingPosts"
      :tags="blogHashtags"
      :isLoggedIn="isLoggedIn"
      @claim="claimQuest"
      @guess="submitBlindGuess"
    />
  </div>
  <el-skeleton :rows="10" animated v-else-if="loading" />
  <el-empty v-else description="Không tìm thấy bài viết" />
</template>
```

Script changes:
```javascript
import { ref, computed, onMounted, onUnmounted } from 'vue'
import BlogSidebar from '@/components/blog/BlogSidebar.vue'
import AuthorCta from '@/components/blog/AuthorCta.vue'
import RelatedPosts from '@/components/blog/RelatedPosts.vue'
import BlogDiscoverySidebar from '@/components/blog/BlogDiscoverySidebar.vue'

// New data refs
const reputation = ref(null)
const streak = ref(0)
const authorBadges = ref([])
const authorStatus = ref(null)
const authorStories = ref([])
const authorPlaylist = ref(null)
const authorCanvas = ref(null)
const myQuests = ref([])
const todayBlind = ref(null)
const myBlindGuess = ref(null)
const categorySkills = ref([])
const mySkillProgress = ref(null)
const trendingPosts = ref([])
const blogHashtags = ref([])
const relatedPosts = ref([])
const isMobile = ref(window.innerWidth < 768)

// Responsive listener
const checkMobile = () => isMobile.value = window.innerWidth < 768
onMounted(() => window.addEventListener('resize', checkMobile))
onUnmounted(() => window.removeEventListener('resize', checkMobile))

// Fetch all data
onMounted(async () => {
  loading.value = true
  try {
    const blogRes = await blogApi.getById(route.params.id)
    blog.value = blogRes.data
    if (blog.value) {
      const authorId = blog.value.authorId
      const [repRes, badgeRes, statusRes, storyRes, playlistRes, canvasRes,
             questRes, blindRes, skillRes, relatedRes, trendRes, tagRes] = await Promise.all([
        reputationApi.getByUser(authorId),
        badgeApi.getByUser(authorId),
        statusApi.getByUser(authorId),
        storyApi.getByUser(authorId),
        playlistApi.getByUser(authorId),
        canvasApi.getByUser(authorId),
        isLoggedIn.value ? questApi.getMyQuests() : Promise.resolve({ data: [] }),
        blindApi.getToday(),
        skillApi.getByCategory(blog.value.categoryId),
        blogApi.getRelated(blog.value.id || route.params.id),
        blogApi.trending(3),
        hashtagApi.getByBlog(blog.value.id || route.params.id),
      ])
      reputation.value = repRes.data
      authorBadges.value = badgeRes.data || []
      authorStatus.value = (statusRes.data || [])[0]
      authorStories.value = storyRes.data || []
      authorPlaylist.value = playlistRes.data
      authorCanvas.value = canvasRes.data
      myQuests.value = questRes.data || []
      todayBlind.value = blindRes.data
      categorySkills.value = skillRes.data || []
      relatedPosts.value = relatedRes.data || []
      trendingPosts.value = trendRes.data || []
      blogHashtags.value = tagRes.data || []
      // ... existing data fetching
    }
  } catch (e) { console.error(e) }
  finally { loading.value = false }
})
```

---

## Task 13: Mobile responsive

Add responsive CSS to Blog.vue:

```scss
.blog-layout {
  display: grid;
  grid-template-columns: 280px 1fr 280px;
  gap: 24px;
  max-width: 100%;
}

@include respond-to(xl) { // ≥1200px
  .blog-layout { grid-template-columns: 280px 1fr 280px; }
}

@include respond-to(lg) { // 992-1199px
  .blog-layout { grid-template-columns: 280px 1fr; }
  .discovery-sidebar { display: none; }
}

@include respond-to(md) { // 768-991px
  .blog-layout { grid-template-columns: 240px 1fr; }
  .blog-sidebar { width: 240px; }
}

@include respond-to(sm) { // <768px
  .blog-layout { grid-template-columns: 1fr; }
  .blog-sidebar { display: none; }
}
```

---

## Self-Review Checklist
- [ ] Spec coverage: All components from spec have corresponding tasks
- [ ] No placeholders: Every code block has actual content
- [ ] Type consistency: Props, methods, API signatures match across tasks
