package main.java.Journal_Management_System.gui;

import main.java.Journal_Management_System.dao.DiaryDAO;
import main.java.Journal_Management_System.dao.DiaryEntry;
import main.java.Journal_Management_System.dao.UserDAO;
import main.java.Journal_Management_System.util.DatabaseConnection;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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
    private DefaultTableModel diaryModel; // 用于存储日志信息的表格模型
    private JTable table; // 作为成员变量声明
    private String username;
    Connection connection= DatabaseConnection.getCon();
    private UserDAO userDAO=new UserDAO(connection);
    private DiaryDAO diaryDAO=new DiaryDAO(connection);
    public AdminDashboard(String username) throws SQLException {
        createUI();
        createDiaryModel();
        this.username=username;
        String avatarPath= userDAO.getAvatarPath(username);
        //showWelcomeDialog(username, avatarPath);
    }
    private void createUI() {
        setTitle("管理员主页");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton viewDiariesButton = new JButton("查看所有用户日志");
        viewDiariesButton.addActionListener(e -> viewAllDiaries());

        JButton publishDiaryButton = new JButton("发布日志");
        publishDiaryButton.addActionListener(e -> {
            try {
                showPublishDialog();
            } catch (SQLException | IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JPanel topPanel = new JPanel();
        topPanel.add(viewDiariesButton);
        topPanel.add(publishDiaryButton);

        ImagePanel2 imagePanel = new ImagePanel2("src/main/java/resources/images/BackGround_Dashboard.jpg");
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(imagePanel, BorderLayout.CENTER);
    }

    private void createDiaryModel() {
        String[] columnNames = {"id","用户名","标题", "内容", "创建时间", "最后发表时间"};

        diaryModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // 使表格不可编辑
                return false;
            }
        };
        table = new JTable(diaryModel);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(0).setMinWidth(0);
        table.getColumnModel().getColumn(0).setMaxWidth(0);
        table.getColumnModel().getColumn(0).setWidth(0);
    }


    private void viewAllDiaries() {
        JDialog diariesDialog = new JDialog(this, "所有用户日志", true);
        diariesDialog.setLayout(new BorderLayout());
        diariesDialog.setSize(800, 600);
        diariesDialog.setLocationRelativeTo(this);
//        int userId = userDAO.getUserIdByUsername(username);
//        List<DiaryEntry> diaries = diaryDAO.getDiariesByUserId(userId);
//        diaryModel.setRowCount(0); // 清空表格模型的现有数据
//
//        for (DiaryEntry diary : diaries) {
//            diaryModel.addRow(new Object[]{diary.getId(),diary.getTitle(), diary.getContent(), diary.getCreatedTime(), diary.getLastCommitTime()});
//        }
        diaryModel.setRowCount(0);
        List<DiaryEntry> diaries = diaryDAO.getAllDiaries();
        for (DiaryEntry diary : diaries) {
            String username = userDAO.getUsernameById(diary.getUserId());
            diaryModel.addRow(new Object[]{diary.getId(),username, diary.getTitle(), diary.getContent(), diary.getCreatedTime(), diary.getLastCommitTime()});
        }

        JButton editButton = new JButton("编辑日志");
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // 打开编辑日志的窗口或对话框
                try {
                    editDiary(selectedRow);
                } catch (SQLException | IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else {
                JOptionPane.showMessageDialog(diariesDialog, "请选择一条日志进行编辑");
            }
        });

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(editButton);

        diariesDialog.add(topPanel, BorderLayout.NORTH);
        diariesDialog.add(new JScrollPane(table), BorderLayout.CENTER);
        diariesDialog.setVisible(true);
    }

    private void editDiary(int selectedRow) throws SQLException, IOException {
        // TODO: 实现编辑日志的逻辑
        // 例如，打开一个新的窗口或对话框，用于编辑选中的日志
        if (selectedRow != -1) {
            int selectedDiaryId = (Integer) table.getModel().getValueAt(selectedRow, 0); // 0 是日志ID所在的列
            DiaryEntry selectedDiary = diaryDAO.getDiaryById(selectedDiaryId);
            new JournalEditor(username,selectedDiaryId).setVisible(true);
            dispose();
        } else {
            // 没有选中任何行的处理
        }
    }


    private void showPublishDialog() throws SQLException, IOException {
        new JournalEditor(username,-1).setVisible(true);
        dispose();
    }

//    private void showWelcomeDialog(String userName, String avatarPath) {
//        JDialog welcomeDialog = new JDialog(this, "欢迎", true);
//        welcomeDialog.setLayout(new BorderLayout());
//        welcomeDialog.setSize(300, 200);
//        welcomeDialog.setLocationRelativeTo(null);
//
//        JLabel welcomeLabel = new JLabel("欢迎, " + userName + "!", JLabel.CENTER);
//        welcomeDialog.add(welcomeLabel, BorderLayout.NORTH);
//
//        ImageIcon icon = (avatarPath != null) ? new ImageIcon(avatarPath) : new ImageIcon(/* 默认头像路径 */);
//        JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
//        imageLabel.setHorizontalAlignment(JLabel.CENTER);
//        welcomeDialog.add(imageLabel, BorderLayout.CENTER);
//        JButton okButton = new JButton("确定");
//        okButton.setPreferredSize(new Dimension(100, 30)); // Set preferred size
//        okButton.setHorizontalAlignment(SwingConstants.CENTER); // Center the text
//        okButton.setMargin(new Insets(5, 5, 5, 5)); // Set margin for padding
//        okButton.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                welcomeDialog.dispose();
//                setVisible(true); // Show the main UserDashboard frame
//            }
//        });
//
//        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Create a panel for the button
//        buttonPanel.add(okButton);
//        welcomeDialog.add(buttonPanel, BorderLayout.SOUTH); // Add the button panel
//
//        welcomeDialog.setVisible(true);
//    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            //UserDashboard userDashboard = new UserDashboard();
//            userDashboard.showWelcomeDialog("用户名", "src/main/java/resources/images/avatar.jpg");
            //userDashboard.setVisible(true);
        });
    }
}
