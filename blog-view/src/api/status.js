import { statuses, users } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const statusApi = {
  getByUser: async (userId) => {
    await delay()
    return { data: statuses.filter(s => s.userId === Number(userId)).slice(0, 3) }
  },
}
