package lecturers;

import java.awt.*;
import javax.swing.*;

public class AddLecturerFrame extends JFrame {
    private JTextField lecturerIdField, firstNameField, lastNameField, titleField, emailField, phoneField,
            departmentField, specialisationField, qualificationField, employmentTypeField;
    private LecturerDAO lecturerDAO;
    private LecturerListFrame parentFrame;

    public AddLecturerFrame(LecturerListFrame parent) {
        this.parentFrame = parent;
        lecturerDAO = new LecturerDAO();

        setTitle("Add Lecturer");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(11, 2, 5, 5));

        panel.add(new JLabel("Lecturer ID:"));
        lecturerIdField = new JTextField();
        panel.add(lecturerIdField);

        panel.add(new JLabel("First Name:"));
        firstNameField = new JTextField();
        panel.add(firstNameField);

        panel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField();
        panel.add(lastNameField);

        panel.add(new JLabel("Title:"));
        titleField = new JTextField();
        panel.add(titleField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Department:"));
        departmentField = new JTextField();
        panel.add(departmentField);

        panel.add(new JLabel("Specialisation:"));
        specialisationField = new JTextField();
        panel.add(specialisationField);

        panel.add(new JLabel("Qualification:"));
        qualificationField = new JTextField();
        panel.add(qualificationField);

        panel.add(new JLabel("Employment Type:"));
        employmentTypeField = new JTextField();
        panel.add(employmentTypeField);

        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> saveLecturer());
        cancelButton.addActionListener(e -> dispose());

        panel.add(saveButton);
        panel.add(cancelButton);

        add(panel);
    }

    private void saveLecturer() {
        try {
            Lecturer lecturer = new Lecturer(
                    lecturerIdField.getText(),
                    firstNameField.getText(),
                    lastNameField.getText(),
                    titleField.getText(),
                    emailField.getText(),
                    phoneField.getText(),
                    departmentField.getText(),
                    specialisationField.getText(),
                    qualificationField.getText(),
                    employmentTypeField.getText());
            lecturerDAO.addLecturer(lecturer);
            JOptionPane.showMessageDialog(this, "Lecturer added successfully!");
            parentFrame.loadLecturers();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error adding lecturer: " + ex.getMessage());
        }
    }
}