package results;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ResultDAO {
    private Connection conn;
    
    public ResultDAO() {
        this.conn = DBConnection.getInstance().getConnection();
    }
    
    public ArrayList<Result> getStudentResult(int studentId) {
        String sql = "SELECT r.result_id, r.module_id, m.name, r.marks, r.grade " +
                     "FROM Results r " +
                     "JOIN Modules m ON r.module_id = m.id " +
                     "WHERE r.student_id = ?";
        ArrayList<Result> results = new ArrayList<>();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new Result(
                        rs.getInt("result_id"),
                        studentId,
                        "",
                        rs.getInt("module_id"),
                        rs.getString("name"),
                        rs.getInt("marks"),
                        rs.getString("grade")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }
    
    // Fetch results for a lecturer
    public List<Result> getResultsByLecturer(String lecturerId) throws SQLException {
        List<Result> results = new ArrayList<>();
        String sql = "SELECT r.result_id, r.student_id, " +
                     "CONCAT(s.first_name, ' ', s.last_name) AS StudentName, " +
                     "m.id AS ModuleID, m.name AS ModuleName, r.marks, r.grade " +
                     "FROM Results r " +
                     "JOIN Students s ON r.student_id = s.student_id " +
                     "JOIN Modules m ON r.module_id = m.id " +
                     "WHERE m.lecturer_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new Result(
                    rs.getInt("result_id"),
                    rs.getInt("student_id"),
                    rs.getString("StudentName"),
                    rs.getInt("ModuleID"),
                    rs.getString("ModuleName"),
                    rs.getInt("marks"),
                    rs.getString("grade")
                ));
            }
        }
        return results;
    }
    
    // Insert new result - FIXED: using class-level conn
    public int addResult(int studentId, int moduleId, int marks, String grade) throws SQLException {
        String sql = "INSERT INTO Results (student_id, module_id, marks, grade) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, studentId);
            stmt.setInt(2, moduleId);
            stmt.setInt(3, marks);
            stmt.setString(4, grade);
            
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1); // Return the generated result_id
                    }
                }
            }
            return -1; // Return -1 if insertion failed
        }
    }
    
    // Update existing result - FIXED: returns boolean
    public boolean updateResult(int resultId, int marks, String grade) throws SQLException {
        String sql = "UPDATE Results SET marks = ?, grade = ? WHERE result_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, marks);
            stmt.setString(2, grade);
            stmt.setInt(3, resultId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }

    // Delete a result by its ID - FIXED: returns boolean
    public boolean deleteResult(int resultId) throws SQLException {
        String sql = "DELETE FROM Results WHERE result_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, resultId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
    

    // Check if student exists
    public boolean studentExists(int studentId, String lecturerId) throws SQLException {
        String sql = "SELECT COUNT(DISTINCT s.student_id) " +
                        "FROM Students s " +
                        "JOIN Enrollments e ON s.student_id = e.student_id " +
                        "JOIN Modules m ON e.course_id = m.course_id " +
                        "WHERE m.lecturer_id = ? AND s.student_id = ?";
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lecturerId);
            stmt.setInt(2, studentId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
