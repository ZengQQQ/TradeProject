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

        JMenuBar menuBar = createMenuBar();
        JMenuBar myInfoMenu = createmyinfoMenuBar();

        JMenuBar combinedMenuBar = new JMenuBar();
        combinedMenuBar.add(menuBar);
        combinedMenuBar.add(myInfoMenu);
        setJMenuBar(combinedMenuBar);

        JPanel card1 = new JPanel(new BorderLayout());
        merproduct = new MerchantProductFrm(this);

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JButton addProductButton = new JButton("增加商品");
        addProductButton.setFont(fontall);
        addProductButton.addActionListener(e -> {
            Product newproduct = new Product();
            ProductAddDialog detailsDialog;
            try {
                detailsDialog = new ProductAddDialog(newproduct, this, merproduct);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            detailsDialog.setVisible(true);
        });

        JButton searchProductButton = new JButton("搜索商品");
        searchProductButton.setFont(fontall);

        topPanel.add(searchProductButton);
        topPanel.add(addProductButton);
        card1.add(topPanel, BorderLayout.NORTH);
        card1.add(merproduct, BorderLayout.CENTER);
        JScrollPane scrollPane = new JScrollPane(card1);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(30);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        mainPanel.add(scrollPane, "card1");

        JPanel card2 = new JPanel(new BorderLayout());
        JPanel topPanel2 = new JPanel(new GridLayout(1, 1));
        JButton searchorderButton = new JButton("搜索订单");
        searchorderButton.setFont(fontall);
        topPanel2.add(searchorderButton);
        merorder = new MerchantOrdersFrm(m_id);

        card2.add(topPanel2, BorderLayout.NORTH);
        card2.add(merorder, BorderLayout.CENTER);

        mainPanel.add(card2, "card2");

        JPanel card3 = new ForumPage(dataControl.selectMerchant(m_id), "商家");
        mainPanel.add(card3, "card3");

        MerchantProductSearch searchproduct = new MerchantProductSearch(this);
        mainPanel.add(searchproduct, "card5");

        MerchantOrdersSearch searchorder = new MerchantOrdersSearch(merorder);
        mainPanel.add(searchorder, "card6");

        JButton upproject = new JButton("商品管理");
        upproject.setFont(fontall);
        JButton order = new JButton("订单");
        order.setFont(fontall);
        JButton forum = new JButton("论坛");
        forum.setFont(fontall);

        int buttonHeight = 45;
        upproject.setPreferredSize(new Dimension(upproject.getPreferredSize().width, buttonHeight));
        order.setPreferredSize(new Dimension(order.getPreferredSize().width, buttonHeight));
        forum.setPreferredSize(new Dimension(forum.getPreferredSize().width, buttonHeight));

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

        pack();
        setLocationRelativeTo(getOwner());
    }

    public void refreshCard1() {
        merproduct.productsPanel.removeAll();
        try {
            List<Product> products = dataControlmer.MerchantProductQuery(getM_id());
            for (Product product : products) {
                JPanel productPanel = merproduct.createProductPanel(product);
                productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                merproduct.productsPanel.add(productPanel);
            }
            merproduct.productsPanel.revalidate();
            merproduct.productsPanel.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void refreshCard2() {
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
            frame.setSize(1200, 1000);
            frame.setVisible(true);
        });
    }
}
