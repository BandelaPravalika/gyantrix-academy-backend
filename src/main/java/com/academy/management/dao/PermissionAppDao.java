package com.academy.management.dao;

import com.academy.management.model.PermissionApp;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository
public class PermissionAppDao {

    private Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver not found", e);
        }
        return DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:orcl", "C##Pravalika", "1234");
    }

    // GET ALL
    public List<PermissionApp> getAllPermissions() throws SQLException {
        String query = "SELECT * FROM app_permissions";
        List<PermissionApp> list = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                PermissionApp p = new PermissionApp();
                p.setId(rs.getLong("id"));
                p.setBusinessNumber(rs.getLong("business_number"));
                p.setUserId(rs.getLong("user_id"));
                p.setPermissionKey(rs.getString("permission_key"));
                p.setStatus(rs.getString("status"));
                p.setCreatedAt(rs.getDate("created_at"));
                p.setUpdatedAt(rs.getDate("updated_at"));
                list.add(p);
            }
        }
        return list;
    }

    // GET BY ID
    public PermissionApp getPermissionById(Long id) throws SQLException {
        String query = "SELECT * FROM app_permissions WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    PermissionApp p = new PermissionApp();
                    p.setId(rs.getLong("id"));
                    p.setBusinessNumber(rs.getLong("business_number"));
                    p.setUserId(rs.getLong("user_id"));
                    p.setPermissionKey(rs.getString("permission_key"));
                    p.setStatus(rs.getString("status"));
                    p.setCreatedAt(rs.getDate("created_at"));
                    p.setUpdatedAt(rs.getDate("updated_at"));
                    return p;
                }
            }
        }
        return null;
    }

    // POST / SAVE
    public PermissionApp savePermission(PermissionApp permission) throws SQLException {
        String insertQuery = "INSERT INTO app_permissions " +
                "(business_number, user_id, permission_key, status, created_at) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(insertQuery, new String[]{"id"})) {

            // Auto-increment business number starting from 1
            String nextBnQuery = "SELECT NVL(MAX(business_number), 0) + 1 AS next_bn FROM app_permissions";
            try (PreparedStatement psBn = conn.prepareStatement(nextBnQuery);
                 ResultSet rs = psBn.executeQuery()) {
                if (rs.next()) {
                    permission.setBusinessNumber(rs.getLong("next_bn"));
                } else {
                    permission.setBusinessNumber(1L);
                }
            }

            // Defaults
            if (permission.getStatus() == null) permission.setStatus("ENABLED");
            if (permission.getCreatedAt() == null) permission.setCreatedAt(new Date());

            // Set values
            ps.setLong(1, permission.getBusinessNumber());
            ps.setLong(2, permission.getUserId() != null ? permission.getUserId() : Types.NULL);
            ps.setString(3, permission.getPermissionKey());
            ps.setString(4, permission.getStatus());
            ps.setTimestamp(5, new java.sql.Timestamp(permission.getCreatedAt().getTime()));

            ps.executeUpdate();

            // Get generated ID
            try (ResultSet rsKeys = ps.getGeneratedKeys()) {
                if (rsKeys.next()) permission.setId(rsKeys.getLong(1));
            }

            return permission;
        }
    }

    // PUT / FULL UPDATE
    public PermissionApp updatePermission(Long id, PermissionApp permission) throws SQLException {
        String query = "UPDATE app_permissions SET user_id=?, permission_key=?, status=?, updated_at=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, permission.getUserId() != null ? permission.getUserId() : Types.NULL);
            ps.setString(2, permission.getPermissionKey());
            ps.setString(3, permission.getStatus() != null ? permission.getStatus() : "ENABLED");
            ps.setTimestamp(4, new java.sql.Timestamp(new Date().getTime()));
            ps.setLong(5, id);

            ps.executeUpdate();
            return getPermissionById(id);
        }
    }

    // PATCH / PARTIAL UPDATE
    public PermissionApp patchPermission(Long id, PermissionApp permission) throws SQLException {
        PermissionApp existing = getPermissionById(id);
        if (existing == null) return null;

        if (permission.getUserId() != null) existing.setUserId(permission.getUserId());
        if (permission.getPermissionKey() != null) existing.setPermissionKey(permission.getPermissionKey());
        if (permission.getStatus() != null) existing.setStatus(permission.getStatus());
        existing.setUpdatedAt(new Date());

        String query = "UPDATE app_permissions SET user_id=?, permission_key=?, status=?, updated_at=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setLong(1, existing.getUserId() != null ? existing.getUserId() : Types.NULL);
            ps.setString(2, existing.getPermissionKey());
            ps.setString(3, existing.getStatus());
            ps.setTimestamp(4, new java.sql.Timestamp(existing.getUpdatedAt().getTime()));
            ps.setLong(5, id);

            ps.executeUpdate();
        }
        return existing;
    }

    // DELETE
    public boolean deletePermission(Long id) throws SQLException {
        String query = "DELETE FROM app_permissions WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
