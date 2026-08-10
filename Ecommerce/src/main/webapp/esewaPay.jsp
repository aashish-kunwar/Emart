<%@ page language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.UUID" %>
<%@ page import="java.util.Locale" %>

<%@ page import="dao.CartDAO" %>
<%@ page import="dao.ProductDAO" %>

<%@ page import="model.User" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.Product" %>

<%@ page import="helper.EsewaUtil" %>

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

String quantityValue =
        request.getParameter("quantity");


boolean isBuyNow =
        "true".equalsIgnoreCase(buyNow)
        && productIdValue != null;


int buyNowQuantity = 1;


if(quantityValue != null){

    try{

        buyNowQuantity =
                Integer.parseInt(
                        quantityValue
                );

    }catch(Exception e){

        buyNowQuantity = 1;
    }
}


if(buyNowQuantity < 1){

    buyNowQuantity = 1;
}


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


        total =
                buyNowProduct.getPrice()
                * buyNowQuantity;


        // Save Buy Now information
        // for EsewaSuccessServlet

        session.setAttribute(
                "esewaBuyNow",
                true
        );


        session.setAttribute(
                "esewaProductId",
                productId
        );


        session.setAttribute(
                "esewaQuantity",
                buyNowQuantity
        );


    }catch(Exception e){

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


    session.setAttribute(
            "esewaBuyNow",
            false
    );


    session.removeAttribute(
            "esewaProductId"
    );


    session.removeAttribute(
            "esewaQuantity"
    );
}


/* =========================
   ESEWA VALUES
========================= */

String amount =
        String.format(
                Locale.US,
                "%.2f",
                total
        );


String taxAmount =
        "0";


String serviceCharge =
        "0";


String deliveryCharge =
        "0";


String totalAmount =
        amount;


/* Unique transaction UUID */

String transactionUuid =
        UUID.randomUUID().toString();


/* Generate signature */

String signature =
        EsewaUtil.generateSignature(
                totalAmount,
                transactionUuid
        );


/* =========================
   SAVE PAYMENT DATA
========================= */

session.setAttribute(
        "esewaTransactionUuid",
        transactionUuid
);


session.setAttribute(
        "esewaTotalAmount",
        totalAmount
);


/* =========================
   CALLBACK URLs
========================= */

String baseUrl =
        request.getScheme()
        + "://"
        + request.getServerName()
        + ":"
        + request.getServerPort()
        + request.getContextPath();


String successUrl =
        baseUrl
        + "/EsewaSuccessServlet";


String failureUrl =
        baseUrl
        + "/EsewaFailureServlet";
%>


<!DOCTYPE html>

<html>

<head>

<meta charset="UTF-8">

<title>eSewa Payment - EMart</title>

<link rel="stylesheet"
      href="<%=request.getContextPath()%>/assets/css/style.css?v=93">

</head>


<body>


<jsp:include page="navbar.jsp" />


<div class="form-container">


    <h2>
        Pay with eSewa
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
         BUY NOW DETAILS
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


        <p>
            Quantity:
            <strong>
                <%=buyNowQuantity%>
            </strong>
        </p>


        <p>
            Subtotal:
            <strong>
                Rs. <%=buyNowProduct.getPrice() * buyNowQuantity%>
            </strong>
        </p>

    <%
    }
    %>


    <!-- =========================
         CART DETAILS
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


    <h2>
        Total: Rs. <%=totalAmount%>
    </h2>


    <!-- =========================
         ESEWA FORM
    ========================= -->

    <form action="<%=EsewaUtil.PAYMENT_URL%>"
          method="post">


        <input type="hidden"
               name="amount"
               value="<%=amount%>">


        <input type="hidden"
               name="tax_amount"
               value="<%=taxAmount%>">


        <input type="hidden"
               name="total_amount"
               value="<%=totalAmount%>">


        <input type="hidden"
               name="transaction_uuid"
               value="<%=transactionUuid%>">


        <input type="hidden"
               name="product_code"
               value="<%=EsewaUtil.PRODUCT_CODE%>">


        <input type="hidden"
               name="product_service_charge"
               value="<%=serviceCharge%>">


        <input type="hidden"
               name="product_delivery_charge"
               value="<%=deliveryCharge%>">


        <input type="hidden"
               name="success_url"
               value="<%=successUrl%>">


        <input type="hidden"
               name="failure_url"
               value="<%=failureUrl%>">


        <input type="hidden"
               name="signed_field_names"
               value="total_amount,transaction_uuid,product_code">


        <input type="hidden"
               name="signature"
               value="<%=signature%>">


        <button type="submit"
                class="payment-btn">

            Continue to eSewa

        </button>


    </form>


    <br>


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


</body>

</html>