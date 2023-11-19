package cn.bjut.jdbc;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class OrdersFrmButtonRender extends JPanel implements TableCellRenderer {

    private JButton detailsButton;
    private JButton modifyButton;
    private JButton deleteButton;

    public  OrdersFrmButtonRender() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // Set to horizontal layout
        Font font = new Font("微软雅黑", Font.BOLD, 16);

        detailsButton = new JButton("详情");
        detailsButton.setFont(font); // Set font
        detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        modifyButton = new JButton("发货");
        modifyButton.setFont(font); // Set font
        modifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        deleteButton = new JButton("确认退货");
        deleteButton.setFont(font); // Set font
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createHorizontalGlue()); // Add horizontal glue for centering
        add(detailsButton);
        add(modifyButton);
        add(deleteButton);
        add(Box.createHorizontalGlue()); // Add horizontal glue for centering
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        //以根据订单状态更改按钮的外观，例如设置颜色、字体等
        // 根据状态设置按钮颜色等
        String orderStatus = table.getValueAt(row, 7).toString();
        if ("待发货".equals(orderStatus)) {
            modifyButton.setVisible(true);
            deleteButton.setVisible(false);
        } else if ("待退货".equals(orderStatus)) {
            modifyButton.setVisible(false);
            deleteButton.setVisible(true);
        } else {
            modifyButton.setVisible(false);
            deleteButton.setVisible(false);
        }
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }
}
