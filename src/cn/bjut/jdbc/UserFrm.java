package cn.bjut.jdbc;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.sql.*;
import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashMap;
/**
 * @author 郭yw          未完成(动态、购物车单价总价、主页商品分类、我的页面内容)，用户登录进来之后的首页
 */

public class UserFrm extends JFrame {
    private JButton homeButton;
    private JButton dynamicButton;
    private JButton shoppingButton;
    private JButton myButton;
    private JPanel mainPanel = new JPanel();
    private CardLayout cardLayout = new CardLayout();
    private int quantityy=10;

    private HashMap<Integer, JPanel> productMap = new HashMap<>();

    public UserFrm(int u_id) {
        initComponents(u_id);
    }

    private void initComponents(int u_id) {
        // 设置主面板为卡片布局
        mainPanel.setLayout(cardLayout);

        // 创建一个菜单栏对象
        JMenuBar menuBar = new JMenuBar();

        // 创建一个菜单对象
        JMenu fileMenu = new JMenu("菜单");
        JMenuItem exitItem = new JMenuItem("退出");

        exitItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    closeAndOpenLogin();
                } catch (UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                } catch (InstantiationException ex) {
                    ex.printStackTrace();
                } catch (IllegalAccessException ex) {
                    ex.printStackTrace();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        // 将菜单项添加到菜单中
        fileMenu.add(exitItem);
        // 将菜单添加到菜单栏中
        menuBar.add(fileMenu);
        // 将菜单栏添加到对话框中
        setJMenuBar(menuBar);

// 创建第一个界面-------------------------------------------------------------------------------------------------------
        JPanel card1 = new JPanel(); // 创建一个空的面板
        card1.setLayout(new BorderLayout()); // 设置面板的布局为边界布局

// 创建一个流式布局的面板，放在第一个界面的上半部分
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10)); // 创建一个流式布局的面板，设置水平和垂直间距为10
        JTextField searchField = new JTextField(20); // 创建一个文本框，设置列数为20
        JButton searchButton = new JButton("搜索"); // 创建一个按钮，设置文本为"搜索"
        JButton refreshButton0=new JButton("刷新");
        JButton concerButton=new JButton("我的关注");
        topPanel.add(searchField); // 将文本框添加到流式布局的面板中
        topPanel.add(searchButton); // 将按钮添加到流式布局的面板中
        topPanel.add(refreshButton0);
        topPanel.add(concerButton);


// 创建一个网格布局的面板，放在第一个界面的下半部分
        JPanel bottomPanel = new JPanel(); // 创建一个空的面板
// 创建一个网格布局管理器，指定4行6列
        GridLayout gridLayout = new GridLayout(0, 6);
// 设置网格之间的水平和垂直间距
        gridLayout.setHgap(10);
        gridLayout.setVgap(10);
        bottomPanel.setLayout(gridLayout); // 设置面板的布局为网格布局

// 连接数据库
        DataBase dataBase = new DataBase();
        dataBase.OpenDB();
        // 为搜索按钮添加动作监听器
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String keyword = searchField.getText(); // 获取搜索框的文本
                searchProduct(keyword,bottomPanel,u_id); // 调用搜索方法
            }
        });
        // 为刷新0按钮添加动作监听器
        refreshButton0.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshProduct(bottomPanel,u_id); // 调用搜索方法
            }
        });
        // 为关注按钮添加动作监听器
        concerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                concerProdict(u_id);

            }
        });
