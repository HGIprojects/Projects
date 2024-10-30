package servlet;

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


@WebServlet("/NewDBContact")
@MultipartConfig
public class CreateContact extends HttpServlet {

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
        String jdbcURL = "jdbc:postgresql://localhost:5432/contactsdb2";
        String dbUser = "postgres";
        String dbPassword = "postgres";

        System.out.println("Ahora los datos nuevos");
        String newPostalCode = request.getParameter("newPostalCode");
        System.out.println(newPostalCode);
        String newAddress = request.getParameter("newAddress");
        System.out.println(newAddress);
        String newCompany = request.getParameter("newCompany");
        System.out.println(newCompany);
        String newLastName = request.getParameter("newLastName");
        System.out.println(newLastName);
        String newFirstName = request.getParameter("newFirstName");
        System.out.println(newFirstName);
        String newPhone = request.getParameter("newPhone");        
        System.out.println(newPhone);
        
        if (newPostalCode == null || newAddress == null || newCompany == null || newLastName == null || newFirstName == null || newPhone == null) {
            response.getWriter().write("{\"error\": \"Missing required parameters.\"}");
            System.out.println("Null parameters received, exiting");    
            return; // Exit the method if parameters are missing
        }


        // SQL query to update data on the DB
        String sqlQuery = "INSERT INTO contact_card (\"postal_code\", \"address\", \"company_name\", \"second_name\", \"first_name\", \"phone_number\") VALUES (?, ?, ?, ?, ?, ?)";

        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Establish connection and execute the query
            try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                // Set parameters for the query
               
                statement.setString(1, newPostalCode);
                statement.setString(2, newAddress);
                statement.setString(3, newCompany);
                statement.setString(4, newLastName);
                statement.setString(5, newFirstName);
                statement.setString(6, newPhone);
                                
                // Execute the query and retrieve the result
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Contact created successfully.");
                } else {
                    System.out.println("Failed to create the contact.");
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
