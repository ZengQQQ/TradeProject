package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MerchantOrdersSearch extends JPanel {
    private final DataControl data;
    private final MerchantOrdersFrm merorderFrm;
    private JComboBox<String> productsearchBox= new JComboBox<>(new String[]{"商品名称", "类别", "价格", "数量"});
    private JComboBox<String> usersearchBox = new JComboBox<>(new String[]{"用户名", "性别", "电话"});
    private JComboBox<String> datesearchBox= new JComboBox<>(new String[]{"日期", "年", "月", "日"});
    private JTextField searchproductField= new JTextField(20);
    private JTextField usersearchField= new JTextField(20);
    private JTextField quantityField = new JTextField(20);
    private JTextField totalPriceField = new JTextField(20);
    private JTextField dateField = new JTextField(20);
    private MerchantOrdersFrm orderDisplayPanel;
    private JPanel ordersPanel ;

    public MerchantOrdersSearch(DataControl data, MerchantOrdersFrm parentFrame) throws SQLException {
        this.data = data;
        this.merorderFrm = parentFrame;
        initComponents();
    }

    private void initComponents() throws SQLException {
        setLayout(new BorderLayout());
        JPanel searchPanel = createSearchPanel();
        ordersPanel = new JPanel();
        ordersPanel.setLayout(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(ordersPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        showAllOrders();
    }

    private JPanel createSearchPanel() {
        JPanel searchPanel = new JPanel();
        JPanel topSearchPanel = new JPanel();
        JPanel bottomSearchPanel = new JPanel();

        JButton searchButton = new JButton("查找");
        JButton allButton = new JButton("显示全部");

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

        topSearchPanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        topSearchPanel.add(new JLabel("商品信息搜索："), c);
        c.gridx = 1;
        topSearchPanel.add(productsearchBox, c);
        c.gridx = 2;
        topSearchPanel.add(searchproductField, c);
        c.gridx = 3;
        topSearchPanel.add(new JLabel("用户信息搜索："), c);
        c.gridx = 4;
        topSearchPanel.add(usersearchBox, c);
        c.gridx = 5;
        topSearchPanel.add(usersearchField, c);

        c.gridx = 6;
        topSearchPanel.add(searchButton, c);
        c.gridx = 7;
        topSearchPanel.add(allButton, c);

        bottomSearchPanel.setLayout(new GridBagLayout());
        c = new GridBagConstraints();
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        bottomSearchPanel.add(new JLabel("购买商品数量："), c);
        c.gridx = 1;
        bottomSearchPanel.add(quantityField, c);

        c.gridx = 2;
        bottomSearchPanel.add(new JLabel("订单总价格："), c);
        c.gridx = 3;
        bottomSearchPanel.add(totalPriceField, c);

        c.gridx = 4;
        bottomSearchPanel.add(new JLabel("购买时间："), c);
        c.gridx = 5;
        bottomSearchPanel.add(datesearchBox, c);
        c.gridx=6;
        bottomSearchPanel.add(dateField, c);

        searchPanel.setLayout(new GridLayout(2, 1));
        searchPanel.add(topSearchPanel);
        searchPanel.add(bottomSearchPanel);

        return searchPanel;
    }

    private void showAllOrders() throws SQLException {
        ordersPanel.removeAll();

        orderDisplayPanel = new MerchantOrdersFrm(data, merorderFrm.m_id);
        ordersPanel.add(orderDisplayPanel);
        ordersPanel.revalidate();
        ordersPanel.repaint();
    }
    private void performSearch() throws SQLException {
        // Retrieve search criteria from the search type and text fields
        String productType = (String) productsearchBox.getSelectedItem();
        String userType = (String) usersearchBox.getSelectedItem();
        String productf = searchproductField.getText();
        String userf = usersearchField.getText();
        String quantityf = quantityField.getText();
        String totalpricef = totalPriceField.getText();
        String dateType = (String) datesearchBox.getSelectedItem();
        String datef = dateField.getText();
        // 输入验证规则
        String validationError = validateInput(productType, userType, dateType, productf, userf, quantityf, totalpricef, datef);
        if (validationError != null) {
            JOptionPane.showMessageDialog(null, validationError, "输入验证错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        List<Order> orders = data.searchOrders( merorderFrm.m_id,productType, userType,dateType, productf, userf, quantityf, totalpricef, datef);

        if (orders != null) {
            // Clear the existing order display panel
            ordersPanel.removeAll();

            // Create a new order display panel with the search results
            orderDisplayPanel = new MerchantOrdersFrm(data, merorderFrm.m_id, orders);
            ordersPanel.add(orderDisplayPanel);

            ordersPanel.revalidate();
            ordersPanel.repaint();
        }
    }
    //返回错误消息
    private String validateInput(String productType, String userType, String dateType, String productf, String userf, String quantityf, String totalpricef, String datef) {
        // 商品信息搜索验证
        if (!validateProductInput(productType, productf)) {
            return "商品信息搜索验证失败，请检查输入！";
        }
        // 用户信息搜索验证
        if (!validateUserInput(userType, userf)) {
            return "用户信息搜索验证失败，请检查输入！";
        }
        // 购买商品数量验证
        if (!validateQuantityInput(quantityf)) {
            return "购买商品数量验证失败，请检查输入！";
        }
        // 订单总价格验证
        if (!validateTotalPriceInput(totalpricef)) {
            return "订单总价格验证失败，请检查输入！";
        }
        // 购买时间验证
        if (!validateDateInput(dateType, datef)) {
            return "购买时间验证失败，请检查输入！";
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

    // 用户信息搜索验证规则
    private boolean validateUserInput(String userType, String userf) {
        switch (userType) {
            case "用户名":
                // 用户名可以包含字母、数字、汉字和常见标点符号
                if (!userf.matches("^[a-zA-Z0-9\\u4e00-\\u9fa5.,!?-]*$")) {
                    return false;
                }
                break;
            case "性别":
                // 性别必须是"男"或"女"
                if (!userf.equals("男") && !userf.equals("女")) {
                    return false;
                }
                break;
            case "电话":
                // 电话号码验证规则，可以根据实际需求进一步完善
                // 假设电话号码必须是数字且长度为11
                if (!userf.matches("^[0-9]{11}$")) {
                    return false;
                }
                break;
            default:
                // 未知的用户信息搜索类型
                return false;
        }
        return true;
    }

    // 购买商品数量验证规则
    private boolean validateQuantityInput(String quantityf) {
        return quantityf == null || quantityf.isEmpty() || isInteger(quantityf);
    }

    // 订单总价格验证规则
    private boolean validateTotalPriceInput(String totalpricef) {
        return totalpricef == null || totalpricef.isEmpty() || isNumeric(totalpricef);
    }

    // 购买时间验证规则
    private boolean validateDateInput(String dateType, String datef) {
        if (datef.isEmpty()){
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

