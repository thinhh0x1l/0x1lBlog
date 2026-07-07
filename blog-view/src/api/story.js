import { users, stories } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

const enrichStory = (story) => {
  const author = users.find(u => u.id === story.userId)
  return {
    ...story,
    userName: author?.displayName || 'Người dùng',
    userAvatar: author?.avatarUrl || '',
  }
}

export const storyApi = {
  getByUser: async (userId) => {
    await delay()
    const now = new Date()
    const all = stories.filter(s => new Date(s.expiresAt) > now).map(enrichStory)
    if (!userId) return { data: all.slice(0, 30) }
    const own = all.filter(s => s.userId === Number(userId))
    if (own.length) return { data: own.slice(0, 30) }
    return { data: all.slice(0, 30) }
  },
}
