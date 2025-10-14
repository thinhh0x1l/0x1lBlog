import axios from '@/plugins/axios.js'


export function blogs(query = '', categoryId = null, pageNum = 1, pageSize = 10){
    return axios({
        url: 'blogs',
        method: "GET",
        params: {
            query: query.trim(),
            categoryId,
            pageNum,
            pageSize,
            sortBy: 'create_time',
            sortOrder: 'desc',

        }
    })
}

export function deleteBlogById(id){
    return axios({
        url: `blogs/${id}`,
        method: "DELETE"
    })
}
