package com.calendar.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/AppointmentUploader")
@MultipartConfig
public class UploadAppointment extends HttpServlet {

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
        String newAppoYear = request.getParameter("newAppointmentYear");
        String newAppoMonth = request.getParameter("newAppointmentMonth");
        String newAppoDay = request.getParameter("newAppointmentDay");
        String newAppoStr = request.getParameter("newAppointmentString");
        String newAppoHrStr = request.getParameter("newAppointmentHour");
        String newAppoMinStr = request.getParameter("newAppointmentMinute");        
        String newAppoEndHrStr = request.getParameter("newAppointmentEndHour");   
        String newAppoEndMinStr = request.getParameter("newAppointmentEndMinute");   
        
        if (newAppoStr == null || newAppoHrStr == null || newAppoMinStr == null || newAppoEndHrStr == null || newAppoEndMinStr == null) {
        response.getWriter().write("{\"error\": \"Missing required parameters.\"}");
        System.out.println("Null parameters received, exiting");    
        return; // Exit the method if parameters are missing
        }

        int year = Integer.parseInt(newAppoYear);
        String month = newAppoMonth;
        int day = Integer.parseInt(newAppoDay);
        String newAppointment = newAppoStr;
        int hour = Integer.parseInt(newAppoHrStr);
        int minute = Integer.parseInt(newAppoMinStr);
        int endHour = Integer.parseInt(newAppoEndHrStr);
        int endMin = Integer.parseInt(newAppoEndMinStr);

        // SQL query to injectdata on the DB
    String sqlQuery = "INSERT INTO calendar_db (\"selectedYear\", \"selectedMonth\", \"selectedDay\", \"calendarHour\", \"calendarMinute\", \"endHour\", \"endMinute\", \"calendarAppointment\") VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

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
                statement.setInt(5, minute);
                statement.setInt(6, endHour);
                statement.setInt(7, endMin);
                statement.setString(8, newAppointment);
                // Execute the query and retrieve the result
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("New appointment uploaded successfully.");
                } else {
                    System.out.println("Failed to upload the new appointment.");
                }
                
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"PostgreSQL JDBC Driver not found.\"}");
        } catch (SQLException e) {
            e.printStackTrace();
            response.getWriter().write("{\"error\": \"Database connection failed: " + e.getMessage() + "\"}");
        } catch (NumberFormatException e) {
            response.getWriter().write("{\"error\": \"Invalid number format.\"}");
            return;
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
