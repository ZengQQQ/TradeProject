package cn.bjut.jdbc;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import java.util.HashMap;
/**
 * @author 郭yw          未完成(每个具体页面内容)，用户登录进来之后的首页
 */

public class UserFrm extends JFrame {
    private JButton searchButton;
    private JButton homeButton;
    private JButton dynamicButton;
    private JButton shoppingButton;
    private JButton myButton;

    // 创建一个主面板和一个卡片布局管理器
    private JPanel mainPanel = new JPanel();
    private CardLayout cardLayout = new CardLayout();

    // 创建一个HashMap来存储商品id和对应的卡片对象
    private HashMap<Integer, JPanel> productMap = new HashMap<>();

    public UserFrm() {
        initComponents();
    }

    private void initComponents() {
        // 设置主面板为卡片布局
        mainPanel.setLayout(cardLayout);

        // 创建第一个界面
        // 创建一个网格布局管理器，指定4行6列
        GridLayout gridLayout = new GridLayout (4, 6);
// 设置网格之间的水平和垂直间距
        gridLayout.setHgap (10);
        gridLayout.setVgap (10);
// 创建第一个界面，使用网格布局管理器
        JPanel card1 = new JPanel (gridLayout);

// 连接数据库
        DataBase dataBase=new DataBase();
        dataBase.OpenDB();

// 查询数据库中的商品信息
        Statement stmt = null;
        try {
            stmt = dataBase.getCon().createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query = "SELECT p_id, p_name, p_img ,p_price FROM product";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

// 遍历结果集，为每个商品创建一个按钮，并添加到第一个界面中
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 获取商品的id，名称，图片路径和价格
            int id=0;
            try {
                id = rs.getInt("p_id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String name = null;
            try {
                name = rs.getString("p_name");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String imagePath = null;
            try {
                imagePath = rs.getString("p_img");
                System.out.println(imagePath);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            double price = 0;
            try {
                price = rs.getDouble("p_price");
            } catch (SQLException e) {
                e.printStackTrace();
            }

            // 创建一个按钮，设置图标和文本
            JButton button = new JButton();
            button.setIcon(new ImageIcon(imagePath+"")); // 设置按钮的图标
            button.setText("<html>" + name + "<br>¥" + price + "</html>"); // 设置按钮的文本，使用html标签换行
            button.setVerticalTextPosition(SwingConstants.BOTTOM); // 设置文本在图标下方
            button.setHorizontalTextPosition(SwingConstants.CENTER); // 设置文本在图标中间

            // 为按钮添加点击事件监听器，跳转到商品详情卡片
            int finalId = id;
            String finalName = name;
            String finalImagePath = imagePath;
            double finalPrice = price;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 根据商品id查找对应的卡片对象
                    JPanel productPanel = productMap.get(finalId);
                    if (productPanel == null) {
                        // 如果没有找到，就创建一个新的卡片对象，并添加到主面板和HashMap中
                        productPanel = createProductPanel(finalId, finalName, finalImagePath, finalPrice);
                        mainPanel.add(productPanel, "product" + finalId);
                        productMap.put(finalId, productPanel);
                    }
                    // 切换到商品详情卡片
                    cardLayout.show(mainPanel, "product" + finalId);
                }
            });

            // 将按钮添加到第一个界面中
            card1.add(button);
        }

// 关闭数据库连接
        try {
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            dataBase.getCon().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

// 创建一个滚动面板，包含第一个界面
        JScrollPane scrollPane = new JScrollPane(card1);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 设置垂直滚动条总是可见

// 将滚动面板添加到主面板中，使用"card1"作为约束字符串
        mainPanel.add(scrollPane, "card1");


        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
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

        // 将其他卡片添加到主面板中，使用不同的约束字符串
        mainPanel.add(card2, "card2");
        mainPanel.add(card3, "card3");
        mainPanel.add(card4, "card4");
        mainPanel.add(card5, "card5");


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

// 显示第一个界面
        pack();
        setLocationRelativeTo(getOwner());
        cardLayout.show(mainPanel, "card1");

    }


    public void xiangxi(){
        System.out.println("click");
    }


    public static void main(String[] args) {
        UserFrm frame = new UserFrm();
        frame.setSize(500,600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
    private JPanel createProductPanel(int id, String name, String imagePath, double price) {
        // 创建一个新的卡片对象
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout());

        // 创建一个标签，显示商品名称
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("宋体", Font.BOLD, 24));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 创建一个标签，显示商品图片
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(imagePath));

        // 创建一个标签，显示商品价格
        JLabel priceLabel = new JLabel("¥" + price);
        priceLabel.setFont(new Font("宋体", Font.BOLD, 18));
        priceLabel.setForeground(Color.RED);
        priceLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 创建一个文本区域，显示商品描述
        JTextArea descriptionArea = new JTextArea();
        descriptionArea.setText("这里是商品的描述。");
        descriptionArea.setLineWrap(true); // 设置自动换行
        descriptionArea.setEditable(false); // 设置不可编辑

        // 创建一个滚动面板，包含文本区域
        JScrollPane descriptionPane = new JScrollPane(descriptionArea);
        descriptionPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 设置垂直滚动条总是可见

        // 创建一个按钮，实现加入购物车的功能
        JButton addToCartButton = new JButton("加入购物车");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 这里可以写你想要实现的功能，比如将商品id添加到用户的购物车列表中，或者弹出一个提示框等
                System.out.println("You added product " + id + " to your cart.");
            }
        });

        // 将各个组件添加到卡片中，使用不同的方位
        productPanel.add(nameLabel, BorderLayout.NORTH);
        productPanel.add(imageLabel, BorderLayout.CENTER);
        productPanel.add(priceLabel, BorderLayout.SOUTH);
        productPanel.add(descriptionPane, BorderLayout.EAST);
        productPanel.add(addToCartButton, BorderLayout.WEST);

        // 返回卡片对象
        return productPanel;
    }}
