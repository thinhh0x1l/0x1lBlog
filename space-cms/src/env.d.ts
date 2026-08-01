/// <reference types="vite/client" />

///env.d.ts
import "vue";
declare module '*.vue' {
    import type { DefineComponent } from 'vue'
    const component: DefineComponent<{}, {}, any>
    export default component
}

declare module '*.svg' {
    import type { DefineComponent } from 'vue'
    const component: DefineComponent<{}, {}, any>
    export default component
}

declare module "@vue/runtime-core" {
    interface ComponentCustomProperties {
        $msgSuccess: (msg: string) => void;
        $msgError: (msg: string) => void;
        $msgInfo: (msg: string) => void;
        $msgWarning: (msg: string) => void;
    }
}
