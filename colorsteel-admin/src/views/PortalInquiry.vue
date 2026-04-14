<template>
  <el-card>
    <template #header><span>留资管理</span></template>
    <el-form :inline="true" class="mb12">
      <el-form-item label="姓名"><el-input v-model="query.customerName" clearable /></el-form-item>
      <el-form-item label="手机号"><el-input v-model="query.mobile" clearable /></el-form-item>
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable style="width: 140px">
          <el-option value="NEW" label="NEW" />
          <el-option value="FOLLOWING" label="FOLLOWING" />
          <el-option value="CLOSED" label="CLOSED" />
        </el-select>
      </el-form-item>
      <el-form-item><el-button type="primary" @click="load">查询</el-button></el-form-item>
    </el-form>

    <el-table :data="rows" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="customerName" label="客户姓名" width="130" />
      <el-table-column prop="mobile" label="电话" width="140" />
      <el-table-column prop="inquiryType" label="咨询类型" width="120" />
      <el-table-column prop="inquiryContent" label="咨询内容" min-width="240" show-overflow-tooltip />
      <el-table-column prop="status" label="状态" width="110" />
      <el-table-column prop="createdAt" label="时间" width="180" />
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }"><el-button link type="primary" @click="openFollow(row)">跟进</el-button></template>
      </el-table-column>
    </el-table>
    <el-pagination class="pager" v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="load" />

    <el-dialog v-model="dialogVisible" title="留资跟进" width="520px">
      <el-form :model="followForm" label-width="100px">
        <el-form-item label="状态">
          <el-select v-model="followForm.status" style="width: 100%">
            <el-option value="NEW" label="NEW" />
            <el-option value="FOLLOWING" label="FOLLOWING" />
            <el-option value="CLOSED" label="CLOSED" />
          </el-select>
        </el-form-item>
        <el-form-item label="跟进备注"><el-input v-model="followForm.followUpNote" type="textarea" :rows="4" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitFollow">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchPortalInquiryPage, followPortalInquiry } from '@/api/portalAdmin'

const loading = ref(false)
const saving = ref(false)
const rows = ref([])
const total = ref(0)
const query = reactive({ current: 1, size: 10, customerName: '', mobile: '', status: '' })
const dialogVisible = ref(false)
const currentId = ref(null)
const followForm = reactive({ status: 'FOLLOWING', followUpNote: '' })

const load = async () => {
  loading.value = true
  try {
    const data = await fetchPortalInquiryPage(query)
    rows.value = data.records || []
    total.value = data.total || 0
  } finally { loading.value = false }
}

const openFollow = (row) => {
  currentId.value = row.id
  followForm.status = row.status || 'FOLLOWING'
  followForm.followUpNote = row.followUpNote || ''
  dialogVisible.value = true
}

const submitFollow = async () => {
  if (!currentId.value) return
  saving.value = true
  try {
    await followPortalInquiry(currentId.value, { ...followForm })
    dialogVisible.value = false
    ElMessage.success('跟进信息已保存')
    load()
  } finally { saving.value = false }
}

onMounted(load)
</script>

<style scoped>
.mb12 { margin-bottom: 12px; }
.pager { margin-top: 16px; justify-content: flex-end; }
</style>
