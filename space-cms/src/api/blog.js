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
        url: `blog/${id}`,
        method: "DELETE"
    })
}

export function updateTop(id, top){
    return axios({
        url: 'blog/top',
        method: 'PUT',
        params:{
            id,
            top
        }
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

export function updateBlogRecommendById(id, recommend){
    return axios({
        url: 'blog/recommend',
        method: 'PUT',
        data: {
            id: id,
            recommend: recommend
        }
    })
}

export function updateBlogPublishedById(id, published){
    return axios({
        url: 'blog/published',
        method: 'PUT',
        data: {
            id: id,
            published: published
        }
    })
}
export function getBlogById(id){
    return axios({
        url: `blog/${id}`,
        method: "GET"

    })
}

export function updateBlog(blog){
    return axios({
        url: 'blog',
        method: "PUT",
        data: {
            blog
        }
    })
}

