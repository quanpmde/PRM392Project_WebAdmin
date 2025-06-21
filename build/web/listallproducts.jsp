<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.*" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    List<Product> products = (List<Product>) request.getAttribute("products");
%>
<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Admin - Dashboard</title>

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
                        <form method="get" action="list-products" class="mb-4">
                            <div class="form-row">
                                <div class="col-md-3">
                                    <input type="text" name="search" value="<%= request.getParameter("search") != null ? request.getParameter("search") : "" %>" class="form-control" placeholder="Tìm theo tên sách">
                                </div>
                                <div class="col-md-2">
                                    <select name="categoryId" class="form-control">
                                        <option value="">-- Thể loại --</option>
                                        <% List<Category> categories = (List<Category>) request.getAttribute("categories");
                   for (Category c : categories) { %>
                                        <option value="<%= c.getId() %>" <%= (request.getParameter("categoryId") != null && request.getParameter("categoryId").equals(String.valueOf(c.getId()))) ? "selected" : "" %> >
                                            <%= c.getName() %>
                                        </option>
                                        <% } %>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <select name="status" class="form-control">
                                        <option value="">-- Trạng thái --</option>
                                        <option value="0" <%= "0".equals(request.getParameter("status")) ? "selected" : "" %>>Khả dụng</option>
                                        <option value="1" <%= "1".equals(request.getParameter("status")) ? "selected" : "" %>>Không thể bán</option>
                                    </select>
                                </div>
                                <div class="col-md-2">
                                    <input type="number" name="minPrice" class="form-control" placeholder="Giá từ" value="<%= request.getParameter("minPrice") != null ? request.getParameter("minPrice") : "" %>">
                                </div>
                                <div class="col-md-2">
                                    <input type="number" name="maxPrice" class="form-control" placeholder="Đến" value="<%= request.getParameter("maxPrice") != null ? request.getParameter("maxPrice") : "" %>">
                                </div>
                                <div class="col-md-1">
                                    <button class="btn btn-primary btn-block">Lọc</button>
                                </div>
                            </div>
                        </form>

                        <h2 class="text-center mb-4">Danh sách sản phẩm</h2>
                        <div class="row">
                            <%
                                if (products != null && !products.isEmpty()) {
                                    for (Product p : products) {
                            %>
                            <div class="col-md-4">
                                <div class="product-card">
                                    <a href="view-book-detail?id=<%= p.getId() %>">
                                        <img src="<%= p.getImage_url() %>" alt="Product Image" class="product-img mb-3"/>
                                    </a>
                                    <h5>
                                        <a href="view-book-detail?id=<%= p.getId() %>"><%= p.getName() %></a>
                                    </h5>
                                    <p><%= p.getDescription() %></p>
                                    <p><strong>Giá:</strong> $<%= p.getPrice() %></p>
                                    <p><strong>Thể loại:</strong> <%= p.getCategory().getName() %></p>
                                    <p><strong>Số lượng:</strong> <%= p.getQuantity() %></p>
                                    <p><strong>Trạng thái:</strong>
                                        <%= (p.getStatus() == 0) ? "Khả dụng" : "Không thể bán" %>
                                    </p>
                                </div>
                            </div>
                            <%
                                    }
                                } else {
                            %>
                            <div class="col-12">
                                <p class="text-center text-muted">Không có sản phẩm nào.</p>
                            </div>
                            <%
                                }
                            %>
                        </div>
                    </div>

                </div>
                <div>
                    <% int totalPages = (Integer) request.getAttribute("totalPages");
   int currentPage = (Integer) request.getAttribute("currentPage"); %>

                    <nav>
                        <ul class="pagination justify-content-center">
                            <% for (int i = 1; i <= totalPages; i++) { %>
                            <li class="page-item <%= (i == currentPage) ? "active" : "" %>">
                                <a class="page-link" href="?page=<%= i %>"><%= i %></a>
                            </li>
                            <% } %>
                        </ul>
                    </nav>
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