<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <h2>用户登录</h2>
      </template>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitForm(loginFormRef)" :loading="loading">登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const emit = defineEmits(['login'])

const loginForm = reactive({
  username: 'user1',
  password: '12345678'
})

const loading = ref(false)
const loginFormRef = ref()

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ]
}

const submitForm = async (formEl) => {
  if (!formEl) return
  await formEl.validate(async (valid, fields) => {
    if (valid) {
      loading.value = true

      try {
        // 调用后端登录接口
        const response = await axios.post('http://localhost:8082/user/login', {
          userAccount: loginForm.username,
          userPassword: loginForm.password
        }, {
          // 关键：允许跨域发送Cookie，以便后端识别用户Session
          withCredentials: true
        })

        if (response.data) {
          ElMessage.success(`欢迎 ${response.data.userName}`)
          emit('login', response.data)
        } else {
          ElMessage.error('登录失败')
        }
      } catch (error) {
        ElMessage.error('登录失败：' + (error.response?.data?.message || error.message))
      } finally {
        loading.value = false
      }
    } else {
      console.log('error submit!', fields)
    }
  })
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background-color: #f5f5f5;
}

.login-card {
  width: 400px;
  max-width: 90%;
}
</style>