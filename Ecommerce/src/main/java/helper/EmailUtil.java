package helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

import database.DBConnection;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class EmailUtil {

    // YOUR GMAIL - ONLY SENDER
    private static final String SENDER_EMAIL =
            "kunwaraashish2006@gmail.com";

    // GOOGLE APP PASSWORD
    private static final String APP_PASSWORD =
            "fout lusd cptb irzw";


    // =========================
    // ORDER CONFIRMATION
    // =========================

    public static boolean sendOrderConfirmation(
            String customerEmail,
            String customerName,
            int orderId,
            double totalAmount,
            String paymentMethod) {

        try {

            if(customerEmail == null ||
               customerEmail.trim().isEmpty()) {

                System.out.println(
                        "Customer email is empty."
                );

                return false;
            }


            customerEmail =
                    customerEmail.trim();


            Properties properties =
                    new Properties();


            properties.put(
                    "mail.smtp.auth",
                    "true"
            );

            properties.put(
                    "mail.smtp.starttls.enable",
                    "true"
            );

            properties.put(
                    "mail.smtp.host",
                    "smtp.gmail.com"
            );

            properties.put(
                    "mail.smtp.port",
                    "587"
            );


            Session mailSession =
                    Session.getInstance(
                            properties,
                            new Authenticator() {

                                @Override
                                protected PasswordAuthentication
                                getPasswordAuthentication() {

                                    return new PasswordAuthentication(
                                            SENDER_EMAIL,
                                            APP_PASSWORD
                                    );
                                }
                            }
                    );


            MimeMessage message =
                    new MimeMessage(mailSession);


            // YOUR GMAIL = FROM
            message.setFrom(
                    new InternetAddress(
                            SENDER_EMAIL,
                            "EMart"
                    )
            );


            // CUSTOMER GMAIL = TO
            message.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(
                            customerEmail
                    )
            );


            message.setSubject(
                    "Your EMart Order is Confirmed | Order #"
                    + orderId
            );


            String productRows =
                    getOrderProducts(
                            orderId
                    );


            String name =
                    customerName == null
                    ? "Customer"
                    : customerName;


            String body =
                    "<html>"

                    + "<body style='"
                    + "margin:0;"
                    + "padding:30px;"
                    + "background:#f3f6fb;"
                    + "font-family:Arial,sans-serif;"
                    + "'>"


                    + "<div style='"
                    + "max-width:650px;"
                    + "margin:auto;"
                    + "background:white;"
                    + "border-radius:14px;"
                    + "overflow:hidden;"
                    + "'>"


                    + "<div style='"
                    + "background:#172554;"
                    + "color:white;"
                    + "padding:25px;"
                    + "text-align:center;"
                    + "'>"

                    + "<h1 style='margin:0;'>"
                    + "EMart"
                    + "</h1>"

                    + "</div>"


                    + "<div style='padding:30px;'>"

                    + "<h2 style='color:#172554;'>"

                    + "Congratulations, "
                    + escapeHtml(name)
                    + "! 🎉"

                    + "</h2>"


                    + "<p style='"
                    + "color:#475569;"
                    + "font-size:16px;"
                    + "line-height:1.6;"
                    + "'>"

                    + "Your order has been placed successfully "
                    + "and is now being prepared."

                    + "</p>"


                    + "<div style='"
                    + "background:#f8fafc;"
                    + "padding:18px;"
                    + "border-radius:10px;"
                    + "margin:20px 0;"
                    + "'>"

                    + "<p>"
                    + "<strong>Order ID:</strong> #"
                    + orderId
                    + "</p>"

                    + "<p>"
                    + "<strong>Customer:</strong> "
                    + escapeHtml(name)
                    + "</p>"

                    + "<p>"
                    + "<strong>Email:</strong> "
                    + escapeHtml(customerEmail)
                    + "</p>"

                    + "<p>"
                    + "<strong>Payment Method:</strong> "
                    + escapeHtml(paymentMethod)
                    + "</p>"

                    + "<p>"
                    + "<strong>Status:</strong> Pending"
                    + "</p>"

                    + "</div>"


                    + "<h3 style='color:#172554;'>"
                    + "Order Items"
                    + "</h3>"


                    + "<table style='"
                    + "width:100%;"
                    + "border-collapse:collapse;"
                    + "'>"

                    + "<tr style='"
                    + "background:#1e3a8a;"
                    + "color:white;"
                    + "'>"

                    + "<th style='padding:10px;'>Product</th>"
                    + "<th style='padding:10px;'>Quantity</th>"
                    + "<th style='padding:10px;'>Price</th>"
                    + "<th style='padding:10px;'>Subtotal</th>"

                    + "</tr>"

                    + productRows

                    + "</table>"


                    + "<div style='"
                    + "margin-top:20px;"
                    + "padding:18px;"
                    + "background:#eff6ff;"
                    + "text-align:right;"
                    + "border-radius:8px;"
                    + "'>"

                    + "<strong style='"
                    + "font-size:20px;"
                    + "color:#1e3a8a;"
                    + "'>"

                    + "Total: Rs. "
                    + String.format(
                            "%.2f",
                            totalAmount
                    )

                    + "</strong>"

                    + "</div>"


                    + "<div style='"
                    + "background:#fef3c7;"
                    + "padding:18px;"
                    + "border-radius:8px;"
                    + "margin-top:20px;"
                    + "'>"

                    + "<strong>"
                    + "Delivery Information"
                    + "</strong>"

                    + "<p style='margin-bottom:0;'>"

                    + "Your order is expected to be delivered "
                    + "within approximately "
                    + "<strong>3 days</strong>."

                    + "</p>"

                    + "</div>"


                    + "<p style='"
                    + "color:#64748b;"
                    + "margin-top:25px;"
                    + "'>"

                    + "You can track your order from the "
                    + "<strong>My Orders</strong> section."

                    + "</p>"


                    + "<p style='color:#475569;'>"
                    + "Thank you for shopping with EMart."
                    + "</p>"


                    + "<p style='color:#475569;'>"
                    + "Warm regards,<br>"
                    + "<strong>EMart Team</strong>"
                    + "</p>"


                    + "</div>"
                    + "</div>"
                    + "</body>"
                    + "</html>";


            message.setContent(
                    body,
                    "text/html; charset=UTF-8"
            );


            System.out.println(
                    "ORDER EMAIL SENDER = "
                    + SENDER_EMAIL
            );

            System.out.println(
                    "ORDER EMAIL RECEIVER = "
                    + customerEmail
            );


            Transport.send(message);


            return true;


        } catch(Exception e) {

            e.printStackTrace();

            return false;
        }
    }


    // =========================
    // GET ORDER PRODUCTS
    // =========================

    private static String getOrderProducts(
            int orderId) {

        StringBuilder rows =
                new StringBuilder();


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT "
                    + "p.name, "
                    + "p.price, "
                    + "oi.quantity "
                    + "FROM order_items oi "
                    + "INNER JOIN products p "
                    + "ON oi.product_id = p.id "
                    + "WHERE oi.order_id = ?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setInt(
                    1,
                    orderId
            );


            ResultSet rs =
                    ps.executeQuery();


            while(rs.next()) {

                String productName =
                        rs.getString(
                                "name"
                        );


                double price =
                        rs.getDouble(
                                "price"
                        );


                int quantity =
                        rs.getInt(
                                "quantity"
                        );


                double subtotal =
                        price
                        * quantity;


                rows.append(
                        "<tr style='"
                        + "border-bottom:1px solid #e2e8f0;"
                        + "'>"

                        + "<td style='padding:10px;'>"
                        + escapeHtml(productName)
                        + "</td>"

                        + "<td style='"
                        + "padding:10px;"
                        + "text-align:center;"
                        + "'>"
                        + quantity
                        + "</td>"

                        + "<td style='"
                        + "padding:10px;"
                        + "text-align:right;"
                        + "'>"

                        + "Rs. "
                        + String.format(
                                "%.2f",
                                price
                        )

                        + "</td>"

                        + "<td style='"
                        + "padding:10px;"
                        + "text-align:right;"
                        + "'>"

                        + "Rs. "
                        + String.format(
                                "%.2f",
                                subtotal
                        )

                        + "</td>"

                        + "</tr>"
                );
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return rows.toString();
    }


    private static String escapeHtml(
            String value) {

        if(value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }
}