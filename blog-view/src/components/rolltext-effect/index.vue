<template>
  <component
      :is="effectComponents[effect]"
      :roll-text="props.rollText"
  />
</template>

<script setup>
import RainBow from "@/components/rolltext-effect/rainbow";
import {ref} from "vue";

const props = defineProps({
  rollText: Array,
  config: Object,
})

const effect = ref('rollText')

const effectComponents = {
  rollText: RainBow,
  // matrixText: MatrixText,
  // glitchText: GlitchText
}
</script>