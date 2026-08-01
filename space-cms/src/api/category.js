import axios from '@/plugins/axios.js'

export function getDataQuery(queryInfo){
    return axios({
        url: 'categories',
        method: 'GET',
        params: {
            ...queryInfo
        }
    })
}

export function createCategory(name){
    return axios({
        url: 'categories',
        method: 'POST',
        params:{
            name
        }
    })
}

export function deleteCategoryById(id){
    return axios({
        url: `category/${id}`,
        method: "DELETE",
    })
}

export function updateCategoryById(id, name){
    return axios({
        url: `category/${id}`,
        method: "PUT",
        params: {
            name
        }
    })
}