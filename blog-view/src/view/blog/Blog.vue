<template>
  <div style="z-index: 10">
    <div class="pb-2 blog-container pt-4 surface-card relative" style="border: 1px solid #d4d4d5;">
      <!-- Skeleton loading chi tiết -->
      <div v-if="loading" class="skeleton-wrapper">
        <!-- PinTop skeleton (chỉ hiển thị khi blog.top là true) -->
        <div v-if="blog.top" class="skeleton-pin-top">
          <Skeleton width="60px" height="24px" class="mb-2" />
        </div>

        <!-- Tiêu đề skeleton -->
        <div class="col-12 text-center" style="padding-top: 0">
          <Skeleton width="70%" height="32px" class="mx-auto mb-2" />
          <Skeleton width="50%" height="32px" class="mx-auto" />
        </div>

        <!-- Thông tin bài viết skeleton -->
        <div class="col-12 text-center">
          <div class="flex flex-wrap justify-content-center gap-4">
            <Skeleton width="120px" height="20px" />
            <Skeleton width="80px" height="20px" />
            <Skeleton width="100px" height="20px" />
            <Skeleton width="130px" height="20px" />
            <Skeleton width="40px" height="20px" />
          </div>
        </div>

        <!-- Category ribbon skeleton -->
        <div class="px-3 py-2">
          <Skeleton width="100px" height="28px" class="mb-3" />
        </div>

        <!-- Nút ghim skeleton -->
        <div class="px-3 py-2">
          <Skeleton width="80px" height="32px" class="mb-3" />
        </div>

        <!-- Player skeleton -->
        <div v-if="blog.musicId" class="px-3 py-2">
          <Skeleton width="100%" height="80px" class="mb-3" />
        </div>

        <!-- Nội dung bài viết skeleton -->
        <div class="typo m-padded-tb-small px-3 blog-content">
          <Skeleton width="100%" height="20px" class="mb-2" />
          <Skeleton width="95%" height="20px" class="mb-2" />
          <Skeleton width="98%" height="20px" class="mb-2" />
          <Skeleton width="60%" height="20px" class="mb-4" />

          <!-- Paragraph 2 -->
          <Skeleton width="100%" height="20px" class="mb-2" />
          <Skeleton width="97%" height="20px" class="mb-2" />
          <Skeleton width="92%" height="20px" class="mb-2" />
          <Skeleton width="85%" height="20px" class="mb-4" />

          <!-- Paragraph 3 -->
          <Skeleton width="100%" height="20px" class="mb-2" />
          <Skeleton width="96%" height="20px" class="mb-2" />
          <Skeleton width="88%" height="20px" class="mb-4" />

          <!-- Image skeleton -->
          <Skeleton width="100%" height="300px" class="mb-4" />

          <!-- Paragraph 4 -->
          <Skeleton width="100%" height="20px" class="mb-2" />
          <Skeleton width="95%" height="20px" class="mb-2" />
          <Skeleton width="90%" height="20px" class="mb-2" />
          <Skeleton width="75%" height="20px" class="mb-4" />

          <!-- Code block skeleton -->
          <Skeleton width="100%" height="160px" class="mb-4" />

          <!-- Paragraph 5 -->
          <Skeleton width="100%" height="20px" class="mb-2" />
          <Skeleton width="94%" height="20px" class="mb-2" />
          <Skeleton width="86%" height="20px" class="mb-2" />
          <Skeleton width="70%" height="20px" class="mb-4" />

          <!-- List skeleton -->
          <Skeleton width="90%" height="20px" class="mb-2" />
          <Skeleton width="88%" height="20px" class="mb-2" />
          <Skeleton width="85%" height="20px" class="mb-4" />

          <!-- Quote skeleton -->
          <Skeleton width="95%" height="80px" class="mb-4" />

          <!-- Paragraph 6 -->
          <Skeleton width="100%" height="20px" class="mb-2" />
          <Skeleton width="92%" height="20px" class="mb-2" />
          <Skeleton width="65%" height="20px" class="mb-4" />
        </div>

        <!-- Divider skeleton -->
        <div class="col-12">
          <div class="border-top-1 surface-border my-2"></div>
        </div>

        <!-- Tags skeleton -->
        <div class="m-padded-tb-no">
          <div class="flex flex-wrap gap-2">
            <Skeleton width="60px" height="28px" borderRadius="16px" />
            <Skeleton width="80px" height="28px" borderRadius="16px" />
            <Skeleton width="70px" height="28px" borderRadius="16px" />
            <Skeleton width="90px" height="28px" borderRadius="16px" />
          </div>
        </div>
      </div>

      <!-- Nội dung thực tế -->
      <div v-else>
        <PinTop v-if="blog.top" />

        <!-- Container với flex layout -->
        <div class="flex flex-column">
          <div>
            <!-- Tiêu đề -->
            <div class="col-12 text-center" style="padding-top: 0">
              <h2 class="header m-scaleup">
                <a href="#" class="text-900 hover:text-primary transition-colors no-underline">
                  {{ blog.title }}
                </a>
              </h2>
            </div>

            <!-- Thông tin bài viết -->
            <div class="col-12 text-center">
              <div class="flex flex-wrap justify-content-center gap-4">
                <div class="flex align-items-center m-datetime">
                  <font-awesome-icon icon="calendar-alt" class="mr-2" />
                  <span>{{ formatDate(blog.updateTime) }}</span>
                </div>

                <div class="flex align-items-center m-views">
                  <font-awesome-icon icon="eye" class="mr-2" />
                  <span>{{ blog.views }}</span>
                </div>

                <div class="flex align-items-center m-common-black">
                  <font-awesome-icon icon="pencil-alt" class="mr-2" />
                  <span>Số chữ ≈ {{ blog.words }} từ</span>
                </div>

                <div class="flex align-items-center m-common-black">
                  <font-awesome-icon icon="clock" class="mr-2" />
                  <span>Thời gian đọc ≈ {{ blog.readTime }} phút</span>
                </div>
                <a
                    class="flex align-items-center m-common-black"
                    @click.prevent="bigFontSize = !bigFontSize"
                    v-tooltip.top="'Nhấp chuột để thay đổi kích thước phông chữ'"
                >
                  <font-awesome-icon icon="font" class="mr-2" />
                </a>
              </div>
            </div>

            <Ribbon v-if="blog.category" :category="blog.category" />

            <div class="px-3 py-2">
              <button @click="toggleFixed">
                {{ isFixed ? ' Hủy ghim ' : ' Ghim ' }}
              </button>
              <div v-if="blog.musicId" ref="playerRef" />

              <!-- Mô tả bài viết -->
              <div
                  class="typo m-padded-tb-small px-3 blog-content line-numbers match-braces rainbow-braces"
                  :class="{ 'm-big-fontsize': bigFontSize }"
                  v-html="blog.content"
              ></div>

              <!-- Divider -->
              <div class="col-12">
                <div class="border-top-1 surface-border my-2"></div>
              </div>

              <!-- Tags -->
              <div class="m-padded-tb-no">
                <Tag v-if="blog.tags" :list-tag="blog.tags"></Tag>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- Blog info container -->
    <div v-if="loading" class="blog-info-container">
      <div style="display: flex; align-items: center;">
        <ul style="list-style-type: disc; padding-left: 1.5em; margin: 0; width: 100%;">
          <li style="margin-bottom: 0.5em;">
            <Skeleton width="200px" height="20px" />
          </li>
          <li style="margin-bottom: 0.5em;">
            <Skeleton width="250px" height="20px" />
          </li>
          <li style="margin-bottom: 0.5em;">
            <Skeleton width="280px" height="20px" />
          </li>
          <li>
            <Skeleton width="100%" height="40px" />
          </li>
        </ul>
      </div>
    </div>
    <div v-else class="blog-info-container">
      <div style="display: flex; align-items: center;">
        <ul style="list-style-type: disc; padding-left: 1.5em; margin: 0;">
          <li style="margin-bottom: 0.5em;">
            Tác giả: {{ author }}
            <router-link to="/about" class="blog-info-link" target="_blank">(Liên hệ tác giả)</router-link>
          </li>
          <li style="margin-bottom: 0.5em;">Ngày phát hành: {{ formatDate(blog.createTime, 'YYYY-MM-DD HH:mm') }}</li>
          <li style="margin-bottom: 0.5em;">Cập nhật lần cuối: {{ formatDate(blog.updateTime, 'YYYY-MM-DD HH:mm') }}</li>
          <li>
            Trang web này được cấp phép theo
            <a href="https://creativecommons.org/licenses/by/4.0/" target="_blank" class="blog-info-link">
              giấy phép Creative Commons Attribution 4.0 International (CC BY 4.0)
            </a>.
            Bạn được phép sao chép, trích dẫn và sử dụng nội dung này cho mục đích thương mại.
            Tuy nhiên, bạn phải ghi rõ tên tác giả và nguồn bài viết.
          </li>
        </ul>
      </div>
    </div>

    <!-- Comment container -->
    <div v-if="loading" class="comment-container">
      <Skeleton width="60%" height="28px" class="mx-auto mb-4" />
      <Skeleton width="100%" height="100px" class="mb-3" borderRadius="8px" />
      <Skeleton width="100%" height="100px" class="mb-3" borderRadius="8px" />
      <Skeleton width="100%" height="100px" class="mb-3" borderRadius="8px" />
      <Skeleton width="100%" height="100px" class="mb-3" borderRadius="8px" />
    </div>
    <div v-else class="comment-container">
      <CommentList v-if="blog.commentEnabled" :blog-id="blogId" :page="0" />
      <h3 style="text-align: center;" v-else>Chức năng bình luận đã bị tắt.</h3>
    </div>
  </div>
