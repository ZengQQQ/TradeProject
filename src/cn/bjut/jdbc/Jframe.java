package cn.bjut.jdbc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author 郭yw   用户进入首页内容，未完成
 */
public class Jframe extends JFrame {
    private JButton searchButton;
    private JButton homeButton;
    private JButton dynamicButton;
    private JButton shoppingButton;
    private JButton myButton;

    public Jframe() {
        initComponents();
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();  // 添加一个新的面板
        CardLayout cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);  // 将CardLayout应用于主面板

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
        contentPane.setLayout(new MigLayout(
                "hidemode 3",
                "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]",
                "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

        searchButton = new JButton("搜索");
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card5");  // 切换到card5
            }
        });
        contentPane.add(searchButton, "cell 4 0");

        homeButton = new JButton("首页");
        homeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card1");  // 切换到card1
            }
        });
        contentPane.add(homeButton, "cell 2 8");

        dynamicButton = new JButton("动态");
        dynamicButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card2");  // 切换到card2
            }
        });
        contentPane.add(dynamicButton, "cell 4 8");

        shoppingButton = new JButton("购物车");
        shoppingButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card3");  // 切换到card3
            }
        });
        contentPane.add(shoppingButton, "cell 6 8");

        myButton = new JButton("我的");
        myButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card4");  // 切换到card4
            }
        });
        contentPane.add(myButton, "cell 8 8");

        contentPane.add(mainPanel, "dock center");  // 将主面板添加到内容面板中

        pack();
        setLocationRelativeTo(getOwner());
    }
    public static void main(String[] args) {
        Jframe frame = new Jframe();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
