
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

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        UserDAO dao = new UserDAO();

        User user = dao.loginUser(email, password);

        if(user != null && user.getRole().equals(role)){

            HttpSession session = request.getSession();
            session.setAttribute("user", user);

            if(role.equals("admin")){
                response.sendRedirect("home.jsp");
            }
            else{
                response.sendRedirect("ProductServlet");
            }

        }
        else{

            response.getWriter().println("Invalid Login");

        }

    }

}

