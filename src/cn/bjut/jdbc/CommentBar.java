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
    private String flag;
    //private int replyCount;//回复的id有多少个
    private List<Comment> replies;

    public CommentBar(String ID,String userName, String time, String content, String flag) {
        this.ID = ID;
        this.userName = userName;
        this.time = time;
        this.content = content;
        this.flag = flag;
        //this.replyCount = replyCount;
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

        JButton viewRepliesButton = new JButton("View Replies  ");
        viewRepliesButton.addActionListener(e -> {
            try {
                viewReplies(this);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
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

    public String getID() {
        return ID;
    }


//查看当前评论的回复
    private void viewReplies(CommentBar commentBar) throws SQLException {
        RepliesDialog repliesDialog = new RepliesDialog(this.ID);
        repliesDialog.setVisible(true);
    }
    //回复当前某一条评论
    public void openReplyDialog(CommentBar commentBar) {
        NewCommentDialog newCommentDialog = new NewCommentDialog();
        newCommentDialog.setVisible(true);
    }

    public class RepliesDialog extends JDialog {
        private String ID;
        public RepliesDialog(String ID) throws SQLException {
            this.ID = ID;
            initComponents();
        }

        private void initComponents() throws SQLException {

            setTitle("Replies");
            DataControl data = new DataControl();

            List<Comment> replyList = data.selectReplyTable(ID);

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
        private String flag;
        public replyBar(String name,String time,String content,String flag){
            this.name = name;
            this.time = time;
            this.content = content;
            this.flag = flag;
            initComponents();
        }

        public replyBar(Comment comment){
            this.name = comment.getUserName();
            this.time = comment.getTime();
            this.content = comment.getContent();
            this.flag = comment.getFlag();
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

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

            JLabel label = new JLabel("来自: " + flag);
            label.setAlignmentX(Component.RIGHT_ALIGNMENT);

            bottomPanel.add(Box.createHorizontalGlue()); // 左侧填充
            bottomPanel.add(label);

            add(topPanel);
            add(contentLabel);
            add(bottomPanel);
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
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
