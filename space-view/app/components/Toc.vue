<template>
  <div class="toc-container" ref="tocRef" v-if="toc.length > 0">
    <div class="toc-header">
      <div class="toc-icon-wrapper">
        <svg class="toc-icon" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
          <path d="M4 6H20M4 12H20M4 18H20M8 6V6.01M8 12V12.01M8 18V18.01" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
          <path d="M12 6H20M12 12H20M12 18H20" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
        </svg>
      </div>
      <h3 class="toc-title">Mục lục</h3>
      <span class="toc-count">{{ flattenedToc.length }}</span>
    </div>

    <nav class="toc-nav">
      <ul class="toc-list">
        <li
            v-for="item in flattenedToc"
            :key="item.id"
            :class="[
            'toc-item',
            `toc-level-${item.level}`,
            { 'is-active': activeId === item.id }
          ]"
            :style="{ paddingLeft: (item.level - 1) * 12 + 'px' }"
        >
          <a
              :href="`#${item.id}`"
              @click.prevent="scrollToHeading(item.id)"
              class="toc-link"
          >
            <span class="toc-link-indicator"></span>
            <span class="toc-link-text">{{ item.text }}</span>
          </a>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup lang="ts">
const route = useRoute()
const props = defineProps({
  contentSelector: {
    type: String,
    default: '.blog-content'
  },
  headingSelector: {
    type: String,
    default: 'h1, h2, h3, h4'
  },
  scrollOffset: {
    type: Number,
    default: 80
  }
})

const toc = ref<any[]>([])
const activeId = ref<string | null>(null)
const tocRef = ref<HTMLElement | null>(null)
let headings: Element[] = []
let isScrolling = false
let scrollTimer: ReturnType<typeof setTimeout> | null = null
let observer: IntersectionObserver | null = null

interface TocItem {
  id: string
  text: string
  level: number
  element: Element
  top: number
  children: TocItem[]
}

const flattenedToc = computed(() => {
  const result: any[] = []
  const flatten = (items: TocItem[]) => {
    items.forEach(item => {
      result.push(item)
      if (item.children && item.children.length) {
        flatten(item.children)
      }
    })
  }
  flatten(toc.value)
  return result
})

function buildHierarchy(headingsList: any[]) {
  const root: TocItem[] = []
  const stack: TocItem[] = []
  headingsList.forEach(heading => {
    const level = heading.level
    const item: TocItem = {
      ...heading,
      children: []
    }
    while (stack.length > 0 && stack[stack.length - 1].level >= level)
      stack.pop()
    if (stack.length === 0)
      root.push(item)
    else
      stack[stack.length - 1].children.push(item)
    stack.push(item)
  })
  return root
}

function hashString(str: string) {
  let hash = 0
  for (let i = 0; i < str.length; i++) {
    hash = (hash << 5) - hash + str.charCodeAt(i)
    hash |= 0
  }
  return Math.abs(hash).toString(16)
}

function generateToc() {
  if (!headings.length) return
  const items = headings.map((heading, index) => {
    if (!heading.id)
      heading.id ||= `h-${hashString(heading.innerText+index)}`
    return {
      id: heading.id,
      text: heading.innerText,
      level: parseInt(heading.tagName[1]),
      element: heading,
      top: heading.offsetTop
    }
  })
  toc.value = buildHierarchy(items)
}

function findActiveHeading() {
  if (!headings.length) return null
  const scrollPosition = window.scrollY
  let activeHeading: Element | null = null
  for (let i = headings.length - 1; i >= 0; i--) {
    const heading = headings[i]
    const headingTop = heading.getBoundingClientRect().top + window.scrollY
    if (headingTop <= scrollPosition) {
      activeHeading = heading
      break
    }
  }
  if (!activeHeading && headings.length > 0)
    activeHeading = headings[0]
  return activeHeading
}

function updateActiveHeading() {
  if (isScrolling) return
  const activeHeading = findActiveHeading()
  if (activeHeading && activeHeading.id !== activeId.value) {
    activeId.value = activeHeading.id
  }
}

