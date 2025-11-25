package com.carrental;

// Custom checked exception
public class CarNotAvailableException extends Exception {
    public CarNotAvailableException(String message) {
        super(message);
    }
}
