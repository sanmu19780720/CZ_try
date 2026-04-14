<template>
  <div class="page">
    <div class="panel">
      <h2 class="section-title">在线咨询</h2>
      <el-form :model="form" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="form.customerName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="form.mobile" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="需求内容">
          <el-input v-model="form.inquiryContent" type="textarea" :rows="4" placeholder="请描述您的需求" />
        </el-form-item>
      </el-form>
      <el-button type="primary" :loading="submitting" @click="submit">提交</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { createInquiry } from '../api/portal'

const submitting = ref(false)
const form = reactive({
  customerName: '',
  mobile: '',
  inquiryContent: '',
})

const submit = async () => {
  if (!form.customerName || !form.mobile) return ElMessage.warning('请填写姓名和手机号')
  submitting.value = true
  try {
    await createInquiry({
      ...form,
      sourcePage: 'INQUIRY',
      inquiryType: 'GENERAL',
    })
    ElMessage.success('提交成功')
    form.customerName = ''
    form.mobile = ''
    form.inquiryContent = ''
  } catch (e) {
    ElMessage.error(e.message || '提交失败')
  } finally {
    submitting.value = false
  }
}
</script>
