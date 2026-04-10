package auth;

import dashboard.AdminDashboard;
import dashboard.LecturerDashboard;
import dashboard.StudentDashboard;
import java.awt.*;
import javax.swing.*;
import models.User;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleCombo;
    private AuthService authService = new AuthService();

    public LoginFrame() {
        setTitle("University Management System - Login");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        add(mainPanel);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(30, 144, 255));
        header.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 10));

        JLabel systemTitle = new JLabel("University Management System");
        systemTitle.setFont(new Font("SansSerif", Font.BOLD, 24));
        systemTitle.setForeground(Color.WHITE);
        header.add(systemTitle, BorderLayout.WEST);

        mainPanel.add(header, BorderLayout.NORTH);

        // Login card
        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1),
                BorderFactory.createEmptyBorder(30, 50, 30, 50)));
        loginCard.setBackground(Color.WHITE);

        // Icon
        ImageIcon rawIcon = new ImageIcon(getClass().getResource("/icons/login.png"));
        Image scaledImg = rawIcon.getImage().getScaledInstance(72, 72, Image.SCALE_SMOOTH);
        JLabel iconLabel = new JLabel(new ImageIcon(scaledImg));
        iconLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(iconLabel);
        loginCard.add(Box.createVerticalStrut(10));

        JLabel loginTitle = new JLabel("User Login");
        loginTitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        loginTitle.setForeground(new Color(60, 60, 60));
        loginTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginCard.add(loginTitle);
        loginCard.add(Box.createVerticalStrut(20));

        Dimension inputSize = new Dimension(250, 30);

        // Username
        JPanel userRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        userRow.setOpaque(false);
        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        usernameField = new JTextField();
        usernameField.setPreferredSize(inputSize);
        userRow.add(userLabel);
        userRow.add(usernameField);

        // Password
        JPanel passRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passRow.setOpaque(false);
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(inputSize);
        passRow.add(passLabel);
        passRow.add(passwordField);

        // Role
        JPanel roleRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        roleRow.setOpaque(false);
        JLabel roleLabel = new JLabel("Login As:");
        roleLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        roleCombo = new JComboBox<>(new String[] { "Select", "Admin", "Lecturer", "Student" });
        roleCombo.setPreferredSize(inputSize);
        roleRow.add(roleLabel);
        roleRow.add(roleCombo);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        loginButton.setBackground(new Color(30, 144, 255));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        clearButton.setBackground(new Color(220, 220, 220));
        clearButton.setForeground(Color.DARK_GRAY);
        clearButton.setFocusPainted(false);

        buttonPanel.add(loginButton);
        buttonPanel.add(clearButton);

        loginCard.add(userRow);
        loginCard.add(Box.createVerticalStrut(15));
        loginCard.add(passRow);
        loginCard.add(Box.createVerticalStrut(15));
        loginCard.add(roleRow);
        loginCard.add(Box.createVerticalStrut(20));
        loginCard.add(buttonPanel);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(new Color(245, 245, 245));
        centerPanel.add(loginCard);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        loginButton.addActionListener(e -> login());
        clearButton.addActionListener(e -> clearFields());

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleCombo.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || role.equals("Select")) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }

        User user = authService.login(username, password, role);
        if (user != null) {
            JOptionPane.showMessageDialog(this, "Login Successful!");

            String normalizedRole = user.getRole().toLowerCase();
            switch (normalizedRole) {
                case "admin":
                    new AdminDashboard(user);
                    break;
                case "student":
                    new StudentDashboard(user);
                    break;
                case "lecturer":
                    new LecturerDashboard(user);
                    break;
                default:
                    JOptionPane.showMessageDialog(this, "Unknown role!");
                    return;
            }
            this.dispose(); // close login frame
        } else {
            JOptionPane.showMessageDialog(this, "Invalid username, password, or role");
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        roleCombo.setSelectedIndex(0);
    }
}

