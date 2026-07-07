# Blog-View UI Overhaul — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Upgrade blog-view UI to premium professional quality — theme engine, typography, spacing, animation/polish, responsive.

**Architecture:** Design system overhaul from inside out. Start with SCSS token layer (variables → CSS custom properties), then add theme engine (dark/light toggle), then animation system, then responsive, then polish pass. All existing `var(--*)` usages remain valid — no Vue component API changes.

**Tech Stack:** Vue 3.5 (Composition API), Vite 7, SCSS (auto-imported `_variables.scss` + `_mixins.scss`), Element Plus 2, Pinia 3

**Plan location:** `docs/superpowers/plans/2026-06-23-blog-view-ui-overhaul.md`
**Spec location:** `docs/superpowers/specs/2026-06-23-blog-view-ui-overhaul-spec.md`

## Global Constraints

- No new npm packages — all work uses existing dependencies (Vue, Pinia, Element Plus)
- No Vue component API changes — all existing `var(--*)` CSS custom properties remain valid
- Mock data layer untouched
- `npm run build` must pass after every task
- All SCSS is auto-imported globally via Vite — no `@use` needed in components
- Use `ref()` from `'vue'` — `$ref()` transform deprecated since Vue 3.5+
- Pinia stores use Composition API (`defineStore('name', () => { ... })`)

---

## File Map

### Files to Create
| File | Responsibility |
|------|----------------|
| `blog-view/src/assets/scss/_tokens-light.scss` | Light theme CSS custom properties |
| `blog-view/src/assets/scss/_tokens-dark.scss` | Dark theme CSS custom properties |
| `blog-view/src/stores/theme.js` | Pinia store — dark/light toggle, localStorage persist, OS detection |
| `blog-view/src/components/common/ThemeToggle.vue` | Sun/moon icon button for header |
| `blog-view/src/components/common/MobileNav.vue` | Fixed bottom navigation bar <768px |
| `blog-view/src/components/common/WithSkeleton.vue` | Loading skeleton → content transition wrapper |
| `blog-view/src/components/common/StaggerWrapper.vue` | Stagger children animation for lists |

### Files to Modify
| File | Changes |
|------|---------|
| `blog-view/src/assets/scss/_variables.scss` | Add `$space-3xs`, `$font-size-5xl`, `$line-height-*` vars, `$ease-in`, `$ease-linear`, `$duration-instant`, `$duration-slowest` |
| `blog-view/src/assets/scss/_tokens.scss` | Delete — replaced by `_tokens-light.scss` + `_tokens-dark.scss` |
| `blog-view/src/assets/scss/_reset.scss` | Add heading hierarchy (h1–h6 with font-weight/letter-spacing), body transition on themed props |
| `blog-view/src/assets/scss/_mixins.scss` | Add `$breakpoint-xs: 640px` + `respond-to(xs)` |
| `blog-view/src/assets/scss/_utilities.scss` | Add `.stack-*`, `.prose`, `.h1`–`.h6`, `.label`, `.body-small`, `.section-*`, `.card-*`, responsive font scale, dark scrollbar, dark selection |
| `blog-view/src/assets/scss/index.scss` | Update imports (light + dark instead of tokens), page transition with position:absolute + scale, `.section-title` consistency |
| `blog-view/src/App.vue` | Add theme store init on mount, `<Transition>` wrapper for `<router-view>` |
| `blog-view/src/components/layout/AppHeader.vue` | Add `<ThemeToggle />`, mobile search input → icon |
| `blog-view/src/components/layout/AppLayout.vue` | Add `<MobileNav />` (conditionally rendered), sidebar visibility logic for 640px breakpoint |

---

## Tasks

### Phase A: SCSS Foundation

---

### Task 1: Add new SCSS variables

**Files:**
- Modify: `blog-view/src/assets/scss/_variables.scss`

**Interfaces:**
- Consumes: existing SCSS variable naming patterns
- Produces: new SCSS variables imported by `_tokens.scss` → CSS custom properties

- [ ] **Step 1: Add spacing/typography/animation variables**

Add to `_variables.scss` after existing variables:

```scss
// ===== Extended Spacing =====
$space-3xs: 2px;  // already exists as $space-2xs? Currently $space-2xs: 2px.
// Actually: current has $space-2xs: 2px and $space-xs: 4px.
// We need $space-3xs: 2px is misleading. Let me check...
// Current: 2px=$space-2xs, 4px=$space-xs, 8px=$space-sm, 16px=$space-md
// New scale: 2px stays $space-2xs, add $space-3xs is redundant.
// Just add what's missing.
```

Wait — I need to check current variables first before writing.

Let me look at the current file more carefully.

Current spacing:
- $space-2xs: 2px
- $space-xs: 4px
- $space-sm: 8px
- $space-md: 16px
- $space-lg: 24px
- $space-xl: 32px
- $space-2xl: 48px
- $space-3xl: 64px
- $space-4xl: 80px

