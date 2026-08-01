import axios from "@/plugins/axios.js";


export function fVisit(queryInfo){
    return axios({
        url: 'visit',
        method: 'GET',
        params:{
            ...queryInfo
        }
    })
}