</template>

<script setup>
import APlayer from 'aplayer'
import 'aplayer/dist/APlayer.min.css'
import { ref, onMounted, onUnmounted, nextTick, computed, watch } from 'vue'
import Tag from '@/components/blogList/Tag.vue'
import {fMusicInfoBySongId, getBlogById} from '@/api/blog'
import { formatDate } from "@/util/dateTimeFormatUtils.js"
import { onBeforeRouteLeave, onBeforeRouteUpdate, useRoute } from "vue-router"
import { useAppStore } from "@/store/index.ts"
import { storeToRefs } from "pinia"
import Ribbon from "@/components/blogList/Ribbon.vue"
import PinTop from "@/components/blogList/PinTop.vue"
import CommentList from "@/components/comments/CommentList.vue"
import { useBlogDetailStore } from "@/store/blogDetailStore.ts"
import { useScrollToTop } from "@/util/ScrollToTop.js"

import mediumZoom from "medium-zoom"
import {useStreamPost} from "@/api/useStreamPost.ts";

let zoom
const { scrollToTop } = useScrollToTop()
const store = useAppStore()
const blogDetail = useBlogDetailStore()
const route = useRoute()

const { author } = storeToRefs(store)
const { isBlogRenderCompleted } = storeToRefs(blogDetail)

