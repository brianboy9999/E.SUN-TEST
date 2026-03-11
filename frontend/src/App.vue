<script setup>
import { ref, computed, onMounted } from 'vue'
import { fetchSeatRows, fetchEmployeeList, updateEmployeeSeat } from './services/api'

const title = '員工座位安排系統'

const seatRows = ref([])
const employees = ref([])
const selectedEmpId = ref('')
const selectedSeatSeq = ref(null)

const loading = ref(false)
const saving = ref(false)
const errorMessage = ref('')
const successMessage = ref('')

const seatGrid = computed(() => {
  const grouped = {}

  for (const row of seatRows.value) {
    const floorNo = Number(row.FLOOR_NO)
    if (!grouped[floorNo]) {
      grouped[floorNo] = []
    }
    grouped[floorNo].push(row)
  }

  return Object.entries(grouped)
    .sort((a, b) => Number(a[0]) - Number(b[0]))
    .map(([floor, rows]) => ({
      floorNo: Number(floor),
      seats: rows.sort((a, b) => Number(a.SEAT_NO) - Number(b.SEAT_NO))
    }))
})

const canAssign = computed(() => !!selectedEmpId.value && selectedSeatSeq.value !== null && !saving.value)

function isSelectedSeat(row) {
  return selectedSeatSeq.value === Number(row.FLOOR_SEAT_SEQ)
}

function seatLabel(row) {
  return `${row.FLOOR_NO}樓: 座位${row.SEAT_NO}`
}

function employeeLabel(emp) {
  return `${emp.EMP_ID} - ${emp.NAME}`
}

function selectSeat(row) {
  selectedSeatSeq.value = Number(row.FLOOR_SEAT_SEQ)
}

async function loadData() {
  loading.value = true
  errorMessage.value = ''

  try {
    const [seatsResult, employeesResult] = await Promise.all([
      fetchSeatRows(),
      fetchEmployeeList()
    ])
    seatRows.value = seatsResult
    employees.value = employeesResult
  } catch (error) {
    errorMessage.value = `載入失敗：${error.message}`
  } finally {
    loading.value = false
  }
}

async function assignSeat() {
  if (!canAssign.value) return

  saving.value = true
  errorMessage.value = ''
  successMessage.value = ''

  try {
    await updateEmployeeSeat(selectedEmpId.value, selectedSeatSeq.value)
    successMessage.value = '座位更新成功'
    await loadData()
  } catch (error) {
    errorMessage.value = `更新失敗：${error.message}`
  } finally {
    saving.value = false
  }
}

onMounted(loadData)
</script>

<template>
  <main class="wrap">
    <header class="page-head">
      <h1>{{ title }}</h1>
      <p>依 SQL 欄位設計顯示：FLOOR_SEAT_SEQ / FLOOR_NO / SEAT_NO / EMP_ID</p>
    </header>

    <section class="toolbar">
      <label>
        請選擇員工
        <select v-model="selectedEmpId">
          <option value="">-- 請選擇 --</option>
          <option v-for="emp in employees" :key="emp.EMP_ID" :value="emp.EMP_ID">
            {{ employeeLabel(emp) }}
          </option>
        </select>
      </label>

      <button type="button" :disabled="!canAssign" @click="assignSeat">
        {{ saving ? '更新中...' : '更新座位' }}
      </button>
    </section>

    <p v-if="loading">資料載入中...</p>
    <p v-if="errorMessage" class="msg error">{{ errorMessage }}</p>
    <p v-if="successMessage" class="msg success">{{ successMessage }}</p>

    <section v-for="floor in seatGrid" :key="floor.floorNo" class="floor-block">
      <h2>{{ floor.floorNo }}樓</h2>
      <div class="seat-grid">
        <button
          v-for="row in floor.seats"
          :key="row.FLOOR_SEAT_SEQ"
          type="button"
          class="seat-card"
          :class="{
            occupied: !!row.EMP_ID,
            selected: isSelectedSeat(row)
          }"
          @click="selectSeat(row)"
        >
          <span>{{ seatLabel(row) }}</span>
          <strong v-if="row.EMP_ID">[員編:{{ row.EMP_ID }}]</strong>
          <small>#{{ row.FLOOR_SEAT_SEQ }}</small>
        </button>
      </div>
    </section>

    <section class="legend">
      <span class="dot empty"></span>空位
      <span class="dot occupied"></span>已佔用
      <span class="dot selected"></span>目前選擇
    </section>
  </main>
</template>
