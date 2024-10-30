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


@WebServlet("/DeleteDBContact")
@MultipartConfig
public class DeleteContact extends HttpServlet {

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
        String delCompany = request.getParameter("deleteCompany");
        String delPostalCode = request.getParameter("deletePostalCode");
        String delAddress = request.getParameter("deleteAddress");
        String delTelephone = request.getParameter("deleteTelephone");
        String delLastName = request.getParameter("deleteLastName");
        String delFirstName = request.getParameter("deleteFirstName");        
                
        
        if (delCompany == null || delPostalCode == null || delAddress == null || delTelephone == null || delLastName == null || delFirstName == null) {
            response.getWriter().write("{\"error\": \"Missing required parameters.\"}");
            System.out.println("Null parameters received, exiting");    
            return; // Exit the method if parameters are missing
        }


        // SQL query to delete data on the DB
        String sqlQuery = "DELETE FROM contact_card WHERE (\"postal_code\" = ? AND \"address\" = ? AND \"company_name\" = ? "
                + "AND \"second_name\" = ? AND \"first_name\" = ? AND \"phone_number\" = ?)";

        try {
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Establish connection and execute the query
            try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
                 PreparedStatement statement = connection.prepareStatement(sqlQuery)) {

                // Set parameters for the query
                statement.setString(1, delPostalCode);
                statement.setString(2, delAddress);
                statement.setString(3, delCompany);
                statement.setString(4, delLastName);
                statement.setString(5, delFirstName);
                statement.setString(6, delTelephone);
                
                // Execute the query and retrieve the result
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Contact deleted successfully.");
                } else {
                    System.out.println("Failed to delete the appointment.");
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
