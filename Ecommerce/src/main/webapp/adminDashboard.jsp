<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="model.User" %>
<%@ page import="dao.ProductDAO" %>
<%@ page import="dao.UserDAO" %>

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


ProductDAO productDAO =
        new ProductDAO();

UserDAO userDAO =
        new UserDAO();


int totalProducts =
        productDAO.getTotalProducts();

int totalCustomers =
        userDAO.getAllCustomers().size();
%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Admin Dashboard - E-Mart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=2500">


<style>

/* ========================================
   ADMIN DASHBOARD PAGE
======================================== */

.admin-dashboard-container {

    width: 92%;
    max-width: 1200px;

    margin: 45px auto 60px;
}


/* ========================================
   TITLE
======================================== */

.admin-dashboard-title {

    text-align: center;

    color: #172554;

    font-size: 34px;

    margin-bottom: 8px;
}


.admin-dashboard-subtitle {

    text-align: center;

    color: #64748b;

    font-size: 15px;

    margin-bottom: 35px;
}


/* ========================================
   STATISTICS
======================================== */

.dashboard-stats {

    display: grid;

    grid-template-columns:
        repeat(2, minmax(220px, 300px));

    justify-content: center;

    gap: 25px;

    margin-bottom: 40px;
}


.dashboard-stat-card {

    background:
        linear-gradient(
            135deg,
            #172554,
            #2563eb
        );

    padding: 30px 25px;

    border-radius: 16px;

    text-align: center;

    box-shadow:
        0 8px 22px
        rgba(37,99,235,0.22);
}


.dashboard-stat-card h3 {

    color: #dbeafe;

    font-size: 16px;

    margin-bottom: 12px;
}


.dashboard-stat-number {

    color: white;

    font-size: 42px;

    font-weight: bold;
}


/* ========================================
   ADMIN ACTION SECTION
======================================== */

.admin-actions {

    display: grid;

    grid-template-columns:
        repeat(2, minmax(280px, 1fr));

    gap: 24px;

    max-width: 950px;

    margin: 0 auto;
}


/* ========================================
   ACTION CARD
======================================== */

.admin-action-card {

    background: white;

    padding: 28px;

    border-radius: 15px;

    text-align: center;

    box-shadow:
        0 6px 20px
        rgba(15,23,42,0.12);

    border:
        1px solid #e2e8f0;

    transition: 0.25s;
}


.admin-action-card:hover {

    transform: translateY(-5px);

    box-shadow:
        0 10px 28px
        rgba(15,23,42,0.17);
}


.admin-action-card h3 {

    color: #172554;

    font-size: 20px;

    margin-bottom: 12px;
}


.admin-action-card p {

    color: #64748b;

    font-size: 14px;

    line-height: 1.6;

    min-height: 45px;

    margin-bottom: 20px;
}


.admin-action-card a {

    text-decoration: none;
}


/* ========================================
   CARD BUTTONS
======================================== */

.admin-action-card button {

    width: 100%;

    padding: 12px 18px;

    border: none;

    border-radius: 8px;

    background:
        linear-gradient(
            135deg,
            #1e3a8a,
            #2563eb
        ) !important;

    color: white !important;

    font-weight: bold;

    cursor: pointer;

    transition: 0.25s;
}


.admin-action-card button:hover {

    background:
        linear-gradient(
            135deg,
            #172554,
            #1d4ed8
        ) !important;

    transform: translateY(-2px);
}


/* ========================================
   MANAGE USERS BUTTON
======================================== */

.admin-action-card .manage-users-btn {

    background:
        linear-gradient(
            135deg,
            #b91c1c,
            #dc2626
        ) !important;
}


.admin-action-card .manage-users-btn:hover {

    background:
        linear-gradient(
            135deg,
            #991b1b,
            #b91c1c
        ) !important;
}


/* ========================================
   RESPONSIVE
======================================== */

@media(max-width: 800px) {

    .dashboard-stats {

        grid-template-columns: 1fr;

        max-width: 350px;

        margin-left: auto;
        margin-right: auto;
        margin-bottom: 35px;
    }


    .admin-actions {

        grid-template-columns: 1fr;
    }


    .admin-dashboard-title {

        font-size: 28px;
    }
}

</style>

</head>


<body>


<jsp:include page="navbar.jsp" />


<div class="admin-dashboard-container">


    <!-- =========================
         TITLE
    ========================= -->

    <h1 class="admin-dashboard-title">

        Admin Dashboard

    </h1>


    <p class="admin-dashboard-subtitle">

        Welcome, <%=user.getName()%>.
        Manage your E-Mart store from here.

    </p>



    <!-- =========================
         STATISTICS
    ========================= -->

    <div class="dashboard-stats">


        <div class="dashboard-stat-card">

            <h3>
                Total Products
            </h3>

            <div class="dashboard-stat-number">

                <%=totalProducts%>

            </div>

        </div>



        <div class="dashboard-stat-card">

            <h3>
                Total Customers
            </h3>

            <div class="dashboard-stat-number">

                <%=totalCustomers%>

            </div>

        </div>


    </div>



    <!-- =========================
         ADMIN ACTIONS
    ========================= -->

    <div class="admin-actions">


        <!-- MANAGE PRODUCTS -->

        <div class="admin-action-card">

            <h3>
                Manage Products
            </h3>

            <p>
                View, edit and delete products
                available in E-Mart.
            </p>

            <a href="<%=request.getContextPath()%>/ProductServlet">

                <button type="button">

                    Manage Products

                </button>

            </a>

        </div>



        <!-- ADD PRODUCT -->

        <div class="admin-action-card">

            <h3>
                Add Product
            </h3>

            <p>
                Add new products and categories
                to your E-Mart store.
            </p>

            <a href="<%=request.getContextPath()%>/addProduct.jsp">

                <button type="button">

                    Add Product

                </button>

            </a>

        </div>



        <!-- MANAGE ORDERS -->

        <div class="admin-action-card">

            <h3>
                Manage Orders
            </h3>

            <p>
                View customer orders and
                update their delivery status.
            </p>

            <a href="<%=request.getContextPath()%>/AdminOrderServlet">

                <button type="button">

                    Manage Orders

                </button>

            </a>

        </div>



        <!-- MANAGE USERS -->

        <div class="admin-action-card">

            <h3>
                Manage Users
            </h3>

            <p>
                View and remove registered
                customer accounts.
            </p>

            <a href="<%=request.getContextPath()%>/manageUsers.jsp">

                <button type="button"
                        class="manage-users-btn">

                    Manage Users

                </button>

            </a>

        </div>


    </div>


</div>


</body>

</html>