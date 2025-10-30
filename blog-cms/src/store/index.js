import { defineStore } from 'pinia'
import {ref} from "vue";

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

    return{
        activePath,
        saveNavState
    }
})