package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class OrderDetailsDialog extends JDialog {
    public OrderDetailsDialog(Frame owner, Order order) {
        super(owner, "订单详细信息", true);
        setSize(1000, 1000);

        JPanel panel = new JPanel(new GridLayout(2, 3)); // 创建一个容器，使用2行3列的网格布局
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // 设置面板的边距

        // 商品信息面板
        JPanel productPanel = new JPanel(new BorderLayout()); // 商品信息面板采用边界布局

        JTextArea productInfo = new JTextArea(getProductInfo(order.getProduct())); // 创建商品信息文本区域
        productInfo.setEditable(false);
        productInfo.setFont(new Font("微软雅黑", Font.PLAIN, 16)); // 设置字体
        productPanel.add(new JScrollPane(productInfo), BorderLayout.CENTER); // 将商品信息文本区域放置在中心
        productPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // 添加边框
        panel.add(productPanel); // 将商品信息面板添加到主面板中

        // 订单信息面板
        JTextArea orderInfo = new JTextArea(getOrderInfo(order)); // 创建订单信息文本区域
        orderInfo.setEditable(false);
        orderInfo.setFont(new Font("微软雅黑", Font.PLAIN, 16)); // 设置字体
        JScrollPane orderScrollPane = new JScrollPane(orderInfo); // 创建滚动面板
        JPanel orderPanel = new JPanel(new BorderLayout()); // 订单信息面板采用边界布局
        orderPanel.add(orderScrollPane, BorderLayout.CENTER); // 将订单信息文本区域放置在中心
        orderPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // 添加边框
        panel.add(orderPanel); // 将订单信息面板添加到主面板中

        // 用户信息面板
        JTextArea userInfo = new JTextArea(getUserInfo(order.getUser())); // 创建用户信息文本区域
        userInfo.setEditable(false);
        userInfo.setFont(new Font("微软雅黑", Font.PLAIN, 16)); // 设置字体
        JScrollPane userScrollPane = new JScrollPane(userInfo); // 创建滚动面板
        JPanel userPanel = new JPanel(new BorderLayout()); // 用户信息面板采用边界布局
        userPanel.add(userScrollPane, BorderLayout.CENTER); // 将用户信息文本区域放置在中心
        userPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // 添加边框
        panel.add(userPanel); // 将用户信息面板添加到主面板中

        JPanel imgPanel = new JPanel(new BorderLayout()); // 商品信息面板采用边界布局
        JLabel imageLabel = createImageLabel(order.getProduct(), 300, 250); // 创建商品图片标签
        imgPanel.add(imageLabel, BorderLayout.CENTER); // 将商品图片标签放置在中心
        imgPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY)); // 添加边框
        panel.add(imgPanel); // 将图片面板添加到主面板中

        add(panel); // 将主面板添加到对话框中
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
        String productName = "名称: " + product.getP_name() + "\n";
        String productdescription = "描述: " + product.getP_description() + "\n";
        String productClass = "分类: " + product.getP_class() + "\n";
        String productPrice = "价格: " + product.getP_price() + "\n";
        String productstatus = "状态: " + product.getP_status() + "\n";
        String productquantity = "数量: " + product.getP_quantity() + "\n";
        String productauditstatus = "审核状态: " + product.getP_audiStatus() + "\n";
        return productName + productdescription + productClass + productPrice + productstatus + productquantity + productauditstatus;
    }

    private String getOrderInfo(Order order) {
        String orderId = "订单 ID: " + order.getO_id() + "\n";
        String quantityInfo = "购买数量: " + order.getQuantity() + "\n";
        String totalPriceInfo = "订单总价格: " + order.getTotalprice() + "￥\n";
        String buyTimeInfo = "购买时间: " + order.getBuytime() + "\n";
        String orderStatus = "订单状态: " + order.getStatus();
        return orderId + quantityInfo + totalPriceInfo + buyTimeInfo + orderStatus;
    }

    private String getUserInfo(User user) {
        String userName = "用户名: " + user.getU_name() + "\n";
        String userSex = "性别: " + user.getU_sex() + "\n";
        String userTelephone = "电话: " + user.getU_tele();
        return userName + userSex + userTelephone;
    }
}
