package cn.bjut.jdbc;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class MerchantProductFrm extends JPanel {

    private MerchantInterFrm merchantInterFrm;
    private DataControlMercahnt dataControlmer = new DataControlMercahnt();
    private DataControlProduct dataControlProduct = new DataControlProduct();
    private ProductDetailsDialog currentDetailsDialog;
    private JTable productTable;
    private JTextField productIdField;
    private JTextField productNameField;
    private JTextField minpriceField;
    private JTextField maxPriceField;
    private JTextField minquantityField;
    private JTextField maxQuantityField;
    private JComboBox<String> statusComboBox;
    private JComboBox<String> auditStatusComboBox;

    public JComboBox<String> categoryComboBox;
    DefaultTableModel tableModel;

    public MerchantProductFrm(MerchantInterFrm mer) throws SQLException {
        this.merchantInterFrm = mer;
        initComponent();
    }

    public void initComponent() {
        setLayout(new BorderLayout());

        // 创建包含表格和按钮的新面板，并设置边框和间距
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15)); // 设置边框和间距

        // 创建表格并添加到新面板的中间位置
        createProductTable();
        JScrollPane scrollPane = new JScrollPane(productTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 创建一个新的面板来包装增加商品的按钮和搜索条件
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // 垂直布局

        // 创建搜索面板
        JPanel searchPanel = new JPanel();
        searchPanel.setLayout(new BorderLayout());

        TitledBorder innerBorder = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "搜索框");
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

        minquantityField = new JTextField(6); // 用于输入最小数量
        minquantityField.setFont(new Font("微软雅黑", Font.BOLD, 20));
        JLabel quantityLabel = new JLabel("数量范围:");
        quantityLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        quantityPanel.add(quantityLabel);
        quantityPanel.add(minquantityField);

        maxQuantityField = new JTextField(6); // 用于输入最大数量
        maxQuantityField.setFont(new Font("微软雅黑", Font.BOLD, 20));
        JLabel maxQuantityLabel = new JLabel("至");
        maxQuantityLabel.setFont(new Font("微软雅黑", Font.BOLD, 20));
        quantityPanel.add(maxQuantityLabel);
        quantityPanel.add(maxQuantityField);

        searchPanel1.add(quantityPanel);

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


        ClassLoader classLoader = getClass().getClassLoader();
        Image searchImage = new ImageIcon(classLoader.getResource("Img/th.jpg")).getImage();
        Image refreshImage = new ImageIcon(classLoader.getResource("Img/refresh.png")).getImage();

        Image scaledSearchImage = searchImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        Image scaledRefreshImage = refreshImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        Icon searchIcon = new ImageIcon(scaledSearchImage);
        Icon refreshIcon = new ImageIcon(scaledRefreshImage);

        JButton searchButton = new JButton("搜索", searchIcon);
        searchButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
        searchButton.addActionListener(e -> {
            // 搜索处理
            refreshProductTableBySearch();
        });

        JButton refreshButton = new JButton("刷新", refreshIcon);
        refreshButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
        refreshButton.addActionListener(e -> {
            refreshProductTable();
        });

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


        // 将搜索和刷新按钮放置在面板的最右边
        JPanel rightButtonsPanel = new JPanel();
        rightButtonsPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        rightButtonsPanel.add(searchButton);
        rightButtonsPanel.add(refreshButton);

        // 将右侧按钮面板添加到搜索面板
        searchPanel2.add(rightButtonsPanel);

        // 将分类面板放置在按钮面板的后面
        // 修改 searchPanel1 和 searchPanel2 的布局管理器为 FlowLayout，并设置水平间距
        searchPanel1.setLayout(new FlowLayout(FlowLayout.LEFT, 13, 10)); // 20 是水平间距, 10 是垂直间距
        searchPanel2.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10)); // 20 是水平间距, 10 是垂直间距
        searchPanel.add(searchPanel1, BorderLayout.NORTH);
        searchPanel.add(searchPanel2, BorderLayout.CENTER); // 将搜索面板添加到按钮面板的第一个位置
        buttonPanel.add(searchPanel);

        // 将增加商品的按钮添加在buttonPanel的最左边
        JPanel addProductPanel = new JPanel();
        addProductPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // Left-aligned layout for the '增加商品' button

        Image addImage = new ImageIcon(classLoader.getResource("Img/productaddicon.jpg")).getImage();

        Image scaledaddImage = addImage.getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        Icon addIcon = new ImageIcon(scaledaddImage);

        JButton addProductButton = new JButton("增加商品",addIcon);
        addProductButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
        addProductButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        addProductButton.addActionListener(e -> {
            Product newProduct = new Product();
            ProductAddDialog detailsDialog;
            try {
                detailsDialog = new ProductAddDialog(newProduct, merchantInterFrm, this);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            detailsDialog.setVisible(true);
        });
        addProductPanel.add(addProductButton);
        // 将增加商品按钮添加到新的面板中
        buttonPanel.add(new JPanel());
        buttonPanel.add(new JPanel());
        buttonPanel.add(new JPanel());
        buttonPanel.add(addProductPanel);

        // 将包含增加商品按钮的新面板添加到tablePanel的北部位置
        tablePanel.add(buttonPanel, BorderLayout.NORTH);

        // 将包含表格和按钮的新面板添加到主面板中
        add(tablePanel, BorderLayout.CENTER);

        refreshProductTable(); // 刷新表格数据
    }


    private void createProductTable() {
        String[] columns = {"ID", "图片", "商品名称", "类别", "单价￥", "剩余数量", "商品状态", "审核状态", "操作"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) { // 商品图片所在列
                    return ImageIcon.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // 带有按钮列的功能这里必须要返回true不然按钮点击时不会触发编辑效果，也就不会触发事件。
                if (column == 8) {
                    return true;
                } else {
                    return false;
                }
            }

        };

        productTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component comp = super.prepareRenderer(renderer, row, column);
                if (comp instanceof JLabel) {
                    JLabel label = (JLabel) comp;
                    label.setHorizontalAlignment(SwingConstants.CENTER);
                    label.setFont(new Font("微软雅黑", Font.BOLD, 18));
                    label.setToolTipText(String.valueOf(getValueAt(row, column)));
                    label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1)); // 给单元格添加内边距
                    label.setOpaque(true);

                    Object value = getValueAt(row, column);
                    if (value != null) {
                        if (value instanceof ImageIcon) {
                            ImageIcon icon = (ImageIcon) value;
                            label.setIcon(icon);
                            label.setText("");

                            // 调整单元格大小以适应图像的实际大小
                            int width = icon.getIconWidth();
                            int height = icon.getIconHeight();
                            if (width > 0 && height > 0) {
                                label.setPreferredSize(new Dimension(width, height));
                                label.setMaximumSize(new Dimension(width, height));
                            }
                        } else {
                            String text = "<html><div style='max-width:" + (column == 0 ? 100 : 150) + "px; text-align:center; color:" + getStatusColor(value.toString()) + "'>" + value + "</div></html>";
                            label.setText(text);
                        }
                    } else {
                        label.setText(""); // 设置空文本或其他默认值
                    }
                }
                return comp;
            }

            private String getStatusColor(String status) {
                if ("上架".equals(status) || "审核通过".equals(status)) {
                    return "green"; // 上架状态显示绿色
                } else if ("下架".equals(status) || "审核不通过".equals(status)) {
                    return "red"; // 下架状态显示红色
                }
                return ""; // 其他状态无颜色
            }


        };
        productTable.getColumnModel().getColumn(0).setPreferredWidth(70); // ID
        productTable.getColumnModel().getColumn(1).setPreferredWidth(200); // 图片
        productTable.getColumnModel().getColumn(2).setPreferredWidth(210); // 商品名称
        productTable.getColumnModel().getColumn(3).setPreferredWidth(160); // 商品类别
        productTable.getColumnModel().getColumn(4).setPreferredWidth(150); // 单价￥
        productTable.getColumnModel().getColumn(5).setPreferredWidth(130); // 剩余数量
        productTable.getColumnModel().getColumn(6).setPreferredWidth(120); // 商品状态
        productTable.getColumnModel().getColumn(7).setPreferredWidth(136); // 审核状态
        productTable.getColumnModel().getColumn(8).setPreferredWidth(250); // 操作
        productTable.setDefaultRenderer(Object.class, new ImageCellRenderer());
        productTable.setRowHeight(160); // 设置行高
        // 为表格添加边框
        productTable.setBorder(BorderFactory.createLineBorder(Color.black, 2)); // 设置表格边框颜色和粗细
        productTable.setGridColor(Color.gray);

        productTable.setFillsViewportHeight(true); // 填充表格的视口高度
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF); // 禁用表格自动调整列宽


        productTable.getColumnModel().getColumn(1).setCellRenderer(new ImageCellRenderer()); // 图片列应用图片渲染器

        JTableHeader header = productTable.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 22));
        productTable.getTableHeader().setReorderingAllowed(false); // 禁用列重新排序
        productTable.getTableHeader().setResizingAllowed(false); // 禁用列调整大小

        productTable.getColumnModel().getColumn(8).setCellEditor(new ProductFrmButtonEditor(new JCheckBox(), this));

        productTable.getColumnModel().getColumn(8).setCellRenderer(new ProductFrmButtonRender());

        productTable.setRowSelectionAllowed(false);// 禁止表格的选择功能。
    }

    public void handleDetails(int selectedRow) throws SQLException {
        Object productId = productTable.getValueAt(selectedRow, 0);
        if (productId != null) {
            int p_id = (int) productId;
            Product product = dataControlProduct.getProductFromPId(p_id);
            showProductDetails(product);

        }
    }

    public void handleModify(int selectedRow) throws SQLException {
        Object productId = productTable.getValueAt(selectedRow, 0);
        Object auditstatus = productTable.getValueAt(selectedRow, 7);
        if (auditstatus.equals("待审核")) {
            JOptionPane.showMessageDialog(null, "商品在审核中，不能修改", "提示", JOptionPane.INFORMATION_MESSAGE);
        } else if (productId != null) {
            int p_id = (int) productId;
            Product product = dataControlProduct.getProductFromPId(p_id);
            ProductUpdateDialog updateDialog;
            try {
                updateDialog = new ProductUpdateDialog(product, this, merchantInterFrm);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            updateDialog.setVisible(true);
        }
    }

    public void handleDelete(int selectedRow) {
        Object productId = productTable.getValueAt(selectedRow, 0);
        int p_id = (int) productId;
        int option = JOptionPane.showConfirmDialog(null, "确定要删除该商品吗？", "确认删除", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            // 获取要删除的商品的唯一标识，通常是商品ID
            boolean success;
            try {
                success = dataControlProduct.deleteProduct(p_id);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            if (success) {
                // 删除成功
                JOptionPane.showMessageDialog(null, "商品删除成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                refreshProductTable();
            } else {
                // 删除失败
                JOptionPane.showMessageDialog(null, "商品删除失败", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public void refreshProductTableBySearch() {
        try {
            // 获取搜索框和下拉列表的值
            String productId = productIdField.getText().trim();
            String productName = productNameField.getText().trim();
            String selectedStatus = (String) statusComboBox.getSelectedItem();
            String selectedAuditStatus = (String) auditStatusComboBox.getSelectedItem();
            String selectedCategory = (String) categoryComboBox.getSelectedItem();
            String minPrice = minpriceField.getText().trim();
            String maxPrice = maxPriceField.getText().trim();
            String minQuantity = minquantityField.getText().trim();
            String maxQuantity = maxQuantityField.getText().trim();
            List<Product> filteredProducts;
            boolean isValid = true;
            StringBuilder errorMessage = new StringBuilder("输入错误:\n");

            if (!productId.isEmpty()) {
                if (!isPositiveInteger(productId) || Integer.parseInt(productId) <= 0) {
                    errorMessage.append("- 商品ID必须为大于0的整数\n");
                    isValid = false;
                }
            }

            if (!productName.isEmpty()) {
                if (productName.length() > 50) {
                    errorMessage.append("- 商品名称长度不能超过50\n");
                    isValid = false;
                }
            }

            if (!minPrice.isEmpty() && !maxPrice.isEmpty()) {
                double minPriceValue = Double.parseDouble(minPrice);
                double maxPriceValue = Double.parseDouble(maxPrice);

                if (minPriceValue <= 0 || maxPriceValue <= 0 || minPriceValue > maxPriceValue || minPrice.length() > 6 || maxPrice.length() > 6) {
                    errorMessage.append("- 价格范围输入错误\n");
                    isValid = false;
                }
            }

            if (!minQuantity.isEmpty() && !maxQuantity.isEmpty()) {
                int minQuantityValue = Integer.parseInt(minQuantity);
                int maxQuantityValue = Integer.parseInt(maxQuantity);

                if (minQuantityValue <= 0 || maxQuantityValue <= 0 || minQuantityValue > maxQuantityValue || minQuantity.length() > 6 || maxQuantity.length() > 6) {
                    errorMessage.append("- 数量范围输入错误\n");
                    isValid = false;
                }
            }


            if (!isValid) {
                JOptionPane.showMessageDialog(null, errorMessage.toString(), "输入错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // 传入所有条件，并获取结果
            filteredProducts = dataControlmer.MerchantProductQueryByCategory(
                    merchantInterFrm.getM_id(),
                    selectedCategory,
                    productId,
                    productName,
                    minPrice,
                    maxPrice,
                    minQuantity,
                    maxQuantity,
                    selectedStatus,
                    selectedAuditStatus
            );

            tableModel.setRowCount(0); // 清空表格数据
            if (filteredProducts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "没有搜索到商品", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
            }
            for (Product product : filteredProducts) {
                Object[] rowData = {
                        product.getP_id(),
                        getScaledImageIcon(product, 200, 210),
                        product.getP_name(),
                        product.getP_class(),
                        product.getP_price(),
                        product.getP_quantity(),
                        product.getP_status(),
                        product.getP_audiStatus()
                };
                tableModel.addRow(rowData);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "输入错误，请输入有效数字", "错误", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private boolean isPositiveInteger(String value) {
        try {
            int intValue = Integer.parseInt(value);
            return intValue > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isPositiveDouble(String value) {
        try {
            double doubleValue = Double.parseDouble(value);
            return doubleValue > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void refreshProductTable() {
        try {
            List<Product> products = dataControlmer.MerchantProductQuery(merchantInterFrm.getM_id());
            tableModel.setRowCount(0); // 清空表格数据
            for (Product product : products) {
                Object[] rowData = {product.getP_id(), getScaledImageIcon(product, 200, 210), product.getP_name(), product.getP_class(), product.getP_price(), product.getP_quantity(), product.getP_status(), product.getP_audiStatus(), // 添加审核状态列的信息

                };
                tableModel.addRow(rowData);
            }
            productIdField.setText("");
            productNameField.setText("");
            minpriceField.setText("");
            maxPriceField.setText("");
            minquantityField.setText("");
            maxQuantityField.setText("");
            statusComboBox.setSelectedIndex(0);
            auditStatusComboBox.setSelectedIndex(0);
            categoryComboBox.setSelectedIndex(0);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Map<String, ImageIcon> imageCache = new HashMap<>();

    ImageIcon getScaledImageIcon(Product product, int width, int height) {
        String imagePath = product.getP_img();
        ImageIcon cachedIcon = imageCache.get(imagePath);

        if (cachedIcon != null) {
            return cachedIcon;
        }

        ImageIcon originalIcon = getImageIcon(product);
        Image originalImage = originalIcon.getImage();
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        imageCache.put(imagePath, scaledIcon);

        return scaledIcon;
    }

    private ImageIcon getImageIcon(Product product) {
        String projectPath = System.getProperty("user.dir");
        String relativeImagePath = product.getP_img();
        String absoluteImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + relativeImagePath;

        File imageFile = new File(absoluteImagePath);
        if (imageFile.exists()) {
            return new ImageIcon(absoluteImagePath);
        } else {
            String defaultImagePath = projectPath + File.separator + "src" + File.separator + "img" + File.separator + "R.jpg";
            return new ImageIcon(defaultImagePath);
        }
    }

    static class ImageCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof ImageIcon) {
                JLabel label = new JLabel((ImageIcon) value);
                label.setHorizontalAlignment(JLabel.CENTER);
                return label;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    // 优化后的 createImageLabel 方法
    public JLabel createImageLabel(Product product, int width, int height) {
        ImageIcon placeHolderIcon = new ImageIcon(getClass().getResource("/Img/loadphoto.jpg"));
        JLabel imageLabel = new JLabel(placeHolderIcon);

        SwingWorker<ImageIcon, Void> worker = new SwingWorker() {
            @Override
            protected ImageIcon doInBackground() {
                return getScaledImageIcon(product, width, height);
            }

            @Override
            protected void done() {
                try {
                    ImageIcon scaledIcon = (ImageIcon) get();
                    imageLabel.setIcon(scaledIcon);
                } catch (InterruptedException | ExecutionException ex) {
                    ex.printStackTrace();
                }
            }
        };

        worker.execute();

        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    showProductDetails(product);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        return imageLabel;
    }

    private void showProductDetails(Product product) throws SQLException {
        closeDetailsDialog(); // 先关闭可能已经打开的商品详细信息界面

        currentDetailsDialog = new ProductDetailsDialog(product, merchantInterFrm, this);
        currentDetailsDialog.setVisible(true);
    }

    // 在 MerchantProductFrm 类中添加一个方法来关闭商品详细信息界面
    public void closeDetailsDialog() {
        if (currentDetailsDialog != null) {
            currentDetailsDialog.dispose();
        }
    }

}
