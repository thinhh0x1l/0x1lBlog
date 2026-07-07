# 0x1lSpace — Blog-View UI Overhaul

**Date:** 2026-06-23
**Author:** Design System Agent
**Status:** Draft
**Approach:** B — Design System Overhaul (theme → typography → spacing → animation/polish → responsive)

---

## 1. Overview

Upgrade blog-view UI to "xịn và chuyên nghiệp hết sức có thể" — premium, cohesive, professional quality. This is a design system overhaul: rewrite SCSS tokens layer first, then systematically apply across all pages/components.

No functionality changes — purely visual/interaction upgrade. Mock data layer stays intact.

### Current State
- Design System v3 với SCSS variables + CSS custom properties + dark mode variables (chưa toggle)
- Element Plus with custom overrides
- Glassmorphism cards, gradient buttons, basic transitions
- 3-column layout (Home, Blog), 2-column (Profile), 1-column (other pages)
- 21 blog components, 7 profile widgets, design utilities

### Target State
- **Theme Engine** — CSS custom properties + dark/light toggle + localStorage persist + smooth transition
- **Typography** — modular scale (1.25), responsive, vertical rhythm, heading hierarchy
- **Spacing** — 4px base scale (0→4→8→12→16→24→32→48→64→96), component spacing tokens
- **Animation + Polish** — easing curves (ease-out/ease-in-out/bounce), stagger children, page transitions, micro-interactions (hover/focus/active), consistent radius/shadow/focus ring
- **Responsive** — mobile bottom nav, touch targets (≥44px), fine-tune breakpoints

---

## 2. Theme Engine

### 2.1 Strategy
Use `data-theme` attribute on `<html>`. Theme toggle writes attribute + persists to `localStorage('theme')`. CSS custom properties switch via `[data-theme='dark']` selector. Smooth transition on `background` / `color` / `border-color` / `box-shadow` (250ms ease-out).

### 2.2 Implementation

#### Token Layer Split

Current: `_tokens.scss` defines `:root` light + `[data-theme='dark']` in one file.

New: split into `_tokens-light.scss` and `_tokens-dark.scss`:

```scss
// _tokens-light.scss
:root,
[data-theme='light'] {
  --bg: #f8fafc;
  --bg-secondary: #f1f5f9;
  --surface: #ffffff;
  --surface-hover: #f8fafc;
  --surface-active: #f1f5f9;
  --border: #e2e8f0;
  --border-light: #f1f5f9;
  --text-primary: #0f172a;
  --text-secondary: #475569;
  --text-muted: #94a3b8;
  // ...
}
```

```scss
// _tokens-dark.scss
[data-theme='dark'] {
  --bg: #0b1121;
  --bg-secondary: #131c31;
  --surface: #1a2332;
  --surface-hover: #1e293b;
  --surface-active: #253044;
  --border: #1e293b;
  --border-light: #1a2332;
  --text-primary: #e2e8f0;
  --text-secondary: #94a3b8;
  --text-muted: #64748b;
  // ...
}
```

All components already use `var(--*)` — no component changes needed for theme switch. The dark palette gets richer shadows (colored tint), slightly adjusted purple/blue cast on dark surfaces.

#### Theme Toggle Component
```vue
<!-- ThemeToggle.vue — sun/moon icon button in header -->
<script setup>
import { useThemeStore } from '@/stores/theme'
const theme = useThemeStore()
</script>

<template>
  <el-button
    :icon="theme.isDark ? 'Sunny' : 'Moon'"
    :title="theme.isDark ? 'Chế độ sáng' : 'Chế độ tối'"
    circle
    @click="theme.toggle()"
  />
</template>
```

#### Theme Pinia Store
```typescript
// stores/theme.js
export const useThemeStore = defineStore('theme', () => {
  const isDark = ref(false)

  function apply(val: boolean) {
    isDark.value = val
    document.documentElement.setAttribute('data-theme', val ? 'dark' : 'light')
    localStorage.setItem('theme', val ? 'dark' : 'light')
  }

  function init() {
    const saved = localStorage.getItem('theme')
    const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
    apply(saved ? saved === 'dark' : prefersDark)
  }

  function toggle() {
    apply(!isDark.value)
  }

  return { isDark, init, toggle }
})
```

