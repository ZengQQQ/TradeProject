package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

//商品详情界面
public class ProductDetailsDialog extends ProductofDialog {
    private MerchantProductFrm merproduct;
    private final MerchantInterFrm mer;
    private DataControlProduct dataControlProduct = new DataControlProduct();

    public ProductDetailsDialog(Product product, MerchantInterFrm mer, MerchantProductFrm merproduct) throws SQLException {
        super(product);
        this.merproduct = merproduct;
        this.mer = mer;
        initComponents();
    }

    private void initComponents() {

        // 设置背景图片
        setContentPane(new AnimatedBackgroundPanel("/Img/detailproduct.jpg", getWidth(), getHeight()));
        // 使面板背景透明
        panel.setOpaque(false);
        getContentPane().add(panel, BorderLayout.CENTER);

        // 创建微软雅黑黑的16号字体
        Font customFont = new Font("微软雅黑", Font.PLAIN, 22);

        setTitle("商品详细信息");
        gbc.gridx = 1;
        gbc.gridy = 0;

        // 添加商品名称
        JLabel nameField = new JLabel(product.getP_name());
        nameField.setFont(customFont); // 设置字体
        panel.add(nameField, gbc);

        gbc.gridy++;

        // 添加商品描述
        JLabel descField = new JLabel();
        descField.setFont(customFont); // 设置字体
        panel.add(descField, gbc);

        String productDescription = product.getP_desc();
        int maxCharactersPerLine = 20;
        StringBuilder formattedDescription = new StringBuilder("<html>");
        for (int i = 0; i < productDescription.length(); i += maxCharactersPerLine) {
            int endIndex = Math.min(i + maxCharactersPerLine, productDescription.length());
            String line = productDescription.substring(i, endIndex);
            formattedDescription.append(line).append("<br>");
        }
        formattedDescription.append("</html>");
        descField.setText(formattedDescription.toString());

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