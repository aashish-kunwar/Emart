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

    private static final long serialVersionUID = 1L;

    private final CartDAO dao = new CartDAO();


    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session =
                request.getSession(false);


        // =========================
        // LOGIN CHECK
        // =========================

        if(session == null){

            response.sendRedirect("login.jsp");
            return;
        }


        User user =
                (User) session.getAttribute("user");


        if(user == null){

            response.sendRedirect("login.jsp");
            return;
        }


        // Only customer can use cart

        if(!"customer".equalsIgnoreCase(
                user.getRole())){

            response.sendRedirect(
                    "adminDashboard.jsp"
            );

            return;
        }


        String action =
                request.getParameter("action");


        // =========================
        // ADD TO CART
        // =========================

        if("add".equals(action)){

            try{

                int productId =
                        Integer.parseInt(
                                request.getParameter("id")
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


                boolean added =
                        dao.addToCart(
                                user.getEmail(),
                                productId,
                                quantity
                        );


                if(added){

                    /*
                     * This will be shown as
                     * CSS popup in products.jsp
                     */

                    session.setAttribute(
                            "cartPopup",
                            "Product added to cart successfully!"
                    );

                }else{

                    session.setAttribute(
                            "cartErrorPopup",
                            "Product could not be added to cart."
                    );
                }


            }catch(Exception e){

                e.printStackTrace();

                session.setAttribute(
                        "cartErrorPopup",
                        "Something went wrong while adding the product."
                );
            }


            // Stay on products page

            response.sendRedirect(
                    "ProductServlet"
            );

            return;
        }


        // =========================
        // INCREASE QUANTITY
        // =========================

        if("increase".equals(action)){

            try{

                int cartId =
                        Integer.parseInt(
                                request.getParameter("id")
                        );


                dao.increaseQuantity(
                        cartId
                );


            }catch(Exception e){

                e.printStackTrace();
            }


            response.sendRedirect(
                    "CartServlet"
            );

            return;
        }


        // =========================
        // DECREASE QUANTITY
        // =========================

        if("decrease".equals(action)){

            try{

                int cartId =
                        Integer.parseInt(
                                request.getParameter("id")
                        );


                dao.decreaseQuantity(
                        cartId
                );


            }catch(Exception e){

                e.printStackTrace();
            }


            response.sendRedirect(
                    "CartServlet"
            );

            return;
        }


        // =========================
        // REMOVE PRODUCT
        // =========================

        if("remove".equals(action)){

            try{

                int cartId =
                        Integer.parseInt(
                                request.getParameter("id")
                        );


                dao.removeItem(
                        cartId
                );


            }catch(Exception e){

                e.printStackTrace();
            }


            response.sendRedirect(
                    "CartServlet"
            );

            return;
        }


        // =========================
        // VIEW CART
        // =========================

        ArrayList<CartItem> cart =
                dao.getCart(
                        user.getEmail()
                );


        request.setAttribute(
                "cart",
                cart
        );


        request.getRequestDispatcher(
                "cart.jsp"
        ).forward(
                request,
                response
        );
    }
}