#### Theme Transition
```scss
// _reset.scss — add transition on all themed properties
body {
  transition: background-color var(--duration-normal) var(--ease-out),
              color var(--duration-normal) var(--ease-out);
}

// Global transition for themed elements
*,
*::before,
*::after {
  transition-property: background-color, border-color, box-shadow, color;
  transition-duration: 150ms; // override per element as needed
  transition-timing-function: ease-out;
}
```
Note: `transition: all` is expensive. Use explicit transition-property list. Elements that need animation opt-in with their own transitions.

#### Dark Mode Enhancements (beyond color swap)
| Token | Light | Dark |
|-------|-------|------|
| `--shadow-sm` | grey shadow | dark blue shadow (tinted) |
| `--shadow-md` | grey shadow | dark blue shadow (richer) |
| `--glass-bg` | rgba(255,255,255,0.72) | rgba(26,35,50,0.72) |
| `--glass-border` | rgba(255,255,255,0.3) | rgba(255,255,255,0.08) |

Add dark-mode specific tokens:
```scss
[data-theme='dark'] {
  --shadow-sm: 0 1px 3px rgba(0,0,0,0.3), 0 1px 2px rgba(0,0,0,0.2);
  --shadow-md: 0 10px 15px -3px rgba(0,0,0,0.4), 0 4px 6px -4px rgba(0,0,0,0.3);
  --shadow-lg: 0 20px 25px -5px rgba(0,0,0,0.5), 0 8px 10px -6px rgba(0,0,0,0.3);
  --glass-bg: rgba(26, 35, 50, 0.72);
  --glass-border: rgba(255, 255, 255, 0.08);
}
```

#### System Preference Detection
On `init()`, check `window.matchMedia('(prefers-color-scheme: dark)')`. If user has no saved preference, respect OS setting. Listen for `change` event to live-update (unless user explicitly toggled).

---

## 3. Typography

### 3.1 Font Scale (Modular Scale 1.25)

| Token | rem | px (16px base) | Usage |
|-------|-----|----------------|-------|
| `--fs-xs` | 0.75rem | 12px | labels, captions |
| `--fs-sm` | 0.875rem | 14px | secondary text, meta |
| `--fs-base` | 1rem | 16px | body text |
| `--fs-lg` | 1.125rem | 18px | lead paragraph |
| `--fs-xl` | 1.25rem | 20px | section subtitle |
| `--fs-2xl` | 1.5rem | 24px | h3 / page header |
| `--fs-3xl` | 1.875rem | 30px | h2 |
| `--fs-4xl` | 2.25rem | 36px | h1 |
| `--fs-5xl` | 3rem | 48px | hero title (landing) |

Add missing levels (xs, 5xl). Keep existing sm-base-lg-xl-2xl-3xl-4xl values.

### 3.2 Line Height Scale
| Token | Value | Usage |
|-------|-------|-------|
| `--lh-tight` | 1.15 | headings h1–h3 |
| `--lh-normal` | 1.4 | body, h4–h6 |
| `--lh-relaxed` | 1.625 | long-form reading |
| `--lh-loose` | 1.75 | quotes, callouts |

Current values are good. Just add `--lh-*` CSS custom properties.

### 3.3 Font Weight
| Token | Value | Usage |
|-------|-------|-------|
| `--fw-normal` | 400 | body |
| `--fw-medium` | 500 | nav, buttons |
| `--fw-semibold` | 600 | subheadings |
| `--fw-bold` | 700 | h3–h4 |
| `--fw-extrabold` | 800 | h1–h2, hero |

Add `--fw-*` CSS custom properties.

