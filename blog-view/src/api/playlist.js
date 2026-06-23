import { playlists, playlistSongs } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const playlistApi = {
  getByUser: async (userId) => {
    await delay()
    const playlist = playlists.find(p => p.ownerId === Number(userId) && p.isActive)
    if (!playlist) return { data: null }
    const songs = playlistSongs.filter(s => s.playlistId === playlist.id).sort((a, b) => a.sortOrder - b.sortOrder)
    return { data: { ...playlist, songs } }
  },
}
