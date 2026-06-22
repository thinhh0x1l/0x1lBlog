import { defineStore } from 'pinia'

export const useTagsViewStore = defineStore('tagsView', {
  state: () => ({
    visitedViews: [],
    cachedViews: []
  }),
  actions: {
    addView(view) {
      this.addVisitedView(view)
      this.addCachedView(view)
    },
    addVisitedView(view) {
      if (this.visitedViews.some(v => v.path === view.path)) return
      const item = Object.assign({}, view, {
        title: view.meta?.title || 'no-name'
      })
      if (view.meta?.affix) {
        this.visitedViews.unshift(item)
      } else {
        this.visitedViews.push(item)
      }
    },
    addCachedView(view) {
      const name = view.name
      if (!name) return
      if (this.cachedViews.includes(name)) return
      if (view.meta?.noCache) return
      this.cachedViews.push(name)
    },
    delView(view) {
      return new Promise(resolve => {
        this.delVisitedView(view)
        this.delCachedView(view)
        resolve({ visitedViews: [...this.visitedViews], cachedViews: [...this.cachedViews] })
      })
    },
    delVisitedView(view) {
      this.visitedViews = this.visitedViews.filter(v => v.path !== view.path)
    },
    delCachedView(view) {
      const name = view.name
      if (name) {
        this.cachedViews = this.cachedViews.filter(n => n !== name)
      }
    },
    delOthersViews(view) {
      this.visitedViews = this.visitedViews.filter(v => v.meta?.affix || v.path === view.path)
      const cacheNames = this.visitedViews.map(v => v.name).filter(Boolean)
      this.cachedViews = cacheNames
    },
    delAllViews() {
      return new Promise(resolve => {
        const affixViews = this.visitedViews.filter(v => v.meta?.affix)
        this.visitedViews = affixViews
        const cacheNames = affixViews.map(v => v.name).filter(Boolean)
        this.cachedViews = cacheNames
        resolve({ visitedViews: [...this.visitedViews], cachedViews: [...this.cachedViews] })
      })
    },
    delLeftViews(view) {
      const index = this.visitedViews.findIndex(v => v.path === view.path)
      if (index > 0) {
        const left = this.visitedViews.slice(0, index).filter(v => !v.meta?.affix)
        this.visitedViews = this.visitedViews.filter((v, i) => i >= index || v.meta?.affix)
        const leftNames = left.map(v => v.name).filter(Boolean)
        this.cachedViews = this.cachedViews.filter(n => !leftNames.includes(n))
      }
    },
    delRightViews(view) {
      const index = this.visitedViews.findIndex(v => v.path === view.path)
      if (index >= 0) {
        const right = this.visitedViews.slice(index + 1).filter(v => !v.meta?.affix)
        this.visitedViews = this.visitedViews.filter((v, i) => i <= index || v.meta?.affix)
        const rightNames = right.map(v => v.name).filter(Boolean)
        this.cachedViews = this.cachedViews.filter(n => !rightNames.includes(n))
      }
    }
  }
})
