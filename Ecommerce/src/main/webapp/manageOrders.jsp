<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Order" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.User" %>
<%@ page import="dao.UserDAO" %>
<%@ page import="dao.OrderDAO" %>

<%
User user =
        (User) session.getAttribute("user");

if(user == null ||
   !"admin".equalsIgnoreCase(user.getRole())) {

    response.sendRedirect(
            request.getContextPath()
            + "/login.jsp"
    );

    return;
}


ArrayList<Order> orders =
        (ArrayList<Order>)
        request.getAttribute("orders");


UserDAO userDAO =
        new UserDAO();

OrderDAO orderDAO =
        new OrderDAO();


String successMessage =
        (String) session.getAttribute(
                "successMessage"
        );

String errorMessage =
        (String) session.getAttribute(
                "errorMessage"
        );


if(successMessage != null) {
    session.removeAttribute("successMessage");
}

if(errorMessage != null) {
    session.removeAttribute("errorMessage");
}
%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Manage Orders - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=1600">


<style>

.manage-orders-container{
    width:96%;
    max-width:1500px;
    margin:40px auto;
}

.manage-orders-container h1{
    text-align:center;
    color:#172554;
    margin-bottom:10px;
}

.manage-orders-subtitle{
    text-align:center;
    color:#64748b;
    margin-bottom:30px;
}

.orders-table-wrapper{
    width:100%;
    overflow-x:auto;
    background:white;
    border-radius:12px;
    box-shadow:0 6px 20px rgba(15,23,42,0.12);
}

.orders-table{
    width:100%;
    min-width:1250px;
    border-collapse:collapse;
}

.orders-table thead{
    background:#172554;
    color:white;
}

.orders-table th{
    padding:14px 12px;
    text-align:left;
    white-space:nowrap;
}

.orders-table td{
    padding:14px 12px;
    border-bottom:1px solid #e2e8f0;
    color:#475569;
    vertical-align:top;
}

.orders-table tbody tr:hover{
    background:#f8fafc;
}

.order-id{
    color:#1e3a8a;
    font-weight:bold;
}

.customer-email{
    color:#334155;
    font-size:14px;
}

.order-price{
    color:#172554;
    font-weight:bold;
    white-space:nowrap;
}

.payment-badge{
    display:inline-block;
    padding:6px 10px;
    border-radius:20px;
    background:#dbeafe;
    color:#1e40af;
    font-size:12px;
    font-weight:bold;
    white-space:nowrap;
}

.status-badge{
    display:inline-block;
    padding:6px 11px;
    border-radius:20px;
    font-size:12px;
    font-weight:bold;
    white-space:nowrap;
}

.status-pending{
    background:#fef3c7;
    color:#92400e;
}

.status-delivered{
    background:#dcfce7;
    color:#166534;
}

.status-cancelled{
    background:#fee2e2;
    color:#b91c1c;
}


/* PRODUCT DETAILS */

.order-items-box{
    min-width:280px;
}

.order-item-row{
    background:#f8fafc;
    border:1px solid #e2e8f0;
    border-radius:8px;
    padding:10px;
    margin-bottom:8px;
}

.order-item-row:last-child{
    margin-bottom:0;
}

.order-item-name{
    color:#172554;
    font-weight:bold;
    margin-bottom:5px;
}

.order-item-info{
    font-size:13px;
    color:#64748b;
    line-height:1.5;
}

.order-item-subtotal{
    color:#1e3a8a;
    font-weight:bold;
}


/* STATUS ACTION */

.status-form{
    display:flex;
    align-items:center;
    gap:8px;
    margin:0;
}

.status-select{
    width:120px;
    padding:8px;
    border:1px solid #cbd5e1;
    border-radius:7px;
    background:white;
    color:#334155;
}

.status-update-btn{
    padding:8px 13px !important;
    white-space:nowrap;
}


/* DELETED CUSTOMER */

.deleted-user-warning{
    display:inline-block;
    background:#fee2e2;
    color:#b91c1c;
    border:1px solid #fecaca;
    padding:8px 12px;
    border-radius:7px;
    font-size:13px;
    font-weight:bold;
    white-space:nowrap;
}


.no-orders{
    padding:40px !important;
    text-align:center !important;
    color:#64748b !important;
}


/* POPUP */

.order-popup{
    position:fixed;
    inset:0;
    display:flex;
    align-items:center;
    justify-content:center;
    background:rgba(15,23,42,0.65);
    z-index:9999;
}

.order-popup-box{
    width:360px;
    max-width:90%;
    padding:30px;
    background:white;
    border-radius:14px;
    text-align:center;
    box-shadow:0 15px 40px rgba(0,0,0,0.25);
}

.popup-success{
    color:#166534;
}

.popup-error{
    color:#b91c1c;
}

</style>

</head>


<body>


<jsp:include page="navbar.jsp" />


