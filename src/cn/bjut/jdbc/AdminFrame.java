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
    }

    private void initComponents() throws SQLException {
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        JPanel buttonpane = new JPanel();
        buttonpane.setLayout(new FlowLayout());
        DataControl data = new DataControl();
        setSize(600,450);
//用户------------------------------------------------------------------
        AdminUserFrame adminuser = new AdminUserFrame(this,data);
        // 用户管理页
        JButton viewUsersButton = new JButton("查看用户");
        buttonpane.add(viewUsersButton);
        viewUsersButton.addActionListener(new ActionListener() {
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
        JButton viewMerchantButton = new JButton("查看商家");
        buttonpane.add(viewMerchantButton);
        viewMerchantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 添加查看商家的逻辑
                cardLayout.show(cardPanel,"card3");
                JOptionPane.showMessageDialog(AdminFrame.this, "查看商家功能");
            }
        });

        JPanel card2 = new JPanel();
        // 论坛评论页
        JButton viewCommentsButton = new JButton("查看论坛");
        buttonpane.add(viewCommentsButton);
        viewCommentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 添加查看评论的逻辑
                cardLayout.show(cardPanel,"card2");
                JOptionPane.showMessageDialog(AdminFrame.this, "查看评论功能");
            }
        });

        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.CENTER);
        add(buttonpane,BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        pack();


        cardPanel.add(adminuser, "card1");
        cardPanel.add(card2, "card2");
        cardPanel.add(adminMerchantFrame, "card3");
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
