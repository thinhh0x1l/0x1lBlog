<template>
  <div class="game-mode-sheet" v-if="gameMode">
    <div class="sheet-bg"></div>
    <div class="sheet-content">
      <div class="sheet-left">
        <div class="sheet-avatar">{{ user?.displayName?.charAt(0) }}</div>
        <h2 class="sheet-name">{{ user?.displayName }}</h2>
        <div class="sheet-class">Lv.{{ level }} Warrior</div>
        <div class="sheet-hp"><span>HP</span><div class="hp-track"><div class="hp-fill" :style="{ width: (level * 1000 + currentExp) / (level * 1000 + nextLevelExp) * 100 + '%' }"></div></div><span>{{ level * 1000 + currentExp }}/{{ level * 1000 + nextLevelExp }}</span></div>
        <div class="sheet-stats">
          <div class="sheet-stat"><span class="stat-label">STR</span><div class="stat-bar"><div class="stat-fill" style="width:72%"></div></div><span>72</span></div>
          <div class="sheet-stat"><span class="stat-label">DEX</span><div class="stat-bar"><div class="stat-fill" style="width:45%"></div></div><span>45</span></div>
          <div class="sheet-stat"><span class="stat-label">INT</span><div class="stat-bar"><div class="stat-fill" style="width:88%"></div></div><span>88</span></div>
        </div>
      </div>
      <div class="sheet-right">
        <div class="sheet-equipment">
          <h3>Equipment</h3>
          <div class="equip-slots">
            <div class="equip-slot" v-for="slot in equipmentSlots" :key="slot.name">
              <span class="slot-icon">{{ slot.icon }}</span>
              <span class="slot-name">{{ slot.name }}</span>
              <span class="slot-item">{{ slot.item || '—' }}</span>
            </div>
          </div>
        </div>
        <div class="sheet-badges">
          <h3>Badges</h3>
          <div class="badge-mini-list">
            <span v-for="b in badges.slice(0, 6)" :key="b.id" class="badge-mini" :title="b.displayName">{{ b.icon || '🏆' }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup>
defineProps({ gameMode: Boolean, user: Object, level: Number, currentExp: Number, nextLevelExp: Number, badges: { type: Array, default: () => [] } })
const equipmentSlots = [
  { name: 'Weapon', icon: '🗡️', item: 'Rust Sword' },
  { name: 'Shield', icon: '🛡️', item: 'Code Shield' },
  { name: 'Helmet', icon: '👑', item: 'Creator Crown' },
  { name: 'Ring', icon: '💍', item: 'React Ring' },
  { name: 'Boots', icon: '👢', item: 'Git Boots' },
  { name: 'Amulet', icon: '📿', item: null },
]
</script>
<style scoped lang="scss">
.game-mode-sheet { margin-top: 24px; position: relative; }
.sheet-bg { position: absolute; inset: 0; background: linear-gradient(135deg, #1e1b4b, #312e81, #1e1b4b); border-radius: var(--radius-xl); opacity: 0.95; z-index: 0; }
.sheet-content { position: relative; z-index: 1; display: flex; gap: 32px; padding: 32px; color: #e0e7ff; }
.sheet-left { flex: 1; text-align: center; }
.sheet-avatar { width: 80px; height: 80px; border-radius: 50%; background: linear-gradient(135deg, #6366f1, #a855f7); display: flex; align-items: center; justify-content: center; font-size: 2rem; font-weight: 700; margin: 0 auto 12px; border: 3px solid #a855f7; }
.sheet-name { font-size: 1.5rem; font-weight: 800; }
.sheet-class { font-size: 0.85rem; color: #a5b4fc; margin-bottom: 16px; }
.sheet-hp { display: flex; align-items: center; gap: 8px; font-size: 0.8rem; margin-bottom: 16px; }
.sheet-hp span:first-child { font-weight: 700; color: #ef4444; }
.hp-track { flex: 1; height: 8px; background: rgba(255,255,255,0.15); border-radius: 99px; overflow: hidden; }
.hp-fill { height: 100%; background: linear-gradient(90deg, #ef4444, #f97316); border-radius: 99px; transition: width 0.5s; }
.sheet-stats { display: flex; flex-direction: column; gap: 8px; }
.sheet-stat { display: flex; align-items: center; gap: 8px; font-size: 0.8rem; }
.sheet-stat .stat-bar { flex: 1; height: 6px; background: rgba(255,255,255,0.15); border-radius: 99px; overflow: hidden; }
.sheet-stat .stat-fill { height: 100%; background: linear-gradient(90deg, #a855f7, #6366f1); border-radius: 99px; }
.sheet-right { flex: 1; }
.sheet-equipment h3, .sheet-badges h3 { font-size: 0.9rem; font-weight: 700; margin-bottom: 12px; color: #a5b4fc; text-transform: uppercase; letter-spacing: 0.05em; }
.equip-slots { display: flex; flex-direction: column; gap: 8px; margin-bottom: 24px; }
.equip-slot { display: flex; align-items: center; gap: 8px; padding: 8px 12px; background: rgba(255,255,255,0.06); border-radius: var(--radius); }
.slot-icon { font-size: 1.1rem; width: 24px; text-align: center; }
.slot-name { font-size: 0.78rem; color: #c7d2fe; width: 60px; }
.slot-item { font-size: 0.78rem; color: #e0e7ff; }
.badge-mini-list { display: flex; gap: 6px; flex-wrap: wrap; }
.badge-mini { width: 32px; height: 32px; border-radius: 50%; background: rgba(255,255,255,0.1); display: flex; align-items: center; justify-content: center; font-size: 1rem; cursor: help; }
</style>
