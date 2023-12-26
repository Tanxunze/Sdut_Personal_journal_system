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
        // ���� selectedDiaryId �ж����½���־���Ǳ༭������־

        // �������û���Ϣ����־����
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        String avatarPath= userDAO.getAvatarPath(username);
        BufferedImage avatarImage = ImageIO.read(new File(avatarPath));
        Image scaledImage = avatarImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        avatarLabel = new JLabel(new ImageIcon(scaledImage));
        nameLabel = new JLabel(username);
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
        dashboardButton = new JButton("���غ�̨");
        //viewerButton = new JButton("Viewer");
        rightPanel.add(publishButton);
        rightPanel.add(deleteButton);
        rightPanel.add(dashboardButton);
        //rightPanel.add(viewerButton);
        add(rightPanel, BorderLayout.EAST);

        // �ײ�����־��Ϣ
        JPanel bottomPanel = new JPanel();
        lastPublishedLabel = new JLabel("��󷢲�ʱ�䣺δ֪"); // ��ʼ��ǩ����
        bottomPanel.add(lastPublishedLabel);
        add(bottomPanel, BorderLayout.SOUTH);

        if (selectedDiaryId != -1) {
            // ���ز���ʾ������־������
            loadDiaryData(selectedDiaryId);
        }
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
        // �����ݿ������־����
        DiaryEntry diaryEntry = diaryDAO.getDiaryById(diaryId);

        if (diaryEntry != null) {
            titleField.setText(diaryEntry.getTitle());
            textArea.setText(diaryEntry.getContent());
            lastPublishedLabel.setText("����޸�ʱ�䣺"+diaryEntry.getLastCommitTime());
        }
    }

    private void publishLog() {
        String logTitle = titleField.getText();
        String logContent = textArea.getText();
        DiaryDAO diaryDAO = new DiaryDAO(connection);
        int userId = diaryDAO.getUserIdByUsername(username);

        // ������½���־���Ǹ���������־
        if (selectedDiaryId == -1) {
            // ��������־
            String currentTime = new Timestamp(System.currentTimeMillis()).toString();
            DiaryEntry newDiaryEntry = new DiaryEntry(
                    -1,
                    userId,
                    logTitle,
                    logContent,
                    currentTime, // ����ʱ��
                    currentTime  // ������ʱ��
            );
            diaryDAO.addDiary(newDiaryEntry);
            JOptionPane.showMessageDialog(this, "��־�Ѵ���");
            lastPublishedLabel.setText("����޸�ʱ�䣺"+newDiaryEntry.getLastCommitTime());
        } else {
            // ����������־
            DiaryEntry existingDiaryEntry = new DiaryEntry(
                    selectedDiaryId, // ������־��ID
                    userId,
                    logTitle,
                    logContent,
                    null,
                    new Timestamp(System.currentTimeMillis()).toString() // ����������ʱ��
            );
            diaryDAO.updateDiary(existingDiaryEntry);
            JOptionPane.showMessageDialog(this, "��Ǹ��Ϊȷ����˽��ȫ����ʹ����ԱҲ�޷��޸���־��");
            lastPublishedLabel.setText("����޸�ʱ�䣺"+existingDiaryEntry.getLastCommitTime());
        }

    }

    private void deleteLog() throws SQLException {
        if (selectedDiaryId != -1) {
            int confirm = JOptionPane.showConfirmDialog(this, "ȷ��Ҫɾ����ƪ��־��?", "ȷ��", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                diaryDAO.deleteDiary(selectedDiaryId);
                JOptionPane.showMessageDialog(this, "��־��ɾ��");
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
            JOptionPane.showMessageDialog(this, "û��ѡ������־����ɾ��");
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
