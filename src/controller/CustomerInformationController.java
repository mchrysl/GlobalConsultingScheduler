package controller;

import DAO.*;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Customer;
import model.CustomerFormTableData;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

import static controller.LoginController.usernameInput;
import static controller.MainMenuController.*;


/**
 * Provides all functionality - view customers, add customer, update customer, delete customer,
 * and closes the form - for the customer javafx file.
 * @author Margaret Chrysler
 * @version 1.6
 */
public class CustomerInformationController implements Initializable {


    @FXML
    Label noticeLbl;

    //customer tableview and columns
    @FXML
    public TableView<CustomerFormTableData> customerTableView;

    @FXML
    public TableColumn<model.CustomerFormTableData, String> ID_CustTblCol;
    @FXML
    private TableColumn<model.CustomerFormTableData, String> name_CustTblCol;
    @FXML
    private TableColumn<model.CustomerFormTableData, String> address_CustTblCol;
    @FXML
    private TableColumn<model.CustomerFormTableData, String> stateProvince_CustTblCol;
    @FXML
    private TableColumn<model.CustomerFormTableData, String> country_CustTblCol;
    @FXML
    private TableColumn<model.CustomerFormTableData, String> postalCode_CustTblCol;
    @FXML
    private TableColumn<model.CustomerFormTableData, String> phone_CustTblCol;

    //textField & listbox connections
    @FXML
    private Label customerIdLbl;
    @FXML
    private TextField customerNameTxt;
    @FXML
    private TextField customerAddressTxt;
    @FXML
    private ComboBox<String> customerCountryComboBox;
    @FXML
    private ComboBox<String> customerDivisionComboBox;
    @FXML
    private TextField customerPostalCodeTxt;
    @FXML
    private TextField customerPhoneTxt;


    @FXML
    private Button actionBtn;
    @FXML
    private Label subTitleLbl;
    @FXML
    Button closeFormBtn;

    public static int customerToDelete = -1;

    /**
     * Closes the customers information stage when the 'Close Customers Form' button in the Customer Information view
     * is clicked.
     */
    @FXML
    private void closeFormButtonClicked() {
        levelTwoFormStage.close();
    }

    /**
     * Fills customers information view with initial values based on the selected action from Main Menu.
     * Ties (binds) the actionBtn and subTitleLbl to what action was chosen for the form to complete. Fills the tableView
     * with customers from the database. Fills the textFields with prompts if the selected action was Add. Sets up
     * via lambda expression what to do when the tableView has a row that is clicked or selected. Also sets up a lambda
     * expression for what to do when a row is selected in the customer tableView.
     * @param url               the url for the view being initialized
     * @param resourceBundle    the resource bundle for the view being initialized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //tie values to the button that initiates the action (add, update, or delete)
        //and to the form's subtitle (a label)
        actionBtn.textProperty().bind(Bindings.createStringBinding(() -> MainMenuController.whatAction + " Customer"));
        subTitleLbl.textProperty().bind(Bindings.createStringBinding(() -> MainMenuController.whatAction + " Customer Form"));

        fillCustomerTable();

        if (whatAction.equals("Add")){
            //find next customer ID
            fillPromptsForAdd(findNextCustomerID());
        }

        //set up row factory
        customerTableView.setRowFactory((rf -> {
            TableRow<model.CustomerFormTableData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    CustomerFormTableData selectedRow = row.getItem();
                    customerTableRowClicked(selectedRow);
                }
            });
            return row;
        }));

    }

    /**
     * Establishes the links between the tableView columns/cells and the customerFormTableDta model using PropertyValueFactory.
     */
    private void fillCustomerTable(){

        //connect TableView with database data
        ID_CustTblCol.setCellValueFactory(new PropertyValueFactory<>("cCustomerId"));
        name_CustTblCol.setCellValueFactory(new PropertyValueFactory<>("cCustomerName"));
        address_CustTblCol.setCellValueFactory(new PropertyValueFactory<>("cAddress"));
        stateProvince_CustTblCol.setCellValueFactory(new PropertyValueFactory<>("cDivision"));
        country_CustTblCol.setCellValueFactory(new PropertyValueFactory<>("cCountry"));
        postalCode_CustTblCol.setCellValueFactory(new PropertyValueFactory<>("cPostalCode"));
        phone_CustTblCol.setCellValueFactory(new PropertyValueFactory<>("cPhone"));
        //get items from database and make it visible
        customerTableView.setItems(CustomerImpl.getCustomerFormTableData());
        customerTableView.setVisible(true);

    }

