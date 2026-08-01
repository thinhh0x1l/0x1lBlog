import axios from "@/plugins/axios.js";

export function fDashboard(){
    return axios({
        method: "GET",
        url: 'dashboard',
    })
}