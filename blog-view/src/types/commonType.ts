

export interface ApiResponse<T = any>{
    code: number,
    msg: string,
    data: T,
}

export interface ApiErrorResponse {
    code: string;
    message: string;
    status: number;
    path: string;
    traceId: string;
    timestamp: Date;
    details?: Record<string, any>;
}

export interface PageResult<T = any> {
    pageNum: number;          // Trang hiện tại (bắt đầu từ 1)
    pageSize: number;         // Kích thước trang
    totalElements: number;            // Tổng số phần tử
    totalPages: number;            // Tổng số trang
    items: T[];                // Danh sách dữ liệu
}
export interface PageInfo {
    pageNum: number
    pageSize: number
    totalPages: number
    totalElements: number
}
