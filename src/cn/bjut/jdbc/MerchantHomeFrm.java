package cn.bjut.jdbc;

import javax.swing.*;

import javax.swing.border.Border;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;


public class MerchantHomeFrm extends JPanel {
    private MerchantInterFrm mer;
    private int weight = 150;
    private int height = 100;
    private int borderWidth = 2; // 增加边框宽度
    private DataControlMercahnt dataControlmercahnt = new DataControlMercahnt();
    private DataControlProduct dataControlproduct = new DataControlProduct();

    public MerchantHomeFrm(MerchantInterFrm mer) throws SQLException {
        this.mer = mer;
        initComponents();
    }

    public void initComponents() throws SQLException {
        setLayout(new GridLayout(2, 1, 40, 40));

        JPanel mainPanel = new JPanel(new GridLayout(2, 3, 30, 30)); // 创建主面板，使用2行3列的GridLayout，设置行列间距为30
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50)); // 添加主面板的外边距
        mainPanel.setPreferredSize(new Dimension(300, 200));
        Border border = new LineBorder(Color.BLACK, borderWidth, true); // 创建带有圆角的边框

        Font font1 = new Font("微软雅黑", Font.ROMAN_BASELINE, 30);
        Font font2 = new Font("微软雅黑", Font.CENTER_BASELINE, 40);

        List<Integer> merchantStats = dataControlmercahnt.getMerchantStats(mer.getM_id());
        JPanel innerPanel1 = new JPanel(new BorderLayout()); // 创建每个内部面板1
        innerPanel1.setPreferredSize(new Dimension(weight, height)); // 设置内部面板1的大小
        innerPanel1.setBorder(border); // 设置内部面板1的边框为带有圆角的边框

        JLabel topLeftLabel1 = new JLabel("   总收入（￥）", SwingConstants.LEFT); // 创建左上角的标签
        topLeftLabel1.setFont(font1); // 设置左上角标签的字体为20号
        innerPanel1.add(topLeftLabel1, BorderLayout.NORTH); // 在左上角添加文字

        JLabel bottomCenterLabel1 = new JLabel(String.valueOf(merchantStats.get(0)), SwingConstants.CENTER); // 创建底部中间的标签
        bottomCenterLabel1.setFont(font2); // 设置底部中间标签的字体为30号
        innerPanel1.add(bottomCenterLabel1, BorderLayout.SOUTH); // 在底部中间添加文字

        mainPanel.add(innerPanel1); // 将内部面板1添加到主面板

        JPanel innerPanel2 = new JPanel(new BorderLayout());
        innerPanel2.setPreferredSize(new Dimension(weight, height));
        innerPanel2.setBorder(border);

        JLabel topLeftLabel2 = new JLabel("   今日收入（￥）", SwingConstants.LEFT);
        topLeftLabel2.setFont(font1);
        innerPanel2.add(topLeftLabel2, BorderLayout.NORTH);

        JLabel bottomCenterLabel2 = new JLabel(String.valueOf(merchantStats.get(1)), SwingConstants.CENTER);
        bottomCenterLabel2.setFont(font2);
        innerPanel2.add(bottomCenterLabel2, BorderLayout.SOUTH);

        mainPanel.add(innerPanel2);

        JPanel innerPanel3 = new JPanel(new BorderLayout());
        innerPanel3.setPreferredSize(new Dimension(weight, height));
        innerPanel3.setBorder(border);

        JLabel topLeftLabel3 = new JLabel("   总商品数", SwingConstants.LEFT);
        topLeftLabel3.setFont(font1);
        innerPanel3.add(topLeftLabel3, BorderLayout.NORTH);

        JLabel bottomCenterLabel3 = new JLabel(String.valueOf(merchantStats.get(2)), SwingConstants.CENTER);
        bottomCenterLabel3.setFont(font2);
        innerPanel3.add(bottomCenterLabel3, BorderLayout.SOUTH);

        mainPanel.add(innerPanel3);
        JPanel innerPanel4 = new JPanel(new BorderLayout());
        innerPanel4.setPreferredSize(new Dimension(weight, height));
        innerPanel4.setBorder(border);

        JLabel topLeftLabel4 = new JLabel("   总订单数", SwingConstants.LEFT);
        topLeftLabel4.setFont(font1);
        innerPanel4.add(topLeftLabel4, BorderLayout.NORTH);

        JLabel bottomCenterLabel4 = new JLabel(String.valueOf(merchantStats.get(4)), SwingConstants.CENTER);
        bottomCenterLabel4.setFont(font2);
        innerPanel4.add(bottomCenterLabel4, BorderLayout.SOUTH);

        mainPanel.add(innerPanel4);

        JPanel innerPanel5 = new JPanel(new BorderLayout());
        innerPanel5.setPreferredSize(new Dimension(weight, height));
        innerPanel5.setBorder(border);

        JLabel topLeftLabel5 = new JLabel("   今日订单数", SwingConstants.LEFT);
        topLeftLabel5.setFont(font1);
        innerPanel5.add(topLeftLabel5, BorderLayout.NORTH);

        JLabel bottomCenterLabel5 = new JLabel(String.valueOf(merchantStats.get(4)), SwingConstants.CENTER);
        bottomCenterLabel5.setFont(font2);
        innerPanel5.add(bottomCenterLabel5, BorderLayout.SOUTH);

        mainPanel.add(innerPanel5);

        // 获取商家的商品信息（假设该函数可以获取商品信息列表）
        List<Product> productList = dataControlproduct.findProductsByHighestQuantity(mer.getM_id());
        List<Product> productList2 = dataControlproduct.findProductsByHighestQuantityToday(mer.getM_id());

        JPanel productPanel = new JPanel(); // 商品信息面板
        productPanel.setLayout(new GridLayout(0, 2, 30, 40)); // 垂直布局

        // 添加每个商品信息面板到主面板
        for (Product product : productList) {
            JPanel highproductPanel = createProductPanel(product); // 创建每个商品信息面板
            highproductPanel.setSize(300, 450);
            productPanel.add(highproductPanel); // 将商品信息面板添加到主面板

        }
        // 添加每个商品信息面板到主面板
        for (Product product : productList2) {
            JPanel highproductPanel2 = createProductPanel2(product); // 创建每个商品信息面板
            highproductPanel2.setSize(300, 450);
            productPanel.add(highproductPanel2); // 将商品信息面板添加到主面板

        }
        // 将商品信息面板放置在带有垂直滚动条的 JScrollPane 中
