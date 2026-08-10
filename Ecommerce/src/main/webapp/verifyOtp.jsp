<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="model.User" %>

<%
User pendingUser =
        (User) session.getAttribute("pendingUser");

if(pendingUser == null){

    response.sendRedirect("register.jsp");
    return;
}

String error =
        (String) request.getAttribute("error");

String success =
        (String) request.getAttribute("success");
%>

<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Verify OTP - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=600">

<style>

.otp-container{
    width:420px;
    max-width:92%;
    margin:60px auto;
    background:white;
    padding:35px;
    border-radius:14px;
    text-align:center;
    box-shadow:0 8px 24px rgba(15,23,42,0.15);
}

.otp-container h2{
    color:#172554;
    margin-bottom:12px;
}

.otp-container p{
    color:#64748b;
    line-height:1.6;
    margin-bottom:20px;
}

.otp-email{
    color:#1e3a8a;
    font-weight:bold;
}

.otp-input{
    width:100%;
    padding:14px;
    text-align:center;
    font-size:24px;
    font-weight:bold;
    letter-spacing:8px;
    border:1px solid #cbd5e1;
    border-radius:8px;
    margin:15px 0;
}

.otp-input:focus{
    outline:none;
    border-color:#2563eb;
    box-shadow:0 0 0 3px rgba(37,99,235,0.12);
}

.verify-btn{
    width:100%;
    padding:12px 20px;
    background:linear-gradient(135deg,#1e3a8a,#2563eb);
    color:white;
    border:none;
    border-radius:8px;
    font-weight:bold;
    cursor:pointer;
}

.verify-btn:hover{
    background:linear-gradient(135deg,#172554,#1d4ed8);
}

.otp-error{
    background:#fee2e2;
    color:#b91c1c;
    border:1px solid #fecaca;
    padding:12px;
    border-radius:8px;
    margin-bottom:15px;
}

.otp-success{
    background:#dcfce7;
    color:#166534;
    border:1px solid #bbf7d0;
    padding:12px;
    border-radius:8px;
    margin-bottom:15px;
}

.resend-link{
    display:inline-block;
    margin-top:18px;
    color:#1d4ed8;
    font-weight:bold;
    text-decoration:none;
}

.resend-link:hover{
    text-decoration:underline;
}

</style>

</head>


<body>


<jsp:include page="navbar.jsp" />


<div class="otp-container">


    <h2>
        Verify Your Email
    </h2>


    <p>
        We sent a 6-digit verification code to
        <span class="otp-email">
            <%=pendingUser.getEmail()%>
        </span>
    </p>


    <% if(error != null){ %>

        <div class="otp-error">
            <%=error%>
        </div>

    <% } %>


    <% if(success != null){ %>

        <div class="otp-success">
            <%=success%>
        </div>

    <% } %>


    <form action="<%=request.getContextPath()%>/VerifyOtpServlet"
          method="post">


        <input type="text"
               name="otp"
               class="otp-input"
               placeholder="000000"
               maxlength="6"
               inputmode="numeric"
               pattern="[0-9]{6}"
               required>


        <button type="submit"
                class="verify-btn">

            Verify OTP

        </button>


    </form>


    <a href="<%=request.getContextPath()%>/ResendOtpServlet"
       class="resend-link">

        Resend OTP

    </a>


</div>


</body>

</html>