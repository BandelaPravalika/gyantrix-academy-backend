package com.academy.management.dao;

import com.academy.management.model.HiringApp;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HiringAppDao {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String USER = "C##Pravalika";
    private static final String PASSWORD = "1234";

    // ✅ Method to get next business number automatically
    private int getNextBusinessNumber(Connection conn) throws SQLException {
        String query = "SELECT NVL(MAX(business_number), 0) + 1 AS next_number FROM app_hirings";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getInt("next_number");
            }
        }
        return 1; // default if table empty
    }
    
    
    // ✅ Add new hiring
    public String addHiring(HiringApp hiring) throws SQLException {
        String query = "INSERT INTO app_hirings (business_number, email, title, description, domain, location, deadline, posted_date, salary, education_details, experience, skills, shortlisted_count, application_count) "
                     + "VALUES (?, ?, ?, ?, ?, ?, ?, NVL(?, SYSDATE), ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            // Get the next business number automatically
            int nextBusinessNumber = getNextBusinessNumber(conn);

            ps.setInt(1, nextBusinessNumber);
            ps.setString(2, hiring.getEmail());
            ps.setString(3, hiring.getTitle());
            ps.setString(4, hiring.getDescription());
            ps.setString(5, hiring.getDomain());
            ps.setString(6, hiring.getLocation());
            ps.setDate(7, hiring.getDeadline());
            ps.setDate(8, hiring.getPostedDate());
            ps.setString(9, hiring.getSalary());
            ps.setString(10, hiring.getEducationDetails());
            ps.setString(11, hiring.getExperience());
            ps.setString(12, hiring.getSkills());
            ps.setInt(13, hiring.getShortlistedCount());
            ps.setInt(14, hiring.getApplicationCount());

            ps.executeUpdate();
            return "Hiring added successfully with business number: " + nextBusinessNumber;
        }
    }

    // Get all hirings
    public List<HiringApp> getAllHirings() throws SQLException {
        String query = "SELECT * FROM app_hirings ORDER BY hiring_id ASC";
        List<HiringApp> list = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                HiringApp h = new HiringApp();
                h.setHiringId(rs.getInt("hiring_id"));
                h.setBusinessNumber(rs.getInt("business_number"));
                h.setEmail(rs.getString("email"));
                h.setTitle(rs.getString("title"));
                h.setDescription(rs.getString("description"));
                h.setDomain(rs.getString("domain"));
                h.setLocation(rs.getString("location"));
                h.setDeadline(rs.getDate("deadline"));
                h.setPostedDate(rs.getDate("posted_date"));
                h.setSalary(rs.getString("salary"));
                h.setEducationDetails(rs.getString("education_details"));
                h.setExperience(rs.getString("experience"));
                h.setSkills(rs.getString("skills"));
                h.setShortlistedCount(rs.getInt("shortlisted_count"));
                h.setApplicationCount(rs.getInt("application_count"));
                list.add(h);
            }
        }
        return list;
    }

    // Get by ID
    public HiringApp getHiringById(int id) throws SQLException {
        String query = "SELECT * FROM app_hirings WHERE hiring_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                HiringApp h = new HiringApp();
                h.setHiringId(rs.getInt("hiring_id"));
                h.setBusinessNumber(rs.getInt("business_number"));
                h.setEmail(rs.getString("email"));
                h.setTitle(rs.getString("title"));
                h.setDescription(rs.getString("description"));
                h.setDomain(rs.getString("domain"));
                h.setLocation(rs.getString("location"));
                h.setDeadline(rs.getDate("deadline"));
                h.setPostedDate(rs.getDate("posted_date"));
                h.setSalary(rs.getString("salary"));
                h.setEducationDetails(rs.getString("education_details"));
                h.setExperience(rs.getString("experience"));
                h.setSkills(rs.getString("skills"));
                h.setShortlistedCount(rs.getInt("shortlisted_count"));
                h.setApplicationCount(rs.getInt("application_count"));
                return h;
            }
        }
        return null;
    }

    // Update (PUT)
    public String updateHiring(int id, HiringApp hiring) throws SQLException {
        String query = "UPDATE app_hirings SET title=?, description=?, domain=?, location=?, deadline=?, salary=?, education_details=?, experience=?, skills=? WHERE hiring_id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, hiring.getTitle());
            ps.setString(2, hiring.getDescription());
            ps.setString(3, hiring.getDomain());
            ps.setString(4, hiring.getLocation());
            ps.setDate(5, hiring.getDeadline());
            ps.setString(6, hiring.getSalary());
            ps.setString(7, hiring.getEducationDetails());
            ps.setString(8, hiring.getExperience());
            ps.setString(9, hiring.getSkills());
            ps.setInt(10, id);

            int rows = ps.executeUpdate();
            return rows > 0 ? "Hiring updated successfully!" : "Hiring not found!";
        }
    }
    
    public String patchHiring(int id, HiringApp hiring) throws SQLException {
        StringBuilder query = new StringBuilder("UPDATE app_hirings SET ");
        List<Object> params = new ArrayList<>();

        if (hiring.getTitle() != null) {
            query.append("title=?, ");
            params.add(hiring.getTitle());
        }
        if (hiring.getDescription() != null) {
            query.append("description=?, ");
            params.add(hiring.getDescription());
        }
        if (hiring.getDomain() != null) {
            query.append("domain=?, ");
            params.add(hiring.getDomain());
        }
        if (hiring.getLocation() != null) {
            query.append("location=?, ");
            params.add(hiring.getLocation());
        }
        if (hiring.getDeadline() != null) {
            query.append("deadline=?, ");
            params.add(hiring.getDeadline());
        }
        if (hiring.getSalary() != null) {
            query.append("salary=?, ");
            params.add(hiring.getSalary());
        }
        if (hiring.getEducationDetails() != null) {
            query.append("education_details=?, ");
            params.add(hiring.getEducationDetails());
        }
        if (hiring.getExperience() != null) {
            query.append("experience=?, ");
            params.add(hiring.getExperience());
        }
        if (hiring.getSkills() != null) {
            query.append("skills=?, ");
            params.add(hiring.getSkills());
        }

        // remove last comma
        if (params.isEmpty()) {
            return "No fields provided for update!";
        }
        query.setLength(query.length() - 2);
        query.append(" WHERE hiring_id=?");
        params.add(id);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < params.size(); i++) {
                Object value = params.get(i);
                if (value instanceof java.sql.Date)
                    ps.setDate(i + 1, (java.sql.Date) value);
                else
                    ps.setObject(i + 1, value);
            }

            int rows = ps.executeUpdate();
            return rows > 0 ? "Hiring patched successfully!" : "Hiring not found!";
        }
    }


    // Delete
    public boolean deleteHiring(int id) throws SQLException {
        String query = "DELETE FROM app_hirings WHERE hiring_id = ?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
