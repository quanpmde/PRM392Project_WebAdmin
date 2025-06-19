/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import model.Order;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    // Thêm đơn hàng mới
    public boolean insertOrder(Order order) {
        String sql = "INSERT INTO `order` (user_id, order_date, total_amount) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, order.getUserId());
            stmt.setTimestamp(2, Timestamp.valueOf(order.getOrderDate()));
            stmt.setDouble(3, order.getTotalAmount());

            int affectedRows = stmt.executeUpdate();

            // Lấy ID tự tăng (generated key)
            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    order.setId(generatedKeys.getInt(1));
                }
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Lấy đơn hàng theo ID
    public Order getOrderById(int id) {
        Order order = null;
        String sql = "SELECT * FROM `order` WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setTotalAmount(rs.getDouble("total_amount"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return order;
    }

    // Lấy tất cả đơn hàng
    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM `order` ORDER BY order_date DESC";

        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setTotalAmount(rs.getDouble("total_amount"));
                orders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    // Lấy đơn hàng theo user
    public List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM `order` WHERE user_id = ? ORDER BY order_date DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setUserId(rs.getInt("user_id"));
                order.setOrderDate(rs.getTimestamp("order_date").toLocalDateTime());
                order.setTotalAmount(rs.getDouble("total_amount"));
                orders.add(order);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return orders;
    }

    // Xoá đơn hàng
    public boolean deleteOrder(int id) {
        String sql = "DELETE FROM `order` WHERE id = ?";

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