    /**
     * Actions to be completed when a row is selected in the populated tableView based on which action is to be performed
     * by the customer informationform.
     * Calls a method that fills the textBoxes and comboBoxes with the data from the selected row. If the selected action
     * is delete then the textFields are also disabled. Sets up a lambda expression to fill the division comboBox when
     * the country comboBox is changed.
     * @param selectedRow   the model of the data for the specifically selected row
     */
    @FXML
    private void customerTableRowClicked(CustomerFormTableData selectedRow){
        switch (whatAction){
            case "Update":
                fillTextBoxes(selectedRow);
                break;
            case "Delete":
                //load information to textFields
                fillTextBoxes(selectedRow);
                noEditTextBoxes();
                break;
            default:
                break;
        }
        //set up a lambda to reload customerDivisionComboBox when a different country is chosen.
        customerCountryComboBox.setOnAction(updateCountry ->{
            String selDiv = "Select State/Province";
            String chosenCountry = customerCountryComboBox.getValue();
            fillDivisionComboBox(chosenCountry, selDiv);
        });
    }

    /**
     * Fills/sets the textField and ComboBox values for the customer information form inputs with the data from the selected row.
     * @param selectedRow   the model of the data for the specifically selected row
     */
    private void fillTextBoxes(CustomerFormTableData selectedRow){
        //populate all the text boxes
        customerIdLbl.setText(selectedRow.getCCustomerId());
        customerNameTxt.setText(selectedRow.getCCustomerName());
        customerAddressTxt.setText(selectedRow.getCAddress());
        customerPostalCodeTxt.setText(selectedRow.getCPostalCode());
        customerPhoneTxt.setText(selectedRow.getCPhone());
        //the following two will be changed to combo boxes
        String selectedCountry = selectedRow.getCCountry();
        String selectedDivision = selectedRow.getCDivision();
        fillCountryComboBox(selectedCountry);
        fillDivisionComboBox(selectedCountry, selectedDivision);

    }

    /**
     * Fills a comboBox with a sorted list of all of the country names and sets the initial value based on which row/customer
     * has been selected from the tableView and which action has been selected for the form.
     * @param selectedCountry   a string representation of the country name from the selected customer row
     */
    private void fillCountryComboBox(String selectedCountry){
        customerCountryComboBox.getItems().clear();
        switch(whatAction){
            case "Update":
            case "Delete":
                customerCountryComboBox.setValue(selectedCountry);
                break;
            default:
                customerCountryComboBox.setValue("Select Country");
                break;
        }
        customerCountryComboBox.getItems().addAll(CountryImpl.getAllCountryNames().sorted());

    }

    /**
     * Enables and fills a comboBox with a sorted list of all of the first-level-division names - states or provinces -
     * filtering for which country has been selected from the country comboBox and sets the initial value in the Division
     * comboBox based on which the selectedDivision passed to the method.
     * @param selectedCountry   a string representation of the country to first-level-division should be filtered for
     * @param selectedDivision  a string representation of the value to be the initial value in the division comboBox
     */
    private void fillDivisionComboBox(String selectedCountry, String selectedDivision){

        if (customerDivisionComboBox.isDisabled()){
            customerDivisionComboBox.setDisable(false);
        }
        customerDivisionComboBox.getItems().clear();
        customerDivisionComboBox.setValue(selectedDivision);
        customerDivisionComboBox.getItems().addAll(firstLevelDivisionDaoImpl.getDivisionNamesForCountry(selectedCountry).sorted());

    }

