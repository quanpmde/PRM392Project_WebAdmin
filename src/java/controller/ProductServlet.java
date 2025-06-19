// ✅ ProductServlet.java - hỗ trợ GET /products và GET /products/{id}
// 📁 Đặt trong: controller.ProductServlet

package controller;

import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.JSONArray;
import org.json.JSONObject;
import DAO.DBUtil;

@WebServlet("/products/*")
public class ProductServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String pathInfo = request.getPathInfo(); // /2 hoặc null

        try (Connection conn = DBUtil.getConnection()) {
            if (pathInfo == null || pathInfo.equals("/")) {
                // ✅ GET /products → trả về danh sách
                String sql = "SELECT * FROM product";
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery();

                JSONArray arr = new JSONArray();
                while (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", rs.getInt("id"));
                    obj.put("name", rs.getString("name"));
                    obj.put("description", rs.getString("description"));
                    obj.put("price", rs.getDouble("price"));
                    obj.put("image_url", rs.getString("image_url"));
                    arr.put(obj);
                }
                out.print(arr.toString());
            } else {
                // ✅ GET /products/{id} → trả về 1 sản phẩm
                int id = Integer.parseInt(pathInfo.substring(1));
                String sql = "SELECT * FROM product WHERE id = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, id);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    JSONObject obj = new JSONObject();
                    obj.put("id", rs.getInt("id"));
                    obj.put("name", rs.getString("name"));
                    obj.put("description", rs.getString("description"));
                    obj.put("price", rs.getDouble("price"));
                    obj.put("image_url", rs.getString("image_url"));
                    out.print(obj.toString());
                } else {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    out.print("{}\n");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            out.close();
        }
    }
}
