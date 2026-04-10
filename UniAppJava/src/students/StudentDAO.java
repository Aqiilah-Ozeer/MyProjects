package students;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import models.Student;

public class StudentDAO {

    public void addStudent(Student student) {
        String sql = "INSERT INTO Students (student_id, first_name, last_name, email, phone, date_of_birth, address) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, student.getStudentId());
            stmt.setString(2, student.getFirstName());
            stmt.setString(3, student.getLastName());
            stmt.setString(4, student.getEmail());
            stmt.setString(5, student.getPhone());
            stmt.setDate(6, student.getDateOfBirth());
            stmt.setString(7, student.getAddress());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM Students";
        Connection conn = DBConnection.getInstance().getConnection();
        try (Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getDate("date_of_birth"),
                        rs.getString("address"));
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    public Student getStudentById(int studentId) {
        String sql = "SELECT * FROM Students WHERE student_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Student(
                        rs.getInt("student_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getDate("date_of_birth"),
                        rs.getString("address"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateStudent(Student student) {
        String sql = "UPDATE Students SET first_name = ?, last_name = ?, email = ?, phone = ?, date_of_birth = ?, address = ? WHERE student_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getEmail());
            stmt.setString(4, student.getPhone());
            stmt.setDate(5, student.getDateOfBirth());
            stmt.setString(6, student.getAddress());
            stmt.setInt(7, student.getStudentId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteStudent(int studentId) {
        String sql = "DELETE FROM Students WHERE student_id = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getStudentAccPwd(String username) {
        String sql = "SELECT password FROM Users WHERE username = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("password");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateStudentAccPwd(String username, String newPassword) {
        String sql = "UPDATE Users SET password = ? WHERE username = ?";
        Connection conn = DBConnection.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, newPassword);
            stmt.setString(2, username);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // getStudentsByLecturer method use in lecturer package
    public ArrayList<Student> getStudentsByLecturer(String lecturerId) throws SQLException {
        ArrayList<Student> students = new ArrayList<>();
        Connection conn = DBConnection.getInstance().getConnection();
        
        System.out.println("=== DEBUG: Getting students for lecturer: " + lecturerId);
        
        // query based on our updated database schema
        String sql = "SELECT DISTINCT s.student_id, s.first_name, s.last_name, s.email, s.phone, " +
                    "s.date_of_birth, s.address, c.name AS course_name " +
                    "FROM Students s " +
                    "INNER JOIN Enrollments e ON s.student_id = e.student_id " +
                    "INNER JOIN Courses c ON e.course_id = c.id " +
                    "INNER JOIN Modules m ON m.course_id = c.id " +
                    "WHERE m.lecturer_id = ? " +
                    "ORDER BY s.student_id";
        
        System.out.println("Executing SQL: " + sql);
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Student student = new Student(
                    rs.getInt("student_id"),
                    rs.getString("first_name"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getDate("date_of_birth"),
                    rs.getString("address")
                );
                student.setModuleName(rs.getString("course_name"));
                students.add(student);
                System.out.println("Added student: " + rs.getInt("student_id") + " - " + rs.getString("first_name"));
            }
            rs.close();
        }
        
        System.out.println("Total students found: " + students.size());
        return students;
    }
}