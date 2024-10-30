import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

public class PostgreSqlTest2 {

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
        Scanner languageScan = new Scanner(System.in);
        String languageString = "";
        Properties propFile = new Properties();
        FileInputStream sourceLocation = new FileInputStream("C:\\Tool\\Properties\\postgreSqlTest2.properties");
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
            String insertQuery = "INSERT INTO employees2 (id, first_name2, last_name2) VALUES (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
            //
            System.out.println("input id");
            readInput = new Scanner(System.in);
            int inputInteger = readInput.nextInt();
            preparedStatement.setInt(1,  inputInteger);
            //
            System.out.println("input first_name");
            readInput = new Scanner(System.in);
            inputString = readInput.nextLine();
            preparedStatement.setString(2,  inputString);
            //
            System.out.println("input last_name");
            readInput = new Scanner(System.in);
            inputString = readInput.nextLine();
            preparedStatement.setString(3,  inputString);
            //
            System.out.println("last_name input now, proceedint to executeUpdate");
            //
            preparedStatement.executeUpdate();
            //
            System.out.println("executeUpdate updated correctly");
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

