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
        int textFieldColumns = 20;
        gbc.gridx = 1;
        gbc.gridy = 0;

        Font font = new Font("微软雅黑", Font.PLAIN, 18);

        JTextField nameField = new JTextField(textFieldColumns);
        nameField.setFont(font);
        panel.add(nameField, gbc);
        gbc.gridy++;

        JTextArea descField = new JTextArea(3, textFieldColumns);
        descField.setFont(font);
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descField);
        panel.add(scrollPane, gbc);
        gbc.gridy++;

        // 修改为 JComboBox 下拉菜单
        String[] categories = { "食品", "酒水饮料", "电脑办公", "手机", "服装", "书籍", "厨具", "家居日用", "其他"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setSelectedIndex(8); // 默认选择全部类别
        categoryComboBox.setFont(new Font("微软雅黑", Font.BOLD, 20));
        panel.add(categoryComboBox, gbc);
        gbc.gridy++;

        JTextField priceField = new JTextField(textFieldColumns);
        priceField.setFont(font);
        panel.add(priceField, gbc);
        gbc.gridy++;

        JTextField quantityField = new JTextField(textFieldColumns);
        quantityField.setFont(font);
        panel.add(quantityField, gbc);
        gbc.gridy++;

        // 创建商品状态
        JLabel statusLabel = new JLabel("下架(添加商品默认为下架)");
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 22));
        statusLabel.setForeground(Color.RED);

        panel.add(statusLabel, gbc);
        gbc.gridy++;
        // 添加商品图片标签
        gbc.gridx=0;
        JLabel imageLabel1 = new JLabel("商品图片:    ");
        imageLabel1.setFont(new Font("微软雅黑", Font.BOLD, 22));
        panel.add(imageLabel1, gbc);
        // 商品图片展示
        gbc.gridx = 1;
        imageLabel = merproduct.createImageLabel(product, 250, 200);
        panel.add(imageLabel, gbc);
        gbc.gridy++;

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
            String newClass = categoryComboBox.getSelectedItem().toString();
            String priceText = priceField.getText();
            String quantityText = quantityField.getText();
            int newquantity = 0;
            // 定义正则表达式
            String nameRegex = "^.{1,20}$";
            String descRegex = "^.{1,60}$";
            String classRegex = "^.{1,20}$";
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
                    JOptionPane.showMessageDialog(this, "商品名格式不正确，长度限制在20", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!newDesc.matches(descRegex)) {
                    JOptionPane.showMessageDialog(this, "商品描述格式不正确，长度限制在60", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!newClass.matches(classRegex)) {
                    JOptionPane.showMessageDialog(this, "商品类别格式不正确，长度限制在20", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!priceText.matches(priceRegex)) {
                    JOptionPane.showMessageDialog(this, "商品价格格式不正确，只能为数字，可以有小数点", "警告", JOptionPane.WARNING_MESSAGE);
                } else if (!quantityField.getText().matches(quantityRegex)) {
                    JOptionPane.showMessageDialog(this, "商品数量格式不正确，只能为数字，不能有小数点", "警告", JOptionPane.WARNING_MESSAGE);
                } else {
                    double newPrice = Double.parseDouble(priceText);
                    if (newPrice < 0 || newPrice > 99999) {
                        JOptionPane.showMessageDialog(this, "商品价格范围不正确，不能为负和超过99999", "警告", JOptionPane.WARNING_MESSAGE);
                    } else if (newquantity < 0 || newquantity > 99999) {
                        JOptionPane.showMessageDialog(this, "商品数量范围不正确，不能为负和超过99999", "警告", JOptionPane.WARNING_MESSAGE);
                    } else {
                        String newStatus ="下架";
                        // 调用父类的方法
                        boolean success;
                        try {
                            success = dataControlProduct.addProduct(m_id, newName, newDesc, newClass, newPrice, newStatus, newquantity, newImgName);
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                        if (success) {
                            JOptionPane.showMessageDialog(this, "创建成功，请等待管理员审核", "提示", JOptionPane.INFORMATION_MESSAGE);
                            mer.refreshProducts();
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
