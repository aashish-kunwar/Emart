<%@ page import="model.User" %>

<!DOCTYPE html>
<html>

<head>

<title>Checkout - EMart</title>

<link rel="stylesheet" href="assets/css/style.css">

</head>


<body>


<jsp:include page="navbar.jsp" />


<%

User user = (User)session.getAttribute("user");


if(user == null){

    response.sendRedirect("login.jsp");
    return;

}

%>



<div class="form-container">


<h1>
Checkout
</h1>


<h3>
Customer:
<%=user.getName()%>
</h3>


<p>
Your order is ready.
</p>


<form action="OrderServlet" method="post">


<input type="hidden"
       name="action"
       value="placeOrder">



<button type="submit">

Place Order

</button>


</form>


</div>


</body>

</html>