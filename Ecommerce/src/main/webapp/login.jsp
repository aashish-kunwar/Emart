<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
String error =
        (String) request.getAttribute("error");

String registrationSuccess =
        (String) session.getAttribute(
                "registrationSuccess"
        );

if(registrationSuccess != null){

    session.removeAttribute(
            "registrationSuccess"
    );
}
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Login - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=700">

</head>


<body>


<jsp:include page="navbar.jsp" />


<div class="form-container">


    <!-- =========================
         REGISTRATION SUCCESS
    ========================= -->

    <%
    if(registrationSuccess != null){
    %>

        <div class="success-message-box">

            <div class="success-check">
                ✓
            </div>

            <h3>
                Account Created!
            </h3>

            <p>
                <%=registrationSuccess%>
            </p>

        </div>

    <%
    }
    %>



    <!-- =========================
         CUSTOMER LOGIN
    ========================= -->

    <h2>
        Customer Login
    </h2>


    <% if(error != null){ %>

        <div class="error-message">

            <%=error%>

        </div>

    <% } %>


    <form action="<%=request.getContextPath()%>/LoginServlet"
          method="post">


        <input type="hidden"
               name="role"
               value="customer">


        <input type="email"
               name="email"
               placeholder="Email"
               required>


        <input type="password"
               name="password"
               placeholder="Password"
               required>


        <button type="submit"
                class="login-button">

            Login

        </button>


    </form>


    <p>

        Don't have an account?

        <a href="<%=request.getContextPath()%>/register.jsp">

            Register Here

        </a>

    </p>


    <hr style="margin:30px 0;">



    <!-- =========================
         ADMIN LOGIN
    ========================= -->

    <h2>
        Admin Login
    </h2>


    <form action="<%=request.getContextPath()%>/LoginServlet"
          method="post">


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


        <button type="submit"
                class="login-button">

            Admin Login

        </button>


    </form>


</div>



<style>

/* =========================
   LOGIN BUTTON
========================= */

.form-container .login-button{

    display:block !important;

    width:100% !important;

    margin-top:12px !important;

    padding:12px 20px !important;

    background:
        linear-gradient(
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

    opacity:1 !important;

    visibility:visible !important;
}


.form-container .login-button:hover{

    background:
        linear-gradient(
            135deg,
            #172554,
            #1d4ed8
        ) !important;

    color:white !important;
}



/* =========================
   REGISTRATION SUCCESS BOX
========================= */

.success-message-box{

    background:#f0fdf4;

    border:1px solid #bbf7d0;

    padding:20px;

    border-radius:10px;

    margin-bottom:25px;

    text-align:center;
}


.success-message-box .success-check{

    width:55px;

    height:55px;

    margin:0 auto 12px;

    border-radius:50%;

    background:#16a34a;

    color:white;

    display:flex;

    justify-content:center;

    align-items:center;

    font-size:28px;

    font-weight:bold;
}


.success-message-box h3{

    color:#166534;

    margin-bottom:8px;
}


.success-message-box p{

    color:#15803d;

    margin:0;

    line-height:1.5;
}

</style>


</body>

</html>