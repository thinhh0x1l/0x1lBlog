<template>
  <div class="tiptap-demo-page">
    <div class="demo-header">
      <h1>TipTap Editor Demo</h1>
      <p class="demo-desc">Trình soạn thảorich text đầy đủ tính năng — viết blog,-formatting, bảng, ảnh, code blocks</p>
    </div>

    <!-- Editor -->
    <ClientOnly>
      <Editor v-model="content" v-model:html="htmlContent" />
    </ClientOnly>

    <!-- Output Tabs -->
    <div class="output-section">
      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="HTML Output" name="html">
          <pre class="output-code"><code>{{ htmlContent }}</code></pre>
        </el-tab-pane>
        <el-tab-pane label="Plain Text" name="text">
          <pre class="output-code"><code>{{ content }}</code></pre>
        </el-tab-pane>
        <el-tab-pane label="Preview" name="preview">
          <div class="preview-content typo" v-html="sanitizedHtml" />
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- Action Buttons -->
    <div class="demo-actions">
      <el-button type="primary" @click="copyHtml">
        <el-icon><DocumentCopy /></el-icon>
        Copy HTML
      </el-button>
      <el-button @click="clearContent">
        <el-icon><Delete /></el-icon>
        Xóa nội dung
      </el-button>
      <el-button @click="loadSampleContent">
        <el-icon><Document /></el-icon>
        Load sample
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { DocumentCopy, Delete, Document } from '@element-plus/icons-vue'

definePageMeta({ layout: 'default', ssr: false })
useHead({ title: 'TipTap Demo — 0x1lBlog' })

const content = ref('')
const htmlContent = ref('')
const activeTab = ref('html')

const sanitizedHtml = computed(async () => {
  if (!htmlContent.value) return ''
  const { default: DOMPurify } = await import('dompurify')
  return DOMPurify.sanitize(htmlContent.value)
})

const copyHtml = async () => {
  await navigator.clipboard.writeText(htmlContent.value)
  ElMessage.success('Đã copy HTML vào clipboard!')
}

const clearContent = () => {
  content.value = ''
  htmlContent.value = ''
}

const loadSampleContent = () => {
  content.value = `Bài viết mẫu về TipTap Editor

Đây là một đoạn văn bản mẫu để bạn có thể thấy cách TipTap hoạt động. Bạn có thể sử dụng toolbar để định dạng văn bản, chèn ảnh, bảng, và nhiều hơn nữa.

Heading 2

Đây là heading level 2. TipTap hỗ trợ heading từ H1 đến H6.

Định dạng văn bản

Bạn có thể in đậm, nghiêng, gạch chân, và gạch ngang văn bản. Đây là inline code trong văn bản.

Code Block

function hello() {
  console.log("Hello from TipTap!")
}

Blockquote

Đây là một blockquote. Bạn có thể sử dụng nó để trích dẫn từ nguồn khác.

Bảng

| Tính năng | Trạng thái |
|-----------|------------|
| Bold/Italic | Hoạt động |
| Headings | Hoạt động |
| Tables | Hoạt động |
| Images | Hoạt động |

Danh sách

- Item 1
- Item 2
- Item 3

1. First
2. Second
3. Third

Liên kết

Bạn có thể thêm link vào văn bản bằng cách chọn text và nhấn nút link trên toolbar.

Highlight

Bạn có thể highlight văn bản để làm nổi bật.`

  htmlContent.value = '<h1>Bài viết mẫu về TipTap Editor</h1><p>Đây là một đoạn văn bản mẫu để bạn có thể thấy cách TipTap hoạt động. Bạn có thể sử dụng toolbar để định dạng văn bản, chèn ảnh, bảng, và nhiều hơn nữa.</p><h2>Heading 2</h2><p>Đây là heading level 2. TipTap hỗ trợ heading từ H1 đến H6.</p><h2>Định dạng văn bản</h2><p>Bạn có thể <strong>in đậm</strong>, <em>nghiêng</em>, <u>gạch chân</u>, và <s>gạch ngang</s> văn bản. Đây là <code>inline code</code> trong văn bản.</p><h2>Code Block</h2><pre><code>function hello() {\n  console.log("Hello from TipTap!")\n}</code></pre><h2>Blockquote</h2><blockquote><p>Đây là một blockquote. Bạn có thể sử dụng nó để trích dẫn từ nguồn khác.</p></blockquote><h2>Bảng</h2><table><tr><th>Tính năng</th><th>Trạng thái</th></tr><tr><td>Bold/Italic</td><td>Hoạt động</td></tr><tr><td>Headings</td><td>Hoạt động</td></tr><tr><td>Tables</td><td>Hoạt động</td></tr><tr><td>Images</td><td>Hoạt động</td></tr></table><h2>Danh sách</h2><ul><li>Item 1</li><li>Item 2</li><li>Item 3</li></ul><ol><li>First</li><li>Second</li><li>Third</li></ol><h2>Liên kết</h2><p>Bạn có thể thêm <a href="https://tiptap.dev">link</a> vào văn bản bằng cách chọn text và nhấn nút link trên toolbar.</p><h2>Highlight</h2><p>Bạn có thể <mark>highlight</mark> văn bản để làm nổi bật.</p>'
}
</script>

