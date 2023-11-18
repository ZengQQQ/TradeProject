package cn.bjut.jdbc;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MerchantOrdersFrm extends JPanel {
    private DataControlOrder dataControlOrder = new DataControlOrder();
    public final int m_id;
    private static final int DEFAULT_IMAGE_WIDTH = 220;
    private static final int DEFAULT_IMAGE_HEIGHT = 200;
    private static final int ORDER_PANEL_HEIGHT = 200;
    private List<Order> orders = null;

    public MerchantOrdersFrm(int m_id) throws SQLException {
        this.m_id = m_id;
        initComponents();
    }

    public MerchantOrdersFrm(int mId, List<Order> orders) throws SQLException {
        this.m_id = mId;
        this.orders = orders;
        initComponents();
    }

    private void initComponents() throws SQLException {
        setLayout(new BorderLayout());

        String[] columnNames = {"ID", "商品图片", "商品基本信息", "用户信息", "购买数量", "订单总价格", "购买时间", "订单状态", "操作"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0) {
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
        if (orders != null) {
            for (Order order : orders) {
                JLabel img = createImageLabel(order.getProduct(), DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
                String orderId = String.valueOf(order.getO_id());
                String productInfo = getProductInfo(order.getProduct());
                String userInfo = getUserInfo(order.getUser());
                String quantityInfo = String.valueOf(order.getQuantity());
                String totalPriceInfo = order.getTotalprice() + "￥";
                String buyTimeInfo = order.getBuytime();
                String orderStatus = order.getStatus();

                Object[] rowData = {orderId, img, productInfo, userInfo, quantityInfo, totalPriceInfo, buyTimeInfo, orderStatus, "操作列内容"};
                tableModel.addRow(rowData);
            }
        } else {
            List<Order> orderInfoList = dataControlOrder.getOrderInfoByM_id(m_id);
            for (Order order : orderInfoList) {
                JLabel img = createImageLabel(order.getProduct(), DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
                String orderId = String.valueOf(order.getO_id());
                String productInfo = getProductInfo(order.getProduct());
                String userInfo = getUserInfo(order.getUser());
                String quantityInfo = String.valueOf(order.getQuantity());
                String totalPriceInfo = order.getTotalprice() + "￥";
                String buyTimeInfo = order.getBuytime();
                String orderStatus = order.getStatus();

                Object[] rowData = {orderId, img, productInfo, userInfo, quantityInfo, totalPriceInfo, buyTimeInfo, orderStatus, "操作列内容"};
                tableModel.addRow(rowData);
            }
        }
        // 初始化表格
        JTable table = new JTable(tableModel);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("微软雅黑", Font.BOLD, 20));
        table.setRowHeight(ORDER_PANEL_HEIGHT);
        // 添加自定义按钮渲染器和编辑器到操作列（第九列）
        table.getColumnModel().getColumn(8).setCellEditor(new OrdersFrmButtonEditor(new JCheckBox(), this));

        table.getColumnModel().getColumn(8).setCellRenderer(new OrdersFrmButtonRender());
        // 禁用表格编辑
        table.setDefaultEditor(Object.class, null);
        table.getTableHeader().setReorderingAllowed(false); // 禁用列重新排序
        table.setRowSelectionAllowed(false);// 禁止表格的选择功能。
        table.setRowHeight(160); // 设置行高
        table.setBorder(BorderFactory.createLineBorder(Color.black, 2));
        table.setGridColor(Color.GRAY);

        // 设置自定义单元格渲染器以显示大字体
        table.getColumnModel().getColumn(0).setCellRenderer(new CustomCellRenderer(20));
        table.getColumnModel().getColumn(2).setCellRenderer(new CustomCellRenderer(20));
        table.getColumnModel().getColumn(3).setCellRenderer(new CustomCellRenderer(20));
        table.getColumnModel().getColumn(4).setCellRenderer(new CustomCellRenderer(20));
        table.getColumnModel().getColumn(5).setCellRenderer(new CustomCellRenderer(20));
        table.getColumnModel().getColumn(6).setCellRenderer(new CustomCellRenderer(20));
        table.getColumnModel().getColumn(7).setCellRenderer(new CustomCellRenderer(20));
        table.getColumnModel().getColumn(1).setCellRenderer(new ImageCellRenderer());
        // 获取表格的列模型
        TableColumnModel columnModel = table.getColumnModel();
        // 设置每一列的宽度
        columnModel.getColumn(0).setPreferredWidth(60); // 第一列宽度为 100 像素
        columnModel.getColumn(1).setPreferredWidth(200);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(3).setPreferredWidth(150);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(5).setPreferredWidth(100);
        columnModel.getColumn(6).setPreferredWidth(120);
        columnModel.getColumn(7).setPreferredWidth(100);
        columnModel.getColumn(8).setPreferredWidth(250);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        // 添加外边距和边框
        scrollPane.setBorder(new EmptyBorder(100, 30, 30, 30));

        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {

        List<Order> updatedOrderInfoList;
        try {
            updatedOrderInfoList = dataControlOrder.getOrderInfoByM_id(m_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        DefaultTableModel tableModel = (DefaultTableModel) ((JTable) ((JScrollPane) getComponent(0)).getViewport().getView()).getModel();
        tableModel.setRowCount(0);

        for (Order order : updatedOrderInfoList) {
            JLabel img = createImageLabel(order.getProduct(), DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
            String orderId = String.valueOf(order.getO_id());
            String productInfo = getProductInfo(order.getProduct());
            String userInfo = getUserInfo(order.getUser());
            String quantityInfo = String.valueOf(order.getQuantity());
            String totalPriceInfo = order.getTotalprice() + "￥";
            String buyTimeInfo = order.getBuytime();
            String orderStatus = order.getStatus();

            Object[] rowData = {orderId, img, productInfo, userInfo, quantityInfo, totalPriceInfo, buyTimeInfo, orderStatus, "操作列内容"};
            tableModel.addRow(rowData);
        }

        tableModel.fireTableDataChanged();

    }

    public void handleDetails(int selectedRow) throws SQLException {
        DefaultTableModel tableModel = (DefaultTableModel) ((JTable) ((JScrollPane) getComponent(0)).getViewport().getView()).getModel();

        Object orderIdObj = tableModel.getValueAt(selectedRow, 0);
        if (orderIdObj != null) {
            String orderIdStr = orderIdObj.toString(); // 将 Object 转换为 String

            try {
                int o_Id = Integer.parseInt(orderIdStr); // 将 String 类型的订单 ID 转换为 int
                Order order = dataControlOrder.getOrderInfoByO_id(o_Id);
                OrderDetailsDialog detailsDialog = new OrderDetailsDialog((Frame) SwingUtilities.getWindowAncestor(this), order);
                detailsDialog.setVisible(true);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "订单 ID 格式不正确", "订单详细信息", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "查询订单信息时出错", "订单详细信息", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "订单不存在", "订单详细信息", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleShipping(int selectedRow) {
        DefaultTableModel tableModel = (DefaultTableModel) ((JTable) ((JScrollPane) getComponent(0)).getViewport().getView()).getModel();

        Object orderIdObj = tableModel.getValueAt(selectedRow, 0);
        if (orderIdObj != null) {
            String orderIdStr = orderIdObj.toString(); // 将 Object 转换为 String

            try {
                int o_Id = Integer.parseInt(orderIdStr); // 将 String 类型的订单 ID 转换为 int
                boolean isShipped = dataControlOrder.shippOrder(o_Id); // 调用发货函数，返回是否发货成功

                if (isShipped) {
                    JOptionPane.showMessageDialog(this, "已发货", "发货操作", JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "发货失败", "发货操作", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "订单 ID 格式不正确", "发货操作", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "订单不存在", "发货操作", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void handleReturn(int selectedRow) {
        DefaultTableModel tableModel = (DefaultTableModel) ((JTable) ((JScrollPane) getComponent(0)).getViewport().getView()).getModel();

        Object orderIdObj = tableModel.getValueAt(selectedRow, 0);
        if (orderIdObj != null) {
            String orderIdStr = orderIdObj.toString(); // 将 Object 转换为 String

            try {
                int o_Id = Integer.parseInt(orderIdStr); // 将 String 类型的订单 ID 转换为 int
                boolean isReturned = dataControlOrder.returnOrder(o_Id); // 调用确认退货函数，返回是否确认退货成功

                if (isReturned) {
                    JOptionPane.showMessageDialog(this, "已确认退货", "退货操作", JOptionPane.INFORMATION_MESSAGE);
                    refreshData();
                } else {
                    JOptionPane.showMessageDialog(this, "确认退货失败", "退货操作", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "订单 ID 格式不正确", "退货操作", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "订单不存在", "退货操作", JOptionPane.ERROR_MESSAGE);
        }
    }


    // 自定义单元格渲染器，用于显示大字体和居中文本
    private static class CustomCellRenderer extends DefaultTableCellRenderer {
        private final Font largerFont;

        public CustomCellRenderer(int fontSize) {
            largerFont = new Font("微软雅黑", Font.PLAIN, fontSize); // 设置字体为微软雅黑，大小为指定的fontSize
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JTextArea textArea = new JTextArea();
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setFont(largerFont); // 使用自定义的字体
            textArea.setText((String) value);
            textArea.setOpaque(true);


            // 设置居中对齐的列索引
            int[] centeredColumns = {0, 4, 5, 6, 7};
            for (int centeredColumn : centeredColumns) {
                if (column == centeredColumn) {
                    setHorizontalAlignment(SwingConstants.CENTER);
                    break;
                } else {
                    setHorizontalAlignment(SwingConstants.CENTER);
                }
            }

            return textArea;
        }
    }

    // 自定义单元格渲染器用于在表格中显示图片
    private static class ImageCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (value instanceof JLabel) {
                return (JLabel) value;
            }
            return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        }
    }

    // 生成产品信息的字符串
    private String getProductInfo(Product product) {
        return "名称: " + product.getP_name() + "\n" + "分类: " + product.getP_class() + "\n" + "价格: " + product.getP_price();
    }

    // 生成用户信息的字符串
    private String getUserInfo(User user) {
        return "用户名: " + user.getU_name() + "\n" + "性别: " + user.getU_sex() + "\n" + "电话: " + user.getU_tele();
    }

    // 创建包含产品图片的JLabel
    public JLabel createImageLabel(Product product, int width, int height) {
        // 获取当前项目的绝对路径
        String projectPath = System.getProperty("user.dir");

        // 构建图片路径
        ImageIcon originalIcon = getImageIcon(product, projectPath);
        // 获取图片对象
        Image originalImage = originalIcon.getImage();

        // 缩放图片（如果需要），可以改变图片的大小
        Image scaledImage = originalImage.getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // 创建一个新的 ImageIcon
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // 创建一个 JLabel，并将缩放后的 ImageIcon 设置为其图标
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

    public static void main(String[] args) throws SQLException {
        DataControl data = new DataControl();
        MerchantOrdersFrm mero = new MerchantOrdersFrm(1);

        JFrame frame = new JFrame("商品订单");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mero);
        frame.setSize(1200, 1000);
        frame.setVisible(true);
    }
}

