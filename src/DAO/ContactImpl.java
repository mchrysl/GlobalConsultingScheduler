package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Contact;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The methods in this class facilitate access to database contact tables using the classes in the model folder,
 * using SQL statements to gather observableLists and resultSets that can be utilized in the controller classes that
 * make the information accessible via the fxml forms/views.
 */
public class ContactImpl {


    /**
     * Returns an observableList of all Contact Names from the contacts table.
     * @return  This returns an observableList of all Contact Names from the contacts table.
     */
    public static ObservableList<String> getAllContactNames(){
        ObservableList<String> allContactNames = FXCollections.observableArrayList();
        String sqlStmt = "SELECT Contact_Name FROM contacts;";
        try {
            Query.buildQuery(sqlStmt);

            ResultSet contactNameResultSet = Query.getResult();
            while (contactNameResultSet.next()) {
                String contactName = contactNameResultSet.getString("Contact_Name");
                allContactNames.add(contactName);
            }

        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return allContactNames;
    }

    /**
     * Returns the integer (int) Contact ID for the specified Contact Name.
     * @param contactName   A String of the name of the contact.
     * @return              This returns an int representation of the contact ID associated with the specified contactName.
     */
    public static int getContactId(String contactName){
        int foundContactId = -1;
        String sqlString = "SELECT Contact_ID, Contact_Name FROM contacts WHERE Contact_Name = '"+ contactName +"';";
        try {
            Query.buildQuery(sqlString);
            ResultSet contactIdResult = Query.getResult();
            while (contactIdResult.next()) {
                foundContactId = contactIdResult.getInt("Contact_ID");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return foundContactId;

    }

    /*    public static ObservableList<Contact> getAllContacts(){
        ObservableList<Contact> allContacts = FXCollections.observableArrayList();
        String sqlStmt = "SELECT * FROM contacts;";
        try {
            Query.buildQuery(sqlStmt);

            ResultSet contactsResultSet = Query.getResult();
            while (contactsResultSet.next()) {
                int contactId = contactsResultSet.getInt("Contact_ID");
                String contactName = contactsResultSet.getString("Contact_Name");
                String contactEmail = contactsResultSet.getString("Email");

                Contact singleContact = new Contact(contactId, contactName, contactEmail);
                allContacts.add(singleContact);
            }

        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return allContacts;
    }*/

}
