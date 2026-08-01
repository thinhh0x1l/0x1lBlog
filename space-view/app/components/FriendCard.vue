<template>
  <div class="friend-card">
    <UserAvatar :src="friend.avatarUrl" :name="friend.displayName" size="lg" />
    <div class="friend-info">
      <h4 class="friend-name">{{ friend.displayName }}</h4>
      <p class="friend-bio" v-if="friend.bio">{{ friend.bio }}</p>
    </div>
    <div class="friend-actions">
      <el-button size="small" @click="$emit('view', friend)">Xem</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
defineProps({
  friend: { type: Object, required: true },
})

defineEmits(['view'])
</script>

<style scoped lang="scss">
.friend-card {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: var(--surface);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-light);
}

.friend-info {
  flex: 1;
  min-width: 0;
}

.friend-name {
  font-size: 0.95rem;
  font-weight: 600;
  margin: 0 0 2px 0;
}

.friend-bio {
  font-size: 0.78rem;
  color: var(--text-muted);
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.friend-actions {
  flex-shrink: 0;
}
</style>
