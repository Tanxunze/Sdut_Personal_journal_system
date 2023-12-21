package main.java.Journal_Management_System.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;


class ImagePanel extends JPanel {
    private Image image;

    public ImagePanel(String imagePath) {
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

    public AdminDashboard() {
        createUI();
        createDiaryModel();
    }

    private void createUI() {
        setTitle("管理员主页");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JButton viewDiariesButton = new JButton("查看所有用户日志");
        viewDiariesButton.addActionListener(e -> viewAllDiaries());

        JButton publishDiaryButton = new JButton("发布日志");
        publishDiaryButton.addActionListener(e -> showPublishDialog());

        JPanel topPanel = new JPanel();
        topPanel.add(viewDiariesButton);
        topPanel.add(publishDiaryButton);
        // 添加带图片的面板
        ImagePanel imagePanel = new ImagePanel("src/main/java/resources/imges/BackGround_Dashboard.jpg"); // 替换为图片的路径
        getContentPane().add(topPanel, BorderLayout.NORTH);
        getContentPane().add(imagePanel, BorderLayout.CENTER);



        getContentPane().add(topPanel, BorderLayout.NORTH);
    }

    private void createDiaryModel() {
        String[] columnNames = {"用户名", "标题", "内容", "创建账户时间", "最后发表时间"};
        diaryModel = new DefaultTableModel(columnNames, 0);
    }

    private void viewAllDiaries() {
        JDialog diariesDialog = new JDialog(this, "所有用户日志", true);
        diariesDialog.setLayout(new BorderLayout());
        diariesDialog.setSize(800, 600);
        diariesDialog.setLocationRelativeTo(this);

        // 创建编辑按钮并添加事件监听器
        JButton editButton = new JButton("编辑日志");
        editButton.addActionListener(e -> {
            // TODO: 实现跳转到编辑页面的逻辑
            // 例如打开一个新的JDialog或JFrame
            new JournalEditor().setVisible(true);
        });

        // 将按钮添加到顶部面板
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.add(editButton);

        JTable table = new JTable(diaryModel);
        table.getTableHeader().setResizingAllowed(false);
        table.getTableHeader().setReorderingAllowed(false);

        diariesDialog.add(topPanel, BorderLayout.NORTH);
        diariesDialog.add(new JScrollPane(table), BorderLayout.CENTER);
        diariesDialog.setVisible(true);
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
            String username = "当前用户名"; // 示例，替换为实际用户名
            String title = "日志标题"; // 可以添加一个标题输入框或以其他方式获取
            String createTime = "创建时间"; // 获取当前时间
            String lastPublishTime = "最后发表时间"; // 获取当前时间

            diaryModel.addRow(new Object[]{username, title, content, createTime, lastPublishTime});
            publishDialog.dispose();
        });

        publishDialog.add(new JScrollPane(contentArea), BorderLayout.CENTER);
        publishDialog.add(publishButton, BorderLayout.SOUTH);

        publishDialog.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminDashboard adminFrame = new AdminDashboard();
            adminFrame.setVisible(true);
        });
    }
}
