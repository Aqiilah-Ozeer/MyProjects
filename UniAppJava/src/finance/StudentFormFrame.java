package finance;

import models.Payment;
import finance.PaymentDAO;
import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.time.LocalDate;

public class StudentFormFrame extends JFrame {
    private PaymentHistoryFrame parentFrame;
    private int studentId;
    
    private JTextField txtAmount, txtCardNumber;
    private JComboBox<String> cmbMethod;
    private JComboBox<Integer> cmbYear;
    private JComboBox<String> cmbCourse;
    private JButton btnSave, btnCancel;

    // Course options (from your courses table)
    private String[] courseOptions = {
        "Transfiguration",
        "Charms", 
        "Potions",
        "History of Magic",
        "Defence Against the Dark Arts",
        "Astronomy",
        "Herbology"
    };

    public StudentFormFrame(PaymentHistoryFrame parentFrame, int studentId) {
        this.parentFrame = parentFrame;
        this.studentId = studentId;
        setupUI();
        setTitle("Add Payment - Student ID: " + studentId);
        setSize(400, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void setupUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Student ID (display only, not editable)
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Student ID:"), gbc);
        gbc.gridx = 1;
        JLabel lblStudentId = new JLabel(String.valueOf(studentId));
        lblStudentId.setFont(new Font("Arial", Font.BOLD, 12));
        add(lblStudentId, gbc);

        // Amount
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        txtAmount = new JTextField(15);
        add(txtAmount, gbc);

        // Payment Method
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Payment Method:"), gbc);
        gbc.gridx = 1;
        cmbMethod = new JComboBox<>(new String[]{"Credit Card", "Debit Card", "Bank Transfer", "Cash"});
        add(cmbMethod, gbc);

        // Card Number
        gbc.gridx = 0; gbc.gridy = 3;
        add(new JLabel("Card Number:"), gbc);
        gbc.gridx = 1;
        txtCardNumber = new JTextField(15);
        add(txtCardNumber, gbc);

        // Course (Dropdown only)
        gbc.gridx = 0; gbc.gridy = 4;
        add(new JLabel("Course:"), gbc);
        gbc.gridx = 1;
        cmbCourse = new JComboBox<>(courseOptions);
        cmbCourse.setEditable(false);
        add(cmbCourse, gbc);

        // Year (Dropdown 1-4)
        gbc.gridx = 0; gbc.gridy = 5;
        add(new JLabel("Year:"), gbc);
        gbc.gridx = 1;
        cmbYear = new JComboBox<>();
        for (int i = 1; i <= 4; i++) {
            cmbYear.addItem(i);
        }
        add(cmbYear, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        btnSave = new JButton("Save");
        btnCancel = new JButton("Cancel");
        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        add(buttonPanel, gbc);

        btnSave.addActionListener(e -> savePayment());
        btnCancel.addActionListener(e -> dispose());
    }

    private void savePayment() {
        try {
            // Validate inputs
            if (txtAmount.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter amount!");
                return;
            }
            
            if (txtCardNumber.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter card number!");
                return;
            }

            double amount = Double.parseDouble(txtAmount.getText().trim());
            String method = (String) cmbMethod.getSelectedItem();
            String cardNumber = txtCardNumber.getText().trim();
            String course = (String) cmbCourse.getSelectedItem();
            int year = (Integer) cmbYear.getSelectedItem();
            
            // Validate amount
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be greater than 0!");
                return;
            }
            
            // Validate card number (must be at least 12 digits and numeric)
            if (!cardNumber.matches("\\d{12,19}")) {
                JOptionPane.showMessageDialog(this, "Please enter a valid card number (at least 12 digits, numbers only)!");
                return;
            }
            
            // Create payment object
            Payment newPayment = new Payment();
            newPayment.setStudentId(studentId);
            newPayment.setAmount(amount);
            newPayment.setPaymentDate(Date.valueOf(LocalDate.now()));
            newPayment.setMethod(method);
            newPayment.setCardNumber(cardNumber);
            newPayment.setCourse(course);
            newPayment.setYear(year);
            
            // Debug output
            System.out.println("=== Payment Details ===");
            System.out.println("Student ID: " + studentId);
            System.out.println("Amount:" + amount);
            System.out.println("Method: " + method);
            System.out.println("Card Number: " + cardNumber.substring(0, 4) + "****" + cardNumber.substring(cardNumber.length()-4));
            System.out.println("Course: " + course);
            System.out.println("Year: " + year);
            System.out.println("Payment Date: " + newPayment.getPaymentDate());
            
            PaymentDAO dao = new PaymentDAO();
            boolean success = dao.addPayment(newPayment);
            
            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Payment added successfully!\n\n" +
                    "Payment Details:\n" +
                    "Student ID: " + studentId + "\n" +
                    "Amount:" + amount + "\n" +
                    "Course: " + course + "\n" +
                    "Year: " + year);
                dispose();
                if (parentFrame != null) {
                    parentFrame.refreshData();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add payment!\nPlease check console for detailed error message.");
            }
            
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for Amount!");
            ex.printStackTrace();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}