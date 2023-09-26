package cn.bjut.jdbc;

import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import javax.swing.*;

public class login extends JFrame {
    private JTextField textField1;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;

    public login() {
        initComponents();
    }

    private void button1MouseClicked(MouseEvent e) throws SQLException {
        String username = textField1.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();


        if (role != null) {
            if (role.equals("用户")) {
                if (username.equals("user") && password.equals("userpassword")) {
                    JOptionPane.showMessageDialog(this, "用户登录成功");
                    UserFrm userFrame = new UserFrm();
                    userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    userFrame.setSize(400, 300);
                    userFrame.setVisible(true);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(this, "用户登录失败，请检查用户名和密码");
                }
            } else if (role.equals("商家")) {
                DataControl dataControl = new DataControl();
                String merchantPsw = dataControl.getMerchantPsw(username);//得到商家密码
                if (password.equals(merchantPsw)) {
                    JOptionPane.showMessageDialog(this, "商家登录成功");
                    MerchantInterFrm merchantFrame = new MerchantInterFrm();
                    merchantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    merchantFrame.setSize(400, 300);
                    merchantFrame.setVisible(true);
                    dispose();

                    } else {
                        JOptionPane.showMessageDialog(this, "商家登录失败，请检查用户名和密码");
                    }
                } else if (role.equals("管理员")) {
                    if (username.equals("admin") && password.equals("adminpassword")) {
                        JOptionPane.showMessageDialog(this, "管理员登录成功");
                        // AdminFrame adminFrame = new AdminFrame();
                        // adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        // adminFrame.setSize(400, 300);
                        // adminFrame.setVisible(true);
                        // dispose();
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
        contentPane.setLayout(new GridBagLayout()); // 使用GridBagLayout布局管理器

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5); // 设置组件之间的间距

        JLabel label1 = new JLabel("Username:");
        constraints.gridx = 0; // 设置组件所在的列
        constraints.gridy = 0; // 设置组件所在的行
        contentPane.add(label1, constraints);

        textField1 = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL; // 横向拉伸
        contentPane.add(textField1, constraints);

        JLabel label2 = new JLabel("Password:");
        constraints.gridx = 0;
        constraints.gridy = 1;
        contentPane.add(label2, constraints);

        passwordField = new JPasswordField();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(passwordField, constraints);

        JLabel label3 = new JLabel("Role:");
        constraints.gridx = 0;
        constraints.gridy = 2;
        contentPane.add(label3, constraints);

        roleComboBox = new JComboBox<>(new String[]{"用户", "商家", "管理员"});
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        contentPane.add(roleComboBox, constraints);

        JButton button1 = new JButton("Login");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    button1MouseClicked(e);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2; // 横向占据两列
        constraints.anchor = GridBagConstraints.CENTER; // 居中对齐
        contentPane.add(button1, constraints);

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
