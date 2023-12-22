package main.java.Journal_Management_System.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DiaryDAO {
    private Connection connection;

    public DiaryDAO(Connection connection) {
        this.connection = connection;
    }


    // 添加新日志
    public boolean addDiary(DiaryEntry diary) {
        String sql = "INSERT INTO journals (user_id, title, content, created_time, lastcommit_time) VALUES (?, ?, ?, NOW(), NOW())";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, diary.getUserId());
            pstmt.setString(2, diary.getTitle());
            pstmt.setString(3, diary.getContent());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 更新日志
    public boolean updateDiary(DiaryEntry diary) {
        String sql = "UPDATE journals SET title = ?, content = ?, lastcommit_time = NOW() WHERE id = ? AND user_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, diary.getTitle());
            pstmt.setString(2, diary.getContent());
            pstmt.setInt(3, diary.getId());
            pstmt.setInt(4, diary.getUserId());

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除日志
    public boolean deleteDiary(int diaryId) {
        String sql = "DELETE FROM journals WHERE id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, diaryId);

            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public int getUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // 或抛出异常
    }

    public DiaryEntry getDiaryById(int diaryId) {
        String sql = "SELECT user_id ,title, content, created_time, lastcommit_time FROM journals WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, diaryId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new DiaryEntry(
                            diaryId,
                            rs.getInt("user_id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("created_time"),
                            rs.getString("lastcommit_time")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // 如果没有找到，返回 null
    }
    public List<DiaryEntry> getDiariesByUserId(int userId) {
        List<DiaryEntry> diaries = new ArrayList<>();
        String sql = "SELECT id, title, content, created_time, lastcommit_time FROM journals WHERE user_id = ?";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String content = rs.getString("content");
                    // 创建内容摘要，例如取前100个字符
                    String summary = content.length() > 100 ? content.substring(0, 100) + "..." : content;

                    diaries.add(new DiaryEntry(
                            rs.getInt("id"),
                            userId,
                            rs.getString("title"),
                            summary,
                            rs.getString("created_time"),
                            rs.getString("lastcommit_time")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return diaries;
    }

    public List<DiaryEntry> getAllDiaries() {
        List<DiaryEntry> diaries = new ArrayList<>();
        String sql = "SELECT id, user_id, title, content, created_time, lastcommit_time FROM journals";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    DiaryEntry diary = new DiaryEntry(
                            rs.getInt("id"),
                            rs.getInt("user_id"),
                            rs.getString("title"),
                            rs.getString("content"),
                            rs.getString("created_time"),
                            rs.getString("lastcommit_time")
                    );
                    diaries.add(diary);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return diaries;
    }
    public List<DiaryEntry> getDiariesByUsername(String username) {
        int userId = getUserIdByUsername(username);
        if (userId == -1) {
            return new ArrayList<>(); // 或处理错误情况
        }
        return getDiariesByUserId(userId);
    }
}
