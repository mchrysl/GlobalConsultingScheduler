package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.ContactsAppointment;
import model.CustomersPerCountryData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.util.ArrayList;


/**
 * The methods in this class facilitate access to database tables for the program's reports using the classes in the model
 * folder, using SQL statements to gather observableLists and resultSets that can be utilized in the controller classes
 * that make the information accessible via the fxml forms/views.
 */
public class ReportsImpl {

    /**
     * Returns the result set used in reportAppointmentsByMonth to create a report of all appointments by month and type.
     * @return ResultSet    This returns a result set used in the reportAppointmentsByMonth view/form.
     */
    public static ResultSet getAppointmentsTypeMonthReport() {

        //create an array or list of the monthAppointments class
        ResultSet appointmentResultSet = null;

        //SELECT everything FROM appointments, assign to a resultSet (rs), sort by Start & put in sorted list
        String sqlStmt = "SELECT Appointment_ID, Type, EXTRACT(MONTH FROM Start) AS StartMonth FROM appointments ORDER BY StartMonth;";
        Query.buildQuery(sqlStmt);
        appointmentResultSet = Query.getResult();

        return appointmentResultSet;
    }

    /**
     * Returns the result set used in reportCustomersPerCoutnry to create a report of percentage of customers per country.
     * @return ResultSet    This returns a result set used in the reportCustomersPerCountry view/form.
     */
    public static ResultSet getCustomersPerCountry() {

        //create a result set from the database
        ResultSet customersPerCountryResultSet = null;
        ArrayList<CustomersPerCountryData> customersPerCountryList = new ArrayList<>();
        //SELECT Country, Country_ID, & Customer_ID FROM customers,countries & FLD, assign to a resultSet (rs)
        String sqlStmt = "SELECT countries.Country, countries.Country_ID, customers.Customer_ID " +
                "FROM customers " +
                "JOIN first_level_divisions ON customers.Division_ID = first_level_divisions.Division_ID " +
                "JOIN countries ON first_level_divisions.COUNTRY_ID = countries.Country_ID;";
        Query.buildQuery(sqlStmt);
        customersPerCountryResultSet = Query.getResult();

        return customersPerCountryResultSet;
    }

    /**
     * Returns the observableList used to populate the TableView in reportContactsSchedules to create a report containing
     * the appointment schedule for each contact.
     * @return observableList   This returns an observableList used to populate the tableView in
     *                          the reportContactsSchedule view/form.
     */
    public static ObservableList<ContactsAppointment> getContactsAppointmentsReport() {

        //create an array or list of the monthAppointments class
        ObservableList<ContactsAppointment> contactsAppointmentsData = FXCollections.observableArrayList();
        //SELECT fields FROM appointments & contacts, assign to a resultSet (rs), sort by contact_ID & put in sorted list
        String sqlStmt = "SELECT contacts.Contact_ID, appointments.Appointment_ID, appointments.Title, appointments.Type, " +
                "appointments.Description, appointments.Start, appointments.End, appointments.Customer_ID " +
                "FROM appointments " +
                "JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "ORDER BY contacts.Contact_ID;";
        try {
            Query.buildQuery(sqlStmt);
            ResultSet contactsAppointmentsRS = Query.getResult();

            while (contactsAppointmentsRS.next()) {
                String thisContactId = (contactsAppointmentsRS.getString("Contact_ID"));
                String thisAppointmentId = String.valueOf(contactsAppointmentsRS.getInt("Appointment_ID"));
                String thisTitle = contactsAppointmentsRS.getString("Title");
                String thisType = contactsAppointmentsRS.getString("Type");
                String thisDescription = contactsAppointmentsRS.getString("Description");

                //set up date & time
                DateFormat formatDateTime = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT);
                //start date & time
                String thisStartDate = formatDateTime.format(contactsAppointmentsRS.getDate("Start"));
                String thisStartTime  = formatDateTime.format(contactsAppointmentsRS.getTime("Start"));
                String thisStartDateTime = thisStartDate + ", " + thisStartTime;
                //end date & time
                String thisEndDate = formatDateTime.format(contactsAppointmentsRS.getDate("End"));
                String thisEndTime  = formatDateTime.format(contactsAppointmentsRS.getTime("End"));
                String thisEndDateTime = thisEndDate + ", " + thisEndTime;
                String thisCustomerId = String.valueOf(contactsAppointmentsRS.getInt("Customer_ID"));

                ContactsAppointment thisRow = new ContactsAppointment( thisContactId, thisAppointmentId, thisTitle, thisType, thisDescription,
                        thisStartDateTime, thisEndDateTime, thisCustomerId);
                contactsAppointmentsData.add(thisRow);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return contactsAppointmentsData;
    }

}
