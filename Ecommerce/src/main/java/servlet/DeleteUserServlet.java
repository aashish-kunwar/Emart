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

@WebServlet(name = "DeleteUserServlet", urlPatterns = {"/DeleteUserServlet"})
public class DeleteUserServlet extends HttpServlet {

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
                    + "/login.jsp"
            );

            return;
        }


        // =========================
        // CHECK ADMIN
        // =========================

        User admin =
                (User) session.getAttribute(
                        "user"
                );


        if(admin == null ||
           !"admin".equalsIgnoreCase(
                   admin.getRole())) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/login.jsp"
            );

            return;
        }


        // =========================
        // GET CUSTOMER EMAIL
        // =========================

        String email =
                request.getParameter(
                        "email"
                );


        if(email == null ||
           email.trim().isEmpty()) {

            session.setAttribute(
                    "userMessage",
                    "Customer email not found."
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/manageUsers.jsp"
            );

            return;
        }


        email =
                email.trim()
                     .toLowerCase();


        // =========================
        // GET CUSTOMER
        // =========================

        UserDAO userDAO =
                new UserDAO();


        User customer =
                userDAO.getUserByEmail(
                        email
                );


        if(customer == null) {

            session.setAttribute(
                    "userMessage",
                    "User is already unavailable."
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/manageUsers.jsp"
            );

            return;
        }


        // =========================
        // DON'T DELETE ADMIN
        // =========================

        if(!"customer".equalsIgnoreCase(
                customer.getRole())) {

            session.setAttribute(
                    "userMessage",
                    "Admin account cannot be deleted."
            );

            response.sendRedirect(
                    request.getContextPath()
                    + "/manageUsers.jsp"
            );

            return;
        }


        // =========================
        // DELETE CUSTOMER
        // =========================

        boolean deleted =
                userDAO.deleteUser(
                        email
                );


        if(deleted) {

            session.setAttribute(
                    "userMessage",
                    "Customer deleted successfully."
            );

        } else {

            session.setAttribute(
                    "userMessage",
                    "Unable to delete customer."
            );
        }


        // =========================
        // BACK TO MANAGE USERS
        // =========================

        response.sendRedirect(
                request.getContextPath()
                + "/manageUsers.jsp"
        );
    }


    // OPTIONAL:
    // IF USER OPENS SERVLET DIRECTLY

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        response.sendRedirect(
                request.getContextPath()
                + "/manageUsers.jsp"
        );
    }
}