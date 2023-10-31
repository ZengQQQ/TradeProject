package cn.bjut.jdbc;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CommentBar extends JPanel {
    //评论人的id
    private String ID;
    private String userName;
    private String time;
    private String content;
    private int replyCount;//回复的id有多少个
    private List<Comment> replies;

    public CommentBar(String ID,String userName, String time, String content, int replyCount) {
        this.ID = ID;
        this.userName = userName;
        this.time = time;
        this.content = content;
        this.replyCount = replyCount;
        this.replies = new ArrayList<>();

        initComponents();
    }

    private void initComponents() {

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JPanel topPanel = new JPanel();
        JLabel nameLabel = new JLabel("Name: " + userName);
        JLabel timeLabel = new JLabel("Time: " + time);
        topPanel.add(nameLabel, BorderLayout.WEST);
        topPanel.add(timeLabel, BorderLayout.EAST);

        JLabel contentLabel = new JLabel("Content: " + content);

        JPanel bottomPanel = new JPanel();

        JButton viewRepliesButton = new JButton("View Replies (" + this.replyCount+ ")");
        viewRepliesButton.addActionListener(e -> {
            viewReplies(this);
        });
        JButton replyButton = new JButton("Reply");
        replyButton.addActionListener(e -> {
            openReplyDialog(this);
        });

        bottomPanel.add(viewRepliesButton, BorderLayout.EAST);
        bottomPanel.add(replyButton, BorderLayout.WEST);



        add(topPanel);
        add(contentLabel);
        add(bottomPanel);

    }

    public void addReply(Comment reply) {
        replies.add(reply);
    }

    public String getUserName() {
        return userName;
    }

    public String getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public int getReplyCount() {
        return replyCount;
    }



    private void viewReplies(CommentBar commentBar) {
        RepliesDialog repliesDialog = new RepliesDialog(replies);
        repliesDialog.setVisible(true);
    }

    public void openReplyDialog(CommentBar commentBar) {
        NewCommentDialog newCommentDialog = new NewCommentDialog();
        newCommentDialog.setVisible(true);
    }

    public class RepliesDialog extends JDialog {
        private List<Comment> replyList;
        public RepliesDialog(List<Comment> replyList) {
            this.replyList = replyList;
            initComponents();
        }

        private void initComponents() {

            JScrollPane scrollPane = new JScrollPane();
            for(Comment reply : replyList){
                scrollPane.add(new replyBar(reply));
            }
            add(scrollPane);
            pack();
            setLocationRelativeTo(null); // 居中显示对话框
        }

    }


    public class replyBar extends JPanel{
        private String name;
        private String time;
        private String content;
        public replyBar(String name,String time,String content){
            this.name = name;
            this.time = time;
            this.content = content;
            initComponents();
        }

        public replyBar(Comment comment){
            this.name = comment.getUserName();
            this.time = comment.getTime();
            this.content = comment.getContent();
            initComponents();
        }
        public void initComponents(){
            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            JPanel topPanel = new JPanel();
            JLabel nameLabel = new JLabel("Name: " + name);
            JLabel timeLabel = new JLabel("Time: " + time);
            topPanel.add(nameLabel, BorderLayout.WEST);
            topPanel.add(timeLabel, BorderLayout.EAST);

            JLabel contentLabel = new JLabel("Content: " + content);

            add(topPanel);
            add(contentLabel);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }

    }



    public class Comment{
        private String userName;
        private String time;
        private String content;

        public Comment(String userName, String time, String content) {
            this.userName = userName;
            this.time = time;
            this.content = content;
        }

        public String getUserName() {
            return userName;
        }

        public String getTime() {
            return time;
        }

        public String getContent() {
            return content;
        }
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
