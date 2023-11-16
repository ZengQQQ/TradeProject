package cn.bjut.jdbc;

import javax.swing.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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

    // 删除商品并修改m_id和p_status
    public boolean deleteProduct(int productId) throws SQLException {
        Connection con;
        PreparedStatement preparedStatement;
        boolean deleted = false;

        // 创建连接
        DataBase dataBase = new DataBase();
        con = dataBase.OpenDB();

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
        return deleted;
    }


    //添加商品
    public boolean addProduct(int m_id, String productName, String productDesc, String productClass, double productPrice, String productState, int productQuantity, String productImg) throws SQLException {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        boolean added = false;

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
                // Perform a secondary query to find the closest products based on the specified price value
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
            // Create a connection
            DataBase dataBase = new DataBase();
            con = dataBase.OpenDB();

            // Prepare the SQL statement to retrieve the product details based on p_id
            String query = "SELECT * FROM product WHERE p_id = ?";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, p_id);

            // Execute the query
            resultSet = preparedStatement.executeQuery();

            // Check if there is a result
            if (resultSet.next()) {
                // Extract data from the result set and create a Product object
                int productId = resultSet.getInt("p_id");
                String productName = resultSet.getString("p_name");
                String productDescription = resultSet.getString("p_desc");
                String productClass = resultSet.getString("p_class");
                double productPrice = resultSet.getDouble("p_price");
                String productStatus = resultSet.getString("p_status");
                String productauditStatus = resultSet.getString("p_auditStatus");
                String productImage = resultSet.getString("p_img");
                int productQuantity = resultSet.getInt("p_quantity");

                // Create a Product object
                product = new Product(productId, productName, productDescription, productClass, productPrice, productStatus,  productQuantity,productauditStatus,productImage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exceptions as needed
            resultSet.close();
            preparedStatement.close();
            con.close();
        }

        return product;
    }

}
