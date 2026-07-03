# Full-Stack ToDo Application (Android & Spring Boot) 📱⚙️

A complete client-server solution for managing tasks. This repository contains both a native Android application and its corresponding backend service, demonstrating modern software development practices across the entire stack.

> **🎓 Academic Context:**  
> This full-stack project was developed as part of a 7th-semester university assignment for the course *"Mobile Software Engineering"*.  
> **Date of Submission:** February 09, 2026  
> **Achieved Grade:** 1.7 (Good)

---

## 🏗️ System Architecture

The project is divided into two main components:
1. **Backend (`/backend`):** A RESTful API built with Java and Spring Boot, utilizing MongoDB for persistence.
2. **Frontend (`/frontend`):** A native Android application built entirely with Kotlin and Jetpack Compose, utilizing the MVVM architectural pattern.

The Android client communicates with the Spring Boot backend via HTTP requests using Retrofit, exchanging JSON data structured around the `ToDo` entity.

---

## 📱 Frontend Features (Android) (partially vibecoded)

* **Modern Declarative UI:** Fully built with Jetpack Compose, featuring custom themes and dynamic state-driven layouts (`ToDoScreen.kt`).
* **Optimistic UI Updates:** State changes (like checking off a task) are reflected in the UI instantly for a snappy experience. The API call is processed asynchronously in the background via Kotlin Coroutines.
* **Network Fallback System:** If the backend API is unreachable, the repository layer automatically switches to a local "Fake Data" fallback mode to prevent crashes and keep the app functional.
* **Smooth Animations:** Utilizes Compose's animation APIs (`animateFloatAsState`, `AnimatedVisibility`) for fluid checkbox interactions.

## ⚙️ Backend Features (Spring Boot)

* **Full CRUD API:** Exposes endpoints under `/api/todos` to Create, Read, Update, and Delete tasks.
* **Layered Architecture:** Strict separation of concerns between Controllers, Services (`ToDoServiceImpl`), and Repositories.
* **MongoDB Integration:** Seamless NoSQL data persistence using Spring Data's `MongoRepository`.
* **Interactive API Documentation:** Integrated with **Swagger UI** (OpenAPI). Accessing the root URL (`/`) automatically redirects to the interactive documentation.
* **CORS Enabled:** Cross-Origin Resource Sharing is enabled globally to allow seamless frontend connectivity.

---

## 🛠️ Tech Stack

**Frontend (Android):**
* Kotlin
* Jetpack Compose
* Retrofit2 & OkHttp (Networking)
* Coroutines & StateFlow (Asynchronous Programming)

**Backend (Spring Boot):**
* Java
* Spring Boot
* Spring Data MongoDB
* Swagger / OpenAPI

---

## 🚀 Getting Started

To run the full stack locally, you need to start the backend first, followed by the Android app.

### 1. Setup the Backend
1. Ensure you have **MongoDB** running locally on port `27017` (or configured via application properties).
2. Navigate to the backend directory.
3. Run the Spring Boot application (e.g., via `mvn spring-boot:run` or your IDE).
4. The server will start on `http://localhost:8089`. You can verify this by visiting `http://localhost:8089/swagger-ui/index.html` in your browser.

### 2. Setup the Frontend
1. Open the frontend project directory in **Android Studio**.
2. Sync the Gradle files.
3. The Retrofit client is pre-configured to point to `http://10.0.2.2:8089/`, which is the correct loopback address for the Android Emulator to communicate with your host machine's localhost.
4. Launch the app on an Android Emulator.
