import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'

dayjs.extend(relativeTime)
dayjs.locale('vi')

export const formatDate = (value: string | Date | null, format = 'YYYY-MM-DD') => {
  if (!value) return ''
  return dayjs(value).format(format)
}

export function formatRelativeTimeOrDate(time: string | Date) {
  const now = dayjs()
  const t = dayjs(time)

  const diffDays = now.diff(t, 'day')

  if (diffDays < 30) {
    return t.fromNow()
  }

  return t.format('DD/MM/YYYY HH:mm')
}