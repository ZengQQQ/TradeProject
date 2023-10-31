package cn.bjut.jdbc;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MerchantProductSearch extends JPanel {
    private DataControl dataControl;
    private MerchantInterFrm merchantInterFrm;

    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel productsPanel;

    public MerchantProductSearch(DataControl dataControl, MerchantInterFrm merchantInterFrm) {
        this.dataControl = dataControl;
        this.merchantInterFrm = merchantInterFrm;

        setLayout(new BorderLayout());

        // 创建搜索面板
        JPanel searchPanel = new JPanel();
        searchTypeComboBox = new JComboBox<>(new String[]{"商品名称", "类别", "价格", "状态", "数量"});
        searchField = new JTextField(20);
        searchButton = new JButton("查找");
        JButton allbuttons = new JButton("显示全部");

        searchPanel.add(searchTypeComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(allbuttons);
        add(searchPanel, BorderLayout.NORTH);

        // 创建用于展示产品的面板，使用GridLayout布局
        productsPanel = new JPanel();
        productsPanel.setLayout(new GridLayout(0, 2));

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });
        allbuttons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllProducts();
            }
        });


        // 创建一个滚动窗格，设置垂直滚动条单位增量
        JScrollPane productsScrollPane = new JScrollPane(productsPanel);
        JScrollBar verticalScrollBar = productsScrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(30);

        // 设置垂直滚动条自动出现
        productsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // 添加滚动窗格到主面板
        add(productsScrollPane, BorderLayout.CENTER);
        showAllProducts();
    }

    private void performSearch() {
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String searchValue = searchField.getText();

        try {
            List<Product> searchResults = dataControl.searchProducts(merchantInterFrm.getM_id(), searchType, searchValue);
            if (searchResults.isEmpty()) {
                JOptionPane.showMessageDialog(this, "找不到结果", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
            } else {
                displaySearchResults(searchResults);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "搜索商品时出错: " + ex.getMessage(), "搜索错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void displaySearchResults(List<Product> products) {
        productsPanel.removeAll();
        for (Product product : products) {
            JPanel productPanel = createProductPanel(product);
            productsPanel.add(productPanel);
        }
        productsPanel.revalidate();
        productsPanel.repaint();
    }

    private void showAllProducts() {
        try {
            List<Product> allProducts = dataControl.searchProducts(merchantInterFrm.getM_id(), null, null);
            displaySearchResults(allProducts);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "获取所有商品时出错: " + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createProductPanel(Product product) {
        JPanel productPanel = new JPanel();
        productPanel.setLayout(new BorderLayout());
        productPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // 添加边框以分隔产品
        productPanel.putClientProperty("product", product);
        JLabel imageLabel = createImageLabel(product, 350, 280);
        productPanel.add(imageLabel, BorderLayout.WEST);
        JPanel infoPanel = createInfoPanel(product);
        productPanel.add(infoPanel, BorderLayout.CENTER);

        return productPanel;
    }

    private JPanel createInfoPanel(Product product) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridLayout(0,1));

        // 添加商品名称
        String productName = "商品名称: " + product.getP_name();
        JTextArea nameTextArea = createWrappedTextArea(productName, 30); // 根据需要调整字符限制
        infoPanel.add(nameTextArea);

        // 添加商品描述
        String productDesc = "商品描述: " + product.getP_desc();
        JTextArea descTextArea = createWrappedTextArea(productDesc, 30); // 根据需要调整字符限制
        infoPanel.add(descTextArea);

        // 添加商品类别
        String productClass = "商品类别: " + product.getP_class();
        JTextArea classTextArea = createWrappedTextArea(productClass, 30); // 根据需要调整字符限制
        infoPanel.add(classTextArea);

        // 添加商品价格
        String productPrice = "商品价格: " + product.getP_price() + "元";
        JTextArea priceTextArea = createWrappedTextArea(productPrice, 30); // 根据需要调整字符限制
        infoPanel.add(priceTextArea);

        // 添加商品状态
        String productStatus = "商品状态: " + product.getP_status();
        JTextArea statusTextArea = createWrappedTextArea(productStatus, 30); // 根据需要调整字符限制
        if ("上架".equals(product.getP_status())) {
            statusTextArea.setForeground(Color.GREEN);
        } else if ("下架".equals(product.getP_status())) {
            statusTextArea.setForeground(Color.RED);
        }
        infoPanel.add(statusTextArea);

        // 添加商品数量
        String productQuantity = "商品数量: " + product.getP_quantity();
        JTextArea quantityTextArea = createWrappedTextArea(productQuantity, 30); // 根据需要调整字符限制
        infoPanel.add(quantityTextArea);

        return infoPanel;
    }

    private JTextArea createWrappedTextArea(String text, int maxCharsPerLine) {
        JTextArea textArea = new JTextArea(text);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        textArea.setOpaque(false); // 使背景透明
        textArea.setFocusable(false);
        textArea.setMargin(new Insets(2, 2, 2, 2)); // 添加一些内边距
        textArea.setRows(2); // 设置行数（根据需要进行调整）

        // 限制每行的字符数
        String[] lines = text.split("\\r?\\n");
        StringBuilder wrappedText = new StringBuilder();
        for (String line : lines) {
            if (line.length() > maxCharsPerLine) {
                for (int i = 0; i < line.length(); i += maxCharsPerLine) {
                    wrappedText.append(line, i, Math.min(i + maxCharsPerLine, line.length()));
                    wrappedText.append('\n');
                }
            } else {
                wrappedText.append(line);
            }

        }

        textArea.setText(wrappedText.toString());
        return textArea;
    }

    private JLabel createImageLabel(Product product, int width, int height) {
        String projectPath = System.getProperty("user.dir");
        ImageIcon originalIcon = getImageIcon(product, projectPath);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        return new JLabel(scaledIcon);
    }

    private ImageIcon getImageIcon(Product product, String projectPath) {
        String relativeImagePath = product.getP_img();
        String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + relativeImagePath;
        ImageIcon originalIcon;
        File imageFile = new File(absoluteImagePath);
        if (imageFile.exists()) {
            originalIcon = new ImageIcon(absoluteImagePath);
        } else {
            String defaultImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + "R.jpg";
            originalIcon = new ImageIcon(defaultImagePath);
        }
        return originalIcon;
    }
}