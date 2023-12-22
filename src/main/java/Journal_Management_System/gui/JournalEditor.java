package main.java.Journal_Management_System.gui;

import main.java.Journal_Management_System.dao.DiaryDAO;
import main.java.Journal_Management_System.dao.DiaryEntry;
import main.java.Journal_Management_System.dao.UserDAO;
import main.java.Journal_Management_System.util.DatabaseConnection;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;

public class JournalEditor extends JFrame {
    private JTextArea textArea;
    private JTextField titleField; // 新增日志标题输入框
    private JLabel lastPublishedLabel; // 新增显示最后发布时间的标签
    private JButton publishButton, deleteButton, dashboardButton, viewerButton;
    private JLabel nameLabel, avatarLabel;
    private String username;
    private Integer selectedDiaryId;
    private Connection connection= DatabaseConnection.getCon();
    private DiaryDAO diaryDAO=new DiaryDAO(connection);
    private UserDAO userDAO=new UserDAO(connection);
    public JournalEditor(String username,int selectedDiaryId) throws SQLException, IOException {
        this.selectedDiaryId=selectedDiaryId;
        this.username=username;
        setTitle("Editor");
        setSize(700, 500); // 调整窗口大小
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setAlwaysOnTop(true);
        // 根据 selectedDiaryId 判断是新建日志还是编辑现有日志

        // 顶部：用户信息和日志标题
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        String avatarPath= userDAO.getAvatarPath(username);
        BufferedImage avatarImage = ImageIO.read(new File(avatarPath));
        Image scaledImage = avatarImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        avatarLabel = new JLabel(new ImageIcon(scaledImage)); // 替换为实际头像路径
        nameLabel = new JLabel(username);
        titleField = new JTextField("日志标题");
        topPanel.add(avatarLabel);
        topPanel.add(nameLabel);
        topPanel.add(titleField);
        add(topPanel, BorderLayout.NORTH);

        // 文本区域
        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // 右侧：操作按钮
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        publishButton = new JButton("保存");
        deleteButton = new JButton("删除");
        dashboardButton = new JButton("返回后台");
        //viewerButton = new JButton("Viewer");
        rightPanel.add(publishButton);
        rightPanel.add(deleteButton);
        rightPanel.add(dashboardButton);
        //rightPanel.add(viewerButton);
        add(rightPanel, BorderLayout.EAST);

        // 底部：日志信息
        JPanel bottomPanel = new JPanel();
        lastPublishedLabel = new JLabel("最后发布时间：未知"); // 初始标签内容
        bottomPanel.add(lastPublishedLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        if (selectedDiaryId != -1) {
            // 加载并显示现有日志的数据
            loadDiaryData(selectedDiaryId);
        }
        // 按钮监听器
        publishButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                publishLog();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    deleteLog();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    goToDashboard();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

//        viewerButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                goToViewer();
//            }
//        });


    }
    private void loadDiaryData(int diaryId) {
        // 从数据库加载日志数据
        DiaryEntry diaryEntry = diaryDAO.getDiaryById(diaryId);

        if (diaryEntry != null) {
            titleField.setText(diaryEntry.getTitle());
            textArea.setText(diaryEntry.getContent());
            lastPublishedLabel.setText("最后修改时间："+diaryEntry.getLastCommitTime());
        }
    }

    private void publishLog() {
        String logTitle = titleField.getText();
        String logContent = textArea.getText();
        DiaryDAO diaryDAO = new DiaryDAO(connection);
        int userId = diaryDAO.getUserIdByUsername(username);

        // 检查是新建日志还是更新现有日志
        if (selectedDiaryId == -1) {
            // 创建新日志
            String currentTime = new Timestamp(System.currentTimeMillis()).toString();
            DiaryEntry newDiaryEntry = new DiaryEntry(
                    -1, // 对于新日志，ID 可以是 -1 或其他占位值
                    userId,
                    logTitle,
                    logContent,
                    currentTime, // 创建时间
                    currentTime  // 最后更新时间
            );
            diaryDAO.addDiary(newDiaryEntry);
            JOptionPane.showMessageDialog(this, "日志已创建");
            lastPublishedLabel.setText("最后修改时间："+newDiaryEntry.getLastCommitTime());
        } else {
            // 更新现有日志
            DiaryEntry existingDiaryEntry = new DiaryEntry(
                    selectedDiaryId, // 现有日志的ID
                    userId,
                    logTitle,
                    logContent,
                    null, // 对于更新，创建时间通常不变
                    new Timestamp(System.currentTimeMillis()).toString() // 更新最后更新时间
            );
            diaryDAO.updateDiary(existingDiaryEntry);
            JOptionPane.showMessageDialog(this, "日志已更新");
            lastPublishedLabel.setText("最后修改时间："+existingDiaryEntry.getLastCommitTime());
        }

    }

    private void deleteLog() throws SQLException {
        if (selectedDiaryId != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "确定要删除这篇日志吗?", "确认", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                diaryDAO.deleteDiary(selectedDiaryId);
                JOptionPane.showMessageDialog(this, "日志已删除");
                // 关闭编辑器或清除表单
                clearForm();
                if(userDAO.getUserRole(username).equals("admin")){
                    new AdminDashboard(username).setVisible(true);
                    dispose();
                }
                else{
                    new UserDashboard(username).setVisible(true);
                    dispose();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "没有选定的日志可以删除");
        }
    }

    private void clearForm() {
        titleField.setText("");
        textArea.setText("");
    }


        private void goToDashboard() throws SQLException {
        new AdminDashboard(username).setVisible(true);
        dispose();
    }

//    private void goToViewer() {
//        // 跳转到Viewer界面的逻辑
//    }
    //TODO:连接到数据库

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //new JournalEditor().setVisible(true);
        });
    }
}
