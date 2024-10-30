import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class PostgreSqlTest {

    /**
     * @param args
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {


//////////////////////////////////////////////////////////////////////////////////////////////////////////
//Funcionando 100% desde aqui
//////////////////////////////////////////////////////////////////////////////////////////////////////////
        
        Properties propFile = new Properties();
        FileInputStream sourceLocation = new FileInputStream("C:\\Tool\\Properties\\postgreSqlTest.properties");
        propFile.load(sourceLocation);
        sourceLocation.close();
        String url = propFile.getProperty("jdbc.url");
        String user = propFile.getProperty("jdbc.username");
        String password = propFile.getProperty("jdbc.password");
        
        Scanner readInput = new Scanner(System.in);
        String inputString = "";

    
        try { 
            Connection conn = DriverManager.getConnection(url, user, password);
            if (conn != null) { 
                System.out.println("connection created");
            } else { 
                System.out.println("connection failed");
            }
            String insertQuery = "INSERT INTO employees (first_name, last_name) VALUES (?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            //
            System.out.println("mete el primer dato");
            readInput = new Scanner(System.in);
            inputString = readInput.nextLine();
            preparedStatement.setString(1,  inputString);
            //
            System.out.println("mete el segundo dato");
            readInput = new Scanner(System.in);
            inputString = readInput.nextLine();
            preparedStatement.setString(2,  inputString);
            //
            System.out.println("se acaba de introducir el segundo dato");
            //
            preparedStatement.executeUpdate();
            //
            System.out.println("se acaba de ejecutar el execute update");
            //
            preparedStatement.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }




//////////////////////////////////////////////////////////////////////////////////////////////////////////
//Funcionando 100% hasta aqui
//////////////////////////////////////////////////////////////////////////////////////////////////////////


    }
}

