<template>
  <div class="comment-form-container">
    <form @submit.prevent="onSubmit">
      <div class="textarea-wrapper">
         <Textarea
             :auto-resize="true"
             :rows="1"
             class="custom-textarea"
             :placeholder="nicknameReply"
             :class="{'textarea-custom' : threadRoot}"
             v-model="commentForm.content"
             name="content"
             ref="textareaRef"
         ></Textarea>
      </div>
      <div class="flex justify-content-between mx-3">
        <EmojiPicker @select="insertEmoji" />
        <div class="align-items-center flex gap-2">
          <span>Thông báo phản hồi</span>
          <ToggleSwitch v-model="commentForm.notice"/>
          <button type="submit" :disabled="commentForm.content.length===0">
            <font-awesome-icon icon="location-arrow" class="mr-2" size="2xl" style="color: #00a6ff; "/>
          </button>
        </div>
      </div>
    </form>
  </div>
</template>
<script setup lang="ts">
import {computed, ref, watch} from "vue";
import {useCommentStore} from "@/store/commentStore";
import {storeToRefs} from "pinia";
import EmojiPicker from "@/components/comments/EmojiPicker.vue";

const commentStore = useCommentStore()
const emit = defineEmits<{
  (e: "submit"): void
}>()
function onSubmit(){ emit("submit")}
const {threadRoot, parentNickname, commentForm} = storeToRefs(commentStore)
const textareaRef = ref<HTMLTextAreaElement | null>(null)

const insertEmoji = (emoji: any) => {
  const textarea = textareaRef.value
  if(textarea){
    commentForm.value.content = commentForm.value.content+ emoji.native

  // const start = textarea.selectionStart
  // const end = textarea.selectionEnd
  //
  // commentForm.value.content =
  //     commentForm.value.content.slice(0, start) +
  //     emoji +
  //     commentForm.value.content.slice(end)
  //
  // textarea.focus()
  // const cursor = start + emoji.length
  // textarea.setSelectionRange(cursor, cursor)
  }
}
const nicknameReply = ref<string>('')
watch(() => parentNickname.value , (newName,oldValue) =>{
  if(newName)
    nicknameReply.value ='@'+ newName+ ' '
  else
    nicknameReply.value ='Viết bình luận'
}, {deep:true, immediate:true})


</script>
<style scoped>

.comment-form-container{
  background: #eaeaea;
  border: none;
  border-radius: 25px;
  margin-top: -1px;
  font-size: 1em;
  line-height: 1.4285em;
  min-height: 1em;
  position: relative;
  width: 100%;
}
.form-container{
  position: relative;
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 100%;
}
.textarea-wrapper {
  width: 98% ;
  justify-content: center;
  display: flex;
  position: relative;
}
.dividing{
  margin: 0;
  border-bottom: #b3b3b3 1px solid;
  width: 98%;
}
:deep(.p-textarea){
  width: 100% ;
  border: none;
}
:deep(.p-inputtext),
:deep(.p-textarea){
  background: transparent;
  border-radius: 0!important;
  box-shadow: none;
}
.icon-button{
  position: absolute;
  right: 15px;
  bottom: 6px;
  cursor: pointer;
}
.input-wrapper{
  border-bottom: #505050 1px solid;
  margin-left: 1rem;
  width: 33%;
}
:deep(.p-inputtext){
  border: none;
  width: 80%;
}
.textarea-custom::placeholder {
  color:  #00a7e0;
  opacity: 1;
}

</style>