import {defineStore} from "pinia";
import {computed, ref, watch} from "vue";

export const useGuestStore = defineStore('guest', () => {
    const TOKEN_KEY = "guest_token";

    const guestToken = ref(initToken())

    const isTokenExist = computed(() =>
        !!localStorage.getItem(TOKEN_KEY));

    const setToken = (token) => {
        guestToken.value = token;
        localStorage.setItem(TOKEN_KEY, token);
    }

    const clearToken = () => {
        guestToken.value = '';
        localStorage.removeItem(TOKEN_KEY);
    };

    function setSelfToken () {
        localStorage.setItem(TOKEN_KEY, guestToken.value);
    }
    function initToken () {
        return localStorage.getItem(TOKEN_KEY) || '';
    }

    const backUpToken = () =>{
        if(!initToken()){
            setSelfToken()
            return true;
        }
        return false;
    }

    return{
        TOKEN_KEY,
        guestToken,
        isTokenExist,
        backUpToken,
        setToken,
        clearToken,
    }
})