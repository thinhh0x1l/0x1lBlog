<template>
  <div class="search-bar">
    <el-input
      v-model="query"
      placeholder="Tìm kiếm..."
      clearable
      @keyup.enter="handleSearch"
      @clear="handleClear"
    >
      <template #prefix>
        <el-icon><Search /></el-icon>
      </template>
    </el-input>
  </div>
</template>

<script setup lang="ts">
import { Search } from '@element-plus/icons-vue'

const query = ref('')
const emit = defineEmits(['search', 'clear'])

const handleSearch = () => {
  if (query.value.trim()) {
    emit('search', query.value.trim())
  }
}

const handleClear = () => {
  emit('clear')
}
</script>

<style scoped lang="scss">
.search-bar {
  width: 100%;
  max-width: 480px;
}
</style>
