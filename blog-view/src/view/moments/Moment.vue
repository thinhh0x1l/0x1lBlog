<template>
  <div>
    <div>
      <div class="ui top attached segment" style="text-align: center">
        <h2 class="m-text-500">Hoạt động của tôi</h2>
      </div>
      <div class="ui attached segment m-padding-bottom-large">
        <div v-if="isLoading" class="moments skeleton-wrapper">
          <div class="moment" v-for="n in 3" :key="n">
            <div class="avatar">
              <Skeleton shape="circle" size="48px" />
            </div>
            <div class="ui card skeleton-card">
              <div class="content m-top">
                <Skeleton width="120px" height="20px" />
                <span class="right floated">
                <Skeleton width="80px" height="16px" />
              </span>
              </div>
              <div class="content typo">
                <Skeleton width="100%" height="20px" class="mb-2" />
                <Skeleton width="95%" height="20px" class="mb-2" />
                <Skeleton width="85%" height="20px" class="mb-2" />
                <Skeleton width="60%" height="20px" />
              </div>
              <div class="extra content">
                <Skeleton width="50px" height="20px" />
              </div>
            </div>
          </div>
        </div>

        <div v-else class="moments">
          <div class="moment" v-for="(moment, index) in momentList" :key="index">
            <div class="avatar">
              <img :src="userAvatar" loading="lazy">
            </div>
            <div class="ui card">
              <div class="content m-top">
                <span style="font-weight: 700">{{ userName }}</span>
                <span class="right floated">{{ formatRelativeTimeOrDate(moment.createTime) }}</span>
              </div>
              <div class="content typo" v-html="moment.content"></div>
              <div class="extra content">
                <a class="left floated" @click="handleLike(moment.id)">
                  <font-awesome-icon
                      :icon="[isLiked(moment.id) ? 'fas' : 'far', 'heart']"
                      style="color: rgb(255, 0, 30);"
                  />
                  {{ getLikeCount(moment.id) }}
                </a>
              </div>
            </div>
          </div>
        </div>

        <div v-if="isLoading" class="pagination-container skeleton-pagination">
          <div class="p-skeleton-wrapper">
            <Skeleton width="300px" height="34px" class="mx-auto" />
          </div>
        </div>

        <div v-else class="pagination-container">
          <Paginator
              :rows="pageSize"
              :totalRecords="totalRecords"
              @page="handlePageChange"
              :always-show="false"
              template="PrevPageLink PageLinks NextPageLink"
          />
        </div>
      </div>
    </div>
    </div>
</template>

<script setup lang="ts">
import {ref, reactive, computed, onMounted, nextTick, watch, shallowRef, onBeforeUnmount, readonly} from 'vue'
import Paginator, {type PageState} from 'primevue/paginator'
import {useAppStore} from "@/store";
import mediumZoom from "medium-zoom"
import {formatDate,formatRelativeTimeOrDate} from "@/util/dateTimeFormatUtils.js";
import type {Moment, MomentLikedByGuestId} from "@/types/momentType";
import {useScrollToTop} from "@/util/ScrollToTop.js";
import {getMomentListByPageNum, toggleLikeApi} from "@/api/moment";
import type {ApiResponse, PageResult} from "@/types/commonType";
// :class="{'privacy': !moment.published}"
const {scrollToTop} = useScrollToTop()

const store = useAppStore()

const pageNum = ref(1)
const pageSize = ref(5)
const totalRecords = ref(0)
const isLoading = ref(false)

const momentList = ref<MomentLikedByGuestId[]>([])

const userAvatar = computed(() => store.introduction?.avatar || 'https://via.placeholder.com/45')
const userName = computed(() => store.introduction?.name || 'Thjnk')

const momentLikes = reactive<Record<number, number>>({}) // show
const momentLiked = reactive<Record<number, boolean>>({}) // show
const originLiked = reactive<Record<number, boolean>>({}) // equal

const likeTimers = new Map<number, ReturnType<typeof setTimeout>>()

const LIKE_DELAY = 800

const isLiked = (momentId: number) => {
  return momentLiked[momentId];
}

const getLikeCount = (momentId: number) => {
  return momentLikes[momentId];
}

