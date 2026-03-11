# Seat Assignment Runbook

## 1. Project Overview
- Frontend: Vue 3 + Vite
- Backend: Spring Boot + JDBC
- Database: SQLite
- SQL strategy: Pure SQL files under `backend/src/main/resources/sql`

## 2. Prerequisites
- JDK 17
- Maven 3.9+
- Node.js 20 LTS
- DB Browser for SQLite
- (Optional) sqlite3 CLI

## 3. Install Check
```powershell
java -version
mvn -version
node -v
npm -v
sqlite3 --version
```

## 4. Start Backend
```powershell
Set-Location d:\Brian\Java\backend
mvn spring-boot:run
```

Expected:
- Backend listens on `http://localhost:8080`
- SQLite file created at `backend/data/app.db`
- Tables/data initialized from `schema.sql` and `seed.sql`

## 5. Start Frontend
```powershell
Set-Location d:\Brian\Java\frontend
npm install
npm run dev
```

Open:
- `http://localhost:5173`

## 6. Switch to Real Backend API
Edit `frontend/src/services/api.js`:
- `USE_MOCK_API = false`

Note:
- Current project default is already `USE_MOCK_API = false`.
- Set it to `true` only when backend is unavailable.

## 7. API Endpoints
- `GET /api/health`
- `GET /api/seats`
- `GET /api/employees`
- `POST /api/seats/assign`

Health check expected response:
```json
{
  "success": true,
  "message": "Service is running",
  "db": "UP"
}
```

POST body example:
```json
{
  "empId": "12006",
  "newSeatSeq": 7
}
```

## 8. Verify Database Data
```sql
SELECT
  S.FLOOR_SEAT_SEQ,
  S.FLOOR_NO,
  S.SEAT_NO,
  E.EMP_ID
FROM SeatingChart S
LEFT JOIN Employee E ON S.FLOOR_SEAT_SEQ = E.FLOOR_SEAT_SEQ
ORDER BY S.FLOOR_NO, S.SEAT_NO;
```

## 9. Stored Procedure Requirement Note
SQLite does not support SQL Server style `CREATE PROCEDURE`.
Equivalent behavior is implemented with transaction + 3 SQL steps in service layer:
1. Clear employee old seat.
2. Clear target seat occupant.
3. Assign employee to target seat.

Reference:
- `backend/docs/stored-procedure-mapping.md`

## 10. Common Troubleshooting
- `E407 authenticationrequired`: proxy auth issue during npm/maven install.
- `mvn not found`: Maven PATH not loaded, reopen terminal.
- `vite not found`: `npm install` did not complete.
- CORS blocked: ensure backend allows `http://localhost:5173`.
