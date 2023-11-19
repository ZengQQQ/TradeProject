package cn.bjut.jdbc;

import javax.swing.*;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DataControlProduct extends DataControl {
    public DataControlProduct() throws SQLException {
    }

    //在商家页面里修改商品信息
    public boolean updateProduct(int p_id, String newName, String newdesc, String newclass, double newPrice, String newsta, int newquantity, String newimg) {
        // 创建连接
        Connection con;
        PreparedStatement preparedStatement;
        DataBase dataBase = new DataBase();
        try {
            // 建立数据库连接
            con = dataBase.OpenDB();
            // 创建SQL更新语句
            String sql = "UPDATE product SET p_name = ?, p_desc = ?, p_class = ?, p_price = ?, p_status = ?,p_quantity = ?, p_img = ?,p_auditStatus = ? WHERE p_id = ?";
            // 创建 PreparedStatement 对象
            preparedStatement = con.prepareStatement(sql);
            // 设置参数
            preparedStatement.setString(1, newName);
            preparedStatement.setString(2, newdesc);
            preparedStatement.setString(3, newclass);
            preparedStatement.setDouble(4, newPrice);
            preparedStatement.setString(5, "下架");
            preparedStatement.setInt(6, newquantity);
            preparedStatement.setString(7, newimg);
            preparedStatement.setString(8, "待审核");
            preparedStatement.setInt(9, p_id);
            // 执行更新
            int rowsAffected = preparedStatement.executeUpdate();
            // 如果更新成功，rowsAffected 应该为 1
            return rowsAffected == 1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteProduct(int productId) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        boolean deleted = false;

        // 创建连接
        DataBase dataBase = new DataBase();
        con = dataBase.OpenDB();

        // 查询与该商品相关的订单状态
        String checkOrdersQuery = "SELECT o_status FROM orders WHERE p_id = ?";
        preparedStatement = con.prepareStatement(checkOrdersQuery);
        preparedStatement.setInt(1, productId);
        rs = preparedStatement.executeQuery();

        boolean orderInProgress = false;

        // 检查是否有未完成的订单
        while (rs.next()) {
            String orderStatus = rs.getString("o_status");
            if (!orderStatus.equals("已完成")) {
                orderInProgress = true;
                break;
            }
        }

        if (orderInProgress) {
            // 有未完成的订单，不能删除商品
            JOptionPane.showMessageDialog(null, "有未完成的订单，不能删除商品", "错误", JOptionPane.ERROR_MESSAGE);
        } else {
            // 创建SQL删除语句，并修改m_id和p_status
            String deleteQuery = "UPDATE product SET m_id = -1, p_status = '下架' WHERE p_id = ?";

            // 准备并执行SQL语句
            preparedStatement = con.prepareStatement(deleteQuery);
            preparedStatement.setInt(1, productId);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0) {
                // 如果成功删除了行，则返回true
                deleted = true;
            }
        }

        // 关闭资源
        if (rs != null) {
            rs.close();
        }
        if (preparedStatement != null) {
            preparedStatement.close();
        }
        if (con != null) {
            con.close();
        }

        return deleted;
    }



    //添加商品
    public boolean addProduct(int m_id, String productName, String productDesc, String productClass, double productPrice, String productState, int productQuantity, String productImg) throws SQLException {
        Connection con;
        PreparedStatement preparedStatement;
        boolean added = false;

        // 创建连接
        DataBase dataBase = new DataBase();
        con = dataBase.OpenDB();
        // 创建SQL插入语句
        String insertQuery = "INSERT INTO product (m_id, p_name, p_desc, p_class, p_price, p_status, p_quantity,p_auditStatus, p_img) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";
        // 准备并执行SQL语句
        preparedStatement = con.prepareStatement(insertQuery);
        preparedStatement.setInt(1, m_id);
        preparedStatement.setString(2, productName);
        preparedStatement.setString(3, productDesc);
        preparedStatement.setString(4, productClass);
        preparedStatement.setDouble(5, productPrice);
        preparedStatement.setString(6, productState);
        preparedStatement.setInt(7, productQuantity);
        preparedStatement.setString(8, "待审核");
        preparedStatement.setString(9, productImg);
        int rowsAffected = preparedStatement.executeUpdate();
        if (rowsAffected > 0) {
            // 如果成功插入了行，则返回true
            added = true;
        }
        return added;
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
                    if (Math.floor(priceValue) == priceValue) {
                        sql += " AND CAST(p_price AS DECIMAL(10, 0)) = ?";
                    } else if (Math.round(priceValue * 10.0) == priceValue * 10.0) {
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

                List<Product> closestProducts = findClosestProducts(searchValue, m_id);
                return closestProducts;
            }
            return productList;
        }
    }

    //找到商品根据最接近的价格
    private List<Product> findClosestProducts(String searchValue, int m_id) throws SQLException {
        List<Product> closestProducts = new ArrayList<>();
        Connection con = DataBase.OpenDB();

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
        con.close();

        return closestProducts;
    }

    public Product getProductFromPId(int p_id) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Product product = null;

        try {

            DataBase dataBase = new DataBase();
            con = dataBase.OpenDB();


            String query = "SELECT * FROM product WHERE p_id = ?";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, p_id);


            resultSet = preparedStatement.executeQuery();


            if (resultSet.next()) {

                int productId = resultSet.getInt("p_id");
                String productName = resultSet.getString("p_name");
                String productDescription = resultSet.getString("p_desc");
                String productClass = resultSet.getString("p_class");
                double productPrice = resultSet.getDouble("p_price");
                String productStatus = resultSet.getString("p_status");
                int productQuantity = resultSet.getInt("p_quantity");
                String productImage;
                if (resultSet.getString("p_img") == null) {
                    productImage = "R.jpg";
                } else {
                    productImage = resultSet.getString("p_img");
                }

                String productauditStatus = resultSet.getString("p_auditStatus");


                product = new Product(productId, productName, productDescription, productClass, productPrice, productStatus, productQuantity, productauditStatus, productImage);
            }
        } catch (SQLException e) {
            e.printStackTrace();

            resultSet.close();
            preparedStatement.close();
            con.close();
        }

        return product;
    }

    public List<Product> getProductofNoAudit() throws SQLException {
        List<Product> products = new ArrayList<>();
        Connection con = DataBase.OpenDB();
        String sql = "SELECT p_id, p_name, p_desc, p_class, p_price, p_status,p_quantity, p_img,p_auditStatus " + "FROM product " + "WHERE p_auditStatus = ?";
        PreparedStatement stmt = null;
        if (con != null) {
            stmt = con.prepareStatement(sql);
        }
        if (stmt != null) {
            stmt.setString(1, "待审核");
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
                System.out.println(product.toString());
            }
        }
        return products;
    }

    public void updateProductStatus(int p_id, String p_auditStatus) throws SQLException {
        Connection con;
        PreparedStatement preparedStatement;

        DataBase dataBase = new DataBase();
        con = dataBase.OpenDB();

        String query = "UPDATE product SET p_auditStatus = ? WHERE p_id = ?";
        preparedStatement = con.prepareStatement(query);
        preparedStatement.setString(1, p_auditStatus);
        preparedStatement.setInt(2, p_id);

        preparedStatement.executeUpdate();
        preparedStatement.close();
        con.close();

    }

    public List<Product> findProductsByHighestQuantity(int merchantId) {
        List<Product> productList = new ArrayList<>();
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt;
        ResultSet rs;

        try {

            // 获取商家的所有商品 p_id
            String getProductsQuery = "SELECT p_id FROM product WHERE m_id = ?";
            stmt = con.prepareStatement(getProductsQuery);
            stmt.setInt(1, merchantId);
            ResultSet productResult = stmt.executeQuery();

            while (productResult.next()) {
                int productId = productResult.getInt("p_id");

                // 查询每个商品的销售数量 quantity
                String getProductQuantityQuery = "SELECT SUM(quantity) as totalQuantity FROM orders WHERE p_id = ?";
                PreparedStatement getProductQuantityStmt = con.prepareStatement(getProductQuantityQuery);
                getProductQuantityStmt.setInt(1, productId);
                rs = getProductQuantityStmt.executeQuery();

                if (rs.next()) {
                    int totalQuantity = rs.getInt("totalQuantity");

                    //找到销售数量最高的商品或商品集
                    if (totalQuantity > 0) {
                        if (productList.isEmpty() || totalQuantity > Integer.parseInt(productList.get(0).getP_quantity())) {
                            productList.clear();
                            productList.add(getProductById(productId,totalQuantity, con));
                        } else if (totalQuantity == Integer.parseInt(productList.get(0).getP_quantity())) {
                            productList.add(getProductById(productId,totalQuantity, con));
                        }
                    }
                }
            }
            if (productResult != null) productResult.close();
            if (stmt != null) stmt.close();
            con.close();

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return productList;
    }
    public List<Product> findProductsByHighestQuantityToday(int merchantId) {
        List<Product> productList = new ArrayList<>();
        Connection con = DataBase.OpenDB();
        PreparedStatement stmt;
        ResultSet rs;

        try {
            // 获取当天日期
            LocalDate today = LocalDate.now();

            // 获取商家的所有商品 p_id
            String getProductsQuery = "SELECT p_id FROM product WHERE m_id = ?";
            stmt = con.prepareStatement(getProductsQuery);
            stmt.setInt(1, merchantId);
            ResultSet productResult = stmt.executeQuery();

            int maxQuantity = 0;

            while (productResult.next()) {
                int productId = productResult.getInt("p_id");

                // 查询当天每个商品的销售数量 quantity
                String getProductQuantityQuery = "SELECT SUM(quantity) as totalQuantity " +
                        "FROM orders " +
                        "WHERE p_id = ? AND DATE(buy_time) = ?";

                PreparedStatement getProductQuantityStmt = con.prepareStatement(getProductQuantityQuery);
                getProductQuantityStmt.setInt(1, productId);
                getProductQuantityStmt.setDate(2, Date.valueOf(today));

                rs = getProductQuantityStmt.executeQuery();

                if (rs.next()) {
                    int totalQuantity = rs.getInt("totalQuantity");

                    if (totalQuantity > maxQuantity) {
                        maxQuantity = totalQuantity;
                        productList.clear();
                        productList.add(getProductById(productId,maxQuantity, con));
                    } else if (totalQuantity == maxQuantity) {
                        productList.add(getProductById(productId,maxQuantity, con));
                    }
                }
            }
            if (productResult != null) productResult.close();
            if (stmt != null) stmt.close();
            con.close();

        } catch (SQLException se) {
            se.printStackTrace();
        }
        return productList;
    }

    // 辅助方法 - 通过 p_id 获取商品信息
    private Product getProductById(int productId,int totalquantity, Connection con) throws SQLException {
        String getProductQuery = "SELECT * FROM product WHERE p_id = ?";
        PreparedStatement getProductStmt = con.prepareStatement(getProductQuery);
        getProductStmt.setInt(1, productId);
        ResultSet productResult = getProductStmt.executeQuery();

        Product product = null;
        if (productResult.next()) {
            product = new Product();
            // 设置 Product 对象的属性值
            product.setP_id(productResult.getInt("p_id"));
            product.setP_name(productResult.getString("p_name"));
            product.setP_desc(productResult.getString("p_desc"));
            product.setP_class(productResult.getString("p_class"));
            product.setP_price(productResult.getString("p_price"));
            product.setP_status(productResult.getString("p_status"));
            product.setP_quantity(productResult.getInt("p_quantity"));
            product.setP_audiStatus(productResult.getString("p_auditStatus"));
            product.setSales(totalquantity);
            product.setP_img(productResult.getString("p_img"));

        }

        // Close resources
        if (productResult != null) productResult.close();
        if (getProductStmt != null) getProductStmt.close();

        return product;
    }

}
