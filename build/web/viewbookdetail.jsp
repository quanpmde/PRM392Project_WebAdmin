<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Product" %>
<%@ page import="model.Category" %>
<%
    String username = (String) session.getAttribute("username");
    if (username == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    Product product = (Product) request.getAttribute("product");
    if (product == null) {
        response.sendRedirect("list-products");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">

    <head>

        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Chi tiết sách - <%= product.getName() %></title>

        <!-- Custom fonts for this template-->
        <link href="vendor/fontawesome-free/css/all.min.css" rel="stylesheet" type="text/css">
        <link
            href="https://fonts.googleapis.com/css?family=Nunito:200,200i,300,300i,400,400i,600,600i,700,700i,800,800i,900,900i"
            rel="stylesheet">

        <!-- Custom styles for this template-->
        <link href="css/sb-admin-2.min.css" rel="stylesheet">

        <style>
            .product-detail-img {
                max-width: 300px;
                max-height: 400px;
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
                        <a href="list-products" class="btn btn-primary">Quay lại danh sách</a>
                        <h2>Chi tiết sản phẩm</h2>
                        <div class="row">
                            <div class="col-md-4">
                                <img src="<%= product.getImage_url() %>" alt="Ảnh sản phẩm" class="product-detail-img" />
                            </div>
                            <div class="col-md-8">
                                <h3><%= product.getName() %></h3>
                                <p><strong>Thể loại:</strong> 
                                    <%
                                        Category c = product.getCategory();
                                        if (c != null) {
                                            out.print(c.getName());
                                        } else {
                                            out.print("Không xác định");
                                        }
                                    %>
                                </p>                               
                                <p><strong>Giá:</strong> $<%= product.getPrice() %></p>
                                <p><strong>Số lượng:</strong> <%= product.getQuantity() %></p>
                                
                                <p><strong>Trạng thái:</strong>
                                    <%= (product.getStatus() == 0) ? "Khả dụng" : "Không thể bán" %>
                                </p>
                                <p><strong>Mô tả:</strong> <%= product.getDescription() %></p>
                            </div>                           
                        </div>

                        <div class="mt-4">
                            <a href="update-book-detail?id=<%= product.getId() %>" class="btn btn-primary">Chỉnh sửa</a>

                            <form action="delete-book" method="post" style="display:inline-block;" 
                                  onsubmit="return confirm('Bạn có chắc chắn muốn ẩn sản phẩm này không?');">
                                <input type="hidden" name="id" value="<%= product.getId() %>"/>
                                <button type="submit" class="btn btn-danger">Ẩn</button>
                            </form>
                        </div>
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