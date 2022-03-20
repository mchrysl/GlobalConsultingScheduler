package controller;

import DAO.AppointmentImpl;
import DAO.UserImpl;
import Main.Main;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.UpcomingAppointment;
import model.User;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.ResourceBundle;


/**
 * Provides all functionality - read in and validate username/password combination, load Main Menu on successful login,
 * save all login attempts to a text file, find any appointments withing 15 minutes of login and display an appropriate Alert dialog,
 * - for the login javafx file.
 * @author Margaret Chrysler
 * @version 1.6
 */
public class LoginController {


    public static String usernameInput;
    ResourceBundle countryLanguageRb = ResourceBundle.getBundle("login");
    File logFile = new File("login_activity.txt");


    @FXML
    TextField usernameTxt;
    @FXML
    PasswordField passwordTxt;
    @FXML
    Button submitBtn;
    @FXML
    Label usernameLbl;
    @FXML
    Label passwordLbl;
    @FXML
    Label locationLbl;
    @FXML
    Label companyNameLbl;
    @FXML
    Label loginErrorLbl;


    @FXML
    Button exitProgramBtn;

    /**
     * Closes the Login stage when the 'Exit Program' button in the Login view is clicked.
     */
    @FXML
    void exitProgramBtnClicked(){
        System.exit(0);
    }

    /**
     * Submits login attempt - reads in username and password, calls method to validate username/password combination,
     * and calls the method to record the login attempt.
     */
    @FXML
    public void submitBtnClicked() {

        //make sure inputs and error field are cleared
        loginErrorLbl.setText("");
        loginErrorLbl.setVisible(false);
        
        //get input from textFields and validate
        boolean usernamePasswordIsValid = getValidateLogin();

        //if the username & password are valid then open the Main Menu form
        if (!usernamePasswordIsValid){
            attemptLog(usernameInput, false);      //send to attemptLog the user and flag for unsuccessful
            passwordTxt.setText("");
        }else {
            attemptLog(usernameInput, true);      //send to attemptLog the user and flag for successful
            loadMainMenu(new Stage());
        }
    }

    /**
     * Loads/opens the Main Menu form and close the login form.
     * @param stage the stage for the Main Menu to be loaded
     */
    private void loadMainMenu(Stage stage){
        //load main menu screen
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/mainMenu.fxml"));
            stage.setTitle("Global Consulting Main Menu");
            stage.setScene(new Scene(root, 650, 850));
            Main.loginStage.close();
            stage.show();
            lookForAppointmentsAlert();
        } catch (IOException exception){
            System.out.println("Error loading Main Menu. See stack trace.");
            exception.printStackTrace();
        }
    }

    /**
     * Records each login attempt to an external file - "login_activity.txt".
     * @param user          a string representation of the attempted login's username
     * @param isSuccessful  a boolean value representing either a successful login - true, or an unsuccessful login - false
     */
    private void attemptLog(String user, boolean isSuccessful){
        //gather line of data to write to file
        ZonedDateTime attemptDateTime = ZonedDateTime.now(Clock.systemUTC());

        //when no username is provided, log empty user
        if (user.equals("")){
            user = "**empty**";
        }

        //build the line of login attempt information to save to the log
        String logLine = user +", " + attemptDateTime + "UTC, " + isSuccessful + "\n";

        //copied & modified from w3schools.com to integrate FileWriter and error handling
        try {
            FileWriter writer = new FileWriter(logFile,true);
            writer.write(logLine);
            writer.close();
        }catch (IOException e) {
            System.out.println("An error occurred writing to the log.");
            e.printStackTrace();
        }

    }

    /**
     * Determines if username and password are valid, returning a boolean expression, and displaying error messages as
     * appropriate in the language appropriate to the system's locale. (Languages limited to english and french)
     * @return boolean  This returns true for a valid username/password combination, false otherwise.
     */
    private boolean getValidateLogin() {
        boolean loginValid = true;
        //get input from textFields
        usernameInput = usernameTxt.getText().trim();
        String passwordInput = passwordTxt.getText().trim();

        //reset errorString
        String errorString = "";

        //First, check for empty input
        if (usernameInput.equals("") && passwordInput.equals("")) {
            //if both are empty, display error: username and password input are required
            errorString = countryLanguageRb.getString("usernamePasswordRequired");
            loginValid = false;
        }else if (usernameInput.equals("")) {
            //if username is empty, display error: username input is required
            errorString = countryLanguageRb.getString("usernameRequired");
            loginValid = false;
        }else if (passwordInput.equals("")) {
            //if password is empty, display error: password input is required
            errorString = countryLanguageRb.getString("passwordRequired");
            loginValid = false;
        }
        User dbUser = null;
        //Next, check input against database
        if (loginValid) {

            //retrieve username and password using usernameInput as filter

            dbUser = UserImpl.getUser(usernameInput);
            if (!(dbUser == null)) {
                //boolean userMatch = true;
                if (dbUser.getPassword().equals(passwordInput)) {
                    errorString = "";
                    loginValid = true;
                } else {
                    errorString = countryLanguageRb.getString("invalidCombo");
                    loginValid = false;
                }
            } else {
                errorString = countryLanguageRb.getString("invalidUsername");
                loginValid = false;
            }
        }
        if (!loginValid){
            //output error message and make error label visible
            loginErrorLbl.setText(errorString);
            loginErrorLbl.setVisible(true);
        }

        return loginValid;
    }

    /**
     * Determines if there is an appointment within the next 15 minutes since the login time and displays a respective
     * message in an Information style Alert Dialog after the user logs in.
     */
    private void lookForAppointmentsAlert(){

        //get the list
        ObservableList<UpcomingAppointment> searchList = AppointmentImpl.getUpcomingAppointments();

        if (searchList.size() != 0){
            for (int i = 0; i < searchList.size(); i++) {
                //yes, decided to show each upcoming appointment alert individually
                //get the row for an appointment
                UpcomingAppointment singleAppointment = searchList.get(i);
                //get the row's/appointment's values & put them in a message
                String alertMessage = "Reminder: \n Appointment #" + singleAppointment.getUaAppointmentId() +
                        " is scheduled to start in less than 15 minutes. \n (" + singleAppointment.getUaStartDate() +
                        " at " + singleAppointment.getUaStartTime() + ").";
                createAlertDialog(alertMessage);
            }
        } else {
            String alertMessage = "\nThere are no appointments scheduled in the next 15 minutes.\n";
            createAlertDialog(alertMessage);
        }

    }

    /**
     * Creates an Information style alert dialog box that displays the custom alertMessage parameter.
     * @param alertMessage  a string value that is the message to be displayed in the alert dialog.
     */
    private void createAlertDialog(String alertMessage){

        Alert appointmentAlert = new Alert(Alert.AlertType.INFORMATION);
        appointmentAlert.setContentText(alertMessage);
        appointmentAlert.initModality(Modality.APPLICATION_MODAL);
        appointmentAlert.setGraphic(null);
        appointmentAlert.setHeaderText("");
        appointmentAlert.show();
    }
}
