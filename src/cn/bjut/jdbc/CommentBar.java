package cn.bjut.jdbc;

import javax.swing.*;
import javax.xml.crypto.Data;
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
    //该条评论的id
    private String ID;

    private User user;
    private Merchant merchant;
    private String userName;
    private String time;
    private String content;
    private String flag;
    //private int replyCount;//回复的id有多少个
    private List<Comment> replies;

    private  CommentBarListener listener;
    public CommentBar(String ID,String userName, String time, String content, String flag,User user,Merchant merchant) {
        this.ID = ID;
        this.userName = userName;
        this.time = time;
        this.content = content;
        this.flag = flag;
        this.user = user;
        this.merchant = merchant;
        initComponents();
        setDefaultFont();
    }

    public interface CommentBarListener {
        void onDeleteButtonClick(CommentBar commentBar) throws SQLException;
    }
    public void setCommentBarListener(CommentBarListener listener) {
        this.listener = listener;
    }
    private void setDefaultFont() {
        Font font = new Font("微软雅黑", Font.PLAIN, 18);

        for (Component component : getComponents()) {
            setComponentFont(component, font);
        }
    }

    private void setComponentFont(Component component, Font font) {
        if (component instanceof JTextArea || component instanceof JLabel || component instanceof JButton) {
            component.setFont(font);
        }

        if (component instanceof Container) {
            for (Component child : ((Container) component).getComponents()) {
                setComponentFont(child, font);
            }
        }
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
        JLabel flagLabel = new JLabel("   \r 来自: " + flag);
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
        JButton viewRepliesButton = new JButton("查看回复");
        viewRepliesButton.setPreferredSize(new Dimension(100, 30)); // 设置按钮大小
        viewRepliesButton.addActionListener(e -> {
            DataControl data = null;
            int count = 0;
            try {
                data = new DataControl();
                count = data.getCommentCount(this.ID);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
            try {
                if(count == 0){
                    JOptionPane.showMessageDialog(null, "暂无回复");
                }
                else
                    viewReplies(this);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        // 回复按钮
        JButton replyButton = new JButton("回复");
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

        JButton deleteButton = new JButton("删除评论");
        deleteButton.setPreferredSize(new Dimension(100, 30)); // 设置按钮大小
        deleteButton.addActionListener(e -> {
            try {
                if(user!=null) {
                    if (userName.equals(user.getU_name())) {
                        int confirm = JOptionPane.showConfirmDialog(null, "确定删除该评论吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            // 执行删除评论的操作
                            new DataControl().deleteComment(ID);
                            if (listener != null) {
                                listener.onDeleteButtonClick(CommentBar.this);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "只有评论者才能删除评论！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
                else if(merchant!=null){
                if(userName.equals(merchant.getM_name())){
                    int confirm = JOptionPane.showConfirmDialog(null, "确定删除该评论吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        // 执行删除评论的操作
                        new DataControl().deleteComment(ID);
                        if (listener != null) {
                            listener.onDeleteButtonClick(CommentBar.this);
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "只有评论者才能删除评论！", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
                }
            }catch (SQLException ex){
                ex.printStackTrace();
            }
        });

        buttonPanel.add(viewRepliesButton);
        buttonPanel.add(replyButton);
        buttonPanel.add(deleteButton);
        bottomPanel.add(buttonPanel, BorderLayout.CENTER);

        // 添加底部面板到主面板
        add(bottomPanel, BorderLayout.SOUTH);
        setMinimumSize(new Dimension(600, 200));
        setPreferredSize(new Dimension(600, 200));
        setMaximumSize(new Dimension(600, 200));
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
    public void openReplyDialog(CommentBar commentBar) throws SQLException {
        replyCommentDialog newCommentDialog = new replyCommentDialog(this.ID,this.user,this.merchant);
        newCommentDialog.setVisible(true);
    }

    public class RepliesDialog extends JFrame{
        private String ID;

        private List<Comment> replyList;
        public RepliesDialog(String ID) throws SQLException {
            this.ID = ID;
            initComponents();
            setDefaultFont();
        }

        private void setDefaultFont() {
            Font font = new Font("微软雅黑", Font.PLAIN, 20);

            for (Component component : getComponents()) {
                setComponentFont(component, font);
            }
        }

        private void setComponentFont(Component component, Font font) {
            if (component instanceof JTextArea || component instanceof JLabel || component instanceof JButton) {
                component.setFont(font);
            }

            if (component instanceof Container) {
                for (Component child : ((Container) component).getComponents()) {
                    setComponentFont(child, font);
                }
            }
        }

        private void initComponents() throws SQLException {
            JPanel basicPanel = new JPanel();
            basicPanel.setLayout(new BorderLayout());
            setTitle("回复列表");
            DataControl data = new DataControl();
            basicPanel.setSize(600, 800);
            replyList = data.selectReplyTable(ID);

            JPanel replyPanel = new JPanel();
            replyPanel.setLayout(new BoxLayout(replyPanel, BoxLayout.Y_AXIS));
            for (Comment reply : replyList) {
                replyBar replys = new replyBar(reply);
                replys.setPreferredSize(new Dimension(600, 100));
                replyPanel.add(replys);
            }

            JScrollPane scrollPane = new JScrollPane(replyPanel); // Wrap the panel in a JScrollPane
            basicPanel.add(scrollPane, BorderLayout.CENTER);
            this.add(basicPanel, BorderLayout.CENTER);

            setSize(650, 800);
            setVisible(true);
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);
        }


    }


    public class replyBar extends JPanel {
        private String name;
        private String time;
        private String content;
        private String flag;
        private String replyID;

        public replyBar(String name, String time, String content, String flag,String ID) {
            this.name = name;
            this.time = time;
            this.content = content;
            this.flag = flag;
            this.replyID = ID;
            initComponents();

        }

        public replyBar(Comment comment) {
            this.name = comment.getUserName();
            this.time = comment.getTime();
            this.content = comment.getContent();
            this.flag = comment.getFlag();
            this.replyID = comment.getComment_id(); // 这里的 comment_id 是回复的评论的f_id
            initComponents();
        }

        public void initComponents() {
            // 设置边框
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(1, 0, 1, 0, Color.BLACK), // 上下横线
                    BorderFactory.createEmptyBorder(10, 10, 10, 10) // 内边距
            ));

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            JPanel topPanel = new JPanel();
            topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
            JLabel nameLabel = new JLabel("From: " + name);
            topPanel.add(nameLabel);

            // 添加 "Name" 和 "Time" 之间的间隔
            topPanel.add(Box.createHorizontalGlue());

            // 为 "Time" 添加边框
            JLabel timeLabel = new  JLabel("Time: " + time);
            topPanel.add(timeLabel);

            this.add(topPanel);

            JTextArea contentTextArea = new JTextArea(content);
            contentTextArea.setLineWrap(true);
            contentTextArea.setEditable(false);
            contentTextArea.setBackground(this.getBackground());
            contentTextArea.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // 添加边距
            this.add(contentTextArea);

            JPanel bottomPanel = new JPanel();
            bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

            JLabel label = new JLabel( flag);

            JButton deletereplyButton = new JButton("删除回复");
            deletereplyButton.setPreferredSize(new Dimension(30, 15)); // 设置按钮大小
            deletereplyButton.addActionListener(e -> {
                // 在这里添加删除回复评论的逻辑
                try {
                    if(user!=null) {
                        if (name.equals(user.getU_name())) {
                            int confirm = JOptionPane.showConfirmDialog(null, "确定删除该评论吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                // 执行删除评论的操作
                                new DataControl().deleteReply(replyID,ID);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "只有评论者才能删除评论！", "提示", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                    else if(merchant!=null){
                        if(name.equals(merchant.getM_name())){
                            int confirm = JOptionPane.showConfirmDialog(null, "确定删除该评论吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                            if (confirm == JOptionPane.YES_OPTION) {
                                // 执行删除评论的操作
                                new DataControl().deleteReply(replyID,ID);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "只有评论者才能删除评论！", "提示", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }catch (SQLException ex){
                    ex.printStackTrace();
                }
        });

            bottomPanel.add(label,Component.RIGHT_ALIGNMENT);
            bottomPanel.add(Box.createHorizontalGlue());
            bottomPanel.add(deletereplyButton);
            this.add(bottomPanel);

            this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            setMinimumSize(new Dimension(600, 200));
            setPreferredSize(new Dimension(600, 200));
            setMaximumSize(new Dimension(600, 200));
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
        setTitle("回复评论");
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
                    JOptionPane.showMessageDialog(null,"回复成功");
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                dispose(); // 关闭对话框
            }
        });

        basicPanel.setLayout(new BoxLayout(basicPanel, BoxLayout.Y_AXIS)); // 垂直布局
        basicPanel.setVisible(true);
        basicPanel.add(conmentPanel);
        basicPanel.add(buttonPanel); // 将按钮面板添加到主面板中
        add(basicPanel);
        setLocationRelativeTo(null); // 居中显示对话框
        setSize(400, 200);
    }

    }
}
