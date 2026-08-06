package servlet;

import java.io.IOException;
import java.util.ArrayList;

import dao.CartDAO;
import dao.OrderDAO;

import model.CartItem;
import model.Order;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final OrderDAO orderDAO = new OrderDAO();
    private final CartDAO cartDAO = new CartDAO();

    // Place Cash on Delivery order
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (!"customer".equals(user.getRole())) {
            response.sendRedirect("home.jsp");
            return;
        }

        String paymentMethod =
                request.getParameter("paymentMethod");

        /*
         * This servlet handles only Cash on Delivery.
         * eSewa orders are handled after successful payment
         * in EsewaSuccessServlet.
         */
        if (!"Cash on Delivery".equals(paymentMethod)) {
            response.sendRedirect("checkout.jsp");
            return;
        }

        ArrayList<CartItem> cart =
                cartDAO.getCart(user.getEmail());

        if (cart == null || cart.isEmpty()) {
            response.sendRedirect("CartServlet");
            return;
        }

        double total = 0.0;

        for (CartItem item : cart) {
            total += item.getPrice() * item.getQuantity();
        }

        int orderId =
                orderDAO.createOrder(
                        user.getEmail(),
                        total
                );

        if (orderId <= 0) {

            session.setAttribute(
                    "orderMessage",
                    "Order could not be placed."
            );

            response.sendRedirect("checkout.jsp");
            return;
        }

        for (CartItem item : cart) {

            orderDAO.addOrderItem(
                    orderId,
                    item.getProductId(),
                    item.getQuantity()
            );
        }

        // Clear cart after successful order
        cartDAO.clearCart(user.getEmail());

        session.setAttribute(
                "paymentMethod",
                "Cash on Delivery"
        );

        session.setAttribute(
                "placedOrderId",
                orderId
        );

        response.sendRedirect("orderSuccess.jsp");
    }

    // Show customer's order history
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        User user = (User) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        if (!"customer".equals(user.getRole())) {
            response.sendRedirect("home.jsp");
            return;
        }

        String action = request.getParameter("action");

        if ("myOrders".equals(action)) {

            ArrayList<Order> orders =
                    orderDAO.getUserOrders(
                            user.getEmail()
                    );

            request.setAttribute("orders", orders);

            request.getRequestDispatcher("myOrders.jsp")
                   .forward(request, response);

        } else {

            response.sendRedirect("ProductServlet");
        }
    }
}
