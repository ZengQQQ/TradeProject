package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.SQLException;
import java.util.List;

public class MerchantInterFrm extends JFrame {
    private JButton searchButton;
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainPanel = new JPanel();
    private JPanel card1;
    private int m_id;
    public DataControl dataControl = new DataControl();

    public MerchantInterFrm(int mid) {
        this.m_id = mid;
        initComponents();
    }

    public int getM_id() {
        return m_id;
    }

    public void setM_id(int m_id) {
        this.m_id = m_id;
    }


    public class ProductofJDialog extends JDialog {
        public Product product;
        public String newImgName; // 添加字段用于存储文件名

        // 在initComponents方法中添加一个新的字段来保存商品图片的标签
        public JLabel imageLabel;
        public JRadioButton onSaleRadioButton;
        public JRadioButton offSaleRadioButton;
        public GridBagConstraints gbc = new GridBagConstraints();
        public JPanel panel = new JPanel(new GridBagLayout());

        public ProductofJDialog() {
            initComponents();
        }

        public ProductofJDialog(Product product) {
            this.product = product;
            this.newImgName = product.getP_img();
            initComponents();
        }

        private void initComponents() {
            setSize(900, 600);
            setLocationRelativeTo(null); // 居中显示

            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            panel.add(new JLabel("商品名称:"), gbc);
            gbc.gridy++;
            panel.add(new JLabel("商品描述:"), gbc);
            gbc.gridy++;
            panel.add(new JLabel("商品类别:"), gbc);
            gbc.gridy++;
            panel.add(new JLabel("商品价格（元）:"), gbc);
            gbc.gridy++;
            panel.add(new JLabel("商品状态:"), gbc);
            gbc.gridy++;
            panel.add(new JLabel("商品图片:"), gbc);
            gbc.gridx = 1;

            getContentPane().add(panel, BorderLayout.CENTER);
        }

        //刷新修改界面的图片
        public void refreshphoto() {
            // 获取项目路径
            String projectPath = System.getProperty("user.dir");
            // 构建新图片路径
            ImageIcon updatedIcon = getImageIcon(projectPath);
            // 获取图片对象
            Image updatedImage = updatedIcon.getImage();
            // 缩放图片（如果需要）
            Image scaledImage = updatedImage.getScaledInstance(400, 300, Image.SCALE_SMOOTH);
            // 创建一个新的 ImageIcon
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            // 更新图片标签
            imageLabel.setIcon(scaledIcon);
        }

        public ImageIcon getImageIcon(String projectPath) {
            String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + newImgName;
            // 创建新的 ImageIcon
            ImageIcon updatedIcon;
            File updatedImageFile = new File(absoluteImagePath);
            if (updatedImageFile.exists()) {
                updatedIcon = new ImageIcon(absoluteImagePath);
            } else {
                // 图片路径不存在，使用默认图片
                String defaultImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + "R.jpg";
                updatedIcon = new ImageIcon(defaultImagePath);
            }
            return updatedIcon;
        }

        //复制图片文件
        public boolean FilephotoCopy(String newImgPath, String newImgName) {
            String currentDirectory = System.getProperty("user.dir");
            String destinationPath = currentDirectory + "\\src\\Img\\" + newImgName;
            File sourceFile = new File(newImgPath);
            File destinationFile = new File(destinationPath);
            try {
                Path source = sourceFile.toPath();
                Path destination = destinationFile.toPath();
                // 使用Files.copy方法复制文件
                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("文件复制成功");
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件复制失败");
                return false;
            }
        }

        // 用于处理“Update”按钮功能的方法
        protected boolean handleUpdateButton(String newName, String newDesc, String newClass, double newPrice, String newStatus) {
            boolean success;
            success = dataControl.updateProduct(product.getP_id(), newName, newDesc, newClass, newPrice, newStatus, newImgName);
            return success;
        }

