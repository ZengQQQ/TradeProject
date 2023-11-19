package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class OrdersFrmButtonEditor extends DefaultCellEditor {

    private JPanel panel;
    private JButton detailsButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private int selectedRow;
    private MerchantOrdersFrm merchantOrdersFrm;

    public OrdersFrmButtonEditor(JCheckBox checkBox,MerchantOrdersFrm merchantOrdersFrm) {
        super(checkBox);
        this.merchantOrdersFrm = merchantOrdersFrm;
        Font font = new Font("微软雅黑", Font.BOLD, 16);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS)); // Set to horizontal layout

        detailsButton = new JButton("详情");
        detailsButton.setFont(font);
        detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsButton.setActionCommand("DETAILS");
        detailsButton.addActionListener(new ButtonClickListener());

        modifyButton = new JButton("发货");
        modifyButton.setFont(font);
        modifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        modifyButton.setActionCommand("SHIP");
        modifyButton.addActionListener(new ButtonClickListener());

        deleteButton = new JButton("确认退货");
        deleteButton.setFont(font);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setActionCommand("RETURN");
        deleteButton.addActionListener(new ButtonClickListener());

        modifyButton.setVisible(false);
        deleteButton.setVisible(false);
        panel.add(Box.createHorizontalGlue()); // Add horizontal glue for centering
        panel.add(detailsButton);
        panel.add(modifyButton);
        panel.add(deleteButton);
        panel.add(Box.createHorizontalGlue()); // Add horizontal glue for centering

        // Set button as the editor component
        editorComponent = panel;
        this.setClickCountToStart(1);
    }


    class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            fireEditingStopped();

            if ("DETAILS".equals(command)) {
                // 处理“详情”按钮点击
                try {
                    merchantOrdersFrm.handleDetails(selectedRow);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else if ("SHIP".equals(command)) {
                // 处理“发货”按钮点击
                merchantOrdersFrm.handleShipping(selectedRow);
            } else if ("RETURN".equals(command)) {
                // 处理“退货”按钮点击
                merchantOrdersFrm.handleReturn(selectedRow);
            }
        }
    }


    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        String orderStatus = table.getValueAt(row, 7).toString(); // Assuming the order status is at column index 7

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

        selectedRow = row;
        return editorComponent;
    }

}
