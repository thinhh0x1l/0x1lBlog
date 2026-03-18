<template>
  <div class="toc-container" ref="tocRef" v-if="toc.length > 0">
    <h3 class="toc-title">📑 Mục lục</h3>
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
            :style="{ paddingLeft: (item.level - 1) * 9 + 'px' }"
        >
          <a
              :href="`#${item.id}`"
              @click.prevent="scrollToHeading(item.id)"
              class="toc-link"
          >
            {{ item.text }}
          </a>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, nextTick, computed } from 'vue'
import {onBeforeRouteLeave, useRoute} from "vue-router";
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

const toc = ref([])
const activeId = ref(null)
const tocRef = ref(null)
let headings = []
let isScrolling = false
let scrollTimer = null
let observer = null


const flattenedToc = computed(() => {
  const result = []
  const flatten = (items) => {
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
function buildHierarchy(headingsList) {
  const root = []
  const stack = []

  headingsList.forEach(heading => {
    const level = heading.level
    const item = {
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

function hashString(str) {
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
  // console.log('TOC generated:', toc.value)
}

// Tìm heading active dựa trên vị trí scroll
function findActiveHeading() {
  if (!headings.length) return null
  const scrollPosition = window.scrollY
  // Tìm heading cuối cùng mà top của nó <= scrollPosition
  let activeHeading = null
  for (let i = headings.length - 1; i >= 0; i--) {
    const heading = headings[i]
    const headingTop = heading.offsetTop
    if (headingTop <= scrollPosition) {
      activeHeading = heading
      break
    }
  }
  // Nếu không tìm thấy, lấy heading đầu tiên
  if (!activeHeading && headings.length > 0)
    activeHeading = headings[0]
  return activeHeading
}

// Cập nhật active heading
function updateActiveHeading() {
  if (isScrolling) return
  const activeHeading = findActiveHeading()
  if (activeHeading && activeHeading.id !== activeId.value) {
    activeId.value = activeHeading.id
    // console.log('Active heading:', activeHeading.id, activeHeading.innerText) // Debug
  }
}

// Scroll đến heading
function scrollToHeading(id) {
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
  // Reset isScrolling sau khi scroll xong
  if (scrollTimer) clearTimeout(scrollTimer)
  scrollTimer = setTimeout(() => {
    isScrolling = false
    // Cập nhật lại active heading sau khi scroll
    setTimeout(updateActiveHeading, 100)
  }, 800)
}

// Handle scroll event với debounce
function handleScroll() {
  if (isScrolling) return
  if (scrollTimer) clearTimeout(scrollTimer)
  scrollTimer = setTimeout(() => {
    updateActiveHeading()
  }, 50)
}

// Khởi tạo Intersection Observer
function initObserver() {
  if (!headings.length) return
  observer = new IntersectionObserver(
      (entries) => {
        if (isScrolling) return
        // Chỉ xét headings đang thực sự visible
        const visibleHeadings = entries
            .filter(entry => {
              // Heading được coi là visible khi:
              // 1. Intersecting với viewport
              // 2. Đã scroll qua một phần (không còn ở quá cao)
              const rect = entry.target.getBoundingClientRect()
              const isPastTop = rect.top <= props.scrollOffset
              const isVisible = entry.isIntersecting && isPastTop
              return isVisible
            })
            .map(entry => entry.target)
        if (visibleHeadings.length > 0) {
          // Lấy heading trên cùng
          const topHeading = visibleHeadings.reduce((prev, current) => {
            return prev.offsetTop < current.offsetTop ? prev : current
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
  headings.forEach(heading => observer.observe(heading))
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

// watch(
//     () => route.fullPath,
//     async () => {
//       cleanup()
//       if (route.name !== 'blog') return
//       await nextTick()
//       refreshToc()
//       window.addEventListener(
//           'scroll',
//           handleScroll,
//           { passive: true }
//       )
//     },
//     { immediate: true }
// )

onUnmounted(cleanup)

defineExpose({
  refreshToc,
  cleanup,
  scrollToHeading
})
</script>

<style scoped>
.toc-container {
  padding: 1rem;
  background: #f8f9fa;
  border-radius: 8px;
  border-left: 3px solid #42b883;
  max-height: calc(100vh - 100px);
  overflow-y: auto;
  font-size: 0.95rem;
}

.toc-title {
  margin: 0 0 1rem 0;
  font-size: 1.1rem;
  font-weight: 600;
  color: #2c3e50;
}

.toc-nav {
  position: relative;
}

.toc-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.toc-item {
  margin: 2px 0;
}

.toc-link {
  display: block;
  padding: 6px 8px;
  color: #4a5568;
  text-decoration: none;
  border-radius: 4px;
  transition: all 0.2s ease;
  cursor: pointer;
  word-break: break-word;
  font-size: 0.9rem;
  line-height: 1.4;
}

.toc-link:hover {
  background: #e9ecef;
  color: #42b883;
  padding-left: 12px;
}


.toc-item.is-active > .toc-link {
  background: #42b883;
  color: white;
  font-weight: 500;
  box-shadow: 0 2px 4px rgba(66, 184, 131, 0.2);
}


.toc-level-2 .toc-link {
  font-weight: 500;
}

.toc-level-3 .toc-link {
  font-size: 0.85rem;
}

.toc-level-4 .toc-link {
  font-size: 0.8rem;
  color: #666;
}


.toc-container::-webkit-scrollbar {
  width: 4px;
}

.toc-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.toc-container::-webkit-scrollbar-thumb {
  background: #42b883;
  border-radius: 4px;
}


@media (max-width: 768px) {
  .toc-container {
    max-height: 300px;
  }
}
</style>