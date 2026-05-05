<template>
    <h3 class="dividing">Comments | Tổng {{ commentStats.totalComments }} bình luận</h3>
    <CommentInfoUser ref="infoRef" :form-validate-message="formValidateMessage"/>
    <CommentForm v-if="!replyCmId"
                 @submit="throttledSubmit"/>
    <div class="comments-list">
      <div v-for="cm in comments"
           :key="cm.id"
      >
        <div class="thread-root">
          <div class="comment-item">
            <Avatar
                :shape="'circle'"
                :image="cm.adminComment ? cm.avatar : `/img/comment-avatar/${cm.avatar}`"
                :size="'large'"
            />
            <div class="comment"  :ref="el => setItemRef(el,cm.id)">
              <div class="comment-header">
                <a class="nickname" :href="cm.website?.trim() || '#'"
                   target="_blank" rel="external nofollow noopener">{{cm.nickname}}</a>
                <span class="owner-tag" :style="{background:  '#fa2745'}"
                      v-if="cm.adminComment">
                  {{siteInfo.commentAdminFlag}}</span>
                <span class="metadata">
                  {{ ` ${formatDate(cm.createTime,'YYYY-MM-DD HH:mm')}`}}
                </span>
                <span class="replying" :style="{background: replyCmId ===cm.id? '#fa2745' : '#48a5ff'}"
                      @click="setReplyComment(cm.threadRoot,cm.id,cm.nickname)">
                  {{replyCmId ===cm.id ? 'Hủy': 'Trả lời'}}</span>
              </div>
              <div>{{cm.content}}</div>
            </div>
          </div>
          <div class="">
            <div  v-if="cm.replyComment"
                  v-for="replyCm in cm.replyComment"
                  :key="replyCm.id"
            >
              <div class="comment-item comment-reply">
                <Avatar
                    class="avt-l"
                    :shape="'circle'"
                    :image="replyCm.adminComment ? replyCm.avatar : `/img/comment-avatar/${replyCm.avatar}`"
                    :size="'normal'"
                />
                <div class="comment" :ref="el => setItemRef(el,replyCm.id)">
                  <div class="comment-header">
                    <a class="nickname"  :href="cm.website?.trim() || '#'"
                          target="_blank" rel="external nofollow noopener">{{replyCm.nickname}}</a>
                    <span class="owner-tag" v-if="replyCm.adminComment">{{siteInfo.commentAdminFlag}}</span>
                    <div class="metadata">
                      {{ ` ${formatDate(replyCm.createTime,'YYYY-MM-DD HH:mm')}`}}
                    </div>
                    <span class="replying" :style="{background: replyCmId ===replyCm.id? '#fa2745' : '#48a5ff'}"
                            @click="setReplyComment(cm.threadRoot,replyCm.id,replyCm.nickname)">
                      {{replyCmId ===replyCm.id ? 'Hủy': 'Trả lời'}}</span>
                  </div>
                  <div><span class="reply">{{`${replyCm.reply}`}}</span>{{ ` ${replyCm.content} `}}</div>
                </div>
              </div>
            </div>
          </div>
          <div v-if="threadRoot === cm.threadRoot && replyCmId" class="comment-item comment-reply">
            <CommentForm @submit="handleSubmit" />
          </div>
          <div v-if="cm.replyComment"  class="thread-line"/>
        </div>
      </div>
    </div>

</template>

<script setup lang="ts">
import {formatDate} from "@/util/dateTimeFormatUtils.js";
import CommentForm from "@/components/comments/CommentForm.vue";
import {ref, watch} from "vue";
import {useCommentStore} from "@/store/commentStore";
import {storeToRefs} from "pinia";
import CommentInfoUser from "@/components/comments/CommentInfoUser.vue";
import {useAppStore} from "@/store";

import {toast} from "@/plugins/primevueConfig/primePluginVue";
import {throttle} from "lodash-es";



const commentStore = useCommentStore()
const store = useAppStore()

const {siteInfo} = storeToRefs(store)
const {comments,threadRoot,commentStats,commentForm} = storeToRefs(commentStore)

const commentRefs = ref<Record<any,any>>({})
let currentEl: any = null
const replyCmId = ref<number|null>(null)
const setReplyComment = (threadRootComment: number,parentId: number ,parentNicknameComment: string) => {
  if(replyCmId.value === parentId){
    replyCmId.value = null
    currentEl.classList.remove('is-reply')
    commentStore.resetFormComment()
    return;
  }
  replyCmId.value = parentId
  if(currentEl )
    currentEl.classList.remove('is-reply')
  currentEl = commentRefs.value[parentId]
  currentEl.classList.add('is-reply')
  commentStore.setFieldFromComment(threadRootComment,parentId,parentNicknameComment)
}

