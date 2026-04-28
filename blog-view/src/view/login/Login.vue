<template>
  <div class="login-container">
    <div class="login-box">

      <div class="avatar-box">
        <img src="/img/avatar.jpg" alt="Avatar" />
      </div>

      <form @submit.prevent="handleLogin" class="login-form">
        <div class="field">
          <span class="p-input-icon-left w-full">
            <i class="pi pi-user" />
            <InputText
                v-model="loginForm.username"
                placeholder="Tên đăng nhập"
                :class="{ 'p-invalid': submitted && !loginForm.username }"
                class="w-full"
                @blur="validateField('username')"
            />
          </span>
          <small v-if="submitted && !loginForm.username" class="p-error">
            Vui lòng nhập tên đăng nhập
          </small>
        </div>

        <div class="field">
          <span class="p-input-icon-left w-full">
            <i class="pi pi-lock" />
            <Password
                v-model="loginForm.password"
                placeholder="Mật khẩu"
                :feedback="false"
                :class="{ 'p-invalid': submitted && !loginForm.password }"
                class="w-full"
                @blur="validateField('password')"
                @keyup.enter="handleLogin"
            />
          </span>
          <small v-if="submitted && !loginForm.password" class="p-error">
            Vui lòng nhập mật khẩu
          </small>
        </div>

        <div class="field btns">
          <Button
              label="Đăng nhập"
              icon="pi pi-sign-in"
              :loading="loading"
              type="submit"
          />
          <Button
              label="Đặt lại"
              icon="pi pi-refresh"
              severity="secondary"
              @click="resetForm"
          />
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useToast } from 'primevue/usetoast'
import { loginAuth as loginApi} from '@/api/login'

import InputText from 'primevue/inputtext'
import Password from 'primevue/password'
import Button from 'primevue/button'
import type {Login, LoginRes} from "@/types/loginType";
import type {ApiResponse} from "@/plugins/axios2";

const router = useRouter()
const toast = useToast()

const loginForm = reactive<Login>({
  username: 'lyrri',
  password: '123'
})

const loading = ref(false)
const submitted = ref(false)

const validateField = (field: any) => {
  if (submitted.value) {
  }
}

const resetForm = () => {
  loginForm.username = ''
  loginForm.password = ''
  submitted.value = false
}

const handleLogin = async () => {
  submitted.value = true

  if (!loginForm.username || !loginForm.password) {
    return
  }

  loading.value = true

  try {
    const res: ApiResponse<LoginRes> = await loginApi(loginForm)

    if (res.code === 200) {
      toast.add({
        severity: 'success',
        summary: 'Thành công',
        detail: res.msg,
        life: 3000
      })

      window.sessionStorage.setItem('token', res.data.token)
      await router.push('/home')
    } else {
      toast.add({
        severity: 'error',
        summary: 'Lỗi',
        detail: res.msg,
        life: 3000
      })
    }
  } catch (error) {
    console.error('Login error:', error)
    toast.add({
      severity: 'error',
      summary: 'Lỗi',
      detail: 'Yêu cầu thất bại',
      life: 3000
    })
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  box-sizing: unset !important;
  height: 100vh;
  background-color: #2b4b6b;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-box {
  width: 450px;
  min-height: 300px;
  background-color: #fff;
  border-radius: 3px;
  position: relative;
  padding: 80px 20px 20px;
}

.avatar-box {
  height: 130px;
  width: 130px;
  border: 1px solid #eee;
  border-radius: 50%;
  padding: 10px;
  box-shadow: 0 0 10px #ddd;
  position: absolute;
  left: 50%;
  top: 0;
  transform: translate(-50%, -50%);
  background-color: #fff;
}

.avatar-box img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #eee;
  object-fit: cover;
}

.login-form {
  width: 100%;
}

.field {
  margin-bottom: 1.5rem;
}

.btns {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.w-full {
  width: 100%;
}

:deep(.p-password) {
  width: 100%;
}

:deep(.p-password-input) {
  width: 100%;
}
</style>