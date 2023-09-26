package cn.bjut.jdbc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author 郭yw          未完成，用户登录页面
 */

public class login extends JFrame {
    private JTextField textField1;
    private JPasswordField passwordField;

    public login() {
        initComponents();
    }

    private void button1MouseClicked(MouseEvent e) {
        String username = textField1.getText();
        String password = new String(passwordField.getPassword());

        // 进行用户名和密码验证逻辑，这里假设用户名为"admin"，密码为"password"
        if (username.equals("admin") && password.equals("password")) {
            JOptionPane.showMessageDialog(this, "登录成功");

            // 创建并显示第二个窗口
            Jframe mainFrame = new Jframe();
            mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            mainFrame.setSize(400, 300);
            mainFrame.setVisible(true);
            // 关闭当前登录窗口
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "登录失败，请检查用户名和密码");
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login");

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(3, 2, 5, 5));

        JLabel label1 = new JLabel("Username:");
        contentPane.add(label1);

        textField1 = new JTextField();
        contentPane.add(textField1);

        JLabel label2 = new JLabel("Password:");
        contentPane.add(label2);

        passwordField = new JPasswordField();
        contentPane.add(passwordField);

        JButton button1 = new JButton("Login");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button1MouseClicked(e);
            }
        });
        contentPane.add(button1);

        pack();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                login login = new login();
                login.setSize(500,400);
                login.setVisible(true);
            }
        });
    }
}
