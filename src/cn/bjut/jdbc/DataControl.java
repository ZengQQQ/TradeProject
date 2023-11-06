package cn.bjut.jdbc;

import javax.swing.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.time.LocalDateTime;


public class DataControl {


    public DataControl() throws SQLException {
    }

    public ResultSet executeQuery(String table_name, String colum_name, String x) throws SQLException {

        String sql = "select * from " + table_name + " where " + colum_name + " = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, x);
        ResultSet rs = stmt.executeQuery();
        return rs;

    }

    public int find_p_id(int u_id, String join_time) throws SQLException {
        int p_id = 0;
        String sql = "select p_id from cart " + " WHERE u_id=" + u_id + " AND join_time='" + join_time + "'";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            p_id = rs.getInt("p_id");
        } else {
            p_id = 0;
        }
        if (con != null) {
            con.close();
        }
        return p_id;
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

    public void insertOrUpdateCart(int u_id, int p_id) {
        DataBase dataBase = new DataBase();
        dataBase.OpenDB();
        LocalDateTime currentDateTime = LocalDateTime.now();

        try {
            // 查询购物车中是否已存在该商品
            String query = "SELECT * FROM cart WHERE p_id=? AND u_id=?";
            PreparedStatement pstmt = dataBase.getCon().prepareStatement(query);
            pstmt.setInt(1, p_id);
            pstmt.setInt(2, u_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // 商品已存在，执行更新操作
                int quantity = rs.getInt("quantity") + 1;
                String updateQuery = "UPDATE cart SET quantity=?, join_time=? WHERE p_id=? AND u_id=?";
                PreparedStatement updateStmt = dataBase.getCon().prepareStatement(updateQuery);
                updateStmt.setInt(1, quantity);
                updateStmt.setObject(2, currentDateTime);
                updateStmt.setInt(3, p_id);
                updateStmt.setInt(4, u_id);
                int rowsUpdated = updateStmt.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("更新成功！");
                } else {
                    System.out.println("更新失败！");
                }
            } else {
                // 商品不存在，执行插入操作
                String insertQuery = "INSERT INTO cart (p_id, u_id, join_time, quantity) VALUES (?, ?, ?, 1)";
                PreparedStatement insertStmt = dataBase.getCon().prepareStatement(insertQuery);
                insertStmt.setInt(1, p_id);
                insertStmt.setInt(2, u_id);
                insertStmt.setObject(3, currentDateTime);
                int rowsInserted = insertStmt.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("插入成功！");
                } else {
                    System.out.println("插入失败！");
                }
            }

