package cn.bjut.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;


public class DataControl {


    public ResultSet executeQuery(String table_name, String colum_name, String x) throws SQLException {

        String sql = "select * from " + table_name + " where " + colum_name + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, x);
        ResultSet rs = stmt.executeQuery();
        return rs;

    }

    public String getUserPsw(String account) throws SQLException {
        String answer;
        String sql = "select u_psw from user " + " where  u_acc" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, account);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            answer = rs.getString("u_psw");
        } else {
            answer = null;
        }

        if (con != null) {
            con.close();
        }
        return answer;

    }

    public List<Integer> getMerchantPsw(String account) throws SQLException {
        List<Integer> psw_id = new ArrayList<>();
        String sql = "select m_psw,m_id from  merchant " + " where  m_acc" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, account);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            psw_id.add(Integer.valueOf(rs.getString("m_psw")));
            psw_id.add(rs.getInt("m_id"));
        }
        if (con != null) {
            con.close();
        }
        return psw_id;
    }

    public String getAdminPsw(String account) throws SQLException {

        String answer;
        String sql = "select ad_psw from  admin " + " where  ad_acc" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, account);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            answer = rs.getString("ad_psw");
        } else {
            answer = null;
        }

        if (con != null) {
            con.close();
        }
        return answer;

    }


    public List<Product> MerchantProductQuery(int m_id) throws SQLException {//获得指定商家的所有商品
        List<Product> products = new ArrayList<>();
        Connection con = DataBase.OpenDB();
        String sql = "SELECT p_id, p_name, p_desc, p_class, p_price, p_status, p_img " +
                "FROM product " + "WHERE m_id = ?";
        PreparedStatement stmt = null;
        if (con != null) {
            stmt = con.prepareStatement(sql);
        }
        if (stmt != null) {
            stmt.setInt(1, m_id);
        }
        ResultSet rs = null;
        if (stmt != null) {
            rs = stmt.executeQuery();
        }
        if (rs != null) {
            while (rs.next()) {
                Product product = new Product();
                product.setP_id(rs.getInt("p_id"));
                product.setP_name(rs.getString("p_name"));
                product.setP_desc(rs.getString("p_desc"));
                product.setP_class(rs.getString("p_class"));
                product.setP_price(rs.getString("p_price"));
                product.setP_status(rs.getString("p_status"));
                product.setP_img(rs.getString("p_img"));
                products.add(product);
            }
        }
        return products;
    }


    public void insert_cart(int u_id, int p_id, int quantity) {//将商品表信息插入到购物车表中
        DataBase dataBase = new DataBase();
        dataBase.OpenDB();
        // 创建一个LocalDateTime对象，表示当前日期和时间
        LocalDateTime dateTime = LocalDateTime.now();
        // 创建预编译语句对象
        try {
            // 定义插入语句，使用占位符代替具体的值
            String sql = "insert into cart (p_id, u_id, join_time, quantity) values (?, ?, ?, ?)";
            // 创建预编译语句对象
            PreparedStatement pstmt = dataBase.getCon().prepareStatement(sql);
            // 设置占位符的值，注意类型和顺序要与表结构一致
            pstmt.setInt(1, p_id);
            pstmt.setInt(2, u_id);
            pstmt.setObject(3, dateTime);
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


    //在商家页面里修改商品信息
    public boolean updateProduct(int m_id, String newName, String newdesc, String newclass, double newPrice, String newsta, String newimg) {
        // 创建连接
        Connection con = null;
        PreparedStatement preparedStatement = null;
        DataBase dataBase = new DataBase();
        try {
            // 建立数据库连接
            con = dataBase.OpenDB();
            // 创建SQL更新语句
            String sql = "UPDATE product SET p_name = ?, p_desc = ?, p_class = ?, p_price = ?, p_status = ?, p_img = ? WHERE m_id = ?";
            // 创建 PreparedStatement 对象
            preparedStatement = con.prepareStatement(sql);
            // 设置参数
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newdesc);
            preparedStatement.setString(3, newclass);
            preparedStatement.setDouble(4, newPrice);
            preparedStatement.setString(5, newsta);
            preparedStatement.setString(6, newimg);
            preparedStatement.setInt(7, m_id);

            // 执行更新
            int rowsAffected = preparedStatement.executeUpdate();
            // 如果更新成功，rowsAffected 应该为 1
            return rowsAffected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接
            try {
                if (con != null) {
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static void main(String[] args) {
        DataControl dataControl = new DataControl();
        dataControl.insert_cart(1, 1, 1);
    }


}



