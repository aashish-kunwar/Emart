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

    private static final long serialVersionUID = 1L;

    private final ProductDAO dao = new ProductDAO();

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        // Delete product
        if ("delete".equals(action)) {

            String idValue = request.getParameter("id");

            try {
                int id = Integer.parseInt(idValue);

                boolean deleted = dao.deleteProduct(id);

                if (deleted) {
                    request.getSession().setAttribute(
                            "message",
                            "Product deleted successfully"
                    );
                } else {
                    request.getSession().setAttribute(
                            "message",
                            "Product could not be deleted"
                    );
                }

            } catch (Exception e) {

                request.getSession().setAttribute(
                        "message",
                        "Invalid product ID"
                );
            }

            response.sendRedirect("ProductServlet");
            return;
        }

        ArrayList<Product> products;

        // Search products
        if ("search".equals(action)) {

            String keyword = request.getParameter("keyword");

            if (keyword == null || keyword.trim().isEmpty()) {
                products = dao.getAllProducts();
            } else {
                products = dao.searchProducts(keyword.trim());
            }

        }

        // Filter products by category
        else if ("category".equals(action)) {

            String category = request.getParameter("category");

            if (category == null
                    || category.trim().isEmpty()
                    || "All".equals(category)) {

                products = dao.getAllProducts();

            } else {

                products = dao.getProductsByCategory(
                        category.trim()
                );
            }

        }

        // Show all products
        else {

            products = dao.getAllProducts();
        }

        request.setAttribute("products", products);

        request.getRequestDispatcher("products.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (!"add".equals(action)) {
            response.sendRedirect("ProductServlet");
            return;
        }

        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceValue = request.getParameter("price");
        String image = request.getParameter("image");
        String category = request.getParameter("category");

        try {

            Product product = new Product();

            product.setName(name);
            product.setDescription(description);
            product.setPrice(Double.parseDouble(priceValue));
            product.setImage(image);
            product.setCategory(category);

            boolean added = dao.addProduct(product);

            if (added) {

                request.getSession().setAttribute(
                        "message",
                        "Product added successfully"
                );

            } else {

                request.getSession().setAttribute(
                        "message",
                        "Product could not be added"
                );
            }

        } catch (Exception e) {

            e.printStackTrace();

            request.getSession().setAttribute(
                    "message",
                    "Invalid product information"
            );
        }

        response.sendRedirect("ProductServlet");
    }
}
