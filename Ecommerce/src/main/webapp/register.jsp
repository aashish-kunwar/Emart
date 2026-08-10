<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
String error =
        (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>

<head>

<meta charset="UTF-8">

<title>Register - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=800">

</head>

<body>

<jsp:include page="navbar.jsp" />


<div class="form-container">

    <h2>
        Create Customer Account
    </h2>


    <% if(error != null){ %>

        <div class="error-message">

            <%=error%>

        </div>

    <% } %>


    <form action="<%=request.getContextPath()%>/RegisterServlet"
          method="post">


        <!-- NAME -->

        <label>
            Full Name
        </label>

        <input type="text"
               name="name"
               placeholder="Enter your full name"
               required>


        <!-- EMAIL -->

        <label>
            Email
        </label>

        <input type="email"
               name="email"
               placeholder="Enter your Gmail"
               autocomplete="off"
               required>


        <!-- PASSWORD -->

        <label>
            Password
        </label>

        <input type="password"
               name="password"
               placeholder="Create password"
               required>


        <!-- REGISTER -->

        <button type="submit"
                class="login-button">

            Send OTP

        </button>


    </form>


    <p style="margin-top:20px;">

        Already have an account?

        <a href="<%=request.getContextPath()%>/login.jsp">

            Login Here

        </a>

    </p>

</div>


<style>

.form-container .login-button{

    display:block !important;

    width:100% !important;

    margin-top:12px !important;

    padding:12px 20px !important;

    background:linear-gradient(
        135deg,
        #1e3a8a,
        #2563eb
    ) !important;

    color:white !important;

    border:none !important;

    border-radius:8px !important;

    font-size:15px !important;

    font-weight:bold !important;

    cursor:pointer !important;
}


.form-container .login-button:hover{

    background:linear-gradient(
        135deg,
        #172554,
        #1d4ed8
    ) !important;
}

</style>


</body>

</html>