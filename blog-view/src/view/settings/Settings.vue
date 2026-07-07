<template>
  <OneColumnLayout>
    <div class="settings-page">
      <h1>Cài đặt</h1>
      <div class="settings-sections">
        <div class="settings-card">
          <h3>Thông tin cá nhân</h3>
          <div class="form-group"><label>Tên hiển thị</label><input v-model="form.displayName" type="text" class="form-input" /></div>
          <div class="form-group"><label>Bio</label><textarea v-model="form.bio" class="form-input" rows="3"></textarea></div>
          <div class="form-group"><label>Website</label><input v-model="form.website" type="url" class="form-input" /></div>
          <div class="form-group"><label>Địa điểm</label><input v-model="form.location" type="text" class="form-input" /></div>
          <el-button type="primary" @click="save">Lưu thay đổi</el-button>
        </div>
        <div class="settings-card">
          <h3>Bảo mật</h3>
          <div class="form-group"><label>Email hiện tại</label><input :value="user?.email" type="email" class="form-input" disabled /></div>
          <div class="form-group"><label>Mật khẩu mới</label><input type="password" class="form-input" placeholder="••••••••" /></div>
          <el-button type="primary">Đổi mật khẩu</el-button>
        </div>
        <div class="settings-card danger-zone">
          <h3>Vùng nguy hiểm</h3>
          <p>Xóa tài khoản sẽ xóa vĩnh viễn tất cả dữ liệu của bạn.</p>
          <el-button type="danger" plain>Xóa tài khoản</el-button>
        </div>
      </div>
    </div>
  </OneColumnLayout>
</template>
<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useAuthStore } from '@/store/auth'
import OneColumnLayout from '@/components/layouts/OneColumnLayout.vue'
const authStore = useAuthStore()
const user = authStore.user
const form = reactive({ displayName: user?.displayName || '', bio: user?.bio || '', website: user?.website || '', location: user?.location || '' })
const save = () => { ElMessage.success('Đã lưu thay đổi') }
</script>
<style scoped lang="scss">
.settings-page h1 { font-size: 1.5rem; font-weight: 800; margin-bottom: var(--space-xl); }
.settings-sections { display: flex; flex-direction: column; gap: var(--space-lg); }
.settings-card { background: var(--surface); border: 1px solid var(--border-light); border-radius: var(--radius-xl); padding: var(--space-xl); box-shadow: var(--shadow-sm); }
.settings-card h3 { font-size: 1rem; font-weight: 700; margin-bottom: var(--space-md); }
.settings-card p { font-size: 0.85rem; color: var(--text-secondary); margin-bottom: var(--space-md); }
.form-group { margin-bottom: var(--space-md); }
.form-group label { display: block; font-size: 0.85rem; font-weight: 600; color: var(--text-secondary); margin-bottom: 4px; }
.form-input { width: 100%; padding: 10px 12px; border: 1px solid var(--border); border-radius: var(--radius-md); background: var(--bg); color: var(--text-primary); font-size: 0.9rem; transition: border-color var(--duration-fast) ease; }
.form-input:focus { outline: none; border-color: var(--primary); box-shadow: 0 0 0 3px var(--primary-50); }
.danger-zone { border-color: var(--danger); }
.danger-zone h3 { color: var(--danger); }
</style>
