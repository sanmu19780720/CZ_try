<template>
  <div class="page" v-loading="loading">
    <div class="panel">
      <img class="thumb" style="height: 180px" :src="detail.displayImage" :alt="detail.displayTitle" />
      <h2 class="section-title" style="margin-top: 10px">{{ detail.displayTitle }}</h2>
      <p><strong>价格：</strong>¥{{ detail.displayPrice || '--' }}/{{ detail.displayUnit || '项' }}</p>
      <p style="margin-top: 8px">{{ detail.displayDesc || '暂无描述' }}</p>
      <el-button type="primary" style="margin-top: 12px" @click="goInquiry">咨询此产品</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getProductDetail } from '../api/portal'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const detail = reactive({})

const load = async () => {
  loading.value = true
  try {
    Object.assign(detail, await getProductDetail(route.params.id))
  } catch (e) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

const goInquiry = () => router.push('/inquiry')

onMounted(load)
</script>
