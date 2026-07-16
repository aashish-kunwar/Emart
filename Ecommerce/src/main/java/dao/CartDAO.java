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


	        // Check if product already exists in cart
	        String check =
	        "SELECT * FROM cart WHERE user_email=? AND product_id=?";


	        PreparedStatement ps1 = con.prepareStatement(check);

	        ps1.setString(1, email);
	        ps1.setInt(2, productId);


	        ResultSet rs = ps1.executeQuery();



	        if(rs.next()) {

	            // Increase quantity
	            String update =
	            "UPDATE cart SET quantity = quantity + 1 WHERE user_email=? AND product_id=?";


	            PreparedStatement ps2 = con.prepareStatement(update);

	            ps2.setString(1, email);
	            ps2.setInt(2, productId);


	            return ps2.executeUpdate() > 0;


	        } else {


	            // Add new product
	            String insert =
	            "INSERT INTO cart(user_email,product_id,quantity) VALUES(?,?,?)";


	            PreparedStatement ps3 = con.prepareStatement(insert);


	            ps3.setString(1, email);
	            ps3.setInt(2, productId);
	            ps3.setInt(3, 1);


	            return ps3.executeUpdate() > 0;

	        }


	    } catch(Exception e) {

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
            "SELECT cart.id, cart.product_id, products.name, products.price, products.image, cart.quantity "
            +
            "FROM cart INNER JOIN products "
            +
            "ON cart.product_id = products.id "
            +
            "WHERE cart.user_email=?";


            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);


            ResultSet rs = ps.executeQuery();


            while(rs.next()) {


                CartItem item = new CartItem();


                item.setId(rs.getInt("id"));

                item.setProductId(rs.getInt("product_id"));

                item.setProductName(rs.getString("name"));

                item.setPrice(rs.getDouble("price"));

                item.setImage(rs.getString("image"));

                item.setQuantity(rs.getInt("quantity"));


                list.add(item);

            }


        } catch(Exception e) {

            e.printStackTrace();

        }


        return list;

    }




    // Remove item
    public boolean removeItem(int id) {


        try {

            Connection con = DBConnection.getConnection();


            PreparedStatement ps =
            con.prepareStatement("DELETE FROM cart WHERE id=?");


            ps.setInt(1,id);


            int result = ps.executeUpdate();


            return result > 0;


        } catch(Exception e) {

            e.printStackTrace();

        }


        return false;

    }


}