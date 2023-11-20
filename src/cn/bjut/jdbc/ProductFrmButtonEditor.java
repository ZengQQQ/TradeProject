package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class ProductFrmButtonEditor extends DefaultCellEditor {

    private JPanel panel;
    private JButton detailsButton;
    private JButton modifyButton;
    private JButton deleteButton;
    private int selectedRow;
    private MerchantProductFrm merchantProductFrm;

    public ProductFrmButtonEditor(JCheckBox checkBox, MerchantProductFrm merchantProductFrm) {
        super(checkBox);
        this.merchantProductFrm = merchantProductFrm;
        Font font = new Font("微软雅黑", Font.BOLD, 16);
        panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));

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
        detailsButton.setActionCommand("DETAILS");
        detailsButton.addActionListener(new ButtonClickListener());


        modifyButton.setFont(font);
        modifyButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        modifyButton.setActionCommand("MODIFY");
        modifyButton.addActionListener(new ButtonClickListener());


        deleteButton.setFont(font);
        deleteButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        deleteButton.setActionCommand("DELETE");
        deleteButton.addActionListener(new ButtonClickListener());

        panel.add(Box.createHorizontalGlue());
        panel.add(detailsButton);
        panel.add(modifyButton);
        panel.add(deleteButton);
        panel.add(Box.createHorizontalGlue());


        editorComponent = panel;
        setClickCountToStart(1);
    }

    class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            fireEditingStopped();

            if ("DETAILS".equals(command)) {

                try {
                    merchantProductFrm.handleDetails(selectedRow);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else if ("MODIFY".equals(command)) {

                try {
                    merchantProductFrm.handleModify(selectedRow);
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            } else if ("DELETE".equals(command)) {

                merchantProductFrm.handleDelete(selectedRow);
            }
        }
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        selectedRow = row;
        return editorComponent;
    }
}
