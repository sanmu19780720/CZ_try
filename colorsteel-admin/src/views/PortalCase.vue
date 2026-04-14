<template>
  <el-card>
    <template #header>
      <div class="toolbar">
        <span>案例管理</span>
        <div>
          <el-button @click="submitSort">保存排序</el-button>
          <el-button type="primary" @click="openCreate">新增案例</el-button>
        </div>
      </div>
    </template>
    <el-table :data="rows" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column label="封面" width="100"><template #default="{ row }"><img :src="row.coverImage" class="thumb" /></template></el-table-column>
      <el-table-column prop="caseTitle" label="标题" />
      <el-table-column prop="caseCategory" label="分类" width="120" />
      <el-table-column label="推荐" width="100">
        <template #default="{ row }"><el-switch :model-value="row.isRecommended === 1" @change="(v) => toggleRecommended(row, v)" /></template>
      </el-table-column>
      <el-table-column label="状态" width="100"><template #default="{ row }"><el-switch v-model="row.status" active-value="ENABLED" inactive-value="DISABLED" @change="() => quickUpdate(row)" /></template></el-table-column>
      <el-table-column label="排序" width="120"><template #default="{ row }"><el-input-number v-model="row.sortNo" :min="0" /></template></el-table-column>
      <el-table-column label="操作" width="260" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="primary" @click="openImageManager(row)">图片管理</el-button>
          <el-button link type="danger" @click="remove(row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination class="pager" v-model:current-page="query.current" v-model:page-size="query.size" :total="total" layout="total, prev, pager, next" @current-change="load" />

    <el-dialog v-model="dialogVisible" :title="form.id ? '编辑案例' : '新增案例'" width="620px">
      <el-form :model="form" label-width="100px">
        <el-form-item label="案例标题"><el-input v-model="form.caseTitle" /></el-form-item>
        <el-form-item label="案例分类"><el-input v-model="form.caseCategory" /></el-form-item>
        <el-form-item label="封面图">
          <el-input v-model="form.coverImage" />
          <el-upload :auto-upload="false" :show-file-list="false" accept="image/*" :on-change="(f) => onUploadImage(f, 'coverImage')"><el-button size="small">上传图片</el-button></el-upload>
        </el-form-item>
        <el-form-item label="案例描述"><el-input v-model="form.caseDesc" type="textarea" :rows="3" /></el-form-item>
        <el-form-item label="项目地址"><el-input v-model="form.projectAddress" /></el-form-item>
        <el-form-item label="首页展示"><el-switch v-model="showOnHome" /></el-form-item>
        <el-form-item label="推荐"><el-switch v-model="recommended" /></el-form-item>
        <el-form-item label="排序"><el-input-number v-model="form.sortNo" :min="0" /></el-form-item>
        <el-form-item label="状态"><el-switch v-model="form.status" active-value="ENABLED" inactive-value="DISABLED" /></el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="imageDialogVisible" title="案例图片管理" width="700px">
      <div class="img-toolbar">
        <el-upload :auto-upload="false" :show-file-list="false" accept="image/*" :on-change="onCaseImageUpload">
          <el-button type="primary">上传图片</el-button>
        </el-upload>
      </div>
      <el-table :data="currentImages" border>
        <el-table-column prop="id" label="ID" width="70" />
        <el-table-column label="图片"><template #default="{ row }"><img :src="row.imageUrl" class="thumb" /></template></el-table-column>
        <el-table-column prop="sortNo" label="排序" width="120" />
        <el-table-column label="操作" width="100"><template #default="{ row }"><el-button link type="danger" @click="removeCaseImage(row)">删除</el-button></template></el-table-column>
      </el-table>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { computed, reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { addPortalCaseImage, createPortalCase, deletePortalCase, deletePortalCaseImage, fetchPortalCasePage, sortPortalCases, updatePortalCase, updatePortalCaseRecommended, uploadPortalImage } from '@/api/portalAdmin'

const loading = ref(false)
const saving = ref(false)
const query = reactive({ current: 1, size: 10 })
const rows = ref([])
const total = ref(0)
const dialogVisible = ref(false)
const form = reactive({
  id: null, caseTitle: '', caseCategory: '', coverImage: '', caseDesc: '',
  projectAddress: '', isRecommended: 0, showOnHome: 0, sortNo: 0, status: 'ENABLED',
})
const recommended = computed({ get: () => form.isRecommended === 1, set: (v) => (form.isRecommended = v ? 1 : 0) })
const showOnHome = computed({ get: () => form.showOnHome === 1, set: (v) => (form.showOnHome = v ? 1 : 0) })
const imageDialogVisible = ref(false)
const currentCase = ref(null)
const currentImages = ref([])

const load = async () => {
  loading.value = true
  try {
    const data = await fetchPortalCasePage(query)
    rows.value = data.records || []
    total.value = data.total || 0
  } finally { loading.value = false }
}
const openCreate = () => {
  Object.assign(form, {
    id: null, caseTitle: '', caseCategory: '', coverImage: '', caseDesc: '',
    projectAddress: '', isRecommended: 0, showOnHome: 0, sortNo: 0, status: 'ENABLED',
  })
  dialogVisible.value = true
}
const openEdit = (row) => { Object.assign(form, { ...row }); dialogVisible.value = true }
const submit = async () => {
  if (!form.caseTitle?.trim()) return ElMessage.warning('请填写案例标题')
  saving.value = true
  try {
    const payload = { ...form, caseTitle: form.caseTitle.trim() }
    if (form.id) await updatePortalCase(form.id, payload)
    else await createPortalCase(payload)
    dialogVisible.value = false
    await load()
  } finally { saving.value = false }
}
const quickUpdate = (row) => updatePortalCase(row.id, row)
const toggleRecommended = async (row, v) => {
  await updatePortalCaseRecommended(row.id, v ? 1 : 0)
  row.isRecommended = v ? 1 : 0
}
const remove = async (id) => {
  await ElMessageBox.confirm('确认删除该案例吗？', '提示')
  await deletePortalCase(id)
  ElMessage.success('已删除')
  load()
}
const submitSort = async () => {
  await sortPortalCases(rows.value.map((it) => ({ id: it.id, sortNo: it.sortNo || 0 })))
  ElMessage.success('排序已保存')
  load()
}
const openImageManager = (row) => {
  currentCase.value = row
  currentImages.value = row.images || []
  imageDialogVisible.value = true
}
const onCaseImageUpload = async (file) => {
  if (!currentCase.value) return
  const imageUrl = await uploadPortalImage(file.raw)
  await addPortalCaseImage(currentCase.value.id, { imageUrl, sortNo: currentImages.value.length + 1 })
  await load()
  const refreshed = rows.value.find((it) => it.id === currentCase.value.id)
  currentImages.value = refreshed?.images || []
  ElMessage.success('图片已上传')
}
const removeCaseImage = async (image) => {
  await deletePortalCaseImage(currentCase.value.id, image.id)
  currentImages.value = currentImages.value.filter((it) => it.id !== image.id)
  ElMessage.success('图片已删除')
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
.img-toolbar { margin-bottom: 10px; }
</style>
