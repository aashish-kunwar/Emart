package servlet;

import java.io.IOException;
import java.util.ArrayList;

import dao.ProductDAO;
import model.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {

    ProductDAO dao = new ProductDAO();

    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // Delete Product
        if(action != null && action.equals("delete")) {

            int id = Integer.parseInt(request.getParameter("id"));

            dao.deleteProduct(id);

            request.getSession().setAttribute(
                    "message",
                    "Product Deleted Successfully");

            response.sendRedirect("ProductServlet");
            return;
        }

        ArrayList<Product> products;

        // Search Product
        if(action != null && action.equals("search")) {

            String keyword = request.getParameter("keyword");

            products = dao.searchProducts(keyword);

        } else {

            products = dao.getAllProducts();

        }

        request.setAttribute("products", products);

        request.getRequestDispatcher("products.jsp")
               .forward(request, response);

    }

    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // Add Product
        if(action != null && action.equals("add")) {

            Product p = new Product();

            p.setName(request.getParameter("name"));

            p.setDescription(request.getParameter("description"));

            p.setPrice(
                    Double.parseDouble(request.getParameter("price"))
            );

            p.setImage(request.getParameter("image"));

            boolean result = dao.addProduct(p);

            if(result) {

                request.getSession().setAttribute(
                        "message",
                        "Product Added Successfully");

            } else {

                request.getSession().setAttribute(
                        "message",
                        "Product Add Failed");

            }

            response.sendRedirect("ProductServlet");

        }

    }

}
