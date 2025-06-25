package controller;

import DAO.DBUtil;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8"); // Xử lý tiếng Việt nếu có
        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullname = request.getParameter("fullname");
        String email = request.getParameter("email");

        try (Connection conn = DBUtil.getConnection()) {

            // ✅ Kiểm tra password hợp lệ: ≥8 ký tự và có 1 ký tự đặc biệt
            String passwordRegex = "^(?=.*[!@#$%^&*()_+=\\-{};':\"\\\\|,.<>/?]).{8,}$";
            if (!password.matches(passwordRegex)) {
                response.getWriter().write("Password must be at least 8 characters and contain a special character.");
                return;
            }

            // ✅ Kiểm tra username/email trùng
            String checkSql = "SELECT * FROM user WHERE username = ? OR email = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                response.getWriter().write("Username or email already exists!");
                return;
            }

            // ✅ Insert nếu không trùng
            String insertSql = "INSERT INTO user (username, password, fullname, email, role) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(insertSql);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, fullname);
            ps.setString(4, email);
            ps.setInt(5, 0); // role mặc định = 0

            int result = ps.executeUpdate();
            response.getWriter().write(result > 0 ? "Register successful!" : "Register failed!");

        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().write("Error occurred!");
        }
    }
}
