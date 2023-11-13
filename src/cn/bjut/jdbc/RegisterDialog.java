package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class RegisterDialog extends JDialog {
    private JTextField registerUserAccountField;
    private JTextField registerUsernameField;
    private JPasswordField registerPasswordField;
    private JComboBox<String> registerRoleComboBox;
    private JTextField registersexField;
    private JTextField registerteleField;

    public RegisterDialog(Frame owner) {
        super(owner, "注册", true);
        initComponents();
        setLocationRelativeTo(owner);
    }

    private void initComponents() {
        JLabel backgroundLabel = new JLabel(new ImageIcon(getClass().getResource("/img/welcome.jpg")));
        backgroundLabel.setLayout(new BoxLayout(backgroundLabel, BoxLayout.Y_AXIS));

        JPanel registerPanel = new JPanel();
        GroupLayout layout = new GroupLayout(registerPanel);
        registerPanel.setLayout(layout);
        registerPanel.setOpaque(false);

        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>欢迎来到商品交易市场！填写以下信息开始注册！</div></html>");
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));

        JPanel welcomePanel = new JPanel(new BorderLayout());
        welcomePanel.add(welcomeLabel, BorderLayout.CENTER);
        welcomePanel.setOpaque(false);

        JLabel label1 = new JLabel("账户名:");
        label1.setFont(new Font("微软雅黑", Font.BOLD, 16));

        registerUserAccountField = new JTextField();

        JLabel label6 = new JLabel("昵称:");
        label6.setFont(new Font("微软雅黑", Font.BOLD, 16));

        registerUsernameField = new JTextField();

        JLabel label2 = new JLabel("密码:");
        label2.setFont(new Font("微软雅黑", Font.BOLD, 16));

        registerPasswordField = new JPasswordField();

        JLabel label3 = new JLabel("身份:");
        label3.setFont(new Font("微软雅黑", Font.BOLD, 16));

        registerRoleComboBox = new JComboBox<>(new String[]{"用户", "商家", "管理员"});
        registerRoleComboBox.setFont(new Font("微软雅黑", Font.BOLD, 16));

        JLabel label4 = new JLabel("性别:");
        label4.setFont(new Font("微软雅黑", Font.BOLD, 16));

        registersexField = new JTextField();

        JLabel label5 = new JLabel("电话号码:");
        label5.setFont(new Font("微软雅黑", Font.BOLD, 16));

        registerteleField = new JTextField();

        JButton registerButton = new JButton("注册");
        registerButton.setFont(new Font("微软雅黑", Font.BOLD, 16));
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    DataControl dataControl = new DataControl();
                    String useraccount = registerUserAccountField.getText();
                    String username = registerUsernameField.getText();
                    String password = new String(registerPasswordField.getPassword());
                    String role = (String) registerRoleComboBox.getSelectedItem();
                    String sex = registersexField.getText();
                    String tele = registerteleField.getText();


                    if (useraccount.isEmpty() || sex.isEmpty() || tele.isEmpty() || password.isEmpty()) {
                        JOptionPane.showMessageDialog(null, "所有字段都必须填写", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!Pattern.matches("[\\u4e00-\\u9fa5a-zA-Z]{1,20}", useraccount) || !Pattern.matches("[\\u4e00-\\u9fa5a-zA-Z]{1,20}", username)) {
                        JOptionPane.showMessageDialog(null, "账户和昵称只能为字母、中文和字符，且长度不超过20", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!Pattern.matches("[男女]", sex)) {
                        JOptionPane.showMessageDialog(null, "性别只能为男或女", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!Pattern.matches("^[1-9]\\d{10}$", tele)) {
                        JOptionPane.showMessageDialog(null, "电话号码必须为有效的11位数字", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    if (!Pattern.matches("[\\u4e00-\\u9fa5a-zA-Z0-9]{1,15}", password)) {
                        JOptionPane.showMessageDialog(null, "密码只能为字母、中文、字符和数字，且长度不超过15", "错误", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    boolean flag = dataControl.register(useraccount, password, username, role, sex, tele);
                    if (flag) {
                        JOptionPane.showMessageDialog(null, "注册成功", "注册", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "注册失败", "注册", JOptionPane.INFORMATION_MESSAGE);
                    }
                    dispose();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        GroupLayout.SequentialGroup hGroup = layout.createSequentialGroup();
        hGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(label1).addComponent(label6).addComponent(label2).addComponent(label3).addComponent(label4).addComponent(label5));
        hGroup.addGroup(layout.createParallelGroup().addComponent(registerUserAccountField).addComponent(registerUsernameField).addComponent(registerPasswordField).addComponent(registerRoleComboBox).addComponent(registersexField).addComponent(registerteleField).addComponent(registerButton));
        layout.setHorizontalGroup(hGroup);

        GroupLayout.SequentialGroup vGroup = layout.createSequentialGroup();
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label1).addComponent(registerUserAccountField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label6).addComponent(registerUsernameField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label2).addComponent(registerPasswordField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label3).addComponent(registerRoleComboBox));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label4).addComponent(registersexField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(label5).addComponent(registerteleField));
        vGroup.addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).addComponent(registerButton));
        layout.setVerticalGroup(vGroup);

        backgroundLabel.add(welcomePanel, BorderLayout.NORTH);
        backgroundLabel.add(registerPanel, BorderLayout.CENTER);

        setContentPane(backgroundLabel);
        pack();
    }
}

