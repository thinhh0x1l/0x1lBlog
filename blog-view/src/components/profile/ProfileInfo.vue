<template>
  <div class="profile-info">
    <div class="avatar-section">
      <div class="avatar-wrapper" :class="{ 'has-border': !!border }">
        <img v-if="user?.avatarUrl" :src="user.avatarUrl" class="profile-avatar" />
        <div v-else class="profile-avatar placeholder">{{ user?.displayName?.charAt(0) || 'U' }}</div>
      </div>
    </div>
    <h1 class="profile-name">{{ user?.displayName }}</h1>
    <span class="profile-role" v-if="user?.isCreator">Creator</span>
    <p class="profile-bio" v-if="user?.bio">{{ user.bio }}</p>

    <div class="profile-stats">
      <div class="stat"><span class="stat-value">{{ user?.blogCount || 0 }}</span><span class="stat-label">Bài viết</span></div>
      <div class="stat"><span class="stat-value">{{ formatCount(user?.followerCount || 0) }}</span><span class="stat-label">Theo dõi</span></div>
      <div class="stat"><span class="stat-value">{{ formatCount(user?.followingCount || 0) }}</span><span class="stat-label">Đang follow</span></div>
    </div>

    <div class="exp-section" v-if="level">
      <div class="exp-header"><span class="exp-level">Lv.{{ level }}</span><span class="exp-rep" v-if="repScore">★ {{ repScore }} Rep</span></div>
      <div class="exp-track"><div class="exp-fill" :style="{ width: expPct + '%' }"></div></div>
      <span class="exp-text">{{ currentExp }}/{{ nextLevelExp }} EXP</span>
    </div>

    <div class="streak-section" v-if="streak">
      <span>🔥 <strong>{{ streak }}</strong> ngày liên tiếp</span>
    </div>

    <div class="profile-actions">
      <template v-if="isOwn">
        <el-button type="primary" round @click="$emit('editProfile')"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z"/></svg> Chỉnh sửa hồ sơ</el-button>
        <el-button round @click="$emit('editLayout')"><svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><rect x="3" y="3" width="7" height="7"/><rect x="14" y="3" width="7" height="7"/><rect x="14" y="14" width="7" height="7"/><rect x="3" y="14" width="7" height="7"/></svg> Sắp xếp</el-button>
        <el-button :type="gameMode ? 'warning' : 'default'" round @click="$emit('toggleGameMode')">🎮 {{ gameMode ? 'Tắt Game Mode' : 'Game Mode' }}</el-button>
      </template>
      <template v-else>
        <el-button :type="isFollowing ? 'default' : 'primary'" round @click="$emit('follow')">{{ isFollowing ? '✓ Đang follow' : 'Follow' }}</el-button>
        <el-button round @click="$emit('message')">✉ Message</el-button>
      </template>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({
  user: Object, border: Object, level: Number, currentExp: Number, nextLevelExp: Number,
  repScore: Number, streak: Number, isOwn: Boolean, isFollowing: Boolean, gameMode: Boolean,
})
defineEmits(['editProfile', 'editLayout', 'toggleGameMode', 'follow', 'message'])
const expPct = computed(() => Math.min(100, Math.round((props.currentExp || 0) / (props.nextLevelExp || 1000) * 100)))
const formatCount = (n) => n >= 1000 ? (n / 1000).toFixed(1) + 'k' : n
</script>
<style scoped lang="scss">
.profile-info { text-align: center; padding: 0 24px 24px; margin-top: -48px; position: relative; z-index: 1; }
.avatar-section { margin-bottom: 12px; }
.avatar-wrapper { display: inline-block; border-radius: 50%; }
.avatar-wrapper.has-border { padding: 3px; background: linear-gradient(135deg, #0ea5e9, #8b5cf6); }
.profile-avatar { width: 96px; height: 96px; border-radius: 50%; object-fit: cover; border: 4px solid var(--surface); }
.profile-avatar.placeholder { background: linear-gradient(135deg, var(--primary), #8b5cf6); color: white; display: flex; align-items: center; justify-content: center; font-size: 2.5rem; font-weight: 700; }
.profile-name { font-size: 1.5rem; font-weight: 800; margin-bottom: 4px; }
.profile-role { display: inline-block; padding: 2px 12px; background: linear-gradient(135deg, #f97316, #f59e0b); color: white; border-radius: 99px; font-size: 0.7rem; font-weight: 700; margin-bottom: 8px; }
.profile-bio { color: var(--text-secondary); margin-bottom: 16px; font-size: 0.95rem; }
.profile-stats { display: flex; justify-content: center; gap: 32px; margin-bottom: 16px; }
.stat { display: flex; flex-direction: column; align-items: center; }
.stat-value { font-size: 1.25rem; font-weight: 700; }
.stat-label { font-size: 0.8rem; color: var(--text-muted); }
.exp-section { max-width: 300px; margin: 0 auto 12px; }
.exp-header { display: flex; justify-content: space-between; margin-bottom: 4px; }
.exp-level { font-size: 0.85rem; font-weight: 700; color: var(--primary); }
.exp-rep { font-size: 0.75rem; color: #f59e0b; }
.exp-track { height: 6px; background: var(--bg-secondary); border-radius: 99px; overflow: hidden; margin-bottom: 2px; }
.exp-fill { height: 100%; border-radius: 99px; background: linear-gradient(90deg, var(--primary), #8b5cf6); transition: width 0.5s; }
.exp-text { font-size: 0.68rem; color: var(--text-muted); }
.streak-section { margin-bottom: 16px; font-size: 0.85rem; color: var(--accent); }
.profile-actions { display: flex; justify-content: center; gap: 8px; flex-wrap: wrap; }
</style>
