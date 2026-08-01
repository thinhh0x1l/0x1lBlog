import { defineStore } from 'pinia'
import { storyApi } from '~/utils/storyApi'
import { enrichStory, groupByUser } from '~/utils/storyHelper'
import { mockUsers, mockStories } from '~/assets/mock/stories'

export const useStoryStore = defineStore('story', {
  state: () => ({
    userGroups: [] as any[],
    viewedMap: {} as Record<number, boolean>,
    currentGroupIndex: -1,
    viewerVisible: false,
  }),
  getters: {
    currentGroup: (state) => state.userGroups[state.currentGroupIndex] || null,
    isGroupViewed: (state) => (groupId: number) => {
      const group = state.userGroups.find(g => g.userId === groupId)
      if (!group) return false
      return group.stories.every(s => state.viewedMap[s.id])
    },
  },
  actions: {
    async fetchStories(userId: number | null) {
      const res = await storyApi.getByUser(userId)
      const enriched = (res.data || []).map(s => enrichStory(s, mockUsers))
      this.userGroups = groupByUser(enriched)
      const ownIdx = this.userGroups.findIndex(g => g.userId === Number(userId))
      if (ownIdx > 0) {
        const own = this.userGroups.splice(ownIdx, 1)
        this.userGroups.unshift(own[0])
      }
    },
    openViewer(groupIndex: number) {
      this.currentGroupIndex = groupIndex
      this.viewerVisible = true
    },
    closeViewer() {
      this.viewerVisible = false
      this.currentGroupIndex = -1
    },
    markViewed(storyId: number) {
      this.viewedMap[storyId] = true
    },
    nextGroup() {
      if (this.currentGroupIndex < this.userGroups.length - 1) {
        this.currentGroupIndex++
      } else {
        this.closeViewer()
      }
    },
  },
})
