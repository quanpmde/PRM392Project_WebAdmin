package controller;

import DAO.ProductDAO;
import model.Product;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/add-book")
public class AddBookServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        // Hiển thị trang form thêm sách khi truy cập GET
        request.getRequestDispatcher("addbook.jsp").forward(request, response);
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int price = Integer.parseInt(request.getParameter("price"));
        String imageUrl = request.getParameter("image_url");

        Product newProduct = new Product();
        newProduct.setName(name);
        newProduct.setDescription(description);
        newProduct.setPrice(price);
        newProduct.setImage_url(imageUrl);

        ProductDAO dao = new ProductDAO();
        boolean inserted = dao.insertProduct(newProduct);

        if (inserted) {
            response.sendRedirect("list-products?msg=Book added successfully!");
        } else {
            response.sendRedirect("addbook.jsp?msg=Failed to add book.");
        }
    }
}
