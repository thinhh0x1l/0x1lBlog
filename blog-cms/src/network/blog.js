import axios from '@/plugins/axios.js'


export function getDataQuery(query = '', categoryId = null, pageNum = 1, pageSize = 10){
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

export function getCategoryAndTag(){
    return axios({
        url: 'categoryAndTag',
        method: "GET"
    })
}

export function saveBlog(blog){
    return axios({
        url: 'blog',
        method: 'POST',
        data: {
            blog
        }
    })
}

