package cn.bjut.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataControlMercahnt extends DataControl{

    public DataControlMercahnt() throws SQLException {
        super();
    }

    //根据账户名称获得商家密码
    public String getMerchantPsw(String account) throws SQLException {
        String psw = null;
        String sql = "select m_psw from  merchant " + " where  m_acc" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, account);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            psw=(rs.getString("m_psw"));
        }
        if (con != null) {
            con.close();
        }
        return psw;
    }
    //根据账户名称获得商家m_id
    public int getMerchantm_id(String account) throws SQLException {
        int m_id=0;
        String sql = "select m_id from  merchant " + " where  m_acc" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, account);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            m_id=rs.getInt("m_id");
        }
        if (con != null) {
            con.close();
        }
        return m_id;
    }

    public String getMerchantm_name(int m_id) throws SQLException {
        String m_name=null;
        String sql = "select m_name from  merchant " + " where  m_id" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, m_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            m_name=rs.getString("m_name");
        }
        if (con != null) {
            con.close();
        }
        return m_name;
    }



    //获得指定商家的所有商品
    public List<Product> MerchantProductQuery(int m_id) throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection con = DataBase.OpenDB();
        String sql = "SELECT p_id, p_name, p_desc, p_class, p_price, p_status,p_quantity, p_img,p_auditStatus " +
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
                product.setP_quantity(rs.getInt("p_quantity"));
                product.setP_audiStatus(rs.getString("p_auditStatus"));
                product.setP_img(rs.getString("p_img"));
                products.add(product);
            }
        }
        return products;
    }

    //获得指定商家的所有信息
    public Merchant MerchantQuery(int m_id) throws SQLException {
        Merchant merchant = new Merchant();
        Connection con = DataBase.OpenDB();
        String sql = "SELECT  m_acc,m_psw, m_name, m_sex,m_tele " +
                "FROM merchant " + "WHERE m_id = ?";
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
                merchant.setAcc(rs.getString("m_acc"));
                merchant.setPsw(rs.getString("m_psw"));
                merchant.setM_name(rs.getString("m_name"));
                merchant.setM_sex(rs.getString("m_sex"));
                merchant.setM_tele(rs.getString("m_tele"));
            }
        }
        return merchant;
    }
    //使用m_id来修改merchanttable，全部更新,
    public String updateMerchant(int m_id,String new_m_name, String new_m_sex, String new_m_tele, String new_m_psw) throws SQLException {
        String sql = "UPDATE merchant SET m_name = ?, m_sex = ?, m_tele = ?,m_psw WHERE m_id = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, new_m_name);
        stmt.setString(2, new_m_sex);
        stmt.setString(3, new_m_tele);
        stmt.setString(4, new_m_psw);
        stmt.setInt(5, m_id);
        int result = stmt.executeUpdate();
        con.close();
        if (result > 0) {
            return "修改成功";
        } else {
            return "修改失败";
        }
    }
}
