package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

//添加商品界面
public class ProductAddDialog extends ProductofDialog {
    private final int m_id;
    private final MerchantInterFrm mer;
    private final MerchantProductFrm merproduct;
    private DataControlProduct dataControlProduct = new DataControlProduct();

    public ProductAddDialog(Product product, MerchantInterFrm mer, MerchantProductFrm merproduct) throws SQLException {
        super(product);
        this.merproduct = merproduct;
        this.m_id = mer.getM_id();
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
                ImageIcon background = new ImageIcon(getClass().getResource("/Img/addproduct.png"));
                g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        });
        // 使面板背景透明
        panel.setOpaque(false);
        getContentPane().add(panel, BorderLayout.CENTER);

        setTitle("添加商品信息");
        int textFieldColumns = 30;
        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField nameField = new JTextField(textFieldColumns);
        panel.add(nameField, gbc);
        gbc.gridy++;
        JTextField descField = new JTextField(textFieldColumns);
        panel.add(descField, gbc);
        gbc.gridy++;
        JTextField classField = new JTextField(textFieldColumns);
        panel.add(classField, gbc);
        gbc.gridy++;
        JTextField priceField = new JTextField(textFieldColumns);
        panel.add(priceField, gbc);
        gbc.gridy++;
        JTextField quantityField = new JTextField(textFieldColumns);
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
        // 根据商品状态设置选择
        onSaleRadioButton.setSelected(true);
        panel.add(statusPanel, gbc);
        gbc.gridy++;

        imageLabel = merproduct.createImageLabel(product, 350, 300);
        panel.add(imageLabel, gbc);

        //商品图片展示
        gbc.gridy++;
        gbc.gridx = 1;
        // 创建“Change Image”按钮
        JButton upImgButton = new JButton("上传图片");
        upImgButton.setFont(new Font("微软雅黑", Font.BOLD, 22));
        panel.add(upImgButton, gbc);
        upImgButton.addActionListener(e -> {
            // 调用父类的方法
            boolean b = handleChangeImageButton();
            refreshphoto();
            if (b) {
                JOptionPane.showMessageDialog(this, "图片上传成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "图片上传失败失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 2;

        // 创建“创建”按钮
        JButton createProductButton = new JButton("创建");
        createProductButton.setFont(new Font("微软雅黑", Font.BOLD, 22));
        panel.add(createProductButton, gbc);
        createProductButton.addActionListener(e -> {
            // 从文本字段和单选按钮获取值
            String newName = nameField.getText();
            String newDesc = descField.getText();
            String newClass = classField.getText();
            String priceText = priceField.getText();
            String quantityText = quantityField.getText();
            int newquantity = 0;
            // 定义正则表达式
            String nameRegex = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,30}$"; // 可以为字母和中文还有数字，长度限制在20
            String descRegex = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,40}$"; // 可以为字母和中文还有数字，长度限制在40
            String classRegex = "^[\\u4e00-\\u9fa5a-zA-Z0-9]{1,20}$"; // 可以为字母和中文还有数字，长度限制在20
            String priceRegex = "^\\d+(\\.\\d+)?$"; // 只能为数字，可以有小数点
            String quantityRegex = "^\\d+$"; // 只能为数字，不能有小数点
            if (!quantityText.isEmpty()) {
                newquantity = Integer.parseInt(quantityText);
            }
            if (newName.isEmpty() || newDesc.isEmpty() || newClass.isEmpty() || priceText.isEmpty() || quantityText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "所有字段都必须填写", "警告", JOptionPane.WARNING_MESSAGE);
            } else {
                // 检查格式和范围
                if (!newName.matches(nameRegex)) {
                    JOptionPane.showMessageDialog(this, "商品名格式不正确，只能为字母和中文还有数字，长度限制在30", "警告", JOptionPane.WARNING_MESSAGE);
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
                        try {
                            success = dataControlProduct.addProduct(m_id, newName, newDesc, newClass, newPrice, newStatus, newquantity, newImgName);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (success) {
                            JOptionPane.showMessageDialog(this, "创建成功，请等待一会", "提示", JOptionPane.INFORMATION_MESSAGE);
                            mer.refreshCard1();
                            dispose(); // 关闭对话框
                        } else {
                            JOptionPane.showMessageDialog(this, "创建失败", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }
}
