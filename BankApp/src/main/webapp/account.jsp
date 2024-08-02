<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Account Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background: #fff;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h2 {
            margin-top: 0;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group label {
            display: block;
            margin-bottom: 5px;
        }
        .form-group input, .form-group select {
            width: 100%;
            padding: 8px;
            box-sizing: border-box;
        }
        .form-group input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
        }
        .form-group input[type="submit"]:hover {
            background-color: #45a049;
        }
        .error, .success {
            color: red;
            margin-top: 10px;
        }
        .success {
            color: green;
        }
        .logout-link {
            display: block;
            margin-top: 20px;
            text-align: center;
        }
        .logout-link a {
            text-decoration: none;
            color: #fff;
            background-color: #f44336;
            padding: 10px 20px;
            border-radius: 5px;
            font-weight: bold;
        }
        .logout-link a:hover {
            background-color: #d32f2f;
        }
    </style>
</head>
<body>
    <div class="container">
        <h2>Account Details</h2>
        <p><strong>First Name:</strong> <%= request.getAttribute("firstName") %></p>
        <p><strong>Last Name:</strong> <%= request.getAttribute("lastName") %></p>
        <p><strong>Email:</strong> <%= request.getAttribute("email") %></p>
        <p><strong>Mobile Number:</strong> <%= request.getAttribute("mobile") %></p>
        <p><strong>Account Number:</strong> <%= request.getAttribute("accountNumber") %></p>
        <p><strong>Account Type:</strong> <%= request.getAttribute("accountType") %></p>
        <p><strong>Balance:</strong> Rs.<%= request.getAttribute("balance") %></p>
        
        <form action="account" method="post">
            <div class="form-group">
                <label for="action">Action:</label>
                <select id="action" name="action" required>
                    <option value="Deposit">Deposit</option>
                    <option value="Withdrawal">Withdrawal</option>
                </select>
            </div>
            <div class="form-group">
                <label for="amount">Amount:</label>
                <input type="number" id="amount" name="amount" step="0.01" min="0" required>
            </div>
            <div class="form-group">
                <input type="submit" value="Submit">
            </div>
            <div id="message">
                <% if (request.getAttribute("success") != null) { %>
                    <p class="success"><%= request.getAttribute("success") %></p>
                <% } %>
                <% if (request.getAttribute("error") != null) { %>
                    <p class="error"><%= request.getAttribute("error") %></p>
                <% } %>
            </div>
        </form>
        
        <h2>Update PIN</h2>
        <form action="account" method="post">
            <div class="form-group">
                <input type="hidden" name="action" value="updatePin">
                <label for="oldPin">Old PIN:</label>
                <input type="password" id="oldPin" name="oldPin" required>
            </div>
            <div class="form-group">
                <label for="newPin">New PIN:</label>
                <input type="password" id="newPin" name="newPin" title="Enter a 4-digit New PIN">
            </div>
            <div class="form-group">
                <input type="submit" value="Update PIN">
            </div>
        </form>
        
        <div class="logout-link">
            <a href="logout">Logout</a>
        </div>
    </div>
</body>
</html>
