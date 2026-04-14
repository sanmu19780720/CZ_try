<template>
  <div class="page" v-loading="loading">
    <div class="panel">
      <el-carousel height="160px" indicator-position="outside">
        <el-carousel-item v-for="item in home.banners" :key="item.id">
          <img class="thumb" style="height: 160px" :src="item.imageUrl" :alt="item.title" />
        </el-carousel-item>
      </el-carousel>
    </div>

    <div class="panel">
      <h2 class="section-title">{{ home.siteConfig?.siteName || '彩钢厂门户' }}</h2>
      <p class="line2">{{ home.siteConfig?.siteIntro || '欢迎访问我们的展示门户。' }}</p>
    </div>

    <div class="panel">
      <h3 class="section-title">主推产品</h3>
      <div class="card-grid">
        <div v-for="item in home.featuredProducts" :key="item.id" @click="goProduct(item.id)">
          <img class="thumb" :src="item.displayImage" :alt="item.displayTitle" />
          <p class="line2">{{ item.displayTitle }}</p>
          <small>¥{{ item.displayPrice || '--' }}/{{ item.displayUnit || '项' }}</small>
        </div>
      </div>
    </div>

    <div class="panel">
      <h3 class="section-title">价格卡片</h3>
      <el-table :data="home.priceCards" size="small" stripe>
        <el-table-column prop="title" label="项目" />
        <el-table-column prop="referencePrice" label="参考价" width="120" />
      </el-table>
    </div>

    <div class="panel">
      <h3 class="section-title">案例精选</h3>
      <div class="card-grid">
        <div v-for="item in home.recommendedCases" :key="item.id" @click="goCase(item.id)">
          <img class="thumb" :src="item.coverImage" :alt="item.caseTitle" />
          <p class="line2">{{ item.caseTitle }}</p>
        </div>
      </div>
    </div>

    <div class="panel">
      <h3 class="section-title">联系方式</h3>
      <p>电话：<a :href="`tel:${home.contactInfo?.contactPhone || ''}`">{{ home.contactInfo?.contactPhone || '-' }}</a></p>
      <p>微信：{{ home.contactInfo?.contactWechat || '-' }}</p>
      <p>地址：{{ home.contactInfo?.contactAddress || '-' }}</p>
      <el-button type="primary" plain style="margin-top: 8px" :href="`tel:${home.contactInfo?.contactPhone || ''}`">一键拨号</el-button>
    </div>

    <div class="panel">
      <h3 class="section-title">快速留资</h3>
      <el-form :model="form" label-width="68px">
        <el-form-item label="姓名"><el-input v-model="form.customerName" /></el-form-item>
        <el-form-item label="手机"><el-input v-model="form.mobile" /></el-form-item>
        <el-form-item label="需求"><el-input v-model="form.inquiryContent" type="textarea" :rows="2" /></el-form-item>
      </el-form>
      <el-button type="primary" :loading="submitting" @click="submit">提交咨询</el-button>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { createInquiry, getHome } from '../api/portal'

const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const home = reactive({
  siteConfig: null,
  banners: [],
  featuredProducts: [],
  priceCards: [],
  recommendedCases: [],
  contactInfo: {},
})
const form = reactive({ customerName: '', mobile: '', inquiryContent: '' })

const load = async () => {
  loading.value = true
  try {
    const data = await getHome()
    Object.assign(home, data || {})
  } catch (e) {
    ElMessage.error(e.message || '加载失败')
  } finally {
    loading.value = false
  }
}

const submit = async () => {
  if (!form.customerName || !form.mobile) return ElMessage.warning('请填写姓名和手机号')
  submitting.value = true
  try {
    await createInquiry({ ...form, sourcePage: 'HOME' })
    ElMessage.success('提交成功，我们会尽快联系您')
    form.customerName = ''
    form.mobile = ''
    form.inquiryContent = ''
  } catch (e) {
    ElMessage.error(e.message || '提交失败')
  } finally {
    submitting.value = false
  }
}

const goProduct = (id) => router.push(`/products/${id}`)
const goCase = (id) => router.push(`/cases/${id}`)

onMounted(load)
</script>
