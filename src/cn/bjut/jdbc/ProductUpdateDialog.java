package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;

public class ProductUpdateDialog extends JDialog {
        private Product product;
        private String newImgName; // 添加字段用于存储文件名
        private JRadioButton onSaleRadioButton;
        private JRadioButton offSaleRadioButton;

        public ProductUpdateDialog(Product product) {
            this.product = product;
            initComponents();
        }

        private void initComponents() {
            setTitle("修改商品信息");
            setSize(900, 500);
            setLocationRelativeTo(null); // 居中显示

            JPanel panel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;

            panel.add(new JLabel("商品名称:"), gbc);
            gbc.gridx = 1;
            JTextField nameField = new JTextField(product.getP_name());
            panel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("商品描述:"), gbc);
            gbc.gridx = 1;
            JTextField descField = new JTextField(product.getP_desc());
            panel.add(descField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("商品类别:"), gbc);
            gbc.gridx = 1;
            JTextField classField = new JTextField(product.getP_class());
            panel.add(classField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("商品价格（元）:"), gbc);
            gbc.gridx = 1;
            JTextField priceField = new JTextField(String.valueOf(product.getP_price()));
            panel.add(priceField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("商品状态:"), gbc);
            gbc.gridx = 1;

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
            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("商品图片:"), gbc);
            gbc.gridx = 1;

            // 为商品图片添加一个文本框
            JTextField imgField = new JTextField(product.getP_img());
            JLabel imageLabel = createImageLabel(product);
            panel.add(imageLabel, gbc);

            // 创建修改图片按钮
            gbc.gridx = 2;
            JButton changeImgButton = new JButton("修改图片");
            panel.add(changeImgButton, gbc);

            // 为修改图片按钮添加事件处理程序
            changeImgButton.addActionListener(e -> {
                JFileChooser fileChooser = new JFileChooser();
                int returnVal = fileChooser.showOpenDialog(ProductUpdateDialog.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    String newImgPath = fileChooser.getSelectedFile().getPath();
                    newImgName = new File(newImgPath).getName();

                    FilephotoCopy(newImgPath, newImgName);
                }
            });

            // 创建“修改”按钮
            // 在initComponents方法中创建修改按钮
            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            JButton updateButton = new JButton("update");
            System.out.println(newImgName);
            createUpdateButton(updateButton,product, nameField, descField, classField, newImgName, priceField, onSaleRadioButton);
            buttonPanel.add(updateButton);

            getContentPane().add(panel, BorderLayout.CENTER);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        }


        //复制图片文件
        public String FilephotoCopy(String newImgPath,String newImgName) {
            String sourcePath = newImgPath;
            System.out.println(sourcePath);
            String currentDirectory = System.getProperty("user.dir");
            String destinationPath = currentDirectory + "\\src\\Img\\" + newImgName;
            File sourceFile = new File(sourcePath);
            File destinationFile = new File(destinationPath);
            try {
                Path source = sourceFile.toPath();
                Path destination = destinationFile.toPath();

                // 使用Files.copy方法复制文件
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("文件复制成功");
                return destinationPath;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件复制失败");
                return null;
            }
        }


        // 创建“修改”按钮
        private void createUpdateButton(JButton updateButton, Product product, JTextField nameField, JTextField descField, JTextField classField, String imgField, JTextField priceField, JRadioButton onSaleRadioButton) {

            updateButton.addActionListener(e -> {
                // 获取文本框中的更新商品信息
                String newName = nameField.getText();
                String newdesc = descField.getText();
                String newclass = classField.getText();
                String newimg = imgField;
                double newPrice = Double.parseDouble(priceField.getText());
                // 获取单选框的选择
                String newStatus = onSaleRadioButton.isSelected() ? "上架" : "下架";
                // 更新商品信息
                DataControl dataControl = null;
                try {
                    dataControl = new DataControl();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                boolean success = dataControl.updateProduct(
                        product.getP_id(), newName, newdesc, newclass, newPrice, newStatus, newimg
                );

                if (success) {
                    JOptionPane.showMessageDialog(ProductUpdateDialog.this, "修改成功", "提示", JOptionPane.INFORMATION_MESSAGE);
                    dispose(); // 关闭窗口
                } else {
                    JOptionPane.showMessageDialog(ProductUpdateDialog.this, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });
        }

    private JLabel createImageLabel(Product product) { // 创建包含商品图片的JLabel
// 获取当前项目的绝对路径
        String projectPath = System.getProperty("user.dir");

// 构建图片路径
        String relativeImagePath = product.getP_img();
        String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + relativeImagePath;
        System.out.println(absoluteImagePath);
// 创建 ImageIcon
        ImageIcon originalIcon;

        File imageFile = new File(absoluteImagePath);
        if (imageFile.exists()) {
            originalIcon = new ImageIcon(absoluteImagePath);
        } else {
            // 图片路径不存在，使用默认图片
            String defaultImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + "R.jpg";
            System.out.println(defaultImagePath);
            originalIcon = new ImageIcon(defaultImagePath);
        }

// 获取图片对象
        Image originalImage = originalIcon.getImage();

// 缩放图片（如果需要）
        Image scaledImage = originalImage.getScaledInstance(100, 100, Image.SCALE_SMOOTH);

// 创建一个新的 ImageIcon
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

// 创建一个 JLabel 并将缩放后的 ImageIcon 设置为其图标
        JLabel label = new JLabel(scaledIcon);

        return label;
    }


}
