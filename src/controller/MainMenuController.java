package controller;

import DAO.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;


/**
 * Provides all functionality - select add, update, or delete function for either appointments or customers, call option
 * for three different reports, and loading the stage for the selected option - for the selected javafx file(s).
 * @author Margaret Chrysler
 * @version 1.6
 */
public class MainMenuController {

    @FXML
    private ToggleGroup customerRadioGroup;
    @FXML
    private RadioButton addCustomerRadioBtn;
    @FXML
    private RadioButton updateCustomerRadioBtn;
    @FXML
    private RadioButton deleteCustomerRadioBtn;
    @FXML
    private ToggleGroup appointmentRadioGroup;
    @FXML
    private RadioButton addAppointmentRadioBtn;
    @FXML
    private RadioButton updateAppointmentRadioBtn;
    @FXML
    private RadioButton deleteAppointmentRadioBtn;

    public static Stage levelThreeFormStage = new Stage();
    public static Stage levelTwoFormStage = new Stage();
    public static String whatAction;

    @FXML
    Button exitProgramBtn;
    @FXML
    void exitProgramButtonClicked(){
        //Make sure to close the Database first!!!
        DatabaseConnection.closeDBConnection();
        System.exit(0);
    }



    @FXML
    public void customerInformationButtonClicked() throws IOException{
        //Open Customer Information screen & pass what action to be taken

        //get selected toggle value
        addCustomerRadioBtn.setUserData("Add");
        updateCustomerRadioBtn.setUserData("Update");
        deleteCustomerRadioBtn.setUserData("Delete");
        whatAction = customerRadioGroup.getSelectedToggle().getUserData().toString();
        if (customerRadioGroup.getSelectedToggle() != null){
            whatAction = customerRadioGroup.getSelectedToggle().getUserData().toString();
        }

        loadTheStage("/view/customerInformation.fxml");

    }

    @FXML
    public void appointmentsButtonClicked() throws IOException{
        //Open Appointments screen & pass which action to be taken

        //get selected toggle value
        addAppointmentRadioBtn.setUserData("Add");
        updateAppointmentRadioBtn.setUserData("Update");
        deleteAppointmentRadioBtn.setUserData("Delete");
        whatAction = appointmentRadioGroup.getSelectedToggle().getUserData().toString();
        if (appointmentRadioGroup.getSelectedToggle() != null){
            whatAction = appointmentRadioGroup.getSelectedToggle().getUserData().toString();
        }
        //load main menu screen if username and password do not throw error
        loadTheStage("/view/appointments.fxml");

    }

    @FXML
    public void appointmentsTypeMonthButtonClicked() throws IOException{
        //Open Appointments by Type and Month report screen

        //load main menu screen if username and password do not throw error
        loadTheStage("/view/reportAppointmentsByTypeMonth.fxml");

    }

    @FXML
    public void contactsSchedulesButtonClicked() throws IOException{
        //Open Contacts Schedules Report screen

        //load main menu screen if username and password do not throw error
        loadTheStage("/view/reportContactsSchedules.fxml");

    }

    @FXML
    public void customerPercentagesButtonClicked() throws IOException{
        //Open customer percentages screen

        //load main menu screen if username and password do not throw error
        loadTheStage("/view/reportCustomersPerCountry.fxml");

    }

    private void loadTheStage(String resourceString) throws IOException {

        Stage stage = new Stage();
        Parent root;
        root = FXMLLoader.load(getClass().getResource(resourceString));
        stage.setTitle("Global Consulting Reports");
        stage.setScene(new Scene(root, 900, 800));
        levelTwoFormStage = stage;
        //because this is opening from MainMenu, level three will always be null from here
        levelThreeFormStage = null;
        stage.show();

    }


}
