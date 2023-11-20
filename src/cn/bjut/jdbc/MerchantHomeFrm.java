package cn.bjut.jdbc;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;


public class MerchantHomeFrm extends JPanel {
    private MerchantInterFrm mer;
    private int weight = 200;
    private int height = 200;
    private int borderWidth = 2; // 增加边框宽度
    private DataControlMercahnt dataControlmercahnt = new DataControlMercahnt();
    private DataControlProduct dataControlproduct = new DataControlProduct();

    public MerchantHomeFrm(MerchantInterFrm mer) throws SQLException {
        this.mer = mer;
        initComponents();
    }

    public void initComponents() throws SQLException {
        setLayout(new BorderLayout());

        Image backgroundImage = loadImageForPanel("Img/innerphoto.jpg"); // 背景图片
        ImagePanel mainPanel = new ImagePanel(backgroundImage); // 带有背景图片的主面板
        mainPanel.setLayout(new BorderLayout());

        JPanel containerPanel = new JPanel();
        containerPanel.setLayout(new BorderLayout());


        JPanel productPanel = createProductPanel();

        ClassLoader classLoader = getClass().getClassLoader();
        Image refreshImage = new ImageIcon(classLoader.getResource("Img/refresh.png")).getImage();
        Image scaledRefreshImage = refreshImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        Icon refreshIcon = new ImageIcon(scaledRefreshImage);
        JButton refreshButton = new JButton("刷新首页",refreshIcon);
        refreshButton.setFont(new Font("微软雅黑", Font.BOLD, 30));
        refreshButton.addActionListener(e -> {
            try {
                refreshHomePage();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });

        JPanel refreshPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        refreshPanel.add(refreshButton);

        mainPanel.add(containerPanel, BorderLayout.NORTH);
        containerPanel.add(createMainPanel(), BorderLayout.NORTH);
        containerPanel.add(productPanel, BorderLayout.CENTER);
        containerPanel.add(refreshPanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);
    }
    // 刷新首页的方法
    private void refreshHomePage() throws SQLException {
        removeAll();
        initComponents(); // 重新初始化组件
        revalidate(); // 重新验证布局
        repaint(); // 重新绘制组件
    }
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridLayout(2, 3, 30, 30));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50));
        mainPanel.setPreferredSize(new Dimension(300, 300));

        List<Integer> merchantStats = dataControlmercahnt.getMerchantStats(mer.getM_id());

        Image bgImage1 = loadImageForPanel("Img/innerphoto.jpg");
        Image bgImage2 = loadImageForPanel("Img/innerphoto.jpg");
        Image bgImage3 = loadImageForPanel("Img/innerphoto.jpg");
        Image bgImage4 = loadImageForPanel("Img/innerphoto.jpg");
        Image bgImage5 = loadImageForPanel("Img/innerphoto.jpg");
        Image bgImage6 = loadImageForPanel("Img/innerphoto.jpg");

        mainPanel.add(createInnerPanel("总收入（￥）", String.valueOf(merchantStats.get(0)), bgImage1));
        mainPanel.add(createInnerPanel("今日收入（￥）",String.valueOf(merchantStats.get(1)), bgImage2));
        mainPanel.add(createInnerPanel("总商品数", String.valueOf(merchantStats.get(2)), bgImage3));
        mainPanel.add(createInnerPanel("总订单数", String.valueOf(merchantStats.get(3)), bgImage4));
        mainPanel.add(createInnerPanel("今日订单数", String.valueOf(merchantStats.get(4)), bgImage5));
        mainPanel.add(createInnerPanel("待退货、已退货订单数", String.valueOf(merchantStats.get(5)), bgImage6));

        return mainPanel;
    }

    private JPanel createInnerPanel(String topLeftLabelText, String bottomCenterLabelText, Image backgroundImage) {
        JPanel innerPanel = new ImagePanel(backgroundImage);
        innerPanel.setLayout(new BorderLayout());
        innerPanel.setPreferredSize(new Dimension(weight, height));
        innerPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, borderWidth, true));

        JLabel topLeftLabel = new JLabel("   " + topLeftLabelText, SwingConstants.LEFT);
        topLeftLabel.setFont(new Font("微软雅黑", Font.ROMAN_BASELINE, 30));
        innerPanel.add(topLeftLabel, BorderLayout.NORTH);

        JLabel bottomCenterLabel = new JLabel("   " + bottomCenterLabelText, SwingConstants.CENTER);
        bottomCenterLabel.setFont(new Font("微软雅黑", Font.CENTER_BASELINE, 40));
        innerPanel.add(bottomCenterLabel, BorderLayout.SOUTH);

        return innerPanel;
    }
    private Image loadImageForPanel(String imagePath) {
        Image bgImage = null;
        try {
            // 使用类加载器加载图片资源
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(imagePath);
            if (inputStream != null) {
                bgImage = ImageIO.read(inputStream);
            } else {
                // 图片资源未找到
                System.out.println("Image not found: " + imagePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
            // 处理图片加载异常
        }
        return bgImage;
    }

    class ImagePanel extends JPanel {
        private Image backgroundImage;

        public ImagePanel(Image backgroundImage) {
            this.backgroundImage = backgroundImage;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
    private JPanel createProductPanel() throws SQLException {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new GridLayout(0, 2, 30, 40));
        List<Product> productList = dataControlproduct.findProductsByHighestQuantity(mer.getM_id());
        List<Product> productList2 = dataControlproduct.findProductsByHighestQuantityToday(mer.getM_id());

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
        return productPanel;
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


