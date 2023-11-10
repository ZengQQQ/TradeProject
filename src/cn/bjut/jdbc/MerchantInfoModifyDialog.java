package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.regex.Pattern;

public class MerchantInfoModifyDialog extends JDialog {
    private Merchant oldmerchant;
    private DataControl data;
    private int m_id;
    private Font font = new Font("微软雅黑", Font.BOLD, 16);

    public MerchantInfoModifyDialog(Merchant merchant, DataControl data, int m_id) {
        this.oldmerchant = merchant;
        this.m_id = m_id;
        this.data = data;
        initialize();
    }

    private void initialize() {
        setTitle("修改您的信息");

        // 创建文本字段，用于填入旧的商家信息
        JTextField accountField = new JTextField(oldmerchant.getAcc(), 20);
        JTextField nameField = new JTextField(oldmerchant.getM_name(), 20);
        JTextField genderField = new JTextField(oldmerchant.getM_sex(), 20);
        JTextField phoneField = new JTextField(oldmerchant.getM_tele(), 20);
        JTextField pswField = new JTextField(oldmerchant.getPsw(), 20);

        // 设置字体
        accountField.setFont(font);
        nameField.setFont(font);
        genderField.setFont(font);
        phoneField.setFont(font);
        pswField.setFont(font);

        // 创建 "保存" 按钮
        JButton saveButton = new JButton("保存");
        saveButton.setFont(font);
        saveButton.addActionListener(e -> {
            // 从文本字段中获取修改后的信息
            String newAccount = accountField.getText();
            String newName = nameField.getText();
            String newGender = genderField.getText();
            String newPhone = phoneField.getText();
            String newPsw = pswField.getText();

            // 验证账户、密码、姓名和电话号码
            if (newAccount.isEmpty() || newName.isEmpty() || newGender.isEmpty() || newPhone.isEmpty() || newPsw.isEmpty()) {
                JOptionPane.showMessageDialog(this, "所有字段都必须填写", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Pattern.matches("[\\u4e00-\\u9fa5a-zA-Z]{1,20}", newAccount)) {
                JOptionPane.showMessageDialog(this, "账户只能为字母、中文和字符，且长度不超过20", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Pattern.matches("[\\u4e00-\\u9fa5a-zA-Z]{1,20}", newName)) {
                JOptionPane.showMessageDialog(this, "姓名只能为字母、中文和字符，且长度不超过20", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Pattern.matches("[男女]", newGender)) {
                JOptionPane.showMessageDialog(this, "性别只能为男或女", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Pattern.matches("^[1-9]\\d{10}$", newPhone)) {
                JOptionPane.showMessageDialog(this, "电话号码必须为有效的11位数字", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!Pattern.matches("[\\u4e00-\\u9fa5a-zA-Z0-9]{1,15}", newPsw)) {
                JOptionPane.showMessageDialog(this, "密码只能为字母、中文、字符和数字，且长度不超过15", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }


            // 保存修改后的信息并显示成功消息
            try {
                data.updateMerchant(m_id, newAccount, newName, newGender, newPhone,newPsw);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            JOptionPane.showMessageDialog(this, "信息修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        // 添加组件到对话框的内容窗格
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("账号名:")).setFont(new Font("微软雅黑", Font.BOLD, 16));
        panel.add(accountField).setFont(new Font("微软雅黑", Font.BOLD, 16));
        panel.add(new JLabel("密码:")).setFont(new Font("微软雅黑", Font.BOLD, 16));
        panel.add(pswField).setFont(new Font("微软雅黑", Font.BOLD, 16));
        panel.add(new JLabel("昵称:")).setFont(new Font("微软雅黑", Font.BOLD, 16));
        panel.add(nameField).setFont(new Font("微软雅黑", Font.BOLD, 16));
        panel.add(new JLabel("性别:")).setFont(new Font("微软雅黑", Font.BOLD, 16));
        panel.add(genderField).setFont(new Font("微软雅黑", Font.BOLD, 16));
        panel.add(new JLabel("电话:")).setFont(new Font("微软雅黑", Font.BOLD, 16));
        panel.add(phoneField).setFont(new Font("微软雅黑", Font.BOLD, 16));
        panel.add(saveButton);

        getContentPane().add(panel);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);  // 居中显示对话框
    }
}
