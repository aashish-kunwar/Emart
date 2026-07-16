
package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.DBConnection;
import model.User;

public class UserDAO {

    Connection con;

    // Register Customer
    public boolean registerUser(User user) {

        try {

            con = DBConnection.getConnection();

            String sql = "INSERT INTO users(name,email,password,role) VALUES(?,?,?,?)";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());

            int i = ps.executeUpdate();

            if (i > 0)
                return true;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // Login (Customer or Admin)
    public User loginUser(String email, String password) {

        User user = null;

        try {

            con = DBConnection.getConnection();

            String sql = "SELECT * FROM users WHERE email=? AND password=?";

            PreparedStatement ps = con.prepareStatement(sql);

            ps.setString(1, email);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                user = new User();

                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

}

