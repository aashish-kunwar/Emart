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
    <link rel="stylesheet" href="assets/css/style.css">
</head>
<body>

<jsp:include page="navbar.jsp" />

<div class="hero">
    <h1>Welcome to eMart, <%= user.getName() %> 👋</h1>
    <p>You have successfully logged in.</p>
</div>

<div class="features">

<%
if(user.getRole().equals("admin")){
%>

    <div class="card">
        <h3>Admin Dashboard</h3>
        <p>Manage your eMart store easily.</p>

        
        <a href="addProduct.jsp">Add Products</a>
        
        <a href="ProductServlet">View Products</a>
        
        <a href="AdminOrderServlet">Manage Orders</a>
    </div>

<%
}else{
%>

    <div class="card">
        <h3>Customer Dashboard</h3>
        <p>Start shopping your favourite products.</p>

        <br>
        <a href="ProductServlet">Browse Products</a>
        <br><br>
        <a href="CartServlet">View Cart</a>
        <br><br>
        <a href="OrderServlet?action=myOrders">My Orders</a>
    </div>

<%
}
%>

</div>

</body>
</html>