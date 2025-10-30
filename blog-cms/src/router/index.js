import { createRouter, createWebHistory } from 'vue-router'
import Login from "@/view/Login.vue"
import Home from "@/view/Home.vue";
import Welcome from "@/view/Welcome.vue";
import BlogList from "@/view/blog/BlogList.vue";
import WriteBlog from "@/view/blog/WriteBlog.vue";
import { useAppStore } from '@/store/index.js'

const routes = [
    {
        path: '/',
        redirect: '/login',
    },
    {
        path: '/login',
        component: Login,
        meta: {
            title: 'Đặng nhập quản trị viên '
        }
    },
    {
        path: '/home',
        component: Home,
        redirect: '/welcome',
        children: [
            {
                path: '/welcome',
                component: Welcome,
                meta: {
                    title: 'Quản lý hệ thống'
                }
            },
            {
                path: '/blogs/write',
                component: WriteBlog,
                meta: {
                    title: 'Viết Blog'
                }
            },
            {
                path: '/blogs/edit/:id',
                component: WriteBlog,
                meta: {
                    title: 'Chỉnh sửa Blog'
                }
            },
            {
                path: '/blogs',
                component: BlogList,
                meta: {
                    title: 'Danh sách Blog'
                }
            },
        ]
    }
]


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

// Thiết lâp route guard
router.beforeEach((to, from, next) => {
    if (to.path !== '/login') {
        // lấy token
        const tokenStr = window.sessionStorage.getItem('token')
        if (!tokenStr) return next("/login")
    }

    if (to.meta.title) {
        document.title = to.meta.title
    }
    const store = useAppStore()
    store.saveNavState(to.path)
    next()
})

export default router