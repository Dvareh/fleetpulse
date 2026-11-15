# FleetPulse - Fleet & Predictive Maintenance Management System

A full-stack MVP web application for commercial vehicle fleet management, tracking maintenance history, and operational cost analytics with predictive service scheduling.

Developed as a recruitment technical showcase following clean architecture, responsive UI design, and modern engineering practices.

---

## Application Demo
https://github.com/user-attachments/assets/fec9b1d9-bf78-42c9-a623-5aee4ee35f17

  

## Tech Stack

### Backend
- **Language & Runtime:** Java 21
- **Framework:** Spring Boot 3.3+ (Spring Web, Spring Data JPA, Validation)
- **Database:** H2 Database Engine (In-Memory, zero-configuration setup)
- **API Documentation:** OpenAPI 3 / Swagger UI (`springdoc-openapi`)
- **Architecture:** Layered architecture (`Controller` → `Service` → `Repository` → `Entity` / `DTO`) with centralized exception handling (`@RestControllerAdvice`) and CORS configuration.

### Frontend
- **Framework:** Angular 19
- **Reactivity:** Angular Signals
- **Language:** TypeScript 5+
- **Styling:** Custom responsive CSS design system

---

## Predictive Maintenance & TCO Analytics

Beyond standard CRUD operations, FleetPulse includes an intelligent analytics engine (`AnalyticsService`):

1. **Predictive Maintenance Scheduling:**
   - Analyzes historical service logs to calculate average daily mileage rate ($\Delta\text{km} / \Delta\text{days}$).
   - Computes remaining distance to the next scheduled maintenance interval (e.g. oil change, brake service).
   - Forecasts the exact calendar date for the next required workshop visit (`estimatedServiceDate`).

2. **Vehicle Health Scoring (0–100%):**
   - Categorizes fleet health into three operational states:
     - **Good (80–100%):** Plenty of remaining mileage before service.
     - **Warning (40–79%):** Service due within 3,000 km / next few weeks.
     - **Critical Overdue (< 40%):** Mileage interval exceeded; immediate inspection flagged.

3. **Total Cost of Ownership (TCO):**
   - Automatically calculates operational cost per kilometer (`PLN / km`):
     $$\text{Cost/km} = \frac{\sum \text{Maintenance Costs}}{\text{Current Mileage}}$$
   - Aggregates fleet-wide financial analytics (total spend and average cost per vehicle).

---

## Project Structure

```text
fleetpulse/
├── backend/                       # Spring Boot 3 REST API
│   ├── src/main/java/com/fleetpulse/api/
│   │   ├── configs/               # WebMvcConfigurer (CORS)
│   │   ├── controllers/           # REST Controllers (Vehicles, Maintenance, Analytics)
│   │   ├── entities/              # JPA Entities
│   │   ├── enums/                 # Enums
│   │   ├── dto/                   # Request/Response DTOs & Validation
│   │   ├── exceptions/            # GlobalExceptionHandler & Custom Exceptions
│   │   ├── repositories/          # Spring Data JPA Repositories
│   │   └── services/              # Business Logic & Analytics Calculation
│   └── src/main/resources/
│       ├── application.properties # H2 in-memory & OpenAPI configuration
│       └── data.sql               # Initial mock fleet dataset
│
└── frontend/                      # Angular 19 SPA
    └── src/app/
        ├── models/                # TypeScript domain models matching API DTOs
        ├── services/              # FleetApiService (HttpClient)
        ├── app.ts                 # Main dashboard logic & Signals
        ├── app.html               # Reactive template (Control flow @if, @for)
        └── app.css                # Custom theme & responsive styles
```

---

## Getting Started

### Prerequisites
- **Java 21** JDK installed
- **Node.js 18+** & npm installed

### 1. Run Backend
Navigate to the `backend` folder and run via Maven Wrapper:

```bash
cd backend
./mvnw clean spring-boot:run
```

- **Backend API:** `http://localhost:8080`
- **Swagger UI (Interactive API Docs):** `http://localhost:8080/swagger-ui.html`
- **H2 Database Console:** `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:fleetdb`
  - User: `sa`
  - Password: *(empty)*

The database automatically populates with realistic initial vehicles and maintenance logs via `data.sql`.

### 2. Run Frontend
In a new terminal window, navigate to the `frontend` directory:

```bash
cd frontend
npm install
npm start
```

Open `http://localhost:4200` in your browser.

---

## REST API Endpoints Overview

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| `GET` | `/api/vehicles` | List all vehicles (filterable by status) |
| `POST` | `/api/vehicles` | Register a new vehicle with validation |
| `GET` | `/api/vehicles/{id}` | Get vehicle details |
| `PUT` | `/api/vehicles/{id}` | Update vehicle mileage or status |
| `DELETE` | `/api/vehicles/{id}` | Delete vehicle and cascade its logs |
| `GET` | `/api/vehicles/{id}/maintenance` | Get vehicle maintenance history |
| `POST` | `/api/vehicles/{id}/maintenance` | Add new maintenance log (auto-syncs mileage) |
| `DELETE` | `/api/maintenance/{id}` | Delete maintenance record |
| `GET` | `/api/vehicles/{id}/health-check` | Calculate Health Score, forecast & TCO |
| `GET` | `/api/dashboard/stats` | Fleet-wide summary metrics |

---

## Git Workflow

- `main` - Production-ready releases.
- `develop` - Integration branch.
- Feature branches (`task-backend/APP-xxx`, `task-frontend/APP-xxx`, `docs/APP-xxx`) merged via Pull Requests.

---

## AI Usage
- Reviewing repetitive boilerplate code (DTOs, entity mappings, and validation rules).
- Refactoring and optimizing CSS (unifying card styles, layout cleanup, and improving table responsiveness).
- Code review and proofreading technical documentation in the README file.
