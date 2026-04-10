package lecturers;

import javax.swing.*;
import java.awt.*;

public class EditLecturerFrame extends JFrame {
    private JTextField lecturerIdField, firstNameField, lastNameField, titleField, emailField, phoneField,
            departmentField, specialisationField, qualificationField, employmentTypeField;
    private LecturerDAO lecturerDAO;
    private LecturerListFrame parentFrame;
    private String originalLecturerId;

    public EditLecturerFrame(LecturerListFrame parent, Lecturer lecturer) {
        this.parentFrame = parent;
        this.originalLecturerId = lecturer.getLecturerId();
        lecturerDAO = new LecturerDAO();

        setTitle("Edit Lecturer");
        setSize(400, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(11, 2, 5, 5));

        panel.add(new JLabel("Lecturer ID:"));
        lecturerIdField = new JTextField(lecturer.getLecturerId());
        lecturerIdField.setEditable(false);
        panel.add(lecturerIdField);

        panel.add(new JLabel("First Name:"));
        firstNameField = new JTextField(lecturer.getFirstName());
        panel.add(firstNameField);

        panel.add(new JLabel("Last Name:"));
        lastNameField = new JTextField(lecturer.getLastName());
        panel.add(lastNameField);

        panel.add(new JLabel("Title:"));
        titleField = new JTextField(lecturer.getTitle());
        panel.add(titleField);

        panel.add(new JLabel("Email:"));
        emailField = new JTextField(lecturer.getEmail());
        panel.add(emailField);

        panel.add(new JLabel("Phone:"));
        phoneField = new JTextField(lecturer.getPhone());
        panel.add(phoneField);

        panel.add(new JLabel("Department:"));
        departmentField = new JTextField(lecturer.getDepartment());
        panel.add(departmentField);

        panel.add(new JLabel("Specialisation:"));
        specialisationField = new JTextField(lecturer.getSpecialisation());
        panel.add(specialisationField);

        panel.add(new JLabel("Qualification:"));
        qualificationField = new JTextField(lecturer.getQualification());
        panel.add(qualificationField);

        panel.add(new JLabel("Employment Type:"));
        employmentTypeField = new JTextField(lecturer.getEmploymentType());
        panel.add(employmentTypeField);

        JButton updateButton = new JButton("Update");
        JButton cancelButton = new JButton("Cancel");

        updateButton.addActionListener(e -> updateLecturer());
        cancelButton.addActionListener(e -> dispose());

        panel.add(updateButton);
        panel.add(cancelButton);

        add(panel);
    }

    private void updateLecturer() {
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
            lecturerDAO.updateLecturer(lecturer);
            JOptionPane.showMessageDialog(this, "Lecturer updated successfully!");
            parentFrame.loadLecturers();
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error updating lecturer: " + ex.getMessage());
        }
    }
}