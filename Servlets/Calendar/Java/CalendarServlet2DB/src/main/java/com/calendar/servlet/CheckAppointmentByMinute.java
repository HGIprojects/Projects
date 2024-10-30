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

import org.json.JSONObject;

@WebServlet("/CheckDBAppointmentMinute")
@MultipartConfig

public class CheckAppointmentByMinute extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        // Add CORS headers to allow the request from any origin
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        
        String jdbcURL = "jdbc:postgresql://localhost:5432/bbdd";
        String dbUser = "postgres";
        String dbPassword = "postgres";
        
        int year = Integer.parseInt(request.getParameter("urlYear"));
        String month = request.getParameter("urlMonth");
        int day = Integer.parseInt(request.getParameter("urlDay"));
        int hour = Integer.parseInt(request.getParameter("hour"));
        int minute = Integer.parseInt(request.getParameter("minute"));
        /*
        int year = 2024;
        String month = "October";
        int day = 12;
        int hour = 9;
        int minute = 0;
        */
        
        boolean minuteExists = false;
        String sqlQuery = "SELECT EXISTS (SELECT 1 FROM calendar_db WHERE \"selectedYear\" = ? AND \"selectedMonth\" = ? AND \"selectedDay\" = ? AND \"calendarHour\" = ?  AND \"calendarMinute\" = ?)";
        String appointmentSqlQuery = "SELECT \"calendarAppointment\", \"endHour\", \"endMinute\" FROM calendar_db WHERE \"selectedYear\" = ? AND \"selectedMonth\" = ? AND \"selectedDay\" = ? AND \"calendarHour\" = ?  AND \"calendarMinute\" = ?";
        String appointmentSqlString = "";
        int endingHour = 0;
        int endingMinute = 0;
        
        try {
        Class.forName("org.postgresql.Driver");
            try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
                PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
    
                statement.setInt(1, year);
                statement.setString(2, month);
                statement.setInt(3, day);
                statement.setInt(4, hour);
                statement.setInt(5, minute);
    
                ResultSet resultSet = statement.executeQuery();
    
                if (resultSet.next()) {
                    minuteExists = resultSet.getBoolean(1);
                }
                    
                if (minuteExists) {
                    //hacer un pull de la base de datos y dar valor a appointmentSql
                    try (PreparedStatement appointmentStatement = connection.prepareStatement(appointmentSqlQuery)) {
                        appointmentStatement.setInt(1, year);
                        appointmentStatement.setString(2, month);
                        appointmentStatement.setInt(3, day);
                        appointmentStatement.setInt(4, hour);
                        appointmentStatement.setInt(5, minute);

                        ResultSet appointmentResultSet = appointmentStatement.executeQuery();
                        if (appointmentResultSet.next()) {
                            appointmentSqlString = appointmentResultSet.getString("calendarAppointment");
                            endingHour = appointmentResultSet.getInt("endHour");
                            endingMinute = appointmentResultSet.getInt("endMinute");
                        }
                    }
                }
                
                JSONObject json = new JSONObject();
                json.put("minuteExists", minuteExists);
                // hace falta convertir estos dos de abajo a string??
                json.put("endingHour", endingHour);
                json.put("endingMinute", endingMinute);
                json.put("appointmentSqlString", appointmentSqlString);
                response.setContentType("application/json");
                response.getWriter().write(json.toString());
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