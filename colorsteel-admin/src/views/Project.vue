<template>
  <el-card>
    <template #header>
      <div class="toolbar">
        <span>项目</span>
        <el-button type="primary" @click="openCreate">新建项目</el-button>
      </div>
    </template>
    <el-form :inline="true" @submit.prevent="load">
      <el-form-item label="名称">
        <el-input v-model="query.name" clearable style="width: 200px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="load">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="table.records" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="projectNo" label="项目编号" width="180" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="customerId" label="客户ID" width="90" />
      <el-table-column prop="status" label="状态" width="100" />
      <el-table-column prop="contractAmount" label="合同金额" width="120" />
      <el-table-column label="操作" width="280" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="showProfit(row)">项目利润</el-button>
          <el-button link type="primary" @click="showLabor(row)">人工成本</el-button>
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

    <el-dialog v-model="createDialog" title="新建项目" width="520px" destroy-on-close @open="loadCustomers">
      <el-form :model="form" label-width="110px">
        <el-form-item label="项目名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="客户">
          <el-select v-model="form.customerId" clearable filterable style="width: 100%">
            <el-option v-for="c in customers" :key="c.id" :label="c.name" :value="c.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="开工日期">
          <el-date-picker v-model="form.startDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="竣工日期">
          <el-date-picker v-model="form.endDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
        </el-form-item>
        <el-form-item label="合同金额">
          <el-input-number v-model="form.contractAmount" :min="0" :precision="2" style="width: 100%" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remark" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="createDialog = false">取消</el-button>
        <el-button type="primary" :loading="saving" @click="submitCreate">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="profitDialog" title="项目利润" width="520px">
      <template v-if="profit">
        <el-descriptions :column="1" border>
          <el-descriptions-item label="收款">{{ profit.totalReceipt }}</el-descriptions-item>
          <el-descriptions-item label="材料成本">{{ profit.materialCost }}</el-descriptions-item>
          <el-descriptions-item label="人工成本">{{ profit.laborCost }}</el-descriptions-item>
          <el-descriptions-item label="项目费用">{{ profit.projectExpense }}</el-descriptions-item>
          <el-descriptions-item label="利润">{{ profit.profit }}</el-descriptions-item>
        </el-descriptions>
      </template>
    </el-dialog>

    <el-dialog v-model="laborDialog" title="项目人工成本" width="720px">
      <template v-if="labor">
        <p>合计：{{ labor.totalLaborCost }}</p>
        <el-table :data="labor.payrolls" border size="small">
          <el-table-column prop="payrollNo" label="工资单号" />
          <el-table-column prop="workerId" label="工人ID" width="90" />
          <el-table-column prop="amount" label="金额" />
          <el-table-column prop="workDays" label="工日" />
        </el-table>
      </template>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { fetchProjectPage, createProject, getProjectProfit, getProjectLaborCost } from '@/api/project'
import { fetchCustomerPage } from '@/api/customer'

const loading = ref(false)
const query = reactive({ current: 1, size: 10, name: '' })
const table = reactive({ records: [], total: 0 })

const createDialog = ref(false)
const saving = ref(false)
const form = reactive({
  name: '',
  customerId: undefined,
  startDate: '',
  endDate: '',
  contractAmount: undefined,
  remark: '',
})
const customers = ref([])

const profitDialog = ref(false)
const profit = ref(null)
const laborDialog = ref(false)
const labor = ref(null)

async function load() {
  loading.value = true
  try {
    const data = await fetchProjectPage({ ...query })
    table.records = data.records || []
    table.total = data.total || 0
  } finally {
    loading.value = false
  }
}

async function loadCustomers() {
  const data = await fetchCustomerPage({ current: 1, size: 200 })
  customers.value = data.records || []
}

function openCreate() {
  Object.assign(form, {
    name: '',
    customerId: undefined,
    startDate: '',
    endDate: '',
    contractAmount: undefined,
    remark: '',
  })
  createDialog.value = true
}

async function submitCreate() {
  if (!form.name?.trim()) return
  saving.value = true
  try {
    await createProject({
      name: form.name.trim(),
      customerId: form.customerId,
      startDate: form.startDate || undefined,
      endDate: form.endDate || undefined,
      contractAmount: form.contractAmount,
      remark: form.remark || undefined,
    })
    createDialog.value = false
    await load()
  } finally {
    saving.value = false
  }
}

async function showProfit(row) {
  profit.value = await getProjectProfit(row.id)
  profitDialog.value = true
}

async function showLabor(row) {
  labor.value = await getProjectLaborCost(row.id)
  laborDialog.value = true
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
