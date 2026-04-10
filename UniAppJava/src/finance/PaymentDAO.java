package finance;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import models.Payment;

public class PaymentDAO {

    // Add a new payment and return generated ID
    public boolean addPayment(Payment p) {
        String sql = "INSERT INTO Payments (StudentID, Amount, PaymentDate, Method, CardNumber, Course, Year) VALUES (?,?,?,?,?,?,?)";
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, p.getStudentId());
            stmt.setDouble(2, p.getAmount());
            stmt.setTimestamp(3, new Timestamp(p.getPaymentDate().getTime()));
            stmt.setString(4, p.getMethod());
            stmt.setString(5, p.getCardNumber());
            stmt.setString(6, p.getCourse());
            stmt.setInt(7, p.getYear());

            int rows = stmt.executeUpdate();
            
            if (rows == 0) {
                conn.rollback();
                System.err.println("No rows inserted!");
                return false;
            }

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setPaymentId(keys.getInt(1));
                }
            }
            
            conn.commit();
            return true;
            
        } catch (SQLException e) {
            System.err.println("SQL Error Code: " + e.getErrorCode());
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Message: " + e.getMessage());
            
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            // Close resources
            try { if (stmt != null) stmt.close(); } catch (SQLException e) {}
            try { if (conn != null) { conn.setAutoCommit(true); conn.close(); } } catch (SQLException e) {}
        }
    }

    // Get all payments (Admin dashboard)
    public List<Payment> getPayments() {
        List<Payment> payments = new ArrayList<>();
        String sql = "SELECT * FROM Payments ORDER BY PaymentDate DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                payments.add(mapRow(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    // Get payments by student (Student dashboard)
    public List<Payment> getPaymentsByStudent(int studentId) {
        List<Payment> list = new ArrayList<>();
        String sql = "SELECT * FROM Payments WHERE StudentID = ? ORDER BY PaymentDate DESC";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Get a single payment by ID
    public Payment getPaymentById(int paymentId) {
        String sql = "SELECT * FROM Payments WHERE PaymentID=?";
        try (Connection conn = DBConnection.getInstance().getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, paymentId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRow(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Update a payment
    public boolean updatePayment(Payment payment) {
        String sql = "UPDATE Payments SET StudentID=?, Amount=?, PaymentDate=?, Method=?, CardNumber=?, Course=?, Year=? WHERE PaymentID=?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, payment.getStudentId());
            ps.setDouble(2, payment.getAmount());
            ps.setTimestamp(3, new Timestamp(payment.getPaymentDate().getTime()));
            ps.setString(4, payment.getMethod());
            ps.setString(5, payment.getCardNumber());
            ps.setString(6, payment.getCourse());
            ps.setInt(7, payment.getYear());
            ps.setInt(8, payment.getPaymentId());
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true);
                    conn.close(); 
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // Delete a payment
    public boolean deletePayment(int paymentId) {
        String sql = "DELETE FROM Payments WHERE PaymentID=?";
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = DBConnection.getInstance().getConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            ps.setInt(1, paymentId);
            
            int rows = ps.executeUpdate();
            if (rows > 0) {
                conn.commit();
                return true;
            } else {
                conn.rollback();
                return false;
            }
        } catch (SQLException e) {
            if (conn != null) {
                try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            }
            e.printStackTrace();
            return false;
        } finally {
            if (ps != null) {
                try { ps.close(); } catch (SQLException e) { e.printStackTrace(); }
            }
            if (conn != null) {
                try { 
                    conn.setAutoCommit(true);
                    conn.close(); 
                } catch (SQLException e) { e.printStackTrace(); }
            }
        }
    }

    // Helper to map a row to Payment object
    private Payment mapRow(ResultSet rs) throws SQLException {
        Payment p = new Payment();
        p.setPaymentId(rs.getInt("PaymentID"));
        p.setStudentId(rs.getInt("StudentID"));
        p.setAmount(rs.getDouble("Amount"));
        p.setPaymentDate(rs.getTimestamp("PaymentDate"));
        p.setMethod(rs.getString("Method"));
        p.setCardNumber(rs.getString("CardNumber"));
        p.setCourse(rs.getString("Course"));
        p.setYear(rs.getInt("Year"));
        return p;
    }
}