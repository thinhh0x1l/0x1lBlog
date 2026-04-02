<template>
  <div>
    <div class="ui top attached segment" style="text-align: center">
      <h2 class="m-text-500">Hoạt động của tôi</h2>
    </div>
    <div class="ui attached segment m-padding-bottom-large">
      <div class="moments">
        <div class="moment" v-for="(moment, index) in momentList" :key="index">
          <div class="avatar">
            <img :src="userAvatar" loading="lazy">
          </div>
          <div class="ui card">
            <div class="content m-top">
              <span style="font-weight: 700">{{ userName }}</span>
              <span class="right floated">{{ formatDate(moment.createTime) }}</span>
            </div>
            <div class="content typo" :class="{'privacy': !moment.published}"
                 v-html="moment.content"></div>

            <div class="extra content">
              <a class="left floated" @click="handleLike(moment.id)">
                <font-awesome-icon :icon="[(isLiked(moment.id)?'fas':'far'),'heart']"
                                   style="color: rgb(255, 0, 30);" />
                {{ moment.likes }}
              </a>
            </div>
          </div>
        </div>
      </div>

      <div class="pagination-container">
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
</template>

<script setup lang="ts">
import {ref, reactive, computed, onMounted, nextTick, watch} from 'vue'
import Paginator, {type PageState} from 'primevue/paginator'
import {useAppStore} from "@/store";
import mediumZoom from "medium-zoom"
import {formatDate} from "@/util/dateTimeFormatUtils.js";
import type {Moment} from "@/types/momentType";
import {useScrollToTop} from "@/util/ScrollToTop.js";
import {getMomentListByPageNum} from "@/api/moment";
import type {ApiResponse, PageInfo} from "@/plugins/axios2";
const {scrollToTop} = useScrollToTop()

const store = useAppStore()

const momentList = ref<Moment[]>([])
const pageNum = ref(1)
const pageSize = ref(5)
const totalRecords = ref(0)
const likedMoments = ref(new Set(JSON.parse(localStorage.getItem('likedMomentIds')||'[]')))

const userAvatar = computed(() => store.introduction?.avatar || 'https://via.placeholder.com/45')
const userName = computed(() => store.introduction?.name || 'Thjnk')


const handleLike = (momentId: number) => {
  const moment = momentMap.value.get(momentId)
  if (!moment) return
  const newSet = new Set(likedMoments.value)
  if (newSet.has(momentId)) {
    newSet.delete(momentId)
    moment.likes = Math.max(0, moment.likes - 1)
  } else {
    newSet.add(momentId)
    moment.likes++
  }
  likedMoments.value = newSet
  localStorage.setItem(
      'likedMomentIds',
      JSON.stringify([...newSet])
  )
}
const momentMap = computed(() => {
  const map = new Map<number, Moment>()
  momentList.value.forEach(m => map.set(m.id, m))
  return map;
})

const isLiked = (momentId: number) => {
  return likedMoments.value.has(momentId)
}

const handlePageChange = (event: PageState) => {
  pageNum.value = event.page+1
  fetchMoments()
}

const fetchMoments = async () => {
  scrollToTop()
 try{
   const res: ApiResponse<PageInfo<Moment>> = await getMomentListByPageNum(pageNum.value)
   if(res.code === 200){
     momentList.value = res.data.list;
     totalRecords.value = res.data.total
   }
 }catch (error){}
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
  z-index: 2 !important;
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