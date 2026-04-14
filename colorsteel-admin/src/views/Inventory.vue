<template>
  <el-card>
    <template #header>
      <div class="toolbar">
        <span>库存列表</span>
        <el-button type="primary" @click="openCreate">新增库存行</el-button>
      </div>
    </template>
    <el-form :inline="true" @submit.prevent="load">
      <el-form-item label="仓库ID">
        <el-input-number v-model="query.warehouseId" :min="1" clearable controls-position="right" />
      </el-form-item>
      <el-form-item label="商品ID">
        <el-input-number v-model="query.productId" :min="1" clearable controls-position="right" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="load">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="table.records" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="warehouseId" label="仓库ID" width="100" />
      <el-table-column prop="productId" label="商品ID" width="100" />
      <el-table-column prop="quantity" label="数量" />
      <el-table-column prop="avgUnitCost" label="加权成本" />
      <el-table-column label="操作" width="120" fixed="right">
        <template #default="{ row }">
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

    <el-dialog v-model="dialog.visible" title="新增库存行" width="480px" destroy-on-close>
      <el-form :model="form" label-width="110px">
        <el-form-item label="仓库ID" required>
          <el-input-number v-model="form.warehouseId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="商品ID" required>
          <el-input-number v-model="form.productId" :min="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="form.quantity" :min="0" :precision="3" style="width: 100%" />
        </el-form-item>
        <el-form-item label="加权成本">
          <el-input-number v-model="form.avgUnitCost" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialog.visible = false">取消</el-button>
        <el-button type="primary" :loading="dialog.saving" @click="saveCreate">保存</el-button>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessageBox } from 'element-plus'
import { fetchInventoryPage, createInventory, deleteInventory } from '@/api/inventory'

const loading = ref(false)
const query = reactive({ current: 1, size: 10, warehouseId: undefined, productId: undefined })
const table = reactive({ records: [], total: 0 })

const dialog = reactive({ visible: false, saving: false })
const form = reactive({
  warehouseId: 1,
  productId: undefined,
  quantity: 0,
  avgUnitCost: undefined,
})

async function load() {
  loading.value = true
  try {
    const params = { current: query.current, size: query.size }
    if (query.warehouseId) params.warehouseId = query.warehouseId
    if (query.productId) params.productId = query.productId
    const data = await fetchInventoryPage(params)
    table.records = data.records || []
    table.total = data.total || 0
  } finally {
    loading.value = false
  }
}

function openCreate() {
  form.warehouseId = 1
  form.productId = undefined
  form.quantity = 0
  form.avgUnitCost = undefined
  dialog.visible = true
}

async function saveCreate() {
  dialog.saving = true
  try {
    await createInventory({ ...form })
    dialog.visible = false
    await load()
  } finally {
    dialog.saving = false
  }
}

async function onDelete(row) {
  await ElMessageBox.confirm('确认删除该库存行？', '提示', { type: 'warning' })
  await deleteInventory(row.id)
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
