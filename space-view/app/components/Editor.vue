<template>
  <div class="tiptap-editor" :class="{ 'is-editable': editable }">
    <div v-if="editor && editable" class="editor-toolbar">
      <!-- Text Formatting -->
      <div class="toolbar-group">
        <el-tooltip content="In đậm (Ctrl+B)" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive('bold') }"
            @click="editor.chain().focus().toggleBold().run()"
          >
            <strong>B</strong>
          </button>
        </el-tooltip>
        <el-tooltip content="Nghiêng (Ctrl+I)" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive('italic') }"
            @click="editor.chain().focus().toggleItalic().run()"
          >
            <em>I</em>
          </button>
        </el-tooltip>
        <el-tooltip content="Gạch chân (Ctrl+U)" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive('underline') }"
            @click="editor.chain().focus().toggleUnderline().run()"
          >
            <span style="text-decoration: underline; font-weight: 600">U</span>
          </button>
        </el-tooltip>
        <el-tooltip content="Gạch ngang (Ctrl+Shift+X)" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive('strike') }"
            @click="editor.chain().focus().toggleStrike().run()"
          >
            <span style="text-decoration: line-through; font-weight: 600">S</span>
          </button>
        </el-tooltip>
      </div>

      <div class="toolbar-divider" />

      <!-- Headings -->
      <div class="toolbar-group">
        <el-dropdown @command="handleHeading" trigger="click">
          <button class="toolbar-text-btn">
            {{ currentHeadingLabel }}
            <el-icon><ArrowDown /></el-icon>
          </button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item :command="0">Normal text</el-dropdown-item>
              <el-dropdown-item :command="1">
                <span style="font-size: 1.5em; font-weight: 800">Heading 1</span>
              </el-dropdown-item>
              <el-dropdown-item :command="2">
                <span style="font-size: 1.25em; font-weight: 700">Heading 2</span>
              </el-dropdown-item>
              <el-dropdown-item :command="3">
                <span style="font-size: 1.1em; font-weight: 600">Heading 3</span>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

      <div class="toolbar-divider" />

      <!-- Lists -->
      <div class="toolbar-group">
        <el-tooltip content="Danh sách" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive('bulletList') }"
            @click="editor.chain().focus().toggleBulletList().run()"
          >
            <el-icon><List /></el-icon>
          </button>
        </el-tooltip>
        <el-tooltip content="Danh sách đánh số" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive('orderedList') }"
            @click="editor.chain().focus().toggleOrderedList().run()"
          >
            <el-icon><Finished /></el-icon>
          </button>
        </el-tooltip>
        <el-tooltip content="Trích dẫn" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive('blockquote') }"
            @click="editor.chain().focus().toggleBlockquote().run()"
          >
            <el-icon><ChatLineSquare /></el-icon>
          </button>
        </el-tooltip>
      </div>

      <div class="toolbar-divider" />

      <!-- Code -->
      <div class="toolbar-group">
        <el-tooltip content="Inline code" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive('code') }"
            @click="editor.chain().focus().toggleCode().run()"
          >
            <el-icon><Monitor /></el-icon>
          </button>
        </el-tooltip>
        <el-tooltip content="Code block" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive('codeBlock') }"
            @click="editor.chain().focus().toggleCodeBlock().run()"
          >
            <el-icon><Cpu /></el-icon>
          </button>
        </el-tooltip>
      </div>

      <div class="toolbar-divider" />

      <!-- Insert -->
      <div class="toolbar-group">
        <el-tooltip content="Chèn link" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive('link') }"
            @click="handleLink"
          >
            <el-icon><Link /></el-icon>
          </button>
        </el-tooltip>
        <el-tooltip content="Chèn ảnh" placement="bottom" :show-after="400">
          <button class="toolbar-btn" @click="showImageDialog = true">
            <el-icon><Picture /></el-icon>
          </button>
        </el-tooltip>
      </div>

      <div class="toolbar-divider" />

      <!-- Table -->
      <div class="toolbar-group">
        <el-dropdown trigger="click" @command="handleTableCommand">
          <button class="toolbar-btn" :class="{ active: editor.isActive('table') }">
            <el-icon><Grid /></el-icon>
          </button>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="insertTable">Tạo bảng</el-dropdown-item>
              <el-dropdown-item command="addColumnBefore" divided>Thêm cột trái</el-dropdown-item>
              <el-dropdown-item command="addColumnAfter">Thêm cột phải</el-dropdown-item>
              <el-dropdown-item command="deleteColumn">Xóa cột</el-dropdown-item>
              <el-dropdown-item command="addRowBefore" divided>Thêm hàng trên</el-dropdown-item>
              <el-dropdown-item command="addRowAfter">Thêm hàng dưới</el-dropdown-item>
              <el-dropdown-item command="deleteRow">Xóa hàng</el-dropdown-item>
              <el-dropdown-item command="deleteTable" divided>Xóa bảng</el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>

      <div class="toolbar-divider" />

      <!-- Text Align -->
      <div class="toolbar-group">
        <el-tooltip content="Căn trái" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive({ textAlign: 'left' }) }"
            @click="editor.chain().focus().setTextAlign('left').run()"
          >
            <el-icon><DArrowLeft /></el-icon>
          </button>
        </el-tooltip>
        <el-tooltip content="Căn giữa" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive({ textAlign: 'center' }) }"
            @click="editor.chain().focus().setTextAlign('center').run()"
          >
            <el-icon><Minus /></el-icon>
          </button>
        </el-tooltip>
        <el-tooltip content="Căn phải" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive({ textAlign: 'right' }) }"
            @click="editor.chain().focus().setTextAlign('right').run()"
          >
            <el-icon><DArrowRight /></el-icon>
          </button>
        </el-tooltip>
      </div>

      <div class="toolbar-divider" />

      <!-- Highlight -->
      <div class="toolbar-group">
        <el-tooltip content="Highlight (Ctrl+Shift+H)" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :class="{ active: editor.isActive('highlight') }"
            @click="editor.chain().focus().toggleHighlight().run()"
          >
            <el-icon><Brush /></el-icon>
          </button>
        </el-tooltip>
      </div>

      <div class="toolbar-divider" />

      <!-- Horizontal Rule -->
      <div class="toolbar-group">
        <el-tooltip content="Đường kẻ ngang" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            @click="editor.chain().focus().setHorizontalRule().run()"
          >
            <el-icon><Remove /></el-icon>
          </button>
        </el-tooltip>
      </div>

      <!-- Right: Undo/Redo -->
      <div class="toolbar-right">
        <el-tooltip content="Hoàn tác (Ctrl+Z)" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :disabled="!editor.can().undo()"
            @click="editor.chain().focus().undo().run()"
          >
            <el-icon><RefreshLeft /></el-icon>
          </button>
        </el-tooltip>
        <el-tooltip content="Làm lại (Ctrl+Shift+Z)" placement="bottom" :show-after="400">
          <button
            class="toolbar-btn"
            :disabled="!editor.can().redo()"
            @click="editor.chain().focus().redo().run()"
          >
            <el-icon><RefreshRight /></el-icon>
          </button>
        </el-tooltip>
      </div>
    </div>

    <!-- Editor Content -->
    <div class="editor-content-wrapper">
      <EditorContent :editor="editor" class="editor-content" />
    </div>

    <!-- Footer: Word Count -->
    <div v-if="editor" class="editor-footer">
      <div class="editor-word-count">
        <span>{{ wordCount }} từ</span>
        <span>{{ charCount }} ký tự</span>
      </div>
    </div>

    <!-- Link Dialog -->
    <el-dialog v-model="showLinkDialog" title="Chèn link" width="420px" :append-to-body="true">
      <el-input v-model="linkUrl" placeholder="https://..." @keyup.enter="confirmLink" />
      <template #footer>
        <el-button v-if="editor?.isActive('link')" type="danger" text @click="removeLink">Bỏ link</el-button>
        <div style="flex: 1" />
        <el-button @click="showLinkDialog = false">Hủy</el-button>
        <el-button type="primary" @click="confirmLink">Áp dụng</el-button>
      </template>
    </el-dialog>

    <!-- Image Dialog -->
    <el-dialog v-model="showImageDialog" title="Chèn ảnh" width="480px" :append-to-body="true">
      <el-input v-model="imageUrl" placeholder="Nhập URL ảnh..." @keyup.enter="confirmImage" />
      <template #footer>
        <el-button @click="showImageDialog = false">Hủy</el-button>
        <el-button type="primary" @click="confirmImage">Chèn ảnh</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { useEditor, EditorContent } from '@tiptap/vue-3'
