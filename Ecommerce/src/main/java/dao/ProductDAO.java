package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import database.DBConnection;
import model.Product;

public class ProductDAO {


    // =========================
    // ADD PRODUCT
    // =========================

    public boolean addProduct(Product product) {

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "INSERT INTO products "
                    + "(name, description, price, image, category) "
                    + "VALUES (?, ?, ?, ?, ?)";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ps.setString(1, product.getName());
            ps.setString(2, product.getDescription());
            ps.setDouble(3, product.getPrice());
            ps.setString(4, product.getImage());
            ps.setString(5, product.getCategory());

            int result =
                    ps.executeUpdate();

            return result > 0;

        } catch(Exception e) {

            e.printStackTrace();
        }

        return false;
    }



    // =========================
    // GET ALL PRODUCTS
    // =========================

    public ArrayList<Product> getAllProducts() {

        ArrayList<Product> products =
                new ArrayList<>();

        try {

            Connection con =
                    DBConnection.getConnection();

            String sql =
                    "SELECT * FROM products "
                    + "ORDER BY id DESC";

            PreparedStatement ps =
                    con.prepareStatement(sql);

            ResultSet rs =
                    ps.executeQuery();


            while(rs.next()) {

                Product product =
                        new Product();

                product.setId(
                        rs.getInt("id")
                );

                product.setName(
                        rs.getString("name")
                );

                product.setDescription(
                        rs.getString("description")
                );

                product.setPrice(
                        rs.getDouble("price")
                );

                product.setImage(
                        rs.getString("image")
                );

                product.setCategory(
                        rs.getString("category")
                );


                products.add(product);
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return products;
    }



    // =========================
    // GET PRODUCT BY ID
    // =========================

    public Product getProductById(int id) {

        Product product = null;


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT * FROM products "
                    + "WHERE id=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setInt(1, id);


            ResultSet rs =
                    ps.executeQuery();


            if(rs.next()) {

                product =
                        new Product();


                product.setId(
                        rs.getInt("id")
                );


                product.setName(
                        rs.getString("name")
                );


                product.setDescription(
                        rs.getString("description")
                );


                product.setPrice(
                        rs.getDouble("price")
                );


                product.setImage(
                        rs.getString("image")
                );


                product.setCategory(
                        rs.getString("category")
                );
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return product;
    }



    // =========================
    // UPDATE PRODUCT
    // =========================

    public boolean updateProduct(Product product) {

        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "UPDATE products "
                    + "SET name=?, "
                    + "description=?, "
                    + "price=?, "
                    + "image=?, "
                    + "category=? "
                    + "WHERE id=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(
                    1,
                    product.getName()
            );


            ps.setString(
                    2,
                    product.getDescription()
            );


            ps.setDouble(
                    3,
                    product.getPrice()
            );


            ps.setString(
                    4,
                    product.getImage()
            );


            ps.setString(
                    5,
                    product.getCategory()
            );


            ps.setInt(
                    6,
                    product.getId()
            );


            int result =
                    ps.executeUpdate();


            return result > 0;


        } catch(Exception e) {

            e.printStackTrace();
        }


        return false;
    }



    // =========================
    // DELETE PRODUCT
    // =========================

    public boolean deleteProduct(int id) {

        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "DELETE FROM products "
                    + "WHERE id=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setInt(1, id);


            int result =
                    ps.executeUpdate();


            return result > 0;


        } catch(Exception e) {

            e.printStackTrace();
        }


        return false;
    }



    // =========================
    // SEARCH PRODUCTS
    // =========================

    public ArrayList<Product> searchProducts(
            String keyword) {

        ArrayList<Product> products =
                new ArrayList<>();


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT * FROM products "
                    + "WHERE name LIKE ? "
                    + "OR description LIKE ? "
                    + "OR category LIKE ? "
                    + "ORDER BY id DESC";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            String searchValue =
                    "%" + keyword + "%";


            ps.setString(
                    1,
                    searchValue
            );


            ps.setString(
                    2,
                    searchValue
            );


            ps.setString(
                    3,
                    searchValue
            );


            ResultSet rs =
                    ps.executeQuery();


            while(rs.next()) {

                Product product =
                        new Product();


                product.setId(
                        rs.getInt("id")
                );


                product.setName(
                        rs.getString("name")
                );


                product.setDescription(
                        rs.getString("description")
                );


                product.setPrice(
                        rs.getDouble("price")
                );


                product.setImage(
                        rs.getString("image")
                );


                product.setCategory(
                        rs.getString("category")
                );


                products.add(product);
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return products;
    }



    // =========================
    // GET PRODUCTS BY CATEGORY
    // =========================

    public ArrayList<Product> getProductsByCategory(
            String category) {

        ArrayList<Product> products =
                new ArrayList<>();


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT * FROM products "
                    + "WHERE category=? "
                    + "ORDER BY id DESC";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(
                    1,
                    category
            );


            ResultSet rs =
                    ps.executeQuery();


            while(rs.next()) {

                Product product =
                        new Product();


                product.setId(
                        rs.getInt("id")
                );


                product.setName(
                        rs.getString("name")
                );


                product.setDescription(
                        rs.getString("description")
                );


                product.setPrice(
                        rs.getDouble("price")
                );


                product.setImage(
                        rs.getString("image")
                );


                product.setCategory(
                        rs.getString("category")
                );


                products.add(product);
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return products;
    }



    // =========================
    // TOTAL PRODUCTS
    // =========================

    public int getTotalProducts() {

        int total = 0;


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT COUNT(*) FROM products";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ResultSet rs =
                    ps.executeQuery();


            if(rs.next()) {

                total =
                        rs.getInt(1);
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return total;
    }

}