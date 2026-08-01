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
import { reactive, computed } from 'vue'

definePageMeta({ layout: 'default', ssr: false })
useHead({ title: 'Cài đặt - 0x1lBlog' })

const authStore = useAuthStore()
const user = computed(() => authStore.user)
const form = reactive({ displayName: user.value?.displayName || '', bio: user.value?.bio || '', website: user.value?.website || '', location: user.value?.location || '' })
const save = async () => { console.log('Saved:', form) }
</script>
<style scoped lang="scss">
.settings-page h1 { font-size: 1.25rem; font-weight: 700; color: var(--text-primary); margin-bottom: 20px; }
.settings-sections { display: flex; flex-direction: column; gap: 16px; }
.settings-card {
  background: var(--surface); border: 1px solid var(--border-light);
  border-radius: 8px; padding: 24px;
  h3 { font-size: 0.95rem; font-weight: 700; color: var(--text-primary); margin-bottom: 16px; }
  p { font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 16px; line-height: 1.5; }
}
.form-group { margin-bottom: 14px; }
.form-group label { display: block; font-size: 0.82rem; font-weight: 600; color: var(--text-secondary); margin-bottom: 6px; }
.form-input {
  width: 100%; padding: 10px 12px; border: 1px solid var(--border); border-radius: 6px;
  background: var(--bg); color: var(--text-primary); font-size: 0.88rem;
  transition: border-color 0.15s; font-family: inherit;
  &:focus { outline: none; border-color: var(--primary); box-shadow: 0 0 0 2px var(--primary-50); }
}
.danger-zone { border-color: var(--danger); }
.danger-zone h3 { color: var(--danger); }
</style>
