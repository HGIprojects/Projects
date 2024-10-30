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


@WebServlet("/AppointmentModification")
@MultipartConfig
public class ModifyAppointment extends HttpServlet {

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
        
        String oldYearStr = request.getParameter("urlYear");
        String oldMonthStr = request.getParameter("urlMonth");
        String oldDayStr = request.getParameter("urlDay");
        String oldStr = request.getParameter("oldString");
        String oldHrStr = request.getParameter("oldHour");
        String oldMinStr = request.getParameter("oldMinute");        
        String oldEndHrStr = request.getParameter("oldEndHour");   
        String oldEndMinStr = request.getParameter("oldEndMinute");   
        
        String modYearStr = request.getParameter("newAppointmentYear");
        String modMonthStr = request.getParameter("newAppointmentMonth");
        String modDayStr = request.getParameter("newAppointmentDay");
        String modStr = request.getParameter("newAppointmentString");
        String modHrStr = request.getParameter("newAppointmentHour");
        String modMinStr = request.getParameter("newAppointmentMinute");        
        String modEndHrStr = request.getParameter("newAppointmentEndHour");   
        String modEndMinStr = request.getParameter("newAppointmentEndMinute");   
        
        if (oldYearStr == null || oldMonthStr == null || oldDayStr == null || oldStr == null || oldHrStr == null || oldMinStr == null || oldEndHrStr == null || oldEndMinStr == null || modYearStr == null || modMonthStr == null || modDayStr == null || modStr == null || modHrStr == null || modMinStr == null || modEndHrStr == null || modEndMinStr == null) {
        response.getWriter().write("{\"error\": \"Missing required parameters.\"}");
        System.out.println("Null parameters received, exiting");    
        return; // Exit the method if parameters are missing
        }
        
        int oldYear = Integer.parseInt(oldYearStr);
        String oldMonth = oldMonthStr;
        int oldDay = Integer.parseInt(oldDayStr);
        String oldAppointment = oldStr;
        int oldHour = Integer.parseInt(oldHrStr);
        int oldMinute = Integer.parseInt(oldMinStr);
        int oldEndHour = Integer.parseInt(oldEndHrStr);
        int oldEndMin = Integer.parseInt(oldEndMinStr);
       
        int newYear = Integer.parseInt(modYearStr);
        String newMonth = modMonthStr;
        int newDay = Integer.parseInt(modDayStr);
        String newAppointment = modStr;
        int newHour = Integer.parseInt(modHrStr);
        int newMinute = Integer.parseInt(modMinStr);
        int newEndHour = Integer.parseInt(modEndHrStr);
        int newEndMin = Integer.parseInt(modEndMinStr);
        

        // SQL query to update data on the DB
    String sqlQuery = "UPDATE calendar_db SET \"selectedYear\" = ?, \"selectedMonth\" = ?, \"selectedDay\" = ?, \"calendarHour\" = ?, \"calendarMinute\" = ?, \"endHour\" = ?, \"endMinute\" = ?, \"calendarAppointment\" = ? WHERE (\"selectedYear\" = ? AND \"selectedMonth\" = ? AND \"selectedDay\" = ? AND \"calendarHour\" = ? AND \"calendarMinute\" = ? AND \"endHour\" = ? AND \"endMinute\" = ? AND \"calendarAppointment\" = ?)";

        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Establish connection and execute the query
            try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                // Set parameters for the query
               
                statement.setInt(1, newYear);
                statement.setString(2, newMonth);
                statement.setInt(3, newDay);
                statement.setInt(4, newHour);
                statement.setInt(5, newMinute);
                statement.setInt(6, newEndHour);
                statement.setInt(7, newEndMin);
                statement.setString(8, newAppointment);
                
                statement.setInt(9, oldYear);
                statement.setString(10, oldMonth);
                statement.setInt(11, oldDay);
                statement.setInt(12, oldHour);
                statement.setInt(13, oldMinute);
                statement.setInt(14, oldEndHour);
                statement.setInt(15, oldEndMin);
                statement.setString(16, oldAppointment); 
                
                // Execute the query and retrieve the result
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Appointment updated successfully.");
                } else {
                    System.out.println("Failed to update the appointment.");
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
