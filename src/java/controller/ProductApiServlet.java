package controller;

import model.Category;
import model.Product;
import DAO.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

@WebServlet("/api/products")
public class ProductApiServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        ArrayList<Product> products = new ArrayList<>();

        String sql = "SELECT p.id, p.name, p.description, p.price, p.image_url, p.quantity, p.status, " +
                     "c.id as category_id, c.name as category_name " +
                     "FROM product p LEFT JOIN category c ON p.category_id = c.id " +
                     "WHERE p.status = 0";  // chỉ lấy sản phẩm có status = 0

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getInt("price"));
                p.setImage_url(rs.getString("image_url"));
                p.setQuantity(rs.getInt("quantity"));
                p.setStatus(rs.getInt("status"));

                Category category = new Category();
                category.setId(rs.getInt("category_id"));
                category.setName(rs.getString("category_name"));
                p.setCategory(category);

                products.add(p);
            }

            // Chuyển đổi danh sách sản phẩm thành JSON
            JSONArray jsonArray = new JSONArray();
            for (Product p : products) {
                JSONObject obj = new JSONObject();
                obj.put("id", p.getId());
                obj.put("name", p.getName());
                obj.put("description", p.getDescription());
                obj.put("price", p.getPrice());
                obj.put("image_url", p.getImage_url());
                obj.put("quantity", p.getQuantity());
                obj.put("status", p.getStatus());

                if (p.getCategory() != null) {
                    obj.put("category", p.getCategory().getName());
                    obj.put("category_id", p.getCategory().getId());
                } else {
                    obj.put("category", JSONObject.NULL);
                    obj.put("category_id", JSONObject.NULL);
                }

                jsonArray.put(obj);
            }

            out.print(jsonArray.toString());

        } catch (Exception e) {
            e.printStackTrace();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"server_error\"}");
        }
        out.flush();
    }
}
