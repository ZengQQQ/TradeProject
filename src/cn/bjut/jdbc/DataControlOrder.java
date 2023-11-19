package cn.bjut.jdbc;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class DataControlOrder extends DataControl {

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
            String sql = "SELECT o.o_id,o.p_id, o.u_id, o.buy_time, o.quantity, o.totalprice,o.send_time,o.receive_time,o.o_status, " +
                    "p.p_name, p.p_desc, p.p_class, p.p_price, p.p_quantity, p.p_img,p.p_auditStatus, " +
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
                order.setO_id(rs.getInt("o_id"));
                order.setP_id(rs.getInt("p_id"));
                order.setU_id(rs.getInt("u_id"));
                order.setBuytime(rs.getString("buy_time"));
                order.setSendtime(rs.getString("send_time"));
                order.setReceivetime(rs.getString("receive_time"));
                order.setQuantity(rs.getInt("quantity"));
                order.setStatus(rs.getString("o_status"));
                order.setTotalprice(String.valueOf(rs.getDouble("totalprice")));

                Product product = new Product();
                product.setP_name(rs.getString("p_name"));
                product.setP_desc(rs.getString("p_desc"));
                product.setP_class(rs.getString("p_class"));
                product.setP_price(rs.getString("p_price"));
                product.setP_quantity(rs.getInt("p_quantity"));
                product.setP_audiStatus(rs.getString("p_auditStatus"));
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
            rs.close();
            stmt.close();
            con.close();
        }
        return orderInfoList;
    }


    // 根据一定的信息查找订单
    public List<Order> searchOrders(int m_id, String productId, String productName, String minPrice, String maxPrice, String minQuantity, String maxQuantity, String status, String auditStatus, String category,
                                    String username, String gender, String phone,
                                    String orderId, String minOrderQuantity, String maxOrderQuantity, String minTotalPrice, String maxTotalPrice, String dateType, String orderStatus, String date) throws SQLException {
        List<Order> orderList = new ArrayList<>();
        Connection con = DataBase.OpenDB();

        try {
            String sql = "SELECT o.o_id,o.p_id, o.u_id, o.buy_time, o.quantity, o.totalprice,o.send_time,o.receive_time,o.o_status, " +
                    " p.p_name, p.p_desc, p.p_class, p.p_price,p.p_img,p.p_quantity,p.p_status,p.p_auditStatus, " +
                    "u.u_name, u.u_sex, u.u_tele FROM orders o ";
            String joinProduct = " LEFT JOIN product p ON o.p_id = p.p_id ";
            String joinUser = " INNER JOIN user u ON o.u_id = u.u_id ";
            String whereClause = " WHERE p.m_id = ? ";

            List<Object> parameters = new ArrayList<>();
            parameters.add(m_id);

            if (productId != null && !productId.isEmpty()) {
                whereClause += " AND o.p_id = ? ";
                parameters.add(Integer.parseInt(productId));
            }
            if (productName != null && !productName.isEmpty()) {
                whereClause += " AND p.p_name LIKE ? ";
                parameters.add("%" + productName + "%");
            }

            if (minPrice != null && !minPrice.isEmpty()) {
                whereClause += " AND p.p_price >= ? ";
                parameters.add(Double.parseDouble(minPrice));
            }

            if (maxPrice != null && !maxPrice.isEmpty()) {
                whereClause += " AND p.p_price <= ? ";
                parameters.add(Double.parseDouble(maxPrice));
            }

            if (minQuantity != null && !minQuantity.isEmpty()) {
                whereClause += " AND p.p_quantity >= ? ";
                parameters.add(Integer.parseInt(minQuantity));
            }
            if (maxQuantity != null && !maxQuantity.isEmpty()) {
                whereClause += " AND p.p_quantity <= ? ";
                parameters.add(Integer.parseInt(maxQuantity));
            }

            if (!status.isEmpty() && !"全部状态".equals(status)) {
                whereClause += " AND p.p_status = ? ";
                parameters.add(status);
            }

            if (!auditStatus.isEmpty() && !"全部审核状态".equals(auditStatus)) {
                whereClause += " AND p.p_auditStatus = ? ";
                parameters.add(auditStatus);
            }

            if (!category.isEmpty() && !"全部类别".equals(category)) {
                whereClause += " AND p.p_class = ? ";
                parameters.add(category);
            }

            if (username != null && !username.isEmpty()) {
                whereClause += " AND u.u_name LIKE ? ";
                parameters.add("%" + username + "%");
            }

            if (!gender.isEmpty() && !"全部性别".equals(gender)) {
                whereClause += " AND u.u_sex = ? ";
                parameters.add(gender);
            }

            if (phone != null && !phone.isEmpty()) {
                whereClause += " AND u.u_tele LIKE ?";
                parameters.add("%" + phone + "%");
            }

            if (orderId != null && !orderId.isEmpty()) {
                whereClause += " AND o.o_id = ? ";
                parameters.add(Integer.parseInt(orderId));
            }

            if (minOrderQuantity != null && !minOrderQuantity.isEmpty()) {
                whereClause += " AND o.quantity >= ? ";
                parameters.add(Integer.parseInt(minOrderQuantity));
            }
            if (maxOrderQuantity != null && !maxOrderQuantity.isEmpty()) {
                whereClause += " AND o.quantity <= ? ";
                parameters.add(Integer.parseInt(maxOrderQuantity));
            }

            if (minTotalPrice != null && !minTotalPrice.isEmpty()) {
                whereClause += " AND o.totalprice >= ? ";
                parameters.add(Double.parseDouble(minTotalPrice));
            }

            if (maxTotalPrice != null && !maxTotalPrice.isEmpty()) {
                whereClause += " AND o.totalprice <= ? ";
                parameters.add(Double.parseDouble(maxTotalPrice));
            }

            if (!orderStatus.isEmpty() && !"全部状态".equals(orderStatus)) {
                whereClause += " AND o.o_status = ? ";
                parameters.add(orderStatus);
            }

            if (date != null && !date.isEmpty()) {
                switch (dateType) {
                    case "日期":
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.M.d");
                        Date inputDate = dateFormat.parse(date);
                        String startDate = new SimpleDateFormat("yyyy-MM-dd").format(inputDate);
                        String endDate = new SimpleDateFormat("yyyy-MM-dd").format(inputDate) + " 23:59:59";
                        whereClause += "AND o.buy_time >= ? AND o.buy_time <= ? ";
                        parameters.add(startDate);
                        parameters.add(endDate);
                        break;
                    case "年":
                        int year = Integer.parseInt(date);
                        whereClause += "AND YEAR(o.buy_time) = ? ";
                        parameters.add(year);
                        break;
                    case "月":
                        int month = Integer.parseInt(date);
                        whereClause += "AND MONTH(o.buy_time) = ? ";
                        parameters.add(month);
                        break;
                    case "日":
                        int day = Integer.parseInt(date);
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
            System.out.println("订单为空");
        }

        return orderList;
    }

    // 从 ResultSet 中创建 Order 对象
    private Order createOrderFromResultSet(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setO_id(rs.getInt("o_id"));
        order.setP_id(rs.getInt("p_id"));
        order.setU_id(rs.getInt("u_id"));
        order.setBuytime(rs.getString("buy_time"));
        order.setSendtime(rs.getString("send_time"));
        order.setReceivetime(rs.getString("receive_time"));
        order.setStatus(rs.getString("o_status"));
        order.setQuantity(rs.getInt("quantity"));
        order.setTotalprice(String.valueOf(rs.getDouble("totalprice")));

        Product product = new Product();
        product.setP_name(rs.getString("p_name"));
        product.setP_desc(rs.getString("p_desc"));
        product.setP_class(rs.getString("p_class"));
        product.setP_price(rs.getString("p_price"));
        product.setP_img(rs.getString("p_img"));
        product.setP_status(rs.getString("p_status"));
        product.setP_audiStatus(rs.getString("p_auditStatus"));
        product.setP_quantity(rs.getInt("p_quantity"));

        User user = new User();
        user.setU_name(rs.getString("u_name"));
        user.setU_sex(rs.getString("u_sex"));
        user.setU_tele(rs.getString("u_tele"));

        order.setProduct(product);
        order.setUser(user);

        return order;
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
                    "WHERE p.m_id = ? ";

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


    // 根据o_id查找订单有关的用户和商品
    public Order getOrderInfoByO_id(int o_id) throws SQLException {
        Order order = new Order();
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            // 编写SQL查询语句，根据o_id查找订单、用户和商品信息
            String sql = "SELECT o.o_id,o.p_id, o.u_id, o.buy_time, o.quantity, o.totalprice,o.send_time,o.receive_time,o.o_status, " +
                    "p.p_name, p.p_desc, p.p_class, p.p_price, p.p_quantity, p.p_img,p.p_auditStatus,p.p_status, " +
                    "u.u_name, u.u_sex, u.u_tele " +
                    "FROM orders o " +
                    "INNER JOIN product p ON o.p_id = p.p_id " +
                    "INNER JOIN user u ON o.u_id = u.u_id " +
                    "WHERE o.o_id = ?";

            // 创建PreparedStatement对象，设置参数并执行查询
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, o_id);
            rs = stmt.executeQuery();

            // 处理查询结果
            if (rs.next()) {
                order.setO_id(rs.getInt("o_id"));
                order.setP_id(rs.getInt("p_id"));
                order.setU_id(rs.getInt("u_id"));
                order.setBuytime(rs.getString("buy_time"));
                order.setSendtime(rs.getString("send_time"));
                order.setReceivetime(rs.getString("receive_time"));
                order.setQuantity(rs.getInt("quantity"));
                order.setStatus(rs.getString("o_status"));
                order.setTotalprice(String.valueOf(rs.getDouble("totalprice")));

                Product product = new Product();
                product.setP_id(rs.getInt("p_id"));
                product.setP_name(rs.getString("p_name"));
                product.setP_desc(rs.getString("p_desc"));
                product.setP_class(rs.getString("p_class"));
                product.setP_price(rs.getString("p_price"));
                product.setP_quantity(rs.getInt("p_quantity"));
                product.setP_audiStatus(rs.getString("p_auditStatus"));
                product.setP_status(rs.getString("p_status"));
                product.setP_img(rs.getString("p_img"));

                User user = new User();
                user.setU_name(rs.getString("u_name"));
                user.setU_sex(rs.getString("u_sex"));
                user.setU_tele(rs.getString("u_tele"));

                order.setProduct(product);
                order.setUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // 关闭数据库连接和资源
            rs.close();
            stmt.close();
            con.close();
        }
        return order;
    }


    public boolean shippOrder(int o_Id) {
        boolean isShipped = false; // 默认值表示失败

        try {
            Connection con = DataBase.OpenDB();
            PreparedStatement stmt = null;
            // 获取订单信息
            Order order = getOrderInfoByO_id(o_Id);
            // 更新订单状态为“待收货”，并将发送时间设置为当前时间
            String sql = "UPDATE orders SET o_status = '待收货', send_time = NOW() WHERE o_id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, o_Id);
            int rowsAffected = stmt.executeUpdate();
            // 如果更新查询影响到了行数，则视为成功
            if (rowsAffected > 0) {
                isShipped = true; // 表示成功发货
            }

            // 关闭资源
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isShipped;
    }


    public boolean returnOrder(int o_Id) {
        boolean isReturned = false; // 默认值表示失败

        try {
            Connection con = DataBase.OpenDB();
            PreparedStatement stmt = null;
            // 获取订单信息
            Order order = getOrderInfoByO_id(o_Id);
            // 更新订单状态为“已退货”，并将退货时间设置为当前时间
            String sql = "UPDATE orders SET o_status = '已退货', receive_time = NOW() WHERE o_id = ?";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, o_Id);
            int rowsAffected = stmt.executeUpdate();
            // 如果更新查询影响到了行数，则视为成功
            if (rowsAffected > 0) {
                isReturned = true; // 表示成功发货
            }

            // 关闭资源
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isReturned;
    }
}
