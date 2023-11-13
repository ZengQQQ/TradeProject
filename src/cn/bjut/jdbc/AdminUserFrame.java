package cn.bjut.jdbc;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class AdminUserFrame extends JPanel {
    private AdminFrame adminFrame;
    private DataControl data;
    private DefaultTableModel datamode;

    public AdminUserFrame(AdminFrame adminFrame, DataControl data) throws SQLException {
        this.adminFrame = adminFrame;
        this.data = data;
        initComponents();
    }

    public void initComponents() throws SQLException {
        datamode = getUserMessageTable(data.selectUserTable());
        this.setLayout(new BorderLayout());//card1 borderlayout 容器使用设计界面。


        JTable userTable = new JTable(getUserMessageTable(data.selectUserTable()));  //创建用户表展示
        JScrollPane scrollPane = new JScrollPane(userTable);
        this.add(scrollPane,BorderLayout.CENTER);

        //创建修改按钮
        JButton editButton = new JButton("修改");
        JButton editButton2 = new JButton("重置");
        JButton editButton3 = new JButton("删除");
        JButton editButton4 = new JButton("添加");

        //使用panel容器添加按钮
        JPanel editButtonPanel = new JPanel(new GridLayout(1,4));
        editButtonPanel.add(editButton);
        editButtonPanel.add(editButton2);
        editButtonPanel.add(editButton3);
        editButtonPanel.add(editButton4);
        this.add(editButtonPanel,BorderLayout.SOUTH);

        //添加查找功能
        JPanel searchPanel = new JPanel(new FlowLayout());
        String[] selectColumnNames = {"ID", "账户", "姓名"};
        JComboBox<String> columnSelector = new JComboBox<>(selectColumnNames);
        JTextField searchField = new JTextField(20);
        JButton searchButton = new JButton("查找");
        JButton searchAll = new JButton("查看全部");
        //添加查看全部按钮事件
        searchAll.addActionListener(e->{
            userTable.setModel(datamode);
        });
        searchPanel.add(columnSelector);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(searchAll);
        this.add(searchPanel,BorderLayout.NORTH);

        //添加修改按钮的监听事件
        updateButtonAction(editButton,data,userTable);
        setNewButtonAction(editButton2,data,userTable);
        deleteButtonAction(editButton3,data,userTable);
        insertButtonAction(editButton4,data,userTable);
        searchButtonAction(searchButton,data,userTable,searchField,columnSelector,this);

    }

    //从数据库中获取用户信息
    public DefaultTableModel getUserMessageTable(java.util.List<User> userList){
        String[] columnNames = {"ID", "账户", "密码", "姓名", "性别", "电话"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (User user : userList) {
            Object[] rowData = {user.getID(), user.getAcc(), user.getPsw(), user.getU_name(), user.getU_sex(), user.getU_tele()};
            model.addRow(rowData);
        }
        return model;
    }


    //四个时间监听事件
    public void updateButtonAction(JButton editButton,DataControl data,JTable userTable) throws SQLException {
        // TODO add your code here
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
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
                            int columnIndex = Arrays.asList(options).indexOf(selectedColumn)+1;
                            userTable.setValueAt(newValue, selectedRow, columnIndex);

                            switch(selectedColumn){
                                case "账户":
                                    database_name = "u_acc";
                                    break;
                                case "密码":
                                    database_name= "u_psw";
                                    break;
                                case "姓名":
                                    database_name = "u_name";
                                    break;
                                case "性别":
                                    database_name = "u_sex";
                                    break;
                                case "电话":
                                    database_name = "u_tele";
                                    break;
                            }

                            try {
                                data.updateUserTable(Integer.parseInt((String) userTable.getValueAt(selectedRow, 0)), database_name, newValue);
                                //顺便更新usertable的数据
                                userTable.setValueAt(newValue, selectedRow, columnIndex);

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

    public void setNewButtonAction( JButton editButton,DataControl data,JTable userTable) throws SQLException {
        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int selectedRow = userTable.getSelectedRow();
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
                        userTable.setValueAt(newAcc, selectedRow, 1);
                        userTable.setValueAt(newPsw, selectedRow, 2);
                        userTable.setValueAt(newName, selectedRow, 3);
                        userTable.setValueAt(newSex, selectedRow, 4);
                        userTable.setValueAt(newTele, selectedRow, 5);

                        // 更新数据库
                        int u_id = Integer.parseInt((String) userTable.getValueAt(selectedRow, 0)) ; // 获取 u_id
                        try {
                            String updateResult = data.updateUserTable(u_id, newAcc, newPsw, newName, newSex, newTele);
                            JOptionPane.showMessageDialog(adminFrame, updateResult, "结果", JOptionPane.INFORMATION_MESSAGE);
                        } catch (SQLException ex) {
                            JOptionPane.showMessageDialog(adminFrame, "更新失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                }

            }

        });
    }

    public void deleteButtonAction(JButton deleteButton,DataControl data,JTable userTable) throws SQLException{
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int selectedRow = userTable.getSelectedRow();
                if (selectedRow != -1) {
                    int confirmResult = JOptionPane.showConfirmDialog(adminFrame, "确认删除该用户吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                    if (confirmResult == JOptionPane.YES_OPTION) {
                        int u_id = Integer.parseInt((String) userTable.getValueAt(selectedRow, 0)); // 获取 u_id
                        try {
                            String deleteResult = data.deleteUserTable(u_id);
                            if (deleteResult.equals("删除成功")) {
                                DefaultTableModel model = (DefaultTableModel) userTable.getModel();
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

    public void insertButtonAction(JButton insertButton, DataControl data, JTable userTable) throws SQLException {
        insertButton.addActionListener(new ActionListener() {
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
                        String insertResult = data.insertUserTable(newAcc, newPsw, newName, newSex, newTele);
                        JOptionPane.showMessageDialog(adminFrame, insertResult, "结果", JOptionPane.INFORMATION_MESSAGE);

                        if (insertResult.equals("添加成功")) {
                            // 更新表格
                            DefaultTableModel  model = getUserMessageTable(data.selectUserTable());
                            userTable.setModel(model);
                        }

                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(adminFrame, "添加失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public void searchButtonAction(JButton searchButton,DataControl data,JTable userTable,JTextField searchField,JComboBox<String> columnSelector,JPanel card1) throws SQLException {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedColumn = (String) columnSelector.getSelectedItem();
                String searchValue = searchField.getText();
                String database_name = null;

                if (selectedColumn != null && !selectedColumn.isEmpty() && searchValue != null && !searchValue.isEmpty()) {
                    try {
                        switch(selectedColumn){
                            case "账户":
                                database_name = "u_acc";
                                break;
                            case "姓名":
                                database_name = "u_name";
                                break;
                            case "ID":
                                database_name = "u_id";
                                break;
                        }
                        //从查找结构中，构建新的表格
                        List<User> userList = data.selectUserTable(database_name, searchValue);
                        userTable.setModel(getUserMessageTable(userList));
                        userTable.repaint();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(adminFrame, "查找失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }
}