### 3.4 Heading Hierarchy
```scss
h1, .h1 {
  font-size: var(--fs-4xl);
  font-weight: var(--fw-extrabold);
  line-height: var(--lh-tight);
  letter-spacing: -0.02em;
}
h2, .h2 {
  font-size: var(--fs-3xl);
  font-weight: var(--fw-bold);
  line-height: var(--lh-tight);
  letter-spacing: -0.01em;
}
h3, .h3 {
  font-size: var(--fs-2xl);
  font-weight: var(--fw-semibold);
  line-height: var(--lh-normal);
}
h4, .h4 {
  font-size: var(--fs-xl);
  font-weight: var(--fw-semibold);
  line-height: var(--lh-normal);
}
h5, .h5 {
  font-size: var(--fs-lg);
  font-weight: var(--fw-medium);
  line-height: var(--lh-normal);
}
h6, .h6 {
  font-size: var(--fs-base);
  font-weight: var(--fw-medium);
  line-height: var(--lh-normal);
  color: var(--text-secondary); // subtle heading
}
p, .body-text {
  font-size: var(--fs-base);
  line-height: var(--lh-relaxed);
  margin-bottom: 1em;
}
.body-small {
  font-size: var(--fs-sm);
  line-height: var(--lh-normal);
  color: var(--text-secondary);
}
.label {
  font-size: var(--fs-xs);
  font-weight: var(--fw-medium);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--text-muted);
}
```

### 3.5 Vertical Rhythm
```scss
// Stack spacing
.stack-xs  > * + * { margin-top: var(--space-xs); }
.stack-sm  > * + * { margin-top: var(--space-sm); }
.stack     > * + * { margin-top: var(--space-md); }
.stack-lg  > * + * { margin-top: var(--space-lg); }
.stack-xl  > * + * { margin-top: var(--space-xl); }

// Prose (articles, blog content)
.prose {
  > * + * { margin-top: 1em; }
  > h2 { margin-top: var(--space-2xl); }
  > h3 { margin-top: var(--space-xl); }
  > h2 + h3 { margin-top: var(--space-md); }
  > p { margin-bottom: 1em; }
  > ul, > ol { margin-left: var(--space-lg); }
  > blockquote {
    margin: var(--space-lg) 0;
    padding: var(--space-md) var(--space-lg);
    border-left: 4px solid var(--primary);
    background: var(--bg-secondary);
    border-radius: 0 var(--radius) var(--radius) 0;
  }
  > pre, > code {
    font-family: var(--font-mono);
    font-size: var(--fs-sm);
  }
  > pre {
    background: var(--bg-secondary);
    border-radius: var(--radius);
    padding: var(--space-md);
    overflow-x: auto;
  }
  > img {
    border-radius: var(--radius);
    margin: var(--space-lg) auto;
  }
}
```

### 3.6 Responsive Font Sizes
```scss
@include respond-to(lg) {
  :root {
    --fs-4xl: 1.875rem; // 30px
    --fs-3xl: 1.625rem; // 26px
  }
}
@include respond-to(md) {
  html { font-size: 15px; } // gentle downscale
  :root {
    --fs-4xl: 1.625rem;
    --fs-3xl: 1.375rem;
  }
}
@include respond-to(sm) {
  html { font-size: 14px; }
  :root {
    --fs-4xl: 1.5rem;
    --fs-3xl: 1.25rem;
  }
}
```

### 3.7 Google Fonts
Current: Inter as `$font-sans`. Consider switching to system font stack for performance (already have fallback). If keeping Inter, load only `wght@400;500;600;700;800` (no italics, no variable unless needed).

---

## 4. Spacing

### 4.1 Spacing Scale (4px base)

| Token | rem | px | Usage |
|-------|-----|-----|-------|
| `--space-3xs` | 0.125rem | 2px | icon internal gap |
| `--space-2xs` | 0.25rem | 4px | tight gap |
| `--space-xs` | 0.5rem | 8px | small gap |
| `--space-sm` | 0.75rem | 12px | compact gap |
| `--space-md` | 1rem | 16px | standard gap |
| `--space-lg` | 1.5rem | 24px | section gap |
| `--space-xl` | 2rem | 32px | large gap |
| `--space-2xl` | 3rem | 48px | page section |
| `--space-3xl` | 4rem | 64px | page section |
| `--space-4xl` | 6rem | 96px | hero section |

