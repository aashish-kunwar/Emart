<%@ page import="model.User" %>

<!DOCTYPE html>
<html>

<head>

<title>Add Product - EMart</title>

<link rel="stylesheet" href="assets/css/style.css">

</head>


<body>


<jsp:include page="navbar.jsp" />


<%

User user = (User)session.getAttribute("user");


if(user == null || !user.getRole().equals("admin")){

    response.sendRedirect("login.jsp");
    return;

}

%>



<div class="form-container">


<h1>
Add New Product
</h1>



<form action="ProductServlet" method="post">


<input type="hidden" name="action" value="add">


<label>
Product Name
</label>

<br>

<input type="text" name="name" required>


<br><br>


<label>
Description
</label>

<br>

<textarea name="description" required></textarea>


<br><br>


<label>
Price
</label>

<br>

<input type="number" name="price" required>


<br><br>


<label>
Image URL
</label>

<br>

<input type="text" name="image" required>


<br><br>


<button type="submit">
Add Product
</button>


</form>


</div>


</body>

</html>
