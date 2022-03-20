package controller;


import DAO.*;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import model.Appointment;
import model.AppointmentFormTableData;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;

import static controller.LoginController.usernameInput;
import static controller.MainMenuController.levelThreeFormStage;
import static controller.CustomerInformationController.customerToDelete;
import static controller.MainMenuController.*;


/**
 * Provides all functionality - view appointments, filter appointments view, add appointment, update appointment,
 * delete appointment with input validation, and closes the form - for the appointments javafx file.
 * @author Margaret Chrysler
 * @version 1.6
 */

public class AppointmentsController implements Initializable {

    @FXML
    private ToggleGroup appointmentFilterGroup;
    @FXML
    private RadioButton viewAllRadioButton;
    @FXML
    private RadioButton viewWeekRadioButton;
    @FXML
    private RadioButton viewMonthRadioButton;
    @FXML
    private TableView<AppointmentFormTableData> appointmentTableView;
    @FXML
    private TableColumn<AppointmentFormTableData, String> ID_ApptTblCol;
    @FXML
    private TableColumn<AppointmentFormTableData, String> title_ApptTblCol;
    @FXML
    private TableColumn<AppointmentFormTableData, String> desc_ApptTblCol;
    @FXML
    private TableColumn<AppointmentFormTableData, String> location_ApptTblCol;
    @FXML
    private TableColumn<AppointmentFormTableData, String> type_ApptTblCol;
    @FXML
    private TableColumn<AppointmentFormTableData, String> startDateTime_ApptTblCol;
    @FXML
    private TableColumn<AppointmentFormTableData, String> endDateTime_ApptTblCol;
    @FXML
    private TableColumn<AppointmentFormTableData, String> contactName_ApptTblCol;
    @FXML
    private TableColumn<AppointmentFormTableData, String> customerId_ApptTblCol;

    //text boxes & user interfaces
    @FXML
    private Label appointmentIdLbl;
    @FXML
    private TextField titleTxt;
    @FXML
    private TextField descriptionTxt;
    @FXML
    private TextField locationTxt;
    @FXML
    private ComboBox<String> contactNameComboBox;
    @FXML
    private TextField typeTxt;
    @FXML
    private DatePicker appointmentDatePick;
    @FXML
    private Spinner<LocalTime> startTimeSpinner;
    @FXML
    private Label startSpinner12HourLbl;
    @FXML
    private Spinner<LocalTime> endTimeSpinner;
    @FXML
    private Label endSpinner12HourLbl;
    @FXML
    private ComboBox<String> customerIdComboBox;


    @FXML
    Button actionBtn;
    @FXML
    Label subTitleLbl;
    @FXML
    Button closeFormBtn;


    private ObservableList<AppointmentFormTableData> tableViewList = FXCollections.observableArrayList();
    AppointmentFormTableData selectedRow;

    /**
     * Closes the appointments stage when the 'Close Appointments Form' button in the appointments view is clicked and
     * resets the static variable customerToDelete back to 0.
     */
    @FXML
    private void closeFormButtonClicked() {
        customerToDelete = 0;
        if (levelThreeFormStage == null) {
            levelTwoFormStage.close();
        } else {
            levelThreeFormStage.close();
        }
    }