Current: add `--space-3xs` (2px). Add `--space-*` CSS custom properties.

### 4.2 Component Spacing Tokens
```scss
// Card padding variants
.card--compact { padding: var(--space-sm) var(--space-md); }
.card--standard { padding: var(--space-md) var(--space-lg); }
.card--relaxed { padding: var(--space-lg) var(--space-xl); }

// Section spacing
.section--sm { padding: var(--space-lg) 0; }
.section--md { padding: var(--space-2xl) 0; }
.section--lg { padding: var(--space-4xl) 0; }
```

### 4.3 List/Grid Gap Tokens
```scss
$gap-xs: var(--space-xs);   // 8px  — icon rows
$gap-sm: var(--space-sm);   // 12px — compact clusters
$gap-md: var(--space-md);   // 16px — card grids
$gap-lg: var(--space-lg);   // 24px — section grids
$gap-xl: var(--space-xl);   // 32px — page grids
```

### 4.4 Consistency Rules
- **Card content padding**: always `--space-md --space-lg` (16px 24px) — unify across all cards
- **Section vertical margin**: `--space-2xl` (48px) between major sections
- **Button icon gap**: `--space-xs` (8px)
- **List item gap**: `--space-sm` (12px) in vertical lists, `--space-md` (16px) in card lists

---

## 5. Animation + Polish

### 5.1 Easing System
```scss
--ease-out: cubic-bezier(0.16, 1, 0.3, 1);         // deceleration
--ease-in-out: cubic-bezier(0.65, 0, 0.35, 1);     // standard
--ease-bounce: cubic-bezier(0.34, 1.56, 0.64, 1);  // playful
--ease-in: cubic-bezier(0.4, 0, 1, 1);              // acceleration
--ease-linear: cubic-bezier(0, 0, 1, 1);            // linear
```

Add `--ease-in` and `--ease-linear` (currently missing).

### 5.2 Duration Tokens
```scss
--duration-instant: 100ms;
--duration-fast: 150ms;
--duration-normal: 250ms;
--duration-slow: 350ms;
--duration-slower: 500ms;
--duration-slowest: 700ms;
```

Add `--duration-instant` and `--duration-slowest`.

### 5.3 Page Transitions (Vue Router)
Current: fade + translateY 12px. Upgrade to:
```vue
<!-- App.vue -->
<router-view v-slot="{ Component }">
  <transition name="page" mode="out-in">
    <component :is="Component" />
  </transition>
</router-view>
```

```scss
.page-enter-active,
.page-leave-active {
  transition: opacity var(--duration-normal) var(--ease-out),
              transform var(--duration-normal) var(--ease-out);
  position: absolute;
  width: 100%;
}
.page-enter-from { opacity: 0; transform: translateY(16px) scale(0.98); }
.page-leave-to { opacity: 0; transform: translateY(-12px) scale(0.98); }
```
`position: absolute` prevents layout jump during transition. Use `scale(0.98)` for depth.

### 5.4 Stagger Children
For lists (search results, related posts, comment tree):
```vue
<!-- StaggerWrapper.vue (utility) -->
<script setup>
defineProps({ staggerDelay: { type: Number, default: 50 } })
</script>
<template>
  <div class="stagger-wrapper">
    <slot />
  </div>
</template>
<style scoped lang="scss">
.stagger-wrapper > :deep(*) {
  opacity: 0;
  animation: fadeInUp var(--duration-slow) var(--ease-out) forwards;
}
.stagger-wrapper > :deep(*):nth-child(1) { animation-delay: 0ms; }
.stagger-wrapper > :deep(*):nth-child(2) { animation-delay: 50ms; }
.stagger-wrapper > :deep(*):nth-child(3) { animation-delay: 100ms; }
@for $i from 4 through 20 {
  .stagger-wrapper > :deep(*):nth-child(#{$i}) { animation-delay: #{($i - 1) * 50}ms; }
}
</style>
```
Used in: RelatedPosts, SearchResults, NotificationList, CommentList, TagList.

