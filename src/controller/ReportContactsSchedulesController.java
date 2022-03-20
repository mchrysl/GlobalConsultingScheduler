package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.ContactsAppointment;

import java.net.URL;
import java.util.ResourceBundle;

import static controller.MainMenuController.levelTwoFormStage;

/**
 * Provides functionality for the report that lists each contact's scheduled appointments.
 * @author Margaret Chrysler
 * @version 1.6
 */
public class ReportContactsSchedulesController implements Initializable {

    @FXML
    private TableView<ContactsAppointment> contactsAppointmentsTableView;
    @FXML
    private TableColumn<ContactsAppointment, String> contactIdTblCol;
    @FXML
    private TableColumn<ContactsAppointment, String> appointmentIdTblCol;
    @FXML
    private TableColumn<ContactsAppointment, String> titleTblCol;
    @FXML
    private TableColumn<ContactsAppointment, String> typeTblCol;
    @FXML
    private TableColumn<ContactsAppointment, String> descriptionTblCol;
    @FXML
    private TableColumn<ContactsAppointment, String> startTblCol;
    @FXML
    private TableColumn<ContactsAppointment, String> endTblCol;
    @FXML
    private TableColumn<ContactsAppointment, String> customerIdTblCol;

    @FXML
    Button closeReportBtn;
    /**
     * Closes the ReportContactsSchedulesController stage when the 'Close Report' button in this report's view
     * is clicked.
     */
    @FXML
    private void closeReportButtonClicked() {
        levelTwoFormStage.close();
    }

    /**
     * Sets up and populates the tableView data for the ReportContactsSchedulesController view.
     * @param url               the url for the view being initialized
     * @param resourceBundle    the resource bundle for the view being initialized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        contactIdTblCol.setCellValueFactory(new PropertyValueFactory<>("caContactId"));
        appointmentIdTblCol.setCellValueFactory(new PropertyValueFactory<>("caAppointmentId"));
        titleTblCol.setCellValueFactory(new PropertyValueFactory<>("caTitle"));
        descriptionTblCol.setCellValueFactory(new PropertyValueFactory<>("caDescription"));
        typeTblCol.setCellValueFactory(new PropertyValueFactory<>("caType"));
        startTblCol.setCellValueFactory(new PropertyValueFactory<>("caStart"));
        endTblCol.setCellValueFactory(new PropertyValueFactory<>("caEnd"));
        customerIdTblCol.setCellValueFactory(new PropertyValueFactory<>("caCustomerId"));


        contactsAppointmentsTableView.setItems(DAO.ReportsImpl.getContactsAppointmentsReport());

        //Display once items are set
        contactsAppointmentsTableView.setVisible(true);

    }

}
