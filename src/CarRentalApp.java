package com.carrental;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

// Simple Swing GUI demonstrating functionality (MVC usage)
public class CarRentalApp {
    private final CarService service;
    private final JFrame frame;
    private final DefaultListModel<String> listModel;

    public CarRentalApp(CarService service) {
        this.service = service;
        this.frame = new JFrame("Car Rental System - Demo");
        this.listModel = new DefaultListModel<>();
    }

    private void refreshList() {
        listModel.clear();
        for (Car c : service.getAvailableCars()) {
            listModel.addElement(c.toString());
        }
    }

    public void show() {
        service.seedDemoData();
        AutoRefreshThread refresher = new AutoRefreshThread(service, 5000);
        refresher.start();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 400);
        frame.setLayout(new BorderLayout());

        JList<String> list = new JList<>(listModel);
        JScrollPane scroll = new JScrollPane(list);
        frame.add(scroll, BorderLayout.CENTER);

        JPanel controls = new JPanel();
        JButton rentBtn = new JButton("Rent Selected Car");
        JButton returnBtn = new JButton("Return Selected Car");
        JButton refreshBtn = new JButton("Refresh");
        controls.add(rentBtn);
        controls.add(returnBtn);
        controls.add(refreshBtn);
        frame.add(controls, BorderLayout.SOUTH);

        refreshBtn.addActionListener(e -> refreshList());
        rentBtn.addActionListener(e -> {
            int idx = list.getSelectedIndex();
            if (idx == -1) {
                JOptionPane.showMessageDialog(frame, "Select a car to rent.");
                return;
            }
            String item = listModel.get(idx);
            int id = Integer.parseInt(item.split(":" )[0].trim());
            try {
                service.rentCar(id);
                JOptionPane.showMessageDialog(frame, "Rented car " + id);
                refreshList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Could not rent: " + ex.getMessage());
            }
        });

        returnBtn.addActionListener(e -> {
            String input = JOptionPane.showInputDialog(frame, "Enter Car ID to return:");
            if (input == null || input.isEmpty()) return;
            try {
                int id = Integer.parseInt(input.trim());
                service.returnCar(id);
                JOptionPane.showMessageDialog(frame, "Returned car " + id);
                refreshList();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(frame, "Could not return: " + ex.getMessage());
            }
        });

        refreshList();
        frame.setVisible(true);
    }

    // Main entry (keeps GUI-only mode if DB driver missing)
    public static void main(String[] args) {
        CarDAO dao = new CarDAO();
        CarService service = new CarService(dao);
        SwingUtilities.invokeLater(() -> new CarRentalApp(service).show());
    }
}

   
