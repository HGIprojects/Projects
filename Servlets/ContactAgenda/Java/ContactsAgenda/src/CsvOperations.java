import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class CsvOperations {

    static ResourceBundle bundle = ResourceBundle.getBundle("messages");
    static ResourceBundle bundle2 = ResourceBundle.getBundle("contactAgendaDb");

    
    /** exports information to a CSV file in the specified path
     * @param contactsInformationList requires the list of contacts to store them in a CSV file
     * @throws IOException in case the path is incorrect or unavailable
     */
    public static void exportToFile(List<ContactsAgenda> contactsInformationList) throws IOException {
        ContactsAgenda bufferContact = new ContactsAgenda();
        String path = bundle2.getString("path");//"C:\\Users\\ghugo\\Documents\\Onboarding\\Ejercicios\\FileExports\\ContactsAgendaBackup\\";
        String fullPath = "";
        String csvString = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String formattedDate = "";
        String fileName = "";
        String fileFormat = "";
        Date date = new Date();
        String header = bundle.getString("csvHeader");

        try {
            fileFormat = ".CSV";
            formattedDate = formatter.format(date);
            fileName = "contactsBackupCSV_" + formattedDate + fileFormat;
            fullPath = path.concat(fileName);

            File file = new File(fullPath);
            FileOutputStream j = new FileOutputStream(file);
            OutputStreamWriter outputStreamer = new OutputStreamWriter(j, StandardCharsets.UTF_8);
            outputStreamer.write(header);
            outputStreamer.write("\n");
            for (int i = 0; i < contactsInformationList.size(); i++) {
                bufferContact = contactsInformationList.get(i);
                List<String> myList = Arrays.asList(Integer.toString(bufferContact.identifier), bufferContact.postalCode, bufferContact.address,
                        bufferContact.companyName, bufferContact.secondName, bufferContact.firstName,
                        bufferContact.phoneNumber);
                csvString = String.join(";", myList);
                outputStreamer.write(csvString);
                outputStreamer.write("\n");
            }
            System.out.println(bundle.getString("file") + fileName + bundle.getString("stored"));
            System.out.println(path);
            System.out.println("");
            outputStreamer.flush();
            outputStreamer.close();

        } catch (Exception e) {
            System.out.println(bundle.getString("error2"));
            System.out.println("");
            e.getStackTrace();
        }
    }
 
    
    /** will grab information from a CSV file that has semi-colons as separatos (;)
     * @param contactsInformationList receives list of contacts so that it can store information in it
     * @throws FileNotFoundException in case the path found in "path" is incorrect or unavailable
     */
    public static void importFromFile(List<ContactsAgenda> contactsInformationList) throws FileNotFoundException {
        
        String path = bundle2.getString("path"); //"C:\\Users\\ghugo\\Documents\\Onboarding\\Ejercicios\\FileExports\\ContactsAgendaBackup\\";
        Scanner csvRead = new Scanner(Tools.getLastModified(path));
        String csvHeader = "";
        csvHeader = csvRead.nextLine();
        while (csvRead.hasNext()) {
            
            contactsInformationList.add(Tools.extractDataFromCsvWithScanner(csvRead));
        }
    }
    

    

    
    

    
    
}
