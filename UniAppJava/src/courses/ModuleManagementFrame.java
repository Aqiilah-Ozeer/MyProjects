package courses;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import db.DBConnection;

public class ModuleManagementFrame extends JFrame {
    private JTable moduleTable;
    private DefaultTableModel tableModel;

    public ModuleManagementFrame() {
        setTitle("Module Management");
        setSize(950, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Table
        String[] columns = {"ID", "Module Name", "Course", "Lecturer", "Schedule", "Credits"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        moduleTable = new JTable(tableModel);
        moduleTable.setRowHeight(26);
        moduleTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 13));
        moduleTable.setFont(new Font("SansSerif", Font.PLAIN, 13));
        moduleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(moduleTable), BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton addBtn = new JButton("Add Module");
        JButton editBtn = new JButton("Edit Module");
        JButton deleteBtn = new JButton("Delete Module");
        JButton refreshBtn = new JButton("Refresh");

        for (JButton btn : new JButton[]{addBtn, editBtn, deleteBtn, refreshBtn}) {
            btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        }

        addBtn.addActionListener(e -> showAddDialog());
        editBtn.addActionListener(e -> showEditDialog());
        deleteBtn.addActionListener(e -> deleteModule());
        refreshBtn.addActionListener(e -> loadModules());

        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        loadModules();
    }

    private void loadModules() {
        tableModel.setRowCount(0);
        String sql = "SELECT m.id, m.name, c.name AS course_name, " +
                "l.First_Name + ' ' + l.Last_Name AS lecturer_name, m.schedule, m.credits " +
                "FROM Modules m " +
                "LEFT JOIN Courses c ON m.course_id = c.id " +
                "LEFT JOIN Lecturer l ON m.lecturer_id = l.Lecturer_Id " +
                "ORDER BY m.id";
        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("course_name"),
                        rs.getString("lecturer_name"),
                        rs.getString("schedule"),
                        rs.getInt("credits")
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading modules: " + e.getMessage());
        }
    }

    private void showAddDialog() {
        JTextField nameField = new JTextField();
        JComboBox<String> courseBox = new JComboBox<>();
        JComboBox<String> lecturerBox = new JComboBox<>();
        JTextField scheduleField = new JTextField();
        JTextField creditsField = new JTextField();

        loadCourses(courseBox);
        loadLecturers(lecturerBox);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Module Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Course:"));
        panel.add(courseBox);
        panel.add(new JLabel("Lecturer:"));
        panel.add(lecturerBox);
        panel.add(new JLabel("Schedule:"));
        panel.add(scheduleField);
        panel.add(new JLabel("Credits:"));
        panel.add(creditsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Add Module", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Module name is required.");
                return;
            }
            int courseId = extractId((String) courseBox.getSelectedItem());
            String lecturerId = extractStringId((String) lecturerBox.getSelectedItem());
            String schedule = scheduleField.getText().trim();
            if (schedule.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Schedule is required.");
                return;
            }
            int credits;
            try {
                credits = Integer.parseInt(creditsField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Credits must be a number.");
                return;
            }

            String sql = "INSERT INTO Modules (name, course_id, lecturer_id, schedule, credits) VALUES (?, ?, ?, ?, ?)";
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setInt(2, courseId);
                ps.setString(3, lecturerId);
                ps.setString(4, schedule);
                ps.setInt(5, credits);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Module added successfully!");
                loadModules();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error adding module: " + e.getMessage());
            }
        }
    }

    private void showEditDialog() {
        int row = moduleTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a module to edit.");
            return;
        }

        int moduleId = (int) tableModel.getValueAt(row, 0);
        String currentName = (String) tableModel.getValueAt(row, 1);
        String currentSchedule = (String) tableModel.getValueAt(row, 4);
        int currentCredits = (int) tableModel.getValueAt(row, 5);

        JTextField nameField = new JTextField(currentName);
        JComboBox<String> courseBox = new JComboBox<>();
        JComboBox<String> lecturerBox = new JComboBox<>();
        JTextField scheduleField = new JTextField(currentSchedule);
        JTextField creditsField = new JTextField(String.valueOf(currentCredits));

        loadCourses(courseBox);
        loadLecturers(lecturerBox);

        // Pre-select current course and lecturer
        String currentCourse = (String) tableModel.getValueAt(row, 2);
        String currentLecturer = (String) tableModel.getValueAt(row, 3);
        selectComboItem(courseBox, currentCourse);
        selectComboItem(lecturerBox, currentLecturer);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.add(new JLabel("Module Name:"));
        panel.add(nameField);
        panel.add(new JLabel("Course:"));
        panel.add(courseBox);
        panel.add(new JLabel("Lecturer:"));
        panel.add(lecturerBox);
        panel.add(new JLabel("Schedule:"));
        panel.add(scheduleField);
        panel.add(new JLabel("Credits:"));
        panel.add(creditsField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Edit Module", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            String name = nameField.getText().trim();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Module name is required.");
                return;
            }
            int courseId = extractId((String) courseBox.getSelectedItem());
            String lecturerId = extractStringId((String) lecturerBox.getSelectedItem());
            String schedule = scheduleField.getText().trim();
            int credits;
            try {
                credits = Integer.parseInt(creditsField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Credits must be a number.");
                return;
            }

            String sql = "UPDATE Modules SET name = ?, course_id = ?, lecturer_id = ?, schedule = ?, credits = ? WHERE id = ?";
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, name);
                ps.setInt(2, courseId);
                ps.setString(3, lecturerId);
                ps.setString(4, schedule);
                ps.setInt(5, credits);
                ps.setInt(6, moduleId);
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "Module updated successfully!");
                loadModules();
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error updating module: " + e.getMessage());
            }
        }
    }

    private void deleteModule() {
        int row = moduleTable.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a module to delete.");
            return;
        }

        int moduleId = (int) tableModel.getValueAt(row, 0);
        String moduleName = (String) tableModel.getValueAt(row, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete module: " + moduleName + "?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            Connection conn = DBConnection.getInstance().getConnection();
            // Delete related results first
            try (PreparedStatement ps = conn.prepareStatement("DELETE FROM Results WHERE module_id = ?")) {
                ps.setInt(1, moduleId);
                ps.executeUpdate();
            } catch (SQLException e) {
                // Results table may not have entries for this module
            }

            String sql = "DELETE FROM Modules WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, moduleId);
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(this, "Module deleted successfully!");
                    loadModules();
                } else {
                    JOptionPane.showMessageDialog(this, "Module not found.");
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error deleting module: " + e.getMessage());
            }
        }
    }

    private void loadCourses(JComboBox<String> box) {
        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name FROM Courses ORDER BY name")) {
            while (rs.next()) {
                box.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadLecturers(JComboBox<String> box) {
        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT Lecturer_Id, First_Name, Last_Name FROM Lecturer ORDER BY Last_Name")) {
            while (rs.next()) {
                box.addItem(rs.getString("Lecturer_Id") + " - " + rs.getString("First_Name") + " " + rs.getString("Last_Name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void selectComboItem(JComboBox<String> box, String search) {
        if (search == null) return;
        for (int i = 0; i < box.getItemCount(); i++) {
            if (box.getItemAt(i).contains(search)) {
                box.setSelectedIndex(i);
                return;
            }
        }
    }

    private int extractId(String item) {
        return Integer.parseInt(item.split(" - ")[0].trim());
    }

    private String extractStringId(String item) {
        return item.split(" - ")[0].trim();
    }
}
