package cn.bjut.jdbc;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ProductFrmButtonRender extends JPanel implements TableCellRenderer {

    private JButton detailsButton;
    private JButton modifyButton;
    private JButton deleteButton;

    public ProductFrmButtonRender() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS)); // Set to horizontal layout
        Font font =new Font("微软雅黑", Font.BOLD, 16);

        detailsButton = new JButton("详情");
        detailsButton.setFont(font); // Set font
        detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        modifyButton = new JButton("修改");
        modifyButton.setFont(font); // Set font
        modifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        deleteButton = new JButton("删除");
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
        if (isSelected) {
            setBackground(table.getSelectionBackground());
        } else {
            setBackground(table.getBackground());
        }
        return this;
    }
}