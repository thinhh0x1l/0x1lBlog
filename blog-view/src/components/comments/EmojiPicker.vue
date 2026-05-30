<template>
  <div class="emoji-wrapper">
    <!-- Button toggle -->
    <button type="button" class="emoji-btn" @click="toggle">
      <font-awesome-icon icon="face-laugh-squint"  />
    </button>

    <!-- Picker -->
    <div
        v-show="visible"
        ref="pickerContainer"
        class="emoji-picker-container"
    ></div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, onBeforeUnmount } from "vue"
const emit = defineEmits(["select"])
const visible = ref(false)
const pickerContainer = ref(null)
let pickerInstance = null

const toggle = async () => {
  visible.value = !visible.value

  if (visible.value) {
    await nextTick()
    await initPicker()
  }
}


const initPicker = async () => {
  if (pickerInstance) return
  const [{ Picker }, dataModule] = await Promise.all([
    import("emoji-mart"),
    import("@emoji-mart/data")
  ])
  const data = dataModule.default
  pickerInstance = new Picker({
    data,
    set: 'native',
    theme: "light",
    previewPosition: "none",
    onEmojiSelect: (emoji) => {
      emit("select", emoji)
    }
  })
  pickerContainer.value.appendChild(pickerInstance)
}

const handleClickOutside = (e) => {
  if (!pickerContainer.value) return

  if (
      visible.value &&
      !pickerContainer.value.contains(e.target) &&
      !e.target.closest(".emoji-btn")
  ) {
    visible.value = false
  }
}

onMounted(() => {
  document.addEventListener("click", handleClickOutside)
})

onBeforeUnmount(() => {
  document.removeEventListener("click", handleClickOutside)
})
</script>

<style scoped>
.emoji-wrapper {
  position: relative;
  display: inline-block;
}

.emoji-btn {
  font-size: 20px;
  cursor: pointer;
  border: none;
  background: transparent;
}

.emoji-picker-container {
  position: absolute;
  top: 20px;
  left: 0;
  z-index: 1;
}
:deep(em-emoji-picker) {
  --color-border-over: transparent;
  --color-border: transparent !important;
  --emoji-border-radius: 0px;
}
</style>