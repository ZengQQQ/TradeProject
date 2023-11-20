package cn.bjut.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataControlReturn {

    public List<ReturnRequest> getReturnRequests() throws SQLException {
        Connection con = DataBase.OpenDB();
        String sql = "select r.r_id as r_id,request_time ,r.reason as reason,r.status as status,u.u_name as u_name," +
                "  p.p_name,r.o_id as o_id from return_detail r" +
                " join orders o on r.o_id = o.o_id" +
                " join product p on o.p_id = p.p_id" +
                " join user u on o.u_id = u.u_id" +
                " where r.status = '待审核' ";
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        List<ReturnRequest> list = new ArrayList<ReturnRequest>();
        while (rs.next()) {
            ReturnRequest returnRequest = new ReturnRequest();
            returnRequest.setR_id(rs.getInt("r_id"));
            returnRequest.setO_id(rs.getInt("o_id"));
            returnRequest.setRequest_time(rs.getString("request_time"));
            returnRequest.setReason(rs.getString("reason"));
            returnRequest.setStatus(rs.getString("status"));
            returnRequest.setU_name(rs.getString("u_name"));
            returnRequest.setP_name(rs.getString("p_name"));
            list.add(returnRequest);
        }
        if(con!= null) {
            con.close();
        }
        return list;
    }

    public void processReturnRequest(int returnRequestId, String action) throws SQLException {
        Connection con = DataBase.OpenDB();
        String sql = "update return_detail set status = ? where r_id = ?";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, action);
        stmt.setInt(2, returnRequestId);
        stmt.executeUpdate();
        if(action.equals("审核通过")){
            String sql1 = "update orders set o_status = '待退货' where o_id = (select o_id from return_detail where r_id = ?)";
            PreparedStatement stmt1 = con.prepareStatement(sql1);
            stmt1.setInt(1, returnRequestId);
            stmt1.executeUpdate();
        }else if(action.equals("审核不通过")){
            String sql2 = "update orders set o_status = '已完成' where o_id = (select o_id from return_detail where r_id = ?)";
            PreparedStatement stmt1 = con.prepareStatement(sql2);
            stmt1.setInt(1, returnRequestId);
            stmt1.executeUpdate();
        }
        if(con!= null) {
            con.close();
        }
    }

}