function scrollToHeading(id: string) {
  const element = document.getElementById(id)
  if (!element) return
  isScrolling = true
  const elementPosition = element.getBoundingClientRect().top
  const offsetPosition = elementPosition + window.pageYOffset - props.scrollOffset
  window.scrollTo({
    top: offsetPosition,
    behavior: 'smooth'
  })
  activeId.value = id
  if (scrollTimer) clearTimeout(scrollTimer)
  scrollTimer = setTimeout(() => {
    isScrolling = false
    setTimeout(updateActiveHeading, 100)
  }, 800)
}

function handleScroll() {
  if (isScrolling) return
  if (scrollTimer) clearTimeout(scrollTimer)
  scrollTimer = setTimeout(() => {
    updateActiveHeading()
  }, 50)
}

function initObserver() {
  if (!headings.length) return
  observer = new IntersectionObserver(
      (entries) => {
        if (isScrolling) return
        const visibleHeadings = entries
            .filter(entry => {
              const rect = entry.target.getBoundingClientRect()
              const isPastTop = rect.top <= props.scrollOffset
              const isVisible = entry.isIntersecting && isPastTop
              return isVisible
            })
            .map(entry => entry.target)
        if (visibleHeadings.length > 0) {
          const topHeading = visibleHeadings.reduce((prev, current) => {
            return prev.getBoundingClientRect().top < current.getBoundingClientRect().top ? prev : current
          })
          if (topHeading.id !== activeId.value)
            activeId.value = topHeading.id
        } else
          updateActiveHeading()
      },
      {
        threshold: [0, 0.5, 1],
        rootMargin: `-${props.scrollOffset + 30}px 0px -100px 0px`
      }
  )
  headings.forEach(heading => observer!.observe(heading))
}

watch(activeId, (newId) => {
  if (newId && tocRef.value) {
    const activeLink = tocRef.value.querySelector(`a[href="#${newId}"]`)
    if (activeLink) {
      const linkRect = activeLink.getBoundingClientRect()
      const containerRect = tocRef.value.getBoundingClientRect()
      if (linkRect.top < containerRect.top || linkRect.bottom > containerRect.bottom) {
        activeLink.scrollIntoView({
          behavior: 'smooth',
          block: 'nearest'
        })
      }
    }
  }
})

function cleanup() {
  observer?.disconnect()
  observer = null
  headings = []
  toc.value = []
  window.removeEventListener('scroll', handleScroll)
  if (scrollTimer) {
    clearTimeout(scrollTimer)
    scrollTimer = null
  }
}

function refreshToc() {
  observer?.disconnect()
  observer = null
  try {
    const selector = props.headingSelector
        .split(',')
        .map(h => `${props.contentSelector} ${h.trim()}`)
        .join(',')
    headings = Array.from(document.querySelectorAll(selector))
    generateToc()
    if (headings.length) {
      initObserver()
      updateActiveHeading()
      window.addEventListener(
          'scroll',
          handleScroll,
          { passive: true }
      )
    }
  } catch (error) {
    console.error('Error refreshToc:', error)
  }
}

onMounted(() => {
  nextTick(() => refreshToc())
})

onUnmounted(cleanup)

watch(() => route.fullPath, () => {
  cleanup()
  nextTick(() => refreshToc())
})

defineExpose({
  refreshToc,
  cleanup,
  scrollToHeading
})
</script>

<style scoped lang="scss">
.toc-container {
  position: relative;
  background: var(--surface);
  border-radius: 16px;
  padding: 1.25rem;
  box-shadow: var(--shadow-md);
  border: 1px solid var(--border-light);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  max-height: calc(100vh - 100px);
  overflow-y: auto;
}

.toc-container:hover {
  box-shadow: var(--shadow-lg);
  border-color: var(--border);
}

.toc-header {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1.25rem;
  padding-bottom: 0.75rem;
  border-bottom: 2px solid var(--border-light);
  position: relative;
}

.toc-header::after {
  content: '';
  position: absolute;
  bottom: -2px;
  left: 0;
  width: 40px;
  height: 2px;
  background: linear-gradient(90deg, var(--primary), var(--primary-dark));
  border-radius: 2px;
}

.toc-icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 32px;
  height: 32px;
  background: linear-gradient(135deg, var(--primary) 0%, var(--primary-dark) 100%);
  border-radius: 10px;
  color: white;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  box-shadow: 0 2px 8px rgba(14, 165, 233, 0.3);
}

.toc-icon-wrapper:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.4);
}

