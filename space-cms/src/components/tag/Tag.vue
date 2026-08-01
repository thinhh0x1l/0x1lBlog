<template>
  <div class="tag-container">
    <div
        class="no-underline label tag  m-1"
        :style="{
        background: `linear-gradient(
          135deg,
          ${lightenColor(tag.tagColor, 15)} 0%,
          ${tag.tagColor} 30%,
          ${darkenColor(tag.tagColor, 25)} 100%
        )`,
        boxShadow: `
          0 2px 8px ${withAlpha(tag.tagColor, 0.3)},
          0 1px 3px ${withAlpha('#000000', 0.12)},
          inset 0 1px 0 ${withAlpha('#FFFFFF', 0.1)}
        `,
        color: getContrastColor(tag.tagColor),
        fontWeight: 500,
        '--hover-glow': withAlpha(tag.tagColor, 0.5),
        '--tag-color': tag.tagColor
      }"
        @mouseenter="hoverTag = tag.tagId"
        @mouseleave="hoverTag = null"
    >
      <span class="tag-content">{{ tag.tagName }}</span>
      <span class="tag-hover-effect"></span>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  tag: {
    type: Object,
    required: true
  }
})

const hoverTag = ref(null)

const colorCache = new Map()

const adjustColor = (hex, percent) => {
  const cacheKey = `${hex}_${percent}`

  if (colorCache.has(cacheKey)) {
    return colorCache.get(cacheKey)
  }

  try {
    let cleanHex = hex.replace('#', '')

    if (cleanHex.length === 3) {
      cleanHex = cleanHex.split('').map(c => c + c).join('')
    }

    const num = parseInt(cleanHex, 16)
    const r = (num >> 16) & 255
    const g = (num >> 8) & 255
    const b = num & 255

    const amt = Math.round(2.55 * percent)

    const newR = Math.max(0, Math.min(255, r + amt))
    const newG = Math.max(0, Math.min(255, g + amt))
    const newB = Math.max(0, Math.min(255, b + amt))

    const result = '#' + (
        (1 << 24) |
        (newR << 16) |
        (newG << 8) |
        newB
    ).toString(16).slice(1).toUpperCase()

    colorCache.set(cacheKey, result)
    return result
  } catch (error) {
    console.warn('Error adjusting color:', error)
    return hex
  }
}

const lightenColor = (hex, percent) => adjustColor(hex, percent)
const darkenColor = (hex, percent) => adjustColor(hex, -percent)

const getContrastColor = (hexColor) => {
  if (!hexColor || typeof hexColor !== 'string') return '#FFFFFF'

  let hex = hexColor.replace('#', '')

  if (hex.length === 3) {
    hex = hex.split('').map(c => c + c).join('')
  }

  if (!/^[0-9A-F]{6}$/i.test(hex)) return '#FFFFFF'

  const r = parseInt(hex.substr(0, 2), 16)
  const g = parseInt(hex.substr(2, 2), 16)
  const b = parseInt(hex.substr(4, 2), 16)

  const luminance = (0.2126 * r + 0.7152 * g + 0.114 * b) / 255
  return luminance > 0.6 ? '#111827' : '#F9FAFB'
}

const withAlpha = (hex, alpha) => {
  // Ensure hex has #
  const cleanHex = hex.startsWith('#') ? hex : `#${hex}`
  const r = parseInt(cleanHex.slice(1, 3), 16)
  const g = parseInt(cleanHex.slice(3, 5), 16)
  const b = parseInt(cleanHex.slice(5, 7), 16)
  return `rgba(${r}, ${g}, ${b}, ${alpha})`
}
</script>

<style scoped>
.tag-container {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;

}

.tag {
  position: relative;
  display: inline-flex;
  align-items: center;
  margin: 0 0.5em;
  cursor: pointer;
  font-size: .85714286rem;
  text-decoration: none;
  vertical-align: middle;
  border-radius: 0 .28571429rem .28571429rem 0;
  clip-path: polygon(
      1.2em 0%,
      100%  0%,
      100% 50%,
      100% 100%,
      1.2em 100%,
      0% 50%
  );
  padding: 0.1rem 0.8rem 0.1rem 1.2rem;

  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  transform: translateZ(0);
  backface-visibility: hidden;
  overflow: hidden;

  border: 1px solid rgba(255, 255, 255, 0.1);
  border-left: none;
}


.tag:after {
  position: absolute;
  content: '';
  top: 50%;
  left: 1em;
  margin-top: -.23em;
  background-color: #fff !important;
  width: .5em;
  height: .5em;
  box-shadow:
      0 -1px 1px 0 rgba(0, 0, 0, .3),
      inset 0 1px 1px rgba(255, 255, 255, 0.8);
  border-radius: 500rem;
  transition: all 0.3s ease;
  z-index: 2;
}

.tag-content {
  padding-left: 0.8em;
  position: relative;
  z-index: 2;
  transition: all 0.3s ease;
}

.tag:hover {
  transform: translateX(10px);
  box-shadow:
      0 4px 12px rgba(0, 0, 0, 0.15),
      0 2px 4px rgba(0, 0, 0, 0.1) !important;
  filter: brightness(1.5);
}

.tag:hover:after {
  transform: scale(1.2);
  box-shadow:
      0 0 6px rgba(255, 255, 255, 0.6),
      0 -1px 1px rgba(0, 0, 0, .3),
      inset 0 1px 1px rgba(255, 255, 255, 0.9);
}

.tag:active {
  transform: translateY(0);
  filter: brightness(0.95);
}
</style>