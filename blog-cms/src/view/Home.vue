<template>
  <el-container class="home-container">
    <!-- Header -->
    <el-header>
      <div class="header-left">
        <span><img src="@/assets/img/logo.png" alt="Logo" height="60">
           Hệ thống quản lý Blog</span>
      </div>
      <el-button type="info" @click="logout">Đăng xuất</el-button>
    </el-header>

    <!-- Main Content -->
    <el-container>
      <!-- Sidebar -->
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <div class="toggle-button" @click="toggleCollapse">
          <i :class="isCollapse ? 'el-icon-s-unfold' : 'el-icon-s-fold'"></i>
        </div>


        <!-- Menu -->
        <el-menu
          background-color="#333744"
          text-color="#fff"
          active-text-color="#409eff"
          :default-openeds="defaultOpeneds"
          :unique-opened="false"
          :collapse="isCollapse"
          :collapse-transition="false"
          :router="true"
          :default-active="activePath"
        >
          <!-- First Level Menu -->
          <el-sub-menu
            v-for="item in menuList"
            :key="item.id"
            :index="String(item.id)"
          >
            <template #title>
              <i class="iconfont" :class="iconsObj[item.id]"></i>
              <span>  {{ item.title }}</span>
            </template>

            <!-- Second Level Menu -->
            <el-menu-item
              v-for="subItem in item.children"
              :key="subItem.id"
              :index="subItem.path"
              @click="saveNavState(subItem.path)"
            >
              <template #title>
                <i :class="iconsObj[subItem.id]"></i>
                <span>{{ subItem.title }}</span>
              </template>

            </el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>

      <!-- Right Content Area -->
      <el-main>
        <keep-alive>
          <router-view/>
        </keep-alive>
      </el-main>

    </el-container>
  </el-container>
</template>

<script setup>
import { ref, onMounted} from "vue";
import {useRouter} from "vue-router";
import { ElMessage } from "element-plus";
import { getCurrentInstance } from "vue";


const { proxy } = getCurrentInstance()

//Router instance
const router = useRouter()

//Reactive data
const isCollapse = ref(false)
const activePath = ref('')
const defaultOpeneds = ref(['1','2','3','4'])

const menuList = [
  {
    id: 1,
    title: 'Quản lý Blog',
    children: [
      {
        id: 11,
        title: 'Viết Blog',
        children: [],
        path: '/write'
      },
      {
        id: 12,
        title: 'Danh sách Blog',
        children: [],
        path: '/blogs'
      },
      {
        id: 13,
        title: 'Danh sách danh mục',
        children: [],
        path: '/types'
      },
      {
        id: 14,
        title: 'Danh sách thẻ',
        children: [],
        path: '/tags'
      }
    ]
  },
  {
    id: 2,
    title: 'Quản lý bình luận',
    children: [
      {
        id: 21,
        title: 'Danh sách bình luận',
        children: [],
        path: '/comments'
      },
      {
        id: 22,
        title: 'Thùng rác',
        children: [],
        path: '/comments/trashes'
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
      }
    ]
  },
  {
    id: 4,
    title: 'Giám sát hệ thống',
    children: [
      {
        id: 41,
        title: 'Nhật ký hệ thống',
        children: [],
        path: '/log'
      }
    ]
  }
]

const iconsObj = {
  '1': 'el-icon-menu',
  '2': 'el-icon-s-order',
  '3': 'el-icon-s-data',
  '4': 'el-icon-s-tools',
  '11': 'el-icon-edit',
  '12': 'el-icon-s-order',
  '13': 'el-icon-s-opportunity',
  '14': 'el-icon-discount',
  '21': 'el-icon-tickets',
  '22': 'el-icon-delete',
  '31': 'el-icon-s-marketing',
  '32': 'el-icon-view',
  '41': 'el-icon-document',
}

// Lifecycle
onMounted(() => {
  activePath.value = window.sessionStorage.getItem('activePath') || ''
})

// Các phương thức
const logout = () => {
  window.sessionStorage.clear()
  router.push('/login')
  proxy.msgSuccess('Đăng xuất thành công')
}

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const saveNavState = (path) => {
  activePath.value = path
  window.sessionStorage.setItem('activePath',path)
}

</script>


<style scoped>
.home-container{
  height: 100%;
}

.el-header{
  background-color: #373d41;
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
  background-color: #333744;
}

.el-aside .el-menu{
  border-right: none ;
}

.el-main{
  background-color: #eaedf1;
}

.iconfont{
  margin-right: 20px;
  font-size: 20px;
}

.toggle-button {
  background-color: #4a5064;
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