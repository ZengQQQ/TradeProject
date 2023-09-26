/*
 * Created by JFormDesigner on Tue Sep 26 09:27:07 CST 2023
 * 商家主界面
 */
package cn.bjut.jdbc;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MerchantInterFrm extends JFrame {
    private JButton searchButton;
    private JButton homeButton;
    private JButton dynamicButton;
    private JButton shoppingButton;
    private JButton myButton;

    public MerchantInterFrm() {
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        JPanel card1 = new JPanel();
        card1.add(new JLabel("这是第一个界面"));
        card1.setBackground(Color.RED);

        JPanel card2 = new JPanel();
        card2.add(new JLabel("这是第二个界面"));
        card2.setBackground(Color.GREEN);

        JPanel card3 = new JPanel();
        card3.add(new JLabel("这是第三个界面"));
        card3.setBackground(Color.BLUE);

        JPanel card4 = new JPanel();
        card4.add(new JLabel("这是第四个界面"));
        card4.setBackground(Color.YELLOW);

        JPanel card5 = new JPanel();
        JTextField textField = new JTextField(10);
        textField.setText("请输入搜索内容");
        card5.add(textField);

        mainPanel.add(card1, "card1");
        mainPanel.add(card2, "card2");
        mainPanel.add(card3, "card3");
        mainPanel.add(card4, "card4");
        mainPanel.add(card5, "card5");

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        searchButton = new JButton("搜索");
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card5");
            }
        });

        homeButton = new JButton("首页");
        homeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card1");
            }
        });

        dynamicButton = new JButton("动态");
        dynamicButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card2");
            }
        });

        shoppingButton = new JButton("购物车");
        shoppingButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card3");
            }
        });

        myButton = new JButton("我的");
        myButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card4");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));
        buttonPanel.add(homeButton);
        buttonPanel.add(dynamicButton);
        buttonPanel.add(shoppingButton);
        buttonPanel.add(myButton);

        contentPane.add(searchButton, BorderLayout.NORTH);
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
    }

    public static void main(String[] args) {
        MerchantInterFrm frame = new MerchantInterFrm();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

