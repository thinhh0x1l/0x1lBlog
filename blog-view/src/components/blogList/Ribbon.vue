<script setup>
const props = defineProps({
  category:{
    type: Object,
    required: true
  }
})
</script>

<style scoped>
.ribbon-label {
  position: relative !important;
  left: -3.12rem !important;
  margin-bottom: 15px!important;
  overflow: visible  !important;
  max-height: 30px;
  background: linear-gradient(75deg,
  #c2410c 0%,
  #f97316 40%,
  #f59e0b 70%,
  #eab308 100%
  ) !important;
  border: none !important;
  padding: 1rem 1rem !important;
  border-radius: 0 2px 2px 0 !important;
  box-shadow:
      3px 3px 10px rgba(0, 0, 0, 0.2),
      inset 0 1px 0 rgba(255, 255, 255, 0.2);
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.3) !important;;
}

.ribbon-label::before {
  position: absolute;
  content: '';
  top: 0;
  margin-top: 30px;
  left: -17px;
  bottom: 0;
  width: 0;
  height: 0;
  border-right: 16px solid #c2410c;
  border-bottom: 16px solid transparent;
  border-left: 17px solid transparent;
}

.ribbon-label:hover {
  background: linear-gradient(135deg,
  #ea580c 0%,
  #d97706 50%,
  #b45309 100%
  ) !important;
}
</style>

<template>
  <!-- Category button -->
  <router-link class="col-12 mb-3" :to="'/category/'+category.slug">
    <Button
        :label="category.name"
        icon="pi pi-folder-open"
        class="ribbon-label m-text-500 text-base-ct "
    />
  </router-link>
</template>