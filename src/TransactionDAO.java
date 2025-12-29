package com.carrental;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.*;

// TransactionDAO - CRUD operations for Transaction objects with multi-currency support
public class TransactionDAO {
    private final Map<Integer, Transaction> cache = Collections.synchronizedMap(new HashMap<>());

    public TransactionDAO() {
        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS transactions (" +
                    "transactionId INTEGER PRIMARY KEY, " +
                    "rentalId INTEGER NOT NULL, " +
                    "amount REAL NOT NULL, " +
                    "currency TEXT NOT NULL, " +
                    "transactionType TEXT NOT NULL, " +
                    "transactionDate TIMESTAMP NOT NULL, " +
                    "status TEXT NOT NULL, " +
                    "paymentMethod TEXT NOT NULL, " +
                    "description TEXT, " +
                    "exchangeRate REAL DEFAULT 1.0, " +
                    "convertedCurrency TEXT, " +
                    "convertedAmount REAL)");
            loadAll();
        } catch (Exception e) {
            System.err.println("TransactionDAO initialization: " + e.getMessage());
        }
    }

    // Create
    public synchronized void add(Transaction transaction) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            String sql = "INSERT INTO transactions(transactionId, rentalId, amount, currency, " +
                    "transactionType, transactionDate, status, paymentMethod, description, " +
                    "exchangeRate, convertedCurrency, convertedAmount) " +
                    "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, transaction.getTransactionId());
                ps.setInt(2, transaction.getRentalId());
                ps.setDouble(3, transaction.getAmount());
                ps.setString(4, transaction.getCurrency());
                ps.setString(5, transaction.getTransactionType());
                ps.setTimestamp(6, Timestamp.valueOf(transaction.getTransactionDate()));
                ps.setString(7, transaction.getStatus());
                ps.setString(8, transaction.getPaymentMethod());
                ps.setString(9, transaction.getDescription());
                ps.setDouble(10, transaction.getExchangeRate());
                ps.setString(11, transaction.getConvertedCurrency());
                ps.setDouble(12, transaction.getConvertedAmount());
                ps.executeUpdate();
                conn.commit();
                cache.put(transaction.getTransactionId(), transaction);
            } catch (SQLException ex) {
                conn.rollback();
                throw ex;
            }
        }
    }

    // Read all
    public List<Transaction> listAll() {
        synchronized (cache) {
            return new ArrayList<>(cache.values());
        }
    }

    // Read by transaction ID
    public Optional<Transaction> findById(int id) {
        return Optional.ofNullable(cache.get(id));
    }

    // Read by rental ID
    public List<Transaction> findByRentalId(int rentalId) {
        List<Transaction> result = new ArrayList<>();
        synchronized (cache) {
            for (Transaction t : cache.values()) {
                if (t.getRentalId() == rentalId) {
                    result.add(t);
                }
            }
        }
        return result;
    }

    // Get total amount for a rental
    public double getTotalByRentalId(int rentalId) {
        double total = 0.0;
        for (Transaction t : findByRentalId(rentalId)) {
            if (t.getStatus().equals("COMPLETED")) {
                total += t.getAmount();
            }
        }
        return total;
    }

    // Get total amount for a rental in specific currency
    public double getTotalByRentalIdInCurrency(int rentalId, String currency) {
        double total = 0.0;
        for (Transaction t : findByRentalId(rentalId)) {
            if (t.getStatus().equals("COMPLETED")) {
                if (t.getConvertedCurrency().equals(currency)) {
                    total += t.getConvertedAmount();
                } else if (t.getCurrency().equals(currency)) {
                    total += t.getAmount();
                }
            }
        }
        return total;
    }

    // Update
    public synchronized void update(Transaction transaction) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            String sql = "UPDATE transactions SET rentalId=?, amount=?, currency=?, " +
                    "transactionType=?, transactionDate=?, status=?, paymentMethod=?, " +
                    "description=?, exchangeRate=?, convertedCurrency=?, convertedAmount=? " +
                    "WHERE transactionId=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, transaction.getRentalId());
                ps.setDouble(2, transaction.getAmount());
                ps.setString(3, transaction.getCurrency());
                ps.setString(4, transaction.getTransactionType());
                ps.setTimestamp(5, Timestamp.valueOf(transaction.getTransactionDate()));
                ps.setString(6, transaction.getStatus());
                ps.setString(7, transaction.getPaymentMethod());
                ps.setString(8, transaction.getDescription());
                ps.setDouble(9, transaction.getExchangeRate());
                ps.setString(10, transaction.getConvertedCurrency());
                ps.setDouble(11, transaction.getConvertedAmount());
                ps.setInt(12, transaction.getTransactionId());
                ps.executeUpdate();
                conn.commit();
                cache.put(transaction.getTransactionId(), transaction);
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
            String sql = "DELETE FROM transactions WHERE transactionId=?";
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
             ResultSet rs = st.executeQuery("SELECT transactionId, rentalId, amount, currency, " +
                     "transactionType, transactionDate, status, paymentMethod, description, " +
                     "exchangeRate, convertedCurrency, convertedAmount FROM transactions")) {
            while (rs.next()) {
                int tid = rs.getInt(1);
                int rid = rs.getInt(2);
                double amount = rs.getDouble(3);
                String currency = rs.getString(4);
                String type = rs.getString(5);
                LocalDateTime date = rs.getTimestamp(6).toLocalDateTime();
                String status = rs.getString(7);
                String paymentMethod = rs.getString(8);
                String description = rs.getString(9);
                
                Transaction t = new Transaction(tid, rid, amount, currency, type, date, status, paymentMethod, description);
                t.setExchangeRate(rs.getDouble(10));
                t.setConvertedCurrency(rs.getString(11));
                t.setConvertedAmount(rs.getDouble(12));
                cache.put(tid, t);
            }
        } catch (Exception e) {
            System.err.println("TransactionDAO.loadAll: " + e.getMessage());
        }
    }
}
