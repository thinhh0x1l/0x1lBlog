import { users, musicComments } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const musicCommentApi = {
  getBySong: async (songId) => {
    await delay()
    const rootComments = musicComments.filter(c => c.songId === Number(songId) && !c.parentId)
    const children = musicComments.filter(c => c.songId === Number(songId) && c.parentId)
    return { data: rootComments.map(c => ({
      ...c,
      authorName: users.find(u => u.id === c.userId)?.displayName || 'Guest',
      authorAvatar: users.find(u => u.id === c.userId)?.avatarUrl,
      children: children.filter(r => r.parentId === c.id).map(r => ({
        ...r,
        authorName: users.find(u => u.id === r.userId)?.displayName || 'Guest',
        authorAvatar: users.find(u => u.id === r.userId)?.avatarUrl,
      }))
    })) }
  },
  create: async (songId, content, parentId = null) => {
    await delay()
    return { data: {
      id: Date.now(),
      songId: Number(songId),
      userId: 1,
      content,
      parentId,
      createdAt: new Date().toISOString(),
    }}
  },
  delete: async () => { await delay(); return { data: null } },
}
