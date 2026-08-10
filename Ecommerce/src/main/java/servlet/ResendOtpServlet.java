package servlet;

import java.io.IOException;

import helper.OTPUtil;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ResendOtpServlet")
public class ResendOtpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        // =========================
        // GET CURRENT SESSION
        // =========================

        HttpSession session =
                request.getSession(false);


        if(session == null){

            response.sendRedirect(
                    request.getContextPath()
                    + "/register.jsp"
            );

            return;
        }


        // =========================
        // GET CUSTOMER WAITING
        // FOR OTP VERIFICATION
        // =========================

        User pendingUser =
                (User) session.getAttribute(
                        "pendingUser"
                );


        if(pendingUser == null){

            response.sendRedirect(
                    request.getContextPath()
                    + "/register.jsp"
            );

            return;
        }


        // =========================
        // CUSTOMER EMAIL
        // =========================

        String customerEmail =
                pendingUser.getEmail();


        String customerName =
                pendingUser.getName();


        if(customerEmail == null ||
           customerEmail.trim().isEmpty()){

            request.setAttribute(
                    "error",
                    "Customer email could not be found. Please register again."
            );


            request.getRequestDispatcher(
                    "/register.jsp"
            ).forward(
                    request,
                    response
            );


            return;
        }


        // =========================
        // DEBUG
        // =========================

        System.out.println(
                "========== RESEND OTP =========="
        );


        System.out.println(
                "Customer name = "
                + customerName
        );


        System.out.println(
                "OTP receiver = "
                + customerEmail
        );


        System.out.println(
                "================================"
        );


        // =========================
        // GENERATE NEW OTP
        // =========================

        String newOtp =
                OTPUtil.generateOTP();


        // =========================
        // SEND TO CUSTOMER EMAIL
        // =========================

        boolean sent =
                OTPUtil.sendOTP(
                        customerEmail,
                        customerName,
                        newOtp
                );


        if(!sent){

            request.setAttribute(
                    "error",
                    "Unable to resend OTP. Please try again."
            );


            request.getRequestDispatcher(
                    "/verifyOtp.jsp"
            ).forward(
                    request,
                    response
            );


            return;
        }


        // =========================
        // REPLACE OLD OTP
        // =========================

        session.setAttribute(
                "registrationOtp",
                newOtp
        );


        // =========================
        // NEW EXPIRY = 5 MINUTES
        // =========================

        long newExpiry =
                System.currentTimeMillis()
                + (5 * 60 * 1000);


        session.setAttribute(
                "otpExpiry",
                newExpiry
        );


        // =========================
        // SUCCESS MESSAGE
        // =========================

        request.setAttribute(
                "success",
                "A new OTP has been sent to "
                + customerEmail
                + "."
        );


        request.getRequestDispatcher(
                "/verifyOtp.jsp"
        ).forward(
                request,
                response
        );
    }
}