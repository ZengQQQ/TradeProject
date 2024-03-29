package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

//商品更新界面
public class ProductUpdateDialog extends ProductofDialog {

    private MerchantProductFrm merProduct;
    private final MerchantInterFrm mer;
    private  String oldImageName;

    public ProductUpdateDialog(Product product, MerchantProductFrm merProduct, MerchantInterFrm mer) throws SQLException {
        super(product);
        this.oldImageName=product.getP_img();
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

        JTextArea descField = new JTextArea(product.getP_description());
        descField.setFont(customFont);
        descField.setLineWrap(true);
        descField.setWrapStyleWord(true);
        descField.setRows(3);
        JScrollPane scrollPane = new JScrollPane(descField);
        scrollPane.setPreferredSize(new Dimension(385, 100));

        panel.add(scrollPane, gbc);

        gbc.gridy++;

        // 修改为 JComboBox 下拉菜单
        String[] categories = { "食品", "酒水饮料", "电脑办公", "手机", "服装", "书籍", "厨具", "家居日用", "其他"};
        JComboBox<String> categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setSelectedItem(product.getP_class()); // 设置商品原有的类别作为默认选择
        categoryComboBox.setFont(customFont);
        panel.add(categoryComboBox, gbc);

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

        // 添加商品图片标签
        gbc.gridx = 0;
        JLabel imageLabel1 = new JLabel("图片:    ");
        imageLabel1.setFont(new Font("微软雅黑", Font.BOLD, 22));
        panel.add(imageLabel1, gbc);
        // 商品图片展示
        gbc.gridx = 1;
        imageLabel = merProduct.createImageLabel(product, 250, 200);
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
            // 获取旧的图片路径信息
            String oldName = product.getP_name();
            String oldDesc = product.getP_description();
            String oldClass = product.getP_class();
            double oldPrice = Double.parseDouble(product.getP_price());
            String oldStatus = product.getP_status();
            int oldQuantity = Integer.parseInt(product.getP_quantity());


            // 从文本字段和单选按钮获取值
            String newName = nameField.getText();
            String newDesc = descField.getText();
            String newClass = categoryComboBox.getSelectedItem().toString();
            String priceText = priceField.getText();
            String quantityText = quantityField.getText();
            String newStatus = onSaleRadioButton.isSelected() ? "上架" : "下架";
            String newImageName = super.newImgName;

            int newquantity = 0;
            // 定义正则表达式
            String nameRegex = "^.{1,20}$";
            String descRegex = "^.{1,60}$";
            String classRegex = "^.{1,20}$";
            String priceRegex = "^\\d+(\\.\\d+)?$"; // 只能为数字，可以有小数点
            String quantityRegex = "^\\d+$"; // 只能为数字，不能有小数点
            // 检查是否有修改
            boolean hasChanged = !newName.equals(oldName) || !newDesc.equals(oldDesc) || !newClass.equals(oldClass)
                    || !priceText.equals(String.valueOf(oldPrice)) || !newStatus.equals(oldStatus)
                    || !quantityText.equals(String.valueOf(oldQuantity));
            // 检查图片信息是否有修改
            if(oldImageName==null ){
                System.out.println("Please");
            }
            boolean hasImageChanged = !oldImageName.equals(newImageName);

            // 判断是否有任何修改
            if (!hasChanged && !hasImageChanged) {
                JOptionPane.showMessageDialog(this, "您的商品信息没有更改", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
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
                        JOptionPane.showMessageDialog(this, "商品描述格式不正确，长度限制在60", "警告", JOptionPane.WARNING_MESSAGE);
                    } else if (!newClass.matches(classRegex)) {
                        JOptionPane.showMessageDialog(this, "商品类别格式不正确，长度限制在20", "警告", JOptionPane.WARNING_MESSAGE);
                    } else if (!priceText.matches(priceRegex)) {
                        JOptionPane.showMessageDialog(this, "商品价格格式不正确，只能为数字，可以有小数点", "警告", JOptionPane.WARNING_MESSAGE);
                    } else if (!quantityField.getText().matches(quantityRegex)) {
                        JOptionPane.showMessageDialog(this, "商品数量格式不正确，只能为数字，不能有小数点", "警告", JOptionPane.WARNING_MESSAGE);
                    } else if (quantityText.equals("0") && newStatus.equals("上架")) {
                        JOptionPane.showMessageDialog(this, "商品数量为0时不能设置为上架", "警告", JOptionPane.WARNING_MESSAGE);
                    } else {
                        double newPrice = Double.parseDouble(priceText);
                        if (newPrice < 0 || newPrice > 99999) {
                            JOptionPane.showMessageDialog(this, "商品价格范围不正确，不能为负和超过99999", "警告", JOptionPane.WARNING_MESSAGE);
                        } else if (newquantity < 0 || newquantity > 99999) {
                            JOptionPane.showMessageDialog(this, "商品数量范围不正确，不能为负和超过99999", "警告", JOptionPane.WARNING_MESSAGE);
                        } else {
                            // 调用父类的方法
                            boolean success = false;

                            boolean hasChanged2 = newName.equals(oldName) && newDesc.equals(oldDesc) && newClass.equals(oldClass)
                                    && priceText.equals(String.valueOf(oldPrice)) && !newStatus.equals(oldStatus)
                                    && quantityText.equals(String.valueOf(oldQuantity)) && product.getP_audiStatus()
                                    .equals("审核通过");

                            if (hasChanged2) {
                                success = handleUpdateButton2(newStatus);
                                if (success) {
                                    JOptionPane.showMessageDialog(this, "修改成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                                    mer.refreshProducts();
                                    dispose(); // 关闭对话框
                                } else {
                                    JOptionPane.showMessageDialog(this, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                                }
                            }else {
                                success = handleUpdateButton(newName, newDesc, newClass, newPrice, newStatus, newquantity); if (success) {
                                    JOptionPane.showMessageDialog(this, "修改成功，请等待管理员审核", "提示", JOptionPane.INFORMATION_MESSAGE);
                                    mer.refreshProducts();
                                    dispose(); // 关闭对话框
                                } else {
                                    JOptionPane.showMessageDialog(this, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                                }

                            }
                        }
                    }
                }
            }
        });
    }

}