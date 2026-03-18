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

      <!-- PrimeVue Paginator -->
      <div class="pagination-container">
        <Paginator
            :first="(pageNum - 1) * pageSize"
            :rows="pageSize"
            :totalRecords="totalRecords"
            @page="handlePageChange"
            template="PrevPageLink PageLinks NextPageLink"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, reactive, computed, onMounted, nextTick} from 'vue'



// PrimeVue components
import Paginator from 'primevue/paginator'
import {useAppStore} from "@/store/index.ts";
import mediumZoom from "medium-zoom"

// Store
const store = useAppStore()

// State
const momentList = ref([])
const pageNum = ref(1)
const pageSize = ref(10)
const totalRecords = ref(0)
const likedMoments = ref(new Set())

// Computed
const userAvatar = computed(() => store.introduction?.avatar || 'https://via.placeholder.com/45')
const userName = computed(() => store.introduction?.name || 'Người dùng')
const totalPage = computed(() => Math.ceil(totalRecords.value / pageSize.value))

// Methods
const formatDate = (date) => {
  if (!date) return ''
  try {

  } catch (error) {
    return 'không xác định'
  }
}

const handleLike = (momentId) => {
  const moment = momentList.value.find(m => m.id === momentId)
  if (moment) {
    if (likedMoments.value.has(momentId)) {
      likedMoments.value.delete(momentId)
      moment.likes--
    } else {
      likedMoments.value.add(momentId)
      moment.likes++
    }
  }
}

const isLiked = (momentId) => {
  return likedMoments.value.has(momentId)
}

const handlePageChange = (event) => {
  pageNum.value = Math.floor(event.first / event.rows) + 1
  fetchMoments()
}

// Fetch moments (mock data)
const fetchMoments = async () => {
  // Giả lập API call
  const mockData = generateMockMoments()

  // Phân trang mock data
  const start = (pageNum.value - 1) * pageSize.value
  const end = start + pageSize.value
  momentList.value = mockData.slice(start, end)
  totalRecords.value = mockData.length
}

// Tạo dữ liệu mẫu
const generateMockMoments = () => {
  const contents = [
    '<p>Hôm nay thật là một ngày đẹp trời! 🌞</p><img class="medium-zoom-image" src="https://picsum.photos/400/300?random=1" alt="sunny day" loading="lazy">',
    '<p>Vừa đọc xong cuốn sách "Nhà giả kim" - thực sự rất hay!</p><img  class="medium-zoom-image" loading="lazy" src="https://picsum.photos/400/300?random=2" alt="book">',
    '<p>Check-in tại quán cà phê mới mở ☕️</p><img  class="medium-zoom-image" loading="lazy" src="https://picsum.photos/400/300?random=3" alt="coffee">',
    '<p>Chia sẻ một vài bức ảnh du lịch Đà Lạt 🏔️</p><img class="medium-zoom-image"  loading="lazy" src="https://picsum.photos/400/300?random=4" alt="dalat">',
    '<p>Coding cả đêm để hoàn thành project 🖥️</p><img class="medium-zoom-image"  loading="lazy" src="https://picsum.photos/400/300?random=5" alt="coding">',
    '<p>Món ăn ngon cuối tuần 🍜</p><img  class="medium-zoom-image" loading="lazy" src="https://picsum.photos/400/300?random=6" alt="food">',
    '<p>Gym time! 💪</p><img loading="lazy"  class="medium-zoom-image" src="https://picsum.photos/400/300?random=7" alt="gym">',
    '<p>Mua sắm cuối tuần 🛍️</p><img loading="lazy" class="medium-zoom-image"  src="https://picsum.photos/400/300?random=8" alt="shopping">',
    '<p>Học tiếng Nhật mỗi ngày 📚</p><img loading="lazy" class="medium-zoom-image"  src="https://picsum.photos/400/300?random=9" alt="study">',
    '<p>Gặp gỡ bạn bè sau giờ làm 🍻</p><img loading="lazy" class="medium-zoom-image"  src="https://picsum.photos/400/300?random=10" alt="friends">'
  ]

  return Array.from({ length: 25 }, (_, i) => ({
    id: i + 1,
    content: contents[i % contents.length],
    createTime: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toISOString(),
    published: Math.random() > 0.2, // 80% published
    likes: Math.floor(Math.random() * 50)
  })).sort((a, b) => new Date(b.createTime) - new Date(a.createTime))
}

let zoom
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