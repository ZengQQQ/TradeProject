package cn.bjut.jdbc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel mainPanel = new JPanel();
    private JPanel card1;
    private final int m_id;
    public DataControl dataControl = new DataControl();

    public MerchantInterFrm(int mid) throws SQLException {
        this.m_id = mid;
        initComponents();
    }

    public int getM_id() {
        return m_id;
    }

    public class MerchantInfoModifyDialog extends JDialog {
        private  Merchant oldmerchant;
        public MerchantInfoModifyDialog(Merchant merchant) {
            this.oldmerchant =merchant;
            initialize();
        }
        private void initialize() {
            setTitle("修改您的信息");

            // 创建文本字段，用于填入旧的商家信息
            JTextField accountField = new JTextField(oldmerchant.getAcc(), 20);
            JTextField nameField = new JTextField(oldmerchant.getM_name(), 20);
            JTextField genderField = new JTextField(oldmerchant.getM_sex(), 20);
            JTextField phoneField = new JTextField(oldmerchant.getM_tele(), 20);
            // 创建 "保存" 按钮
            JButton saveButton = new JButton("保存");
            saveButton.addActionListener(e -> {
                // 从文本字段中获取修改后的信息
                String newAccount = accountField.getText();
                String newName = nameField.getText();
                String newGender = genderField.getText();
                String newPhone = phoneField.getText();

                // 验证性别（假设性别应为 "男" 或 "女"）
                if (!newGender.equals("男") && !newGender.equals("女")) {
                    JOptionPane.showMessageDialog(this, "性别必须是'男'或'女'", "错误", JOptionPane.ERROR_MESSAGE);
                    return; // 不继续执行后续操作
                }
                // 验证电话号码（您可以使用正则表达式进行更复杂的验证）
                if (!newPhone.matches("\\d{11}")) {
                    JOptionPane.showMessageDialog(this, "电话号码必须为11位数字", "错误", JOptionPane.ERROR_MESSAGE);
                    return; // 不继续执行后续操作
                }

                // 此时，性别和电话号码验证成功
                // 您现在可以保存修改后的信息并显示成功消息
                // 可以将信息保存到您的数据源
                oldmerchant.setAcc(newAccount);
                oldmerchant.setM_name(newName);
                oldmerchant.setM_sex(newGender);
                oldmerchant.setM_tele(newPhone);

                JOptionPane.showMessageDialog(this, "信息修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            });

            // 添加组件到对话框的内容窗格
            JPanel panel = new JPanel(new GridLayout(6, 2));
            panel.add(new JLabel("账号名:"));
            panel.add(accountField);
            panel.add(new JLabel("姓名:"));
            panel.add(nameField);
            panel.add(new JLabel("性别:"));
            panel.add(genderField);
            panel.add(new JLabel("电话:"));
            panel.add(phoneField);
            panel.add(saveButton);

            getContentPane().add(panel);
            pack();
            setLocationRelativeTo(null);  // 居中显示对话框
        }

    }
    //主界面的创建
    private void initComponents() throws SQLException {
        setTitle("商家管理界面");
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
        JPanel card4JPanel = getCard4(m_id);
        // 将“我的信息”面板添加到 mainPanel
        mainPanel.add(card4JPanel, "card4");

        //搜索界面------------------------------------------------
        JPanel card5 = new JPanel();
        JTextField textField = new JTextField(10);
        textField.setText("请输入搜索内容");
        card5.add(textField);

        mainPanel.add(card2, "card2");
        mainPanel.add(card3, "card3");
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

        JPanel buttonPanel2 = new JPanel();
        buttonPanel2.setLayout(new GridLayout(1, 4));
        buttonPanel2.add(upproject);
        buttonPanel2.add(downproject);
        buttonPanel2.add(evaluate);
        buttonPanel2.add(myButton);

        contentPane.add(searchButton, BorderLayout.NORTH);
        contentPane.add(mainPanel, BorderLayout.CENTER);
        contentPane.add(buttonPanel2, BorderLayout.SOUTH);
        //----------------------------------------------------------------
        pack();
        setLocationRelativeTo(getOwner());
    }

    private JPanel getCard4(int m_id) throws SQLException {
        JPanel card4 = new JPanel(new GridBagLayout());
        GridBagConstraints card4gbc = new GridBagConstraints();
        card4gbc.insets = new Insets(1, 1, 5, 5);
        card4gbc.gridx = 0;
        card4gbc.gridy = 0;
        card4gbc.anchor = GridBagConstraints.NORTH;
        //获取商家信息
        Merchant merchant =dataControl.MerchantQuery(m_id);
        // 创建一个“修改”按钮
        JButton modifyButton = new JButton("修改信息");
        modifyButton.addActionListener(e -> {
            // 打开一个对话框以修改信息
            MerchantInfoModifyDialog modifyDialog = new MerchantInfoModifyDialog(merchant);
            modifyDialog.setVisible(true);
        });
        // 将标签和文本字段添加到 card4 面板
        card4.add(new JLabel("账号名: "+ merchant.getAcc()), card4gbc);
        card4gbc.gridy++;
        card4.add(new JLabel("昵称: "+ merchant.getM_name()), card4gbc);
        card4gbc.gridy++;
        card4.add(new JLabel("性别: "+ merchant.getM_sex()), card4gbc);
        card4gbc.gridy++;
        card4.add(new JLabel("电话: "+merchant.getM_tele()), card4gbc);
        // 将“修改”按钮添加到信息下方
        card4gbc.gridx = 1;
        card4.add(modifyButton, card4gbc);
        return card4;
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
        // 为按钮信息面板添加线框
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

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

        //添加商品数量
        JLabel quantityLabel = new JLabel("商品数量: " + product.getP_quantity());
        infoPanel.add(quantityLabel);
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
            MerchantInterFrm frame = null;
            try {
                frame = new MerchantInterFrm(1);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1200, 1000);
            frame.setVisible(true);
        });
    }
}

