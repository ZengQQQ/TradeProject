package cn.bjut.jdbc;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;

public class login extends JFrame {
    private JTextField textField1;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private Font fort = new Font("微软雅黑", Font.BOLD, 16);

    public login() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        initComponents();
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    }

    private void button1MouseClicked(MouseEvent e) {
        String logname = textField1.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (role != null) {
            JDialog loadingDialog = new JDialog(this, "正在加载中...");
            loadingDialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            loadingDialog.setSize(210, 210);
            loadingDialog.setLocationRelativeTo(this);
            loadingDialog.setModal(true);
            URL imageURL = login.class.getResource("/img/load.gif");
            String imagePath = imageURL.getPath();
            ImageIcon gif = new ImageIcon(imagePath);
            JLabel imageLabel = new JLabel(gif);

            loadingDialog.getContentPane().add(imageLabel);
            SwingWorker<Void, Void> loginWorker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    DataControl dataControl = new DataControl();
                    if (role.equals("用户")) {
                        if (password.equals(dataControl.getUserPsw(logname))) {
                            JOptionPane.showMessageDialog(null, "登录成功", "登录", JOptionPane.INFORMATION_MESSAGE);
                            UserFrm userFrame = new UserFrm(dataControl.getUserid(logname));
                            userFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            userFrame.setSize(400, 300);
                            userFrame.setVisible(true);
                            dispose();
                        } else {
                            loadingDialog.dispose();
                            JOptionPane.showMessageDialog(null, "登录失败，请检查账户和密码", "输入验证错误", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (role.equals("商家")) {
                        if (password.equals(dataControl.getMerchantPsw(logname)) && !password.isEmpty()) {
                            JOptionPane.showMessageDialog(null, "登录成功", "登录", JOptionPane.INFORMATION_MESSAGE);
                            MerchantInterFrm merchantFrame = new MerchantInterFrm(dataControl.getMerchantm_id(logname));
                            merchantFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            merchantFrame.setSize(1200, 1000);
                            merchantFrame.setVisible(true);
                            dispose();
                        } else {
                            loadingDialog.dispose();
                            JOptionPane.showMessageDialog(null, "登录失败，请检查账户和密码", "输入验证错误", JOptionPane.ERROR_MESSAGE);
                        }
                    } else if (role.equals("管理员")) {
                        if (password.equals(dataControl.getAdminPsw(logname))) {
                            JOptionPane.showMessageDialog(null, "登录成功", "登录", JOptionPane.INFORMATION_MESSAGE);
                            AdminFrame adminFrame = new AdminFrame();
                            adminFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            adminFrame.setSize(400, 300);
                            adminFrame.setVisible(true);
                            dispose();
                        } else {
                            loadingDialog.dispose();
                            JOptionPane.showMessageDialog(null, "登录失败，请检查账户和密码", "输入验证错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                    return null;
                }

                @Override
                protected void done() {
                    loadingDialog.dispose();
                }
            };
            loginWorker.execute();
            loadingDialog.setVisible(true);
        }
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("登录");
        JPanel backgroundPanel = new AnimatedBackgroundPanel("/img/login.gif");

        setContentPane(backgroundPanel);
        JPanel loginPanel = new JPanel(new GridBagLayout());
        loginPanel.setBackground(Color.white);
        loginPanel.setPreferredSize(new Dimension(200, 100));
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(5, 5, 5, 5);
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(loginPanel, BorderLayout.EAST);
        loginPanel.setOpaque(true);

        JLabel label1 = new JLabel("账户名:");
        label1.setFont(fort);
        constraints.gridx = 0;
        constraints.gridy = 0;
        loginPanel.add(label1, constraints);

        textField1 = new JTextField();
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(textField1, constraints);

        JLabel label2 = new JLabel("密码:");
        label2.setFont(fort);
        constraints.gridx = 0;
        constraints.gridy = 1;
        loginPanel.add(label2, constraints);

        passwordField = new JPasswordField();
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(passwordField, constraints);

        JLabel label3 = new JLabel("身份:");
        label3.setFont(fort);
        constraints.gridx = 0;
        constraints.gridy = 2;
        loginPanel.add(label3, constraints);

        roleComboBox = new JComboBox<>(new String[]{"用户", "商家", "管理员"});
        roleComboBox.setFont(fort);
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        loginPanel.add(roleComboBox, constraints);

        JButton button1 = new JButton("登录");
        button1.setFont(fort);
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button1MouseClicked(e);
            }
        });
        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        loginPanel.add(button1, constraints);


        pack();
    }

    private static class AnimatedBackgroundPanel extends JPanel {
        private ImageIcon background;

        public AnimatedBackgroundPanel(String gifPath) {
            this.background = new ImageIcon(getClass().getResource(gifPath));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Display the animated GIF
            background.paintIcon(this, g, 0, 0);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            login login;
            try {
                login = new login();
            } catch (UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            login.setLocationRelativeTo(null); // 居中显示
            login.setSize(900, 600);
            login.setVisible(true);
        });
    }
}