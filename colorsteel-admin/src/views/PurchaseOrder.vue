<template>
  <el-card>
    <template #header>
      <div class="toolbar">
        <span>采购单</span>
        <el-button type="primary" @click="openCreate">新建采购单</el-button>
      </div>
    </template>
    <el-form :inline="true" @submit.prevent="load">
      <el-form-item label="状态">
        <el-select v-model="query.status" clearable placeholder="全部" style="width: 140px">
          <el-option label="草稿" value="DRAFT" />
          <el-option label="已审核" value="APPROVED" />
          <el-option label="已作废" value="CANCELLED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="load">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="table.records" border v-loading="loading">
      <el-table-column prop="purchaseNo" label="单号" width="180" />
      <el-table-column prop="supplierId" label="供应商ID" width="100" />
      <el-table-column prop="warehouseId" label="仓库ID" width="90" />
      <el-table-column prop="orderDate" label="单据日期" width="120" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column prop="totalAmount" label="金额" width="120" />
      <el-table-column label="操作" width="220" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="viewDetail(row)">详情</el-button>
          <el-button v-if="row.status === 'DRAFT'" link type="success" @click="approve(row)">审核</el-button>
          <el-button v-if="row.status === 'DRAFT'" link type="danger" @click="cancel(row)">作废</el-button>
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

    <el-dialog v-model="createDialog" title="新建采购单" width="720px" destroy-on-close @open="onCreateOpen">
      <el-form label-width="100px">
        <el-form-item label="供应商" required>
          <el-select v-model="form.supplierId" filterable style="width: 100%">
            <el-option v-for="s in suppliers" :key="s.id" :label="s.name + ' (' + s.supplierNo + ')'" :value="s.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="仓库" required>
          <el-select v-model="form.warehouseId" filterable style="width: 100%">
            <el-option v-for="w in warehouses" :key="w.id" :label="w.name" :value="w.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="单据日期" required>
          <el-date-picker v-model="form.orderDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
        <el-divider>明细</el-divider>
        <el-button size="small" @click="addLine">添加行</el-button>
        <el-table :data="form.lines" border size="small" style="margin-top: 8px">
          <el-table-column label="商品">
            <template #default="{ row }">
              <el-select v-model="row.productId" filterable placeholder="选择商品" style="width: 100%">
                <el-option v-for="p in products" :key="p.id" :label="p.sku + ' ' + p.name" :value="p.id" />
              </el-select>
            </template>
          </el-table-column>
          <el-table-column label="数量" width="140">
            <template #default="{ row }">
              <el-input-number v-model="row.quantity" :min="0.001" :precision="3" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column label="单价" width="140">
            <template #default="{ row }">
              <el-input-number v-model="row.unitPrice" :min="0" :precision="2" style="width: 100%" />
            </template>
          </el-table-column>
          <el-table-column width="80">
            <template #default="{ $index }">
              <el-button link type="danger" @click="form.lines.splice($index, 1)">删</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-form>
      <template #footer>
        <el-button @click="createDialog = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitCreate">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="detailDialog" title="采购单详情" width="800px">
      <template v-if="detail">
        <p>单号：{{ detail.order.purchaseNo }} 状态：{{ detail.order.status }} 金额：{{ detail.order.totalAmount }}</p>
        <el-table :data="detail.items" border size="small">
          <el-table-column prop="lineNo" label="行" width="60" />
          <el-table-column prop="productId" label="商品ID" width="90" />
          <el-table-column prop="quantity" label="数量" />
          <el-table-column prop="unitPrice" label="单价" />
          <el-table-column prop="amount" label="金额" />
        </el-table>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  fetchPurchaseOrderPage,
  getPurchaseOrderDetail,
  createPurchaseOrder,
  approvePurchaseOrder,
  cancelPurchaseOrder,
} from '@/api/purchase'
import { fetchSupplierPage } from '@/api/supplier'
import { fetchWarehousePage } from '@/api/warehouse'
import { fetchProductPage } from '@/api/product'

const loading = ref(false)
const query = reactive({ current: 1, size: 10, status: '' })
const table = reactive({ records: [], total: 0 })

const createDialog = ref(false)
const saving = ref(false)
const form = reactive({
  supplierId: undefined,
  warehouseId: undefined,
  orderDate: '',
  remark: '',
  lines: [],
})
const suppliers = ref([])
const warehouses = ref([])
const products = ref([])

const detailDialog = ref(false)
const detail = ref(null)

async function load() {
  loading.value = true
  try {
    const params = { current: query.current, size: query.size }
    if (query.status) params.status = query.status
    const data = await fetchPurchaseOrderPage(params)
    table.records = data.records || []
    table.total = data.total || 0
  } finally {
    loading.value = false
  }
}

async function onCreateOpen() {
  const [sup, wh, pr] = await Promise.all([
    fetchSupplierPage({ current: 1, size: 200 }),
    fetchWarehousePage({ current: 1, size: 100 }),
    fetchProductPage({ current: 1, size: 500 }),
  ])
  suppliers.value = sup.records || []
  warehouses.value = wh.records || []
  products.value = pr.records || []
  form.supplierId = suppliers.value[0]?.id
  form.warehouseId = warehouses.value[0]?.id
  form.orderDate = new Date().toISOString().slice(0, 10)
  form.remark = ''
  form.lines = [{ productId: undefined, quantity: 1, unitPrice: 0 }]
}

function openCreate() {
  createDialog.value = true
}

function addLine() {
  form.lines.push({ productId: undefined, quantity: 1, unitPrice: 0 })
}

async function submitCreate() {
  const lines = form.lines
    .filter((l) => l.productId && l.quantity > 0)
    .map((l) => ({
      productId: l.productId,
      quantity: l.quantity,
      unitPrice: l.unitPrice ?? 0,
      remark: l.remark,
    }))
  if (!lines.length) {
    ElMessage.warning('请至少一行有效明细')
    return
  }
  saving.value = true
  try {
    await createPurchaseOrder({
      supplierId: form.supplierId,
      warehouseId: form.warehouseId,
      orderDate: form.orderDate,
      remark: form.remark,
      lines,
    })
    createDialog.value = false
    ElMessage.success('创建成功')
    await load()
  } finally {
    saving.value = false
  }
}

async function viewDetail(row) {
  detail.value = await getPurchaseOrderDetail(row.id)
  detailDialog.value = true
}

async function approve(row) {
  await ElMessageBox.confirm('确认审核入库？', '提示', { type: 'warning' })
  await approvePurchaseOrder(row.id)
  ElMessage.success('已审核')
  await load()
}

async function cancel(row) {
  await ElMessageBox.confirm('确认作废？', '提示', { type: 'warning' })
  await cancelPurchaseOrder(row.id)
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
