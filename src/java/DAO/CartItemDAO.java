/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import model.CartItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartItemDAO {

    // Lấy danh sách giỏ hàng theo user_id
    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT * FROM cart_item WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                CartItem item = new CartItem();
                item.setId(rs.getInt("id"));
                item.setUserId(rs.getInt("user_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));

                cartItems.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cartItems;
    }

    // Thêm sản phẩm vào giỏ hàng
    public boolean addToCart(CartItem item) {
        String sql = "INSERT INTO cart_item (user_id, product_id, quantity) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getUserId());
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public boolean updateQuantity(CartItem item) {
        String sql = "UPDATE cart_item SET quantity = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getQuantity());
            stmt.setInt(2, item.getId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Xoá sản phẩm khỏi giỏ hàng theo id dòng cart_item
    public boolean removeCartItem(int id) {
        String sql = "DELETE FROM cart_item WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Xoá toàn bộ giỏ hàng của 1 user (dùng khi thanh toán xong)
    public boolean clearCartByUser(int userId) {
        String sql = "DELETE FROM cart_item WHERE user_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
