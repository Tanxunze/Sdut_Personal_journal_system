package main.java.Journal_Management_System.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {
    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }
    //获取头像
    public String getAvatarPath(String username) {
        String sql = "SELECT avatar_path FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("avatar_path");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "src/main/java/resources/imges/avatar.jpg"; // 返回一个默认头像路径
    }


    // 验证用户凭证
    public boolean validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // 在实际应用中，密码应该是加密过的

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next(); // 如果找到匹配的用户，返回true
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 获取用户角色
    public String getUserRole(String username) {
        String sql = "SELECT role FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("role");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 添加新用户
    public boolean addUser(String username, String password) {
        String sql = "INSERT INTO users (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password); // 密码应该是加密过的

            return pstmt.executeUpdate() == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
