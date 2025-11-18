package com.academy.management.dao;

import com.academy.management.model.CourseApp;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CourseAppDao {

    // -------------------------
    // Connection method
    // -------------------------
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

    // -------------------------
    // Generate next business number
    // -------------------------
    private long getNextBusinessNumber() {
        String sql = "SELECT NVL(MAX(business_number),0)+1 AS next_bn FROM app_courses";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) return rs.getLong("next_bn");
        } catch (SQLException e) {
            throw new RuntimeException("Error generating business number: " + e.getMessage(), e);
        }
        return 1L;
    }

    // -------------------------
    // CREATE - insert course
    // -------------------------
    public CourseApp save(CourseApp course) {
        if (course.getBusinessNumber() == null) course.setBusinessNumber(getNextBusinessNumber());
        if (course.getStatus() == null) course.setStatus("ACTIVE");

        String sql = "INSERT INTO app_courses " +
                     "(business_number, name, description, duration, fee, status, created_at) " +
                     "VALUES (?, ?, ?, ?, ?, ?, SYSDATE)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, new String[]{"ID"})) {

            ps.setLong(1, course.getBusinessNumber());
            ps.setString(2, course.getName());
            ps.setString(3, course.getDescription());
            ps.setString(4, course.getDuration());
            ps.setDouble(5, course.getFee());
            ps.setString(6, course.getStatus());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) course.setId(rs.getLong(1));
            return course;

        } catch (SQLException e) {
            throw new RuntimeException("Error saving course: " + e.getMessage(), e);
        }
    }

    // -------------------------
    // GET all courses
    // -------------------------
    public List<CourseApp> getAllCourses() {
        List<CourseApp> list = new ArrayList<>();
        String sql = "SELECT * FROM app_courses ORDER BY id ASC";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(mapResultSetToCourse(rs));
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching courses: " + e.getMessage(), e);
        }
        return list;
    }

    // -------------------------
    // GET by ID
    // -------------------------
    public CourseApp getCourseById(Long id) {
        String sql = "SELECT * FROM app_courses WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapResultSetToCourse(rs);

        } catch (SQLException e) {
            throw new RuntimeException("Error fetching course: " + e.getMessage(), e);
        }
        return null;
    }

    // -------------------------
    // UPDATE (PUT)
    // -------------------------
    public void update(CourseApp course) {
        String sql = "UPDATE app_courses SET name=?, description=?, duration=?, fee=?, status=?, updated_at=SYSDATE WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, course.getName());
            ps.setString(2, course.getDescription());
            ps.setString(3, course.getDuration());
            ps.setDouble(4, course.getFee());
            ps.setString(5, course.getStatus() != null ? course.getStatus() : "ACTIVE");
            ps.setLong(6, course.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error updating course: " + e.getMessage(), e);
        }
    }

    // -------------------------
    // PATCH (partial update)
    // -------------------------
    public void updatePartial(CourseApp course) {
        CourseApp existing = getCourseById(course.getId());
        if (existing == null) throw new RuntimeException("Course not found with ID: " + course.getId());

        String sql = "UPDATE app_courses SET name=?, description=?, duration=?, fee=?, status=?, updated_at=SYSDATE WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, course.getName() != null ? course.getName() : existing.getName());
            ps.setString(2, course.getDescription() != null ? course.getDescription() : existing.getDescription());
            ps.setString(3, course.getDuration() != null ? course.getDuration() : existing.getDuration());
            ps.setDouble(4, course.getFee() != null ? course.getFee() : existing.getFee());
            ps.setString(5, course.getStatus() != null ? course.getStatus() : existing.getStatus());
            ps.setLong(6, course.getId());

            ps.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException("Error partially updating course: " + e.getMessage(), e);
        }
    }

    // -------------------------
    // DELETE
    // -------------------------
    public boolean delete(Long id) {
        String sql = "DELETE FROM app_courses WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Error deleting course: " + e.getMessage(), e);
        }
    }

    // -------------------------
    // Map ResultSet -> CourseApp
    // -------------------------
    private CourseApp mapResultSetToCourse(ResultSet rs) throws SQLException {
        CourseApp course = new CourseApp();
        course.setId(rs.getLong("id"));
        course.setBusinessNumber(rs.getLong("business_number"));
        course.setName(rs.getString("name"));
        course.setDescription(rs.getString("description"));
        course.setDuration(rs.getString("duration"));
        course.setFee(rs.getDouble("fee"));
        course.setStatus(rs.getString("status"));
        course.setCreatedAt(rs.getDate("created_at"));
        course.setUpdatedAt(rs.getDate("updated_at"));
        return course;
    }
}
