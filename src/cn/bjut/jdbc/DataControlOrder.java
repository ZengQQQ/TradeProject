package cn.bjut.jdbc;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataControlOrder extends DataControl{

    public DataControlOrder() throws SQLException {
    }

    // 根据m_id查找订单有关的用户和商品
    public List<Order> getOrderInfoByM_id(int m_id) throws SQLException {
        List<Order> orderInfoList = new ArrayList<>();

        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // 编写SQL查询语句，根据m_id查找订单、用户和商品信息
            String sql = "SELECT o.p_id, o.u_id, o.buy_time, o.quantity, o.totalprice, " +
                    "p.p_name, p.p_desc, p.p_class, p.p_price, p.p_quantity, p.p_img, " +
                    "u.u_name, u.u_sex, u.u_tele " +
                    "FROM orders o " +
                    "INNER JOIN product p ON o.p_id = p.p_id " +
                    "INNER JOIN user u ON o.u_id = u.u_id " +
                    "WHERE p.m_id = ?";  // 修改这里的条件，根据商品表的m_id进行查询

            // 创建PreparedStatement对象，设置参数并执行查询
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, m_id);
            rs = stmt.executeQuery();

            // 处理查询结果
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
                product.setP_quantity(rs.getInt("p_quantity"));
                product.setP_img(rs.getString("p_img"));

                User user = new User();
                user.setU_name(rs.getString("u_name"));
                user.setU_sex(rs.getString("u_sex"));
                user.setU_tele(rs.getString("u_tele"));

                order.setProduct(product);
                order.setUser(user);

                orderInfoList.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭数据库连接和资源
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (con != null) {
                con.close();
            }
        }
        return orderInfoList;
    }



    // 根据一定的信息查找订单
    public List<Order> searchOrders(int m_id, String productType, String userType, String dateType, String productf, String userf, String quantityf, String totalpricef, String datef) throws SQLException {
        List<Order> orderList = new ArrayList<>();
        Connection con = DataBase.OpenDB();

        try {
            String sql = "SELECT o.p_id, o.u_id, o.buy_time, o.quantity, o.totalprice, p.p_name, p.p_desc, p.p_class, p.p_price,p_img,p_quantity, u.u_name, u.u_sex, u.u_tele FROM orders o ";
            String joinProduct = "LEFT JOIN product p ON o.p_id = p.p_id ";
            String joinUser = "INNER JOIN user u ON o.u_id = u.u_id ";
            String whereClause = "WHERE p.m_id = ? ";
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
                        if (totalpricef == null || totalpricef.isEmpty()) {
                            double price = Double.parseDouble(productf);
                            if (Math.floor(price) == price) {
                                whereClause += "AND CAST(p.p_price AS DECIMAL(10, 0)" + ") = ? ";
                            } else if (Math.round(price * 10.0) == price * 10.0) {
                                whereClause += "AND CAST(p.p_price AS DECIMAL(10, 1)" + ") = ? ";
                            } else {
                                whereClause += "AND CAST(p.p_price AS DECIMAL(10, 2)" + ") = ? ";
                            }
                            parameters.add(price);
                        }
                        break;
                    case "数量":
                        whereClause += "AND p.p_quantity = ? ";
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
                double totalPrice = Double.parseDouble(totalpricef);
                if (Math.floor(totalPrice) == totalPrice) {
                    whereClause += "AND ABS(o.totalprice - ?) < 1 ";
                } else if (Math.round(totalPrice * 10.0) == totalPrice * 10.0) {
                    whereClause += "AND CAST(o.totalprice AS DECIMAL(10, 1)" + ") = ? ";
                } else {
                    whereClause += "AND CAST(o.totalprice AS DECIMAL(10, 2)" + ") = ? ";
                }
                parameters.add(totalPrice);
            }

            if (datef != null && !datef.isEmpty()) {
                switch (dateType) {
                    case "日期":
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.M.d");
                        Date inputDate = dateFormat.parse(datef);
                        String startDate = new SimpleDateFormat("yyyy-MM-dd").format(inputDate);
                        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(inputDate) + " 23:59:59";
                        whereClause += "AND o.buy_time >= ? AND o.buy_time <= ? ";
                        parameters.add(startDate);
                        parameters.add(endDate);
                        break;
                    case "年":
                        int year = Integer.parseInt(datef);
                        whereClause += "AND YEAR(o.buy_time) = ? ";
                        parameters.add(year);
                        break;
                    case "月":
                        int month = Integer.parseInt(datef);
                        whereClause += "AND MONTH(o.buy_time) = ? ";
                        parameters.add(month);
                        break;
                    case "日":
                        int day = Integer.parseInt(datef);
                        whereClause += "AND DAY(o.buy_time) = ? ";
                        parameters.add(day);
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
                Order order = createOrderFromResultSet(rs);
                orderList.add(order);
            }
            rs.close();
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (orderList.isEmpty()) {
            System.out.println("cuole");
        }

        if (orderList.isEmpty() && ((userf != null && !userf.isEmpty()) || (quantityf != null && !quantityf.isEmpty()) ||
                (datef != null && !datef.isEmpty()) || !(productType.equals("价格") && !productf.isEmpty()))) {
            JOptionPane.showMessageDialog(null, "订单结果没有找到", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
            return null;
        } else if (orderList.isEmpty() && (totalpricef != null && !totalpricef.isEmpty())) {
            // 执行次要查询以根据指定的条件找到最接近的订单
            return findClosestOrders(productf, totalpricef, m_id);
        }

        return orderList;
    }


    // 找到最接近的订单
    private List<Order> findClosestOrders(String productPrice, String totalPrice, int m_id) {
        List<Order> closestOrders = new ArrayList<>();
        Connection con = DataBase.OpenDB();

        try {
            // 构建 SQL 查询基于可用的条件
            String sql = "SELECT o.p_id, o.u_id, o.buy_time, o.quantity, o.totalprice, p.p_name, p.p_desc, p.p_class, p.p_price,p.p_img,p.p_quantity, u.u_name, u.u_sex, u.u_tele " +
                    "FROM orders o " +
                    "INNER JOIN product p ON o.p_id = p.p_id " +
                    "INNER JOIN user u ON o.u_id = u.u_id " +
                    "WHERE o.m_id = ? ";

            if (totalPrice != null && !totalPrice.isEmpty()) {
                JOptionPane.showMessageDialog(null, "订单结果没有找到，给您订单总价格最接近的3个订单", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
                try {
                    sql += "ORDER BY ABS(o.totalprice - ?) LIMIT 3";
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("无效的总价格式。");
                }
            } else if (productPrice != null && !productPrice.isEmpty()) {
                JOptionPane.showMessageDialog(null, "订单结果没有找到，给您商品价格最接近的3个订单", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
                try {
                    sql += "ORDER BY ABS(p.p_price - ?) LIMIT 3";
                } catch (NumberFormatException e) {
                    // 处理 productPrice 不是有效数字的情况
                    throw new IllegalArgumentException("无效的商品价格格式。");
                }
            }

            // 执行查询
            PreparedStatement stmt = con.prepareStatement(sql);
            int parameterIndex = 1;  // 从第一个参数开始

            stmt.setInt(parameterIndex, m_id);  // 设置第一个参数
            if (totalPrice != null && !totalPrice.isEmpty()) {
                stmt.setDouble(2, Double.parseDouble(totalPrice));
            } else if (productPrice != null && !productPrice.isEmpty()) {
                stmt.setDouble(2, Double.parseDouble(productPrice));
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Order order = createOrderFromResultSet(rs);
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

    // 从 ResultSet 中创建 Order 对象
    private Order createOrderFromResultSet(ResultSet rs) throws SQLException {
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
        product.setP_img(rs.getString("p_img"));
        product.setP_quantity(rs.getInt("p_quantity"));

        User user = new User();
        user.setU_name(rs.getString("u_name"));
        user.setU_sex(rs.getString("u_sex"));
        user.setU_tele(rs.getString("u_tele"));

        order.setProduct(product);
        order.setUser(user);

        return order;
    }
}
