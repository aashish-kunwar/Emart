<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="model.User" %>

<%
User user = (User) session.getAttribute("user");

if(user == null){
    response.sendRedirect("login.jsp");
    return;
}
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>EMart Home</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=7">

</head>

<body>

<jsp:include page="navbar.jsp" />

<div class="hero">

    <h1>
        Welcome to eMart, <%=user.getName()%> 👋
    </h1>

    <p>
        Discover products, manage your cart and track your orders.
    </p>

</div>

<div class="features">

<%
if("admin".equals(user.getRole())){
%>

    <div class="card">

        <h3>Admin Dashboard</h3>

        <p>
            Manage products and customer orders from one place.
        </p>

        <div class="dashboard-buttons">

            <a href="addProduct.jsp">
                <button type="button">
                    Add Products
                </button>
            </a>

            <a href="ProductServlet">
                <button type="button">
                    View Products
                </button>
            </a>

            <a href="AdminOrderServlet">
                <button type="button">
                    Manage Orders
                </button>
            </a>

        </div>

    </div>

<%
}else{
%>

    <div class="card">

        <h3>Customer Dashboard</h3>

        <p>
            Browse products, check your cart and track your orders.
        </p>

        <div class="dashboard-buttons">

            <a href="ProductServlet">
                <button type="button">
                    Browse Products
                </button>
            </a>

            <a href="CartServlet">
                <button type="button">
                    View Cart
                </button>
            </a>

            <a href="OrderServlet?action=myOrders">
                <button type="button">
                    My Orders
                </button>
            </a>

        </div>

    </div>

<%
}
%>

</div>

</body>

</html>