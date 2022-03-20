package DAO;

import model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;


/**
 * The methods in this class facilitate access to database users table using the classes in the model folder,
 * using SQL statements to gather observableLists and resultSets that can be utilized in the controller classes
 * that make the information accessible via the fxml forms/views.
 */
public class UserImpl {

    /**
     * Returns the User associated with the String of the username input into the login screen or returns null
     * if no User with that username is found.
     * @param userNameInput The String of the username input into the login screen.
     * @return User         This returns the User associated with the String userNameInput, returns null if not found.
     */
    public static User getUser(String userNameInput) {
        String sqlStmt = "select * FROM users WHERE User_Name = '" + userNameInput +"'";
        Query.buildQuery(sqlStmt);
        ResultSet userResultSet = Query.getResult();
        try {
            while (userResultSet.next()) {
                int userId = userResultSet.getInt("User_ID");
                String userName = userResultSet.getString("User_Name");
                String password = userResultSet.getString("Password");
                String createDateStr = userResultSet.getString("Create_Date");
                //System.out.println("Format Testing DATETIME '\n" + createDateStr);
                Date createDate = (Date) userResultSet.getObject("Create_Date");
                //System.out.println(createDate);
                //ZonedDateTime createDate = null;    //ZonedDateTime.from(userResultSet.getTimestamp("Create_Date").toLocalDateTime());
                String createdBy = userResultSet.getString("Created_By");
                String lastUpdatedStr = userResultSet.getString("Last_Update");
                //System.out.println("Format Testing TIMESTAMP '\n" + lastUpdatedStr);
                Date lastUpdated = (Date) userResultSet.getObject("Last_Update");
                //System.out.println(lastUpdated);
                //ZonedDateTime lastUpdated = null;    //ZonedDateTime.from(userResultSet.getTimestamp("Last_Update").toLocalDateTime());
                String lastUpdatedBy = userResultSet.getString("Last_Updated_By");

                User singleUser = new User(userId, userName, password, createDate, createdBy, lastUpdated, lastUpdatedBy);
                return singleUser;
            }
        }catch (SQLException throwables){
            System.out.println("Error getting User. See stack trace.");
            throwables.printStackTrace();
        }
        //no matching user found
        return null;
    }


    /**
     * Returns an integer (int) user ID that is associated with the String inputUsername.
     * @param inputUsername A String representation of the username input on the login form.
     * @return int          This returns an integer (int) user ID that is associated with the String inputUsername.
     */
    public static int getUserId(String inputUsername){
        int foundUserId = 0;
        ResultSet userIdResults;
        String sqlStmt = "SELECT User_ID, User_Name FROM users WHERE User_Name = '" + inputUsername + "';";
        try {
            Query.buildQuery(sqlStmt);
            userIdResults = Query.getResult();
            while (userIdResults.next()) {
                foundUserId = userIdResults.getInt("User_ID");
            }
        } catch (SQLException throwables){
            System.out.println("Problem getting User_ID" +foundUserId+" for: " + inputUsername);
            throwables.printStackTrace();
        }

        return foundUserId;
    }


}

