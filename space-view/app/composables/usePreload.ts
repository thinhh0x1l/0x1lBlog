export function usePreload() {
  let observer: IntersectionObserver | null = null

  const preload = (url: string) => {
    if (!url) return
    const img = new Image()
    img.src = url
  }

  const watchElement = (el: Element, loadFn: () => void) => {
    if (!window.IntersectionObserver) return
    observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((e) => {
          if (e.isIntersecting) {
            loadFn()
            observer?.unobserve(e.target)
          }
        })
      },
      { rootMargin: '200px' }
    )
    if (el) observer.observe(el)
  }

  const destroy = () => {
    observer?.disconnect()
    observer = null
  }

  return { preload, watchElement, destroy }
}