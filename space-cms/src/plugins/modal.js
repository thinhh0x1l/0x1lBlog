import { ElMessage, ElMessageBox, ElNotification, ElLoading } from 'element-plus'

let loadingInstance

export default {
  msg(content) {
    ElMessage.info(content)
  },
  msgError(content) {
    ElMessage.error(content)
  },
  msgSuccess(content) {
    ElMessage.success(content)
  },
  msgWarning(content) {
    ElMessage.warning(content)
  },
  alert(content) {
    ElMessageBox.alert(content, 'Thông báo')
  },
  alertError(content) {
    ElMessageBox.alert(content, 'Thông báo', { type: 'error' })
  },
  alertSuccess(content) {
    ElMessageBox.alert(content, 'Thông báo', { type: 'success' })
  },
  alertWarning(content) {
    ElMessageBox.alert(content, 'Thông báo', { type: 'warning' })
  },
  notify(content) {
    ElNotification.info(content)
  },
  notifyError(content) {
    ElNotification.error(content)
  },
  notifySuccess(content) {
    ElNotification.success(content)
  },
  notifyWarning(content) {
    ElNotification.warning(content)
  },
  confirm(content) {
    return ElMessageBox.confirm(content, 'Xác nhận', {
      confirmButtonText: 'Đồng ý',
      cancelButtonText: 'Hủy bỏ',
      type: 'warning',
    })
  },
  prompt(content) {
    return ElMessageBox.prompt(content, 'Nhập dữ liệu', {
      confirmButtonText: 'Đồng ý',
      cancelButtonText: 'Hủy bỏ',
      type: 'warning',
    })
  },
  loading(content) {
    loadingInstance = ElLoading.service({
      lock: true,
      text: content,
      background: 'rgba(0, 0, 0, 0.7)',
    })
  },
  closeLoading() {
    loadingInstance.close()
  }
}
