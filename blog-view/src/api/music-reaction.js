import { musicReactions, musicReactionTypes } from '@/data/dummy'
const delay = (ms = 200) => new Promise(r => setTimeout(r, ms))

export const musicReactionApi = {
  getBySong: async (songId) => {
    await delay()
    const songReactions = musicReactions.filter(r => r.songId === Number(songId))
    const summary = {}
    musicReactionTypes.forEach(t => { summary[t] = songReactions.filter(r => r.type === t).length })
    return { data: summary }
  },
  toggle: async (songId, type) => {
    await delay()
    return { data: { songId: Number(songId), type } }
  },
}
