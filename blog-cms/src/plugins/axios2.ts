
import axios from 'axios'
import type { AxiosInstance, InternalAxiosRequestConfig, AxiosResponse, AxiosError } from 'axios'
// @ts-ignore
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'
import {useGuestStore} from "@/store/guessStore";
import {pinia} from "@/store/pinia/pinia";

export interface ApiResponse<T = any>{
    code: number,
    msg: string,
    data?: T,
}

export interface ApiErrorResponse {
    code: string;
    message: string;
    status: number;
    path: string;
    traceId: string;
    timestamp: string;
    details?: Record<string, any>;
}

export interface PageInfo<T = any> {
    pageNum: number;          // Trang hiện tại (bắt đầu từ 1)
    pageSize: number;         // Kích thước trang
    size: number;             // Số phần tử trong trang hiện tại
    startRow: number;         // Dòng bắt đầu
    endRow: number;           // Dòng kết thúc
    total: number;            // Tổng số phần tử
    pages: number;            // Tổng số trang
    list: T[];                // Danh sách dữ liệu
    hasPreviousPage: boolean; // Có trang trước không
    hasNextPage: boolean;     // Có trang sau không
}

export const request: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8090/admin/',
    timeout: 10000,
})

request.interceptors.request.use(
    (config: InternalAxiosRequestConfig): InternalAxiosRequestConfig => {
        NProgress.start()

        const guestStore =
            useGuestStore(pinia);
        const isYourApi =  !(config.url)?.includes("http");
        if (  isYourApi && guestStore.guestToken ) {
            config.headers["X-Guest-Token"] = guestStore.guestToken;
        }

        const token = window.sessionStorage.getItem('token')
        if (token) {
            config.headers.Authorization = `Bearer ${token}`
        }
        const tradeId = crypto.randomUUID();
        config.headers["X-Trace-Id"] = tradeId;
        (config as any).tradeId = tradeId
        return config
    }
)

request.interceptors.response.use(
    (response: AxiosResponse) => {
        NProgress.done()
        const guestStore =
            useGuestStore(pinia);
        const newToken =response.headers[ "x-guest-token" ] as string | undefined;
        if (newToken) {
            guestStore.setToken( newToken  );
        }

        return response.data
    }
)
