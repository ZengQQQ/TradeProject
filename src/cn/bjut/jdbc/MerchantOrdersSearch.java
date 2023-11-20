package cn.bjut.jdbc;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MerchantOrdersSearch extends JPanel {
    private DataControlOrder dataControlOrder = new DataControlOrder();
    private final MerchantOrdersFrm merorderFrm;
    private final Font font = new Font("微软雅黑", Font.PLAIN, 16);

    // 创建独立的搜索框面板
    private JPanel productSearchPanel = new JPanel();
    private JPanel userSearchPanel = new JPanel();
    private JPanel orderSearchPanel = new JPanel();

    // 商品搜索框
    private JTextField productIdField;
    private JTextField productNameField;
    private JTextField minpriceField;
    private JTextField maxPriceField;
    private JTextField minproductField;
    private JTextField maxproductField;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> auditStatusComboBox;
    public JComboBox<String> categoryComboBox;
    // 用户搜索框
    private JTextField usernameField;
    private JComboBox<String> genderComboBox;
    private JTextField phoneField;

    // 订单搜索框
    private JPanel ordersPanel;
    private JTextField orderIdField;
    private JTextField minorderField;
    private JTextField maxorderField;
    private JTextField minTotalPriceField;
    private JTextField maxTotalPriceField;
    private JComboBox<String> dateSearchComboBox;
    private JComboBox<String> orderStatusComboBox;
    private JTextField dateField;

    private MerchantOrdersFrm orderDisplayPanel;


    public MerchantOrdersSearch(MerchantOrdersFrm parentFrame) throws SQLException {
        this.merorderFrm = parentFrame;
        initComponents();
    }

    private void initComponents() throws SQLException {
        Font font = new Font("微软雅黑", Font.BOLD, 20);
        setLayout(new BorderLayout());
        // 设置商品搜索框
        productSearchPanel = createProductSearchPanel();
        // 设置用户搜索框
        userSearchPanel = createUserSearchPanel();
        // 设置订单搜索框
        orderSearchPanel = createOrderSearchPanel();

        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS)); // 设置为垂直布局
        searchPanel.add(productSearchPanel);
        searchPanel.add(userSearchPanel);
        searchPanel.add(orderSearchPanel);

        // 添加搜索按钮和显示全部按钮
        ClassLoader classLoader = getClass().getClassLoader();
        Image searchImage = new ImageIcon(classLoader.getResource("Img/th.jpg")).getImage();
        Image refreshImage = new ImageIcon(classLoader.getResource("Img/refresh.png")).getImage();

        Image scaledSearchImage = searchImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledRefreshImage = refreshImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        Icon searchIcon = new ImageIcon(scaledSearchImage);
        Icon refreshIcon = new ImageIcon(scaledRefreshImage);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton searchButton = new JButton("查找",searchIcon);
        searchButton.setFont(font);
        JButton allButton = new JButton("刷新全部",refreshIcon);
        allButton.setFont(font);

        searchButton.addActionListener(e -> {
            try {
                performSearch();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        allButton.addActionListener(e -> {
            try {
                showAllOrders();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        buttonPanel.add(searchButton);
        buttonPanel.add(allButton);

        searchPanel.add(buttonPanel);

        ordersPanel = new JPanel();
        ordersPanel.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        showAllOrders();
    }

    private JPanel createProductSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        TitledBorder innerBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "商品信息搜索框");
        Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        innerBorder.setTitleFont(new Font("微软雅黑", Font.BOLD, 20));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        JPanel searchPanel1 = new JPanel();
        searchPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));

        // 商品ID搜索框
        productIdField = new JTextField(5);
        productIdField.setFont(new Font("微软雅黑", Font.BOLD, 20));
        JLabel productIdLabel = new JLabel("商品ID:");
        productIdLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        searchPanel1.add(productIdLabel);
        searchPanel1.add(productIdField);

        // 商品名称搜索框
        productNameField = new JTextField(15);
        productNameField.setFont(new Font("微软雅黑", Font.BOLD, 20));
        JLabel productNameLabel = new JLabel("商品名称:");
        productNameLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        searchPanel1.add(productNameLabel);
        searchPanel1.add(productNameField);

        // 单价搜索框
        JPanel pricePanel = new JPanel();
        pricePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        minpriceField = new JTextField(5); // 用于输入最小价格
        minpriceField.setFont(new Font("微软雅黑", Font.BOLD, 20));
        JLabel priceLabel = new JLabel("价格范围(￥):");
        priceLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        pricePanel.add(priceLabel);
        pricePanel.add(minpriceField);

        maxPriceField = new JTextField(5); // 用于输入最大价格
        maxPriceField.setFont(new Font("微软雅黑", Font.BOLD, 20));
        JLabel maxPriceLabel = new JLabel("至");
        maxPriceLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        pricePanel.add(maxPriceLabel);
        pricePanel.add(maxPriceField);

        searchPanel1.add(pricePanel);

        // 剩余数量搜索框
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        minproductField = new JTextField(6); // 用于输入最小数量
        minproductField.setFont(new Font("微软雅黑", Font.BOLD, 20));
        JLabel quantityLabel = new JLabel("数量范围:");
        quantityLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        quantityPanel.add(quantityLabel);
        quantityPanel.add(minproductField);

        maxproductField = new JTextField(6); // 用于输入最大数量
        maxproductField.setFont(new Font("微软雅黑", Font.BOLD, 20));
        JLabel maxQuantityLabel = new JLabel("至");
        maxQuantityLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        quantityPanel.add(maxQuantityLabel);
        quantityPanel.add(maxproductField);

        searchPanel1.add(quantityPanel);

        // 添加其他搜索组件，例如下拉框等
        // 商品状态下拉框
        String[] statusOptions = {"全部状态", "上架", "下架"};
        statusComboBox = new JComboBox<>(statusOptions);
        statusComboBox.setSelectedIndex(0);
        statusComboBox.setFont(new Font("微软雅黑", Font.BOLD, 20));

        // 审核状态下拉框
        String[] auditStatusOptions = {"全部审核状态", "待审核", "审核通过", "审核不通过"};
        auditStatusComboBox = new JComboBox<>(auditStatusOptions);
        auditStatusComboBox.setSelectedIndex(0);
        auditStatusComboBox.setFont(new Font("微软雅黑", Font.BOLD, 20));

        // 创建一个面板包含下拉框
        JPanel searchPanel2 = new JPanel();
        searchPanel2.setLayout(new BoxLayout(searchPanel2, BoxLayout.X_AXIS));

        // 创建分类选择下拉框并添加选项
        String[] categories = {"全部类别", "食品", "酒水饮料", "电脑办公", "手机", "服装", "书籍", "厨具", "家居日用", "其他"};
        categoryComboBox = new JComboBox<>(categories);
        categoryComboBox.setSelectedIndex(0); // 默认选择全部类别
        categoryComboBox.setFont(new Font("微软雅黑", Font.BOLD, 20));


        // 将下拉框和按钮放置在面板中
        JLabel categoryLabel = new JLabel("类别:");
        categoryLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        searchPanel2.add(categoryLabel);
        searchPanel2.add(categoryComboBox);

        JLabel statusLabel = new JLabel("商品状态:");
        statusLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        searchPanel2.add(statusLabel);
        searchPanel2.add(statusComboBox);

        JLabel auditStatusLabel = new JLabel("审核状态:");
        auditStatusLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        searchPanel2.add(auditStatusLabel);
        searchPanel2.add(auditStatusComboBox);

        // 添加到searchPanel
        searchPanel.add(searchPanel1, BorderLayout.NORTH);
        searchPanel.add(searchPanel2, BorderLayout.CENTER);

        return searchPanel;
    }

    private JPanel createUserSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        // 创建 TitledBorder
        TitledBorder innerBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "用户信息搜索框");
        Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        innerBorder.setTitleFont(new Font("微软雅黑", Font.BOLD, 20));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        // 创建组件
        JPanel userSearchFieldsPanel = new JPanel();
        userSearchFieldsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        // 用户名搜索框
        usernameField = new JTextField(15);
        usernameField.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        JLabel usernameLabel = new JLabel("用户名:");
        usernameLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        userSearchFieldsPanel.add(usernameLabel);
        userSearchFieldsPanel.add(usernameField);

        // 性别下拉框
        String[] genders = {"全部性别", "男", "女"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        JLabel genderLabel = new JLabel("性别:");
        genderLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        userSearchFieldsPanel.add(genderLabel);
        userSearchFieldsPanel.add(genderComboBox);

        // 电话搜索框
        phoneField = new JTextField(15);
        phoneField.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        JLabel phoneLabel = new JLabel("电话:");
        phoneLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        userSearchFieldsPanel.add(phoneLabel);
        userSearchFieldsPanel.add(phoneField);

        searchPanel.add(userSearchFieldsPanel, BorderLayout.CENTER);

        return searchPanel;
    }

    private JPanel createOrderSearchPanel() {
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        // 创建 TitledBorder
        TitledBorder innerBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "订单信息搜索框");
        Border outerBorder = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        innerBorder.setTitleFont(new Font("微软雅黑", Font.BOLD, 20));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(outerBorder, innerBorder));

        // 创建订单信息搜索框组件
        JPanel orderSearchFieldsPanel1 = new JPanel();
        orderSearchFieldsPanel1.setLayout(new FlowLayout(FlowLayout.LEFT));
        JPanel orderSearchFieldsPanel2 = new JPanel();
        orderSearchFieldsPanel2.setLayout(new FlowLayout(FlowLayout.LEFT));

        // 订单ID搜索框
        orderIdField = new JTextField(15);
        orderIdField.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        JLabel orderIdLabel = new JLabel("订单ID:");
        orderIdLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        orderSearchFieldsPanel1.add(orderIdLabel);
        orderSearchFieldsPanel1.add(orderIdField);

        // 购买数量搜索范围
        JPanel quantityPanel = new JPanel();
        quantityPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        minorderField = new JTextField(6);
        minorderField.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        JLabel quantityRangeLabel = new JLabel("购买数量范围:");
        quantityRangeLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        quantityPanel.add(quantityRangeLabel);
        quantityPanel.add(minorderField);

        maxorderField = new JTextField(6);
        maxorderField.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        JLabel toLabel = new JLabel("至");
        toLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        quantityPanel.add(toLabel);
        quantityPanel.add(maxorderField);

        orderSearchFieldsPanel1.add(quantityPanel);

        // 订单总价格搜索范围
        JPanel totalPricePanel = new JPanel();
        totalPricePanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        minTotalPriceField = new JTextField(6);
        minTotalPriceField.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        JLabel totalPriceRangeLabel = new JLabel("订单总价格范围:");
        totalPriceRangeLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        totalPricePanel.add(totalPriceRangeLabel);
        totalPricePanel.add(minTotalPriceField);

        maxTotalPriceField = new JTextField(6);
        maxTotalPriceField.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        JLabel toTotalPriceLabel = new JLabel("至");
        toTotalPriceLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        totalPricePanel.add(toTotalPriceLabel);
        totalPricePanel.add(maxTotalPriceField);

        orderSearchFieldsPanel1.add(totalPricePanel);

        // 购买时间搜索框
        dateSearchComboBox = new JComboBox<>(new String[]{"日期", "年", "月", "日"});
        dateSearchComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        JLabel dateSearchLabel = new JLabel("购买时间:");
        dateSearchLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        dateField = new JTextField(10);
        dateField.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        orderSearchFieldsPanel2.add(dateSearchLabel);
        orderSearchFieldsPanel2.add(dateSearchComboBox);
        orderSearchFieldsPanel2.add(dateField);

        // 订单状态下拉框
        String[] orderStatusOptions = {"全部状态", "待发货", "待收货", "待退货", "已退货", "已完成"};
        orderStatusComboBox = new JComboBox<>(orderStatusOptions);
        orderStatusComboBox.setFont(new Font("微软雅黑", Font.PLAIN, 20));
        JLabel orderStatusLabel = new JLabel("订单状态:");
        orderStatusLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        orderSearchFieldsPanel2.add(orderStatusLabel);
        orderSearchFieldsPanel2.add(orderStatusComboBox);

        searchPanel.add(orderSearchFieldsPanel1, BorderLayout.NORTH);
        searchPanel.add(orderSearchFieldsPanel2, BorderLayout.CENTER);

        return searchPanel;
    }


    private void showAllOrders() throws SQLException {
        // 清空商品搜索框
        productIdField.setText("");
        productNameField.setText("");
        minpriceField.setText("");
        maxPriceField.setText("");
        minproductField.setText("");
        maxproductField.setText("");
        statusComboBox.setSelectedIndex(0);
        auditStatusComboBox.setSelectedIndex(0);
        categoryComboBox.setSelectedIndex(0);

        // 清空用户搜索框
        usernameField.setText("");
        genderComboBox.setSelectedIndex(0);
        phoneField.setText("");

        // 清空订单搜索框
        orderIdField.setText("");
        minorderField.setText("");
        maxorderField.setText("");
        minTotalPriceField.setText("");
        maxTotalPriceField.setText("");
        dateSearchComboBox.setSelectedIndex(0);
        orderStatusComboBox.setSelectedIndex(0);
        dateField.setText("");

        ordersPanel.removeAll();

        orderDisplayPanel = new MerchantOrdersFrm(merorderFrm.m_id);
        ordersPanel.add(orderDisplayPanel);
        ordersPanel.revalidate();
        ordersPanel.repaint();
    }

    private void performSearch() throws SQLException {
        // 提取商品搜索框内容
        String productId = productIdField.getText();
        String productName = productNameField.getText();
        String minPrice = minpriceField.getText();
        String maxPrice = maxPriceField.getText();
        String minproductQuantity = minproductField.getText();
        String maxproductQuantity = maxproductField.getText();
        String status = (String) statusComboBox.getSelectedItem();
        String auditStatus = (String) auditStatusComboBox.getSelectedItem();
        String category = (String) categoryComboBox.getSelectedItem();

        // 提取用户搜索框内容
        String username = usernameField.getText();
        String gender = (String) genderComboBox.getSelectedItem();
        String phone = phoneField.getText();

        // 提取订单搜索框内容
        String orderId = orderIdField.getText();
        String minOrderQuantity = minorderField.getText();
        String maxOrderQuantity = maxorderField.getText();
        String minTotalPrice = minTotalPriceField.getText();
        String maxTotalPrice = maxTotalPriceField.getText();
        String dateType = (String) dateSearchComboBox.getSelectedItem();
        String date = dateField.getText();
        String orderStatus = (String) orderStatusComboBox.getSelectedItem();
        // 正则表达式定义
        String intRegex = "^\\d+$"; // 大于0的整数
        String positiveDoubleRegex = "^[1-9]\\d{0,5}(\\.\\d+)?$"; // 大于0的六位以下数字
        String eightDigitsRegex = "^[1-9]\\d{0,7}$"; // 大于0的8位以下数字
        String fiftyCharactersRegex = "^.{0,50}$"; // 长度限制为50的字符串

        // 校验输入格式
        StringBuilder errorMessage = new StringBuilder("搜索条件错误：\n");

        if (!productId.isEmpty() && !productId.matches(intRegex)) {
            errorMessage.append("- 商品ID应为大于0的整数\n");
        }
        if (!productName.isEmpty() && !productName.matches(fiftyCharactersRegex)) {
            errorMessage.append("- 商品名称长度应在50字符以内\n");
        }
        if (!minPrice.isEmpty() && !minPrice.matches(positiveDoubleRegex)) {
            errorMessage.append("- 最小价格应为大于0的六位以下数字\n");
        }
        if (!maxPrice.isEmpty() && !maxPrice.matches(positiveDoubleRegex)) {
            errorMessage.append("- 最大价格应为大于0的六位以下数字\n");
        }

        if (!minproductQuantity.isEmpty() && !minproductQuantity.matches(intRegex)) {
            errorMessage.append("- 最小商品数量应为大于0的六位以下整数\n");
        }
        if (!maxproductQuantity.isEmpty() && !maxproductQuantity.matches(intRegex)) {
            errorMessage.append("- 最大商品数量应为大于0的六位以下整数\n");
        }
        //

        if (!username.isEmpty() && !username.matches(fiftyCharactersRegex)) {
            errorMessage.append("- 用户名称长度应在50字符以内\n");
        }
        if (!phone.isEmpty() && !phone.matches(intRegex)) {
            errorMessage.append("- 电话号码应为大于0的整数\n");
        }
        //
        if (!orderId.isEmpty() && !orderId.matches(intRegex)) {
            errorMessage.append("- 订单ID应为大于0的整数\n");
        }
        if (!minOrderQuantity.isEmpty() && !minOrderQuantity.matches(intRegex)) {
            errorMessage.append("- 最小购买数量应为大于0的六位以下整数\n");
        }
        if (!maxOrderQuantity.isEmpty() && !maxOrderQuantity.matches(intRegex)) {
            errorMessage.append("- 最大购买数量应为大于0的六位以下整数\n");
        }
        if (!minTotalPrice.isEmpty() && !minTotalPrice.matches(eightDigitsRegex)) {
            errorMessage.append("- 最小订单总价格应为大于0的8位以下数字\n");
        }
        if (!maxTotalPrice.isEmpty() && !maxTotalPrice.matches(eightDigitsRegex)) {
            errorMessage.append("- 最大订单总价格应为大于0的8位以下数字\n");
        }
        // 执行日期输入的验证
        if (!date.isEmpty() && !validateDateInput(dateType, date)) {
            errorMessage.append("- 购买时间输入不合法\n");
        }

        // 如果有错误，则显示错误消息
        if (errorMessage.length() > 8) {
            JOptionPane.showMessageDialog(this, errorMessage.toString(), "搜索条件错误", JOptionPane.ERROR_MESSAGE);
            return; // 结束函数执行，不进行搜索
        }
        // 执行订单搜索查询
        List<Order> orders = dataControlOrder.searchOrders(
                merorderFrm.m_id,
                productId, productName, minPrice, maxPrice, minproductQuantity, maxproductQuantity, status, auditStatus, category,
                username, gender, phone,
                orderId, minOrderQuantity, maxOrderQuantity, minTotalPrice, maxTotalPrice, dateType, orderStatus, date
        );

        // 根据查询结果显示订单
        if (!orders.isEmpty()) {
            ordersPanel.removeAll();
            orderDisplayPanel = new MerchantOrdersFrm(merorderFrm.m_id, orders);
            ordersPanel.add(orderDisplayPanel);
            ordersPanel.revalidate();
            ordersPanel.repaint();
            return;
        } else {
            ordersPanel.removeAll();

            ordersPanel.revalidate();
            ordersPanel.repaint();
        }
        JOptionPane.showMessageDialog(this, "没有搜索到订单", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
    }

    // 购买时间验证规则
    private boolean validateDateInput(String dateType, String datef) {
        if (datef.isEmpty()) {
            return true;
        }
        switch (dateType) {
            case "日期":
                // 日期格式验证规则，例如检查日期格式是否合法
                if (!isValidDate(datef, "yyyy.M.d")) {
                    return false;
                }
                break;
            case "年":
                // 年份必须是数字且长度为4
                if (!datef.matches("^[0-9]{4}$")) {
                    return false;
                }
                break;
            case "月":
                // 月份必须是数字且在1到12之间
                if (!datef.matches("^(1[0-2]|[1-9])$")) {
                    return false;
                }
                break;
            case "日":
                // 日必须是数字且在1到31之间
                if (!datef.matches("^(3[01]|[12][0-9]|0?[1-9])$")) {
                    return false;
                }
                break;
            default:
                // 未知的购买时间类型
                return false;
        }
        return true;
    }

    // 添加日期格式验证规则
    private boolean isValidDate(String dateStr, String dateFormat) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        sdf.setLenient(false); // 设置为严格的日期格式
        try {
            Date date = sdf.parse(dateStr);
            return date != null;
        } catch (ParseException e) {
            return false;
        }
    }

}

