<script setup>
import { ref, reactive } from 'vue'
import Login from './components/Login.vue'
import Editor from './components/Editor.vue'

const isLoggedIn = ref(false)
const currentUser = ref(null)

const handleLogin = (user) => {
  isLoggedIn.value = true
  currentUser.value = user
}

const handleLogout = () => {
  isLoggedIn.value = false
  currentUser.value = null
}
</script>

<template>
  <div class="app-container">
    <Login v-if="!isLoggedIn" @login="handleLogin" />
    <Editor v-else :user="currentUser" @logout="handleLogout" />
  </div>
</template>

<style scoped>
.app-container {
  min-height: 100vh;
  padding: 20px;
  box-sizing: border-box;
}
</style>