package com.carrental;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDateTime;
import java.util.List;

// Transaction UI for Swing GUI with multi-currency support
public class TransactionUI {
    private JFrame frame;
    private TransactionDAO transactionDAO;
    private DefaultListModel<String> transactionListModel;
    private JList<String> transactionList;
    private JLabel totalLabel;
    private String selectedCurrency = "USD";

    public TransactionUI(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
        this.transactionListModel = new DefaultListModel<>();
        this.frame = new JFrame("Transaction Management");
    }

    public void show() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout(10, 10));

        // Top panel - Currency selector and info
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        topPanel.add(new JLabel("Display Currency:"));
        String[] currencies = {"USD", "EUR", "INR", "GBP", "JPY", "AUD", "CAD"};
        JComboBox<String> currencyBox = new JComboBox<>(currencies);
        currencyBox.setSelectedItem(selectedCurrency);
        currencyBox.addActionListener(e -> {
            selectedCurrency = (String) currencyBox.getSelectedItem();
            refreshTransactionList();
            updateTotal();
        });
        topPanel.add(currencyBox);
        topPanel.add(new JSeparator(SwingConstants.VERTICAL));
        totalLabel = new JLabel("Total: " + selectedCurrency + " 0.00");
        totalLabel.setFont(new Font("Arial", Font.BOLD, 14));
        topPanel.add(totalLabel);
        frame.add(topPanel, BorderLayout.NORTH);

        // Center panel - Transaction list
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        transactionList = new JList<>(transactionListModel);
        transactionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(transactionList);
        centerPanel.add(new JLabel("Transactions:"), BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        frame.add(centerPanel, BorderLayout.CENTER);

        // Bottom panel - Action buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        
        JButton addBtn = new JButton("Add Transaction");
        addBtn.addActionListener(e -> showAddTransactionDialog());
        bottomPanel.add(addBtn);

        JButton editBtn = new JButton("Edit Selected");
        editBtn.addActionListener(e -> showEditTransactionDialog());
        bottomPanel.add(editBtn);

        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> deleteSelectedTransaction());
        bottomPanel.add(deleteBtn);

        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshTransactionList());
        bottomPanel.add(refreshBtn);

        JButton historyBtn = new JButton("View by Rental");
        historyBtn.addActionListener(e -> showRentalTransactionHistory());
        bottomPanel.add(historyBtn);

        frame.add(bottomPanel, BorderLayout.SOUTH);
        refreshTransactionList();
        frame.setVisible(true);
    }

    private void refreshTransactionList() {
        transactionListModel.clear();
        List<Transaction> transactions = transactionDAO.listAll();
        for (Transaction t : transactions) {
            String display = String.format("[%d] Rental:%d | %s | Status:%s | %s",
                    t.getTransactionId(), t.getRentalId(), t.getFormattedAmount(),
                    t.getStatus(), t.getPaymentMethod());
            transactionListModel.addElement(display);
        }
    }

    private void updateTotal() {
        List<Transaction> transactions = transactionDAO.listAll();
        double total = 0.0;
        for (Transaction t : transactions) {
            if (t.getStatus().equals("COMPLETED")) {
                if (t.getConvertedCurrency().equals(selectedCurrency)) {
                    total += t.getConvertedAmount();
                } else if (t.getCurrency().equals(selectedCurrency)) {
                    total += t.getAmount();
                }
            }
        }
        totalLabel.setText(String.format("Total: %s %.2f", selectedCurrency, total));
    }

    private void showAddTransactionDialog() {
        JDialog dialog = new JDialog(frame, "Add Transaction", true);
        dialog.setSize(500, 400);
        dialog.setLayout(new GridLayout(10, 2, 10, 10));

        dialog.add(new JLabel("Rental ID:"));
        JTextField rentalIdField = new JTextField();
        dialog.add(rentalIdField);

        dialog.add(new JLabel("Amount:"));
        JTextField amountField = new JTextField();
        dialog.add(amountField);

        dialog.add(new JLabel("Currency:"));
        JComboBox<String> currencyBox = new JComboBox<>(new String[]{"USD", "EUR", "INR", "GBP", "JPY", "AUD", "CAD"});
        dialog.add(currencyBox);

        dialog.add(new JLabel("Transaction Type:"));
        JComboBox<String> typeBox = new JComboBox<>(new String[]{"PAYMENT", "REFUND", "CHARGE"});
        dialog.add(typeBox);

        dialog.add(new JLabel("Payment Method:"));
        JComboBox<String> methodBox = new JComboBox<>(new String[]{"CREDIT_CARD", "DEBIT_CARD", "UPI", "NET_BANKING"});
        dialog.add(methodBox);

        dialog.add(new JLabel("Description:"));
        JTextField descField = new JTextField();
        dialog.add(descField);

        dialog.add(new JLabel("Status:"));
        JComboBox<String> statusBox = new JComboBox<>(new String[]{"COMPLETED", "PENDING", "FAILED"});
        dialog.add(statusBox);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            try {
                int rentalId = Integer.parseInt(rentalIdField.getText());
                double amount = Double.parseDouble(amountField.getText());
                String currency = (String) currencyBox.getSelectedItem();
                String type = (String) typeBox.getSelectedItem();
                String method = (String) methodBox.getSelectedItem();
                String desc = descField.getText();
                String status = (String) statusBox.getSelectedItem();

                int transactionId = System.identityHashCode(new Object()) % 10000;
                Transaction transaction = new Transaction(transactionId, rentalId, amount, currency,
                        type, LocalDateTime.now(), status, method, desc);
                transactionDAO.add(transaction);
                JOptionPane.showMessageDialog(dialog, "Transaction added successfully!");
                refreshTransactionList();
                updateTotal();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage());
            }
        });
        dialog.add(saveBtn);

        JButton cancelBtn = new JButton("Cancel");
        cancelBtn.addActionListener(e -> dialog.dispose());
        dialog.add(cancelBtn);

        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }

    private void showEditTransactionDialog() {
        int selectedIndex = transactionList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a transaction to edit.");
            return;
        }
        JOptionPane.showMessageDialog(frame, "Edit functionality can be implemented similarly.");
    }

    private void deleteSelectedTransaction() {
        int selectedIndex = transactionList.getSelectedIndex();
        if (selectedIndex == -1) {
            JOptionPane.showMessageDialog(frame, "Please select a transaction to delete.");
            return;
        }
        try {
            String item = transactionListModel.get(selectedIndex);
            int transactionId = Integer.parseInt(item.split("\\[")[1].split("\\]")[0]);
            transactionDAO.delete(transactionId);
            refreshTransactionList();
            updateTotal();
            JOptionPane.showMessageDialog(frame, "Transaction deleted successfully!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(frame, "Error: " + ex.getMessage());
        }
    }

    private void showRentalTransactionHistory() {
        String rentalIdStr = JOptionPane.showInputDialog(frame, "Enter Rental ID:");
        if (rentalIdStr == null || rentalIdStr.isEmpty()) return;
        
        try {
            int rentalId = Integer.parseInt(rentalIdStr);
            List<Transaction> transactions = transactionDAO.findByRentalId(rentalId);
            
            StringBuilder history = new StringBuilder();
            history.append("Transactions for Rental ").append(rentalId).append(":\n\n");
            for (Transaction t : transactions) {
                history.append(t.toString()).append("\n");
            }
            
            JTextArea textArea = new JTextArea(history.toString());
            textArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(textArea);
            
            JDialog historyDialog = new JDialog(frame, "Rental Transaction History", true);
            historyDialog.add(scrollPane);
            historyDialog.setSize(500, 400);
            historyDialog.setLocationRelativeTo(frame);
            historyDialog.setVisible(true);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Invalid Rental ID!");
        }
    }
}
