package DAO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.Appointment;
import model.AppointmentFormTableData;
import model.UpcomingAppointment;

import java.sql.*;
import java.time.*;
import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Locale;
import java.util.Optional;

/**
 * The methods in this class facilitate access to database appointment tables using the classes in the model folder,
 * using SQL statements to gather observableLists and resultSets that can be utilized in the controller classes that
 * make the information accessible via the fxml forms/views.
 * @author Margaret Chrysler
 * @version 1.6
 */
public class AppointmentImpl {

    /**
     * Creates and returns an observableList that is specifically formatted to be input for the tableView of the
     * appointments form/stage.
     * An appropriate Select statement is chosen depending on what filter is requested for the table.
     * After the query is run, the each item in the result set is formatted for ease of use and put into an
     * observableList for use in the appointment form/stage tableView.
     * @param tableFilter   A string representation of the filter type to be used on the appointment tableView.
     * @return  This returns an observableList that is used in the appointments form tableView.
     */
    public static ObservableList<AppointmentFormTableData> getAppointmentFormTableData(String tableFilter) {

        ObservableList<AppointmentFormTableData> appointmentData = FXCollections.observableArrayList();
        int largestIDnumber = 0;

        //grab customer data
        String sqlStmt = "";
        switch(tableFilter){
            case "View All":
                sqlStmt = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description," +
                        "appointments.location, contacts.Contact_Name, appointments.Type, appointments.Start, " +
                        "appointments.End, appointments.Customer_ID " +
                        "FROM appointments " +
                        "JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                        "ORDER BY appointments.Appointment_ID;";
                break;
            case "View This Week":
                LocalTime startTime = LocalTime.of(00,00,00);
                LocalTime endTime = LocalTime.of(23,59,59);
                TemporalField fieldDefault = WeekFields.of(Locale.getDefault()).dayOfWeek();
                Timestamp startWeek = Timestamp.valueOf((LocalDateTime.of(LocalDate.now(),startTime).with(fieldDefault, 1)));
                Timestamp endWeek = Timestamp.valueOf(LocalDateTime.of(LocalDate.now(),endTime).with(fieldDefault, 7));
                sqlStmt = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description," +
                        "appointments.location, contacts.Contact_Name, appointments.Type, appointments.Start, " +
                        "appointments.End, appointments.Customer_ID " +
                        "FROM appointments " +
                        "JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                        "WHERE appointments.Start >= '" + startWeek + "' AND appointments.Start <= '" + endWeek + "' " +
                        "ORDER BY appointments.Appointment_ID;";
                break;
            case "View This Month":
                LocalDate thisDay = LocalDate.now();
                Timestamp startMonth = Timestamp.valueOf(LocalDateTime.of(thisDay.withDayOfMonth(1),LocalTime.of(00,00)));
                Timestamp endMonth = Timestamp.valueOf(LocalDateTime.of(thisDay.withDayOfMonth(thisDay.lengthOfMonth()),LocalTime.of(23,59)));
                sqlStmt ="SELECT appointments.Appointment_ID, appointments.Title, appointments.Description," +
                        "appointments.location, contacts.Contact_Name, appointments.Type, appointments.Start, " +
                        "appointments.End, appointments.Customer_ID " +
                        "FROM appointments " +
                        "JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                        "WHERE appointments.Start >= '" + startMonth + "' AND appointments.Start <= '" + endMonth + "' " +
                        "ORDER BY appointments.Appointment_ID;";
        }

        try {
            Query.buildQuery(sqlStmt);

            ResultSet appointmentResultSet = Query.getResult();

            //grab data to build the row
            while (appointmentResultSet.next()) {
                //determine largest ID number so next ID can be calculated for adding
                int thisID = appointmentResultSet.getInt("appointments.Appointment_ID");
                if (largestIDnumber < thisID) {
                    largestIDnumber = thisID;
                }
                //continue pulling in string values to put into AppointmentFormTable class
                String arsID = appointmentResultSet.getString("appointments.Appointment_ID");
                String arsTitle = appointmentResultSet.getString("appointments.Title");
                String arsDescription = appointmentResultSet.getString("appointments.Description");
                String arsLocation = appointmentResultSet.getString("appointments.Location");
                String arsContactName = appointmentResultSet.getString("contacts.Contact_Name");
                String arsType = appointmentResultSet.getString("appointments.Type");
                //Make sure time stuff is correct use 8.x jdbc interface
                LocalDate arsLocalDateStart = appointmentResultSet.getTimestamp("appointments.Start").toLocalDateTime().toLocalDate();
                LocalTime arsLocalTimeStart = appointmentResultSet.getTimestamp("appointments.Start").toLocalDateTime().toLocalTime();
                LocalDate arsLocalDateEnd = appointmentResultSet.getTimestamp("appointments.End").toLocalDateTime().toLocalDate();
                LocalTime arsLocalTimeEnd = appointmentResultSet.getTimestamp("appointments.End").toLocalDateTime().toLocalTime();

                String arsCustomerId = appointmentResultSet.getString("appointments.Customer_ID");

                LocalDate arsLocalDate = null;
                if (arsLocalDateStart.equals(arsLocalDateEnd)) {
                    arsLocalDate = arsLocalDateEnd;
                }

                AppointmentFormTableData apptRow = new AppointmentFormTableData(arsID, arsTitle, arsDescription, arsLocation,
                        arsType, arsLocalDate, arsLocalTimeStart, arsLocalTimeEnd, arsContactName, arsCustomerId);

                appointmentData.add(apptRow);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentData;
    }


    /**
     * Creates and returns an observableList that is specifically formatted to be input for the tableView of the
     * appointments form/stage when a customer is to be deleted but first must have associated appointments deleted.
     * A Select statement is used to limit the resulting appointments to ones with the specified customer ID.
     * After the query is run, the each item in the result set is formatted for ease of use and put into an
     * observableList for use in the appointment form/stage tableView.
     * @param customerId    An int representation of the customer ID used to limit the resulting data set.
     * @return              This returns an observableList that is used in the appointments form tableView.
     */
    public static ObservableList<AppointmentFormTableData> getAppointmentFormTableData(int customerId) {

        ObservableList<AppointmentFormTableData> appointmentData = FXCollections.observableArrayList();
        int largestIDnumber = 0;
        //grab customer data
        String sqlStmt = "SELECT appointments.Appointment_ID, appointments.Title, appointments.Description," +
                "appointments.location, contacts.Contact_Name, appointments.Type, appointments.Start, " +
                "appointments.End, appointments.Customer_ID " +
                "FROM appointments " +
                "JOIN contacts ON appointments.Contact_ID = contacts.Contact_ID " +
                "WHERE appointments.Customer_ID = " + customerId +
                " ORDER BY appointments.Appointment_ID;";
        try {
            Query.buildQuery(sqlStmt);

            ResultSet appointmentResultSet = Query.getResult();

            //grab data to build the row
            while (appointmentResultSet.next()) {
                //determine largest ID number so next ID can be calculated for adding
                int thisID = appointmentResultSet.getInt("appointments.Appointment_ID");
                if (largestIDnumber < thisID) {
                    largestIDnumber = thisID;
                }
                //continue pulling in string values to put into AppointmentFormTable class
                String arsID = appointmentResultSet.getString("appointments.Appointment_ID");
                String arsTitle = appointmentResultSet.getString("appointments.Title");
                String arsDescription = appointmentResultSet.getString("appointments.Description");
                String arsLocation = appointmentResultSet.getString("appointments.Location");
                String arsContactName = appointmentResultSet.getString("contacts.Contact_Name");
                String arsType = appointmentResultSet.getString("appointments.Type");
                //Make sure time stuff is correct use 8.x jdbc interface
                LocalDate arsLocalDateStart = appointmentResultSet.getTimestamp("appointments.Start").toLocalDateTime().toLocalDate();
                LocalTime arsLocalTimeStart = appointmentResultSet.getTimestamp("appointments.Start").toLocalDateTime().toLocalTime();
                LocalDate arsLocalDateEnd = appointmentResultSet.getTimestamp("appointments.End").toLocalDateTime().toLocalDate();
                LocalTime arsLocalTimeEnd = appointmentResultSet.getTimestamp("appointments.End").toLocalDateTime().toLocalTime();

                String arsCustomerId = appointmentResultSet.getString("appointments.Customer_ID");

                LocalDate arsLocalDate = null;
                if (arsLocalDateStart.equals(arsLocalDateEnd)) {
                    arsLocalDate = arsLocalDateEnd;
                }

                AppointmentFormTableData apptRow = new AppointmentFormTableData(arsID, arsTitle, arsDescription, arsLocation,
                        arsType, arsLocalDate, arsLocalTimeStart, arsLocalTimeEnd, arsContactName, arsCustomerId);

                appointmentData.add(apptRow);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return appointmentData;
    }

    /**
     * Returns a resultSet containing Appointment ID, Start, and End for a specified customer ID so the calling method
     * can compare a new appointment for overlapping times.
     * @param customerId    An int representation of a customer ID used to limit the resulting data set
     * @return              This returns a result set to compare appointment times for overlap.
     */
    public static ResultSet getForOverlapComparison(int customerId){
        ResultSet comparisonSet = null;
        String sqlStmt = "SELECT Appointment_ID, Start, End FROM appointments WHERE Customer_ID = " + customerId + ";";
        Query.buildQuery(sqlStmt);
        comparisonSet = Query.getResult();
        return comparisonSet;

    }

    /**
     * Returns an observableList containing all of the Appointment IDs for a specific customer which is used to determine
     * if the customer has any appointments that would need to be deleted before the customer can be deleted.
     * @param customerId    An int representation of a customer ID used to limit the resulting data set.
     * @return              This returns an ObservableList containing all appointment IDs for a single customer.
     */
    public static ObservableList<Integer> getAllAppointmentIdsForCustomer(int customerId){
        ObservableList<Integer> allAppointmentIds = FXCollections.observableArrayList();
        String sqlStmt = "SELECT Appointment_ID FROM appointments WHERE Customer_ID = " + customerId + ";";
        try {
            Query.buildQuery(sqlStmt);

            ResultSet appointmentIdsResultSet = Query.getResult();
            while (appointmentIdsResultSet.next()) {
                allAppointmentIds.add(appointmentIdsResultSet.getInt("Appointment_ID"));
            }
        } catch(SQLException throwables) {
            System.out.println("Error in DAO.AppointmentInterface.getAllAppointmentIds");
            throwables.printStackTrace();
        }
        return allAppointmentIds;
    }

    /**
     * Returns an observableList containing Appointment ID, Start Time, and Start Date for all appointments starting
     * in the next fifteen minutes (according to current system time).
     * @return  This returns an observable list of UpcomingAppointment items.
     */
    public static ObservableList<UpcomingAppointment> getUpcomingAppointments(){
        ObservableList<UpcomingAppointment> upcomingAppointmentsList = FXCollections.observableArrayList();
        String sqlStmt = "SELECT Appointment_ID, Start FROM appointments;";
        try {
            Query.buildQuery(sqlStmt);

            ResultSet upcomingAppointmentsResultSet = Query.getResult();
            while (upcomingAppointmentsResultSet.next()) {

                String uaID = String.valueOf(upcomingAppointmentsResultSet.getInt("Appointment_ID"));
                Timestamp uaDateTime = upcomingAppointmentsResultSet.getTimestamp("Start");
                //LocalDateTime uaStartDateTime = uaDateTime.toLocalDateTime();
                LocalDate uaStartDate = uaDateTime.toLocalDateTime().toLocalDate();
                LocalTime uaStartTime = uaDateTime.toLocalDateTime().toLocalTime();

                LocalTime filterTime = LocalTime.now().plusMinutes(15);
                LocalTime appointmentTime = uaDateTime.toLocalDateTime().toLocalTime();
                if (uaStartDate.equals(LocalDate.now()) && appointmentTime.isAfter(LocalTime.now()) && appointmentTime.isBefore(filterTime)) {
                    UpcomingAppointment uaRow = new UpcomingAppointment(uaID, uaStartDate, uaStartTime);
                    upcomingAppointmentsList.add(uaRow);
                }
            }
        } catch(SQLException throwables) {
            System.out.println("Error in DAO.AppointmentInterface.getAllAppointmentIds");
            throwables.printStackTrace();
        }
        return upcomingAppointmentsList;
    }

    /**
     * Adds a new Appointment to the appointments table in the database.
     * First gets the individual fields of data for adding an appointment. Builds an SQL INSERT statement that adds the
     * new data to the table, executes the INSERT statement, and returns true if successful - false if unsuccessful.
     * @param newAppointment    An Appointment model instance that contains new data to be added to the appointments table.
     * @return                  This returns a boolean that indicates a successful (true) or unsuccessful (false) add.
     */
    public static boolean addAppointment(Appointment newAppointment){
        boolean isSuccessful = true;

        String addAppointmentTitle = newAppointment.getTitle();
        String addAppointmentLocation = newAppointment.getLocation();
        String addAppointmentDescription = newAppointment.getDescription();
        String addAppointmentType = newAppointment.getType();

        Timestamp addStart = Timestamp.valueOf(newAppointment.getStart().atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());

        Timestamp addEnd = Timestamp.valueOf(newAppointment.getEnd().atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());

        //Since this is adding a new appointment, CreateDate and LastUpdate are the same as are CreatedBy and LastUpdatedBy
        String addCreatedBy = newAppointment.getCreatedBy();
        String addLastUpdatedBy = newAppointment.getCreatedBy();

        int addCustomerId = newAppointment.getCustomerId();
        int addAppointmentUserId = newAppointment.getUserId();
        int addAppointmentContactId = newAppointment.getContactId();

        String sqlStmt = "INSERT INTO WJ06zQf.appointments (Title, Description, Location, Type, Start, End, Create_Date, " +
                "Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID) " +
                "VALUES ('" + addAppointmentTitle + "', '"+ addAppointmentDescription + "', '" + addAppointmentLocation +
                "', '" + addAppointmentType + "', '" + addStart + "', '" + addEnd + "', now(), '" + addCreatedBy + "', now(), '"
                + addLastUpdatedBy + "', '" + addCustomerId + "', '" + addAppointmentUserId + "', '" + addAppointmentContactId + "');";

        Query.buildQuery(sqlStmt);

        return isSuccessful;

    }

    /**
     * Deletes an appointment from the appointments table using appointmentIdToDelete as a unique identifier for the
     * appointment to be deleted.
     * Get the appointment ID and Type from appointments to display in conform Alert dialog.
     * Display confirm Alert dialog and read in result button - Ok to confirm, otherwise do not delete.
     * Delete the appointment once it is confirmed.
     * Returns the boolean isSuccessful to indicate whether the delete was successful or unsuccessful.
     * @param appointmentIdToDelete An int representation of the appointment ID of the appointment to be deleted.
     * @return                      This returns a boolean which indicates a successful (true) or unsuccessful (false) delete.
     */
    public static boolean deleteAppointment(int appointmentIdToDelete){
        boolean isSuccessful = true;

        String appointmentType = "";
        String sqlStmt = "SELECT Appointment_ID, Type FROM appointments " +
                "WHERE Appointment_ID = " + appointmentIdToDelete + ";";
        try {
            Query.buildQuery(sqlStmt);
            ResultSet appointmentIdTypeResult = Query.getResult();
            while (appointmentIdTypeResult.next()) {
                appointmentType = appointmentIdTypeResult.getString("Type");
            }
        } catch(SQLException throwables) {
            System.out.println("Error in DAO.AppointmentInterface.getAllAppointmentIds");
            throwables.printStackTrace();
        }
        //Confirmation Alert Dialog
        Alert doubleCheck = new Alert(Alert.AlertType.CONFIRMATION);
        doubleCheck.setHeaderText("Confiirm Delete");
        doubleCheck.setContentText("Are you sure you want to delete\nAppointment ID: " + appointmentIdToDelete +
                ",\nType: " + appointmentType + "?");
        Optional<ButtonType> result = doubleCheck.showAndWait();
        //Delete upon confirming user wants to delete *this* appointment
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String sqlDeleteStmt = "DELETE FROM appointments WHERE Appointment_ID = " + appointmentIdToDelete + ";";
            Query.buildQuery(sqlDeleteStmt);
        } else {
            isSuccessful = false;
        }

        return isSuccessful;
    }

    /**
     * Updates an Appointment in the appointments table using the data in changeAppointment.
     * Create SQL UPDATE statement using the data from changeAppointment.
     * Try to execute the UPDATE statement and return a boolean indicating whether the UPDATE was successful (true)
     * or unsuccessful (false).
     * @param changeAppointment An Appointment that contains the data updates for an Appointment.
     * @return boolean          This returns a boolean which indicates whether the update was successful (true) or
     *                          unsuccessful (false).
     */
    public static boolean updateAppointment(Appointment changeAppointment){
        boolean isSuccessful = true;    //did the update work?
        boolean afterSetFlag = true;    //is this the first field=value pair after 'SET' part of UPDATE statement?

        //build update statement for database
        String sqlStmt = "UPDATE appointments SET ";

        if (!changeAppointment.getTitle().equals("N/A")){
            sqlStmt = sqlStmt + "Title = '" + changeAppointment.getTitle() + "'";
            afterSetFlag = false;
        }
        if (!changeAppointment.getLocation().equals("N/A")){
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            sqlStmt = sqlStmt + "Location = '" + changeAppointment.getLocation() + "'";
            afterSetFlag = false;
        }
        if (!changeAppointment.getDescription().equals("N/A")){
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            sqlStmt = sqlStmt + "Description = '" + changeAppointment.getDescription() + "'";
            afterSetFlag = false;
        }
        if (!changeAppointment.getType().equals("N/A")){
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            sqlStmt = sqlStmt + "Type = '" + changeAppointment.getType() + "'";
            afterSetFlag = false;
        }


        //remember year value of 9999 means no updates happened for either appointment date or time
        if (changeAppointment.getStart().getYear() != 9999) {
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            Timestamp addStart = Timestamp.valueOf(changeAppointment.getStart().atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
            sqlStmt = sqlStmt + "Start = '" + addStart + "'";
            afterSetFlag = false;
        }

        if (changeAppointment.getEnd().getYear() != 9999) {
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            Timestamp addEnd = Timestamp.valueOf(changeAppointment.getEnd().atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
            sqlStmt = sqlStmt + "End = '" + addEnd + "'";
            afterSetFlag = false;
        }


        if (changeAppointment.getCustomerId() > -1){
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            sqlStmt = sqlStmt + "Customer_ID = " + changeAppointment.getCustomerId();
            afterSetFlag = false;
        }
        if (changeAppointment.getUserId() > -1) {
            if (!afterSetFlag) {
                sqlStmt = sqlStmt + ", ";
            }
            sqlStmt = sqlStmt + "User_ID = " + changeAppointment.getUserId();
            afterSetFlag = false;
        }
        if (changeAppointment.getContactId() > -1) {
            if (!afterSetFlag) {
                sqlStmt = sqlStmt + ", ";
            }
            sqlStmt = sqlStmt + "Contact_ID = " + changeAppointment.getContactId();
            afterSetFlag = false;
        }

        //Since this is updating appointment, only change Last_Updated_By and Last_Updated
        //Note: these will always change on update!
        if (!changeAppointment.getLastUpdatedBy().equals("N/A")){
            if (!afterSetFlag){
                sqlStmt = sqlStmt + ", ";
            }
            Timestamp lastUpdate = Timestamp.valueOf(LocalDateTime.now().atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneId.of("UTC")).toLocalDateTime());
            sqlStmt = sqlStmt + "Last_Updated_By = '" + changeAppointment.getLastUpdatedBy() +
                    "', Last_Update = '" + lastUpdate + "'";
        }
        //finish sqlStmt
        sqlStmt = sqlStmt + " WHERE Appointment_ID = " + changeAppointment.getAppointmentId() + ";";

        Query.buildQuery(sqlStmt);

        return isSuccessful;

    }


}
