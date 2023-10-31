package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ForumPage extends JPanel {

    private User user;
    private Merchant merchant;
    private String flag;
    private List<CommentBar> commentBars;

    private DataControl dataControl = new DataControl();

    public ForumPage(User user,String flag) throws SQLException {
        this.user = user;
        this.flag = flag;
        this.merchant = null;
        this.commentBars = getCommentBars();
        initComponents();
    }

    public ForumPage(Merchant merchant,String flag) throws SQLException {
        this.merchant = merchant;
        this.flag = flag;
        this.user = null;
        this.commentBars = getCommentBars();
        initComponents();
    }


    private void initComponents() {

        JPanel commentPanel = new JPanel();
        commentPanel.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        for (CommentBar comment : commentBars) {
            commentPanel.add(comment);
        }

        JScrollPane scrollPane = new JScrollPane(commentPanel);
        this.add(scrollPane,BorderLayout.CENTER);


        JButton newCommentButton = new JButton("New Comment");
        newCommentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openNewCommentDialog();
            }
        });
        add(newCommentButton, BorderLayout.SOUTH);
    }

    private void openNewCommentDialog() {
        NewCommentDialog newCommentDialog = new NewCommentDialog();
        newCommentDialog.setVisible(true);
    }


    public List<CommentBar> getCommentBars() throws SQLException {
        List<CommentBar> commentBars = dataControl.selectForumList();

        return commentBars;
    }

    public class NewCommentDialog extends JDialog {

        private JTextField commentTextField = new JTextField(20);
        private JButton postButton = new JButton("提交");

        private JButton cancelButton = new JButton("取消");

        public NewCommentDialog() {
            initComponents();
        }

        private void initComponents() {
            setTitle("New Comment");
            setModal(true);

            JPanel panel = new JPanel();
            panel.add(new JLabel("评论内容: "));
            panel.add(commentTextField, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel(new BoxLayout(postButton, BoxLayout.X_AXIS));
            buttonPanel.add(postButton);
            buttonPanel.add(Box.createHorizontalStrut(10));
            buttonPanel.add(cancelButton);

            panel.add(buttonPanel, BorderLayout.SOUTH);
            cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose(); // 关闭对话框
                }
            });

            postButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String commentText = commentTextField.getText();
                    // 在这里可以对评论进行处理
                    // TODO: 保存评论到数据库
                    dispose(); // 关闭对话框
                }
            });

            add(panel);
            pack();
        }

        public CommentBar getComment() {

            return null;

        }
    }

}
