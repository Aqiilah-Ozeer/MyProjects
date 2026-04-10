import db.DBConnection;
import auth.LoginFrame;
import javax.swing.*;

public class MainApplication {
    public static void main(String[] args) {
        // Test database connection first
        if (DBConnection.getInstance().testConnection()) {
            System.out.println("✓ Database connection successful!");
            SwingUtilities.invokeLater(() -> new LoginFrame());
        } else {
            System.err.println("✗ Database connection failed!");
            JOptionPane.showMessageDialog(null,
                    "Cannot connect to database!\n\n" +
                            "Please check:\n" +
                            "1. SQL Server is running\n" +
                            "2. Username/password in DBConnection.java\n" +
                            "3. Database 'UniversityDB' exists",
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
