<script setup>
const props = defineProps({
  isDark:{
    type: Boolean
  },
  placeholder:{
    type: String,
    default: 'Nhập nội dung ...'
  }
})
const content = defineModel('content',{
    type: String,
    default: ''
})
</script>
<template>
  <mavon-editor
      class="mavon-editor"
      v-model="content"
      language="en"
      :placeholder='props.placeholder'
      :ishljs="true"
      toolbarsBackground="#000"
      editorBackground="#121111 "
      previewBackground="#121111"
      :font-size="'18px'"
      :scrollStyle="true  "
  />
</template>

<style scoped>
.mavon-editor {
  width: 100%;
  height: 100%;
}
.dark{

}
:deep(.auto-textarea-wrapper .auto-textarea-input){
  color: #b5d5f1 !important;
  background: #121111 !important;
  font-family: monospace !important;
}
:deep(.v-note-panel ){
  background: #121111 !important;
}

:deep(.v-note-show){
  color: #cee9fd !important;
  font-family: monospace!important;
}
</style>