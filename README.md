# LAB 1 & LAB 2 — Campus Shop: Order & Inventory System

**Student:** Snyd Jabines  
**Course:** CIT-U IT Project Management / Capstone  
**Repository:** jabines-lab2

---

## 1. Project Overview

A modular monolith Spring Boot application demonstrating in-process communication between **Order** and **Inventory** modules using a shared Supabase PostgreSQL database.

The system allows users to:
- Place orders through a React frontend
- Check inventory in real-time
- View order status (CONFIRMED/REJECTED)
- Cancel orders and restock inventory (Lab 2)

---

## 2. Technologies

| Layer | Technology |
|-------|-----------|
| **Backend** | Java 17, Spring Boot 3.2.0 |
| **Build** | Maven 3.9+ |
| **Database** | Supabase PostgreSQL |
| **Frontend** | React 18.2, Vite 5.0 |
| **API** | REST |
| **Architecture** | Modular Monolith |

---

## 3. Architecture

```
┌─────────────────────────────────────────┐
│         React Frontend (Port 5173)      │
│      - Order Form                       │
│      - Inventory Display                │
│      - Order History (Lab 2)            │
└────────────────┬────────────────────────┘
                 │ HTTP/REST
                 │
┌─────────────────┴────────────────────────┐
│    Spring Boot Backend (Port 8080)       │
│  edu.cit.jabines                         │
│  ├── shop (Order Module)                 │
│  │   ├── OrderController                 │
│  │   ├── OrderService                    │
│  │   ├── Order (Entity)                  │
│  │   └── OrderRepository                 │
│  │                                       │
│  ├── inventory (Inventory Module)        │
│  │   ├── InventoryService (public)       │
│  │   ├── InventoryServiceImpl (package)   │
│  │   ├── Inventory (Entity)              │
│  │   └── InventoryRepository             │
│  │                                       │
│  └── notification (Lab 2 only)           │
│      └── Domain Events                   │
└─────────────────┬────────────────────────┘
                  │ JPA/SQL
                  │
        ┌─────────┴─────────┐
        │  Supabase         │
        │  PostgreSQL       │
        │                   │
        │ - inventory       │
        │ - orders          │
        │ - order_items     │
        │ - notifications   │
        └───────────────────┘
```

---

## 4. Project Structure

```
jabines-lab2/
├── backend/
│   ├── pom.xml
│   ├── .env.example
│   └── src/main/
│       ├── java/edu/cit/jabines/
│       │   ├── Application.java
│       │   ├── shop/
│       │   │   ├── controller/
│       │   │   │   └── OrderController.java
│       │   │   ├── service/
│       │   │   │   └── OrderService.java
│       │   │   ├── model/
│       │   │   │   └── Order.java
│       │   │   └── repository/
│       │   │       └── OrderRepository.java
│       │   └── inventory/
│       │       ├── service/
│       │       │   ├── InventoryService.java (public interface)
│       │       │   └── InventoryServiceImpl.java (package-private)
│       │       ├── model/
│       │       │   └── Inventory.java
│       │       └── repository/
│       │           └── InventoryRepository.java
│       └── resources/
│           └── application.properties
├── frontend/
│   ├── package.json
│   ├── vite.config.js
│   ├── index.html
│   └── src/
│       ├── main.jsx
│       ├── App.jsx
│       ├── App.css
│       └── index.css
├── database/
│   └── schema.sql
├── .gitignore
└── README.md
```

---

## 5. Supabase Setup

### 5.1 Create Supabase Project

