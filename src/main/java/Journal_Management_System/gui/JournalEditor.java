package main.java.Journal_Management_System.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JournalEditor extends JFrame {
    private JTextArea textArea;
    private JTextField titleField; // 新增日志标题输入框
    private JLabel lastPublishedLabel; // 新增显示最后发布时间的标签
    private JButton publishButton, deleteButton, dashboardButton, viewerButton;
    private JLabel nameLabel, avatarLabel;

    public JournalEditor() {
        setTitle("Editor");
        setSize(700, 500); // 调整窗口大小
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setAlwaysOnTop(true);


        // 顶部：用户信息和日志标题
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        avatarLabel = new JLabel(new ImageIcon("path/to/user/avatar.jpg")); // 替换为实际头像路径
        nameLabel = new JLabel("用户名"); // 替换为实际用户名
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
        dashboardButton = new JButton("Dashboard");
        viewerButton = new JButton("Viewer");
        rightPanel.add(publishButton);
        rightPanel.add(deleteButton);
        rightPanel.add(dashboardButton);
        rightPanel.add(viewerButton);
        add(rightPanel, BorderLayout.EAST);

        // 底部：日志信息
        JPanel bottomPanel = new JPanel();
        lastPublishedLabel = new JLabel("最后发布时间：未知"); // 初始标签内容
        bottomPanel.add(lastPublishedLabel);
        add(bottomPanel, BorderLayout.SOUTH);


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
                deleteLog();
            }
        });

        dashboardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToDashboard();
            }
        });

        viewerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goToViewer();
            }
        });


    }

    private void publishLog() {
        String logContent = textArea.getText();
    }

    private void deleteLog() {
        textArea.setText(""); // 清空文本框内容
        // 这里需要一个标识符来确定要删除的数据库记录，比如日志ID
        // String logId = ...;
        // deleteLogFromDatabase(logId);
    }


        private void goToDashboard() {
        new UserDashboard().setVisible(true);
        dispose();
    }

    private void goToViewer() {
        // 跳转到Viewer界面的逻辑
    }
    //TODO:连接到数据库

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JournalEditor().setVisible(true);
        });
    }
}
