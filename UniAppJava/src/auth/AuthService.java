    package auth;

    import db.DBConnection;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import models.User;

    public class AuthService {

        public User login(String username, String password, String role) {
            String sql = "SELECT * FROM Users WHERE username = ? AND password = ? AND role = ?";
            Connection conn = DBConnection.getInstance().getConnection();
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setString(3, role);

                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("role"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;

        }

    }