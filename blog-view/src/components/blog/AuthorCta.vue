<template>
  <div class="author-cta" v-if="author">
    <div class="cta-content">
      <router-link :to="`/profile/${author.id}`" class="cta-avatar">
        <img v-if="author.avatarUrl" :src="author.avatarUrl" />
        <div v-else class="cta-avatar-placeholder">{{ author.displayName?.charAt(0) || 'U' }}</div>
      </router-link>
      <div class="cta-text">
        <strong>{{ author.displayName }}</strong>
        <p>{{ ctaMessage }}</p>
      </div>
    </div>
    <div class="cta-actions">
      <el-button :type="isFollowing ? 'default' : 'primary'" round @click="$emit('follow')">
        {{ isFollowing ? '✓ Đang follow' : '❤️ Follow' }}
      </el-button>
      <el-button round @click="$router.push(`/profile/${author.id}`)">📝 Xem thêm bài viết</el-button>
    </div>
  </div>
</template>
<script setup>
import { computed } from 'vue'
const props = defineProps({
  author: Object,
  isFollowing: { type: Boolean, default: false },
})
defineEmits(['follow'])
const messages = ['Nếu bạn thấy bài viết hay, hãy theo dõi để ủng hộ mình nhé!', 'Đừng quên follow để không bỏ lỡ bài viết mới!', 'Cảm ơn bạn đã đọc bài viết!']
const ctaMessage = computed(() => messages[(props.author?.id || 0) % messages.length])
</script>
<style scoped lang="scss">
.author-cta { background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-xl); padding: 20px; margin-top: 24px; }
.cta-content { display: flex; align-items: center; gap: 12px; margin-bottom: 16px; }
.cta-avatar img { width: 48px; height: 48px; border-radius: 50%; object-fit: cover; }
.cta-avatar-placeholder { width: 48px; height: 48px; border-radius: 50%; background: linear-gradient(135deg, var(--primary), #8b5cf6); color: white; display: flex; align-items: center; justify-content: center; font-weight: 700; font-size: 1.2rem; }
.cta-text { flex: 1; }
.cta-text strong { display: block; font-size: 0.9rem; margin-bottom: 4px; }
.cta-text p { font-size: 0.82rem; color: var(--text-secondary); margin: 0; }
.cta-actions { display: flex; gap: 10px; }
</style>
