package lecturers;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LecturerDAO {
    private DBConnection dbConnection;

    public LecturerDAO() {
        dbConnection = DBConnection.getInstance();
    }  

    public List<Lecturer> listLecturers() {
        List<Lecturer> lecturers = new ArrayList<>();
        String sql = "SELECT * FROM Lecturer";
        Connection conn = dbConnection.getConnection();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Lecturer lecturer = new Lecturer(
                        rs.getString("Lecturer_Id"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getString("Title"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Department"),
                        rs.getString("Specialisation"),
                        rs.getString("Qualification"),
                        rs.getString("Employment_Type"));
                lecturers.add(lecturer);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lecturers;
    }

    public boolean addLecturer(Lecturer lecturer) {
        String sql = "INSERT INTO Lecturer (Lecturer_Id, First_Name, Last_Name, Title, Email, Phone, Department, Specialisation, Qualification, Employment_Type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = dbConnection.getConnection();
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, lecturer.getLecturerId());
            pstmt.setString(2, lecturer.getFirstName());
            pstmt.setString(3, lecturer.getLastName());
            pstmt.setString(4, lecturer.getTitle());
            pstmt.setString(5, lecturer.getEmail());
            pstmt.setString(6, lecturer.getPhone());
            pstmt.setString(7, lecturer.getDepartment());
            pstmt.setString(8, lecturer.getSpecialisation());
            pstmt.setString(9, lecturer.getQualification());
            pstmt.setString(10, lecturer.getEmploymentType());
            
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
            
        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println("Duplicate key error: Lecturer ID already exists!");
            e.printStackTrace();
            return false;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void updateLecturer(Lecturer lecturer) {
        String sql = "UPDATE Lecturer SET First_Name=?, Last_Name=?, Title=?, Email=?, Phone=?, Department=?, Specialisation=?, Qualification=?, Employment_Type=? WHERE Lecturer_Id=?";
        Connection conn = dbConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lecturer.getFirstName());
            stmt.setString(2, lecturer.getLastName());
            stmt.setString(3, lecturer.getTitle());
            stmt.setString(4, lecturer.getEmail());
            stmt.setString(5, lecturer.getPhone());
            stmt.setString(6, lecturer.getDepartment());
            stmt.setString(7, lecturer.getSpecialisation());
            stmt.setString(8, lecturer.getQualification());
            stmt.setString(9, lecturer.getEmploymentType());
            stmt.setString(10, lecturer.getLecturerId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteLecturer(String lecturerId) {
        String sql = "DELETE FROM Lecturer WHERE Lecturer_Id=?";
        Connection conn = dbConnection.getConnection();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lecturerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Lecturer getLecturerById(String lecturerId) {
        String sql = "SELECT * FROM Lecturer WHERE Lecturer_Id=?";
        Connection conn = dbConnection.getConnection();
        
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, lecturerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Lecturer(
                        rs.getString("Lecturer_Id"),
                        rs.getString("First_Name"),
                        rs.getString("Last_Name"),
                        rs.getString("Title"),
                        rs.getString("Email"),
                        rs.getString("Phone"),
                        rs.getString("Department"),
                        rs.getString("Specialisation"),
                        rs.getString("Qualification"),
                        rs.getString("Employment_Type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
