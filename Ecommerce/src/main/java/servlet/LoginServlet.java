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

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        // =========================
        // GET LOGIN DATA
        // =========================

        String email =
                request.getParameter("email");

        String password =
                request.getParameter("password");

        String selectedRole =
                request.getParameter("role");


        // =========================
        // VALIDATION
        // =========================

        if(email == null ||
           password == null ||
           email.trim().isEmpty() ||
           password.trim().isEmpty()) {

            request.setAttribute(
                    "error",
                    "Please enter your email and password."
            );

            request.getRequestDispatcher(
                    "/login.jsp"
            ).forward(
                    request,
                    response
            );

            return;
        }


        email =
                email.trim().toLowerCase();


        // =========================
        // LOGIN FROM DATABASE
        // =========================

        UserDAO dao =
                new UserDAO();


        User user =
                dao.loginUser(
                        email,
                        password
                );


        // =========================
        // WRONG EMAIL / PASSWORD
        // =========================

        if(user == null) {

            request.setAttribute(
                    "error",
                    "Incorrect email or password."
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
        // WRONG LOGIN SECTION
        // =========================

        if(selectedRole == null ||
           !selectedRole.equalsIgnoreCase(
                   user.getRole())) {

            request.setAttribute(
                    "error",
                    "Please use the correct login section."
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
        // REMOVE OLD SESSION
        // =========================

        HttpSession oldSession =
                request.getSession(false);


        if(oldSession != null) {

            oldSession.invalidate();
        }


        // =========================
        // CREATE NEW SESSION
        // =========================

        HttpSession newSession =
                request.getSession(true);


        newSession.setAttribute(
                "user",
                user
        );


        // =========================
        // ADMIN LOGIN
        // =========================

        if("admin".equalsIgnoreCase(
                user.getRole())) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/adminDashboard.jsp"
            );

            return;
        }


        // =========================
        // CUSTOMER LOGIN
        // GO TO CUSTOMER HOME
        // =========================

        if("customer".equalsIgnoreCase(
                user.getRole())) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/home.jsp"
            );

            return;
        }


        // =========================
        // UNKNOWN ROLE
        // =========================

        newSession.invalidate();


        response.sendRedirect(
                request.getContextPath()
                + "/login.jsp"
        );
    }
}