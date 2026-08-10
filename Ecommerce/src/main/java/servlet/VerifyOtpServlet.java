package servlet;

import java.io.IOException;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/VerifyOtpServlet")
public class VerifyOtpServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        // =========================
        // GET SESSION
        // =========================

        HttpSession session =
                request.getSession(false);


        if(session == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/register.jsp"
            );

            return;
        }


        // =========================
        // GET PENDING USER
        // =========================

        User pendingUser =
                (User) session.getAttribute(
                        "pendingUser"
                );


        String savedOtp =
                (String) session.getAttribute(
                        "registrationOtp"
                );


        Long otpExpiry =
                (Long) session.getAttribute(
                        "otpExpiry"
                );


        // =========================
        // CHECK REGISTRATION SESSION
        // =========================

        if(pendingUser == null ||
           savedOtp == null ||
           otpExpiry == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/register.jsp"
            );

            return;
        }


        // =========================
        // GET ENTERED OTP
        // =========================

        String enteredOtp =
                request.getParameter(
                        "otp"
                );


        if(enteredOtp == null ||
           enteredOtp.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Please enter the OTP."
            );


            request.getRequestDispatcher(
                    "/verifyOtp.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }


        enteredOtp =
                enteredOtp.trim();


        // =========================
        // CHECK OTP EXPIRY
        // =========================

        if(System.currentTimeMillis()
                > otpExpiry) {

            request.setAttribute(
                    "error",
                    "OTP has expired. Please request a new OTP."
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
        // CHECK OTP
        // =========================

        if(!savedOtp.equals(enteredOtp)) {

            request.setAttribute(
                    "error",
                    "Incorrect OTP. Please try again."
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
        // DEBUG CUSTOMER
        // =========================

        System.out.println(
                "========== OTP VERIFIED =========="
        );

        System.out.println(
                "Customer Name = "
                + pendingUser.getName()
        );

        System.out.println(
                "Customer Email = "
                + pendingUser.getEmail()
        );

        System.out.println(
                "=================================="
        );


        // =========================
        // CHECK DUPLICATE EMAIL
        // =========================

        UserDAO userDAO =
                new UserDAO();


        if(userDAO.emailExists(
                pendingUser.getEmail())) {

            // Remove temporary registration

            session.removeAttribute(
                    "pendingUser"
            );

            session.removeAttribute(
                    "registrationOtp"
            );

            session.removeAttribute(
                    "otpExpiry"
            );


            request.setAttribute(
                    "error",
                    "This email is already registered. Please login."
            );


            request.getRequestDispatcher(
                    "/login.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }


        // =========================
        // SAVE VERIFIED CUSTOMER
        // =========================

        boolean registered =
                userDAO.registerUser(
                        pendingUser
                );


        if(!registered) {

            request.setAttribute(
                    "error",
                    "Registration failed. Please try again."
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
        // REGISTRATION COMPLETE
        // =========================

        String verifiedEmail =
                pendingUser.getEmail();


        // Remove OTP information

        session.removeAttribute(
                "pendingUser"
        );

        session.removeAttribute(
                "registrationOtp"
        );

        session.removeAttribute(
                "otpExpiry"
        );


        // =========================
        // SUCCESS MESSAGE
        // =========================

        session.setAttribute(
                "registrationSuccess",
                "Email "
                + verifiedEmail
                + " verified successfully! "
                + "Your EMart account has been created. Please login."
        );


        // =========================
        // GO TO LOGIN
        // =========================

        response.sendRedirect(
                request.getContextPath()
                + "/login.jsp"
        );
    }
}