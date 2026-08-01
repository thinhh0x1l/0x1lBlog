import { skillTrees, userSkillProgress, userSkillUnlocks, categories } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const skillApi = {
  getByCategory: async (categoryId) => {
    await delay()
    return { data: skillTrees.filter(s => s.categoryId === Number(categoryId)).sort((a, b) => a.sortOrder - b.sortOrder) }
  },
  getMyProgress: async (userId, categoryId) => {
    await delay()
    const progress = userSkillProgress.find(p => p.userId === Number(userId) && p.categoryId === Number(categoryId))
    const unlocks = userSkillUnlocks.filter(u => u.userId === Number(userId))
    const unlockedSkillIds = unlocks.map(u => u.skillId)
    return { data: { progress: progress || { totalPoints: 0 }, unlockedSkillIds } }
  },
}
