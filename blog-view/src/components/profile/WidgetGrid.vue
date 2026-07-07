<template>
  <div class="widget-grid">
    <BlogListWidget v-if="isVisible('BLOG_LIST')" :blogs="profileData.recentBlogs" />
    <BadgeWallWidget v-if="isVisible('BADGE_WALL')" :badges="profileData.badges" />
    <SkillTreeWidget v-if="isVisible('SKILL_TREE')" :trees="profileData.skillTrees" :progress="profileData.skillProgress" :unlocks="profileData.skillUnlocks" :is-logged-in="true" />
    <StatusesWidget v-if="isVisible('STATUSES')" :statuses="profileData.recentStatuses" />
    <StreakCalendarWidget v-if="isVisible('STREAK')" :streak="profileData.streak" />
    <MusicBoxWidget v-if="isVisible('MUSIC_BOX')" :equipped-items="profileData.user?.equippedItems" />
    <QuestProgressWidget v-if="isVisible('QUEST')" :quests="profileData.quests" />
  </div>
</template>
<script setup>
import BlogListWidget from './widgets/BlogListWidget.vue'
import BadgeWallWidget from './widgets/BadgeWallWidget.vue'
import SkillTreeWidget from './widgets/SkillTreeWidget.vue'
import StatusesWidget from './widgets/StatusesWidget.vue'
import StreakCalendarWidget from './widgets/StreakCalendarWidget.vue'
import MusicBoxWidget from './widgets/MusicBoxWidget.vue'
import QuestProgressWidget from './widgets/QuestProgressWidget.vue'

const props = defineProps({
  profileData: { type: Object, default: () => ({}) },
  isOwn: Boolean,
})
const isVisible = (type) => {
  const layout = props.profileData.profileLayout || []
  const w = layout.find(l => l.widgetType === type)
  return w ? w.isVisible : false
}
</script>
<style scoped lang="scss">
.widget-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; padding: 24px 0; }
@media (max-width: 1024px) { .widget-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 640px) { .widget-grid { grid-template-columns: 1fr; } }
</style>
