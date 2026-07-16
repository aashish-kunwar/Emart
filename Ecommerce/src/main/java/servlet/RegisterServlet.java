
package servlet;

import java.io.IOException;

import dao.UserDAO;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        User user = new User();

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole("customer");

        UserDAO dao = new UserDAO();

        if (dao.registerUser(user)) {

            response.sendRedirect("login.jsp");

        } else {

            response.getWriter().println("Registration Failed!");

        }

    }

}

