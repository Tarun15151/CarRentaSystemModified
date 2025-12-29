<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="com.carrental.Transaction" %>
<%@ page import="com.carrental.TransactionDAO" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Transaction Management - Car Rental System</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 30px rgba(0,0,0,0.2);
            padding: 30px;
        }
        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 30px;
            border-bottom: 2px solid #667eea;
            padding-bottom: 15px;
        }
        .header h1 {
            color: #333;
            font-size: 28px;
        }
        .filters {
            display: flex; gap: 15px; margin-bottom: 20px;
        }
        .filters select, .filters input {
            padding: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table thead {
            background-color: #667eea;
            color: white;
        }
        table th {
            padding: 15px;
            text-align: left;
            font-weight: 600;
        }
        table td {
            padding: 12px 15px;
            border-bottom: 1px solid #eee;
        }
        table tr:hover {
            background-color: #f5f5f5;
        }
        .status-badge {
            padding: 5px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
        }
        .status-completed {
            background-color: #d4edda;
            color: #155724;
        }
        .status-pending {
            background-color: #fff3cd;
            color: #856404;
        }
        .status-failed {
            background-color: #f8d7da;
            color: #721c24;
        }
        .action-buttons {
            display: flex; gap: 10px;
        }
        .btn {
            padding: 8px 15px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            font-size: 13px;
            font-weight: 600;
            transition: all 0.3s;
        }
        .btn-primary {
            background-color: #667eea;
            color: white;
        }
        .btn-primary:hover {
            background-color: #5568d3;
        }
        .btn-info {
            background-color: #17a2b8;
            color: white;
        }
        .btn-info:hover {
            background-color: #138496;
        }
        .btn-danger {
            background-color: #dc3545;
            color: white;
        }
        .btn-danger:hover {
            background-color: #c82333;
        }
        .btn-group {
            display: flex; gap: 10px; margin-bottom: 20px;
        }
        .summary {
            display: grid; grid-template-columns: repeat(4, 1fr);
            gap: 15px; margin-bottom: 30px;
        }
        .summary-card {
            background: #f8f9fa;
            padding: 20px;
            border-radius: 8px;
            border-left: 4px solid #667eea;
        }
        .summary-card h3 {
            color: #666;
            font-size: 12px;
            text-transform: uppercase;
            margin-bottom: 8px;
        }
        .summary-card .value {
            color: #333;
            font-size: 24px;
            font-weight: 700;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>💳 Transaction Management</h1>
            <a href="addTransaction.jsp" class="btn btn-primary">+ Add Transaction</a>
        </div>

        <div class="summary">
            <div class="summary-card">
                <h3>Total Transactions</h3>
                <div class="value"><%
                    TransactionDAO tdao = new TransactionDAO();
                    List<Transaction> transactions = tdao.listAll();
                    out.print(transactions.size());
                %></div>
            </div>
            <div class="summary-card">
                <h3>Completed</h3>
                <div class="value"><%
                    long completed = transactions.stream().filter(t -> "COMPLETED".equals(t.getStatus())).count();
                    out.print(completed);
                %></div>
            </div>
            <div class="summary-card">
                <h3>Pending</h3>
                <div class="value"><%
                    long pending = transactions.stream().filter(t -> "PENDING".equals(t.getStatus())).count();
                    out.print(pending);
                %></div>
            </div>
            <div class="summary-card">
                <h3>Failed</h3>
                <div class="value"><%
                    long failed = transactions.stream().filter(t -> "FAILED".equals(t.getStatus())).count();
                    out.print(failed);
                %></div>
            </div>
        </div>

        <div class="filters">
            <select id="currencyFilter">
                <option value="">Filter by Currency</option>
                <option value="USD">USD</option>
                <option value="EUR">EUR</option>
                <option value="INR">INR</option>
                <option value="GBP">GBP</option>
            </select>
            <select id="statusFilter">
                <option value="">Filter by Status</option>
                <option value="COMPLETED">Completed</option>
                <option value="PENDING">Pending</option>
                <option value="FAILED">Failed</option>
            </select>
            <input type="text" id="searchRental" placeholder="Search by Rental ID...">
        </div>

        <table>
            <thead>
                <tr>
                    <th>Transaction ID</th>
                    <th>Rental ID</th>
                    <th>Amount</th>
                    <th>Currency</th>
                    <th>Type</th>
                    <th>Payment Method</th>
                    <th>Status</th>
                    <th>Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% for (Transaction t : transactions) { %>
                <tr>
                    <td><%= t.getTransactionId() %></td>
                    <td><%= t.getRentalId() %></td>
                    <td><%= String.format("%.2f", t.getAmount()) %></td>
                    <td><%= t.getCurrency() %></td>
                    <td><%= t.getTransactionType() %></td>
                    <td><%= t.getPaymentMethod() %></td>
                    <td>
                        <span class="status-badge status-<%= t.getStatus().toLowerCase() %>">
                            <%= t.getStatus() %>
                        </span>
                    </td>
                    <td><%= t.getTransactionDate() %></td>
                    <td>
                        <div class="action-buttons">
                            <a href="editTransaction.jsp?id=<%= t.getTransactionId() %>" class="btn btn-info">Edit</a>
                            <a href="deleteTransaction.jsp?id=<%= t.getTransactionId() %>" class="btn btn-danger" onclick="return confirm('Delete this transaction?')">Delete</a>
                        </div>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>

    <script>
        document.getElementById('currencyFilter').addEventListener('change', filterTable);
        document.getElementById('statusFilter').addEventListener('change', filterTable);
        document.getElementById('searchRental').addEventListener('keyup', filterTable);

        function filterTable() {
            const currency = document.getElementById('currencyFilter').value.toUpperCase();
            const status = document.getElementById('statusFilter').value.toUpperCase();
            const rental = document.getElementById('searchRental').value;
            const rows = document.querySelectorAll('table tbody tr');

            rows.forEach(row => {
                const rowCurrency = row.cells[3].textContent.toUpperCase();
                const rowStatus = row.cells[6].textContent.toUpperCase();
                const rowRental = row.cells[1].textContent;

                const matchCurrency = !currency || rowCurrency.includes(currency);
                const matchStatus = !status || rowStatus.includes(status);
                const matchRental = !rental || rowRental.includes(rental);

                row.style.display = (matchCurrency && matchStatus && matchRental) ? '' : 'none';
            });
        }
    </script>
</body>
</html>
