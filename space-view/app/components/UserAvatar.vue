<template>
  <div class="user-avatar" :class="[sizeClass, { bordered }]" :style="bordered ? borderStyle : {}">
    <img v-if="src" :src="src" class="avatar-image" />
    <div v-else class="avatar-placeholder">{{ fallback }}</div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  src: { type: String, default: '' },
  name: { type: String, default: '' },
  size: { type: String, default: 'md', validator: (v: string) => ['sm', 'md', 'lg', 'xl'].includes(v) },
  bordered: { type: Boolean, default: false },
  borderColor: { type: String, default: 'var(--primary)' },
})

const fallback = computed(() => props.name?.charAt(0)?.toUpperCase() || '?')

const sizeClass = computed(() => `avatar-${props.size}`)

const borderStyle = computed(() => ({
  border: `3px solid ${props.borderColor}`,
}))
</script>

<style scoped lang="scss">
.user-avatar {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border-radius: 50%;
  overflow: hidden;
  flex-shrink: 0;
}

.avatar-sm { width: 32px; height: 32px; }
.avatar-md { width: 40px; height: 40px; }
.avatar-lg { width: 56px; height: 56px; }
.avatar-xl { width: 80px; height: 80px; }

.avatar-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.avatar-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, var(--primary), #8b5cf6);
  color: white;
  font-weight: 700;
}

.avatar-sm .avatar-placeholder { font-size: 0.75rem; }
.avatar-md .avatar-placeholder { font-size: 0.875rem; }
.avatar-lg .avatar-placeholder { font-size: 1.25rem; }
.avatar-xl .avatar-placeholder { font-size: 1.75rem; }
</style>
