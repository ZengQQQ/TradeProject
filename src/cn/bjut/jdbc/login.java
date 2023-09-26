package cn.bjut.jdbc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class login extends JFrame {
    private JTextField textField1;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox; // 添加角色选择下拉菜单

    public login() {
        initComponents();
    }

    private void button1MouseClicked(MouseEvent e) {
        String username = textField1.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (role != null) {
            if (role.equals("用户")) {
                if (username.equals("user") && password.equals("userpassword")) {
                      JOptionPane.showMessageDialog(this, "用户登录成功");
//                    UserFrame userFrame = new UserFrame();
//                    userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    userFrame.setSize(400, 300);
//                    userFrame.setVisible(true);
//                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "用户登录失败，请检查用户名和密码");
                }
            } else if (role.equals("商家")) {
               if (username.equals("merchant") && password.equals("merchantpassword")) {
                     JOptionPane.showMessageDialog(this, "商家登录成功");
//                    MerchantFrame merchantFrame = new MerchantFrame();
//                    merchantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    merchantFrame.setSize(400, 300);
//                    merchantFrame.setVisible(true);
//                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "商家登录失败，请检查用户名和密码");
                }
            } else if (role.equals("管理员")) {
                if (username.equals("admin") && password.equals("adminpassword")) {
                      JOptionPane.showMessageDialog(this, "管理员登录成功");
//                    AdminFrame adminFrame = new AdminFrame();
//                    adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                    adminFrame.setSize(400, 300);
//                    adminFrame.setVisible(true);
//                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "管理员登录失败，请检查用户名和密码");
                }
            }
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Login");

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);
        contentPane.setLayout(new GridLayout(4, 2, 5, 5)); // 增加一行用于放置角色选择

        JLabel label1 = new JLabel("Username:");
        contentPane.add(label1);

        textField1 = new JTextField();
        contentPane.add(textField1);

        JLabel label2 = new JLabel("Password:");
        contentPane.add(label2);

        passwordField = new JPasswordField();
        contentPane.add(passwordField);

        JLabel label3 = new JLabel("Role:"); // 添加角色选择标签
        contentPane.add(label3);

        roleComboBox = new JComboBox<>(new String[]{"用户", "商家", "管理员"}); // 添加角色选择下拉菜单
        contentPane.add(roleComboBox);

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
                login.setSize(500, 400);
                login.setVisible(true);
            }
        });
    }
}