1. Go to [supabase.com](https://supabase.com)
2. Click "New Project"
3. Choose organization and project name
4. Set database password (save it!)
5. Wait for project creation (3-5 minutes)

### 5.2 Get Connection Details

1. Go to **Settings** → **Database** → **Connection String**
2. Copy the **Connection string (JDBC)**
3. Format:
   ```
   jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres?user=postgres&password=YOUR_PASSWORD
   ```

---

## 6. Environment Variables

### 6.1 Backend Setup

Create `backend/.env`:

```bash
DB_URL=jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres
DB_USERNAME=postgres
DB_PASSWORD=your_actual_password_here
```

### 6.2 How Spring Reads .env

Spring doesn't read `.env` by default. Use one of:

**Option A: IDE Launch Configuration (Easiest for dev)**
1. Run → Edit Configurations
2. Set Environment variables
3. Paste the three variables

**Option B: Command Line**
```bash
export DB_URL="jdbc:postgresql://..."
export DB_USERNAME="postgres"
export DB_PASSWORD="your_password"
mvn spring-boot:run
```

**Option C: application.properties (NOT recommended for secrets)**
Use placeholders:
```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

---

## 7. SQL Setup

### 7.1 Create Tables

1. In Supabase Dashboard:
   - Go to **SQL Editor**
   - Click **New Query**
   - Copy entire `database/schema.sql`
   - Click **Run**
   - Verify tables created

Or from CLI:

```bash
psql -h aws-0-ap-southeast-1.pooler.supabase.com \
     -U postgres \
     -d postgres \
     -f database/schema.sql
```

### 7.2 Verify

```sql
SELECT * FROM inventory;
```

Expected output:
```
 product_id |      name      | stock
────────────┼────────────────┼───────
 P100       | Wireless Mouse |    25
 P200       | Mechanical ... |    10
 P300       | USB-C Hub      |     0
```

---

## 8. Backend Setup

### 8.1 Prerequisites

- **Java 17+**
  ```bash
  java -version
  ```
- **Maven 3.9+**
  ```bash
  mvn -version
  ```

### 8.2 Install Dependencies

```bash
cd backend
mvn clean install
```

### 8.3 Run Spring Boot

**In VS Code:**

1. Install extension: **Spring Boot Extension Pack** (VMware)
2. Open `Application.java`
3. Click **Run** (▶ icon in editor)
4. Or: F5

**Command Line:**

```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--DB_URL=jdbc:postgresql://... --DB_USERNAME=postgres --DB_PASSWORD=xxx"
```

### 8.4 Verify Backend

Visit: `http://localhost:8080/api/inventory`

Expected:
```json
[
  {"productId": "P100", "name": "Wireless Mouse", "stock": 25},
  {"productId": "P200", "name": "Mechanical Keyboard", "stock": 10},
  {"productId": "P300", "name": "USB-C Hub", "stock": 0}
]
```

---

## 9. Frontend Setup

### 9.1 Install Dependencies

```bash
cd frontend
npm install
```

### 9.2 Run Development Server

```bash
npm run dev
```

Visit: `http://localhost:5173`

### 9.3 Build for Production

```bash
npm run build
```

Output: `frontend/dist/`

---

## 10. API Endpoints

### POST /api/orders

**Request:**
```json
{
  "productId": "P100",
  "quantity": 2
}
```

**Response (CONFIRMED):**
```json
{
  "status": "CONFIRMED",
  "reason": "Order confirmed",
  "inventory": [
    {"productId": "P100", "name": "Wireless Mouse", "stock": 23},
    {"productId": "P200", "name": "Mechanical Keyboard", "stock": 10},
    {"productId": "P300", "name": "USB-C Hub", "stock": 0}
  ]
}
```

**Response (REJECTED):**
```json
{
  "status": "REJECTED",
  "reason": "Insufficient stock",
  "inventory": [...]
}
```

---

## 11. Testing

### TEST 1 — Confirmed Order

1. In browser, open **DevTools** → **Network**
2. Select product: **Wireless Mouse**
3. Quantity: **2**
4. Click **PLACE ORDER**
5. Check Network tab:
   - Request: `POST /api/orders`
   - Response status: **200**
   - Response body shows `"status": "CONFIRMED"`
6. Inventory updates: `25 → 23`

**Screenshot:** Capture the Network tab showing the request/response

### TEST 2 — Rejected Order

1. Keep Network tab open
2. Select product: **USB-C Hub** (stock = 0)
3. Quantity: **1**
4. Click **PLACE ORDER**
5. Check Network tab:
   - Response status: **200**
   - Response body shows `"status": "REJECTED"`
   - Reason: "Insufficient stock"
6. Inventory remains **0**

**Screenshot:** Capture the Network tab evidence

---

## 12. Reflection: Lab 1 Concepts

### Question 1: In-Process vs. Microservices

**In-Process (What we built):**
- Order and Inventory modules run in the **same JVM process**
- Communication via **direct method calls** (OrderService → InventoryService)
- Shared database connection
- Synchronous, atomic transactions
- Simple deployment (one JAR)

**Microservices (What we avoided):**
- Each module runs in **separate processes/containers**
- Communication via **HTTP/REST API calls**
- Separate databases per service
- Network latency, potential failures
- Requires orchestration (Docker, Kubernetes)

**What's Free In-Process:**
- Atomic transactions (all-or-nothing)
- Direct error handling
- No network overhead
- Easy debugging

**What Requires Additional Work in Microservices:**
- Distributed transactions (2-phase commit, sagas)
- Error handling across network
- Eventual consistency
- Circuit breakers, retries

---

### Question 2: Package-Private Visibility on InventoryServiceImpl

**Why It Matters:**

```java
// ✓ CORRECT: Shop module depends on interface
OrderService {
    private final InventoryService inventoryService; // interface
}

// ✗ WRONG: If InventoryServiceImpl was public
OrderService {
    private final InventoryServiceImpl impl; // direct dependency!
}
```

**If InventoryServiceImpl Becomes Public:**
- Other modules might depend directly on the implementation
- Harder to swap implementations (e.g., mock, different strategy)
- Breaks encapsulation (internal details exposed)
- Violates Dependency Inversion Principle

**Consequence:**
```
Shop Module
    ↓
InventoryServiceImpl (implementation detail)
    ↓
Inventory Repository

If we replace InventoryServiceImpl with a new implementation:
- Compilation breaks
- Tight coupling
```

---

### Question 3: When to Extract Inventory to Microservice?

**Consider Extraction When:**

1. **Independent Scaling**
   - Inventory read traffic >> Order traffic
   - Need separate load balancing

2. **Data Ownership**
   - Inventory team owns the data exclusively
   - Different update patterns

3. **Deployment Independence**
   - Inventory updates don't affect Order deployment
   - Different release cycles

4. **Technical Boundaries**
   - Inventory uses different database (e.g., cache layer)
   - Order uses PostgreSQL, Inventory uses Redis

**Code Changes Needed:**

```java
// Current (in-process)
OrderService {
    @Autowired
    private InventoryService inventoryService;
    
    public Order placeOrder(...) {
        boolean reserved = inventoryService.reserve(...);
    }
}

// As Microservice
OrderService {
    public Order placeOrder(...) {
        // REST call instead
        HttpClient.post("http://inventory-service/api/reserve", ...)
        
        // Handle network failures, timeouts, retries
    }
}
```

---

## 13. Troubleshooting

| Error | Solution |
|-------|----------|
| `Connection refused` | Check Supabase is running, DB_URL is correct |
| `Table not found` | Run schema.sql in Supabase SQL Editor |
| `CORS error` | Backend is running on 8080? Check OrderController @CrossOrigin |
| `npm not found` | Install Node.js from nodejs.org |
| `mvn not found` | Install Maven from maven.apache.org |
| `Port 8080 in use` | `lsof -i :8080` (Mac/Linux) or `netstat -ano \| findstr :8080` (Windows) |

---

## 14. Checkpoint: Lab 1 Completion

Before proceeding to Lab 2, verify:

- [ ] Spring Boot starts without errors
- [ ] Supabase connection works
- [ ] SQL creates all tables
- [ ] Seed products appear in `/api/inventory`
- [ ] InventoryService is used (not InventoryServiceImpl)
- [ ] InventoryServiceImpl is package-private
- [ ] CONFIRMED order updates inventory
- [ ] REJECTED order doesn't update inventory
- [ ] React connects to backend
- [ ] CORS works (no 403 errors)
- [ ] Network tab shows POST /api/orders
- [ ] Inventory table displays correctly

When all pass: **LAB 1 COMPLETE** → Proceed to Lab 2

---

## 15. Lab 2 Overview (Coming Soon)

Lab 2 extends Lab 1 with:
- Multi-item orders (atomic validation)
- Order cancellation with restock
- Domain events (OrderPlaced, OrderRejected, LowStock)
- Notification module (event-driven)
- Enhanced React UI (cart, order history, activity feed)

---

## 16. Quick Start Commands

```bash
# Backend
cd backend
export DB_URL="jdbc:postgresql://..."
export DB_USERNAME="postgres"
export DB_PASSWORD="your_password"
mvn spring-boot:run

# Frontend (in new terminal)
cd frontend
npm install
npm run dev

# Setup Database (in Supabase SQL Editor)
# Copy/paste entire database/schema.sql and run

# Build for Production
cd frontend && npm run build
cd backend && mvn clean package
```

---

## 17. Resources

- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [Supabase Docs](https://supabase.com/docs)
- [React Docs](https://react.dev)
- [Vite Docs](https://vitejs.dev)

---

**Created:** September 2026  
**Lab 1 Status:** Ready to Test  
**Lab 2 Status:** Pending Lab 1 Completion

