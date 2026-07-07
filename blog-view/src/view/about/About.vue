<template>
  <MarkdownPage :content="aboutContent" />
</template>
<script setup>
import aboutContent from '@/content/about.md?raw'
import MarkdownPage from '@/components/layouts/MarkdownPage.vue'
</script>