import StarterKit from '@tiptap/starter-kit'
import Link from '@tiptap/extension-link'
import Image from '@tiptap/extension-image'
import Placeholder from '@tiptap/extension-placeholder'
import Underline from '@tiptap/extension-underline'
import TextAlign from '@tiptap/extension-text-align'
import Highlight from '@tiptap/extension-highlight'
import CodeBlockLowlight from '@tiptap/extension-code-block-lowlight'
import { Table, TableRow, TableCell, TableHeader } from '@tiptap/extension-table'
import { common, createLowlight } from 'lowlight'

import {
  ArrowDown, List, Finished, ChatLineSquare, Monitor, Cpu,
  Link as LinkIcon, Picture, Grid,
  DArrowLeft, Minus, DArrowRight, Brush, Remove,
  RefreshLeft, RefreshRight,
} from '@element-plus/icons-vue'

const lowlight = createLowlight(common)

const props = withDefaults(defineProps<{
  modelValue?: string
  placeholder?: string
  editable?: boolean
}>(), {
  modelValue: '',
  placeholder: 'Bắt đầu viết...',
  editable: true,
})

const emit = defineEmits<{
  'update:modelValue': [value: string]
  'update:html': [value: string]
}>()

const showLinkDialog = ref(false)
const linkUrl = ref('')
const showImageDialog = ref(false)
const imageUrl = ref('')

