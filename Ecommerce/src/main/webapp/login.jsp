
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>

<link rel="stylesheet" href="assets/css/style.css">

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

    <h2>Login</h2>

    <form action="LoginServlet" method="post">
    <input type="hidden" name="role" value="customer">

        <input type="email" name="email" placeholder="Email" required>

        <input type="password" name="password" placeholder="Password" required>

        <input type="submit" value="Login">

    </form>

    <br>

    <a href="register.jsp">Create New Account</a>

    <br><br>

    <h3>Admin Login</h3>

    <form action="LoginServlet" method="post">

        <input type="hidden" name="role" value="admin">

        <input type="email" name="email" placeholder="Admin Email" required>

        <input type="password" name="password" placeholder="Admin Password" required>

        <input type="submit" value="Admin Login">

    </form>

</div>

</body>
</html>

