package com.carrental;

// Car extends Vehicle and implements Rentable (interface)
public class Car extends Vehicle implements Rentable {
    private int seats;
    private boolean available;

    public Car(int id, String make, String model, int year, double dailyRate, int seats) {
        super(id, make, model, year, dailyRate);
        this.seats = seats;
        this.available = true;
    }

    public int getSeats() { return seats; }
    public boolean isAvailable() { return available; }

    public void setAvailable(boolean available) { this.available = available; }

    @Override
    public String getType() { return "Car"; }

    @Override
    public synchronized void rent() throws CarNotAvailableException {
        if (!available) throw new CarNotAvailableException("Car " + id + " is not available.");
        available = false;
    }

    @Override
    public synchronized void returns() {
        available = true;
    }
}
