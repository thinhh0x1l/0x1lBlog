<template>
  <div class="post-form">
    <div class="form-header">
      <h3 class="form-title">{{ isEditing ? 'Chỉnh sửa bài viết' : 'Viết bài mới' }}</h3>
    </div>
    <div class="form-body">
      <input v-model="form.title" class="form-input form-title-input" placeholder="Tiêu đề bài viết..." />
      <textarea v-model="form.content" class="form-textarea" placeholder="Nội dung bài viết..." rows="12"></textarea>
      <div class="form-actions">
        <el-button type="primary" @click="$emit('submit', form)" :loading="loading">Đăng bài</el-button>
        <el-button @click="$emit('cancel')">Hủy</el-button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  initialData: { type: Object, default: () => ({}) },
  loading: { type: Boolean, default: false },
})

const emit = defineEmits(['submit', 'cancel'])

const isEditing = computed(() => !!props.initialData?.id)

const form = reactive({
  title: props.initialData?.title || '',
  content: props.initialData?.content || '',
  categoryId: props.initialData?.categoryId || null,
  tags: props.initialData?.tags || [],
})
</script>

<style scoped lang="scss">
.post-form {
  background: var(--surface);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-light);
  overflow: hidden;
}

.form-header {
  padding: 16px 20px;
  border-bottom: 1px solid var(--border-light);
}

.form-title {
  font-size: 1.1rem;
  font-weight: 700;
  margin: 0;
}

.form-body {
  padding: 20px;
}

.form-input {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  background: var(--bg-secondary);
  color: var(--text-primary);
  font-size: 1rem;
  font-weight: 600;
  margin-bottom: 16px;

  &:focus {
    outline: none;
    border-color: var(--primary);
  }
}

.form-textarea {
  width: 100%;
  padding: 12px 16px;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-md);
  background: var(--bg-secondary);
  color: var(--text-primary);
  font-size: 0.95rem;
  resize: vertical;
  font-family: inherit;
  margin-bottom: 16px;

  &:focus {
    outline: none;
    border-color: var(--primary);
  }
}

.form-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}
</style>
