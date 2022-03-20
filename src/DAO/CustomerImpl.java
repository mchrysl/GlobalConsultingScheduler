package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Customer;
import model.CustomerFormTableData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;


/**
 * The methods in this class facilitate access to database appointment tables using the classes in the model folder,
 * using SQL statements to gather observableLists and resultSets that can be utilized in the controller classes that
 * make the information accessible via the fxml forms/views.
 */
public class CustomerImpl {

    /**
     * Returns an observableList of items formatted for use in the customer tableView.
     * Select data from customer, first_level_division, and countries tables.
     * Format all data to Strings for use in customer tableView and store as a CustomerFormTableData type.
     * @return observableList   This returns an observableList of items formatted for use in the customer tableView.
     */
    public static ObservableList<CustomerFormTableData> getCustomerFormTableData() {
        ObservableList<CustomerFormTableData> customerData = FXCollections.observableArrayList();
        String sqlStmt = "SELECT customers.Customer_ID, customers.Customer_Name, customers.Address," +
                "first_level_divisions.Division, countries.Country, customers.Postal_Code, customers.Phone " +
                "FROM customers " +
                "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                "JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID " +
                "ORDER BY Customer_ID;";
        try {
            Query.buildQuery(sqlStmt);

            ResultSet customerResultSet = Query.getResult();

            //Data fill
            while (customerResultSet.next()) {
                String crsID = customerResultSet.getString("customers.Customer_ID");
                String crsName = customerResultSet.getString("customers.Customer_Name");
                String crsAddress = customerResultSet.getString("customers.Address");
                String crsDivision = customerResultSet.getString("first_level_divisions.Division");
                String crsCountry = customerResultSet.getString("countries.Country");
                String crsPostalCode = customerResultSet.getString("customers.Postal_Code");
                String crsPhone = customerResultSet.getString("customers.Phone");

                CustomerFormTableData custRow = new CustomerFormTableData(crsID, crsName, crsAddress, crsDivision, crsCountry, crsPostalCode, crsPhone);
                customerData.add(custRow);

            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return customerData;
    }


    /**
     * Returns an observableList of customer IDs in a String format to be used in a comboBox.
     * @return  This returns an observableList of customer IDs in a String format.
     */
    public static ObservableList<String> getAllCustomerIdsString(){
        ObservableList<String> allCustomerIds = FXCollections.observableArrayList();
        String sqlStmt = "SELECT Customer_ID FROM customers;";
        try {
            Query.buildQuery(sqlStmt);

            ResultSet customerIdsResultSet = Query.getResult();
            while (customerIdsResultSet.next()) {
                allCustomerIds.add(String.valueOf(customerIdsResultSet.getInt("Customer_ID")));
            }

        } catch(SQLException throwables) {
            throwables.printStackTrace();
        }
        return allCustomerIds;
    }

    /*public static int getCustomerIdFromName(String selectedCustomerName){
        int customerId = 0;
        String sqlStmt = "SELECT Customer_ID, Customer_Name FROM customers " +
                "WHERE customers.Customer_Name = '" + selectedCustomerName + "';";
        try {
            Query.buildQuery(sqlStmt);
            ResultSet rs = Query.getResult();
            while (rs.next()) {
                customerId = rs.getInt("Customer_ID");
                System.out.print("selected customer Id: " + customerId);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        };
        return customerId;
    }*/


    /**
     * Adds a new customer to the customers table in the database.
     * Get new customer data from the newCustomer parameter. Build and execute the INSERT statement for the new
     * customer. Return a boolean that indicates whether the add was successful (true) or unsuccessful (false).
     * @param newCustomer   A Customer type that contains the data for the new entry to the customer table.
     * @return boolean      This returns a boolean that indicates whether the add was successful (true) or not (false).
     */
    public static boolean addCustomer(Customer newCustomer) {
        boolean isSuccessful = true;

        String addCustomerName = newCustomer.getCustomerName();
        String addCustomerAddress = newCustomer.getCustomerAddress();
        String addCustomerPostalCode = newCustomer.getPostalCode();
        String addCustomerPhone = newCustomer.getPhone();
        Timestamp addCreateDate = Timestamp.valueOf(newCustomer.getCreateDate());
        String addCreatedBy = newCustomer.getCreatedBy();
        Timestamp addLastUpdated = Timestamp.valueOf(newCustomer.getLastUpdate());
        String addLastUpdatedBy = newCustomer.getLastUpdatedBy();
        int addDivisionId = newCustomer.getDivisionId();

        String sqlStmt = "INSERT into customers " +
                "(Customer_Name, Address, Postal_Code, Phone, Create_Date, Created_By, Last_Update, " +
                "Last_Updated_By, Division_ID) " +
                "VALUES('" + addCustomerName + "', '"+ addCustomerAddress + "', '"+ addCustomerPostalCode +
                "', '"+ addCustomerPhone + "', '"+ addCreateDate + "', '"+ addCreatedBy + "', '"+ addLastUpdated +
                "', '"+ addLastUpdatedBy + "', "+ addDivisionId + ");";

        Query.buildQuery(sqlStmt);

        return isSuccessful;
    }

    /**
     * Updates a customer entry in the customers table using the data in the changeCustomer input.
     * Create SQL UPDATE statement using the data from changeCustomer.
     * Try to execute the UPDATE statement and return a boolean indicating whether the UPDATE was successful (true)
     * or unsuccessful (false).
     * @param changeCustomer    A Customer that contains the data updates for a Customer.
     * @return boolean          This returns a boolean which indicates whether the update was successful (true) or
     *                          unsuccessful (false).
     */
    public static boolean updateCustomer(Customer changeCustomer){
        boolean isSuccessful = true;
        boolean afterSetFlag = true;    //is this the first field=value pair after 'SET' part of UPDATE statement?

        //build update statement for database
        String sqlStmt = "UPDATE customers SET ";

        if (changeCustomer.getCustomerId() > -1){
            sqlStmt = sqlStmt + "Customer_ID = '" + changeCustomer.getCustomerId() + "'";
            afterSetFlag = false;
        }
        if (!changeCustomer.getCustomerName().equals("N/A")){
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            sqlStmt = sqlStmt + "Customer_Name = '" + changeCustomer.getCustomerName() + "'";
            afterSetFlag = false;
        }
        if (!changeCustomer.getCustomerAddress().equals("N/A")){
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            sqlStmt = sqlStmt + "Address = '" + changeCustomer.getCustomerAddress() + "'";
            afterSetFlag = false;
        }
        if (!changeCustomer.getPostalCode().equals("N/A")){
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            sqlStmt = sqlStmt + "Postal_Code = '" + changeCustomer.getPostalCode() + "'";
            afterSetFlag = false;
        }
        if (!changeCustomer.getPhone().equals("N/A")){
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            sqlStmt = sqlStmt + "Phone = '" + changeCustomer.getPhone() + "'";
            afterSetFlag = false;
        }
        if (changeCustomer.getDivisionId() > -1){
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            sqlStmt = sqlStmt + "Division_ID = " + changeCustomer.getDivisionId();
            afterSetFlag = false;
        }

        //Since this is updating customer, only change Last_Updated_By and Last_Updated
        //Note: these will always change on update!
        if (!changeCustomer.getLastUpdatedBy().equals("N/A")){
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
            sqlStmt = sqlStmt + "Last_Updated_By = '" + changeCustomer.getLastUpdatedBy() +
                    "', Last_Update = '" + lastUpdate + "'";
        }
        //finish sqlStmt
        sqlStmt = sqlStmt + " WHERE Customer_ID = " + changeCustomer.getCustomerId() + ";";

        Query.buildQuery(sqlStmt);

        return isSuccessful;

    }

    /**
     * Deletes a customer from the customer table using customerToDelete as a unique identifier for the
     * customer to be deleted.
     * Get the customer Name and ID from customers table to display in conform Alert dialog.
     * Display confirm Alert dialog and read in result button - Ok to confirm, otherwise do not delete.
     * Delete the customer once it is confirmed.
     * Returns the boolean isSuccessful to indicate whether the delete was successful or unsuccessful.
     * @param customerToDelete  An int representation of the customer ID of the customer to be deleted.
     * @return boolean          This returns a boolean which indicates a successful (true) or unsuccessful (false) delete.
     */
    public static boolean deleteCustomer(int customerToDelete){
        boolean isSuccessful = true;

        //Confirmation Alert Dialog
        String customerName = "";
        String sqlStmt = "SELECT Customer_ID, Customer_Name FROM customers " +
                "WHERE Customer_ID = " + customerToDelete + ";";
        try {
            Query.buildQuery(sqlStmt);
            ResultSet customerIdNameResult = Query.getResult();
            while (customerIdNameResult.next()) {
                customerName = customerIdNameResult.getString("Customer_Name");
            }
        } catch(SQLException throwables) {
            System.out.println("Error getting customer name");
            throwables.printStackTrace();
        }
        Alert doubleCheck = new Alert(Alert.AlertType.CONFIRMATION);
        doubleCheck.setHeaderText("Confiirm Delete");
        doubleCheck.setContentText("Are you sure you want to delete\nCustomer ID: " + customerToDelete +
                ",\nNamed: " + customerName + "?");
        Optional<ButtonType> result = doubleCheck.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sqlDeleteStmt = "DELETE FROM customers WHERE Customer_ID = " + customerToDelete + ";";
            Query.buildQuery(sqlDeleteStmt);
        } else {
            isSuccessful = false;
        }

        return isSuccessful;
    }
}
