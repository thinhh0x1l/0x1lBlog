import { createRouter, createWebHistory } from 'vue-router'
import Login from "@/view/Login.vue"
import { useAppStore } from '@/store/index.js'
import Layout from "@/layout"

const routes = [
    {
        path: '/login',
        component: Login,
        meta: {
            title: 'Đăng nhập quản trị viên'
        },
        hidden: true
    },
    {
        path: '/redirect/:path(.*)',
        component: Layout,
        hidden: true,
        children: [
            {
                path: '',
                component: () => import('@/view/redirect/index.vue'),
                meta: { title: 'Redirect' }
            }
        ]
    },
    {
        path: '/',
        component: Layout,
        redirect: '/dashboard',
        children: [
            {
                path: 'dashboard',
                name: 'Dashboard',
                component: () => import('@/view/dashboard/Dashboard.vue'),
                meta: { title: 'Dashboard', icon: 'Odometer', affix: true }
            }
        ]
    },
    {
        path: '/blog',
        component: Layout,
        redirect: '/blog/list',
        meta: { title: 'Quản lý blog', icon: 'Document' },
        children: [
            {
                path: 'write',
                name: 'WriteBlog',
                component: () => import('@/view/blog/WriteBlog.vue'),
                meta: { title: 'Viết Blog', icon: 'Edit', hidden: true, activeMenu: '/blog/list' }
            },
            {
                path: 'moment/write',
                name: 'WriteMoment',
                component: () => import('@/view/moment/WriteMoment.vue'),
                meta: { title: 'Viết Moment', icon: 'Edit', hidden: true, activeMenu: '/blog/moment/list' }
            },
            {
                path: 'edit/:id',
                name: 'EditBlog',
                component: () => import('@/view/blog/WriteBlog.vue'),
                meta: { title: 'Chỉnh sửa Blog', icon: 'Edit', hidden: true, activeMenu: '/blog/list' }
            },
            {
                path: 'moment/edit/:id',
                name: 'EditMoment',
                component: () => import('@/view/moment/WriteMoment.vue'),
                meta: { title: 'Chỉnh sửa Moment', icon: 'Edit', hidden: true, activeMenu: '/blog/moment/list' }
            },
            {
                path: 'list',
                name: 'BlogList',
                component: () => import('@/view/blog/BlogList.vue'),
                meta: { title: 'Blogs List', icon: 'List' }
            },
            {
                path: 'moment/list',
                name: 'MomentList',
                component: () => import('@/view/moment/Moments.vue'),
                meta: { title: 'Moments List', icon: 'ChatDotSquare' }
            },
            {
                path: 'category/list',
                name: 'CategoryList',
                component: () => import('@/view/category/CategoryList.vue'),
                meta: { title: 'Thể loại', icon: 'Collection' }
            },
            {
                path: 'tag/list',
                name: 'TagList',
                component: () => import('@/view/tag/TagList.vue'),
                meta: { title: 'Thẻ', icon: 'PriceTag' }
            },
            {
                path: 'comment/list',
                name: 'CommentList',
                component: () => import('@/view/comment/CommentList.vue'),
                meta: { title: 'Bình luận', icon: 'ChatLineSquare' }
            },
        ]
    },
    {
        path: '/system',
        redirect: '/system/site-setting',
        meta: { title: 'Quản lý hệ thống', icon: 'Setting' },
        component: Layout,
        children: [
            {
                path: 'site-setting',
                name: 'SiteSetting',
                component: () => import('@/view/site/SiteSetting.vue'),
                meta: { title: 'SiteSetting', icon: 'Tools' }
            }
        ]
    },
    {
        path: '/monitor',
        redirect: '/monitor/visit',
        component: Layout,
        meta: { title: 'Giám sát hệ thống', icon: 'Monitor' },
        children: [
            {
                path: 'visit',
                name: 'Visit',
                component: () => import('@/view/log/Visit.vue'),
                meta: { title: 'Visit', icon: 'View' }
            },
            {
                path: 'overview',
                name: 'Overview',
                component: () => import('@/view/monitor/Overview.vue'),
                meta: { title: 'Overview', icon: 'DataAnalysis' }
            },
            {
                path: 'realtime',
                name: 'Realtime',
                component: () => import('@/view/monitor/Realtime.vue'),
                meta: { title: 'Realtime', icon: 'Clock' }
            },
            {
                path: 'visits',
                name: 'Visits',
                component: () => import('@/view/monitor/Visits.vue'),
                meta: { title: 'Visits', icon: 'User' }
            },
            {
                path: 'guests',
                name: 'Guests',
                component: () => import('@/view/monitor/Guests.vue'),
                meta: { title: 'Guests', icon: 'Avatar' }
            },
            {
                path: 'pages',
                name: 'Pages',
                component: () => import('@/view/monitor/Pages.vue'),
                meta: { title: 'Pages', icon: 'Document' }
            },
            {
                path: 'events',
                name: 'Events',
                component: () => import('@/view/monitor/Events.vue'),
                meta: { title: 'Events', icon: 'List' }
            }
        ]
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes: routes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) return savedPosition
        return { top: 0 }
    }
})

router.beforeEach((to, from, next) => {
    // if (to.path !== '/login') {
    //     const tokenStr = localStorage.getItem('token')
    //     if (!tokenStr) return next("/login")
    // }

    if (to.meta.title) {
        document.title = to.meta.title + " | think's Blog"
    }
    const store = useAppStore()
    store.saveNavState(to.path)
    next()
})

export default router
