<script setup lang="ts">
const theme = useThemeStore()

function handleToggle(e: MouseEvent) {
  const isDark = theme.isDark
  const x = e.clientX
  const y = e.clientY
  const endRadius = Math.hypot(
    Math.max(x, window.innerWidth - x),
    Math.max(y, window.innerHeight - y)
  )

  if (!document.startViewTransition) {
    theme.toggleDark()
    return
  }

  const transition = document.startViewTransition(() => {
    if (isDark) {
      document.documentElement.classList.remove('dark')
    } else {
      document.documentElement.classList.add('dark')
    }
    theme.toggleDark()
  })

  transition.ready.then(() => {
    document.documentElement.animate(
      {
        clipPath: [
          `circle(0px at ${x}px ${y}px)`,
          `circle(${endRadius}px at ${x}px ${y}px)`
        ]
      },
      {
        duration: 800,
        easing: 'ease-in-out',
        pseudoElement: '::view-transition-new(root)'
      }
    )
  })
}
</script>

<template>
  <button class="theme-toggle" :title="theme.isDark ? 'Chế độ sáng' : 'Chế độ tối'" @click="handleToggle">
    <span class="toggle-icons">
      <svg class="icon-sun" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <circle cx="12" cy="12" r="5"/>
        <line x1="12" y1="1" x2="12" y2="3"/>
        <line x1="12" y1="21" x2="12" y2="23"/>
        <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/>
        <line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/>
        <line x1="1" y1="12" x2="3" y2="12"/>
        <line x1="21" y1="12" x2="23" y2="12"/>
        <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/>
        <line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/>
      </svg>
      <svg class="icon-moon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
        <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>
      </svg>
    </span>
  </button>
</template>

<style scoped lang="scss">
.theme-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 36px;
  height: 36px;
  border: 1px solid var(--border);
  border-radius: var(--radius-md);
  background: transparent;
  color: var(--text-secondary);
  cursor: pointer;
  transition: all var(--duration-normal) var(--ease-out);
  position: relative;
  overflow: hidden;

  &:hover {
    background: var(--surface);
    color: var(--text-primary);
    border-color: var(--text-muted);
    box-shadow: var(--shadow-sm);
  }

  &:active {
    transform: scale(0.92);
  }
}

.toggle-icons {
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 18px;
  height: 18px;
}

.icon-sun,
.icon-moon {
  position: absolute;
  width: 18px;
  height: 18px;
  transition: all var(--duration-slower) var(--ease-out);
}

.icon-sun {
  opacity: 1;
  transform: scale(1);
}

.icon-moon {
  opacity: 0;
  transform: scale(0);
}

:global(html.dark) .icon-sun {
  opacity: 0;
  transform: scale(0);
}

:global(html.dark) .icon-moon {
  opacity: 1;
  transform: scale(1);
}
</style>