// 查询数据库中的商品信息
        final Statement[] stmt = {null};
        try {
            stmt[0] = dataBase.getCon().createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query = "SELECT p_id, p_name, p_img ,p_price,p_desc,m_id FROM product";
        final ResultSet[] rs = {null};
        try {
            rs[0] = stmt[0].executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String projectPath = System.getProperty("user.dir");

// 遍历结果集，为每个商品创建一个按钮，并添加到网格布局的面板中
        while (true) {
            try {
                if (!rs[0].next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 获取商品的id，名称，图片路径和价格
            int id = 0;
            try {
                id = rs[0].getInt("p_id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            int m_id = 0;
            try {
                m_id = rs[0].getInt("m_id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String name = null;
            try {
                name = rs[0].getString("p_name");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String imagePath = null;
            boolean flag=false;
            try {
                imagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + rs[0].getString("p_img");

                if (rs[0].getString("p_img")==null){flag=true;}

            } catch (SQLException e) {
                e.printStackTrace();
            }
            String desc = null;
            try {
                desc = rs[0].getString("p_desc");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            double price = 0;
            try {
                price = rs[0].getDouble("p_price");
            } catch (SQLException e) {
                e.printStackTrace();
            }



            // 创建一个按钮，设置图标和文本
            JButton button = new JButton();
            if (flag!=true){

                // 获取原始图片
                Image image = new ImageIcon(imagePath + "").getImage();
                // 创建缩放后的图片
                Image newImage = image.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                // 设置按钮的图标
                button.setIcon(new ImageIcon(newImage));
            }
            else {
                // 获取原始图片
                Image image = new ImageIcon(projectPath + File.separator + "src" + File.separator + "img" + File.separator +"R.jpg").getImage();
                // 创建缩放后的图片
                Image newImage = image.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                // 设置按钮的图标
                button.setIcon(new ImageIcon(newImage));
            }
            button.setText("<html>" + name + "<br>¥" + price + "</html>"); // 设置按钮的文本，使用html标签换行
            button.setVerticalTextPosition(SwingConstants.BOTTOM); // 设置文本在图标下方
            button.setHorizontalTextPosition(SwingConstants.CENTER); // 设置文本在图标中间

            // 为按钮添加点击事件监听器，跳转到商品详情卡片
            int finalId = id;//p_id
            int finalm_Id=m_id;
            String finalName = name;
            String finalImagePath = imagePath;
            double finalPrice = price;
            String finaldesc = desc;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 根据商品id查找对应的卡片对象
                    JPanel productPanel = productMap.get(finalId);
                    if (productPanel == null) {
                        // 如果没有找到，就创建一个新的卡片对象，并添加到主面板和HashMap中
                        productPanel = createProductPanel(finalId, u_id, finalName, finalImagePath, finalPrice, finaldesc,finalm_Id);
                        mainPanel.add(productPanel, "product" + finalId);
                        productMap.put(finalId, productPanel);
                    }
                    // 切换到商品详情卡片
                    cardLayout.show(mainPanel, "product" + finalId);
                }
            });

            // 将按钮添加到网格布局的面板中
            bottomPanel.add(button);
        }

// 关闭数据库连接
        try {
            rs[0].close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            stmt[0].close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            dataBase.getCon().close();
        } catch (Exception e) {
            e.printStackTrace();
        }

// 将网格布局的面板添加到第一个界面的下半部分
        card1.add(bottomPanel, BorderLayout.CENTER);

// 创建一个滚动面板，包含第一个界面
        JScrollPane scrollPane = new JScrollPane(card1);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 设置垂直滚动条总是可见
        scrollPane.setColumnHeaderView(topPanel); // 将流式布局的面板设置为滚动面板的列头视图
// 增加这一行，将流式布局的面板设置为滚动面板的列头视图，这样就可以固定在界面的上方
// 将滚动面板添加到主面板中，使用"card1"作为约束字符串
        mainPanel.add(scrollPane, "card1");



//创建第二个界面-------------------------------------------------------------------------------------------------------


        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        JPanel card2 = new JPanel();
        DataControl dataControl2= null;
        try {
            dataControl2 = new DataControl();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        card2.add(new JLabel());
        JPanel cardF = null;
        try {
            cardF = new ForumPage(dataControl2.selectuser(u_id),"用户");
            cardF.setPreferredSize(new Dimension(1150,920));
        } catch (SQLException e) {
            e.printStackTrace();
        }




// 创建第三个界面---------------------------------------------------------------------------------------------------------
        JPanel card3 = new JPanel();
        card3.setLayout(new BorderLayout());

// 在card3界面中，创建一个JTable对象，并设置其列名和数据模型
        JTable cartTable = new JTable();

// 定义表格的列名数组
        String[] columnNames = {"商品图片", "商品名称", "商品单价", "加入时间", "数量", "选择","p_id"};

// 定义表格的数据数组，初始为空
        Object[][] data = {};

// 创建表格模型对象
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
            // 重写getColumnClass方法，返回每一列的类型
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 0: // 第一列是图片类型
                        return ImageIcon.class;
                    case 4: // 第五列是整数类型
                        return Integer.class;
                    case 5: // 最后一列是布尔类型
                        return Boolean.class;
                    default: // 其他列是字符串类型
                        return String.class;
                }
            }

            // 重写isCellEditable方法，设置哪些单元格可以编辑
            @Override
            public boolean isCellEditable(int row, int column) {
                // 只有数量列和选择列可以编辑
                return column == 4 || column == 5;
            }
        };

// 将表格模型设置给表格对象
        cartTable.setModel(tableModel);


        cartTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        cartTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        cartTable.setRowHeight(150);
        Font font = new Font("宋", Font.PLAIN, 16);  // 创建字体对象，指定字体样式和大小
        cartTable.setFont(font);  // 设置表格的字体


// 创建一个表格渲染器类，继承自JPanel，实现TableCellRenderer接口
        class QuantityCellRenderer extends JPanel implements TableCellRenderer {
            private JTextField quantityField; // 数量文本框

            public QuantityCellRenderer() {
                setLayout(new BorderLayout()); // 设置布局为边界布局
                quantityField = new JTextField(); // 创建数量文本框对象
                quantityField.setHorizontalAlignment(JTextField.CENTER); // 设置文本框水平居中对齐
                quantityField.setEditable(false); // 设置文本框为只读模式

                add(quantityField, BorderLayout.CENTER); // 将文本框添加到面板的中间
            }

            // 重写getTableCellRendererComponent方法，返回渲染器组件
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                quantityField.setText(value.toString()); // 设置文本框的内容为表格单元格的值
                return this; // 返回当前面板对象作为渲染器组件
            }
        }

// 将第五列的单元格渲染器设置为QuantityCellRenderer对象
        cartTable.getColumn("数量").setCellRenderer(new QuantityCellRenderer());


// 设置表格可编辑
        cartTable.setEnabled(true);

        // 添加鼠标事件监听器
        cartTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                System.out.println("sadasdadsadafdsf");


                int row = cartTable.getSelectedRow();
                int col = cartTable.getSelectedColumn();
                // 判断是否是数量列
                if (col == 4) {
                    // 获取数量数据
                    quantityy = (int) cartTable.getValueAt(row, 4);
                    // 在这里你可以对数量数据进行任何操作，例如打印出来或者传递给其他方法
                    System.out.println(quantityy);

                }

            }
        });

// 添加表格编辑监听器
        cartTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4) {
                    int row = e.getFirstRow();
                    int quantity = (int) cartTable.getValueAt(row, 4);
                    Object join_time = cartTable.getValueAt(row, 3);

                    // 检查quantity是否是正整数
                    if (!String.valueOf(quantity).matches("\\d+")) {
                        // 如果不是，弹出提示框
                        JOptionPane.showMessageDialog(null, "请输入一个正整数", "输入无效", JOptionPane.ERROR_MESSAGE);
                        // 恢复原来的值
                        cartTable.setValueAt(quantityy, row, 4);
                        return;
                    }

                    // 更新数据库中的数量
                    Statement stmt = null;
                    try {
                        stmt = dataBase.getCon().createStatement();
                        // 查询product表中的p_quantity
                        String selectQuery = "SELECT p_quantity FROM product WHERE p_id=" + cartTable.getValueAt(row, 6);
                        ResultSet rs = stmt.executeQuery(selectQuery);
                        // 如果查询结果不为空
                        if (rs.next()) {
                            // 获取p_quantity的值
                            int p_quantity = rs.getInt("p_quantity");
                            // 判断quantity是否小于p_quantity
                            if (quantity < p_quantity) {
                                // 如果是，判断quantity是否为0
                                if (quantity == 0) {
                                    // 如果是0，就执行一个删除语句
                                    String deleteQuery = "DELETE FROM cart WHERE u_id=" + u_id + " AND join_time='" + join_time + "'";
                                    stmt.executeUpdate(deleteQuery);
                                    // 从表格模型中移除对应的行
                                    ((DefaultTableModel)cartTable.getModel()).removeRow(row);
                                } else {
                                    // 如果不是0，就执行一个更新语句
                                    String updateQuery = "UPDATE cart SET quantity=" + quantity + " WHERE u_id=" + u_id + " AND join_time='" + join_time + "'";
                                    stmt.executeUpdate(updateQuery);
                                }
                            } else {
                                // 如果不是，弹出提示框
                                JOptionPane.showMessageDialog(null, "库存不足，无法修改数量", "修改失败", JOptionPane.WARNING_MESSAGE);
                                // 恢复原来的值
                                cartTable.setValueAt(quantityy, row, 4);
                            }
                        }
                        stmt.close();
                        dataBase.getCon().close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });




        // 创建一个JLabel对象，用于显示总价
        JLabel totalLabel = new JLabel("总价：0 元");
        totalLabel.setFont(new Font("宋体", Font.BOLD, 20));

        // 创建一个JPanel对象，用于容纳总价标签和结算按钮
        JPanel totalPanel = new JPanel();
        totalPanel.setLayout(new BorderLayout());

        // 将总价标签添加到totalPanel的中间
        totalPanel.add(totalLabel, BorderLayout.CENTER);
        // 创建一个JButton对象，用于结算
        JButton checkoutButton = new JButton("结算");
        // 设置结算按钮的尺寸和字体
        checkoutButton.setPreferredSize(new Dimension(120, 40));
        checkoutButton.setFont(new Font("宋体", Font.BOLD, 18));
        // 设置结算按钮的点击事件
        checkoutButton.addActionListener(new ActionListener() {
            DataControl dataControl;

            {
                try {
                    dataControl = new DataControl();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void actionPerformed(ActionEvent e) {
                // 遍历表格的数据模型，从后往前遍历
                for (int i = tableModel.getRowCount() - 1; i >= 0; i--) {
                    // 获取选择列的值
                    boolean selected = (boolean) tableModel.getValueAt(i, 5);
                    String join_time=(String) tableModel.getValueAt(i, 3);
                    if (selected) {
                        int p_id= 0;
                        try {
                            p_id = dataControl.find_p_id(u_id,join_time);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }
                        dataControl.insertOrderFromCart(u_id,p_id);
                        // 删除表格中该条记录
                        tableModel.removeRow(i);
                    }
                }
            }
        });



        // 将结算按钮添加到totalPanel的东边
        totalPanel.add(checkoutButton, BorderLayout.EAST);

        // 将totalPanel添加到card3界面中的南边
        card3.add(totalPanel, BorderLayout.SOUTH);

        // 添加表格编辑监听器
        cartTable.getModel().addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e.getType() == TableModelEvent.UPDATE && e.getColumn() == 4) {
                    int row = e.getFirstRow();
                    int quantity = (int) cartTable.getValueAt(row, 4);
                    Object join_time = cartTable.getValueAt(row, 3);

                    // 更新数据库中的数量
                    Statement stmt = null;
                    try {
                        stmt = dataBase.getCon().createStatement();
                        String updateQuery = "UPDATE cart SET quantity=" + quantity + " WHERE u_id=" + u_id + " AND join_time='" + join_time + "'";
                        stmt.executeUpdate(updateQuery);
                        stmt.close();
                        dataBase.getCon().close();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    // 计算总价并更新JLabel的显示内容
                    double total = 0;
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        // 只计算选择列为true的行
                        if ((boolean) tableModel.getValueAt(i, 5)) {
                            String priceStr = (String) tableModel.getValueAt(i, 2);
                            int q = (int) tableModel.getValueAt(i, 4);
                            double price = Double.parseDouble(priceStr);
                            total += price * q;
                        }
                    }
                    totalLabel.setText("总价：" + String.format("%.2f", total) + " 元");

                }
            }
        });

        // 创建一个JPopupMenu对象，用于弹出右键菜单
        JPopupMenu menu = new JPopupMenu();
