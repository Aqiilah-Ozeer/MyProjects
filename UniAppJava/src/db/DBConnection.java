package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    private Connection connection;
    private final String url = "jdbc:sqlserver://OZEER_LAPTOP\\SQLEXPRESS;databaseName=UniOosdDB;encrypt=true;trustServerCertificate=true;"; // SQL Server URL
    private final String username = "sa"; // SQL Server username
    private final String password = "@ozeerNserver249"; // SQL Server password

    // Private constructor to prevent instantiation
    private DBConnection() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            this.connection = DriverManager.getConnection(url, username, password);
            this.connection.setAutoCommit(true);
            System.out.println("Database connected successfully!");
        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    // Thread-safe singleton instance getter
    public static synchronized DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(url, username, password);
                connection.setAutoCommit(true);
                System.out.println("Database reconnected successfully!");
            }
        } catch (SQLException e) {
            System.out.println("Connection error!");
            e.printStackTrace();
        }
        return connection;
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean testConnection() {
        return connection != null;
    }
}
