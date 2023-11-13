package cn.bjut.jdbc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AdminMerchantFrame extends JPanel {
    private AdminFrame adminFrame;
    private DataControl data;

    public AdminMerchantFrame(AdminFrame adminFrame, DataControl data) throws SQLException {
        this.adminFrame = adminFrame;
        this.data = data;
        initComponents();
    }

    public void initComponents() throws SQLException {
        this.setLayout(new BorderLayout());
        // 商家信息展示界面
        JTable merchantTable = new JTable(getMerchantMessageTable(data.selectMerchantTable()));
        JScrollPane merchantScrollPane = new JScrollPane(merchantTable);
        this.add(merchantScrollPane, BorderLayout.CENTER);

        //添加查找功能
        JPanel merchantSearchPanel = new JPanel(new FlowLayout());
        String[] merchantSelectColumnNames = {"ID", "账户", "姓名"};
        JComboBox<String> merchantColumnSelector = new JComboBox<>(merchantSelectColumnNames);
        JTextField merchantSearchField = new JTextField(20);
        JButton merchantSearchButton = new JButton("查找商家");
        JButton merchantSearchAll = new JButton("查看全部");

        // 添加事件 "查看全部"
        merchantSearchAll.addActionListener(e -> {
            try {
                merchantTable.setModel(getMerchantMessageTable(data.selectMerchantTable()));
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        merchantSearchPanel.add(merchantColumnSelector);
        merchantSearchPanel.add(merchantSearchField);
        merchantSearchPanel.add(merchantSearchButton);
        merchantSearchPanel.add(merchantSearchAll);
        this.add(merchantSearchPanel, BorderLayout.NORTH);

        //创建修改按钮
        JButton editMerButton = new JButton("修改");
        JButton editMerButton2 = new JButton("重置");
        JButton editMerButton3 = new JButton("删除");
        JButton editMerButton4 = new JButton("添加");

        //商家修改按钮组
        JPanel merchantButtonPanel = new JPanel(new FlowLayout());
        merchantButtonPanel.add(editMerButton);
        merchantButtonPanel.add(editMerButton2);
        merchantButtonPanel.add(editMerButton3);
        merchantButtonPanel.add(editMerButton4);
        this.add(merchantButtonPanel, BorderLayout.SOUTH);

        //添加修改按钮的监听事件
        updateMerButtonAction(editMerButton, data, merchantTable);
        setNewMerButtonAction(editMerButton2, data, merchantTable);
        deleteMerButtonAction(editMerButton3, data, merchantTable);
        insertMerButtonAction(editMerButton4, data, merchantTable);
        searchMerButtonAction(merchantSearchButton, data, merchantTable, merchantSearchField, merchantColumnSelector);

    }


    // 从数据库得到商家信息
    public DefaultTableModel getMerchantMessageTable(List<Merchant> merchantList) {
        String[] columnNames = {"ID", "账户", "密码", "姓名", "性别", "电话"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Merchant merchant : merchantList) {
            Object[] rowData = {merchant.getID(), merchant.getAcc(), merchant.getPsw(), merchant.getM_name(), merchant.getM_sex(), merchant.getM_tele()};
            model.addRow(rowData);
        }
        return model;
    }

    //商家四个时间监听事件
    public void updateMerButtonAction(JButton editMerButton, DataControl data, JTable merchantTable) throws SQLException {
        // TODO add your code here
        editMerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = merchantTable.getSelectedRow();
                if (selectedRow != -1) {
                    String[] options = {"账户", "密码", "姓名", "性别", "电话"};
                    JComboBox<String> columnSelector = new JComboBox<>(options);
                    JTextField valueField = new JTextField();

                    JPanel panel = new JPanel(new GridLayout(2, 1));
                    panel.add(columnSelector);
                    panel.add(valueField);

                    int result = JOptionPane.showConfirmDialog(adminFrame, panel, "选择列并输入新值", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        String selectedColumn = (String) columnSelector.getSelectedItem();
                        String newValue = valueField.getText();
                        String database_name = null;
                        if (selectedColumn != null && !selectedColumn.isEmpty() && newValue != null && !newValue.isEmpty()) {
                            int columnIndex = Arrays.asList(options).indexOf(selectedColumn) + 1;
                            merchantTable.setValueAt(newValue, selectedRow, columnIndex);

                            switch (selectedColumn) {
                                case "账户":
                                    database_name = "m_acc";
                                    break;
                                case "密码":
                                    database_name = "m_psw";
                                    break;
                                case "姓名":
                                    database_name = "m_name";
                                    break;
                                case "性别":
                                    database_name = "m_sex";
                                    break;
                                case "电话":
                                    database_name = "m_tele";
                                    break;
                            }

                            try {
                                data.updateMerTable(Integer.parseInt((String) merchantTable.getValueAt(selectedRow, 0)), database_name, newValue);
                                //顺便更新usertable的数据
                                merchantTable.setValueAt(newValue, selectedRow, columnIndex);

                                JOptionPane.showMessageDialog(adminFrame, "修改成功");
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                                JOptionPane.showMessageDialog(adminFrame, "修改失败");
                            }
                        }
                    }
                }
            }
        });
    }

    public void setNewMerButtonAction(JButton editMerButton, DataControl data, JTable merchantTable) throws SQLException {
        editMerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int selectedRow = merchantTable.getSelectedRow();
                if (selectedRow != -1) {
                    JPanel panel = new JPanel(new GridLayout(6, 2));

                    JTextField accField = new JTextField();
                    JTextField pswField = new JTextField();
                    JTextField nameField = new JTextField();
                    JTextField sexField = new JTextField();
                    JTextField teleField = new JTextField();

                    panel.add(new JLabel("账户: "));
                    panel.add(accField);
                    panel.add(new JLabel("密码: "));
                    panel.add(pswField);
                    panel.add(new JLabel("姓名: "));
                    panel.add(nameField);
                    panel.add(new JLabel("性别: "));
                    panel.add(sexField);
                    panel.add(new JLabel("电话: "));
                    panel.add(teleField);

                    int result = JOptionPane.showConfirmDialog(adminFrame, panel, "输入新值", JOptionPane.OK_CANCEL_OPTION);
                    if (result == JOptionPane.OK_OPTION) {
                        String newAcc = accField.getText();
                        String newPsw = pswField.getText();
                        String newName = nameField.getText();
                        String newSex = sexField.getText();
                        String newTele = teleField.getText();

                        // 更新表格
                        merchantTable.setValueAt(newAcc, selectedRow, 1);
                        merchantTable.setValueAt(newPsw, selectedRow, 2);
                        merchantTable.setValueAt(newName, selectedRow, 3);
                        merchantTable.setValueAt(newSex, selectedRow, 4);
                        merchantTable.setValueAt(newTele, selectedRow, 5);

                        // 更新数据库
                        int m_id = Integer.parseInt((String) merchantTable.getValueAt(selectedRow, 0)); // 获取 m_id
                        try {
                            String updateResult = data.updateMerTable(m_id, newAcc, newPsw, newName, newSex, newTele);
                            JOptionPane.showMessageDialog(adminFrame, updateResult, "结果", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(adminFrame, "更新失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

            }

        });
    }

    public void deleteMerButtonAction(JButton deleteMerButton, DataControl data, JTable merchantTable) throws SQLException {
        deleteMerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = merchantTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirmResult = JOptionPane.showConfirmDialog(adminFrame, "确认删除该用户吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                    if (confirmResult == JOptionPane.YES_OPTION) {
                        int m_id = Integer.parseInt((String) merchantTable.getValueAt(selectedRow, 0)); // 获取 u_id
                        try {
                            String deleteResult = data.deleteMerTable(m_id);
                            if (deleteResult.equals("删除成功") ) {
                                data.deleteProductBym_id(m_id);
                                DefaultTableModel model = (DefaultTableModel) merchantTable.getModel();
                                model.removeRow(selectedRow);
                                JOptionPane.showMessageDialog(adminFrame, "删除成功");
                            } else {
                                JOptionPane.showMessageDialog(adminFrame, "删除失败");
                            }
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(adminFrame, "删除失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }
            }
        });
    }

    public void insertMerButtonAction(JButton inserMrtButton, DataControl data, JTable merchantTable) throws SQLException {
        inserMrtButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JPanel panel = new JPanel(new GridLayout(6, 2));

                JTextField accField = new JTextField();
                JTextField pswField = new JTextField();
                JTextField nameField = new JTextField();
                JTextField sexField = new JTextField();
                JTextField teleField = new JTextField();

                panel.add(new JLabel("账户: "));
                panel.add(accField);
                panel.add(new JLabel("密码: "));
                panel.add(pswField);
                panel.add(new JLabel("姓名: "));
                panel.add(nameField);
                panel.add(new JLabel("性别: "));
                panel.add(sexField);
                panel.add(new JLabel("电话: "));
                panel.add(teleField);

                int result = JOptionPane.showConfirmDialog(adminFrame, panel, "输入新值", JOptionPane.OK_CANCEL_OPTION);
                if (result == JOptionPane.OK_OPTION) {
                    String newAcc = accField.getText();
                    String newPsw = pswField.getText();
                    String newName = nameField.getText();
                    String newSex = sexField.getText();
                    String newTele = teleField.getText();

                    // 在数据库中插入新数据
                    try {
                        String insertResult = data.insertMerTable(newAcc, newPsw, newName, newSex, newTele);
                        JOptionPane.showMessageDialog(adminFrame, insertResult, "结果", JOptionPane.INFORMATION_MESSAGE);

                        if (insertResult.equals("添加成功")) {
                            // 更新表格
                            DefaultTableModel model = getMerchantMessageTable(data.selectMerchantTable());
                            merchantTable.setModel(model);
                        }

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(adminFrame, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public void searchMerButtonAction(JButton searchMerButton, DataControl data, JTable merchantTable, JTextField searchField, JComboBox<String> columnSelector) throws SQLException {
        searchMerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedColumn = (String) columnSelector.getSelectedItem();
                String searchValue = searchField.getText();
                String database_name = null;

                if (selectedColumn != null && !selectedColumn.isEmpty() && searchValue != null && !searchValue.isEmpty()) {
                    try {
                        switch (selectedColumn) {
                            case "账户":
                                database_name = "m_acc";
                                break;
                            case "姓名":
                                database_name = "m_name";
                                break;
                            case "ID":
                                database_name = "m_id";
                                break;
                        }
                        //从查找结构中，构建新的表格
                        List<Merchant> merList = data.selectMerTable(database_name, searchValue);
                        merchantTable.setModel(getMerchantMessageTable(merList));
                        merchantTable.repaint();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(adminFrame, "查找失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

}
