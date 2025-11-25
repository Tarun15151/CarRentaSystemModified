package com.carrental;

import java.sql.*;
import java.util.*;

// DAO demonstrating CRUD, PreparedStatements, transactions, collections & generics
public class CarDAO {
    // In-memory cache demonstrating use of Collections & Generics (Map of id->Car)
    private final Map<Integer, Car> cache = Collections.synchronizedMap(new HashMap<>());

    public CarDAO() {
        // attempt to create table if DB available
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS cars (id INTEGER PRIMARY KEY, make TEXT, model TEXT, year INTEGER, dailyRate REAL, seats INTEGER, available INTEGER)");
            // load into cache
            loadAll();
        } catch (Exception e) {
            // DB may be absent; that is okay for running as GUI-only app
        }
    }

    // Create
    public synchronized void add(Car car) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO cars(id, make, model, year, dailyRate, seats, available) VALUES(?,?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, car.getId());
                ps.setString(2, car.getMake());
                ps.setString(3, car.getModel());
                ps.setInt(4, car.getYear());
                ps.setDouble(5, car.getDailyRate());
                ps.setInt(6, car.getSeats());
                ps.setInt(7, car.isAvailable() ? 1 : 0);
                ps.executeUpdate();
                conn.commit();
                cache.put(car.getId(), car);
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    // Read all
    public List<Car> listAll() {
        synchronized (cache) {
            return new ArrayList<>(cache.values());
        }
    }

    // Read by id (uses cache)
    public Optional<Car> findById(int id) {
        return Optional.ofNullable(cache.get(id));
    }

    // Update
    public synchronized void update(Car car) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            String sql = "UPDATE cars SET make=?, model=?, year=?, dailyRate=?, seats=?, available=? WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, car.getMake());
                ps.setString(2, car.getModel());
                ps.setInt(3, car.getYear());
                ps.setDouble(4, car.getDailyRate());
                ps.setInt(5, car.getSeats());
                ps.setInt(6, car.isAvailable() ? 1 : 0);
                ps.setInt(7, car.getId());
                ps.executeUpdate();
                conn.commit();
                cache.put(car.getId(), car);
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    // Delete
    public synchronized void delete(int id) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            String sql = "DELETE FROM cars WHERE id=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, id);
                ps.executeUpdate();
                conn.commit();
                cache.remove(id);
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    // Load all from DB into cache
    public final void loadAll() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT id, make, model, year, dailyRate, seats, available FROM cars")) {
            while (rs.next()) {
                int id = rs.getInt(1);
                String make = rs.getString(2);
                String model = rs.getString(3);
                int year = rs.getInt(4);
                double rate = rs.getDouble(5);
                int seats = rs.getInt(6);
                boolean available = rs.getInt(7) == 1;
                Car car = new Car(id, make, model, year, rate, seats);
                car.setAvailable(available);
                cache.put(id, car);
            }
        } catch (Exception e) {
            // ignore if DB not available
        }
    }
}

           
