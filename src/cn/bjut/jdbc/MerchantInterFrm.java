package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

public class MerchantInterFrm extends JFrame {
    private JButton searchproductButton = new JButton("搜索商品");
    private final CardLayout cardLayout = new CardLayout();
    public  JPanel mainPanel = new JPanel();
    private final int m_id;

    public DataControl dataControl = new DataControl();
    private  MerchantProductFrm merproduct;
    private MerchantOrdersFrm merorder;

    public MerchantInterFrm(int mid) throws SQLException {
        this.m_id = mid;
        initComponents();
    }
    public int getM_id() {
        return m_id;
    }

    //主界面的创建
    private void initComponents() throws SQLException {
        setTitle("商家管理界面");
        mainPanel.setLayout(cardLayout);
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        //第一个界面------------------------------------------------
        JPanel card1 = new JPanel(new BorderLayout());
        merproduct = new MerchantProductFrm(this,dataControl);
        // 创建一个顶部的面板，用于放置按钮
        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JButton addProductButton = new JButton("增加商品");
        addProductButton.addActionListener(e -> {
            Product newproduct = new Product();
            ProductAddDialog detailsDialog = new ProductAddDialog(dataControl,newproduct,this,merproduct);
            detailsDialog.setVisible(true);
        });

        topPanel.add(searchproductButton);
        topPanel.add(addProductButton);
        // 将搜索按钮和商品展示部分添加到MerchantProductFrm
        card1.add(topPanel, BorderLayout.NORTH);
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
        topPanel2.add(searchorderButton);
        merorder = new MerchantOrdersFrm(dataControl,m_id);

        card2.add(topPanel2,BorderLayout.NORTH);
        card2.add(merorder,BorderLayout.CENTER);

        mainPanel.add(card2, "card2");
        //第三个界面------------------------------------------------
        JPanel card3 = new ForumPage(dataControl.selectMerchant(m_id),"商家");
        mainPanel.add(card3, "card3");
        //第四个界面------------------------------------------------
        JPanel card4JPanel = getCard4(m_id);
        // 将“我的信息”面板添加到 mainPanel
        mainPanel.add(card4JPanel, "card4");

        //搜索商品界面------------------------------------------------
        MerchantProductSearch searchproduct = new MerchantProductSearch(dataControl,this);
        mainPanel.add(searchproduct, "card5");

        //搜索订单界面
        MerchantOrdersSearch searchorder = new MerchantOrdersSearch(dataControl,merorder);
        mainPanel.add(searchorder, "card6");

        // 按钮------------------------------------------------
        // 创建按钮

        JButton upproject = new JButton("商品管理");
        JButton downproject = new JButton("订单");
        JButton evaluate = new JButton("论坛");
        JButton myButton = new JButton("我的");

        // 设置按钮的高度（例如，将高度设置为 50 像素）
        int buttonHeight = 45;
        upproject.setPreferredSize(new Dimension(upproject.getPreferredSize().width, buttonHeight));
        downproject.setPreferredSize(new Dimension(downproject.getPreferredSize().width, buttonHeight));
        evaluate.setPreferredSize(new Dimension(evaluate.getPreferredSize().width, buttonHeight));
        myButton.setPreferredSize(new Dimension(myButton.getPreferredSize().width, buttonHeight));

        //设置动作
        searchproductButton.addMouseListener(new MouseAdapter() {
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

        downproject.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card2");
            }
        });

        evaluate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card3");
            }
        });

        myButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card4");
            }
        });

        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new GridLayout(1, 4));
        buttonPanel2.add(upproject);
        buttonPanel2.add(downproject);
        buttonPanel2.add(evaluate);
        buttonPanel2.add(myButton);

        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel2, BorderLayout.SOUTH);
        //----------------------------------------------------------------
        pack();
        setLocationRelativeTo(getOwner());
    }

    private JPanel getCard4(int m_id) throws SQLException {
        JPanel card4 = new JPanel(new GridBagLayout());
        GridBagConstraints card4gbc = new GridBagConstraints();
        card4gbc.insets = new Insets(1, 1, 5, 5);
        card4gbc.gridx = 0;
        card4gbc.gridy = 0;
        card4gbc.anchor = GridBagConstraints.NORTH;
        //获取商家信息
        Merchant merchant =dataControl.MerchantQuery(m_id);
        // 创建一个“修改”按钮
        JButton modifyButton = new JButton("修改信息");
        modifyButton.addActionListener(e -> {
            // 打开一个对话框以修改信息
            MerchantInfoModifyDialog modifyDialog = new MerchantInfoModifyDialog(merchant);
            modifyDialog.setVisible(true);
        });
        // 将标签和文本字段添加到 card4 面板
        card4.add(new JLabel("账号名: "+ merchant.getAcc()), card4gbc);
        card4gbc.gridy++;
        card4.add(new JLabel("昵称: "+ merchant.getM_name()), card4gbc);
        card4gbc.gridy++;
        card4.add(new JLabel("性别: "+ merchant.getM_sex()), card4gbc);
        card4gbc.gridy++;
        card4.add(new JLabel("电话: "+merchant.getM_tele()), card4gbc);
        // 将“修改”按钮添加到信息下方
        card4gbc.gridx = 1;
        card4.add(modifyButton, card4gbc);
        return card4;
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
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("菜单");

        JButton refreshButton1 = new JButton("刷新商品");
        JButton refreshButton2 = new JButton("刷新订单");

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

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1));
        buttonPanel.add(refreshButton1);
        buttonPanel.add(refreshButton2); // Add the "刷新订单" button
        menu.add(buttonPanel);
        menuBar.add(menu);
        return menuBar;
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MerchantInterFrm frame;
            try {
                frame = new MerchantInterFrm(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 1000);
            frame.setVisible(true);
        });
    }
}

