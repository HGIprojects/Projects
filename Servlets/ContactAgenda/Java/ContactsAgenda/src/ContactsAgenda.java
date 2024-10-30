import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Scanner;

public class ContactsAgenda {

    int identifier;
    String postalCode;
    String address;
    String companyName;
    String secondName;
    String firstName;
    String phoneNumber;
    static String[] OPTIONS = new String[6]; // initlization of available options
    static ResourceBundle bundle = ResourceBundle.getBundle("messages");
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	

    /** loop that keeps displaying the main menu until you select "exit"
     * @param args
     * @throws IOException errors related to the backup and import of data
     * @throws SQLException in case DB is unavailable
     * @throws ClassNotFoundException optionChooser requires this
     */
    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        
        
        //Locale localizationJP = new Locale("ja", "JP");
        //Locale localizationEN = new Locale("en", "US");
        Scanner languageScan = new Scanner(System.in);
        String languageString = "";
        System.out.println("日本語は大丈夫ですか 押しえてください。// Is Japanese OK? Press the button, please");
        System.out.println("1. はい // Yes");
        System.out.println("2. いいえ // No");
        languageString = languageScan.nextLine();
        if (languageString.equals("1")) {
            Locale.setDefault(new Locale("ja", "JP"));
            System.out.println("選択された言語は日本語です。");
            ResourceBundle bundle = ResourceBundle.getBundle("messages");
        } else {
            Locale.setDefault(new Locale("en", "US"));
            System.out.println("The selected language is English.");
            ResourceBundle bundle = ResourceBundle.getBundle("messages");
        }
        Address addressData = new Address();   
        ContactsAgenda buffer = new ContactsAgenda();
        System.out.println();
        int looper = 0;
        int indexInput = 0;
        List<ContactsAgenda> contactsInformationList = new ArrayList<ContactsAgenda>(); // Initialize list of classes
        OPTIONS[0] = bundle.getString("options1");
        OPTIONS[1] = bundle.getString("options2");
        OPTIONS[2] = bundle.getString("options3");
        OPTIONS[3] = bundle.getString("options4");
        OPTIONS[4] = bundle.getString("options5");
        OPTIONS[5] = bundle.getString("options7");
        
