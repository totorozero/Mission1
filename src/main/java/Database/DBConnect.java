package Database;

import java.sql.*;

public class DBConnect {
    public static Connection connectDB() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            String dbFile = "C:\\dev\\sqlite\\wifi.db";
            conn = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        return conn;
    }

    public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {

        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (preparedStatement != null && !preparedStatement.isClosed()) {
                preparedStatement.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
