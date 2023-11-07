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

    private User user;
    private Merchant merchant;
    private String userName;
    private String time;
    private String content;
    private String flag;
    //private int replyCount;//回复的id有多少个
    private List<Comment> replies;

    public CommentBar(String ID,String userName, String time, String content, String flag,User user,Merchant merchant) {
        this.ID = ID;
        this.userName = userName;
        this.time = time;
        this.content = content;
        this.flag = flag;
        this.user = user;
        this.merchant = merchant;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10)); // 设置主布局

        // 添加边框和间距
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createEmptyBorder(10, 10, 10, 10),
                BorderFactory.createLineBorder(Color.BLACK)
        ));

        // 创建顶部信息面板
        JPanel topPanel = new JPanel(new BorderLayout());
        // 添加边框
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));

        JLabel nameLabel = new JLabel("Name: " + userName);
        JLabel flagLabel = new JLabel("来自: " + flag);
        JLabel timeLabel = new JLabel("Time: " + time);
        topPanel.add(nameLabel, BorderLayout.WEST);
        topPanel.add(flagLabel, BorderLayout.CENTER);
        topPanel.add(timeLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // 创建评论内容标签
        JTextArea contentTextArea = new JTextArea(content);
        contentTextArea.setLineWrap(true);
        contentTextArea.setEditable(false);
        contentTextArea.setBackground(this.getBackground());
        contentTextArea.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0)); // 添加边距
        add(contentTextArea, BorderLayout.CENTER);
        // 创建底部按钮面板
        JPanel bottomPanel = new JPanel(new BorderLayout());

        // 查看回复按钮
        JButton viewRepliesButton = new JButton("View Replies");
        viewRepliesButton.setPreferredSize(new Dimension(100, 30)); // 设置按钮大小
        viewRepliesButton.addActionListener(e -> {
            try {
                viewReplies(this);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        // 回复按钮
        JButton replyButton = new JButton("Reply");
        replyButton.setPreferredSize(new Dimension(100, 30)); // 设置按钮大小
        replyButton.addActionListener(e -> {
            try {
                openReplyDialog(this);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        // 创建按钮面板，并添加查看回复按钮和回复按钮
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // 使用流式布局，将按钮放在右边
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // 添加边距
        buttonPanel.add(viewRepliesButton);
        buttonPanel.add(replyButton);

        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        // 添加底部面板到主面板
        add(bottomPanel, BorderLayout.SOUTH);
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
        //todo:在这里添加回复的内容
        repliesDialog.setVisible(true);
    }
    //回复当前某一条评论
    public void openReplyDialog(CommentBar commentBar) throws SQLException {
        replyCommentDialog newCommentDialog = new replyCommentDialog(this.ID,this.user,this.merchant);
        newCommentDialog.setVisible(true);
    }

    public class RepliesDialog extends JDialog {
        private String ID;

        private List<Comment> replyList;
        public RepliesDialog(String ID) throws SQLException {
            this.ID = ID;
            initComponents();
        }

        private void initComponents() throws SQLException {
            this.setLayout(new BorderLayout());
            setTitle("Replies");
            DataControl data = new DataControl();

            this.setLayout(new BorderLayout());
            replyList = data.selectReplyTable(ID);

            JScrollPane scrollPane = new JScrollPane();
            JPanel replyPanel = new JPanel();
            replyPanel.setLayout(new BoxLayout(replyPanel, BoxLayout.Y_AXIS));

            for (Comment reply : replyList) {
                System.out.println((reply.getFlag()));
                replyPanel.add(new replyBar(reply));
            }

            scrollPane.setViewportView(replyPanel);
            add(scrollPane);
            setSize(500, 500);
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
            setLayout(new BorderLayout(10, 10));
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(10, 10, 10, 10),
                    BorderFactory.createLineBorder(Color.BLACK)
            ));

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
            JLabel nameLabel = new JLabel("Name: " + name);
            JLabel timeLabel = new JLabel("Time: " + time);
            topPanel.add(nameLabel, BorderLayout.WEST);
            topPanel.add(Box.createHorizontalGlue());
            topPanel.add(timeLabel, BorderLayout.EAST);

            JTextArea contentTextArea = new JTextArea(content);
            contentTextArea.setLineWrap(true);
            contentTextArea.setEditable(false);
            contentTextArea.setBackground(this.getBackground());


            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
            JLabel label = new JLabel("来自: " + flag);
            label.setAlignmentX(Component.RIGHT_ALIGNMENT);
            bottomPanel.add(Box.createHorizontalGlue());
            bottomPanel.add(label);

            add(topPanel, BorderLayout.NORTH);
            add(contentTextArea, BorderLayout.CENTER);
            add(bottomPanel, BorderLayout.SOUTH);

            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        }



    }



//回复某条评论的对话框
    public class replyCommentDialog extends JDialog {

        private String ID;//待回复的评论的id
        private User user;//当前用户
        private Merchant merchant;//或者当前商家
        private String flag;

        private JTextArea commentTextField = new JTextArea(5,30);
        private JButton postButton = new JButton("提交");

        private JButton cancelButton = new JButton("取消");

        public replyCommentDialog(String ID,User user,Merchant merchant) throws SQLException {
            this.ID = ID;
            if(user != null){
                this.user = user;
                this.merchant = null;
                this.flag = "用户";
            }
            else {
                this.merchant = merchant;
                this.user = null;
                this.flag = "商家";
            }
            initComponents();
        }



    private void initComponents() {
        setTitle("New Comment");
        setModal(true);
        JPanel basicPanel = new JPanel();

        JPanel conmentPanel = new JPanel();
        conmentPanel.add(new JLabel("评论内容: "));
        conmentPanel.add(commentTextField, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(postButton);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(cancelButton);
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // 关闭对话框
            }
        });

        postButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String commentText = commentTextField.getText();
                // 在这里可以对评论进行处理
                if(commentText.equals("")){
                    JOptionPane.showMessageDialog(null, "评论不能为空");
                    return;
                }
                try {
                    DataControl data = new DataControl();
                    data.insertReplyToforum(ID,commentText,user,merchant);
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                // TODO: 保存评论到数据库
                dispose(); // 关闭对话框
            }
        });

        basicPanel.setLayout(new BoxLayout(basicPanel, BoxLayout.Y_AXIS)); // 垂直布局
        basicPanel.setVisible(true);
        basicPanel.add(conmentPanel);
        basicPanel.add(buttonPanel); // 将按钮面板添加到主面板中
        add(basicPanel);
        setLocationRelativeTo(null); // 居中显示对话框
        pack();
    }

    }
}
