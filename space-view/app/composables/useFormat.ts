import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'

dayjs.extend(relativeTime)
dayjs.locale('vi')

export const useFormat = () => {
  const formatDate = (date: string | Date) => dayjs(date).format('DD/MM/YYYY')
  const formatRelative = (date: string | Date) => dayjs(date).fromNow()
  const formatCount = (count: number) => {
    if (count >= 1000000) return (count / 1000000).toFixed(1) + 'M'
    if (count >= 1000) return (count / 1000).toFixed(1) + 'K'
    return count.toString()
  }
  return { formatDate, formatRelative, formatCount }
}
