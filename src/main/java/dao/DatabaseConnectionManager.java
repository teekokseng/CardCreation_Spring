package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionManager {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/Assignment";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "123456";

    // Using ThreadLocal to manage separate connections for different threads
    private static ThreadLocal<Connection> threadLocalConnection = new ThreadLocal<>();

    // Private constructor to prevent instantiation
    private DatabaseConnectionManager() { }

    // Get connection method
    public static Connection getConnection() {
        Connection conn = threadLocalConnection.get();
        if (conn == null) {
            try {
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                threadLocalConnection.set(conn);  // Set connection to thread-local storage
                System.out.println("Connection established successfully!");
            } catch (SQLException e) {
                System.out.println("\u001B[31mFailed to connect to database!\u001B[0m");
                e.printStackTrace();
            }
        }
        return conn;
    }

    // Close connection method
    public static void closeConnection() {
        Connection conn = threadLocalConnection.get();
        if (conn != null) {
            try {
                conn.close();
                threadLocalConnection.remove(); // Remove connection from thread-local storage
                System.out.println("Database connection closed.");
            } catch (SQLException e) {
                System.out.println("\u001B[31mFailed to close database connection!\u001B[0m");
                e.printStackTrace();
            }
        }
    }
}
