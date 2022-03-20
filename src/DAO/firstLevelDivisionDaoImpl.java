package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * The methods in this class facilitate access to database first_level_division tables using the classes in the model
 * folder, using SQL statements to gather observableLists and resultSets that can be utilized in the controller classes
 * that make the information accessible via the fxml forms/views.
 */
public class firstLevelDivisionDaoImpl {

    /**
     * Returns an observableList of the names of the (first-level-)divisions associated with the specified country.
     * @param selectedCountry   A string representation of the name of the country whose divisions need found.
     * @return observableList   This returns an observableList of the names of the (first-level-)divisions associated
     *                          with the specified country.
     */
    public static ObservableList<String> getDivisionNamesForCountry(String selectedCountry) {
        int selectedCountryId = CountryImpl.getCountryIdFromName(selectedCountry);
        ObservableList<String> allDivisionsForCountry = FXCollections.observableArrayList();
        String sqlStmt = "SELECT Division FROM first_level_divisions " +
                "WHERE Country_ID = " + selectedCountryId + ";";
        try {
            Query.buildQuery(sqlStmt);

            ResultSet divisionNamesResultSet = Query.getResult();
            while (divisionNamesResultSet.next()) {
                String divisionName = divisionNamesResultSet.getString("Division");
                allDivisionsForCountry.add(divisionName);
            }

        } catch (SQLException throwables) {
            System.out.println("Error getting Division Names for a Country");
            throwables.printStackTrace();
        }
        return allDivisionsForCountry;
    }

    /**
     * Returns the integer (int) value of the division ID associated with the specified division name.
     * @param selectedDivisionName  A String representation of the name of the division whose ID is to be found.
     * @return int                  This returns the value of the division ID associated with a given division name.
     */
    public static int getDivisionIdFromName(String selectedDivisionName){
        int divisionId = 0;
        String sqlStmt = "SELECT Division_ID, Division FROM first_level_divisions " +
                "WHERE first_level_divisions.Division = '" + selectedDivisionName + "';";
        try {
            Query.buildQuery(sqlStmt);
            ResultSet rs = Query.getResult();
            while (rs.next()) {
                divisionId = rs.getInt("Division_ID");
            }
        } catch (SQLException throwables) {
            System.out.println("Error getting Division ID from Name");
            throwables.printStackTrace();
        }

        return divisionId;

    }
}
