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

export function fMusicInfoBySongId(songId){
    return axios({
        url: `music/${songId}`,
        method: 'GET',
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