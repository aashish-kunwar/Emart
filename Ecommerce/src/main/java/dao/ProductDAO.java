package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import database.DBConnection;
import model.Product;

public class ProductDAO {


    // Add Product
    public boolean addProduct(Product p) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
            "INSERT INTO products(name,description,price,image) VALUES(?,?,?,?)";

            PreparedStatement ps =
            con.prepareStatement(sql);

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getImage());

            int result = ps.executeUpdate();

            return result > 0;

        } catch(Exception e) {

            e.printStackTrace();

        }

        return false;
    }



    // Get All Products
    public ArrayList<Product> getAllProducts() {

        ArrayList<Product> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM products";

            PreparedStatement ps =
            con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                Product p = new Product();

                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setImage(rs.getString("image"));

                list.add(p);

            }

        } catch(Exception e) {

            e.printStackTrace();

        }

        return list;
    }



    // Search Products
    public ArrayList<Product> searchProducts(String keyword) {

        ArrayList<Product> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql =
            "SELECT * FROM products WHERE name LIKE ?";

            PreparedStatement ps =
            con.prepareStatement(sql);

            ps.setString(1, "%" + keyword + "%");

            ResultSet rs = ps.executeQuery();

            while(rs.next()) {

                Product p = new Product();

                p.setId(rs.getInt("id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setImage(rs.getString("image"));

                list.add(p);

            }

        } catch(Exception e) {

            e.printStackTrace();

        }

        return list;

    }



    // Delete Product
    public boolean deleteProduct(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql =
            "DELETE FROM products WHERE id=?";

            PreparedStatement ps =
            con.prepareStatement(sql);

            ps.setInt(1, id);

            int result = ps.executeUpdate();

            return result > 0;

        } catch(Exception e) {

            e.printStackTrace();

        }

        return false;

    }

}