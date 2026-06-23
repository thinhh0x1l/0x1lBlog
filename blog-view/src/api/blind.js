import { blindChallenges, blindChallengeGuesses, categories } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const blindApi = {
  getToday: async () => {
    await delay()
    const today = new Date().toISOString().split('T')[0]
    const challenge = blindChallenges.find(c => c.date === today) || blindChallenges[0]
    if (!challenge) return { data: null }
    const options = JSON.parse(challenge.options || '[]').map(o => ({
      ...o, name: categories.find(c => c.id === o.id)?.name || o.name
    }))
    return { data: { ...challenge, options } }
  },
  getMyGuess: async (userId) => {
    await delay()
    return { data: blindChallengeGuesses.find(g => g.userId === Number(userId)) || null }
  },
  submitGuess: async (challengeId, guessedTopicId) => {
    await delay()
    return { data: { id: 999, challengeId, guessedTopicId, isCorrect: null } }
  },
}
