package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
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


}

