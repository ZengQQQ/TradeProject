package cn.bjut.jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public String getMerchantPsw(String account) throws SQLException {
        String answer;
        String sql = "select m_psw from  merchant " + " where  m_acc" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, account);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            answer = rs.getString("m_psw");
        } else {
            answer = null;
        }

        if (con != null) {
            con.close();
        }
        return answer;
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
    
}