const editor = useEditor({
  content: props.modelValue,
  editable: props.editable,
  immediatelyRender: false,
  extensions: [
    StarterKit.configure({
      codeBlock: false,
    }),
    Link.configure({
      openOnClick: false,
      HTMLAttributes: { class: 'editor-link' },
    }),
    Image.configure({
      HTMLAttributes: { class: 'editor-image' },
    }),
    Placeholder.configure({
      placeholder: props.placeholder,
    }),
    Underline,
    TextAlign.configure({
      types: ['heading', 'paragraph'],
    }),
    Highlight,
    CodeBlockLowlight.configure({ lowlight }),
    Table.configure({ resizable: true }),
    TableRow,
    TableCell,
    TableHeader,
  ],
  onUpdate: ({ editor: e }) => {
    emit('update:modelValue', e.getText())
    emit('update:html', e.getHTML())
  },
})

// Sync external content changes
watch(() => props.modelValue, (val) => {
  if (editor.value && val !== editor.value.getText()) {
    editor.value.commands.setContent(val || '')
  }
})

onBeforeUnmount(() => {
  editor.value?.destroy()
})

// ===== Computed =====
const currentHeadingLabel = computed(() => {
  if (!editor.value) return 'Text'
  for (let i = 1; i <= 3; i++) {
    if (editor.value.isActive('heading', { level: i })) return `H${i}`
  }
  return 'Text'
})

const wordCount = computed(() => {
  if (!editor.value) return 0
  const text = editor.value.getText().trim()
  return text ? text.split(/\s+/).length : 0
})

const charCount = computed(() => {
  if (!editor.value) return 0
  return editor.value.getText().length
})

// ===== Heading =====
const handleHeading = (level: number) => {
  if (!editor.value) return
  if (level === 0) {
    editor.value.chain().focus().setParagraph().run()
  } else {
    editor.value.chain().focus().toggleHeading({ level: level as 1 | 2 | 3 }).run()
  }
}

// ===== Link =====
const handleLink = () => {
  if (!editor.value) return
  if (editor.value.isActive('link')) {
    linkUrl.value = editor.value.getAttributes('link').href || ''
  } else {
    linkUrl.value = ''
  }
  showLinkDialog.value = true
}

const confirmLink = () => {
  if (!editor.value || !linkUrl.value) return
  editor.value.chain().focus().setLink({ href: linkUrl.value }).run()
  showLinkDialog.value = false
  linkUrl.value = ''
}

const removeLink = () => {
  editor.value?.chain().focus().unsetLink().run()
  showLinkDialog.value = false
}

// ===== Image =====
const confirmImage = () => {
  if (!editor.value || !imageUrl.value) return
  editor.value.chain().focus().setImage({ src: imageUrl.value }).run()
  showImageDialog.value = false
  imageUrl.value = ''
}

// ===== Table =====
const handleTableCommand = (command: string) => {
  if (!editor.value) return
  const chain = editor.value.chain().focus()

  switch (command) {
    case 'insertTable':
      chain.insertTable({ rows: 3, cols: 3, withHeaderRow: true }).run()
      break
    case 'addColumnBefore':
      chain.addColumnBefore().run()
      break
    case 'addColumnAfter':
      chain.addColumnAfter().run()
      break
    case 'deleteColumn':
      chain.deleteColumn().run()
      break
    case 'addRowBefore':
      chain.addRowBefore().run()
      break
    case 'addRowAfter':
      chain.addRowAfter().run()
      break
    case 'deleteRow':
      chain.deleteRow().run()
      break
    case 'deleteTable':
      chain.deleteTable().run()
      break
  }
}
</script>
