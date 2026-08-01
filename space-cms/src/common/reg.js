import { ref } from 'vue'

/**
 * Kiểm tra định dạng email hợp lệ
 */
export const useEmailValidator = () => {
    const emailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/

    const validateEmail = (rule, value, callback) => {
        if (!value) {
            callback(new Error('Vui lòng nhập email!!'))
            return
        }

        if (emailRegex.test(value)) {
            callback()
        } else {
            callback(new Error('Vui lòng nhập email hợp lệ'))
        }
    }

    return { validateEmail }
}

/**
 * Kiểm tra định dạng IPv4 hợp lệ
 */
export const useIPv4Validator = () => {
    const ipv4Regex = /^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$/

    const validateIPv4 = (rule, value, callback) => {
        if (!value) {
            callback(new Error('Vui lòng nhập địa chỉ IPv4'))
            return
        }

        if (ipv4Regex.test(value)) {
            callback()
        } else {
            callback(new Error('Vui lòng nhập địa chỉ IPv4 hợp lệ'))
        }
    }

    return { validateIPv4 }
}

/**
 * Kiểm tra định dạng IPv6 hợp lệ
 */
export const useIPv6Validator = () => {
    const ipv6Regex = /^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$/i

    const validateIPv6 = (rule, value, callback) => {
        if (!value) {
            callback(new Error('Vui lòng nhập địa chỉ IPv6'))
            return
        }

        if (ipv6Regex.test(value)) {
            callback()
        } else {
            callback(new Error('Vui lòng nhập địa chỉ IPv6 hợp lệ'))
        }
    }

    return { validateIPv6 }
}


export const checkEmail = (rule, value, callback) => {
    const { validateEmail } = useEmailValidator()
    return validateEmail(rule, value, callback)
}

export const checkIpv4 = (rule, value, callback) => {
    const { validateIPv4 } = useIPv4Validator()
    return validateIPv4(rule, value, callback)
}

export const checkIpv6 = (rule, value, callback) => {
    const { validateIPv6 } = useIPv6Validator()
    return validateIPv6(rule, value, callback)
}


export const useValidators = () => {
    const { validateEmail } = useEmailValidator()
    const { validateIPv4 } = useIPv4Validator()
    const { validateIPv6 } = useIPv6Validator()

    return {
        validateEmail,
        validateIPv4,
        validateIPv6
    }
}