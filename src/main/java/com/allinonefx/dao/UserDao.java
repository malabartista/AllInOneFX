/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.allinonefx.dao;

import com.allinonefx.config.DbHandler;
import com.allinonefx.models.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/*
 * User DAO class
 */
public class UserDao {

    // The interface of our DAO should be like
//    User getUser();
//    Set<User> getAllUsers();
//    User getUserByUserNameAndPassword();
//    boolean insertUser();
//    boolean updateUser();
//    boolean deleteUser();
    public User getUser(int id) {
        DbHandler handler = new DbHandler();
        Connection connection = handler.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user WHERE id=" + id);
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public Set getAllUsers() {
        DbHandler handler = new DbHandler();
        Connection connection = handler.getConnection();
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM user");
            Set users = new HashSet();
            while (rs.next()) {
                User user = extractUserFromResultSet(rs);
                users.add(user);
            }
            return users;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public User getUserByUserNameAndPassword(String user, String pass) {
        DbHandler handler = new DbHandler();
        Connection connection = handler.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM user WHERE username=? AND password=?");
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUserFromResultSet(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean insertUser(User user) {
        DbHandler handler = new DbHandler();
        Connection connection = handler.getConnection();
        try {
//            String insert = "INSERT INTO students(fname,lname,location,gender,email,phone,"
//                    + "`level`,department,course,paid) "
//                    + "VALUES (?,?,?,?,?,?,?,?,?,?)";
            PreparedStatement ps = connection.prepareStatement("INSERT INTO user(username,password,email,fname,lname,location,gender,level,department,course) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps = userPreparedStatement(ps, user);
            int i = ps.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean updateUser(User user) {
        DbHandler handler = new DbHandler();
        Connection connection = handler.getConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE user SET username=?, password=?, mobile=?, fname=?, lname=? WHERE id=?");
            ps = userPreparedStatement(ps, user);
            ps.setInt(6, user.id.get());
            int i = ps.executeUpdate();
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(int id) {
        DbHandler handler = new DbHandler();
        Connection connection = handler.getConnection();
        try {
            Statement stmt = connection.createStatement();
            int i = stmt.executeUpdate("DELETE FROM user WHERE id=" + id);
            if (i == 1) {
                return true;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
    
    public PreparedStatement userPreparedStatement(PreparedStatement ps, User user) throws SQLException{
        ps.setString(1, user.userName.get());
        ps.setString(2, user.password.get());
        ps.setInt(3, user.mobile.get());
        ps.setString(4, user.firstName.get());
        ps.setString(5, user.lastName.get());
        ps.setString(6, user.location.get());
        ps.setString(7, user.gender.get());
        ps.setString(8, user.level.get());
        ps.setString(9, user.department.get());
        return ps;
    }

    private User extractUserFromResultSet(ResultSet rs) throws SQLException {
        User user = new User();
        // user table
        user.id.set(rs.getInt("id"));
        user.userName.set(rs.getString("username"));
        user.password.set(rs.getString("password"));
        user.firstName.set(rs.getString("fname"));
        user.lastName.set(rs.getString("lname"));
        user.email.set(rs.getString("email"));
        user.location.set(rs.getString("location"));
        user.gender.set(rs.getString("gender"));
        user.level.set(rs.getString("level"));
        user.department.set(rs.getString("department"));
        user.mobile.set(rs.getInt("mobile"));

        // user image
        String imagePath = "icons/profile1.png";
        if (rs.getString("gender") != null && rs.getString("gender").equals("MALE")) {
            imagePath = "icons/profile-man.png";
        } else {
            imagePath = "icons/profile-woman.png";
        }
        Image img = new Image(imagePath);
        ImageView mv = new ImageView();
        mv.setImage(img);
        user.userPhoto.set(mv);

        return user;
    }
}
