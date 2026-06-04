import type { Component } from 'vue'

// import toàn bộ svg trong thư mục (Vite magic)
const modules = import.meta.glob('../assets/icons/svg/*.svg', {
    eager: true,
    import: 'default'
}) as Record<string, Component>

// map lại thành key
const icons: Record<string, Component> = {}

Object.keys(modules).forEach((key) => {
    // key: /src/assets/icons/user.svg
    const name = key.split('/').pop()!.replace('.svg', '')
    icons[name] = modules[key]
})

export default icons