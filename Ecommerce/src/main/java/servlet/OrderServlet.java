package servlet;

import java.io.IOException;
import java.util.ArrayList;

import dao.OrderDAO;
import dao.CartDAO;

import model.User;
import model.CartItem;
import model.Order;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {


    OrderDAO orderDAO = new OrderDAO();

    CartDAO cartDAO = new CartDAO();



    // Place Order
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession();


        User user = (User) session.getAttribute("user");


        if(user == null){

            response.sendRedirect("login.jsp");
            return;

        }



        ArrayList<CartItem> cart =
        cartDAO.getCart(user.getEmail());



        double total = 0;


        for(CartItem item : cart){

            total += item.getPrice() * item.getQuantity();

        }



        int orderId =
        orderDAO.createOrder(user.getEmail(), total);



        for(CartItem item : cart){


            orderDAO.addOrderItem(
                    orderId,
                    item.getProductId(),
                    item.getQuantity()
            );

        }



        response.sendRedirect("orderSuccess.jsp");


    }





    // View My Orders
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {



        HttpSession session = request.getSession();


        User user = (User) session.getAttribute("user");



        if(user == null){

            response.sendRedirect("login.jsp");
            return;

        }



        String action = request.getParameter("action");



        if(action != null && action.equals("myOrders")){


            ArrayList<Order> orders =
            orderDAO.getUserOrders(user.getEmail());



            request.setAttribute("orders", orders);



            request.getRequestDispatcher("myOrders.jsp")
                   .forward(request, response);


        }

        else {

            response.sendRedirect("ProductServlet");

        }



    }



}