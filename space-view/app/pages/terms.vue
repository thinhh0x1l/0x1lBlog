<template>
  <MarkdownPage :content="termsContent" />
</template>
<script setup>
definePageMeta({ layout: 'default' })

useHead({ title: 'Điều khoản sử dụng - 0x1lBlog' })

const termsContent = await import('~/content/terms.md?raw').then(m => m.default).catch(() => '# Điều khoản sử dụng\n\nĐang cập nhật...')
</script>
