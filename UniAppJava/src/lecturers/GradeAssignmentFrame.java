package lecturers;

import java.awt.*;
import java.sql.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import results.ResultDAO;

public class GradeAssignmentFrame extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private ResultDAO dao;
    private String lecturerId;
    private Connection conn;

    public GradeAssignmentFrame(String lecturerId, Connection conn) {
        this.lecturerId = lecturerId;
        this.conn = conn;
        this.dao = new ResultDAO();
        
        if (this.conn == null) {
            JOptionPane.showMessageDialog(this, 
                "Database connection error. Please restart the application.", 
                "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        initializeUI();
        loadData();
    }

    private void initializeUI() {
        setTitle("Grade Assignment - Lecturer ID: " + lecturerId);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(new String[]{"Result ID", "Student ID", "Student Name", "Module ID", "Module Name", "Marks", "Grade"}, 0);
        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowHeight(25);

        add(new JScrollPane(table), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton addBtn = new JButton("Add Grade");
        JButton editBtn = new JButton("Edit Grade");
        JButton deleteBtn = new JButton("Delete Result");
        JButton refreshBtn = new JButton("Refresh");
        
        buttonPanel.add(addBtn);
        buttonPanel.add(editBtn);
        buttonPanel.add(deleteBtn);
        buttonPanel.add(refreshBtn);
        add(buttonPanel, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> addResultDialog());
        editBtn.addActionListener(e -> editResultDialog());
        deleteBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                int resultId = (int) model.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(this, 
                    "Are you sure you want to delete this result?", 
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        dao.deleteResult(resultId);
                        loadData();
                        JOptionPane.showMessageDialog(this, "Result deleted successfully!");
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error deleting result: " + ex.getMessage());
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a result to delete.");
            }
        });
        
        refreshBtn.addActionListener(e -> loadData());
    }

    // Method to calculate grade based on marks
    private String calculateGrade(int marks) {
        if (marks >= 80 && marks <= 100) {
            return "A";
        } else if (marks >= 70 && marks <= 79) {
            return "B";
        } else if (marks >= 60 && marks <= 69) {
            return "C";
        } else if (marks >= 50 && marks <= 59) {
            return "D";
        } else if (marks >= 40 && marks <= 49) {
            return "E";
        } else {
            return "F";
        }
    }
    
    // Method to verify if module belongs to the lecturer
    private boolean isModuleAssignedToLecturer(int moduleId, String lecturerId) {
        String query = "SELECT COUNT(*) FROM Modules WHERE id = ? AND lecturer_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, moduleId);
            pstmt.setString(2, lecturerId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Method to get module name for verification feedback
    private String getModuleName(int moduleId) {
        String query = "SELECT name FROM Modules WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, moduleId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadData() {
        try {
            // Clear existing data
            model.setRowCount(0);
            
            // Get results for this lecturer using DAO
            List<results.Result> resultList = dao.getResultsByLecturer(lecturerId);
            
            int rowCount = 0;
            for (results.Result result : resultList) {
                model.addRow(new Object[]{
                    result.getResultId(),
                    result.getStudentId(),
                    result.getStudentName(),
                    result.getModuleId(),
                    result.getModuleName(),
                    result.getMarks(),
                    result.getGrade()
                });
                rowCount++;
            }
            
            System.out.println("Loaded " + rowCount + " results for lecturer: " + lecturerId);
            
            if (rowCount == 0) {
                JOptionPane.showMessageDialog(this, 
                    "No results found in the database.\nUse 'Add Grade' to enter results.", 
                    "Information", JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error loading data: " + e.getMessage(), 
                "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addResultDialog() {
        JTextField studentIdField = new JTextField();
        JTextField moduleIdField = new JTextField();
        JTextField marksField = new JTextField();
        
        // Grade field will be auto-calculated and read-only
        JTextField gradeField = new JTextField();
        gradeField.setEditable(false);
        gradeField.setBackground(Color.LIGHT_GRAY);

        // Add a listener to auto-calculate grade when marks are entered
        marksField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            private void updateGrade() {
                try {
                    String marksText = marksField.getText().trim();
                    if (!marksText.isEmpty()) {
                        int marks = Integer.parseInt(marksText);
                        if (marks >= 0 && marks <= 100) {
                            String grade = calculateGrade(marks);
                            gradeField.setText(grade);
                        } else {
                            gradeField.setText("Invalid marks");
                        }
                    } else {
                        gradeField.setText("");
                    }
                } catch (NumberFormatException e) {
                    gradeField.setText("Invalid input");
                }
            }
            
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { updateGrade(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { updateGrade(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { updateGrade(); }
        });

        Object[] message = {
            "Student ID:", studentIdField,
            "Module ID:", moduleIdField,
            "Marks (0-100):", marksField,
            "Grade (Auto-calculated):", gradeField
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Add Grade", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            try {
                int studentId = Integer.parseInt(studentIdField.getText().trim());
                int moduleId = Integer.parseInt(moduleIdField.getText().trim());
                int marks = Integer.parseInt(marksField.getText().trim());
                
                // Auto-calculate grade based on marks
                String grade = calculateGrade(marks);

                if (marks < 0 || marks > 100) {
                    JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                
                // Verify if module belongs to the lecturer
                if (!isModuleAssignedToLecturer(moduleId, lecturerId)) {
                    String moduleName = getModuleName(moduleId);
                    if (moduleName == null) {
                        JOptionPane.showMessageDialog(this, 
                            "Module ID " + moduleId + " does not exist in the system.", 
                            "Module Verification Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, 
                            "Wrong module ID entered!\n\nModule ID " + moduleId + " (" + moduleName + ") is not assigned to you.\n" +
                            "You can only add grades for modules that are assigned to you.", 
                            "Module Verification Error", JOptionPane.ERROR_MESSAGE);
                    }
                    return;
                }

                // Check if student exists and is enrolled in the module
                if (!dao.studentExists(studentId, lecturerId)) {
                    JOptionPane.showMessageDialog(this, "Student with ID " + studentId + " is not enrolled in your modules.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // Insert the result with auto-calculated grade
                int result = dao.addResult(studentId, moduleId, marks, grade);
                
                if (result > 0) {
                    loadData();
                    String moduleName = getModuleName(moduleId);
                    JOptionPane.showMessageDialog(this, 
                        "Grade added successfully!\n\n" +
                        "Student ID: " + studentId + "\n" +
                        "Module: " + moduleId + " - " + (moduleName != null ? moduleName : "Unknown") + "\n" +
                        "Marks: " + marks + "\n" +
                        "Grade: " + grade, 
                        "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add grade.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException nfe) {
                JOptionPane.showMessageDialog(this, "Please enter valid numeric values.", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void editResultDialog() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            int resultId = (int) model.getValueAt(row, 0);
            int currentMarks = (int) model.getValueAt(row, 5);
            int moduleId = (int) model.getValueAt(row, 3);
            String currentGrade = (String) model.getValueAt(row, 6);
            
            JTextField marksField = new JTextField(String.valueOf(currentMarks));
            
            // Grade field will be auto-calculated and read-only
            JTextField gradeField = new JTextField(currentGrade);
            gradeField.setEditable(false);
            gradeField.setBackground(Color.LIGHT_GRAY);

            // Add a listener to auto-calculate grade when marks are edited
            marksField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
                private void updateGrade() {
                    try {
                        String marksText = marksField.getText().trim();
                        if (!marksText.isEmpty()) {
                            int marks = Integer.parseInt(marksText);
                            if (marks >= 0 && marks <= 100) {
                                String newGrade = calculateGrade(marks);
                                gradeField.setText(newGrade);
                            } else {
                                gradeField.setText("Invalid marks");
                            }
                        } else {
                            gradeField.setText("");
                        }
                    } catch (NumberFormatException e) {
                        gradeField.setText("Invalid input");
                    }
                }
                
                @Override
                public void insertUpdate(javax.swing.event.DocumentEvent e) { updateGrade(); }
                @Override
                public void removeUpdate(javax.swing.event.DocumentEvent e) { updateGrade(); }
                @Override
                public void changedUpdate(javax.swing.event.DocumentEvent e) { updateGrade(); }
            });
            
            Object[] message = {
                "Module ID: " + moduleId + " (Cannot be changed)", 
                "Marks (0-100):", marksField,
                "Grade (Auto-calculated):", gradeField
            };
            
            int option = JOptionPane.showConfirmDialog(this, message, "Edit Grade", JOptionPane.OK_CANCEL_OPTION);
            
            if (option == JOptionPane.OK_OPTION) {
                try {
                    int marks = Integer.parseInt(marksField.getText().trim());
                    
                    // Auto-calculate grade based on marks
                    String grade = calculateGrade(marks);
                    
                    if (marks < 0 || marks > 100) {
                        JOptionPane.showMessageDialog(this, "Marks must be between 0 and 100.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    
                    boolean updated = dao.updateResult(resultId, marks, grade);
                    if (updated) {
                        loadData();
                        String moduleName = getModuleName(moduleId);
                        JOptionPane.showMessageDialog(this, 
                            "Grade updated successfully!\n\n" +
                            "Module: " + moduleId + " - " + (moduleName != null ? moduleName : "Unknown") + "\n" +
                            "Marks: " + marks + "\n" +
                            "Grade: " + grade, 
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(this, "Failed to update grade.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(this, "Please enter a valid number for marks.", "Input Error", JOptionPane.ERROR_MESSAGE);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error updating grade: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a result to edit.");
        }
    }
}