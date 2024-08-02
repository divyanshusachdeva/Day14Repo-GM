package com.graymatter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/account")
public class AccountServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String url = "jdbc:mysql://localhost:3306/bankdb";
    private static final String user = "root";
    private static final String pass = "password";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT * FROM users WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, userId);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        request.setAttribute("firstName", rs.getString("first_name"));
                        request.setAttribute("lastName", rs.getString("last_name"));
                        request.setAttribute("email", rs.getString("email"));
                        request.setAttribute("mobile", rs.getString("mobile"));
                        request.setAttribute("accountNumber", rs.getString("account_number"));
                        request.setAttribute("accountType", rs.getString("account_type"));
                        request.setAttribute("balance", rs.getDouble("balance"));
                    } else {
                        request.setAttribute("error", "User not found.");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database access error", e);
        }

        request.getRequestDispatcher("account.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int userId = (Integer) session.getAttribute("userId");
        String action = request.getParameter("action");
        double amount = 0.00;
        
        try {
            if (request.getParameter("amount") != null) {
                amount = Double.parseDouble(request.getParameter("amount"));
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount format.");
            request.getRequestDispatcher("account.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            if ("deposit".equalsIgnoreCase(action)) 
            	depositFunction(request, response, userId);
     
            else if ("withdrawal".equalsIgnoreCase(action)) 
            	withdrawalFunction(request, response, userId);
               
                
            else if ("updatePin".equalsIgnoreCase(action)) 
            	pinChange(request, response, userId);
            
            
            else 
                request.setAttribute("error", "Invalid action.");
            
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database access error", e);
        }

        doGet(request, response);
    }
    
    
    
    private void depositFunction(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
        String amountStr = request.getParameter("amount");
        double amount = 0.00;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount format.");
            request.getRequestDispatcher("account.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "UPDATE users SET balance = balance + ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setDouble(1, amount);
                ps.setInt(2, userId);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    recordTransaction(conn, userId, "Deposit", amount);
                    request.setAttribute("success", "Deposit successful.");
                } else {
                    request.setAttribute("error", "Deposit failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database access error", e);
        }
    }
    
    private void withdrawalFunction(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
        String amountStr = request.getParameter("amount");
        double amount = 0.00;

        try {
            amount = Double.parseDouble(amountStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount format.");
            request.getRequestDispatcher("account.jsp").forward(request, response);
            return;
        }

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String checkBal = "SELECT balance FROM users WHERE id = ?";
            double currentBalance;
            try (PreparedStatement checkPs = conn.prepareStatement(checkBal)) {
                checkPs.setInt(1, userId);
                try (ResultSet rs = checkPs.executeQuery()) {
                    if (rs.next()) {
                        currentBalance = rs.getDouble("balance");
                        if (currentBalance < amount) {
                            request.setAttribute("error", "Insufficient balance.");
                            request.getRequestDispatcher("account.jsp").forward(request, response);
                            return;
                        }
                    } else {
                        request.setAttribute("error", "User not found.");
                        request.getRequestDispatcher("account.jsp").forward(request, response);
                        return;
                    }
                }
            }

            String query = "UPDATE users SET balance = balance - ? WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setDouble(1, amount);
                ps.setInt(2, userId);

                int rowsUpdated = ps.executeUpdate();
                if (rowsUpdated > 0) {
                    recordTransaction(conn, userId, "Withdrawal", amount);
                    request.setAttribute("success", "Withdrawal successful.");
                } else {
                    request.setAttribute("error", "Withdrawal failed.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database access error", e);
        }
    }
    
    
    private void pinChange(HttpServletRequest request, HttpServletResponse response, int userId) throws ServletException, IOException {
    	String oldPin = request.getParameter("oldPin");
        String newPin = request.getParameter("newPin");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String verifyQuery = "SELECT pin FROM users WHERE id = ?";
            try (PreparedStatement verifyPs = conn.prepareStatement(verifyQuery)) {
                verifyPs.setInt(1, userId);
                try (ResultSet rs = verifyPs.executeQuery()) {
                    if (rs.next()) {
                        String dbPin = rs.getString("pin");
                        if (!dbPin.equals(oldPin)) {
                            request.setAttribute("error", "Incorrect old PIN.");
                            request.getRequestDispatcher("account.jsp").forward(request, response);
                            return;
                        }
                    } else {
                        request.setAttribute("error", "User not found.");
                        request.getRequestDispatcher("account.jsp").forward(request, response);
                        return;
                    }
                }
            }

            String updateQuery = "UPDATE users SET pin = ? WHERE id = ?";
            try (PreparedStatement updatePs = conn.prepareStatement(updateQuery)) {
                updatePs.setString(1, newPin);
                updatePs.setInt(2, userId);

                int rowsUpdated = updatePs.executeUpdate();
                if (rowsUpdated > 0) {
                    request.setAttribute("success", "PIN updated successfully.");
                } else {
                    request.setAttribute("error", "Failed to update PIN.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database access error", e);
        }
    }

    
    private void recordTransaction(Connection conn, int userId, String type, double amount) throws SQLException {
        String query = "INSERT INTO transactions (user_id, transaction_type, amount) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            ps.executeUpdate();
        }
    }
}
