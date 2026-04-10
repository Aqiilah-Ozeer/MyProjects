package dashboard;

import auth.LoginFrame;
import courses.CourseListFrame;
import courses.ModuleManagementFrame;
import enrollment.EnrollmentFrame;
import finance.PaymentHistoryPanel;
import java.awt.*;
import javax.swing.*;
import lecturers.LecturerListFrame;
import models.User;
import students.StudentListFrame;

public class AdminDashboard extends JFrame {
    private User user;
    private JPanel contentPanel;
    private StudentListFrame studentListFrame;
    private LecturerListFrame lecturerListFrame;
    private CourseListFrame courseListFrame;
    private ModuleManagementFrame moduleManagementFrame;
    private EnrollmentFrame enrollmentFrame;
    private PaymentHistoryPanel paymentHistoryPanel;

    public AdminDashboard(User user) {
        this.user = user;
        setTitle("Admin Dashboard - " + user.getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(true); // allow resizing
        setSize(1200, 800); // give it a normal default size
        setLocationRelativeTo(null); // center on screen

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new GridLayout(6, 1, 0, 0));
        sidebar.setBackground(new Color(30, 60, 120));
        sidebar.setPreferredSize(new Dimension(220, getHeight()));

        JButton studentsBtn = createSidebarButton("Manage Students", "/icons/students.png");
        JButton lecturersBtn = createSidebarButton("Manage Lecturers", "/icons/lecturers.png");
        JButton coursesBtn = createSidebarButton("Manage Courses", "/icons/ManageCourses.png");
        JButton modulesBtn = createSidebarButton("Manage Modules", "/icons/ManageCourses.png");
        JButton enrollmentsBtn = createSidebarButton("Enrollments", "/icons/ManageEnrollments.png");
        JButton financeBtn = createSidebarButton("Finance", "/icons/Finance.png");

        sidebar.add(studentsBtn);
        sidebar.add(lecturersBtn);
        sidebar.add(coursesBtn);
        sidebar.add(modulesBtn);
        sidebar.add(enrollmentsBtn);
        sidebar.add(financeBtn);

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(240, 240, 240));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));

        JLabel headerLabel = new JLabel("Admin Dashboard");
        headerLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));

        JLabel roleLabel = new JLabel("Role: " + user.getRole());
        roleLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        logoutBtn.addActionListener(e -> {
            dispose();
            new LoginFrame();
        });

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.add(roleLabel);
        rightPanel.add(logoutBtn);

        headerPanel.add(headerLabel, BorderLayout.WEST);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        // Content area
        contentPanel = new JPanel(new BorderLayout());

        setLayout(new BorderLayout());
        add(headerPanel, BorderLayout.NORTH);
        add(sidebar, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // Pre-initialize frames so layouts are ready
        studentListFrame = new StudentListFrame();
        studentListFrame.pack();
        studentListFrame.setVisible(false);

        lecturerListFrame = new LecturerListFrame();
        lecturerListFrame.pack();
        lecturerListFrame.setVisible(false);

        courseListFrame = new CourseListFrame();
        courseListFrame.pack();
        courseListFrame.setVisible(false);

        moduleManagementFrame = new ModuleManagementFrame();
        moduleManagementFrame.pack();
        moduleManagementFrame.setVisible(false);

        enrollmentFrame = new EnrollmentFrame();
        enrollmentFrame.pack();
        enrollmentFrame.setVisible(false);

        // Finance is now a JPanel, not a JFrame
        paymentHistoryPanel = null;

        // Button actions
        studentsBtn.addActionListener(e -> showStudents());
        lecturersBtn.addActionListener(e -> showLecturers());
        coursesBtn.addActionListener(e -> showCourses());
        modulesBtn.addActionListener(e -> showModules());
        enrollmentsBtn.addActionListener(e -> showEnrollments());
        financeBtn.addActionListener(e -> showFinance());

        showStudents();
        setVisible(true);

        // ✅ maximize only after showing, so it has a normal restore size
        setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    private JButton createSidebarButton(String text, String iconPath) {
        ImageIcon rawIcon = new ImageIcon(getClass().getResource(iconPath));
        Image scaledImg = rawIcon.getImage().getScaledInstance(22, 22, Image.SCALE_SMOOTH);
        JButton button = new JButton(text, new ImageIcon(scaledImg));
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(new Color(50, 90, 160));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setIconTextGap(15);
        return button;
    }

    // Wrapper for JFrame
    private void showContent(JFrame frame) {
        contentPanel.removeAll();
        contentPanel.add(frame.getContentPane(), BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // Wrapper for JPanel
    private void showContent(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void showStudents() {
        showContent(studentListFrame);
    }

    private void showLecturers() {
        showContent(lecturerListFrame);
    }

    private void showCourses() {
        showContent(courseListFrame);
    }

    private void showModules() {
        showContent(moduleManagementFrame);
    }

    private void showEnrollments() {
        showContent(enrollmentFrame);
    }

    private void showFinance() {
        if (paymentHistoryPanel == null) {
            paymentHistoryPanel = new PaymentHistoryPanel();
        }
        showContent(paymentHistoryPanel);
    }
}