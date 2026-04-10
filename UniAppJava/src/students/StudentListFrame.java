package students;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import models.Student;

public class StudentListFrame extends JFrame {
    JTable studentTable;
    JButton btnView, btnAdd, btnEdit, btnDelete;
    StudentDAO dao;

    public StudentListFrame() {
        setTitle("Student Management");
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        dao = new StudentDAO();

        // Column headers
        String[] columnNames = { "Student ID", "First Name", "Last Name", "Email", 
        "Phone", "Date of Birth", "Address" };

        // Build table model
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        studentTable = new JTable(model);
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Load data immediately
        refreshTable();

        btnAdd = new JButton("Add");
        btnEdit = new JButton("Edit");
        btnDelete = new JButton("Delete");
        btnView = new JButton("View");

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(btnView);
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);

        btnView.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    Student studentAtId = dao.getStudentById((int) studentTable.getValueAt(selectedRow, 0));
                    StudentDetailForm detailForm = new StudentDetailForm(StudentListFrame.this, studentAtId, "VIEW");
                    detailForm.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(StudentListFrame.this, "Please select a student to view.", "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnAdd.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StudentDetailForm detailForm = new StudentDetailForm(StudentListFrame.this,null, "ADD");
                detailForm.setVisible(true);
                if(detailForm.isSaved()) {
                    refreshTable();
                }
            }
        });

        btnEdit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    Student studentAtId = dao.getStudentById((int) studentTable.getValueAt(selectedRow, 0));
                    StudentDetailForm detailForm = new StudentDetailForm(StudentListFrame.this, studentAtId, "EDIT");
                    detailForm.setVisible(true);
                    if(detailForm.isSaved()) {
                        refreshTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(StudentListFrame.this, "Please select a student to edit.", "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        btnDelete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = studentTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirm = JOptionPane.showConfirmDialog(StudentListFrame.this, "Are you sure you want to delete this student?", 
                    "Confirm Delete", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                    if (confirm == JOptionPane.YES_OPTION) {
                        dao.deleteStudent((int) studentTable.getValueAt(selectedRow, 0));
                        refreshTable();
                    }
                } else {
                    JOptionPane.showMessageDialog(StudentListFrame.this, "Please select a student to delete.", 
                    "No Selection", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        add(new JScrollPane(studentTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setSize(900, 500);
        setLocationRelativeTo(null);
    }

    private void refreshTable() {
        DefaultTableModel model = (DefaultTableModel) studentTable.getModel();
        model.setRowCount(0); // clear existing rows
        for (Student s : dao.getAllStudents()) {
            model.addRow(new Object[] {
                    s.getStudentId(),
                    s.getFirstName(),
                    s.getLastName(),
                    s.getEmail(),
                    s.getPhone(),
                    s.getDateOfBirth(),
                    s.getAddress()
            });
        }
    }
}  