    /**
     * Fills appointments with initial values based on the selected action from Main Menu.
     * Ties (binds) the actionBtn and subTitleLbl to what action was chose for the form to complete. Fills the tableView
     * with appointments from the database. Fills the textFields with prompts if the selected action was Add. Sets up
     * what to do when the toggle group's radio buttons are clicked, what to do when the tableView has a row that
     * is clicked or selected, and how to update the 12-hour display label for the spinner inputs using lambda functions.
     * The lambda expressions in this method are used to set up code that executes after specific user actions, such
     * as when a radio button for the view is chosen, when a row is selected in the appointments tableView, or when
     * the time is changed in either of the spinners.
     * @param url               the url for the view being initialized
     * @param resourceBundle    the resource bundle for the view being initialized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        actionBtn.textProperty().bind(Bindings.createStringBinding(() -> whatAction + " Appointment"));
        subTitleLbl.textProperty().bind(Bindings.createStringBinding(() -> whatAction + " Appointments Form"));

        setupAppointmentTable();    //tableview columns setup
        fillAppointmentTable(((RadioButton) appointmentFilterGroup.getSelectedToggle()).getText());     //fill the tableview (default toggle button is "view all")

        //Add does not fill any of the textFields, but does prompt
        if (whatAction.equals("Add")) {
            fillPromptsForAdd(findNextAppointmentId());
            appointmentTableView.setSelectionModel(null);
        }

        //Waiting for changes within toggle group
        viewAllRadioButton.setOnAction(s -> {
            fillAppointmentTable(((RadioButton) appointmentFilterGroup.getSelectedToggle()).getText());
        });
        viewWeekRadioButton.setOnAction(s -> {
            fillAppointmentTable(((RadioButton) appointmentFilterGroup.getSelectedToggle()).getText());
        });
        viewMonthRadioButton.setOnAction(s -> {
            fillAppointmentTable(((RadioButton) appointmentFilterGroup.getSelectedToggle()).getText());
        });


        //set up row factory to initiate filling textFields when a tableveiw row is selected
        appointmentTableView.setRowFactory((rf -> {
            TableRow<AppointmentFormTableData> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY) {
                    selectedRow = row.getItem();
                    appointmentTableRowClicked(selectedRow);
                }
            });
            return row;
        }));

        startTimeSpinner.setOnMouseClicked((l -> {
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
            startSpinner12HourLbl.setText("(" + timeFormat.format(startTimeSpinner.getValue()) + ")");
        }));

        endTimeSpinner.setOnMouseClicked((l -> {
            DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
            endSpinner12HourLbl.setText("(" + timeFormat.format(endTimeSpinner.getValue()) + ")");
        }));
    }

    /**
     * Establishes the links between the tableView columns/cells and the AppointmentFormTableData model using PropertyValueFactory.
     */
    private void setupAppointmentTable() {

        ID_ApptTblCol.setCellValueFactory(new PropertyValueFactory<>("aId"));
        title_ApptTblCol.setCellValueFactory(new PropertyValueFactory<>("aTitle"));
        desc_ApptTblCol.setCellValueFactory(new PropertyValueFactory<>("aDescription"));
        location_ApptTblCol.setCellValueFactory(new PropertyValueFactory<>("aLocation"));
        contactName_ApptTblCol.setCellValueFactory(new PropertyValueFactory<>("aContactName"));
        type_ApptTblCol.setCellValueFactory(new PropertyValueFactory<>("aType"));
        startDateTime_ApptTblCol.setCellValueFactory(new PropertyValueFactory<>("aStartDateTime"));
        endDateTime_ApptTblCol.setCellValueFactory(new PropertyValueFactory<>("aEndDateTime"));
        customerId_ApptTblCol.setCellValueFactory(new PropertyValueFactory<>("aCustomerId"));

    }

    /**
     * Fills the appointment tableView with refreshed data and determines if it is appropriate for the toggle group
     * to be active. TableView is cleared of old data first. Toggle group is disabled if appointments view is accessed
     * as a part of deleting a customer which would need all associated appointments deleted first. Gets the appropriate
     * table data base on either customer being deleted or which toggle button is chosen. Sets and displays data in tableView.
     * @param filterInput   the string indicating if the viewed data should be all, for this week, or for this month
     */
    private void fillAppointmentTable(String filterInput) {

        //clear the list
        tableViewList.removeAll(tableViewList);

        //if the Form is called while deleting a Customer
        if (customerToDelete > 0) {
            //disable toggle group
            viewAllRadioButton.setDisable(true);
            viewWeekRadioButton.setDisable(true);
            viewMonthRadioButton.setDisable(true);
            //get the item data in the tableview
            tableViewList = AppointmentImpl.getAppointmentFormTableData(customerToDelete);
        } else {
            //if the Form is called directly from Main Menu, get item data using toggle group filter
            tableViewList = AppointmentImpl.getAppointmentFormTableData(filterInput);
        }
        appointmentTableView.setItems(tableViewList);
        //Display once items are set
        appointmentTableView.setVisible(true);

    }

    /**
     * Actions to be completed when a row is selected in the populated tableView based on which action is to be performed
     * by the appointment form.
     * Calls a method that clears the tableView and a method that fills data in textFields. When the form action is Update,
     * calls a method that fills the comboBoxes values with selection options. When form action is Delete, calls a method
     * that fills comboBox values only then calls a method that sets all textFields and comboBoxes to uneditable.
     * @param selectedRow   the model of the data for the specifically selected row
     */
    @FXML
    private void appointmentTableRowClicked(model.AppointmentFormTableData selectedRow) {
        switch (whatAction) {
            case "Update":
                //load information to textFields
                clearFormInput();
                fillTextBoxes(selectedRow);
                fillCustomerComboBox();
                fillContactComboBox();
                break;
            case "Delete":
                //load information to textFields
                clearFormInput();
                fillTextBoxes(selectedRow);
                contactNameComboBox.setValue(selectedRow.getAContactName());
                customerIdComboBox.setValue(selectedRow.getACustomerId());
                //make textFields uneditable
                noEditTextBoxes();
                break;
            default:
                break;
        }
    }


