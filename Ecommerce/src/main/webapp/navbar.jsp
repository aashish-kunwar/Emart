<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="model.User" %>

<%
User navUser =
        (User) session.getAttribute("user");


/* =========================
   LOGO DESTINATION
========================= */

String logoLink;


if(navUser != null &&
   "admin".equalsIgnoreCase(
           navUser.getRole())) {

    // ADMIN -> DASHBOARD

    logoLink =
            request.getContextPath()
            + "/adminDashboard.jsp";
}

else if(navUser != null &&
        "customer".equalsIgnoreCase(
                navUser.getRole())) {

    // CUSTOMER -> CUSTOMER HOME

    logoLink =
            request.getContextPath()
            + "/home.jsp";
}

else {

    // NOT LOGGED IN -> PUBLIC HOME

    logoLink =
            request.getContextPath()
            + "/index.jsp";
}
%>


<div class="navbar">


    <!-- =========================
         LOGO
    ========================== -->

    <a class="logo"
       href="<%=logoLink%>">


        <img
            src="<%=request.getContextPath()%>/assets/images/logo.svg"
            alt="E-Mart">


    </a>



    <!-- =========================
         CENTER MENU
    ========================== -->

    <div class="menu">


        <!-- =========================
             NOT LOGGED IN
        ========================== -->

        <%
        if(navUser == null) {
        %>


            <a href="<%=request.getContextPath()%>/index.jsp">

                Home

            </a>


            <a href="<%=request.getContextPath()%>/about.jsp">

                About Us

            </a>


        <%
        }
        %>



        <!-- =========================
             CUSTOMER
        ========================== -->

        <%
        if(navUser != null &&
           "customer".equalsIgnoreCase(
                   navUser.getRole())) {
        %>


            <!-- HOME -->

            <a href="<%=request.getContextPath()%>/home.jsp">

                Home

            </a>


            <!-- PRODUCTS -->

            <a href="<%=request.getContextPath()%>/ProductServlet">

                Products

            </a>


            <!-- CART -->

            <a href="<%=request.getContextPath()%>/CartServlet">

                Cart

            </a>


            <!-- MY ORDERS -->

            <a href="<%=request.getContextPath()%>/OrderServlet?action=myOrders">

                My Orders

            </a>


            <!-- ABOUT -->

            <a href="<%=request.getContextPath()%>/about.jsp">

                About Us

            </a>


        <%
        }
        %>



        <!-- =========================
             ADMIN
        ========================== -->

        <%
        if(navUser != null &&
           "admin".equalsIgnoreCase(
                   navUser.getRole())) {
        %>


            <!-- PRODUCTS -->

            <a href="<%=request.getContextPath()%>/ProductServlet">

                Products

            </a>


            <!-- DASHBOARD -->

            <a href="<%=request.getContextPath()%>/adminDashboard.jsp">

                Dashboard

            </a>


            <!-- ADD PRODUCT -->

            <a href="<%=request.getContextPath()%>/addProduct.jsp">

                Add Product

            </a>


            <!-- MANAGE ORDERS -->

            <a href="<%=request.getContextPath()%>/AdminOrderServlet">

                Manage Orders

            </a>


            <!-- MANAGE USERS -->

            <a href="<%=request.getContextPath()%>/manageUsers.jsp">

                Manage Users

            </a>


        <%
        }
        %>


    </div>



    <!-- =========================
         RIGHT MENU
    ========================== -->

    <div class="right-menu">


        <!-- =========================
             NOT LOGGED IN
        ========================== -->

        <%
        if(navUser == null) {
        %>


            <a href="<%=request.getContextPath()%>/login.jsp">

                Login

            </a>


            <a href="<%=request.getContextPath()%>/register.jsp">

                Register

            </a>


        <%
        }

        else {
        %>


            <!-- =========================
                 LOGGED IN USER
            ========================== -->

            <span class="username">

                Welcome,
                <%=navUser.getName()%>

            </span>


            <!-- LOGOUT -->

            <a href="javascript:void(0)"
               onclick="openLogoutPopup()">

                Logout

            </a>


        <%
        }
        %>


    </div>


</div>



<!-- =========================
     LOGOUT POPUP
========================= -->

<%
if(navUser != null) {
%>


<div id="logoutModal"
     class="logout-modal">


    <div class="logout-box">


        <h2>

            Logout?

        </h2>


        <p>

            Are you sure you want to logout from E-Mart?

        </p>


        <div class="logout-actions">


            <!-- CANCEL -->

            <button type="button"
                    class="cancel-logout"
                    onclick="closeLogoutPopup()">

                Cancel

            </button>


            <!-- YES LOGOUT -->

            <a href="<%=request.getContextPath()%>/LogoutServlet"
               class="confirm-logout">

                Yes, Logout

            </a>


        </div>


    </div>


</div>



<script>


function openLogoutPopup(){

    document.getElementById(
        "logoutModal"
    ).style.display =
        "flex";
}


function closeLogoutPopup(){

    var modal =
        document.getElementById(
            "logoutModal"
        );


    if(modal){

        modal.style.display =
            "none";
    }
}


/* CLICK OUTSIDE TO CLOSE */

window.addEventListener(
    "click",
    function(event){

        var modal =
            document.getElementById(
                "logoutModal"
            );


        if(modal &&
           event.target === modal){

            closeLogoutPopup();
        }
    }
);


</script>


<%
}
%>