//        JScrollPane productScrollPane = new JScrollPane(productPanel);
//        JScrollBar verticalScrollBar = productScrollPane.getVerticalScrollBar();
//        verticalScrollBar.setUnitIncrement(20);
//        productScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
////        productScrollPane.setPreferredSize(new Dimension(300, 350));

        add(mainPanel); // 将主面板添加到北部
        add(productPanel);

    }

    // 创建单个商品信息面板
    private JPanel createProductPanel(Product product) {
        JPanel mainPanel = new JPanel(); // 主面板
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        // 商品信息面板
        JPanel productPanel = new JPanel(new BorderLayout()); // 商品信息面板采用边界布局
        productPanel.setPreferredSize(new Dimension(300, 350)); // 设置固定大小
        JTextArea productInfo = new JTextArea(getProductInfo(product)); // 创建商品信息文本区域
        productInfo.setEditable(false);
        productInfo.setFont(new Font("微软雅黑", Font.PLAIN, 20)); // 设置字体
        productInfo.setLineWrap(true); // 允许自动换行
        productPanel.add(productInfo, BorderLayout.CENTER); // 将商品信息文本区域放置在中心
        TitledBorder innerBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "总销量最高商品信息");
        Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        innerBorder.setTitleFont(new Font("微软雅黑", Font.BOLD, 20));
        productPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        // 商品图片面板
        JPanel imgPanel = new JPanel(new BorderLayout()); // 商品图片面板采用边界布局
        JLabel imageLabel = createImageLabel(product, 300, 320); // 创建商品图片标签
        imgPanel.add(imageLabel, BorderLayout.CENTER); // 将商品图片标签放置在中心

        productPanel.add(imgPanel, BorderLayout.WEST); // 将商品图片面板放置在商品信息面板的右侧

        mainPanel.add(productPanel); // 将商品信息面板添加到主面板中

        return mainPanel; // 返回商品信息面板
    }

    private JPanel createProductPanel2(Product product) {
        JPanel mainPanel = new JPanel(); // 主面板
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));

        // 商品信息面板
        JPanel productPanel = new JPanel(new BorderLayout()); // 商品信息面板采用边界布局
        productPanel.setPreferredSize(new Dimension(300, 350)); // 设置固定大小
        JTextArea productInfo = new JTextArea(getProductInfo(product)); // 创建商品信息文本区域
        productInfo.setEditable(false);
        productInfo.setFont(new Font("微软雅黑", Font.PLAIN, 20)); // 设置字体
        productInfo.setLineWrap(true); // 允许自动换行
        productPanel.add(productInfo, BorderLayout.CENTER); // 将商品信息文本区域放置在中心

        TitledBorder innerBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "今日销量最高商品信息");
        Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        innerBorder.setTitleFont(new Font("微软雅黑", Font.BOLD, 20));
        productPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        // 商品图片面板
        JPanel imgPanel = new JPanel(new BorderLayout()); // 商品图片面板采用边界布局
        JLabel imageLabel = createImageLabel(product, 300, 320); // 创建商品图片标签
        imgPanel.add(imageLabel, BorderLayout.CENTER); // 将商品图片标签放置在中心

        productPanel.add(imgPanel, BorderLayout.WEST); // 将商品图片面板放置在商品信息面板的右侧

        mainPanel.add(productPanel); // 将商品信息面板添加到主面板中

        return mainPanel; // 返回商品信息面板
    }

    private JLabel createImageLabel(Product product, int width, int height) {
        String projectPath = System.getProperty("user.dir");
        ImageIcon originalIcon = getImageIcon(product, projectPath);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        return new JLabel(scaledIcon);
    }

    private ImageIcon getImageIcon(Product product, String projectPath) {
        String relativeImagePath = product.getP_img();
        String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + relativeImagePath;
        ImageIcon originalIcon;
        File imageFile = new File(absoluteImagePath);
        if (imageFile.exists()) {
            originalIcon = new ImageIcon(absoluteImagePath);
        } else {
            String defaultImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + "R.jpg";
            originalIcon = new ImageIcon(defaultImagePath);
        }
        return originalIcon;
    }

    private String getProductInfo(Product product) {
        String productId = "商品ID: " + product.getP_id() + "\n";
        String productName = "名称: " + product.getP_name() + "\n";
        String productdescription = "描述: " + product.getP_description() + "\n";
        String productClass = "分类: " + product.getP_class() + "\n";
        String productPrice = "价格: " + product.getP_price() + "\n";
        String productstatus = "状态: " + product.getP_status() + "\n";
        String productquantity = "数量: " + product.getP_quantity() + "\n";
        String sale = "销量: " + product.getSales() + "\n";
        String productauditstatus = "审核状态: " + product.getP_audiStatus() + "\n";
        return productId + productName + productdescription + productClass + productPrice + productstatus + productquantity + sale + productauditstatus;
    }
}


