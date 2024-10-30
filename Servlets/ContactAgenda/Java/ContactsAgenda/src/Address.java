import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Address {

    String prefecture;
    String city;
    String district;
    String buildingNumber;
    String buildingName;
    String officeNumber;
    static ResourceBundle bundle = ResourceBundle.getBundle("messages");
    

    /** method for the address input exclusively (and class)
     * @return returns a string that will be stored under the "address" field in ContactsAgendaEN class
     */
    static String getAddress() {
        Scanner readInput = new Scanner(System.in);
        Address addressData = new Address();
        String buffer = "";
        Locale.setDefault(new Locale("ja", "JP"));
        System.out.println(bundle.getString("enterPrefecture"));
        buffer = readInput.nextLine();
        
        if (buffer.equals("")) {
            System.out.println(bundle.getString("skip1"));
            addressData.prefecture = "";
            addressData.city = "";
            addressData.district = "";
            addressData.buildingNumber = "";
            addressData.buildingName = "";
            addressData.officeNumber = "";
            
        } else {
        
            addressData.prefecture = buffer;
            System.out.println(bundle.getString("enterCity"));
            buffer = readInput.nextLine();
            if (buffer.equals("")) {
                System.out.println(bundle.getString("skip1"));
                addressData.city = "";
                addressData.district = "";
                addressData.buildingNumber = "";
                addressData.buildingName = "";
                addressData.officeNumber = "";
                
            } 
            addressData.city = buffer;         
            System.out.println(bundle.getString("enterDistrict"));
            buffer = readInput.nextLine();
            if (buffer.equals("")) {
                System.out.println(bundle.getString("skip1"));
                addressData.district = "";
                addressData.buildingNumber = "";
                addressData.buildingName = "";
                addressData.officeNumber = "";
                
            } else { 
                addressData.district = buffer;        
                System.out.println(bundle.getString("enterBuildingNumber"));
                buffer = readInput.nextLine();
                if (buffer.equals("")) {
                    System.out.println(bundle.getString("skip1"));
                    addressData.buildingNumber = "";
                    addressData.buildingName = "";
                    addressData.officeNumber = "";
                    
                } else {
                    addressData.buildingNumber = buffer;
                    System.out.println(bundle.getString("enterBuildingName"));
                    buffer = readInput.nextLine();
                    if (buffer.equals("")) {
                        System.out.println(bundle.getString("skip1"));
                        addressData.buildingName = "";
                        addressData.officeNumber = "";
                        
                    } else {
                        addressData.buildingName = buffer;        
                        System.out.println(bundle.getString("enterOfficeNumber"));
                        buffer = readInput.nextLine();
                        if (buffer.equals("")) {
                            System.out.println(bundle.getString("skip1"));
                            addressData.officeNumber = "";                            
                            
                        } else {
                            addressData.officeNumber = buffer; 
                        }
                    }
                }
            }
        }

        if (addressData.prefecture.equals("") || addressData.city.equals("") || addressData.district.equals("")) {
            if (addressData.buildingNumber.equals("") || addressData.buildingName.equals("") || addressData.officeNumber.equals("")) {
                buffer = "";
            }
        } else {
            buffer = addressData.prefecture.concat(", ").concat(addressData.city).concat(", ").concat(addressData.district);
            buffer = buffer.concat(", ").concat(addressData.buildingNumber).concat(", ").concat(addressData.buildingName);
            buffer = buffer.concat(", ").concat(addressData.officeNumber);
        }
        
        return buffer;
    }    
  
}