    /**
     * Fills the textFields and comboBoxes with prompts for the Add action of the customer information view and sets up
     * via lambda expression what to do to fill the division comboBox when the country comboBox is updated.
     * @param nextCustomerId    the string representation of the number of the next customer ID
     */
    private void fillPromptsForAdd(String nextCustomerId){
        //fill textFields with prompts
        customerIdLbl.setText(nextCustomerId);
        customerNameTxt.setPromptText("Enter a new Name");
        customerAddressTxt.setPromptText("Enter a new Address");
        customerPostalCodeTxt.setPromptText("Enter a new Postal Code");
        customerPhoneTxt.setPromptText("Enter a new Phone Number");
        fillCountryComboBox("Select Country");

        //set up to reload customerDivisionComboBox when a different country is chosen.
        customerCountryComboBox.setOnAction(updateCountry ->{
            fillDivisionComboBox(customerCountryComboBox.getValue(), "Select State/Province");
        });

        customerDivisionComboBox.setDisable(true); //disable Division textfield on first load
    }

    /**
     * Sets all input fields - textFields and comboBoxes - to an uneditable state.
     * This is used when deleting a customer so the information can be seen but no attempt to change individual
     * pieces of information can be made.
     */
    private void noEditTextBoxes(){
        customerNameTxt.setEditable(false);
        customerAddressTxt.setEditable(false);
        customerPostalCodeTxt.setEditable(false);
        customerPhoneTxt.setEditable(false);
        //the following two will be changed to combo boxes
        customerDivisionComboBox.setEditable(false);
        customerDivisionComboBox.getItems().clear();
        customerCountryComboBox.setEditable(false);
        customerCountryComboBox.getItems().clear();
    }

    /**
     * Returns the string representation of the next customer ID number which is determined by referencing the
     * customerss table in the database.
     * @return String   This returns the string representation of the the next Customer ID from the database.
     */
    private String findNextCustomerID(){
        int nextCustomerId = DAO.DatabaseConnection.getNextTablePrimaryKey("customers");
        return String.valueOf(nextCustomerId);

    }

