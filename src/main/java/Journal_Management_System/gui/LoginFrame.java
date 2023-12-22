package main.java.Journal_Management_System.gui;

import main.java.Journal_Management_System.dao.UserDAO;
import main.java.Journal_Management_System.server.ClientHandler;
import main.java.Journal_Management_System.util.DatabaseConnection;
import main.java.Journal_Management_System.util.Request;
import main.java.Journal_Management_System.util.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister, btnReset;
    private JLabel lblAvatar;
    private static final int serverPort = 9999; // 替换为您的服务器监听的端口
    Connection connection= DatabaseConnection.getCon();
    UserDAO UserDAO=new UserDAO(connection);

    public LoginFrame() throws IOException, SQLException {
        setTitle("认证 - 个人日志管理系统");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // 窗口居中
        setLayout(new BorderLayout(10, 10)); // 使用BorderLayout

        lblAvatar = new JLabel();
        lblAvatar.setHorizontalAlignment(JLabel.CENTER);
        lblAvatar.setBorder(new EmptyBorder(100, 0, 0, 0)); // 上边距为10像素

        // 创建并添加头像标签到北区
        BufferedImage originalImage = ImageIO.read(new File("src/main/java/resources/imges/avatar.jpg")); // 加载原始图片
        // 调整头像大小
        Image scaledImage = originalImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH); // 缩放图片
        lblAvatar.setIcon(new ImageIcon(scaledImage));
        add(lblAvatar, BorderLayout.NORTH);

        // 创建中央面板放置登录表单
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);

        // 用户名输入框
        constraints.gridx = 0;
        constraints.gridy = 0;
        centerPanel.add(new JLabel("用户名:"), constraints);
        txtUsername = new JTextField(20);
        constraints.gridx = 1;
        centerPanel.add(txtUsername, constraints);

        // 密码输入框
        constraints.gridx = 0;
        constraints.gridy = 1;
        centerPanel.add(new JLabel("密码:"), constraints);
        txtPassword = new JPasswordField(20);
        constraints.gridx = 1;
        centerPanel.add(txtPassword, constraints);

        // 登录按钮
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        btnLogin = new JButton("登录");
        centerPanel.add(btnLogin, constraints);

        add(centerPanel, BorderLayout.CENTER);

        // 创建南区面板放置其他按钮
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnRegister = new JButton("注册");
        btnReset = new JButton("重置");
        southPanel.add(btnRegister);
        southPanel.add(btnReset);
        add(southPanel, BorderLayout.SOUTH);

        // 事件监听器
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                Request request = new Request();
                request.setRequestType("login");
                request.addData("username", username);
                request.addData("password", password);

                try {
                    ClientHandler clientHandler = new ClientHandler("localhost", serverPort);
                    clientHandler.sendRequest(request);
                    Response response = clientHandler.receiveResponse();

                    if (response.getStatusCode() == 200) {
                        String role = (String) response.getData().get("role");
                        String avatarPath= UserDAO.getAvatarPath(username);
                        if ("admin".equals(role)) {
                            // 打开 AdminDashboard
                            showWelcomeDialog(username,avatarPath);
                           new AdminDashboard(username).setVisible(true);
                        } else {
                            // 打开 UserDashboard
                            showWelcomeDialog(username,avatarPath);
                            new UserDashboard(username).setVisible(true);
                        }
                        dispose(); // 关闭登录窗口
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, response.getMessage());
                    }

                    clientHandler.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "无法连接至服务器！");
                    ex.printStackTrace();
                }
            }
        });
        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = txtUsername.getText();
                String password = new String(txtPassword.getPassword());

                Request request = new Request();
                request.setRequestType("register");
                request.addData("username", username);
                request.addData("password", password);

                try {
                    ClientHandler clientHandler = new ClientHandler("localhost", serverPort);
                    clientHandler.sendRequest(request);
                    Response response = clientHandler.receiveResponse();

                    if (response.getStatusCode() == 200) {
                        JOptionPane.showMessageDialog(LoginFrame.this, "注册成功！");
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, response.getMessage());
                    }

                    clientHandler.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "无法连接至服务器！");
                    ex.printStackTrace();
                }
            }
        });
        txtUsername.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String username = txtUsername.getText();
                String avatarPath = UserDAO.getAvatarPath(username);
                updateAvatar(avatarPath);
            }
        });

    }
    private void updateAvatar(String avatarPath) {
        if (avatarPath != null && !avatarPath.isEmpty()) {
            try {
                BufferedImage avatarImage = ImageIO.read(new File(avatarPath));
                Image scaledImage = avatarImage.getScaledInstance(150, 150, Image.SCALE_SMOOTH);
                lblAvatar.setIcon(new ImageIcon(scaledImage));
            } catch (IOException ex) {
                lblAvatar.setIcon(null);
                lblAvatar.setText("No avatar");
            }
        } else {
            lblAvatar.setIcon(null);
            lblAvatar.setText("No avatar");
        }
    }

    private void showWelcomeDialog(String userName, String avatarPath) {
        JDialog welcomeDialog = new JDialog(this, "欢迎", true);
        welcomeDialog.setLayout(new BorderLayout());
        welcomeDialog.setSize(300, 200);
        welcomeDialog.setLocationRelativeTo(null);

        JLabel welcomeLabel = new JLabel("欢迎, " + userName + "!", JLabel.CENTER);
        welcomeDialog.add(welcomeLabel, BorderLayout.NORTH);

        ImageIcon icon = (avatarPath != null) ? new ImageIcon(avatarPath) : new ImageIcon(/* 默认头像路径 */);
        JLabel imageLabel = new JLabel(new ImageIcon(icon.getImage().getScaledInstance(100, 100, Image.SCALE_DEFAULT)));
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        welcomeDialog.add(imageLabel, BorderLayout.CENTER);
        JButton okButton = new JButton("确定");
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
            try {
                new LoginFrame().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
