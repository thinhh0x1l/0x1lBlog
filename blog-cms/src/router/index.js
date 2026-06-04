import { createRouter, createWebHistory } from 'vue-router'
import Login from "@/view/Login.vue"
import { useAppStore } from '@/store/index.js'
import Layout from "@/layout"

const routes = [
    {
        path: '/login',
        component: Login,
        meta: {
            title: 'Đặng nhập quản trị viên '
        },
        hidden: true
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
                meta: {title: 'Dashboard', icon: 'Orange'}
            }
        ]
    },
    {
        path: '/blog',
        name: 'Blog',
        redirect: '/blog/write',
        component: Layout,
        meta: {title: 'Quản lý blog', icon: 'View'},
        children: [
            {
                path: 'write',
                name: 'WriteBlog',
                component: () => import('@/view/blog/WriteBlog.vue'),
                meta: {title: 'Viết Blog', icon: 'edit'}
            },
            {
                path: 'moment/write',
                name: 'WriteMoment',
                component: () => import('@/view/moment/WriteMoment.vue'),
                meta: {title: 'Viết Blog', icon: 'edit'}
            },
            {
                path: 'edit/:id',
                name: 'EditBlog',
                component: () => import('@/view/blog/WriteBlog.vue'),
                meta: {title: 'Chỉnh sửa Blog', icon: 'edit'},
                hidden: true
            },
            {
                path: 'moment/edit/:id',
                name: 'EditMoment',
                component: () => import('@/view/moment/WriteMoment.vue'),
                meta: {title: 'Chỉnh sửa Moment', icon: 'edit'},
                hidden: true
            },
            {
                path: 'list',
                name: 'BlogList',
                component: () => import('@/view/blog/BlogList.vue'),
                meta: {title: 'Blogs List', icon: 'el-icon-s-order'}
            },
            {
                path: 'moment/list',
                name: 'MomentList',
                component: () => import('@/view/moment/Moments.vue'),
                meta: {title: 'Moments List', icon: 'el-icon-chat-dot-round'}
            },
            {
                path: 'category/list',
                name: 'CategoryList',
                component: () => import('@/view/category/CategoryList.vue'),
                meta: {title: 'Thể loại', icon: 'el-icon-s-opportunity'}
            },
            {
                path: 'tag/list',
                name: 'TagList',
                component: () => import('@/view/tag/TagList.vue'),
                meta: {title: 'Thẻ', icon: ''}
            },
            {
                path: 'comment/list',
                name: 'CommentList',
                component: () => import('@/view/comment/CommentList.vue'),
                meta: {title: 'Bình luận', icon: 'el-icon-s-comment'}
            },
        ]
    },
    {
        path: '/system',
        name: 'System',
        redirect: '/system/site-setting',
        meta: {title: 'Quản lý hệ thống', icon: 'View'},
        component: Layout,
        children: [
            {
                path: 'site-setting',
                name: 'SiteSetting',
                component: () => import('@/view/site/SiteSetting.vue'),
                meta: {title: 'SiteSetting', icon: 'View'}
            }
        ]
    },
    {
        path: '/monitor',
        name: 'Monitor',
        redirect: '/monitor/visit',
        component: Layout,
        meta: {title: 'Giám sát hệ thống', icon: 'View'},
        children: [
            {
                path: 'visit',
                name: 'Visit',
                component: () => import('@/view/log/Visit.vue'),
                meta: {title: 'Visit', icon: 'View'}
            }
        ]
    }
    // {
    //     path: '/home',
    //     component: Layout,
    //     redirect: '/dashboard',
    //     children: [
    //         {
    //             path: '/dashboard',
    //             component: Dashboard,
    //             meta: {
    //                 title: 'Quản lý hệ thống',
    //                 icon: 'Edit'
    //             }
    //         },
    //         {
    //             path: '/blogs/write',
    //             component: WriteBlog,
    //             meta: {
    //                 title: 'Viết Blog'
    //             }
    //         },
    //         {
    //             path: '/blogs/edit/:id',
    //             component: WriteBlog,
    //             meta: {
    //                 title: 'Chỉnh sửa Blog'
    //             }
    //         },
    //         {
    //             path: '/blogs',
    //             component: BlogList,
    //             meta: {
    //                 title: 'Quản lý Blog'
    //             }
    //         },
    //         {
    //             path: '/moments/write',
    //             component: WriteMoment,
    //             meta: {
    //                 title: 'Viết Moment'
    //             }
    //         },
    //         {
    //             path: '/moments/edit/:id',
    //             component: WriteMoment,
    //             meta: {
    //                 title: 'Chỉnh sửa Moment'
    //             }
    //         },
    //         {
    //             path: '/moments',
    //             component: Moments,
    //             meta: {title: 'Quản lý khoảng khắc'}
    //         },
    //         {
    //             path: '/categories',
    //             component: CategoryList,
    //             meta: {
    //                 title: "Quản lý thể loại"
    //             }
    //         },
    //         {
    //             path: '/tags',
    //             component: TagList,
    //             meta: {
    //                 title: 'Quản lý Tag'
    //             }
    //         },
    //         {
    //             path: '/comments',
    //             component: CommentList,
    //             meta: {
    //                 title: 'Quản lý Commnet'
    //             }
    //         },
    //         {
    //             path: '/siteSettings',
    //             component: SiteSetting,
    //             meta: {
    //                 title: 'Quản lý trang Web'
    //             }
    //         },
    //         {
    //             path: '/about',
    //             component: About,
    //             meta: {
    //                 title: 'Quản lý About'
    //             }
    //         },
    //         {
    //             path: '/visit',
    //             component: Visit,
    //             meta: {
    //                 title: 'Quản lý trang Web'
    //             }
    //         },
    //     ]
    // }
]
//
//
// const router = createRouter({
//     history: createWebHistory(import.meta.env.BASE_URL),
//     routes
// })
//
// // Thiết lâp route guard

