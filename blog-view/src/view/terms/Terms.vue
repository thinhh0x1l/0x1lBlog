<template>
  <MarkdownPage :content="termsContent" />
</template>
<script setup>
import termsContent from '@/content/terms.md?raw'
import MarkdownPage from '@/components/layouts/MarkdownPage.vue'
</script>
