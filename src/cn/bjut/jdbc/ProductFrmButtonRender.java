package cn.bjut.jdbc;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class ProductFrmButtonRender extends JPanel implements TableCellRenderer {

    private JButton detailsButton;
    private JButton modifyButton;
    private JButton deleteButton;

    public ProductFrmButtonRender() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        Font font = new Font("微软雅黑", Font.BOLD, 16);

        // 加载图标并设置大小
        ImageIcon detailsIcon = new ImageIcon(getClass().getClassLoader().getResource("Img/information.jpg"));
        ImageIcon modifyIcon = new ImageIcon(getClass().getClassLoader().getResource("Img/updateicon.png"));
        ImageIcon deleteIcon = new ImageIcon(getClass().getClassLoader().getResource("Img/delete.jpg"));

        int width = 45;
        int height =45;
        // 调整图标大小
        Image detailsImg = detailsIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Image modifyImg = modifyIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        Image deleteImg = deleteIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);

        // 创建按钮并设置图标
        detailsButton = new JButton(new ImageIcon(detailsImg));
        modifyButton = new JButton( new ImageIcon(modifyImg));
        deleteButton = new JButton(new ImageIcon(deleteImg));


        detailsButton.setFont(font);
        detailsButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        modifyButton.setFont(font);
        modifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        deleteButton.setFont(font);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createHorizontalGlue());
        add(detailsButton);
        add(modifyButton);
        add(deleteButton);
        add(Box.createHorizontalGlue());
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
