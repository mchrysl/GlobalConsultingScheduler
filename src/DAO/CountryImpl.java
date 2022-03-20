package DAO;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The methods in this class facilitate access to database country tables using the classes in the model folder,
 * using SQL statements to gather observableLists and resultSets that can be utilized in the controller classes that
 * make the information accessible via the fxml forms/views.
 */
public class CountryImpl {

    /**
     * Returns an observableList of Strings containing all of the Country Names in the countries table.
     * @return ObeservableList of type String  This returns an observableList of all of the Country Names in the countries table.
     */
    public static ObservableList<String> getAllCountryNames() {
        ObservableList<String> allCountryNames = FXCollections.observableArrayList();
        String sqlStmt = "SELECT Country FROM countries;";
        try {
            Query.buildQuery(sqlStmt);

            ResultSet countryNamesResultSet = Query.getResult();
            while (countryNamesResultSet.next()) {
                String countryName = countryNamesResultSet.getString("Country");
                allCountryNames.add(countryName);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allCountryNames;
    }

    /**
     * Returns the country ID that is associated with the selectedCountryName.
     * @param selectedCountryName   A String representation of the name of the country whose ID is to be found.
     * @return int                  This returns the country ID that is associated with the selectedCountryName.
     */
    public static int getCountryIdFromName(String selectedCountryName){
        int countryId = 0;
        String sqlStmt = "SELECT Country_ID, Country FROM countries " +
                "WHERE countries.Country = '" + selectedCountryName + "';";
        try {
            Query.buildQuery(sqlStmt);
            ResultSet rs = Query.getResult();
            while (rs.next()) {
                countryId = rs.getInt(1);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        };

        return countryId;

    }


}
