import { canvases, canvasStrokes } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const canvasApi = {
  getByUser: async (userId) => {
    await delay()
    const canvas = canvases.find(c => c.ownerId === Number(userId) && c.type === 'PROFILE' && c.isActive)
    return { data: canvas || null }
  },
  getById: async (id) => {
    await delay()
    return { data: canvases.find(c => c.id === Number(id)) || null }
  },
  getCommunity: async () => {
    await delay()
    return { data: canvases.filter(c => c.type === 'COMMUNITY' && c.isActive) }
  },
  getEvents: async () => {
    await delay()
    return { data: canvases.filter(c => c.type === 'EVENT' && c.isActive) }
  },
  getStrokes: async (canvasId) => {
    await delay(100)
    return { data: canvasStrokes.filter(s => s.canvasId === Number(canvasId)) }
  },
  addStroke: async (canvasId, stroke) => {
    await delay(50)
    return { data: { id: 999, canvasId: Number(canvasId), ...stroke, createdAt: new Date().toISOString() } }
  },
}
