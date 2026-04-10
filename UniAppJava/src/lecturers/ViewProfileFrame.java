package lecturers;

import db.DBConnection;
import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class ViewProfileFrame extends JFrame {
    private Lecturer lecturer;

    private JTextField idField, titleField, firstNameField, lastNameField, emailField, phoneField, departmentField, specialisationField, qualificationField, employmentTypeField;

    private Connection conn;

    public ViewProfileFrame(String lecturerId) {
        this.conn = DBConnection.getInstance().getConnection();
        lecturer = loadLecturer(lecturerId);

        if (lecturer == null) {
            JOptionPane.showMessageDialog(this, "No lecturer found for ID: " + lecturerId);
            dispose();
            return;
        }

        setTitle("Lecturer Profile - " + lecturer.getFirstName() + " " + lecturer.getLastName());
        setSize(500, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Main panel
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        // Labels and fields
        idField = addField(panel, gbc, 0, "Lecturer ID:", lecturer.getLecturerId(), false);
        titleField = addField(panel, gbc, 1, "Title:", lecturer.getTitle(), true);
        firstNameField = addField(panel, gbc, 2, "Name:", lecturer.getFirstName(), true);
        lastNameField = addField(panel, gbc, 3, "Surname:", lecturer.getLastName(), true);
        emailField = addField(panel, gbc, 4, "Email:", lecturer.getEmail(), true);
        phoneField = addField(panel, gbc, 5, "Phone:", lecturer.getPhone(), true);
        departmentField = addField(panel, gbc, 6, "Department:", lecturer.getDepartment(), true);
        specialisationField = addField(panel, gbc, 7, "Specialisation:", lecturer.getSpecialisation(), true);
        qualificationField = addField(panel, gbc, 8, "Qualification:", lecturer.getQualification(), true);
        employmentTypeField = addField(panel, gbc, 9, "Employment Type:", lecturer.getEmploymentType(), true);

        // Buttons
        JPanel buttonPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton closeBtn = new JButton("Close");
        saveBtn.addActionListener(e -> saveChanges());
        closeBtn.addActionListener(e -> dispose());
        buttonPanel.add(saveBtn);
        buttonPanel.add(closeBtn);

        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);

        add(panel);

        // Show the frame
        setVisible(true);
    }

    private JTextField addField(JPanel panel, GridBagConstraints gbc, int y, String label, String value, boolean editable) {
        gbc.gridx = 0;
        gbc.gridy = y;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        JTextField field = new JTextField(25);
        field.setText(value);
        field.setEditable(editable);
        panel.add(field, gbc);
        return field;
    }

    private Lecturer loadLecturer(String lecturerId) {
        try {
            String sql = "SELECT * FROM Lecturer WHERE Lecturer_Id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Lecturer(
                    rs.getString("Lecturer_Id"),
                    rs.getString("Title"),
                    rs.getString("First_Name"),
                    rs.getString("Last_Name"),
                    rs.getString("Email"),
                    rs.getString("Phone"),
                    rs.getString("Department"),
                    rs.getString("Specialisation"),
                    rs.getString("Qualification"),
                    rs.getString("Employment_Type")
                );
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading lecturer: " + e.getMessage());
        }
        return null;
    }

    private void saveChanges() {
        try {
            String sql = "UPDATE Lecturer SET Title=?, First_Name=?, Last_Name=?, Email=?, Phone=?, " +
                         "Department=?, Specialisation=?, Qualification=?, Employment_Type=? WHERE Lecturer_Id=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, titleField.getText());
            ps.setString(2, firstNameField.getText());
            ps.setString(3, lastNameField.getText());
            ps.setString(4, emailField.getText());
            ps.setString(5, phoneField.getText());
            ps.setString(6, departmentField.getText());
            ps.setString(7, specialisationField.getText());
            ps.setString(8, qualificationField.getText());
            ps.setString(9, employmentTypeField.getText());
            ps.setString(10, idField.getText());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Profile updated successfully!");
            } else {
                JOptionPane.showMessageDialog(this, "No changes were made.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error saving changes: " + e.getMessage());
        }
    }
}





