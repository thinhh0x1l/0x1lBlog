import axios from '@/plugins/axios.js'

export function getBlogById(id){
    return axios({
        url: 'blog',
        method: 'GET',
        params: {
            id
        }
    })
}


export function increaseView(id){
    return axios({
        url:'blog/increase-view',
        method: 'POST',
        params : {id}
    })
}

export function fSearchBlog(query){
    return axios({
        url: 'search-blog',
        method: 'GET',
        params: {
            query
        }
    })
}