// 创建一个JMenuItem对象，用于删除菜单项
        JMenuItem deleteItem = new JMenuItem("删除");
// 为删除菜单项添加动作监听器
        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取当前选中的行
                int row = cartTable.getSelectedRow();
                // 如果没有选中任何行，直接返回
                if (row == -1) return;
                // 获取当前行的商品ID
                Object value = cartTable.getValueAt(row, 6); // 假设商品ID是第0列
                // 创建一个Statement对象
                Statement stmt = null;
                try {
                    stmt = dataBase.getCon().createStatement();
                    // 执行一个删除语句，根据你的数据库表结构和主键来修改
                    String deleteQuery = "DELETE FROM cart WHERE u_id=" + u_id + " AND p_id=" + value;

                    stmt.executeUpdate(deleteQuery);
                    // 从表格模型中移除对应的行
                    ((DefaultTableModel)cartTable.getModel()).removeRow(row);
                    // 关闭Statement对象和数据库连接
                    stmt.close();
                    dataBase.getCon().close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

// 将删除菜单项添加到右键菜单中
        menu.add(deleteItem);

// 为表格添加鼠标监听器，用于弹出右键菜单
        cartTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                // 判断是否为鼠标右键
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // 获取鼠标点击的位置
                    Point p = e.getPoint();
                    // 获取鼠标点击的行和列
                    int row = cartTable.rowAtPoint(p);
                    int column = cartTable.columnAtPoint(p);
                    // 如果点击的位置不在表格中，直接返回
                    if (row == -1 || column == -1) {
                        return;
                    }
                    // 将表格的选中行和列设为鼠标点击的位置
                    cartTable.setRowSelectionInterval(row, row);
                    cartTable.setColumnSelectionInterval(column, column);
                    // 弹出右键菜单
                    menu.show(cartTable, p.x, p.y);
                }
            }
        });


