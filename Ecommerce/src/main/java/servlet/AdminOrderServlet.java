package servlet;

import java.io.IOException;
import java.util.ArrayList;

import dao.OrderDAO;
import model.Order;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/AdminOrderServlet")
public class AdminOrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final OrderDAO dao =
            new OrderDAO();


    // =========================
    // SHOW ALL ORDERS
    // =========================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session =
                request.getSession(false);


        // =========================
        // ADMIN LOGIN CHECK
        // =========================

        if(session == null) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/login.jsp"
            );

            return;
        }


        User user =
                (User) session.getAttribute(
                        "user"
                );


        if(user == null ||
           !"admin".equalsIgnoreCase(
                   user.getRole())) {

            response.sendRedirect(
                    request.getContextPath()
                    + "/login.jsp"
            );

            return;
        }


        // =========================
        // CHECK ACTION
        // =========================

        String action =
                request.getParameter(
                        "action"
                );


        // =========================
        // UPDATE STATUS
        // =========================

        if("update".equalsIgnoreCase(action)) {

            String idValue =
                    request.getParameter(
                            "id"
                    );


            String status =
                    request.getParameter(
                            "status"
                    );


            if(idValue == null ||
               status == null ||
               idValue.trim().isEmpty() ||
               status.trim().isEmpty()) {

                session.setAttribute(
                        "errorMessage",
                        "Invalid order information."
                );


                response.sendRedirect(
                        request.getContextPath()
                        + "/AdminOrderServlet"
                );

                return;
            }


            try {

                int id =
                        Integer.parseInt(
                                idValue
                        );


                // =========================
                // VALID STATUS CHECK
                // =========================

                if(!"Pending".equalsIgnoreCase(status) &&
                   !"Delivered".equalsIgnoreCase(status) &&
                   !"Cancelled".equalsIgnoreCase(status)) {

                    session.setAttribute(
                            "errorMessage",
                            "Invalid order status."
                    );


                    response.sendRedirect(
                            request.getContextPath()
                            + "/AdminOrderServlet"
                    );

                    return;
                }


                // =========================
                // CORRECT DAO METHOD
                // =========================

                boolean updated =
                        dao.updateOrderStatus(
                                id,
                                status
                        );


                if(updated) {

                    session.setAttribute(
                            "successMessage",
                            "Order status updated successfully."
                    );

                } else {

                    session.setAttribute(
                            "errorMessage",
                            "Unable to update order status."
                    );
                }


            } catch(NumberFormatException e) {

                session.setAttribute(
                        "errorMessage",
                        "Invalid order ID."
                );
            }


            response.sendRedirect(
                    request.getContextPath()
                    + "/AdminOrderServlet"
            );

            return;
        }


        // =========================
        // SHOW ALL ORDERS
        // =========================

        ArrayList<Order> orders =
                dao.getAllOrders();


        request.setAttribute(
                "orders",
                orders
        );


        request.getRequestDispatcher(
                "/manageOrders.jsp"
        ).forward(
                request,
                response
        );
    }


    // =========================
    // POST
    // =========================

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
}