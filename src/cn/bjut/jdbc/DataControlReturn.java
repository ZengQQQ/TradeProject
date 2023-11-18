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
        String sql = "select * from return_detail";
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
        if(con!= null) {
            con.close();
        }
    }

}
