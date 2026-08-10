package servlet;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import dao.CartDAO;
import dao.OrderDAO;
import dao.ProductDAO;

import helper.EmailUtil;

import model.CartItem;
import model.Product;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/EsewaSuccessServlet")
public class EsewaSuccessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private static final String SECRET_KEY =
            "8gBm/:&EnhH.1/q";

    private static final String PRODUCT_CODE =
            "EPAYTEST";

    private final CartDAO cartDAO =
            new CartDAO();

    private final OrderDAO orderDAO =
            new OrderDAO();

    private final ProductDAO productDAO =
            new ProductDAO();


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        if(session == null){

            response.sendRedirect("login.jsp");
            return;
        }


        User user =
                (User) session.getAttribute("user");


        if(user == null){

            response.sendRedirect("login.jsp");
            return;
        }


        if(!"customer".equalsIgnoreCase(
                user.getRole())){

            response.sendRedirect("home.jsp");
            return;
        }


        // =========================
        // EXACT LOGGED-IN CUSTOMER
        // =========================

        String customerEmail =
                user.getEmail();

        String customerName =
                user.getName();


        System.out.println(
                "========== ESEWA CUSTOMER =========="
        );

        System.out.println(
                "Customer name = "
                + customerName
        );

        System.out.println(
                "Customer email = "
                + customerEmail
        );

        System.out.println(
                "===================================="
        );


        String encodedData =
                request.getParameter("data");


        if(encodedData == null ||
           encodedData.trim().isEmpty()){

            response.sendRedirect(
                    "EsewaFailureServlet"
            );

            return;
        }


        try{

            // =========================
            // DECODE ESEWA RESPONSE
            // =========================

            encodedData =
                    encodedData.replace(
                            " ",
                            "+"
                    );


            String decodedJson =
                    new String(
                            Base64.getDecoder()
                                  .decode(encodedData),
                            StandardCharsets.UTF_8
                    );


            Map<String,String> paymentData =
                    parseJson(
                            decodedJson
                    );


            String status =
                    paymentData.get(
                            "status"
                    );


            String transactionUuid =
                    paymentData.get(
                            "transaction_uuid"
                    );


            String totalAmount =
                    paymentData.get(
                            "total_amount"
                    );


            String productCode =
                    paymentData.get(
                            "product_code"
                    );


            String signedFieldNames =
                    paymentData.get(
                            "signed_field_names"
                    );


            String receivedSignature =
                    paymentData.get(
                            "signature"
                    );


            String transactionCode =
                    paymentData.get(
                            "transaction_code"
                    );


            // =========================
            // EXPECTED SESSION VALUES
            // =========================

            String expectedUuid =
                    (String) session.getAttribute(
                            "esewaTransactionUuid"
                    );


            String expectedAmount =
                    (String) session.getAttribute(
                            "esewaTotalAmount"
                    );


            // =========================
            // BASIC CHECK
            // =========================

            if(!"COMPLETE".equals(status)
                    || !PRODUCT_CODE.equals(productCode)
                    || expectedUuid == null
                    || expectedAmount == null
                    || !expectedUuid.equals(transactionUuid)
                    || !sameAmount(
                            expectedAmount,
                            totalAmount
                    )
                    || signedFieldNames == null
                    || receivedSignature == null){

                response.sendRedirect(
                        "EsewaFailureServlet"
                );

                return;
            }


            // =========================
            // VERIFY SIGNATURE
            // =========================

            String signatureMessage =
                    createSignatureMessage(
                            signedFieldNames,
                            paymentData
                    );


            String generatedSignature =
                    generateSignature(
                            signatureMessage
                    );


            boolean validSignature =
                    MessageDigest.isEqual(
                            generatedSignature.getBytes(
                                    StandardCharsets.UTF_8
                            ),
                            receivedSignature.getBytes(
                                    StandardCharsets.UTF_8
                            )
                    );


            if(!validSignature){

                response.sendRedirect(
                        "EsewaFailureServlet"
                );

                return;
            }


            // =========================
            // DUPLICATE PROCESSING CHECK
            // =========================

            String processedUuid =
                    (String) session.getAttribute(
                            "processedEsewaTransaction"
                    );


            if(transactionUuid.equals(
                    processedUuid)){

                response.sendRedirect(
                        "orderSuccess.jsp"
                );

                return;
            }


            Boolean buyNowValue =
                    (Boolean) session.getAttribute(
                            "esewaBuyNow"
                    );


            boolean isBuyNow =
                    buyNowValue != null
                    && buyNowValue;


            int orderId;

            double finalTotal;


            // =================================================
            // BUY NOW + ESEWA
            // =================================================

            if(isBuyNow){

                Integer productId =
                        (Integer) session.getAttribute(
                                "esewaProductId"
                        );


                Integer quantity =
                        (Integer) session.getAttribute(
                                "esewaQuantity"
                        );


                if(productId == null){

                    response.sendRedirect(
                            "EsewaFailureServlet"
                    );

                    return;
                }


                if(quantity == null ||
                   quantity < 1){

                    quantity = 1;
                }


                Product product =
                        productDAO.getProductById(
                                productId
                        );


                if(product == null){

                    response.sendRedirect(
                            "EsewaFailureServlet"
                    );

                    return;
                }


                finalTotal =
                        product.getPrice()
                        * quantity;


                if(!sameAmount(
                        String.valueOf(finalTotal),
                        totalAmount)){

                    response.sendRedirect(
                            "EsewaFailureServlet"
                    );

                    return;
                }


                // CREATE ORDER FOR LOGGED-IN CUSTOMER

                orderId =
                        orderDAO.createOrder(
                                customerEmail,
                                finalTotal,
                                "eSewa"
                        );


                if(orderId <= 0){

                    response.sendRedirect(
                            "EsewaFailureServlet"
                    );

                    return;
                }


                // SAVE EXACT PRODUCT

                orderDAO.addOrderItem(
                        orderId,
                        productId,
                        quantity
                );

            }


            // =================================================
            // CART CHECKOUT + ESEWA
            // =================================================

            else{

                ArrayList<CartItem> cart =
                        cartDAO.getCart(
                                customerEmail
                        );


                if(cart == null ||
                   cart.isEmpty()){

                    response.sendRedirect(
                            "CartServlet"
                    );

                    return;
                }


                finalTotal = 0.0;


                for(CartItem item : cart){

                    finalTotal +=
                            item.getPrice()
                            * item.getQuantity();
                }


                if(!sameAmount(
                        String.valueOf(finalTotal),
                        totalAmount)){

                    response.sendRedirect(
                            "EsewaFailureServlet"
                    );

                    return;
                }


                // CREATE ORDER FOR LOGGED-IN CUSTOMER

                orderId =
                        orderDAO.createOrder(
                                customerEmail,
                                finalTotal,
                                "eSewa"
                        );


                if(orderId <= 0){

                    response.sendRedirect(
                            "EsewaFailureServlet"
                    );

                    return;
                }


                // SAVE EXACT CART PRODUCTS

                for(CartItem item : cart){

                    orderDAO.addOrderItem(
                            orderId,
                            item.getProductId(),
                            item.getQuantity()
                    );
                }


                // CLEAR ONLY THIS CUSTOMER'S CART

                cartDAO.clearCart(
                        customerEmail
                );
            }


            // =========================
            // SEND EMAIL TO CUSTOMER
            // =========================

            System.out.println(
                    "ESEWA EMAIL RECEIVER = "
                    + customerEmail
            );


            boolean emailSent =
                    EmailUtil.sendOrderConfirmation(
                            customerEmail,
                            customerName,
                            orderId,
                            finalTotal,
                            "eSewa"
                    );


            System.out.println(
                    "ESEWA EMAIL RESULT = "
                    + emailSent
            );


            // =========================
            // SAVE SUCCESS DATA
            // =========================

            session.setAttribute(
                    "processedEsewaTransaction",
                    transactionUuid
            );


            session.setAttribute(
                    "esewaTransactionCode",
                    transactionCode
            );


            session.setAttribute(
                    "paymentMethod",
                    "eSewa"
            );


            session.setAttribute(
                    "placedOrderId",
                    orderId
            );


            session.setAttribute(
                    "orderMessage",
                    "Order placed successfully!"
            );


            // =========================
            // REMOVE TEMP ESEWA DATA
            // =========================

            session.removeAttribute(
                    "esewaTransactionUuid"
            );


            session.removeAttribute(
                    "esewaTotalAmount"
            );


            session.removeAttribute(
                    "esewaBuyNow"
            );


            session.removeAttribute(
                    "esewaProductId"
            );


            session.removeAttribute(
                    "esewaQuantity"
            );


            // =========================
            // SUCCESS
            // =========================

            response.sendRedirect(
                    "orderSuccess.jsp"
            );


        }catch(Exception e){

            e.printStackTrace();

            response.sendRedirect(
                    "EsewaFailureServlet"
            );
        }
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        doGet(
                request,
                response
        );
    }


    // =========================
    // SIGNATURE MESSAGE
    // =========================

    private String createSignatureMessage(
            String signedFieldNames,
            Map<String,String> paymentData){

        StringBuilder message =
                new StringBuilder();


        String[] fields =
                signedFieldNames.split(",");


        for(int i = 0;
            i < fields.length;
            i++){

            String field =
                    fields[i].trim();


            String value =
                    paymentData.get(
                            field
                    );


            if(value == null){

                throw new IllegalArgumentException(
                        "Missing signed field: "
                        + field
                );
            }


            if(i > 0){

                message.append(",");
            }


            message.append(field)
                   .append("=")
                   .append(value);
        }


        return message.toString();
    }


    // =========================
    // GENERATE SIGNATURE
    // =========================

    private String generateSignature(
            String message)
            throws Exception{

        Mac mac =
                Mac.getInstance(
                        "HmacSHA256"
                );


        SecretKeySpec secretKey =
                new SecretKeySpec(
                        SECRET_KEY.getBytes(
                                StandardCharsets.UTF_8
                        ),
                        "HmacSHA256"
                );


        mac.init(
                secretKey
        );


        byte[] hash =
                mac.doFinal(
                        message.getBytes(
                                StandardCharsets.UTF_8
                        )
                );


        return Base64.getEncoder()
                     .encodeToString(
                             hash
                     );
    }


    // =========================
    // COMPARE AMOUNTS
    // =========================

    private boolean sameAmount(
            String first,
            String second){

        try{

            BigDecimal amount1 =
                    new BigDecimal(
                            first
                            .replace(",", "")
                            .trim()
                    );


            BigDecimal amount2 =
                    new BigDecimal(
                            second
                            .replace(",", "")
                            .trim()
                    );


            return amount1.compareTo(
                    amount2
            ) == 0;


        }catch(Exception e){

            return false;
        }
    }


    // =========================
    // SIMPLE JSON PARSER
    // =========================

    private Map<String,String> parseJson(
            String json){

        Map<String,String> values =
                new HashMap<>();


        Pattern pattern =
                Pattern.compile(
                        "\"([^\"]+)\"\\s*:\\s*"
                        + "(?:\"([^\"]*)\"|([^,}\\s]+))"
                );


        Matcher matcher =
                pattern.matcher(
                        json
                );


        while(matcher.find()){

            String key =
                    matcher.group(1);


            String value =
                    matcher.group(2) != null
                    ? matcher.group(2)
                    : matcher.group(3);


            values.put(
                    key,
                    value
            );
        }


        return values;
    }
}