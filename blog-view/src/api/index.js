import axios from "@/plugins/axios.js";

export function getHitokoto() {
    return axios({
        url: 'https://v1.hitokoto.cn/?c=a',
        method: 'GET',
        withCredentials: false
    })
}

export function translateUrl(a){
    return axios({
        url: `https://translate.googleapis.com/translate_a/single?client=gtx&sl=zh-CN&tl=vi&dt=t&q=${encodeURIComponent(a)}`,
        method: 'GET',
        withCredentials: false
    })
}

export function getSite(){
    return axios({
        url: 'site',
        method: 'GET'
    })
}

export function createGuestToken(){
    return axios({
        url: 'guest/bootstrap',
        method: 'GET'
    })
}