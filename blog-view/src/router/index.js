import {createRouter, createWebHistory} from "vue-router";
import Index from "@/view/Index.vue";
import Home from "@/view/home/Home.vue";
import GridL from "@/view/GridL.vue";
import {useAppStore} from "@/store/index.ts";
import Archives from "@/view/archives/Archives.vue";
import Blog from "@/view/blog/Blog.vue";
import Tag from "@/view/tag/Tag.vue";
import Moment from "@/view/moments/Moment.vue";
import About from "@/view/about/About.vue";
import Login from "@/view/login/Login.vue";

const routes = [
    {
        path: '/grid',
        component: GridL,
        meta:{
            title: 'GridL'
        }
    },
    {
        path: '/login',
        component: Login,
        meta:{
            title: 'Đăng nhập'
        }
    },
    {
        path: '/',
        component: Index,
        redirect: '/home',
        children: [
            {
                path: '/home',
                name: 'home',
                component: Home,
                meta:{
                    title: 'Trang chủ'
                }
            },
            {
                path: '/archives',
                name: 'archives',
                component: Archives,
                meta: {
                    title: 'Lưu trữ'
                }
            },
            {
                path: '/blog/:id',
                name: 'blog',
                component: Blog,
                meta: {
                    title: 'Blog'
                }
            },
            {
                path: '/tag/:id',
                name: 'tag',
                component: Tag,
                meta: {
                    title: 'Tag'
                }
            },
            {
                path: '/category/:name',
                name: 'category',
                component: () => import('@/view/category/Category.vue'),
                meta: {title: 'Thể loại'}
            },
            {
                path: '/moments',
                name: 'moments',
                component: Moment,
                meta: {
                    title: 'Khoảng khắc'
                }
            },
            {
                path: '/about',
                name: 'about',
                component: About,
                meta: {
                    title: 'Về tôi'
                }
            },
        ]
    },
]

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes
})

router.beforeEach((to, from, next) => {
    const store =  useAppStore()
    if(to.meta.title){
        if(store.webTitleSuffix !== ''){
            document.title = to.meta.title + ' | ' + store.webTitleSuffix
        }
        else {
            document.title = to.meta.title
        }
    }
    next()
})

export default router