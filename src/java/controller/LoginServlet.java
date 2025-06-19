package controller;

import DAO.DBUtil;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.*;
import java.sql.*;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();

        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM user WHERE username=? AND password=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                out.print("{\"success\": true, \"username\": \"" + username + "\"}");
            } else {
                out.print("{\"success\": false}");
            }
        } catch (Exception e) {
            out.print("{\"success\": false, \"error\": \"" + e.getMessage() + "\"}");
        }
    }
}