    /**
     * Sequence of what actions should occur when the customer form action button is clicked.
     * Add action creates the new customer, attempts to add it to the database: success displays a message while failure
     * throws an exception, calls a method that refreshes/refills the customer tableView, calls a method that clears
     * the customer form inputs, and calls a method that fills the prompts for the customer form inputs.
     * Update action creates an update customer, attempts to update it in the database: success or failure displays a respective
     * Alert message, calls a method that refreshes/refills the customer tableView, calls a method that clears
     * the customer form inputs, and calls a method that clears all input fields on the form.
     * Delete action gets the customer ID number to delete from the label in the form input section, attempts to
     * delete all of the selected customer information from the database: success or failure displays a respective
     * Alert message, calls a method that refreshes/refills the customer tableView, and calls a method that clears
     * all input fields on the form.
     */
    @FXML
    private void actionButtonClicked(){

        switch (whatAction) {
            case "Add":
                Customer inputCustomer = readInCustomerValues(whatAction);
                // SQL call to add (insert) this customer to database
                if (inputCustomer != null) {
                    if (CustomerImpl.addCustomer(inputCustomer)) {
                        Alert addSuccessAlert = new Alert(Alert.AlertType.INFORMATION);
                        addSuccessAlert.setHeaderText("Add Successful");
                        addSuccessAlert.setContentText("New customer was added successfully.");
                        addSuccessAlert.show();
                    }
                    fillCustomerTable();
                    clearFormInput();
                    fillPromptsForAdd(findNextCustomerID());
                }
                break;
            case "Update":
                //read in values for updated entry
                Customer modifiedCustomer = readInCustomerValues(whatAction);
                int customerIdToUpdate = -1;
                if (!customerIdLbl.getText().isEmpty()) {
                    customerIdToUpdate = Integer.parseInt((customerIdLbl.getText().trim()));
                    if (CustomerImpl.updateCustomer(modifiedCustomer)) {
                        Alert updateSuccessAlert = new Alert(Alert.AlertType.INFORMATION);
                        updateSuccessAlert.setHeaderText("Update Successful");
                        updateSuccessAlert.setContentText("Customer ID: " + customerIdToUpdate + " was updated successfully.");
                        updateSuccessAlert.show();
                    } else {
                        Alert updateFailAlert = new Alert(Alert.AlertType.INFORMATION);
                        updateFailAlert.setHeaderText("Update Failed");
                        updateFailAlert.setContentText("Appointment ID: " + customerIdToUpdate + " was not updated.");
                        updateFailAlert.show();
                        //update to database
                    }
                } else {
                    Alert updateFailAlert = new Alert(Alert.AlertType.INFORMATION);
                    updateFailAlert.setHeaderText("Update Failed");
                    updateFailAlert.setContentText("Customer ID was not found.\nPlease select a customer from the table");
                    updateFailAlert.show();
                }

                fillCustomerTable();
                clearFormInput();
                break;
            case "Delete":
                if (customerIdLbl.getText().equals("")){
                    Alert cannotDeleteAlert = new Alert(Alert.AlertType.WARNING);
                    cannotDeleteAlert.setHeaderText("Delete Unsuccessful");
                    cannotDeleteAlert.setContentText("Please select a Customer to delete from the list");
                    cannotDeleteAlert.show();
                } else {
                    customerToDelete = Integer.parseInt(customerIdLbl.getText());
                    //determine if selected customer has appointments
                    //if so, open Appointments screen with selected customer's appointments only (to manually delete)
                    if (!isAppointmentListEmpty(customerToDelete)) {
                        deleteCustomerAppointments(customerToDelete);
                        //also, open an alert dialog to provide instructions on why the appointments must be deleted first
                        Alert appointmentAlert = new Alert(Alert.AlertType.INFORMATION);
                        appointmentAlert.setContentText("The Customer's Appointments must be deleted before the Customer " +
                                "Information can be deleted.");
                        appointmentAlert.initModality(Modality.APPLICATION_MODAL);
                        appointmentAlert.setGraphic(null);
                        appointmentAlert.setHeaderText("Notice for Deleting");
                        appointmentAlert.show();
                        //close both appointments screen and alert dialog once the appointments are deleted
                    } else if (isAppointmentListEmpty(customerToDelete)) {
                        //delete customer in database
                        if (CustomerImpl.deleteCustomer(customerToDelete)) {
                            Alert deleteSuccessAlert = new Alert(Alert.AlertType.INFORMATION);
                            deleteSuccessAlert.setHeaderText("Customer Deleted");
                            deleteSuccessAlert.setContentText("Customer Id: " + customerToDelete + " was successfully deleted.");
                            deleteSuccessAlert.setGraphic(null);
                            deleteSuccessAlert.show();
                            customerToDelete = -1;
                        }

                    }
                    //refresh the tableview and form inputs
                    fillCustomerTable();
                    clearFormInput();
                }
                break;
            default:
                Alert invalidAction = new Alert(Alert.AlertType.WARNING);
                invalidAction.setHeaderText("Invalid action requested");
                invalidAction.setContentText("Invalid action: " + actionBtn + "\n This should not be possible");
                invalidAction.show();
                break;
            };

    }

    /**
     * Determines whether the customer selected to be deleted does or does not have any scheduled appointments.
     * In this case true means the list is empty - has no appointments, false means there are appointments for
     * the selected customer.
     * @param customerToDelete  an int representation of the customer selected to search for associated appointments
     * @return boolean          true means the list of appointments is empty - no appointments exist, false means the
     *                          appointment list is not empty - the customer has scheduled appointments
     */
    private boolean isAppointmentListEmpty(int customerToDelete){
        ObservableList<Integer> allAppointmentIds = AppointmentImpl.getAllAppointmentIdsForCustomer(customerToDelete);
        return allAppointmentIds.isEmpty();
    }

