import {defineStore} from "pinia";
import {computed, ref} from "vue";

export const useGuestStore = defineStore('guest', () => {
    const TOKEN_KEY = "guest_token";

    const guestToken = ref(initToken())


    const isTokenExist = computed(() => {
        return !!guestToken.value;
    });

    const setToken = (token) => {
        guestToken.value = token;
        localStorage.setItem(TOKEN_KEY, token);
    }

    const clearToken = () => {
        guestToken.value = '';
        localStorage.removeItem(TOKEN_KEY);
    };

    function initToken () {
        return localStorage.getItem(TOKEN_KEY) || '';
    }

    return{
        guestToken,
        isTokenExist,
        setToken,
        clearToken,
    }
})