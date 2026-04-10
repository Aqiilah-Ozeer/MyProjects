package enrollment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import db.DBConnection;

public class MyEnrollmentsFrame extends JFrame {
    private int studentId;
    private JLabel courseLabel;
    private JTable modulesTable;
    private DefaultTableModel tableModel;
    private int currentCourseId = -1;
    private JButton courseActionBtn;

    public MyEnrollmentsFrame(int studentId) {
        this.studentId = studentId;

        setTitle("My Enrollments - Student " + studentId);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 144, 255));
        headerPanel.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        JLabel titleLabel = new JLabel("My Enrollments");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.WEST);
        add(headerPanel, BorderLayout.NORTH);

        // Course info panel
        JPanel coursePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        coursePanel.setBorder(BorderFactory.createTitledBorder("Enrolled Course"));
        courseLabel = new JLabel("Loading...");
        courseLabel.setFont(new Font("SansSerif", Font.BOLD, 15));
        coursePanel.add(courseLabel);
        add(coursePanel, BorderLayout.SOUTH);

        // Modules table
        String[] columns = {"Module ID", "Module Name", "Lecturer", "Schedule", "Credits"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        modulesTable = new JTable(tableModel);
        modulesTable.setRowHeight(28);
        modulesTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        modulesTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(modulesTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("My Modules"));
        add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        courseActionBtn = new JButton("Enroll");
        courseActionBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        courseActionBtn.addActionListener(e -> {
            if (currentCourseId == -1) {
                showEnrollDialog();
            } else {
                showChangeCourseDialog();
            }
        });

        buttonPanel.add(courseActionBtn);

        // Wrap course panel and buttons at the bottom
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(coursePanel, BorderLayout.NORTH);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(bottomPanel, BorderLayout.SOUTH);

        loadEnrollment();
        loadModules();
    }

    private void loadEnrollment() {
        String sql = "SELECT e.course_id, c.name AS course_name, c.credits, e.enrollment_date " +
                "FROM Enrollments e JOIN Courses c ON e.course_id = c.id " +
                "WHERE e.student_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, studentId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                currentCourseId = rs.getInt("course_id");
                String courseName = rs.getString("course_name");
                int credits = rs.getInt("credits");
                String date = rs.getString("enrollment_date");
                courseLabel.setText("Course: " + courseName + "   |   Credits: " + credits + "   |   Enrolled: " + date);
                courseActionBtn.setText("Change Course");
            } else {
                currentCourseId = -1;
                courseLabel.setText("You are not enrolled in any course.");
                courseActionBtn.setText("Enroll");
            }
        } catch (SQLException e) {
            courseLabel.setText("Error loading enrollment.");
            e.printStackTrace();
        }
    }

    private void loadModules() {
        tableModel.setRowCount(0);
        if (currentCourseId == -1) {
            return;
        }
        String sql = "SELECT m.id, m.name, l.First_Name + ' ' + l.Last_Name AS lecturer, m.schedule, m.credits " +
                "FROM Modules m LEFT JOIN Lecturer l ON m.lecturer_id = l.Lecturer_Id " +
                "WHERE m.course_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, currentCourseId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("lecturer"),
                        rs.getString("schedule"),
                        rs.getInt("credits")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading modules: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showEnrollDialog() {
        JComboBox<String> courseBox = new JComboBox<>();
        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, name FROM Courses");
            while (rs.next()) {
                courseBox.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (courseBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No courses available.");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.add(new JLabel("Select Course:"));
        panel.add(courseBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Enroll in Course", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int courseId = Integer.parseInt(((String) courseBox.getSelectedItem()).split(" - ")[0]);

            String insertSql = "INSERT INTO Enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, CONVERT(VARCHAR(10), GETDATE(), 120))";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Enrolled successfully!");
                loadEnrollment();
                loadModules();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error enrolling: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void showChangeCourseDialog() {
        JComboBox<String> courseBox = new JComboBox<>();
        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, name FROM Courses");
            while (rs.next()) {
                int id = rs.getInt("id");
                if (id != currentCourseId) {
                    courseBox.addItem(id + " - " + rs.getString("name"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (courseBox.getItemCount() == 0) {
            JOptionPane.showMessageDialog(this, "No other courses available.");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(1, 2, 10, 10));
        panel.add(new JLabel("New Course:"));
        panel.add(courseBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Change Course", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int newCourseId = Integer.parseInt(((String) courseBox.getSelectedItem()).split(" - ")[0]);

            String updateSql = "UPDATE Enrollments SET course_id = ?, enrollment_date = CONVERT(VARCHAR(10), GETDATE(), 120) " +
                    "WHERE student_id = ? AND course_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setInt(1, newCourseId);
                ps.setInt(2, studentId);
                ps.setInt(3, currentCourseId);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Course changed successfully!");
                    loadEnrollment();
                    loadModules();
                } else {
                    JOptionPane.showMessageDialog(this, "No enrollment found to update.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error changing course: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}
