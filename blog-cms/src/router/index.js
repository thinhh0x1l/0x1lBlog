import { createRouter, createWebHistory } from 'vue-router'
import Login from "@/view/Login.vue"
import Home from "@/view/Home.vue";
import Welcome from "@/view/Welcome.vue";
import BlogList from "@/view/blog/BlogList.vue";
import WriteBlog from "@/view/blog/WriteBlog.vue";
import { useAppStore } from '@/store/index.js'
import CategoryList from "@/view/category/CategoryList.vue";
import TagList from "@/view/tag/TagList.vue";
import CommentList from "@/view/comment/CommentList.vue";
import Site from "@/view/site/SiteSetting.vue";
import SiteSetting from "@/view/site/SiteSetting.vue";
import TsTest from "@/view/TsTest.vue";
import Moments from "@/view/moment/Moments.vue";
import WriteMoment from "@/view/moment/WriteMoment.vue";

const routes = [
    {
        path: '/',
        redirect: '/login',
    },{
        path: '/ts',
        component: TsTest
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
                    title: 'Quản lý Blog'
                }
            },
            {
                path: '/moments/write',
                component: WriteMoment,
                meta: {
                    title: 'Viết Moment'
                }
            },
            {
                path: '/moments/edit/:id',
                component: WriteMoment,
                meta: {
                    title: 'Chỉnh sửa Moment'
                }
            },
            {
                path: '/moments',
                component: Moments,
                meta: {title: 'Quản lý khoảng khắc'}
            },
            {
                path: '/categories',
                component: CategoryList,
                meta: {
                    title: "Quản lý thể loại"
                }
            },
            {
                path: '/tags',
                component: TagList,
                meta: {
                    title: 'Quản lý Tag'
                }
            },
            {
                path: '/comments',
                component: CommentList,
                meta: {
                    title: 'Quản lý Commnet'
                }
            },
            {
                path: '/siteSettings',
                component: SiteSetting,
                meta: {
                    title: 'Quản lý trang Web'
                }
            }
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
        document.title = to.meta.title + ' | think\'s Blog'
    }
    const store = useAppStore()
    store.saveNavState(to.path)
    next()
})

export default router