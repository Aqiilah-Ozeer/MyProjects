package finance;

import models.Payment;
import javax.swing.*;
import java.awt.*;

public class PaymentViewDialog extends JDialog {
    
    public PaymentViewDialog(JFrame parent, Payment payment) {
        super(parent, "VIEW PAYMENT", true);
        setupUI(payment);
        setSize(450, 480);
        setLocationRelativeTo(parent);
        setResizable(false);
    }
    
    private void setupUI(Payment payment) {
        setLayout(new BorderLayout());
        
        // Panel with 2 columns (reduced from 9 to 8 rows)
        JPanel infoPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add labels and values
        infoPanel.add(new JLabel("Payment ID:"));
        infoPanel.add(new JLabel(String.valueOf(payment.getPaymentId())));
        
        infoPanel.add(new JLabel("Student ID:"));
        infoPanel.add(new JLabel(String.valueOf(payment.getStudentId())));
        
        infoPanel.add(new JLabel("Amount:"));
        infoPanel.add(new JLabel(String.format("$%.2f", payment.getAmount())));
        
        infoPanel.add(new JLabel("Payment Date:"));
        infoPanel.add(new JLabel(payment.getPaymentDate().toString()));
        
        infoPanel.add(new JLabel("Method:"));
        infoPanel.add(new JLabel(payment.getMethod()));
        
        infoPanel.add(new JLabel("Card Number:"));
        String cardNumber = payment.getCardNumber();
        String maskedCard;
        if (cardNumber != null && cardNumber.length() > 4) {
            maskedCard = "**** **** **** " + cardNumber.substring(Math.max(0, cardNumber.length() - 4));
        } else {
            maskedCard = "****";
        }
        infoPanel.add(new JLabel(maskedCard));
        
        infoPanel.add(new JLabel("Course:"));
        infoPanel.add(new JLabel(payment.getCourse()));
        
        infoPanel.add(new JLabel("Year:"));
        infoPanel.add(new JLabel(String.valueOf(payment.getYear())));
        
        // Button Panel
        JPanel buttonPanel = new JPanel();
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        
        add(infoPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
}