### 5.5 Micro-Interactions

| Element | State | Effect | Duration |
|---------|-------|--------|----------|
| Card | hover | `translateY(-2px)` + `shadow-md → shadow-lg` | 250ms |
| Button | hover | `translateY(-1px)` + shadow deepen | 150ms |
| Button | active | `scale(0.97)` | 100ms |
| Link inline | hover | underline via `background-size` slide | 200ms |
| Tag/Pill | hover | `translateY(-1px)` + colored border | 150ms |
| Input | focus | ring: `0 0 0 2px var(--primary-100)` + border-primary | 150ms |
| Avatar | hover | `scale(1.05)` + ring glow | 200ms |
| Reaction | click | `scale(1)→1.3→1` keyframe + count bump | 300ms |
| Bookmark | toggle | icon fill morph with color | 250ms |
| Follow | click | text morph "Follow" → "✓ Following" | 300ms |
| Progress bar | update | width transition with 300ms ease | 300ms |
| Skeleton → content | appear | `fadeIn scale(0.98→1)` | 350ms |
| Badge | appear | `scale(0)→scale(1)` bounce | 400ms ease-bounce |
| Toast/notification | enter | `slideInRight` | 350ms |
| Toast | exit | `slideOutRight` | 250ms |

### 5.6 Consistent Component Polish

#### Border Radius
- **Buttons**: `--radius` (8px) — pill/round via `--radius-full`
- **Cards**: `--radius-xl` (16px) — unified
- **Inputs**: `--radius-md` (10px)
- **Tags**: `--radius-full` (pill)
- **Modals**: `--radius-2xl` (20px)
- **Tooltips**: `--radius` (8px)
- **Dropdown menus**: `--radius` (8px)
- **Avatars**: `--radius-full` (circle)
- **Skeleton**: `--radius` (8px)

Review all components for radius consistency. Especially: `.el-card` (currently `--radius-xl` ✅), `.el-input__wrapper` (currently `--radius-md` ✅), `.el-tag` (currently `--radius-full` ✅).

#### Shadow Elevation
| Elevation | Token | Usage |
|-----------|-------|-------|
| 0 (flat) | `--shadow-xs` | cards at rest, avatars |
| 1 (low) | `--shadow-sm` | cards, dropdown hover area |
| 2 (mid) | `--shadow-md` | card hover, sticky header |
| 3 (high) | `--shadow-lg` | modal, drawer |
| 4 (top) | `--shadow-xl` | toast, tooltip, popover |

#### Focus Ring
```scss
*:focus-visible {
  outline: none;
  box-shadow: 0 0 0 2px var(--primary-100), 0 0 0 4px rgba(14,165,233,0.15);
  border-radius: var(--radius-sm);
  transition: box-shadow var(--duration-fast) ease;
}
```
Use `box-shadow` instead of `outline` for smoother look. Double-ring: inner 2px primary-100 + outer 4px primary glow.

#### Scrollbar (dark mode)
```scss
[data-theme='dark'] {
  ::-webkit-scrollbar-thumb { background: #334155; }
  ::-webkit-scrollbar-thumb:hover { background: #475569; }
}
```

#### Selection
```scss
::selection { background: var(--primary-100); color: var(--primary-dark); }
[data-theme='dark'] ::selection { background: rgba(14,165,233,0.3); color: var(--text-primary); }
```

### 5.7 Loading → Content Transition
When async data resolves:
1. Show skeleton immediately (same dimensions as real content)
2. On data resolve: skeleton fades out (150ms), content fades in (350ms) with scale 0.98→1
3. Use `<Transition mode="out-in">` per component

