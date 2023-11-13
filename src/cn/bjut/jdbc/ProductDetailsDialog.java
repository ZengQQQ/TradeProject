package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

//商品详情界面
public class ProductDetailsDialog extends ProductofDialog {
    private MerchantProductFrm merproduct;
    private DataControlProduct dataControlProduct = new DataControlProduct();

    public ProductDetailsDialog(Product product, MerchantProductFrm merproduct) throws SQLException {
        super(product);
        this.merproduct = merproduct;
        initComponents();
    }

    private void initComponents() {

        // 设置背景图片
        setContentPane(new AnimatedBackgroundPanel("/Img/detailproduct.jpg", getWidth(), getHeight()));
        // 使面板背景透明
        panel.setOpaque(false);
        getContentPane().add(panel, BorderLayout.CENTER);

        // 创建微软雅黑黑的16号字体
        Font customFont = new Font("微软雅黑", Font.PLAIN, 16);

        setTitle("商品详细信息");
        gbc.gridx = 1;
        gbc.gridy = 0;

        // 添加商品名称
        JLabel nameField = new JLabel(product.getP_name());
        nameField.setFont(customFont); // 设置字体
        panel.add(nameField, gbc);

        gbc.gridy++;

        // 添加商品描述
        JLabel descField = new JLabel(product.getP_desc());
        descField.setFont(customFont); // 设置字体
        panel.add(descField, gbc);

        gbc.gridy++;

        // 添加商品类别
        JLabel classField = new JLabel(product.getP_class());
        classField.setFont(customFont); // 设置字体
        panel.add(classField, gbc);

        gbc.gridy++;

        // 添加商品价格
        JLabel priceField = new JLabel(product.getP_price() + "元");
        priceField.setFont(customFont); // 设置字体
        panel.add(priceField, gbc);

        gbc.gridy++;

        // 添加商品数量
        JLabel quantityField = new JLabel(product.getP_quantity());
        quantityField.setFont(customFont); // 设置字体
        panel.add(quantityField, gbc);

        gbc.gridy++;

        // 创建商品状态
        JLabel status = new JLabel();
        if (product.getP_status().equals("上架")) {
            status.setText("上架");
        } else {
            status.setText("下架");
        }
        status.setFont(customFont); // 设置字体
        panel.add(status, gbc);

        gbc.gridy++;

        // 商品图片展示
        imageLabel = merproduct.createImageLabel(product, 350, 300);
        panel.add(imageLabel, gbc);

        // 添加“修改”按钮
        JButton modifyButton = new JButton("修改");
        modifyButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        gbc.gridy++;
        panel.add(modifyButton, gbc);

        // 添加“删除”按钮
        JButton deleteButton = new JButton("删除");
        deleteButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        gbc.gridx++;
        panel.add(deleteButton, gbc);
        // 在点击“修改”按钮后
        modifyButton.addActionListener(e -> {
            ProductUpdateDialog updateDialog = null;
            try {
                updateDialog = new ProductUpdateDialog(product, merproduct);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            updateDialog.setVisible(true);
            // 关闭当前的详细信息窗口
            dispose();
        });

        // 在点击“删除”按钮后
        deleteButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null, "确定要删除该商品吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // 获取要删除的商品的唯一标识，通常是商品ID
                int productId = product.getP_id();
                // 执行删除商品的操作，你需要实现该方法
                boolean success;
                try {
                    success = dataControlProduct.deleteProduct(productId);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (success) {
                    // 删除成功
                    JOptionPane.showMessageDialog(null, "商品删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    // 刷新界面，删除商品对应的面板
                    merproduct.remove(imageLabel.getParent());
                    merproduct.revalidate();
                    merproduct.repaint();

                    // 关闭当前的详细信息窗口
                    dispose();
                } else {
                    // 删除失败
                    JOptionPane.showMessageDialog(null, "商品删除失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    private static class AnimatedBackgroundPanel extends JPanel {
        private ImageIcon background;

        public AnimatedBackgroundPanel(String gifPath, int width, int height) {
            this.background = new ImageIcon(getClass().getResource(gifPath));
            this.background = new ImageIcon(background.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            background.paintIcon(this, g, 0, 0);
        }
    }
}