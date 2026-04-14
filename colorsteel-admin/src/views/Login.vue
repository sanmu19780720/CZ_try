<template>
  <div class="wrap">
    <el-card class="card">
      <template #header>彩钢 ERP 登录</template>
      <el-form :model="form" @submit.prevent="onSubmit">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="username" />
        </el-form-item>
        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" autocomplete="current-password" show-password />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" native-type="submit" :loading="loading" style="width: 100%">登录</el-button>
        </el-form-item>
      </el-form>
      <p class="hint">演示账号：admin / admin123</p>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const loading = ref(false)
const form = reactive({
  username: 'admin',
  password: 'admin123',
})

async function onSubmit() {
  loading.value = true
  try {
    const data = await login({ username: form.username, password: form.password })
    userStore.setToken(data.token)
    ElMessage.success('登录成功')
    const redirect = route.query.redirect || '/customers'
    router.replace(redirect)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.wrap {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #1d3557, #457b9d);
}
.card {
  width: 400px;
}
.hint {
  font-size: 12px;
  color: #909399;
  margin: 0;
  text-align: center;
}
</style>
