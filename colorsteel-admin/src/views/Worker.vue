<template>
  <el-card>
    <template #header>
      <div class="toolbar">
        <span>工人</span>
        <el-button type="primary" @click="openCreate">新增工人</el-button>
      </div>
    </template>
    <el-table :data="table.records" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column prop="workerNo" label="编号" width="160" />
      <el-table-column prop="name" label="姓名" />
      <el-table-column prop="phone" label="手机" width="130" />
      <el-table-column prop="unitWage" label="日单价" width="100" />
      <el-table-column label="操作" width="140" fixed="right">
        <template #default="{ row }">
          <el-button link type="primary" @click="openPayroll(row)">工资记录</el-button>
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

    <el-dialog v-model="createDialog" title="新增工人" width="480px" destroy-on-close>
      <el-form :model="form" label-width="100px">
        <el-form-item label="姓名" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="身份证">
          <el-input v-model="form.idCardNo" />
        </el-form-item>
        <el-form-item label="日单价">
          <el-input-number v-model="form.unitWage" :min="0" :precision="2" style="width: 100%" />
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

    <el-dialog v-model="payrollDialog" title="工资记录" width="800px">
      <div class="mb12">
        <el-button type="primary" size="small" @click="openPayrollForm">登记工资</el-button>
      </div>
      <el-table :data="payrolls" border size="small" v-loading="payrollLoading">
        <el-table-column prop="payrollNo" label="单号" width="180" />
        <el-table-column prop="periodStart" label="周期起" width="120" />
        <el-table-column prop="periodEnd" label="周期止" width="120" />
        <el-table-column prop="workDays" label="工日" width="90" />
        <el-table-column prop="unitWage" label="日单价" width="90" />
        <el-table-column prop="amount" label="实发" />
        <el-table-column prop="projectId" label="项目ID" width="90" />
      </el-table>

      <el-dialog v-model="payrollFormVisible" title="登记工资" width="520px" append-to-body destroy-on-close>
        <el-form :model="payrollForm" label-width="110px">
          <el-form-item label="计薪周期起" required>
            <el-date-picker v-model="payrollForm.periodStart" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-form-item>
          <el-form-item label="计薪周期止" required>
            <el-date-picker v-model="payrollForm.periodEnd" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-form-item>
          <el-form-item label="工日(可空)">
            <el-input-number v-model="payrollForm.workDays" :min="0" :precision="3" style="width: 100%" />
          </el-form-item>
          <el-form-item label="项目ID">
            <el-input-number
              v-model="payrollForm.projectId"
              :min="1"
              :value-on-clear="undefined"
              controls-position="right"
              style="width: 100%"
            />
          </el-form-item>
          <el-form-item label="扣款">
            <el-input-number v-model="payrollForm.deduction" :min="0" :precision="2" style="width: 100%" />
          </el-form-item>
          <el-form-item label="奖金">
            <el-input-number v-model="payrollForm.bonus" :min="0" :precision="2" style="width: 100%" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="payrollForm.remark" type="textarea" />
          </el-form-item>
        </el-form>
        <template #footer>
          <el-button @click="payrollFormVisible = false">取消</el-button>
          <el-button type="primary" :loading="payrollSaving" @click="submitPayroll">提交</el-button>
        </template>
      </el-dialog>
    </el-dialog>
  </el-card>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { fetchWorkerPage, createWorker } from '@/api/worker'
import { fetchPayrollPage, createPayroll } from '@/api/payroll'

const loading = ref(false)
const query = reactive({ current: 1, size: 10 })
const table = reactive({ records: [], total: 0 })

const createDialog = ref(false)
const saving = ref(false)
const form = reactive({
  name: '',
  phone: '',
  idCardNo: '',
  unitWage: undefined,
  remark: '',
})

const payrollDialog = ref(false)
const payrollLoading = ref(false)
const payrolls = ref([])
const currentWorkerId = ref(null)

const payrollFormVisible = ref(false)
const payrollSaving = ref(false)
const payrollForm = reactive({
  periodStart: '',
  periodEnd: '',
  workDays: undefined,
  projectId: undefined,
  deduction: 0,
  bonus: 0,
  remark: '',
})

async function load() {
  loading.value = true
  try {
    const data = await fetchWorkerPage({ ...query })
    table.records = data.records || []
    table.total = data.total || 0
  } finally {
    loading.value = false
  }
}

function openCreate() {
  Object.assign(form, { name: '', phone: '', idCardNo: '', unitWage: undefined, remark: '' })
  createDialog.value = true
}

async function submitCreate() {
  saving.value = true
  try {
    await createWorker({ ...form })
    createDialog.value = false
    await load()
  } finally {
    saving.value = false
  }
}

async function openPayroll(row) {
  currentWorkerId.value = row.id
  payrollDialog.value = true
  payrollLoading.value = true
  try {
    const data = await fetchPayrollPage({ current: 1, size: 50, workerId: row.id })
    payrolls.value = data.records || []
  } finally {
    payrollLoading.value = false
  }
}

function openPayrollForm() {
  payrollForm.periodStart = new Date().toISOString().slice(0, 10)
  payrollForm.periodEnd = new Date().toISOString().slice(0, 10)
  payrollForm.workDays = undefined
  payrollForm.projectId = undefined
  payrollForm.deduction = 0
  payrollForm.bonus = 0
  payrollForm.remark = ''
  payrollFormVisible.value = true
}

async function submitPayroll() {
  if (!currentWorkerId.value) return
  payrollSaving.value = true
  try {
    await createPayroll({
      workerId: currentWorkerId.value,
      periodStart: payrollForm.periodStart,
      periodEnd: payrollForm.periodEnd,
      workDays: payrollForm.workDays,
      projectId: payrollForm.projectId || undefined,
      deduction: payrollForm.deduction,
      bonus: payrollForm.bonus,
      remark: payrollForm.remark,
    })
    ElMessage.success('已保存')
    payrollFormVisible.value = false
    const data = await fetchPayrollPage({ current: 1, size: 50, workerId: currentWorkerId.value })
    payrolls.value = data.records || []
  } finally {
    payrollSaving.value = false
  }
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
.mb12 {
  margin-bottom: 12px;
}
</style>
