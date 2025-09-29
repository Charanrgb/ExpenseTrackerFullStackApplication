Public website coming soon!
# 💰 Money Manager — Full-Stack Application

A full-stack money management web application with a **Spring Boot backend** and a **React + Vite frontend**.
The system provides user authentication, income & expense tracking, dashboards with charts, and export features (Excel, email, Cloudinary for image uploads).

---

## 🚀 Tech Stack

* **Backend (`moneymanager`)**

  * Java 21, Spring Boot 3.5.3
  * Spring Data JPA, Spring Security, JWT
  * Apache POI (Excel export), JavaMail (email)
  * MySQL / PostgreSQL
* **Frontend (`moneymanagerwebapp`)**

  * React 18, Vite
  * TailwindCSS, axios, Recharts
  * Cloudinary for profile image uploads

---

## 📂 Project Structure

```
moneymanager/         # Backend (Spring Boot)
 ├── pom.xml          # Maven project file
 ├── src/main/java/   # Controllers, services, repositories, DTOs, entities
 └── src/main/resources/application.properties  # Config (DB, mail, JWT)

moneymanagerwebapp/   # Frontend (React + Vite)
 ├── package.json     # Dependencies & scripts
 ├── src/             # Components, pages, context, hooks, util
 └── vite.config.js   # Vite configuration
```

---

## ⚙️ How to Run Locally

### 1. Backend (Spring Boot)

**Requirements**: Java 21, Maven (or wrapper), MySQL/PostgreSQL.

```bash
cd moneymanager
./mvnw clean package     # Windows: .\mvnw.cmd clean package
./mvnw spring-boot:run   # Windows: .\mvnw.cmd spring-boot:run
```

**Configuration**: edit `src/main/resources/application.properties`

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/moneymanager
spring.datasource.username=root
spring.datasource.password=secret
spring.jpa.hibernate.ddl-auto=update

spring.mail.host=smtp.example.com
spring.mail.port=587
spring.mail.username=you@example.com
spring.mail.password=mailpassword

jwt.secret=your_jwt_secret_here
```

**API base URL** (used by frontend):

```
http://localhost:8080/api/v1.0
```

---

### 2. Frontend (React + Vite)

**Requirements**: Node.js >= 18, npm.

```bash
cd moneymanagerwebapp
npm install
npm run dev
```

Frontend runs at:

```
http://localhost:5173
```

It calls the backend API at `http://localhost:8080/api/v1.0` (see `src/util/apiEndpoints.js`).

---

## 🏗️ Production Build

**Backend**:

```bash
cd moneymanager
./mvnw clean package
java -jar target/moneymanager-0.0.1-SNAPSHOT.jar
```

**Frontend**:

```bash
cd moneymanagerwebapp
npm run build
# deploy dist/ folder with a static server or integrate with backend
```

---

## 📊 Features

* 🔐 User Authentication (register/login with JWT)
* 📂 Category management (CRUD)
* 💵 Income & Expense management (CRUD)
* 📈 Dashboard with charts (Recharts)
* 📤 Excel export for incomes/expenses (Apache POI)
* 📧 Email sending (JavaMail)
* 🖼️ Profile image upload (Cloudinary)
* 🔎 Filters and reporting

---

## 🛠️ Troubleshooting

* **CORS errors** → ensure backend allows requests from `http://localhost:5173` in `SecurityConfig.java`.
* **DB errors** → verify database exists and credentials in `application.properties`.
* **Port conflicts** → backend defaults to 8080, frontend defaults to 5173. Update configs if needed.
* **Auth issues** → check `jwt.secret` value and token validity.

---

## 📌 Notes

* Update `BASE_URL` in `moneymanagerwebapp/src/util/apiEndpoints.js` if backend runs on a different port/host.
* Consider using Vite `.env` variables for API base URLs:

  ```env
  VITE_API_BASE=http://localhost:8080/api/v1.0
  VITE_CLOUDINARY_CLOUD_NAME=your_cloud_name
  ```

---
