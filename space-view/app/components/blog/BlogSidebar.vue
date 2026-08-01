<template>
  <div class="blog-sidebar">
    <div class="sidebar-card author-card">
      <div class="author-cover" :style="{ background: `linear-gradient(135deg, ${authorColors[0]}, ${authorColors[1]})` }"></div>
      <div class="author-content">
        <NuxtLink :to="`/profile/${author?.id}`" class="author-avatar-link">
          <EquippedBorder :border="equippedBorder">
            <img v-if="author?.avatarUrl" :src="author.avatarUrl" class="author-avatar" />
            <div v-else class="author-avatar author-avatar-placeholder">{{ author?.displayName?.charAt(0) || 'U' }}</div>
          </EquippedBorder>
        </NuxtLink>
        <h3 class="author-name">{{ author?.displayName }}</h3>
        <span class="author-role" v-if="author?.isCreator">Creator</span>

        <div class="author-stats">
          <div class="stat"><span class="stat-value">{{ author?.blogCount || 0 }}</span><span class="stat-label">Bài viết</span></div>
          <div class="stat"><span class="stat-value">{{ formatCount(author?.followerCount || 0) }}</span><span class="stat-label">Theo dõi</span></div>
          <div class="stat"><span class="stat-value">Lv.{{ author?.level || 1 }}</span><span class="stat-label">Cấp độ</span></div>
        </div>

        <ExpBar v-if="reputation" :level="reputation.level" :current-exp="reputation.currentExp" :next-level-exp="reputation.nextLevelExp" :rep="author?.repScore" />

        <div class="streak-row" v-if="streak">
          <span class="streak-item">🔥 <strong>{{ streak }}</strong> ngày</span>
          <span class="streak-item" v-if="author?.repScore">★ <strong>{{ author.repScore }}</strong> Rep</span>
        </div>

        <BadgeRow v-if="badges.length" :badges="badges" />

        <transition name="expand">
          <div class="author-expanded" v-if="showDetail">
            <div class="info-row" v-if="author?.bio">
              <span class="info-label">Giới thiệu</span>
              <span class="info-value">{{ author.bio }}</span>
            </div>
            <div class="info-row" v-if="author?.website">
              <span class="info-label">Website</span>
              <a :href="author.website" target="_blank" class="info-link">{{ author.website }}</a>
            </div>
            <div class="info-row" v-if="author?.location">
              <span class="info-label">Địa điểm</span>
              <span class="info-value">📍 {{ author.location }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">Tham gia</span>
              <span class="info-value">{{ formatDate(author?.createdAt) }}</span>
            </div>
            <div class="info-row" v-if="author?.exp">
              <span class="info-label">Kinh nghiệm</span>
              <span class="info-value">{{ formatCount(author.exp) }} EXP</span>
            </div>
          </div>
        </transition>

        <button class="toggle-btn" @click="showDetail = !showDetail">
          {{ showDetail ? 'Thu gọn' : 'Xem thêm' }}
          <svg :class="{ rotated: showDetail }" width="12" height="12" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><polyline points="6 9 12 15 18 9"/></svg>
        </button>

        <AuthorStatus v-if="authorStatus" :status="authorStatus" />

        <AuthorStory v-if="authorStories.length" :stories="authorStories" @viewStory="(s: any) => $emit('viewStory', s)" />

        <button v-if="author?.id !== currentUserId" :class="['follow-btn', { following: isFollowing }]" @click="$emit('toggleFollow')">
          {{ isFollowing ? '✓ Đang follow' : 'Follow' }}
        </button>
      </div>
    </div>

    <div class="sidebar-card toc-card" v-if="headings.length">
      <div class="toc-header">
        <div class="toc-icon">
          <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M4 6h16M4 12h12M4 18h8"/></svg>
        </div>
        <h3>Mục lục</h3>
      </div>
      <nav class="toc-nav">
        <a v-for="(heading, idx) in headings" :key="idx" :href="`#${heading.id}`" :class="['toc-item', `level-${heading.level}`, { active: activeHeading === heading.id }]" @click.prevent="scrollToHeading(heading.id)">
          <span class="toc-dot"></span>
          <span class="toc-text">{{ heading.text }}</span>
        </a>
      </nav>
    </div>

    <div class="sidebar-card progress-card">
      <div class="progress-info">
        <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><circle cx="12" cy="12" r="10"/><polyline points="12 6 12 12 16 14"/></svg>
        <span>Tiến độ đọc</span>
        <span class="progress-pct">{{ readProgress }}%</span>
      </div>
      <div class="progress-track">
        <div class="progress-fill" :style="{ width: readProgress + '%', background: progressColor }"></div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  author: Object,
  content: String,
  isFollowing: Boolean,
  reputation: Object,
  streak: { type: Number, default: 0 },
  badges: { type: Array, default: () => [] },
  authorStatus: Object,
  authorStories: { type: Array, default: () => [] },
  equippedBorder: Object,
})

