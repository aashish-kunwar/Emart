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

import model.CartItem;
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

    // eSewa test credentials
    private static final String SECRET_KEY =
            "8gBm/:&EnhH.1/q";

    private static final String PRODUCT_CODE =
            "EPAYTEST";

    private final CartDAO cartDAO =
            new CartDAO();

    private final OrderDAO orderDAO =
            new OrderDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);

        if (session == null) {

            response.sendRedirect("login.jsp");
            return;
        }

        User user =
                (User) session.getAttribute("user");

        if (user == null) {

            response.sendRedirect("login.jsp");
            return;
        }

        String encodedData =
                request.getParameter("data");

        if (encodedData == null
                || encodedData.trim().isEmpty()) {

            response.sendRedirect(
                    "EsewaFailureServlet"
            );

            return;
        }

        try {

            encodedData =
                    encodedData.replace(" ", "+");

            String decodedJson =
                    new String(
                            Base64.getDecoder()
                                  .decode(encodedData),
                            StandardCharsets.UTF_8
                    );

            Map<String, String> paymentData =
                    parseJson(decodedJson);

            String status =
                    paymentData.get("status");

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

            String expectedUuid =
                    (String) session.getAttribute(
                            "esewaTransactionUuid"
                    );

            String expectedAmount =
                    (String) session.getAttribute(
                            "esewaTotalAmount"
                    );

            // Basic payment verification
            if (!"COMPLETE".equals(status)
                    || !PRODUCT_CODE.equals(productCode)
                    || expectedUuid == null
                    || expectedAmount == null
                    || !expectedUuid.equals(
                            transactionUuid
                    )
                    || !sameAmount(
                            expectedAmount,
                            totalAmount
                    )
                    || signedFieldNames == null
                    || receivedSignature == null) {

                response.sendRedirect(
                        "EsewaFailureServlet"
                );

                return;
            }

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

            if (!validSignature) {

                response.sendRedirect(
                        "EsewaFailureServlet"
                );

                return;
            }

            /*
             * Prevent duplicate order creation
             * when the customer refreshes the page.
             */
            String processedUuid =
                    (String) session.getAttribute(
                            "processedEsewaTransaction"
                    );

            if (transactionUuid.equals(
                    processedUuid)) {

                response.sendRedirect(
                        "orderSuccess.jsp"
                );

                return;
            }

            ArrayList<CartItem> cart =
                    cartDAO.getCart(
                            user.getEmail()
                    );

            if (cart == null || cart.isEmpty()) {

                response.sendRedirect(
                        "CartServlet"
                );

                return;
            }

            double cartTotal = 0.0;

            for (CartItem item : cart) {

                cartTotal +=
                        item.getPrice()
                        * item.getQuantity();
            }

            // Verify cart total with paid amount
            if (!sameAmount(
                    String.valueOf(cartTotal),
                    totalAmount)) {

                response.sendRedirect(
                        "EsewaFailureServlet"
                );

                return;
            }

            // Create order
            int orderId =
                    orderDAO.createOrder(
                            user.getEmail(),
                            cartTotal
                    );

            if (orderId <= 0) {

                response.sendRedirect(
                        "EsewaFailureServlet"
                );

                return;
            }

            // Save order items
            for (CartItem item : cart) {

                orderDAO.addOrderItem(
                        orderId,
                        item.getProductId(),
                        item.getQuantity()
                );
            }

            // Clear cart only after successful order
            cartDAO.clearCart(
                    user.getEmail()
            );

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

            session.removeAttribute(
                    "esewaTransactionUuid"
            );

            session.removeAttribute(
                    "esewaTotalAmount"
            );

            response.sendRedirect(
                    "orderSuccess.jsp"
            );

        } catch (Exception e) {

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

        doGet(request, response);
    }

    private String createSignatureMessage(
            String signedFieldNames,
            Map<String, String> paymentData) {

        StringBuilder message =
                new StringBuilder();

        String[] fields =
                signedFieldNames.split(",");

        for (int i = 0;
                i < fields.length;
                i++) {

            String field =
                    fields[i].trim();

            String value =
                    paymentData.get(field);

            if (value == null) {

                throw new IllegalArgumentException(
                        "Missing signed field: "
                        + field
                );
            }

            if (i > 0) {

                message.append(",");
            }

            message.append(field)
                   .append("=")
                   .append(value);
        }

        return message.toString();
    }

    private String generateSignature(
            String message)
            throws Exception {

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

        mac.init(secretKey);

        byte[] hash =
                mac.doFinal(
                        message.getBytes(
                                StandardCharsets.UTF_8
                        )
                );

        return Base64.getEncoder()
                     .encodeToString(hash);
    }

    private boolean sameAmount(
            String firstAmount,
            String secondAmount) {

        try {

            BigDecimal first =
                    new BigDecimal(
                            firstAmount
                                    .replace(",", "")
                                    .trim()
                    );

            BigDecimal second =
                    new BigDecimal(
                            secondAmount
                                    .replace(",", "")
                                    .trim()
                    );

            return first.compareTo(second) == 0;

        } catch (Exception e) {

            return false;
        }
    }

    private Map<String, String> parseJson(
            String json) {

        Map<String, String> values =
                new HashMap<>();

        Pattern pattern =
                Pattern.compile(
                        "\"([^\"]+)\"\\s*:\\s*"
                        + "(?:\"([^\"]*)\""
                        + "|([^,}\\s]+))"
                );

        Matcher matcher =
                pattern.matcher(json);

        while (matcher.find()) {

            String key =
                    matcher.group(1);

            String value =
                    matcher.group(2) != null
                    ? matcher.group(2)
                    : matcher.group(3);

            values.put(key, value);
        }

        return values;
    }
}