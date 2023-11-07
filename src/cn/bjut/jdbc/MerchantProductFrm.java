package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MerchantProductFrm extends JPanel {

    private MerchantInterFrm merchantInterFrm;
    private DataControl data;
    private ProductDetailsDialog currentDetailsDialog; // 用于存储当前显示的商品详情对话框

    public MerchantProductFrm(MerchantInterFrm mer, DataControl data) {
        this.data = data;
        this.merchantInterFrm = mer;
        initComponent();
    }

    public void initComponent() {
        setLayout(new GridLayout(0, 2));
        createproductcard();
    }

    public JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout());
        // 设置商品对象为面板的客户属性
        productPanel.putClientProperty("product", product);
        // 创建商品图片标签并添加到productPanel的西边
        JLabel imageLabel = createImageLabel(product, 350, 300);
        productPanel.add(imageLabel, BorderLayout.WEST);
        // 创建商品信息面板
        JPanel infoPanel = getjPanel(product);
        // 添加商品按钮面板

        // 将商品信息面板和按钮面板添加到productPanel的中部
        productPanel.add(infoPanel, BorderLayout.CENTER);

        return productPanel;
    }

    private static JPanel getjPanel(Product product) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 1)); // 一个商品信息一行

        // 为商品信息面板添加线框
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        // 添加商品名称
        JLabel nameLabel = new JLabel("商品名称: " + product.getP_name());
        // 设置字体大小为大号
        nameLabel.setFont(new Font("微软雅黑", Font.BOLD, 16)); // 16是字体大小
        infoPanel.add(nameLabel);
        // 添加商品价格
        JLabel priceLabel = new JLabel("商品价格: " + product.getP_price() + "元");
        // 设置字体大小为大号
        priceLabel.setFont(new Font("微软雅黑", Font.BOLD, 16)); // 16是字体大小
        infoPanel.add(priceLabel);
        // 创建商品状态的标签
        // 使用HTML标签来设置文字颜色
        String statusColor = "";
        if ("上架".equals(product.getP_status())) {
            statusColor = "<font color='green'>上架</font>";
        } else if ("下架".equals(product.getP_status())) {
            statusColor = "<font color='red'>下架</font>";
        }
        JLabel statusLabel = new JLabel("<html>商品状态: " + statusColor + "</html>");
        // 设置字体大小为大号
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 16)); // 16是字体大小
        infoPanel.add(statusLabel);
        // 添加商品数量
        JLabel quantityLabel = new JLabel("商品数量: " + product.getP_quantity());
        // 设置字体大小为大号
        quantityLabel.setFont(new Font("微软雅黑", Font.BOLD, 16)); // 16是字体大小
        infoPanel.add(quantityLabel);

        return infoPanel;
    }

    // 创建包含商品图片的JLabel
    public JLabel createImageLabel(Product product, int width, int height) {
        // 获取当前项目的绝对路径
        String projectPath = System.getProperty("user.dir");

        // 构建图片路径
        ImageIcon originalIcon = getImageIcon(product, projectPath);
        // 获取图片对象
        Image originalImage = originalIcon.getImage();

        // 缩放图片（如果需要）可以改图片的大小
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // 创建一个新的 ImageIcon
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // 创建一个 JLabel 并将缩放后的 ImageIcon 设置为其图标
        JLabel imageLabel = new JLabel(scaledIcon);

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showProductDetails(product);
            }
        });

        return imageLabel;
    }

    private void showProductDetails(Product product) {
        // 关闭当前显示的商品详情对话框，如果有的话
        if (currentDetailsDialog != null) {
            currentDetailsDialog.dispose();
        }

        // 创建新的商品详情对话框
        currentDetailsDialog = new ProductDetailsDialog(data, product, this);
        currentDetailsDialog.setVisible(true);
    }

    private ImageIcon getImageIcon(Product product, String projectPath) {
        String relativeImagePath = product.getP_img();
        String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + relativeImagePath;
        // 创建 ImageIcon
        ImageIcon originalIcon;
        File imageFile = new File(absoluteImagePath);
        if (imageFile.exists()) {
            originalIcon = new ImageIcon(absoluteImagePath);
        } else {
            // 图片路径不存在，使用默认图片
            String defaultImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + "R.jpg";
            originalIcon = new ImageIcon(defaultImagePath);
        }
        return originalIcon;
    }

    public void createproductcard() {
        try {
            DataControl dataControl = new DataControl();
            List<Product> products = dataControl.MerchantProductQuery(merchantInterFrm.getM_id());
            for (Product product : products) {
                JPanel productPanel = createProductPanel(product);
                productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                this.add(productPanel, CENTER_ALIGNMENT);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 刷新修改后的商品信息
    public void refreshCard1Product(Product updatedProduct) {
        // 查找要更新的商品的位置
        int index = -1;
        Component[] components = this.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JPanel) {
                JPanel productPanel = (JPanel) components[i];
                Product product = (Product) productPanel.getClientProperty("product");
                if (product != null && product.getP_id() == updatedProduct.getP_id()) {
                    index = i;
                    break;
                }
            }
        }
        // 如果找到了要更新的商品，将其删除，然后重新插入原来的位置
        if (index >= 0) {
            this.remove(index);
            JPanel productPanel = this.createProductPanel(updatedProduct);
            productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            this.add(productPanel, index);
            this.revalidate();
            this.repaint();
        }
    }
}
