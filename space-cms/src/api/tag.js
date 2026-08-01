import axios from "@/plugins/axios.js";


export function getData(queryInfo){
    return axios({
        url: 'tags',
        method: 'GET',
        params:{
            ...queryInfo
        }
    })
}

export function createTag(tag){
    return axios({
        url: 'tag',
        method: 'POST',
        data: {
            tagName: tag.tagName,
            tagColor: tag.tagColor
        }
    })
}

export function updateTag(tag){
    return axios({
        url: 'tag',
        method: 'PUT',
        data: {
            id: tag.tagId,
            tagName: tag.tagName,
            tagColor: tag.tagColor
        }
    })
}

export function deleteTagById(id){
    return axios({
        url: `tag/${id}`,
        method: 'DELETE',

    })
}