const {
  post,
  loading,
  progress,
  fetchPost
} = useStreamPost()
const bigFontSize = ref(false)
const blogId = computed(() => parseInt(route.params.id))
const blog = ref({
  top: false,
  title: '',
  updateTime: '',
  views: 0,
  words: 0,
  readTime: 0,
  category: null,
  content: '',
  tags: null,
  createTime: '',
  commentEnabled: true
})
const musicInfo = ref(null)
const playerRef = ref(null)
let playerInstance = null
const isFixed = ref(false)
const playerOptions = ref({
  lrcType: 3,
  autoplay: true,
  fixed: false,
  audio: {
    name:'UNKNOWN',
    artist:'UNKNOWN',
    lrc:'UNKNOWN',
    url:'',
    theme:"#000",
    cover:''
  }
})

const toggleFixed = () => {
  if (!playerInstance) return

  isFixed.value = !isFixed.value

  const currentTime = playerInstance.audio.currentTime
  const isCurrentlyPlaying = !playerInstance.audio.paused

  playerInstance.options.fixed = isFixed.value

  const container = playerInstance.container
  const playerFixedClass = 'aplayer-fixed'

  if (isFixed.value) {
    container.classList.add(playerFixedClass)
  } else {
    container.classList.remove(playerFixedClass)
  }

  if (isCurrentlyPlaying) {
    setTimeout(() => {
      playerInstance.seek(currentTime)
      playerInstance.play()
    }, 10)
  }
}

