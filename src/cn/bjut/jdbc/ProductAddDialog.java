package cn.bjut.jdbc;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

//添加商品界面
public class ProductAddDialog extends ProductofDialog {
    private int m_id;
    MerchantInterFrm mer;

    public ProductAddDialog(DataControl dataControladd,Product product , MerchantInterFrm mer) {
        super(dataControladd, product);
        this.m_id =mer.getM_id();
        this.mer = mer;
        initComponents();
    }

    private void initComponents() {
        setTitle("添加商品信息");
        int textFieldColumns = 50;
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

        imageLabel = mer.createImageLabel(product, 400, 300);
        panel.add(imageLabel, gbc);
        gbc.gridx = 2;
        //商品图片展示

        // 创建“Change Image”按钮
        JButton upImgButton = new JButton("上传图片");
        panel.add(upImgButton, gbc);
        upImgButton.addActionListener(e -> {
            // 调用父类的方法
            boolean b = handleChangeImageButton();
            gbc.gridx = 1;
            panel.add(imageLabel, gbc);
            refreshphoto();
            if (b) {
                JOptionPane.showMessageDialog(this, "图片上传成功", "提示", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "图片上传失败失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        // 创建“创建”按钮
        JButton createProductButton = new JButton("创建");
        createProductButton.addActionListener(e -> {
            // 从文本字段和单选按钮获取值
            String newName = nameField.getText();
            String newDesc = descField.getText();
            String newClass = classField.getText();
            String priceText = priceField.getText();
            int newquantity = Integer.parseInt(quantityField.getText());
            if (newName.isEmpty() || newDesc.isEmpty() || newClass.isEmpty() || priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "请填写完整", "警告", JOptionPane.WARNING_MESSAGE);
            } else {
                double newPrice = Double.parseDouble(priceText);
                String newStatus = onSaleRadioButton.isSelected() ? "上架" : "下架";
                // 调用父类的方法
                boolean success;
                try {
                    success = dataControl.addProduct(m_id, newName, newDesc, newClass, newPrice, newStatus, newquantity, newImgName);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (success) {
                    JOptionPane.showMessageDialog(this, "创建成功，请等待一会", "提示", JOptionPane.INFORMATION_MESSAGE);
                    mer.refreshCard1All();
                    dispose(); // 关闭对话框
                } else {
                    JOptionPane.showMessageDialog(this, "创建失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }


        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(createProductButton);
        getContentPane().add(buttonPanel, BorderLayout.SOUTH);

    }
}
