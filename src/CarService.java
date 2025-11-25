package com.carrental;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.locks.*;

// Service layer implementing business logic and thread-safety
public class CarService {
    private final CarDAO dao;
    // Lock to protect rental operations
    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    public CarService(CarDAO dao) {
        this.dao = dao;
    }

    public List<Car> getAvailableCars() {
        lock.readLock().lock();
        try {
            List<Car> result = new ArrayList<>();
            for (Car c : dao.listAll()) {
                if (c.isAvailable()) result.add(c);
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    // Rent a car with transactional safety
    public void rentCar(int id) throws CarNotAvailableException, SQLException {
        lock.writeLock().lock();
        try {
            Car car = dao.findById(id).orElseThrow(() -> new CarNotAvailableException("Car not found: " + id));
            car.rent(); // may throw CarNotAvailableException
            dao.update(car); // persist availability
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void returnCar(int id) throws SQLException {
        lock.writeLock().lock();
        try {
            Car car = dao.findById(id).orElse(null);
            if (car != null) {
                car.returns();
                dao.update(car);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    // Seed demo data (used by GUI)
    public void seedDemoData() {
        try {
            if (dao.listAll().isEmpty()) {
                dao.add(new Car(1, "Toyota", "Corolla", 2019, 35.0, 5));
                dao.add(new Car(2, "Honda", "Civic", 2020, 40.0, 5));
                dao.add(new Car(3, "Ford", "EcoSport", 2018, 45.0, 5));
            }
        } catch (SQLException e) {
            // ignore DB errors for demo mode
            // but still populate cache directly if DB not present
            try {
                dao.loadAll(); // attempt to reload after failure
            } catch (Exception ex) {}
        }
    }
}
