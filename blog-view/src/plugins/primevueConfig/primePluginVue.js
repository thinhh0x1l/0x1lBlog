// plugins/primevue.js
import PrimeVue from 'primevue/config'
import ToastService from 'primevue/toastservice'
import ConfirmationService from 'primevue/confirmationservice'
import { inject } from 'vue'


import 'primeicons/primeicons.css' // Chỉ cần 1 lần
import 'primeflex/themes/primeone-light.css'
import 'primeflex/primeflex.css'

// Import PrimeVue components
import Textarea from 'primevue/textarea'
import FloatLabel from 'primevue/floatlabel'
import Toast from 'primevue/toast'
import ConfirmDialog from 'primevue/confirmdialog'
import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Dialog from 'primevue/dialog'
import Checkbox from 'primevue/checkbox'
import Paginator from "primevue/paginator";
import RadioButton from 'primevue/radiobutton'
import FileUpload from 'primevue/fileupload'
import Message from 'primevue/message'
import Avatar from 'primevue/avatar'
import Chip from 'primevue/chip'
import Skeleton from 'primevue/skeleton'
import Image from 'primevue/image'
import Divider from 'primevue/divider'
import Menubar from 'primevue/menubar'
import Menu from 'primevue/menu'
import InputText from 'primevue/inputtext'
import Button from 'primevue/button'
import Badge from "primevue/badge"
import Card from 'primevue/card'
import Accordion from 'primevue/accordion'
import AccordionPanel from 'primevue/accordionpanel'
import Popover from 'primevue/popover'
import ToggleSwitch  from 'primevue/toggleswitch'
import Form from '@primevue/forms/form';
import FormField from '@primevue/forms/formfield';
import AutoComplete from 'primevue/autocomplete';
import {SimplePreset} from "@/plugins/primevueConfig/primeThemeConfig.js";


const PrimeVuePlugin = {
    install(app) {
        app.use(PrimeVue,{
            theme:{
                preset: SimplePreset,
                options: {
                    // prefix: 'p',
                    darkModeSelector: false,
                    cssLayer: false
                }
            }
        })
        app.use(ToastService)
        app.use(ConfirmationService)

        const components = {
            'Button': Button,
            'InputText': InputText,
            'Textarea': Textarea,
            'Toast': Toast,
            'Paginator': Paginator,
            'ConfirmDialog': ConfirmDialog,
            'DataTable': DataTable,
            'Column': Column,
            'Dialog': Dialog,
            'Checkbox': Checkbox,
            'RadioButton': RadioButton,
            'FileUpload': FileUpload,
            'Popover': Popover,
            'Message': Message,
            'Avatar': Avatar,
            'FloatLabel': FloatLabel,
            'Badge': Badge,
            'Chip': Chip,
            'Skeleton': Skeleton,
            'Image': Image,
            'Divider': Divider,
            'Menubar': Menubar,
            'Card': Card,
            'Menu': Menu,
            'AccordionPanel': AccordionPanel,
            'ToggleSwitch': ToggleSwitch,
            'Form': Form,
            'Accordion': Accordion,
            'FormField': FormField,
            'AutoComplete': AutoComplete,
        }

        Object.entries(components).forEach(([name, component]) => {
            app.component(name, component)
        })

        // Tạo toast service object (chỉ dùng inject)
        const toastService = {
            success: (msg, options = {}) => {
                app.config.globalProperties.$toast.add({
                    severity: 'success',
                    summary: options.summary || 'Thành công',
                    detail: msg,
                    life: options.life || 3000,
                    ...options
                })
            },
            error: (msg, options = {}) => {
                app.config.globalProperties.$toast.add({
                    severity: 'error',
                    summary: options.summary || 'Lỗi',
                    detail: msg,
                    life: options.life || 5000,
                    ...options
                })
            },
            info: (msg, options = {}) => {
                app.config.globalProperties.$toast.add({
                    severity: 'info',
                    summary: options.summary || 'Thông tin',
                    detail: msg,
                    life: options.life || 3000,
                    ...options
                })
            },
            warn: (msg, options = {}) => {
                app.config.globalProperties.$toast.add({
                    severity: 'warn',
                    summary: options.summary || 'Cảnh báo',
                    detail: msg,
                    life: options.life || 4000,
                    ...options
                })
            },
            showLoading: (message = 'Đang xử lý...') => {
                app.config.globalProperties.$toast.add({
                    severity: 'info',
                    summary: 'Đang xử lý',
                    detail: message,
                    life: 100000
                })
            },
            hideLoading: () => {
                app.config.globalProperties.$toast.removeAllGroups()
            },
            confirm: (options = {}) => {
                return new Promise((resolve) => {
                    app.config.globalProperties.$confirm.require({
                        message: options.message || 'Bạn có chắc chắn không?',
                        header: options.header || 'Xác nhận',
                        icon: options.icon || 'pi pi-exclamation-triangle',
                        acceptLabel: options.acceptLabel || 'Đồng ý',
                        rejectLabel: options.rejectLabel || 'Hủy',
                        acceptClass: options.acceptClass || 'p-button-danger',
                        rejectClass: options.rejectClass || 'p-button-secondary p-button-text',
                        accept: () => resolve(true),
                        reject: () => resolve(false)
                    })
                })
            }
        }

        // Provide toast service cho Composition API
        app.provide('toast', toastService)
        app.config.globalProperties.$toastService = toastService
    }
}
let globalToastService = null
export const toast = {
    success: (...args) => globalToastService?.success(...args),
    error: (...args) => globalToastService?.error(...args),
    info: (...args) => globalToastService?.info(...args),
    warn: (...args) => globalToastService?.warn(...args),
    confirm: (...args) => globalToastService?.confirm(...args),
    showLoading: (...args) => globalToastService?.showLoading(...args),
    hideLoading: () => globalToastService?.hideLoading()
}
// Composition API helper
export function useToast() {
    const toast = inject('toast')

    if (!toast) {
        throw new Error('useToast() must be used within a component that has PrimeVue plugin installed')
    }

    return toast
}

export default PrimeVuePlugin