
# Car Rental System

## Overview
A simple Java-based Car Rental System that allows users to:
- View available cars
- Rent and return cars
- Store and manage data with a relational database

## Features
- Core functionality for renting and returning cars
- Integrated database connectivity
- Event-driven GUI (if applicable)
- Error handling for database and user input
- Data validation on all input fields

## How to Run
1. Ensure you have Java and a MySQL-compatible database installed.
2. Configure the database credentials in `DBConnection.java`.
3. Compile the project:
   ```
   javac -d bin src/*.java
   ```
4. Run the application:
   ```
   java -cp bin CarRentalApp
   ```

## Code Structure
- `Car.java` – Represents car model
- `CarDAO.java` – Data access operations
- `DBConnection.java` – Handles database connections
- `CarRentalApp.java` – Main entry point
- `TestDB.java` – For DB testing

## Enhancements
- Robust error handling
- Input validation
- Cleaner modular code
- Readable naming conventions

## Author
[Tarun]
