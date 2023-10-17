package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

public class MerchantInterFrm extends JFrame {
    private JButton searchButton;
    private JButton upproject;
    private JButton downproject;
    private JButton evaluate;
    private JButton myButton;
    public int m_id;

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }

    private JPanel card1; // 用于显示商品的卡片

    public MerchantInterFrm(int m_id) {
        this.m_id = m_id;
        initComponents();
    }

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
            JButton updateButton = createUpdateButton(product, nameField, descField, classField, newImgName, priceField, onSaleRadioButton);
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
        private JButton createUpdateButton(Product product, JTextField nameField, JTextField descField, JTextField classField, String imgField, JTextField priceField, JRadioButton onSaleRadioButton) {
            JButton updateButton = new JButton("修改");
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
                DataControl dataControl = new DataControl();
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
            return updateButton;
        }

    }

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
    }

    private void initComponents() {
        //主界面的创建
        JPanel mainPanel = new JPanel();
        CardLayout cardLayout = new CardLayout();
        mainPanel.setLayout(cardLayout);
        //第一个界面------------------------------------------------
        card1 = new JPanel();
        card1.setLayout(new GridLayout(0, 2));
        JScrollPane scrollPane = new JScrollPane(card1);// 创建一个 JScrollPane 来包装 card1 面板
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();// 获取垂直滚动条
        verticalScrollBar.setUnitIncrement(20);// 设置单次滚动单位的大小为 20 像素
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);// 设置垂直滚动条自动出现

        try {
            DataControl dataControl = new DataControl();
            List<Product> products = dataControl.MerchantProductQuery(getM_id());
            for (Product product : products) {
                JPanel productPanel = createProductPanel(product);
                productPanel.setAlignmentX(Component.LEFT_ALIGNMENT); // 设置商品面板左对齐
                card1.add(productPanel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        mainPanel.add(scrollPane, "card1");
        //第二个界面------------------------------------------------
        JPanel card2 = new JPanel();
        card2.add(new JLabel("这是第二个界面"));
        card2.setBackground(Color.GREEN);
        //第三个界面------------------------------------------------
        JPanel card3 = new JPanel();
        card3.add(new JLabel("这是第三个界面"));
        card3.setBackground(Color.BLUE);

        //第四个界面------------------------------------------------
        JPanel card4 = new JPanel();
        card4.add(new JLabel("这是第四个界面"));
        card4.setBackground(Color.YELLOW);

        //搜索界面------------------------------------------------
        JPanel card5 = new JPanel();
        JTextField textField = new JTextField(10);
        textField.setText("请输入搜索内容");
        card5.add(textField);

        mainPanel.add(card2, "card2");
        mainPanel.add(card3, "card3");
        mainPanel.add(card4, "card4");
        mainPanel.add(card5, "card5");


        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // 按钮------------------------------------------------
        // 创建按钮
        searchButton = new JButton("搜索");
        upproject = new JButton("商品管理");
        downproject = new JButton("暂定");
        evaluate = new JButton("评价");
        myButton = new JButton("我的");

        // 设置按钮的高度（例如，将高度设置为 50 像素）
        int buttonHeight = 45;
        searchButton.setPreferredSize(new Dimension(searchButton.getPreferredSize().width, buttonHeight));
        upproject.setPreferredSize(new Dimension(upproject.getPreferredSize().width, buttonHeight));
        downproject.setPreferredSize(new Dimension(downproject.getPreferredSize().width, buttonHeight));
        evaluate.setPreferredSize(new Dimension(evaluate.getPreferredSize().width, buttonHeight));
        myButton.setPreferredSize(new Dimension(myButton.getPreferredSize().width, buttonHeight));

        //设置动作
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card5");
            }
        });

        upproject.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card1");
            }
        });

        downproject.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card2");
            }
        });

        evaluate.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card3");
            }
        });

        myButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card4");
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 4));
        buttonPanel.add(upproject);
        buttonPanel.add(downproject);
        buttonPanel.add(evaluate);
        buttonPanel.add(myButton);

        contentPane.add(searchButton, BorderLayout.NORTH);
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel, BorderLayout.SOUTH);
        //----------------------------------------------------------------
        pack();
        setLocationRelativeTo(getOwner());
    }

    private JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout());

        // 创建商品图片标签并添加到productPanel的西边
        JLabel imageLabel = createImageLabel(product);
        productPanel.add(imageLabel, BorderLayout.WEST);

        // 创建商品信息面板
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0, 1)); // 一个商品信息一行

        // 为商品信息面板添加线框
        infoPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // 添加商品名称
        JLabel nameLabel = new JLabel("商品名称: " + product.getP_name());
        infoPanel.add(nameLabel);

        // 添加商品价格
        JLabel priceLabel = new JLabel("商品价格: " + product.getP_price() + "元");
        infoPanel.add(priceLabel);

        // 添加商品状态
        JLabel statusLabel = new JLabel("商品状态: " + product.getP_status());

        // 根据商品状态设置文本颜色
        if ("上架".equals(product.getP_status())) {
            statusLabel.setForeground(Color.GREEN);
        } else if ("下架".equals(product.getP_status())) {
            statusLabel.setForeground(Color.RED);
        }

        infoPanel.add(statusLabel);

        // 添加商品按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        // 创建“修改”按钮并添加到按钮面板
        JButton alertButton = new JButton("修改");
        alertButton.addActionListener(e -> {
            ProductUpdateDialog updateDialog = new ProductUpdateDialog(product);
            updateDialog.setVisible(true);
        });
        buttonPanel.add(alertButton);

        // 创建“详情”按钮并添加到按钮面板
        JButton detailsButton = new JButton("详情");
        detailsButton.addActionListener(e -> {
            ProductDetailsDialog detailsDialog = new ProductDetailsDialog(product);
            detailsDialog.setVisible(true);
        });
        buttonPanel.add(detailsButton);

        // 将商品信息面板和按钮面板添加到productPanel的中部
        productPanel.add(infoPanel, BorderLayout.CENTER);
        productPanel.add(buttonPanel, BorderLayout.EAST);

        return productPanel;
    }





    private JLabel createImageLabel(Product product) { // 创建包含商品图片的JLabel
        String relativeImagePath = product.getP_img();
        ClassLoader classLoader = getClass().getClassLoader();
        URL imageURL = classLoader.getResource(relativeImagePath);

        // 创建图片标签
        JLabel imageLabel;

        if (imageURL != null) {
            ImageIcon originalIcon = new ImageIcon(imageURL);
            // 调整图片大小
            Image scaledImage = originalIcon.getImage().getScaledInstance(150, 120, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            imageLabel = new JLabel(scaledIcon);
        } else {
            imageLabel = new JLabel("----------图片不能显示---------");
        }
        return imageLabel;
    }


    public static void main(String[] args) throws SQLException {
        MerchantInterFrm frame = new MerchantInterFrm(1);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 900);
        frame.setVisible(true);
    }
}
