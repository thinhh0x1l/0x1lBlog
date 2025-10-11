import axios from '@/plugins/axios.js'

export function blogs(query = '', typeId = null, pageNum = 1, pageSize = 10){
    return axios({
        url: 'blogs',
        method: "GET",
        params: {
            query: query.trim(),
            typeId,
            pageNum,
            pageSize,
        }
    })
}

export function deleteBlogById(id){
    return axios({
        url: `blogs/${id}`,
        method: "DELETE"
    })
}
