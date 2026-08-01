import axios from "@/plugins/axios.js";

export function createGuestToken(){
    return axios({
        url: 'guest/bootstrap',
        method: 'GET'
    })
}