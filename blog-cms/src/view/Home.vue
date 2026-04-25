  <template>
  <el-container class="home-container">
    <!-- Header -->
    <el-header>
      <div class="header-left">
        <span><img src="/img/logo.svg" alt="Logo" height="60">
           Hệ thống quản lý Blog</span>
      </div>
      <el-button type="info" @click="logout">Đăng xuất</el-button>
    </el-header>

    <!-- Main Content -->
    <el-container>
      <!-- Sidebar -->
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <div class="toggle-button" @click="toggleCollapse">
          <el-icon>
            <component :is="isCollapse ? Expand : Fold" />
          </el-icon>
        </div>


        <!-- Menu -->
        <el-menu
          background-color="#223344"
          text-color="#fff"
          active-text-color="#409eff"
          :default-openeds="defaultOpeneds"
          :unique-opened="false"
          :collapse="isCollapse"
          :collapse-transition="false"
          :router="true"
          :default-active="store.activePath"
        >
          <!-- First Level Menu -->
          <el-sub-menu
            v-for="item in menuList"
            :key="item.id"
            :index="String(item.id)"
          >
            <template #title>
              <el-icon>
                <component :is="iconsObj[item.id]"/>
              </el-icon>
              <span>  {{ item.title }}</span>
            </template>

            <!-- Second Level Menu -->
            <el-menu-item
              v-for="subItem in item.children"
              :key="subItem.id"
              :index="subItem.path"
              @click="store.saveNavState(subItem.path)"
            >
              <template #title>
                <el-icon>
                  <component :is="iconsObj[subItem.id]" v-if="iconsObj[subItem.id]"/>
                </el-icon>
                <span>{{ subItem.title }}</span>
              </template>

            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

<!--      <el-main :class="isCollapse?'m-el-main-width-64':'m-el-main-width-190'">-->
<!--        <router-view v-slot="{ Component }">-->
<!--          <keep-alive include="WriteBlog,WriteMoment">-->
<!--            <component :is="Component" />-->
<!--          </keep-alive>-->
<!--        </router-view>-->
<!--      </el-main>-->
      <el-main :class="isCollapse?'m-el-main-width-64':'m-el-main-width-190'">
        <router-view :key="route.fullPath"/>
      </el-main>

    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted} from "vue";
import {useRouter ,useRoute } from "vue-router";
import { getCurrentInstance } from "vue";
import { useAppStore } from '@/store/index.js'

const store = useAppStore()

const { proxy } = getCurrentInstance()

//Router instance
const route = useRoute()
const router = useRouter()

//Reactive data
const isCollapse = ref(false)
const defaultOpeneds = ref(['1','2','3'])

const menuList = [
  {
    id: 1,
    title: 'Quản lý Blog',
    children: [
      {
        id: 11,
        title: 'Viết Blog',
        children: [],
        path: '/blogs/write'
      },
      {
        id: 12,
        title: 'Quản lý Blogs',
        children: [],
        path: '/blogs'
      },
      {
        id: 13,
        title: 'Quản lý khoảng khắc',
        children: [],
        path: '/moments'
      },
      {
        id: 14,
        title: 'Viết khoảng khắc',
        children: [],
        path: '/moments/write'
      },
      {
        id: 15,
        title: 'Quản lý thể loại',
        children: [],
        path: '/categories'
      },
      {
        id: 16,
        title: 'Quản lý Tags',
        children: [],
        path: '/tags'
      },
      {
        id: 17,
        title: 'Quản lý bình luận',
        children: [],
        path: '/comments'
      }
    ]
  },
  {
    id: 2,
    title: 'Quản lý hệ thống',
    children: [
      {
        id: 21,
        title: 'Quản lý Menus',
        children: [],
        path: '/menus'
      },
      {
        id: 22,
        title: 'Quản lý trang Web',
        children: [],
        path: '/siteSettings'
      },
      {
        id: 23,
        title: 'Quản lý Giới thiệu',
        children: [],
        path: '/about'
      }
    ]
  },
  {
    id: 3,
    title: 'Thống kê dữ liệu',
    children: [
      {
        id: 31,
        title: 'Lượt truy cập',
        children: [],
        path: '/pv'
      },
      {
        id: 32,
        title: 'Truy cập trực tuyến',
        children: [],
        path: '/latest'
      },
      {
        id: 33,
        title: 'Nhật ký hệ thống',
        children: [],
        path: '/log'
      }
    ]
  },

]

import {
  Menu,
  Tickets,
  DataAnalysis,
  Tools,
  Edit,
  Opportunity,
  Discount,
  DocumentCopy,
  Delete,
  TrendCharts,
  View,
  Document, Expand, Fold
} from '@element-plus/icons-vue'

import {defineAsyncComponent} from "vue";
import {useToast} from "@/plugins/primevueConfig/primePluginVue.js";
const iconsObj = {
  '1': defineAsyncComponent(()=> import('@/assets/css/icon/MenuIcon.vue')),
  '2': Tools,
  '3': DataAnalysis,
  '11': defineAsyncComponent(()=> import('@/assets/css/icon/WriteIcon.vue')),
  '12': 'el-icon-s-order',
  '13': Opportunity,
  '14': defineAsyncComponent(()=> import('@/assets/css/icon/TagIcon.vue')),
  '15': defineAsyncComponent(()=> import('@/assets/css/icon/CommentIcon.vue')),
  '21': Tickets,
  '22': defineAsyncComponent(()=> import('@/assets/css/icon/WebMangeIcon.vue')),
  '31': 'el-icon-s-marketing',
  '32': View,
  '33': Document,
}

const toast = useToast()
// Lifecycle
onMounted(() => {

})

// Các phương thức
const logout = () => {
  window.sessionStorage.clear()
  router.push('/login')
  proxy.$msgSuccess('Đăng xuất thành công')
}

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}


</script>


<style scoped>
.home-container{
  height: 100%;
}

.el-header{
  background-color: #2e404c;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-left: 10px;
  color: #ffffff;
  font-size: 22px;
}

.header-left span{
  display: flex ;
  align-items: center;
}

.el-aside{
  background-color: #353d78;
  position: absolute;
  top: 60px;
  bottom: 0;
}

.el-aside .el-menu{
  border-right: none ;
}

.el-main{
  background-color: #ffffff;
  position: absolute;
  top: 60px;
  bottom: 0;
  right: 0;
  overflow-y: auto;
}

.m-el-main-width-190 {
  width: calc(100% - 200px);
}

.m-el-main-width-64 {
  width: calc(100% - 64px);
}

.iconfont{
  margin-right: 5px;
  font-size: 20px;
}

.toggle-button {
  background-color: #4f6aaf;
  font-size: 20px;
  line-height: 40px;
  color: #ffffff;
  text-align: center;
  cursor: pointer;
  transition: background-color 0.3s;
}

.toggle-button:hover {
  background-color: #5a6074;
}
</style>