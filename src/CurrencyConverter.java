package com.carrental;

import java.util.HashMap;
import java.util.Map;

// Multi-currency converter with predefined exchange rates
public class CurrencyConverter {
    // Exchange rates relative to USD (base currency)
    private static final Map<String, Double> EXCHANGE_RATES = new HashMap<>();

    static {
        // Base currency is USD (1.0)
        EXCHANGE_RATES.put("USD", 1.0);
        // Common currencies for international car rentals
        EXCHANGE_RATES.put("EUR", 0.92);
        EXCHANGE_RATES.put("GBP", 0.79);
        EXCHANGE_RATES.put("INR", 83.12);
        EXCHANGE_RATES.put("JPY", 149.50);
        EXCHANGE_RATES.put("AUD", 1.53);
        EXCHANGE_RATES.put("CAD", 1.36);
        EXCHANGE_RATES.put("CHF", 0.88);
        EXCHANGE_RATES.put("SGD", 1.35);
        EXCHANGE_RATES.put("HKD", 7.85);
        EXCHANGE_RATES.put("MXN", 17.05);
        EXCHANGE_RATES.put("BRL", 4.97);
        EXCHANGE_RATES.put("ZAR", 18.75);
    }

    public static double convert(double amount, String fromCurrency, String toCurrency) {
        fromCurrency = fromCurrency.toUpperCase();
        toCurrency = toCurrency.toUpperCase();

        if (!EXCHANGE_RATES.containsKey(fromCurrency)) {
            throw new IllegalArgumentException("Unsupported currency: " + fromCurrency);
        }
        if (!EXCHANGE_RATES.containsKey(toCurrency)) {
            throw new IllegalArgumentException("Unsupported currency: " + toCurrency);
        }

        // Convert to USD first, then to target currency
        double amountInUSD = amount / EXCHANGE_RATES.get(fromCurrency);
        double convertedAmount = amountInUSD * EXCHANGE_RATES.get(toCurrency);

        return Math.round(convertedAmount * 100.0) / 100.0;
    }

    public static double getExchangeRate(String fromCurrency, String toCurrency) {
        fromCurrency = fromCurrency.toUpperCase();
        toCurrency = toCurrency.toUpperCase();

        if (!EXCHANGE_RATES.containsKey(fromCurrency) || !EXCHANGE_RATES.containsKey(toCurrency)) {
            throw new IllegalArgumentException("Unsupported currency");
        }

        return Math.round((EXCHANGE_RATES.get(toCurrency) / EXCHANGE_RATES.get(fromCurrency)) * 10000.0) / 10000.0;
    }

    public static String[] getSupportedCurrencies() {
        return EXCHANGE_RATES.keySet().toArray(new String[0]);
    }

    public static boolean isSupportedCurrency(String currency) {
        return EXCHANGE_RATES.containsKey(currency.toUpperCase());
    }

    public static void updateExchangeRate(String currency, double rate) {
        if (rate <= 0) {
            throw new IllegalArgumentException("Exchange rate must be positive");
        }
        EXCHANGE_RATES.put(currency.toUpperCase(), rate);
    }

    public static double getRateToUSD(String currency) {
        currency = currency.toUpperCase();
        if (!EXCHANGE_RATES.containsKey(currency)) {
            throw new IllegalArgumentException("Unsupported currency: " + currency);
        }
        return EXCHANGE_RATES.get(currency);
    }

    public static double convertTransaction(Transaction transaction, String targetCurrency) {
        return convert(transaction.getAmount(), transaction.getCurrency(), targetCurrency);
    }
}
