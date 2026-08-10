<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.ProductDAO" %>
<%@ page import="model.Product" %>
<%@ page import="model.User" %>

<%
User user =
        (User) session.getAttribute("user");

ProductDAO productDAO =
        new ProductDAO();

String search =
        request.getParameter("search");

String category =
        request.getParameter("category");

ArrayList<Product> products;


/* =========================
   SEARCH
========================= */

if(search != null &&
   !search.trim().isEmpty()) {

    products =
            productDAO.searchProducts(
                    search.trim()
            );
}


/* =========================
   CATEGORY FILTER
========================= */

else if(category != null &&
        !category.trim().isEmpty() &&
        !"All".equalsIgnoreCase(category)) {

    products =
            productDAO.getProductsByCategory(
                    category.trim()
            );
}


/* =========================
   ALL PRODUCTS
========================= */

else {

    products =
            productDAO.getAllProducts();
}


/* =========================
   CART POPUP MESSAGE
========================= */

String cartPopup =
        (String) session.getAttribute("cartPopup");

String cartErrorPopup =
        (String) session.getAttribute("cartErrorPopup");

if(cartPopup != null){
    session.removeAttribute("cartPopup");
}

if(cartErrorPopup != null){
    session.removeAttribute("cartErrorPopup");
}
%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=8000">


<style>

/* =========================================
   HORIZONTAL CATEGORY BAR
========================================= */

.category-bar {

    width: 92%;
    max-width: 1250px;

    margin: 20px auto 30px;

    padding: 11px 12px;

    display: flex;
    align-items: center;

    gap: 10px;

    background: #ffffff;

    border-radius: 14px;

    box-shadow:
        0 5px 18px rgba(15,23,42,0.10);

    overflow-x: auto;

    white-space: nowrap;

    box-sizing: border-box;
}


/* CATEGORY ITEM */

.category-item {

    flex-shrink: 0;

    display: inline-block;

    padding: 9px 15px;

    background: #eff6ff;

    border: 1px solid #dbeafe;

    border-radius: 22px;

    color: #1e3a8a !important;

    text-decoration: none;

    font-size: 13px;

    font-weight: 600;

    transition: 0.25s;
}


/* CATEGORY HOVER */

.category-item:hover {

    color: #ffffff !important;

    background:
        linear-gradient(
            135deg,
            #1e40af,
            #2563eb
        );

    border-color: #2563eb;

    transform: translateY(-1px);
}


/* SCROLLBAR */

.category-bar::-webkit-scrollbar {

    height: 5px;
}


.category-bar::-webkit-scrollbar-thumb {

    background: #cbd5e1;

    border-radius: 10px;
}


.category-bar::-webkit-scrollbar-track {

    background: transparent;
}

</style>


</head>


<body>


<!-- =========================
     NAVBAR
========================= -->

<jsp:include page="navbar.jsp" />



<!-- =========================
     CART SUCCESS POPUP
========================= -->

<%
if(cartPopup != null){
%>

<div id="cartPopup"
     class="cart-success-popup">

    <div class="cart-success-box">

        <div class="cart-success-icon">

            ✓

        </div>


        <h3>

            Added!

        </h3>


        <p>

            <%=cartPopup%>

        </p>


        <button type="button"
                onclick="closeCartPopup()">

            OK

        </button>


    </div>

</div>

<%
}
%>



<!-- =========================
     CART ERROR POPUP
========================= -->

<%
if(cartErrorPopup != null){
%>

<div id="cartErrorPopup"
     class="cart-success-popup">

    <div class="cart-success-box">


        <div class="cart-error-icon">

            !

        </div>


        <h3 style="color:#b91c1c;">

            Error

        </h3>


        <p>

            <%=cartErrorPopup%>

        </p>


        <button type="button"
                onclick="closeCartErrorPopup()">

            OK

        </button>


    </div>

</div>

<%
}
%>



<!-- =========================
     HERO SECTION
========================= -->

<div class="hero">


    <div class="hero-content">


        <span class="hero-badge">

            NEW ARRIVALS EVERY WEEK

        </span>


        <h1>

            Big Deals,

            <span>

                Every Day

            </span>

        </h1>


        <p>

            Explore authentic local products including fresh vegetables,
            homemade foods, traditional wear, handicrafts and daily
            essentials — all from trusted local sellers.

        </p>


        <a href="#products">

            <button type="button">

                Shop Now →

            </button>

        </a>


    </div>


</div>



<!-- =========================
     PRODUCTS SECTION
========================= -->

