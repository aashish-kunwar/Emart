<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Order" %>

<!DOCTYPE html>
<html>

<head>

<title>My Orders - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=7">
</head>


<body>


<jsp:include page="navbar.jsp" />



<h1 style="text-align:center; margin:30px;">
My Orders
</h1>



<div class="products-container">


<%

ArrayList<Order> orders =
(ArrayList<Order>) request.getAttribute("orders");


if(orders != null && !orders.isEmpty()){


for(Order o : orders){

%>



<div class="product-card">


<h2>
Order ID: <%=o.getId()%>
</h2>


<p>
Total Amount:
Rs. <%=o.getTotalAmount()%>
</p>


<p>
Order Date:
<%=o.getOrderDate()%>
</p>


<h3>
Status:
<%=o.getStatus()%>
</h3>


</div>



<%

}

}

else{

%>


<h2 style="text-align:center;">
No Orders Found
</h2>


<%

}

%>


</div>


</body>

</html>