    /**
     *Returns a single Customer created from the data collected and validated from the form inputs.
     * Reads in values from textFields and comboBoxes making sure no values are empty. Displays appropriate Alert dialog
     * messages for input issues that need addressed.
     * @param actionButtonValue a string representation of what action the form is supposed to perform
     * @return  Customer     this returns an Customer created by the data in the form input fields
     */
    private Customer readInCustomerValues(String actionButtonValue){
        boolean isNameValidFlag = true;
        boolean isAddressValidFlag = true;
        boolean isPostalCodeValidFalg = true;
        boolean isPhoneValidFlag = true;
        boolean isCountryValidFlag = true;
        boolean isDivisionValidFlag = true;
        boolean isFirstInputErrorFlag = true;
        String inCustomerName = "";
        String inCustomerAddress = "";
        String inCustomerPostalCode = "";
        String inCustomerPhone = "";
        String inputErrors = "";
        int inCustomerDivisionId = -1;
        Customer customerToModify;

        //Read Values in from text & combo boxes
        if (customerNameTxt.getText().isEmpty()){
            highlightTextFieldForInput(customerNameTxt);
            isNameValidFlag = false;
            //build string of input errors for alert dialog
            inputErrors = inputErrors + "Customer Name";
            isFirstInputErrorFlag = false;

        }else{
            inCustomerName = customerNameTxt.getText().trim();
        }
        if (customerAddressTxt.getText().isEmpty()){
            highlightTextFieldForInput(customerAddressTxt);
            isAddressValidFlag = false;
            if (isFirstInputErrorFlag){
                inputErrors = inputErrors + "Address";
                isFirstInputErrorFlag = false;
            } else {
                inputErrors = inputErrors + "\nAddress";
            }
        }else {
            inCustomerAddress = customerAddressTxt.getText().trim();
        }
        if (customerPostalCodeTxt.getText().isEmpty()){
            highlightTextFieldForInput(customerPostalCodeTxt);
            isPostalCodeValidFalg = false;
            if (isFirstInputErrorFlag){
                inputErrors = inputErrors + "Postal Code";
                isFirstInputErrorFlag = false;
            } else {
                inputErrors = inputErrors + "\nPostal Code";
            }
        }else {
            inCustomerPostalCode = customerPostalCodeTxt.getText().trim();
        }
        if (customerPhoneTxt.getText().isEmpty()){
            highlightTextFieldForInput(customerPhoneTxt);
            isPhoneValidFlag = false;
            if (isFirstInputErrorFlag){
                inputErrors = inputErrors + "Phone";
                isFirstInputErrorFlag = false;
            } else {
                inputErrors = inputErrors + "\nPhone";
            }
        }else {
            inCustomerPhone = customerPhoneTxt.getText().trim();
        }

        if (customerCountryComboBox.getValue().equals("Select Country")){
            highlightComboBoxForInput(customerCountryComboBox);
            isCountryValidFlag = false;
            if (isFirstInputErrorFlag){
                inputErrors = inputErrors + "Country";
                isFirstInputErrorFlag = false;
            } else {
                inputErrors = inputErrors + "\nCountry";
            }
        }
        if (customerDivisionComboBox.isDisabled() || customerDivisionComboBox.getValue().equals("Select State/Province") ||
                customerDivisionComboBox.getValue()==null || customerDivisionComboBox.getValue().equals("")){
            highlightComboBoxForInput(customerDivisionComboBox);
            isDivisionValidFlag = false;
            if (isFirstInputErrorFlag){
                inputErrors = inputErrors + "Division";
                isFirstInputErrorFlag = false;
            } else {
                inputErrors = inputErrors + "\nDivision";
            }
        }else {
            inCustomerDivisionId = DAO.firstLevelDivisionDaoImpl.getDivisionIdFromName(String.valueOf(customerDivisionComboBox.getValue()));
        }


        if (isNameValidFlag && isAddressValidFlag && isPostalCodeValidFalg && isPhoneValidFlag && isCountryValidFlag
                && isDivisionValidFlag) {
            int inCustomerId = Integer.parseInt(customerIdLbl.getText());

            //only update the "Created" values on an "add"
            LocalDateTime inCustomerCreated;
            String inCustomerCreatedBy;
            if (actionButtonValue.equals("Add")) {
                inCustomerCreated = LocalDateTime.now();
                inCustomerCreatedBy = usernameInput;
            } else {
                //Flag with an invalid date/time
                inCustomerCreated = LocalDateTime.of(9999, 12,31,00,00,00);
                inCustomerCreatedBy = "";
            }
            //updated and updated by are always read in
            LocalDateTime inCustomerUpdated = LocalDateTime.now();
            String inCustomerUpdatedBy = usernameInput;

            customerToModify = new Customer(inCustomerId, inCustomerName, inCustomerAddress, inCustomerPostalCode,
                    inCustomerPhone, inCustomerCreated, inCustomerCreatedBy, inCustomerUpdated, inCustomerUpdatedBy,
                    inCustomerDivisionId);
        } else {
            customerToModify = null;
            Alert readInErrorsAlert = new Alert(Alert.AlertType.WARNING);
            readInErrorsAlert.setHeaderText("Incomplete/Invalid Input");
            readInErrorsAlert.setContentText("Please correct inputs for: \n" + inputErrors + "\nand retry 'ADD CUSTOMER'");
            readInErrorsAlert.show();
        }

        return customerToModify;
    }

