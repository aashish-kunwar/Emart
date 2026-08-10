package helper;

import java.security.SecureRandom;
import java.util.Properties;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public class OTPUtil {

    // YOUR GMAIL - ONLY USED TO SEND EMAILS
    private static final String SENDER_EMAIL =
            "kunwaraashish2006@gmail.com";

    // GOOGLE APP PASSWORD
    private static final String APP_PASSWORD =
            "fout lusd cptb irzw";

    private static final SecureRandom random =
            new SecureRandom();


    // =========================
    // GENERATE OTP
    // =========================

    public static String generateOTP() {

        int otp =
                100000
                + random.nextInt(900000);

        return String.valueOf(otp);
    }


    // =========================
    // SEND OTP
    // =========================

    public static boolean sendOTP(
            String customerEmail,
            String customerName,
            String otp) {

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


            // YOUR EMAIL = FROM
            message.setFrom(
                    new InternetAddress(
                            SENDER_EMAIL,
                            "EMart"
                    )
            );


            // CUSTOMER EMAIL = TO
            message.setRecipient(
                    Message.RecipientType.TO,
                    new InternetAddress(
                            customerEmail
                    )
            );


            message.setSubject(
                    "EMart Email Verification Code"
            );


            String name =
                    customerName == null
                    ? "Customer"
                    : customerName;


            String body =
                    "<html>"
                    + "<body style='"
                    + "font-family:Arial,sans-serif;"
                    + "background:#f3f6fb;"
                    + "padding:30px;"
                    + "'>"

                    + "<div style='"
                    + "max-width:550px;"
                    + "margin:auto;"
                    + "background:white;"
                    + "padding:30px;"
                    + "border-radius:14px;"
                    + "'>"

                    + "<h2 style='color:#172554;'>"
                    + "Welcome to EMart, "
                    + escapeHtml(name)
                    + "!"
                    + "</h2>"

                    + "<p style='"
                    + "color:#475569;"
                    + "font-size:16px;"
                    + "line-height:1.6;"
                    + "'>"

                    + "Please use the following verification code "
                    + "to confirm your email address."

                    + "</p>"

                    + "<div style='"
                    + "text-align:center;"
                    + "margin:30px 0;"
                    + "'>"

                    + "<div style='"
                    + "display:inline-block;"
                    + "background:#eff6ff;"
                    + "color:#1e3a8a;"
                    + "font-size:32px;"
                    + "font-weight:bold;"
                    + "letter-spacing:8px;"
                    + "padding:18px 25px;"
                    + "border-radius:10px;"
                    + "'>"

                    + otp

                    + "</div>"
                    + "</div>"

                    + "<p style='color:#64748b;'>"
                    + "This OTP is valid for 5 minutes."
                    + "</p>"

                    + "<p style='color:#64748b;'>"
                    + "If you did not create an EMart account, "
                    + "you can safely ignore this email."
                    + "</p>"

                    + "<hr style='"
                    + "border:none;"
                    + "border-top:1px solid #e2e8f0;"
                    + "margin:25px 0;"
                    + "'>"

                    + "<p style='"
                    + "text-align:center;"
                    + "color:#94a3b8;"
                    + "font-size:13px;"
                    + "'>"

                    + "EMart • Shop • Save • Smile"

                    + "</p>"

                    + "</div>"
                    + "</body>"
                    + "</html>";


            message.setContent(
                    body,
                    "text/html; charset=UTF-8"
            );


            System.out.println(
                    "OTP SENDER = "
                    + SENDER_EMAIL
            );

            System.out.println(
                    "OTP RECEIVER = "
                    + customerEmail
            );


            Transport.send(message);


            return true;


        } catch(Exception e) {

            e.printStackTrace();

            return false;
        }
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