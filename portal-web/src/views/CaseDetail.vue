<template>
  <div class="page" v-loading="loading">
    <div class="panel">
      <h2 class="section-title">{{ detail.caseTitle }}</h2>
      <p><strong>分类：</strong>{{ detail.caseCategory || '-' }}</p>
      <p><strong>地址：</strong>{{ detail.projectAddress || '-' }}</p>
      <p style="margin: 8px 0 12px">{{ detail.caseDesc || '暂无描述' }}</p>
      <el-carousel v-if="detail.images?.length" height="220px" indicator-position="outside">
        <el-carousel-item v-for="img in detail.images" :key="img.id">
          <img class="thumb" style="height: 220px" :src="img.imageUrl" alt="case image" />
        </el-carousel-item>
      </el-carousel>
      <el-empty v-else description="暂无案例图片" />
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCaseDetail } from '../api/portal'

const route = useRoute()
const loading = ref(false)
const detail = reactive({ images: [] })

const load = async () => {
  loading.value = true
  try {
    Object.assign(detail, await getCaseDetail(route.params.id))
  } catch (e) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(load)
</script>
