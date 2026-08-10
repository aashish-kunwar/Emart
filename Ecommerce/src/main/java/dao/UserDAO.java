package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import database.DBConnection;
import model.User;

public class UserDAO {


    // =========================
    // REGISTER USER
    // =========================

    public boolean registerUser(User user) {

        try {

            Connection con =
                    DBConnection.getConnection();


            String checkSql =
                    "SELECT id FROM users WHERE email=?";


            PreparedStatement checkPs =
                    con.prepareStatement(checkSql);


            checkPs.setString(
                    1,
                    user.getEmail()
            );


            ResultSet checkRs =
                    checkPs.executeQuery();


            if(checkRs.next()) {

                return false;
            }


            String sql =
                    "INSERT INTO users "
                    + "(name,email,password,role) "
                    + "VALUES(?,?,?,?)";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(
                    1,
                    user.getName()
            );


            ps.setString(
                    2,
                    user.getEmail()
            );


            ps.setString(
                    3,
                    user.getPassword()
            );


            ps.setString(
                    4,
                    user.getRole()
            );


            return ps.executeUpdate() > 0;


        } catch(Exception e) {

            e.printStackTrace();
        }


        return false;
    }



    // =========================
    // LOGIN USER
    // =========================

    public User loginUser(
            String email,
            String password) {

        User user = null;


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT id,name,email,password,role "
                    + "FROM users "
                    + "WHERE email=? AND password=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(
                    1,
                    email.trim()
            );


            ps.setString(
                    2,
                    password
            );


            ResultSet rs =
                    ps.executeQuery();


            if(rs.next()) {

                user =
                        new User();


                user.setId(
                        rs.getInt("id")
                );


                user.setName(
                        rs.getString("name")
                );


                user.setEmail(
                        rs.getString("email")
                );


                user.setPassword(
                        rs.getString("password")
                );


                user.setRole(
                        rs.getString("role")
                );
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return user;
    }



    // =========================
    // CHECK EMAIL EXISTS
    // =========================

    public boolean emailExists(
            String email) {

        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT id "
                    + "FROM users "
                    + "WHERE email=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(
                    1,
                    email.trim()
            );


            ResultSet rs =
                    ps.executeQuery();


            return rs.next();


        } catch(Exception e) {

            e.printStackTrace();
        }


        return false;
    }



    // =========================
    // GET USER BY EMAIL
    // =========================

    public User getUserByEmail(
            String email) {

        User user = null;


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT id,name,email,password,role "
                    + "FROM users "
                    + "WHERE email=?";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ps.setString(
                    1,
                    email.trim()
            );


            ResultSet rs =
                    ps.executeQuery();


            if(rs.next()) {

                user =
                        new User();


                user.setId(
                        rs.getInt("id")
                );


                user.setName(
                        rs.getString("name")
                );


                user.setEmail(
                        rs.getString("email")
                );


                user.setPassword(
                        rs.getString("password")
                );


                user.setRole(
                        rs.getString("role")
                );
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return user;
    }



    // =========================
    // GET ALL CUSTOMERS
    // =========================

    public ArrayList<User> getAllCustomers() {

        ArrayList<User> users =
                new ArrayList<>();


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT id,name,email,password,role "
                    + "FROM users "
                    + "WHERE role='customer' "
                    + "ORDER BY id DESC";


            PreparedStatement ps =
                    con.prepareStatement(sql);


            ResultSet rs =
                    ps.executeQuery();


            while(rs.next()) {

                User user =
                        new User();


                user.setId(
                        rs.getInt("id")
                );


                user.setName(
                        rs.getString("name")
                );


                user.setEmail(
                        rs.getString("email")
                );


                user.setPassword(
                        rs.getString("password")
                );


                user.setRole(
                        rs.getString("role")
                );


                users.add(user);
            }


        } catch(Exception e) {

            e.printStackTrace();
        }


        return users;
    }



    // =========================
    // TOTAL USERS
    // =========================

    public int getTotalUsers() {

        int total = 0;


        try {

            Connection con =
                    DBConnection.getConnection();


            String sql =
                    "SELECT COUNT(*) FROM users";


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



    // =========================
    // DELETE CUSTOMER
    // KEEP ORDER HISTORY
    // =========================

    public boolean deleteUser(
            String email) {

        Connection con = null;


        try {

            con =
                    DBConnection.getConnection();


            con.setAutoCommit(false);



            // =========================
            // DELETE CUSTOMER CART
            // =========================

            String cartSql =
                    "DELETE FROM cart "
                    + "WHERE user_email=?";


            PreparedStatement cartPs =
                    con.prepareStatement(
                            cartSql
                    );


            cartPs.setString(
                    1,
                    email.trim()
            );


            cartPs.executeUpdate();



            // =========================
            // DELETE CUSTOMER ACCOUNT
            // =========================

            String userSql =
                    "DELETE FROM users "
                    + "WHERE email=? "
                    + "AND role='customer'";


            PreparedStatement userPs =
                    con.prepareStatement(
                            userSql
                    );


            userPs.setString(
                    1,
                    email.trim()
            );


            int deleted =
                    userPs.executeUpdate();



            con.commit();


            return deleted > 0;


        } catch(Exception e) {

            e.printStackTrace();


            try {

                if(con != null) {

                    con.rollback();
                }

            } catch(Exception ex) {

                ex.printStackTrace();
            }


        } finally {

            try {

                if(con != null) {

                    con.setAutoCommit(true);
                }

            } catch(Exception e) {

                e.printStackTrace();
            }
        }


        return false;
    }

}