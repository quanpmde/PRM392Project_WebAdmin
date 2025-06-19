// ✅ File: CartServlet.java (NetBeans - Servlet)
// ➤ Bổ sung chức năng DELETE /cart?userId=...&productId=... để xóa sản phẩm khỏi giỏ hàng

package controller;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import DAO.DBUtil;
import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/cart")
public class CartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        int productId = Integer.parseInt(request.getParameter("productId"));
        int quantity = Integer.parseInt(request.getParameter("quantity"));

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO cart_item (user_id, product_id, quantity) " +
                         "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity = quantity + ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setInt(4, quantity);
            stmt.executeUpdate();

            response.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        int userId = Integer.parseInt(request.getParameter("userId"));

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT ci.product_id, ci.quantity, p.name, p.price, p.image_url " +
                         "FROM cart_item ci JOIN product p ON ci.product_id = p.id " +
                         "WHERE ci.user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            JSONArray arr = new JSONArray();
            while (rs.next()) {
                JSONObject obj = new JSONObject();
                obj.put("productId", rs.getInt("product_id"));
                obj.put("quantity", rs.getInt("quantity"));
                obj.put("name", rs.getString("name"));
                obj.put("price", rs.getDouble("price"));
                obj.put("image_url", rs.getString("image_url"));
                arr.put(obj);
            }
            out.print(arr.toString());
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.close();
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        int productId = Integer.parseInt(request.getParameter("productId"));

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "DELETE FROM cart_item WHERE user_id = ? AND product_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            stmt.setInt(2, productId);
            int rows = stmt.executeUpdate();

            if (rows > 0) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