    /**
     * Highlights a single textField by changing its border color to red and changing its prompt to "Required Input".
     * @param highlightTextField    the textField that is to have its display altered
     */
    private void highlightTextFieldForInput(TextField highlightTextField){
        highlightTextField.setPromptText("Input Required");
        highlightTextField.setStyle("-fx-border-color:red");
    }

    /**
     * Highlights a single comboBox by changing its border color to red and changing its prompt to "Selection Required".
     * @param highlightComboBox   the comboBox that is to have its display altered
     */
    private void highlightComboBoxForInput(ComboBox highlightComboBox){
        highlightComboBox.setPromptText("Selection Required");
        highlightComboBox.setStyle("-fx-border-color:red");
    }

    /**
     * Clears all values, prompts, and styles for all appointments form inputs.
     */
    private void clearFormInput(){
        customerIdLbl.setText("");
        customerNameTxt.setText("");
        customerNameTxt.setStyle("");
        customerAddressTxt.setText("");
        customerAddressTxt.setStyle("");
        customerPostalCodeTxt.setText("");
        customerPostalCodeTxt.setStyle("");
        customerPhoneTxt.setText("");
        customerPhoneTxt.setStyle("");
        //the following two will be changed to combo boxes
        customerCountryComboBox.setValue("");
        customerCountryComboBox.getItems().clear();
        customerCountryComboBox.setPromptText(null);
        customerCountryComboBox.setStyle("");
        customerDivisionComboBox.setValue("");
        customerDivisionComboBox.getItems().clear();
        customerDivisionComboBox.setPromptText(null);
        customerDivisionComboBox.setStyle("");

    }

    /**
     * Loads/Opens the appointments form when a customer selected to be deleted has appointment that also need deleted.
     * @param customerToDelete  an int representation of the customer selected to be deleted.
     */
    @FXML
    public void deleteCustomerAppointments(int customerToDelete) {
        Stage stage = new Stage();
        Parent root;
        try {
            root = FXMLLoader.load(getClass().getResource("/view/appointments.fxml"));
            stage.setTitle("Global Consulting Reports");
            stage.setScene(new Scene(root, 900, 800));
            levelThreeFormStage = stage;
            stage.show();
        } catch (IOException exception){
            System.out.println("Error load the appointment screen. See stack trace.");
            exception.printStackTrace();
        }
    }


}


