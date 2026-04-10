package courses;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import db.DBConnection;

public class CourseListFrame extends JFrame {
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JTabbedPane tabbedPane;

    public CourseListFrame() {
        setTitle("Course Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Courses", createCoursesPanel());
        tabbedPane.addTab("Modules", createModulesPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }

    private JPanel createCoursesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = { "ID", "Course Name", "Description", "Credits" };
        tableModel = new DefaultTableModel(columns, 0);
        courseTable = new JTable(tableModel);
        loadCourses();

        JButton addButton = new JButton("Add Course");
        JButton editButton = new JButton("Edit Course");
        JButton deleteButton = new JButton("Delete Course");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> addCourse());
        editButton.addActionListener(e -> editCourse());
        deleteButton.addActionListener(e -> deleteCourse());
        refreshButton.addActionListener(e -> loadCourses());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        panel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadCourses() {
        tableModel.setRowCount(0);
        String sql = "SELECT id, name, description, credits FROM Courses";
        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tableModel.addRow(new Object[] {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("description"),
                        rs.getInt("credits")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addCourse() {
        JTextField nameField = new JTextField();
        JTextField descField = new JTextField();
        JTextField creditsField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Course Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(new JLabel("Credits:"));
        panel.add(creditsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Course", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement ps = conn
                            .prepareStatement("INSERT INTO Courses (name, description, credits) VALUES (?, ?, ?)")) {
                ps.setString(1, nameField.getText());
                ps.setString(2, descField.getText());
                ps.setInt(3, Integer.parseInt(creditsField.getText()));
                ps.executeUpdate();
                loadCourses();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void editCourse() {
        int row = courseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to edit.");
            return;
        }

        int courseId = (int) tableModel.getValueAt(row, 0);
        String currentName = (String) tableModel.getValueAt(row, 1);
        String currentDesc = (String) tableModel.getValueAt(row, 2);
        int currentCredits = (int) tableModel.getValueAt(row, 3);

        JTextField nameField = new JTextField(currentName);
        JTextField descField = new JTextField(currentDesc);
        JTextField creditsField = new JTextField(String.valueOf(currentCredits));

        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(new JLabel("Course Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Description:"));
        panel.add(descField);
        panel.add(new JLabel("Credits:"));
        panel.add(creditsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Course", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement ps = conn
                            .prepareStatement("UPDATE Courses SET name = ?, description = ?, credits = ? WHERE id = ?")) {
                ps.setString(1, nameField.getText());
                ps.setString(2, descField.getText());
                ps.setInt(3, Integer.parseInt(creditsField.getText()));
                ps.setInt(4, courseId);
                ps.executeUpdate();
                loadCourses();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }

    private void deleteCourse() {
        int row = courseTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a course to delete.");
            return;
        }

        int courseId = (int) tableModel.getValueAt(row, 0);
        String courseName = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete course: " + courseName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Courses WHERE id = ?")) {
                ps.setInt(1, courseId);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Course deleted successfully!");
                    loadCourses();
                } else {
                    JOptionPane.showMessageDialog(this, "Course not found.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting course: " + e.getMessage());
            }
        }
    }

    private JPanel createModulesPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        String[] columns = { "ID", "Module Name", "Course ID", "Course Name", "Lecturer", "Schedule", "Credits" };
        DefaultTableModel moduleModel = new DefaultTableModel(columns, 0);
        JTable moduleTable = new JTable(moduleModel);
        loadModules(moduleModel);

        JButton addButton = new JButton("Add Module");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> addModule(moduleModel));
        refreshButton.addActionListener(e -> loadModules(moduleModel));

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(refreshButton);

        panel.add(new JScrollPane(moduleTable), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void loadModules(DefaultTableModel model) {
        model.setRowCount(0);
        String sql = "SELECT m.id, m.name, m.course_id, c.name AS course_name, "
                + "l.First_Name + ' ' + l.Last_Name AS lecturer_name, m.schedule, m.credits "
                + "FROM Modules m JOIN Courses c ON m.course_id = c.id "
                + "LEFT JOIN Lecturer l ON m.lecturer_id = l.Lecturer_Id";
        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                model.addRow(new Object[] {
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("course_id"),
                        rs.getString("course_name"),
                        rs.getString("lecturer_name"),
                        rs.getString("schedule"),
                        rs.getInt("credits")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addModule(DefaultTableModel model) {
        JTextField nameField = new JTextField();
        JComboBox<String> courseBox = new JComboBox<>();
        JTextField lecturerField = new JTextField();
        JTextField scheduleField = new JTextField();
        JTextField creditsField = new JTextField();

        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT id, name FROM Courses")) {
            while (rs.next()) {
                courseBox.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("Module Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Course:"));
        panel.add(courseBox);
        panel.add(new JLabel("Lecturer:"));
        panel.add(lecturerField);
        panel.add(new JLabel("Schedule:"));
        panel.add(scheduleField);
        panel.add(new JLabel("Credits:"));
        panel.add(creditsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Module", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                int courseId = Integer.parseInt(((String) courseBox.getSelectedItem()).split(" - ")[0]);
                String sql = "INSERT INTO Modules (name, course_id, lecturer_id, schedule, credits) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(sql)) {
                    ps.setString(1, nameField.getText());
                    ps.setInt(2, courseId);
                    ps.setString(3, lecturerField.getText());
                    ps.setString(4, scheduleField.getText());
                    ps.setInt(5, Integer.parseInt(creditsField.getText()));
                    ps.executeUpdate();
                    loadModules(model);
                }
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            }
        }
    }
}