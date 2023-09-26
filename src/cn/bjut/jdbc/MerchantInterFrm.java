/*
 * Created by JFormDesigner on Tue Sep 26 09:27:07 CST 2023
 * 商家主界面登录
 */

package cn.bjut.jdbc;

import java.awt.*;
import java.awt.event.*;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;
import net.miginfocom.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class MerchantInterFrm extends JFrame {

    private DataBase data=new DataBase();
    private JTextField usernameField;
    private JPasswordField passwordField;

    public MerchantInterFrm() {
        setTitle("商家主界面");
        setSize(700, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建登录界面
        JPanel loginPanel = new JPanel();
        JLabel usernameLabel = new JLabel("用户名:");
        JLabel passwordLabel = new JLabel("密码:");
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("登录");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int m_id = Integer.parseInt(usernameField.getText());
                String m_psw = new String(passwordField.getPassword());

                // 验证商家登录信息，连接数据库执行验证操作
                boolean loginSuccessful;
                try {
                    loginSuccessful = validateMerchantLogin(m_id, m_psw);//loginSuccessful的值表示登录成功没有
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }

                if (loginSuccessful) {
                    // 登录成功后打开商家主页
                    openMerchantHomePage();
                } else {
                    JOptionPane.showMessageDialog(null, "登录失败，请检查用户名和密码。");
                }
            }
        });

        loginPanel.add(usernameLabel);
        loginPanel.add(usernameField);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);

        // 创建主界面的其他组件和功能
        // ...

        // 添加登录界面到主窗口
        add(loginPanel);
    }

    // 连接数据库，验证商家登录信息
    private boolean validateMerchantLogin(int username, String password) throws Exception {
        Connection conn = data.getCon();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean loginSuccessful = false;

        try {
            conn = data.getCon();
            String sql = "SELECT * FROM merchant WHERE m_id = ? AND m_psw = ?";
            stmt = conn.prepareStatement(sql);//查询sql语句
            stmt.setInt(1, username);//补全第一个？号
            stmt.setString(2, password);//补全第二个？号
            rs = stmt.executeQuery();//rs是一个结果集
            //以下是选择要输出的结果集 供测试用
            while (rs.next()) {
                int id = rs.getInt("m_id"); // 根据表中的列名获取数据
                String psw = rs.getString("m_psw");
                String name = rs.getString("m_name");
                System.out.println("ID: " + id +" "+ "paw: " + psw + " " + "name: " + name);
                System.out.println("--------------------------");
            }
            if (rs.first()) {
                loginSuccessful = true; // 表示至少有一行匹配
            } else {
                loginSuccessful = false; // 没有匹配的行
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {//关闭
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return loginSuccessful;
    }

    // 打开商家主页
    private void openMerchantHomePage() {
        // 创建商家主页的界面和功能
        // ...

        // 移除登录界面，添加商家主页界面到主窗口
        getContentPane().removeAll();
        // ...

        // 刷新界面
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                MerchantInterFrm merchantInterFrm = new MerchantInterFrm();
                merchantInterFrm.setVisible(true);
            }
        });
    }
}

