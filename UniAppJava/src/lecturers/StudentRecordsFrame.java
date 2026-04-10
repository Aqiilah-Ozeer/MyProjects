package lecturers;

import students.StudentDAO;
import models.Student;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class StudentRecordsFrame extends JFrame {

    private JTable studentTable;
    private DefaultTableModel tableModel;
    private StudentDAO studentDAO;
    private String lecturerId;

    public StudentRecordsFrame(String lecturerId) {
        this.lecturerId = lecturerId;
        this.studentDAO = new StudentDAO();

        setTitle("Student Records - Lecturer: " + lecturerId);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Table setup
        String[] columns = {"Student ID", "First Name", "Last Name", "Email", "Phone", "Date of Birth", "Address", "Module"};
        tableModel = new DefaultTableModel(columns, 0);
        studentTable = new JTable(tableModel);
        studentTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Button panel - Only Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton closeBtn = new JButton("Close");
        closeBtn.setFont(new Font("SansSerif", Font.BOLD, 14));
        closeBtn.addActionListener(e -> dispose());
        
        buttonPanel.add(closeBtn);

        // Layout
        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Load students
        loadStudents();
        
        setVisible(true);
    }
    
    private void loadStudents() {
    // Clear existing rows
    tableModel.setRowCount(0);
    
    // Use SwingWorker to load data in background
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
        private ArrayList<Student> students = null;
        private Exception error = null;
        
        @Override
        protected Void doInBackground() {
            try {
                System.out.println("=== Calling getStudentsByLecturer for: " + lecturerId);
                students = studentDAO.getStudentsByLecturer(lecturerId);
                System.out.println("=== Got " + students.size() + " students from DAO");
            } catch (SQLException ex) {
                error = ex;
                ex.printStackTrace();
            }
            return null;
        }
        
        @Override
        protected void done() {
            if (error != null) {
                JOptionPane.showMessageDialog(StudentRecordsFrame.this,
                    "Error loading students: " + error.getMessage(),
                    "Database Error",
                    JOptionPane.ERROR_MESSAGE);
            } else if (students != null) {
                System.out.println("=== Adding " + students.size() + " students to table");
                for (Student s : students) {
                    Object[] row = {
                        s.getStudentId(),
                        s.getFirstName(),
                        s.getLastName(),
                        s.getEmail(),
                        s.getPhone(),
                        s.getDateOfBirth(),
                        s.getAddress(),
                        s.getModuleName()
                    };
                    tableModel.addRow(row);
                }
                System.out.println("=== Table now has " + tableModel.getRowCount() + " rows");
                
                if (students.isEmpty()) {
                    JOptionPane.showMessageDialog(StudentRecordsFrame.this,
                        "No students found for your modules.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    };
    
    worker.execute();
}
}