        // 用于处理“Change Image”按钮功能的方法
        protected boolean handleChangeImageButton() {
            JFileChooser fileChooser = new JFileChooser();
            int returnVal = fileChooser.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                String newImgPath = fileChooser.getSelectedFile().getPath();
                newImgName = new File(newImgPath).getName();
                boolean b = FilephotoCopy(newImgPath, newImgName);

                if (b) {
                    // Update the image label with the new image
                    ImageIcon updatedIcon = getImageIcon(newImgName);
                    imageLabel.setIcon(updatedIcon);
                    // Revalidate and repaint the container to reflect the changes
                    imageLabel.getParent().revalidate();
                    imageLabel.getParent().repaint();
                    return true;
                }
            }
            return false;
        }


    }

    //更新界面
    public class ProductUpdateDialog extends ProductofJDialog {
        public ProductUpdateDialog(Product product) {
            super(product);
            initComponents();
        }

        private void initComponents() {
            setTitle("修改商品信息");
            int textFieldColumns = 50;
            gbc.gridx = 1;
            gbc.gridy = 0;
            JTextField nameField = new JTextField(product.getP_name(), textFieldColumns);
            panel.add(nameField, gbc);
            gbc.gridy++;
            JTextField descField = new JTextField(product.getP_desc(), textFieldColumns);
            panel.add(descField, gbc);
            gbc.gridy++;
            JTextField classField = new JTextField(product.getP_class(), textFieldColumns);
            panel.add(classField, gbc);
            gbc.gridy++;
            JTextField priceField = new JTextField(String.valueOf(product.getP_price()), textFieldColumns);
            panel.add(priceField, gbc);
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
            //商品图片展示
            imageLabel = createImageLabel(product, 400, 300);
            panel.add(imageLabel, gbc);
            // 创建修改图片按钮
            gbc.gridx = 2;

            // 创建“Change Image”按钮
            JButton changeImgButton = new JButton("修改图片");
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
            updateButton.addActionListener(e -> {
                // 从文本字段和单选按钮获取值
                String newName = nameField.getText();
                String newDesc = descField.getText();
                String newClass = classField.getText();
                double newPrice = Double.parseDouble(priceField.getText());
                String newStatus = onSaleRadioButton.isSelected() ? "上架" : "下架";

                // 调用父类的方法
                boolean success = handleUpdateButton(newName, newDesc, newClass, newPrice, newStatus);

                if (success) {
                    JOptionPane.showMessageDialog(this, "修改成功，请等待一会", "提示", JOptionPane.INFORMATION_MESSAGE);
                    refreshCard1Product(new Product(product.getP_id(), newName, newDesc, newClass, newPrice, newStatus, newImgName));
                    dispose(); // 关闭对话框
                } else {
                    JOptionPane.showMessageDialog(this, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            });


            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.add(updateButton);
            getContentPane().add(buttonPanel, BorderLayout.SOUTH);
        }
    }

    //详情界面
    public class ProductDetailsDialog extends ProductofJDialog {
        public ProductDetailsDialog(Product product) {
            super(product);
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
            imageLabel = createImageLabel(product, 400, 300);
            panel.add(imageLabel, gbc);
        }
    }

    //添加商品界面
    public class ProductAddDialog extends ProductofJDialog {
        public ProductAddDialog(Product product) {
            super(product);
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

            imageLabel = createImageLabel(product, 400, 300);
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
                if (newName.isEmpty() || newDesc.isEmpty() || newClass.isEmpty() || priceText.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "请填写完整", "警告", JOptionPane.WARNING_MESSAGE);
                } else {
                    double newPrice = Double.parseDouble(priceText);
                    String newStatus = onSaleRadioButton.isSelected() ? "上架" : "下架";
                    // 调用父类的方法
                    boolean success = dataControl.addProduct(m_id, newName, newDesc, newClass, newPrice, newStatus, newImgName);
                    if (success) {
                        JOptionPane.showMessageDialog(this, "创建成功，请等待一会", "提示", JOptionPane.INFORMATION_MESSAGE);
                        refreshCard1Product(new Product(newName, newDesc, newClass, newPrice, newStatus, newImgName, m_id));
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

    //主界面的创建
    private void initComponents() {
        mainPanel.setLayout(cardLayout);
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);
        //第一个界面------------------------------------------------
        card1 = new JPanel();
        card1.setLayout(new GridLayout(0, 2));
        JScrollPane scrollPane = new JScrollPane(card1);// 创建一个 JScrollPane 来包装 card1 面板
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();// 获取垂直滚动条
        verticalScrollBar.setUnitIncrement(20);// 设置单次滚动单位的大小为 20 像素
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);// 设置垂直滚动条自动出现
        createproductcard();

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
        JButton upproject = new JButton("商品管理");
        JButton downproject = new JButton("暂定");
        JButton evaluate = new JButton("评价");
        JButton myButton = new JButton("我的");

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
        // 设置商品对象为面板的客户属性
        productPanel.putClientProperty("product", product);
        // 创建商品图片标签并添加到productPanel的西边
        JLabel imageLabel = createImageLabel(product, 350, 250);
        productPanel.add(imageLabel, BorderLayout.WEST);
        // 创建商品信息面板
        JPanel infoPanel = getjPanel(product);
        // 添加商品按钮面板
        JPanel buttonPanel = getPanel(product);
        // 将商品信息面板和按钮面板添加到productPanel的中部
        productPanel.add(infoPanel, BorderLayout.CENTER);
        productPanel.add(buttonPanel, BorderLayout.EAST);
        return productPanel;
    }

    private JPanel getPanel(Product product) {
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

        // 创建“删除”按钮并添加到按钮面板
        JButton deleteButton = new JButton("删除");
        deleteButton.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(null, "确定要删除该商品吗？", "确认删除", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                // 获取要删除的商品的唯一标识，通常是商品ID
                int productId = product.getP_id();
                // 执行删除商品的操作，你需要实现该方法
                boolean success;
                try {
                    success = dataControl.deleteProduct(productId);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
                if (success) {
                    // 删除成功
                    JOptionPane.showMessageDialog(null, "商品删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    // 刷新界面，删除商品对应的面板
                    card1.remove(buttonPanel.getParent());
                    card1.revalidate();
                    card1.repaint();
                } else {
                    // 删除失败
                    JOptionPane.showMessageDialog(null, "商品删除失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        buttonPanel.add(deleteButton);
        return buttonPanel;
    }

    private static JPanel getjPanel(Product product) {
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
        return infoPanel;
    }

    // 创建包含商品图片的JLabel
    private JLabel createImageLabel(Product product, int width, int height) {
        // 获取当前项目的绝对路径
        String projectPath = System.getProperty("user.dir");

        // 构建图片路径
        ImageIcon originalIcon = getImageIcon(product, projectPath);
        // 获取图片对象
        Image originalImage = originalIcon.getImage();

        // 缩放图片（如果需要）可以改图片的大小
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // 创建一个新的 ImageIcon
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // 创建一个 JLabel 并将缩放后的 ImageIcon 设置为其图标
        return new JLabel(scaledIcon);
    }

    private static ImageIcon getImageIcon(Product product, String projectPath) {
        String relativeImagePath = product.getP_img();
        String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + relativeImagePath;
        // 创建 ImageIcon
        ImageIcon originalIcon;
        File imageFile = new File(absoluteImagePath);
        if (imageFile.exists()) {
            originalIcon = new ImageIcon(absoluteImagePath);
        } else {
            // 图片路径不存在，使用默认图片
            String defaultImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + "R.jpg";
            originalIcon = new ImageIcon(defaultImagePath);
        }
        return originalIcon;
    }


    private void createproductcard() {
        try {
            DataControl dataControl = new DataControl();
            List<Product> products = dataControl.MerchantProductQuery(getM_id());
            for (Product product : products) {
                JPanel productPanel = createProductPanel(product);
                productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                card1.add(productPanel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void refreshCard1All() {
        card1.removeAll();
        try {
            dataControl = new DataControl();
            List<Product> products = dataControl.MerchantProductQuery(getM_id());
            for (Product product : products) {
                JPanel productPanel = createProductPanel(product);
                productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
                card1.add(productPanel);
            }
            card1.revalidate();
            card1.repaint();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //刷新修改后的商品信息
    private void refreshCard1Product(Product updatedProduct) {
        // 查找要更新的商品的位置
        int index = -1;
        Component[] components = card1.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] instanceof JPanel) {
                JPanel productPanel = (JPanel) components[i];
                Product product = (Product) productPanel.getClientProperty("product");
                if (product != null && product.getP_id() == updatedProduct.getP_id()) {
                    index = i;
                    break;
                }
            }
        }
        // 如果找到了要更新的商品，将其删除，然后重新插入原来的位置
        if (index >= 0) {
            card1.remove(index);
            JPanel productPanel = createProductPanel(updatedProduct);
            productPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
            card1.add(productPanel, index);
            card1.revalidate();
            card1.repaint();
        }
    }

    //创建菜单
    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("菜单");
        JButton refreshButton = new JButton("刷新");
        searchButton = new JButton("搜索");
        Product newproduct = new Product();
        JButton addProductButton = new JButton("增加商品");


        refreshButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                refreshCard1All();
            }
        });
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "card5");
            }
        });

        addProductButton.addActionListener(e -> {
            ProductAddDialog detailsDialog = new ProductAddDialog(newproduct);
            detailsDialog.setVisible(true);
        });
        JPanel buttonPanel = new JPanel(new GridLayout(3, 0)); // Use GridLayout with 3 columns
        buttonPanel.add(refreshButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(addProductButton);
        menu.add(buttonPanel);
        menuBar.add(menu);
        return menuBar;
    }

    public static void main(String[] args) {
        // 将Swing应用程序放在Event Dispatch Thread (EDT)中运行
        SwingUtilities.invokeLater(() -> {
            MerchantInterFrm frame = new MerchantInterFrm(1);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 1000);
            frame.setVisible(true);
        });
    }
}

