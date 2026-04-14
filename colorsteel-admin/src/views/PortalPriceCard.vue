<template>
  <el-card>
    <template #header>
      <div class="toolbar">
        <span>价格卡片管理</span>
        <div>
          <el-button @click="submitSort">保存排序</el-button>
          <el-button type="primary" @click="openCreate">新增价格卡片</el-button>
        </div>
      </div>
    </template>
    <el-table :data="rows" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column label="图片" width="100"><template #default="{ row }"><img :src="row.imageUrl" class="thumb" /></template></el-table-column>
      <el-table-column prop="title" label="标题" />
      <el-table-column prop="referencePrice" label="参考价" width="100" />
      <el-table-column prop="priceUnit" label="单位" width="80" />
      <el-table-column label="状态" width="100"><template #default="{ row }"><el-switch v-model="row.status" active-value="ENABLED" inactive-value="DISABLED" @change="() => quickUpdate(row)" /></template></el-table-column>
      <el-table-column label="排序" width="120"><template #default="{ row }"><el-input-number v-model="row.sortNo" :min="0" /></template></el-table-column>
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination class="pager" v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="load" />

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑价格卡片' : '新增价格卡片'" width="560px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="标题"><el-input v-model="form.title" /></el-form-item>
        <el-form-item label="图片">
          <el-input v-model="form.imageUrl" />
          <el-upload :auto-upload="false" :show-file-list="false" accept="image/*" :on-change="(f) => onUploadImage(f, 'imageUrl')"><el-button size="small">上传图片</el-button></el-upload>
        </el-form-item>
        <el-form-item label="参考价"><el-input-number v-model="form.referencePrice" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="单位"><el-input v-model="form.priceUnit" /></el-form-item>
        <el-form-item label="描述"><el-input v-model="form.descText" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortNo" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" active-value="ENABLED" inactive-value="DISABLED" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submit">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createPortalPriceCard, deletePortalPriceCard, fetchPortalPriceCardPage, sortPortalPriceCards, updatePortalPriceCard, uploadPortalImage } from '@/api/portalAdmin'

const loading = ref(false)
const saving = ref(false)
const query = reactive({ current: 1, size: 10 })
const rows = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({ id: null, title: '', imageUrl: '', referencePrice: undefined, priceUnit: '', descText: '', sortNo: 0, status: 'ENABLED' })

const load = async () => {
  loading.value = true
  try {
    const data = await fetchPortalPriceCardPage(query)
    rows.value = data.records || []
    total.value = data.total || 0
  } finally { loading.value = false }
}
const openCreate = () => { Object.assign(form, { id: null, title: '', imageUrl: '', referencePrice: undefined, priceUnit: '', descText: '', sortNo: 0, status: 'ENABLED' }); dialogVisible.value = true }
const openEdit = (row) => { Object.assign(form, { ...row }); dialogVisible.value = true }
const submit = async () => {
  if (!form.title?.trim()) return ElMessage.warning('请填写标题')
  saving.value = true
  try {
    const payload = { ...form, title: form.title.trim() }
    if (form.id) await updatePortalPriceCard(form.id, payload)
    else await createPortalPriceCard(payload)
    dialogVisible.value = false
    await load()
  } finally { saving.value = false }
}
const quickUpdate = (row) => updatePortalPriceCard(row.id, row)
const remove = async (id) => {
  await ElMessageBox.confirm('确认删除该价格卡片吗？', '提示')
  await deletePortalPriceCard(id)
  ElMessage.success('已删除')
  load()
}
const submitSort = async () => {
  await sortPortalPriceCards(rows.value.map((it) => ({ id: it.id, sortNo: it.sortNo || 0 })))
  ElMessage.success('排序已保存')
  load()
}
const onUploadImage = async (file, field) => {
  form[field] = await uploadPortalImage(file.raw)
  ElMessage.success('图片上传成功')
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; }
.pager { margin-top: 16px; justify-content: flex-end; }
.thumb { width: 70px; height: 40px; object-fit: cover; border-radius: 4px; }
</style>
