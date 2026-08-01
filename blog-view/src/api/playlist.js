import { users, playlists, playlistSongs } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

const attachSongs = (playlist) => {
  if (!playlist) return null
  const songs = playlistSongs.filter(s => s.playlistId === playlist.id)
    .sort((a, b) => (a.sortOrder || 0) - (b.sortOrder || 0))
  return { ...playlist, songs, songCount: songs.length }
}

export const playlistApi = {
  getAll: async () => {
    await delay()
    const list = playlists.filter(p => p.isPublic).map(p => ({
      ...p,
      songCount: playlistSongs.filter(s => s.playlistId === p.id).length
    }))
    return { data: list }
  },
  getByUser: async (userId) => {
    await delay()
    const playlist = playlists.find(p => p.ownerId === Number(userId) && p.isActive)
    return { data: attachSongs(playlist) }
  },
  getById: async (id) => {
    await delay()
    return { data: attachSongs(playlists.find(p => p.id === Number(id))) }
  },
  addSong: async (playlistId, songData) => {
    await delay(200)
    return { data: { id: 999, playlistId: Number(playlistId), ...songData, addedBy: 1, voteCount: 0, isApproved: true, createdAt: new Date().toISOString() } }
  },
  removeSong: async (songId) => {
    await delay(200)
    return { data: null }
  },
  voteSong: async (songId, vote) => {
    await delay(100)
    return { data: { id: songId, voteCount: vote > 0 ? 1 : -1 } }
  },
  reorderSongs: async (playlistId, songIds) => {
    await delay(200)
    return { data: null }
  },
}
