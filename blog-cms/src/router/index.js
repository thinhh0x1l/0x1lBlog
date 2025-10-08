import { createRouter, createWebHistory } from 'vue-router'
import Login from "@/view/Login.vue"

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
]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

// Thiết lâp route guard
router.beforeEach((to, from, next) => {
    if (to.path !== '/login') {
        // lấy token
        // const tokenStr = window.sessionStorage.getItem('token')
        // if (!tokenStr) return next("/login")
    }

    if (to.meta.title) {
        document.title = to.meta.title
    }

    next()
})

export default router