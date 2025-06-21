/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import DAO.*;
import java.io.File;
import model.*;
import java.io.IOException;
import java.nio.file.Paths;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 *
 * @author Admin
 */
@WebServlet("/update-book")
@MultipartConfig(fileSizeThreshold = 1024 * 1024 * 2, // 2MB
        maxFileSize = 1024 * 1024 * 10, // 10MB
        maxRequestSize = 1024 * 1024 * 50)       // 50MB
public class UpdateBookServlet extends HttpServlet {

    private ProductDAO productDAO = new ProductDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // nếu cần xử lý UTF-8
        int id = Integer.parseInt(request.getParameter("id"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        int price = Integer.parseInt(request.getParameter("price"));
        String imageUrl = request.getParameter("image_url");
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        int status = Integer.parseInt(request.getParameter("status"));
        int categoryId = Integer.parseInt(request.getParameter("categoryId"));

        // Xử lý file upload nếu có
        Part filePart = request.getPart("image_file");
        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        if (fileName != null && !fileName.isEmpty()) {
            String appPath = request.getServletContext().getRealPath("");
            String uploadPath = appPath + File.separator + "img";

            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Lưu file vào thư mục img
            filePart.write(uploadPath + File.separator + fileName);

            // Gán imageUrl là đường dẫn tương đối tới ảnh trong thư mục img
            imageUrl = "img/" + fileName;
        }

        // Tạo đối tượng Category để gán vào Product
        Category category = new Category();
        category.setId(categoryId);

        // Tạo product đầy đủ
        Product p = new Product(id, name, description, price, imageUrl, category, quantity, status);

        boolean updated = productDAO.updateProduct(p);

        if (updated) {
            response.sendRedirect("view-book-detail?id=" + id);
        } else {
            // xử lý lỗi, có thể show thông báo lỗi
            request.setAttribute("errorMessage", "Cập nhật không thành công");
            request.setAttribute("product", p);
            request.getRequestDispatcher("updatebookdetail.jsp").forward(request, response);
        }
    }
}
