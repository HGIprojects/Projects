package com.calendar.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/CheckDatabaseConnection")
public class CheckPostgresConnection extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String jdbcURL = "jdbc:postgresql://localhost:5432/bbdd";
        String dbUser = "postgres";
        String dbPassword = "postgres";

        try {
            Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword)) {
                response.setContentType("text/plain");
                response.getWriter().write("Database connection successful!");
            } catch (SQLException e) {
                response.setContentType("text/plain");
                response.getWriter().write("Database connection failed: " + e.getMessage());
                e.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            response.setContentType("text/plain");
            response.getWriter().write("PostgreSQL JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 