    /**
     * Fills/sets the text for the appointment form inputs with the data from the selected row.
     * @param selectedRow   the model of the data for the specifically selected row
     */
    private void fillTextBoxes(model.AppointmentFormTableData selectedRow) {
        //populate all the text boxes
        appointmentIdLbl.setText(selectedRow.getAId());
        titleTxt.setText(selectedRow.getATitle());
        descriptionTxt.setText(selectedRow.getADescription());
        locationTxt.setText(selectedRow.getALocation());
        typeTxt.setText(selectedRow.getAType());
        appointmentDatePick.setValue(selectedRow.getALocalDate());

        fillStartSpinner();
        fillEndSpinner();

    }

    /**
     * Fills a comboBox with a sorted list of all of the contact's names and sets the initial value based on what
     * appointment form action has been selected.
     */
    private void fillContactComboBox() {
        //fill contacts combo box
        contactNameComboBox.getItems().clear();
        contactNameComboBox.getItems().addAll(ContactImpl.getAllContactNames().sorted());
        if (whatAction.equals("Add")) {
            contactNameComboBox.setValue("Select Contact");
        } else if (whatAction.equals("Update") || whatAction.equals("Delete")) {
            contactNameComboBox.setValue(selectedRow.getAContactName());
        }
    }

    /**
     * Fills a comboBox with a sorted list of all the customer IDs and sets the initial value base on what
     * appointment form action has been selected.
     */
    private void fillCustomerComboBox() {
        //fill customerId combo box
        customerIdComboBox.getItems().clear();
        customerIdComboBox.getItems().addAll(CustomerImpl.getAllCustomerIdsString().sorted());
        if (whatAction.equals("Add")) {
            customerIdComboBox.setValue("Select Customer");
        } else if (whatAction.equals("Update") || whatAction.equals("Delete")) {
            customerIdComboBox.setValue(selectedRow.getACustomerId());
        }
    }

    /**
     * Fills a spinner with a sorted list of potential start times for appointments base on 24-hour clock.
     * Minimum value is 08:00 AM and maximum value is 09:00 PM. Initial value depends on the selected action for
     * the appointment form.
     */
    private void fillStartSpinner() {
        //set up factory with limited potential office hours
        SpinnerValueFactory<LocalTime> factory = new SpinnerValueFactory<>() {
            final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
            final LocalTime minimumValue = LocalTime.parse("08:00 AM", timeFormat);
            final LocalTime maximumValue = LocalTime.parse("10:00 PM", timeFormat);

            {
                //set the initial value and it's associated 12-hour clock label Text
                setValue(findStartTime());
                startSpinner12HourLbl.setText("(" + timeFormat.format(findStartTime()) + ")");
            }

            //determine initial start time value as determined by form's selected action
            private LocalTime findStartTime() {
                LocalTime timeValue = null;
                if (whatAction.equals("Add")) {
                    timeValue = minimumValue;
                } else if (whatAction.equals("Update") || whatAction.equals("Delete")) {
                    timeValue = selectedRow.getAStartTime();
                }
                return timeValue;
            }

            //set up what to do on decrement
            @Override
            public void decrement(int steps) {
                LocalTime value = getValue();
                if (value == null) {
                    setValue(findStartTime());
                } else if (value.minusHours(steps).isAfter(minimumValue)) {
                    setValue(value.minusHours(steps));
                }

            }

            //set up what to do on increment
            @Override
            public void increment(int steps) {
                LocalTime value = getValue();
                if (value == null) {
                    setValue(findStartTime());
                } else if (value.plusHours(steps).isBefore(maximumValue)) {
                    setValue(value.plusHours(steps));
                }

            }

        };
        //set up the information in the actual spinner
        startTimeSpinner.setDisable(false);
        startTimeSpinner.setValueFactory(factory);

    }

