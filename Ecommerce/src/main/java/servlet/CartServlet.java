package servlet;

import java.io.IOException;
import java.util.ArrayList;

import dao.CartDAO;
import model.CartItem;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {


    CartDAO dao = new CartDAO();



    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {


        HttpSession session = request.getSession();


        User user = (User) session.getAttribute("user");


        if(user == null){

            response.sendRedirect("login.jsp");
            return;

        }



        String action = request.getParameter("action");



        // Add product to cart

        if(action != null && action.equals("add")){


            int productId =
            Integer.parseInt(request.getParameter("id"));


            dao.addToCart(user.getEmail(), productId);


            response.sendRedirect("ProductServlet");


        }



        // Remove product

        else if(action != null && action.equals("remove")){


            int id =
            Integer.parseInt(request.getParameter("id"));


            dao.removeItem(id);


            response.sendRedirect("CartServlet");

        }



        // View cart

        else{


            ArrayList<CartItem> cart =
            dao.getCart(user.getEmail());


            request.setAttribute("cart", cart);


            request.getRequestDispatcher("cart.jsp")
                   .forward(request, response);

        }


    }


}