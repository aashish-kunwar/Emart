<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.util.Locale" %>

<%@ page import="dao.CartDAO" %>

<%@ page import="model.User" %>
<%@ page import="model.CartItem" %>

<%@ page import="helper.EsewaUtil" %>


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


double total = 0.0;

for (CartItem item : cart) {

    total += item.getPrice() * item.getQuantity();
}


/*
 * eSewa requires amounts without comma formatting.
 * Example: 1500.00
 */
String amount =
        String.format(Locale.US, "%.2f", total);

String taxAmount = "0";
String serviceCharge = "0";
String deliveryCharge = "0";

String totalAmount = amount;


/*
 * A unique transaction ID is required for every request.
 * UUID contains only letters, numbers and hyphens.
 */
String transactionUuid =
        UUID.randomUUID().toString();


String signature =
        EsewaUtil.generateSignature(
                totalAmount,
                transactionUuid
        );


/*
 * Save transaction information temporarily.
 * SuccessServlet will use these values for verification.
 */
session.setAttribute(
        "esewaTransactionUuid",
        transactionUuid
);

session.setAttribute(
        "esewaTotalAmount",
        totalAmount
);


/*
 * Create absolute callback URLs.
 */
String baseUrl =
        request.getScheme()
        + "://"
        + request.getServerName()
        + ":"
        + request.getServerPort()
        + request.getContextPath();


String successUrl =
        baseUrl + "/EsewaSuccessServlet";

String failureUrl =
        baseUrl + "/EsewaFailureServlet";
%>


<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Pay with eSewa - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=7">
</head>


<body>


<jsp:include page="navbar.jsp" />


<div class="form-container">


    <h2>Pay with eSewa</h2>


    <p>
        Customer:
        <strong><%=user.getName()%></strong>
    </p>


    <p>
        Total Amount:
        <strong>Rs. <%=totalAmount%></strong>
    </p>


    <br>


    <form action="<%=EsewaUtil.PAYMENT_URL%>"
          method="post">


        <input type="hidden"
               name="amount"
               value="<%=amount%>">


        <input type="hidden"
               name="tax_amount"
               value="<%=taxAmount%>">


        <input type="hidden"
               name="total_amount"
               value="<%=totalAmount%>">


        <input type="hidden"
               name="transaction_uuid"
               value="<%=transactionUuid%>">


        <input type="hidden"
               name="product_code"
               value="<%=EsewaUtil.PRODUCT_CODE%>">


        <input type="hidden"
               name="product_service_charge"
               value="<%=serviceCharge%>">


        <input type="hidden"
               name="product_delivery_charge"
               value="<%=deliveryCharge%>">


        <input type="hidden"
               name="success_url"
               value="<%=successUrl%>">


        <input type="hidden"
               name="failure_url"
               value="<%=failureUrl%>">


        <input type="hidden"
               name="signed_field_names"
               value="total_amount,transaction_uuid,product_code">


        <input type="hidden"
               name="signature"
               value="<%=signature%>">


        <button type="submit">
            Continue to eSewa
        </button>


    </form>


    <br>


    <a href="CartServlet">
        Back to Cart
    </a>


</div>


</body>

</html>