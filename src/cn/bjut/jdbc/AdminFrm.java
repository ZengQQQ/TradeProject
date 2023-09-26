/*
 * Created by JFormDesigner on Tue Sep 26 15:00:07 CST 2023
 */

package cn.bjut.jdbc;

import java.awt.*;
import javax.swing.*;
import net.miginfocom.swing.*;

/**
 * @author Zcqer
 */
public class AdminFrm extends JFrame {
    public AdminFrm() {
        initComponents();
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents  @formatter:off

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new MigLayout(
            "hidemode 3,alignx left",
            // columns
            "[fill]" +
            "[fill]" +
            "[26,fill]" +
            "[fill]" +
            "[fill]",
            // rows
            "[]" +
            "[]" +
            "[24]" +
            "[]"));
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents  @formatter:on
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables  @formatter:off
    // JFormDesigner - End of variables declaration  //GEN-END:variables  @formatter:on
}