const infoRef = ref<{
  scrollToField: (field: "nickname" | "email") => void
}|null>(null)

const formValidateMessage = ref<Record<string, string>>({
  nickname: '',
  email:'',
})
const handleSubmit = async () => {
  if(sessionStorage.getItem('adminToken')){
    if(commentForm.value.content==='' || commentForm.value.content.length >250)
      toast.warn('Nội dung bình luận không hợp lê!')
    else
      await commentStore.postComment()
    return;
  }
  const { valid, firstErrorKey } = commentStore.validateAll()
  formValidateMessage.value = {}
  if (!valid && firstErrorKey) {
    formValidateMessage.value[firstErrorKey[0]] = firstErrorKey[1]
    infoRef.value?.scrollToField(firstErrorKey[0] as any)
    console.log('aaa')
    return
  }
  await commentStore.postComment()
}

const throttledSubmit = throttle(handleSubmit, 10000)

const setItemRef= (el: any, index: number) =>{
  if(el&&!commentRefs.value[index])
    commentRefs.value[index]=el
}

// khi đổi trang cần phải reset tránh truy cập id tồn tại
// trong commentRefs nhưng không có trong Dom hiện tại
watch(()=> comments.value , () =>{
  commentRefs.value = {}
  replyCmId.value = null
})
</script>

<style scoped>

.comments-list{
  position: relative;
  display: flex;
  flex-direction: column;
}
.thread-root{
  position: relative;
}
.thread-line {
  position: absolute;
  left: 30px;
  top: 48px;
  height: calc(100% - 48px);
  bottom: 0;
  border-style: solid;
  border-width: 0 0 0 2px;
  border-color: #e0e0e0;
}
.comment-item{
  display: flex;
  flex-direction: row;
  margin: 10px  ;
}

.comment{
  background: rgba(234, 234, 234, 0.73);
  border-radius: 10px;
  padding: 0.4rem 1rem;
  margin: 0 3px;
}

.comment-header{
  display: flex;
  align-items: center;
  gap: .75rem;
  margin-bottom: 2px;
}
.nickname{
  font-size: 1.1rem;
  font-weight: bolder;
  color: rgba(0,0,0,.87);
  text-decoration: none;
}
.metadata {
  font-weight: 700;
  color: rgba(0, 0, 0, 0.51);
  font-size: 1em;
}
.owner-tag{
  cursor: default;
  padding: 4px 5px !important;
  font-weight: 500 !important;
  font-size: .85714286rem;
  position: relative;
  background-color: #1b1c1d !important;
  border-color: #1b1c1d !important;
  color: #fff !important;
  border-radius: 3px;
}
.owner-tag:before{
  position: absolute;
  content: '';
  background: #1b1c1d ;
  z-index: 2;
  width: .6666em;
  height: .6666em;
  transition: background .1s ease;
  border-width: 0 0 1px 1px;
  -webkit-transform: translateX(-50%) translateY(-50%) rotate(45deg);
  transform: translateX(-50%) translateY(-50%) rotate(45deg);
  bottom: auto;
  right: auto;
  top: 50%;
  left: 0;
}
.replying{
  padding: 4px 5px;
  color: #FFF;
  font-size: 12px;
  border-radius: 3px;
  cursor: pointer;
  //margin-left: auto
}
.comment-reply{
  margin-left: 4.57rem  ;
}
.reply{
  background: #8fede8;
}


.avt-l{
  position: relative;
}
.avt-l::before{
  position: absolute;
  content: '';
  top: 0;
  width: 2rem;
  height: 50%;
  right: 0;
  left: -2.43rem;
  bottom: 0;
  border-left: 2px solid #e0e0e0;
  border-bottom: 2px solid #e0e0e0;
  border-radius: 0 0 0 10px;
}
.dividing{
  border-bottom: 1px solid #e0e0e0;
}

:deep(.p-avatar){
  flex-shrink: 0;
}
.is-reply {
  border: 2px solid #00a7e0 !important;
  animation: replyPulse 2s infinite;
  position: relative;
  border-radius: 8px;
}

@keyframes replyPulse {
  0%, 100% {
    box-shadow: 0 0 0 0 rgba(0, 167, 224, 0.4);
    border-width: 2px;
  }
  50% {
    box-shadow: 0 0 0 10px rgba(0, 167, 224, 0);
    border-width: 3px;
  }
}
</style>