// 设置刷新按钮
        JButton refreshButton = new JButton();
        refreshButton.setText("刷新");

// 在refreshButton的点击事件中，从cart表格中查询用户购物车中的商品信息，并更新JTable对象的数据模型
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 点击刷新按钮时，清空表格模型中的数据
                tableModel.setRowCount(0);
                // 连接数据库
                DataBase dataBase = new DataBase();
                dataBase.OpenDB();

                // 查询数据库中的cart表格中的用户购物车信息
                Statement stmt = null;
                try {
                    stmt = dataBase.getCon().createStatement();
                    String query = "SELECT p_id, join_time, quantity FROM cart WHERE u_id=" + u_id;
                    ResultSet rs = stmt.executeQuery(query);
                    double total = 0; // 总价变量
                    while (rs.next()) {
                        // 获取商品的id，加入时间和数量
                        int p_id = rs.getInt("p_id");
                        Statement stmt1 = dataBase.getCon().createStatement();
                        String query1 = "SELECT p_name, p_price,p_img FROM product WHERE p_id=" + p_id;
                        ResultSet rs1 = stmt1.executeQuery(query1);
                        while (rs1.next()) {
                            String p_name = rs1.getString("p_name");
                            String p_price = rs1.getString("p_price");
                            String p_img = rs1.getString("p_img");
                            String join_time = rs.getString("join_time");
                            int quantity = rs.getInt("quantity");
                            if (p_img!=null){
                                Image image0 = new ImageIcon(projectPath + File.separator + "src" + File.separator
                                        + "img" + File.separator +rs1.getString("p_img")).getImage();
                                // 创建缩放后的图片
                                Image newImage = image0.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                                ImageIcon image=new ImageIcon(newImage);
                                // 将这些信息添加到表格模型中的一行
                                tableModel.addRow(new Object[]{image, p_name, p_price, join_time, quantity, false,p_id});
                            }
                            else {
                                Image image0 = new ImageIcon(projectPath + File.separator + "src" + File.separator
                                        + "img" + File.separator +"R.jpg").getImage();
                                // 创建缩放后的图片
                                Image newImage = image0.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                                ImageIcon image=new ImageIcon(newImage);
                                // 将这些信息添加到表格模型中的一行
                                tableModel.addRow(new Object[]{image, p_name, p_price, join_time, quantity, false,p_id});
                            }

                            // 判断第六列是否为true
                            boolean selected = (boolean)tableModel.getValueAt(tableModel.getRowCount()-1, 5);
                            if (selected) {
                                // 计算总价
                                double price = Double.parseDouble(p_price);
                                total += price * quantity;
                            }
                        }
                        rs1.close();
                        stmt1.close();
                    }
                    rs.close();
                    stmt.close();
                    dataBase.getCon().close();

                    // 更新JLabel的显示内容
                    totalLabel.setText("总价：" + String.format("%.2f", total) + " 元");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

// 给表格模型添加一个监听器，监听第六列的复选框的状态改变
        tableModel.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                // 如果是第六列的数据改变了
                if (e.getColumn() == 5) {
                    // 重新计算总价
                    double total = 0;
                    for (int i = 0; i < tableModel.getRowCount(); i++) {
                        boolean selected = (boolean)tableModel.getValueAt(i, 5);
                        if (selected) {
                            double price = Double.parseDouble((String)tableModel.getValueAt(i, 2));
                            int quantity = (int)tableModel.getValueAt(i, 4);
                            total += price * quantity;
                        }
                    }
                    // 更新JLabel的显示内容
                    totalLabel.setText("总价：" + String.format("%.2f", total) + " 元");
                }
            }
        });

        card3.add(refreshButton, BorderLayout.NORTH);

        // 在card3界面中，添加一个JScrollPane对象，并将JTable对象作为其视图组件
        JScrollPane scrollPane3 = new JScrollPane(cartTable);
        scrollPane3.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS); // 设置垂直滚动条总是可见

        // 将JScrollPane对象添加到card3界面中
        card3.add(scrollPane3, BorderLayout.CENTER);



//创建第四个界面---------------------------------------------------------------------------------------------------------
        // 创建第四个界面，显示用户信息和功能按钮
        JPanel card4 = new JPanel();
        card4.setLayout(new BorderLayout());
        DataControl dataControl= null;
        try {
            dataControl = new DataControl();
        } catch (SQLException e) {
            e.printStackTrace();
        }


