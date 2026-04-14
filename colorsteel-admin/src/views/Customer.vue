<template>
  <el-card>
    <template #header>
      <div class="toolbar">
        <span>客户管理</span>
        <el-button type="primary" @click="openCreate">新增客户</el-button>
      </div>
    </template>
    <el-form :inline="true" @submit.prevent="load">
      <el-form-item label="名称">
        <el-input v-model="query.name" clearable placeholder="模糊查询" style="width: 200px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="load">查询</el-button>
      </el-form-item>
    </el-form>
    <el-table :data="table.records" border v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="customerNo" label="客户编号" width="140" />
      <el-table-column prop="name" label="名称" />
      <el-table-column prop="contactName" label="联系人" width="120" />
      <el-table-column prop="phone" label="手机" width="130" />
      <el-table-column label="操作" width="160" fixed="right">
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

    <el-dialog v-model="dialog.visible" :title="dialog.id ? '编辑客户' : '新增客户'" width="520px" destroy-on-close @closed="resetForm">
      <el-form :model="form" label-width="100px">
        <el-form-item label="客户编号" required>
          <el-input v-model="form.customerNo" :disabled="!!dialog.id" />
        </el-form-item>
        <el-form-item label="名称" required>
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="联系人">
          <el-input v-model="form.contactName" />
        </el-form-item>
        <el-form-item label="手机">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" type="textarea" />
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
import { fetchCustomerPage, getCustomer, createCustomer, updateCustomer, deleteCustomer } from '@/api/customer'

const loading = ref(false)
const query = reactive({ current: 1, size: 10, name: '' })
const table = reactive({ records: [], total: 0 })

const dialog = reactive({ visible: false, id: null, saving: false })
const form = reactive({
  customerNo: '',
  name: '',
  contactName: '',
  phone: '',
  address: '',
  remark: '',
})

async function load() {
  loading.value = true
  try {
    const data = await fetchCustomerPage({ ...query })
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
  const detail = await getCustomer(row.id)
  Object.assign(form, {
    customerNo: detail.customerNo,
    name: detail.name,
    contactName: detail.contactName,
    phone: detail.phone,
    address: detail.address,
    remark: detail.remark,
  })
  dialog.visible = true
}

function resetForm() {
  Object.assign(form, {
    customerNo: '',
    name: '',
    contactName: '',
    phone: '',
    address: '',
    remark: '',
  })
}

async function save() {
  dialog.saving = true
  try {
    if (dialog.id) {
      await updateCustomer(dialog.id, {
        name: form.name,
        contactName: form.contactName,
        phone: form.phone,
        address: form.address,
        remark: form.remark,
      })
    } else {
      await createCustomer({ ...form })
    }
    dialog.visible = false
    await load()
  } finally {
    dialog.saving = false
  }
}

async function onDelete(row) {
  await ElMessageBox.confirm('确认删除该客户？', '提示', { type: 'warning' })
  await deleteCustomer(row.id)
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
