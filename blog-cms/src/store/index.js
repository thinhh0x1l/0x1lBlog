import { defineStore } from 'pinia'

// Tạo store chính thay cho Vuex root store
export const useAppStore = defineStore('app', {
    state: () => ({
        // State sẽ được thêm vào đây
    }),

    getters: {
        // Getters sẽ được thêm vào đây
    },

    actions: {
        // Actions sẽ được thêm vào đây
    }
})