Create utility:
```vue
<!-- WithSkeleton.vue -->
<script setup>
defineProps({ loading: Boolean, skeletonClass: String })
</script>
<template>
  <Transition mode="out-in" name="skeleton">
    <div v-if="loading" :class="['skeleton-placeholder', skeletonClass]" key="skeleton" />
    <div v-else key="content"><slot /></div>
  </Transition>
</template>
<style scoped lang="scss">
.skeleton-enter-active, .skeleton-leave-active {
  transition: opacity 150ms ease;
}
.skeleton-enter-from, .skeleton-leave-to { opacity: 0; }
.skeleton-enter-to, .skeleton-leave-from { opacity: 1; }
</style>
```

Skeleton shapes:
- **Text lines**: 3 shimmer bars (100%, 80%, 60% width)
- **Card**: rectangle shimmer with border-radius
- **Avatar**: circle shimmer (48px/72px)
- **Image**: rectangle shimmer with aspect-ratio
- **Stats row**: 3 small shimmer pills
- **List**: multiple skeleton cards with stagger

---

## 6. Responsive

### 6.1 Breakpoint Architecture
```scss
$breakpoint-sm:  640px;  // mobile
$breakpoint-md:  768px;  // tablet portrait
$breakpoint-lg:  1024px; // tablet landscape / small desktop
$breakpoint-xl:  1280px; // desktop
$breakpoint-2xl: 1536px; // large desktop
```

Current: 3-column ≥1200px, 2-col 768–1199px, 1-col <768px.

Refinement:
| Viewport | Layout | Sidebars | Bottom Nav |
|----------|--------|----------|------------|
| ≥1200px | 3-col | both visible | hidden |
| 1024–1199px | 3-col | right narrower (220px) | hidden |
| 768–1023px | 2-col | only left | hidden |
| 640–767px | 1-col | none (content below) | shown |
| <640px | 1-col | none | shown + compact |

Added `640px` tier for small phones.

### 6.2 Mobile Bottom Navigation
```vue
<!-- MobileNav.vue — fixed bottom bar, shown <768px -->
<template>
  <nav class="mobile-nav">
    <router-link v-for="item in navItems" :key="item.path" :to="item.path" class="mobile-nav-item">
      <el-icon><component :is="item.icon" /></el-icon>
      <span class="mobile-nav-label">{{ item.label }}</span>
    </router-link>
  </nav>
</template>
```

Items: Trang chủ, Khám phá, Lưu trữ, Thông báo, Hồ sơ.

Style:
```scss
.mobile-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: var(--z-fixed);
  display: flex;
  background: var(--glass-bg);
  backdrop-filter: blur(16px);
  border-top: 1px solid var(--border-light);
  padding: var(--space-xs) 0;
  padding-bottom: env(safe-area-inset-bottom, 0);
}
.mobile-nav-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 2px;
  padding: var(--space-2xs) 0;
  color: var(--text-muted);
  font-size: var(--fs-xs);
  transition: color var(--duration-fast) ease;
  &.router-link-active { color: var(--primary); }
  .el-icon { font-size: 1.25rem; }
}
```

### 6.3 Touch Targets (≥44px)
- All interactive elements (buttons, links, inputs, tags, icons)
- Minimum 44×44px hit area (add padding if needed)
- Icon-only buttons: ensure 44×44px clickable area
- List items: full row tap zone

### 6.4 Mobile-Specific Adjustments

#### Header
- Logo left, search icon right (hide search input)
- Click search icon → overlay search drawer
- Notifications → bell icon with badge
- Theme toggle → keep (icon only)

#### Cards
- Full-bleed on mobile (remove margin, use padding)
- Compact padding: `--space-sm --space-md` instead of `--space-md --space-lg`

#### Buttons
- Full-width action buttons on mobile forms
- Floating action button (FAB) for compose

#### Typography
- Scale down headings (see §3.6)
- Body text: 16px minimum (prevents iOS zoom)

#### Sidebar → Page Content
- Left sidebar content folds into page as sections below main content
- Right sidebar content folds below left sidebar sections
- TOC → floating button (bottom-left)
- Music Box → floating mini player (bottom-right, above mobile nav)

