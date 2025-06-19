/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import model.ShippingInfo;

import java.sql.*;

public class ShippingInfoDAO {

    // Thêm thông tin giao hàng
    public boolean insertShippingInfo(ShippingInfo info) {
        String sql = "INSERT INTO shipping_info (order_id, full_name, phone, address, city) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, info.getOrderId());
            stmt.setString(2, info.getFullName());
            stmt.setString(3, info.getPhone());
            stmt.setString(4, info.getAddress());
            stmt.setString(5, info.getCity());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Lấy thông tin giao hàng theo order_id
    public ShippingInfo getShippingInfoByOrderId(int orderId) {
        String sql = "SELECT * FROM shipping_info WHERE order_id = ?";
        ShippingInfo info = null;

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                info = new ShippingInfo();
                info.setId(rs.getInt("id"));
                info.setOrderId(rs.getInt("order_id"));
                info.setFullName(rs.getString("full_name"));
                info.setPhone(rs.getString("phone"));
                info.setAddress(rs.getString("address"));
                info.setCity(rs.getString("city"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return info;
    }

    // Cập nhật thông tin giao hàng
    public boolean updateShippingInfo(ShippingInfo info) {
        String sql = "UPDATE shipping_info SET full_name = ?, phone = ?, address = ?, city = ? WHERE order_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, info.getFullName());
            stmt.setString(2, info.getPhone());
            stmt.setString(3, info.getAddress());
            stmt.setString(4, info.getCity());
            stmt.setInt(5, info.getOrderId());

            return stmt.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Xoá thông tin giao hàng theo order_id
    public boolean deleteShippingInfoByOrderId(int orderId) {
        String sql = "DELETE FROM shipping_info WHERE order_id = ?";

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