        /**************************************************************************************************************
         * 
         *To import from DB file
         */ 
        DbOperations.contactsPullDB(contactsInformationList);
        /*
         **************************************************************************************************************
         * 
         *To import from CSV file
         * 
         *CsvOperations.importFromFile(contactsInformationList);
         *
         **************************************************************************************************************
         */
        while (looper == 0) { // loops through the available options
            startup(); // welcome text
            looper = optionChooser(contactsInformationList); // this counter helps us get out of the while loop at line 78          
            bottomOfLoopText(); // UI text
            System.out.println();
            System.out.println();
        }
        if (contactsInformationList.size() < 1) { // when exiting the while loop
            System.out.println(bundle.getString("noContacts"));
            System.out.println();
            System.out.println(bundle.getString("byebye1"));
            System.out.println(bundle.getString("byebye3"));
        } else { // CSV backup
            System.out.println(bundle.getString("csvDisplay"));
            System.out.println();
            System.out.println("=============================================================================");
            for (int j = 0; j < contactsInformationList.size(); j++) {
                buffer = contactsInformationList.get(j);
                Tools.contactsStringCsvFormat(buffer);
            }
            System.out.println("=============================================================================");
            System.out.println();
            CsvOperations.exportToFile(contactsInformationList);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	


    /**
     * UI text
     */
    public static void bottomOfLoopText() {
        System.out.println();
        System.out.println();
        System.out.println("=============================================================================");
        System.out.println("=============================================================================");
        System.out.println();
        System.out.println();
    }


    /**
     *  UI text
     */
    public static void startup() {
        System.out.println(bundle.getString("greeting"));
        System.out.println(bundle.getString("caution1"));
        System.out.println();
        displayOptions(OPTIONS);
        System.out.println();
    }



    /**
     *  sample of a stored contact
     */
    public static void exampleText() {
        System.out.println(bundle.getString("exit3"));
        System.out.println();
        System.out.println("*******************************Example*******************************");
        System.out.println(bundle.getString("exampleLine2"));
        System.out.println(bundle.getString("exampleLine3"));
        System.out.println(bundle.getString("exampleLine4"));
        System.out.println(bundle.getString("exampleLine5"));
        System.out.println(bundle.getString("exampleLine6"));
        System.out.println(bundle.getString("exampleLine7"));
        System.out.println("*********************************************************************");
        System.out.println();
    }


    /** displays available options
     * @param listOptions list of available options
     */
    public static void displayOptions(String[] listOptions) {
        for (int i = 0; i < listOptions.length; i++) {
            System.out.print(listOptions[i]);
        }
    }



    /** creates a new contact, if the field is empty, it will exit the contact creation
     * maximum lenght input is restricted by size to 500 bytes
     * @param contactsInformationList needs the total list to assign an identifier
     * @return returns a stored contact
     * @throws IOException required to exit
     */
    public static ContactsAgenda contactsGenerator(List<ContactsAgenda> contactsInformationList) throws IOException {
        Address addressData = new Address();
        ContactsAgenda contact = new ContactsAgenda();
        String inputString = "";
        String buffer = "";
        StringBuilder sb = new StringBuilder();
        int k = 0;
        int maxLengthPC = 9;
        int maxLengthPN = 13;
        int maxStringLength = 0;
        int getOut = 0; // used to exit the while loop
        System.out.println(bundle.getString("selection1"));
        exampleText();
        System.out.println(bundle.getString("question1"));
        System.out.println();
        contact.identifier = contactsInformationList.size() + 1;
        while (getOut < 1) {     
            inputString = Tools.inputterToString();
            if (inputString.equals("")) {
                System.out.println(bundle.getString("error1"));
                contact.postalCode = "";
                contact.address = "";
                contact.companyName = "";
                contact.secondName = "";
                contact.firstName = "";
                contact.phoneNumber = "";
                k = continueOrExit(contactsInformationList);
                break;
            } else {
                while ((inputString.length() < 5) || (inputString.length() > 9)) { // gets postal code and adds a hyphen (-) if it doesnt have one
                    System.out.println(bundle.getString("invalidLengthPC"));
                    inputString = Tools.inputterToString();
                }
                if (!inputString.contains("-")) {
                    char[] charArray = new char[maxLengthPC];
                    charArray = inputString.toCharArray();
                    for (int i = 0; i < 3; i++) {
                        sb.append(charArray[i]);                  
                    }
                    sb.append('-');
                    for (int i = 3; i < inputString.length(); i++) {
                        sb.append(charArray[i]);
                    }
                    buffer = sb.toString();
                    contact.postalCode = buffer;
                } else {
                    contact.postalCode = inputString;
                }
                if (maxStringLength < inputString.length()) {
                    maxStringLength = inputString.length();
                }
            }
            System.out.println();
            contact.address = Address.getAddress();
            if (contact.address.equals("")) {
                System.out.println(bundle.getString("error1"));
                contact.companyName = "";
                contact.secondName = "";
                contact.firstName = "";
                contact.phoneNumber = "";
                k = continueOrExit(contactsInformationList);
                break;
            } else {
                while (contact.address.length() > 500) {
                    System.out.println(bundle.getString("invalidLengthA"));
                    contact.address = Address.getAddress();
                }
                if (maxStringLength < contact.address.length()) {
                    maxStringLength = contact.address.length();
                }
            }
            System.out.println();
            System.out.println(bundle.getString("question3"));
            System.out.println();
            inputString = Tools.inputterToString();
            if (inputString.equals("")) {
                contact.companyName = "";
                contact.secondName = "";
                contact.firstName = "";
                contact.phoneNumber = "";
                k = continueOrExit(contactsInformationList);
                break;
            } else {
                while (inputString.getBytes().length > 50) { // (inputString.length() > 150) {
                    System.out.println(bundle.getString("invalidLengthCN"));
                    inputString = Tools.inputterToString();
                }
                contact.companyName = inputString;
                if (maxStringLength < inputString.length()) {
                    maxStringLength = inputString.length();
                }
            }
            System.out.println();
            System.out.println(bundle.getString("question4"));
            System.out.println();
            inputString = Tools.inputterToString();
            if (inputString.equals("")) {
                System.out.println(bundle.getString("error1"));
                contact.secondName = "";
                contact.firstName = "";
                contact.phoneNumber = "";
                k = continueOrExit(contactsInformationList);
                break;
            } else {
                while (inputString.getBytes().length > 50) { // (inputString.length() > 150) {
                    System.out.println(bundle.getString("invalidLengthSN"));
                    inputString = Tools.inputterToString();
                }
                contact.secondName = inputString;
                if (maxStringLength < inputString.length()) {
                    maxStringLength = inputString.length();
                }
            }
            System.out.println();
            System.out.println(bundle.getString("question5"));
            System.out.println();
            inputString = Tools.inputterToString();
            if (inputString.equals("")) {
                System.out.println(bundle.getString("error1"));
                contact.firstName = "";
                contact.phoneNumber = "";
                k = continueOrExit(contactsInformationList);
                break;
            } else {
                while (inputString.getBytes().length > 50) { // (inputString.length() > 150) {
                    System.out.println(bundle.getString("invalidLengthFN"));
                    inputString = Tools.inputterToString();
                }
                contact.firstName = inputString;
                if (maxStringLength < inputString.length()) {
                    maxStringLength = inputString.length();
                }
            }
            System.out.println();
            System.out.println(bundle.getString("question6"));
            System.out.println();
            inputString = Tools.inputterToString(); // separates a phone number with hyphens (-) if they dont have them
            if (inputString.equals("")) {
                System.out.println(bundle.getString("error1"));
                contact.phoneNumber = "";
                k = continueOrExit(contactsInformationList);
                break;
            } else {
                while ((inputString.length() < 10) || (inputString.length() > 13)) {
                    System.out.println(bundle.getString("invalidLengthPN"));
                    inputString = Tools.inputterToString();
                }
                sb = new StringBuilder();
                if (!inputString.contains("-")) {
                    char[] charArray = new char[maxLengthPN];
                    charArray = inputString.toCharArray();
                    for (int i = 0; i < 3; i++) {
                        sb.append(charArray[i]);
                    }
                    sb.append('-');
                    for (int i = 3; i < 7; i++) {
                        sb.append(charArray[i]);
                    }
                    sb.append('-');
                    for (int i = 7; i < inputString.length(); i++) {
                        sb.append(charArray[i]);
                    }
                    contact.phoneNumber = sb.toString();
                } else {
                    char[] charArray = new char[13];
                    charArray = inputString.toCharArray();
                    if ((charArray[3] == '-') && (charArray[8] == '-')) {
                        contact.phoneNumber = inputString;
                    } else { 
                        if (charArray[3] == '-') {
                            for (int i = 0; i < 8; i++) {
                                sb.append(charArray[i]);
                            }
                            sb.append('-');
                            for (int i = 8; i < inputString.length(); i++) {
                                sb.append(charArray[i]);
                            }
                            contact.phoneNumber = sb.toString();
                        } else {
                            if (charArray[7] == '-') {
                                for (int i = 0; i < 3; i++) {
                                    sb.append(charArray[i]);
                                }
                                sb.append('-');
                                for (int i = 3; i < inputString.length(); i++) {
                                    sb.append(charArray[i]);
                                }
                                contact.phoneNumber = sb.toString();
                            }
                        }
                    }               
                }
                if (maxStringLength < inputString.length()) {
                    maxStringLength = inputString.length();
                }
            }
            DbOperations.contactsExporterDB(contact);
            contactsDisplayer(contact);
            getOut = getOut + 1;
        }
        return contact;
    }


    /** updates a contact from the contact list
     * @param contact to obtain the contact from the list
     * @param contactsInformationList to select the contact from the list
     * @return contact information updated
     * @throws IOException  because of working with database
     */
    public static ContactsAgenda contactsUpdater(ContactsAgenda contact, List<ContactsAgenda> contactsInformationList) throws IOException {
        /*
         *  the buffer is to store information before saving the original
         *  in case the user cancels the input at any point
         */
        StringBuilder sb = new StringBuilder();
        int maxLengthPC = 9;
        int maxLengthPN = 13;
        ContactsAgenda buffer = new ContactsAgenda();
        String inputString = "";
        int k = 0;
        int maxStringLength = 0;
        int getOut = 0;
        System.out.println(bundle.getString("request"));
        exampleText();
        System.out.println(bundle.getString("question1"));
        System.out.println();
        inputString = Tools.inputterToString();
        while (getOut < 1) {
            if (inputString.equals("")) {
                System.out.println(bundle.getString("skip1"));
                buffer.postalCode = contact.postalCode;
            } else {
                while ((inputString.length() < 5) || (inputString.length() > 10)) { // gets postal code and adds a hyphen (-) if it doesnt have one
                    System.out.println(bundle.getString("invalidLengthPC"));
                    inputString = Tools.inputterToString();
                }
                if (!inputString.contains("-")) {
                    char[] charArray = new char[maxLengthPC];
                    charArray = inputString.toCharArray();
                    for (int i = 0; i < 3; i++) {
                        sb.append(charArray[i]);                  
                    }
                    sb.append('-');
                    for (int i = 3; i < inputString.length(); i++) {
                        sb.append(charArray[i]);
                    }
                    buffer.postalCode = sb.toString();
                } else {
                    buffer.postalCode = inputString;
                }
                if (maxStringLength < inputString.length()) {
                    maxStringLength = inputString.length();
                }
            }           
            System.out.println();
            buffer.address = Address.getAddress();
            if (buffer.address.equals("")) {
                System.out.println(bundle.getString("skip1"));
                buffer.address = contact.address;
            } else { 
                while (buffer.address.length() > 500) {
                    System.out.println(bundle.getString("invalidLengthA"));
                    buffer.address = Address.getAddress();
                }
                if (maxStringLength < buffer.address.length()) {
                    maxStringLength = buffer.address.length();
                }
            }    
            System.out.println();
            System.out.println(bundle.getString("question3"));
            System.out.println();
            inputString = Tools.inputterToString();
            if (inputString.equals("")) {
                System.out.println(bundle.getString("skip1"));
                buffer.companyName = contact.companyName;
            } else { 
                while (inputString.length() > 50) {
                    System.out.println(bundle.getString("invalidLengthCN"));
                    inputString = Tools.inputterToString();
                }
                buffer.companyName = inputString;
                if (maxStringLength < inputString.length()) {
                    maxStringLength = inputString.length();
                }
            }
            System.out.println();
            System.out.println(bundle.getString("question4"));
            System.out.println();
            inputString = Tools.inputterToString();
            if (inputString.equals("")) {
                System.out.println(bundle.getString("skip1"));
                buffer.secondName = contact.secondName;
            } else { 
                while (inputString.length() > 50) {
                    System.out.println(bundle.getString("invalidLengthLN"));
                    inputString = Tools.inputterToString();
                }
                buffer.secondName = inputString;
                if (maxStringLength < inputString.length()) {
                    maxStringLength = inputString.length();
                }
            }
            System.out.println();
            System.out.println(bundle.getString("question5"));
            System.out.println();
            inputString = Tools.inputterToString();
            if (inputString.equals("")) {
                System.out.println(bundle.getString("skip1"));
                buffer.firstName = contact.firstName;
            } else { 
                while (inputString.length() > 50) {
                    System.out.println(bundle.getString("invalidLengthFN"));
                    inputString = Tools.inputterToString();
                }
                buffer.firstName = inputString;
                if (maxStringLength < inputString.length()) {
                    maxStringLength = inputString.length();
                }
            }
            System.out.println();
            System.out.println(bundle.getString("question6"));
            System.out.println();
            inputString = Tools.inputterToString();
            if (inputString.equals("")) {
                System.out.println(bundle.getString("skip1"));
                buffer.phoneNumber = contact.phoneNumber;
            } else {
                while (inputString.length() < 11) {
                    System.out.println(bundle.getString("invalidLengthPN"));
                    inputString = Tools.inputterToString();
                }
                sb = new StringBuilder();
                if (!inputString.contains("-")) {
                    char[] charArray = new char[maxLengthPN];
                    charArray = inputString.toCharArray();
                    for (int i = 0; i < 3; i++) {
                        sb.append(charArray[i]);
                    }
                    sb.append('-');
                    for (int i = 3; i < 7; i++) {
                        sb.append(charArray[i]);
                    }
                    sb.append('-');
                    for (int i = 7; i < inputString.length(); i++) {
                        sb.append(charArray[i]);
                    }
                    buffer.phoneNumber = sb.toString();
                } else {
                    char[] charArray = new char[13];
                    charArray = inputString.toCharArray();
                    if ((charArray[3] == '-') && (charArray[8] == '-')) {
                        buffer.phoneNumber = inputString;
                    } else { 
                        if (charArray[3] == '-') {
                            for (int i = 0; i < 8; i++) {
                                sb.append(charArray[i]);
                            }
                            sb.append('-');
                            for (int i = 8; i < inputString.length(); i++) {
                                sb.append(charArray[i]);
                            }
                            buffer.phoneNumber = sb.toString();
                        } else {
                            if (charArray[7] == '-') {
                                for (int i = 0; i < 3; i++) {
                                    sb.append(charArray[i]);
                                }
                                sb.append('-');
                                for (int i = 3; i < inputString.length(); i++) {
                                    sb.append(charArray[i]);
                                }
                                buffer.phoneNumber = sb.toString();
                            }
                        }
                    }
                    buffer.phoneNumber = inputString;
                    if (maxStringLength < inputString.length()) {
                        maxStringLength = inputString.length();
                    }
                }
            }
            System.out.println(bundle.getString("updateTo"));
            System.out.println();
            contactsDisplayer(contact);
            System.out.println();
            System.out.println("--> --> --> --> --> --> --> --> -->");
            System.out.println();     
            getOut = getOut + 1;
        }
        // now we save the information from the buffer to the volatile memory
        if (k < 5) {
            contact.postalCode = buffer.postalCode;
            contact.address = buffer.address;
            contact.companyName = buffer.companyName;
            contact.secondName = buffer.secondName;
            contact.firstName = buffer.firstName;
            contact.phoneNumber = buffer.phoneNumber;
            //DbOperations.contactsExporterDB(contact);
            contactsDisplayer(contact);
        }
        return contact;
    }



    /** displays on screen all contacts
     * @param contactsInformation needs the information to display it
     */
    public static void contactsDisplayer(ContactsAgenda contactsInformation) {

        int k = 0;
        int totalHeaderLength = 0;
        boolean longHeaderFlag = false;
        int maxStringLength = 0;
        int exampleHeaderSize = 69;
        int i = 0;

        maxStringLength = Tools.tapeMeasurer(contactsInformation);
        totalHeaderLength = maxStringLength + 20;
        if (totalHeaderLength > exampleHeaderSize - 1) {
            longHeaderFlag = true;
            i = totalHeaderLength;
            while (i > 0) {
                System.out.print("*");
                i--;
            }
        } else {
            for (int j = 0; j < exampleHeaderSize; j++) {
                System.out.print("*");
            }
        }
        System.out.println("*");
        System.out.print(bundle.getString("postLine") + contactsInformation.postalCode);
        k = 0; // initialization because it begins a different task
        if (contactsInformation.postalCode == null) {
            k = 20;
        } else {
            k = contactsInformation.postalCode.length() + 20;
        }
        if (longHeaderFlag) {
            while (k < totalHeaderLength) {
                System.out.print(" ");
                k++;
            }
        } else {
            while (k < exampleHeaderSize - 1) {
                System.out.print(" ");
                k++;
            }
        }
        System.out.println(" *");
        System.out.print(bundle.getString("addressLine") + contactsInformation.address);
        k = 0; // initialization because it begins a different task
        if (contactsInformation.address == null) {
            k = 20;
            contactsInformation = new ContactsAgenda();
        } else {
            k = contactsInformation.address.length() + 20;
        }
        if (longHeaderFlag) {
            while ((k < totalHeaderLength)) {
                System.out.print(" ");
                k++;
            }
        } else {
            while (k < exampleHeaderSize - 1) {
                System.out.print(" ");
                k++;
            }
        }
        System.out.println(" *");
        System.out.print(bundle.getString("companyLine") + contactsInformation.companyName);
        if (contactsInformation.companyName == null) {
            k = 20;
        } else {
            k = contactsInformation.companyName.length() + 20;
        }
        if (longHeaderFlag) {
            while ((k < totalHeaderLength)) {
                System.out.print(" ");
                k++;
            }
        } else {
            while (k < exampleHeaderSize - 1) {
                System.out.print(" ");
                k++;
            }
        }
        System.out.println(" *");
        System.out.print(bundle.getString("secondLine") + contactsInformation.secondName);
        if (contactsInformation.secondName == null) {
            k = 20;
        } else {
            k = contactsInformation.secondName.length() + 20;
        }
        if (longHeaderFlag) {
            while ((k < totalHeaderLength)) {
                System.out.print(" ");
                k++;
            }
        } else {
            while (k < exampleHeaderSize - 1) {
                System.out.print(" ");
                k++;
            }
        }
        System.out.println(" *");
        System.out.print(bundle.getString("firstLine") + contactsInformation.firstName);
        if (contactsInformation.firstName == null) {
            k = 20;
        } else {
            k = contactsInformation.firstName.length() + 20;
        }
        if (longHeaderFlag) {
            while ((k < totalHeaderLength)) {
                System.out.print(" ");
                k++;
            }
        } else {
            while (k < exampleHeaderSize - 1) {
                System.out.print(" ");
                k++;
            }
        }
        System.out.println(" *");
        System.out.print(bundle.getString("phoneLine") + contactsInformation.phoneNumber);
        if (contactsInformation.phoneNumber == null) {
            k = 20;
        } else {
            k = contactsInformation.phoneNumber.length() + 20;
        }
        if (longHeaderFlag) {
            while ((k < totalHeaderLength)) {
                System.out.print(" ");
                k++;
            }
        } else {
            while (k < exampleHeaderSize - 1) {
                System.out.print(" ");
                k++;
            }
        }
        System.out.println(" *");
        if (totalHeaderLength > exampleHeaderSize - 1) {
            i = totalHeaderLength;
            while (i > 0) {
                System.out.print("*");
                i--;
            }
        } else {
            for (int j = 0; j < exampleHeaderSize; j++) {
                System.out.print("*");
            }
        }
        System.out.println("*");
    }

    

    /** this is how the options are chose, now with no stirng of booleans
     * @param contactsInformationList required for working with it
     * @return returns looper to exit the while at the beginning
     * @throws IOException required when working with files
     * @throws SQLException req	uired in case the DB is unavailable
     */
    public static int optionChooser(List<ContactsAgenda> contactsInformationList)
            throws IOException, SQLException {
        ContactsAgenda buffer = new ContactsAgenda();
        Scanner scan = new Scanner(System.in);
        Scanner readSelection = new Scanner(System.in);
        int looper = 0;
        int selector = 0;
        int selectedOption = 0;
        int totalOptions = OPTIONS.length;
        int exitNumber = 9;
        
        selectedOption = readSelection.nextInt();      
        if ((selectedOption > 0) && (selectedOption <= totalOptions)) {
            switch (selectedOption) {
                case 1: // adding a contact to the list
                    contactsInformationList.add(contactsGenerator(contactsInformationList));
                    looper = continueOrExit(contactsInformationList);
                    break;
                case 2: // update contacts,first it goes over the full list
                    if (contactsInformationList.size() < 1) {
                        System.out.println(bundle.getString("noUpdate"));
                        break;
                    } else {
                        System.out.println(bundle.getString("yesUpdate"));
                        for (int j = 0; j < contactsInformationList.size(); j++) {
                            int k = j + 1;
                            System.out.println(" (" + k + ")");
                            buffer = contactsInformationList.get(j);
                            contactsDisplayer(buffer);
                        }
                        System.out.println(bundle.getString("update1"));
                        System.out.println();
                        try {
                            selector = Integer.parseInt(scan.nextLine()) - 1;
                            if ((0 <= selector) && (selector < contactsInformationList.size())) {
                                buffer = contactsUpdater(contactsInformationList.get(selector), contactsInformationList);
                                DbOperations.contactsUpdaterDB(contactsInformationList.get(selector), contactsInformationList.get(selector).identifier);
                                looper = continueOrExit(contactsInformationList);
                            } else {
                                System.out.println(bundle.getString("wrongSelection"));
                                looper = continueOrExit(contactsInformationList);
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println(bundle.getString("noNumber"));
                            break;
                        }
                    }
                    break;
                case 3: // delete a contact
                    if (contactsInformationList.size() < 1) {
                        System.out.println(bundle.getString("noData"));
                        break;
                    } else {
                        System.out.println(bundle.getString("delete1"));
                        for (int j = 0; j < contactsInformationList.size(); j++) {
                            int k = j + 1;
                            System.out.println(" (" + k + ")");
                            buffer = contactsInformationList.get(j);
                            contactsDisplayer(buffer);
                        }
                        System.out.println(bundle.getString("delete2"));
                        System.out.println();
                        try {
                            selector = Integer.parseInt(scan.nextLine()) - 1;
                            if ((0 <= selector) && (selector < contactsInformationList.size())) {
                                DbOperations.contactDeleteDb(contactsInformationList.get(selector).identifier);
                                contactsInformationList.remove(selector);
                                System.out.println(bundle.getString("delete3"));
                                System.out.println();
                                looper = continueOrExit(contactsInformationList);
                                break;
                            } else {
                                System.out.println(bundle.getString("wrongSelection"));
                                looper = continueOrExit(contactsInformationList);
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println(bundle.getString("noNumber"));
                            break;
                        }
                    }
                    break;

                case 4: // display contact list
                    if (contactsInformationList.size() < 1) {
                        System.out.println(bundle.getString("noDisplay"));
                        break;
                    } else {
                        System.out.println(bundle.getString("display1"));
                        for (int j = 0; j < contactsInformationList.size(); j++) {
                            int k = j + 1;
                            System.out.println(" (" + k + ")");
                            buffer = contactsInformationList.get(j);
                            contactsDisplayer(buffer);
                        }
                        looper = continueOrExit(contactsInformationList);
                    }
                    break;

                case 5: // CSV
                    if (contactsInformationList.size() < 1) {
                        System.out.println(bundle.getString("noContacts"));
                        break;
                    } else {
                        System.out.println(bundle.getString("csvDisplay"));
                        System.out.println();
                        for (int j = 0; j < contactsInformationList.size(); j++) {
                            buffer = contactsInformationList.get(j);
                            Tools.contactsStringCsvFormat(buffer);
                        }
                        CsvOperations.exportToFile(contactsInformationList);
                        looper = continueOrExit(contactsInformationList);
                    }
                    break;

                case 6: // exit the program
                    looper = continueOrExit(contactsInformationList);
                    break;
                default:
                    System.out.println(bundle.getString("unexpected"));
            }     
        }
        return looper;
    }

    

    /** after exiting each of the options, you can return to the main menu or exit the agenda
     * @param contactsInformationList needs to check the size to display text
     * @return returns looper to exit the first while loop in the main
     * @throws IOException because it works with files
     */
    public static int continueOrExit(List<ContactsAgenda> contactsInformationList) throws IOException {
        
        Scanner inputReader = new Scanner(System.in);
        ContactsAgenda buffer = new ContactsAgenda();
        System.out.println(bundle.getString("goBack"));
        String input = "";
        int looper = 3;
        input = inputReader.nextLine();
        if (input.equals("1")) {
            looper = 0;
        } else {
            if (input.equals("9")) {
                looper = 1;
                //OPTIONS_BOOLEAN[(OPTIONS_BOOLEAN.length - 1)] = false;
                System.out.println(bundle.getString("byebye2"));
                System.out.println(bundle.getString("byebye1"));
                System.out.println(bundle.getString("byebye3"));
                System.out.println();
                System.out.println();

            
                if (contactsInformationList.size() < 1) {
                    System.out.println(bundle.getString("noContacts"));
                    System.out.println();
    
                } else {
                    System.out.println(bundle.getString("csvDisplay"));
                    System.out.println();
                    System.out.println("=============================================================================");
    
                    for (int j = 0; j < contactsInformationList.size(); j++) {
                        buffer = contactsInformationList.get(j);
                        Tools.contactsStringCsvFormat(buffer);
                    }
                    System.out.println("=============================================================================");
                    System.out.println();
    
                    CsvOperations.exportToFile(contactsInformationList);
                }
                System.exit(0);
            } else {
                System.out.println(bundle.getString("incorrect1"));

                continueOrExit(contactsInformationList);
            }
        }
        return looper;

    }



    /** multi-language support
     * @return returns the messages
     */
    public static ResourceBundle readPropertyFile() {
        ResourceBundle messages = null;
        try {
            messages = ResourceBundle.getBundle("messages_jp");
        } catch (MissingResourceException e) {
            System.out.println(e);
            System.out.println(e.getMessage());
        }
        return messages;
    }

}