package com.academy.management.dao;

import com.academy.management.model.UserApp;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class UserAppDao {

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver not found", e);
        }

        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username = "C##Pravalika";
        String password = "1234";

        return DriverManager.getConnection(url, username, password);
    }

    // Fetch all users
    public List<UserApp> getAllUsers() {
        List<UserApp> users = new ArrayList<>();
        String sql = "SELECT id, business_number, name, email, password, role, mobile_number, created_at, updated_at FROM APP_USERS ORDER BY id ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching users: " + e.getMessage(), e);
        }
        return users;
    }

    // Fetch user by ID
    public UserApp getUserById(Long id) {
        String sql = "SELECT id, business_number, name, email, password, role, mobile_number, created_at, updated_at FROM APP_USERS WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToUser(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching user by ID: " + e.getMessage(), e);
        }
        return null;
    }

    // Insert user
    public UserApp save(UserApp user) {
        if (user.getBusinessNumber() == null) {
            user.setBusinessNumber(getNextBusinessNumber());
        }

        String sql = "INSERT INTO APP_USERS (business_number, name, email, password, role, mobile_number, created_at) VALUES (?, ?, ?, ?, ?, ?, SYSDATE)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID"})) {

            ps.setLong(1, user.getBusinessNumber());
            ps.setString(2, user.getName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setString(5, user.getRole() != null ? user.getRole() : "STUDENT");
            ps.setString(6, user.getMobileNumber());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) user.setId(rs.getLong(1));
            user.setCreatedAt(new Date());
            return user;
        } catch (SQLException e) {
            throw new RuntimeException("Error saving user: " + e.getMessage(), e);
        }
    }

    // Full update
    public void update(UserApp user) {
        String sql = "UPDATE APP_USERS SET name=?, email=?, password=?, role=?, mobile_number=?, updated_at=SYSDATE WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getRole());
            ps.setString(5, user.getMobileNumber());
            ps.setLong(6, user.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating user: " + e.getMessage(), e);
        }
    }

    // Partial update
    public void updatePartial(UserApp user) {
        StringBuilder sql = new StringBuilder("UPDATE APP_USERS SET ");
        List<Object> params = new ArrayList<>();

        if (user.getName() != null) { sql.append("name = ?, "); params.add(user.getName()); }
        if (user.getEmail() != null) { sql.append("email = ?, "); params.add(user.getEmail()); }
        if (user.getPassword() != null) { sql.append("password = ?, "); params.add(user.getPassword()); }
        if (user.getRole() != null) { sql.append("role = ?, "); params.add(user.getRole()); }
        if (user.getMobileNumber() != null) { sql.append("mobile_number = ?, "); params.add(user.getMobileNumber()); }

        if (params.isEmpty()) return; // Nothing to update

        sql.append("updated_at = SYSDATE WHERE id = ?");
        params.add(user.getId());

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++) ps.setObject(i + 1, params.get(i));
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error partially updating user: " + e.getMessage(), e);
        }
    }

    // Delete user
    public boolean delete(Long id) {
        String sql = "DELETE FROM APP_USERS WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

    // Generate business number
    private long getNextBusinessNumber() {
        String sql = "SELECT NVL(MAX(business_number), 0) + 1 FROM APP_USERS";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong(1);
        } catch (SQLException e) {
            throw new RuntimeException("Error generating business number: " + e.getMessage(), e);
        }
        return 1L;
    }

    private UserApp mapResultSetToUser(ResultSet rs) throws SQLException {
        UserApp user = new UserApp();
        user.setId(rs.getLong("id"));
        user.setBusinessNumber(rs.getLong("business_number"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRole(rs.getString("role"));
        user.setMobileNumber(rs.getString("mobile_number"));

        Timestamp createdTs = rs.getTimestamp("created_at");
        Timestamp updatedTs = rs.getTimestamp("updated_at");
        if (createdTs != null) user.setCreatedAt(new Date(createdTs.getTime()));
        if (updatedTs != null) user.setUpdatedAt(new Date(updatedTs.getTime()));

        return user;
    }
}
