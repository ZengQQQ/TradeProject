package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MerchantProductFrm extends JPanel {

    private MerchantInterFrm merchantInterFrm;
    private DataControl data;

    public MerchantProductFrm(MerchantInterFrm mer,DataControl data) {
        this.data=data;
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
        JPanel buttonPanel = getPanel(product);
        // 将商品信息面板和按钮面板添加到productPanel的中部
        productPanel.add(infoPanel, BorderLayout.CENTER);
        productPanel.add(buttonPanel, BorderLayout.EAST);
        return productPanel;
    }

    private JPanel getPanel(Product product) {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        // 为按钮信息面板添加线框
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // 创建“修改”按钮并添加到按钮面板
        JButton alertButton = new JButton("修改");
        alertButton.addActionListener(e -> {
            ProductUpdateDialog updateDialog = new ProductUpdateDialog(data,product,this);
            updateDialog.setVisible(true);
        });
        buttonPanel.add(alertButton);

        // 创建“详情”按钮并添加到按钮面板
        JButton detailsButton = new JButton("详情");
        detailsButton.addActionListener(e -> {
            ProductDetailsDialog detailsDialog = new ProductDetailsDialog(data,product,this);
            detailsDialog.setVisible(true);
        });
        buttonPanel.add(detailsButton);

        // 创建“删除”按钮并添加到按钮面板
        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null, "确定要删除该商品吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // 获取要删除的商品的唯一标识，通常是商品ID
                int productId = product.getP_id();
                // 执行删除商品的操作，你需要实现该方法
                boolean success;
                try {
                    success = data.deleteProduct(productId);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (success) {
                    // 删除成功
                    JOptionPane.showMessageDialog(null, "商品删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    // 刷新界面，删除商品对应的面板
                    this.remove(buttonPanel.getParent());
                    this.revalidate();
                    this.repaint();
                } else {
                    // 删除失败
                    JOptionPane.showMessageDialog(null, "商品删除失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(deleteButton);
        return buttonPanel;
    }

    private static JPanel getjPanel(Product product) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 1)); // 一个商品信息一行

        // 为商品信息面板添加线框
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // 添加商品名称
        JLabel nameLabel = new JLabel("商品名称: " + product.getP_name());
        infoPanel.add(nameLabel);

        // 添加商品价格
        JLabel priceLabel = new JLabel("商品价格: " + product.getP_price() + "元");
        infoPanel.add(priceLabel);

        // 添加商品状态
        JLabel statusLabel = new JLabel("商品状态: " + product.getP_status());

        //添加商品数量
        JLabel quantityLabel = new JLabel("商品数量: " + product.getP_quantity());
        infoPanel.add(quantityLabel);
        // 根据商品状态设置文本颜色
        if ("上架".equals(product.getP_status())) {
            statusLabel.setForeground(Color.GREEN);
        } else if ("下架".equals(product.getP_status())) {
            statusLabel.setForeground(Color.RED);
        }

        infoPanel.add(statusLabel);
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
        return new JLabel(scaledIcon);
    }

    private static ImageIcon getImageIcon(Product product, String projectPath) {
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
                this.add(productPanel,CENTER_ALIGNMENT);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //刷新修改后的商品信息
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
