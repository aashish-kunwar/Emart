<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Product" %>
<%@ page import="model.User" %>

<%
User user = (User) session.getAttribute("user");

ArrayList<Product> products =
        (ArrayList<Product>) request.getAttribute("products");

String message =
        (String) session.getAttribute("message");

if (message != null) {
    session.removeAttribute("message");
}

String selectedCategory =
        request.getParameter("category");

String keyword =
        request.getParameter("keyword");
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Products - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=9">

</head>

<body>

<jsp:include page="navbar.jsp" />

<h1 style="text-align:center; margin:30px 0 15px;">
    Our Products
</h1>

<% if (message != null) { %>

    <div class="success-message">
        <%=message%>
    </div>

<% } %>

<!-- Search Bar -->

<div class="search-box">

    <form action="ProductServlet" method="get">

        <input type="hidden"
               name="action"
               value="search">

        <input type="text"
               name="keyword"
               value="<%=keyword != null ? keyword : ""%>"
               placeholder="Search products...">

        <button type="submit">
            Search
        </button>

    </form>

</div>

<!-- Category Filter -->

<div class="category-filter">

    <a href="ProductServlet">
        <button type="button">
            All
        </button>
    </a>

    <a href="ProductServlet?action=category&category=Electronics">
        <button type="button">
            Electronics
        </button>
    </a>

    <a href="ProductServlet?action=category&category=Fashion">
        <button type="button">
            Fashion
        </button>
    </a>

    <a href="ProductServlet?action=category&category=Groceries">
        <button type="button">
            Groceries
        </button>
    </a>

    <a href="ProductServlet?action=category&category=Handicrafts">
        <button type="button">
            Handicrafts
        </button>
    </a>

    <a href="ProductServlet?action=category&category=Books">
        <button type="button">
            Books
        </button>
    </a>

    <a href="ProductServlet?action=category&category=Others">
        <button type="button">
            Others
        </button>
    </a>

</div>

<% if (user != null && "admin".equals(user.getRole())) { %>

    <div style="text-align:center; margin:25px 0;">

        <a href="addProduct.jsp">

            <button type="button">
                Add New Product
            </button>

        </a>

    </div>

<% } %>

<div class="products-container">

<%
if (products != null && !products.isEmpty()) {

    for (Product p : products) {
%>

    <div class="product-card">

        <img src="<%=p.getImage()%>"
             alt="<%=p.getName()%>">

        <h2>
            <%=p.getName()%>
        </h2>

        <p class="product-category">
            <%=p.getCategory()%>
        </p>

        <p>
            <%=p.getDescription()%>
        </p>

        <h3>
            Rs. <%=p.getPrice()%>
        </h3>

        <% if (user != null
                && "customer".equals(user.getRole())) { %>

            <a href="CartServlet?action=add&id=<%=p.getId()%>">

                <button type="button">
                    Add To Cart
                </button>

            </a>

        <% } %>

        <% if (user != null
                && "admin".equals(user.getRole())) { %>

            <a href="ProductServlet?action=delete&id=<%=p.getId()%>"
               onclick="return confirm('Delete this product?');">

                <button type="button">
                    Delete
                </button>

            </a>

        <% } %>

    </div>

<%
    }

} else {
%>

    <h2 style="text-align:center; width:100%; margin:40px 0;">
        No Products Available
    </h2>

<%
}
%>

</div>

</body>

</html>