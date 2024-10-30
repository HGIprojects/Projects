import java.io.File;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Tools {
    
    static ResourceBundle bundle = ResourceBundle.getBundle("messages");


    
    
    /** method used to measure the total size of the displayed chart in the UI
     * @param contact takes into account the size of the "title" of that row and the length of the stored information
     * @return returns the size to make it look like a rectangle
     */
    public static int tapeMeasurer(ContactsAgenda contact) {
        int maxStringLength = 0;

        if (contact.postalCode.length() > maxStringLength) {
            maxStringLength = contact.postalCode.length();
        }
        if (contact.address.length() > maxStringLength) {
            maxStringLength = contact.address.length();
        }
        if (contact.companyName.length() > maxStringLength) {
            maxStringLength = contact.companyName.length();
        }
        if (contact.secondName.length() > maxStringLength) {
            maxStringLength = contact.secondName.length();
        }
        if (contact.firstName.length() > maxStringLength) {
            maxStringLength = contact.firstName.length();
        }
        if (contact.phoneNumber.length() > maxStringLength) {
            maxStringLength = contact.phoneNumber.length();
        }
        return maxStringLength;
    }


    /** turns the contact in the address book into a string ready to be stitched together to build a CSV file using semi-colons (;)
     * @param contact needs access to the contacts to join them all with a semi-colon (;)
     * @return returns a string that is CSV formatted with a semi-colon (;)
     */
    public static String contactsStringCsvFormat(ContactsAgenda contact) {
        String identifierString = String.valueOf(contact.identifier);
        List<String> myList = Arrays.asList(identifierString, contact.postalCode, contact.address, contact.companyName,
                contact.secondName, contact.firstName, contact.phoneNumber);
        String csvString = String.join(";", myList);
        System.out.println(csvString);
        System.out.println();
        return csvString;
    }

    /** turns an input into a string
     * @return returns the input string
     */
    public static String inputterToString() {

        Scanner readInput = new Scanner(System.in);
        String inputString = "";
        System.out.println("");
        inputString = readInput.nextLine();
        return inputString;
    }

    /** turns an input into an integer number
     * @return returns the input number
     */
    public static int inputterToInt() {
        Scanner readInput = new Scanner(System.in);
        int inputInt = 0;
        int stringToInteger = 0;
        int exitOption = 6;
        System.out.println("");
        try {
            inputInt = readInput.nextInt();
            while ((inputInt > exitOption) || (inputInt <= 0)) {
                System.out.println(bundle.getString("wrongInput"));
                System.out.println("");
                ContactsAgenda.displayOptions(ContactsAgenda.OPTIONS);
                System.out.println("");
                inputInt = readInput.nextInt();
            }
        } catch (InputMismatchException e) {
            System.out.println(bundle.getString("wrongInput"));
        }
        stringToInteger = inputInt - 1;
        return stringToInteger;
    }

    /** extracts data from a CSV style string and puts the information into the contacts agenda list
     * @param stringFromArray string that has CSV style format separated with a semi-colon (;)
     * @return returns the contacts filled in as per the class description (everything is a string except the identifier)
     */
    public static ContactsAgenda extractDataWithArrayString(String stringFromArray) {
        ContactsAgenda bufferContact = new ContactsAgenda();
        String[] bufferStringList = new String[7];
        
        bufferStringList = stringFromArray.split(";");
        bufferContact.identifier = Integer.parseInt(bufferStringList[0]);
        bufferContact.postalCode = bufferStringList[1];
        bufferContact.address = bufferStringList[2];
        bufferContact.companyName = bufferStringList[3];
        bufferContact.secondName = bufferStringList[4];
        bufferContact.firstName = bufferStringList[5];
        bufferContact.phoneNumber = bufferStringList[6];  
        
        return bufferContact;
    }

    
    /** extract data from a CSV file
     * @param csvRead the CSV file
     * @return returns the contacts filled in as per the class description (everything is a string except the identifier)
     */
    public static ContactsAgenda extractDataFromCsvWithScanner(Scanner csvRead) {
        ContactsAgenda bufferContact = new ContactsAgenda();
        String[] bufferStringList = new String[7];
        String csvString = "";
        
        csvString = csvRead.nextLine();
        bufferStringList = csvString.split(";");
        System.out.println(bundle.getString("csvReadOut"));
        
        bufferContact.identifier = Integer.parseInt(bufferStringList[0]);
        bufferContact.postalCode = bufferStringList[1];
        bufferContact.address = bufferStringList[2];
        bufferContact.companyName = bufferStringList[3];
        bufferContact.secondName = bufferStringList[4];
        bufferContact.firstName = bufferStringList[5];
        bufferContact.phoneNumber = bufferStringList[6];  
        
        return bufferContact;
    }
    
    /** gets the last modified file in a folder to load the latest CSV file stored
     * @param path path on the PC where the CSV file is
     * @return returns the latest file
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
        System.out.println(chosenFile);
        return chosenFile;
    }
}
