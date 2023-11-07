package cn.bjut.jdbc;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MerchantOrdersFrm extends JPanel {
    private final DataControl data;
    public final int m_id;

    private static final int DEFAULT_IMAGE_WIDTH = 220;
    private static final int DEFAULT_IMAGE_HEIGHT = 200;
    private static final int ORDER_PANEL_HEIGHT = 200;
    private List<Order> orders = null;

    public MerchantOrdersFrm(DataControl data, int m_id) throws SQLException {
        this.data = data;
        this.m_id = m_id;
        initComponents();
    }

    public MerchantOrdersFrm(DataControl data, int mId, List<Order> orders) throws SQLException {
        this.data = data;
        this.m_id = mId;
        this.orders = orders;
        initComponents();
    }

    private void initComponents() throws SQLException {
        setLayout(new BorderLayout());


        String[] columnNames = {"商品图片", "商品详细信息", "用户信息", "购买商品数量", "订单总价格", "购买时间"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        if(orders!=null){
            for (Order order : orders) {
                JLabel img = createImageLabel(order.getProduct(), DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
                String productInfo = getProductInfo(order.getProduct());
                String userInfo = getUserInfo(order.getUser());
                String quantityInfo = String.valueOf(order.getQuantity());
                String totalPriceInfo = order.getTotalprice() + "￥";
                String buyTimeInfo = order.getBuytime();

                Object[] rowData = {img, productInfo, userInfo, quantityInfo, totalPriceInfo, buyTimeInfo};
                tableModel.addRow(rowData);
            }
        }else {
            List<Order> orderInfoList = data.getOrderInfoByM_id(m_id);
            for (Order order : orderInfoList) {
                JLabel img = createImageLabel(order.getProduct(), DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
                String productInfo = getProductInfo(order.getProduct());
                String userInfo = getUserInfo(order.getUser());
                String quantityInfo = String.valueOf(order.getQuantity());
                String totalPriceInfo = order.getTotalprice() + "￥";
                String buyTimeInfo = order.getBuytime();

                Object[] rowData = {img, productInfo, userInfo, quantityInfo, totalPriceInfo, buyTimeInfo};
                tableModel.addRow(rowData);
            }
        }
        JTable table = new JTable(tableModel);
        table.setRowHeight(ORDER_PANEL_HEIGHT);

        // 禁用表格编辑
        table.setDefaultEditor(Object.class, null);

        // 设置自定义单元格渲染器以显示大字体
        table.getColumnModel().getColumn(1).setCellRenderer(new CustomCellRenderer(16));
        table.getColumnModel().getColumn(2).setCellRenderer(new CustomCellRenderer(16));
        table.getColumnModel().getColumn(3).setCellRenderer(new CustomCellRenderer(16));
        table.getColumnModel().getColumn(4).setCellRenderer(new CustomCellRenderer(16));
        table.getColumnModel().getColumn(5).setCellRenderer(new CustomCellRenderer(16));
        table.getColumnModel().getColumn(0).setCellRenderer(new ImageCellRenderer());

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(30);
        scrollPane.setPreferredSize(new Dimension(800, 400));

        add(scrollPane, BorderLayout.CENTER);
    }

    public void refreshData() {
        // Get the updated order information
        List<Order> updatedOrderInfoList;
        try {
            updatedOrderInfoList = data.getOrderInfoByM_id(m_id);
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        // Clear existing data from the table
        DefaultTableModel tableModel = (DefaultTableModel) ((JTable) ((JScrollPane) getComponent(0)).getViewport().getView()).getModel();
        tableModel.setRowCount(0);

        // Populate the table with the updated data
        for (Order order : updatedOrderInfoList) {
            JLabel img = createImageLabel(order.getProduct(), DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);
            String productInfo = getProductInfo(order.getProduct());
            String userInfo = getUserInfo(order.getUser());
            String quantityInfo = String.valueOf(order.getQuantity());
            String totalPriceInfo = order.getTotalprice() + "￥";
            String buyTimeInfo = order.getBuytime();

            Object[] rowData = {img, productInfo, userInfo, quantityInfo, totalPriceInfo, buyTimeInfo};
            tableModel.addRow(rowData);
        }

        // Repaint the table to reflect the changes
        tableModel.fireTableDataChanged();
    }


    // 自定义单元格渲染器，用于显示大字体
    private static class CustomCellRenderer extends DefaultTableCellRenderer {
        private final Font largerFont;

        public CustomCellRenderer(int fontSize) {
            largerFont = new Font(Font.SERIF, Font.PLAIN, fontSize);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JTextArea textArea = new JTextArea();
            textArea.setWrapStyleWord(true);
            textArea.setLineWrap(true);
            textArea.setFont(largerFont);
            textArea.setText((String) value);
            textArea.setOpaque(true);
            textArea.setBackground(isSelected ? table.getSelectionBackground() : table.getBackground());
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
        return "名称: " + product.getP_name() + "\n" +
                "描述: " + product.getP_desc() + "\n" +
                "分类: " + product.getP_class() + "\n" +
                "价格: " + product.getP_price() + "\n" +
                "数量: " + product.getP_quantity();
    }

    // 生成用户信息的字符串
    private String getUserInfo(User user) {
        return "用户名: " + user.getU_name() + "\n" +
                "性别: " + user.getU_sex() + "\n" +
                "电话: " + user.getU_tele();
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
        MerchantOrdersFrm mero = new MerchantOrdersFrm(data, 1);

        JFrame frame = new JFrame("商品订单");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mero);
        frame.setSize(1200, 1000);
        frame.setVisible(true);
    }
}

