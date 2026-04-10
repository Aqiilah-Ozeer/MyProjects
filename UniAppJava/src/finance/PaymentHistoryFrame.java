package finance;

import models.Payment;
import finance.PaymentDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PaymentHistoryFrame extends JFrame {
    private JTable tblPayments;
    private DefaultTableModel tableModel;
    private int studentId;

    public PaymentHistoryFrame(int studentId) {
        this.studentId = studentId;
        setTitle("My Payment History");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setupPanel();
        loadPaymentsByStudent(studentId);

        setVisible(true);
    }

    private void setupPanel() {
        setLayout(new BorderLayout());

        String[] columns = {
            "Payment ID", "Student ID", "Amount", "Payment Date",
            "Method", "Card Number", "Course", "Year"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tblPayments = new JTable(tableModel);
        add(new JScrollPane(tblPayments), BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton btnView = new JButton("View Payment");
        JButton btnAdd = new JButton("Add Payment");
        
        buttonPanel.add(btnView);
        buttonPanel.add(btnAdd);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        btnView.addActionListener(e -> viewPayment());
        btnAdd.addActionListener(e -> {
            new StudentFormFrame(this, studentId);
        });
    }

    private void viewPayment() {
        int row = tblPayments.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select a payment to view.");
            return;
        }
        
        int paymentId = (int) tableModel.getValueAt(row, 0);
        PaymentDAO dao = new PaymentDAO();
        Payment selected = dao.getPaymentById(paymentId);
        
        if (selected != null) {
            // Create and show the payment view dialog
            PaymentViewDialog viewDialog = new PaymentViewDialog(this, selected);
            viewDialog.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Payment not found!");
        }
    }

    private void loadPaymentsByStudent(int studentId) {
        try {
            PaymentDAO dao = new PaymentDAO();
            List<Payment> list = dao.getPaymentsByStudent(studentId);
            tableModel.setRowCount(0);
            
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No payment records found for this student.");
            }
            
            for (Payment p : list) {
                tableModel.addRow(new Object[] {
                    p.getPaymentId(),
                    p.getStudentId(),
                    p.getAmount(),
                    String.valueOf(p.getPaymentDate()),
                    p.getMethod(),
                    p.getCardNumber(),
                    p.getCourse(),
                    p.getYear()
                });
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error loading student history: " + ex.getMessage());
        }
    }
    
    public void refreshData() {
        loadPaymentsByStudent(studentId);
    }
}



