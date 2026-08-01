<template>
  <MarkdownPage :content="aboutContent" />
</template>
<script setup>
definePageMeta({ layout: 'default' })

useHead({ title: 'Về chúng tôi - 0x1lBlog' })

const aboutContent = await import('~/content/about.md?raw').then(m => m.default).catch(() => '# About\n\n0x1lBlog - Nơi chia sẻ kiến thức lập trình.')
</script>
