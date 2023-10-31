package cn.bjut.jdbc;


import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class MerchantProductSearch extends JPanel {
    private DataControl dataControl;
    private MerchantInterFrm merchantInterFrm;

    private JComboBox<String> searchTypeComboBox;
    private JTextField searchField;
    private JButton searchButton;
    private JButton showAllButton;

    public MerchantProductSearch(DataControl dataControl, MerchantInterFrm merchantInterFrm) {
        this.dataControl = dataControl;
        this.merchantInterFrm = merchantInterFrm;

        setLayout(new FlowLayout());

        // 创建选择搜索类型的下拉框
        String[] searchTypes = {"商品名称", "类别", "价格", "状态", "数量"};
        searchTypeComboBox = new JComboBox<>(searchTypes);
        add(searchTypeComboBox);

        // 创建搜索输入框
        searchField = new JTextField(20);
        add(searchField);

        // 创建搜索按钮
        searchButton = new JButton("查找");
        add(searchButton);

        // 创建显示全部按钮
        showAllButton = new JButton("显示全部");
        add(showAllButton);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch();
            }
        });

        showAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAllProducts();
            }
        });
    }

    private void performSearch() {
        String searchType = (String) searchTypeComboBox.getSelectedItem();
        String searchValue = searchField.getText();

        // 执行商品搜索
        List searchResults = (List) dataControl.searchProducts(searchType, searchValue);


    }

    private void showAllProducts() {
        // 显示所有商品

    }
}
