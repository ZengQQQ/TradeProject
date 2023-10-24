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

    public String getUserPsw(int u_id) throws SQLException {
        String answer;
        String sql = "select u_psw from user " + " where  u_id" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, u_id);
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

    public int getUserid(String account) throws SQLException {
        int id=0;
        String sql = "select u_id from user " + " where  u_acc" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, account);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            id = rs.getInt("u_id");
        } else {
            id = 0;
        }

        if (con != null) {
            con.close();
        }
        return id;

    }
    public String getUserName(int u_id) throws SQLException {
        String name=null;
        String sql = "select u_name from user " + " where  u_id" + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, u_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            name = rs.getString("u_name");
        } else {
            name = null;
        }

        if (con != null) {
            con.close();
        }
        return name;

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
    public boolean updateProduct(int p_id, String newName, String newdesc, String newclass, double newPrice, String newsta, String newimg) {
        // 创建连接
        Connection con = null;
        PreparedStatement preparedStatement;
        DataBase dataBase = new DataBase();
        try {
            // 建立数据库连接
            con = dataBase.OpenDB();
            // 创建SQL更新语句
            String sql = "UPDATE product SET p_name = ?, p_desc = ?, p_class = ?, p_price = ?, p_status = ?, p_img = ? WHERE p_id = ?";
            // 创建 PreparedStatement 对象
            preparedStatement = con.prepareStatement(sql);
            // 设置参数
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newdesc);
            preparedStatement.setString(3, newclass);
            preparedStatement.setDouble(4, newPrice);
            preparedStatement.setString(5, newsta);
            preparedStatement.setString(6, newimg);
            preparedStatement.setInt(7, p_id);

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

    //删除商品
    public boolean  deleteProduct(int productId) throws SQLException {

//        String sql = "select u_psw from user " + " where  u_acc" + " = ?";
//        Connection con = DataBase.OpenDB();
//        PreparedStatement stmt = con.prepareStatement(sql);

        return  true;
    }

    public static void main(String[] args) throws SQLException {
        DataControl dataControl = new DataControl();
        dataControl.insert_cart(1, 1, 1);
    }


    public boolean addProduct( int m_is,String productName, String productDesc, String productClass, double productPrice,String productstste,String productimg) {

        return true;
    }



//查找所有用户的信息，不用参数
    public List<User> selectUserTable() throws SQLException {
        String sql= "select *  from  user";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        List<User> userList = new ArrayList<User>();
        while (rs.next()) {
            String id = Integer.toString(rs.getInt("u_id"));
            String acc = rs.getString("u_acc");
            String psw = rs.getString("u_psw");
            String name = rs.getString("u_name");
            String sex = rs.getString("u_sex");
            String tele = rs.getString("u_tele");

            User user = new User(id, acc, psw, name, sex, tele);
            userList.add(user);
        }
        con.close();
        return userList;
    }

    public List<User> selectUserTable(String column_name,String new_value) throws SQLException {
        String sql = "select * from user where " + column_name + " = ?";
        Connection con = DataBase.OpenDB();

        if(column_name.equals("u_name")){
            sql = "select * from user where " + column_name + " like ?";
            new_value = "%" + new_value + "%";
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, new_value);
        ResultSet rs = stmt.executeQuery();
        List<User> userList = new ArrayList<User>();
        while(rs.next()){
            String id = Integer.toString(rs.getInt("u_id"));
            String acc = rs.getString("u_acc");
            String psw = rs.getString("u_psw");
            String name = rs.getString("u_name");
            String sex = rs.getString("u_sex");
            String tele = rs.getString("u_tele");

            User user = new User(id,acc,psw,name,sex,tele);
            userList.add(user);
        }
        con.close();
        return userList;
    }

    //修改Usertable中的某一项的内容，根据u_id 来修改，输入修改的列名与修改值,成功返回相应的字符串
    public String updateUserTable(int u_id, String column_name, String new_value) throws SQLException {
        String sql = "UPDATE user SET " + column_name + " = ? WHERE u_id = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);

        stmt.setString(1, new_value);
        stmt.setInt(2, u_id);
        int result = stmt.executeUpdate();
        con.close();
        if(result>0){
            return "修改成功";
        }
        else{
            return "修改失败";
        }
    }

    //使用u_id来修改usertable，全部更新,
    public String updateUserTable(int u_id, String new_u_acc, String new_u_psw, String new_u_name, String new_u_sex, String new_u_tele) throws SQLException {
        String sql = "UPDATE user SET u_acc = ?, u_psw = ?, u_name = ?, u_sex = ?, u_tele = ? WHERE u_id = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);

        stmt.setString(1, new_u_acc);
        stmt.setString(2, new_u_psw);
        stmt.setString(3, new_u_name);
        stmt.setString(4, new_u_sex);
        stmt.setString(5, new_u_tele);
        stmt.setInt(6, u_id);

        int result = stmt.executeUpdate();
        con.close();

        if(result > 0){
            return "修改成功";
        } else {
            return "修改失败";
        }
    }

    //根据u_id删除用户
public String deleteUserTable(int u_id) throws SQLException {
        String sql = "DELETE FROM user WHERE u_id = ?";
        Connection con = DataBase.OpenDB();

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, u_id);
        int result = stmt.executeUpdate();

        con.close();
    if(result > 0){
        return "删除成功";
    } else {
        return "删除失败";
    }
}

//插入一条新的用户信息，输入用户的所有信息，成功返回相应的字符串
public String insertUserTable(String new_u_acc, String new_u_psw, String new_u_name, String new_u_sex, String new_u_tele) throws SQLException {
    String sql = "INSERT INTO user (u_id, u_acc, u_psw, u_name, u_sex,  u_tele) VALUES (null, ?, ?, ?, ?, ?)";
    Connection con = DataBase.OpenDB();
    PreparedStatement stmt = con.prepareStatement(sql);
    stmt.setString(1, new_u_acc);
    stmt.setString(2, new_u_psw);
    stmt.setString(3, new_u_name);
    stmt.setString(4,new_u_sex);
    stmt.setString(5, new_u_tele);

    int result = stmt.executeUpdate();
    con.close();
    if(result > 0){
        return "添加成功";
    } else {
        return "添加失败";
    }

    }

}



