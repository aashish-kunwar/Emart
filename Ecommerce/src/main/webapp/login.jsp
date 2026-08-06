<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Login</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=7">

</head>

<body>

<!-- Common Navbar -->
<jsp:include page="navbar.jsp" />

<div class="form-container">

    <h2>Customer Login</h2>

    <form action="LoginServlet" method="post">

        <input type="hidden" name="role" value="customer">

        <input type="email"
               name="email"
               placeholder="Email"
               required>

        <input type="password"
               name="password"
               placeholder="Password"
               required>

        <input type="submit"
               value="Login">

    </form>

    <br>

    <p>
        Don't have an account?
        <a href="register.jsp">Register Here</a>
    </p>

    <hr style="margin:30px 0;">

    <h2>Admin Login</h2>

    <form action="LoginServlet" method="post">

        <input type="hidden"
               name="role"
               value="admin">

        <input type="email"
               name="email"
               placeholder="Admin Email"
               required>

        <input type="password"
               name="password"
               placeholder="Admin Password"
               required>

        <input type="submit"
               value="Admin Login">

    </form>

</div>

</body>

</html>
