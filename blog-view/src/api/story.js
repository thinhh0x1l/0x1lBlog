import { stories } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const storyApi = {
  getByUser: async (userId) => {
    await delay()
    const now = new Date()
    return { data: stories.filter(s => s.userId === Number(userId) && new Date(s.expiresAt) > now).slice(0, 5) }
  },
}
