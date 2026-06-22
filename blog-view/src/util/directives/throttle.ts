import type { Directive, DirectiveBinding } from 'vue'

/**
 * Throttle (điều tiết) - Mỗi khoảng thời gian chỉ kích hoạt một lần
 * Ví dụ: <el-button v-throttle="[reset, 'click', 300]">Làm mới</el-button>
 * Truyền tham số: <el-button v-throttle="[() => reset(param), 'click', 300]">Làm mới</el-button>
 */
export const throttle: Directive = {
    mounted(el: HTMLElement, binding: DirectiveBinding) {
        let [fn, event = 'click', time = 300] = binding.value
        let now: number
        let preTime: number | null = null

        el.addEventListener(event, () => {
            now = Date.now()
            if (preTime === null || now - preTime > time) {
                preTime = now
                if (typeof fn === 'function') fn()
            }
        })
    }
}