### 6.5 Safe Area
```scss
// iPhone notch / rounded corners
.pad-safe-top { padding-top: env(safe-area-inset-top); }
.pad-safe-bottom { padding-bottom: env(safe-area-inset-bottom); }

body {
  padding-bottom: env(safe-area-inset-bottom);
}
```

---

## 7. Existing Component Audit

### 7.1 What to Keep (no changes needed)
- Color palette (primary, accent, success, warning, danger, info)
- Font stack (Inter + system fallback)
- Z-index scale
- Chart colors palette
- Glass card mixin (update bg color per theme)

### 7.2 What to Update (token-only changes)
| File | Changes |
|------|---------|
| `_variables.scss` | Add `--space-3xs`, `--fs-5xl`, `--lh-*`, `--fw-*`, `--ease-in`, `--ease-linear`, `--duration-instant`, `--duration-slowest` |
| `_tokens.scss` → split | `_tokens-light.scss` + `_tokens-dark.scss`. Add new tokens as CSS custom props |
| `_reset.scss` | Heading hierarchy with font-weight/letter-spacing. Body transition on themed props |
| `_mixins.scss` | No changes (glass, card, skeleton, focus-ring, etc. are fine) |
| `_utilities.scss` | Add stack/stack-xs/stack-sm/stack-lg/stack-xl, prose class, `.h1`–`.h6`, `.label`, `.body-small`. Dark scrollbar |
| `index.scss` | Add `.page` transition improvement, fadeInUp scale fix, stagger |

### 7.3 What to Create
| File | Purpose |
|------|---------|
| `stores/theme.js` | Pinia store for dark/light toggle + persist |
| `components/common/ThemeToggle.vue` | Sun/moon icon button |
| `components/common/MobileNav.vue` | Fixed bottom navigation <768px |
| `components/common/WithSkeleton.vue` | Loading → content transition wrapper |
| `components/common/StaggerWrapper.vue` | Stagger children animation |
| `mixins/responsive.scss` | Breakpoint maps, container queries? |

### 7.4 What to Modify (components)
| Component | Changes |
|-----------|---------|
| `AppHeader.vue` | Add `<ThemeToggle />`, hide search input on mobile |
| `AppLayout.vue` | Add `<MobileNav />`, conditionally render sidebars |
| `BlogContent.vue` | Apply `.prose` class to blog body |
| `BlogCard.vue` | Card polish (radius, shadow, hover) |
| `BlogSidebar.vue` | Stack spacing with `.stack-*` classes |
| `ProfileWidgets.vue` | Consistent card padding |
| All page views `<Transition name="page">` | Use `position: absolute` + scale |

---

## 8. Implementation Order

### Phase A — Foundation (SCSS tokens)
1. Split `_tokens.scss` → `_tokens-light.scss` + `_tokens-dark.scss`
2. Add new tokens to `_variables.scss` (3xs, 5xl, lh, fw, ease-in, ease-linear, duration-instant/slowest)
3. Write dark mode shadows + glass bg
4. Write heading hierarchy in `_reset.scss`
5. Add `.h1`–`.h6`, `.prose`, `.stack-*`, `.label`, `.body-small`, `.section-*`, `.card-*` in `_utilities.scss`
6. Responsive font scale in `_utilities.scss`
7. Update global `::selection`, `:focus-visible`, scrollbar dark

### Phase B — Theme Engine
8. Create `stores/theme.js` Pinia store
9. Create `ThemeToggle.vue` component
10. Integrate into `AppHeader.vue`
11. `App.vue` onMounted: `themeStore.init()`
12. Update glass card styles for dark theme

### Phase C — Animation System
13. Add missing duration/easing tokens
14. Update page transition in `index.scss` (position:absolute + scale)
15. Create `StaggerWrapper.vue`
16. Create `WithSkeleton.vue`
17. Apply stagger to list components
18. Apply micro-interactions (hover/focus/active) per component

