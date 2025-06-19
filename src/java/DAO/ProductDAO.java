/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {

    // Lấy toàn bộ sản phẩm
    public List<Product> getAllProducts() {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT * FROM product";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getInt("price"));  // hoặc getDouble nếu cần
                p.setImage_url(rs.getString("image_url"));
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
        String sql = "SELECT * FROM product WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getInt("price"));
                p.setImage_url(rs.getString("image_url"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return p;
    }

    // Thêm sản phẩm
    public boolean insertProduct(Product p) {
        String sql = "INSERT INTO product (name, description, price, image_url) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setInt(3, p.getPrice());
            stmt.setString(4, p.getImage_url());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Cập nhật sản phẩm
    public boolean updateProduct(Product p) {
        String sql = "UPDATE product SET name = ?, description = ?, price = ?, image_url = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, p.getName());
            stmt.setString(2, p.getDescription());
            stmt.setInt(3, p.getPrice());
            stmt.setString(4, p.getImage_url());
            stmt.setInt(5, p.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Xoá sản phẩm theo ID
    public boolean deleteProduct(int id) {
        String sql = "DELETE FROM product WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
