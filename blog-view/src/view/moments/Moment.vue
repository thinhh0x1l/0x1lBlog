<template>
  <div class="moments-wrapper">
    <div class="moments-header">
      <div class="ui top attached segment" style="text-align: center; border-top: 3px solid #00a7e0;">
        <h2 class="m-text-500" style="margin: 0; color: #2c3e50;">
          <font-awesome-icon icon="fa-regular fa-clock" style="margin-right: 8px; color: #00a7e0;" />
          My Sìtory
        </h2>
      </div>

      <div class="ui attached segment m-padding-bottom-large" style="background: #fafbfc;">
        <!-- Loading State -->
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

        <!-- Moments List -->
        <div v-else class="moments">
          <div
              v-for="(moment, index) in momentList"
              :key="index"
              class="moment"
          >
            <div class="avatar">
              <img :src="userAvatar" :alt="userName" loading="lazy">
            </div>
            <div class="ui card">
              <div class="content m-top divider">
                <span style="font-weight: 700; color: #2c3e50;">{{ userName }}</span>
                <span class="right floated">
                  <font-awesome-icon icon="fa-regular fa-calendar-alt" style="font-size: 11px; margin-right: 4px; color: #95a5a6;" />
                  {{ formatRelativeTimeOrDate(moment.createTime) }}
                </span>
              </div>
              <div class="content typo" v-html="moment.content"></div>
              <div class="extra content">
                <a class="left floated like-btn" @click="handleLike(moment.id)" :class="{ 'liked': isLiked(moment.id) }">
                  <font-awesome-icon
                      :icon="[isLiked(moment.id) ? 'fas' : 'far', 'heart']"
                      class="like-icon"
                  />
                  <span class="like-text">{{ getLikeCount(moment.id) }}</span>
                </a>
              </div>
            </div>
          </div>
        </div>

        <!-- Empty State -->
        <div v-if="!isLoading && momentList.length === 0" class="empty-moments">
          <div class="empty-icon">
            <font-awesome-icon icon="fa-regular fa-smile-wink" />
          </div>
          <p>Chưa có hoạt động nào</p>
        </div>

        <!-- Pagination -->
        <div v-if="isLoading" class="pagination-container skeleton-pagination">
          <div class="p-skeleton-wrapper">
            <Skeleton width="300px" height="34px" class="mx-auto" />
          </div>
        </div>

        <div v-else-if="totalRecords > pageSize" class="pagination-container">
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
import { ref, reactive, computed, onMounted, nextTick, onBeforeUnmount } from 'vue'
import Paginator, { type PageState } from 'primevue/paginator'
import { useAppStore } from "@/store";
import mediumZoom from "medium-zoom"
import { formatRelativeTimeOrDate } from "@/util/dateTimeFormatUtils.js";
import type { MomentLikedByGuestId } from "@/types/momentType";
import { useScrollToTop } from "@/util/ScrollToTop.js";
import { getMomentListByPageNum, toggleLikeApi } from "@/api/moment";
import type { ApiResponse, PageResult } from "@/types/commonType";

const { scrollToTop } = useScrollToTop()
const store = useAppStore()

const pageNum = ref(1)
const pageSize = ref(5)
const totalRecords = ref(0)
const isLoading = ref(false)
const momentList = ref<MomentLikedByGuestId[]>([])

const userAvatar = computed(() => store.introduction?.avatar || 'https://via.placeholder.com/45')
const userName = computed(() => store.introduction?.name || 'Thjnk')

const momentLikes = reactive<Record<number, number>>({})
const momentLiked = reactive<Record<number, boolean>>({})
const originLiked = reactive<Record<number, boolean>>({})
const likeTimers = new Map<number, ReturnType<typeof setTimeout>>()
const LIKE_DELAY = 800

const isLiked = (momentId: number) => momentLiked[momentId] || false
const getLikeCount = (momentId: number) => momentLikes[momentId] || 0

