package com.carrental;

// Base class demonstrating inheritance and polymorphism
public abstract class Vehicle {
    protected int id;
    protected String make;
    protected String model;
    protected int year;
    protected double dailyRate;

    public Vehicle(int id, String make, String model, int year, double dailyRate) {
        this.id = id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.dailyRate = dailyRate;
    }

    public int getId() { return id; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getDailyRate() { return dailyRate; }

    // Polymorphic behavior
    public abstract String getType();

    @Override
    public String toString() {
        return String.format("%d: %s %s (%d) - $%.2f/day", id, make, model, year, dailyRate);
    }
}
