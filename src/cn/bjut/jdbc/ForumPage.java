package cn.bjut.jdbc;

import javax.swing.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ForumPage extends JPanel {

    private User user;
    private Merchant merchant;
    private String flag;
    private JPanel commentPanel = new JPanel();
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


    private void initComponents() throws SQLException {
        try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
        e.printStackTrace();
    }

        this.setLayout(new BorderLayout());

        commentPanel.setLayout(new GridLayout(0,1));

        refreshComments();

        JScrollPane scrollPane = new JScrollPane(commentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();// 获取垂直滚动条
        verticalScrollBar.setUnitIncrement(30);
        scrollPane.setVisible(true);



        this.add(scrollPane, BorderLayout.CENTER);

        JButton newCommentButton = new JButton("发表评论");
        newCommentButton.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        newCommentButton.setBackground(new Color(122, 191, 255));
        newCommentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    openNewCommentDialog();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        add(newCommentButton, BorderLayout.SOUTH);

        setBounds(0,0,1200,900);

    }



    private void openNewCommentDialog() throws SQLException {
        NewCommentDialog newCommentDialog;
        if(this.flag.equals("用户")){
            newCommentDialog = new NewCommentDialog(this.user,this.flag);
        }
        else {
             newCommentDialog = new NewCommentDialog(this.merchant,this.flag);
        }
        newCommentDialog.setVisible(true);
    }


    public List<CommentBar> getCommentBars() throws SQLException {
        List<CommentBar> commentBars = dataControl.selectForumList(this.user,this.merchant);
        return commentBars;
    }

    public class NewCommentDialog extends JDialog {

        private User user;
        private Merchant merchant;
        private String flag;
        private JTextArea commentTextField = new JTextArea(15 ,50);
        private JButton postButton = new JButton("提交");

        private JButton cancelButton = new JButton("取消");

        public NewCommentDialog(User user,String flag) throws SQLException {
            this.user = user;
            this.flag = flag;
            this.merchant = null;
            initComponents();
        }
        public NewCommentDialog(Merchant merchant,String flag) throws SQLException {
            this.merchant = merchant;
            this.flag = flag;
            this.user = null;
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
                    if(commentText.equals("")){
                        JOptionPane.showMessageDialog(null, "评论不能为空！", "提示", JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                    int result;
                    try {
                        if(flag.equals("用户")){
                            result = dataControl.insertCommentToforum(user,commentText,flag);
                        }
                        else{
                            result = dataControl.insertCommentToforum(merchant,commentText,flag);
                        }
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                    if(result == 1){
                        JOptionPane.showMessageDialog(null, "评论成功！", "提示", JOptionPane.INFORMATION_MESSAGE);
                        try {
                            ForumPage.this.refreshComments();
                        } catch (SQLException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(null, "评论失败！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    }
                    dispose(); // 关闭对话框
                }
            });

            basicPanel.setLayout(new BoxLayout(basicPanel, BoxLayout.Y_AXIS)); // 垂直布局
            basicPanel.setVisible(true);
            basicPanel.add(conmentPanel);
            basicPanel.add(buttonPanel); // 将按钮面板添加到主面板中
            add(basicPanel);
            pack();
            setLocationRelativeTo(null); // 居中显示对话框
        }

    }



    public void refreshComments() throws SQLException {
        // 重新从数据库中获取评论
        this.commentBars = getCommentBars();

        // 清空评论面板
        commentPanel.removeAll();

        // 将新的评论添加到面板中
        for (CommentBar comment : commentBars) {
            commentPanel.add(comment);
        }



        // 刷新面板
        commentPanel.revalidate();
        commentPanel.repaint();
    }


    public static void main(String[] args) throws SQLException {
        JFrame frame = new JFrame("ForumPage");
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        User user = new User("2","1","1","Chiba Kasumi","男","120");
        ForumPage forumPage = new ForumPage(user,"用户");
        frame.add(forumPage);
        frame.setVisible(true);
    }
}
