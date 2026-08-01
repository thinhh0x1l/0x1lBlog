import type {ApiErrorResponse} from "@/plugins/axios2";
import {AxiosError} from "axios";

export const handleAxiosError = (err: unknown): ApiErrorResponse => {
    const error = err as AxiosError<ApiErrorResponse>;
    if(error.response?.data){
        return error.response.data;
    }
    return {
        code: "NETWORK_ERROR",
        message: "Không thể kết nối đến server",
        status: 0,
        path: "",
        traceId: "",
        timestamp: new Date().toISOString(),
    };
}