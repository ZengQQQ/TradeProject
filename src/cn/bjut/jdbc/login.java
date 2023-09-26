package cn.bjut.jdbc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author 郭yw          未完成，用户登录界面
 */
public class login extends JFrame {
    public login() {
        initComponents();
    }
    StringBuilder sb=new StringBuilder();
    private void button1MouseClicked(MouseEvent e,String[] s) {
        // TODO add your code here
        for (int i = 0; i <s.length ; i++) {
            if (sb.equals(s[i])){
                System.out.println("yes");
            }
            else{
                System.out.println(s[i]);
                System.out.println("no");
            }
        }
    }

    private void button2MouseClicked(MouseEvent e) {
        // TODO add your code here
        System.out.println("222222");
        dispose();

    }


    private void textField1KeyPressed(KeyEvent e) {
        String st=textField1.getText();
        sb.append(st);
        System.out.println(st);
    }

    private void textField2KeyPressed(KeyEvent e) {
        // TODO add your code here
        System.out.print(e.getKeyChar());
    }

    private void textField2MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void button1MouseClicked(MouseEvent e) {
        // TODO add your code here
    }

    private void initComponents() {

        String account[]={"1234","2345"};
        char code[]=new char[50];

        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        label1 = new JLabel();
        textArea1 = new JTextArea();
        label2 = new JLabel();
        textField1 = new JTextField();
        textField2 = new JTextField();
        label3 = new JLabel();
        button1 = new JButton();
        button2 = new JButton();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
                "hidemode 3",
                // columns
                "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[54,fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]",
                // rows
                "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

        //---- label1 ----
        label1.setText("请登录");
        contentPane.add(label1, "cell 8 1");
        contentPane.add(textArea1, "cell 3 3");

        //---- label2 ----
        label2.setText("\u8d26\u53f7");
        contentPane.add(label2, "cell 4 3");

        //---- textField1 ----
        textField1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                textField1KeyPressed(e);
            }
        });
        contentPane.add(textField1, "cell 5 3");

        //---- textField2 ----
        textField2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                textField2KeyPressed(e);
            }
        });
        textField2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                textField2MouseClicked(e);
            }
        });
        contentPane.add(textField2, "cell 5 4 1 3");

        //---- label3 ----
        label3.setText("\u5bc6\u7801");
        contentPane.add(label3, "cell 4 5");

        //---- button1 ----
        button1.setText("\u786e\u5b9a");
        button1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button1MouseClicked(e);
            }
        });
        contentPane.add(button1, "cell 5 8");

        //---- button2 ----
        button2.setText("\u53d6\u6d88");
        button2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                button2MouseClicked(e);
            }
        });
        contentPane.add(button2, "cell 12 8");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    public static void main(String[] args) {
        new login().setVisible(true);


    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JLabel label1;
    private JTextArea textArea1;
    private JLabel label2;
    private JTextField textField1;
    private JTextField textField2;
    private JLabel label3;
    private JButton button1;
    private JButton button2;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
