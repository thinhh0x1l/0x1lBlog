<template>
  <div class="login_container">
    <div class="login_box">
      <div class="avatar_box">
        <img src="@/assets/img/avatar.jpg" alt="Avatar">
      </div>

      <el-form
          ref="loginFormRef"
          :model="loginForm"
          :rules="loginFormRules"
          class="login_form"
          @keyup.enter="handleLogin"
      >
        <el-form-item prop="username">
          <el-input
              v-model="loginForm.username"
              placeholder="Tên người dùng"
              :prefix-icon="User"
              size="large"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="loginForm.password"
              type="password"
              placeholder="Mật khẩu"
              :prefix-icon="Lock"
              show-password
              size="large"
          />
        </el-form-item>

        <el-form-item class="btns">
          <el-button
              type="primary"
              @click="handleLogin"
              :loading="loading"
              size="large"
          >
            {{ loading ? 'Đang đăng nhập...' : 'Đăng nhập' }}
          </el-button>
          <el-button @click="resetLoginForm" size="large">
            Đặt lại
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, getCurrentInstance } from 'vue'
import { useRouter } from 'vue-router'
import { login } from '@/network/login'

const router = useRouter()

// Lấy instance để truy cập global properties
const { proxy } = getCurrentInstance()

// Refs
const loginFormRef = ref()

// Reactive data
const loginForm = reactive({
  username: 'Thjnk',
  password: '11'
})

// Validation rules
const loginFormRules = {
  username: [
    { required: true, message: 'Vui lòng nhập tên người dùng', trigger: 'blur' }
  ],
  password: [
    { required: true, message: 'Vui lòng nhập mật khẩu', trigger: 'blur' }
  ]
}

// Loading state
const loading = ref(false)

// Methods
const resetLoginForm = () => {
  loginFormRef.value?.resetFields()
}

const handleLogin = async () => {
  if (!loginFormRef.value) return

  try {
    // Validate form
    const valid = await loginFormRef.value.validate()
    if (!valid) return

    loading.value = true

    // Call API
    const res = await login(loginForm.username, loginForm.password)

    console.log('Login response:', res)

    if (res.code === 200) {

      proxy.msgSuccess(res.msg)

      // Store token
      sessionStorage.setItem('token', res.data.token)

      // Navigate to dashboard
      router.push('/')
    } else {

      proxy.msgError(res.msg)
    }
  } catch (error) {
    console.error('Login error:', error)

    proxy.msgError(error.response?.data?.msg || 'Yêu cầu thất bại')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login_container {
  height: 100%;
  background-color: #2b4b6b;
}

.login_box {
  width: 450px;
  height: 300px;
  background-color: #fff;
  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.login_box .avatar_box {
  height: 130px;
  width: 130px;
  border: 1px solid #eee;
  border-radius: 50%;
  padding: 10px;
  box-shadow: 0 0 10px #ddd;
  position: absolute;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
}

.login_box .avatar_box img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #eee;
}

.login_form {
  position: absolute;
  bottom: 0;
  width: 100%;
  padding: 0 20px;
  box-sizing: border-box;
}

.btns {
  display: flex;
  justify-content: flex-end;
}
</style>