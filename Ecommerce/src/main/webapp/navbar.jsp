<div class="navbar">

    <h2>EMart</h2>

    <div class="menu">

        <a href="index.jsp">Home</a>

        <a href="ProductServlet">Products</a>


        <%
        model.User user = (model.User)session.getAttribute("user");

        if(user != null){

            if(user.getRole().equals("customer")){
        %>

                <a href="CartServlet">Cart</a>

                <a href="OrderServlet?action=myOrders">
                    My Orders
                </a>


        <%
            }

            if(user.getRole().equals("admin")){
        %>

                <a href="AdminOrderServlet">
                    Manage Orders
                </a>


        <%
            }
        %>

            <a href="LogoutServlet">
                Logout
            </a>


        <%
        }
        else{
        %>

            <a href="login.jsp">Login</a>

            <a href="register.jsp">Register</a>


        <%
        }
        %>


    </div>

</div>

