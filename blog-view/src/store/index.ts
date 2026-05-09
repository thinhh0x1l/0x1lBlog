import { defineStore } from "pinia";
import {computed, ref, watch} from "vue";
import router from "@/router";
import {useRoute} from "vue-router";

// Interfaces
export interface SiteInfo {
    webTitleSuffix?: string;
    author?: string;
    [key: string]: any;
}

export interface Introduction {
    avatar: string;
    name: string;
    leetCode: string;
    facebook: string;
    instagram: string;
    github: string;
    email: string;
    favorites: string[];
    rollText: string[];
}

export const useAppStore = defineStore('app', () => {
    // Site info state
    const siteInfo = ref<SiteInfo>(getStorage<SiteInfo>('siteInfo', {}));
    const introduction = ref<Introduction>(getStorage<Introduction>('introduction', getDefaultIntro()));

    // watch(prevRouteName, (newName) => {console.log(newName)})
    // Getters
    const webTitleSuffix = computed(() => siteInfo.value?.webTitleSuffix || '');
    const author = computed(() => siteInfo.value?.author || '');

    // Helper functions
    function getStorage<T>(key: string, defaultValue: T): T {
        try {
            const stored = sessionStorage.getItem(key);
            return stored ? JSON.parse(stored) : defaultValue;
        } catch {
            return defaultValue;
        }
    }

    function setStorage<T>(key: string, value: T): void {
        sessionStorage.setItem(key, JSON.stringify(value));
    }

    function getDefaultIntro(): Introduction {
        return {
            avatar: '',
            name: '',
            leetCode: '',
            facebook: '',
            instagram: '',
            github: '',
            email: '',
            favorites: [],
            rollText: [],
        };
    }

    // Site info actions
    const saveSiteInfo = (data: SiteInfo) => {
        siteInfo.value = data;
        setStorage('siteInfo', data);
    };
/*
* // Required<T> - tất cả đều bắt buộc
const data: Required<Introduction> // phải có đủ fields

// Pick<T, K> - chọn một vài fields
const data: Pick<Introduction, 'name' | 'email'> // chỉ lấy name và email

// Omit<T, K> - loại bỏ một vài fields
const data: Omit<Introduction, 'password'> // tất cả trừ password

// Readonly<T> - không thể thay đổi
const data: Readonly<Introduction> // chỉ đọc, không ghi

// Partial<T> - Optional
* */
    const saveIntroduction = (data: Partial<Introduction>) => {
        introduction.value = { ...introduction.value, ...data };
        setStorage('introduction', introduction.value);
    };

    const resetIntroduction = () => {
        introduction.value = getDefaultIntro();
        setStorage('introduction', introduction.value);
    };
    return {
        // Site info
        siteInfo,
        introduction,
        webTitleSuffix,
        author,
        saveSiteInfo,
        saveIntroduction,
        resetIntroduction,
    };
});