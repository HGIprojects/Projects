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


@WebServlet("/ModifyDBContact")
@MultipartConfig
public class ModifyContact extends HttpServlet {

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
        
        String oldCompany = request.getParameter("oldCompany");
        String oldPostalCode = request.getParameter("oldPostalCode");
        String oldAddress = request.getParameter("oldAddress");
        String oldPhone = request.getParameter("oldPhone");
        String oldLastName = request.getParameter("oldLastName");
        String oldFirstName = request.getParameter("oldFirstName");        
        String modPostalCode = request.getParameter("modifiedPostalCode");
        String modAddress = request.getParameter("modifiedAddress");
        String modCompany = request.getParameter("modifiedCompany");
        String modLastName = request.getParameter("modifiedLastName");
        String modFirstName = request.getParameter("modifiedFirstName");
        String modPhone = request.getParameter("modifiedPhone");        
        
        if (oldCompany == null || oldPostalCode == null || oldAddress == null || oldPhone == null || oldLastName == null || oldFirstName == null 
                || modPostalCode == null || modAddress == null || modCompany == null || modLastName == null || modFirstName == null || modPhone == null) {
            response.getWriter().write("{\"error\": \"Missing required parameters.\"}");
            System.out.println("Null parameters received, exiting");    
            return; // Exit the method if parameters are missing
        }


        // SQL query to update data on the DB
        String sqlQuery = "UPDATE contact_card SET \"postal_code\" = ?, \"address\" = ?, \"company_name\" = ?, \"second_name\" = ?, "
                + "\"first_name\" = ?, \"phone_number\" = ? WHERE (\"postal_code\" = ? AND \"address\" = ? AND \"company_name\" = ? "
                + "AND \"second_name\" = ? AND \"first_name\" = ? AND \"phone_number\" = ?)";

        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Establish connection and execute the query
            try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                // Set parameters for the query
               
                statement.setString(1, modPostalCode);
                statement.setString(2, modAddress);
                statement.setString(3, modCompany);
                statement.setString(4, modLastName);
                statement.setString(5, modFirstName);
                statement.setString(6, modPhone);
                statement.setString(7, oldPostalCode);
                statement.setString(8, oldAddress);            
                statement.setString(9, oldCompany);
                statement.setString(10, oldLastName);
                statement.setString(11, oldFirstName);
                statement.setString(12, oldPhone);

                
                // Execute the query and retrieve the result
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Contact updated successfully.");
                } else {
                    System.out.println("Failed to update the contact.");
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
