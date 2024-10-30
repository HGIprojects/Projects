import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DbOperations {

    static ResourceBundle bundle = ResourceBundle.getBundle("messages");
    static ResourceBundle postgresBundle = ResourceBundle.getBundle("contactAgendaDb");
    

    /** exports data to the DB as a new row
     * @param contactsInformation information that will be sent
     * @throws IOException in case there is a problem sending the data
     */
    public static void contactsExporterDB(ContactsAgenda contactsInformation) throws IOException {

        String url = postgresBundle.getString("jdbc.url"); 
        String user = postgresBundle.getString("jdbc.username"); 
        String password = postgresBundle.getString("jdbc.password"); 


        try {
            Connection databaseConnection = DriverManager.getConnection(url, user, password);
            if (databaseConnection != null) {
                System.out.println(bundle.getString("dbConnect"));
            } else {
                System.out.println(bundle.getString("dbDisconnect"));
            }
            String insertQuery = "INSERT INTO contact_card_size (postal_code, address, company_name, second_name, first_name, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(insertQuery);

            preparedStatement.setString(1, contactsInformation.postalCode);
            preparedStatement.setString(2, contactsInformation.address);
            preparedStatement.setString(3, contactsInformation.companyName);
            preparedStatement.setString(4, contactsInformation.secondName);
            preparedStatement.setString(5, contactsInformation.firstName);
            preparedStatement.setString(6, contactsInformation.phoneNumber);
            System.out.println(bundle.getString("updatingDB"));
            preparedStatement.executeUpdate();
            preparedStatement.close();
            databaseConnection.close();
            System.out.println(bundle.getString("updatedDB"));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    
    /** updates a specific row
     * @param contactsInformation in order to know what row to update
     * @throws IOException required when working with DB
     */
    public static void contactsUpdaterDB(ContactsAgenda contactsInformation, int rowIdentifier) throws IOException {

        String url = postgresBundle.getString("jdbc.url"); 
        String user = postgresBundle.getString("jdbc.username"); 
        String password = postgresBundle.getString("jdbc.password"); 

        try {
            Connection databaseConnection = DriverManager.getConnection(url, user, password);
            if (databaseConnection != null) {
                System.out.println(bundle.getString("dbConnect"));
            } else {
                System.out.println(bundle.getString("dbDisconnect"));
            }
            String updateQuery = "UPDATE contact_card_size SET postal_code = ?, address = ?, company_name = ?, second_name = ?, first_name = ?, phone_number = ? WHERE id =" + rowIdentifier + ";";
            PreparedStatement preparedStatement = databaseConnection.prepareStatement(updateQuery);

            preparedStatement.setString(1, contactsInformation.postalCode);
            preparedStatement.setString(2, contactsInformation.address);
            preparedStatement.setString(3, contactsInformation.companyName);
            preparedStatement.setString(4, contactsInformation.secondName);
            preparedStatement.setString(5, contactsInformation.firstName);
            preparedStatement.setString(6, contactsInformation.phoneNumber);
            System.out.println(bundle.getString("updatingDB"));
            int i = preparedStatement.executeUpdate();
            preparedStatement.close();
            databaseConnection.close();
            if (i > 0) {
                System.out.println(bundle.getString("updatedDB"));
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /** gets data from database
     * @param contactsInformationList loads information from DB into this
     * @return returns the obtained information
     * @throws IOException in case there is a problem witht the connection
     * @throws SQLException in case there is a problem with the DB
     */
    public static ContactsAgenda contactsPullDB(List<ContactsAgenda> contactsInformationList) throws IOException, SQLException  {
        
        List<String> bufferArrayList = new ArrayList<String>();
        String buffer = "";
        String intToString = "";
        ContactsAgenda contactsAgenda = new ContactsAgenda();

        String url = postgresBundle.getString("jdbc.url"); 
        String user = postgresBundle.getString("jdbc.username"); 
        String password = postgresBundle.getString("jdbc.password");
        String selectQuery = "SELECT id, postal_code, address, company_name, second_name, first_name, phone_number\r\n"
                + " FROM public.contact_card_size;"; 
        Connection databaseConnection = DriverManager.getConnection(url, user, password);
        Statement stmt = databaseConnection.createStatement();     
        if (databaseConnection != null) {
            System.out.println(bundle.getString("dbConnect"));
        } else {
            System.out.println(bundle.getString("dbDisconnect"));
        }
        ResultSet rs = stmt.executeQuery(selectQuery);

        while (rs.next()) {
            intToString = Integer.toString(rs.getInt("id"));
            buffer = intToString;
            buffer = buffer.concat(";");
            buffer = buffer.concat(rs.getString("postal_code"));
            buffer = buffer.concat(";");
            buffer = buffer.concat(rs.getString("address"));
            buffer = buffer.concat(";");
            buffer = buffer.concat(rs.getString("company_name"));
            buffer = buffer.concat(";");
            buffer = buffer.concat(rs.getString("second_name"));
            buffer = buffer.concat(";");
            buffer = buffer.concat(rs.getString("first_name"));
            buffer = buffer.concat(";");
            buffer = buffer.concat(rs.getString("phone_number"));                     
            bufferArrayList.add(buffer);
        }
        databaseConnection.close();
        for (int k = 0; k < bufferArrayList.size(); k++) {
            contactsInformationList.add(Tools.extractDataWithArrayString(bufferArrayList.get(k)));
        }
        return contactsAgenda;
    }
    
    
    /** deletes from DB
     * @param identifier in order to know what item should be deleted
     * @throws IOException in case there is a problem witht the connection
     * @throws SQLException in case there is a problem with the DB
     */
    public static void contactDeleteDb(int identifier) throws SQLException, IOException {

        String url = postgresBundle.getString("jdbc.url"); 
        String user = postgresBundle.getString("jdbc.username"); 
        String password = postgresBundle.getString("jdbc.password");

        Connection databaseConnection = DriverManager.getConnection(url, user, password);
        if (databaseConnection != null) {
            System.out.println(bundle.getString("dbConnect"));
        } else {
            System.out.println(bundle.getString("dbDisconnect"));
        }
        String deleteQuery = "DELETE FROM public.contact_card_size WHERE id =" + identifier + ";";
        PreparedStatement preparedStatement = databaseConnection.prepareStatement(deleteQuery);
        preparedStatement.executeUpdate();    
        preparedStatement.close();
        databaseConnection.close();    
    } 
    
    
}
