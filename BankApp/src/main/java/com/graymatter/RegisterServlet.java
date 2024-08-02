package com.graymatter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String url = "jdbc:mysql://localhost:3306/bankdb";
    private static final String user = "root";
    private static final String pass = "password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String mobile = request.getParameter("mobile");
        String accountType = request.getParameter("accountType");
        String password = request.getParameter("password");
        String pin = request.getParameter("pin");


        if (password == null || password.isEmpty()) {
            request.setAttribute("error", "Password is required.");
            request.getRequestDispatcher("register.jsp").forward(request, response);
            return;
        }

        String accountNumber = "HDFC" + (int) (Math.random() * 1000000);

        String query = "INSERT INTO users (first_name, last_name, username, email, mobile, account_number, account_type, balance, pin, password) VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DriverManager.getConnection(url, user, pass);
             PreparedStatement ps = con.prepareStatement(query)) {

        	ps.setString(1, firstName);
            ps.setString(2, lastName);
            ps.setString(3, username);
            ps.setString(4, email);
            ps.setString(5, mobile);
            ps.setString(6, accountNumber);
            ps.setString(7, accountType);
            ps.setDouble(8, 0.00);
            ps.setString(9, pin);
            ps.setString(10, password);

            int rowsInserted = ps.executeUpdate();
            if (rowsInserted > 0) {
                response.sendRedirect("login.jsp");
            } else {
                request.setAttribute("error", "Registration failed. Please try again.");
                request.getRequestDispatcher("register.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database access error", e);
        }
    }
}
