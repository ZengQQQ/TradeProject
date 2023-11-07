package cn.bjut.jdbc;
import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MerchantInfoModifyDialog extends JDialog {
    private Merchant oldmerchant;
    private DataControl data;
    private int m_id;

    public MerchantInfoModifyDialog(Merchant merchant,DataControl data,int m_id) {
        this.oldmerchant = merchant;
        this.m_id=m_id;
        this.data=data;
        initialize();
    }

    private void initialize() {
        setTitle("修改您的信息");

        // 创建文本字段，用于填入旧的商家信息
        JTextField accountField = new JTextField(oldmerchant.getAcc(), 20);
        JTextField nameField = new JTextField(oldmerchant.getM_name(), 20);
        JTextField genderField = new JTextField(oldmerchant.getM_sex(), 20);
        JTextField phoneField = new JTextField(oldmerchant.getM_tele(), 20);

        // 创建 "保存" 按钮
        JButton saveButton = new JButton("保存");
        saveButton.addActionListener(e -> {
            // 从文本字段中获取修改后的信息
            String newAccount = accountField.getText();
            String newName = nameField.getText();
            String newGender = genderField.getText();
            String newPhone = phoneField.getText();

            // 验证性别和电话号码等信息（你可以根据需要添加更多验证）

            // 此时，你可以保存修改后的信息并显示成功消息
            // 可以将信息保存到你的数据源
            try {
                data.updateMerchant(m_id,newAccount, newName, newGender, newPhone);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }

            JOptionPane.showMessageDialog(this, "信息修改成功", "成功", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });

        // 添加组件到对话框的内容窗格
        JPanel panel = new JPanel(new GridLayout(6, 2));
        panel.add(new JLabel("账号名:"));
        panel.add(accountField);
        panel.add(new JLabel("姓名:"));
        panel.add(nameField);
        panel.add(new JLabel("性别:"));
        panel.add(genderField);
        panel.add(new JLabel("电话:"));
        panel.add(phoneField);
        panel.add(saveButton);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);  // 居中显示对话框
    }
}

