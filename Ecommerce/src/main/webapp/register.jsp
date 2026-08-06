
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=7">

</head>
<body>

<div class="navbar">

    <h2>EMart</h2>

    <div class="menu">
        <a href="index.jsp">Home</a>
        <a href="login.jsp">Login</a>
        <a href="register.jsp">Register</a>
    </div>

</div>

<div class="form-container">

    <h2>Create Account</h2>

    <form action="RegisterServlet" method="post">

        <input type="text" name="name" placeholder="Full Name" required>

        <input type="email" name="email" placeholder="Email" required>

        <input type="password" name="password" placeholder="Password" required>

        <input type="submit" value="Register">

    </form>

    <p>
        Already have an account?
        <a href="login.jsp">Login</a>
    </p>

</div>

</body>
</html>

