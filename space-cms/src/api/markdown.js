import axios from "@/plugins/axios.js";

export function generateHtml(text){
    return axios({
        method: "GET",
        url: 'markdown/1',
        params:{
            text
        }
    })
}
export function generateText(text){
    return axios({
        method: "GET",
        url: 'markdown/2',
        params:{
            text
        }
    })
}