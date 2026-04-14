<template>
  <el-card>
    <template #header>
      <div class="toolbar">
        <span>商品管理</span>
        <el-button type="primary" @click="openCreate">新增商品</el-button>
      </div>
    </template>
    <el-form :inline="true" @submit.prevent="load">
      <el-form-item label="品名">
        <el-input v-model="query.name" clearable style="width: 200px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="load">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="table.records" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="sku" label="SKU" width="140" />
      <el-table-column prop="name" label="品名" />
      <el-table-column prop="unit" label="单位" width="80" />
      <el-table-column prop="standardPrice" label="参考售价" width="100" />
      <el-table-column prop="lastPurchasePrice" label="最近采购价" width="110" />
      <el-table-column prop="avgCostPrice" label="平均成本" width="100" />
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openEdit(row)">编辑</el-button>
          <el-button link type="danger" @click="onDelete(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      class="pager"
      v-model:current-page="query.current"
      v-model:page-size="query.size"
      :total="table.total"
      layout="total, prev, pager, next"
      @current-change="load"
    />

    <el-dialog v-model="dialog.visible" :title="dialog.id ? '编辑商品' : '新增商品'" width="520px" destroy-on-close @closed="resetForm">
      <el-form :model="form" label-width="100px">
        <el-form-item v-if="!dialog.id" label="SKU" required>
          <el-input v-model="form.sku" />
        </el-form-item>
        <el-form-item label="品名" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="单位" required>
          <el-input v-model="form.unit" placeholder="默认 件" />
        </el-form-item>
        <el-form-item label="规格">
          <el-input v-model="form.spec" />
        </el-form-item>
        <el-form-item label="参考售价">
          <el-input-number v-model="form.standardPrice" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="dialog.saving" @click="save">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessageBox } from 'element-plus'
import { fetchProductPage, getProduct, createProduct, updateProduct, deleteProduct } from '@/api/product'

const loading = ref(false)
const query = reactive({ current: 1, size: 10, name: '' })
const table = reactive({ records: [], total: 0 })

const dialog = reactive({ visible: false, id: null, saving: false })
const form = reactive({
  sku: '',
  name: '',
  unit: '件',
  spec: '',
  standardPrice: undefined,
  remark: '',
})

async function load() {
  loading.value = true
  try {
    const data = await fetchProductPage({ ...query })
    table.records = data.records || []
    table.total = data.total || 0
  } finally {
    loading.value = false
  }
}

function openCreate() {
  dialog.id = null
  resetForm()
  dialog.visible = true
}

async function openEdit(row) {
  dialog.id = row.id
  const d = await getProduct(row.id)
  Object.assign(form, {
    sku: d.sku,
    name: d.name,
    unit: d.unit || '件',
    spec: d.spec,
    standardPrice: d.standardPrice != null ? Number(d.standardPrice) : undefined,
    remark: d.remark,
  })
  dialog.visible = true
}

function resetForm() {
  Object.assign(form, {
    sku: '',
    name: '',
    unit: '件',
    spec: '',
    standardPrice: undefined,
    remark: '',
  })
}

async function save() {
  dialog.saving = true
  try {
    if (dialog.id) {
      await updateProduct(dialog.id, {
        name: form.name,
        unit: form.unit,
        spec: form.spec,
        standardPrice: form.standardPrice,
        remark: form.remark,
      })
    } else {
      await createProduct({ ...form })
    }
    dialog.visible = false
    await load()
  } finally {
    dialog.saving = false
  }
}

async function onDelete(row) {
  await ElMessageBox.confirm('确认删除？', '提示', { type: 'warning' })
  await deleteProduct(row.id)
  await load()
}

onMounted(load)
</script>

<style scoped>
.toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.pager {
  margin-top: 16px;
  justify-content: flex-end;
}
</style>
