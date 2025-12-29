<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.carrental.TransactionDAO" %>
<%@ page import="com.carrental.Transaction" %>
<%@ page import="java.time.LocalDateTime" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Add Transaction - Car Rental System</title>
    <style>
        * { margin: 0; padding: 0; box-sizing: border-box; }
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        .form-container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 40px rgba(0,0,0,0.2);
            padding: 40px;
            width: 100%;
            max-width: 600px;
        }
        .form-container h1 {
            color: #333;
            margin-bottom: 30px;
            font-size: 24px;
        }
        .form-group {
            margin-bottom: 20px;
        }
        .form-group label {
            display: block;
            margin-bottom: 8px;
            color: #555;
            font-weight: 600;
            font-size: 14px;
        }
        .form-group input, .form-group select, .form-group textarea {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 5px;
            font-size: 14px;
            font-family: inherit;
            transition: border-color 0.3s;
        }
        .form-group input:focus, .form-group select:focus, .form-group textarea:focus {
            outline: none;
            border-color: #667eea;
            box-shadow: 0 0 5px rgba(102, 126, 234, 0.3);
        }
        .form-row {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 20px;
        }
        .button-group {
            display: flex;
            gap: 15px;
            margin-top: 30px;
        }
        .btn {
            flex: 1;
            padding: 12px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s;
        }
        .btn-submit {
            background-color: #667eea;
            color: white;
        }
        .btn-submit:hover {
            background-color: #5568d3;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(102, 126, 234, 0.4);
        }
        .btn-cancel {
            background-color: #f0f0f0;
            color: #333;
        }
        .btn-cancel:hover {
            background-color: #e0e0e0;
        }
        .message {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        .message.success {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        .message.error {
            background-color: #f8d7da;
            color: #721c24;
            border: 1px solid #f5c6cb;
        }
    </style>
</head>
<body>
    <div class="form-container">
        <h1>💳 Add New Transaction</h1>
        
        <%
            String action = request.getParameter("action");
            if ("add".equals(action)) {
                try {
                    int rentalId = Integer.parseInt(request.getParameter("rentalId"));
                    double amount = Double.parseDouble(request.getParameter("amount"));
                    String currency = request.getParameter("currency");
                    String transactionType = request.getParameter("transactionType");
                    String paymentMethod = request.getParameter("paymentMethod");
                    String description = request.getParameter("description");
                    String status = request.getParameter("status");
                    
                    TransactionDAO tdao = new TransactionDAO();
                    int transactionId = System.identityHashCode(new Object()) % 10000;
                    Transaction t = new Transaction(transactionId, rentalId, amount, currency,
                            transactionType, LocalDateTime.now(), status, paymentMethod, description);
                    tdao.add(t);
        %>
                    <div class="message success">✓ Transaction added successfully! Redirecting...</div>
                    <script>
                        setTimeout(function() {
                            window.location.href = 'listTransactions.jsp';
                        }, 2000);
                    </script>
        <%
                } catch (Exception e) {
        %>
                    <div class="message error">✗ Error: <%= e.getMessage() %></div>
        <%
                }
            }
        %>
        
        <form method="POST">
            <input type="hidden" name="action" value="add">
            
            <div class="form-row">
                <div class="form-group">
                    <label for="rentalId">Rental ID *</label>
                    <input type="number" id="rentalId" name="rentalId" required>
                </div>
                <div class="form-group">
                    <label for="amount">Amount *</label>
                    <input type="number" id="amount" name="amount" step="0.01" required>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="currency">Currency *</label>
                    <select id="currency" name="currency" required>
                        <option value="">Select Currency</option>
                        <option value="USD">USD (US Dollar)</option>
                        <option value="EUR">EUR (Euro)</option>
                        <option value="INR">INR (Indian Rupee)</option>
                        <option value="GBP">GBP (British Pound)</option>
                        <option value="JPY">JPY (Japanese Yen)</option>
                        <option value="AUD">AUD (Australian Dollar)</option>
                        <option value="CAD">CAD (Canadian Dollar)</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="transactionType">Transaction Type *</label>
                    <select id="transactionType" name="transactionType" required>
                        <option value="">Select Type</option>
                        <option value="PAYMENT">Payment</option>
                        <option value="REFUND">Refund</option>
                        <option value="CHARGE">Charge</option>
                    </select>
                </div>
            </div>
            
            <div class="form-row">
                <div class="form-group">
                    <label for="paymentMethod">Payment Method *</label>
                    <select id="paymentMethod" name="paymentMethod" required>
                        <option value="">Select Payment Method</option>
                        <option value="CREDIT_CARD">Credit Card</option>
                        <option value="DEBIT_CARD">Debit Card</option>
                        <option value="UPI">UPI</option>
                        <option value="NET_BANKING">Net Banking</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="status">Status *</label>
                    <select id="status" name="status" required>
                        <option value="">Select Status</option>
                        <option value="COMPLETED">Completed</option>
                        <option value="PENDING">Pending</option>
                        <option value="FAILED">Failed</option>
                    </select>
                </div>
            </div>
            
            <div class="form-group">
                <label for="description">Description</label>
                <textarea id="description" name="description" rows="3" placeholder="Enter transaction description..."></textarea>
            </div>
            
            <div class="button-group">
                <button type="submit" class="btn btn-submit">Add Transaction</button>
                <a href="listTransactions.jsp" class="btn btn-cancel">Cancel</a>
            </div>
        </form>
    </div>
</body>
</html>
