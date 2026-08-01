<template>
  <div class="event-card">
    <div class="event-date">
      <span class="event-day">{{ day }}</span>
      <span class="event-month">{{ month }}</span>
    </div>
    <div class="event-info">
      <h4 class="event-title">{{ event.title }}</h4>
      <p class="event-desc" v-if="event.description">{{ event.description }}</p>
      <div class="event-meta">
        <span class="event-time">{{ event.time }}</span>
        <span class="event-location" v-if="event.location">📍 {{ event.location }}</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps({
  event: { type: Object, required: true },
})

const date = computed(() => new Date(props.event.date))
const day = computed(() => date.value.getDate())
const month = computed(() => date.value.toLocaleDateString('vi-VN', { month: 'short' }))
</script>

<style scoped lang="scss">
.event-card {
  display: flex;
  gap: 16px;
  padding: 16px;
  background: var(--surface);
  border-radius: var(--radius-lg);
  border: 1px solid var(--border-light);
}

.event-date {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  min-width: 56px;
  padding: 8px;
  background: var(--primary-50);
  border-radius: var(--radius);
}

.event-day {
  font-size: 1.5rem;
  font-weight: 800;
  color: var(--primary);
  line-height: 1;
}

.event-month {
  font-size: 0.72rem;
  font-weight: 600;
  color: var(--primary);
  text-transform: uppercase;
}

.event-info {
  flex: 1;
}

.event-title {
  font-size: 1rem;
  font-weight: 600;
  margin: 0 0 4px 0;
}

.event-desc {
  font-size: 0.85rem;
  color: var(--text-secondary);
  margin: 0 0 8px 0;
}

.event-meta {
  display: flex;
  gap: 16px;
  font-size: 0.78rem;
  color: var(--text-muted);
}
</style>
