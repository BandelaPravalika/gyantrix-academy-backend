package com.academy.management.dao;

import com.academy.management.model.FeeApp;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FeeAppDao {

    // ===== Database connection =====
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver not found", e);
        }
        return DriverManager.getConnection(
                "jdbc:oracle:thin:@localhost:1521:orcl",  // change if needed
                "C##Pravalika",                            // your DB username
                "1234"                                     // your DB password
        );
    }

    // ===== Get next business_number (auto-increment) =====
    private Long getNextBusinessNumber(Connection conn) throws SQLException {
        String sql = "SELECT NVL(MAX(business_number), 0) + 1 AS next_bn FROM app_fees";
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getLong("next_bn");
            }
        }
        return 1L;
    }

    // ===== Save Fee =====
 // Save Fee
    public FeeApp save(FeeApp fee) throws SQLException {
        String sql = "INSERT INTO app_fees (business_number, student_id, course_id, amount_paid, payment_date, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            // Auto-increment business_number
            if (fee.getBusinessNumber() == null) {
                fee.setBusinessNumber(getNextBusinessNumber(conn));
            }

            // Set values safely using setObject for nullable numeric fields
            ps.setObject(1, fee.getBusinessNumber(), Types.NUMERIC);
            ps.setObject(2, fee.getStudentId(), Types.NUMERIC);
            ps.setObject(3, fee.getCourseId(), Types.NUMERIC);
            ps.setObject(4, fee.getAmountPaid() != null ? fee.getAmountPaid() : 0.0, Types.NUMERIC);
            ps.setDate(5, fee.getPaymentDate() != null ? new java.sql.Date(fee.getPaymentDate().getTime()) :
                    new java.sql.Date(System.currentTimeMillis()));
            ps.setString(6, fee.getStatus() != null ? fee.getStatus() : "PAID");
            ps.setDate(7, fee.getCreatedAt() != null ? new java.sql.Date(fee.getCreatedAt().getTime()) :
                    new java.sql.Date(System.currentTimeMillis()));

            ps.executeUpdate();

            // Fetch the inserted record by business_number to get auto-generated id
            String selectSql = "SELECT * FROM app_fees WHERE business_number = ?";
            try (PreparedStatement ps2 = conn.prepareStatement(selectSql)) {
                ps2.setObject(1, fee.getBusinessNumber(), Types.NUMERIC);
                try (ResultSet rs = ps2.executeQuery()) {
                    if (rs.next()) {
                        fee = mapResultSetToFee(rs); // mapResultSetToFee handles all nullable fields safely
                    }
                }
            }
        }

        return fee;
    }


    // ===== Update Fee (partial update) =====
    public FeeApp update(Long id, FeeApp fee) throws SQLException {
        String sql = "UPDATE app_fees SET " +
                     "student_id = ?, " +
                     "course_id = ?, " +
                     "amount_paid = ?, " +
                     "payment_date = ?, " +
                     "status = ?, " +
                     "updated_at = ? " +
                     "WHERE id = ?";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            FeeApp existing = getById(id);
            if (existing == null) return null;

            ps.setObject(1, fee.getStudentId() != null ? fee.getStudentId() : existing.getStudentId(), Types.NUMERIC);
            ps.setObject(2, fee.getCourseId() != null ? fee.getCourseId() : existing.getCourseId(), Types.NUMERIC);
            ps.setObject(3, fee.getAmountPaid() != null ? fee.getAmountPaid() : existing.getAmountPaid(), Types.NUMERIC);
            ps.setDate(4, fee.getPaymentDate() != null ? new java.sql.Date(fee.getPaymentDate().getTime()) :
                    existing.getPaymentDate() != null ? new java.sql.Date(existing.getPaymentDate().getTime()) : new java.sql.Date(System.currentTimeMillis()));
            ps.setString(5, fee.getStatus() != null ? fee.getStatus() : existing.getStatus());
            ps.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            ps.setLong(7, id);

            ps.executeUpdate();
        }

        return getById(id);
    }
 // Partial update by ID
    public String patch(Long id, FeeApp fee) {
        String sql = "UPDATE app_fees SET " +
                     "student_id = ?, " +
                     "course_id = ?, " +
                     "amount_paid = ?, " +
                     "payment_date = ?, " +
                     "status = ?, " +
                     "updated_at = ? " +
                     "WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            FeeApp existing = getById(id);
            if (existing == null) return "No fee record found with ID: " + id;

            ps.setObject(1, fee.getStudentId() != null ? fee.getStudentId() : existing.getStudentId(), Types.NUMERIC);
            ps.setObject(2, fee.getCourseId() != null ? fee.getCourseId() : existing.getCourseId(), Types.NUMERIC);
            ps.setObject(3, fee.getAmountPaid() != null ? fee.getAmountPaid() : existing.getAmountPaid(), Types.NUMERIC);
            ps.setDate(4, fee.getPaymentDate() != null ? new java.sql.Date(fee.getPaymentDate().getTime())
                    : existing.getPaymentDate() != null ? new java.sql.Date(existing.getPaymentDate().getTime())
                    : new java.sql.Date(System.currentTimeMillis()));
            ps.setString(5, fee.getStatus() != null ? fee.getStatus() : existing.getStatus());
            ps.setDate(6, new java.sql.Date(System.currentTimeMillis()));
            ps.setLong(7, id);

            int rows = ps.executeUpdate();
            return rows > 0 ? "Fee updated successfully (PATCH)." : "Fee update failed.";
        } catch (SQLException e) {
            throw new RuntimeException("Error patching fee: " + e.getMessage(), e);
        }
    }


    // ===== Get all fees =====
    public List<FeeApp> getAll() throws SQLException {
        List<FeeApp> list = new ArrayList<>();
        String sql = "SELECT * FROM app_fees ORDER BY id ASC";
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToFee(rs));
            }
        }
        return list;
    }

    // ===== Get fee by ID =====
    public FeeApp getById(Long id) throws SQLException {
        FeeApp fee = null;
        String sql = "SELECT * FROM app_fees WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    fee = mapResultSetToFee(rs);
                }
            }
        }
        return fee;
    }

    // ===== Delete fee =====
    public String delete(Long id) throws SQLException {
        String sql = "DELETE FROM app_fees WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                return "Fee record deleted successfully.";
            } else {
                return "No fee record found with ID: " + id;
            }
        }
    }


    // ===== Map ResultSet to FeeApp =====
    private FeeApp mapResultSetToFee(ResultSet rs) throws SQLException {
        FeeApp fee = new FeeApp();
        fee.setId(rs.getLong("id"));
        fee.setBusinessNumber(rs.getObject("business_number", Long.class));
        fee.setStudentId(rs.getObject("student_id", Long.class));
        fee.setCourseId(rs.getObject("course_id", Long.class));
        fee.setAmountPaid(rs.getObject("amount_paid", Double.class));
        fee.setPaymentDate(rs.getDate("payment_date"));
        fee.setStatus(rs.getString("status"));
        fee.setCreatedAt(rs.getDate("created_at"));
        fee.setUpdatedAt(rs.getDate("updated_at"));
        return fee;
    }
}
