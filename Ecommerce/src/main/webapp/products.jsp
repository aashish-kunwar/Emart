<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Product" %>
<%@ page import="model.User" %>

<%
User user =
        (User) session.getAttribute("user");

ArrayList<Product> products =
        (ArrayList<Product>) request.getAttribute("products");

String keyword =
        request.getParameter("keyword");


/* =========================
   PRODUCT MESSAGE
========================= */

String productMessage =
        (String) session.getAttribute("message");

if(productMessage != null){
    session.removeAttribute("message");
}


/* =========================
   CART POPUP
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

<title>Products - E-Mart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=5000">


<style>

/* =========================
   CATEGORY BAR
========================= */

.category-bar{

    width:94%;
    max-width:1400px;

    margin:22px auto 30px;

    display:flex;
    align-items:center;

    gap:10px;

    padding:12px 14px;

    background:#ffffff;

    border-radius:14px;

    box-shadow:
        0 5px 18px rgba(15,23,42,0.10);

    overflow-x:auto;

    white-space:nowrap;

    scrollbar-width:thin;
}


/* CATEGORY ITEM */

.category-item{

    flex-shrink:0;

    display:inline-block;

    padding:10px 16px;

    border-radius:22px;

    background:#eff6ff;

    color:#1e3a8a !important;

    font-size:13px;

    font-weight:600;

    text-decoration:none;

    border:1px solid #dbeafe;

    transition:0.25s;
}


/* HOVER */

.category-item:hover{

    background:
        linear-gradient(
            135deg,
            #1e3a8a,
            #2563eb
        );

    color:white !important;

    border-color:#2563eb;

    transform:translateY(-2px);
}


/* SCROLLBAR */

.category-bar::-webkit-scrollbar{

    height:6px;
}


.category-bar::-webkit-scrollbar-thumb{

    background:#cbd5e1;

    border-radius:10px;
}


.category-bar::-webkit-scrollbar-track{

    background:transparent;
}

</style>

</head>


<body>


<!-- =========================
     NAVBAR
========================= -->

<jsp:include page="navbar.jsp" />



<!-- =========================
     PRODUCT SUCCESS POPUP
========================= -->

<%
if(productMessage != null){
%>

<div id="productSuccessPopup"
     class="success-popup">

    <div class="success-popup-box">

        <div class="success-check">
            ✓
        </div>

        <h3>
            Success!
        </h3>

        <p>
            <%=productMessage%>
        </p>

        <button type="button"
                onclick="closeProductPopup()">

            OK

        </button>

    </div>

</div>

<%
}
%>



<!-- =========================
     CART SUCCESS POPUP
========================= -->

