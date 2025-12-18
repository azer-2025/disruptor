<template>
  <div class="editor-container">
    <el-page-header @back="handleBack">
      <template #content>
        <span class="header-title">图片编辑器</span>
      </template>
      <template #extra>
        <div class="header-extra">
          <span>欢迎您，{{ user.userName }}</span>
          <el-button type="danger" @click="handleLogout">退出</el-button>
        </div>
      </template>
    </el-page-header>

    <div class="chat-container">
      <el-card class="chat-card">
        <template #header>
          <h3>协作消息</h3>
        </template>
        <div class="messages" ref="messagesContainer">
          <div v-for="(message, index) in messages" :key="index" class="message"
            :class="{ 'self-message': message.userId === user.userId }">
            <div class="message-content">{{ message.content }}</div>
            <div class="message-time">{{ message.time }}</div>
          </div>
        </div>
        <div class="input-area">
          <el-input v-model="messageInput" placeholder="输入消息..." @keyup.enter="sendMessage"></el-input>
          <el-button type="primary" @click="sendMessage">发送</el-button>
        </div>
      </el-card>
    </div>

    <div class="actions-container">
      <el-card class="actions-card">
        <template #header>
          <h3>编辑操作</h3>
        </template>
        <div class="actions-buttons">
          <el-button type="primary" @click="sendAction('ZOOM_IN')" size="large">
            放大
          </el-button>
          <el-button type="primary" @click="sendAction('ZOOM_OUT')" size="large">
            缩小
          </el-button>
          <el-button type="primary" @click="sendAction('ROTATE_LEFT')" size="large">
            左旋转
          </el-button>
          <el-button type="primary" @click="sendAction('ROTATE_RIGHT')" size="large">
            右旋转
          </el-button>
        </div>
      </el-card>
    </div>

    <!-- 显示系统通知 -->
    <el-alert v-for="notification in notifications" :key="notification.id" :title="notification.message" type="info"
      show-icon closable @close="removeNotification(notification.id)" class="notification-alert" />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted, nextTick } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  user: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['logout'])

// WebSocket连接
let websocket = null

// 消息相关
const messages = ref([])
const messageInput = ref('')
const notifications = ref([])

// DOM引用
const messagesContainer = ref(null)

// 动作映射
const actionMap = {
  'ZOOM_IN': '放大操作',
  'ZOOM_OUT': '缩小操作',
  'ROTATE_LEFT': '左旋操作',
  'ROTATE_RIGHT': '右旋操作'
}

// 建立WebSocket连接
const connectWebSocket = () => {
  try {
    // 连接到后端WebSocket服务
    const wsUrl = `ws://localhost:8082/ws/picture/edit?pictureId=1`
    websocket = new WebSocket(wsUrl)

    websocket.onopen = () => {
      ElMessage.success(`${props.user.userName} 成功连接到协作编辑`)

      // 发送进入编辑的消息
      const enterMessage = {
        type: "ENTER_EDIT"
      }
      websocket.send(JSON.stringify(enterMessage))
    }

    websocket.onmessage = (event) => {
      const data = JSON.parse(event.data)
      console.log(data);
      if (data.type === "INFO" || data.type === "ENTER_EDIT" || data.type === "EXIT_EDIT") {
        addSystemMessage(data.message)
      } else if (data.type === "EDIT_ACTION") {
        if (data.user.userId !== props.user.userId) {
          // 其他用户执行的操作，广播给当前用户，以通知形式展示
          const actionText = actionMap[data.editAction] || data.editAction
          ElMessage.success(`${data.user.userName} 正在执行 ${actionText}`)
          addSystemNotification(`${data.user.userName} 正在执行 ${actionText}`)
        }
      }
    }

    websocket.onerror = (error) => {
      console.error('WebSocket error:', error)
      ElMessage.error('WebSocket连接出错')
    }

    websocket.onclose = () => {
      console.log('WebSocket connection closed')
    }
  } catch (error) {
    console.error('Failed to connect to WebSocket:', error)
    ElMessage.error('无法连接到协作服务')
  }
}

