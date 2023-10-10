package cn.bjut.jdbc;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.*;

public class DataControl {


    public ResultSet executeQuery(String table_name,String colum_name,String x) throws SQLException {

        String sql= "select * from " + table_name + " where "+colum_name + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,x);
        ResultSet rs = stmt.executeQuery();
        return rs;

    }

    public String getUserPsw(String account) throws SQLException {
        String answer;
        String sql= "select u_psw from user "+ " where  u_acc" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,account);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            answer =  rs.getString("u_psw");
        }
        else{
            answer = null;
        }

        if(con!=null){
            con.close();
        }
        return answer;

    }

    public String getMerchantPsw(String account) throws SQLException {
        String answer;
        String sql= "select m_psw from  merchant "+ " where  m_acc" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,account);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            answer =  rs.getString("m_psw");
        }
        else{
            answer = null;
        }

        if(con!=null){
            con.close();
        }
        return answer;
    }

    public String getAdminPsw(String account) throws SQLException {

        String answer;
        String sql= "select ad_psw from  admin "+ " where  ad_acc" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1,account);
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            answer =  rs.getString("ad_psw");
        }
        else{
            answer = null;
        }

        if(con!=null){
            con.close();
        }
        return answer;

    }
    public void insert_cart(int u_id,int p_id,int quantity){//将商品表信息插入到购物车表中
        DataBase dataBase=new DataBase();
        dataBase.OpenDB();
        // 创建一个java.sql.Date对象，表示当前日期
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());
// 打印输出结果
        System.out.println(currentDate); // 例如：2023-04-21
        // 创建预编译语句对象
        try {
            // 定义插入语句，使用占位符代替具体的值
            String sql = "insert into cart (p_id, u_id, join_time, quantity) values (?, ?, ?, ?)";
            // 创建预编译语句对象
            PreparedStatement pstmt = dataBase.getCon().prepareStatement(sql);
            // 设置占位符的值，注意类型和顺序要与表结构一致
            pstmt.setInt(1, p_id);
            pstmt.setInt(2, u_id);
            pstmt.setDate(3,currentDate);
            pstmt.setInt(4, quantity);
            // 执行插入操作，返回影响的行数
            int rows = pstmt.executeUpdate();
            // 判断是否插入成功
            if (rows > 0) {
                System.out.println("插入成功！");
            } else {
                System.out.println("插入失败！");
            }
        } catch (Exception e) {
            // 捕获异常并打印
            e.printStackTrace();
        }
        }

    public static void main(String[] args) {
        DataControl dataControl= new DataControl();
        dataControl.insert_cart(1,1,1);
    }


}



