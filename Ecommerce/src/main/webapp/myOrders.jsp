<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Order" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.User" %>
<%@ page import="dao.OrderDAO" %>

<%
User user =
        (User) session.getAttribute("user");


if(user == null ||
   !"customer".equalsIgnoreCase(
           user.getRole())) {

    response.sendRedirect(
            request.getContextPath()
            + "/login.jsp"
    );

    return;
}


ArrayList<Order> orders =
        (ArrayList<Order>)
        request.getAttribute("orders");


OrderDAO orderDAO =
        new OrderDAO();
%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>My Orders - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=1700">


<style>

.my-orders-container{
    width:90%;
    max-width:1100px;
    margin:40px auto;
}

.my-orders-container h1{
    text-align:center;
    color:#172554;
    margin-bottom:10px;
}

.my-orders-subtitle{
    text-align:center;
    color:#64748b;
    margin-bottom:30px;
}

.my-order-card{
    background:white;
    padding:24px;
    margin-bottom:22px;
    border-radius:14px;
    box-shadow:0 6px 20px rgba(15,23,42,0.13);
}

.my-order-card h2{
    color:#172554;
    margin-bottom:14px;
}

.my-order-card p{
    color:#475569;
    margin:7px 0;
}

.order-products-box{
    margin-top:18px;
    padding:15px;
    background:#f8fafc;
    border-radius:10px;
}

.order-products-box h3{
    color:#172554;
    margin-bottom:12px;
}

.order-product-item{
    background:white;
    padding:14px;
    margin-bottom:10px;
    border:1px solid #e2e8f0;
    border-radius:8px;
}

.order-product-item:last-child{
    margin-bottom:0;
}

.order-product-name{
    color:#172554 !important;
    font-weight:bold;
    margin-bottom:6px !important;
}

.order-product-info{
    color:#64748b !important;
    font-size:14px;
}

.order-product-subtotal{
    color:#1e3a8a;
    font-weight:bold;
}

.payment-badge{
    display:inline-block;
    background:#dbeafe;
    color:#1e40af;
    padding:6px 11px;
    border-radius:20px;
    font-size:13px;
    font-weight:bold;
}

.order-status{
    display:inline-block;
    padding:6px 12px;
    border-radius:20px;
    font-size:13px;
    font-weight:bold;
}

.order-pending{
    background:#fef3c7;
    color:#92400e;
}

.order-delivered{
    background:#dcfce7;
    color:#166534;
}

.order-cancelled{
    background:#fee2e2;
    color:#b91c1c;
}

.no-orders{
    background:white;
    padding:40px;
    border-radius:14px;
    text-align:center;
    color:#64748b;
    box-shadow:0 6px 20px rgba(15,23,42,0.13);
}

</style>

</head>


<body>


<jsp:include page="navbar.jsp" />


<div class="my-orders-container">


    <h1>
        My Orders
    </h1>


    <p class="my-orders-subtitle">
        View your previous purchases and order status.
    </p>


    <%
    if(orders != null &&
       !orders.isEmpty()) {


        for(Order o : orders) {


            String status =
                    o.getStatus();


            if(status == null ||
               status.trim().isEmpty()) {

                status =
                        "Pending";
            }


            String statusClass =
                    "order-pending";


            if("Delivered".equalsIgnoreCase(
                    status)) {

                statusClass =
                        "order-delivered";
            }


            else if("Cancelled".equalsIgnoreCase(
                    status)) {

                statusClass =
                        "order-cancelled";
            }


            ArrayList<CartItem> orderItems =
                    orderDAO.getOrderItems(
                            o.getId()
                    );
    %>


    <div class="my-order-card">


        <h2>
            Order #<%=o.getId()%>
        </h2>


        <p>

            <strong>
                Email:
            </strong>

            <%=o.getUserEmail()%>

        </p>


        <p>

            <strong>
                Total Amount:
            </strong>

            Rs. <%=o.getTotalAmount()%>

        </p>


        <p>

            <strong>
                Payment:
            </strong>

            <span class="payment-badge">
                <%=o.getPaymentMethod()%>
            </span>

        </p>


        <p>

            <strong>
                Status:
            </strong>

            <span class="order-status <%=statusClass%>">
                <%=status%>
            </span>

        </p>


        <div class="order-products-box">


            <h3>
                Products
            </h3>


            <%
            if(orderItems != null &&
               !orderItems.isEmpty()) {


                for(CartItem item : orderItems) {


                    double subtotal =
                            item.getPrice()
                            * item.getQuantity();
            %>


                <div class="order-product-item">


                    <p class="order-product-name">
                        <%=item.getProductName()%>
                    </p>


                    <p class="order-product-info">

                        Quantity:

                        <strong>
                            <%=item.getQuantity()%>
                        </strong>

                    </p>


                    <p class="order-product-info">

                        Unit Price:

                        Rs. <%=item.getPrice()%>

                    </p>


                    <p class="order-product-info">

                        Subtotal:

                        <span class="order-product-subtotal">

                            Rs. <%=subtotal%>

                        </span>

                    </p>


                </div>


            <%
                }

            } else {
            %>


                <p style="color:#94a3b8;">

                    No product information available.

                </p>


            <%
            }
            %>


        </div>


    </div>


    <%
        }

    } else {
    %>


        <div class="no-orders">

            No orders available yet.

        </div>


    <%
    }
    %>


</div>


</body>

</html>