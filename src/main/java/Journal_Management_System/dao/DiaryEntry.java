package main.java.Journal_Management_System.dao;

public class DiaryEntry {
    private int id;
    private int userId;
    private String title;
    private String content;
    private String createdTime;
    private String lastCommitTime;
    private String username;

    public DiaryEntry(int id, int userId, String title, String content, String createdTime, String lastCommitTime) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
        this.lastCommitTime = lastCommitTime;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastCommitTime() {
        return lastCommitTime;
    }

    public void setLastCommitTime(String lastCommitTime) {
        this.lastCommitTime = lastCommitTime;
    }
}

