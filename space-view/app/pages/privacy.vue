<template>
  <MarkdownPage :content="privacyContent" />
</template>
<script setup>
definePageMeta({ layout: 'default' })

useHead({ title: 'Chính sách bảo mật - 0x1lBlog' })

const privacyContent = await import('~/content/privacy.md?raw').then(m => m.default).catch(() => '# Chính sách bảo mật\n\nĐang cập nhật...')
</script>
