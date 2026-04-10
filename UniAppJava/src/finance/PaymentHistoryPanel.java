package finance;

import models.Payment;
import finance.PaymentDAO;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class PaymentHistoryPanel extends JPanel {
    private JTable tblPayments;
    private DefaultTableModel tableModel;

    public PaymentHistoryPanel() {
        setupPanel();
        loadAllPayments();
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
        JButton btnAdd = new JButton("Add Payment");
        JButton btnEdit = new JButton("Edit Payment");
        JButton btnDelete = new JButton("Delete Payment");
        JButton btnView = new JButton("View Payment");

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnView);

        add(buttonPanel, BorderLayout.SOUTH);

        // Fix: Pass null to indicate ADD mode
        btnAdd.addActionListener(e -> new AdminFormFrame(this, null));
        btnEdit.addActionListener(e -> editPayment());
        btnDelete.addActionListener(e -> deletePayment());
        btnView.addActionListener(e -> viewPayment());
    }

    public void refreshData() {
        loadAllPayments();
    }

    private void loadAllPayments() {
        try {
            PaymentDAO dao = new PaymentDAO();
            List<Payment> list = dao.getPayments();
            tableModel.setRowCount(0);
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
            JOptionPane.showMessageDialog(this, "Error loading all payments: " + ex.getMessage());
        }
    }

    private void editPayment() {
        int row = tblPayments.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a payment to edit.");
            return;
        }
        int paymentId = (int) tableModel.getValueAt(row, 0);
        PaymentDAO dao = new PaymentDAO();
        Payment selected = dao.getPaymentById(paymentId);
        if (selected != null) {
            new AdminFormFrame(this, selected);
        }
    }

    private void deletePayment() {
        int row = tblPayments.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a payment to delete.");
            return;
        }
        int paymentId = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this,
            "Delete payment ID " + paymentId + "?",
            "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            PaymentDAO dao = new PaymentDAO();
            boolean success = dao.deletePayment(paymentId);
            if (success) {
                JOptionPane.showMessageDialog(this, "Deleted successfully!");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete payment!");
            }
        }
    }

    private void viewPayment() {
        int row = tblPayments.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a payment to view.");
            return;
        }
        int paymentId = (int) tableModel.getValueAt(row, 0);
        PaymentDAO dao = new PaymentDAO();
        Payment selected = dao.getPaymentById(paymentId);
        if (selected != null) {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            PaymentViewDialog viewDialog = new PaymentViewDialog(parentFrame, selected);
            viewDialog.setVisible(true);
        }
    }
}



