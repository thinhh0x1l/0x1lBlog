<template>
  <MarkdownPage :content="privacyContent" />
</template>
<script setup>
import privacyContent from '@/content/privacy.md?raw'
import MarkdownPage from '@/components/layouts/MarkdownPage.vue'
</script>
