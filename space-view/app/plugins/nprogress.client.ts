import NProgress from 'nprogress'
NProgress.configure({ showSpinner: false })

export default defineNuxtPlugin(() => {
  const router = useRouter()
  router.beforeEach(() => { NProgress.start() })
  router.afterEach(() => { NProgress.done() })
})
