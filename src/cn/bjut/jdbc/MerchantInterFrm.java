package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class MerchantInterFrm extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    public  JPanel mainPanel = new JPanel();
    private final int m_id;

    public DataControl dataControl = new DataControl();
    private  MerchantProductFrm merproduct;
    private MerchantOrdersFrm merorder;
    private final Timer initialInfoTimer;
    private final Font fontall = new Font("微软雅黑", Font.BOLD, 18);
    public MerchantInterFrm(int mid) throws SQLException {
        this.m_id = mid;
        initComponents();
     // 创建一个计时器，延迟后触发
        initialInfoTimer = new Timer(500, e -> showInitialInfoPopup());
        initialInfoTimer.setRepeats(false); // 只触发一次
        initialInfoTimer.start();
    }
    public int getM_id() {
        return m_id;
    }

    //主界面的创建
    private void initComponents() throws SQLException {
        setTitle("商家管理界面");
        mainPanel.setLayout(cardLayout);

        JMenuBar menuBar = createMenuBar();
        JMenuBar myInfoMenu = createmyinfoMenuBar();

        JMenuBar combinedMenuBar = new JMenuBar();
        combinedMenuBar.add(menuBar);
        combinedMenuBar.add(myInfoMenu);
        setJMenuBar(combinedMenuBar);

        //第一个界面------------------------------------------------
        JPanel card1 = new JPanel(new BorderLayout());
        merproduct = new MerchantProductFrm(this,dataControl);
       // 创建一个顶部的面板，用于放置按钮
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JButton addProductButton = new JButton("增加商品");
        addProductButton.setFont(fontall);
        addProductButton.addActionListener(e -> {
            Product newproduct = new Product();
            ProductAddDialog detailsDialog = new ProductAddDialog(dataControl,newproduct,this,merproduct);
            detailsDialog.setVisible(true);
        });

        JButton searchProductButton = new JButton("搜索商品");
        searchProductButton.setFont(fontall);

        topPanel.add(searchProductButton);
        topPanel.add(addProductButton);
        card1.add(topPanel,BorderLayout.NORTH);
        card1.add(merproduct, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(card1);// 创建一个 JScrollPane 来包装 card1 面板
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();// 获取垂直滚动条
        verticalScrollBar.setUnitIncrement(30);// 设置单次滚动单位的大小为 20 像素
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);// 设置垂直滚动条自动出现

        mainPanel.add(scrollPane, "card1");

        //第二个界面------------------------------------------------
        JPanel card2 = new JPanel(new BorderLayout());
        JPanel topPanel2 = new JPanel(new GridLayout(1, 1));
        JButton searchorderButton = new JButton("搜索订单");
        searchorderButton.setFont(fontall);
        topPanel2.add(searchorderButton);
        merorder = new MerchantOrdersFrm(dataControl,m_id);

        card2.add(topPanel2,BorderLayout.NORTH);
        card2.add(merorder,BorderLayout.CENTER);

        mainPanel.add(card2, "card2");
        //第三个界面------------------------------------------------
        JPanel card3 = new ForumPage(dataControl.selectMerchant(m_id),"商家");
        mainPanel.add(card3, "card3");

        //搜索商品界面------------------------------------------------
        MerchantProductSearch searchproduct = new MerchantProductSearch(dataControl,this);
        mainPanel.add(searchproduct, "card5");

        //搜索订单界面
        MerchantOrdersSearch searchorder = new MerchantOrdersSearch(dataControl,merorder);
        mainPanel.add(searchorder, "card6");

        // 按钮------------------------------------------------
        // 创建按钮
        JButton upproject = new JButton("商品管理");
        upproject.setFont(fontall);
        JButton order = new JButton("订单");
        order.setFont(fontall);
        JButton forum = new JButton("论坛");
        forum.setFont(fontall);

        // 设置按钮的高度（例如，将高度设置为 50 像素）
        int buttonHeight = 45;
        upproject.setPreferredSize(new Dimension(upproject.getPreferredSize().width, buttonHeight));
        order.setPreferredSize(new Dimension(order.getPreferredSize().width, buttonHeight));
        forum.setPreferredSize(new Dimension(forum.getPreferredSize().width, buttonHeight));

        //设置动作
        searchProductButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card5");
            }
        });

        searchorderButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card6");
            }
        });

        upproject.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card1");
            }
        });

        order.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card2");
            }
        });

        forum.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card3");
            }
        });


        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new GridLayout(1, 4));
        buttonPanel2.add(upproject);
        buttonPanel2.add(order);
        buttonPanel2.add(forum);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel2, BorderLayout.SOUTH);
        //----------------------------------------------------------------
        pack();
        setLocationRelativeTo(getOwner());

    }

    public void refreshCard1() {
        merproduct.removeAll();
        try {
            List<Product> products = dataControl.MerchantProductQuery(getM_id());
            for (Product product : products) {
                JPanel productPanel = merproduct.createProductPanel(product);
                productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                merproduct.add(productPanel);
            }
            merproduct.revalidate();
            merproduct.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshCard2() {
        merorder.refreshData();
    }


    private JMenuBar createMenuBar() {
        // 设置字体和高度
        Font font = new Font("微软雅黑", Font.BOLD, 18);

        int buttonHeight = 40;

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("菜单");
        menu.setFont(font);

        JButton refreshButton1 = new JButton("刷新商品");
        refreshButton1.setFont(font);
        refreshButton1.setPreferredSize(new Dimension(refreshButton1.getPreferredSize().width, buttonHeight));

        JButton refreshButton2 = new JButton("刷新订单");
        refreshButton2.setFont(font);
        refreshButton2.setPreferredSize(new Dimension(refreshButton2.getPreferredSize().width, buttonHeight));

        JButton refreshButton3 = new JButton("刷新用户信息");
        refreshButton3.setFont(font);
        refreshButton3.setPreferredSize(new Dimension(refreshButton3.getPreferredSize().width, buttonHeight));

        JButton refreshButton4 = new JButton("退出");
        refreshButton4.setFont(font);
        refreshButton4.setBackground(Color.red);
        refreshButton4.setPreferredSize(new Dimension(refreshButton4.getPreferredSize().width, buttonHeight));
        refreshButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                refreshCard1();
            }
        });
        refreshButton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                refreshCard2();
            }
        });
        refreshButton3.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showMerchantInfoDialog();
            }
        });
        refreshButton4.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    closeAndOpenLogin(); // 点击退出按钮时关闭当前窗口并打开登录窗口
                } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                         IllegalAccessException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        buttonPanel.add(refreshButton1);
        buttonPanel.add(refreshButton2);
        buttonPanel.add(refreshButton3);// 添加新的刷新用户信息按钮
        buttonPanel.add(refreshButton4);
        menu.add(buttonPanel);
        menuBar.add(menu);
        return menuBar;
    }

    private void closeAndOpenLogin() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        this.dispose(); // 关闭当前窗口
        login loginFrm = new login(); // 创建一个新的登录窗口
        loginFrm.setLocationRelativeTo(null); // 将登录窗口设置为居中显示
        loginFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrm.setSize(500, 400);
        loginFrm.setVisible(true);
    }

    private JMenuBar createmyinfoMenuBar() {
        Font font = new Font("微软雅黑", Font.BOLD, 18);

        int buttonHeight = 40;

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("我的");
        menu.setFont(font);

        JButton refreshButton1 = new JButton("我的信息");
        refreshButton1.setFont(font);
        refreshButton1.setPreferredSize(new Dimension(refreshButton1.getPreferredSize().width, buttonHeight));

        JButton refreshButton2 = new JButton("修改信息");
        refreshButton2.setFont(font);
        refreshButton2.setPreferredSize(new Dimension(refreshButton2.getPreferredSize().width, buttonHeight));

        refreshButton1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showMerchantInfoDialog(); // 显示我的信息界面
            }
        });

        refreshButton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showMerchantInfoModifyDialog(); // 弹出修改信息对话框
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(2, 0));
        buttonPanel.add(refreshButton1);
        buttonPanel.add(refreshButton2);
        menu.add(buttonPanel);
        menuBar.add(menu);
        return menuBar;
    }
    private void showMerchantInfoDialog() {
        try {
            MerchantInfo merchantInfo = new MerchantInfo(dataControl, m_id);
            merchantInfo.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void showMerchantInfoModifyDialog() {
        try {
            Merchant merchant = dataControl.MerchantQuery(m_id);
            MerchantInfoModifyDialog modifyDialog = new MerchantInfoModifyDialog(merchant, dataControl, m_id);
            modifyDialog.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showInitialInfoPopup() {
        JOptionPane.showMessageDialog(this, "点击图片查看更多", "提示", JOptionPane.INFORMATION_MESSAGE);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MerchantInterFrm frame;
            try {
                frame = new MerchantInterFrm(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            frame.setLocationRelativeTo(null); // 居中显示
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 1000);
            frame.setVisible(true);
        });
    }
}

