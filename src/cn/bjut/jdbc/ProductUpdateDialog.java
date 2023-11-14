package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

//商品更新界面
public class ProductUpdateDialog extends ProductofDialog {

    private MerchantProductFrm merProduct;
    private final MerchantInterFrm mer;

    public ProductUpdateDialog(Product product, MerchantProductFrm merProduct, MerchantInterFrm mer) throws SQLException {
        super(product);
        this.merProduct = merProduct;
        this.mer = mer;
        initComponents();
    }

    private void initComponents() {
        // 设置背景图片
        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 加载背景图片
                ImageIcon background = new ImageIcon(getClass().getResource("/Img/updateproduct.jpg"));
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        });
        // 使面板背景透明
        panel.setOpaque(false);
        getContentPane().add(panel, BorderLayout.CENTER);

        // 创建微软雅黑16号字体
        Font customFont = new Font("微软雅黑", Font.PLAIN, 20);

        setTitle("修改商品信息");
        int textFieldColumns = 20;
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
        gbc.gridy++;
        gbc.gridx = 1;

        // 创建“Change Image”按钮
        JButton changeImgButton = new JButton("修改图片");
        changeImgButton.setFont(new Font("微软雅黑", Font.BOLD, 22));
        panel.add(changeImgButton, gbc);
        changeImgButton.addActionListener(e -> {
            // 调用父类的方法
            boolean b = handleChangeImageButton();
            if (b) {
                refreshphoto();
                JOptionPane.showMessageDialog(this, "图片上传成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "图片上传失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        gbc.gridx = 2;

        // 创建“Update”按钮
        JButton updateButton = new JButton("修改");
        updateButton.setFont(new Font("微软雅黑", Font.BOLD, 22));
        panel.add(updateButton, gbc);
        updateButton.addActionListener(e -> {
            // 从文本字段和单选按钮获取值
            String newName = nameField.getText();
            String newDesc = descField.getText();
            String newClass = classField.getText();
            String priceText = priceField.getText();
            String quantityText = quantityField.getText();
            String newStatus = onSaleRadioButton.isSelected() ? "上架" : "下架";
            int newquantity = 0;
            // 定义正则表达式
            String nameRegex = "^.{1,20}$";
            String descRegex = "^.{1,60}$";
            String classRegex = "^.{1,20}$";
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
                    JOptionPane.showMessageDialog(this, "商品名格式不正确，长度限制在20", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!newDesc.matches(descRegex)) {
                    JOptionPane.showMessageDialog(this, "商品描述格式不正确，，长度限制在60", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!newClass.matches(classRegex)) {
                    JOptionPane.showMessageDialog(this, "商品类别格式不正确，长度限制在20", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!priceText.matches(priceRegex)) {
                    JOptionPane.showMessageDialog(this, "商品价格格式不正确，只能为数字，可以有小数点", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!quantityField.getText().matches(quantityRegex)) {
                    JOptionPane.showMessageDialog(this, "商品数量格式不正确，只能为数字，不能有小数点", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (quantityText.equals("0") && newStatus.equals("上架")){
                    JOptionPane.showMessageDialog(this, "商品数量为0时不能设置为上架", "警告", JOptionPane.WARNING_MESSAGE);
                }else {
                    double newPrice = Double.parseDouble(priceText);
                    if (newPrice < 0 || newPrice > 99999) {
                        JOptionPane.showMessageDialog(this, "商品价格范围不正确，不能为负和超过99999", "警告", JOptionPane.WARNING_MESSAGE);
                    } else if (newquantity < 0 || newquantity > 99999) {
                        JOptionPane.showMessageDialog(this, "商品数量范围不正确，不能为负和超过99999", "警告", JOptionPane.WARNING_MESSAGE);
                    } else {
                        // 调用父类的方法
                        boolean success;
                        success = handleUpdateButton(newName, newDesc, newClass, newPrice, newStatus, newquantity);
                        if (success) {
                            JOptionPane.showMessageDialog(this, "修改成功，请等待一会", "提示", JOptionPane.INFORMATION_MESSAGE);
                            mer.refreshCard1();
                            dispose(); // 关闭对话框
                        } else {
                            JOptionPane.showMessageDialog(this, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

}