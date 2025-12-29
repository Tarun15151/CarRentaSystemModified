# 🚗 Car Rental Management System  
### Java Swing GUI + JSP/Servlet Web App + JDBC + DAO + OOP + Multithreading

A complete Java-based Car Rental System built for academic and professional learning.  
This project includes both **GUI (Swing)** and **Web (JSP/Servlet)** versions, fully integrated with a database and structured using MVC, DAO, and OOP principles.

This project fulfills the **Java Review Rubric** requirements:
✔ OOP  
✔ Exception Handling  
✔ Collections & Generics  
✔ Multithreading  
✔ DAO + Database  
✔ GUI  
✔ Servlet/JSP  
✔ Code Quality  

---

# ✨ Features

## 🎯 Object-Oriented Programming (OOP)
- `Vehicle` — abstract base class  
- `Car` — child class (inheritance)  
- `Rentable` — interface  
- `CarNotAvailableException` — custom exception  
- Polymorphism (`getType()`)  

---

## 🧠 Collections & Generics
- Uses `Map<Integer, Car>` for caching  
- DAO returns `List<Car>`  
- Thread-safe access using locks  

---

## ⚙️ Multithreading
- `AutoRefreshThread` updates availability  
- Read/Write locks avoid race conditions  

---

## 🗄 Database (JDBC + DAO)
- CRUD operations  
- Prepared statements  
- Transactions (commit/rollback)  
- SQLite-ready  
- Auto table creation  

---

## 💻 GUI (Java Swing)
Located in:
```
src/com/carrental/CarRentalApp.java
```

---

## 🌐 Web Version (JSP + Servlet)
Servlet:
```
websrc/com/carrental/web/CarServlet.java
```
JSP Pages:
```
webapp/views/
```

---

# 🗂 Project Structure

```
CarRentalSystem/
 ├── src/com/carrental/
 ├── websrc/com/carrental/web/
 ├── webapp/
 │     ├── index.jsp
 │     ├── views/
 │     └── WEB-INF/
 ├── README.md
 └── .gitignore
```

---

# 🚀 How to Run (GUI)

1. Open the project in IntelliJ IDEA  
2. Mark `src/` as *Sources Root*  
3. Run:
```
com.carrental.CarRentalApp
```

---

# 🌐 How to Run (Web Version)

### 1️⃣ Install Tomcat  
### 2️⃣ Add dependencies:
- sqlite-jdbc  
- servlet-api  

### 3️⃣ Configure Artifact  
### 4️⃣ Run server  

Open:
```
http://localhost:8080/CarRentalSystem
```

---

# 🧪 Database
SQLite database auto-creates:
```
car_rental.db
```

---

## 💳 Transaction Management

**Overview:**
The Car Rental System includes a comprehensive transaction tracking module that records all rental activities and financial transactions.

### Transaction Features:

- **Transaction Recording**: Automatically logs every rental transaction with timestamps
- **Financial Tracking**: Tracks rental costs, deposits, and payment methods
- **Transaction History**: Maintains complete history of all transactions
- **Payment Status**: Records payment completion and status
- **Multi-currency Support**: CurrencyConverter utility for international transactions
- **Transaction Reports**: Generate transaction summaries and financial reports

### Transaction Data Captured:

- **Transaction ID**: Unique identifier for each transaction
- **Customer ID**: Link to the renting customer
- **Car ID**: Vehicle being rented
- **Rental Date**: Date when rental started
- **Return Date**: Expected/actual return date
- **Total Cost**: Calculated rental amount
- **Payment Method**: How payment was made
- **Transaction Status**: Pending/Completed/Failed
- **Timestamp**: When the transaction was recorded

### Web Interface for Transactions:

JSP Pages:

`addTransaction.jsp` - Form to add new transactions

`listTransactions.jsp` - Display all transaction records

### Database Support:

Transactions are persisted in SQLite database with proper indexing for fast queries and reporting.

---

# Author 
Tarun

