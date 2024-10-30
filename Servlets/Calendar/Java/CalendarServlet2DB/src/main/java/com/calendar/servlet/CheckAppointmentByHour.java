package com.calendar.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/CheckDBAppointmentHour")
@MultipartConfig
public class CheckAppointmentByHour extends HttpServlet {

    // This method will handle POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    // This method will handle GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);  // Use the same logic for both GET and POST
    }

    // Common method for handling both POST and GET requests
    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Add CORS headers to allow the request from any origin
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);

        // Database connection parameters
        String jdbcURL = "jdbc:postgresql://localhost:5432/bbdd";
        String dbUser = "postgres";
        String dbPassword = "postgres";
        String urlYearString = request.getParameter("urlYear");
        String urlMonthString = request.getParameter("urlMonth");
        String urldayString = request.getParameter("urlDay");        
        String urlHourString = request.getParameter("hour");   
        
        if (urlYearString == null || urlMonthString == null || urldayString == null || urlHourString == null) {
        response.getWriter().write("{\"error\": \"Missing required parameters.\"}");
System.out.println("Null parameters received, exiting");    
        return; // Exit the method if parameters are missing
        }

        int year = Integer.parseInt(urlYearString);
        String month = urlMonthString;
        int day = Integer.parseInt(urldayString);
        int hour = Integer.parseInt(urlHourString);
    

        // Sample data for testing
/*        int year = 2024;
        String month = "October";
        int day = 12;
        int hour = 9;
*/
        boolean hourExists = false;
        // SQL query to check if an appointment exists
        String sqlQuery = "SELECT EXISTS (SELECT 1 FROM calendar_db WHERE \"selectedYear\" = ? AND \"selectedMonth\" = ? AND \"selectedDay\" = ? AND \"calendarHour\" = ?)";
        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Establish connection and execute the query
            try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                // Set parameters for the query
                statement.setInt(1, year);
                statement.setString(2, month);
                statement.setInt(3, day);
                statement.setInt(4, hour);
                // Execute the query and retrieve the result
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    hourExists = resultSet.getBoolean(1);  // Get the result (1 if exists, 0 if not)
                }
                System.out.println("Appointment exists: " + hourExists);
                // Send the result back as JSON
                response.setContentType("application/json");
                response.getWriter().write("{\"hourExists\": " + (hourExists  ? "true" : "false") + "}");
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"PostgreSQL JDBC Driver not found.\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"Database connection failed: " + e.getMessage() + "\"}");
        }
    }

    // Override doOptions to handle preflight requests for CORS
    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
