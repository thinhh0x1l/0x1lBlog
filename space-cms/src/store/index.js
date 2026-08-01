import { defineStore } from 'pinia'
import {ref, reactive} from "vue";

// Tạo store chính
export const useAppStore =
    defineStore('app', () => {
    // State
    const activePath =
        ref(window.sessionStorage.getItem('activePath')|| '')

    // Actions
    const saveNavState = (path) => {
        activePath.value = path
        window.sessionStorage.setItem('activePath',path)
    }

    const sidebar = reactive({
        opened: false,
        withoutAnimation: false,
        hide: false
    })

    const device = ref('desktop')

    const size = ref('default')

    const toggleSideBar = (withoutAnimation = false) => {
        if (sidebar.hide) return
        sidebar.opened = !sidebar.opened
        sidebar.withoutAnimation = withoutAnimation
    }

    const closeSideBar = (withoutAnimation = false) => {
        sidebar.opened = false
        sidebar.withoutAnimation = withoutAnimation
    }

    const setSize = value => {
        size.value = value
    }

    const setDevice = value => {
        device.value = value
    }

    return {
        activePath,
        sidebar,
        device,
        size,

        saveNavState,
        toggleSideBar,
        closeSideBar,
        setSize,
        setDevice
    }

})