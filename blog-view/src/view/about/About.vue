<template>
  <div class="pb-2  pt-4 surface-card relative ">
    <p class="m-text-cover"
        style="text-align: center; font-size: 25px">
      Best forever
    </p>
    <div class="mx-3">
      <h2 class="m-text-500"
          style="text-align: center; font-weight: 800 "
      >{{ about.title }}</h2>

      <div v-if="about.musicInfo" ref="playerRef"/>
      <div
          class="typo content m-margin-top-large"
          v-html="about.content"
      ></div>
    </div>

    <!-- Phần bình luận -->
    <div class="ui bottom teal attached segment threaded comments">
      <CommentList
          :page="1"
          :blog-id="null"
          v-if="about.commentEnabled === 'true'"
      />
      <h3 class="ui header" v-else>Bình luận đã bị tắt</h3>
    </div>
  </div>
</template>

<script setup lang="ts">
import APlayer from 'aplayer'
import 'aplayer/dist/APlayer.min.css'
import {ref, onMounted, nextTick, onUnmounted} from 'vue'
import { fGetAboutList } from "@/api/about"
import CommentList from "@/components/comments/CommentList.vue"
import { useToast } from 'primevue/usetoast'
import mediumZoom from "medium-zoom";
import type {About} from "@/types/aboutType.ts";
import type {ApiResponse} from "@/types/commonType";

const playerRef = ref(null)
let playerInstance: { destroy: () => void; } | null = null
const isFixed = ref(false)
const playerOptions = ref({
  lrcType: 3,
  autoplay: true,
  fixed: false,
  audio: {},
  container: null,
})
const about = ref<About>({
  title: '',
  musicId: '',
  content: '',
  commentEnabled: 'false',
  musicInfo: ''
})

const toast = useToast()

const msgError = (message: string) => {
  toast.add({
    severity: 'error',
    summary: 'Lỗi',
    detail: message,
    life: 3000
  })
}

function initPlayer() {
  if (!playerRef.value) return
  clearPlayer()
  playerOptions.value.container = playerRef.value
  playerInstance = new APlayer(playerOptions.value)
}
function clearPlayer() {
  if (playerInstance) {
    playerInstance.destroy()
    playerInstance = null
  }
}
const getData = async () => {
  try {
    const res: ApiResponse<About>  = await fGetAboutList()
    if (res.code === 200) {
      about.value = res.data
      await nextTick();
      playerOptions.value.audio = res.data.musicInfo
      initPlayer();
    } else {
      msgError(res.msg)
    }
  } catch (error) {
    msgError("Yêu cầu thất bại")
  }
}
let zoom;
const initZoom = () => {
  zoom = mediumZoom(".typo img", {
    margin: 24,
    background: "#000"
  })
}
// Lifecycle
onMounted(async () => {
  await getData()
  await nextTick()
  initZoom()
})
onUnmounted(() => {
  clearPlayer()
})
</script>

<style scoped>
.content{
  letter-spacing: 1px !important;
}
</style>