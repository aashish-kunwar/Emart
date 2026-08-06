<%@ page import="model.User" %>

<%
User user = (User) session.getAttribute("user");
%>

<div class="navbar">

    <!-- Logo -->
    <a href="<%=request.getContextPath()%>/home.jsp" class="logo">

        <img src="<%=request.getContextPath()%>/assets/images/logo.svg"
             alt="EMart Logo">

    </a>

    <!-- Menu -->
    <div class="menu">

        <% if(user == null){ %>

            <a href="<%=request.getContextPath()%>/index.jsp">Home</a>

        <% } else { %>

            <a href="<%=request.getContextPath()%>/home.jsp">Home</a>

        <% } %>

        <a href="<%=request.getContextPath()%>/ProductServlet">Products</a>

        <% if(user != null && "customer".equals(user.getRole())) { %>

            <a href="<%=request.getContextPath()%>/CartServlet">Cart</a>

            <a href="<%=request.getContextPath()%>/OrderServlet?action=myOrders">
                My Orders
            </a>

        <% } %>

        <% if(user != null && "admin".equals(user.getRole())) { %>

            <a href="<%=request.getContextPath()%>/addProduct.jsp">
                Add Product
            </a>

            <a href="<%=request.getContextPath()%>/ProductServlet">
                View Products
            </a>

            <a href="<%=request.getContextPath()%>/AdminOrderServlet">
                Manage Orders
            </a>

        <% } %>

    </div>

    <!-- Right Menu -->
    <div class="right-menu">

        <% if(user == null){ %>

            <a href="<%=request.getContextPath()%>/login.jsp">
                Login
            </a>

            <a href="<%=request.getContextPath()%>/register.jsp">
                Register
            </a>

        <% } else { %>

            <span class="username">
                Welcome, <%=user.getName()%>
            </span>

            <a href="<%=request.getContextPath()%>/LogoutServlet">
                Logout
            </a>

        <% } %>

    </div>

</div>