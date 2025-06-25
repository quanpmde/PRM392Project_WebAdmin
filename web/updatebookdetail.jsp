<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.*" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Product product = (Product) request.getAttribute("product");
    if (product == null) {
        response.sendRedirect("listallproducts");
        return;
    }
    List<Category> categories = (List<Category>) request.getAttribute("categories");
%>
<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Update Product</title>

        <!-- Custom fonts for this template-->
        <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">

        <!-- Custom styles for this template-->
        <link href="css/sb-admin-2.min.css" rel="stylesheet">
        <style>
            .product-card {
                box-shadow: 0 0 10px rgba(0,0,0,0.1);
                border-radius: 8px;
                padding: 15px;
                margin: 10px;
                height: 100%;
            }
            .product-img {
                width: 100%;
                height: 200px;
                object-fit: contain;
            }
        </style>

    </head>

    <body id="page-top">

        <!-- Page Wrapper -->
        <div id="wrapper">

            <!-- Sidebar -->
            <jsp:include page="includes/sidebar.jsp" />

            <!-- Content Wrapper -->
            <div id="content-wrapper" class="d-flex flex-column">

                <!-- Main Content -->
                <div id="content">

                    <!-- Topbar -->
                    <jsp:include page="includes/topbar.jsp" />

                    <div class="container mt-5">
                        <h2>Cập nhật sản phẩm</h2>
                        <form action="update-book" method="post" enctype="multipart/form-data">
                            <input type="hidden" name="id" value="<%= product.getId() %>"/>
                            <label>Tên:</label><br>
                            <input type="text" name="name" value="<%= product.getName() %>" required/><br><br>

                            <label>Thể loại:</label><br>
                            <select name="categoryId" required>
                                <%
                                    if (categories != null) {
                                        for (Category c : categories) {
                                %>
                                <option value="<%= c.getId() %>" <%= (product.getCategory() != null && product.getCategory().getId() == c.getId()) ? "selected" : "" %>>
                                    <%= c.getName() %>
                                </option>
                                <%
                                        }
                                    }
                                %>
                            </select><br><br>

                            <label>Giá:</label><br>
                            <input type="number" name="price" value="<%= product.getPrice() %>" required/><br><br>

                            <label>Số lượng:</label><br>
                            <input type="number" name="quantity" value="<%= product.getQuantity() %>" required/><br><br>

                            <label>Trạng thái:</label><br>
                            <select name="status" required>
                                <option value="0" <%= (product.getStatus() == 0) ? "selected" : "" %>>Khả dụng</option>
                                <option value="1" <%= (product.getStatus() == 1) ? "selected" : "" %>>Không thể bán</option>
                            </select><br><br>

                            <label>Mô tả:</label><br>
                            <textarea name="description" rows="5" cols="40"><%= product.getDescription() %></textarea><br><br>

                            <label>Link hình ảnh (hoặc để trống nếu upload file):</label><br>
                            <input type="text" name="image_url" value="<%= product.getImage_url() %>"/><br><br>

                            <label>Hoặc chọn file hình ảnh (.jpg, .png):</label><br>
                            <input type="file" name="image_file" accept=".jpg,.jpeg,.png"/><br><br>

                            <button type="submit" class="btn btn-success">Cập nhật</button>
                            <a href="list-products" class="btn btn-secondary">Hủy</a>
                        </form>
                    </div>

                </div>
                <!-- End of Main Content -->

                <!-- Footer -->
                <jsp:include page="includes/footer.jsp" />

            </div>
            <!-- End of Content Wrapper -->

        </div>
        <!-- End of Page Wrapper -->

        <!-- Scroll to Top Button-->
        <a class="scroll-to-top rounded" href="#page-top">
            <i class="fas fa-angle-up"></i>
        </a>

        <!-- Logout Modal-->
        <jsp:include page="includes/logoutModal.jsp" />

        <!-- Bootstrap core JavaScript-->
        <script src="vendor/jquery/jquery.min.js"></script>
        <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>

        <!-- Core plugin JavaScript-->
        <script src="vendor/jquery-easing/jquery.easing.min.js"></script>

        <!-- Custom scripts for all pages-->
        <script src="js/sb-admin-2.min.js"></script>

        <!-- Page level plugins -->
        <script src="vendor/chart.js/Chart.min.js"></script>

        <!-- Page level custom scripts -->
        <script src="js/demo/chart-area-demo.js"></script>
        <script src="js/demo/chart-pie-demo.js"></script>

    </body>

</html>