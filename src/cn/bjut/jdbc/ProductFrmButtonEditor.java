package cn.bjut.jdbc;

import cn.bjut.jdbc.MerchantProductFrm;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ProductFrmButtonEditor extends AbstractCellEditor implements TableCellEditor {

    private JPanel panel;
    private JButton detailsButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private int selectedRow;
    private MerchantProductFrm merchantProductFrm;

    public ProductFrmButtonEditor(MerchantProductFrm merchantProductFrm) {
        this.merchantProductFrm = merchantProductFrm;
        Font font =new Font("微软雅黑", Font.BOLD, 16);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS)); // Set to horizontal layout

        detailsButton = new JButton("详情");
        detailsButton.setFont(font); // Set font
        detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        detailsButton.setActionCommand("DETAILS");
        detailsButton.addActionListener(new ButtonClickListener());

        modifyButton = new JButton("修改");
        modifyButton.setFont(font); // Set font
        modifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        modifyButton.setActionCommand("MODIFY");
        modifyButton.addActionListener(new ButtonClickListener());

        deleteButton = new JButton("删除");
        deleteButton.setFont(font); // Set font
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setActionCommand("DELETE");
        deleteButton.addActionListener(new ButtonClickListener());

        panel.add(Box.createHorizontalGlue()); // Add horizontal glue for centering
        panel.add(detailsButton);
        panel.add(modifyButton);
        panel.add(deleteButton);
        panel.add(Box.createHorizontalGlue()); // Add horizontal glue for centering
    }

    class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            fireEditingStopped(); // Make the editor stop editing immediately and accept the current value.

            if ("DETAILS".equals(command)) {
                // Code for handling details button click
                try {
                    merchantProductFrm.handleDetails(selectedRow);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else if ("MODIFY".equals(command)) {
                //Code for handling modify button click
                try {
                    merchantProductFrm.handleModify(selectedRow);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else if ("DELETE".equals(command)) {
                // Code for handling delete button click
                merchantProductFrm.handleDelete(selectedRow);
            }
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        selectedRow = row;
        return panel;
    }

    @Override
    public Object getCellEditorValue() {
        return "";
    }
}
