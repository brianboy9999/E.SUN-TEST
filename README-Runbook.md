# 員工座位系統操作手冊

## 1. 專案概要
- 前端：Vue 3 + Vite
- 後端：Spring Boot + JDBC
- 資料庫：SQLite
- SQL 策略：Repository 層使用純 SQL，檔案放在 `backend/src/main/resources/sql`

## 2. 環境需求
- JDK 17
- Maven 3.9+
- Node.js 20 LTS
- DB Browser for SQLite
- （可選）sqlite3 CLI

## 3. 安裝檢查
```powershell
java -version
mvn -version
node -v
npm -v
sqlite3 --version
```

## 4. 啟動後端
```powershell
Set-Location d:\Java\backend
mvn spring-boot:run
```

預期結果：
- 後端監聽 `http://localhost:8080`
- SQLite 檔案建立在 `backend/data/app.db`
- 資料表與初始資料會由 `schema.sql`、`seed.sql` 自動初始化

## 5. 啟動前端
```powershell
Set-Location d:\Java\frontend
npm install
npm run dev
```

開啟：
- `http://localhost:5173`

## 6. 切換為真實後端 API
編輯 `frontend/src/services/api.js`：
- `USE_MOCK_API = false`

說明：
- 目前預設已經是 `USE_MOCK_API = false`
- 只有在後端無法啟動時，才改成 `true`

## 7. API 端點
- `GET /api/health`
- `GET /api/seats`
- `GET /api/employees`
- `POST /api/seats/assign`

健康檢查預期回應：
```json
{
  "success": true,
  "message": "Service is running",
  "db": "UP"
}
```

POST 請求內容範例：
```json
{
  "empId": "12006",
  "newSeatSeq": 7
}
```

## 8. 驗證資料庫內容
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

## 9. 預存程序需求說明
SQLite 不支援 SQL Server 風格的 `CREATE PROCEDURE`。
本專案改用「Service 交易 + 3 段 SQL」達到等效流程：
1. 清除員工舊座位
2. 清除目標座位目前佔用者
3. 將員工指定到新座位

參考文件：
- `backend/docs/stored-procedure-mapping.md`

## 10. 常見問題排查
- `E407 authenticationrequired`：npm/maven 安裝時可能遇到代理驗證問題
- `mvn not found`：Maven PATH 未生效，請重開終端機
- `vite not found`：代表 `npm install` 尚未完成
- CORS 被擋：請確認後端允許 `http://localhost:5173`
