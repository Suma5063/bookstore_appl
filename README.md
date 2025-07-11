# 📚 BookStore Spring Boot Application

This is a full-featured BookStore application built using **Spring Boot** and **MongoDB**.  
Beyond basic CRUD, it supports **user registration**, **book purchases**, **email confirmations**, **queue-based processing**, and **scheduled cleanups**.  
The app is ready to deploy on **Azure**, **Docker**, or other cloud platforms.

---

## 🚀 Features

- ✅ Add, view, update, and delete books
- 🛍️ Purchase books with quantity tracking
- 👥 User registration & lookup
- 📧 Email notifications (via Gmail SMTP) to buyer and admin on purchase
- ⏳ Queue-based book processing (manual trigger & background task)
- 🧹 Scheduled task to delete old books (older than 30 days)
- 🧾 RESTful APIs using Spring Web
- 🛢️ MongoDB as the backend database
- 📦 Dockerized for easy containerized deployment
- 🧪 Ready for deployment on **Azure** or any cloud platform

---

## 🛠️ Tech Stack

- Java 17+
- Spring Boot (Web, Data MongoDB, Mail, Scheduling)
- MongoDB Atlas
- Maven
- Docker
- Git & GitHub for version control
- Azure (optional for deployment)

---

## 📁 Project Structure

<pre>
bookStore/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/bookstore/
│       │       ├── entity/
│       │       │   ├── Book.java              # Entity representing a Book
│       │       │   ├── User.java              # Entity representing a User
│       │       │   └── Purchase.java          # Entity representing a Purchase
│       │       ├── controller/
│       │       │   ├── BookController.java    # REST APIs for managing books
│       │       │   ├── PurchaseController.java# REST API to handle purchases
│       │       │   └── ManualTriggerController.java # Manual queue trigger
│       │       ├── repository/
│       │       │   ├── BookRepository.java
│       │       │   ├── UserRepository.java
│       │       │   └── PurchaseRepository.java
│       │       ├── schedulers/
│       │       │   └── BookCleanupScheduler.java   # Deletes old books daily
│       │       ├── service/
│       │       │   ├── BookService.java
│       │       │   ├── PurchaseService.java
│       │       │   ├── EmailService.java
│       │       │   └── BookQueueService.java
│       │       └── BookStoreApplication.java  # Main Spring Boot entry
│       └── resources/
│           ├── application.properties
│           ├── application.local.properties
│           └── application.azure.properties
├── Dockerfile
├── pom.xml
└── README.md
</pre>

---

## 🔄 How It Works

### 📘 Add a Book
POST /books
{
  "title": "The Alchemist",
  "author": "Paulo Coelho",
  "price": 399,
  "quantity": 10
}

👤 Register a USER
----------------
POST /users
{
  "name": "Suma",
  "email": "suma@mailinator.com"
}

🛒 Purchase a Book
-------------------
POST /purchases?email=suma@mailinator.com&bookId=BOOK_ID&quantity=2

⏲️ Scheduled Jobs
------------------
🧹 Old Book Cleanup
Runs daily at 2 AM

Deletes books older than 30 days

🔁 Process Queue (Manual or Scheduled)
--------------------------------------
Add books to a queue on creation
  Process via: -> POST /admin/trigger-queue (manual)  or scheduled every hour

💌 Email Setup
--------------
Email notifications are configured with Gmail SMTP in application.properties

🐳 Docker Support
------------------
To build and run the app: 
    docker build -t bookstore-app .
    docker run -p 8081:8081 bookstore-app

🧪 Testing the APIs
   -> Use curl, Postman, or Swagger UI (enabled at /swagger-ui.html)
