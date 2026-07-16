<%
if(session.getAttribute("user") == null){
    response.sendRedirect("login.jsp");
    return;
}
%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.CartItem" %>

<!DOCTYPE html>
<html>

<head>

<title>My Cart - EMart</title>

<link rel="stylesheet" href="assets/css/style.css">

</head>


<body>


<jsp:include page="navbar.jsp" />


<h1 style="text-align:center; margin:30px;">
    My Cart
</h1>



<div class="products-container">


<%

ArrayList<CartItem> cart =
(ArrayList<CartItem>) request.getAttribute("cart");


double total = 0;


if(cart != null && !cart.isEmpty()){


for(CartItem item : cart){


total = total + (item.getPrice() * item.getQuantity());


%>


<div class="product-card">


<img src="<%=item.getImage()%>"
     width="200"
     height="200">


<h2>
<%=item.getProductName()%>
</h2>
<a href="checkout.jsp">

<button>
Checkout
</button>

</a>

<p>
Price: Rs. <%=item.getPrice()%>
</p>


<p>
Quantity: <%=item.getQuantity()%>
</p>


<a href="CartServlet?action=remove&id=<%=item.getId()%>">

<button>
Remove
</button>

</a>


</div>


<%

}

%>


</div>


<h2 style="text-align:center; margin:30px;">
Total: Rs. <%=total%>
</h2>


<%

}

else{

%>


<h2 style="text-align:center;">
Your cart is empty
</h2>


<%

}

%>


</body>

</html>