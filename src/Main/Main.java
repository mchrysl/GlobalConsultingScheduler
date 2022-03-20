package Main;

import DAO.DatabaseConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static DAO.DatabaseConnection.openConn;

public class Main extends Application {

    public static Stage loginStage = new Stage();
    public static Locale locale = Locale.getDefault();


    /**
     * Loads the first screen of the program by overriding the default start method.
     * @param primaryStage  The first stage loaded for this program, the login screen.
     */
    @Override
    public void start(Stage primaryStage) {
        //output location for troubleshooting
        loginStage = primaryStage;

        //choose which location
        ResourceBundle countryLanguageRb = ResourceBundle.getBundle("login");

        try {
            //load login form
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"), countryLanguageRb);
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root, 450, 450));
            primaryStage.setTitle(countryLanguageRb.getString("companyName"));

            //open the database for application to use
            openConn = DatabaseConnection.openDbConnection();
            primaryStage.show();
        } catch(Exception e){
            System.out.println("Error loading login form. See stack trace.");
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }


}
