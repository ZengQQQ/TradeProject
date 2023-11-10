package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

//商品更新界面
public class ProductUpdateDialog extends ProductofDialog {

    private MerchantProductFrm merProduct;

    public ProductUpdateDialog(DataControl dataControl, Product product, MerchantProductFrm merProduct) {
        super(dataControl, product);
        this.merProduct = merProduct;
        initComponents();
    }

    private void initComponents() {
        // 创建微软雅黑16号字体
        Font customFont = new Font("微软雅黑", Font.PLAIN, 12);

        setTitle("修改商品信息");
        int textFieldColumns = 30;
        gbc.gridx = 1;
        gbc.gridy = 0;

        JTextField nameField = new JTextField(product.getP_name(), textFieldColumns);
        nameField.setFont(customFont); // 设置字体
        panel.add(nameField, gbc);

        gbc.gridy++;

        JTextField descField = new JTextField(product.getP_desc(), textFieldColumns);
        descField.setFont(customFont); // 设置字体
        panel.add(descField, gbc);

        gbc.gridy++;

        JTextField classField = new JTextField(product.getP_class(), textFieldColumns);
        classField.setFont(customFont); // 设置字体
        panel.add(classField, gbc);

        gbc.gridy++;

        JTextField priceField = new JTextField(String.valueOf(product.getP_price()), textFieldColumns);
        priceField.setFont(customFont); // 设置字体
        panel.add(priceField, gbc);

        gbc.gridy++;

        JTextField quantityField = new JTextField(product.getP_quantity(), textFieldColumns);
        quantityField.setFont(customFont); // 设置字体
        panel.add(quantityField, gbc);

        gbc.gridy++;

        // 创建商品状态的单选框
        ButtonGroup statusGroup = new ButtonGroup();
        onSaleRadioButton = new JRadioButton("上架");
        offSaleRadioButton = new JRadioButton("下架");
        statusGroup.add(onSaleRadioButton);
        statusGroup.add(offSaleRadioButton);
        JPanel statusPanel = new JPanel();
        statusPanel.add(onSaleRadioButton);
        statusPanel.add(offSaleRadioButton);
        // 根据商品状态设置默认选择
        if (product.getP_status().equals("上架")) {
            onSaleRadioButton.setSelected(true);
        } else {
            offSaleRadioButton.setSelected(true);
        }
        panel.add(statusPanel, gbc);

        gbc.gridy++;

        // 商品图片展示
        imageLabel = merProduct.createImageLabel(product, 350, 300);
        panel.add(imageLabel, gbc);

        // 创建修改图片按钮
        gbc.gridx = 2;

        // 创建“Change Image”按钮
        JButton changeImgButton = new JButton("修改图片");
        changeImgButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        panel.add(changeImgButton, gbc);
        changeImgButton.addActionListener(e -> {
            // 调用父类的方法
            boolean b = handleChangeImageButton();
            if (b) {
                refreshphoto();
                JOptionPane.showMessageDialog(this, "图片上传成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "图片上传失败失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        // 创建“Update”按钮
        JButton updateButton = new JButton("修改");
        updateButton.setFont(new Font("微软雅黑", Font.BOLD, 18));
        updateButton.addActionListener(e -> {
            // 从文本字段和单选按钮获取值
            String newName = nameField.getText();
            String newDesc = descField.getText();
            String newClass = classField.getText();
            String priceText = priceField.getText();
            String quantityText = quantityField.getText();
            int newquantity = 0;
            // 定义正则表达式
            String nameRegex = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,20}$"; // 可以为字母和中文还有数字，长度限制在20
            String descRegex = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,40}$"; // 可以为字母和中文还有数字，长度限制在40
            String classRegex = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,20}$"; // 可以为字母和中文还有数字，长度限制在20
            String priceRegex = "^\\d+(\\.\\d+)?$"; // 只能为数字，可以有小数点
            String quantityRegex = "^\\d+$"; // 只能为数字，不能有小数点
            // 检查格式和范围
            if (!quantityText.isEmpty()) {
                newquantity = Integer.parseInt(quantityText);
            }
            if (newName.isEmpty() || newDesc.isEmpty() || newClass.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "所有字段都必须填写", "警告", JOptionPane.WARNING_MESSAGE);
            } else {
                if (!newName.matches(nameRegex)) {
                    JOptionPane.showMessageDialog(this, "商品名格式不正确，只能为字母和中文还有数字，长度限制在20", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!newDesc.matches(descRegex)) {
                    JOptionPane.showMessageDialog(this, "商品描述格式不正确，只能为字母和中文还有数字，长度限制在40", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!newClass.matches(classRegex)) {
                    JOptionPane.showMessageDialog(this, "商品类别格式不正确，只能为字母和中文还有数字，长度限制在20", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!priceText.matches(priceRegex)) {
                    JOptionPane.showMessageDialog(this, "商品价格格式不正确，只能为数字，可以有小数点", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!quantityField.getText().matches(quantityRegex)) {
                    JOptionPane.showMessageDialog(this, "商品数量格式不正确，只能为数字，不能有小数点", "警告", JOptionPane.WARNING_MESSAGE);
                } else {
                    double newPrice = Double.parseDouble(priceText);
                    if (newPrice < 0 || newPrice > 99999) {
                        JOptionPane.showMessageDialog(this, "商品价格范围不正确，不能为负和超过99999", "警告", JOptionPane.WARNING_MESSAGE);
                    } else if (newquantity < 0 || newquantity > 100) {
                        JOptionPane.showMessageDialog(this, "商品数量范围不正确，不能为负和超过100", "警告", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String newStatus = onSaleRadioButton.isSelected() ? "上架" : "下架";
                        // 调用父类的方法
                        boolean success;
                        success = handleUpdateButton(newName, newDesc, newClass, newPrice, newStatus, newquantity);
                        if (success) {
                            JOptionPane.showMessageDialog(this, "修改成功，请等待一会", "提示", JOptionPane.INFORMATION_MESSAGE);
                            merProduct.refreshCard1Product(new Product(product.getP_id(), newName, newDesc, newClass, newPrice, newStatus, newquantity, newImgName));
                            dispose(); // 关闭对话框
                        } else {
                            JOptionPane.showMessageDialog(this, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(updateButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    }

}