            // 关闭资源
            rs.close();
            pstmt.close();
            dataBase.getCon().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void insertOrderFromCart(int u_id, int p_id) {
        DataBase dataBase = new DataBase();
        dataBase.OpenDB();
        LocalDateTime currentDateTime = LocalDateTime.now();

        try {
            // 查询购物车中是否存在该商品
            String query = "SELECT * FROM cart WHERE p_id=? AND u_id=?";
            PreparedStatement pstmt = dataBase.getCon().prepareStatement(query);
            pstmt.setInt(1, p_id);
            pstmt.setInt(2, u_id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int quantity = rs.getInt("quantity");

                // 将购物车内容插入订单表
                String insertQuery = "INSERT INTO orders (p_id, u_id, buy_time, quantity) VALUES (?, ?, ?, ?)";
                PreparedStatement insertStmt = dataBase.getCon().prepareStatement(insertQuery);
                insertStmt.setInt(1, p_id);
                insertStmt.setInt(2, u_id);
                insertStmt.setObject(3, currentDateTime);
                insertStmt.setInt(4, quantity);
                int rowsInserted = insertStmt.executeUpdate();

                if (rowsInserted > 0) {
                    System.out.println("插入成功！");
                } else {
                    System.out.println("插入失败！");
                }
            } else {
                System.out.println("购物车中不存在该商品！");
            }

            // 关闭资源
            rs.close();
            pstmt.close();
            dataBase.getCon().close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getUserid(String account) throws SQLException {
        int id = 0;
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
        String name = null;
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


    //获得指定商家的所有商品
    public List<Product> MerchantProductQuery(int m_id) throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection con = DataBase.OpenDB();
        String sql = "SELECT p_id, p_name, p_desc, p_class, p_price, p_status,p_quantity, p_img " +
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

    //在商家页面里修改商品信息
    public boolean updateProduct(int p_id, String newName, String newdesc, String newclass, double newPrice, String newsta, int newquantity, String newimg) {
        // 创建连接
        Connection con = null;
        PreparedStatement preparedStatement;
        DataBase dataBase = new DataBase();
        try {
            // 建立数据库连接
            con = dataBase.OpenDB();
            // 创建SQL更新语句
            String sql = "UPDATE product SET p_name = ?, p_desc = ?, p_class = ?, p_price = ?, p_status = ?,p_quantity = ?, p_img = ? WHERE p_id = ?";
            // 创建 PreparedStatement 对象
            preparedStatement = con.prepareStatement(sql);
            // 设置参数
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newdesc);
            preparedStatement.setString(3, newclass);
            preparedStatement.setDouble(4, newPrice);
            preparedStatement.setString(5, newsta);
            preparedStatement.setInt(6, newquantity);
            preparedStatement.setString(7, newimg);
            preparedStatement.setInt(8, p_id);
            // 执行更新
            int rowsAffected = preparedStatement.executeUpdate();
            // 如果更新成功，rowsAffected 应该为 1
            return rowsAffected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //删除商品
    public boolean deleteProduct(int productId) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        boolean deleted = false;
        try {
            // 创建连接
            DataBase dataBase = new DataBase();
            con = dataBase.OpenDB();
            // 创建SQL删除语句
            String deleteQuery = "DELETE FROM product WHERE p_id = ?";
            // 准备并执行SQL语句
            preparedStatement = con.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, productId);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // 如果成功删除了行，则返回true
                deleted = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleted;
    }

    //添加商品
    public boolean addProduct(int m_id, String productName, String productDesc, String productClass, double productPrice, String productState, int productQuantity, String productImg) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        boolean added = false;
        try {
            // 创建连接
            DataBase dataBase = new DataBase();
            con = dataBase.OpenDB();
            // 创建SQL插入语句
            String insertQuery = "INSERT INTO product (m_id, p_name, p_desc, p_class, p_price, p_status, p_quantity, p_img) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            // 准备并执行SQL语句
            preparedStatement = con.prepareStatement(insertQuery);
            preparedStatement.setInt(1, m_id);
            preparedStatement.setString(2, productName);
            preparedStatement.setString(3, productDesc);
            preparedStatement.setString(4, productClass);
            preparedStatement.setDouble(5, productPrice);
            preparedStatement.setString(6, productState);
            preparedStatement.setInt(7, productQuantity);
            preparedStatement.setString(8, productImg);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // 如果成功插入了行，则返回true
                added = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return added;
    }

    //将商品表信息插入到购物车表中
    public void insert_cart(int u_id, int p_id, int quantity) {
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


    //查找所有用户的信息，不用参数
    public List<User> selectUserTable() throws SQLException {
        String sql = "select *  from  user";
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

    public List<User> selectUserTable(String column_name, String new_value) throws SQLException {
        String sql = "select * from user where " + column_name + " = ?";
        Connection con = DataBase.OpenDB();

        if (column_name.equals("u_name")) {
            sql = "select * from user where " + column_name + " like ?";
            new_value = "%" + new_value + "%";
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, new_value);
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

    //修改Usertable中的某一项的内容，根据u_id 来修改，输入修改的列名与修改值,成功返回相应的字符串
    public String updateUserTable(int u_id, String column_name, String new_value) throws SQLException {
        String sql = "UPDATE user SET " + column_name + " = ? WHERE u_id = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);

        stmt.setString(1, new_value);
        stmt.setInt(2, u_id);
        int result = stmt.executeUpdate();
        con.close();
        if (result > 0) {
            return "修改成功";
        } else {
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

        if (result > 0) {
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
        if (result > 0) {
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
        stmt.setString(4, new_u_sex);
        stmt.setString(5, new_u_tele);

        int result = stmt.executeUpdate();
        con.close();
        if (result > 0) {
            return "添加成功";
        } else {
            return "添加失败";
        }

    }


    //查找所有商家的信息，不用参数
    public List<Merchant> selectMerchantTable() throws SQLException {
        String sql = "select *  from  merchant";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);

        ResultSet rs = stmt.executeQuery();
        List<Merchant> merchantList = new ArrayList<>();
        while (rs.next()) {
            String id = Integer.toString(rs.getInt("m_id"));
            String acc = rs.getString("m_acc");
            String psw = rs.getString("m_psw");
            String name = rs.getString("m_name");
            String sex = rs.getString("m_sex");
            String tele = rs.getString("m_tele");

            Merchant mer = new Merchant(id, acc, psw, name, sex, tele);
            merchantList.add(mer);
        }
        con.close();
        return merchantList;
    }

    public List<Merchant> selectMerTable(String column_name, String new_value) throws SQLException {
        String sql = "select * from merchant where " + column_name + " = ?";
        Connection con = DataBase.OpenDB();
        if (column_name.equals("m_name")) {
            sql = "select * from merchant where " + column_name + " like ?";
            new_value = "%" + new_value + "%";
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, new_value);
        ResultSet rs = stmt.executeQuery();
        List<Merchant> merList = new ArrayList<>();
        while (rs.next()) {
            String id = Integer.toString(rs.getInt("m_id"));
            String acc = rs.getString("m_acc");
            String psw = rs.getString("m_psw");
            String name = rs.getString("m_name");
            String sex = rs.getString("m_sex");
            String tele = rs.getString("m_tele");
            Merchant mer = new Merchant(id, acc, psw, name, sex, tele);
            merList.add(mer);
        }
        con.close();
        return merList;
    }

    //修改Merchanttable中的某一项的内容，根据m_id 来修改，输入修改的列名与修改值,成功返回相应的字符串
    public String updateMerTable(int m_id, String column_name, String new_value) throws SQLException {
        String sql = "UPDATE merchant SET " + column_name + " = ? WHERE m_id = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);

        stmt.setString(1, new_value);
        stmt.setInt(2, m_id);
        int result = stmt.executeUpdate();
        con.close();
        if (result > 0) {
            return "修改成功";
        } else {
            return "修改失败";
        }
    }

    //使用m_id来修改merchanttable，全部更新,
    public String updateMerTable(int m_id, String new_m_acc, String new_m_psw, String new_m_name, String new_m_sex, String new_m_tele) throws SQLException {
        String sql = "UPDATE merchant SET m_acc = ?, m_psw = ?, m_name = ?, m_sex = ?, m_tele = ? WHERE m_id = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, new_m_acc);
        stmt.setString(2, new_m_psw);
        stmt.setString(3, new_m_name);
        stmt.setString(4, new_m_sex);
        stmt.setString(5, new_m_tele);
        stmt.setInt(6, m_id);
        int result = stmt.executeUpdate();
        con.close();
        if (result > 0) {
            return "修改成功";
        } else {
            return "修改失败";
        }
    }

    //根据m_id删除用户
    public String deleteMerTable(int m_id) throws SQLException {
        String sql = "DELETE FROM merchant WHERE m_id = ?";
        Connection con = DataBase.OpenDB();

        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, m_id);
        int result = stmt.executeUpdate();
        con.close();
        if (result > 0) {
            return "删除成功";
        } else {
            return "删除失败";
        }
    }

    //插入一条新的商家信息，输入商家的所有信息，成功返回相应的字符串
    public String insertMerTable(String new_m_acc, String new_m_psw, String new_m_name, String new_m_sex, String new_m_tele) throws SQLException {
        String sql = "INSERT INTO merchant (m_id, m_acc, m_psw, m_name, m_sex,  m_tele) VALUES (null, ?, ?, ?, ?, ?)";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setString(1, new_m_acc);
        stmt.setString(2, new_m_psw);
        stmt.setString(3, new_m_name);
        stmt.setString(4, new_m_sex);
        stmt.setString(5, new_m_tele);

        int result = stmt.executeUpdate();
        con.close();
        if (result > 0) {
            return "添加成功";
        } else {
            return "添加失败";
        }

    }

    public static void main(String[] args) throws SQLException {
        DataControl dataControl = new DataControl();
        dataControl.insert_cart(1, 1, 1);
        dataControl.selectReplyTable("1");
    }

    //查找某一商家的信息，m-id
    public Merchant selectMerchant(int m_id) throws SQLException {
        String sql = "select * from merchant where m_id = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, m_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String id = Integer.toString(rs.getInt("m_id"));
            String acc = rs.getString("m_acc");
            String psw = rs.getString("m_psw");
            String name = rs.getString("m_name");
            String sex = rs.getString("m_sex");
            String tele = rs.getString("m_tele");
            return new Merchant(id, acc, psw, name, sex, tele);
        }
        return null;
    }

    //根据当前的评论的ID，查找该评论的回复
    public List<Comment> selectReplyTable(String ID) throws SQLException {
        String sql = "SELECT f.f_id, " +
                "     COALESCE(u.u_name, m.m_name) as author_name, " +
                "      f.f_time, f.f_con,f.flag  FROM forum f " +
                "  LEFT JOIN user u ON f.u_id = u.u_id  " +
                "  LEFT JOIN merchant m ON f.m_id = m.m_id  " +
                "  WHERE f.reply_to = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(ID));
        ResultSet rs = stmt.executeQuery();
        List<Comment> replyList = new ArrayList<Comment>();
        while (rs.next()) {
            String author_name = rs.getString("author_name");
            String f_time = rs.getString("f_time");
            String f_con = rs.getString("f_con");
            String flag = rs.getString("flag");
            Comment comment = new Comment(author_name, f_time, f_con, flag);
        }
        return replyList;
    }

    //查找评论表，有回复id的不用返回，返回论坛发表的评论
    public List<CommentBar> selectForumList() throws SQLException {
        String sql = "SELECT f.f_id, " +
                "       COALESCE(u.u_name, m.m_name) as author_name, " +
                "       f.f_time, " +
                "       f.f_con,  " +
                "       f.flag  " +
                " FROM forum f" +
                " LEFT JOIN user u ON f.u_id = u.u_id  " +
                " LEFT JOIN merchant m ON f.m_id = m.m_id  " +
                " WHERE f.reply_to IS NULL  ";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        List<CommentBar> commentBarList = new ArrayList<CommentBar>();
        while (rs.next()) {
            String id = Integer.toString(rs.getInt("f_id"));
            String author_name = rs.getString("author_name");
            String f_time = rs.getString("f_time");
            String f_con = rs.getString("f_con");
            String flag = rs.getString("flag");
            CommentBar commentBar = new CommentBar(id, author_name, f_time, f_con, flag);
        }

        return commentBarList;
    }

    //根据选项搜索商品信息
    public List<Product> searchProducts(int m_id, String searchType, String searchValue) throws SQLException {
        List<Product> productList = new ArrayList<>();
        Connection con = DataBase.OpenDB();
        String sql = "SELECT * FROM product WHERE m_id = ?";

        if (searchType != null && !searchType.isEmpty() && searchValue != null && !searchValue.isEmpty()) {
            switch (searchType) {
                case "商品名称":
                    sql += " AND p_name LIKE ?";
                    break;
                case "类别":
                    sql += " AND p_class LIKE ?";
                    break;
                case "价格":
                    double priceValue = Double.parseDouble(searchValue);
                    double rounded = Math.round(priceValue * 10) / 10.0;
                    if (rounded == priceValue) {
                        sql += " AND CAST(p_price AS DECIMAL(10, 1)) = ?";
                    } else {
                        sql += " AND CAST(p_price AS DECIMAL(10, 2)) = ?";
                    }
                    break;
                case "状态":
                    sql += " AND p_status LIKE ?";
                    break;
                case "数量":
                    sql += " AND p_quantity = ?";
                    break;
                default:
                    break;
            }
        }
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, m_id);

        if (searchType != null && !searchType.isEmpty() && searchValue != null && !searchValue.isEmpty()) {
            int parameterIndex = 2;

            if (searchType.equals("数量")) {
                stmt.setInt(parameterIndex, Integer.parseInt(searchValue));
            } else if (searchType.equals("价格")) {
                double price = Double.parseDouble(searchValue);
                stmt.setDouble(parameterIndex, price);
            } else {
                stmt.setString(parameterIndex, "%" + searchValue + "%");
            }
        }

        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            do {
                Product product = new Product();
                product.setP_id(rs.getInt("p_id"));
                product.setP_name(rs.getString("p_name"));
                product.setP_desc(rs.getString("p_desc"));
                product.setP_class(rs.getString("p_class"));
                product.setP_price(String.valueOf(rs.getDouble("p_price")));
                product.setP_status(rs.getString("p_status"));
                product.setP_img(rs.getString("p_img"));
                product.setP_quantity(rs.getInt("p_quantity"));
                productList.add(product);
            } while (rs.next());
            rs.close();
            stmt.close();
            con.close();
            return productList;
        } else {
            rs.close();
            stmt.close();
            if (searchType.equals("价格") && productList.isEmpty()) {
                JOptionPane.showMessageDialog(null, "商品结果没有找到，给您价格最接近的5个商品", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
                // Perform a secondary query to find the closest products based on the specified price value
                List<Product> closestProducts = findClosestProducts(searchValue, m_id);
                return closestProducts;
            }
            return productList;
        }
    }

    private List<Product> findClosestProducts(String searchValue, int m_id) {
        List<Product> closestProducts = new ArrayList<>();
        Connection con = DataBase.OpenDB();

        try {
            String sql = "SELECT * FROM product WHERE m_id = ? ORDER BY ABS(p_price - ?) LIMIT 5";

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, m_id);

            double priceValue = Double.parseDouble(searchValue);
            stmt.setDouble(2, priceValue);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product product = new Product();
                product.setP_id(rs.getInt("p_id"));
                product.setP_name(rs.getString("p_name"));
                product.setP_desc(rs.getString("p_desc"));
                product.setP_class(rs.getString("p_class"));
                product.setP_price(String.valueOf(rs.getDouble("p_price")));
                product.setP_status(rs.getString("p_status"));
                product.setP_img(rs.getString("p_img"));
                product.setP_quantity(rs.getInt("p_quantity"));
                closestProducts.add(product);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return closestProducts;
    }


    //根据m_id查找订单有关的用户和商品
    public List<Order> getOrderInfoByM_id(int m_id) throws SQLException {
        List<Order> orderInfoList = new ArrayList<>();

        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // 编写SQL查询语句，根据m_id查找订单、用户和商品信息
            String sql = "SELECT o.p_id, o.u_id, o.buy_time, o.quantity,o.totalprice, " +
                    "p.p_name, p.p_desc, p.p_class, p.p_price, p.p_status, p.p_quantity, p.p_img, " +
                    "u.u_acc, u.u_name, u.u_sex, u.u_tele " +
                    "FROM orders o " +
                    "INNER JOIN product p ON o.p_id = p.p_id AND o.m_id = p.m_id " +
                    "INNER JOIN user u ON o.u_id = u.u_id " +
                    "WHERE o.m_id = ?";
            // 创建PreparedStatement对象，设置参数并执行查询
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, m_id);
            rs = stmt.executeQuery();

            // 处理查询结果
            while (rs.next()) {
                Order orderInfo = new Order();
                orderInfo.setP_id(rs.getInt("p_id"));
                orderInfo.setU_id(rs.getInt("u_id"));
                orderInfo.setBuytime(rs.getString("buy_time"));
                orderInfo.setQuantity(rs.getInt("quantity"));
                orderInfo.setTotalprice(String.valueOf(rs.getDouble("totalprice")));

                Product product = new Product();
                product.setP_name(rs.getString("p_name"));
                product.setP_desc(rs.getString("p_desc"));
                product.setP_class(rs.getString("p_class"));
                product.setP_price(String.valueOf(rs.getDouble("p_price")));
                product.setP_status(rs.getString("p_status"));
                product.setP_quantity(rs.getInt("p_quantity"));
                product.setP_img(rs.getString("p_img"));

                User user = new User();
                user.setID(String.valueOf(rs.getInt("u_id")));
                user.setAcc(rs.getString("u_acc"));
                user.setU_name(rs.getString("u_name"));
                user.setU_sex(rs.getString("u_sex"));
                user.setU_tele(rs.getString("u_tele"));

                orderInfo.setProduct(product);
                orderInfo.setUser(user);

                orderInfoList.add(orderInfo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭数据库连接和资源
            rs.close();
            stmt.close();
            con.close();
        }
        return orderInfoList;
    }

    //根据一定的信息查找订单
    public List<Order> searchOrders(int m_id, String productType, String userType,String dateType, String productf, String userf, String quantityf, String totalpricef, String datef) throws SQLException {
        List<Order> orderList = new ArrayList<>();
        Connection con = DataBase.OpenDB();

        try {
            String sql = "SELECT o.p_id, o.u_id, o.buy_time, o.quantity,o.totalprice, p.p_name, p.p_desc, p.p_class, p.p_price, u.u_name, u.u_sex, u.u_tele FROM orders o ";
            String joinProduct = "INNER JOIN product p ON o.p_id = p.p_id ";
            String joinUser = "INNER JOIN user u ON o.u_id = u.u_id ";
            String whereClause = "WHERE o.m_id = ? ";
            List<Object> parameters = new ArrayList<>();
            parameters.add(m_id);

            if (productf != null && !productf.isEmpty() && !productType.isEmpty()) {
                switch (productType) {
                    case "商品名称":
                        whereClause += "AND p.p_name LIKE ? ";
                        parameters.add("%" + productf + "%");
                        break;
                    case "类别":
                        whereClause += "AND p.p_class LIKE ? ";
                        parameters.add("%" + productf + "%");
                        break;
                    case "价格":
                        double rounded = Math.round(Double.parseDouble(productf) * 10) / 10.0;
                        if (rounded == Double.parseDouble(productf)) {
                            whereClause += "AND CAST(p.p_price AS DECIMAL(10, 1)) = ? ";
                        } else {
                            whereClause += "AND CAST(p.p_price AS DECIMAL(10, 2)) = ? ";
                        }
                        parameters.add(Double.parseDouble(productf));
                        break;
                    case "数量":
                        whereClause += "AND o.quantity = ? ";
                        parameters.add(Integer.parseInt(productf));
                        break;
                    default:
                        break;
                }
            }

            if (userType != null && !userType.isEmpty()) {
                switch (userType) {
                    case "用户名":
                        whereClause += "AND u.u_name LIKE ? ";
                        parameters.add("%" + userf + "%");
                        break;
                    case "性别":
                        whereClause += "AND u.u_sex LIKE ? ";
                        parameters.add("%" + userf + "%");
                        break;
                    case "电话":
                        whereClause += "AND u.u_tele LIKE ? ";
                        parameters.add("%" + userf + "%");
                        break;
                    default:
                        break;
                }
            }

            if (quantityf != null && !quantityf.isEmpty()) {
                whereClause += "AND o.quantity = ? ";
                parameters.add(Integer.parseInt(quantityf));
            }

            if (totalpricef != null && !totalpricef.isEmpty()) {
                whereClause += "AND o.totalprice = ? ";
                parameters.add(Double.parseDouble(totalpricef));
            }

            if (datef != null && !datef.isEmpty()) {
                switch (dateType) {
                    case "日期":
                        // Check if the input is a valid date (e.g., "2023.2.26")
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.M.d");
                        try {
                            Date inputDate = dateFormat.parse(datef);
                            String startDate = new SimpleDateFormat("yyyy-MM-dd").format(inputDate);
                            String endDate = new SimpleDateFormat("yyyy-MM-dd").format(inputDate) + " 23:59:59";

                            whereClause += "AND o.buy_time >= ? AND o.buy_time <= ? ";
                            parameters.add(startDate);
                            parameters.add(endDate);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        break;
                    case "年":
                        // Check if the input is a valid year (e.g., "2023")
                        try {
                            int year = Integer.parseInt(datef);
                            whereClause += "AND YEAR(o.buy_time) = ? ";
                            parameters.add(year);
                        } catch (NumberFormatException e) {
                            // Handle invalid year format
                        }
                        break;
                    case "月":
                        // Check if the input is a valid month (e.g., "2")
                        try {
                            int month = Integer.parseInt(datef);
                            whereClause += "AND MONTH(o.buy_time) = ? ";
                            parameters.add(month);
                        } catch (NumberFormatException e) {
                            // Handle invalid month format
                        }
                        break;
                    case "日":
                        // Check if the input is a valid day (e.g., "5")
                        try {
                            int day = Integer.parseInt(datef);
                            whereClause += "AND DAY(o.buy_time) = ? ";
                            parameters.add(day);
                        } catch (NumberFormatException e) {
                            // Handle invalid day format
                        }
                        break;
                    default:
                        break;
                }
            }


            sql += joinProduct + joinUser + whereClause;

            PreparedStatement stmt = con.prepareStatement(sql);
            for (int i = 0; i < parameters.size(); i++) {
                stmt.setObject(i + 1, parameters.get(i));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setP_id(rs.getInt("p_id"));
                order.setU_id(rs.getInt("u_id"));
                order.setBuytime(rs.getString("buy_time"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotalprice(String.valueOf(rs.getDouble("totalprice")));

                Product product = new Product();
                product.setP_name(rs.getString("p_name"));
                product.setP_desc(rs.getString("p_desc"));
                product.setP_class(rs.getString("p_class"));
                product.setP_price(rs.getString("p_price"));

                User user = new User();
                user.setU_name(rs.getString("u_name"));
                user.setU_sex(rs.getString("u_sex"));
                user.setU_tele(rs.getString("u_tele"));

                order.setProduct(product);
                order.setUser(user);

                orderList.add(order);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (orderList.isEmpty() && (userf != null && !userf.isEmpty() || quantityf != null && !quantityf.isEmpty() || datef != null && !datef.isEmpty())) {
            JOptionPane.showMessageDialog(null, "订单结果没有找到", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
            return null;
        } else if (orderList.isEmpty() && (totalpricef != null && !totalpricef.isEmpty() || (productType.equals("价格") && (productf != null && !productf.isEmpty())))) {
            // Perform a secondary query to find the closest orders based on the specified criteria
            JOptionPane.showMessageDialog(null, "订单结果没有找到，给您价格最接近的3个订单", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
            List<Order> closestOrders = findClosestOrders(productf, totalpricef, m_id);
            return closestOrders;
        }

        return orderList;
    }

    private List<Order> findClosestOrders(String productPrice, String totalPrice, int m_id) throws SQLException {
        List<Order> closestOrders = new ArrayList<>();
        Connection con = DataBase.OpenDB();

        try {
            // Construct the SQL query based on the available criteria
            String sql = "SELECT o.p_id, o.u_id, o.buy_time, o.quantity, o.totalprice, p.p_name, p.p_desc, p.p_class, p.p_price, u.u_name, u.u_sex, u.u_tele " +
                    "FROM orders o " +
                    "INNER JOIN product p ON o.p_id = p.p_id " +
                    "INNER JOIN user u ON o.u_id = u.u_id " +
                    "WHERE o.m_id = ? ";

            if (totalPrice != null && !totalPrice.isEmpty()) {
                try {
//                    Double totalPrice2  = Double.parseDouble(totalPrice);
//                    double rounded = Math.round(totalPrice2 * 10) / 10.0;
//                    if (rounded == totalPrice2) {
//                        sql += "AND CAST(o.totalprice AS DECIMAL(10, 1)) = ? ";
//                    } else {
//                        sql += "AND CAST(o.totalprice AS DECIMAL(10, 2)) = ? ";
//                    }
                    sql += "ORDER BY ABS(o.totalprice - ?) LIMIT 3";
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Invalid totalprice format.");
                }
            } else if (productPrice != null && !productPrice.isEmpty()) {
                Double productPrice2 = Double.parseDouble(productPrice);
                try {
//                    double rounded = Math.round(productPrice2 * 10) / 10.0;
//                    if (rounded == productPrice2) {
//                        sql += "AND CAST(p.p_price AS DECIMAL(10, 1)) = ? ";
//                    } else {
//                        sql += "AND CAST(p.p_price AS DECIMAL(10, 2)) = ? ";
//                    }
                    sql += "ORDER BY ABS(p_price - ?) LIMIT 3";
                } catch (NumberFormatException e) {
                    // Handle the case where productPrice is not a valid number
                    throw new IllegalArgumentException("Invalid product price format.");
                }
            }

            // Execute the query
            PreparedStatement stmt = con.prepareStatement(sql);
            int parameterIndex = 1;  // Start with the first parameter

            stmt.setInt(parameterIndex, m_id);  // Set the first parameter
            if (totalPrice != null && !totalPrice.isEmpty()) {
                stmt.setDouble(2, Double.parseDouble(totalPrice));
            } else if (productPrice != null && !productPrice.isEmpty()) {
                stmt.setDouble(2, Double.parseDouble(productPrice));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setP_id(rs.getInt("p_id"));
                order.setU_id(rs.getInt("u_id"));
                order.setBuytime(rs.getString("buy_time"));
                order.setQuantity(rs.getInt("quantity"));
                order.setTotalprice(String.valueOf(rs.getDouble("totalprice")));

                Product product = new Product();
                product.setP_name(rs.getString("p_name"));
                product.setP_desc(rs.getString("p_desc"));
                product.setP_class(rs.getString("p_class"));
                product.setP_price(rs.getString("p_price"));

                User user = new User();
                user.setU_name(rs.getString("u_name"));
                user.setU_sex(rs.getString("u_sex"));
                user.setU_tele(rs.getString("u_tele"));

                order.setProduct(product);
                order.setUser(user);
                System.out.println(order.getProduct().getP_desc());

                closestOrders.add(order);
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (NumberFormatException | SQLException e) {
            throw new RuntimeException(e);
        }
        return closestOrders;
    }


}



