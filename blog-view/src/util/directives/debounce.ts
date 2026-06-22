import type {Directive, DirectiveBinding} from 'vue'

/**
 * Debounce (chống rung) - Chỉ kích hoạt lần cuối sau khoảng thời gian chờ
 * Ví dụ: <el-button v-debounce="[reset, 'click', 300]">Làm mới</el-button>
 * Viết tắt: <el-button v-debounce="[reset]">Làm mới</el-button>
 */
export const debounce: Directive = {
    mounted(el: HTMLElement, binding: DirectiveBinding) {
        let [fn, event = 'click', time = 300] = binding.value
        let timer: ReturnType<typeof setTimeout> | null = null

        el.addEventListener(event, () => {
            if (timer) clearTimeout(timer)
            timer = setTimeout(() => {
                if (typeof fn === 'function') fn()
            }, time)
        })
    }
}