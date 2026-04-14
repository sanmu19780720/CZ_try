<template>
  <el-container class="layout">
    <el-aside width="220px" class="aside">
      <div class="logo">彩钢 ERP</div>
      <el-menu router :default-active="$route.path" background-color="#304156" text-color="#bfcbd9" active-text-color="#409eff">
        <el-menu-item index="/customers">
          <span>客户管理</span>
        </el-menu-item>
        <el-menu-item index="/products">
          <span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/inventories">
          <span>库存列表</span>
        </el-menu-item>
        <el-menu-item index="/purchase-orders">
          <span>采购单</span>
        </el-menu-item>
        <el-menu-item index="/sales-orders">
          <span>销售单</span>
        </el-menu-item>
        <el-menu-item index="/projects">
          <span>项目</span>
        </el-menu-item>
        <el-menu-item index="/workers">
          <span>工人</span>
        </el-menu-item>
        <el-menu-item index="/reports">
          <span>报表</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header class="header">
        <span class="title">{{ title }}</span>
        <el-button type="primary" link @click="onLogout">退出</el-button>
      </el-header>
      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/store/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const title = computed(() => route.name || '')

function onLogout() {
  userStore.logout()
  router.push({ name: 'Login' })
}
</script>

<style scoped>
.layout {
  height: 100vh;
}
.aside {
  background: #304156;
}
.logo {
  height: 56px;
  line-height: 56px;
  text-align: center;
  color: #fff;
  font-weight: 600;
  font-size: 16px;
}
.header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border-bottom: 1px solid #ebeef5;
  background: #fff;
}
.title {
  font-size: 16px;
}
.main {
  background: #f0f2f5;
  padding: 16px;
}
</style>
