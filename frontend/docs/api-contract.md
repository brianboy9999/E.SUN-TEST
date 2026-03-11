# API Contract (Seat Assignment)

## Base URL
- `http://localhost:8080`

## 1) Get Seat List
- Method: `GET`
- Path: `/api/seats`
- Description: Return all seats with assigned employee (if any).

Response `200`:
```json
[
  {
    "FLOOR_SEAT_SEQ": 1,
    "FLOOR_NO": 1,
    "SEAT_NO": 1,
    "EMP_ID": null
  },
  {
    "FLOOR_SEAT_SEQ": 3,
    "FLOOR_NO": 1,
    "SEAT_NO": 3,
    "EMP_ID": "12006"
  }
]
```

## 2) Get Employee List
- Method: `GET`
- Path: `/api/employees`
- Description: Return employee options for assignment.

Response `200`:
```json
[
  {
    "EMP_ID": "12006",
    "NAME": "Bob"
  },
  {
    "EMP_ID": "16142",
    "NAME": "David"
  }
]
```

## 3) Assign/Update Seat
- Method: `POST`
- Path: `/api/seats/assign`
- Description: Clear old seat of employee, clear occupant of target seat, then assign employee to target seat.

Request body:
```json
{
  "empId": "12006",
  "newSeatSeq": 7
}
```

Response `200`:
```json
{
  "success": true,
  "message": "Seat updated"
}
```

Error response `400/500`:
```json
{
  "success": false,
  "message": "Reason here"
}
```

## Business Rules
- One employee can only own one seat.
- A target seat can only belong to one employee.
- Assignment should run in one transaction.
