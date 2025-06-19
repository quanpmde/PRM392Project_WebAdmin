/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import model.OrderItem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDAO {

    // Thêm một dòng sản phẩm vào đơn hàng
    public boolean insertOrderItem(OrderItem item) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, item.getOrderId());
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setDouble(4, item.getPrice());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Thêm nhiều dòng sản phẩm vào đơn hàng
    public boolean insertMultipleOrderItems(List<OrderItem> items) {
        String sql = "INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            for (OrderItem item : items) {
                stmt.setInt(1, item.getOrderId());
                stmt.setInt(2, item.getProductId());
                stmt.setInt(3, item.getQuantity());
                stmt.setDouble(4, item.getPrice());
                stmt.addBatch();
            }

            int[] results = stmt.executeBatch();
            return results.length == items.size();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Lấy danh sách dòng sản phẩm theo order_id
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        List<OrderItem> list = new ArrayList<>();
        String sql = "SELECT * FROM order_item WHERE order_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setId(rs.getInt("id"));
                item.setOrderId(rs.getInt("order_id"));
                item.setProductId(rs.getInt("product_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setPrice(rs.getDouble("price"));

                list.add(item);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Xoá toàn bộ dòng sản phẩm theo đơn hàng
    public boolean deleteOrderItemsByOrderId(int orderId) {
        String sql = "DELETE FROM order_item WHERE order_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
