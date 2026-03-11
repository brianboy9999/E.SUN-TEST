# Test Cases - Seat Assignment System

## Scope
- Seat display
- Employee selection
- Seat assignment transaction
- API error handling
- Persistence verification

## Test Data Notes
- Seat seq uses `FLOOR_SEAT_SEQ`
- Employee ID is fixed length 5

## Functional Cases
1. Load seat map successfully
- Steps: Open frontend page.
- Expected: 4x4 seats render; occupied seats show employee id.

2. Load employee list successfully
- Steps: Open employee dropdown.
- Expected: employee options shown in ascending EMP_ID order.

3. Assign empty seat to employee
- Steps: Select employee `18888`; click empty seat; press update.
- Expected: success message; seat now shows `[員編:18888]`.

4. Reassign employee with old seat
- Steps: Select employee `12006`; click another seat; press update.
- Expected: old seat becomes empty; new seat shows `12006`.

5. Assign to occupied seat (swap-clear rule)
- Steps: Select employee `18888`; choose seat occupied by `16142`; press update.
- Expected: target seat becomes `18888`; `16142` seat becomes empty.

6. Prevent submit with no employee selected
- Steps: Keep employee unselected; click seat; try update.
- Expected: update button disabled or validation error.

7. Prevent submit with no seat selected
- Steps: Select employee only; do not click seat; try update.
- Expected: update button disabled or validation error.

8. Invalid empId format
- Steps: Call API with empId not length 5.
- Expected: HTTP 400 with validation message.

9. Unknown employee ID
- Steps: Call API with non-existing empId `99999`.
- Expected: HTTP 400 and message `Employee not found`.

10. Persistence after refresh
- Steps: Complete one assignment; refresh page.
- Expected: assigned result remains unchanged.

11. SQL injection safety (API request)
- Steps: send empId like `12006' OR '1'='1`.
- Expected: validation rejects input; no mass update.

12. Concurrent update simulation (manual)
- Steps: Send two assignment requests quickly for same empId.
- Expected: final state consistent (one seat only).

## API Checks (quick)
- `GET /api/seats` returns keys: `FLOOR_SEAT_SEQ`, `FLOOR_NO`, `SEAT_NO`, `EMP_ID`
- `GET /api/employees` returns keys: `EMP_ID`, `NAME`
- `POST /api/seats/assign` returns `{ success, message }`
