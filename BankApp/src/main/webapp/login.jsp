<html>
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 400px;
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
        .form-group input {
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
        .error {
            color: red;
            margin-top: 10px;
        }
        .success {
            color: green;
            margin-top: 10px;
        }
    </style>
    <script>
        function validateForm(event) {
            var username = document.getElementById("username").value;
            var password = document.getElementById("password").value;
            
            if (!username || !password) {
                document.getElementById("error").innerText = "Username or Password cannot be empty.";
                event.preventDefault();
                return false;
            }
            return true;
        }
    </script>
</head>
<body>
    <div class="container">
        <h2>Login</h2>
        <form action="login" method="post" onsubmit="return validateForm(event)">
            <div class="form-group">
                <label for="username">Username: </label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">Password: </label>
                <input type="password" id="password" name="password" required>
            </div>
            <div class="form-group">
                <input type="submit" value="Login">
            </div>
            <div id="error" class="error">
                <% if (request.getAttribute("error") != null) { %>
                    <%= request.getAttribute("error") %>
                <% } %>
            </div>
            <div id="success" class="success">
                <% if (request.getAttribute("success") != null) { %>
                    <%= request.getAttribute("success") %>
                <% } %>
            </div>
        </form>
    </div>
</body>
</html>
