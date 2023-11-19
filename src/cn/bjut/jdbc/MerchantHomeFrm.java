package cn.bjut.jdbc;

import javax.swing.*;

import javax.swing.border.Border;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;


public class MerchantHomeFrm extends JPanel {
    private MerchantInterFrm mer;
    private int weight = 200;
    private int height = 150;
    private int borderWidth = 2; // 增加边框宽度
    private DataControlMercahnt datacontroller = new DataControlMercahnt();

    public MerchantHomeFrm(MerchantInterFrm mer) throws SQLException {
        this.mer = mer;
        initComponents();
    }

    public void initComponents() throws SQLException {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridLayout(2, 3, 30, 30)); // 创建主面板，使用2行3列的GridLayout，设置行列间距为30
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 30, 50)); // 添加主面板的外边距

        Border border = new LineBorder(Color.BLACK, borderWidth, true); // 创建带有圆角的边框

        Font font1 = new Font("微软雅黑", Font.ROMAN_BASELINE, 30); // 创建字体20号
        Font font2 = new Font("微软雅黑", Font.CENTER_BASELINE, 40); // 创建字体30号

        List<Integer> merchantStats = datacontroller.getMerchantStats(mer.getM_id());
        JPanel innerPanel1 = new JPanel(new BorderLayout()); // 创建每个内部面板1
        innerPanel1.setPreferredSize(new Dimension(weight, height)); // 设置内部面板1的大小
        innerPanel1.setBorder(border); // 设置内部面板1的边框为带有圆角的边框

        JLabel topLeftLabel1 = new JLabel("   总收入（￥）", SwingConstants.LEFT); // 创建左上角的标签
        topLeftLabel1.setFont(font1); // 设置左上角标签的字体为20号
        innerPanel1.add(topLeftLabel1, BorderLayout.NORTH); // 在左上角添加文字

        JLabel bottomCenterLabel1 = new JLabel(String.valueOf(merchantStats.get(0)), SwingConstants.CENTER); // 创建底部中间的标签
        bottomCenterLabel1.setFont(font2); // 设置底部中间标签的字体为30号
        innerPanel1.add(bottomCenterLabel1, BorderLayout.SOUTH); // 在底部中间添加文字

        mainPanel.add(innerPanel1); // 将内部面板1添加到主面板

        JPanel innerPanel2 = new JPanel(new BorderLayout()); // 创建每个内部面板2
        innerPanel2.setPreferredSize(new Dimension(weight, height)); // 设置内部面板2的大小
        innerPanel2.setBorder(border); // 设置内部面板2的边框为带有圆角的边框

        JLabel topLeftLabel2 = new JLabel("   今日收入（￥）", SwingConstants.LEFT); // 创建左上角的标签
        topLeftLabel2.setFont(font1); // 设置左上角标签的字体为20号
        innerPanel2.add(topLeftLabel2, BorderLayout.NORTH); // 在左上角添加文字

        JLabel bottomCenterLabel2 = new JLabel(String.valueOf(merchantStats.get(1)), SwingConstants.CENTER); // 创建底部中间的标签
        bottomCenterLabel2.setFont(font2); // 设置底部中间标签的字体为30号
        innerPanel2.add(bottomCenterLabel2, BorderLayout.SOUTH); // 在底部中间添加文字

        mainPanel.add(innerPanel2); // 将内部面板2添加到主面板

        JPanel innerPanel3 = new JPanel(new BorderLayout()); // 创建每个内部面板3
        innerPanel3.setPreferredSize(new Dimension(weight, height)); // 设置内部面板3的大小
        innerPanel3.setBorder(border); // 设置内部面板3的边框为带有圆角的边框

        JLabel topLeftLabel3 = new JLabel("   总商品数", SwingConstants.LEFT); // 创建左上角的标签
        topLeftLabel3.setFont(font1); // 设置左上角标签的字体为20号
        innerPanel3.add(topLeftLabel3, BorderLayout.NORTH); // 在左上角添加文字

        JLabel bottomCenterLabel3 = new JLabel(String.valueOf(merchantStats.get(2)), SwingConstants.CENTER); // 创建底部中间的标签
        bottomCenterLabel3.setFont(font2); // 设置底部中间标签的字体为30号
        innerPanel3.add(bottomCenterLabel3, BorderLayout.SOUTH); // 在底部中间添加文字

        mainPanel.add(innerPanel3); // 将内部面板3添加到主面板

        JPanel innerPanel4 = new JPanel(new BorderLayout()); // 创建每个内部面板4
        innerPanel4.setPreferredSize(new Dimension(weight, height)); // 设置内部面板4的大小
        innerPanel4.setBorder(border); // 设置内部面板4的边框为带有圆角的边框

        JLabel topLeftLabel4 = new JLabel("   总订单数", SwingConstants.LEFT); // 创建左上角的标签
        topLeftLabel4.setFont(font1); // 设置左上角标签的字体为20号
        innerPanel4.add(topLeftLabel4, BorderLayout.NORTH); // 在左上角添加文字

        JLabel bottomCenterLabel4 = new JLabel(String.valueOf(merchantStats.get(4)), SwingConstants.CENTER); // 创建底部中间的标签
        bottomCenterLabel4.setFont(font2); // 设置底部中间标签的字体为30号
        innerPanel4.add(bottomCenterLabel4, BorderLayout.SOUTH); // 在底部中间添加文字

        mainPanel.add(innerPanel4); // 将内部面板4添加到主面板

        JPanel innerPanel5 = new JPanel(new BorderLayout()); // 创建每个内部面板5
        innerPanel5.setPreferredSize(new Dimension(weight, height)); // 设置内部面板5的大小
        innerPanel5.setBorder(border); // 设置内部面板5的边框为带有圆角的边框

        JLabel topLeftLabel5 = new JLabel("   今日订单数", SwingConstants.LEFT); // 创建左上角的标签
        topLeftLabel5.setFont(font1); // 设置左上角标签的字体为20号
        innerPanel5.add(topLeftLabel5, BorderLayout.NORTH); // 在左上角添加文字

        JLabel bottomCenterLabel5 = new JLabel(String.valueOf(merchantStats.get(4)), SwingConstants.CENTER); // 创建底部中间的标签
        bottomCenterLabel5.setFont(font2); // 设置底部中间标签的字体为30号
        innerPanel5.add(bottomCenterLabel5, BorderLayout.SOUTH); // 在底部中间添加文字

        mainPanel.add(innerPanel5);

        add(mainPanel, BorderLayout.NORTH); // 将主面板添加到北部
    }

}


