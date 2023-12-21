package main.java.Journal_Management_System.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class ImagePanel1 extends JPanel {
    private Image image;

    public ImagePanel1(String imagePath) {
        image = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}

public class AdminDashboard extends JFrame {
    private DefaultTableModel diaryModel; // ���ڴ洢��־��Ϣ�ı��ģ��
    private JTable table; // ��Ϊ��Ա��������

    public AdminDashboard() {
        createUI();
        createDiaryModel();
        showWelcomeDialog("�û���", "src/main/java/resources/imges/avatar.jpg");
    }

    private void createUI() {
        setTitle("����Ա��ҳ");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton viewDiariesButton = new JButton("�鿴�����û���־");
        viewDiariesButton.addActionListener(e -> viewAllDiaries());

        JButton publishDiaryButton = new JButton("������־");
        publishDiaryButton.addActionListener(e -> showPublishDialog());

        JPanel topPanel = new JPanel();
        topPanel.add(viewDiariesButton);
        topPanel.add(publishDiaryButton);

        ImagePanel2 imagePanel = new ImagePanel2("src/main/java/resources/imges/BackGround_Dashboard.jpg");
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(imagePanel, BorderLayout.CENTER);
    }

    private void createDiaryModel() {
        String[] columnNames = {"�û���","����", "����", "����ʱ��", "��󷢱�ʱ��"};

        diaryModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // ʹ��񲻿ɱ༭
                return false;
            }
        };

        table = new JTable(diaryModel);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }


    private void viewAllDiaries() {
        JDialog diariesDialog = new JDialog(this, "�ҵ�������־", true);
        diariesDialog.setLayout(new BorderLayout());
        diariesDialog.setSize(800, 600);
        diariesDialog.setLocationRelativeTo(this);

        JButton editButton = new JButton("�༭��־");
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // �򿪱༭��־�Ĵ��ڻ�Ի���
                editDiary(selectedRow);
            } else {
                JOptionPane.showMessageDialog(diariesDialog, "��ѡ��һ����־���б༭");
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(editButton);

        diariesDialog.add(topPanel, BorderLayout.NORTH);
        diariesDialog.add(new JScrollPane(table), BorderLayout.CENTER);
        diariesDialog.setVisible(true);
    }

    private void editDiary(int selectedRow) {
        // TODO: ʵ�ֱ༭��־���߼�
        // ���磬��һ���µĴ��ڻ�Ի������ڱ༭ѡ�е���־
        new JournalEditor().setVisible(true);
        dispose();

    }

    private void showPublishDialog() {
        JDialog publishDialog = new JDialog(this, "������־", true);
        publishDialog.setLayout(new BorderLayout());
        publishDialog.setSize(400, 300);
        publishDialog.setLocationRelativeTo(this);

        JTextArea contentArea = new JTextArea();
        JButton publishButton = new JButton("����");

        publishButton.addActionListener(e -> {
            String content = contentArea.getText();
            String title = "��־����"; // ����������һ��������������������ʽ��ȡ
            String createTime = "����ʱ��"; // ��ȡ��ǰʱ��
            String lastPublishTime = "��󷢱�ʱ��"; // ��ȡ��ǰʱ��
            diaryModel.addRow(new Object[]{title, content, createTime, lastPublishTime});
            publishDialog.dispose();
        });

        publishDialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        publishDialog.add(publishButton, BorderLayout.SOUTH);
        publishDialog.setVisible(true);
    }

    private void showWelcomeDialog(String userName, String imagePath) {
        JDialog welcomeDialog = new JDialog(this, "��ӭ", true);
        welcomeDialog.setLayout(new BorderLayout());
        welcomeDialog.setSize(300, 200);
        welcomeDialog.setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("��ӭ, Administer!", JLabel.CENTER); // Ӳ�����û���
        welcomeDialog.add(welcomeLabel, BorderLayout.NORTH);

        ImageIcon icon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeDialog.add(imageLabel, BorderLayout.CENTER);
        JButton okButton = new JButton("ȷ��");
        okButton.setPreferredSize(new Dimension(100, 30)); // Set preferred size
        okButton.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
        okButton.setMargin(new Insets(5, 5, 5, 5)); // Set margin for padding
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                welcomeDialog.dispose();
                setVisible(true); // Show the main UserDashboard frame
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Create a panel for the button
        buttonPanel.add(okButton);
        welcomeDialog.add(buttonPanel, BorderLayout.SOUTH); // Add the button panel
        welcomeDialog.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard adminDashboard = new AdminDashboard();
//            adminDashboard.showWelcomeDialog("Administer", "src/main/java/resources/imges/avatar.jpg");
            adminDashboard.setVisible(true);
        });

    }
}
