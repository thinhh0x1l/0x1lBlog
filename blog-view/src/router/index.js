import { createRouter, createWebHistory } from 'vue-router'
import Index from '@/view/Index.vue'
import { useAppStore } from '@/store/index.ts'

const routes = [
  { path: '/login', name: 'login', component: () => import('@/view/login/Login.vue'), meta: { title: 'Đăng nhập' } },
  { path: '/oauth2/callback', name: 'oauth2-callback', component: () => import('@/view/oauth2/OAuth2Callback.vue'), meta: { title: 'Đang đăng nhập...' } },
  {
    path: '/', component: Index, redirect: '/home',
    children: [
      { path: '/home', name: 'home', component: () => import('@/view/home/Home.vue'), meta: { title: 'Trang chủ' } },
      { path: '/blog/:id', name: 'blog', component: () => import('@/view/blog/Blog.vue'), meta: { title: 'Blog' } },
      { path: '/profile/:userId', name: 'profile', component: () => import('@/view/profile/Profile.vue'), meta: { title: 'Hồ sơ' } },
      { path: '/search', name: 'search', component: () => import('@/view/search/Search.vue'), meta: { title: 'Tìm kiếm' } },
      { path: '/notifications', name: 'notifications', component: () => import('@/view/notifications/Notifications.vue'), meta: { title: 'Thông báo' } },
      { path: '/bookmarks', name: 'bookmarks', component: () => import('@/view/bookmarks/Bookmarks.vue'), meta: { title: 'Bài viết đã lưu' } },
      { path: '/series', name: 'series', component: () => import('@/view/series/SeriesList.vue'), meta: { title: 'Series' } },
      { path: '/series/:id', name: 'series-detail', component: () => import('@/view/series/SeriesDetail.vue'), meta: { title: 'Series' } },
      { path: '/trending', name: 'trending', component: () => import('@/view/trending/Trending.vue'), meta: { title: 'Xu hướng' } },
      { path: '/settings', name: 'settings', component: () => import('@/view/settings/Settings.vue'), meta: { title: 'Cài đặt' } },
      { path: '/archives', name: 'archives', component: () => import('@/view/archives/Archives.vue'), meta: { title: 'Lưu trữ' } },
      { path: '/about', name: 'about', component: () => import('@/view/about/About.vue'), meta: { title: 'Về tôi' } },
      { path: '/tag/:id', name: 'tag', component: () => import('@/view/tag/Tag.vue'), meta: { title: 'Tag' } },
      { path: '/category/:name', name: 'category', component: () => import('@/view/category/Category.vue'), meta: { title: 'Thể loại' } },
      { path: '/music', name: 'music', component: () => import('@/view/music/MusicList.vue'), meta: { title: 'Âm nhạc' } },
      { path: '/music/:id', name: 'music-detail', component: () => import('@/view/music/MusicDetail.vue'), meta: { title: 'Nghe nhạc' } },
      { path: '/canvas', name: 'canvas', component: () => import('@/view/canvas/CanvasPage.vue'), meta: { title: 'Canvas' } },
      { path: '/terms', name: 'terms', component: () => import('@/view/terms/Terms.vue'), meta: { title: 'Điều khoản' } },
      { path: '/privacy', name: 'privacy', component: () => import('@/view/privacy/Privacy.vue'), meta: { title: 'Chính sách bảo mật' } },
    ]
  }
]

const router = createRouter({ history: createWebHistory(import.meta.env.BASE_URL), routes })
router.beforeEach((to) => {
  const store = useAppStore()
  document.title = to.meta.title ? (store.webTitleSuffix ? `${to.meta.title} | ${store.webTitleSuffix}` : to.meta.title) : '0x1lBlog'
})

export default router
