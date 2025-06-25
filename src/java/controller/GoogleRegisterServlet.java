package controller;

import DAO.DBUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;

@WebServlet("/GoogleRegisterServlet")
public class GoogleRegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");

        try (Connection conn = DBUtil.getConnection()) {
            // 🔍 Kiểm tra email đã tồn tại
            String checkSql = "SELECT * FROM user WHERE email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, email);
            ResultSet rs = checkStmt.executeQuery();

            if (!rs.next()) {
                // ✅ Email chưa tồn tại → thêm mới
                String sql = "INSERT INTO user (username, password, fullname, email, role) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, email); // Dùng email làm username
                stmt.setString(2, "");    // Mật khẩu rỗng cho Google
                stmt.setString(3, fullname);
                stmt.setString(4, email);
                stmt.setInt(5, 0); // role mặc định
                stmt.executeUpdate();
            }

            response.setContentType("text/plain");
            response.getWriter().write("success");

        } catch (Exception e) {
            e.printStackTrace();
            response.setContentType("text/plain");
            response.getWriter().write("error: " + e.getMessage());
        }
    }
}
