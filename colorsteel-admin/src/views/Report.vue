<template>
  <div class="reports">
    <el-card class="block">
      <template #header>日报表</template>
      <el-date-picker v-model="dailyDate" type="date" value-format="YYYY-MM-DD" placeholder="选择日期" />
      <el-button type="primary" style="margin-left: 12px" :loading="loadingDaily" @click="loadDaily">查询</el-button>
      <el-descriptions v-if="daily" class="mt" :column="1" border>
        <el-descriptions-item label="销售收入">{{ daily.salesRevenue }}</el-descriptions-item>
        <el-descriptions-item label="项目收入">{{ daily.projectRevenue }}</el-descriptions-item>
        <el-descriptions-item label="材料成本">{{ daily.materialCost }}</el-descriptions-item>
        <el-descriptions-item label="人工成本">{{ daily.laborCost }}</el-descriptions-item>
        <el-descriptions-item label="项目费用">{{ daily.projectExpense }}</el-descriptions-item>
        <el-descriptions-item label="毛利">{{ daily.grossProfit }}</el-descriptions-item>
        <el-descriptions-item label="净利润">{{ daily.netProfit }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card class="block">
      <template #header>月报表</template>
      <el-date-picker v-model="monthVal" type="month" value-format="YYYY-MM" placeholder="选择月份" />
      <el-button type="primary" style="margin-left: 12px" :loading="loadingMonthly" @click="loadMonthly">查询</el-button>
      <el-descriptions v-if="monthly" class="mt" :column="1" border>
        <el-descriptions-item label="销售收入">{{ monthly.salesRevenue }}</el-descriptions-item>
        <el-descriptions-item label="项目收入">{{ monthly.projectRevenue }}</el-descriptions-item>
        <el-descriptions-item label="材料成本">{{ monthly.materialCost }}</el-descriptions-item>
        <el-descriptions-item label="人工成本">{{ monthly.laborCost }}</el-descriptions-item>
        <el-descriptions-item label="项目费用">{{ monthly.projectExpense }}</el-descriptions-item>
        <el-descriptions-item label="毛利">{{ monthly.grossProfit }}</el-descriptions-item>
        <el-descriptions-item label="净利润">{{ monthly.netProfit }}</el-descriptions-item>
      </el-descriptions>
    </el-card>

    <el-card class="block">
      <template #header>项目利润报表</template>
      <el-date-picker v-model="range" type="daterange" range-separator="至" start-placeholder="开始" end-placeholder="结束" value-format="YYYY-MM-DD" />
      <el-button type="primary" style="margin-left: 12px" :loading="loadingProj" @click="loadProjectProfit">查询</el-button>
      <el-table v-if="projRows.length" :data="projRows" border class="mt" size="small">
        <el-table-column prop="projectNo" label="项目编号" width="180" />
        <el-table-column prop="projectName" label="名称" />
        <el-table-column prop="projectRevenue" label="收款" width="110" />
        <el-table-column prop="materialCost" label="材料" width="110" />
        <el-table-column prop="laborCost" label="人工" width="110" />
        <el-table-column prop="projectExpense" label="费用" width="110" />
        <el-table-column prop="netProfit" label="净利润" width="110" />
      </el-table>
      <p v-else-if="projLoaded" class="empty">暂无数据</p>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { getDailyReport, getMonthlyReport, getProjectProfitReport } from '@/api/report'

const dailyDate = ref(new Date().toISOString().slice(0, 10))
const daily = ref(null)
const loadingDaily = ref(false)

const monthVal = ref(new Date().toISOString().slice(0, 7))
const monthly = ref(null)
const loadingMonthly = ref(false)

const range = ref([])
const projRows = ref([])
const loadingProj = ref(false)
const projLoaded = ref(false)

async function loadDaily() {
  loadingDaily.value = true
  try {
    daily.value = await getDailyReport(dailyDate.value)
  } finally {
    loadingDaily.value = false
  }
}

async function loadMonthly() {
  if (!monthVal.value) return
  const [y, m] = monthVal.value.split('-').map(Number)
  loadingMonthly.value = true
  try {
    monthly.value = await getMonthlyReport(y, m)
  } finally {
    loadingMonthly.value = false
  }
}

async function loadProjectProfit() {
  if (!range.value?.length) return
  loadingProj.value = true
  projLoaded.value = true
  try {
    projRows.value = await getProjectProfitReport(range.value[0], range.value[1])
  } finally {
    loadingProj.value = false
  }
}
</script>

<style scoped>
.reports {
  display: flex;
  flex-direction: column;
  gap: 16px;
}
.block {
  margin: 0;
}
.mt {
  margin-top: 16px;
}
.empty {
  color: #909399;
  margin-top: 12px;
}
</style>
