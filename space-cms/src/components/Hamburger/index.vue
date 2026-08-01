<template>
  <div class="hamburger-container" @click="toggleClick">
    <el-icon :size="20" class="hamburger" :class="{ 'is-active': isActive }">
      <Fold v-if="isActive" />
      <Expand v-else />
    </el-icon>
  </div>
</template>

<script setup>
defineProps({ isActive: { type: Boolean, default: false } })
const emit = defineEmits(['toggleClick'])
const toggleClick = () => emit('toggleClick')
</script>

<style lang="scss" scoped>
.hamburger-container {
  padding: 0 16px;
  cursor: pointer;
  line-height: 50px;
  .hamburger {
    vertical-align: middle;
    transition: transform .3s;
    &.is-active { transform: rotate(180deg); }
  }
}
</style>
