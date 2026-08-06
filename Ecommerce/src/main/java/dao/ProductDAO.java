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
                    "INSERT INTO products(name,description,price,image,category) "
                    + "VALUES(?,?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, p.getName());
            ps.setString(2, p.getDescription());
            ps.setDouble(3, p.getPrice());
            ps.setString(4, p.getImage());
            ps.setString(5, p.getCategory());

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // Get All Products
    public ArrayList<Product> getAllProducts() {

        ArrayList<Product> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql = "SELECT * FROM products ORDER BY id DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product p = createProduct(rs);

                list.add(p);
            }

        } catch (Exception e) {

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
                    "SELECT * FROM products "
                    + "WHERE name LIKE ? OR description LIKE ? "
                    + "ORDER BY id DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            String searchKeyword = "%" + keyword + "%";

            ps.setString(1, searchKeyword);
            ps.setString(2, searchKeyword);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product p = createProduct(rs);

                list.add(p);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // Filter Products by Category
    public ArrayList<Product> getProductsByCategory(String category) {

        ArrayList<Product> list = new ArrayList<>();

        try {

            Connection con = DBConnection.getConnection();

            String sql =
                    "SELECT * FROM products "
                    + "WHERE category=? "
                    + "ORDER BY id DESC";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, category);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Product p = createProduct(rs);

                list.add(p);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return list;
    }

    // Delete Product
    public boolean deleteProduct(int id) {

        try {

            Connection con = DBConnection.getConnection();

            String sql = "DELETE FROM products WHERE id=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }

    // Common method for creating Product object
    private Product createProduct(ResultSet rs) throws Exception {

        Product p = new Product();

        p.setId(rs.getInt("id"));
        p.setName(rs.getString("name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getDouble("price"));
        p.setImage(rs.getString("image"));
        p.setCategory(rs.getString("category"));

        return p;
    }
}