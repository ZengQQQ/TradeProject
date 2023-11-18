package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class MerchantInterFrm extends JFrame {

    private final CardLayout cardLayout = new CardLayout();
    public JPanel mainPanel = new JPanel();
    private final int m_id;

    public DataControl dataControl = new DataControl();
    private DataControlMercahnt dataControlmer = new DataControlMercahnt();
    private MerchantProductFrm merproduct;
    private MerchantOrdersFrm merorder;
    private final Timer initialInfoTimer;
    private final Font fontall = new Font("微软雅黑", Font.BOLD, 18);

    public MerchantInterFrm(int mid) throws SQLException {
        this.m_id = mid;
        initComponents();
        initialInfoTimer = new Timer(500, e -> showInitialInfoPopup());
        initialInfoTimer.setRepeats(false);
        initialInfoTimer.start();
    }

    public int getM_id() {
        return m_id;
    }

    private void initComponents() throws SQLException {
        setTitle("商家管理界面");
        mainPanel.setLayout(cardLayout);
        //----------------------------------------------------------------
        JMenuBar menuBar = createMenuBar();
        JMenuBar myInfoMenu = createmyinfoMenuBar();

        JMenuBar combinedMenuBar = new JMenuBar();
        combinedMenuBar.add(menuBar);
        combinedMenuBar.add(myInfoMenu);
        setJMenuBar(combinedMenuBar);
        //----------------------------------------------------------------
        JPanel card1 = new JPanel(new BorderLayout());

        JLabel homeLabel = new JLabel("这里是首页，您可以放置首页的内容。");
        homeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        homeLabel.setFont(new Font("微软雅黑", Font.BOLD, 18));
        card1.add(homeLabel, BorderLayout.CENTER);

        mainPanel.add(card1, "card1");
        //----------------------------------------------------------------
        JPanel card2 = new JPanel(new BorderLayout());
        merproduct = new MerchantProductFrm(this);

        JPanel topPanel = new JPanel(new GridLayout(1, 2));


        JButton searchProductButton = new JButton("搜索商品");
        searchProductButton.setFont(fontall);

        topPanel.add(searchProductButton);


        card2.add(topPanel, BorderLayout.NORTH);
        card2.add(merproduct, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane(card2);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(30);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(scrollPane, "card2");
        //----------------------------------------------------------------
        JPanel card3 = new JPanel(new BorderLayout());

        JPanel topPanel2 = new JPanel(new GridLayout(1, 1));
        JButton searchorderButton = new JButton("搜索订单");
        searchorderButton.setFont(fontall);
        topPanel2.add(searchorderButton);
        merorder = new MerchantOrdersFrm(m_id);

        card3.add(topPanel2, BorderLayout.NORTH);
        card3.add(merorder, BorderLayout.CENTER);

        mainPanel.add(card3, "card3");
        //----------------------------------------------------------------
        JPanel card4 = new ForumPage(dataControl.selectMerchant(m_id), "商家");
        mainPanel.add(card4, "card4");
        //----------------------------------------------------------------

        MerchantProductSearch searchproduct = new MerchantProductSearch(this);
        mainPanel.add(searchproduct, "card5");
        //----------------------------------------------------------------
        MerchantOrdersSearch searchorder = new MerchantOrdersSearch(merorder);
        mainPanel.add(searchorder, "card6");
        //----------------------------------------------------------------
        JButton homeButton = new JButton("首页");
        homeButton.setFont(fontall);
        JButton productButton = new JButton("商品管理");
        productButton.setFont(fontall);
        JButton orderButton = new JButton("订单");
        orderButton.setFont(fontall);
        JButton forumButton = new JButton("论坛");
        forumButton.setFont(fontall);

        int buttonHeight = 45;
        homeButton.setPreferredSize(new Dimension(homeButton.getPreferredSize().width, buttonHeight));
        productButton.setPreferredSize(new Dimension(productButton.getPreferredSize().width, buttonHeight));
        orderButton.setPreferredSize(new Dimension(orderButton.getPreferredSize().width, buttonHeight));
        forumButton.setPreferredSize(new Dimension(forumButton.getPreferredSize().width, buttonHeight));

        //----------------------------------------------------------------
        homeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card1");
            }
        });
        productButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card2");
            }
        });

        orderButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card3");
            }
        });

        forumButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card4");
            }
        });
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
        //----------------------------------------------------------------
        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new GridLayout(1, 4));
        buttonPanel2.add(homeButton); // 在按钮面板的最左边添加首页按钮
        buttonPanel2.add(productButton);
        buttonPanel2.add(orderButton);
        buttonPanel2.add(forumButton);
        //----------------------------------------------------------------
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel2, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getOwner());
    }

    public void refreshProducts() {
        try {
            List<Product> products = dataControlmer.MerchantProductQuery(m_id);
            merproduct.tableModel.setRowCount(0); // 清空表格数据
            for (Product product : products) {
                Object[] rowData = {product.getP_id(), merproduct.getScaledImageIcon(product, 250, 200), product.getP_name(), product.getP_price(), product.getP_quantity(), product.getP_status(), product.getP_audiStatus()};
                merproduct.tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void refreshCard2() {
        try {
            List<Product> products = dataControlmer.MerchantProductQuery(m_id);
            merproduct.tableModel.setRowCount(0); // 清空表格数据
            for (Product product : products) {
                Object[] rowData = {product.getP_id(), merproduct.getScaledImageIcon(product, 250, 200), product.getP_name(), product.getP_price(), product.getP_quantity(), product.getP_status(), product.getP_audiStatus()};
                merproduct.tableModel.addRow(rowData);
                merproduct.categoryComboBox.setSelectedItem("全部类别"); // 设置默认选择
            }
            JOptionPane.showMessageDialog(this, "刷新成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "刷新失败！", "提示", JOptionPane.INFORMATION_MESSAGE);
            e.printStackTrace();
        }
    }

    public void refreshCard3() {
        merorder.refreshData();
    }

    private JMenuBar createMenuBar() {
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
                refreshCard2();
            }
        });

        refreshButton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                refreshCard3();
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
                    closeAndOpenLogin();
                } catch (UnsupportedLookAndFeelException | ClassNotFoundException | InstantiationException |
                         IllegalAccessException | SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));
        buttonPanel.add(refreshButton1);
        buttonPanel.add(refreshButton2);
        buttonPanel.add(refreshButton3);
        buttonPanel.add(refreshButton4);
        menu.add(buttonPanel);
        menuBar.add(menu);
        return menuBar;
    }

    private void closeAndOpenLogin() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        this.dispose();
        login loginFrm = new login();
        loginFrm.setLocationRelativeTo(null);
        loginFrm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrm.setSize(900, 600);
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
                showMerchantInfoDialog();
            }
        });

        refreshButton2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showMerchantInfoModifyDialog();
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
            MerchantInfo merchantInfo = new MerchantInfo(m_id);
            merchantInfo.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showMerchantInfoModifyDialog() {
        try {
            Merchant merchant = dataControlmer.MerchantQuery(m_id);
            MerchantInfoModifyDialog modifyDialog = new MerchantInfoModifyDialog(merchant, m_id);
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
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1500, 1300);
            frame.setVisible(true);
        });
    }
}