But the spec says:
- 3xs: 2px (currently 2xs = 2px — rename conflict!)
- 2xs: 4px (currently xs = 4px)
- xs: 8px (currently sm = 8px)
- sm: 12px (currently missing — gap between 8 and 16)
- md: 16px
- lg: 24px
- xl: 32px
- 2xl: 48px
- 3xl: 64px
- 4xl: 96px (currently 80px — change!)

The spec renames the scale. But the spec also says "Existing token names remain valid." These are SCSS variables not CSS custom props, so they're internal.

Hmm, but changing `$space-4xl: 96px` from `80px` is a breaking change. And renaming `$space-2xs` from `2px` to `4px` is definitely breaking.

I think the better approach is: keep existing variable names, add missing ones, but don't rename existing ones. The spec's spacing table is aspirational. Let me adapt:

New additions to `_variables.scss`:
- $space-sm: 12px (wait, it's already 8px. Add $space-12: 12px? No, just use the semantic names)
- Actually, let me just add the missing CSS custom properties and keep SCSS vars as-is. The CSS custom properties are what components actually use.

Let me re-read the spec's spacing section more carefully:

The spec's spacing table shows a mapping of token → value. These are CSS custom property tokens, not SCSS variables. The spec says:

> Current: add `--space-3xs` (2px). Add `--space-*` CSS custom properties.

So the plan should be: add the SCSS variable `$space-12: 12px` (since there's no 12px one currently), and add CSS custom properties for the new ones. The existing 2px/4px/8px/16px/24px/32px/48px/64px/80px stay as-is.

Actually, for font-size the spec says "Add missing levels (xs, 5xl)." Current font sizes:
- xs: 0.75rem (fine, 12px)
- sm: 0.875rem (14px)
- base: 1rem (16px)
- lg: 1.125rem (18px)
- xl: 1.25rem (20px)
- 2xl: 1.5rem (24px)
- 3xl: 1.875rem (30px)
- 4xl: 2.25rem (36px)

Missing: 5xl (3rem, 48px). The spec also mentions "xs" is missing but we already have xs. So we just need 5xl.

For line-height — current has `$line-height-tight: 1.25`, `$line-height-normal: 1.5`, `$line-height-relaxed: 1.625`, `$line-height-loose: 1.75`. These map fine. Just need CSS custom props.

OK let me write the actual implementation plan with real code. I'll be very specific.

- [ ] **Step 1: Add new SCSS variables to `_variables.scss`**

Insert after `$space-4xl: 80px;`:
```scss
$space-12: 12px; // gap between 8px and 16px
```

Insert after `$font-size-4xl: 2.25rem;`:
```scss
$font-size-5xl: 3rem;
```

Insert after `$line-height-loose: 1.75;`:
```scss
// Add CSS custom property equivalents (already exist as SCSS vars)
// No new SCSS line-height vars needed
```

Insert after `$duration-slower: 500ms;`:
```scss
$duration-instant: 100ms;
$duration-slowest: 700ms;
```

Insert after `$ease-bounce: cubic-bezier(0.34, 1.56, 0.64, 1);`:
```scss
$ease-in: cubic-bezier(0.4, 0, 1, 1);
$ease-linear: cubic-bezier(0, 0, 1, 1);
```

- [ ] **Step 2: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes with no errors.

- [ ] **Step 3: Commit**

```bash
git add blog-view/src/assets/scss/_variables.scss
git commit -m "feat(blog-view): add new SCSS variables for spacing, font-size, animation tokens"
```

---

### Task 2: Split tokens into light + dark + add new CSS custom props

**Files:**
- Delete: `blog-view/src/assets/scss/_tokens.scss`
- Create: `blog-view/src/assets/scss/_tokens-light.scss`
- Create: `blog-view/src/assets/scss/_tokens-dark.scss`

**Interfaces:**
- Consumes: SCSS variables from `_variables.scss` (auto-imported)
- Produces: CSS custom properties on `:root, [data-theme='light']` and `[data-theme='dark']`

- [ ] **Step 1: Create `_tokens-light.scss`**

```scss
// ============================================
// Design Tokens — Light Theme
// ============================================

@import 'variables';

:root,
[data-theme='light'] {
  --primary: #{$primary};
  --primary-dark: #{$primary-dark};
  --primary-light: #{$primary-light};
  --primary-50: #{$primary-50};
  --primary-100: #{$primary-100};

  --accent: #{$accent};
  --accent-light: #{$accent-light};

  --success: #{$success};
  --warning: #{$warning};
  --danger: #{$danger};
  --info: #{$info};

  --bg: #{$bg};
  --bg-secondary: #{$bg-secondary};
  --surface: #{$surface};
  --surface-hover: #{$surface-hover};
  --surface-active: #{$surface-active};
  --border: #{$border};
  --border-light: #{$border-light};
  --border-focus: #{$border-focus};

  --text-primary: #{$text-primary};
  --text-secondary: #{$text-secondary};
  --text-muted: #{$text-muted};
  --text-inverse: #{$text-inverse};
  --text-link: #{$text-link};
  --text-link-hover: #{$text-link-hover};

  --shadow-xs: #{$shadow-xs};
  --shadow-sm: #{$shadow-sm};
  --shadow: #{$shadow};
  --shadow-md: #{$shadow-md};
  --shadow-lg: #{$shadow-lg};
  --shadow-xl: #{$shadow-xl};
  --shadow-inner: #{$shadow-inner};
  --shadow-colored: #{$shadow-colored};
  --shadow-colored-accent: #{$shadow-colored-accent};
  --shadow-colored-info: #{$shadow-colored-info};

  --radius-xs: #{$radius-xs};
  --radius-sm: #{$radius-sm};
  --radius: #{$radius};
  --radius-md: #{$radius-md};
  --radius-lg: #{$radius-lg};
  --radius-xl: #{$radius-xl};
  --radius-2xl: #{$radius-2xl};
  --radius-3xl: #{$radius-3xl};
  --radius-full: #{$radius-full};

  --space-2xs: #{$space-2xs};
  --space-xs: #{$space-xs};
  --space-sm: #{$space-sm};
  --space-12: #{$space-12};
  --space-md: #{$space-md};
  --space-lg: #{$space-lg};
  --space-xl: #{$space-xl};
  --space-2xl: #{$space-2xl};
  --space-3xl: #{$space-3xl};
  --space-4xl: #{$space-4xl};

  --header-height: #{$header-height};
  --sidebar-width: #{$sidebar-width};
  --page-max-width: #{$page-max-width};
  --content-max-width: #{$content-max-width};

  --font-sans: #{$font-sans};
  --font-mono: #{$font-mono};

  // Typography tokens (new CSS custom props)
  --fs-xs: #{$font-size-xs};
  --fs-sm: #{$font-size-sm};
  --fs-base: #{$font-size-base};
  --fs-lg: #{$font-size-lg};
  --fs-xl: #{$font-size-xl};
  --fs-2xl: #{$font-size-2xl};
  --fs-3xl: #{$font-size-3xl};
  --fs-4xl: #{$font-size-4xl};
  --fs-5xl: #{$font-size-5xl};

  --fw-normal: #{$font-weight-normal};
  --fw-medium: #{$font-weight-medium};
  --fw-semibold: #{$font-weight-semibold};
  --fw-bold: #{$font-weight-bold};
  --fw-extrabold: #{$font-weight-extrabold};

  --lh-tight: #{$line-height-tight};
  --lh-normal: #{$line-height-normal};
  --lh-relaxed: #{$line-height-relaxed};
  --lh-loose: #{$line-height-loose};

  // Animation tokens (new CSS custom props)
  --ease-out: #{$ease-out};
  --ease-in-out: #{$ease-in-out};
  --ease-bounce: #{$ease-bounce};
  --ease-in: #{$ease-in};
  --ease-linear: #{$ease-linear};

  --duration-instant: #{$duration-instant};
  --duration-fast: #{$duration-fast};
  --duration-normal: #{$duration-normal};
  --duration-slow: #{$duration-slow};
  --duration-slower: #{$duration-slower};
  --duration-slowest: #{$duration-slowest};

  // Z-index
  --z-dropdown: #{$z-dropdown};
  --z-sticky: #{$z-sticky};
  --z-fixed: #{$z-fixed};
  --z-modal-backdrop: #{$z-modal-backdrop};
  --z-modal: #{$z-modal};
  --z-popover: #{$z-popover};
  --z-tooltip: #{$z-tooltip};

  // Element Plus overrides
  --el-color-primary: var(--primary);
  --el-color-primary-light-3: var(--primary-light);
  --el-color-primary-light-5: #7dd3fc;
  --el-color-primary-light-7: #bae6fd;
  --el-color-primary-light-9: var(--primary-50);
  --el-color-primary-dark-2: var(--primary-dark);
  --el-color-success: var(--success);
  --el-color-warning: var(--warning);
  --el-color-danger: var(--danger);
  --el-color-info: var(--info);
  --el-border-radius-base: var(--radius);
  --el-font-family: var(--font-sans);
  --el-border-color: var(--border);
  --el-fill-color-light: var(--bg-secondary);

  // Glass
  --glass-bg: rgba(255, 255, 255, 0.72);
  --glass-border: rgba(255, 255, 255, 0.3);
}
```

- [ ] **Step 2: Create `_tokens-dark.scss`**

```scss
// ============================================
// Design Tokens — Dark Theme
// ============================================

@import 'variables';

[data-theme='dark'] {
  --bg: #{$dark-bg};
  --bg-secondary: #{$dark-bg-secondary};
  --surface: #{$dark-surface};
  --surface-hover: #{$dark-surface-hover};
  --surface-active: #{$dark-surface-active};
  --border: #{$dark-border};
  --border-light: #{$dark-border-light};
  --text-primary: #{$dark-text-primary};
  --text-secondary: #{$dark-text-secondary};
  --text-muted: #{$dark-text-muted};
  --text-inverse: #0f172a;

  // Dark shadows (tinted blue)
  --shadow-sm: 0 1px 2px rgba(0,0,0,0.3);
  --shadow-md: 0 4px 12px rgba(0,0,0,0.4), 0 0 0 1px rgba(14,165,233,0.05);
  --shadow-lg: 0 10px 30px rgba(0,0,0,0.5), 0 0 0 1px rgba(14,165,233,0.08);
  --shadow-xl: 0 25px 50px rgba(0,0,0,0.6);

  // Dark glass
  --glass-bg: rgba(26, 35, 50, 0.8);
  --glass-border: rgba(255, 255, 255, 0.06);

  // Dark primary adjust
  --primary-light: #60cffc;
  --primary-100: rgba(14,165,233,0.15);
  --primary-50: rgba(14,165,233,0.08);

  // Dark accent
  --accent-light: #fdba74;

  // Dark Element Plus
  --el-fill-color-light: #{$dark-bg-secondary};
  --el-border-color: #{$dark-border};
}
```

- [ ] **Step 3: Delete old `_tokens.scss`**

Delete the file. The new files replace it.

- [ ] **Step 4: Update imports in `index.scss`**

Replace in `blog-view/src/assets/scss/index.scss`:
```scss
@import 'tokens';
```
with:
```scss
@import 'tokens-light';
@import 'tokens-dark';
```

- [ ] **Step 5: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 6: Commit**

```bash
git add blog-view/src/assets/scss/_tokens*.scss blog-view/src/assets/scss/index.scss
git commit -m "feat(blog-view): split tokens into light/dark, add typography and animation CSS custom props"
```

---

### Task 3: Add heading hierarchy + themed transitions to reset

**Files:**
- Modify: `blog-view/src/assets/scss/_reset.scss`

**Interfaces:**
- Consumes: CSS custom properties from `_tokens-light.scss` / `_tokens-dark.scss`
- Produces: Consistent heading styles, smooth theme transition on body

- [ ] **Step 1: Update body with theme transition**

Replace existing `body` in `_reset.scss`:
```scss
body {
  font-family: var(--font-sans);
  background: var(--bg);
  color: var(--text-primary);
  line-height: 1.6;
  min-height: 100vh;
  transition: background-color var(--duration-normal) var(--ease-out),
              color var(--duration-normal) var(--ease-out);
}
```

- [ ] **Step 2: Add heading hierarchy**

Replace existing `h1, h2, h3, h4, h5, h6` block:
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
  color: var(--text-secondary);
}
```

- [ ] **Step 3: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 4: Commit**

```bash
git add blog-view/src/assets/scss/_reset.scss
git commit -m "feat(blog-view): add heading hierarchy and theme transition to reset"
```

---

### Task 4: Add utility classes (prose, stacks, headings, dark utilities)

**Files:**
- Modify: `blog-view/src/assets/scss/_utilities.scss`

**Interfaces:**
- Consumes: CSS custom properties, SCSS mixins
- Produces: Reusable utility classes for components

- [ ] **Step 1: Add stack, card, section, prose, and heading utility classes**

Append to `_utilities.scss` before `@include respond-to(md)` block:

```scss
// ===== Stack Spacing =====
.stack-xs  > * + * { margin-top: var(--space-xs); }
.stack-sm  > * + * { margin-top: var(--space-sm); }
.stack     > * + * { margin-top: var(--space-md); }
.stack-lg  > * + * { margin-top: var(--space-lg); }
.stack-xl  > * + * { margin-top: var(--space-xl); }

// ===== Card Padding Variants =====
.card--compact { padding: var(--space-sm) var(--space-md); }
.card--standard { padding: var(--space-md) var(--space-lg); }
.card--relaxed { padding: var(--space-lg) var(--space-xl); }

// ===== Section Spacing =====
.section--sm { padding: var(--space-lg) 0; }
.section--md { padding: var(--space-2xl) 0; }
.section--lg { padding: var(--space-4xl) 0; }

// ===== Prose (blog content) =====
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

// ===== Label =====
.label {
  font-size: var(--fs-xs);
  font-weight: var(--fw-medium);
  text-transform: uppercase;
  letter-spacing: 0.08em;
  color: var(--text-muted);
}

// ===== Body Small =====
.body-small {
  font-size: var(--fs-sm);
  line-height: var(--lh-normal);
  color: var(--text-secondary);
}

// ===== Responsive Font Scale =====
@include respond-to(xl) {
  :root {
    --fs-4xl: 1.875rem;
    --fs-3xl: 1.625rem;
  }
}
@include respond-to(lg) {
  :root {
    --fs-4xl: 1.625rem;
    --fs-3xl: 1.375rem;
  }
}
@include respond-to(md) {
  html { font-size: 15px; }
  :root {
    --fs-4xl: 1.5rem;
    --fs-3xl: 1.25rem;
  }
}
@include respond-to(sm) {
  html { font-size: 14px; }
  :root {
    --fs-4xl: 1.375rem;
    --fs-3xl: 1.125rem;
  }
}
```

(Remove the existing `@include respond-to(md) { html { font-size: 14px; } }` since it's replaced by the block above.)

- [ ] **Step 2: Add dark mode scrollbar + selection**

Append after existing `::selection` block:

```scss
[data-theme='dark'] {
  ::-webkit-scrollbar-thumb { background: #334155; }
  ::-webkit-scrollbar-thumb:hover { background: #475569; }

  ::selection { background: rgba(14,165,233,0.3); color: var(--text-primary); }
}
```

- [ ] **Step 3: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 4: Commit**

```bash
git add blog-view/src/assets/scss/_utilities.scss
git commit -m "feat(blog-view): add utility classes — stack, prose, card variants, responsive font scale, dark scrollbar"
```

---

### Task 5: Add xs breakpoint to mixins

**Files:**
- Modify: `blog-view/src/assets/scss/_mixins.scss`

**Interfaces:**
- Consumes: existing mixin patterns
- Produces: `respond-to(xs)` for 640px breakpoint

- [ ] **Step 1: Add xs breakpoint**

Insert after existing breakpoint variables:
```scss
$breakpoint-xs: 640px;
```

Add to `respond-to` mixin — insert before `@else if $bp == sm`:
```scss
  @else if $bp == xs { @media (max-width: #{$breakpoint-xs}) { @content; } }
```

- [ ] **Step 2: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 3: Commit**

```bash
git add blog-view/src/assets/scss/_mixins.scss
git commit -m "feat(blog-view): add xs (640px) breakpoint to mixins"
```

---

### Phase B: Theme Engine

---

### Task 6: Create Pinia theme store

**Files:**
- Create: `blog-view/src/stores/theme.js`

**Interfaces:**
- Consumes: Vue/Pinia from existing deps
- Produces: `useThemeStore` — `isDark: ref`, `init()`, `toggle()`

- [ ] **Step 1: Create the store**

```javascript
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useThemeStore = defineStore('theme', () => {
  const isDark = ref(false)

  function apply(val) {
    isDark.value = val
    document.documentElement.setAttribute('data-theme', val ? 'dark' : 'light')
    localStorage.setItem('theme', val ? 'dark' : 'light')
  }

  function init() {
    const saved = localStorage.getItem('theme')
    if (saved) {
      apply(saved === 'dark')
    } else {
      const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches
      apply(prefersDark)
    }
    // Listen for OS changes if user hasn't explicitly chosen
    window.matchMedia('(prefers-color-scheme: dark)').addEventListener('change', (e) => {
      if (!localStorage.getItem('theme')) {
        apply(e.matches)
      }
    })
  }

  function toggle() {
    apply(!isDark.value)
  }

  return { isDark, init, toggle }
})
```

- [ ] **Step 2: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 3: Commit**

```bash
git add blog-view/src/stores/theme.js
git commit -m "feat(blog-view): add theme Pinia store with dark/light toggle and persist"
```

---

### Task 7: Create ThemeToggle component

**Files:**
- Create: `blog-view/src/components/common/ThemeToggle.vue`

**Interfaces:**
- Consumes: `useThemeStore` from `@/stores/theme`
- Produces: Self-contained toggle button component

- [ ] **Step 1: Create the component**

```vue
<script setup>
import { useThemeStore } from '@/stores/theme'

const theme = useThemeStore()
</script>

<template>
  <el-button
    :icon="theme.isDark ? 'Sunny' : 'Moon'"
    :title="theme.isDark ? 'Chế độ sáng' : 'Chế độ tối'"
    circle
    size="default"
    @click="theme.toggle()"
  />
</template>

<style scoped lang="scss">
.el-button {
  transition: transform var(--duration-fast) var(--ease-out);

  &:active {
    transform: scale(0.9);
  }
}
</style>
```

- [ ] **Step 2: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 3: Commit**

```bash
git add blog-view/src/components/common/ThemeToggle.vue
git commit -m "feat(blog-view): add ThemeToggle component with sun/moon icon"
```

---

### Task 8: Integrate theme into App

**Files:**
- Modify: `blog-view/src/App.vue`

**Interfaces:**
- Consumes: `useThemeStore`, `ThemeToggle.vue`
- Produces: App-initialized theme, theme toggle button in header

- [ ] **Step 1: Update App.vue with theme init + router view transition**

Read existing App.vue content first.

```vue
<script setup>
import { onMounted } from 'vue'
import { useThemeStore } from '@/stores/theme'

const theme = useThemeStore()

onMounted(() => {
  theme.init()
})
</script>

<template>
  <router-view v-slot="{ Component }">
    <transition name="page" mode="out-in">
      <component :is="Component" />
    </transition>
  </router-view>
</template>
```

- [ ] **Step 2: Add ThemeToggle to AppHeader**

Modify `blog-view/src/components/layout/AppHeader.vue`:
- Import and add `<ThemeToggle />` in the header right section (near notifications/user menu)

- [ ] **Step 3: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 4: Commit**

```bash
git add blog-view/src/App.vue blog-view/src/components/layout/AppHeader.vue
git commit -m "feat(blog-view): integrate theme store init and ThemeToggle into app"
```

---

### Phase C: Animation System

---

### Task 9: Update page transition styles

**Files:**
- Modify: `blog-view/src/assets/scss/index.scss`

**Interfaces:**
- Consumes: existing `.page-enter-active` / `.page-leave-active` classes
- Produces: Smooth page transitions with scale and fade

- [ ] **Step 1: Replace page transition styles**

Replace existing `.page-*` blocks in `index.scss`:
```scss
// ===== Smooth page transition =====
.page-enter-active,
.page-leave-active {
  transition: opacity var(--duration-slow) var(--ease-out),
              transform var(--duration-slow) var(--ease-out);
  position: absolute;
  width: 100%;
}

.page-enter-from {
  opacity: 0;
  transform: translateY(16px) scale(0.98);
}

.page-leave-to {
  opacity: 0;
  transform: translateY(-12px) scale(0.98);
}
```

- [ ] **Step 2: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 3: Commit**

```bash
git add blog-view/src/assets/scss/index.scss
git commit -m "feat(blog-view): update page transition with scale and absolute positioning"
```

---

### Task 10: Create StaggerWrapper component

**Files:**
- Create: `blog-view/src/components/common/StaggerWrapper.vue`

**Interfaces:**
- Produces: Stagger children animation wrapper for lists

- [ ] **Step 1: Create the component**

```vue
<script setup>
defineProps({
  staggerDelay: {
    type: Number,
    default: 50
  }
})
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
.stagger-wrapper > :deep(*):nth-child(4) { animation-delay: 150ms; }
.stagger-wrapper > :deep(*):nth-child(5) { animation-delay: 200ms; }
.stagger-wrapper > :deep(*):nth-child(6) { animation-delay: 250ms; }
.stagger-wrapper > :deep(*):nth-child(7) { animation-delay: 300ms; }
.stagger-wrapper > :deep(*):nth-child(8) { animation-delay: 350ms; }
.stagger-wrapper > :deep(*):nth-child(9) { animation-delay: 400ms; }
.stagger-wrapper > :deep(*):nth-child(10) { animation-delay: 450ms; }
</style>
```

- [ ] **Step 2: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 3: Commit**

```bash
git add blog-view/src/components/common/StaggerWrapper.vue
git commit -m "feat(blog-view): add StaggerWrapper component for list animations"
```

---

### Task 11: Create WithSkeleton component

**Files:**
- Create: `blog-view/src/components/common/WithSkeleton.vue`

**Interfaces:**
- Produces: Loading → content transition wrapper

- [ ] **Step 1: Create the component**

```vue
<script setup>
defineProps({
  loading: {
    type: Boolean,
    default: true
  },
  skeletonClass: {
    type: String,
    default: ''
  }
})
</script>

<template>
  <Transition mode="out-in" name="skeleton">
    <div
      v-if="loading"
      :key="'skeleton'"
      :class="['skeleton-placeholder', skeletonClass]"
    />
    <div v-else :key="'content'" class="skeleton-content">
      <slot />
    </div>
  </Transition>
</template>

<style scoped lang="scss">
.skeleton-placeholder {
  @include skeleton;
  display: block;
  height: 1em;
  margin-bottom: var(--space-sm);
}

.skeleton-content {
  animation: fadeIn var(--duration-slow) var(--ease-out) both;
}

.skeleton-enter-active,
.skeleton-leave-active {
  transition: opacity var(--duration-fast) ease;
}

.skeleton-enter-from,
.skeleton-leave-to {
  opacity: 0;
}

.skeleton-enter-to,
.skeleton-leave-from {
  opacity: 1;
}
</style>
```

- [ ] **Step 2: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 3: Commit**

```bash
git add blog-view/src/components/common/WithSkeleton.vue
git commit -m "feat(blog-view): add WithSkeleton component for loading transitions"
```

---

### Task 12: Apply micro-interactions to components

**Files:**
- Modify: `blog-view/src/assets/scss/index.scss` (Element Plus overrides)
- Modify: blog components that need hover/active effects

**Interfaces:**
- Consumes: CSS custom properties for transitions
- Produces: Consistent hover/focus/active states

- [ ] **Step 1: Enhance Element Plus button overrides with active scale**

In `index.scss`, update existing `.el-button--primary` block — add `&:active`:
```scss
&:active {
  transform: scale(0.97);
}
```

Add default button active:
```scss
.el-button--default {
  &:active {
    transform: scale(0.97);
  }
}
```

- [ ] **Step 2: Update card mixin hover consistency**

In `_mixins.scss`, verify the `@mixin card` hover transitions are correct. The current implementation is already good.

- [ ] **Step 3: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 4: Commit**

```bash
git add blog-view/src/assets/scss/index.scss blog-view/src/assets/scss/_mixins.scss
git commit -m "feat(blog-view): add micro-interactions — button active scale, consistent hover effects"
```

---

### Phase D: Responsive

---

### Task 13: Create MobileNav component

**Files:**
- Create: `blog-view/src/components/common/MobileNav.vue`

**Interfaces:**
- Produces: Fixed bottom navigation bar for <768px

- [ ] **Step 1: Create the component**

```vue
<script setup>
import { useRouter } from 'vue-router'
import { computed } from 'vue'

const router = useRouter()

const navItems = [
  { path: '/', label: 'Trang chủ', icon: 'HomeFilled' },
  { path: '/search', label: 'Tìm kiếm', icon: 'Search' },
  { path: '/trending', label: 'Xu hướng', icon: 'TrendCharts' },
  { path: '/notifications', label: 'Thông báo', icon: 'BellFilled' },
  { path: '/profile', label: 'Hồ sơ', icon: 'UserFilled' },
]

const currentRoute = computed(() => router.currentRoute.value.path)
</script>

<template>
  <nav class="mobile-nav">
    <router-link
      v-for="item in navItems"
      :key="item.path"
      :to="item.path"
      class="mobile-nav-item"
      :class="{ active: currentRoute.startsWith(item.path === '/' ? '/' : item.path) }"
    >
      <el-icon :size="20">
        <component :is="item.icon" />
      </el-icon>
      <span class="mobile-nav-label">{{ item.label }}</span>
    </router-link>
  </nav>
</template>

<style scoped lang="scss">
.mobile-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  z-index: var(--z-fixed);
  display: flex;
  background: var(--glass-bg);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  border-top: 1px solid var(--border-light);
  padding: var(--space-xs) 0;
  padding-bottom: env(safe-area-inset-bottom, 0);

  // Show only on mobile
  @media (min-width: 769px) {
    display: none;
  }
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
  -webkit-tap-highlight-color: transparent;

  &:active {
    transform: scale(0.95);
  }

  &.active {
    color: var(--primary);
  }

  .el-icon {
    font-size: 1.25rem;
  }
}

.mobile-nav-label {
  font-size: 0.625rem;
  font-weight: var(--fw-medium);
}
</style>
```

- [ ] **Step 2: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 3: Commit**

```bash
git add blog-view/src/components/common/MobileNav.vue
git commit -m "feat(blog-view): add MobileNav component for bottom navigation"
```

---

### Task 14: Integrate MobileNav and responsive layout adjustments

**Files:**
- Modify: `blog-view/src/components/layout/AppLayout.vue`

**Interfaces:**
- Consumes: MobileNav component
- Produces: Responsive layout with bottom nav, sidebar visibility control

- [ ] **Step 1: Update AppLayout.vue**

Read existing `AppLayout.vue` first. Add:
- Import and render `<MobileNav />` at bottom
- Add sidebar visibility logic for 640px breakpoint (show left sidebar only >768px, right sidebar only >1024px)
- Add `padding-bottom` for mobile nav on main content area

Suggest using a `useBreakpoint` composable or CSS-only approach with `display: none`.

For CSS-only approach:
```scss
.left-sidebar {
  @include respond-to(md) { display: none; }
}
.right-sidebar {
  @include respond-to(lg) { display: none; }
}
```

- [ ] **Step 2: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 3: Commit**

```bash
git add blog-view/src/components/layout/AppLayout.vue
git commit -m "feat(blog-view): integrate MobileNav and responsive sidebar visibility"
```

---

### Task 15: Mobile responsive — header + touch targets

**Files:**
- Modify: `blog-view/src/components/layout/AppHeader.vue`

- [ ] **Step 1: Hide search input on mobile, show search icon**

Read existing `AppHeader.vue`. Add a CSS class that hides the search input on `< 768px` and shows only the search icon. On mobile, clicking search icon navigates to `/search` or opens an overlay.

- [ ] **Step 2: Touch target audit**

Ensure all interactive elements have minimum 44×44px hit area:
- Icon buttons: add padding
- Tags: verified
- List items: full row tap zone

- [ ] **Step 3: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 4: Commit**

```bash
git add blog-view/src/components/layout/AppHeader.vue
git commit -m "feat(blog-view): mobile header responsive — search icon for small screens, touch targets"
```

---

### Task 16: Safe area + sidebar content folding

**Files:**
- Modify: `blog-view/src/assets/scss/_reset.scss`

- [ ] **Step 1: Add safe area padding to body**

Append to bottom of `_reset.scss`:
```scss
body {
  padding-bottom: env(safe-area-inset-bottom, 0);
}
```

- [ ] **Step 2: Add safe area utility classes**

Append to `_utilities.scss`:
```scss
// ===== Safe Area =====
.pad-safe-top { padding-top: env(safe-area-inset-top); }
.pad-safe-bottom { padding-bottom: env(safe-area-inset-bottom); }
```

- [ ] **Step 3: Note on sidebar content folding**

The left/right sidebar content should fold into the main content area on mobile. This is implemented in page-level components (e.g., Blog.vue, Profile.vue) — add a note that when sidebars are hidden on mobile, their content sections should render below the main content as collapsible sections. This is page-specific and implemented in Phase E.

- [ ] **Step 4: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 5: Commit**

```bash
git add blog-view/src/assets/scss/_reset.scss blog-view/src/assets/scss/_utilities.scss
git commit -m "feat(blog-view): add safe area padding and utility classes"
```

---

### Phase E: Polish Pass

---

### Task 17: Consistent border-radius and shadow elevation review

**Files:**
- Modify: `blog-view/src/assets/scss/index.scss`

- [ ] **Step 1: Review all Element Plus overrides for radius consistency**

In `index.scss`, verify:
- `.el-card` → `--radius-xl` (16px) ✅
- `.el-input__wrapper` → `--radius-md` (10px) ✅
- `.el-tag` → `--radius-full` ✅
- `.el-button--primary.is-round` → already has padding

Add if missing:
```scss
.el-dialog {
  border-radius: var(--radius-2xl);
}
```

- [ ] **Step 2: Update focus ring**

Replace existing `:focus-visible` in `_utilities.scss`:
```scss
:focus-visible {
  outline: none;
  box-shadow: 0 0 0 2px var(--primary-100), 0 0 0 4px rgba(14,165,233,0.15);
  border-radius: var(--radius-sm);
  transition: box-shadow var(--duration-fast) ease;
}
```

- [ ] **Step 3: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 4: Commit**

```bash
git add blog-view/src/assets/scss/index.scss blog-view/src/assets/scss/_utilities.scss
git commit -m "feat(blog-view): consistent border-radius, shadow elevation, focus ring polish"
```

---

### Task 18: Apply .prose + .card--standard to components

**Files:**
- Modify: `blog-view/src/components/blog/BlogContent.vue` (if exists)
- Modify: relevant card components

- [ ] **Step 1: Add .prose to blog body**

Find blog body/wrapper component. Add `.prose` class to the blog content wrapper.

- [ ] **Step 2: Add .card--standard to card elements**

Find card components that have inconsistent padding. Apply `.card--standard` class or equivalent.

- [ ] **Step 3: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 4: Commit**

```bash
git add <affected files>
git commit -m "feat(blog-view): apply prose and card-standard classes to components"
```

---

### Task 19: Sidebar content collapse on mobile

**Files:**
- Modify: page-level views (Blog.vue, Profile.vue, Home.vue) that use sidebars

- [ ] **Step 1: Create reactive sidebar content sections**

For each page with sidebars, create collapsible sections that render sidebar content below the main content on mobile.

Using `@include respond-to(md)` to show/hide:
```scss
.sidebar-section {
  display: none;

  @include respond-to(md) {
    display: block;
  }
}
```

- [ ] **Step 2: Run build to verify**

```bash
cd blog-view && npm run build
```
Expected: Build passes.

- [ ] **Step 3: Commit**

```bash
git add <affected page files>
git commit -m "feat(blog-view): sidebar content collapses to main flow on mobile"
```

---

### Task 20: Final build verification

- [ ] **Step 1: Full build**

```bash
cd blog-view && npm run build
```
Expected: Build passes with no errors.

- [ ] **Step 2: Visual check list** (manual)
- Light mode: Home, Blog, Profile, Search
- Dark mode toggle (click ThemeToggle) — verify all pages
- Mobile viewport (Chrome DevTools <768px) — verify bottom nav visible
- Page transitions between routes
- Card hover effects
- Heading hierarchy on blog pages
- .prose styling on blog content
- Scrollbar in dark mode

- [ ] **Step 3: Commit any remaining changes**

```bash
git status
git add -A
git commit -m "chore: final UI overhaul polish and build verification"
```

---

## Summary of All Files

### Created (7 files)
| # | File |
|---|------|
| 1 | `blog-view/src/assets/scss/_tokens-light.scss` |
| 2 | `blog-view/src/assets/scss/_tokens-dark.scss` |
| 3 | `blog-view/src/stores/theme.js` |
| 4 | `blog-view/src/components/common/ThemeToggle.vue` |
| 5 | `blog-view/src/components/common/MobileNav.vue` |
| 6 | `blog-view/src/components/common/WithSkeleton.vue` |
| 7 | `blog-view/src/components/common/StaggerWrapper.vue` |

### Deleted (1 file)
| # | File |
|---|------|
| 1 | `blog-view/src/assets/scss/_tokens.scss` |

### Modified (9 files, 2 may need page-specific edits)
| # | File |
|---|------|
| 1 | `blog-view/src/assets/scss/_variables.scss` |
| 2 | `blog-view/src/assets/scss/_reset.scss` |
| 3 | `blog-view/src/assets/scss/_mixins.scss` |
| 4 | `blog-view/src/assets/scss/_utilities.scss` |
| 5 | `blog-view/src/assets/scss/index.scss` |
| 6 | `blog-view/src/App.vue` |
| 7 | `blog-view/src/components/layout/AppHeader.vue` |
| 8 | `blog-view/src/components/layout/AppLayout.vue` |
| 9 | Page-level views (Blog.vue, Profile.vue, Home.vue) — sidebar folding on mobile |
