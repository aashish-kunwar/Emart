package servlet;

import java.io.IOException;

import dao.UserDAO;
import helper.OTPUtil;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // =========================
        // GET FORM DATA
        // =========================

        String name =
                request.getParameter("name");

        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");


        // =========================
        // EMPTY FIELD CHECK
        // =========================

        if(name == null ||
           email == null ||
           password == null ||
           name.trim().isEmpty() ||
           email.trim().isEmpty() ||
           password.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Please fill in all fields."
            );

            request.getRequestDispatcher(
                    "/register.jsp"
            ).forward(request, response);

            return;
        }


        name = name.trim();

        email =
                email.trim()
                     .toLowerCase();


        // =========================
        // CHECK EMAIL ALREADY EXISTS
        // =========================

        UserDAO userDAO =
                new UserDAO();


        if(userDAO.emailExists(email)) {

            request.setAttribute(
                    "error",
                    "Account already exists with this email address."
            );

            request.getRequestDispatcher(
                    "/register.jsp"
            ).forward(request, response);

            return;
        }


        // =========================
        // CREATE TEMP USER
        // =========================

        User pendingUser =
                new User();


        pendingUser.setName(name);

        pendingUser.setEmail(email);

        pendingUser.setPassword(password);

        pendingUser.setRole("customer");


        // =========================
        // GENERATE OTP
        // =========================

        String otp =
                OTPUtil.generateOTP();


        // =========================
        // SEND OTP
        // =========================

        boolean sent =
                OTPUtil.sendOTP(
                        pendingUser.getEmail(),
                        pendingUser.getName(),
                        otp
                );


        // =========================
        // EMAIL FAILED
        // =========================

        if(!sent) {

            request.setAttribute(
                    "error",
                    "Unable to send OTP. Please try again."
            );

            request.getRequestDispatcher(
                    "/register.jsp"
            ).forward(request, response);

            return;
        }


        // =========================
        // SAVE TEMP USER IN SESSION
        // =========================

        HttpSession session =
                request.getSession();


        session.setAttribute(
                "pendingUser",
                pendingUser
        );


        session.setAttribute(
                "registrationOtp",
                otp
        );


        // =========================
        // OTP EXPIRY = 5 MINUTES
        // =========================

        long otpExpiry =
                System.currentTimeMillis()
                + (5 * 60 * 1000);


        session.setAttribute(
                "otpExpiry",
                otpExpiry
        );


        // =========================
        // GO TO OTP PAGE
        // =========================

        response.sendRedirect(
                request.getContextPath()
                + "/verifyOtp.jsp"
        );
    }
}