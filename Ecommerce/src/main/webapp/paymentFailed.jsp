
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

String message =
        (String) session.getAttribute("paymentMessage");

if (message == null) {
    message = "Payment failed or was cancelled.";
}

session.removeAttribute("paymentMessage");
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Payment Failed - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=7">

</head>

<body>

<jsp:include page="navbar.jsp" />

<div class="form-container">

    <h2>Payment Failed</h2>

    <p>
        <%=message%>
    </p>

    <br>

    <a href="CartServlet">
        <button type="button">
            Return to Cart
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

