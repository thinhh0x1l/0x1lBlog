<template>
  <OneColumnLayout>
    <div class="markdown-page">
      <div class="markdown-content typo" v-html="renderedContent" />
    </div>
  </OneColumnLayout>
</template>
<script setup>
import { computed } from 'vue'
import { marked } from 'marked'
import DOMPurify from 'dompurify'
import OneColumnLayout from '@/components/layouts/OneColumnLayout.vue'

const props = defineProps({
  content: { type: String, required: true }
})

const renderedContent = computed(() => DOMPurify.sanitize(marked.parse(props.content)))
</script>
<style scoped lang="scss">
.markdown-page {
  padding: var(--space-xl) 0;
}
.markdown-content {
  background: var(--surface);
  border: 1px solid var(--border-light);
  border-radius: var(--radius-xl);
  padding: var(--space-xl) var(--space-2xl);
  box-shadow: var(--shadow-sm);
  line-height: 1.8;
}
</style>
