package cn.bjut.jdbc;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
/**
 * @author 郭yw          未完成(动态、购物车单价总价、主页商品分类、我的页面内容)，用户登录进来之后的首页
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

    private HashMap<Integer, JPanel> productMap = new HashMap<>();

    public UserFrm(int u_id) throws SQLException {
        initComponents(u_id);
    }

    private void initComponents(int u_id) throws SQLException {
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
        String query = "SELECT p_id, p_name, p_img ,p_price,p_desc FROM product";
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
            String desc = null;
            try {
                desc = rs.getString("p_desc");
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
            String finaldesc= desc;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 根据商品id查找对应的卡片对象
                    JPanel productPanel = productMap.get(finalId);
                    if (productPanel == null) {
                        // 如果没有找到，就创建一个新的卡片对象，并添加到主面板和HashMap中
                        try {
                            productPanel = createProductPanel(finalId, finalName, finalImagePath, finalPrice,finaldesc);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
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



//创建第二个界面


        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel card2 = new JPanel();
        card2.add(new JLabel("这是第二个界面"));




//创建第三个界面

        JPanel card3 = new JPanel();
        // 在card3界面中，创建一个JTable对象，并设置其列名和数据模型
        JTable cartTable = new JTable();
        // 定义表格的列名数组
        String[] columnNames = {"商品名称", "加入时间", "数量"};
        // 定义表格的数据数组，初始为空
        Object[][] data = {};
        // 创建一个表格模型对象，并传入列名和数据数组
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        // 将表格模型设置给表格对象
        cartTable.setModel(tableModel);
        // 设置表格不可编辑
        cartTable.setEnabled(false);
        //设置刷新按钮
        JButton refreshButton=new JButton();
        refreshButton.setText("刷新");

        // 在refreshButton的点击事件中，从cart表格中查询用户购物车中的商品信息，并更新JTable对象的数据模型
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 点击刷新按钮时，清空表格模型中的数据
                tableModel.setRowCount(0);
                // 连接数据库
                DataBase dataBase=new DataBase();
                dataBase.OpenDB();
                // 获取当前用户的id
                try {
                    DataControl dataControl=new DataControl();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }

                // 查询数据库中的cart表格中的用户购物车信息
                Statement stmt = null;
                try {
                    stmt = dataBase.getCon().createStatement();
                    String query = "SELECT p_id, join_time, quantity FROM cart WHERE u_id=" + u_id;
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        // 获取商品的id，加入时间和数量
                        int p_id = rs.getInt("p_id");
                        Statement stmt1 = dataBase.getCon().createStatement();
                        String query1 = "SELECT p_name FROM product WHERE p_id=" + p_id;
                        ResultSet rs1 = stmt1.executeQuery(query1);
                        while (rs1.next()) {
                            String p_name = rs1.getString("p_name");
                            String join_time = rs.getString("join_time");
                            int quantity = rs.getInt("quantity");
                            // 将这些信息添加到表格模型中的一行
                            tableModel.addRow(new Object[]{p_name, join_time, quantity});
                        }
                        rs1.close();
                        stmt1.close();
                    }
                    rs.close();
                    stmt.close();
                    dataBase.getCon().close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        card3.add(refreshButton);

        // 在card3界面中，添加一个JScrollPane对象，并将JTable对象作为其视图组件
        JScrollPane scrollPane3 = new JScrollPane(cartTable);
        scrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 设置垂直滚动条总是可见

        // 将JScrollPane对象添加到card3界面中
        card3.add(scrollPane3, BorderLayout.CENTER);




//创建第四个界面
        // 创建第四个界面，显示用户信息和功能按钮
        JPanel card4 = new JPanel();
        card4.setLayout(new BorderLayout());
        DataControl dataControl=new DataControl();

// 创建一个标签，显示用户头像
        JLabel avatarLabel = new JLabel();
        avatarLabel.setIcon(new ImageIcon("D:\\test\\TradeProject\\src\\cn\\bjut\\jdbc\\pic\\2.png")); // 设置标签的图标为用户头像
        avatarLabel.setHorizontalAlignment(SwingConstants.CENTER); // 设置标签在水平方向居中

// 创建一个标签，显示用户名称
        JLabel nameLabel = null;
        try {
            nameLabel = new JLabel(dataControl.getUserName(u_id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        nameLabel.setFont(new Font("宋体", Font.BOLD, 24));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

// 创建一个按钮，实现修改密码的功能
        JButton changePasswordButton = new JButton("修改密码");
        changePasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 弹出一个对话框，让用户输入旧密码和新密码
                JDialog dialog = new JDialog(UserFrm.this, "修改密码", true);
                dialog.setLayout(new GridLayout(3, 2));
                dialog.add(new JLabel("旧密码："));
                JPasswordField oldPasswordField = new JPasswordField();
                dialog.add(oldPasswordField);
                dialog.add(new JLabel("新密码："));
                JPasswordField newPasswordField = new JPasswordField();
                dialog.add(newPasswordField);
                JButton confirmButton = new JButton("确定");
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 获取用户输入的旧密码和新密码
                        String oldPassword = new String(oldPasswordField.getPassword());
                        String newPassword = new String(newPasswordField.getPassword());
                        // 检查旧密码是否正确
                        try {
                            if (dataControl.getUserPsw(u_id).equals(oldPassword)) {
                                // 调用UserDao的方法修改用户密码
                                int result = updatePassword(dataControl.getUserName(u_id), newPassword);
                                if (result == 1) {
                                    JOptionPane.showMessageDialog(dialog, "修改失败！");
                                } else {
                                    JOptionPane.showMessageDialog(dialog, "修改成功！");
                                    dialog.dispose();
                                }
                            } else { // 如果旧密码不正确，提示用户
                                JOptionPane.showMessageDialog(dialog, "旧密码不正确！");
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                dialog.add(confirmButton);
                JButton cancelButton = new JButton("取消");
                cancelButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        dialog.dispose(); // 关闭对话框
                    }
                });
                dialog.add(cancelButton);
                dialog.pack();
                dialog.setLocationRelativeTo(UserFrm.this);
                dialog.setVisible(true); // 显示对话框
            }
        });

// 创建一个按钮，实现查看订单的功能
        JButton viewOrderButton = new JButton("查看订单");
        viewOrderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                // 跳转到一个新的界面，显示用户的订单信息
//                OrderFrm orderFrm = new OrderFrm(u_id); // 创建一个OrderFrm对象，传入用户id作为参数
//                orderFrm.setVisible(true); // 显示订单界面
            }
        });

// 将各个组件添加到卡片中，使用不同的方位
        card4.add(avatarLabel, BorderLayout.NORTH);
        card4.add(nameLabel, BorderLayout.CENTER);
        card4.add(changePasswordButton, BorderLayout.WEST);
        card4.add(viewOrderButton, BorderLayout.EAST);

// 将卡片添加到主面板中，使用"card4"作为约束字符串
        mainPanel.add(card4, "card4");






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

    // 修改用户密码的方法
    public int updatePassword(String username, String newPassword) {
        int result = 0; // 存储操作结果的变量，0表示失败，1表示成功
        Connection conn = null; // 数据库连接对象
        PreparedStatement ps = null; // 预编译语句对象
        try {
            DataBase dataBase=new DataBase();
            dataBase.OpenDB();
            conn=dataBase.getCon();
            // 定义SQL语句
            String sql = "UPDATE user SET u_psw = ? WHERE u_name = ?";
            // 获取预编译语句对象
            ps = conn.prepareStatement(sql);
            // 设置参数
            ps.setString(1, newPassword);
            ps.setString(2, username);
            // 执行更新
            result = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (ps != null) {
                    ps.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }


    private JPanel createProductPanel(int id, String name, String imagePath, double price,String desc) throws SQLException {
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
        descriptionArea.setText(desc);
        descriptionArea.setLineWrap(true); // 设置自动换行
        descriptionArea.setEditable(false); // 设置不可编辑

        // 创建一个滚动面板，包含文本区域
        JScrollPane descriptionPane = new JScrollPane(descriptionArea);
        descriptionPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 设置垂直滚动条总是可见
        DataControl dataControl=new DataControl();
        // 创建一个按钮，实现加入购物车的功能
        JButton addToCartButton = new JButton("加入购物车");
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dataControl.insert_cart(1,id,1);
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
    }

}