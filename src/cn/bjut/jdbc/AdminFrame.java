package cn.bjut.jdbc;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
public class AdminFrame extends JFrame {
    private CardLayout cardLayout;
    private JPanel cardPanel;
    public AdminFrame() throws SQLException {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultFont();
    }

    private void setDefaultFont() {
        Font font = new Font("微软雅黑", Font.PLAIN, 18);

        for (Component component : getComponents()) {
            setComponentFont(component, font);
        }
    }

    private void setComponentFont(Component component, Font font) {
        if (component instanceof JTextArea || component instanceof JLabel || component instanceof JButton) {
            component.setFont(font);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setComponentFont(child, font);
            }
        }
    }

    private void initComponents() throws SQLException {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        DataControl data = new DataControl();
        setSize(600,450);


        JMenuBar menuBar = new JMenuBar();
        // 创建菜单
        JMenu viewMenu = new JMenu("菜单");
        // 创建菜单项
        JMenuItem viewUsersItem = new JMenuItem("查看用户");
        JMenuItem viewMerchantsItem = new JMenuItem("查看商家");
        JMenuItem exitItem = new JMenuItem("退出");
        JMenuItem logoutItem = new JMenuItem("注销");

    // 添加菜单项到菜单
            viewMenu.add(viewUsersItem);
            viewMenu.add(viewMerchantsItem);
            viewMenu.addSeparator(); // 添加分隔线
            viewMenu.add(logoutItem);
            viewMenu.add(exitItem);
            // 添加菜单到菜单栏
            menuBar.add(viewMenu);

            // 设置菜单栏到框架
            this.setJMenuBar(menuBar);
//用户------------------------------------------------------------------
        AdminUserFrame adminuser = new AdminUserFrame(this,data);
        // 用户管理页
        viewUsersItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 添加查看用户的逻辑
             cardLayout.show(cardPanel,"card1");
                JOptionPane.showMessageDialog(AdminFrame.this, "查看用户功能");
            }
        });
//商家------------------------------------------------------------------
        AdminMerchantFrame adminMerchantFrame = new AdminMerchantFrame(this,data);
        // 商家管理页
        viewMerchantsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 添加查看商家的逻辑
                cardLayout.show(cardPanel,"card3");
                JOptionPane.showMessageDialog(AdminFrame.this, "查看商家功能");
            }
        });


        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        pack();


        cardPanel.add(adminuser, "card1");
        cardPanel.add(adminMerchantFrame, "card3");

        exitItem.addActionListener(e -> {
            System.exit(0);
        });
        logoutItem.addActionListener(e -> {
            AdminFrame.this.dispose();
            SwingUtilities.invokeLater(() -> {
                login login;
                try {
                    login = new login();
                } catch (UnsupportedLookAndFeelException | InstantiationException | ClassNotFoundException |
                         IllegalAccessException | SQLException x) {
                    throw new RuntimeException(x);
                }
                login.setVisible(true);
            });
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                AdminFrame frame = null;
                try {
                    frame = new AdminFrame();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                frame.setVisible(true);
            }
        });
    }
}
