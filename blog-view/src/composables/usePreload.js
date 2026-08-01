export function usePreload() {
  let observer = null

  const preload = (url) => {
    if (!url) return
    const img = new Image()
    img.src = url
  }

  const watchElement = (el, loadFn) => {
    if (!window.IntersectionObserver) return
    observer = new IntersectionObserver((entries) => {
      entries.forEach(e => { if (e.isIntersecting) { loadFn(); observer?.unobserve(e.target) } })
    }, { rootMargin: '200px' })
    if (el) observer.observe(el)
  }

  const destroy = () => { observer?.disconnect(); observer = null }

  return { preload, watchElement, destroy }
}