// export const routes = [
//     {
//         path: '/login',
//         component: Login,
//         meta: {
//             title: 'Quản lý trang Web'
//         }
//     },
//     {
//         path: '/redirect',
//         component: Layout,
//         hidden: true,
//         children: [
//             {
//                 path: '/redirect/:path(.*)',
//                 component: () => import('@/view/redirect/index.vue')
//             }
//         ]
//     },
//     {
//         path:'',
//         component: Layout,
//         redirect: '/dashboard',
//         children:[
//             {
//                 path: '/dashboard',
//                 component: () => import('@/view/dashboard/Dashboard.vue')
//             },
//             {
//                 path: '/visit',
//                 component: Visit,
//                 meta: {
//                     title: 'Quản lý trang Web'
//                 }
//             },
//
//         ]
//     },
// ]

const router = createRouter({
    history: createWebHistory(),
    routes: routes,
    scrollBehavior(to, home, savedPosition){
        if(savedPosition)
            return savedPosition
        return { top: 0 }
    }
})
router.beforeEach((to, from, next) => {

    if (to.path !== '/dashboard' && to.path !== '/login') {
        const tokenStr = localStorage.getItem('token')
        if (!tokenStr) return next("/login")
    }
    else {
        next()
    }
    if (to.path !== '/login' && to.path !== '/dashboard') {
        // lấy token
        const tokenStr = localStorage.getItem('token')
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


/**
 * Ghi chú: Các tùy chọn cấu hình route
 *
 * hidden: true
 * => Khi đặt là true, route này sẽ không xuất hiện trên sidebar.
 * Ví dụ: trang login, 401, hoặc các trang chỉnh sửa như /edit/1.
 *
 * alwaysShow: true
 * => Thông thường nếu route cha chỉ có 1 route con thì route con sẽ được hiển thị trực tiếp trên sidebar.
 * Nếu muốn luôn hiển thị route cha bất kể có bao nhiêu route con,
 * hãy đặt alwaysShow: true.
 *
 * redirect: noRedirect
 * => Khi đặt là noRedirect, mục này trên breadcrumb sẽ không thể click.
 *
 * name: 'router-name'
 * => Tên của route.
 * Bắt buộc phải khai báo nếu sử dụng <keep-alive>,
 * nếu không có thể gây lỗi cache component.
 *
 * query: '{"id":1,"name":"ry"}'
 * => Query parameter mặc định được truyền khi truy cập route.
 *
 * roles: ['admin', 'common']
 * => Các role được phép truy cập route này.
 *
 * permissions: ['a:a:a', 'b:b:b']
 * => Các permission được phép truy cập route này.
 *
 * meta: {
 *
 *   noCache: true
 *   => Nếu là true thì component sẽ không được cache bởi <keep-alive>.
 *   Mặc định là false.
 *
 *   title: 'title'
 *   => Tên hiển thị trên sidebar, breadcrumb và tags view.
 *
 *   icon: 'svg-name'
 *   => Icon hiển thị trên sidebar.
 *   Tương ứng với file trong:
 *   src/assets/icons/svg
 *
 *   breadcrumb: false
 *   => Nếu là false thì route này sẽ không xuất hiện trên breadcrumb.
 *
 *   activeMenu: '/system/user'
 *   => Khi route hiện tại được mở,
 *   sidebar sẽ highlight menu tương ứng với đường dẫn này.
 *   Thường dùng cho các trang edit/detail bị hidden.
 *
 * }
 */