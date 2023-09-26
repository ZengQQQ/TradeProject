package cn.bjut.jdbc;
import java.sql.*;
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
}


