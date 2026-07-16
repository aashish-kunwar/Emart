<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Product" %>
<%@ page import="model.User" %>

<!DOCTYPE html>
<html>

<head>

<title>Products - EMart</title>

<link rel="stylesheet" href="assets/css/style.css">

</head>

<body>

<jsp:include page="navbar.jsp" />

<%

String message = (String)session.getAttribute("message");

if(message != null){

%>

<h3 style="text-align:center; color:green;">
<%=message%>
</h3>

<%

session.removeAttribute("message");

}

%>

<h1 style="text-align:center; margin:30px;">
Our Products
</h1>

<!-- Search Bar -->
<div class="search-box">

<form action="ProductServlet" method="get">

<input type="hidden" name="action" value="search">

<input type="text"
       name="keyword"
       placeholder="Search products..."
       style="padding:8px; width:250px;">

<button type="submit">
Search
</button>

</form>

</div>

<%

User user = (User)session.getAttribute("user");

if(user != null && user.getRole().equals("admin")){

%>

<div style="text-align:center; margin-bottom:20px;">

<a href="addProduct.jsp">

<button>
Add New Product
</button>

</a>

</div>

<%

}

%>

<div class="products-container">

<%

ArrayList<Product> products =
(ArrayList<Product>)request.getAttribute("products");

if(products != null && !products.isEmpty()){

for(Product p : products){

%>

<div class="product-card">

<img src="<%=p.getImage()%>"
     width="200"
     height="200">

<h2>
<%=p.getName()%>
</h2>

<p>
<%=p.getDescription()%>
</p>

<h3>
Rs. <%=p.getPrice()%>
</h3>

<%

if(user != null && user.getRole().equals("customer")){

%>

<a href="CartServlet?action=add&id=<%=p.getId()%>">

<button>
Add To Cart
</button>

</a>

<%

}

if(user != null && user.getRole().equals("admin")){

%>

<a href="ProductServlet?action=delete&id=<%=p.getId()%>">

<button>
Delete
</button>

</a>

<%

}

%>

</div>

<%

}

}else{

%>

<h2 style="text-align:center;">
No Product Available
</h2>

<%

}

%>

</div>

</body>
</html>