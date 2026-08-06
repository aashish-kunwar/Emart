
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import database.DBConnection;
import model.CartItem;

public class CartDAO {


    // Add product to cart
    public boolean addToCart(String email, int productId) {

        try {

            Connection con = DBConnection.getConnection();


            // Check whether product already exists
            String checkSql =
                    "SELECT id FROM cart "
                    + "WHERE user_email=? AND product_id=?";


            PreparedStatement checkPs =
                    con.prepareStatement(checkSql);


            checkPs.setString(1, email);
            checkPs.setInt(2, productId);


            ResultSet rs = checkPs.executeQuery();


            if (rs.next()) {

                // Increase quantity
                String updateSql =
                        "UPDATE cart "
                        + "SET quantity=quantity+1 "
                        + "WHERE user_email=? AND product_id=?";


                PreparedStatement updatePs =
                        con.prepareStatement(updateSql);


                updatePs.setString(1, email);
                updatePs.setInt(2, productId);


                return updatePs.executeUpdate() > 0;

            } else {

                // Insert new cart item
                String insertSql =
                        "INSERT INTO cart"
                        + "(user_email,product_id,quantity) "
                        + "VALUES(?,?,?)";


                PreparedStatement insertPs =
                        con.prepareStatement(insertSql);


                insertPs.setString(1, email);
                insertPs.setInt(2, productId);
                insertPs.setInt(3, 1);


                return insertPs.executeUpdate() > 0;
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


        return false;
    }



    // Get user's cart
    public ArrayList<CartItem> getCart(String email) {

        ArrayList<CartItem> list = new ArrayList<>();


        try {

            Connection con = DBConnection.getConnection();


            String sql =
                    "SELECT cart.id, cart.product_id, "
                    + "products.name, products.price, "
                    + "products.image, cart.quantity "
                    + "FROM cart "
                    + "INNER JOIN products "
                    + "ON cart.product_id=products.id "
                    + "WHERE cart.user_email=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(1, email);


            ResultSet rs = ps.executeQuery();


            while (rs.next()) {

                CartItem item = new CartItem();


                item.setId(rs.getInt("id"));

                item.setProductId(
                        rs.getInt("product_id")
                );

                item.setProductName(
                        rs.getString("name")
                );

                item.setPrice(
                        rs.getDouble("price")
                );

                item.setImage(
                        rs.getString("image")
                );

                item.setQuantity(
                        rs.getInt("quantity")
                );


                list.add(item);
            }


        } catch (Exception e) {

            e.printStackTrace();
        }


        return list;
    }



    // Remove one cart item
    public boolean removeItem(int id) {

        try {

            Connection con = DBConnection.getConnection();


            String sql =
                    "DELETE FROM cart WHERE id=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setInt(1, id);


            return ps.executeUpdate() > 0;


        } catch (Exception e) {

            e.printStackTrace();
        }


        return false;
    }



    // Clear all cart items after successful order
    public boolean clearCart(String email) {

        try {

            Connection con = DBConnection.getConnection();


            String sql =
                    "DELETE FROM cart WHERE user_email=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(1, email);


            ps.executeUpdate();

            return true;


        } catch (Exception e) {

            e.printStackTrace();
        }


        return false;
    }

}

