<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.User" %>

<%
User user =
        (User) session.getAttribute("user");

if(user == null){

    response.sendRedirect("login.jsp");
    return;
}

ArrayList<CartItem> cart =
        (ArrayList<CartItem>) request.getAttribute("cart");

double total = 0.0;
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>My Cart - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=91">

</head>

<body>

<jsp:include page="navbar.jsp" />


<h1 style="text-align:center; margin:30px;">
    My Cart
</h1>


<div class="products-container">

<%
if(cart != null &&
   !cart.isEmpty()){

    for(CartItem item : cart){

        double subtotal =
                item.getPrice()
                * item.getQuantity();

        total += subtotal;
%>


<div class="product-card">

    <img src="<%=item.getImage()%>"
         alt="<%=item.getProductName()%>">


    <h2>
        <%=item.getProductName()%>
    </h2>


    <p>
        Price:
        Rs. <%=item.getPrice()%>
    </p>


    <!-- QUANTITY -->

    <div class="quantity-box">

        <a href="CartServlet?action=decrease&id=<%=item.getId()%>">

            <button type="button">
                -
            </button>

        </a>


        <span class="cart-quantity">
            <%=item.getQuantity()%>
        </span>


        <a href="CartServlet?action=increase&id=<%=item.getId()%>">

            <button type="button">
                +
            </button>

        </a>

    </div>


    <h3>
        Subtotal:
        Rs. <%=subtotal%>
    </h3>


    <a href="CartServlet?action=remove&id=<%=item.getId()%>">

        <button type="button">
            Remove
        </button>

    </a>

</div>


<%
    }
%>

</div>


<!-- CART TOTAL -->

<div class="card">

    <h2>
        Cart Total
    </h2>

    <h1 style="
        color:#1e3a8a;
        margin:15px 0;">

        Rs. <%=total%>

    </h1>


    <a href="checkout.jsp">

        <button type="button">
            Proceed To Checkout
        </button>

    </a>

</div>


<%
}
else{
%>

</div>


<div class="card">

    <h2>
        Your Cart Is Empty
    </h2>

    <p>
        Add some products to continue shopping.
    </p>

    <br>

    <a href="ProductServlet">

        <button type="button">
            Continue Shopping
        </button>

    </a>

</div>


<%
}
%>


</body>

</html>