defineEmits(['toggleFollow', 'viewStory'])

const showDetail = ref(false)
const activeHeading = ref('')
const readProgress = ref(0)

const currentUserId = computed(() => {
  try { return JSON.parse(localStorage.getItem('user') || '{}')?.id } catch { return null }
})

const authorColors = computed(() => {
  const c = [['#0ea5e9','#8b5cf6'],['#f97316','#f59e0b'],['#10b981','#06b6d4'],['#ec4899','#8b5cf6'],['#6366f1','#ec4899']]
  return c[(props.author?.id || 0) % c.length]
})

const headings = computed(() => {
  if (!props.content) return []
  return (props.content.match(/^#{1,6}\s+.+$/gm) || []).map((m, i) => ({
    level: m.match(/^#+/)[0].length,
    text: m.replace(/^#+\s+/, ''),
    id: `heading-${i}`
  }))
})

const formatCount = (n: number) => n >= 1000 ? (n / 1000).toFixed(1) + 'k' : String(n)
const formatDate = (d: string) => d ? new Date(d).toLocaleDateString('vi-VN', { year: 'numeric', month: 'long' }) : ''

const scrollToHeading = (id: string) => {
  document.getElementById(id)?.scrollIntoView({ behavior: 'smooth', block: 'start' })
  activeHeading.value = id
}

const updateProgress = () => {
  const el = document.querySelector('.blog-content')
  if (!el) return
  const rect = el.getBoundingClientRect()
  const total = el.scrollHeight - window.innerHeight
  readProgress.value = Math.min(100, Math.max(0, Math.round((-rect.top / total) * 100)))
}

const updateActiveHeading = () => {
  const el = document.querySelector('.blog-content')
  if (!el) return
  let current = ''
  el.querySelectorAll('h1, h2, h3, h4').forEach(h => {
    if (h.getBoundingClientRect().top <= 120) current = h.id || h.textContent || ''
  })
  activeHeading.value = current
}

const progressColor = computed(() => readProgress.value < 30 ? '#0ea5e9' : readProgress.value < 70 ? '#f59e0b' : '#10b981')

const onScroll = () => { updateProgress(); updateActiveHeading() }

onMounted(() => window.addEventListener('scroll', onScroll))
onUnmounted(() => window.removeEventListener('scroll', onScroll))
</script>

<style scoped lang="scss">
.blog-sidebar {
  width: 320px;
  flex-shrink: 0;
  display: flex;
  flex-direction: column;
  gap: var(--space-md);
  position: sticky;
  top: calc(var(--header-height) + var(--space-lg));
  height: fit-content;
}

.sidebar-card {
  background: var(--surface);
  border-radius: var(--radius-xl);
  border: 1px solid var(--border-light);
  overflow: hidden;
}

.author-card { position: relative; }
.author-cover { height: 80px; }
.author-content { padding: 0 var(--space-md) var(--space-md); text-align: center; }
.author-avatar-link { display: inline-block; margin-top: -32px; margin-bottom: 8px; }
.author-avatar { width: 64px; height: 64px; border-radius: 50%; object-fit: cover; border: 3px solid var(--surface); box-shadow: var(--shadow-md); }
.author-avatar-placeholder { display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, var(--primary), #8b5cf6); color: white; font-weight: 700; font-size: 1.4rem; }
.author-name { font-size: 1rem; font-weight: 700; margin-bottom: 4px; }
.author-role { display: inline-block; padding: 2px 10px; background: linear-gradient(135deg, #f97316, #f59e0b); color: white; border-radius: var(--radius-full); font-size: 0.68rem; font-weight: 700; margin-bottom: 8px; }
.author-stats { display: flex; justify-content: space-around; padding: 10px 0; margin-bottom: 8px; border-top: 1px solid var(--border-light); border-bottom: 1px solid var(--border-light); }
.stat { display: flex; flex-direction: column; align-items: center; }
.stat-value { font-size: 0.95rem; font-weight: 700; }
.stat-label { font-size: 0.68rem; color: var(--text-muted); }

.author-expanded { text-align: left; padding: 8px 0; }
.info-row { display: flex; flex-direction: column; gap: 2px; padding: 8px 0; border-bottom: 1px solid var(--border-light); }
.info-row:last-child { border-bottom: none; }
.info-label { font-size: 0.68rem; font-weight: 600; color: var(--text-muted); text-transform: uppercase; letter-spacing: 0.06em; }
.info-value { font-size: 0.82rem; color: var(--text-secondary); }
.info-link { font-size: 0.82rem; color: var(--primary); word-break: break-all; }

.streak-row { display: flex; justify-content: center; gap: 16px; padding: 6px 0; margin-bottom: 4px; }
.streak-item { font-size: 0.78rem; color: var(--text-secondary); }
.streak-item strong { color: var(--accent); }

.toggle-btn { display: flex; align-items: center; justify-content: center; gap: 4px; width: 100%; padding: 8px; border: none; background: var(--bg-secondary); border-radius: var(--radius); font-size: 0.78rem; font-weight: 500; color: var(--text-muted); cursor: pointer; transition: all var(--duration-fast) ease; margin-bottom: 8px; }
.toggle-btn:hover { background: var(--primary-50); color: var(--primary); }
.toggle-btn svg { transition: transform var(--duration-normal) ease; }
.toggle-btn .rotated { transform: rotate(180deg); }

.expand-enter-active, .expand-leave-active { transition: all 0.3s var(--ease-out); overflow: hidden; }
.expand-enter-from, .expand-leave-to { opacity: 0; max-height: 0; }
.expand-enter-to, .expand-leave-from { opacity: 1; max-height: 400px; }

.follow-btn {
  width: 100%; padding: 10px; border: none; border-radius: var(--radius-lg);
  background: linear-gradient(135deg, var(--primary), var(--primary-dark));
  color: white; font-size: 0.88rem; font-weight: 600; cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
  box-shadow: 0 2px 8px rgba(14,165,233,0.3);
}
.follow-btn:hover { transform: translateY(-1px); box-shadow: 0 4px 12px rgba(14,165,233,0.4); }
.follow-btn.following { background: var(--bg-secondary); color: var(--text-secondary); box-shadow: none; }
.follow-btn.following:hover { background: var(--surface-hover); }

.toc-card { padding: var(--space-md); }
.toc-header { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; padding-bottom: 10px; border-bottom: 1px solid var(--border-light); }
.toc-icon { width: 26px; height: 26px; border-radius: var(--radius-sm); background: linear-gradient(135deg, var(--primary), #6366f1); display: flex; align-items: center; justify-content: center; color: white; flex-shrink: 0; }
.toc-header h3 { font-size: 0.88rem; font-weight: 700; }
.toc-nav { display: flex; flex-direction: column; gap: 2px; }
.toc-item {
  display: flex; align-items: center; gap: 8px;
  padding: 7px 10px; border-radius: var(--radius);
  color: var(--text-secondary); text-decoration: none;
  font-size: 0.82rem; transition: all var(--duration-fast) ease;
  border-left: 2px solid transparent;
}
.toc-item:hover { background: var(--surface-hover); color: var(--primary); }
.toc-item.active { background: var(--primary-50); color: var(--primary); font-weight: 600; border-left-color: var(--primary); }
.toc-dot { width: 5px; height: 5px; border-radius: 50%; background: var(--border); flex-shrink: 0; transition: all var(--duration-fast) ease; }
.toc-item.active .toc-dot { background: var(--primary); box-shadow: 0 0 0 3px var(--primary-50); }
.toc-text { overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.level-1 { padding-left: 10px; }
.level-1 .toc-text { font-weight: 600; }
.level-2 { padding-left: 24px; }
.level-3 { padding-left: 40px; }
.level-3 .toc-text { font-size: 0.78rem; }

.progress-card { padding: var(--space-md); }
.progress-info { display: flex; align-items: center; gap: 6px; margin-bottom: 8px; font-size: 0.82rem; font-weight: 500; color: var(--text-secondary); }
.progress-info svg { color: var(--primary); }
.progress-pct { margin-left: auto; font-weight: 600; color: var(--text-primary); }
.progress-track { height: 6px; background: var(--bg-secondary); border-radius: var(--radius-full); overflow: hidden; }
.progress-fill { height: 100%; border-radius: var(--radius-full); transition: width 0.3s ease; }

@media (max-width: 1200px) { .blog-sidebar { width: 280px; } }
</style>
