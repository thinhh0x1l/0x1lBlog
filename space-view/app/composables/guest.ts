import { defineStore } from 'pinia'

export const useGuestStore = defineStore('guest', () => {
  const guestToken = useCookie('guestToken', { maxAge: 60 * 60 * 24 * 30 })
  const initGuest = async () => {
    if (!guestToken.value && import.meta.client) {
      guestToken.value = crypto.randomUUID()
    }
  }
  return { guestToken, initGuest }
})