<div class="manage-orders-container">


    <h1>
        Manage Orders
    </h1>


    <p class="manage-orders-subtitle">
        View products, quantities, payments and customer order status.
    </p>


    <div class="orders-table-wrapper">


        <table class="orders-table">


            <thead>

                <tr>

                    <th>Order ID</th>

                    <th>Customer</th>

                    <th>Products</th>

                    <th>Total</th>

                    <th>Payment</th>

                    <th>Status</th>

                    <th>Action</th>

                </tr>

            </thead>


            <tbody>


            <%
            if(orders != null &&
               !orders.isEmpty()) {


                for(Order o : orders) {


                    boolean customerExists =
                            userDAO.emailExists(
                                    o.getUserEmail()
                            );


                    String status =
                            o.getStatus();


                    if(status == null ||
                       status.trim().isEmpty()) {

                        status = "Pending";
                    }


                    String statusClass =
                            "status-pending";


                    if("Delivered".equalsIgnoreCase(status)) {

                        statusClass =
                                "status-delivered";
                    }


                    else if("Cancelled".equalsIgnoreCase(status)) {

                        statusClass =
                                "status-cancelled";
                    }


                    ArrayList<CartItem> orderItems =
                            orderDAO.getOrderItems(
                                    o.getId()
                            );
            %>


                <tr>


                    <td>

                        <span class="order-id">

                            #<%=o.getId()%>

                        </span>

                    </td>


                    <td>

                        <span class="customer-email">

                            <%=o.getUserEmail()%>

                        </span>

                    </td>


                    <td>

                        <div class="order-items-box">


                        <%
                        if(orderItems != null &&
                           !orderItems.isEmpty()) {


                            for(CartItem item : orderItems) {


                                double subtotal =
                                        item.getPrice()
                                        * item.getQuantity();
                        %>


                            <div class="order-item-row">


                                <div class="order-item-name">

                                    <%=item.getProductName()%>

                                </div>


                                <div class="order-item-info">

                                    Quantity:

                                    <strong>
                                        <%=item.getQuantity()%>
                                    </strong>

                                    <br>

                                    Price:
                                    Rs. <%=item.getPrice()%>

                                    <br>

                                    Subtotal:

                                    <span class="order-item-subtotal">

                                        Rs. <%=subtotal%>

                                    </span>

                                </div>


                            </div>


                        <%
                            }

                        } else {
                        %>


                            <span style="color:#94a3b8;">

                                No product information available

                            </span>


                        <%
                        }
                        %>


                        </div>

                    </td>


                    <td>

                        <span class="order-price">

                            Rs. <%=o.getTotalAmount()%>

                        </span>

                    </td>


                    <td>

                        <span class="payment-badge">

                            <%=o.getPaymentMethod()%>

                        </span>

                    </td>


                    <td>

                        <span class="status-badge <%=statusClass%>">

                            <%=status%>

                        </span>

                    </td>


                    <td>


                    <%
                    if(customerExists) {
                    %>


                        <form action="<%=request.getContextPath()%>/AdminOrderServlet"
                              method="get"
                              class="status-form">


                            <input type="hidden"
                                   name="action"
                                   value="update">


                            <input type="hidden"
                                   name="id"
                                   value="<%=o.getId()%>">


                            <select name="status"
                                    class="status-select">


                                <option value="Pending"
                                    <%= "Pending".equalsIgnoreCase(status)
                                        ? "selected" : "" %>>

                                    Pending

                                </option>


                                <option value="Delivered"
                                    <%= "Delivered".equalsIgnoreCase(status)
                                        ? "selected" : "" %>>

                                    Delivered

                                </option>


                                <option value="Cancelled"
                                    <%= "Cancelled".equalsIgnoreCase(status)
                                        ? "selected" : "" %>>

                                    Cancelled

                                </option>


                            </select>


                            <button type="submit"
                                    class="status-update-btn">

                                Update

                            </button>


                        </form>


                    <%
                    }
                    else {
                    %>


                        <span class="deleted-user-warning">

                            User is no longer available

                        </span>


                    <%
                    }
                    %>


                    </td>


                </tr>


            <%
                }
            }

            else {
            %>


                <tr>

                    <td colspan="7"
                        class="no-orders">

                        No Orders Available

                    </td>

                </tr>


            <%
            }
            %>


            </tbody>


        </table>


    </div>


</div>


<%
if(successMessage != null) {
%>

<div id="successPopup"
     class="order-popup">

    <div class="order-popup-box">

        <h2 class="popup-success">
            ✓ Success
        </h2>

        <p>
            <%=successMessage%>
        </p>

        <button type="button"
                onclick="closeSuccessPopup()">

            OK

        </button>

    </div>

</div>

<%
}
%>


<%
if(errorMessage != null) {
%>

<div id="errorPopup"
     class="order-popup">

    <div class="order-popup-box">

        <h2 class="popup-error">
            Error
        </h2>

        <p>
            <%=errorMessage%>
        </p>

        <button type="button"
                onclick="closeErrorPopup()">

            OK

        </button>

    </div>

</div>

<%
}
%>


<script>

function closeSuccessPopup(){

    var popup =
        document.getElementById(
            "successPopup"
        );

    if(popup){

        popup.style.display =
            "none";
    }
}


function closeErrorPopup(){

    var popup =
        document.getElementById(
            "errorPopup"
        );

    if(popup){

        popup.style.display =
            "none";
    }
}

</script>


</body>

</html>