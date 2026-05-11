
export function updatePageInfo(target, resData) {
    target.value.pageSize = resData.pageSize
    target.value.pageNum = resData.pageNum
    target.value.totalPages = resData.totalPages
    target.value.totalElements = resData.totalElements
}