const handleLike = (momentId: number) => {

  const previousLiked = momentLiked[momentId]
  const previousLikes = momentLikes[momentId]

  // Tính toán state mới
  const newLiked = !previousLiked
  const newLikes = newLiked
      ? previousLikes + 1
      : Math.max(0, previousLikes - 1)

  momentLiked[momentId] = newLiked;
  momentLikes[momentId] = newLikes

  // console.log(`${momentId}:\n
  //             original: ${originLiked[momentId]}\n
  //             prev: ${previousLiked}\n
  //             newLiked: ${newLiked}
  //             `)

  // Clear timer cũ nếu có
  const oldTimer = likeTimers.get(momentId)
  if (oldTimer) {
    clearTimeout(oldTimer)
  }

  //Debounce request
  const timer = setTimeout(async () => {
    const finalLiked = momentLiked[momentId]

    // ct = hành động mới (newLiked) - hành động ban đầu (origin)
    // = 0 : không thây đổi => không gửi req
    // = +-1: gửi req
    const isFetch = Number(finalLiked) - Number(originLiked[momentId])

    // = 0 : không thây đổi => không gửi req
    if (isFetch === 0)
      return

    try {
      // = +-1: gửi req
        await toggleLikeApi({
          id: momentId,
          liked: isFetch
        })
        originLiked[momentId] = !originLiked[momentId]


    } catch (error) {
      console.error('Lỗi update Like:', error)

      // không thây đổi thì return
      if(momentLiked[momentId] === originLiked[momentId])
        return
      else{
        momentLiked[momentId] = originLiked[momentId]

        // ex: false -> true ở UI: 77->78 => isFetch = +1
        // roll back 78 += -(+1) = 77
        momentLikes[momentId] += -isFetch
      }

    } finally {
      likeTimers.delete(momentId)
    }
  }, LIKE_DELAY)

  likeTimers.set(momentId, timer)
}


const fetchMoments = async () => {
  scrollToTop()
  isLoading.value = true

  try {
    const res: ApiResponse<PageResult<MomentLikedByGuestId>> =
        await getMomentListByPageNum(pageNum.value)

    if (res.code === 200) {
      momentList.value = res.data.items
      res.data.items.forEach(m => {
        momentLikes[m.id] = m.likes
        originLiked[m.id] = m.liked
        momentLiked[m.id] = m.liked
      })
      totalRecords.value = res.data.totalElements
    }

  } catch (error) {
    console.error('Lỗi lấy moments:', error)
  } finally {
    isLoading.value = false
  }
}

const handlePageChange = (event: PageState) => {
  pageNum.value = event.page + 1
  fetchMoments()
}

const refreshMoments = () => {
  fetchMoments()
}

onBeforeUnmount(() => {
  // Clear all timers
  likeTimers.forEach(timer => {
    clearTimeout(timer)
  })
  likeTimers.clear()
})


let zoom;
const initZoom = () => {
  zoom = mediumZoom(".typo img", {
    margin: 24,
    background: "#000"
  })
}
// Lifecycle
onMounted(async () => {
  await fetchMoments()
  await nextTick()
  initZoom()
})
</script>

<style scoped>
.avatar {
  margin-left: -62.5px;
  float: left !important;
}

.avatar img {
  height: 45px;
  width: 45px;
  border-radius: 500px;
  object-fit: cover;
}

.moments {
  margin-left: 26px !important;
  padding-left: 40px !important;
  border-left: 1px solid #dee5e7 !important;
}

.moment {
  margin-top: 30px;
  position: relative;
}

.moment:first-child {
  margin-top: 0 !important;
}

.card {
  width: 100% !important;
  position: relative;
  border-radius: 0.28571429rem;
  box-shadow: 0 1px 3px 0 #d4d4d5, 0 0 0 1px #d4d4d5;
  background: #fff;
}

.card:before {
  border-width: 0 0 1px 1px !important;
  transform: translateX(-50%) translateY(-50%) rotate(45deg) !important;
  bottom: auto !important;
  right: auto !important;
  top: 22px !important;
  left: 0 !important;
  position: absolute !important;
  content: '' !important;
  background-image: none !important;
  //z-index: 2 !important;
  width: 12px !important;
  height: 12px !important;
  transition: background .1s ease !important;
  background-color: inherit !important;
  border-style: solid !important;
  border-color: #d4d4d5 !important;
}

.content.m-top {
  padding: 10px 14px !important;
}

.content .right.floated {
  font-size: 12px !important;
  float: right;
  color: #999;
}

.content.typo * {
  font-size: 14px !important;
  line-height: 1.6;
}

.content.typo img {
  max-width: 100%;
  border-radius: 8px;
  margin: 8px 0;
}

.extra.content {
  padding: 5px 14px !important;
  border-top: 1px solid rgba(34,36,38,.15);
  display: flex;
  justify-content: flex-start;
}

.extra.content a {
  color: rgba(0, 0, 0, 0.7) !important;
  font-size: 12px !important;
  cursor: pointer;
  text-decoration: none;
  display: flex;
  align-items: center;
  gap: 4px;
}

.extra.content a:hover {
  color: red !important;
}

.extra.content .like-color {
  color: red !important;
}

.extra.content i {
  font-size: 12px !important;
  font-style: normal;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 3em;
}

:deep(.p-paginator) {
  background: transparent;
  border: none;
}

:deep(.p-paginator .p-paginator-pages .p-paginator-page.p-highlight) {
  background: #00a7e0;
  color: white;
}

.privacy {
  background: repeating-linear-gradient(145deg, #f2f2f2, #f2f2f2 15px, #fff 0, #fff 30px) !important;
}

/* Responsive */
@media (max-width: 768px) {
  .avatar {
    margin-left: -45px;
  }

  .avatar img {
    height: 35px;
    width: 35px;
  }

  .moments {
    margin-left: 15px !important;
    padding-left: 25px !important;
  }
}
</style>