<div id="products">


    <h1 style="
        text-align:center;
        color:#172554;
        margin:40px 0 10px;">

        Explore Our Products

    </h1>


    <p style="
        text-align:center;
        color:#64748b;
        margin-bottom:20px;">

        Find the products you need from our categories.

    </p>



    <!-- =========================
         SEARCH
    ========================= -->

    <div class="search-box">


        <form action="index.jsp"
              method="get">


            <input type="text"
                   name="search"
                   value="<%=search != null ? search : ""%>"
                   placeholder="Search local products...">


            <button type="submit">

                Search

            </button>


        </form>


    </div>



    <!-- =========================
         NEW CATEGORY FILTER
    ========================= -->

    <div class="category-bar">


        <!-- ALL -->

        <a href="index.jsp#products"
           class="category-item">

            All

        </a>



        <!-- LOCAL FOOD -->

        <a href="index.jsp?category=Local+Food+%26+Grocery#products"
           class="category-item">

            Local Food & Grocery

        </a>



        <!-- TEA -->

        <a href="index.jsp?category=Tea+%26+Coffee#products"
           class="category-item">

            Tea & Coffee

        </a>



        <!-- CLOTHING -->

        <a href="index.jsp?category=Clothing+%26+Textiles#products"
           class="category-item">

            Clothing & Textiles

        </a>



        <!-- HANDICRAFTS -->

        <a href="index.jsp?category=Handicrafts#products"
           class="category-item">

            Handicrafts

        </a>



        <!-- LOKTA -->

        <a href="index.jsp?category=Lokta+%26+Stationery#products"
           class="category-item">

            Lokta & Stationery

        </a>



        <!-- JEWELRY -->

        <a href="index.jsp?category=Jewelry+%26+Accessories#products"
           class="category-item">

            Jewelry & Accessories

        </a>



        <!-- HOME -->

        <a href="index.jsp?category=Home+%26+D%C3%A9cor#products"
           class="category-item">

            Home & Décor

        </a>



        <!-- GIFTS -->

        <a href="index.jsp?category=Gifts+%26+Souvenirs#products"
           class="category-item">

            Gifts & Souvenirs

        </a>



        <!-- OTHERS -->

        <a href="index.jsp?category=Others#products"
           class="category-item">

            Others

        </a>


    </div>



    <!-- =========================
         PRODUCTS
    ========================= -->

    <div class="products-container">


    <%
    if(products != null &&
       !products.isEmpty()) {


        for(Product p : products) {
    %>


        <div class="product-card">


            <!-- PRODUCT IMAGE -->

            <img src="<%=p.getImage()%>"
                 alt="<%=p.getName()%>">



            <!-- PRODUCT NAME -->

            <h2>

                <%=p.getName()%>

            </h2>



            <!-- CATEGORY -->

            <p class="product-category">

                <%=p.getCategory()%>

            </p>



            <!-- DESCRIPTION -->

            <p>

                <%=p.getDescription()%>

            </p>



            <!-- PRICE -->

            <h3>

                Rs. <%=p.getPrice()%>

            </h3>



            <!-- =========================
                 PUBLIC / NOT LOGGED IN
            ========================= -->

            <%
            if(user == null){
            %>


                <a href="<%=request.getContextPath()%>/login.jsp">

                    <button type="button">

                        Login to Buy

                    </button>

                </a>


            <%
            }
            %>



            <!-- =========================
                 CUSTOMER LOGGED IN
            ========================= -->

            <%
            if(user != null &&
               "customer".equalsIgnoreCase(
                       user.getRole())) {
            %>


            <div class="product-buy-buttons">


                <!-- ADD TO CART -->

                <a href="<%=request.getContextPath()%>/CartServlet?action=add&id=<%=p.getId()%>&quantity=1">

                    <button type="button">

                        Add To Cart

                    </button>

                </a>



                <!-- BUY NOW -->

                <a href="<%=request.getContextPath()%>/checkout.jsp?buyNow=true&productId=<%=p.getId()%>">

                    <button type="button">

                        Buy Now

                    </button>

                </a>


            </div>


            <%
            }
            %>



            <!-- =========================
                 ADMIN
            ========================= -->

            <%
            if(user != null &&
               "admin".equalsIgnoreCase(
                       user.getRole())) {
            %>


                <a href="<%=request.getContextPath()%>/ProductServlet?action=delete&id=<%=p.getId()%>"
                   onclick="return confirm('Are you sure you want to delete this product?');">

                    <button type="button">

                        Delete Product

                    </button>

                </a>


            <%
            }
            %>


        </div>


    <%
        }

    }

    else {
    %>


        <h2 style="
            width:100%;
            text-align:center;
            color:#64748b;
            margin:40px;">

            No Products Found

        </h2>


    <%
    }
    %>


    </div>


</div>



<!-- =========================
     FEATURES
========================= -->

<div class="features">


    <!-- QUALITY -->

    <div class="card">


        <div class="feature-icon">

            ✓

        </div>


        <h3>

            Quality Products

        </h3>


        <p>

            Authentic and high-quality products
            from trusted sellers.

        </p>


    </div>



    <!-- EASY SHOPPING -->

    <div class="card">


        <div class="feature-icon">

            🛒

        </div>


        <h3>

            Easy Shopping

        </h3>


        <p>

            Simple, convenient and secure
            shopping experience.

        </p>


    </div>



    <!-- FAST DELIVERY -->

    <div class="card">


        <div class="feature-icon">

            🚚

        </div>


        <h3>

            Fast Delivery

        </h3>


        <p>

            Quick and reliable delivery
            directly to your location.

        </p>


    </div>


</div>



<!-- =========================
     JAVASCRIPT
========================= -->

<script>


function closeCartPopup(){

    var popup =
            document.getElementById(
                    "cartPopup"
            );


    if(popup){

        popup.style.display =
                "none";
    }
}



function closeCartErrorPopup(){

    var popup =
            document.getElementById(
                    "cartErrorPopup"
            );


    if(popup){

        popup.style.display =
                "none";
    }
}


</script>


</body>

</html>