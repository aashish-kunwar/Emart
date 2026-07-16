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


    OrderDAO dao = new OrderDAO();



    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession();


        User user = (User) session.getAttribute("user");


        // Check admin
        if(user == null || !user.getRole().equals("admin")) {

            response.sendRedirect("login.jsp");
            return;

        }



        String action = request.getParameter("action");



        if(action != null && action.equals("update")) {


            int id =
            Integer.parseInt(request.getParameter("id"));


            String status =
            request.getParameter("status");


            dao.updateStatus(id, status);


            response.sendRedirect("AdminOrderServlet");


        }

        else {


            ArrayList<Order> orders =
            dao.getAllOrders();


            request.setAttribute("orders", orders);


            request.getRequestDispatcher("manageOrders.jsp")
                   .forward(request, response);

        }


    }

}