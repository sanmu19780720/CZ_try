<template>
  <el-card v-loading="loading">
    <template #header>
      <div class="toolbar">
        <span>站点设置</span>
        <el-button type="primary" :loading="saving" @click="submit">保存</el-button>
      </div>
    </template>
    <el-form :model="form" label-width="120px">
      <el-form-item label="站点名称">
        <el-input v-model="form.siteName" />
      </el-form-item>
      <el-form-item label="站点Logo">
        <el-input v-model="form.siteLogo" placeholder="支持图片上传或URL" />
        <el-upload :auto-upload="false" :show-file-list="false" accept="image/*" :on-change="(f) => onUploadImage(f, 'siteLogo')">
          <el-button size="small">上传图片</el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="联系电话"><el-input v-model="form.contactPhone" /></el-form-item>
      <el-form-item label="联系微信"><el-input v-model="form.contactWechat" /></el-form-item>
      <el-form-item label="联系地址"><el-input v-model="form.contactAddress" /></el-form-item>
      <el-form-item label="站点简介"><el-input v-model="form.siteIntro" type="textarea" :rows="3" /></el-form-item>
      <el-form-item label="分享标题"><el-input v-model="form.shareTitle" /></el-form-item>
      <el-form-item label="分享描述"><el-input v-model="form.shareDesc" type="textarea" :rows="2" /></el-form-item>
      <el-form-item label="分享图片">
        <el-input v-model="form.shareImage" placeholder="支持图片上传或URL" />
        <el-upload :auto-upload="false" :show-file-list="false" accept="image/*" :on-change="(f) => onUploadImage(f, 'shareImage')">
          <el-button size="small">上传图片</el-button>
        </el-upload>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getPortalSiteConfig, updatePortalSiteConfig, uploadPortalImage } from '@/api/portalAdmin'

const loading = ref(false)
const saving = ref(false)
const form = reactive({
  siteName: '',
  siteLogo: '',
  contactPhone: '',
  contactWechat: '',
  contactAddress: '',
  siteIntro: '',
  shareTitle: '',
  shareDesc: '',
  shareImage: '',
})

const load = async () => {
  loading.value = true
  try {
    Object.assign(form, (await getPortalSiteConfig()) || {})
  } finally {
    loading.value = false
  }
}

const submit = async () => {
  if (!form.siteName?.trim()) return ElMessage.warning('请填写站点名称')
  saving.value = true
  try {
    await updatePortalSiteConfig({ ...form, siteName: form.siteName.trim() })
    ElMessage.success('保存成功')
  } finally {
    saving.value = false
  }
}

const onUploadImage = async (file, field) => {
  const raw = file.raw
  if (!raw) return
  form[field] = await uploadPortalImage(raw)
  ElMessage.success('图片上传成功')
}

onMounted(load)
</script>

<style scoped>
.toolbar { display: flex; justify-content: space-between; align-items: center; }
</style>
