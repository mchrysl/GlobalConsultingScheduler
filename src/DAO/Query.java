package DAO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static DAO.DatabaseConnection.openConn;

/**
 * This class is used to build queries based on the SQL statements used as queryInput.
 */
public class Query {

    private static ResultSet queryResultSet;

    /**
     * Builds a query using the String queryInput which is an SQL statement.
     * @param queryInput    A String that is a valid SQL statement of ADD, UPDATE, DELETE, or INSERT.
     */
    public static void buildQuery(String queryInput) {

        try {
            //create statement
            Statement statement = openConn.prepareStatement(queryInput);
            //determine which method to use
            if (queryInput.toLowerCase().startsWith("select")) {
                queryResultSet = statement.executeQuery(queryInput);
            }
            if (queryInput.toLowerCase().startsWith("update") || queryInput.toLowerCase().startsWith("delete") ||
                    queryInput.toLowerCase().startsWith("insert")) {
                statement.executeUpdate(queryInput);
            }
        } catch (SQLException throwables){
            System.out.println("Query Error: " + throwables.getMessage());
            throwables.printStackTrace();
        }
    }

    public static ResultSet getResult()
    {
        return queryResultSet;
    }

}
