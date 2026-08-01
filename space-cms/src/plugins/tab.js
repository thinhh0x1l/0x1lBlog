import { useTagsViewStore } from '@/store/modules/tagsView'
import router from '@/router'

export default {
  refreshPage(obj) {
    const { path, query, matched } = router.currentRoute.value
    if (path.startsWith('/redirect/')) return
    if (obj === undefined) {
      matched.forEach((m) => {
        if (m.components && m.components.default && m.components.default.name) {
          if (!['Layout', 'ParentView'].includes(m.components.default.name)) {
            obj = { name: m.components.default.name, path, query }
          }
        }
      })
    }
    if (!obj) return
    useTagsViewStore().delCachedView(obj)
    router.replace({ path: '/redirect' + obj.path, query: obj.query })
  },
  closeOpenPage(obj) {
    useTagsViewStore().delView(router.currentRoute.value)
    if (obj !== undefined) return router.push(obj)
  },
  closePage(obj) {
    if (obj === undefined) {
      return useTagsViewStore().delView(router.currentRoute.value).then(({ visitedViews }) => {
        const latestView = visitedViews.slice(-1)[0]
        if (latestView) return router.push(latestView.fullPath || latestView.path)
        return router.push('/')
      })
    }
    return useTagsViewStore().delView(obj)
  },
  closeAllPage() {
    return useTagsViewStore().delAllViews()
  },
  closeLeftPage(obj) {
    return useTagsViewStore().delLeftViews(obj || router.currentRoute.value)
  },
  closeRightPage(obj) {
    return useTagsViewStore().delRightViews(obj || router.currentRoute.value)
  },
  closeOtherPage(obj) {
    return useTagsViewStore().delOthersViews(obj || router.currentRoute.value)
  },
  openPage(title, url, params) {
    const obj = { path: url, meta: { title } }
    useTagsViewStore().addView(obj)
    return router.push({ path: url, query: params })
  }
}
