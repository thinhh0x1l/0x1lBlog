<template>
  <div class="message-thread">
    <div class="thread-messages" ref="messagesRef">
      <div v-for="msg in messages" :key="msg.id" :class="['message', { own: msg.isOwn }]">
        <img v-if="!msg.isOwn" :src="msg.senderAvatar || fallbackAvatar" class="message-avatar" />
        <div class="message-bubble">
          <p class="message-text">{{ msg.content }}</p>
          <span class="message-time">{{ fromNow(msg.createdAt) }}</span>
        </div>
      </div>
    </div>
    <div class="thread-compose">
      <input v-model="newMessage" class="compose-input" placeholder="Nhắn tin..." @keydown.enter="send" />
      <button class="compose-send" @click="send" :disabled="!newMessage.trim()">
        <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"><line x1="22" y1="2" x2="11" y2="13"/><polygon points="22 2 15 22 11 13 2 9 22 2"/></svg>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { fromNow } from '~/utils/time'

const props = defineProps({
  messages: { type: Array, default: () => [] },
})

const emit = defineEmits(['send'])

const newMessage = ref('')
const messagesRef = ref<HTMLElement | null>(null)
const fallbackAvatar = 'https://api.dicebear.com/7.x/avataaars/svg?seed=guest'

const send = () => {
  if (!newMessage.value.trim()) return
  emit('send', newMessage.value.trim())
  newMessage.value = ''
}

onMounted(() => {
  if (messagesRef.value) {
    messagesRef.value.scrollTop = messagesRef.value.scrollHeight
  }
})
</script>

<style scoped lang="scss">
.message-thread {
  display: flex;
  flex-direction: column;
  height: 100%;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-lg);
  overflow: hidden;
}

.thread-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.message {
  display: flex;
  align-items: flex-end;
  gap: 8px;

  &.own {
    flex-direction: row-reverse;

    .message-bubble {
      background: var(--primary);
      color: white;
    }

    .message-time {
      color: rgba(255, 255, 255, 0.7);
    }
  }
}

.message-avatar {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  object-fit: cover;
}

.message-bubble {
  max-width: 70%;
  padding: 10px 14px;
  background: var(--bg-secondary);
  border-radius: var(--radius-lg);
}

.message-text {
  font-size: 0.9rem;
  margin: 0 0 4px 0;
  line-height: 1.4;
}

.message-time {
  font-size: 0.65rem;
  color: var(--text-muted);
}

.thread-compose {
  display: flex;
  gap: 8px;
  padding: 12px;
  border-top: 1px solid var(--border-light);
}

.compose-input {
  flex: 1;
  padding: 10px 14px;
  border: 1px solid var(--border-light);
  border-radius: var(--radius-full);
  background: var(--bg-secondary);
  color: var(--text-primary);
  font-size: 0.9rem;
  outline: none;

  &:focus {
    border-color: var(--primary);
  }
}

.compose-send {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border: none;
  border-radius: 50%;
  background: var(--primary);
  color: white;
  cursor: pointer;
  transition: background 0.15s ease;

  &:hover:not(:disabled) {
    background: var(--primary-dark);
  }

  &:disabled {
    opacity: 0.5;
    cursor: not-allowed;
  }
}
</style>