### Phase D — Responsive
19. Add `640px` breakpoint to `_mixins.scss` (`@include respond-to(xs)`)
20. Create `MobileNav.vue`
21. Integrate into `AppLayout.vue`
22. Touch targets audit (min 44×44px)
23. Mobile header (search drawer)
24. Content folding from sidebars to main flow
25. CSS safe area support

### Phase E — Polish Pass
26. Consistent border-radius review
27. Consistent shadow elevation review
28. Ensure `el-button` micro-interactions (scale on active)
29. Card hover effects consistent
30. Skeleton → content transition in data-bound components

---

## 9. Key Decisions

- **Theme**: `data-theme` attribute on `<html>`, localStorage persist, OS preference detection
- **No component library upgrade**: Element Plus stays — only SCSS overrides/tokens
- **No third-party animation library**: Vue `<Transition>` + CSS only (lightweight, no GSAP/Framer)
- **No loading skeleton library**: custom `WithSkeleton` + `@include skeleton` (already have shimmer keyframe)
- **Responsive bottom nav**: native router-link (no external dependency)
- **No CSS-in-JS**: SCSS tokens + CSS custom properties — maintain current approach
- **No Tailwind**: keep utility classes approach (`.card-base`, `.text-gradient`, etc.)
- **Font loading**: Inter via Google Fonts with `display=swap` and `weights=400;500;600;700;800`

---

## 10. Migration Notes

### 10.1 Breaking Changes
- `_tokens.scss` removed → import `_tokens-light.scss` and `_tokens-dark.scss` instead
- `index.scss` imports updated: `@import 'tokens-light'; @import 'tokens-dark';` (no more single `tokens` import)
- New heading styles may affect existing h1–h6 in pages — review each page for sizing changes

### 10.2 Compatibility
- All existing `var(--*)` usages remain valid
- No Vue component API changes — all new components are additive
- Mock data layer untouched
- Build system unchanged (Vite SCSS auto-import is fine)

### 10.3 Verification
- `npm run build` passes after each phase
- Visual check: light + dark mode on Home, Blog, Profile, Search pages
- No console errors on route transitions
- No z-index issues with mobile nav (bottom bar competes with modals — ensure modal backdrop z-index > nav)

---

## Appendix A: Dark Mode Color Tail

Current dark mode is flat. Enhance with subtle colored tints:

```scss
[data-theme='dark'] {
  // Surface with subtle blue cast
  --surface: #1a2332;        // was #1e293b (slate-800) — slightly more blue
  --bg: #0b1121;             // navy-black
  --bg-secondary: #131c31;   // deep navy

  // Shadows with blue tint
  --shadow-sm: 0 1px 2px rgba(0,0,0,0.3);
  --shadow-md: 0 4px 12px rgba(0,0,0,0.4), 0 0 0 1px rgba(14,165,233,0.05);
  --shadow-lg: 0 10px 30px rgba(0,0,0,0.5), 0 0 0 1px rgba(14,165,233,0.08);
  --shadow-xl: 0 25px 50px rgba(0,0,0,0.6);

  // Overlays
  --overlay: rgba(0,0,0,0.6);
  --glass-bg: rgba(26, 35, 50, 0.8);
  --glass-border: rgba(255,255,255,0.06);

  // Primary adjust (brighter on dark)
  --primary-light: #60cffc;
  --primary-100: rgba(14,165,233,0.15);
  --primary-50: rgba(14,165,233,0.08);

  // Accent (warmer on dark)
  --accent-light: #fdba74;

  // Element Plus overrides
  --el-fill-color-light: #131c31;
  --el-border-color: #1e293b;
}
```

---

## Appendix B: Transition Performance Notes

- Prefer `transform` and `opacity` for animations (GPU-composited)
- Avoid animating `width`, `height`, `top`, `left`, `margin`, `padding`
- Use `will-change: transform` only on animating elements (sparingly)
- For page transitions, `position: absolute` prevents layout thrash
- For dark mode transition, only transition `background-color`, `color`, `border-color` — skip `box-shadow` (expensive)