package controller;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import DAO.DBUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/order")
public class OrderServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try (Connection conn = DBUtil.getConnection()) {
            BufferedReader reader = request.getReader();
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) json.append(line);

            JsonObject jsonObj = JsonParser.parseString(json.toString()).getAsJsonObject();
            int userId = jsonObj.get("userId").getAsInt();
            JsonArray items = jsonObj.getAsJsonArray("items");

            // 1. Tính tổng tiền
            double totalAmount = 0;
            for (int i = 0; i < items.size(); i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                double price = item.get("price").getAsDouble();
                int quantity = item.get("quantity").getAsInt();
                totalAmount += price * quantity;
            }

            // 2. Tạo đơn hàng
            String insertOrderSQL = "INSERT INTO `order` (user_id, total_amount) VALUES (?, ?)";
            PreparedStatement psOrder = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS);
            psOrder.setInt(1, userId);
            psOrder.setDouble(2, totalAmount);
            psOrder.executeUpdate();

            ResultSet rs = psOrder.getGeneratedKeys();
            int orderId = 0;
            if (rs.next()) {
                orderId = rs.getInt(1);
            }

            // 3. Thêm sản phẩm vào order_item
            String insertItemSQL = "INSERT INTO order_item (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement psItem = conn.prepareStatement(insertItemSQL);

            for (int i = 0; i < items.size(); i++) {
                JsonObject item = items.get(i).getAsJsonObject();
                int productId = item.get("productId").getAsInt();
                int quantity = item.get("quantity").getAsInt();
                double price = item.get("price").getAsDouble();

                psItem.setInt(1, orderId);
                psItem.setInt(2, productId);
                psItem.setInt(3, quantity);
                psItem.setDouble(4, price);
                psItem.addBatch();
            }

            psItem.executeBatch();

            // 4. Xóa giỏ hàng
            String deleteCart = "DELETE FROM cart_item WHERE user_id = ?";
            PreparedStatement psDel = conn.prepareStatement(deleteCart);
            psDel.setInt(1, userId);
            psDel.executeUpdate();

            // 5. Phản hồi
            response.setContentType("application/json");
            response.getWriter().write("{\"success\":true}");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(500);
            response.getWriter().write("{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
