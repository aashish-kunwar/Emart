<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="dao.CartDAO" %>
<%@ page import="dao.ProductDAO" %>
<%@ page import="model.User" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.Product" %>

<%
User user =
        (User) session.getAttribute("user");

if(user == null){

    response.sendRedirect("login.jsp");
    return;
}

if(!"customer".equalsIgnoreCase(user.getRole())){

    response.sendRedirect("index.jsp");
    return;
}


/* =========================
   CHECKOUT TYPE
========================= */

String buyNow =
        request.getParameter("buyNow");

String productIdValue =
        request.getParameter("productId");


boolean isBuyNow =
        "true".equalsIgnoreCase(buyNow)
        && productIdValue != null;


double total = 0.0;

Product buyNowProduct = null;

ArrayList<CartItem> cart = null;


/* =========================
   BUY NOW
========================= */

if(isBuyNow){

    try{

        int productId =
                Integer.parseInt(
                        productIdValue
                );

        ProductDAO productDAO =
                new ProductDAO();

        buyNowProduct =
                productDAO.getProductById(
                        productId
                );

        if(buyNowProduct == null){

            response.sendRedirect(
                    "ProductServlet"
            );

            return;
        }

        // Default Buy Now quantity = 1
        total =
                buyNowProduct.getPrice();

    }
    catch(Exception e){

        e.printStackTrace();

        response.sendRedirect(
                "ProductServlet"
        );

        return;
    }
}


/* =========================
   CART CHECKOUT
========================= */

else{

    CartDAO cartDAO =
            new CartDAO();

    cart =
            cartDAO.getCart(
                    user.getEmail()
            );

    if(cart == null ||
       cart.isEmpty()){

        response.sendRedirect(
                "CartServlet"
        );

        return;
    }


    for(CartItem item : cart){

        total +=
                item.getPrice()
                * item.getQuantity();
    }
}
%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>Checkout - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=100">

</head>


<body>


<jsp:include page="navbar.jsp" />


<div class="form-container">


    <h2>
        Checkout
    </h2>


    <p>
        Customer:
        <strong>
            <%=user.getName()%>
        </strong>
    </p>


    <p>
        Email:
        <strong>
            <%=user.getEmail()%>
        </strong>
    </p>


    <hr style="margin:20px 0;">



    <!-- =========================
         BUY NOW
    ========================= -->

    <%
    if(isBuyNow){
    %>


        <h3>
            Buy Now
        </h3>


        <p>
            Product:
            <strong>
                <%=buyNowProduct.getName()%>
            </strong>
        </p>


        <p>
            Price:
            <strong>
                Rs. <%=buyNowProduct.getPrice()%>
            </strong>
        </p>


        <!-- QUANTITY SELECTOR -->

        <p style="margin-top:15px;">
            <strong>Quantity</strong>
        </p>


        <div class="quantity-box">

            <button type="button"
                    onclick="decreaseBuyQuantity()">

                −

            </button>


            <span id="buyQuantity"
                  class="cart-quantity">

                1

            </span>


            <button type="button"
                    onclick="increaseBuyQuantity()">

                +

            </button>

        </div>


        <p>
            Subtotal:
            <strong>
                Rs.
                <span id="buySubtotal">
                    <%=buyNowProduct.getPrice()%>
                </span>
            </strong>
        </p>


    <%
    }
    %>



    <!-- =========================
         CART CHECKOUT
    ========================= -->

    <%
    if(!isBuyNow){
    %>


        <h3>
            Cart Items
        </h3>


        <%
        for(CartItem item : cart){

            double itemSubtotal =
                    item.getPrice()
                    * item.getQuantity();
        %>


            <div class="checkout-item">


                <p>

                    <strong>
                        <%=item.getProductName()%>
                    </strong>

                    × <%=item.getQuantity()%>

                </p>


                <p>
                    Rs. <%=itemSubtotal%>
                </p>


            </div>


        <%
        }
        %>


    <%
    }
    %>


    <hr style="margin:20px 0;">



    <!-- TOTAL -->

    <h2>

        Total:
        Rs.

        <span id="checkoutTotal">
            <%=total%>
        </span>

    </h2>



    <h3 style="margin-top:25px;">
        Select Payment Method
    </h3>



    <!-- =========================
         CASH ON DELIVERY
    ========================= -->

    <form action="OrderServlet"
          method="post">


        <input type="hidden"
               name="paymentMethod"
               value="Cash on Delivery">


        <input type="hidden"
               name="buyNow"
               value="<%=isBuyNow%>">


        <% if(isBuyNow){ %>


            <input type="hidden"
                   name="productId"
                   value="<%=buyNowProduct.getId()%>">


            <input type="hidden"
                   name="quantity"
                   id="codQuantity"
                   value="1">


        <% } %>


        <button type="submit"
                class="payment-btn">

            Cash on Delivery

        </button>


    </form>



    <!-- =========================
         ESEWA
    ========================= -->

    <form action="esewaPay.jsp"
          method="get">


        <input type="hidden"
               name="buyNow"
               value="<%=isBuyNow%>">


        <% if(isBuyNow){ %>


            <input type="hidden"
                   name="productId"
                   value="<%=buyNowProduct.getId()%>">


            <input type="hidden"
                   name="quantity"
                   id="esewaQuantity"
                   value="1">


        <% } %>


        <button type="submit"
                class="payment-btn">

            Pay with eSewa

        </button>


    </form>



    <br>



    <!-- BACK -->

    <% if(isBuyNow){ %>

        <a href="ProductServlet">
            Back to Products
        </a>

    <% } else { %>

        <a href="CartServlet">
            Back to Cart
        </a>

    <% } %>


</div>



<!-- =========================
     BUY NOW QUANTITY SCRIPT
========================= -->

<%
if(isBuyNow){
%>

<script>

var buyQuantity = 1;

var productPrice =
    <%=buyNowProduct.getPrice()%>;


function increaseBuyQuantity(){

    buyQuantity++;

    updateBuyNowTotal();
}


function decreaseBuyQuantity(){

    if(buyQuantity > 1){

        buyQuantity--;

        updateBuyNowTotal();
    }
}


function updateBuyNowTotal(){

    var total =
        productPrice * buyQuantity;


    document.getElementById(
        "buyQuantity"
    ).innerText =
        buyQuantity;


    document.getElementById(
        "buySubtotal"
    ).innerText =
        total.toFixed(2);


    document.getElementById(
        "checkoutTotal"
    ).innerText =
        total.toFixed(2);


    // COD quantity

    document.getElementById(
        "codQuantity"
    ).value =
        buyQuantity;


    // eSewa quantity

    document.getElementById(
        "esewaQuantity"
    ).value =
        buyQuantity;
}

</script>

<%
}
%>


</body>

</html>