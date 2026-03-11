# Stored Procedure Mapping (SQLite)

## Why this mapping exists
SQLite does not support SQL Server style stored procedures (`CREATE PROCEDURE`).
To keep equivalent business behavior, this project uses service-layer transaction + pure SQL scripts.

## Original procedure intent
- Name: `usp_UpdateEmployeeSeat`
- Behavior:
  1. Clear employee old seat.
  2. Clear target seat current occupant.
  3. Assign employee to target seat.
  4. Commit or rollback as one transaction.

## Implemented mapping
- Transaction boundary:
  - `SeatAssignmentService.assignSeat(...)` with `@Transactional`
- SQL step files:
  - `sql/queries/seats/assign/step1_clear_employee_old_seat.sql`
  - `sql/queries/seats/assign/step2_clear_target_seat_occupant.sql`
  - `sql/queries/seats/assign/step3_assign_employee_to_new_seat.sql`
- Execution order:
  1. `clearEmployeeSeat(empId)`
  2. `clearTargetSeatOccupant(newSeatSeq)`
  3. `assignEmployeeToSeat(empId, newSeatSeq)`

## Benefits
- Keeps pure SQL in repository/resource files.
- Keeps logic auditable and version-controlled.
- Behavior is equivalent to stored procedure transaction flow.
