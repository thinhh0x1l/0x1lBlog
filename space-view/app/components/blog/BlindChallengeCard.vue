<template>
  <div class="sidebar-card blind-card" v-if="challenge">
    <div class="card-header">
      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><path d="M17.94 17.94A10.07 10.07 0 0 1 12 20c-7 0-11-8-11-8a18.45 18.45 0 0 1 5.06-5.94M9.9 4.24A9.12 9.12 0 0 1 12 4c7 0 11 8 11 8a18.5 18.5 0 0 1-2.16 3.19m-6.72-1.07a3 3 0 1 1-4.24-4.24"/><line x1="1" y1="1" x2="23" y2="23"/></svg>
      <span>Blind Challenge</span>
    </div>
    <div class="blind-hint" v-if="!challenge.revealed">
      <p class="blind-question">"??? — Hãy đoán chủ đề hôm nay"</p>
      <p class="blind-hint-text" v-if="challenge.topicHint">Gợi ý: {{ challenge.topicHint }}</p>
      <div class="blind-options" v-if="!myGuess">
        <button v-for="opt in parsedOptions" :key="opt.id" class="blind-option" @click="selectedOption = opt.id" :class="{ selected: selectedOption === opt.id }">{{ opt.name }}</button>
      </div>
      <div class="blind-result" v-if="myGuess">
        <p v-if="myGuess.isCorrect">🎉 Bạn đã đoán đúng!</p>
        <p v-else>😅 Chưa đúng. Còn {{ challenge.options?.length || 0 }} lựa chọn khác.</p>
      </div>
      <div class="blind-actions" v-if="!myGuess">
        <el-button type="primary" size="small" round @click="submitGuess" :disabled="!selectedOption">Đoán ngay</el-button>
      </div>
    </div>
    <div class="blind-revealed" v-else>
      <p class="blind-topic">Chủ đề hôm nay: <strong>{{ topicName }}</strong></p>
      <p v-if="myGuess?.isCorrect">🎉 Chính xác! Bạn đã đoán đúng!</p>
      <p v-else>😅 Rất tiếc, bạn đã đoán sai.</p>
    </div>
    <div class="blind-countdown">
      <span v-if="!challenge.revealed">🔓 Tiết lộ lúc 20:00 · {{ timeUntilReveal }}</span>
    </div>
  </div>
</template>
<script setup lang="ts">
import dayjs from 'dayjs'

const props = defineProps({
  challenge: Object,
  myGuess: Object,
})
const emit = defineEmits(['guess'])
const selectedOption = ref<string | null>(null)

const parsedOptions = computed(() => {
  if (!props.challenge?.options) return []
  if (Array.isArray(props.challenge.options)) return props.challenge.options
  try { return JSON.parse(props.challenge.options) } catch { return [] }
})

const topicName = computed(() => {
  if (!props.challenge?.topicId) return ''
  return parsedOptions.value.find((o: any) => o.id === props.challenge.topicId)?.name || ''
})

const timeUntilReveal = computed(() => {
  const now = dayjs()
  const reveal = dayjs().hour(20).minute(0).second(0)
  if (now.isAfter(reveal)) return 'Đã đến giờ!'
  const diff = reveal.diff(now, 'minute')
  const hours = Math.floor(diff / 60)
  const minutes = diff % 60
  return `${hours}h ${minutes}p`
})

const submitGuess = () => {
  if (selectedOption.value) emit('guess', { challengeId: props.challenge.id, guessedTopicId: selectedOption.value })
}
</script>
<style scoped lang="scss">
.blind-card { padding: 16px; }
.blind-card .card-header { display: flex; align-items: center; gap: 8px; font-size: 0.85rem; font-weight: 600; margin-bottom: 12px; }
.blind-question { font-size: 0.9rem; font-weight: 600; text-align: center; color: var(--primary); margin-bottom: 8px; }
.blind-hint-text { font-size: 0.75rem; color: var(--text-muted); text-align: center; margin-bottom: 12px; }
.blind-options { display: flex; flex-wrap: wrap; gap: 6px; margin-bottom: 12px; }
.blind-option { padding: 6px 12px; border: 1px solid var(--border); border-radius: 99px; background: var(--surface); font-size: 0.75rem; cursor: pointer; transition: all 0.15s; color: var(--text-secondary); }
.blind-option:hover { border-color: var(--primary); color: var(--primary); }
.blind-option.selected { background: var(--primary); color: white; border-color: var(--primary); }
.blind-actions { text-align: center; }
.blind-revealed { text-align: center; padding: 12px 0; }
.blind-topic { font-size: 0.9rem; margin-bottom: 8px; }
.blind-countdown { font-size: 0.7rem; color: var(--text-muted); text-align: center; margin-top: 10px; padding-top: 8px; border-top: 1px solid var(--border-light); }
</style>
