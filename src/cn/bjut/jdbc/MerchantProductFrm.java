package cn.bjut.jdbc;

import javax.swing.*;
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
    private  DataControlProduct dataControlProduct=new DataControlProduct();
    private ProductDetailsDialog currentDetailsDialog;
    private JTable productTable;
    DefaultTableModel tableModel;

    public MerchantProductFrm(MerchantInterFrm mer) throws SQLException {
        this.merchantInterFrm = mer;
        initComponent();
    }

    public void initComponent() {
        setLayout(new BorderLayout());

        // 创建包含表格和按钮的新面板
        JPanel tablePanel = new JPanel();
        tablePanel.setLayout(new BorderLayout());

        // 创建表格并添加到新面板的中间位置
        createProductTable();
        JScrollPane scrollPane = new JScrollPane(productTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        // 创建一个新的面板来包装增加商品的按钮
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 左对齐布局，也可以选择其他布局方式

        // 创建增加商品的按钮并设置样式
        JButton addProductButton = new JButton("增加商品");
        addProductButton.setFont(new Font("微软雅黑", Font.BOLD, 20));
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

        // 将增加商品按钮添加到新的面板中
        buttonPanel.add(addProductButton);

        // 将包含增加商品按钮的新面板添加到tablePanel的北部位置
        tablePanel.add(buttonPanel, BorderLayout.NORTH);

        // 将包含表格和按钮的新面板添加到主面板中
        add(tablePanel, BorderLayout.CENTER);

        refreshProductTable(); // 刷新表格数据
    }

    private void createProductTable() {
        String[] columns = {"ID", "图片", "商品名称", "单价￥", "剩余数量", "商品状态","审核状态", "操作"};
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
                if (column == 7) {
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
                    label.setPreferredSize(new Dimension(column == 0 ? 100 : 150, 50));
                    label.setMaximumSize(new Dimension(column == 0 ? 100 : 150, 70));
                    label.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1)); // 给单元格添加内边距
                    label.setOpaque(true);

                    Object value = getValueAt(row, column);
                    if (value != null) {
                        if (value instanceof ImageIcon) {
                            label.setIcon((ImageIcon) value);
                            label.setText("");
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
                if ("上架".equals(status)) {
                    return "green"; // 上架状态显示绿色
                } else if ("下架".equals(status)) {
                    return "red"; // 下架状态显示红色
                }
                return ""; // 其他状态无颜色
            }


        };
        productTable.getColumnModel().getColumn(0).setPreferredWidth(5); // ID column
        productTable.getColumnModel().getColumn(4).setPreferredWidth(5); // Quantity column
        productTable.getColumnModel().getColumn(5).setPreferredWidth(4); // Status column
        productTable.setDefaultRenderer(Object.class, new ImageCellRenderer());
        productTable.setRowHeight(130); // 设置行高
        productTable.setFillsViewportHeight(true); // 填充表格的视口高度
        productTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // 启用自动列调整


        productTable.getColumnModel().getColumn(1).setCellRenderer(new ImageCellRenderer()); // 图片列应用图片渲染器

        JTableHeader header = productTable.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 22));
        productTable.getTableHeader().setReorderingAllowed(false); // 禁用列重新排序
        productTable.getTableHeader().setResizingAllowed(false); // 禁用列调整大小

        productTable.getColumnModel().getColumn(7).setCellEditor(new ProductFrmButtonEditor(this));

        productTable.getColumnModel().getColumn(7).setCellRenderer(new ProductFrmButtonRender());

        productTable.setRowSelectionAllowed(false);// 禁止表格的选择功能。不然在点击按钮时表格的整行都会被选中。也可以通过其它方式来实现。
    }
    public void handleDetails(int selectedRow) throws SQLException {
        Object productId = productTable.getValueAt(selectedRow, 0);
        if (productId != null) {
            int p_id = (int) productId;
            Product product = dataControlProduct.getProductFromPId(p_id);
            showProductDetails(product);
            // Add further logic here using the obtained product ID
        }
    }

    public void handleModify(int selectedRow) throws SQLException {
        Object productId = productTable.getValueAt(selectedRow, 0);
        if (productId != null) {
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
    public void refreshProductTable() {
        try {
            List<Product> products = dataControlmer.MerchantProductQuery(merchantInterFrm.getM_id());
            tableModel.setRowCount(0); // 清空表格数据
            for (Product product : products) {
                Object[] rowData = {
                        product.getP_id(),
                        getScaledImageIcon(product, 200, 200),
                        product.getP_name(),
                        product.getP_price(),
                        product.getP_quantity(),
                        product.getP_status(),
                        product.getP_audiStatus(), // 添加审核状态列的信息
                        // 如果之前有第7列的操作按钮信息，这里需要根据列数调整
                };
                tableModel.addRow(rowData);
            }
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

    private static class ImageCellRenderer extends DefaultTableCellRenderer {
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
        if (currentDetailsDialog != null) {
            currentDetailsDialog.dispose();
        }

        currentDetailsDialog = new ProductDetailsDialog(product, merchantInterFrm, this);
        currentDetailsDialog.setVisible(true);
    }


}
