import axios from '@/plugins/axios.js'

export function getCommentListByQuery(queryInfo){
    return axios({
        url: 'comments',
        method: "GET",
        params: {
            ...queryInfo
        }
    })
}

export function getBlogList(){
    return axios({
        url: 'blogIdAndTitle',
        method: "GET"
    })
}

export function deleteCommentById(id){
    return axios({
        url: `comment/${id}`,
        method: 'DELETE'
    })
}

export function updateCommentPublishedById(id, published){
    return axios({
        url: `comment/published/${id}`,
        method: 'PUT',
        params:{
            published
        }
    })
}

export function updateCommentNoticeById(id, notice){
    return axios({
        url: `comment/notice/${id}`,
        method: 'PUT',
        params:{
            notice
        }
    })
}

export function updateComment(request){
    return axios({
        url: 'comment',
        method: 'PUT',
        data:{
            id: request.id,
            content: request.content,
            ip: request.ip,
            email: request.email,
            nickname: request.nickname,
        }
    })
}