package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class OrderDetailsDialog extends JDialog {
    public OrderDetailsDialog(Frame owner, Order order) {
        super(owner, "订单详细信息", true);
        setSize(1000, 1000);

        JPanel mainPanel = new JPanel(); // 主面板
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // 纵向排列

        // 商品信息面板
        JPanel productPanel = new JPanel(new BorderLayout()); // 商品信息面板采用边界布局
        productPanel.setPreferredSize(new Dimension(700, 320)); // 设置固定大小
        JTextArea productInfo = new JTextArea(getProductInfo(order.getProduct())); // 创建商品信息文本区域
        productInfo.setEditable(false);
        productInfo.setFont(new Font("微软雅黑", Font.PLAIN, 20)); // 设置字体
        productInfo.setLineWrap(true); // 允许自动换行
        JScrollPane productScrollPane = new JScrollPane(productInfo); // 创建滚动面板
        productPanel.add(productScrollPane, BorderLayout.CENTER); // 将商品信息文本区域放置在中心
        productPanel.setBorder(BorderFactory.createTitledBorder("商品信息")); // 添加边框和标题

        // 商品图片面板
        JPanel imgPanel = new JPanel(new BorderLayout()); // 商品图片面板采用边界布局
        JLabel imageLabel = createImageLabel(order.getProduct(), 300, 300); // 创建商品图片标签
        imgPanel.add(imageLabel, BorderLayout.CENTER); // 将商品图片标签放置在中心
        imgPanel.setBorder(BorderFactory.createTitledBorder("商品图片")); // 添加边框和标题
        productPanel.add(imgPanel, BorderLayout.EAST); // 将商品图片面板放置在商品信息面板的右侧
        mainPanel.add(productPanel); // 将商品信息面板添加到主面板中
        // 订单信息面板
        JPanel orderPanel = new JPanel(new BorderLayout()); // 订单信息面板采用边界布局
        orderPanel.setPreferredSize(new Dimension(700, 240)); // 设置固定大小
        JTextArea orderInfo = new JTextArea(getOrderInfo(order)); // 创建订单信息文本区域
        orderInfo.setEditable(false);
        orderInfo.setFont(new Font("微软雅黑", Font.PLAIN, 20)); // 设置字体
        orderInfo.setLineWrap(true); // 允许自动换行
        JScrollPane orderScrollPane = new JScrollPane(orderInfo); // 创建滚动面板
        orderPanel.add(orderScrollPane, BorderLayout.CENTER); // 将订单信息文本区域放置在中心
        orderPanel.setBorder(BorderFactory.createTitledBorder("订单信息")); // 添加边框和标题
        mainPanel.add(orderPanel); // 将订单信息面板添加到主面板中

        // 用户信息面板
        JPanel userPanel = new JPanel(new BorderLayout()); // 用户信息面板采用边界布局
        userPanel.setPreferredSize(new Dimension(700, 130)); // 设置固定大小
        JTextArea userInfo = new JTextArea(getUserInfo(order.getUser())); // 创建用户信息文本区域
        userInfo.setEditable(false);
        userInfo.setFont(new Font("微软雅黑", Font.PLAIN, 20)); // 设置字体
        userInfo.setLineWrap(true); // 允许自动换行
        JScrollPane userScrollPane = new JScrollPane(userInfo); // 创建滚动面板
        userPanel.add(userScrollPane, BorderLayout.CENTER); // 将用户信息文本区域放置在中心
        userPanel.setBorder(BorderFactory.createTitledBorder("用户信息")); // 添加边框和标题
        mainPanel.add(userPanel); // 将用户信息面板添加到主面板中


        add(mainPanel); // 将主面板添加到对话框中
        pack();
        setLocationRelativeTo(owner);
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
        String productauditstatus = "审核状态: " + product.getP_audiStatus() + "\n";
        return productId + productName + productdescription + productClass + productPrice + productstatus + productquantity + productauditstatus;
    }

    private String getOrderInfo(Order order) {
        String orderId = "订单 ID: " + order.getO_id() + "\n";
        String quantityInfo = "购买数量: " + order.getQuantity() + "\n";
        String totalPriceInfo = "订单总价格: " + order.getTotalprice() + "￥\n";
        String buyTimeInfo = "购买时间: " + order.getBuytime() + "\n";
        String sentTimeInfo;
        String receiveTimeInfo;
        if (order.getSendtime() == null) {
            sentTimeInfo = "发货时间: " + "无" + "\n";
        } else {
            sentTimeInfo = "发货时间: " + order.getSendtime() + "\n";
        }
        if (order.getReceivetime() == null) {
            receiveTimeInfo = "退货时间: " + "无" + "\n";
        } else {
            receiveTimeInfo = "退货时间: " + order.getReceivetime() + "\n";
        }
        String orderStatus = "订单状态: " + order.getStatus();
        return orderId + quantityInfo + totalPriceInfo + buyTimeInfo + sentTimeInfo + receiveTimeInfo + orderStatus;
    }

    private String getUserInfo(User user) {
        String userName = "用户名: " + user.getU_name() + "\n";
        String userSex = "性别: " + user.getU_sex() + "\n";
        String userTelephone = "电话: " + user.getU_tele();
        return userName + userSex + userTelephone;
    }
}
