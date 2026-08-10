package servlet;

import java.io.IOException;
import java.util.ArrayList;

import dao.ProductDAO;
import model.Product;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ProductDAO dao =
            new ProductDAO();


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String action =
                request.getParameter("action");


        // =========================
        // EDIT PRODUCT
        // =========================

        if("edit".equals(action)) {

            HttpSession session =
                    request.getSession(false);

            User user =
                    session == null
                    ? null
                    : (User) session.getAttribute("user");


            if(user == null ||
               !"admin".equalsIgnoreCase(user.getRole())) {

                response.sendRedirect("login.jsp");
                return;
            }


            try {

                int id =
                        Integer.parseInt(
                                request.getParameter("id")
                        );


                Product product =
                        dao.getProductById(id);


                if(product == null) {

                    response.sendRedirect(
                            "ProductServlet"
                    );

                    return;
                }


                request.setAttribute(
                        "product",
                        product
                );


                request.getRequestDispatcher(
                        "editProduct.jsp"
                ).forward(
                        request,
                        response
                );


                return;


            } catch(Exception e) {

                e.printStackTrace();

                response.sendRedirect(
                        "ProductServlet"
                );

                return;
            }
        }


        // =========================
        // DELETE PRODUCT
        // =========================

        if("delete".equals(action)) {

            HttpSession session =
                    request.getSession(false);

            User user =
                    session == null
                    ? null
                    : (User) session.getAttribute("user");


            if(user == null ||
               !"admin".equalsIgnoreCase(user.getRole())) {

                response.sendRedirect("login.jsp");
                return;
            }


            try {

                int id =
                        Integer.parseInt(
                                request.getParameter("id")
                        );


                boolean deleted =
                        dao.deleteProduct(id);


                if(deleted) {

                    session.setAttribute(
                            "message",
                            "Product deleted successfully!"
                    );

                } else {

                    session.setAttribute(
                            "message",
                            "Product could not be deleted."
                    );
                }


            } catch(Exception e) {

                e.printStackTrace();
            }


            response.sendRedirect(
                    "ProductServlet"
            );

            return;
        }


        ArrayList<Product> products;


        // =========================
        // SEARCH
        // =========================

        if("search".equals(action)) {

            String keyword =
                    request.getParameter("keyword");


            if(keyword == null) {
                keyword = "";
            }


            products =
                    dao.searchProducts(
                            keyword.trim()
                    );
        }


        // =========================
        // CATEGORY
        // =========================

        else if("category".equals(action)) {

            String category =
                    request.getParameter("category");


            if(category == null ||
               category.trim().isEmpty()) {

                products =
                        dao.getAllProducts();

            } else {

                products =
                        dao.getProductsByCategory(
                                category
                        );
            }
        }


        // =========================
        // ALL PRODUCTS
        // =========================

        else {

            products =
                    dao.getAllProducts();
        }


        request.setAttribute(
                "products",
                products
        );


        request.getRequestDispatcher(
                "products.jsp"
        ).forward(
                request,
                response
        );
    }


    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        User user =
                session == null
                ? null
                : (User) session.getAttribute("user");


        if(user == null ||
           !"admin".equalsIgnoreCase(user.getRole())) {

            response.sendRedirect("login.jsp");
            return;
        }


        String action =
                request.getParameter("action");


        // =========================
        // UPDATE PRODUCT
        // =========================

        if("update".equals(action)) {

            try {

                int id =
                        Integer.parseInt(
                                request.getParameter("id")
                        );


                String name =
                        request.getParameter("name");


                String description =
                        request.getParameter("description");


                double price =
                        Double.parseDouble(
                                request.getParameter("price")
                        );


                String image =
                        request.getParameter("image");


                String category =
                        request.getParameter("category");


                Product product =
                        new Product();


                product.setId(id);
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setImage(image);
                product.setCategory(category);


                boolean updated =
                        dao.updateProduct(product);


                if(updated) {

                    session.setAttribute(
                            "message",
                            "Product updated successfully!"
                    );

                } else {

                    session.setAttribute(
                            "message",
                            "Product could not be updated."
                    );
                }


                response.sendRedirect(
                        "ProductServlet"
                );

                return;


            } catch(Exception e) {

                e.printStackTrace();

                session.setAttribute(
                        "message",
                        "Something went wrong while updating the product."
                );


                response.sendRedirect(
                        "ProductServlet"
                );

                return;
            }
        }


        // =========================
        // ADD PRODUCT
        // =========================

        try {

            String name =
                    request.getParameter("name");


            String description =
                    request.getParameter("description");


            double price =
                    Double.parseDouble(
                            request.getParameter("price")
                    );


            String image =
                    request.getParameter("image");


            String category =
                    request.getParameter("category");


            Product product =
                    new Product();


            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setImage(image);
            product.setCategory(category);


            boolean added =
                    dao.addProduct(product);


            if(added) {

                session.setAttribute(
                        "message",
                        "Product added successfully!"
                );

                response.sendRedirect(
                        "ProductServlet"
                );

            } else {

                response.sendRedirect(
                        "addProduct.jsp"
                );
            }


        } catch(Exception e) {

            e.printStackTrace();

            response.sendRedirect(
                    "addProduct.jsp"
            );
        }
    }
}