// 添加系统消息
const addSystemMessage = (content) => {
  const now = new Date()
  const timeStr = `${now.getHours()}:${now.getMinutes().toString().padStart(2, '0')}`

  messages.value.push({
    userId: 0,
    content: content,
    time: timeStr,
    isSystem: true
  })

  scrollToBottom()
}

// 添加系统通知
const addSystemNotification = (message) => {
  const notification = {
    id: Date.now(),
    message: message
  }
  notifications.value.push(notification)

  // 3秒后自动移除通知
  setTimeout(() => {
    removeNotification(notification.id)
  }, 3000)
}

// 移除通知
const removeNotification = (id) => {
  const index = notifications.value.findIndex(n => n.id === id)
  if (index !== -1) {
    notifications.value.splice(index, 1)
  }
}

// 发送聊天消息
const sendMessage = () => {
  if (!messageInput.value.trim()) return

  const now = new Date()
  const timeStr = `${now.getHours()}:${now.getMinutes().toString().padStart(2, '0')}`

  messages.value.push({
    userId: props.user.userId,
    userName: props.user.userName,
    content: messageInput.value,
    time: timeStr
  })

  messageInput.value = ''
  scrollToBottom()
}

// 发送编辑动作
const sendAction = (action) => {
  if (websocket && websocket.readyState === WebSocket.OPEN) {
    const actionText = actionMap[action]
    addSystemMessage(`您执行了${actionText}`)

    // 发送编辑动作到后端
    const actionMessage = {
      type: "EDIT_ACTION",
      editAction: action
    }
    websocket.send(JSON.stringify(actionMessage))
  } else {
    ElMessage.error('未连接到协作服务')
  }
}

// 滚动到底部
const scrollToBottom = () => {
  nextTick(() => {
    if (messagesContainer.value) {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
    }
  })
}

// 返回登录页
const handleBack = () => {
  if (websocket) {
    // 发送退出编辑的消息
    if (websocket.readyState === WebSocket.OPEN) {
      const exitMessage = {
        type: "EXIT_EDIT"
      }
      websocket.send(JSON.stringify(exitMessage))
    }
    websocket.close()
  }
  emit('logout')
}

// 退出登录
const handleLogout = () => {
  if (websocket) {
    // 发送退出编辑的消息
    if (websocket.readyState === WebSocket.OPEN) {
      const exitMessage = {
        type: "EXIT_EDIT"
      }
      websocket.send(JSON.stringify(exitMessage))
    }
    websocket.close()
  }
  emit('logout')
}

onMounted(() => {
  connectWebSocket()
})

onUnmounted(() => {
  if (websocket) {
    websocket.close()
  }
})
</script>

<style scoped>
.editor-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.header-title {
  font-size: 18px;
  font-weight: bold;
}

.header-extra {
  display: flex;
  align-items: center;
  gap: 15px;
}

.chat-container,
.actions-container {
  margin-top: 20px;
}

.chat-card,
.actions-card {
  height: 100%;
}

.messages {
  height: 300px;
  overflow-y: auto;
  padding: 10px;
  border: 1px solid #eee;
  margin-bottom: 15px;
  background-color: #fafafa;
}

.message {
  margin-bottom: 15px;
  padding: 8px 12px;
  border-radius: 8px;
  background-color: #f0f0f0;
}

.self-message {
  background-color: #409eff;
  color: white;
  text-align: right;
}

.message-content {
  margin-bottom: 5px;
}

.message-time {
  font-size: 12px;
  opacity: 0.8;
}

.input-area {
  display: flex;
  gap: 10px;
}

.actions-buttons {
  display: flex;
  justify-content: space-around;
  flex-wrap: wrap;
  gap: 15px;
}

.notification-alert {
  position: fixed;
  top: 20px;
  right: 20px;
  z-index: 1000;
  min-width: 300px;
}

@media (max-width: 768px) {
  .editor-container {
    padding: 10px;
  }

  .actions-buttons {
    flex-direction: column;
  }

  .notification-alert {
    right: 10px;
    left: 10px;
  }
}
</style>