package com.app.dao_implementation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.app.dao.UserDAO;
import com.app.models.User;
import com.app.utility.DBConnection;

public class UserDAOImpl implements UserDAO {

    private static final String INSERT_USER_QUERY =
        "INSERT INTO users (name, username, password, email, phone, address, role) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_USER_QUERY =
        "SELECT * FROM users WHERE user_id = ?";

    private static final String UPDATE_USER_QUERY =
        "UPDATE users SET name=?, username=?, password=?, email=?, phone=?, address=?, role=? WHERE user_id=?";

    private static final String DELETE_USER_QUERY =
        "DELETE FROM users WHERE user_id = ?";

    private static final String GET_ALL_USERS_QUERY =
        "SELECT * FROM users";

    private static final String GET_USER_BY_USERNAME_QUERY =
        "SELECT * FROM users WHERE username = ?";

    // -------------------------------------------------------------------------
    // Add user
    // -------------------------------------------------------------------------
    @Override
    public void addUser(User user) {

        Connection connection = DBConnection.getConnection();
        if (connection == null) {
            throw new RuntimeException("❌ Database connection is NULL (addUser)");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(INSERT_USER_QUERY)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getAddress());
            pstmt.setString(7, user.getRole());

            pstmt.executeUpdate();
            System.out.println("✅ User inserted successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
    }

    // -------------------------------------------------------------------------
    // Get user by ID
    // -------------------------------------------------------------------------
    @Override
    public User getUser(int userId) {

        Connection connection = DBConnection.getConnection();
        if (connection == null) {
            throw new RuntimeException("❌ Database connection is NULL (getUser)");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(GET_USER_QUERY)) {

            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // Get user by username (THIS WAS FAILING)
    // -------------------------------------------------------------------------
    @Override
    public User getUserByUsername(String username) {

        Connection connection = DBConnection.getConnection();
        if (connection == null) {
            throw new RuntimeException("❌ Database connection is NULL (getUserByUsername)");
        }

        try (PreparedStatement pstmt =
                 connection.prepareStatement(GET_USER_BY_USERNAME_QUERY)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractUser(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }

        return null;
    }

    // -------------------------------------------------------------------------
    // Update user
    // -------------------------------------------------------------------------
    @Override
    public void updateUser(User user) {

        Connection connection = DBConnection.getConnection();
        if (connection == null) {
            throw new RuntimeException("❌ Database connection is NULL (updateUser)");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(UPDATE_USER_QUERY)) {

            pstmt.setString(1, user.getName());
            pstmt.setString(2, user.getUsername());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getEmail());
            pstmt.setString(5, user.getPhone());
            pstmt.setString(6, user.getAddress());
            pstmt.setString(7, user.getRole());
            pstmt.setInt(8, user.getUserId());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
    }

    // -------------------------------------------------------------------------
    // Delete user
    // -------------------------------------------------------------------------
    @Override
    public void deleteUser(int userId) {

        Connection connection = DBConnection.getConnection();
        if (connection == null) {
            throw new RuntimeException("❌ Database connection is NULL (deleteUser)");
        }

        try (PreparedStatement pstmt = connection.prepareStatement(DELETE_USER_QUERY)) {

            pstmt.setInt(1, userId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
    }

    // -------------------------------------------------------------------------
    // Get all users
    // -------------------------------------------------------------------------
    @Override
    public List<User> getAllUsers() {

        List<User> users = new ArrayList<>();
        Connection connection = DBConnection.getConnection();

        if (connection == null) {
            throw new RuntimeException("❌ Database connection is NULL (getAllUsers)");
        }

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(GET_ALL_USERS_QUERY)) {

            while (rs.next()) {
                users.add(extractUser(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }

        return users;
    }

    // -------------------------------------------------------------------------
    // Helper methods
    // -------------------------------------------------------------------------
    private User extractUser(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("user_id"),
            rs.getString("name"),
            rs.getString("username"),
            rs.getString("password"),
            rs.getString("email"),
            rs.getString("phone"),
            rs.getString("address"),
            rs.getString("role"),
            null,
            null
        );
    }

    private void close(Connection con) {
        try {
            if (con != null) con.close();
        } catch (SQLException ignored) {}
    }
}