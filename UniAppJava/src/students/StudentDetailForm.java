package students;

import java.util.Calendar;
import java.util.Date;
import javax.swing.*;
import java.awt.event.*;
import models.Student;
import java.awt.*;

public class StudentDetailForm extends JDialog {
    Student student;
    String mode;
    boolean saved;
    JTextField txtStudentId, txtFirstName, txtLastName, txtEmail, txtPhone, txtPassword;
    JTextArea txtAddress;
    JSpinner spnDateOfBirth;
    JButton btnSave, btnCancel, btnClose;
    StudentDAO dao;

    public StudentDetailForm(JFrame owner, Student student, String mode) {
        super(owner, true);
        this.student = student;
        this.mode = mode;
        this.saved = false;
        dao = new StudentDAO();

        setTitle(mode + " Student");
        setSize(450, 550);
        setLocationRelativeTo(owner);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel();
        // Main panel with BoxLayout (vertical stacking) best layout for form style i want
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        // Added padding around the edges like in webpage
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        txtStudentId = new JTextField(student != null ? String.valueOf(student.getStudentId()) : "", 20);
        txtFirstName = new JTextField(student != null ? student.getFirstName() : "", 20);
        txtLastName = new JTextField(student != null ? student.getLastName() : "", 20);
        txtEmail = new JTextField(student != null ? student.getEmail() : "", 20);
        txtPhone = new JTextField(student != null ? student.getPhone() : "", 20);

        Date today = new Date();
        spnDateOfBirth = new JSpinner(new SpinnerDateModel(student != null ? student.getDateOfBirth() : today, null,
        today, Calendar.DAY_OF_MONTH));
        spnDateOfBirth.setEditor(new JSpinner.DateEditor(spnDateOfBirth, "yyyy-MM-dd"));

        txtAddress = new JTextArea(student != null ? student.getAddress() : "", 4, 20);
        txtAddress.setLineWrap(true);
        JScrollPane addressScrollPane = new JScrollPane(txtAddress);

        // Creating and adding each row as a panel with FlowLayout (LEFT aligned) for positioning
        mainPanel.add(createRow("Student ID:", txtStudentId));
        mainPanel.add(createRow("First Name:", txtFirstName));
        mainPanel.add(createRow("Last Name:", txtLastName));
        mainPanel.add(createRow("Email:", txtEmail));
        mainPanel.add(createRow("Phone:", txtPhone));
        mainPanel.add(createRow("Date of Birth:", spnDateOfBirth));
        mainPanel.add(createRow("Address:", addressScrollPane));

        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        btnClose = new JButton("Close");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        if (mode.equals("VIEW")) {
            setEditable(false);
            mainPanel.add(Box.createVerticalStrut(20));
            buttonPanel.add(btnClose);
        } else if (mode.equals("PROFILE")) {
            txtStudentId.setEditable(false);
            txtPassword = new JTextField(student != null ? dao.getStudentAccPwd(
                String.valueOf(student.getStudentId())) : "", 20);
            mainPanel.add(createRow("Password:", txtPassword));
            mainPanel.add(Box.createVerticalStrut(20));
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);
        } else if (mode.equals("EDIT")) {
            txtStudentId.setEditable(false);
            mainPanel.add(Box.createVerticalStrut(20));
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);
        } else {
            mainPanel.add(Box.createVerticalStrut(20));
            buttonPanel.add(btnSave);
            buttonPanel.add(btnCancel);
        }

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveStudent();
            }
        });

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

        btnClose.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        mainPanel.add(buttonPanel);
        add(mainPanel);
    }

    // method to quickly create a labeled row
    private JPanel createRow(String labelText, JComponent component) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JLabel label = new JLabel(labelText);
        label.setPreferredSize(new Dimension(100, 25));
        row.add(label);
        row.add(component);
        return row;
    }

    private void setEditable(boolean editable) {
        txtStudentId.setEditable(editable);
        txtFirstName.setEditable(editable);
        txtLastName.setEditable(editable);
        txtEmail.setEditable(editable);
        txtPhone.setEditable(editable);
        spnDateOfBirth.setEnabled(editable);
        txtAddress.setEditable(editable);
    }

    private boolean validateInput() {
        if (txtStudentId.getText().trim().isEmpty() || !txtStudentId.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Valid Student ID is required.");
            return false;
        } else {
            int studentId = Integer.parseInt(txtStudentId.getText().trim());
            // check against existing student IDs when add
            if (mode.equals("ADD") && dao.getStudentById(studentId) != null) {
                JOptionPane.showMessageDialog(this, "Student ID already exists. Please enter a different ID.", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        if (txtFirstName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name is required.");
            return false;
        }
        if (txtLastName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last Name is required.");
            return false;
        }
        if (txtEmail.getText().trim().isEmpty() || !txtEmail.getText().matches("^[\\w.-]+@umail.com$")) {
            JOptionPane.showMessageDialog(this, "Email is required and must be a valid email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (txtPhone.getText().trim().isEmpty() || !txtPhone.getText().matches("^5\\d{3} \\d{4}$")) {
            JOptionPane.showMessageDialog(this, "Phone is required and must be a valid phone number.", 
            "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        Date dob = (Date) spnDateOfBirth.getValue();
        Date today = new Date();
        if (dob.after(today)) {
            JOptionPane.showMessageDialog(this, "Date of Birth cannot be in the future.", 
            "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        } else {
            // Check if the student is at least 16 years old
            Calendar cal = Calendar.getInstance();
            cal.setTime(today);
            cal.add(Calendar.YEAR, -16);
            Date sixteenYearsAgo = cal.getTime();
            if (dob.after(sixteenYearsAgo)) {
                JOptionPane.showMessageDialog(this, "Student must be at least 16 years old.", 
                "Validation Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        if (txtAddress.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Address is required.", 
            "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (mode.equals("PROFILE") && (txtPassword.getText().trim().isEmpty() || txtPassword.getText().length() < 6)) {
            JOptionPane.showMessageDialog(this, "Password is required and must be at least 6 characters long.", 
            "Validation Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private void saveStudent() {
        if (!validateInput()){
            return;
        }
            
        int studentId = Integer.parseInt(txtStudentId.getText().trim());
        String firstName = txtFirstName.getText().trim();
        String lastName = txtLastName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        java.sql.Date dateOfBirth = new java.sql.Date(((Date) spnDateOfBirth.getValue()).getTime());
        String address = txtAddress.getText().trim();

        Student newStudent = new Student(studentId, firstName, lastName, email, phone, dateOfBirth, address);

        if (mode.equals("ADD")) {
            dao.addStudent(newStudent);
        } else if (mode.equals("EDIT")) {
            dao.updateStudent(newStudent);
        } else if (mode.equals("PROFILE")) {
            dao.updateStudent(newStudent);
            if (!dao.getStudentAccPwd(String.valueOf(student.getStudentId())).equals(txtPassword.getText().trim())) {
                int confirm = JOptionPane.showConfirmDialog(StudentDetailForm.this, "Are you sure you want to change the password?", 
                    "Confirm Password Change", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    dao.updateStudentAccPwd(txtStudentId.getText().trim(), txtPassword.getText().trim());
                } 
            }
        }
        saved = true;
        dispose();
    }

    private void onCancel() {
        saved = false;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
}
