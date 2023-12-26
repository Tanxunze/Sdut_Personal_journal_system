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
    private JTextField titleField;
    private JLabel lastPublishedLabel;
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
        setSize(700, 500);
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
        avatarLabel = new JLabel(new ImageIcon(scaledImage));
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
                    -1,
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
                    null,
                    new Timestamp(System.currentTimeMillis()).toString() // 更新最后更新时间
            );
            diaryDAO.updateDiary(existingDiaryEntry);
            JOptionPane.showMessageDialog(this, "抱歉，为确保隐私安全，即使管理员也无法修改日志！");
            lastPublishedLabel.setText("最后修改时间："+existingDiaryEntry.getLastCommitTime());
        }

    }

    private void deleteLog() throws SQLException {
        if (selectedDiaryId != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "确定要删除这篇日志吗?", "确认", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                diaryDAO.deleteDiary(selectedDiaryId);
                JOptionPane.showMessageDialog(this, "日志已删除");
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
            String role=userDAO.getUserRole(username);
            if(role.equals("admin")){
                new AdminDashboard(username).setVisible(true);
            }
            else{
                new UserDashboard(username).setVisible(true);
            }
            dispose();
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //new JournalEditor().setVisible(true);
        });
    }
}
