import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Testing {


     
    /** the main of Testing.java
     * @param args main 
     * @throws SQLException in case theres an error with the database
     * @throws ClassNotFoundException  exceptino
     * @throws IOException exception
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException, IOException {


//////////////////////////////////////////////////////////////////////////////////////////////////////////
//Funcionando 100% desde aqui
//////////////////////////////////////////////////////////////////////////////////////////////////////////
        String path = "C:\\Users\\ghugo\\Documents\\Onboarding\\Ejercicios\\FileExports\\ContactsAgendaBackup\\";
        System.out.println("vamos a usar este programa para mostrar por pantalla el ultimo archivo CSV en esta ubicacion: ");
        System.out.println(path);
        System.out.println("Llamando al metodo importFromFile! ");
        importfromFile(path);
        System.out.println("Fin! ");



//////////////////////////////////////////////////////////////////////////////////////////////////////////
//Funcionando 100% hasta aqui
//////////////////////////////////////////////////////////////////////////////////////////////////////////

    }
    
    /**
     * @throws FileNotFoundException d
     */
    public static void importfromFile(String path) throws FileNotFoundException {

        Scanner csvRead = new Scanner(getLastModified(path));
        String csvString = "";
        String csvHeader = "";
        
        csvHeader = csvRead.nextLine();
        while (csvRead.hasNext()) {
            csvString = csvRead.nextLine();
            System.out.println("Importando informacion del CSV");
            System.out.println(csvString);
            
        }
    }
    
    /**
     * @param path d
     * @return d
     */
    public static File getLastModified(String path) {
        File directory = new File(path);
        File[] files = directory.listFiles(File::isFile);
        long lastModifiedTime = Long.MIN_VALUE;
        File chosenFile = null;

        if ((files != null) && (files.length > 0)) {
            for (File file : files) {
                if (file.lastModified() > lastModifiedTime) {
                    chosenFile = file;
                    lastModifiedTime = file.lastModified();
                }
            }
        }

        return chosenFile;
    }
}

