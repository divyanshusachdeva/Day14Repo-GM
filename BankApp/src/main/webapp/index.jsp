<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Banking Application</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
            text-align: center;
        }
        .container {
            max-width: 400px;
            margin: 0 auto;
            padding: 20px;
            background: #fff;
            border-radius: 5px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
        }
        h1 {
            margin-top: 0;
        }
        .form-group {
            margin-bottom: 15px;
        }
        .form-group input[type="submit"] {
            background-color: #4CAF50;
            color: white;
            border: none;
            cursor: pointer;
            padding: 10px 20px;
            border-radius: 5px;
        }
        .form-group input[type="submit"]:hover {
            background-color: #45a049;
        }
        .links {
            margin-top: 20px;
        }
        .links a {
            text-decoration: none;
            color: #4CAF50;
            font-weight: bold;
            padding: 10px;
            display: inline-block;
        }
        .links a:hover {
            color: #45a049;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Welcome to the Banking Application</h1>
        <p>Please choose one of the following options: </p>
        <div class="links">
            <a href="login.jsp">Login</a>
            <a href="register.jsp">Register</a>
        </div>
    </div>
</body>
</html>