.toc-icon {
  width: 18px;
  height: 18px;
}

.toc-title {
  margin: 0;
  font-size: 1.1rem;
  font-weight: 600;
  background: linear-gradient(135deg, var(--primary-dark) 0%, var(--primary) 100%);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  letter-spacing: -0.3px;
}

.toc-count {
  margin-left: auto;
  font-size: 0.75rem;
  font-weight: 500;
  color: var(--primary);
  background: rgba(14, 165, 233, 0.1);
  padding: 2px 8px;
  border-radius: 20px;
  transition: all 0.2s ease;
}

.toc-count:hover {
  background: rgba(14, 165, 233, 0.2);
  transform: scale(1.05);
}

.toc-nav { position: relative; }

.toc-list {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  flex-direction: column;
  gap: 0.25rem;
}

.toc-item {
  position: relative;
  margin: 0;
}

.toc-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 0.75rem;
  color: var(--text-secondary);
  text-decoration: none;
  border-radius: 10px;
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  font-size: 0.875rem;
  line-height: 1.4;
  position: relative;
  overflow: hidden;
}

.toc-link::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  width: 0;
  height: 100%;
  background: linear-gradient(90deg, var(--primary-50), transparent);
  transition: width 0.3s ease;
}

.toc-link:hover::before {
  width: 100%;
}

.toc-link:hover {
  background: var(--primary-50);
  color: var(--primary);
  transform: translateX(4px);
}

.toc-link-indicator {
  width: 4px;
  height: 4px;
  background: var(--text-muted);
  border-radius: 50%;
  transition: all 0.25s ease;
}

.toc-link:hover .toc-link-indicator {
  background: var(--primary);
  transform: scale(1.5);
  box-shadow: 0 0 0 2px var(--primary-100);
}

.toc-link-text {
  flex: 1;
  font-weight: 400;
}

.toc-item.is-active .toc-link {
  background: linear-gradient(90deg, var(--primary) 0%, var(--primary-dark) 100%);
  color: white;
  box-shadow: 0 4px 12px rgba(14, 165, 233, 0.35);
}

.toc-item.is-active .toc-link-indicator {
  background: white;
  width: 8px;
  height: 8px;
  border-radius: 2px;
  transform: rotate(45deg);
  box-shadow: none;
}

.toc-item.is-active .toc-link-text {
  font-weight: 500;
}

.toc-level-1 .toc-link-text {
  font-weight: 600;
  font-size: 0.9rem;
}

.toc-level-2 .toc-link-text {
  font-weight: 500;
}

.toc-level-3 .toc-link-text {
  font-size: 0.8125rem;
  opacity: 0.9;
}

.toc-level-4 .toc-link-text {
  font-size: 0.75rem;
  opacity: 0.8;
}

.toc-container::-webkit-scrollbar {
  width: 5px;
}

.toc-container::-webkit-scrollbar-track {
  background: transparent;
  border-radius: 10px;
}

.toc-container::-webkit-scrollbar-thumb {
  background: var(--border);
  border-radius: 10px;
  transition: all 0.2s ease;
}

.toc-container::-webkit-scrollbar-thumb:hover {
  background: var(--text-muted);
}

@media (max-width: 768px) {
  .toc-container {
    max-height: 350px;
    padding: 1rem;
    border-radius: 12px;
  }

  .toc-header {
    margin-bottom: 1rem;
    padding-bottom: 0.5rem;
  }

  .toc-icon-wrapper {
    width: 28px;
    height: 28px;
  }

  .toc-icon {
    width: 16px;
    height: 16px;
  }

  .toc-title {
    font-size: 1rem;
  }

  .toc-link {
    padding: 0.375rem 0.5rem;
    font-size: 0.8125rem;
  }

  .toc-link-indicator {
    width: 3px;
    height: 3px;
  }

  .toc-item.is-active .toc-link-indicator {
    width: 6px;
    height: 6px;
  }
}

html.dark .toc-link-indicator {
  background: var(--text-muted);
}

@media print {
  .toc-container {
    background: white;
    box-shadow: none;
    border: 1px solid #ddd;
    max-height: none;
    overflow: visible;
  }

  .toc-link {
    color: black;
  }

  .toc-link:hover {
    transform: none;
  }

  .toc-icon-wrapper {
    background: var(--primary);
  }
}
</style>
