import {type ApiResponse, request} from '@/plugins/axios2'

export function login(username: string, password: string): Promise<ApiResponse<Record<any, any>>> {
    return request({
        url: 'auth/login',
        method: 'POST',
        data: {
            username,
            password
        }
    })
}