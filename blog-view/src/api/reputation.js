import { users, userExpLog } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const reputationApi = {
  getByUser: async (userId) => {
    await delay()
    const u = users.find(u => u.id === Number(userId))
    if (!u) return { data: null }
    const totalExp = userExpLog.filter(l => l.userId === Number(userId)).reduce((s, l) => s + (l.amount || 0), 0)
    const level = Math.floor(totalExp / 1000) + 1
    return { data: { userId: u.id, level, currentExp: totalExp % 1000, nextLevelExp: 1000, totalExp } }
  },
}
