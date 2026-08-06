<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="model.User" %>

<%
User user = (User) session.getAttribute("user");

if (user == null) {
    response.sendRedirect("login.jsp");
    return;
}

String paymentMethod =
        (String) session.getAttribute("paymentMethod");

Integer orderId =
        (Integer) session.getAttribute("placedOrderId");

String transactionCode =
        (String) session.getAttribute("esewaTransactionCode");

if (paymentMethod == null) {
    paymentMethod = "Not Available";
}
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Order Successful - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=7">

</head>

<body>

<jsp:include page="navbar.jsp" />

<div class="form-container">

    <h1>Order Placed Successfully 🎉</h1>

    <p>
        Thank you,
        <strong><%=user.getName()%></strong>,
        for shopping with EMart.
    </p>

    <br>

    <% if (orderId != null) { %>

        <p>
            Order ID:
            <strong><%=orderId%></strong>
        </p>

    <% } %>

    <p>
        Payment Method:
        <strong><%=paymentMethod%></strong>
    </p>

    <% if ("eSewa".equals(paymentMethod)
            && transactionCode != null) { %>

        <p>
            eSewa Transaction Code:
            <strong><%=transactionCode%></strong>
        </p>

        <p style="color:green;">
            Payment completed successfully.
        </p>

    <% } else if ("Cash on Delivery".equals(paymentMethod)) { %>

        <p style="color:#555;">
            Please pay when your order is delivered.
        </p>

    <% } %>

    <br>

    <a href="OrderServlet?action=myOrders">
        <button type="button">
            View My Orders
        </button>
    </a>

    <br><br>

    <a href="ProductServlet">
        <button type="button">
            Continue Shopping
        </button>
    </a>

</div>

</body>

</html>

<%
session.removeAttribute("paymentMethod");
session.removeAttribute("placedOrderId");
session.removeAttribute("esewaTransactionCode");
%>