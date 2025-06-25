/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package androidController;

import DAO.DBUtil;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.*;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Admin
 */
@WebServlet("/api/login")
public class LoginApiServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM user WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Đăng nhập thành công, không giới hạn theo ID
                int id = rs.getInt("id");

                // Nếu muốn lưu session vẫn được
                HttpSession session = req.getSession();
                session.setAttribute("username", username);
                session.setAttribute("userId", id);

                resp.getWriter().write("{\"success\": true, \"id\": " + id + ", \"username\": \"" + username + "\"}");
            } else {
                // Sai tài khoản hoặc mật khẩu
                resp.getWriter().write("{\"success\": false, \"error\": \"invalid_credentials\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            resp.getWriter().write("{\"success\": false, \"error\": \"server_error\"}");
        }
    }
}
