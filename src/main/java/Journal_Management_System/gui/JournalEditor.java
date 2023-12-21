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
    private JTextField titleField; // ������־���������
    private JLabel lastPublishedLabel; // ������ʾ��󷢲�ʱ��ı�ǩ
    private JButton publishButton, deleteButton, dashboardButton, viewerButton;
    private JLabel nameLabel, avatarLabel;

    public JournalEditor() {
        setTitle("Editor");
        setSize(700, 500); // �������ڴ�С
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setAlwaysOnTop(true);


        // �������û���Ϣ����־����
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        avatarLabel = new JLabel(new ImageIcon("path/to/user/avatar.jpg")); // �滻Ϊʵ��ͷ��·��
        nameLabel = new JLabel("�û���"); // �滻Ϊʵ���û���
        titleField = new JTextField("��־����");
        topPanel.add(avatarLabel);
        topPanel.add(nameLabel);
        topPanel.add(titleField);
        add(topPanel, BorderLayout.NORTH);

        // �ı�����
        textArea = new JTextArea();
        add(new JScrollPane(textArea), BorderLayout.CENTER);

        // �Ҳࣺ������ť
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        publishButton = new JButton("����");
        deleteButton = new JButton("ɾ��");
        dashboardButton = new JButton("Dashboard");
        viewerButton = new JButton("Viewer");
        rightPanel.add(publishButton);
        rightPanel.add(deleteButton);
        rightPanel.add(dashboardButton);
        rightPanel.add(viewerButton);
        add(rightPanel, BorderLayout.EAST);

        // �ײ�����־��Ϣ
        JPanel bottomPanel = new JPanel();
        lastPublishedLabel = new JLabel("��󷢲�ʱ�䣺δ֪"); // ��ʼ��ǩ����
        bottomPanel.add(lastPublishedLabel);
        add(bottomPanel, BorderLayout.SOUTH);


        // ��ť������
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
        textArea.setText(""); // ����ı�������
        // ������Ҫһ����ʶ����ȷ��Ҫɾ�������ݿ��¼��������־ID
        // String logId = ...;
        // deleteLogFromDatabase(logId);
    }


        private void goToDashboard() {
        new UserDashboard().setVisible(true);
        dispose();
    }

    private void goToViewer() {
        // ��ת��Viewer������߼�
    }
    //TODO:���ӵ����ݿ�

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new JournalEditor().setVisible(true);
        });
    }
}
