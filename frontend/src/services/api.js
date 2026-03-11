const API_BASE = 'http://localhost:8080'

// Toggle to true only when backend is unavailable.
const USE_MOCK_API = false

let mockSeatRows = [
  { FLOOR_SEAT_SEQ: 1, FLOOR_NO: 1, SEAT_NO: 1, EMP_ID: null },
  { FLOOR_SEAT_SEQ: 2, FLOOR_NO: 1, SEAT_NO: 2, EMP_ID: null },
  { FLOOR_SEAT_SEQ: 3, FLOOR_NO: 1, SEAT_NO: 3, EMP_ID: '12006' },
  { FLOOR_SEAT_SEQ: 4, FLOOR_NO: 1, SEAT_NO: 4, EMP_ID: null },
  { FLOOR_SEAT_SEQ: 5, FLOOR_NO: 2, SEAT_NO: 1, EMP_ID: null },
  { FLOOR_SEAT_SEQ: 6, FLOOR_NO: 2, SEAT_NO: 2, EMP_ID: null },
  { FLOOR_SEAT_SEQ: 7, FLOOR_NO: 2, SEAT_NO: 3, EMP_ID: '16142' },
  { FLOOR_SEAT_SEQ: 8, FLOOR_NO: 2, SEAT_NO: 4, EMP_ID: null },
  { FLOOR_SEAT_SEQ: 9, FLOOR_NO: 3, SEAT_NO: 1, EMP_ID: '13040' },
  { FLOOR_SEAT_SEQ: 10, FLOOR_NO: 3, SEAT_NO: 2, EMP_ID: '17081' },
  { FLOOR_SEAT_SEQ: 11, FLOOR_NO: 3, SEAT_NO: 3, EMP_ID: null },
  { FLOOR_SEAT_SEQ: 12, FLOOR_NO: 3, SEAT_NO: 4, EMP_ID: '11221' },
  { FLOOR_SEAT_SEQ: 13, FLOOR_NO: 4, SEAT_NO: 1, EMP_ID: null },
  { FLOOR_SEAT_SEQ: 14, FLOOR_NO: 4, SEAT_NO: 2, EMP_ID: null },
  { FLOOR_SEAT_SEQ: 15, FLOOR_NO: 4, SEAT_NO: 3, EMP_ID: '16722' },
  { FLOOR_SEAT_SEQ: 16, FLOOR_NO: 4, SEAT_NO: 4, EMP_ID: null }
]

const mockEmployees = [
  { EMP_ID: '11221', NAME: 'Alice' },
  { EMP_ID: '12006', NAME: 'Bob' },
  { EMP_ID: '13040', NAME: 'Carol' },
  { EMP_ID: '16142', NAME: 'David' },
  { EMP_ID: '16722', NAME: 'Eric' },
  { EMP_ID: '17081', NAME: 'Fiona' },
  { EMP_ID: '18888', NAME: 'Grace' }
]

async function requestJson(path, options = {}) {
  const response = await fetch(`${API_BASE}${path}`, {
    headers: {
      'Content-Type': 'application/json',
      ...(options.headers || {})
    },
    ...options
  })

  if (!response.ok) {
    throw new Error(`API ${response.status}: ${response.statusText}`)
  }

  if (response.status === 204) {
    return null
  }

  return response.json()
}

export async function fetchSeatRows() {
  if (USE_MOCK_API) {
    return [...mockSeatRows]
  }
  return requestJson('/api/seats')
}

export async function fetchEmployeeList() {
  if (USE_MOCK_API) {
    return [...mockEmployees]
  }
  return requestJson('/api/employees')
}

export async function updateEmployeeSeat(empId, newSeatSeq) {
  if (USE_MOCK_API) {
    mockSeatRows = mockSeatRows.map((row) => {
      if (row.EMP_ID === empId) {
        return { ...row, EMP_ID: null }
      }
      if (row.FLOOR_SEAT_SEQ === newSeatSeq) {
        return { ...row, EMP_ID: null }
      }
      return row
    })

    mockSeatRows = mockSeatRows.map((row) =>
      row.FLOOR_SEAT_SEQ === newSeatSeq ? { ...row, EMP_ID: empId } : row
    )

    return { success: true }
  }

  return requestJson('/api/seats/assign', {
    method: 'POST',
    body: JSON.stringify({ empId, newSeatSeq })
  })
}
