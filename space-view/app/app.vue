<template>
  <ElConfigProvider :locale="viLocale">
    <NuxtLayout>
      <NuxtPage />
    </NuxtLayout>
  </ElConfigProvider>
  <universe />
</template>

<script setup>
import viLocale from 'element-plus/es/locale/lang/vi'
import universe from '@/components/universe'
</script>
