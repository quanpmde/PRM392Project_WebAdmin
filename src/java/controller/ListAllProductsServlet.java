package controller;

import DAO.*;
import model.*;
import java.io.IOException;
import java.util.List;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/list-products")
public class ListAllProductsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ProductDAO dao = new ProductDAO();
        CategoryDAO categoryDAO = new CategoryDAO();

        String keyword = request.getParameter("search");
        String categoryStr = request.getParameter("categoryId");
        String statusStr = request.getParameter("status");
        String minStr = request.getParameter("minPrice");
        String maxStr = request.getParameter("maxPrice");

        Integer categoryId = (categoryStr != null && !categoryStr.isEmpty()) ? Integer.parseInt(categoryStr) : null;
        Integer status = (statusStr != null && !statusStr.isEmpty()) ? Integer.parseInt(statusStr) : null;
        Integer minPrice = (minStr != null && !minStr.isEmpty()) ? Integer.parseInt(minStr) : null;
        Integer maxPrice = (maxStr != null && !maxStr.isEmpty()) ? Integer.parseInt(maxStr) : null;

        int page = 1;
        int pageSize = 6;

        String pageStr = request.getParameter("page");
        if (pageStr != null && !pageStr.isEmpty()) {
            page = Integer.parseInt(pageStr);
        }

        int offset = (page - 1) * pageSize;

        List<Product> products = dao.filterProducts(keyword, categoryId, status, minPrice, maxPrice, offset, pageSize);
        int totalProducts = dao.countFilteredProducts(keyword, categoryId, status, minPrice, maxPrice);
        int totalPages = (int) Math.ceil((double) totalProducts / pageSize);

        request.setAttribute("products", products);
        request.setAttribute("categories", categoryDAO.getAllCategories());
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);

        request.getRequestDispatcher("listallproducts.jsp").forward(request, response);
    }
}
