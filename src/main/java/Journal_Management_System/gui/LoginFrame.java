package main.java.Journal_Management_System.gui;

import main.java.Journal_Management_System.server.ClientHandler;
import main.java.Journal_Management_System.server.Server;
import main.java.Journal_Management_System.util.Request;
import main.java.Journal_Management_System.util.Response;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister, btnReset;
    private JLabel lblAvatar;
    private static final int serverPort = 9999; // 替换为您的服务器监听的端口

    public LoginFrame() throws IOException {
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
        //TODO: 后端传入头像
//        txtUsername.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                String username = txtUsername.getText();
//                String avatarPath = getAvatarPathByUsername(username);
//
//                if (avatarPath != null) {
//                    // 加载并显示头像
//                    try {
//                        BufferedImage avatarImage = ImageIO.read(new File(avatarPath));
//                        Image scaledImage = avatarImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
//                        lblAvatar.setIcon(new ImageIcon(scaledImage));
//                    } catch (IOException ex) {
//                        lblAvatar.setText("Avatar not found");
//                    }
//                } else {
//                    lblAvatar.setIcon(null); // 如果没有头像，清除之前的显示
//                    lblAvatar.setText("No avatar");
//                }
//            }
//        });


        // 创建中央面板放置登录表单
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(10, 10, 10, 10);

        // 用户名输入框
        constraints.gridx = 0;
        constraints.gridy = 0;
        centerPanel.add(new JLabel("Username:"), constraints);
        txtUsername = new JTextField(20);
        constraints.gridx = 1;
        centerPanel.add(txtUsername, constraints);

        // 密码输入框
        constraints.gridx = 0;
        constraints.gridy = 1;
        centerPanel.add(new JLabel("Password:"), constraints);
        txtPassword = new JPasswordField(20);
        constraints.gridx = 1;
        centerPanel.add(txtPassword, constraints);

        // 登录按钮
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.gridwidth = 2;
        btnLogin = new JButton("Login");
        centerPanel.add(btnLogin, constraints);

        add(centerPanel, BorderLayout.CENTER);

        // 创建南区面板放置其他按钮
        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnRegister = new JButton("Register");
        btnReset = new JButton("Reset");
        southPanel.add(btnRegister);
        southPanel.add(btnReset);
        add(southPanel, BorderLayout.SOUTH);

        // 事件监听器
        // TODO: 添加按钮的事件处理逻辑
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
                        if ("admin".equals(role)) {
                            // 打开 AdminDashboard
                           //new AdminDashboard().setVisible(true);
                        } else {
                            // 打开 UserDashboard
                            //new UserDashboard().setVisible(true);
                        }
                        dispose(); // 关闭登录窗口
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, response.getMessage());
                    }

                    clientHandler.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Error connecting to the server");
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
                        JOptionPane.showMessageDialog(LoginFrame.this, "Registration successful");
                    } else {
                        JOptionPane.showMessageDialog(LoginFrame.this, response.getMessage());
                    }

                    clientHandler.close();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(LoginFrame.this, "Error connecting to the server");
                    ex.printStackTrace();
                }
            }
        });


    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new LoginFrame().setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
