/*
 * Created by JFormDesigner on Tue Sep 26 08:21:52 CST 2023
 */

package cn.bjut.jdbc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author 郭yw
 */
public class Jframe extends JFrame {
    public Jframe() {
        initComponents();
    }

    private void button1KeyPressed(KeyEvent e) {
        // TODO add your code here
    }

    private void button2KeyPressed(KeyEvent e) {
        // TODO add your code here
    }

    private void button3KeyPressed(KeyEvent e) {
        // TODO add your code here
    }

    private void button4KeyPressed(KeyEvent e) {
        // TODO add your code here
    }

    private void button5KeyPressed(KeyEvent e) {
        // TODO add your code here
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off
        button5 = new JButton();
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        button4 = new JButton();

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
            "[]"));

        //---- button5 ----
        button5.setText("\u641c\u7d22");
        button5.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                button5KeyPressed(e);
            }
        });
        contentPane.add(button5, "cell 4 0");

        //---- button1 ----
        button1.setText("\u9996\u9875");
        button1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                button1KeyPressed(e);
            }
        });
        contentPane.add(button1, "cell 2 8");

        //---- button2 ----
        button2.setText("\u52a8\u6001");
        button2.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                button2KeyPressed(e);
            }
        });
        contentPane.add(button2, "cell 4 8");

        //---- button3 ----
        button3.setText("\u8d2d\u7269\u8f66");
        button3.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                button3KeyPressed(e);
            }
        });
        contentPane.add(button3, "cell 6 8");

        //---- button4 ----
        button4.setText("\u6211\u7684");
        button4.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                button4KeyPressed(e);
            }
        });
        contentPane.add(button4, "cell 8 8");
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    private JButton button5;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
