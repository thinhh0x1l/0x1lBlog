<template>
  <div id="app">
    <Toast/>
    <ConfirmDialog/>
    <router-view/>
    <universe/>
  </div>
</template>

<script setup>
import Universe from '@/components/universe'
</script>