<style scoped lang="scss">
.tiptap-demo-page {
  max-width: 900px;
  margin: 0 auto;
}

.demo-header {
  margin-bottom: var(--space-lg);

  h1 {
    font-size: var(--fs-3xl);
    font-weight: var(--fw-extrabold);
    margin-bottom: 4px;
    background: linear-gradient(135deg, var(--primary) 0%, var(--info) 100%);
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    background-clip: text;
  }

  .demo-desc {
    color: var(--text-muted);
    font-size: var(--fs-sm);
  }
}

.output-section {
  margin-top: var(--space-md);
  border-radius: var(--radius-lg);
  overflow: hidden;

  :deep(.el-tabs__header) {
    margin: 0;
  }

  :deep(.el-tabs__content) {
    padding: 0;
  }
}

.output-code {
  background: var(--bg-secondary);
  padding: 16px 20px;
  border-radius: 0 0 var(--radius-lg) var(--radius-lg);
  overflow-x: auto;
  font-family: var(--font-mono);
  font-size: 13px;
  line-height: 1.6;
  color: var(--text-secondary);
  max-height: 400px;
  overflow-y: auto;
  margin: 0;

  code {
    background: none;
    padding: 0;
    color: inherit;
    font-size: inherit;
  }
}

.preview-content {
  padding: 20px;
  min-height: 200px;
  background: var(--surface);
  border-radius: 0 0 var(--radius-lg) var(--radius-lg);

  :deep(h1) {
    font-size: 1.8rem;
    font-weight: 800;
    margin: 1em 0 0.5em;
  }

  :deep(h2) {
    font-size: 1.4rem;
    font-weight: 700;
    margin: 1em 0 0.4em;
  }

  :deep(h3) {
    font-size: 1.15rem;
    font-weight: 600;
    margin: 0.8em 0 0.3em;
  }

  :deep(code) {
    background: var(--bg-secondary);
    padding: 2px 6px;
    border-radius: var(--radius-sm);
    font-family: var(--font-mono);
    font-size: 0.88em;
    color: var(--danger);
  }

  :deep(pre) {
    background: var(--bg-secondary);
    padding: 16px;
    border-radius: var(--radius-md);
    overflow-x: auto;

    code {
      background: none;
      padding: 0;
      color: inherit;
    }
  }

  :deep(blockquote) {
    border-left: 4px solid var(--primary);
    padding-left: 16px;
    margin: 1em 0;
    color: var(--text-secondary);
    font-style: italic;
  }

  :deep(table) {
    width: 100%;
    border-collapse: collapse;
    margin: 1em 0;

    th, td {
      border: 1px solid var(--border);
      padding: 8px 12px;
      text-align: left;
    }

    th {
      background: var(--bg-secondary);
      font-weight: 600;
    }
  }

  :deep(ul), :deep(ol) {
    padding-left: 24px;
    margin: 0.5em 0;
  }

  :deep(a) {
    color: var(--primary);
    text-decoration: underline;
  }

  :deep(mark) {
    background: #fff3cd;
    padding: 2px 4px;
    border-radius: 2px;
  }
}

.demo-actions {
  display: flex;
  gap: 8px;
  margin-top: var(--space-md);
}
</style>
