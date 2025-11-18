package com.academy.management.dao;

import com.academy.management.model.EnrollmentApp;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EnrollmentAppDao {

    private Connection getConnection() throws SQLException {
        String url = "jdbc:oracle:thin:@localhost:1521:orcl";
        String username = "C##Pravalika";
        String password = "1234";
        return DriverManager.getConnection(url, username, password);
    }

 // Save enrollment (POST)
    public EnrollmentApp save(EnrollmentApp enrollment) throws SQLException {
        try (Connection con = getConnection()) {

            // 1️⃣ Generate next business_number (start from 1)
            String maxSql = "SELECT NVL(MAX(business_number), 0) FROM app_enrollments";
            long nextBusinessNumber;
            try (PreparedStatement ps = con.prepareStatement(maxSql);
                 ResultSet rs = ps.executeQuery()) {
                rs.next();
                nextBusinessNumber = rs.getLong(1) + 1;
            }

            // 2️⃣ Assign default values if not provided
            if (enrollment.getProgress() == null) enrollment.setProgress(0.0);
            if (enrollment.getStatus() == null) enrollment.setStatus("ENROLLED");
            if (enrollment.getEnrolledDate() == null) enrollment.setEnrolledDate(new java.util.Date());

            // 3️⃣ Insert record (let Oracle auto-generate 'id')
            String sql = "INSERT INTO app_enrollments " +
                    "(business_number, student_id, course_id, progress, status, enrolled_date, created_at) " +
                    "VALUES (?, ?, ?, ?, ?, ?, SYSDATE)";

            try (PreparedStatement ps = con.prepareStatement(sql, new String[]{"id"})) {
                ps.setLong(1, nextBusinessNumber);
                ps.setLong(2, enrollment.getStudentId());
                ps.setLong(3, enrollment.getCourseId());
                ps.setDouble(4, enrollment.getProgress());
                ps.setString(5, enrollment.getStatus());
                ps.setDate(6, new java.sql.Date(enrollment.getEnrolledDate().getTime()));

                ps.executeUpdate();

                // 4️⃣ Fetch generated ID
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        enrollment.setId(rs.getLong(1));
                    }
                }
            }

            // 5️⃣ Set business number in object
            enrollment.setBusinessNumber(nextBusinessNumber);

            return enrollment;
        }
    }


    public List<EnrollmentApp> findAll() throws SQLException {
        List<EnrollmentApp> list = new ArrayList<>();
        String sql = "SELECT * FROM app_enrollments ORDER BY id ASC";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public EnrollmentApp findById(Long id) throws SQLException {
        String sql = "SELECT * FROM app_enrollments WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return mapRow(rs);
            }
        }
        return null;
    }

    public EnrollmentApp update(EnrollmentApp e) throws SQLException {
        String sql = "UPDATE app_enrollments SET student_id=?, course_id=?, progress=?, status=?, enrolled_date=?, updated_at=SYSDATE WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, e.getStudentId());
            ps.setLong(2, e.getCourseId());
            ps.setDouble(3, e.getProgress());
            ps.setString(4, e.getStatus());
            ps.setDate(5, new java.sql.Date(e.getEnrolledDate().getTime()));
            ps.setLong(6, e.getId());
            ps.executeUpdate();
        }
        return e;
    }

    public EnrollmentApp patch(EnrollmentApp e) throws SQLException {
        EnrollmentApp existing = findById(e.getId());
        if (existing == null) return null;

        if (e.getStudentId() != null) existing.setStudentId(e.getStudentId());
        if (e.getCourseId() != null) existing.setCourseId(e.getCourseId());
        if (e.getProgress() != null) existing.setProgress(e.getProgress());
        if (e.getStatus() != null) existing.setStatus(e.getStatus());
        if (e.getEnrolledDate() != null) existing.setEnrolledDate(e.getEnrolledDate());

        return update(existing);
    }

    public boolean delete(Long id) throws SQLException {
        String sql = "DELETE FROM app_enrollments WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private EnrollmentApp mapRow(ResultSet rs) throws SQLException {
        EnrollmentApp e = new EnrollmentApp();
        e.setId(rs.getLong("id"));
        e.setBusinessNumber(rs.getLong("business_number"));
        e.setStudentId(rs.getLong("student_id"));
        e.setCourseId(rs.getLong("course_id"));
        e.setProgress(rs.getDouble("progress"));
        e.setEnrolledDate(rs.getDate("enrolled_date"));
        e.setStatus(rs.getString("status"));
        e.setCreatedAt(rs.getDate("created_at"));
        e.setUpdatedAt(rs.getDate("updated_at"));
        return e;
    }
}
