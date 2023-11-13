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

    //查找某一用户的信息，u-id
    public User selectuser(int u_id) throws SQLException {
        String sql = "select * from user where u_id = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, u_id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            String id = Integer.toString(rs.getInt("u_id"));
            String acc = rs.getString("u_acc");
            String psw = rs.getString("u_psw");
            String name = rs.getString("u_name");
            String sex = rs.getString("u_sex");
            String tele = rs.getString("u_tele");
            return new User(id, acc, psw, name, sex, tele);
        }
        return null;
    }

    // 定义一个insertOrUpdateConcern方法，将用户id和商家id添加到concern表中
    public void insertOrUpdateConcern(int u_id, int m_id) {
        // 连接数据库
        DataBase dataBase = new DataBase();
        dataBase.OpenDB();
        // 构建一个SQL查询语句，检查是否已经存在相同的记录 // 添加一个新的查询语句
        String checkQuery = "SELECT COUNT(*) FROM concern WHERE u_id = ? AND m_id = ?";
        PreparedStatement checkPstmt = null;
        ResultSet rs = null;
        try {
            // 创建一个预编译语句对象
            try {
                checkPstmt = dataBase.getCon().prepareStatement(checkQuery);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 设置占位符的值
            checkPstmt.setInt(1, u_id);
            checkPstmt.setInt(2, m_id);
            // 执行查询语句，得到一个结果集对象
            rs = checkPstmt.executeQuery();
            // 如果结果集不为空，获取第一行第一列的值，即记录的数量
            if (rs.next()) {
                int count = rs.getInt(1);
                // 如果记录的数量大于0，说明已经存在相同的记录，不需要插入或更新 // 添加一个新的判断条件
                if (count > 0) {
                    // 输出提示信息
                    System.out.println("已经关注过了");
                    // 结束方法，不再执行后面的代码
                    return;
                }
            }
        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            // 关闭结果集和查询语句对象
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                checkPstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // 构建一个SQL插入或更新语句，使用ON DUPLICATE KEY UPDATE子句来处理重复的记录
        String query = "INSERT INTO concern (u_id, m_id) VALUES (?, ?) ON DUPLICATE KEY UPDATE u_id = u_id, m_id = m_id";
        PreparedStatement pstmt = null;
        try {
            // 创建一个预编译语句对象
            try {
                pstmt = dataBase.getCon().prepareStatement(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // 设置占位符的值
            pstmt.setInt(1, u_id);
            pstmt.setInt(2, m_id);
            // 执行插入或更新语句，返回受影响的行数
            int rows = pstmt.executeUpdate();
            // 判断是否成功
            if (rows > 0) {
                // 输出成功信息
                System.out.println("关注成功");
            } else {
                // 输出失败信息
                System.out.println("关注失败");
            }
        } catch (SQLException e) {
            // 处理异常
            e.printStackTrace();
        } finally {
            // 关闭插入或更新语句对象
            try {
                pstmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // 关闭数据库连接
            try {
                dataBase.getCon().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
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
            replyList.add(comment);
        }
        return replyList;
    }

    //查找评论表，有回复id的不用返回，返回论坛发表的评论
    public List<CommentBar> selectForumList(User user, Merchant merchant) throws SQLException {
        String sql = "SELECT f.f_id, " +
                "       COALESCE(u.u_name, m.m_name) as author_name, " +
                "       f.f_time, " +
                "       f.f_con,  " +
                "       f.flag,  " +
                " u.u_id , m.m_id " +
                " FROM forum f " +
                " LEFT JOIN user u ON f.u_id = u.u_id  " +
                " LEFT JOIN merchant m ON f.m_id = m.m_id  " +
                " WHERE f.reply_to IS NULL  order by f.f_time desc";
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
            CommentBar commentBar = new CommentBar(id, author_name, f_time, f_con, flag, user, merchant);
            commentBarList.add(commentBar);
        }

        return commentBarList;
    }

    public int insertCommentToforum(User user, String content, String flag) throws SQLException {
        Connection con = DataBase.OpenDB();
        String sql = "insert into forum values (null,?,null,?,?,null,?)";
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(user.getID()));
        stmt.setString(2, LocalDateTime.now().toString());
        stmt.setString(3, content);
        stmt.setString(4, flag);
        int result = stmt.executeUpdate();
        con.close();
        return result;
    }

    public int insertCommentToforum(Merchant merchant, String content, String flag) throws SQLException {
        Connection con = DataBase.OpenDB();
        String sql = "insert into forum values (null,null,?,?,?,null,?)";
        PreparedStatement stmt = con.prepareStatement(sql);

        System.out.println(flag);
        stmt = con.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(merchant.getID()));
        stmt.setString(2, LocalDateTime.now().toString());
        stmt.setString(3, content);
        stmt.setString(4, flag);
        int result = stmt.executeUpdate();
        con.close();
        return result;
    }

    public void insertReplyToforum(String reply_to, String content, User user, Merchant merhant) throws SQLException {
        Connection con = DataBase.OpenDB();
        String sql;
        int x = 1;
        if (user != null) {
            sql = "insert into forum values (null,?,null,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(user.getID()));
            stmt.setString(2, LocalDateTime.now().toString());
            stmt.setString(3, content);
            stmt.setString(4, reply_to);
            stmt.setString(5, "用户");
            int result = stmt.executeUpdate();

        } else {
            sql = "insert into forum values (null,null,?,?,?,?,?)";
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, Integer.parseInt(merhant.getID()));
            stmt.setString(2, LocalDateTime.now().toString());
            stmt.setString(3, content);
            stmt.setString(4, reply_to);
            stmt.setString(5, "商家");
            int result = stmt.executeUpdate();
        }
        con.close();

    }

    //注册用户
    public boolean register(String useraccount, String password, String name, String role, String sex, String tele) throws SQLException {
        Connection con = DataBase.OpenDB();
        String sql;

        if ("用户".equals(role)) {
            sql = "INSERT INTO user (u_acc, u_psw, u_name, u_sex, u_tele) VALUES (?, ?, ?, ?, ?)";
        } else if ("商家".equals(role)) {
            sql = "INSERT INTO merchant (m_acc, m_psw, m_name, m_sex, m_tele) VALUES (?, ?, ?, ?, ?)";
        } else {
            return false;
        }
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, useraccount);
            stmt.setString(2, password);
            stmt.setString(3, name);
            stmt.setString(4, sex);
            stmt.setString(5, tele);

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;
        }
    }

    public int getCommentCount(String ID) throws SQLException {
        String sql = "select count(*) from forum where reply_to = ?";
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = con.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(ID));
        ResultSet rs = stmt.executeQuery();
        if(rs.next()){
            return rs.getInt(1);
        }
        else return 0;
    }
}



