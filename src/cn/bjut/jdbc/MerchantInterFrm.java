package cn.bjut.jdbc;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class MerchantInterFrm extends JFrame {
    private JButton searchButton;
    private JButton upproject;
    private JButton downproject;
    private JButton evaluate;
    private JButton myButton;

    private JPanel card1; // 用于显示商品的卡片

    public MerchantInterFrm() {
        initComponents();
    }

    private void initComponents() {
        //主界面的创建
        JPanel mainPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);

        card1 = new JPanel();
        card1.setLayout(new GridLayout(0, 1)); // 垂直布局
        // 在第一个界面中显示所有商品
        try {
            DataControl dataControl = new DataControl();
            List<Product> products = dataControl.MerchantProductQuery(1); // 替换为您的商家ID
            for (Product product : products) {
                JPanel productPanel = createProductPanel(product);
                card1.add(productPanel, BorderLayout.AFTER_LINE_ENDS);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainPanel.add(card1, "card1");

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

        mainPanel.add(card2, "card2");
        mainPanel.add(card3, "card3");
        mainPanel.add(card4, "card4");
        mainPanel.add(card5, "card5");



        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // 按钮
        searchButton = new JButton("搜索");
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card5");
            }
        });

        upproject = new JButton("商品管理");
        upproject.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card1");
            }
        });

        downproject = new JButton("暂定");
        downproject.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card2");
            }
        });

        evaluate = new JButton("评价");
        evaluate.addMouseListener(new MouseAdapter() {
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
        buttonPanel.add(upproject);
        buttonPanel.add(downproject);
        buttonPanel.add(evaluate);
        buttonPanel.add(myButton);

        contentPane.add(searchButton, BorderLayout.NORTH);
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);



        pack();
        setLocationRelativeTo(getOwner());


    }

    private JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel();

        productPanel.setLayout(new BorderLayout());

        // 显示图片
        String relativeImagePath = product.getP_img();
        ClassLoader classLoader = getClass().getClassLoader();
        URL imageURL = classLoader.getResource(relativeImagePath);
        if (imageURL != null) {
            ImageIcon originalIcon = new ImageIcon(imageURL);
            // 调整图片大小
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            JLabel imageLabel = new JLabel(scaledIcon);
            productPanel.add(imageLabel, BorderLayout.WEST);
        }

        // 创建商品信息面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BorderLayout());

        // 添加商品相关信息，例如商品名称、描述等
        // 创建商品信息标签
        JLabel infoLabel = new JLabel("商品名称: " + product.getP_name() + " | 商品描述: " + product.getP_desc());

        // 其他商品信息的标签，您可以根据需要添加更多

        // 将商品信息标签添加到 infoPanel
        infoPanel.add(infoLabel, BorderLayout.CENTER);
        // 将 infoPanel 添加到 productPanel
        productPanel.add(infoPanel, BorderLayout.CENTER);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout()); // 使用 FlowLayout 排列按钮

        // 创建上架和下架按钮
        JButton upButton = new JButton("上架");
        JButton downButton = new JButton("下架");
        JButton detailsButton = new JButton("详情");

        // 将按钮添加到按钮面板
        buttonPanel.add(upButton);
        buttonPanel.add(downButton);
        buttonPanel.add(detailsButton);

        // 将按钮面板添加到 productPanel
        productPanel.add(buttonPanel, BorderLayout.EAST);

        return productPanel;
    }

    public static void main(String[] args) throws SQLException {
        MerchantInterFrm frame = new MerchantInterFrm();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 900);
        frame.setVisible(true);
    }
}