const handleLike = (momentId: number) => {
  const previousLiked = momentLiked[momentId]
  const previousLikes = momentLikes[momentId] || 0

  const newLiked = !previousLiked
  const newLikes = newLiked ? previousLikes + 1 : Math.max(0, previousLikes - 1)

  momentLiked[momentId] = newLiked
  momentLikes[momentId] = newLikes

  const oldTimer = likeTimers.get(momentId)
  if (oldTimer) clearTimeout(oldTimer)

  const timer = setTimeout(async () => {
    const finalLiked = momentLiked[momentId]
    const isFetch = Number(finalLiked) - Number(originLiked[momentId])

    if (isFetch === 0) {
      likeTimers.delete(momentId)
      return
    }

    try {
      await toggleLikeApi({ id: momentId, liked: isFetch })
      originLiked[momentId] = !originLiked[momentId]
    } catch (error) {
      console.error('Lỗi update Like:', error)
      if (momentLiked[momentId] !== originLiked[momentId]) {
        momentLiked[momentId] = originLiked[momentId]
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
    const res: ApiResponse<PageResult<MomentLikedByGuestId>> = await getMomentListByPageNum(pageNum.value)
    if (res.code === 200) {
      momentList.value = res.data.items
      res.data.items.forEach(m => {
        momentLikes[m.id] = m.likes || 0
        originLiked[m.id] = m.liked || false
        momentLiked[m.id] = m.liked || false
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

let zoom: any

const initZoom = () => {
  zoom = mediumZoom(".typo img", {
    margin: 24,
    background: "rgba(0, 0, 0, 0.9)"
  })
}

onBeforeUnmount(() => {
  likeTimers.forEach(timer => clearTimeout(timer))
  likeTimers.clear()
  if (zoom) zoom.detach()
})

onMounted(async () => {
  await fetchMoments()
  await nextTick()
  initZoom()
})
</script>

<style scoped>
.moments-wrapper {
  background: linear-gradient(135deg, #f5f7fa 0%, #f8f9fc 100%);
  min-height: 100vh;
  padding: 20px 0;

}

/* Header Segment */
.ui.top.attached.segment {
  background: white !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04);
  border-top: 3px solid #00a7e0 !important;

}

.ui.attached.segment {
  background: white !important;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.04);
  margin-bottom: 20px;

}

/* Avatar */
.avatar {
  margin-left: -62.5px;
  float: left !important;
  transition: transform 0.2s ease;
}

.avatar:hover {
  transform: scale(1.05);
}

.avatar img {
  height: 45px;
  width: 45px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

/* Moments Timeline */
.moments {
  margin-left: 26px !important;
  padding-left: 40px !important;
  border-left: 2px solid #e8ecf0 !important;
}

.moment {
  margin-top: 30px;
  position: relative;
  animation: fadeInUp 0.4s ease;
}

@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.moment:first-child {
  margin-top: 0 !important;
}

/* Card Styling */
.card {
  position: relative;
  border-radius: 6px !important;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.04), 0 1px 2px rgba(0, 0, 0, 0.03) !important;
  background: #fff !important;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid #919191;
  margin-left: 10px !important;
  left: -15px
}
.content.divider{
  border-bottom: 1px solid #dddddd;
}
.card:hover {
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08) !important;
  transform: translateY(-2px);
  border-color: #8fb8fa;
}

/* Card Arrow */
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
  width: 12px !important;
  height: 12px !important;
  transition: background 0.1s ease !important;
  background-color: #fff !important;
  border-style: solid !important;
  border-color: #919191 ;
  box-shadow: -1px -1px 1px rgba(0, 0, 0, 0.02);
}
.card:hover:before{
  border-color: #8fb8fa !important;
}

  /* Card Content */
.content.m-top {
  padding: 14px 18px !important;
  background: linear-gradient(to bottom, #fff, #fafbfc);
  border-radius: 16px 16px 0 0;
}

.content .right.floated {
  font-size: 12px !important;
  float: right;
  color: #95a5a6 !important;
  display: flex;
  align-items: center;
  gap: 4px;
}

.content.typo {
  padding: 16px 18px !important;
  background: white;
}

.content.typo * {
  font-size: 14px !important;
  line-height: 1.7;
  color: #4a5568;
}

.content.typo img {
  max-width: 100%;
  border-radius: 12px;
  margin: 12px 0;
  cursor: pointer;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.content.typo img:hover {
  transform: scale(1.02);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

/* Extra Content / Like Button */
.extra.content {
  padding: 10px 18px !important;
  border-top: 1px solid #edf2f7 !important;
  display: flex;
  justify-content: flex-start;
  background: #fafbfc;
  border-radius: 0 0 16px 16px;
}

.like-btn {
  color: #94a3b8 !important;
  font-size: 13px !important;
  cursor: pointer;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  border-radius: 24px;
  transition: all 0.2s ease;
  background: transparent;
}

.like-btn:hover {
  background: #fff5f5;
  color: #e53e3e !important;
  transform: translateX(2px);
}

.like-btn.liked {
  color: #e53e3e !important;
}

.like-icon {
  font-size: 14px;
  transition: transform 0.2s ease;
}

.like-btn:hover .like-icon {
  transform: scale(1.1);
}

.like-btn.liked .like-icon {
  animation: heartBeat 0.3s ease;
}

@keyframes heartBeat {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.2);
  }
}

.like-text {
  font-weight: 500;
}

/* Privacy Mode */
.privacy .card {
  background: repeating-linear-gradient(145deg, #fafbfc, #fafbfc 15px, #fff 15px, #fff 30px) !important;
  border-style: dashed;
}

.privacy .card:before {
  background-color: #fafbfc;
}

/* Empty State */
.empty-moments {
  text-align: center;
  padding: 60px 20px;
  color: #95a5a6;
}

.empty-icon {
  font-size: 64px;
  margin-bottom: 16px;
  color: #cbd5e0;
}

.empty-moments p {
  font-size: 16px;
  margin: 0;
}

/* Pagination */
.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #edf2f7;
}

:deep(.p-paginator) {
  background: transparent;
  border: none;
  gap: 4px;
}

:deep(.p-paginator .p-paginator-pages .p-paginator-page) {
  min-width: 36px;
  height: 36px;
  border-radius: 10px;
  transition: all 0.2s ease;
  font-weight: 500;
}

:deep(.p-paginator .p-paginator-pages .p-paginator-page:hover) {
  background: #f0f2f5;
  color: #00a7e0;
}

:deep(.p-paginator .p-paginator-pages .p-paginator-page.p-highlight) {
  background: linear-gradient(135deg, #00a7e0, #0090c4);
  color: white;
  box-shadow: 0 2px 8px rgba(0, 167, 224, 0.3);
}

:deep(.p-paginator .p-paginator-prev),
:deep(.p-paginator .p-paginator-next) {
  min-width: 36px;
  height: 36px;
  border-radius: 10px;
  transition: all 0.2s ease;
}

:deep(.p-paginator .p-paginator-prev:hover),
:deep(.p-paginator .p-paginator-next:hover) {
  background: #f0f2f5;
  color: #00a7e0;
}

/* Skeleton Loading */
.skeleton-card {
  border-radius: 16px !important;
  overflow: hidden;
}

.mb-2 {
  margin-bottom: 8px;
}

.mx-auto {
  margin-left: auto;
  margin-right: auto;
}

/* Responsive */
@media (max-width: 768px) {
  .moments-wrapper {
    padding: 12px;
  }

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

  .content.m-top {
    padding: 10px 14px !important;
  }

  .content.typo {
    padding: 12px 14px !important;
  }

  .content.typo * {
    font-size: 13px !important;
  }

  .extra.content {
    padding: 8px 14px !important;
  }

  .card:before {
    top: 18px !important;
  }
}

/* Scrollbar */
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 10px;
}

::-webkit-scrollbar-thumb {
  background: #cbd5e0;
  border-radius: 10px;
}

::-webkit-scrollbar-thumb:hover {
  background: #a0aec0;
}
</style>