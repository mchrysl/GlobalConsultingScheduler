package DAO;


import java.sql.*;

/**
 * The methods in this class facilitate and help manage the connection between this program and the database
 * this program accesses for data.
 */
public class DatabaseConnection {

    //open db connection variables
    private static final String serverName = "wgudb.ucertify.com";
    private static final String port =":3306";
    private static final String databaseName = "WJ06zQf";

    private static final String dbURL = "jdbc:mysql://" + serverName + port + "/" + databaseName;
    private static final String username = "U06zQf";
    private static final String password = "53688916629";
    public static Connection openConn;

    /**
     * Opens a connection with the database.
     * @return This returns the Connection identifier for the database connection.
     */
    public static Connection openDbConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(dbURL, username, password);
        } catch (SQLException throwables) {
            System.out.println("DB Connection Failed" + conn);
            throwables.printStackTrace();
        }
        return conn;
    }

    /**
     * Closes the database connection.
     */
    public static void closeDBConnection(){
        try {
            openConn.close();
        } catch (SQLException throwables) {
            System.out.println("Database did not close properly. See stack trace.");
            throwables.printStackTrace();
        }
    }

    /**
     * Returns an integer (int) of the next primary key for the requested table.
     * @param tableName A String of the name of the table for which the primary key is to be found.
     * @return int      This returns the next primary key for the requested table.
     */
    public static int getNextTablePrimaryKey(String tableName){
        int nextPrimaryKey = -1;
        ResultSet queryResultSet = null;
        String findPK = "SELECT AUTO_INCREMENT " +
                        "FROM information_schema.TABLES " +
                        "WHERE TABLE_SCHEMA = '" + databaseName + "' " +
                        "AND TABLE_NAME = '" + tableName + "';";

        try {
            Statement statement = openConn.prepareStatement(findPK);
            queryResultSet = statement.executeQuery(findPK);
            while (queryResultSet.next()){
                nextPrimaryKey = queryResultSet.getInt(1);
            }

        } catch (SQLException throwables){
            System.out.println("Query Error: " + throwables.getMessage());
            throwables.printStackTrace();
        }

        return nextPrimaryKey;
    }
}
