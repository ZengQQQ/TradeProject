package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class ProductDetailsDialog extends JDialog {

        private Product product;

        public ProductDetailsDialog(Product product) {
            this.product = product;
            initComponents();
        }

        private void initComponents() {
            setTitle("商品详细信息");
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
            JLabel nameField = new JLabel(product.getP_name());
            panel.add(nameField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("商品描述:"), gbc);
            gbc.gridx = 1;
            JLabel descField = new JLabel(product.getP_desc());
            panel.add(descField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("商品类别:"), gbc);
            gbc.gridx = 1;
            JLabel classField = new JLabel(product.getP_class());
            panel.add(classField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("商品价格:"), gbc);
            gbc.gridx = 1;
            JLabel priceField = new JLabel(product.getP_price() + "元");
            panel.add(priceField, gbc);

            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("商品状态:"), gbc);
            gbc.gridx = 1;

            // 创建商品状态
            JLabel status = new JLabel();

            // 根据商品状态设置默认选择
            if (product.getP_status().equals("上架")) {
                status.setText("上架");
            } else {
                status.setText("下架");
            }
            panel.add(status, gbc);
            gbc.gridx = 0;
            gbc.gridy++;
            panel.add(new JLabel("商品图片:"), gbc);
            gbc.gridx = 1;
            // 在createProductPanel方法中添加图片标签
            JLabel imageLabel = createImageLabel(product);
            panel.add(imageLabel, gbc);

            getContentPane().add(panel, BorderLayout.CENTER);
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
