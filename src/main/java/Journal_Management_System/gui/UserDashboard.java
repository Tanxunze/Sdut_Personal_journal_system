package main.java.Journal_Management_System.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

class ImagePanel2 extends JPanel {
    private Image image;

    public ImagePanel2(String imagePath) {
        image = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}

public class UserDashboard extends JFrame {
    private DefaultTableModel diaryModel; // 用于存储日志信息的表格模型
    private JTable table; // 作为成员变量声明

    public UserDashboard() {
        createUI();
        createDiaryModel();
        showWelcomeDialog("用户名", "src/main/java/resources/imges/avatar.jpg");
    }

    private void createUI() {
        setTitle("用户主页");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton viewDiariesButton = new JButton("查看我的日志");
        viewDiariesButton.addActionListener(e -> viewAllDiaries());

        JButton publishDiaryButton = new JButton("发布日志");
        publishDiaryButton.addActionListener(e -> showPublishDialog());

        JPanel topPanel = new JPanel();
        topPanel.add(viewDiariesButton);
        topPanel.add(publishDiaryButton);

        ImagePanel2 imagePanel = new ImagePanel2("src/main/java/resources/imges/BackGround_Dashboard.jpg");
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(imagePanel, BorderLayout.CENTER);
    }

    private void createDiaryModel() {
        String[] columnNames = {"标题", "内容", "创建时间", "最后发表时间"};

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
    }


    private void viewAllDiaries() {
        JDialog diariesDialog = new JDialog(this, "所有用户日志", true);
        diariesDialog.setLayout(new BorderLayout());
        diariesDialog.setSize(800, 600);
        diariesDialog.setLocationRelativeTo(this);

        JButton editButton = new JButton("编辑日志");
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // 打开编辑日志的窗口或对话框
                editDiary(selectedRow);
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

    private void editDiary(int selectedRow) {
        // TODO: 实现编辑日志的逻辑
        // 例如，打开一个新的窗口或对话框，用于编辑选中的日志
    }

    private void showPublishDialog() {
        JDialog publishDialog = new JDialog(this, "发布日志", true);
        publishDialog.setLayout(new BorderLayout());
        publishDialog.setSize(400, 300);
        publishDialog.setLocationRelativeTo(this);

        JTextArea contentArea = new JTextArea();
        JButton publishButton = new JButton("发布");

        publishButton.addActionListener(e -> {
            String content = contentArea.getText();
            String title = "日志标题"; // 这里可以添加一个标题输入框或以其他方式获取
            String createTime = "创建时间"; // 获取当前时间
            String lastPublishTime = "最后发表时间"; // 获取当前时间
            diaryModel.addRow(new Object[]{title, content, createTime, lastPublishTime});
            publishDialog.dispose();
        });

        publishDialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        publishDialog.add(publishButton, BorderLayout.SOUTH);

        publishDialog.setVisible(true);
    }

    private void showWelcomeDialog(String userName, String imagePath) {
        JDialog welcomeDialog = new JDialog(this, "欢迎", true);
        welcomeDialog.setLayout(new BorderLayout());
        welcomeDialog.setSize(300, 200);
        welcomeDialog.setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("欢迎, " + userName + "!", JLabel.CENTER);
        welcomeDialog.add(welcomeLabel, BorderLayout.NORTH);

        ImageIcon icon = new ImageIcon(imagePath);
        JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeDialog.add(imageLabel, BorderLayout.CENTER);

        welcomeDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserDashboard userDashboard = new UserDashboard();
//            userDashboard.showWelcomeDialog("用户名", "src/main/java/resources/imges/avatar.jpg");
            userDashboard.setVisible(true);
        });
    }
}
