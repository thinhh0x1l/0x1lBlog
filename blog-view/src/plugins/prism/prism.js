import Prism from 'prismjs'

import 'prismjs/themes/prism-tomorrow.css'

import 'prismjs/components/prism-java.js'
import 'prismjs/components/prism-javascript.js'
import 'prismjs/components/prism-python.js'
import 'prismjs/components/prism-css.js'
import 'prismjs/components/prism-sql.js'
import 'prismjs/components/prism-rust.js'
import 'prismjs/components/prism-clike.js'
import 'prismjs/components/prism-markup.js'

import 'prismjs/plugins/line-numbers/prism-line-numbers.css'
import 'prismjs/plugins/line-numbers/prism-line-numbers.js'
import 'prismjs/plugins/toolbar/prism-toolbar.css'
import 'prismjs/plugins/toolbar/prism-toolbar.js'
import 'prismjs/plugins/copy-to-clipboard/prism-copy-to-clipboard.js'
import 'prismjs/plugins/show-language/prism-show-language.js'

const rainbowBracesCSS = `
pre[class*="language-"],
code[class*="language-"] {
    border-radius: 0 !important; 
}
.token.punctuation.brace-level-1 { color: #ff6b6b; }
.token.punctuation.brace-level-2 { color: #4ecdc4; }
.token.punctuation.brace-level-3 { color: #ffe66d; }
.token.punctuation.brace-level-4 { color: #ff9a76; }
.token.punctuation.brace-level-5 { color: #a3de83; }
.token.punctuation.brace-level-6 { color: #c44569; }
.token.punctuation.brace-level-7 { color: #786fa6; }
.token.punctuation.brace-level-8 { color: #f7d794; }
.copy-to-clipboard-button {
    background: #4299e1 !important;
    color: white !important;
    border: none !important;
    border-radius: 4px !important;
    padding: 4px 12px !important;
    margin-right: 1em !important;
    font-size: 12px !important;
}
`

// Inject CSS
if (typeof document !== 'undefined') {
    const style = document.createElement('style')
    style.textContent = rainbowBracesCSS
    document.head.appendChild(style)
}

const PrismPlugin = {
    install(app) {
        app.config.globalProperties.$prism = Prism
        app.provide('prism', Prism)
        if (typeof window !== 'undefined') {
            window.Prism = Prism
        }
        app.config.globalProperties.$forceJavaSyntax = function() {
            Prism.highlightAll()
        }
    }
}

export { Prism, PrismPlugin }