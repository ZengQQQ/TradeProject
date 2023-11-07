package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class MerchantInfo extends JPanel {
    private DataControl dataControl;
    private int m_id;

    public MerchantInfo(DataControl dataControl, int m_id) throws SQLException {
        this.dataControl = dataControl;
        this.m_id = m_id;
        initComponents();
    }

    private void initComponents() throws SQLException {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(1, 1, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.NORTH;

        // 获取商家信息
        Merchant merchant = dataControl.MerchantQuery(m_id);

        // 将标签和文本字段添加到面板
        add(new JLabel("账号名: " + merchant.getAcc()), gbc);
        gbc.gridy++;
        add(new JLabel("昵称: " + merchant.getM_name()), gbc);
        gbc.gridy++;
        add(new JLabel("性别: " + merchant.getM_sex()), gbc);
        gbc.gridy++;
        add(new JLabel("电话: " + merchant.getM_tele()), gbc);


    }
}
