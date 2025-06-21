package controller;

import DAO.ProductDAO;
import model.Product;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/view-book-detail")
public class ViewBookDetailServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam == null || idParam.isEmpty()) {
            response.sendRedirect("list-products");
            return;
        }

        int id = Integer.parseInt(idParam);
        ProductDAO dao = new ProductDAO();
        Product product = dao.getProductById(id);

        if (product == null) {
            // Nếu không tìm thấy sản phẩm, redirect về danh sách
            response.sendRedirect("list-products");
            return;
        }

        request.setAttribute("product", product);
        request.getRequestDispatcher("viewbookdetail.jsp").forward(request, response);
    }
}
