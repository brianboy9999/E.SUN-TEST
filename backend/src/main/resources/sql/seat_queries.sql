-- Seat list for API: GET /api/seats
SELECT
  S.FLOOR_SEAT_SEQ,
  S.FLOOR_NO,
  S.SEAT_NO,
  E.EMP_ID
FROM SeatingChart S
LEFT JOIN Employee E ON S.FLOOR_SEAT_SEQ = E.FLOOR_SEAT_SEQ
ORDER BY S.FLOOR_NO, S.SEAT_NO;

-- Employee list for API: GET /api/employees
SELECT EMP_ID, NAME
FROM Employee
ORDER BY EMP_ID;

-- Assignment transaction steps (execute in one transaction from service layer)
-- 1) Clear old seat of employee
-- UPDATE Employee SET FLOOR_SEAT_SEQ = NULL WHERE EMP_ID = :empId;
-- 2) Clear current occupant of target seat
-- UPDATE Employee SET FLOOR_SEAT_SEQ = NULL WHERE FLOOR_SEAT_SEQ = :newSeatSeq;
-- 3) Assign employee to target seat
-- UPDATE Employee SET FLOOR_SEAT_SEQ = :newSeatSeq WHERE EMP_ID = :empId;
