package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MerchantProductSearch extends JPanel {
    private static final String[] SEARCH_TYPES = {"商品名称", "类别", "价格", "状态", "数量"};
    private static final String DEFAULT_IMAGE_PATH = "src/img/R.jpg";
    private static final int IMAGE_WIDTH = 350;
    private static final int IMAGE_HEIGHT = 280;
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

        JPanel searchPanel = createSearchPanel();
        productsPanel = new JPanel();
        productsPanel.setLayout(new GridLayout(0, 2));

        JScrollPane productsScrollPane = new JScrollPane(productsPanel);
        productsScrollPane.getVerticalScrollBar().setUnitIncrement(30);
        productsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        add(searchPanel, BorderLayout.NORTH);
        add(productsScrollPane, BorderLayout.CENTER);

        showAllProducts();
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchTypeComboBox = new JComboBox<>(SEARCH_TYPES);
        searchField = new JTextField(20);
        searchButton = new JButton("查找");
        JButton allButton = new JButton("显示全部");

        searchButton.addActionListener(e -> performSearch());
        allButton.addActionListener(e -> showAllProducts());

        searchPanel.add(searchTypeComboBox);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(allButton);

        return searchPanel;
    }

    private void performSearch() {
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String searchValue = searchField.getText();
        // 输入验证规则
        String validationError = validateInput(searchType, searchValue);
        if (validationError != null) {
            JOptionPane.showMessageDialog(null, validationError, "输入验证错误", JOptionPane.ERROR_MESSAGE);
            return;
        }

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
        productPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 使用FlowLayout控制布局
        productPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // 添加边框以分隔产品
        productPanel.putClientProperty("product", product);
        JLabel imageLabel = createImageLabel(product);
        productPanel.add(imageLabel);
        JPanel infoPanel = createInfoPanel(product);
        productPanel.add(infoPanel);

        return productPanel;
    }

    private JPanel createInfoPanel(Product product) {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // Use vertical BoxLayout for layout

        Font font = new Font("SansSerif", Font.PLAIN, 16); // Set a consistent font

        // Create and add JLabels for each piece of information with spacing
        infoPanel.add(createLabel("商品名称: " + product.getP_name(), font, 20));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        infoPanel.add(createLabel("商品描述: " + product.getP_desc(), font, 15));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        infoPanel.add(createLabel("商品类别: " + product.getP_class(), font, 15));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        infoPanel.add(createLabel("商品价格: " + product.getP_price() + "元", font, 15));

        // Create and add JLabel for the status with color based on the status
        String statusColor = "";
        if ("上架".equals(product.getP_status())) {
            statusColor = "<font color='green'>上架</font>";
        } else if ("下架".equals(product.getP_status())) {
            statusColor = "<font color='red'>下架</font>";
        }
        JLabel statusLabel = new JLabel("<html>商品状态: " + statusColor + "</html>");
        statusLabel.setFont(font);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        infoPanel.add(createLabel("商品数量: " + product.getP_quantity(), font, 10));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add vertical spacing
        infoPanel.add(statusLabel);

        return infoPanel;
    }

    private JLabel createLabel(String text, Font font, int maxCharsPerLine) {
        JLabel label = new JLabel(wrapText(text, maxCharsPerLine)); // Use the wrapText method to split the text and add HTML tags
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentY(Component.TOP_ALIGNMENT);
        label.setVerticalAlignment(SwingConstants.TOP);
        return label;
    }

    private String wrapText(String text, int maxCharsPerLine) {
        // This method will split the text according to the given max chars per line and add HTML tags to wrap the text
        StringBuilder sb = new StringBuilder();
        sb.append("<html>"); // Start the HTML tag
        String[] lines = text.split("\\r?\\n"); // Split the text by line breaks
        for (String line : lines) {
            if (line.length() > maxCharsPerLine) { // If the line is longer than the max chars per line
                for (int i = 0; i < line.length(); i += maxCharsPerLine) { // Split the line by the max chars per line
                    sb.append(line, i, Math.min(i + maxCharsPerLine, line.length())); // Append the substring
                    sb.append("<br>"); // Append the HTML tag for line break
                }
            } else { // If the line is shorter than or equal to the max chars per line
                sb.append(line); // Append the line
                sb.append("<br>"); // Append the HTML tag for line break
            }
        }
        sb.append("</html>"); // End the HTML tag
        return sb.toString(); // Return the wrapped text
    }

    private JLabel createImageLabel(Product product) {
        String projectPath = System.getProperty("user.dir");
        ImageIcon originalIcon = getImageIcon(product, projectPath);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(IMAGE_WIDTH, IMAGE_HEIGHT, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);
        return new JLabel(scaledIcon);
    }

    private ImageIcon getImageIcon(Product product, String projectPath) {
        String relativeImagePath = product.getP_img();
        String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + relativeImagePath;
        File imageFile = new File(absoluteImagePath);

        if (imageFile.exists()) {
            return new ImageIcon(absoluteImagePath);
        } else {
            return new ImageIcon(projectPath + File.separator + DEFAULT_IMAGE_PATH);
        }
    }

    //返回错误消息
    private String validateInput(String productType, String productf) {
        // 商品信息搜索验证
        if (!validateProductInput(productType, productf)) {
            return "商品信息搜索验证失败，请检查输入！";
        }
        return null; // 没有错误
    }

    // 商品信息搜索验证规则
    private boolean validateProductInput(String productType, String productf) {
        switch (productType) {
            case "商品名称":
                // 商品名称可以包含字母、数字、汉字和常见标点符号
                if (!productf.matches("^[a-zA-Z0-9\\u4e00-\\u9fa5.,!?-]*$")) {
                    return false;
                }
                break;
            case "类别":
                // 类别必须是字符串，不包含数字
                if (productf.matches(".*[0-9].*")) {
                    return false;
                }
                break;
            case "价格":
                // 价格必须是合法的数字
                if (!isNumeric(productf)) {
                    return false;
                }
                break;
            case "数量":
                // 数量必须是整数
                if (!isInteger(productf)) {
                    return false;
                }
                break;
            default:
                // 未知的商品信息搜索类型
                return false;
        }
        return true;
    }

    // 检查字符串是否为数字
    private boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("\\d+(\\.\\d+)?");
    }

    // 检查字符串是否为整数
    private boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.matches("\\d+");
    }
}