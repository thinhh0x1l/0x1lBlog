import dayjs from "dayjs";
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'

dayjs.extend(relativeTime)
dayjs.locale('vi')

export const formatDate = (value, format = 'YYYY-MM-DD HH:mm:ss') => {
    if(!value)
        return'';
    return dayjs(value).format(format);
}
export function formatRelativeTimeOrDate(time) {
    const now = dayjs()
    const t = dayjs(time)

    const diffDays = now.diff(t, 'second')

    if (diffDays <= 30) {
        return 'Online'
    }

    return t.fromNow()
}

export function isOnline(time) {
    const now = dayjs()
    const t = dayjs(time)

    const diffDays = now.diff(t, 'second')

    return diffDays <= 30

}