package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import database.DBConnection;
import model.Order;

public class OrderDAO {


    // Create order
    public int createOrder(String email, double total) {

        int orderId = 0;

        try {

            Connection con = DBConnection.getConnection();

            String sql =
            "INSERT INTO orders(user_email,total_amount,status) VALUES(?,?,?)";


            PreparedStatement ps =
            con.prepareStatement(sql,
            PreparedStatement.RETURN_GENERATED_KEYS);


            ps.setString(1, email);
            ps.setDouble(2, total);
            ps.setString(3, "Pending");


            ps.executeUpdate();


            ResultSet rs = ps.getGeneratedKeys();


            if(rs.next()) {

                orderId = rs.getInt(1);

            }


        } catch(Exception e) {

            e.printStackTrace();

        }


        return orderId;

    }




    // Save order items
    public void addOrderItem(int orderId, int productId, int quantity) {


        try {

            Connection con = DBConnection.getConnection();


            String sql =
            "INSERT INTO order_items(order_id,product_id,quantity) VALUES(?,?,?)";


            PreparedStatement ps =
            con.prepareStatement(sql);


            ps.setInt(1, orderId);
            ps.setInt(2, productId);
            ps.setInt(3, quantity);


            ps.executeUpdate();


        } catch(Exception e) {

            e.printStackTrace();

        }

    }





    // Customer view orders
    public ArrayList<Order> getUserOrders(String email) {


        ArrayList<Order> list = new ArrayList<>();


        try {

            Connection con = DBConnection.getConnection();


            String sql =
            "SELECT * FROM orders WHERE user_email=?";


            PreparedStatement ps =
            con.prepareStatement(sql);


            ps.setString(1,email);


            ResultSet rs = ps.executeQuery();


            while(rs.next()) {


                Order order = new Order();


                order.setId(rs.getInt("id"));

                order.setUserEmail(rs.getString("user_email"));

                order.setTotalAmount(rs.getDouble("total_amount"));

                order.setOrderDate(rs.getString("order_date"));

                order.setStatus(rs.getString("status"));


                list.add(order);

            }


        } catch(Exception e) {

            e.printStackTrace();

        }


        return list;

    }




    // Admin view all orders
    public ArrayList<Order> getAllOrders() {


        ArrayList<Order> list = new ArrayList<>();


        try {


            Connection con = DBConnection.getConnection();


            PreparedStatement ps =
            con.prepareStatement("SELECT * FROM orders");


            ResultSet rs = ps.executeQuery();



            while(rs.next()) {


                Order order = new Order();


                order.setId(rs.getInt("id"));

                order.setUserEmail(rs.getString("user_email"));

                order.setTotalAmount(rs.getDouble("total_amount"));

                order.setOrderDate(rs.getString("order_date"));

                order.setStatus(rs.getString("status"));


                list.add(order);

            }


        } catch(Exception e) {

            e.printStackTrace();

        }


        return list;

    }





    // Update order status
    public void updateStatus(int id, String status) {


        try {


            Connection con = DBConnection.getConnection();


            PreparedStatement ps =
            con.prepareStatement(
            "UPDATE orders SET status=? WHERE id=?");


            ps.setString(1,status);

            ps.setInt(2,id);


            ps.executeUpdate();



        } catch(Exception e) {

            e.printStackTrace();

        }

    }


}