// 创建一个按钮，实现修改密码的功能
        JButton changePasswordButton = new JButton("修改密码");
        DataControl finalDataControl = dataControl;
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
                            if (finalDataControl.getUserPsw(u_id).equals(oldPassword)) {
                                // 调用UserDao的方法修改用户密码
                                int result = updatePassword(finalDataControl.getUserName(u_id), newPassword);
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
                dialog.setSize(200, 130);
                dialog.setVisible(true); // 显示对话框
            }
        });

        // 创建一个按钮，实现修改个人信息的功能
        JButton modifyInfoButton = new JButton("修改个人信息");
        modifyInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 弹出一个对话框，让用户输入要修改的信息
                JDialog dialog = new JDialog(UserFrm.this, "修改个人信息", true);
                dialog.setLayout(new GridLayout(5, 2));
                dialog.add(new JLabel("姓名："));
                JTextField nameField = new JTextField();
                dialog.add(nameField);
                dialog.add(new JLabel("性别："));
                JComboBox<String> sexBox = new JComboBox<>(new String[]{"男", "女"});
                dialog.add(sexBox);
                dialog.add(new JLabel("电话："));
                JTextField teleField = new JTextField();
                dialog.add(teleField);
                JButton confirmButton = new JButton("确定");
                confirmButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 获取用户输入的要修改的信息
                        String name = nameField.getText();
                        String sex = (String) sexBox.getSelectedItem();
                        String tele = teleField.getText();
                        // 调用一个方法，将用户信息更新到数据库中
                        int result = updateInfo(u_id, name, sex, tele);
                        if (result == 1) {
                            JOptionPane.showMessageDialog(dialog, "修改成功！");
                            dialog.dispose();
                        } else {
                            JOptionPane.showMessageDialog(dialog, "修改失败！");
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
                // 创建一个OrderFrm对象，传入用户id作为参数
                JFrame orderFrm = new JFrame();
                orderFrm.setLayout(new BorderLayout());
                JTable cartTable = new JTable();
                String[] columnNames = {"商品图片", "商品名称", "商品单价", "加入时间", "数量","p_id"};
                Object[][] data = {};
                // 创建表格模型对象
                DefaultTableModel tableModel = new DefaultTableModel(data, columnNames) {
                    // 重写getColumnClass方法，返回每一列的类型
                    @Override
                    public Class<?> getColumnClass(int columnIndex) {
                        switch (columnIndex) {
                            case 0: // 第一列是图片类型
                                return ImageIcon.class;
                            case 4: // 第五列是整数类型
                                return Integer.class;
                            case 5: // 最后一列是布尔类型
                                return Integer.class;
                            default: // 其他列是字符串类型
                                return String.class;
                        }
                    }
                };
                cartTable.setModel(tableModel);
                cartTable.getColumnModel().getColumn(0).setPreferredWidth(120);
                cartTable.getColumnModel().getColumn(4).setPreferredWidth(120);
                cartTable.setRowHeight(150);
                Font font = new Font("宋", Font.PLAIN, 16);  // 创建字体对象，指定字体样式和大小
                cartTable.setFont(font);  // 设置表格的字体
                // 设置刷新按钮
                JButton refreshButton = new JButton();
                refreshButton.setText("刷新");
                refreshButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 点击刷新按钮时，清空表格模型中的数据
                        tableModel.setRowCount(0);
                        // 连接数据库
                        DataBase dataBase = new DataBase();
                        dataBase.OpenDB();

                        // 查询数据库中的cart表格中的用户购物车信息
                        Statement stmt = null;
                        try {
                            stmt = dataBase.getCon().createStatement();
                            String query = "SELECT p_id, buy_time, quantity FROM orders WHERE u_id=" + u_id;
                            ResultSet rs = stmt.executeQuery(query);
                            while (rs.next()) {
                                // 获取商品的id，加入时间和数量
                                int p_id = rs.getInt("p_id");
                                Statement stmt1 = dataBase.getCon().createStatement();
                                String query1 = "SELECT p_name, p_price,p_img FROM product WHERE p_id=" + p_id;
                                ResultSet rs1 = stmt1.executeQuery(query1);
                                while (rs1.next()) {
                                    String p_name = rs1.getString("p_name");
                                    String p_price = rs1.getString("p_price");
                                    String buy_time = rs.getString("buy_time");
                                    int quantity = rs.getInt("quantity");
                                    String projectPath = System.getProperty("user.dir");
                                    boolean flag = false;
                                    if (rs1.getString("p_img")==null) {//.equals("(Null)")

                                        flag = true;
                                    }
                                    if (flag != true){
                                        String imagePath = projectPath + File.separator + "src"
                                                + File.separator + "img" + File.separator + rs1.getString("p_img");
                                        // 获取原始图片
                                        Image image0 = new ImageIcon(imagePath + "").getImage();
                                        // 创建缩放后的图片
                                        Image newImage = image0.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                                        ImageIcon image=new ImageIcon(newImage);
                                        // 将这些信息添加到表格模型中的一行
                                        tableModel.addRow(new Object[]{image, p_name, p_price, buy_time, quantity,p_id});
                                    }
                                    else {
                                        String imagePath = projectPath + File.separator + "src"
                                                + File.separator + "img" + File.separator + "R.jpg";
                                        // 获取原始图片
                                        Image image0 = new ImageIcon(imagePath + "").getImage();
                                        // 创建缩放后的图片
                                        Image newImage = image0.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                                        ImageIcon image=new ImageIcon(newImage);
                                        // 将这些信息添加到表格模型中的一行
                                        tableModel.addRow(new Object[]{image, p_name, p_price, buy_time, quantity,p_id});
                                    }

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

                // 创建一个JPopupMenu对象，用于弹出右键菜单
                JPopupMenu menu = new JPopupMenu();
// 创建一个JMenuItem对象，用于删除菜单项
                JMenuItem deleteItem = new JMenuItem("删除");
// 为删除菜单项添加动作监听器
                deleteItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 获取当前选中的行
                        int row = cartTable.getSelectedRow();
                        // 如果没有选中任何行，直接返回
                        if (row == -1) return;
                        // 获取当前行的商品ID
                        Object value = cartTable.getValueAt(row, 5);
                        Object buy_time=cartTable.getValueAt(row,3);
                        // 创建一个Statement对象
                        Statement stmt = null;
                        try {
                            stmt = dataBase.getCon().createStatement();
                            // 执行一个删除语句，根据你的数据库表结构和主键来修改
                            String deleteQuery = "DELETE FROM orders WHERE u_id=" + u_id + " AND p_id=" + value
                                    +" AND buy_time='" +buy_time + "'";

                            stmt.executeUpdate(deleteQuery);
                            // 从表格模型中移除对应的行
                            ((DefaultTableModel)cartTable.getModel()).removeRow(row);
                            // 关闭Statement对象和数据库连接
                            stmt.close();
                            dataBase.getCon().close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });

// 将删除菜单项添加到右键菜单中
                menu.add(deleteItem);

// 为表格添加鼠标监听器，用于弹出右键菜单
                cartTable.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // 判断是否为鼠标右键
                        if (e.getButton() == MouseEvent.BUTTON3) {
                            // 获取鼠标点击的位置
                            Point p = e.getPoint();
                            // 获取鼠标点击的行和列
                            int row = cartTable.rowAtPoint(p);
                            int column = cartTable.columnAtPoint(p);
                            // 如果点击的位置不在表格中，直接返回
                            if (row == -1 || column == -1) {
                                return;
                            }
                            // 将表格的选中行和列设为鼠标点击的位置
                            cartTable.setRowSelectionInterval(row, row);
                            cartTable.setColumnSelectionInterval(column, column);
                            // 弹出右键菜单
                            menu.show(cartTable, p.x, p.y);
                        }
                    }
                });


                orderFrm.add(refreshButton, BorderLayout.NORTH);
                orderFrm.add(new JScrollPane(cartTable), BorderLayout.CENTER);
                orderFrm.pack();
                orderFrm.setLocationRelativeTo(null);
                orderFrm.setVisible(true);
                refreshButton.doClick();
                // 显示订单界面
            }
        });
        JLabel nameLabel = new JLabel("名称：" );
        nameLabel.setFont(new Font("宋体", Font.PLAIN, 18));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 创建一个标签，显示用户账号
        JLabel accLabel = new JLabel("账号：" );
        accLabel.setFont(new Font("宋体", Font.PLAIN, 18));
        accLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 创建一个标签，显示用户性别
        JLabel sexLabel = new JLabel("性别：" );
        sexLabel.setFont(new Font("宋体", Font.PLAIN, 18));
        sexLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 创建一个标签，显示用户电话
        JLabel teleLabel = new JLabel("电话：" );
        teleLabel.setFont(new Font("宋体", Font.PLAIN, 18));
        teleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        // 创建一个标签，显示用户信息
        // 连接数据库
        dataBase.OpenDB();
        // 构建新的SQL查询语句
        String query4 = "SELECT u_acc, u_psw, u_name,u_sex,u_tele FROM user WHERE u_id=" + u_id;

        try {
            try {
                stmt[0] = dataBase.getCon().createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rs[0] = stmt[0].executeQuery(query4);
            if (rs[0].next()) {
                String u_acc = rs[0].getString("u_acc");
                String u_psw = rs[0].getString("u_psw");
                String u_name = rs[0].getString("u_name");
                String u_sex = rs[0].getString("u_sex");
                String u_tele = rs[0].getString("u_tele");

                // 更新标签的文本
                nameLabel.setText("名称：" + u_name);
                accLabel.setText("账号：" + u_acc);
                sexLabel.setText("性别：" + u_sex);
                teleLabel.setText("电话：" + u_tele);

                // 创建一个面板，用于放置用户信息的标签
                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(4, 1,0,0));
                infoPanel.add(nameLabel);
                infoPanel.add(accLabel);
                infoPanel.add(sexLabel);
                infoPanel.add(teleLabel);
                card4.add(infoPanel, BorderLayout.CENTER);
            }

            // 关闭数据库连接
            rs[0].close();
            stmt[0].close();
            try {
                dataBase.getCon().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // 创建一个按钮，设置文本为“刷新”
        JButton myrefreshButton = new JButton("刷新");
// 为按钮添加监听事件
        myrefreshButton.addActionListener(new ActionListener() {
            // 事件处理方法
            public void actionPerformed(ActionEvent e) {
                // 重新连接数据库
                dataBase.OpenDB();
                // 重新执行SQL查询语句
                String query4 = "SELECT u_acc, u_psw, u_name,u_sex,u_tele FROM user WHERE u_id=" + u_id;
                try {
                    try {
                        stmt[0] = dataBase.getCon().createStatement();
                    } catch (Exception e1) {
                        e1.printStackTrace();
                    }
                    rs[0] = stmt[0].executeQuery(query4);
                    if (rs[0].next()) {
                        // 获取数据库中的用户信息
                        String u_acc = rs[0].getString("u_acc");
                        String u_psw = rs[0].getString("u_psw");
                        String u_name = rs[0].getString("u_name");
                        String u_sex = rs[0].getString("u_sex");
                        String u_tele = rs[0].getString("u_tele");
                        // 更新标签的文本
                        nameLabel.setText("名称：" + u_name);
                        accLabel.setText("账号：" + u_acc);
                        sexLabel.setText("性别：" + u_sex);
                        teleLabel.setText("电话：" + u_tele);
                    }
                    // 关闭数据库连接
                    rs[0].close();
                    stmt[0].close();
                    try {
                        dataBase.getCon().close();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                } catch (SQLException e3) {
                    e3.printStackTrace();
                }
                // 重绘窗口
                repaint();
            }
        });


// 将各个组件添加到卡片中，使用不同的方位
        JPanel buttonpanel=new JPanel();
        buttonpanel.add(changePasswordButton);
        buttonpanel.add(viewOrderButton);
        buttonpanel.add(modifyInfoButton);
        buttonpanel.add(myrefreshButton);
        card4.add(buttonpanel,BorderLayout.SOUTH);
//        card4.add(changePasswordButton, BorderLayout.EAST);
//        card4.add(viewOrderButton, BorderLayout.EAST);


        //-----------------------------------------------------------------------------------------------------------

// 将卡片添加到主面板中，使用"card4"作为约束字符串
        mainPanel.add(card4, "card4");
        // 将其他卡片添加到主面板中，使用不同的约束字符串
        mainPanel.add(cardF, "card2");
        mainPanel.add(card3, "card3");




        homeButton = new JButton("首页");
        homeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card1");
            }
        });

        dynamicButton = new JButton("论坛");
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
                refreshButton.doClick();
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



    private JPanel createProductPanel(int id,int u_id, String name, String imagePath, double price,String desc,int m_id) {
        // 创建一个新的卡片对象
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout());
        String m_name=null;
        try {
            DataControlMercahnt dataControlMercahnt=new DataControlMercahnt();
            m_name=dataControlMercahnt.getMerchantm_name(m_id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 创建一个标签，显示商品名称
        JLabel mnameLabel = new JLabel("商家名称："+m_name);
        mnameLabel.setFont(new Font("宋体", Font.BOLD, 24));
        mnameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 创建一个标签，显示商品名称
        JLabel nameLabel = new JLabel("商品名称："+name);
        nameLabel.setFont(new Font("宋体", Font.BOLD, 24));
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // 创建一个标签，显示商品图片
        JLabel imageLabel = new JLabel();
        imageLabel.setIcon(new ImageIcon(imagePath));

        // 创建一个标签，显示商品价格
        JLabel priceLabel = new JLabel("商品价格：¥" + price);
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
        DataControl dataControl= null;
        try {
            dataControl = new DataControl();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // 创建一个按钮，实现加入购物车的功能
        JButton addToCartButton = new JButton("加入购物车");
        DataControl finalDataControl = dataControl;
        addToCartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                finalDataControl.insertOrUpdateCart(u_id,id);
            }
        });

        // 创建一个按钮，实现关注商家的功能
        JButton followButton = new JButton("关注商家");
        followButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 调用dataControl的方法，将用户id和商家id添加到concern表中 // 添加一个新的方法调用
                finalDataControl.insertOrUpdateConcern(u_id,m_id);
            }
        });

        // 创建一个新的面板，用于放置nameLabel和priceLabel
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        northPanel.setBackground(Color.WHITE);
        northPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        northPanel.add(nameLabel);
        northPanel.add(priceLabel);



        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(Color.WHITE);
        southPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        southPanel.add(mnameLabel);
        southPanel.add(followButton);


        // 将各个组件添加到卡片中，使用不同的方位
        productPanel.add(northPanel, BorderLayout.NORTH);
        productPanel.add(imageLabel, BorderLayout.CENTER);
        productPanel.add(descriptionPane, BorderLayout.EAST);
        productPanel.add(addToCartButton, BorderLayout.WEST);
        productPanel.add(southPanel, BorderLayout.SOUTH);

        // 返回卡片对象
        return productPanel;
    }

    private void concerProdict(int u_id) {

        // 创建一个新的窗口对象
        JFrame followFrame = new JFrame("关注的商家");
        // 设置窗口的大小和位置
        followFrame.setSize(800, 600);
        followFrame.setLocationRelativeTo(null);
        // 设置窗口的关闭操作
        followFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // 连接数据库
        DataBase dataBase = new DataBase();
        dataBase.OpenDB();
        String[] columnnames={"商家名称","商品名称","价格"};
        Object data[][]=null;
        Statement stmt = null;
        DefaultTableModel tableModel=new DefaultTableModel(data,columnnames);
        try {
            stmt = dataBase.getCon().createStatement();
            String query = "SELECT m_id FROM concern WHERE u_id=" + u_id;
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                int m_id = rs.getInt("m_id");
                Statement stmt1 = dataBase.getCon().createStatement();
                Statement stmt2 = dataBase.getCon().createStatement();

                String query1 = "SELECT p_name, p_price FROM product WHERE m_id=" + m_id;
                ResultSet rs1 = stmt1.executeQuery(query1);
                String query2 = "SELECT m_name FROM merchant WHERE m_id=" + m_id;
                ResultSet rs2=stmt2.executeQuery(query2);
                while (rs2.next()){
                    String m_name=rs2.getString("m_name");
                while (rs1.next()) {
                    String p_name = rs1.getString("p_name");
                    String p_price = rs1.getString("p_price");
                    tableModel.addRow(new Object[]{ m_name,p_name, p_price});

                }}
                rs1.close();
                rs2.close();
                stmt1.close();
                stmt2.close();
            }

            rs.close();
            stmt.close();
            dataBase.getCon().close();

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // 创建一个表格对象，用于显示表格模型中的数据
        JTable table = new JTable(tableModel);
        // 创建一个滚动面板，并将表格放入其中
        JScrollPane scrollPane = new JScrollPane(table);
        // 创建一个面板对象，用于容纳滚动面板
        JPanel followPanel = new JPanel();
        followPanel.setLayout(new BorderLayout());
        // 将滚动面板添加到面板对象中
        followPanel.add(scrollPane, BorderLayout.CENTER);
        // 将面板对象添加到窗口对象中
        followFrame.add(followPanel);
        // 设置窗口可见
        followFrame.setVisible(true);
    }

    // 创建搜索方法
    private void searchProduct(String keyword,JPanel bottomPanel,int u_id) {
        // 清空网格布局的面板
        bottomPanel.removeAll();

        // 连接数据库
        DataBase dataBase = new DataBase();
        dataBase.OpenDB();

        // 构建新的SQL查询语句
        String query = "SELECT p_id, p_name, p_img ,p_price,p_desc,m_id FROM product WHERE p_name LIKE '%" + keyword + "%' OR p_desc LIKE '%" + keyword + "%'";
        Statement stmt = null;
        ResultSet rs;

        try {
            try {
                stmt = dataBase.getCon().createStatement();
            } catch (Exception e) {
                e.printStackTrace();
            }
            rs = stmt.executeQuery(query);
            String projectPath = System.getProperty("user.dir");
            // 遍历结果集，为每个匹配的商品创建一个按钮，并添加到网格布局的面板中
            while (rs.next()) {
                int id = rs.getInt("p_id");
                int m_id = rs.getInt("m_id");
                String name = rs.getString("p_name");
                boolean flag = false;
                String  imagePath = projectPath + File.separator + "src"
                        + File.separator + "img" + File.separator + rs.getString("p_img");
                if (rs.getString("p_img") == null) {
                    flag = true;
                }
                String desc = rs.getString("p_desc");
                double price = rs.getDouble("p_price");

                JButton button = new JButton();
                if (flag != true) {

                    // 获取原始图片
                    Image image = new ImageIcon(imagePath + "").getImage();
                    // 创建缩放后的图片
                    Image newImage = image.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                    // 设置按钮的图标
                    button.setIcon(new ImageIcon(newImage));
                } else {
                    // 获取原始图片
                    Image image = new ImageIcon(projectPath + File.separator + "src" + File.separator + "img" + File.separator + "R.jpg").getImage();
                    // 创建缩放后的图片
                    Image newImage = image.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                    // 设置按钮的图标
                    button.setIcon(new ImageIcon(newImage));
                }
                button.setText("<html>" + name + "<br>¥" + price + "</html>");
                button.setVerticalTextPosition(SwingConstants.BOTTOM);
                button.setHorizontalTextPosition(SwingConstants.CENTER);

                // 添加点击事件监听器
                int finalId = id;
                int finalm_Id = m_id;
                String finalName = name;
                String finalImagePath = imagePath;
                double finalPrice = price;
                String finalDesc = desc;
                button.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        // 根据商品id查找对应的卡片对象
                        JPanel productPanel = productMap.get(finalId);
                        if (productPanel == null) {
                            // 如果没有找到，就创建一个新的卡片对象，并添加到主面板和HashMap中
                            productPanel = createProductPanel(finalId, u_id, finalName, finalImagePath, finalPrice, finalDesc,finalm_Id);
                            mainPanel.add(productPanel, "product" + finalId);
                            productMap.put(finalId, productPanel);
                        }
                        // 切换到商品详情卡片
                        cardLayout.show(mainPanel, "product" + finalId);
                    }
                });

                bottomPanel.add(button); // 将按钮添加到网格布局的面板中
            }

            // 关闭数据库连接
            rs.close();
            stmt.close();
            try {
                dataBase.getCon().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 刷新界面，显示更新后的商品列表
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }

    // 定义一个方法，用于将用户信息更新到数据库中
    public int updateInfo(int u_id, String name, String sex, String tele) {
        // 创建一个布尔变量，用于存储修改结果
        int flag = 0;

        // 创建一个Connection对象，用于连接数据库
        Connection conn = null;

        // 创建一个PreparedStatement对象，用于执行SQL语句
        PreparedStatement ps = null;

        // 定义一个SQL语句，根据u_id修改users表中的信息
        String sql = "UPDATE user SET u_name = ?, u_sex = ?, u_tele = ? WHERE u_id = ?";

        try {
            DataBase dataBase=new DataBase();
            dataBase.OpenDB();
            conn=dataBase.getCon();
            // 预编译SQL语句
            ps = conn.prepareStatement(sql);

            // 设置SQL语句的参数
            ps.setString(1, name);
            ps.setString(2, sex);
            ps.setString(3, tele);
            ps.setInt(4, u_id);

            // 执行SQL语句，得到影响的行数
            int rows = ps.executeUpdate();

            // 判断是否修改成功
            if (rows > 0) {
                // 修改成功，将flag设为true
                flag = 1;
            }
        } catch (Exception e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        // 返回flag
        return flag;
    }


    private void closeAndOpenLogin() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        this.dispose(); // 关闭当前窗口
        login loginFrm = null; // 创建一个新的登录窗口
        try {
            loginFrm = new login();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        loginFrm.setLocationRelativeTo(null); // 将登录窗口设置为居中显示
        loginFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrm.setSize(800, 600);
        loginFrm.setLocationRelativeTo(null);
        loginFrm.setVisible(true);
    }

    // 创建刷新方法
    private void refreshProduct(JPanel bottomPanel,int u_id) {
        // 清空网格布局的面板
        bottomPanel.removeAll();

        // 连接数据库
        DataBase dataBase = new DataBase();
        dataBase.OpenDB();

        // 查询数据库中的商品信息
        Statement stmt = null;
        try {
            stmt = dataBase.getCon().createStatement();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String query = "SELECT p_id, p_name, p_img ,p_price,p_desc,m_id FROM product";
        ResultSet rs = null;
        try {
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String projectPath = System.getProperty("user.dir");

// 遍历结果集，为每个商品创建一个按钮，并添加到网格布局的面板中
        while (true) {
            try {
                if (!rs.next()) break;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 获取商品的id，名称，图片路径和价格
            int id = 0;
            try {
                id = rs.getInt("p_id");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            int m_id = 0;
            try {
                m_id = rs.getInt("m_id");
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
            boolean flag = false;
            try {
                imagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + rs.getString("p_img");

                if (rs.getString("p_img") == null) {
                    flag = true;
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            String desc = null;
            try {
                desc = rs.getString("p_desc");
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
            if (flag != true) {

                // 获取原始图片
                Image image = new ImageIcon(imagePath + "").getImage();
                // 创建缩放后的图片
                Image newImage = image.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                // 设置按钮的图标
                button.setIcon(new ImageIcon(newImage));
            } else {
                // 获取原始图片
                Image image = new ImageIcon(projectPath + File.separator + "src" + File.separator + "img" + File.separator + "R.jpg").getImage();
                // 创建缩放后的图片
                Image newImage = image.getScaledInstance(170, 170, Image.SCALE_SMOOTH);
                // 设置按钮的图标
                button.setIcon(new ImageIcon(newImage));
            }
            button.setText("<html>" + name + "<br>¥" + price + "</html>"); // 设置按钮的文本，使用html标签换行
            button.setVerticalTextPosition(SwingConstants.BOTTOM); // 设置文本在图标下方
            button.setHorizontalTextPosition(SwingConstants.CENTER); // 设置文本在图标中间

            // 为按钮添加点击事件监听器，跳转到商品详情卡片
            int finalId = id;//p_id
            int finalm_Id = m_id;
            String finalName = name;
            String finalImagePath = imagePath;
            double finalPrice = price;
            String finaldesc = desc;
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // 根据商品id查找对应的卡片对象
                    JPanel productPanel = productMap.get(finalId);
                    if (productPanel == null) {
                        // 如果没有找到，就创建一个新的卡片对象，并添加到主面板和HashMap中
                        productPanel = createProductPanel(finalId, u_id, finalName, finalImagePath, finalPrice, finaldesc, finalm_Id);
                        mainPanel.add(productPanel, "product" + finalId);
                        productMap.put(finalId, productPanel);
                    }
                    // 切换到商品详情卡片
                    cardLayout.show(mainPanel, "product" + finalId);
                }
            });

            // 将按钮添加到网格布局的面板中
            bottomPanel.add(button);
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
        // 刷新界面，显示更新后的商品列表
        bottomPanel.revalidate();
        bottomPanel.repaint();
    }

}