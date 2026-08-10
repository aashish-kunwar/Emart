package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import database.DBConnection;
import model.CartItem;
import model.Order;

public class OrderDAO {


    // =====================================================
    // CREATE ORDER
    // =====================================================

    public int createOrder(
            String userEmail,
            double totalAmount,
            String paymentMethod) {

        int orderId = 0;

        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "INSERT INTO orders "
                    + "(user_email, total_amount, payment_method, status) "
                    + "VALUES (?, ?, ?, ?)";


            PreparedStatement ps =
                    con.prepareStatement(
                            sql,
                            Statement.RETURN_GENERATED_KEYS
                    );


            ps.setString(
                    1,
                    userEmail
            );


            ps.setDouble(
                    2,
                    totalAmount
            );


            ps.setString(
                    3,
                    paymentMethod
            );


            ps.setString(
                    4,
                    "Pending"
            );


            int result =
                    ps.executeUpdate();


            if(result > 0) {

                ResultSet rs =
                        ps.getGeneratedKeys();


                if(rs.next()) {

                    orderId =
                            rs.getInt(1);
                }
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return orderId;
    }



    // =====================================================
    // ADD ORDER ITEM
    // =====================================================

    public boolean addOrderItem(
            int orderId,
            int productId,
            int quantity) {

        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "INSERT INTO order_items "
                    + "(order_id, product_id, quantity) "
                    + "VALUES (?, ?, ?)";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setInt(
                    1,
                    orderId
            );


            ps.setInt(
                    2,
                    productId
            );


            ps.setInt(
                    3,
                    quantity
            );


            return ps.executeUpdate() > 0;


        } catch(Exception e) {

            e.printStackTrace();
        }


        return false;
    }



    // =====================================================
    // GET USER ORDERS
    // =====================================================

    public ArrayList<Order> getUserOrders(
            String email) {

        ArrayList<Order> orders =
                new ArrayList<>();


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT * FROM orders "
                    + "WHERE user_email=? "
                    + "ORDER BY id DESC";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(
                    1,
                    email
            );


            ResultSet rs =
                    ps.executeQuery();


            while(rs.next()) {

                Order order =
                        createOrderObject(
                                rs
                        );


                orders.add(
                        order
                );
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return orders;
    }



    // =====================================================
    // GET ALL ORDERS
    // =====================================================

    public ArrayList<Order> getAllOrders() {

        ArrayList<Order> orders =
                new ArrayList<>();


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT * FROM orders "
                    + "ORDER BY id DESC";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ResultSet rs =
                    ps.executeQuery();


            while(rs.next()) {

                Order order =
                        createOrderObject(
                                rs
                        );


                orders.add(
                        order
                );
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return orders;
    }



    // =====================================================
    // GET ORDER ITEMS
    // =====================================================

    public ArrayList<CartItem> getOrderItems(
            int orderId) {

        ArrayList<CartItem> items =
                new ArrayList<>();


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT "
                    + "oi.product_id, "
                    + "oi.quantity, "
                    + "p.name AS product_name, "
                    + "p.price AS product_price, "
                    + "p.image AS product_image "
                    + "FROM order_items oi "
                    + "LEFT JOIN products p "
                    + "ON oi.product_id = p.id "
                    + "WHERE oi.order_id=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setInt(
                    1,
                    orderId
            );


            ResultSet rs =
                    ps.executeQuery();


            while(rs.next()) {

                CartItem item =
                        new CartItem();


                item.setProductId(
                        rs.getInt(
                                "product_id"
                        )
                );


                item.setQuantity(
                        rs.getInt(
                                "quantity"
                        )
                );


                String productName =
                        rs.getString(
                                "product_name"
                        );


                if(productName == null ||
                   productName.trim().isEmpty()) {

                    productName =
                            "Product no longer available";
                }


                item.setProductName(
                        productName
                );


                item.setPrice(
                        rs.getDouble(
                                "product_price"
                        )
                );


                item.setImage(
                        rs.getString(
                                "product_image"
                        )
                );


                items.add(
                        item
                );
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return items;
    }



    // =====================================================
    // UPDATE ORDER STATUS
    // =====================================================

    public boolean updateOrderStatus(
            int orderId,
            String status) {

        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "UPDATE orders "
                    + "SET status=? "
                    + "WHERE id=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(
                    1,
                    status
            );


            ps.setInt(
                    2,
                    orderId
            );


            return ps.executeUpdate() > 0;


        } catch(Exception e) {

            e.printStackTrace();
        }


        return false;
    }



    // =====================================================
    // GET SINGLE ORDER
    // =====================================================

    public Order getOrderById(
            int orderId) {

        Order order = null;


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT * FROM orders "
                    + "WHERE id=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setInt(
                    1,
                    orderId
            );


            ResultSet rs =
                    ps.executeQuery();


            if(rs.next()) {

                order =
                        createOrderObject(
                                rs
                        );
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return order;
    }



    // =====================================================
    // CREATE ORDER OBJECT
    // =====================================================

    private Order createOrderObject(
            ResultSet rs)
            throws Exception {

        Order order =
                new Order();


        order.setId(
                rs.getInt(
                        "id"
                )
        );


        order.setUserEmail(
                rs.getString(
                        "user_email"
                )
        );


        order.setTotalAmount(
                rs.getDouble(
                        "total_amount"
                )
        );


        order.setPaymentMethod(
                rs.getString(
                        "payment_method"
                )
        );


        order.setStatus(
                rs.getString(
                        "status"
                )
        );


        return order;
    }

}