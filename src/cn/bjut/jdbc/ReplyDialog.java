package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReplyDialog extends JDialog {
    private JTextArea replyTextArea;
    private JButton replyButton;

    private JPanel parentPanel;

    private int postID;

    public ReplyDialog(JPanel parentPanel, int postID) {
        this.parentPanel = parentPanel;
        this.postID = postID;

        initComponents();
    }

    private void initComponents() {
        replyTextArea = new JTextArea(5, 30);

        replyButton = new JButton("回复");
        replyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String replyContent = replyTextArea.getText();
                saveReplyToDatabase(replyContent);
                dispose(); // 关闭对话框
            }
        });

        JPanel contentPane = new JPanel();
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(new BorderLayout());
        contentPane.add(new JScrollPane(replyTextArea), BorderLayout.CENTER);
        contentPane.add(replyButton, BorderLayout.SOUTH);

        setContentPane(contentPane);
        pack();
        setLocationRelativeTo(null); // 居中显示对话框
    }

    private void saveReplyToDatabase(String replyContent) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database", "username", "password");
            PreparedStatement stmt = conn.prepareStatement("INSERT INTO replies (post_id, reply_content) VALUES (?, ?)");
            stmt.setInt(1, postID);
            stmt.setString(2, replyContent);
            stmt.executeUpdate();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
