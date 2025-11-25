package com.carrental;

import java.util.List;

// Demonstrates multithreading: periodically logs available cars.
public class AutoRefreshThread extends Thread {
    private final CarService service;
    private volatile boolean running = true;
    private final long intervalMs;

    public AutoRefreshThread(CarService service, long intervalMs) {
        this.service = service;
        this.intervalMs = intervalMs;
        setDaemon(true);
    }

    @Override
    public void run() {
        while (running) {
            try {
                List<Car> list = service.getAvailableCars();
                System.out.println("[AutoRefresh] Available cars: " + list.size());
                Thread.sleep(intervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public void shutdown() {
        running = false;
        this.interrupt();
    }
}
