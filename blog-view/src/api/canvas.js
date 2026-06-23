import { canvases } from '@/data/dummy'
const delay = (ms = 300) => new Promise(r => setTimeout(r, ms))

export const canvasApi = {
  getByUser: async (userId) => {
    await delay()
    const canvas = canvases.find(c => c.userId === Number(userId) && c.isEquipped)
    return { data: canvas || null }
  },
}
