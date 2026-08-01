<template>
  <div class="group-card" @click="$emit('click', group)">
    <div class="group-cover" :style="{ background: coverColor }">
      <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
        <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
        <circle cx="9" cy="7" r="4"/>
        <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
        <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
      </svg>
    </div>
    <div class="group-info">
      <h4 class="group-name">{{ group.name }}</h4>
      <span class="group-members">{{ group.memberCount || 0 }} thành viên</span>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  group: { type: Object, required: true },
})

defineEmits(['click'])

const coverColors = ['#0ea5e9', '#8b5cf6', '#10b981', '#f59e0b', '#ec4899']
const coverColor = computed(() => coverColors[props.group.id % coverColors.length])
</script>

<style scoped lang="scss">
.group-card {
  display: flex;
  flex-direction: column;
  border-radius: var(--radius-lg);
  overflow: hidden;
  border: 1px solid var(--border-light);
  background: var(--surface);
  cursor: pointer;
  transition: all 0.2s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: var(--shadow-md);
  }
}

.group-cover {
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.group-info {
  padding: 12px;
}

.group-name {
  font-size: 0.95rem;
  font-weight: 600;
  margin: 0 0 4px 0;
}

.group-members {
  font-size: 0.78rem;
  color: var(--text-muted);
}
</style>
