package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static controller.MainMenuController.levelTwoFormStage;


/**
 * Provides functionality for the report that lists the total number of appointments by type and month.
 * @author Margaret Chrysler
 * @version 1.6
 */
public class ReportAppointmentsByTypeMonth implements Initializable {

    @FXML
    private Label deBriefings1_Lbl;
    @FXML
    private Label planningSessions1_Lbl;
    @FXML
    private Label total1_Lbl;
    @FXML
    private Label deBriefings2_Lbl;
    @FXML
    private Label planningSessions2_Lbl;
    @FXML
    private Label total2_Lbl;
    @FXML
    private Label deBriefings3_Lbl;
    @FXML
    private Label planningSessions3_Lbl;
    @FXML
    private Label total3_Lbl;
    @FXML
    private Label deBriefings4_Lbl;
    @FXML
    private Label planningSessions4_Lbl;
    @FXML
    private Label total4_Lbl;
    @FXML
    private Label deBriefings5_Lbl;
    @FXML
    private Label planningSessions5_Lbl;
    @FXML
    private Label total5_Lbl;
    @FXML
    private Label deBriefings6_Lbl;
    @FXML
    private Label planningSessions6_Lbl;
    @FXML
    private Label total6_Lbl;
    @FXML
    private Label deBriefings7_Lbl;
    @FXML
    private Label planningSessions7_Lbl;
    @FXML
    private Label total7_Lbl;
    @FXML
    private Label deBriefings8_Lbl;
    @FXML
    private Label planningSessions8_Lbl;
    @FXML
    private Label total8_Lbl;
    @FXML
    private Label deBriefings9_Lbl;
    @FXML
    private Label planningSessions9_Lbl;
    @FXML
    private Label total9_Lbl;
    @FXML
    private Label deBriefings10_Lbl;
    @FXML
    private Label planningSessions10_Lbl;
    @FXML
    private Label total10_Lbl;
    @FXML
    private Label deBriefings11_Lbl;
    @FXML
    private Label planningSessions11_Lbl;
    @FXML
    private Label total11_Lbl;
    @FXML
    private Label deBriefings12_Lbl;
    @FXML
    private Label planningSessions12_Lbl;
    @FXML
    private Label total12_Lbl;

    @FXML
    Button closeReportBtn;
    /**
     * Closes the ReportAppointmentsByTypeMonth stage when the 'Close Report' button in this report's view
     * is clicked.
     */
    @FXML
    private void closeReportButtonClicked() {
        levelTwoFormStage.close();
    }

    int totalCount = 0;
    int deBriefingCount = 0;
    int planningSessionCount = 0;
    int thisRowsMonth;
    String inputType;

    /**
     * Initiates processes that collect the report information and fill out the report
     * @param url               the url for the view being initialized
     * @param resourceBundle    the resource bundle for the view being initialized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillAppointmentTypeMonthReport(DAO.ReportsImpl.getAppointmentsTypeMonthReport());

    }

    /**
     * Analyzes each entry and maintains counts for each month's appointment types until an entry is for the next month.
     * Bring in month and type from each appointmentsList entry.
     * Analyze each entry and maintain a count for each appointment type until the month value changes.
     * Call method to populate the forms labels for this month.
     * Reset the counters when the month value changes and continue loop for the new month until all 12 months are analyzed.
     * @param appointmentsList the ResultSet containing the data for this report (Appointment_ID, Type, and StartMonth
     *                         which is the month value extracted from Start field and sorted by StartMonth)
     */
    private void fillAppointmentTypeMonthReport(ResultSet appointmentsList) {
        //get the list created from the SQL query
        try {
            //get first month from appointmentsList
            boolean endOfListFlag = false;
            if (appointmentsList.next()) {
                thisRowsMonth = appointmentsList.getInt("StartMonth");
                inputType = appointmentsList.getString("Type");
            } else {
                endOfListFlag = true;
            }
            //set up for loop to populate each set of report labels
            for (int monthIndex = 1; monthIndex <= 12; monthIndex++) {
                while (thisRowsMonth == monthIndex && !endOfListFlag) {

                    switch (inputType) {
                        case "De-Briefing":
                            deBriefingCount++;
                            totalCount++;
                            break;
                        case "Planning Session":
                            planningSessionCount++;
                            totalCount++;
                            break;
                        default:
                            totalCount++;
                            break;
                    }
                    if (appointmentsList.next()) {
                        thisRowsMonth = appointmentsList.getInt("StartMonth");
                        inputType = appointmentsList.getString("Type");
                    } else {
                        endOfListFlag = true;
                    }
                }

                //populate the report labels
                populateReportLabels(monthIndex);
                deBriefingCount = 0;
                planningSessionCount = 0;
                totalCount = 0;
            }
        } catch(SQLException throwables){
            throwables.printStackTrace();
        }
    }


    /**
     * Populates the labels of the report form for the month indicated by monthIndex.
     * @param monthIndex    an integer representation of the month that is to have data populated.
     */

    private void populateReportLabels(int monthIndex){
        //write to the appropriate label
        switch (monthIndex){
            case 1:
                deBriefings1_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions1_Lbl.setText(String.valueOf(planningSessionCount));
                total1_Lbl.setText(String.valueOf(totalCount));
                break;
            case 2:
                deBriefings2_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions2_Lbl.setText(String.valueOf(planningSessionCount));
                total2_Lbl.setText(String.valueOf(totalCount));
                break;
            case 3:
                deBriefings3_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions3_Lbl.setText(String.valueOf(planningSessionCount));
                total3_Lbl.setText(String.valueOf(totalCount));
                break;
            case 4:
                deBriefings4_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions4_Lbl.setText(String.valueOf(planningSessionCount));
                total4_Lbl.setText(String.valueOf(totalCount));
                break;
            case 5:
                deBriefings5_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions5_Lbl.setText(String.valueOf(planningSessionCount));
                total5_Lbl.setText(String.valueOf(totalCount));
                break;
            case 6:
                deBriefings6_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions6_Lbl.setText(String.valueOf(planningSessionCount));
                total6_Lbl.setText(String.valueOf(totalCount));
                break;
            case 7:
                deBriefings7_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions7_Lbl.setText(String.valueOf(planningSessionCount));
                total7_Lbl.setText(String.valueOf(totalCount));
                break;
            case 8:
                deBriefings8_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions8_Lbl.setText(String.valueOf(planningSessionCount));
                total8_Lbl.setText(String.valueOf(totalCount));
                break;
            case 9:
                deBriefings9_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions9_Lbl.setText(String.valueOf(planningSessionCount));
                total9_Lbl.setText(String.valueOf(totalCount));
                break;
            case 10:
                deBriefings10_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions10_Lbl.setText(String.valueOf(planningSessionCount));
                total10_Lbl.setText(String.valueOf(totalCount));
                break;
            case 11:
                deBriefings11_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions11_Lbl.setText(String.valueOf(planningSessionCount));
                total11_Lbl.setText(String.valueOf(totalCount));
                break;
            case 12:
                deBriefings12_Lbl.setText(String.valueOf(deBriefingCount));
                planningSessions12_Lbl.setText(String.valueOf(planningSessionCount));
                total12_Lbl.setText(String.valueOf(totalCount));
                break;
            default:
                break;
        };
    }
}