    /**
     * Fills a spinner with a sorted list of potential start times for appointments base on 24-hour clock.
     * Minimum value is 08:00 AM and maximum value is 09:00 PM. Initial value depends on the selected action for
     * the appointment form.
     */
    private void fillEndSpinner() {
        //set up factory with limited potential office hours
        SpinnerValueFactory<LocalTime> factory = new SpinnerValueFactory<>() {
            final DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault());
            final LocalTime minimumValue = LocalTime.parse("08:00 AM", timeFormat);
            final LocalTime maximumValue = LocalTime.parse("10:00 PM", timeFormat);

            {
                //set the initial value and it's associated 12-hour clock label Text
                setValue(findEndTime());
                endSpinner12HourLbl.setText("(" + timeFormat.format(findEndTime()) + ")");
            }

            //determine initial start time value as determined by form's selected action
            private LocalTime findEndTime() {
                LocalTime timeValue = null;
                if (whatAction.equals("Add")) {
                    timeValue = maximumValue;
                } else if (whatAction.equals("Update") || whatAction.equals("Delete")) {
                    timeValue = selectedRow.getAEndTime();
                }
                return timeValue;
            }
            //set up what to do on decrement
            @Override
            public void decrement(int steps) {
                LocalTime value = getValue();
                if (value == null) {
                    setValue(findEndTime());
                } else if (value.minusHours(steps).isAfter(minimumValue)) {
                    setValue(value.minusHours(steps));
                }
            }
            //set up what to do on increment
            @Override
            public void increment(int steps) {
                LocalTime value = getValue();
                if (value == null) {
                    setValue(findEndTime());
                } else if (value.plusHours(steps).isBefore(maximumValue)) {
                    setValue(value.plusHours(steps));
                }
            }
        };
        //set up the information in the actual spinner
        endTimeSpinner.setDisable(false);
        endTimeSpinner.setValueFactory(factory);
    }

    /**
     * Fills the textFields, comboBoxes, and spinners with prompts for the Add action of the appointment view.
     * @param nextAppointmentId the string representation of the number of the next appointment ID
     */
    private void fillPromptsForAdd(String nextAppointmentId) {
        //fill textFields with prompts
        appointmentIdLbl.setText(nextAppointmentId);
        titleTxt.setPromptText("Enter title");
        descriptionTxt.setPromptText("Enter a description");
        locationTxt.setPromptText("Enter location");
        typeTxt.setPromptText("Enter appointment type");
        fillCustomerComboBox();
        fillContactComboBox();
        fillStartSpinner();
        fillEndSpinner();

    }

    /**
     * Returns the string representation of the next appointment ID number which is determined by referencing the
     * appointments table in the database.
     * @return String   This returns the string representation of the the next Appointment ID from the database.
     */
    private String findNextAppointmentId() {
        int nextAppointmentId = DAO.DatabaseConnection.getNextTablePrimaryKey("appointments");
        return String.valueOf(nextAppointmentId);
    }

    /**
     * Sets all input fields - textFields, comboBoxes, datePicker, and spinners - to an uneditable state.
     * This is used when deleting an appointment so the information can be seen but no attempt to change individual
     * pieces of information can be made.
     */
    private void noEditTextBoxes() {
        titleTxt.setEditable(false);
        descriptionTxt.setEditable(false);
        locationTxt.setEditable(false);
        typeTxt.setEditable(false);
        contactNameComboBox.setEditable(false);
        customerIdComboBox.setEditable(false);
        appointmentDatePick.setEditable(false);
        startTimeSpinner.setEditable(false);
        endTimeSpinner.setEditable(false);
    }

    /**
     * Clears all values, prompts, and styles for all appointments form inputs.
     */
    private void clearFormInput() {
        appointmentIdLbl.setText("");
        titleTxt.clear();
        titleTxt.setPromptText("");
        titleTxt.setStyle("");
        descriptionTxt.clear();
        descriptionTxt.setPromptText("");
        descriptionTxt.setStyle("");
        locationTxt.clear();
        locationTxt.setPromptText("");
        locationTxt.setStyle("");
        typeTxt.clear();
        typeTxt.setPromptText("");
        typeTxt.setStyle("");
        customerIdComboBox.setValue("");
        customerIdComboBox.setPromptText(null);
        customerIdComboBox.setStyle("");
        contactNameComboBox.setValue("");
        contactNameComboBox.setPromptText(null);
        contactNameComboBox.setStyle("");
        startTimeSpinner.setStyle("");
        startTimeSpinner.setDisable(true);
        endTimeSpinner.setStyle("");
        endTimeSpinner.setDisable(true);
        appointmentDatePick.setValue(LocalDate.now());
        appointmentDatePick.setPromptText("Select a Date");
        appointmentDatePick.setStyle("");
        startSpinner12HourLbl.setText("");
        endSpinner12HourLbl.setText("");

    }

    /**
     * Sequence of what actions should occur when the appointment form action button is clicked.
     * Add action creates the new appointment, attempts to add it to the database: success displays a message while failure
     * throws an exception, calls a method that refreshes/refills the appointment tableView, calls a method that clears
     * the appointment form inputs, and calls a method that fills the prompts for the appointment form inputs.
     * Update action creates the new appointment, attempts to update it in the database: success or failure displays a respective
     * Alert message, calls a method that refreshes/refills the appointment tableView, calls a method that clears
     * the appointment form inputs, and calls a method that clears all input fields on the form.
     * Delete action gets the appointment ID number to delete from the label in the form input section, attempts to
     * delete all of the selected appointment information from the database: success or failure displays a respective
     * Alert message, calls a method that refreshes/refills the appointment tableView, and calls a method that clears
     * all input fields on the form.
     */
    @FXML
    private void actionButtonClicked() {

        switch (whatAction) {
            case "Add":
                Appointment inputAppointment = readInAppointmentValues(whatAction);
                // SQL call to add (insert) this customer
                if (inputAppointment != null) {
                    if (AppointmentImpl.addAppointment(inputAppointment)) {
                        Alert deleteSuccessAlert = new Alert(Alert.AlertType.INFORMATION);
                        deleteSuccessAlert.setHeaderText("Add Successful");
                        deleteSuccessAlert.setContentText("New appointment was added successfully.");
                        deleteSuccessAlert.show();
                    }
                    fillAppointmentTable(((RadioButton) appointmentFilterGroup.getSelectedToggle()).getText());
                    clearFormInput();
                    fillPromptsForAdd(findNextAppointmentId());

                }
                break;
            case "Update":

                Appointment changeAppointment = readInAppointmentValues(whatAction);
                if (changeAppointment!=null) {
                    //update to database
                    int appointmentIdToUpdate = Integer.parseInt((appointmentIdLbl.getText().trim()));
                    if (AppointmentImpl.updateAppointment(changeAppointment)) {
                        Alert updateSuccessAlert = new Alert(Alert.AlertType.INFORMATION);
                        updateSuccessAlert.setHeaderText("Update Successful");
                        updateSuccessAlert.setContentText("Appointment ID: " + appointmentIdToUpdate + " was updated successfully.");
                        updateSuccessAlert.show();
                    } else {
                        Alert updateFailAlert = new Alert(Alert.AlertType.INFORMATION);
                        updateFailAlert.setHeaderText("Update Failed");
                        updateFailAlert.setContentText("Appointment ID: " + appointmentIdToUpdate + " was not updated.");
                        updateFailAlert.show();
                    }
                    //refresh the screen
                    fillAppointmentTable(((RadioButton) appointmentFilterGroup.getSelectedToggle()).getText());
                    clearFormInput();
                }
                break;
            case "Delete":
                //get ID of appointment to delete from selected appointmentId label
                int appointmentIdToDelete = Integer.parseInt(appointmentIdLbl.getText().trim());
                //delete customer in database
                if (AppointmentImpl.deleteAppointment(appointmentIdToDelete)) {
                    Alert deleteSuccessAlert = new Alert(Alert.AlertType.INFORMATION);
                    deleteSuccessAlert.setHeaderText("Delete Successful");
                    deleteSuccessAlert.setContentText("Appointment ID: " + appointmentIdToDelete + " was deleted successfully.");
                    deleteSuccessAlert.show();
                    fillAppointmentTable(((RadioButton) appointmentFilterGroup.getSelectedToggle()).getText());
                    clearFormInput();
                } else {
                    Alert deleteFailAlert = new Alert(Alert.AlertType.INFORMATION);
                    deleteFailAlert.setHeaderText("Delete Failed");
                    deleteFailAlert.setContentText("Appointment ID: " + appointmentIdToDelete + " was not deleted.");
                    deleteFailAlert.show();
                    fillAppointmentTable(((RadioButton) appointmentFilterGroup.getSelectedToggle()).getText());
                    clearFormInput();
                }

                break;
            default:
                Alert invalidAction = new Alert(Alert.AlertType.WARNING);
                invalidAction.setHeaderText("Invalid action requested");
                invalidAction.setContentText("Invalid action: " + actionBtn + "\n This should not be possible");
                invalidAction.show();
                break;
        }
        ;

    }

    /**
     *Returns a single Appointment created from the data collected and validated from the form inputs.
     * Reads in values from textFields, comboBoxes, datePicker, and spinners making sure no values are empty. Validates
     * the data that is read in making sure there are no overlapping appointment times. Displays appropriate Alert dialog
     * messages for input issues that need addressed.
     * @param actionButtonValue a string represention of what action the form is supposed to perform
     * @return  Appointment     this returns an Appointment created by the data in the form input fields
     */
    private Appointment readInAppointmentValues(String actionButtonValue) {
        boolean isTitleValidFlag = true;
        boolean isDescriptionValidFlag = true;
        boolean isLocationValidFlag = true;
        boolean isTypeValidFlag = true;
        boolean isCustomerIdValidFlag = true;
        boolean isUserIdValidFlag = true;
        boolean isContactIdValidFlag = true;
        boolean isNoCustomerOverlapFlag = false;
        boolean isLocalDateValidFlag = true;

        String inTitle = "";
        String inDescription = "";
        String inLocation = "";
        String inType = "";
        LocalDate inLocalDate = null;
        LocalTime inStartTimeF = null;
        LocalTime inEndTimeF = null;
        LocalDateTime inStartDateTime = null;
        LocalDateTime inEndDateTime = null;
        String inContactName = "";
        int inCustomerId = -1;
        int inUserId;
        int inContactId = -1;

        Appointment appointmentFormInput = null;                   //for add input
        AppointmentFormTableData appointmentTableViewInput; //for update input

        //Read Values in from text & combo boxes
        if (titleTxt.getText().isEmpty()) {
            highlightTextFieldForInput(titleTxt);
            isTitleValidFlag = false;
        } else {
            inTitle = titleTxt.getText().trim();
        }
        if (descriptionTxt.getText().isEmpty()) {
            highlightTextFieldForInput(descriptionTxt);
            isDescriptionValidFlag = false;
        } else {
            inDescription = descriptionTxt.getText().trim();
        }
        if (locationTxt.getText().isEmpty()) {
            highlightTextFieldForInput(locationTxt);
            isLocationValidFlag = false;
        } else {
            inLocation = locationTxt.getText().trim();
        }
        if (typeTxt.getText().isEmpty()) {
            highlightTextFieldForInput(typeTxt);
            isTypeValidFlag = false;
        } else {
            inType = typeTxt.getText().trim();
        }

        if (customerIdComboBox.getValue().equals("Select Customer")) {
            highlightComboBoxForInput(customerIdComboBox);
            isCustomerIdValidFlag = false;
        } else {
            inCustomerId = Integer.parseInt(customerIdComboBox.getValue());
        }
        inUserId = UserImpl.getUserId(usernameInput);
        if (inUserId == -1) {
            Alert badAlert = new Alert(Alert.AlertType.WARNING);
            badAlert.setHeaderText("Stop Working!");
            badAlert.setContentText("This should not be possible.\nPlease call for help.");
            badAlert.show();
            isUserIdValidFlag = false;
        }
        if (contactNameComboBox.getValue().equals("Select Contact")) {
            highlightComboBoxForInput(contactNameComboBox);
            isContactIdValidFlag = false;
        } else {
            inContactName = contactNameComboBox.getValue();
            inContactId = ContactImpl.getContactId(inContactName);
        }

        //Read in Date and time items
        //DatePicker
        if (appointmentDatePick.getValue()==null) {
            highlightPickerForInput();
            isLocalDateValidFlag = false;
        }else{
            inLocalDate = appointmentDatePick.getValue();
        }
        //Start Time
        inStartTimeF = startTimeSpinner.getValue();
        //End time
        inEndTimeF = endTimeSpinner.getValue();
        if (inLocalDate!=null && inStartTimeF!=null && inEndTimeF!=null) {
            inStartDateTime = LocalDateTime.of(inLocalDate, inStartTimeF);
            inEndDateTime = LocalDateTime.of(inLocalDate, inEndTimeF);
        }

        //if all text and combo box input is okay, read in appointment date/time information
        if (isTitleValidFlag && isDescriptionValidFlag && isTypeValidFlag  && isLocationValidFlag &&
                isLocalDateValidFlag && isCustomerIdValidFlag && isUserIdValidFlag && isContactIdValidFlag) {

            //Validate: No overlap for date & times?
            int aId = Integer.parseInt(appointmentIdLbl.getText());     //appointment ID
            if (checkNoCustomerAppointmentOverlap(aId, inCustomerId, inStartDateTime, inEndDateTime)){
                isNoCustomerOverlapFlag = true;

                //get Appointment ID, Created and/or Updated Info, and create the Appointment to return
                int inAppointmentId = Integer.parseInt(appointmentIdLbl.getText());

                //only update the "Created" values on an "add"
                LocalDateTime inAppointmentCreated;
                String inAppointmentCreatedBy;
                if (actionButtonValue.equals("Add")) {
                    inAppointmentCreated = LocalDateTime.now();
                    inAppointmentCreatedBy = usernameInput;
                } else {
                    inAppointmentCreated = null;
                    inAppointmentCreatedBy = null;
                }

                //updated and updated by are always read in
                LocalDateTime inAppointmentUpdated = LocalDateTime.now();
                String inAppointmentUpdatedBy = usernameInput;

                if (whatAction.equals("Add")) {
                    appointmentFormInput = new Appointment(inAppointmentId, inTitle, inDescription, inLocation, inType,
                            inStartDateTime, inEndDateTime, inAppointmentCreated, inAppointmentCreatedBy, inAppointmentUpdated,
                            inAppointmentUpdatedBy, inCustomerId, inUserId, inContactId);

                } else if (whatAction.equals("Update")) {
                    //create
                    appointmentTableViewInput = new AppointmentFormTableData(String.valueOf(inAppointmentId), inTitle, inDescription,
                            inLocation, inType, inLocalDate, inStartTimeF, inEndTimeF, String.valueOf(inContactName), String.valueOf(inCustomerId));
                    appointmentFormInput = buildUpdateAppointment(appointmentTableViewInput);
                }

            } else{
                //when customer has overlapping appointments
                highlightSpinnersForInput();
                Alert overlapAlert = new Alert(Alert.AlertType.WARNING);
                overlapAlert.setHeaderText("Overlapping Appointment Times!");
                overlapAlert.setContentText("This customer already has an appointment at this time. \n" +
                        "Please choose an appointment time that does not overlap for this customer.");
                overlapAlert.show();
            }

        } else {
            //when the text or combo boxes have bad input
            String itemsToCheck = "";
            boolean startOfStringFlag = true;
            if (!isTitleValidFlag) {
                itemsToCheck = itemsToCheck + "Title";
                startOfStringFlag = false;
            }
            if (!isDescriptionValidFlag) {
                if (!startOfStringFlag) {
                    itemsToCheck = itemsToCheck + "\nDescription";
                    startOfStringFlag = false;
                } else {
                    itemsToCheck = itemsToCheck + "Description";
                }
            }
            if (!isTypeValidFlag) {
                if (!startOfStringFlag) {
                    itemsToCheck = itemsToCheck + "\nType";
                    startOfStringFlag = false;
                } else {
                    itemsToCheck = itemsToCheck + "Type";
                }
            }
            if (!isLocationValidFlag) {
                if (!startOfStringFlag) {
                    itemsToCheck = itemsToCheck + "\nLocation";
                    startOfStringFlag = false;
                } else {
                    itemsToCheck = itemsToCheck + "Location";
                }
            }
            if (!isCustomerIdValidFlag) {
                if (!startOfStringFlag) {
                    itemsToCheck = itemsToCheck + "\nCustomer ID";
                    startOfStringFlag = false;
                } else {
                    itemsToCheck = itemsToCheck + "Customer ID";
                }
            }
            if (!isContactIdValidFlag) {
                if (!startOfStringFlag) {
                    itemsToCheck = itemsToCheck + "\nContact ID";
                    startOfStringFlag = false;
                } else {
                    itemsToCheck = itemsToCheck + "Contact ID";
                }
            }
            if (!isLocalDateValidFlag) {
                if (!startOfStringFlag) {
                    itemsToCheck = itemsToCheck + "\nDate Picker";
                }else {
                    itemsToCheck = itemsToCheck + "Date Picker";
                }
            }
            Alert invalidInput = new Alert(Alert.AlertType.ERROR);
            invalidInput.setHeaderText("Invalid Appointment Input");
            invalidInput.setContentText("Please correct input for: \n" + itemsToCheck + "\nand retry");
            invalidInput.show();
        }
        return appointmentFormInput;
    }

    /**
     * Highlights a single textField by changing its border color to red and changing its prompt to "Required Input".
     * @param highlightTextField    the textField that is to have its display altered
     */
    private void highlightTextFieldForInput(TextField highlightTextField) {
        highlightTextField.setPromptText("Required Input");
        highlightTextField.setStyle("-fx-border-color:red");
    }

    /**
     * Highlights a single comboBox by changing its border color to red and changing its prompt to "Selection Required".
     * @param highlightComboBox   the comboBox that is to have its display altered
     */
    private void highlightComboBoxForInput(ComboBox highlightComboBox) {
        highlightComboBox.setPromptText("Selection Required");
        highlightComboBox.setStyle("-fx-border-color:red");
    }

    /**
     * Highlights both spinners by changing their border color to red.
     */
    private void highlightSpinnersForInput() {
        startTimeSpinner.setStyle("-fx-border-color:red");
        endTimeSpinner.setStyle("-fx-border-color:red");
    }

    /**
     * Highlights the datePicker by changing the border color to red.
     */
    private void highlightPickerForInput() {
        appointmentDatePick.setStyle("-fx-border-color:red");
    }

    /**
     * Creates a new Appointment containing only data for update by comparing data from the inputAppointment to the
     * data in the selected row.
     * @param inputAppointment  an instance of the model AppointmentFormTableData that provides input to build an Appointment
     * @return Appointment      the newly created instance of Appointment
     */
    private Appointment buildUpdateAppointment(model.AppointmentFormTableData inputAppointment) {
        String updateTitle = "N/A";
        String updateDescription = "N/A";
        String updateLocation = "N/A";
        String updateType = "N/A";
        LocalDate updateDate = LocalDate.of(9999, 12, 31);
        LocalTime updateStartTime = LocalTime.of(00, 00, 00);
        LocalTime updateEndTime = LocalTime.of(00, 00, 00);
        LocalDateTime updateStart = LocalDateTime.of(updateDate, updateStartTime);
        LocalDateTime updateEnd = LocalDateTime.of(updateDate, updateEndTime);
        int updateContactId = -1;
        int updateCustomerId = -1;

        // Id and UserId are not to be compared or changed
        int updateId = Integer.parseInt(selectedRow.getAId());
        int updateUserId = UserImpl.getUserId(usernameInput);  //never null

        //if the values are equal then nothing has changed and the update value is either null or -1, as initialized
        //however, if the values are not equal, then the field needs to be recorded so it can be changed/updated
        if (!selectedRow.getATitle().equals(inputAppointment.getATitle())) {
            updateTitle = inputAppointment.getATitle();
        }
        if (!selectedRow.getADescription().equals(inputAppointment.getADescription())) {
            updateDescription = inputAppointment.getADescription();
        }
        if (!selectedRow.getALocation().equals(inputAppointment.getALocation())) {
            updateLocation = inputAppointment.getALocation();
        }
        if (!selectedRow.getAType().equals(inputAppointment.getAType())) {
            updateType = inputAppointment.getAType();
        }

        if (!selectedRow.getAContactName().equals(inputAppointment.getAContactName())) {
            updateContactId = ContactImpl.getContactId(inputAppointment.getAContactName());
        }
        if (!selectedRow.getACustomerId().equals(inputAppointment.getACustomerId())) {
            updateCustomerId = Integer.parseInt(inputAppointment.getACustomerId());
        }

        //put together updateStart and updateEnd
        //If either date or time changes for either Start or End, rebuild its LocalDateTime
        if (selectedRow.getALocalDate() != inputAppointment.getALocalDate() ||
                selectedRow.getAStartTime() != inputAppointment.getAStartTime()) {
            updateDate = inputAppointment.getALocalDate();
            updateStartTime = inputAppointment.getAStartTime();
            updateStart = LocalDateTime.of(updateDate, updateStartTime);
        }
        if (selectedRow.getALocalDate() != inputAppointment.getALocalDate() ||
                selectedRow.getAEndTime() != inputAppointment.getAEndTime()) {
            updateDate = inputAppointment.getALocalDate();
            updateEndTime = inputAppointment.getAEndTime();
            updateEnd = LocalDateTime.of(updateDate, updateEndTime);
        }

        //create the updateAppointment of Appointment model
        return new Appointment(updateId, updateTitle, updateDescription, updateLocation,
                updateType, updateStart, updateEnd, LocalDateTime.of(9999, 12, 31, 00, 00),
                "N/A", LocalDateTime.now(), usernameInput,
                updateCustomerId, updateUserId, updateContactId);
    }

    /**
     * Checks the input appointment date and times for a customer to make sure another appointment is not already scheduled.
     * @param aId       an integer representing the appointment ID that was input
     * @param cId       an integer representing the customer ID that input
     * @param sdt       a LocalDateTime that is the start date and time of the input appointment
     * @param edt       a LocalDateTime that is the end date and time of the input appointment
     * @return  boolean a flag indicating whether there is no potential appointment overlap
     */
    private boolean checkNoCustomerAppointmentOverlap(int aId, int cId, LocalDateTime sdt, LocalDateTime edt){
        boolean isThereNoOverlap = true;

        try {
            ResultSet existingAppointmentSet = AppointmentImpl.getForOverlapComparison(cId);
            while (existingAppointmentSet.next()){
                LocalDateTime existingStart = (existingAppointmentSet.getTimestamp("Start")).toLocalDateTime();
                LocalDateTime existingEnd = (existingAppointmentSet.getTimestamp("End")).toLocalDateTime();
                int existingAppointmentId =(existingAppointmentSet.getInt("Appointment_ID"));
                if (existingAppointmentId!=aId) {
                    if (existingStart.equals(sdt) || existingEnd.equals(edt)) {
                        isThereNoOverlap = false;
                    }
                    if (sdt.isBefore(existingStart) && edt.isAfter(existingStart)) {
                        isThereNoOverlap = false;
                    }
                    if (sdt.isAfter(existingStart) && sdt.isBefore(existingEnd)){
                        isThereNoOverlap = false;
                    }
                }
            }
        } catch (SQLException throwables){
            throwables.printStackTrace();
        }

        return isThereNoOverlap;
    }

}