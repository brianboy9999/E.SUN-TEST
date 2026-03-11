# Vue + Spring Boot + SQLite 學習計畫

這份筆記用來追蹤我們接下來要一步一步完成的內容。

## 目標
建立一套員工座位分配系統，技術如下：
- 前端：Vue（Vite）
- 後端：Spring Boot（稍後進行）
- 資料庫：SQLite
- 資料存取：在 repository 層使用純 SQL

## 階段 1 - 先做前端
1. 先理解目前前端腳手架檔案。
2. 建立座位格狀 UI（先做 4 樓 x 每樓 4 個座位示範）。
3. 加入座位狀態與顏色：
   - 空位
   - 已佔用
   - 已選擇
4. 實作點擊選位邏輯（同一時間只能選一個座位）。
5. 加上員工輸入欄位與指派按鈕。
6. 在 UI 加入基本驗證訊息。

## 階段 2 - 前端結構優化
1. 將 UI 拆成元件：
   - `SeatGrid.vue`
   - `SeatCard.vue`
   - `LegendPanel.vue`
2. 建立本地端 mock 資料模組。
3. 建立 API 服務檔 `src/services/api.js`（先放預留方法）。

## 階段 3 - 後端骨架（稍後）
1. 在 `backend` 初始化 Spring Boot 專案。
2. 加入 SQLite JDBC 相依套件。
3. 補上 `application.yml` 資料來源設定。
4. 將 SQL 檔放在 `src/main/resources/sql`。
5. 建立 repository 類別，透過 JDBC template 執行 SQL。
6. 建立座位指派 API 的 service 與 controller。

## 階段 4 - 前後端整合
1. 將前端 mock 呼叫替換成真實 API 呼叫。
2. 處理 loading、成功與錯誤狀態。
3. 驗證指派規則：
   - 一位員工只能有一個座位
   - 已佔用座位不可被選擇
   - 換位或重複指派流程要明確定義

## 階段 5 - 驗證與安全
1. 後端輸入驗證。
2. 防 SQL Injection（使用 prepared statements）。
3. 前端基本 XSS 安全輸出。

## 每日學習流程
1. 從這份文件挑一個小任務。
2. 一起實作。
3. 執行並驗證結果。
4. 在「進度紀錄」補一段簡短心得。

## 回家前環境準備（第一次）
1. 安裝 JDK 17（Spring Boot 3 建議）。
2. 安裝 Maven 3.9+。
3. 安裝 Node.js 20 LTS（含 npm）。
4. 安裝 DB Browser for SQLite（圖形化看資料庫）。
5. 可選：安裝 SQLite CLI（方便直接跑 SQL 指令）。

### 回家要下載的東西（Checklist）
1. JDK 17
2. Maven
3. Node.js LTS
4. DB Browser for SQLite
5. （可選）sqlite3 CLI

### Windows 安裝指令（可用 winget）
```powershell
winget install --id EclipseAdoptium.Temurin.17.JDK -e
winget install --id Apache.Maven -e
winget install --id OpenJS.NodeJS.LTS -e
winget install --id SQLiteBrowser.SQLiteBrowser -e
winget install --id SQLite.SQLite -e
```

## 安裝完成檢查
在新開的終端機執行：
```powershell
java -version
mvn -version
node -v
npm -v
sqlite3 --version
```

## 啟動順序（回家後）
1. 啟動後端（先啟動，會建立 SQLite DB 並載入 schema/seed）。
```powershell
Set-Location d:\Brian\Java\backend
mvn spring-boot:run
```
2. 啟動前端（另一個終端機）。
```powershell
Set-Location d:\Brian\Java\frontend
npm install
npm run dev
```
3. 前端切真 API：
    - 編輯 `frontend/src/services/api.js`
   - 確認 `USE_MOCK_API = false`（目前預設已是 false）

### 回家要做的事情（Checklist）
1. 跑版本檢查指令（java/mvn/node/npm/sqlite3）。
2. 啟動後端 `mvn spring-boot:run`。
3. 呼叫 `GET http://localhost:8080/api/health` 檢查服務和 DB 都是 UP。
4. 啟動前端 `npm install`、`npm run dev`。
5. 開 `http://localhost:5173` 做 UI 操作。

## 第一次測試清單
1. 打開 `http://localhost:5173`。
2. 畫面能看到 4 樓 x 4 座位。
3. 下拉可以選員工。
4. 點任一座位後，按「更新座位」成功。
5. 重新整理頁面，座位結果仍保留（表示已寫入 DB）。

### 回家要測試的重點（Checklist）
1. `GET /api/health` 回傳 `success=true` 且 `db=UP`。
2. `GET /api/seats` 有 16 筆座位資料。
3. `GET /api/employees` 有員工清單。
4. `POST /api/seats/assign` 成功後，舊座位清空、新座位更新。
5. 刷新頁面後資料仍存在（交易已寫入 SQLite）。

## DB 驗證（DB Browser 或 sqlite3）
資料庫檔案預期位置：`backend/data/app.db`

可用查詢：
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

## 常見問題排查
1. `mvn` 找不到：Maven 沒安裝或環境變數未生效，重開終端機再試。
2. `java` 版本不是 17：切換到 JDK 17。
3. 前端無資料：確認後端 `8080` 有啟動，且 `USE_MOCK_API = false`。
4. CORS 錯誤：確認後端 `@CrossOrigin` 包含 `http://localhost:5173`。
5. DB 沒建立：確認 `backend/src/main/resources/application.yml` 有 `sql.init.mode=always`。

## 已建立的操作文件
1. 啟動與交付手冊：`README-Runbook.md`
2. 測試案例清單：`backend/docs/test-cases.md`
3. Stored Procedure 對應說明：`backend/docs/stored-procedure-mapping.md`

## 你下一次開工建議順序
1. 先照 `README-Runbook.md` 安裝與啟動。
2. 跑 `backend/docs/test-cases.md` 的前 1~5 項核心情境。
3. 前端切到真 API（`USE_MOCK_API = false`）。
4. 驗證 DB 資料是否符合畫面結果。

## 進度紀錄
- 2026-03-11：已手動建立前端腳手架（Vite 等效）與後端骨架資料夾。
- 2026-03-11：已建立前後端 API 契約文件（`frontend/docs/api-contract.md`）與 SQLite SQL 檔（`backend/src/main/resources/sql`）。
- 2026-03-11：已完成 Spring Boot 後端骨架（Controller/Service/Repository）、三支 API 與交易式座位更新流程。
- 2026-03-11：已補上回家安裝與啟動測試手冊（JDK/Maven/Node/SQLite）。
- 2026-03-11：已建立交付 Runbook（`README-Runbook.md`）與測試案例表（`backend/docs/test-cases.md`）。
- 2026-03-11：已新增健康檢查 API（`GET /api/health`）並補齊回家下載/執行/測試清單。
- 下一步：建立座位格 UI 與互動式選位功能。
