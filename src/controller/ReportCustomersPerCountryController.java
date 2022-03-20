package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static controller.MainMenuController.levelTwoFormStage;

/**
 * Provides functionality for the report that calculates and graphs the percentage of customers from each country.
 * @author Margaret Chrysler
 * @version 1.6
 */
public class ReportCustomersPerCountryController implements Initializable {

    @FXML
    private Label usCountLbl;
    @FXML
    private Label usPercentLbl;
    @FXML
    private Label ukCountLbl;
    @FXML
    private Label ukPercentLbl;
    @FXML
    private Label canadaCountLbl;
    @FXML
    private Label canadaPercentLbl;
    @FXML
    private PieChart customersPerCountryPieChart;

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
     * Gets data and calculates and graphs the percentage of customers from each country.
     * Gets custom formatted resultSet data from the database.
     * Analyze each entry for country and increment count for specified country until no more entries in resultSet.
     * Calculate percentages and populate report labels text with appropriately formatted strings.
     * Format and display a graph for concise representation of customer percentages.
     * @param url               the url for the view being initialized
     * @param resourceBundle    the resource bundle for the view being initialized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        int totalCustomerCount = 0;
        int usCount = 0;
        int ukCount = 0;
        int canadaCount = 0;
        String country;
        ResultSet customersPerCountryDataList = DAO.ReportsImpl.getCustomersPerCountry();

        //grab the first country
        //for each entry:
        //increment total count & increment appropriate country count.
        try {
            while (customersPerCountryDataList.next()) {
                totalCustomerCount++;
                country = customersPerCountryDataList.getString("countries.Country");
                switch (country) {
                    case "U.S":
                        usCount++;
                        break;
                    case "UK":
                        ukCount++;
                        break;
                    case "Canada":
                        canadaCount++;
                        break;
                    default:
                        break;
                }
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }

        //set labels for output
        usCountLbl.setText(String.valueOf(usCount));
        double usPercent = 100.0*usCount/totalCustomerCount;
        usPercentLbl.setText(String.format("%.2f", usPercent) + "%");
        ukCountLbl.setText(String.valueOf(ukCount));
        double ukPercent = 100.0*ukCount/totalCustomerCount;
        ukPercentLbl.setText(String.format("%.2f", ukPercent) + "%");
        canadaCountLbl.setText(String.valueOf(canadaCount));
        double canadaPercent = 100.0*canadaCount/totalCustomerCount;
        canadaPercentLbl.setText(String.format("%.2f", canadaPercent) + "%");

        //add-on: Pie Chart
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("U.S.", usPercent),
                        new PieChart.Data("UK", ukPercent),
                        new PieChart.Data("Canada", canadaPercent));

        customersPerCountryPieChart.setData(pieChartData);

    }

}
