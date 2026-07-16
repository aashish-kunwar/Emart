<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Order" %>

<!DOCTYPE html>
<html>

<head>

<title>Manage Orders - EMart</title>

<link rel="stylesheet" href="assets/css/style.css">

</head>


<body>


<jsp:include page="navbar.jsp" />



<h1 style="text-align:center; margin:30px;">
Manage Orders
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
Customer:
<%=o.getUserEmail()%>
</p>


<p>
Total:
Rs. <%=o.getTotalAmount()%>
</p>


<p>
Date:
<%=o.getOrderDate()%>
</p>


<p>
Current Status:
<b><%=o.getStatus()%></b>
</p>



<form action="AdminOrderServlet" method="get">


<input type="hidden"
       name="action"
       value="update">


<input type="hidden"
       name="id"
       value="<%=o.getId()%>">



<select name="status">

<option value="Pending">
Pending
</option>


<option value="Delivered">
Delivered
</option>


<option value="Cancelled">
Cancelled
</option>


</select>



<button type="submit">
Update
</button>


</form>



</div>



<%

}

}

else{

%>


<h2 style="text-align:center;">
No Orders Available
</h2>


<%

}

%>


</div>


</body>

</html>