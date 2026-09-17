# QUICK START — Lab 1 in VS Code

---

## STEP 0 — Prerequisites

**Install these first:**

1. **Java 17+**
   - Download: https://www.oracle.com/java/technologies/downloads/
   - Verify: Open Terminal → `java -version`

2. **Maven 3.9+**
   - Download: https://maven.apache.org/download.cgi
   - Verify: `mvn -version`

3. **Node.js 18+** (for React)
   - Download: https://nodejs.org/
   - Verify: `node -v` and `npm -v`

4. **VS Code Extensions**
   - Open VS Code
   - Go to Extensions (Ctrl+Shift+X)
   - Install:
     - **Extension Pack for Java** (Microsoft)
     - **Spring Boot Extension Pack** (VMware)
     - **REST Client** (Huachao Mao) — optional but handy

5. **Supabase Account**
   - Go to https://supabase.com
   - Create free account
   - Create one project
   - Copy connection details (we'll need them)

---

## STEP 1 — Extract and Open Project

```bash
# Extract ZIP
unzip jabines-lab2.zip
cd jabines-lab2

# Open in VS Code
code .
```

---

## STEP 2 — Set Up Database

### 2.1 Get Supabase Connection Info

1. Login to Supabase
2. Click your project
3. Go to **Settings** → **Database**
4. Find **Connection string (JDBC)**
5. Copy everything

Example:
```
jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres?user=postgres&password=YOUR_PASSWORD
```

Break it into parts:
```
DB_URL=jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres
DB_USERNAME=postgres
DB_PASSWORD=YOUR_ACTUAL_PASSWORD_HERE
```

### 2.2 Run Schema

1. In Supabase Dashboard
2. Go to **SQL Editor**
3. Click **New Query**
4. Open `database/schema.sql` in VS Code
5. Copy entire content
6. Paste into Supabase SQL Editor
7. Click **Run**

### 2.3 Verify

```sql
SELECT * FROM inventory;
```

Should show 3 products.

---

## STEP 3 — Run Backend

### 3.1 In VS Code

1. Open `backend/src/main/java/edu/cit/jabines/Application.java`
2. You should see a **Run** button (▶ icon) above `public static void main`
3. Click **Run**
4. Terminal should show:
   ```
   Started Application in X seconds
   ```

### 3.2 OR Use Command Line

```bash
cd backend

# Set environment variables
export DB_URL="jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres"
export DB_USERNAME="postgres"
export DB_PASSWORD="your_password"

# Run
mvn spring-boot:run
```

### 3.3 Verify Backend is Running

Open browser: `http://localhost:8080/api/inventory`

Should show:
```json
[
  {"productId":"P100","name":"Wireless Mouse","stock":25},
  {"productId":"P200","name":"Mechanical Keyboard","stock":10},
  {"productId":"P300","name":"USB-C Hub","stock":0}
]
```

---

## STEP 4 — Run Frontend

**Open a NEW terminal** (keep backend running)

```bash
cd frontend

# Install dependencies
npm install

# Start dev server
npm run dev
```

You'll see:
```
  VITE v5.0.0  ready in 123 ms
  
  ➜  Local:   http://localhost:5173/
  ➜  press h + enter to show help
```

---

## STEP 5 — Test It

1. Open browser: `http://localhost:5173`
2. You should see:
   ```
   CAMPUS SHOP
   Order & Inventory System
   ```

3. Select product: **Wireless Mouse**
4. Quantity: **2**
5. Click **PLACE ORDER**

Result:
```
✓ ORDER CONFIRMED
```

Inventory should update:
```
Wireless Mouse: 25 → 23
```

---

## STEP 6 — Capture Evidence (Important!)

### Test 1 — Confirmed Order

1. Open **Developer Tools**: F12 or Right-click → Inspect
2. Go to **Network** tab
3. Place order for Wireless Mouse × 2
4. In Network tab, find **orders** request
5. Click it, view response
6. **Screenshot this**

### Test 2 — Rejected Order

1. Select **USB-C Hub** (stock = 0)
2. Quantity: 1
3. Click **PLACE ORDER**
4. Response should show: `"status": "REJECTED"`
5. **Screenshot this**

---

## TROUBLESHOOTING

| Issue | Fix |
|-------|-----|
| "Connection refused" | Is Supabase running? Check DB_URL |
| "Table not found" | Did you run schema.sql? |
| "CORS error" | Backend on port 8080? |
| Port 8080 already in use | `lsof -i :8080` (Mac/Linux) or `netstat -ano \| findstr :8080` (Windows) |
| `npm: command not found` | Install Node.js |
| `mvn: command not found` | Install Maven, restart terminal |

---

## NEXT STEPS

Once all tests pass:
1. Read the full README.md
2. Answer the reflection questions
3. Wait for Lab 2 instructions

✅ **You're ready!**
