<template>
  <el-card>
    <template #header>
      <div class="toolbar">
        <span>展示产品管理</span>
        <div>
          <el-button @click="submitSort">保存排序</el-button>
          <el-button type="primary" @click="openCreate">新增展示产品</el-button>
        </div>
      </div>
    </template>
    <el-table :data="rows" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="productId" label="商品ID" width="90" />
      <el-table-column label="图片" width="100"><template #default="{ row }"><img :src="row.displayImage" class="thumb" /></template></el-table-column>
      <el-table-column prop="displayTitle" label="标题" />
      <el-table-column prop="displayPrice" label="价格" width="100" />
      <el-table-column label="首页展示" width="110">
        <template #default="{ row }"><el-switch :model-value="row.showOnHome === 1" @change="(v) => toggleHome(row, v)" /></template>
      </el-table-column>
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

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑展示产品' : '新增展示产品'" width="560px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="商品ID"><el-input-number v-model="form.productId" :min="1" /></el-form-item>
        <el-form-item label="展示标题"><el-input v-model="form.displayTitle" /></el-form-item>
        <el-form-item label="展示图片">
          <el-input v-model="form.displayImage" />
          <el-upload :auto-upload="false" :show-file-list="false" accept="image/*" :on-change="(f) => onUploadImage(f, 'displayImage')"><el-button size="small">上传图片</el-button></el-upload>
        </el-form-item>
        <el-form-item label="展示价格"><el-input-number v-model="form.displayPrice" :min="0" :precision="2" /></el-form-item>
        <el-form-item label="价格单位"><el-input v-model="form.displayUnit" /></el-form-item>
        <el-form-item label="展示描述"><el-input v-model="form.displayDesc" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="是否精选"><el-switch v-model="featured" /></el-form-item>
        <el-form-item label="首页展示"><el-switch v-model="home" /></el-form-item>
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
import { computed, reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createPortalProduct, deletePortalProduct, fetchPortalProductPage, sortPortalProducts, updatePortalProduct, updatePortalProductHome, uploadPortalImage } from '@/api/portalAdmin'

const loading = ref(false)
const saving = ref(false)
const query = reactive({ current: 1, size: 10 })
const rows = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({
  id: null, productId: undefined, displayTitle: '', displayImage: '', displayPrice: undefined, displayUnit: '',
  displayDesc: '', isFeatured: 0, showOnHome: 0, sortNo: 0, status: 'ENABLED',
})
const featured = computed({ get: () => form.isFeatured === 1, set: (v) => (form.isFeatured = v ? 1 : 0) })
const home = computed({ get: () => form.showOnHome === 1, set: (v) => (form.showOnHome = v ? 1 : 0) })

const load = async () => {
  loading.value = true
  try {
    const data = await fetchPortalProductPage(query)
    rows.value = data.records || []
    total.value = data.total || 0
  } finally { loading.value = false }
}
const openCreate = () => {
  Object.assign(form, {
    id: null, productId: undefined, displayTitle: '', displayImage: '', displayPrice: undefined, displayUnit: '',
    displayDesc: '', isFeatured: 0, showOnHome: 0, sortNo: 0, status: 'ENABLED',
  })
  dialogVisible.value = true
}
const openEdit = (row) => { Object.assign(form, { ...row }); dialogVisible.value = true }
const submit = async () => {
  if (!form.productId || !form.displayTitle?.trim()) return ElMessage.warning('请填写商品ID与展示标题')
  saving.value = true
  try {
    const payload = { ...form, displayTitle: form.displayTitle.trim() }
    if (form.id) await updatePortalProduct(form.id, payload)
    else await createPortalProduct(payload)
    dialogVisible.value = false
    await load()
  } finally { saving.value = false }
}
const quickUpdate = (row) => updatePortalProduct(row.id, row)
const toggleHome = async (row, v) => {
  await updatePortalProductHome(row.id, v ? 1 : 0)
  row.showOnHome = v ? 1 : 0
}
const remove = async (id) => {
  await ElMessageBox.confirm('确认删除该展示产品吗？', '提示')
  await deletePortalProduct(id)
  ElMessage.success('已删除')
  load()
}
const submitSort = async () => {
  await sortPortalProducts(rows.value.map((it) => ({ id: it.id, sortNo: it.sortNo || 0 })))
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
