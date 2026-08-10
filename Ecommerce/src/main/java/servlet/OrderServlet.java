package servlet;

import java.io.IOException;
import java.util.ArrayList;

import dao.CartDAO;
import dao.OrderDAO;
import dao.ProductDAO;

import helper.EmailUtil;

import model.CartItem;
import model.Order;
import model.Product;
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

    private final OrderDAO orderDAO =
            new OrderDAO();

    private final CartDAO cartDAO =
            new CartDAO();

    private final ProductDAO productDAO =
            new ProductDAO();


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        if(session == null){

            response.sendRedirect(
                    "login.jsp"
            );

            return;
        }


        User user =
                (User) session.getAttribute(
                        "user"
                );


        if(user == null){

            response.sendRedirect(
                    "login.jsp"
            );

            return;
        }


        if(!"customer".equalsIgnoreCase(
                user.getRole())){

            response.sendRedirect(
                    "home.jsp"
            );

            return;
        }


        // =========================
        // CUSTOMER EMAIL
        // =========================

        String customerEmail =
                user.getEmail();


        String customerName =
                user.getName();


        System.out.println(
                "========== COD ORDER =========="
        );

        System.out.println(
                "Customer Name = "
                + customerName
        );

        System.out.println(
                "ORDER EMAIL RECEIVER = "
                + customerEmail
        );

        System.out.println(
                "==============================="
        );


        // =========================
        // PAYMENT METHOD
        // =========================

        String paymentMethod =
                request.getParameter(
                        "paymentMethod"
                );


        if(!"Cash on Delivery".equals(
                paymentMethod)){

            response.sendRedirect(
                    "checkout.jsp"
            );

            return;
        }


        // =========================
        // BUY NOW CHECK
        // =========================

        boolean buyNow =
                "true".equalsIgnoreCase(
                        request.getParameter(
                                "buyNow"
                        )
                );


        // =================================================
        // BUY NOW ORDER
        // =================================================

        if(buyNow){

            try{

                int productId =
                        Integer.parseInt(
                                request.getParameter(
                                        "productId"
                                )
                        );


                int quantity = 1;


                String quantityValue =
                        request.getParameter(
                                "quantity"
                        );


                if(quantityValue != null){

                    try{

                        quantity =
                                Integer.parseInt(
                                        quantityValue
                                );

                    }catch(Exception e){

                        quantity = 1;
                    }
                }


                if(quantity < 1){

                    quantity = 1;
                }


                Product product =
                        productDAO.getProductById(
                                productId
                        );


                if(product == null){

                    response.sendRedirect(
                            "ProductServlet"
                    );

                    return;
                }


                double total =
                        product.getPrice()
                        * quantity;


                // =========================
                // CREATE ORDER
                // =========================

                int orderId =
                        orderDAO.createOrder(
                                customerEmail,
                                total,
                                "Cash on Delivery"
                        );


                if(orderId <= 0){

                    session.setAttribute(
                            "orderMessage",
                            "Order could not be placed."
                    );


                    response.sendRedirect(
                            "checkout.jsp"
                    );

                    return;
                }


                // =========================
                // SAVE ORDER ITEM
                // =========================

                orderDAO.addOrderItem(
                        orderId,
                        productId,
                        quantity
                );


                // =========================
                // SEND EMAIL TO CUSTOMER
                // =========================

                boolean emailSent =
                        EmailUtil.sendOrderConfirmation(
                                customerEmail,
                                customerName,
                                orderId,
                                total,
                                "Cash on Delivery"
                        );


                System.out.println(
                        "COD EMAIL SENT = "
                        + emailSent
                );


                // =========================
                // SUCCESS DATA
                // =========================

                session.setAttribute(
                        "paymentMethod",
                        "Cash on Delivery"
                );


                session.setAttribute(
                        "placedOrderId",
                        orderId
                );


                session.setAttribute(
                        "orderMessage",
                        "Order placed successfully!"
                );


                response.sendRedirect(
                        "orderSuccess.jsp"
                );


                return;


            }catch(Exception e){

                e.printStackTrace();


                session.setAttribute(
                        "orderMessage",
                        "Something went wrong while placing the order."
                );


                response.sendRedirect(
                        "ProductServlet"
                );


                return;
            }
        }


        // =================================================
        // CART ORDER
        // =================================================

        ArrayList<CartItem> cart =
                cartDAO.getCart(
                        customerEmail
                );


        if(cart == null ||
           cart.isEmpty()){

            response.sendRedirect(
                    "CartServlet"
            );

            return;
        }


        // =========================
        // TOTAL
        // =========================

        double total = 0.0;


        for(CartItem item : cart){

            total +=
                    item.getPrice()
                    * item.getQuantity();
        }


        // =========================
        // CREATE ORDER
        // =========================

        int orderId =
                orderDAO.createOrder(
                        customerEmail,
                        total,
                        "Cash on Delivery"
                );


        if(orderId <= 0){

            session.setAttribute(
                    "orderMessage",
                    "Order could not be placed."
            );


            response.sendRedirect(
                    "checkout.jsp"
            );


            return;
        }


        // =========================
        // SAVE ITEMS
        // =========================

        for(CartItem item : cart){

            orderDAO.addOrderItem(
                    orderId,
                    item.getProductId(),
                    item.getQuantity()
            );
        }


        // =========================
        // SEND EMAIL
        // =========================

        boolean emailSent =
                EmailUtil.sendOrderConfirmation(
                        customerEmail,
                        customerName,
                        orderId,
                        total,
                        "Cash on Delivery"
                );


        System.out.println(
                "COD EMAIL SENT TO = "
                + customerEmail
        );


        System.out.println(
                "COD EMAIL RESULT = "
                + emailSent
        );


        // =========================
        // CLEAR CART
        // =========================

        cartDAO.clearCart(
                customerEmail
        );


        // =========================
        // SUCCESS
        // =========================

        session.setAttribute(
                "paymentMethod",
                "Cash on Delivery"
        );


        session.setAttribute(
                "placedOrderId",
                orderId
        );


        session.setAttribute(
                "orderMessage",
                "Order placed successfully!"
        );


        response.sendRedirect(
                "orderSuccess.jsp"
        );
    }



    // =================================================
    // MY ORDERS
    // =================================================

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        if(session == null){

            response.sendRedirect(
                    "login.jsp"
            );

            return;
        }


        User user =
                (User) session.getAttribute(
                        "user"
                );


        if(user == null){

            response.sendRedirect(
                    "login.jsp"
            );

            return;
        }


        if(!"customer".equalsIgnoreCase(
                user.getRole())){

            response.sendRedirect(
                    "home.jsp"
            );

            return;
        }


        String action =
                request.getParameter(
                        "action"
                );


        if("myOrders".equals(action)){

            ArrayList<Order> orders =
                    orderDAO.getUserOrders(
                            user.getEmail()
                    );


            request.setAttribute(
                    "orders",
                    orders
            );


            request.getRequestDispatcher(
                    "myOrders.jsp"
            ).forward(
                    request,
                    response
            );


        }else{

            response.sendRedirect(
                    "ProductServlet"
            );
        }
    }
}