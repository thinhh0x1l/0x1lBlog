import { quests, userQuests, users } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const questApi = {
  getActiveQuests: async () => {
    await delay()
    return { data: quests.filter(q => q.isActive) }
  },
  getMyQuests: async (userId) => {
    await delay()
    return { data: userQuests.filter(q => q.userId === Number(userId)) }
  },
  claim: async (userQuestId) => {
    await delay()
    return { data: { success: true } }
  },
}
