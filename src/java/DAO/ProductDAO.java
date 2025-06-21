/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Lấy toàn bộ sản phẩm (kèm Category)
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM product p "
                + "LEFT JOIN category c ON p.category_id = c.id";

        try (Connection conn = DBUtil.getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getInt("price"));
                p.setImage_url(rs.getString("image_url"));
                p.setQuantity(rs.getInt("quantity"));
                p.setStatus(rs.getInt("status"));

                // Gán Category
                Category c = new Category();
                c.setId(rs.getInt("category_id"));
                c.setName(rs.getString("category_name"));
                p.setCategory(c);

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Lấy sản phẩm theo trạng thái
    public List<Product> getProductsByStatus(int status) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT p.*, c.name AS category_name FROM product p "
                + "LEFT JOIN category c ON p.category_id = c.id "
                + "WHERE p.status = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, status);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getInt("price"));
                p.setImage_url(rs.getString("image_url"));
                p.setQuantity(rs.getInt("quantity"));
                p.setStatus(rs.getInt("status"));

                Category c = new Category();
                c.setId(rs.getInt("category_id"));
                c.setName(rs.getString("category_name"));
                p.setCategory(c);

                list.add(p);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Tìm sản phẩm theo ID
    public Product getProductById(int id) {
        Product p = null;
        String sql = "SELECT p.*, c.name AS category_name FROM product p "
                + "LEFT JOIN category c ON p.category_id = c.id "
                + "WHERE p.id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getInt("price"));
                p.setImage_url(rs.getString("image_url"));
                p.setQuantity(rs.getInt("quantity"));
                p.setStatus(rs.getInt("status"));

                Category c = new Category();
                c.setId(rs.getInt("category_id"));
                c.setName(rs.getString("category_name"));
                p.setCategory(c);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    // Thêm sản phẩm
    public boolean insertProduct(Product p) {
        String sql = "INSERT INTO product (name, description, price, image_url, category_id, quantity, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setInt(3, p.getPrice());
            stmt.setString(4, p.getImage_url());
            stmt.setInt(5, p.getCategory().getId());
            stmt.setInt(6, p.getQuantity());
            stmt.setInt(7, p.getStatus());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Cập nhật sản phẩm
    public boolean updateProduct(Product p) {
        String sql = "UPDATE product SET name = ?, description = ?, price = ?, image_url = ?, "
                + "category_id = ?, quantity = ?, status = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setInt(3, p.getPrice());
            stmt.setString(4, p.getImage_url());
            stmt.setInt(5, p.getCategory().getId());
            stmt.setInt(6, p.getQuantity());
            stmt.setInt(7, p.getStatus());
            stmt.setInt(8, p.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Đánh dấu sản phẩm là "đã xóa" bằng cách đổi status sang 1
    public boolean deleteProduct(int id) {
        String sql = "UPDATE product SET status = 1 WHERE id = ?";

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Product> filterProducts(String keyword, Integer categoryId, Integer status, Integer minPrice, Integer maxPrice, int offset, int limit) {
        List<Product> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT p.*, c.name AS category_name FROM product p JOIN category c ON p.category_id = c.id WHERE 1=1");

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND p.name LIKE ?");
        }
        if (categoryId != null) {
            sql.append(" AND p.category_id = ?");
        }
        if (status != null) {
            sql.append(" AND p.status = ?");
        }
        if (minPrice != null) {
            sql.append(" AND p.price >= ?");
        }
        if (maxPrice != null) {
            sql.append(" AND p.price <= ?");
        }
        sql.append(" LIMIT ? OFFSET ?");

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;

            if (keyword != null && !keyword.isEmpty()) {
                stmt.setString(idx++, "%" + keyword + "%");
            }
            if (categoryId != null) {
                stmt.setInt(idx++, categoryId);
            }
            if (status != null) {
                stmt.setInt(idx++, status);
            }
            if (minPrice != null) {
                stmt.setInt(idx++, minPrice);
            }
            if (maxPrice != null) {
                stmt.setInt(idx++, maxPrice);
            }

            stmt.setInt(idx++, limit);
            stmt.setInt(idx++, offset);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getInt("price"));
                p.setImage_url(rs.getString("image_url"));
                p.setQuantity(rs.getInt("quantity"));
                p.setStatus(rs.getInt("status"));
                p.setStatus(rs.getInt("status"));

                Category cat = new Category();
                cat.setId(rs.getInt("category_id"));
                cat.setName(rs.getString("category_name"));
                p.setCategory(cat);

                list.add(p);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public int countFilteredProducts(String keyword, Integer categoryId, Integer status, Integer minPrice, Integer maxPrice) {
        int count = 0;
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM product WHERE 1=1");

        if (keyword != null && !keyword.isEmpty()) {
            sql.append(" AND name LIKE ?");
        }
        if (categoryId != null) {
            sql.append(" AND category_id = ?");
        }
        if (status != null) {
            sql.append(" AND status = ?");
        }
        if (minPrice != null) {
            sql.append(" AND price >= ?");
        }
        if (maxPrice != null) {
            sql.append(" AND price <= ?");
        }

        try (Connection conn = DBUtil.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            int idx = 1;

            if (keyword != null && !keyword.isEmpty()) {
                stmt.setString(idx++, "%" + keyword + "%");
            }
            if (categoryId != null) {
                stmt.setInt(idx++, categoryId);
            }
            if (status != null) {
                stmt.setInt(idx++, status);
            }
            if (minPrice != null) {
                stmt.setInt(idx++, minPrice);
            }
            if (maxPrice != null) {
                stmt.setInt(idx++, maxPrice);
            }

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return count;
    }

}
