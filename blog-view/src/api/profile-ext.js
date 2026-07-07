import { users, canvases, userBadges, badges, userSkillProgress, userSkillUnlocks, skillTrees, userQuests, quests, userExpLog, profileViews, reputationHistory } from '@/data/dummy'

const delay = (ms = 200) => new Promise(r => setTimeout(r, ms))

export const profileExtApi = {
  getProfileData: async (userId) => {
    await delay()
    const u = users.find(u => u.id === Number(userId))
    if (!u) return { data: null }
    const canvas = canvases.find(c => c.ownerId === Number(userId) && c.type === 'PROFILE' && c.isActive)
    const userB = userBadges.filter(b => b.userId === Number(userId)).map(b => {
      const def = badges.find(bd => bd.id === b.badgeId)
      return def || { id: b.badgeId, displayName: 'Badge', icon: '🏆', tier: 'bronze' }
    })
    const skills = skillTrees.filter(s => s.categoryId <= 5).slice(0, 4)
    const skillProgress = userSkillProgress.filter(p => p.userId === Number(userId))
    const skillUnlocks = userSkillUnlocks.filter(u => u.userId === Number(userId))
    const activeQuests = userQuests.filter(q => q.userId === Number(userId) && q.status !== 'EXPIRED').slice(0, 3)
    const totalExp = userExpLog.filter(l => l.userId === Number(userId)).reduce((s, l) => s + (l.amount || 0), 0)
    const level = Math.floor(totalExp / 1000) + 1
    const streak = Math.floor(Math.random() * 20)

    const { blogs } = await import('@/data/dummy')
    const recentBlogs = blogs.filter(b => b.authorId === Number(userId) && b.status === 'PUBLISHED').slice(0, 3)

    const { statuses } = await import('@/data/dummy')
    const recentStatuses = statuses.filter(s => s.userId === Number(userId)).slice(0, 3)

    return {
      data: {
        user: u,
        profileLayout: u.profileLayout || [],
        gameMode: u.gameMode || false,
        canvas: canvas || null,
        badges: userB,
        skillTrees: skills,
        skillProgress,
        skillUnlocks: skillUnlocks.map(su => su.skillId),
        quests: activeQuests,
        level,
        currentExp: totalExp % 1000,
        nextLevelExp: 1000,
        totalExp,
        streak,
        repScore: u.repScore || 0,
        recentBlogs,
        recentStatuses,
        reputationHistory,
      }
    }
  },
}
