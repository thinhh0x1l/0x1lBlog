<template>
  <div class="navbar" :class="'nav' + settingsStore.navType" >
    <hamburger id="hamburger-container" :is-active="appStore.sidebar.opened"
               class="hamburger-container" @toggleClick="toggleSideBar"/>
    <breadcrumb id="breadcrumb-container" class="breadcrumb-container" />
    <div class="right-menu">
      <template v-if="appStore.device !== 'mobile'">

        <screenfull id="screenfull" class="right-menu-item hover-effect" />

        <el-tooltip content="Hiệu ứng theme" effect="dark" placement="bottom">
          <div class="right-menu-item hover-effect theme-switch-wrapper" @click="toggleTheme">
            <sunny v-if="settingsStore.isThemeDark" style="height: 30px; width: 30px; background: #99a9bf !important;" />
            <moon v-if="!settingsStore.isThemeDark" style="height: 30px; width: 30px"/>
<!--            <svg-icon v-if="settingsStore.isDark" icon-class="sunny" />-->
<!--            <svg-icon v-if="!settingsStore.isDark" icon-class="moon" />-->
          </div>
        </el-tooltip>

        <el-tooltip content="sdvcds" effect="dark" placement="bottom">
<!--          <size-select id="size-select" class="right-menu-item hover-effect" />-->
        </el-tooltip>

        <el-tooltip content="Thông báo" effect="dark" placement="bottom">
<!--          <header-notice id="header-notice" class="right-menu-item hover-effect" />-->
        </el-tooltip>
      </template>

      <el-dropdown @command="handleCommand" class="avatar-container right-menu-item hover-effect" trigger="hover">
        <div class="avatar-wrapper">
          <img :src="user?.avatar? user.avatar: '/img/avatar.jpg'" class="user-avatar">
          <span class="user-nickname"> {{ user.nickname }} </span>
        </div>

        <template #dropdown>
          <el-dropdown-menu>
<!--            <router-link to="/user/profile">-->
<!--              <el-dropdown-item>ccx</el-dropdown-item>-->
<!--            </router-link>-->
<!--            <el-dropdown-item command="setLayout" v-if="settingsStore.showSettings">-->
<!--              <span>xcvs</span>-->
<!--            </el-dropdown-item>-->
<!--            <el-dropdown-item command="lockScreen">-->
<!--              <span>sdv</span>-->
<!--            </el-dropdown-item>-->
            <el-dropdown-item divided command="logout">
              <span>Đăng xuất</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
</template>

<script setup>
import Hamburger from '@/components/Hamburger'
import {useSettingsStore} from "@/store/modules/settings.js";
import {useAppStore} from "@/store/index.js";
import sunny from '@/assets/icons/svg/sunny.svg'
import moon from '@/assets/icons/svg/moon.svg'
import {storeToRefs} from "pinia";
import Breadcrumb from '@/components/Breadcrumb/index.vue'
import Screenfull from '@/components/Screenfull/index.vue'
import {getCurrentInstance, onMounted} from "vue";

const route = useRoute()
const router = useRouter()
const settingsStore = useSettingsStore()
const {isThemeDark} = storeToRefs(settingsStore)
const appStore = useAppStore()

function toggleTheme(e) {
  // Lưu lại để dùng cho animation
  const isDark = isThemeDark.value

  // Bắt đầu transition
  const transition = document.startViewTransition(() => {
    // Toggle class
    if (isDark) {
      document.documentElement.classList.remove('dark')
    } else {
      document.documentElement.classList.add('dark')
    }
    isThemeDark.value = !isDark
  })

  // Tùy chọn: thêm hiệu ứng ripple từ vị trí click
  const x = e.clientX
  const y = e.clientY
  const endRadius = Math.hypot(
      Math.max(x, window.innerWidth - x),
      Math.max(y, window.innerHeight - y)
  )

  transition.ready.then(() => {
    if (x && y) {
      document.documentElement.animate(
          {
            clipPath: [
              `circle(0px at ${x}px ${y}px)`,
              `circle(${endRadius}px at ${x}px ${y}px)`
            ]
          },
          {
            duration: 800,
            easing: 'ease-in-out',
            pseudoElement: '::view-transition-new(root)'
          }
      )
    }
  })
}
const { proxy } = getCurrentInstance()
// Các phương thức
const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
  proxy.$msgSuccess('Đăng xuất thành công')
}
function handleCommand(command) {
  switch (command) {
    // case "setLayout":
    //   setLayout()
    //   break
    // case "lockScreen":
    //   lockScreen()
    //   break
    case "logout":
      logout()
      break
    default:
      break
  }
}
function toggleSideBar() {
  appStore.toggleSideBar()
}
const user = ref({})
onMounted(() => {
  const userData = localStorage.getItem('user')
  user.value = userData ? JSON.parse(userData) : {}
})
</script>

<style lang='scss' scoped>
.navbar.nav3 {
  .hamburger-container {
    display: none !important;
  }
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: var(--navbar-bg);
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  // padding: 0 8px;
  box-sizing: border-box;

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    cursor: pointer;
    transition: background 0.3s;
    -webkit-tap-highlight-color: transparent;
    display: flex;
    align-items: center;
    flex-shrink: 0;
    margin-right: 8px;

    &:hover {
      background: rgba(0, 0, 0, 0.025);
    }
  }

  .breadcrumb-container {
    flex-shrink: 0;
  }

  .topmenu-container {
    position: absolute;
    left: 50px;
  }

  .topbar-container {
    flex: 1;
    min-width: 0;
    display: flex;
    align-items: center;
    overflow: hidden;
    margin-left: 8px;
  }

  .right-menu {
    height: 100%;
    line-height: 50px;
    display: flex;
    align-items: center;
    margin-left: auto;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background 0.3s;

        &:hover {
          background: rgba(0, 0, 0, 0.025);
        }
      }

      &.theme-switch-wrapper {
        display: flex;
        align-items: center;

        svg {
          transition: transform 0.3s;

          &:hover {
            transform: scale(1.15);
          }
        }
      }
    }

    .avatar-container {
      margin-right: 0px;
      padding-right: 0px;

      .avatar-wrapper {
        margin-top: 10px;
        right: 8px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 30px;
          height: 30px;
          margin-right: 8px;
          border-radius: 50%;
        }

        .user-nickname{
          position: relative;
          left: 0px;
          bottom: 10px;
          font-size: 14px;
          font-weight: bold;
        }

        i {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>