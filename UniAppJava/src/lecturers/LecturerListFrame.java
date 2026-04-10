package lecturers;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class LecturerListFrame extends JFrame {
    private JTable lecturerTable;
    private DefaultTableModel tableModel;
    private LecturerDAO lecturerDAO;

    public LecturerListFrame() {
        setTitle("Lecturer Management");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        lecturerDAO = new LecturerDAO();

        String[] columnNames = { "Lecturer ID", "First Name", "Last Name", "Title", "Email", "Phone", "Department",
                "Specialisation", "Qualification", "Employment Type" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        lecturerTable = new JTable(tableModel);
        lecturerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        loadLecturers();

        // Buttons
        JButton addButton = new JButton("Add Lecturer");
        JButton editButton = new JButton("Edit Lecturer");
        JButton deleteButton = new JButton("Delete Lecturer");
        JButton refreshButton = new JButton("Refresh");

        addButton.addActionListener(e -> {
            AddLecturerFrame addFrame = new AddLecturerFrame(this);
            addFrame.setVisible(true);
        });

        editButton.addActionListener(e -> {
            int selectedRow = lecturerTable.getSelectedRow();
            if (selectedRow != -1) {
                String lecturerId = (String) tableModel.getValueAt(selectedRow, 0);
                String firstName = (String) tableModel.getValueAt(selectedRow, 1);
                String lastName = (String) tableModel.getValueAt(selectedRow, 2);
                String title = (String) tableModel.getValueAt(selectedRow, 3);
                String email = (String) tableModel.getValueAt(selectedRow, 4);
                String phone = (String) tableModel.getValueAt(selectedRow, 5);
                String department = (String) tableModel.getValueAt(selectedRow, 6);
                String specialisation = (String) tableModel.getValueAt(selectedRow, 7);
                String qualification = (String) tableModel.getValueAt(selectedRow, 8);
                String employmentType = (String) tableModel.getValueAt(selectedRow, 9);

                Lecturer lecturer = new Lecturer(lecturerId, firstName, lastName, title, email, phone, department,
                        specialisation, qualification, employmentType);
                EditLecturerFrame editFrame = new EditLecturerFrame(this, lecturer);
                editFrame.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a lecturer to edit.");
            }
        });

        deleteButton.addActionListener(e -> {
            int selectedRow = lecturerTable.getSelectedRow();
            if (selectedRow != -1) {
                String lecturerId = (String) tableModel.getValueAt(selectedRow, 0);
                String firstName = (String) tableModel.getValueAt(selectedRow, 1);
                String lastName = (String) tableModel.getValueAt(selectedRow, 2);
                String lecturerName = firstName + " " + lastName;

                DeleteLecturerDialog deleteDialog = new DeleteLecturerDialog(this, lecturerId, lecturerName);
                deleteDialog.setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Please select a lecturer to delete.");
            }
        });

        refreshButton.addActionListener(e -> loadLecturers());

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);

        setLayout(new BorderLayout());
        add(new JScrollPane(lecturerTable), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    public void loadLecturers() {
        tableModel.setRowCount(0);
        List<Lecturer> lecturers = lecturerDAO.listLecturers();
        for (Lecturer l : lecturers) {
            Object[] row = {
                    l.getLecturerId(),
                    l.getFirstName(),
                    l.getLastName(),
                    l.getTitle(),
                    l.getEmail(),
                    l.getPhone(),
                    l.getDepartment(),
                    l.getSpecialisation(),
                    l.getQualification(),
                    l.getEmploymentType()
            };
            tableModel.addRow(row);
        }

        // Auto-resize columns
        lecturerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
}