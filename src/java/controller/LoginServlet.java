package controller;

import DAO.DBUtil;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        System.out.println(username+ " "+ password);
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM user WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id");
                if (id == 1) {
                    // Nếu id = 1 thì đăng nhập thành công
                    HttpSession session = req.getSession();
                    session.setAttribute("username", username);
                    resp.sendRedirect("index.jsp");
                } else {
                    // Nếu id khác 1 thì coi như đăng nhập thất bại
                    req.setAttribute("error", "Tài khoản User không có quyền truy cập!");
                    RequestDispatcher dispatcher = req.getRequestDispatcher("login.jsp");
                    dispatcher.forward(req, resp);
                }
            } else {
                // Nếu thất bại, quay lại login.jsp và hiện thông báo lỗi
                req.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu!");
                RequestDispatcher dispatcher = req.getRequestDispatcher("login.jsp");
                dispatcher.forward(req, resp);
            }
        } catch (Exception e) {
            req.setAttribute("error", "Lỗi máy chủ: " + e.getMessage());
            RequestDispatcher dispatcher = req.getRequestDispatcher("login.jsp");
            dispatcher.forward(req, resp);
        }
    }
}
