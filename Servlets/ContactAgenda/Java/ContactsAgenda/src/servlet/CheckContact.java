package servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import org.json.JSONArray;
import org.json.JSONObject;


@WebServlet("/CheckDBContact")
@MultipartConfig
public class CheckContact extends HttpServlet {

    // This method will handle POST requests
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
    }

    // This method will handle GET requests
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);  // Use the same logic for both GET and POST
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setStatus(HttpServletResponse.SC_OK);
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

        // SQL query to check if an appointment exists
        String sqlQuery = "SELECT * FROM contact_card ORDER BY second_name ASC;";
        try {	
            // Load PostgreSQL JDBC Driver
            Class.forName("org.postgresql.Driver");
            // Establish connection and execute the query
            try (Connection connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
                    PreparedStatement statement = connection.prepareStatement(sqlQuery)) {
                // Execute the query and retrieve the result
                ResultSet resultSet = statement.executeQuery();
                // Send the result back as JSON
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                JSONArray jsonArray = new JSONArray();
      
                while (resultSet.next()) {
                    JSONObject json = new JSONObject();
                    json.put("postalCodeDB",  resultSet.getString("postal_code"));
                    json.put("addressDB",  resultSet.getString("address"));
                    json.put("companyDB",  resultSet.getString("company_name"));
                    json.put("lastNameDB",  resultSet.getString("second_name"));
                    json.put("firstNameDB",  resultSet.getString("first_name"));
                    json.put("telephoneDB",  resultSet.getString("phone_number"));
                    jsonArray.put(json);
                }
                String jsonString = jsonArray.toString();
                System.out.println("JSON Output: " + jsonString);
                out.print(jsonArray.toString());
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
