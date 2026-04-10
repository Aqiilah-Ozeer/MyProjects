package finance;

import models.Payment;
import finance.PaymentDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class AdminFormFrame extends JFrame {
    private PaymentHistoryPanel parentPanel;
    private Payment existingPayment; // null for ADD mode, not null for EDIT mode
    
    private JTextField txtStudentId, txtAmount, txtCardNumber;
    private JComboBox<String> cmbMethod;
    private JComboBox<String> cmbCourse;
    private JComboBox<Integer> cmbYear;
    private JButton btnSave, btnCancel;

    // Course options from your courses table
    private String[] courseOptions = {
        "Transfiguration",
        "Charms", 
        "Potions",
        "History of Magic",
        "Defence Against the Dark Arts",
        "Astronomy",
        "Herbology"
    };

    public AdminFormFrame(PaymentHistoryPanel parentPanel, Payment payment) {
        this.parentPanel = parentPanel;
        this.existingPayment = payment;
        setupUI();
        
        if (payment != null) {
            setTitle("Edit Payment");
            loadPaymentData();
        } else {
            setTitle("Add Payment");
        }
        
        setSize(400, 480);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Student ID
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        txtStudentId = new JTextField(15);
        add(txtStudentId, gbc);

        // Row 1: Amount
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Amount ($):"), gbc);
        gbc.gridx = 1;
        txtAmount = new JTextField(15);
        add(txtAmount, gbc);

        // Row 2: Payment Method
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        cmbMethod = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "Bank Transfer", "Cash"});
        add(cmbMethod, gbc);

        // Row 3: Card Number
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        txtCardNumber = new JTextField(15);
        add(txtCardNumber, gbc);

        // Row 4: Course (Dropdown)
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        cmbCourse = new JComboBox<>(courseOptions);
        cmbCourse.setEditable(false);
        add(cmbCourse, gbc);

        // Row 5: Year (Dropdown 1-4)
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        cmbYear = new JComboBox<>();
        for (int i = 1; i <= 4; i++) {
            cmbYear.addItem(i);
        }
        add(cmbYear, gbc);

        // Row 6: Buttons
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, gbc);

        // Button Actions
        btnSave.addActionListener(e -> savePayment());
        btnCancel.addActionListener(e -> dispose());
    }

    private void loadPaymentData() {
        if (existingPayment != null) {
            txtStudentId.setText(String.valueOf(existingPayment.getStudentId()));
            txtAmount.setText(String.valueOf(existingPayment.getAmount()));
            cmbMethod.setSelectedItem(existingPayment.getMethod());
            txtCardNumber.setText(existingPayment.getCardNumber());
            
            // Set course if it matches one in the dropdown
            String paymentCourse = existingPayment.getCourse();
            boolean courseFound = false;
            for (int i = 0; i < courseOptions.length; i++) {
                if (courseOptions[i].equals(paymentCourse)) {
                    cmbCourse.setSelectedIndex(i);
                    courseFound = true;
                    break;
                }
            }
            if (!courseFound && paymentCourse != null && !paymentCourse.isEmpty()) {
                cmbCourse.addItem(paymentCourse);
                cmbCourse.setSelectedItem(paymentCourse);
            }
            
            cmbYear.setSelectedItem(existingPayment.getYear());
        }
    }

    private void savePayment() {
        try {
            // Validate inputs
            if (txtStudentId.getText().trim().isEmpty() ||
                txtAmount.getText().trim().isEmpty() ||
                txtCardNumber.getText().trim().isEmpty()) {
                
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            int studentId = Integer.parseInt(txtStudentId.getText().trim());
            double amount = Double.parseDouble(txtAmount.getText().trim());
            String method = (String) cmbMethod.getSelectedItem();
            String cardNumber = txtCardNumber.getText().trim();
            String course = (String) cmbCourse.getSelectedItem();
            int year = (Integer) cmbYear.getSelectedItem();
            
            // Validate amount is positive
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0!");
                return;
            }
            
            // Validate card number (must be at least 12 digits and numeric)
            if (!cardNumber.matches("\\d{12,19}")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid card number (at least 12 digits, numbers only)!");
                return;
            }
            
            PaymentDAO dao = new PaymentDAO();
            
            if (existingPayment != null) {
                // EDIT mode - Update existing payment
                existingPayment.setStudentId(studentId);
                existingPayment.setAmount(amount);
                existingPayment.setMethod(method);
                existingPayment.setCardNumber(cardNumber);
                existingPayment.setCourse(course);
                existingPayment.setYear(year);
                
                boolean success = dao.updatePayment(existingPayment);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Payment updated successfully!");
                    dispose();
                    if (parentPanel != null) parentPanel.refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update payment! Check console for errors.");
                }
            } else {
                // ADD mode - Create new payment
                Payment newPayment = new Payment();
                newPayment.setStudentId(studentId);
                newPayment.setAmount(amount);
                newPayment.setPaymentDate(Date.valueOf(LocalDate.now())); // Current date
                newPayment.setMethod(method);
                newPayment.setCardNumber(cardNumber);
                newPayment.setCourse(course);
                newPayment.setYear(year);
                
                System.out.println("Attempting to save payment: " + newPayment.getStudentId());
                System.out.println("Course: " + course);
                System.out.println("Year: " + year);
                
                boolean success = dao.addPayment(newPayment);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Payment added successfully! Payment ID: " + newPayment.getPaymentId());
                    dispose();
                    if (parentPanel != null) parentPanel.refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add payment! Please check:\n1. Student ID exists\n2. All fields are valid\n3. Check console for details");
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for Student ID and Amount!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error saving payment: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}

