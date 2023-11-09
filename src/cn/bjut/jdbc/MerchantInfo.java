package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MerchantInfo extends JDialog {
    private DataControl dataControl;
    private int m_id;
    private Font fort = new Font("微软雅黑", Font.BOLD, 35);


    public MerchantInfo(DataControl dataControl, int m_id) throws SQLException {
        this.dataControl = dataControl;
        this.m_id = m_id;
        initComponents();
    }

    private void initComponents() throws SQLException {
        setTitle("您的信息");
        JPanel panel = new JPanel(new GridLayout(6, 2));

        // 获取商家信息
        Merchant merchant = dataControl.MerchantQuery(m_id);

        // 将标签和文本字段添加到面板
        JLabel label1 = new JLabel("账号名: " + merchant.getAcc());
        label1.setFont(fort);
        panel.add(label1);

        JLabel label2 = new JLabel("昵称: " + merchant.getM_name());
        label2.setFont(fort);
        panel.add(label2);

        JLabel label3 = new JLabel("性别: " + merchant.getM_sex());
        label3.setFont(fort);
        panel.add(label3);

        JLabel label4 = new JLabel("电话: " + merchant.getM_tele());
        label4.setFont(fort);
        panel.add(label4);

        // 添加组件到对话框的内容窗格
        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(null);  // 居中显示对话框

        setSize(500, 400);

    }
}
