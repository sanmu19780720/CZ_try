<template>
  <div class="page" v-loading="loading">
    <div class="panel">
      <h2 class="section-title">案例展示</h2>
      <el-select v-model="query.caseCategory" clearable placeholder="筛选分类" @change="reload" style="width: 180px; margin-bottom: 10px">
        <el-option v-for="item in categories" :key="item" :label="item" :value="item" />
      </el-select>
      <div class="card-grid">
        <div v-for="item in rows" :key="item.id" @click="goDetail(item.id)">
          <img class="thumb" :src="item.coverImage" :alt="item.caseTitle" />
          <p class="line2">{{ item.caseTitle }}</p>
          <small>{{ item.caseCategory || '未分类' }}</small>
        </div>
      </div>
      <el-pagination
        style="margin-top: 10px"
        layout="prev, pager, next"
        :current-page="query.current"
        :page-size="query.size"
        :total="total"
        @current-change="pageChange"
      />
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getCases } from '../api/portal'

const router = useRouter()
const loading = ref(false)
const rows = ref([])
const total = ref(0)
const categories = ref([])
const query = reactive({ current: 1, size: 10, caseCategory: '' })

const load = async () => {
  loading.value = true
  try {
    const data = await getCases({ ...query, caseCategory: query.caseCategory || undefined })
    rows.value = data.records || []
    total.value = data.total || 0
    categories.value = [...new Set([...(categories.value || []), ...rows.value.map((it) => it.caseCategory).filter(Boolean)])]
  } catch (e) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

const reload = () => {
  query.current = 1
  load()
}

const pageChange = (p) => {
  query.current = p
  load()
}

const goDetail = (id) => router.push(`/cases/${id}`)

onMounted(load)
</script>
