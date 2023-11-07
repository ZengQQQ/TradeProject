package cn.bjut.jdbc;

import javax.swing.*;

//商品详情界面
public class ProductDetailsDialog extends ProductofDialog {
    private  MerchantProductFrm merproduct;
    public ProductDetailsDialog(DataControl dataControl,Product product,MerchantProductFrm merproduct) {
        super(dataControl, product);
        this.merproduct =merproduct;
        initComponents();
    }

    private void initComponents() {
        setTitle("商品详细信息");
        gbc.gridx = 1;
        gbc.gridy = 0;
        JLabel nameField = new JLabel(product.getP_name());
        panel.add(nameField, gbc);
        gbc.gridy++;
        JLabel descField = new JLabel(product.getP_desc());
        panel.add(descField, gbc);
        gbc.gridy++;
        JLabel classField = new JLabel(product.getP_class());
        panel.add(classField, gbc);
        gbc.gridy++;
        JLabel priceField = new JLabel(product.getP_price() + "元");
        panel.add(priceField, gbc);
        gbc.gridy++;
        JLabel quantityField = new JLabel(product.getP_quantity());
        panel.add(quantityField, gbc);
        gbc.gridy++;
        // 创建商品状态
        JLabel status = new JLabel();
        // 根据商品状态设置默认选择
        if (product.getP_status().equals("上架")) {
            status.setText("上架");
        } else {
            status.setText("下架");
        }
        panel.add(status, gbc);
        gbc.gridy++;
        //商品图片展示
        imageLabel = merproduct.createImageLabel(product, 350, 300);
        panel.add(imageLabel, gbc);
    }
}