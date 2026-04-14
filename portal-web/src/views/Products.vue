<template>
  <div class="page" v-loading="loading">
    <div class="panel">
      <h2 class="section-title">产品展示</h2>
      <el-tabs v-model="categoryId" @tab-change="changeTab">
        <el-tab-pane label="全部" name="" />
        <el-tab-pane v-for="item in categoryTabs" :key="item.value" :label="item.label" :name="String(item.value)" />
      </el-tabs>
      <div class="card-grid">
        <div v-for="item in rows" :key="item.id" @click="goDetail(item.id)">
          <img class="thumb" :src="item.displayImage" :alt="item.displayTitle" />
          <p class="line2">{{ item.displayTitle }}</p>
          <small>¥{{ item.displayPrice || '--' }}/{{ item.displayUnit || '项' }}</small>
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
import { getProductCategories, getProducts } from '../api/portal'

const router = useRouter()
const loading = ref(false)
const rows = ref([])
const total = ref(0)
const categoryId = ref('')
const query = reactive({ current: 1, size: 10 })
const categoryTabs = ref([])

const load = async () => {
  loading.value = true
  try {
    const data = await getProducts({
      current: query.current,
      size: query.size,
      categoryId: categoryId.value || undefined,
    })
    rows.value = data.records || []
    total.value = data.total || 0
  } catch (e) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  try {
    const data = await getProductCategories()
    categoryTabs.value = (data || []).map((it) => ({ label: it.name, value: it.id }))
  } catch (e) {
    ElMessage.error(e.message || '分类加载失败')
  }
}

const pageChange = (p) => {
  query.current = p
  load()
}

const changeTab = (name) => {
  categoryId.value = name
  query.current = 1
  load()
}

const goDetail = (id) => router.push(`/products/${id}`)

onMounted(async () => {
  await loadCategories()
  await load()
})
</script>