<%
if(cartPopup != null){
%>

<div id="cartSuccessPopup"
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
     SEARCH
========================= -->

<div class="search-box">

<form action="<%=request.getContextPath()%>/ProductServlet"
      method="get">

    <input type="hidden"
           name="action"
           value="search">

    <input type="text"
           name="keyword"
           value="<%=keyword != null ? keyword : ""%>"
           placeholder="Search local products...">

    <button type="submit">
        Search
    </button>

</form>

</div>



<!-- =========================
     HORIZONTAL CATEGORIES
========================= -->

<div class="category-bar">


    <a href="<%=request.getContextPath()%>/ProductServlet"
       class="category-item">

        All

    </a>


    <a href="<%=request.getContextPath()%>/ProductServlet?action=category&category=Local+Food+%26+Grocery"
       class="category-item">

        Local Food & Grocery

    </a>


    <a href="<%=request.getContextPath()%>/ProductServlet?action=category&category=Tea+%26+Coffee"
       class="category-item">

        Tea & Coffee

    </a>


    <a href="<%=request.getContextPath()%>/ProductServlet?action=category&category=Clothing+%26+Textiles"
       class="category-item">

        Clothing & Textiles

    </a>


    <a href="<%=request.getContextPath()%>/ProductServlet?action=category&category=Handicrafts"
       class="category-item">

        Handicrafts

    </a>


    <a href="<%=request.getContextPath()%>/ProductServlet?action=category&category=Lokta+%26+Stationery"
       class="category-item">

        Lokta & Stationery

    </a>


    <a href="<%=request.getContextPath()%>/ProductServlet?action=category&category=Jewelry+%26+Accessories"
       class="category-item">

        Jewelry & Accessories

    </a>


    <a href="<%=request.getContextPath()%>/ProductServlet?action=category&category=Home+%26+D%C3%A9cor"
       class="category-item">

        Home & Décor

    </a>


    <a href="<%=request.getContextPath()%>/ProductServlet?action=category&category=Gifts+%26+Souvenirs"
       class="category-item">

        Gifts & Souvenirs

    </a>


    <a href="<%=request.getContextPath()%>/ProductServlet?action=category&category=Others"
       class="category-item">

        Others

    </a>


</div>



<!-- =========================
     ADMIN ADD PRODUCT
========================= -->

<%
if(user != null &&
   "admin".equalsIgnoreCase(user.getRole())){
%>

<div style="text-align:center; margin-bottom:20px;">

<a href="<%=request.getContextPath()%>/addProduct.jsp">

    <button type="button">
        Add New Product
    </button>

</a>

</div>

<%
}
%>



<!-- =========================
     PRODUCTS
========================= -->

<div class="products-container">


<%
if(products != null &&
   !products.isEmpty()){


    for(Product p : products){
%>


<div class="product-card">


    <img src="<%=p.getImage()%>"
         alt="<%=p.getName()%>">


    <h2>
        <%=p.getName()%>
    </h2>


    <p class="product-category">
        <%=p.getCategory()%>
    </p>


    <p>
        <%=p.getDescription()%>
    </p>


    <h3>
        Rs. <%=p.getPrice()%>
    </h3>



    <!-- CUSTOMER -->

    <%
    if(user != null &&
       "customer".equalsIgnoreCase(user.getRole())){
    %>


    <div class="product-buy-buttons">


        <a href="<%=request.getContextPath()%>/CartServlet?action=add&id=<%=p.getId()%>&quantity=1">

            <button type="button">
                Add To Cart
            </button>

        </a>


        <a href="<%=request.getContextPath()%>/checkout.jsp?buyNow=true&productId=<%=p.getId()%>">

            <button type="button">
                Buy Now
            </button>

        </a>


    </div>


    <%
    }
    %>



    <!-- ADMIN -->

    <%
    if(user != null &&
       "admin".equalsIgnoreCase(user.getRole())){
    %>


    <div class="admin-product-buttons">


        <a href="<%=request.getContextPath()%>/ProductServlet?action=edit&id=<%=p.getId()%>">

            <button type="button"
                    class="edit-product-btn">

                Edit

            </button>

        </a>


        <button type="button"
                class="delete-btn"
                onclick="openDeletePopup(<%=p.getId()%>)">

            Delete

        </button>


    </div>


    <%
    }
    %>


</div>


<%
    }
}

else{
%>


<div class="card">

    <h2>
        No Products Available
    </h2>

</div>


<%
}
%>


</div>



<!-- =========================
     DELETE CONFIRMATION
========================= -->

<div id="deleteModal"
     class="delete-modal">


    <div class="delete-box">


        <h2>
            Delete Product?
        </h2>


        <p>
            Are you sure you want to delete this product?
        </p>


        <div class="delete-actions">


            <button type="button"
                    class="cancel-delete"
                    onclick="closeDeletePopup()">

                Cancel

            </button>


            <a id="confirmDeleteLink"
               class="confirm-delete"
               href="#">

                Delete

            </a>


        </div>


    </div>


</div>



<script>


function openDeletePopup(productId){

    document.getElementById(
        "confirmDeleteLink"
    ).href =
        "<%=request.getContextPath()%>/ProductServlet?action=delete&id="
        + productId;


    document.getElementById(
        "deleteModal"
    ).style.display =
        "flex";
}



function closeDeletePopup(){

    var modal =
        document.getElementById(
            "deleteModal"
        );


    if(modal){

        modal.style.display =
            "none";
    }
}



function closeProductPopup(){

    var popup =
        document.getElementById(
            "productSuccessPopup"
        );


    if(popup){

        popup.style.display =
            "none";
    }
}



function closeCartPopup(){

    var popup =
        document.getElementById(
            "cartSuccessPopup"
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



window.addEventListener(
    "click",
    function(event){


        var deleteModal =
            document.getElementById(
                "deleteModal"
            );


        if(deleteModal &&
           event.target === deleteModal){

            closeDeletePopup();
        }



        var productPopup =
            document.getElementById(
                "productSuccessPopup"
            );


        if(productPopup &&
           event.target === productPopup){

            closeProductPopup();
        }



        var cartSuccess =
            document.getElementById(
                "cartSuccessPopup"
            );


        if(cartSuccess &&
           event.target === cartSuccess){

            closeCartPopup();
        }



        var cartError =
            document.getElementById(
                "cartErrorPopup"
            );


        if(cartError &&
           event.target === cartError){

            closeCartErrorPopup();
        }

    }
);


</script>


</body>

</html>