function initPlayer() {
  if (!playerRef.value) return
  clearPlayer()
  playerOptions.value.container = playerRef.value
  playerInstance = new APlayer(playerOptions.value)
}

const fetchBlog = async () => {
  try {
    loading.value = true

    const response = await getBlogById(blogId.value)
    console.log(response)
    if (response.code !== 200) return

    blog.value = response.data

    loading.value = false

    await nextTick()

    isBlogRenderCompleted.value = true
    zoom?.detach()
    initZoom()

    if (typeof Prism !== 'undefined') {
      Prism.highlightAll()
    }

    if (blog.value.musicId) {
      await loadMusicAsync(blog.value.musicId)
    }

  } catch (error) {
    console.log(error)
    loading.value = false
  }
}

const loadMusicAsync = async (musicId) => {
  try {
    const musicRes = await fMusicInfoBySongId(musicId)

    if (!musicRes?.data) return

    playerOptions.value.audio = musicRes.data
    musicInfo.value = musicRes.data

    initPlayer()

  } catch (err) {
    console.log("music load failed", err)
  }
}

watch(() => route.params.id, async () => {
  clearPlayer()
  await fetchPost(54)
  const hash = route.hash
  if (!hash) {
    scrollToTop()
  }
}, { immediate: true })

onBeforeRouteLeave(() => {
  isBlogRenderCompleted.value = false
})

onBeforeRouteUpdate(async (to, from) => {
  if (to.path !== from.path) {
    await nextTick()
  }
})

function clearPlayer() {
  if (playerInstance) {
    playerInstance.destroy()
    playerInstance = null
  }
}


const initZoom = () => {
  zoom = mediumZoom(".typo img", {
    margin: 24,
    background: "#000"
  })
}

onMounted(async () => {
  await nextTick()
  initZoom()
})

onUnmounted(() => {
  clearPlayer()
  isBlogRenderCompleted.value = false
})
</script>

<style scoped>
.comment-container {
  margin-top: -1px;
  padding: 0 15px;
  background: #fff;
  border: 1px solid #d4d4d5;
}

.blog-info-container {
  background: #fcfff5;
  color: #2c662d;
  border: 1px solid #a3c293;
  margin-top: -1px;
  padding: 1em 1.53666666em;
  font-size: 1em;
  line-height: 1.4285em;
  min-height: 1em;
  position: relative;
  justify-content: space-between;
}

.blog-info-link {
  color: #4183c4;
  text-decoration: none;
  font-weight: 500;
  transition: opacity 0.2s ease;
}

.blog-info-link:hover {
  opacity: 0.7;
  text-decoration: none;
}

.blog-container {
  padding-left: 1.5rem;
  padding-right: 1.5rem;
}

@media (max-width: 768px) {
  .blog-container {
    padding-right: max((100vw - 421px)/25, 0px) !important;
    padding-left: max((100vw - 421px)/25, 0px) !important;
  }
  .comment-container {
    padding-right: max((100vw - 421px)/25, 2px) !important;
    padding-left: max((100vw - 421px)/25, 2px) !important;
  }
}

.header {
  border: none;
  margin: 0 1rem;
  top: 13px;
  padding: 0 0;
  font-size: 1.71428571rem;
  font-family: Lato, 'Helvetica Neue', Arial, Helvetica, sans-serif;
  font-weight: 700;
  line-height: 1.28571429em;
  text-transform: none;
  color: rgba(0, 0, 0, .87);
}

/* Skeleton styles */
.skeleton-wrapper {
  width: 100%;
}

.skeleton-pin-top {
  padding: 0 1rem;
  margin-bottom: 0.5rem;
}

/* Animation cho skeleton */
:deep(.p-skeleton) {
  animation: skeleton-wave 1.2s ease-in-out infinite;
}

@keyframes skeleton-wave {
  0% {
    opacity: 1;
  }
  50% {
    opacity: 0.6;
  }
  100% {
    opacity: 1;
  }
}
</style>