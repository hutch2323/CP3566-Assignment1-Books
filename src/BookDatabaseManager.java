import java.sql.*;

public class BookDatabaseManager {
    static private String DB_URL = "jdbc:mariadb://localhost:3300/books";
    static private String USER = "root";
    static private String PASS = "password";
//    static final String QUERY = "SELECT authorID, firstName, lastName FROM authors";

    private void loadDatabase(){

    }

//    public static void main(String[] args) {
//        // Open a connection
//        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
//             Statement stmt = conn.createStatement();
//             ResultSet rs = stmt.executeQuery(QUERY);) {
//            // Extract data from result set
//            while (rs.next()) {
//                // Retrieve by column name
//                System.out.print("ID: " + rs.getInt("authorID"));
//                System.out.print(", First: " + rs.getString("firstName"));
//                System.out.println(", Last: " + rs.getString("lastName"));
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
}