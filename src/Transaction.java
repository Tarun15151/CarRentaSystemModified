package com.carrental;

import java.time.LocalDateTime;
import java.util.Currency;
import java.util.Locale;

// Transaction model supporting multi-currency operations
public class Transaction {
    private int transactionId;
    private int rentalId;
    private double amount;
    private String currency;  // ISO 4217 currency code (USD, EUR, INR, GBP, etc.)
    private String transactionType;  // PAYMENT, REFUND, CHARGE
    private LocalDateTime transactionDate;
    private String status;  // COMPLETED, PENDING, FAILED
    private String paymentMethod;  // CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING
    private String description;
    private double exchangeRate;  // Rate used for conversion (if applicable)
    private String convertedCurrency;  // Currency converted to (if applicable)
    private double convertedAmount;  // Amount after conversion

    // Constructor
    public Transaction(int transactionId, int rentalId, double amount, String currency,
                      String transactionType, LocalDateTime transactionDate, 
                      String status, String paymentMethod, String description) {
        this.transactionId = transactionId;
        this.rentalId = rentalId;
        this.amount = amount;
        this.currency = currency.toUpperCase();
        this.transactionType = transactionType;
        this.transactionDate = transactionDate;
        this.status = status;
        this.paymentMethod = paymentMethod;
        this.description = description;
        this.exchangeRate = 1.0;
        this.convertedCurrency = currency.toUpperCase();
        this.convertedAmount = amount;
    }

    // Getters and Setters
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public int getRentalId() { return rentalId; }
    public void setRentalId(int rentalId) { this.rentalId = rentalId; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency.toUpperCase(); }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(double exchangeRate) { this.exchangeRate = exchangeRate; }

    public String getConvertedCurrency() { return convertedCurrency; }
    public void setConvertedCurrency(String convertedCurrency) { 
        this.convertedCurrency = convertedCurrency.toUpperCase(); 
    }

    public double getConvertedAmount() { return convertedAmount; }
    public void setConvertedAmount(double convertedAmount) { this.convertedAmount = convertedAmount; }

    // Multi-currency conversion method
    public void convertCurrency(String targetCurrency, double exchangeRate) {
        if (targetCurrency == null || targetCurrency.isEmpty()) {
            throw new IllegalArgumentException("Target currency cannot be null or empty");
        }
        if (exchangeRate <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive");
        }
        
        this.convertedCurrency = targetCurrency.toUpperCase();
        this.exchangeRate = exchangeRate;
        this.convertedAmount = this.amount * exchangeRate;
    }

    // Get formatted currency string
    public String getFormattedAmount() {
        try {
            Currency curr = Currency.getInstance(currency);
            return String.format("%s %.2f", curr.getSymbol(Locale.US), amount);
        } catch (IllegalArgumentException e) {
            return String.format("%s %.2f", currency, amount);
        }
    }

    public String getFormattedConvertedAmount() {
        try {
            Currency curr = Currency.getInstance(convertedCurrency);
            return String.format("%s %.2f", curr.getSymbol(Locale.US), convertedAmount);
        } catch (IllegalArgumentException e) {
            return String.format("%s %.2f", convertedCurrency, convertedAmount);
        }
    }

    @Override
    public String toString() {
        return String.format("Transaction{id=%d, rentalId=%d, amount=%s, type=%s, status=%s, date=%s}",
                transactionId, rentalId, getFormattedAmount(), transactionType, status, transactionDate);
    }
}
