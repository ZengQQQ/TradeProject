package cn.bjut.jdbc;

import java.util.Date;import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ForumPage extends JPanel {
    private JTextArea postTextArea;
    private JButton postButton;
    private JPanel postPanel;

    private DataControl data = new DataControl();

    public ForumPage(User user) throws SQLException {
        initComponents();
    }

    private void initComponents() {

        // 发表帖子区域
        postTextArea = new JTextArea(5, 30);
        postButton = new JButton("发表");
        postButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 处理发表帖子逻辑
                String postContent = postTextArea.getText();
                // 将内容保存到数据库
                // 刷新帖子列表
            }
        });

        // 帖子列表面板
        postPanel = new JPanel();
        postPanel.setLayout(new BoxLayout(postPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(postPanel);

        JPanel inputPanel = new JPanel();
        inputPanel.add(new JScrollPane(postTextArea));
        inputPanel.add(postButton);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.add(scrollPane, BorderLayout.CENTER);
        contentPane.add(inputPanel, BorderLayout.SOUTH);

        this.add(contentPane);
    }

    // 更新帖子列表的方法，从数据库中读取帖子并显示在postPanel中
    private void updatePostList() {
        postPanel.removeAll(); // 清空已有的帖子列表
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM posts");
            while (rs.next()) {
                int postID = rs.getInt("post_id");
                String postContent = rs.getString("post_content");
                JButton replyButton = new JButton("回复");
                replyButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        openReplyDialog(postID);
                    }
                });
                postPanel.add(new JLabel(postContent));
                postPanel.add(replyButton);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        postPanel.revalidate(); // 更新UI
    }

    private void openReplyDialog(int postID) {
        ReplyDialog replyDialog = new ReplyDialog(this, postID);
        replyDialog.setVisible(true);
    }

//    public static void main(String[] args) {
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                ForumPage forumPage = new ForumPage();
//                forumPage.setVisible(true);
//            }
//        });
//    }
}
