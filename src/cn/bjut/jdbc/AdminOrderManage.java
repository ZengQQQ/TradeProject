package cn.bjut.jdbc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class AdminOrderManage extends JPanel {
    private JTable orderTable;
    private DefaultTableModel tableModel;

    public AdminOrderManage() throws SQLException {
        initUI();
        refreshOrderTable();
        setDefaultFont();
    }

    private void setDefaultFont() {
        // 设置所有组件的默认字体
        Font font = new Font("微软雅黑", Font.PLAIN, 18);
        for (Component component : getComponents()) {
            setComponentFont(component, font);
        }
    }

    private void setComponentFont(Component component, Font font) {
        // 递归设置所有组件及其子组件的字体
        if (component instanceof JTextArea || component instanceof JLabel || component instanceof JButton) {
            component.setFont(font);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setComponentFont(child, font);
            }
        }
    }

    private void initUI() throws SQLException {
        setLayout(new BorderLayout());

        // 创建表格
        createOrderTable();
        JScrollPane scrollPane = new JScrollPane(orderTable);
        add(scrollPane, BorderLayout.CENTER);

        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        // 添加修改订单状态的按钮
        JButton updateStatusButton = new JButton("修改订单状态");
        setButtonAction(updateStatusButton, e -> {
            try {
                updateOrderStatus();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        buttonPanel.add(updateStatusButton);

        // 添加删除订单的按钮
//        JButton deleteOrderButton = new JButton("删除订单");
//        setButtonAction(deleteOrderButton, e -> {
//            try {
//                int result = JOptionPane.showConfirmDialog(this, "确定要删除该订单吗？", "删除订单", JOptionPane.YES_NO_OPTION);
//                if(result == JOptionPane.YES_OPTION){
//                    deleteSelectedOrder();
//                }
//            } catch (SQLException ex) {
//                throw new RuntimeException(ex);
//            }
//        });
//        buttonPanel.add(deleteOrderButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void createOrderTable() {
        String[] columns = {"Order ID", "User_Name", "Product_Name","Buy Time", "send Time","receive Time","Status"};
        tableModel = new DefaultTableModel(columns, 0);
        orderTable = new JTable(tableModel);
        // 根据需要自定义单元格渲染器
        orderTable.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        orderTable.setRowHeight(55);
    }

    public void refreshOrderTable() {
        try {
            List<Order> orders = getOrders();
            tableModel.setRowCount(0); // 清空现有数据

            for (Order order : orders) {
                Object[] rowData = {
                        order.getO_id(),
                        order.getU_name(),
                        order.getP_name(),
                        order.getBuytime(),
                        order.getSend_time(),
                        order.getReceive_time(),
                        order.getO_status()
                };
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private List<Order> getOrders() throws SQLException {
        return new DataControlOrder().getOrders();
    }

    private void updateOrderStatus() throws SQLException {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            // 获取选定行的订单ID
            int orderId = (int) orderTable.getValueAt(selectedRow, 0);

            // 显示对话框以选择新的订单状态
            String[] statusOptions = {"待发货", "待收货", "已完成", "已退货", "申请退货", "待退货"};
            String newStatus = (String) JOptionPane.showInputDialog(
                    this,
                    "请选择新的订单状态:",
                    "更新订单状态",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    statusOptions,
                    statusOptions[0]);

            if (newStatus != null) {
                // 在这里添加您的更新订单状态的逻辑
                new DataControlOrder().updateOrderStatus(orderId, newStatus);

                refreshOrderTable(); // 刷新表格
            }
        } else {
            JOptionPane.showMessageDialog(this, "请选择要更新状态的订单。", "警告", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void deleteSelectedOrder() throws SQLException {
        int selectedRow = orderTable.getSelectedRow();
        if (selectedRow != -1) {
            // 获取选定行的订单ID
            int orderId = (int) orderTable.getValueAt(selectedRow, 0);

            if (new DataControlOrder().hasReturnRecord(orderId)) {
                // 有关联的退货记录，显示错误消息
                JOptionPane.showMessageDialog(this, "不能删除该订单，因为存在相关的退货记录。", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                // 在这里添加您的删除订单的逻辑
                new DataControlOrder().deleteOrder(orderId);

                refreshOrderTable(); // 刷新表格
            }

            refreshOrderTable(); // 刷新表格
        } else {
            JOptionPane.showMessageDialog(this, "请选择要删除的订单。", "警告", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setButtonAction(JButton button, ActionListener listener) {
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listener.actionPerformed(e);
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Order Management Panel");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            try {
                frame.getContentPane().add(new AdminOrderManage());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            frame.setSize(800, 600);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
