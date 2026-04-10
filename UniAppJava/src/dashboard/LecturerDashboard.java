package dashboard;

import auth.LoginFrame;
import courses.CourseListFrame;
import db.DBConnection;
import java.awt.*;
import java.io.File;
import java.sql.Connection;
import javax.swing.*;
import lecturers.GradeAssignmentFrame;
import lecturers.StudentRecordsFrame;
import lecturers.ViewCoursesModulesFrame;
import lecturers.ViewProfileFrame;
import models.User;

public class LecturerDashboard extends JFrame {
    private User user;

    public LecturerDashboard(User user) {
        this.user = user;

        setTitle("Lecturer Dashboard - " + user.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 144, 255));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel titleLabel = new JLabel("Lecturer Dashboard");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        header.add(titleLabel, BorderLayout.WEST);

        JLabel profileLabel = new JLabel("Welcome, Lecturer (" + user.getUsername() + ")");
        profileLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        profileLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutButton.addActionListener(e -> {
            this.dispose();
            new LoginFrame();
        });

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(profileLabel);
        rightPanel.add(logoutButton);
        header.add(rightPanel, BorderLayout.EAST);

        mainPanel.add(header, BorderLayout.NORTH);

        // Cards panel
        JPanel cardPanel = new JPanel(new GridLayout(2, 2, 60, 60));
        cardPanel.setBorder(BorderFactory.createEmptyBorder(60, 100, 60, 100));

        // Four required cards
        cardPanel.add(createCard("My Profile", "View your lecturer information", "profile.png",
                e -> viewProfile()));

        cardPanel.add(createCard("Enter Marks", "Enter student marks for your modules", "EnterResults.png",
                e -> enterMarks()));

        cardPanel.add(createCard("View Students Records", "View student records", "studentRecords.png",
                e -> viewStudentsRecords()));

        cardPanel.add(createCard("Courses & Modules", "View courses and modules you teach", "lecturerCourse.png",
                e -> viewCoursesModules()));

        mainPanel.add(cardPanel, BorderLayout.CENTER);

        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JPanel createCard(String title, String description, String filename, java.awt.event.ActionListener action) {
        JPanel card = new JPanel(new BorderLayout(20, 20));
        card.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 2));
        card.setBackground(Color.WHITE);
        card.setPreferredSize(new Dimension(380, 240));

        // Load icon from src/icons folder
        ImageIcon icon = loadIcon(filename);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel descLabel = new JLabel("<html><center>" + description + "</center></html>");
        descLabel.setFont(new Font("SansSerif", Font.PLAIN, 15));
        descLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JButton goButton = new JButton("View");
        goButton.setFont(new Font("SansSerif", Font.BOLD, 15));
        goButton.addActionListener(action);

        JPanel textPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        textPanel.setBackground(Color.WHITE);
        textPanel.add(titleLabel);
        textPanel.add(descLabel);
        textPanel.add(goButton);

        card.add(iconLabel, BorderLayout.NORTH);
        card.add(textPanel, BorderLayout.CENTER);
        return card;
    }

    private ImageIcon loadIcon(String filename) {
        // Try multiple locations to find the icon (focusing on src/icons)
        String[] paths = {
            // Most likely location - src/icons folder
            "src/icons/" + filename,
            "./src/icons/" + filename,
            "../src/icons/" + filename,
            
            // Project root locations
            "icons/" + filename,
            "./icons/" + filename,
            
            // Current directory
            filename,
            "./" + filename,
            
            // Full absolute paths
            "G:/university-erp-oosd-main/UniAppOosd/src/icons/" + filename,
            System.getProperty("user.dir") + "/src/icons/" + filename,
            System.getProperty("user.dir") + "/icons/" + filename
        };
        
        for (String path : paths) {
            File file = new File(path);
            if (file.exists()) {
                try {
                    ImageIcon originalIcon = new ImageIcon(file.getAbsolutePath());
                    Image scaledImg = originalIcon.getImage().getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                    System.out.println(" Loaded icon: " + file.getAbsolutePath());
                    return new ImageIcon(scaledImg);
                } catch (Exception e) {
                    System.err.println("Error loading from " + path + ": " + e.getMessage());
                }
            }
        }
        
        // If icon not found, print debug info
        System.err.println(" Icon not found: " + filename);
        System.err.println("   Current working directory: " + System.getProperty("user.dir"));
        
        // List directories to help debug
        File srcDir = new File("src");
        if (srcDir.exists() && srcDir.isDirectory()) {
            System.err.println("   src directory exists");
            File iconsDir = new File("src/icons");
            if (iconsDir.exists() && iconsDir.isDirectory()) {
                System.err.println("   src/icons directory exists");
                System.err.println("   Files in src/icons:");
                for (File f : iconsDir.listFiles()) {
                    System.err.println("      - " + f.getName());
                }
            } else {
                System.err.println("   src/icons directory does NOT exist");
            }
        } else {
            System.err.println("   src directory does NOT exist");
        }
        
        // Return a blank icon (dashboard will still work)
        return new ImageIcon();
    }

    private void enterMarks() {
    System.out.println("Opening Grade Assignment for lecturer: " + user.getUsername());
    Connection conn = DBConnection.getInstance().getConnection();
    if (conn != null) {
        GradeAssignmentFrame gradeFrame = new GradeAssignmentFrame(String.valueOf(user.getUsername()), conn);
        gradeFrame.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(this,
            "Cannot connect to database. Please check your database connection.",
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void viewStudentsRecords() {
    System.out.println("Opening Student Records for lecturer: " + user.getUsername());
    try {
        StudentRecordsFrame frame = new StudentRecordsFrame(String.valueOf(user.getUsername()));
        frame.setVisible(true);
        System.out.println("StudentRecordsFrame should now be visible");
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this,
            "Error opening Student Records: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void viewCoursesModules() {
    System.out.println("Opening Courses and Modules for lecturer - UserID: " + user.getUsername());
    try {
        ViewCoursesModulesFrame frame = new ViewCoursesModulesFrame(user);
        frame.setVisible(true);
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, 
            "Error opening Courses and Modules: " + e.getMessage(), 
            "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    private void viewProfile() {
        new ViewProfileFrame(String.valueOf(user.getUsername()));
    }
}
