package enrollment;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.sql.*;
import db.DBConnection;

public class EnrollmentFrame extends JFrame {
    private JTable enrollmentTable;
    private DefaultTableModel tableModel;

    public EnrollmentFrame() {
        setTitle("Enrollment Management");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columns = { "Student ID", "Student Name", "Course ID", "Course Name", "Enrollment Date" };
        tableModel = new DefaultTableModel(columns, 0);
        enrollmentTable = new JTable(tableModel);
        loadEnrollments();

        JButton enrollButton = new JButton("Enroll Student");
        JButton editButton = new JButton("Edit Enrollment");
        JButton deleteButton = new JButton("Delete Enrollment");
        JButton refreshButton = new JButton("Refresh");

        enrollButton.addActionListener(e -> showEnrollDialog());
        editButton.addActionListener(e -> editEnrollment());
        deleteButton.addActionListener(e -> deleteEnrollment());
        refreshButton.addActionListener(e -> loadEnrollments());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(enrollButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        add(new JScrollPane(enrollmentTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void loadEnrollments() {
        tableModel.setRowCount(0);
        String sql = "SELECT e.student_id, s.first_name + ' ' + s.last_name AS student_name, " +
                "e.course_id, c.name AS course_name, e.enrollment_date " +
                "FROM Enrollments e " +
                "JOIN Students s ON e.student_id = s.student_id " +
                "JOIN Courses c ON e.course_id = c.id";
        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("student_id"),
                        rs.getString("student_name"),
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("enrollment_date")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showEnrollDialog() {
        JComboBox<String> studentBox = new JComboBox<>();
        JComboBox<String> courseBox = new JComboBox<>();

        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT student_id, first_name, last_name FROM Students");
            while (rs.next()) {
                studentBox.addItem(
                        rs.getInt("student_id") + " - " + rs.getString("first_name") + " " + rs.getString("last_name"));
            }

            rs = stmt.executeQuery("SELECT id, name FROM Courses");
            while (rs.next()) {
                courseBox.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Student:"));
        panel.add(studentBox);
        panel.add(new JLabel("Course:"));
        panel.add(courseBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Enroll Student", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int studentId = Integer.parseInt(((String) studentBox.getSelectedItem()).split(" - ")[0]);
            int courseId = Integer.parseInt(((String) courseBox.getSelectedItem()).split(" - ")[0]);

            String insertSql = "INSERT INTO Enrollments (student_id, course_id, enrollment_date) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                ps.setString(3, LocalDate.now().toString());
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Student enrolled successfully!");
                loadEnrollments();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void editEnrollment() {
        int row = enrollmentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an enrollment to edit.");
            return;
        }

        int oldStudentId = (int) tableModel.getValueAt(row, 0);
        int oldCourseId = (int) tableModel.getValueAt(row, 2);

        JComboBox<String> courseBox = new JComboBox<>();
        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT id, name FROM Courses");
            while (rs.next()) {
                String item = rs.getInt("id") + " - " + rs.getString("name");
                courseBox.addItem(item);
                if (rs.getInt("id") == oldCourseId) {
                    courseBox.setSelectedItem(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.add(new JLabel("Student:"));
        panel.add(new JLabel(tableModel.getValueAt(row, 1).toString()));
        panel.add(new JLabel("Course:"));
        panel.add(courseBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Enrollment", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            int newCourseId = Integer.parseInt(((String) courseBox.getSelectedItem()).split(" - ")[0]);
            String sql = "UPDATE Enrollments SET course_id = ?, enrollment_date = ? WHERE student_id = ? AND course_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, newCourseId);
                ps.setString(2, LocalDate.now().toString());
                ps.setInt(3, oldStudentId);
                ps.setInt(4, oldCourseId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Enrollment updated successfully!");
                loadEnrollments();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void deleteEnrollment() {
        int row = enrollmentTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an enrollment to delete.");
            return;
        }

        int studentId = (int) tableModel.getValueAt(row, 0);
        String studentName = (String) tableModel.getValueAt(row, 1);
        int courseId = (int) tableModel.getValueAt(row, 2);
        String courseName = (String) tableModel.getValueAt(row, 3);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove " + studentName + " from " + courseName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = DBConnection.getInstance().getConnection();
            String sql = "DELETE FROM Enrollments WHERE student_id = ? AND course_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, studentId);
                ps.setInt(2, courseId);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Enrollment deleted successfully!");
                    loadEnrollments();
                } else {
                    JOptionPane.showMessageDialog(this, "Enrollment not found.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
}