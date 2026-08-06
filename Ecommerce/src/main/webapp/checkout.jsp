
<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.CartDAO" %>
<%@ page import="model.User" %>
<%@ page import="model.CartItem" %>

<%
User user = (User) session.getAttribute("user");

if (user == null) {
    response.sendRedirect("login.jsp");
    return;
}

if (!"customer".equals(user.getRole())) {
    response.sendRedirect("home.jsp");
    return;
}

CartDAO cartDAO = new CartDAO();

ArrayList<CartItem> cart =
        cartDAO.getCart(user.getEmail());

if (cart == null || cart.isEmpty()) {
    response.sendRedirect("CartServlet");
    return;
}

double total = 0;

for (CartItem item : cart) {
    total += item.getPrice() * item.getQuantity();
}
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Checkout - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=7">

</head>

<body>

<jsp:include page="navbar.jsp" />

<div class="form-container">

    <h2>Checkout</h2>

    <p>
        Customer:
        <strong><%=user.getName()%></strong>
    </p>

    <p>
        Email:
        <strong><%=user.getEmail()%></strong>
    </p>

    <br>

    <h3>
        Order Total: Rs. <%=String.format("%.2f", total)%>
    </h3>

    <br>

    <h3>Select Payment Method</h3>

    <br>

    <!-- Cash on Delivery -->

    <form action="OrderServlet" method="post">

        <input type="hidden"
               name="paymentMethod"
               value="Cash on Delivery">

        <button type="submit">
            Cash on Delivery
        </button>

    </form>

    <br>

    <!-- eSewa Payment -->

    <form action="esewaPay.jsp" method="get">

        <button type="submit">
            Pay with eSewa
        </button>

    </form>

    <br>

    <a href="CartServlet">
        Back to Cart
    </a>

</div>

</body>

</html>

