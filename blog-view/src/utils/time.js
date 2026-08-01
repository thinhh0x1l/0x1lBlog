import dayjs from 'dayjs'
import relativeTime from 'dayjs/plugin/relativeTime'
import 'dayjs/locale/vi'
dayjs.extend(relativeTime)
dayjs.locale('vi')

export const fromNow = (date) => dayjs(date).fromNow()

export const formatDuration = (ms) => {
  const s = Math.floor(ms / 1000)
  if (s >= 60) return `${Math.floor(s / 60)}m`
  return `${s}s`
}
