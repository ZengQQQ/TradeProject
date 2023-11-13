package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

// 商家商品搜索面板
public class MerchantProductSearch extends JPanel {
    private static final String[] SEARCH_TYPES = {"商品名称", "类别", "价格", "状态", "数量"};
    private static final String DEFAULT_IMAGE_PATH = "src/img/R.jpg";
    private static final int IMAGE_WIDTH = 350;
    private static final int IMAGE_HEIGHT = 280;
    private Font fontall = new Font("微软雅黑", Font.BOLD, 18);
    private  DataControlProduct dataControlProduct  = new DataControlProduct();
    private MerchantInterFrm merchantInterFrm;
    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JPanel productsPanel;

    public MerchantProductSearch(MerchantInterFrm merchantInterFrm) throws SQLException {
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
        searchTypeComboBox.setFont(fontall);
        searchField = new JTextField(20);
        searchButton = new JButton("查找");
        searchButton.setFont(fontall);
        JButton allButton = new JButton("显示全部");
        allButton.setFont(fontall);
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
            List<Product> searchResults = dataControlProduct.searchProducts(merchantInterFrm.getM_id(), searchType, searchValue);
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
            List<Product> allProducts = dataControlProduct.searchProducts(merchantInterFrm.getM_id(), null, null);
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
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS)); // 使用垂直 BoxLayout 布局

        Font font = new Font("微软雅黑", Font.PLAIN, 16); // 设置一致的字体

        // 创建并添加每个信息片段的 JLabel，包括间距
        infoPanel.add(createLabel("商品名称: " + product.getP_name(), font, 17));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(createLabel("商品描述: " + product.getP_desc(), font, 14));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(createLabel("商品类别: " + product.getP_class(), font, 15));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        infoPanel.add(createLabel("商品价格: " + product.getP_price() + "元", font, 15));

        // 创建并添加带有基于状态的颜色的状态 JLabel
        String statusColor = "";
        if ("上架".equals(product.getP_status())) {
            statusColor = "<font color='green'>上架</font>";
        } else if ("下架".equals(product.getP_status())) {
            statusColor = "<font color='red'>下架</font>";
        }
        JLabel statusLabel = new JLabel("<html>商品状态: " + statusColor + "</html>");
        statusLabel.setFont(font);
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 添加垂直间距
        infoPanel.add(createLabel("商品数量: " + product.getP_quantity(), font, 10));
        infoPanel.add(Box.createRigidArea(new Dimension(0, 20))); // 添加垂直间距
        infoPanel.add(statusLabel);

        return infoPanel;
    }

    private JLabel createLabel(String text, Font font, int maxCharsPerLine) {
        JLabel label = new JLabel(wrapText(text, maxCharsPerLine)); // 使用 wrapText 方法拆分文本并添加 HTML 标签
        label.setFont(font);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setAlignmentY(Component.TOP_ALIGNMENT);
        label.setVerticalAlignment(SwingConstants.TOP);
        return label;
    }

    private String wrapText(String text, int maxCharsPerLine) {
        // 此方法将根据给定的每行最大字符数拆分文本并添加 HTML 标签以包装文本
        StringBuilder sb = new StringBuilder();
        sb.append("<html>"); // 开始 HTML 标签
        String[] lines = text.split("\\r?\\n"); // 通过换行符拆分文本
        for (String line : lines) {
            if (line.length() > maxCharsPerLine) { // 如果行长超过了每行最大字符数
                for (int i = 0; i < line.length(); i += maxCharsPerLine) { // 通过每行的最大字符数拆分行
                    sb.append(line, i, Math.min(i + maxCharsPerLine, line.length())); // 追加子字符串
                    sb.append("<br>"); // 追加 HTML 换行标签
                }
            } else { // 如果行长小于或等于每行最大字符数
                sb.append(line); // 追加行
                sb.append("<br>"); // 追加 HTML 换行标签
            }
        }
        sb.append("</html>"); // 结束 HTML 标签
        return sb.toString(); // 返回包装的文本
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

    // 返回错误消息
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
            case "状态":
                // 状态必须是上架或下架
                if (!productf.equals("上架") && !productf.equals("下架")) {
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
