////package com.academy.management.dao;
////
////import com.academy.management.model.LeadApp;
////import org.springframework.stereotype.Repository;
////
////import java.sql.*;
////import java.util.ArrayList;
////import java.util.Date;
////import java.util.List;
////
////@Repository
////public class LeadAppDao {
////
////    private Connection getConnection() throws SQLException {
////        try { Class.forName("oracle.jdbc.driver.OracleDriver"); }
////        catch (ClassNotFoundException e) { throw new SQLException("Oracle JDBC Driver not found", e); }
////
////        return DriverManager.getConnection(
////                "jdbc:oracle:thin:@localhost:1521:orcl", "C##Pravalika", "1234");
////    }
////
////    private Long getNextBusinessNumber() throws SQLException {
////        String query = "SELECT NVL(MAX(business_number), 0) + 1 AS next_bn FROM app_leads";
////        try (Connection conn = getConnection();
////             PreparedStatement ps = conn.prepareStatement(query);
////             ResultSet rs = ps.executeQuery()) {
////            if (rs.next()) return rs.getLong("next_bn");
////        }
////        return 1L;
////    }
////
////    // Save Lead (POST)
////    public LeadApp saveLead(LeadApp lead) throws SQLException {
////        String insertQuery = "INSERT INTO app_leads " +
////                "(business_number, name, email, mobile_number, source, status, course, message, created_at) " +
////                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
////
////        try (Connection conn = getConnection();
////             PreparedStatement ps = conn.prepareStatement(insertQuery, new String[]{"id"})) {
////
////            lead.setBusinessNumber(getNextBusinessNumber());
////
////            if (lead.getStatus() == null) lead.setStatus("NEW");
////            if (lead.getCreatedAt() == null) lead.setCreatedAt(new Date());
////            if (lead.getCourse() == null) lead.setCourse(null);
////            if (lead.getMessage() == null) lead.setMessage(null);
////
////            ps.setLong(1, lead.getBusinessNumber());
////            ps.setString(2, lead.getName());
////            ps.setString(3, lead.getEmail());
////            ps.setString(4, lead.getMobileNumber());
////            ps.setString(5, lead.getSource());
////            ps.setString(6, lead.getStatus());
////            ps.setString(7, lead.getCourse());
////            ps.setString(8, lead.getMessage());
////            ps.setTimestamp(9, new java.sql.Timestamp(lead.getCreatedAt().getTime()));
////
////            ps.executeUpdate();
////
////            try (ResultSet rsKeys = ps.getGeneratedKeys()) {
////                if (rsKeys.next()) lead.setId(rsKeys.getLong(1));
////            }
////
////            return lead;
////        }
////    }
////
////    // Full Update (PUT)
////    public LeadApp updateLead(Long id, LeadApp lead) throws SQLException {
////        String updateQuery = "UPDATE app_leads SET name=?, email=?, mobile_number=?, source=?, status=?, course=?, message=?, updated_at=? WHERE id=?";
////        try (Connection conn = getConnection();
////             PreparedStatement ps = conn.prepareStatement(updateQuery)) {
////
////            if (lead.getStatus() == null) lead.setStatus("NEW");
////            lead.setUpdatedAt(new Date());
////
////            ps.setString(1, lead.getName());
////            ps.setString(2, lead.getEmail());
////            ps.setString(3, lead.getMobileNumber());
////            ps.setString(4, lead.getSource());
////            ps.setString(5, lead.getStatus());
////            ps.setString(6, lead.getCourse());
////            ps.setString(7, lead.getMessage());
////            ps.setTimestamp(8, new java.sql.Timestamp(lead.getUpdatedAt().getTime()));
////            ps.setLong(9, id);
////
////            ps.executeUpdate();
////        }
////        return lead;
////    }
////
////    // Partial Update (PATCH)
////    public LeadApp patchLead(Long id, LeadApp lead) throws SQLException {
////        LeadApp existing = getLeadById(id);
////        if (existing == null) return null;
////
////        if (lead.getName() != null) existing.setName(lead.getName());
////        if (lead.getEmail() != null) existing.setEmail(lead.getEmail());
////        if (lead.getMobileNumber() != null) existing.setMobileNumber(lead.getMobileNumber());
////        if (lead.getSource() != null) existing.setSource(lead.getSource());
////        if (lead.getStatus() != null) existing.setStatus(lead.getStatus());
////        if (lead.getCourse() != null) existing.setCourse(lead.getCourse());
////        if (lead.getMessage() != null) existing.setMessage(lead.getMessage());
////        existing.setUpdatedAt(new Date());
////
////        String updateQuery = "UPDATE app_leads SET name=?, email=?, mobile_number=?, source=?, status=?, course=?, message=?, updated_at=? WHERE id=?";
////        try (Connection conn = getConnection();
////             PreparedStatement ps = conn.prepareStatement(updateQuery)) {
////
////            ps.setString(1, existing.getName());
////            ps.setString(2, existing.getEmail());
////            ps.setString(3, existing.getMobileNumber());
////            ps.setString(4, existing.getSource());
////            ps.setString(5, existing.getStatus());
////            ps.setString(6, existing.getCourse());
////            ps.setString(7, existing.getMessage());
////            ps.setTimestamp(8, new java.sql.Timestamp(existing.getUpdatedAt().getTime()));
////            ps.setLong(9, id);
////
////            ps.executeUpdate();
////        }
////        return existing;
////    }
////
////    // Delete Lead
////    public boolean deleteLead(Long id) throws SQLException {
////        String deleteQuery = "DELETE FROM app_leads WHERE id=?";
////        try (Connection conn = getConnection();
////             PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
////            ps.setLong(1, id);
////            return ps.executeUpdate() > 0;
////        }
////    }
////
////    // Get Lead by ID
////    public LeadApp getLeadById(Long id) throws SQLException {
////        String query = "SELECT * FROM app_leads WHERE id=?";
////        try (Connection conn = getConnection();
////             PreparedStatement ps = conn.prepareStatement(query)) {
////            ps.setLong(1, id);
////            ResultSet rs = ps.executeQuery();
////            if (!rs.next()) return null;
////
////            LeadApp lead = new LeadApp();
////            lead.setId(rs.getLong("id"));
////            lead.setBusinessNumber(rs.getLong("business_number"));
////            lead.setName(rs.getString("name"));
////            lead.setEmail(rs.getString("email"));
////            lead.setMobileNumber(rs.getString("mobile_number"));
////            lead.setSource(rs.getString("source"));
////            lead.setStatus(rs.getString("status"));
////            lead.setCourse(rs.getString("course"));
////            lead.setMessage(rs.getString("message"));
////            lead.setCreatedAt(rs.getDate("created_at"));
////            lead.setUpdatedAt(rs.getDate("updated_at"));
////            return lead;
////        }
////    }
////
////    // Get All Leads
////    public List<LeadApp> getAllLeads() throws SQLException {
////        String query = "SELECT * FROM app_leads ORDER BY business_number ASC";
////        List<LeadApp> leads = new ArrayList<>();
////        try (Connection conn = getConnection();
////             PreparedStatement ps = conn.prepareStatement(query);
////             ResultSet rs = ps.executeQuery()) {
////
////            while (rs.next()) {
////                LeadApp lead = new LeadApp();
////                lead.setId(rs.getLong("id"));
////                lead.setBusinessNumber(rs.getLong("business_number"));
////                lead.setName(rs.getString("name"));
////                lead.setEmail(rs.getString("email"));
////                lead.setMobileNumber(rs.getString("mobile_number"));
////                lead.setSource(rs.getString("source"));
////                lead.setStatus(rs.getString("status"));
////                lead.setCourse(rs.getString("course"));
////                lead.setMessage(rs.getString("message"));
////                lead.setCreatedAt(rs.getDate("created_at"));
////                lead.setUpdatedAt(rs.getDate("updated_at"));
////                leads.add(lead);
////            }
////        }
////        return leads;
////    }
////}
//
//package com.academy.management.dao;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//import org.springframework.stereotype.Repository;
//
//import com.academy.management.model.LeadApp;
//
//@Repository
//public class LeadAppDao {
//
//    // PostgreSQL connection
//    private Connection getConnection() throws SQLException {
//        try {
//            Class.forName("org.postgresql.Driver");
//        } catch (ClassNotFoundException e) {
//            throw new SQLException("PostgreSQL JDBC Driver not found", e);
//        }
//
//        return DriverManager.getConnection(
//                "jdbc:postgresql://localhost:5432/your_db_name", "username", "password");
//    }
//
//    // Generate next business number
//    private Long getNextBusinessNumber() throws SQLException {
//        String query = "SELECT COALESCE(MAX(business_number), 0) + 1 AS next_bn FROM app_leads";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(query);
//             ResultSet rs = ps.executeQuery()) {
//            if (rs.next()) return rs.getLong("next_bn");
//        }
//        return 1L;
//    }
//
//    // Save Lead (POST)
//    public LeadApp saveLead(LeadApp lead) throws SQLException {
//        String insertQuery = "INSERT INTO app_leads " +
//                "(business_number, name, email, mobile_number, source, status, course, message, created_at) " +
//                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
//
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(insertQuery, new String[]{"id"})) {
//
//            lead.setBusinessNumber(getNextBusinessNumber());
//
//            if (lead.getStatus() == null) lead.setStatus("NEW");
//            if (lead.getCreatedAt() == null) lead.setCreatedAt(new Date());
//
//            ps.setLong(1, lead.getBusinessNumber());
//            ps.setString(2, lead.getName());
//            ps.setString(3, lead.getEmail());
//            ps.setString(4, lead.getMobileNumber());
//            ps.setString(5, lead.getSource());
//            ps.setString(6, lead.getStatus());
//            ps.setString(7, lead.getCourse());
//            ps.setString(8, lead.getMessage());
//            ps.setTimestamp(9, new java.sql.Timestamp(lead.getCreatedAt().getTime()));
//
//            ps.executeUpdate();
//
//            try (ResultSet rsKeys = ps.getGeneratedKeys()) {
//                if (rsKeys.next()) lead.setId(rsKeys.getLong(1));
//            }
//
//            return lead;
//        }
//    }
//
//    // Full Update (PUT)
//    public LeadApp updateLead(Long id, LeadApp lead) throws SQLException {
//        String updateQuery = "UPDATE app_leads SET name=?, email=?, mobile_number=?, source=?, status=?, course=?, message=?, updated_at=? WHERE id=?";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(updateQuery)) {
//
//            if (lead.getStatus() == null) lead.setStatus("NEW");
//            lead.setUpdatedAt(new Date());
//
//            ps.setString(1, lead.getName());
//            ps.setString(2, lead.getEmail());
//            ps.setString(3, lead.getMobileNumber());
//            ps.setString(4, lead.getSource());
//            ps.setString(5, lead.getStatus());
//            ps.setString(6, lead.getCourse());
//            ps.setString(7, lead.getMessage());
//            ps.setTimestamp(8, new java.sql.Timestamp(lead.getUpdatedAt().getTime()));
//            ps.setLong(9, id);
//
//            ps.executeUpdate();
//        }
//        return lead;
//    }
//
//    // Partial Update (PATCH)
//    public LeadApp patchLead(Long id, LeadApp lead) throws SQLException {
//        LeadApp existing = getLeadById(id);
//        if (existing == null) return null;
//
//        if (lead.getName() != null) existing.setName(lead.getName());
//        if (lead.getEmail() != null) existing.setEmail(lead.getEmail());
//        if (lead.getMobileNumber() != null) existing.setMobileNumber(lead.getMobileNumber());
//        if (lead.getSource() != null) existing.setSource(lead.getSource());
//        if (lead.getStatus() != null) existing.setStatus(lead.getStatus());
//        if (lead.getCourse() != null) existing.setCourse(lead.getCourse());
//        if (lead.getMessage() != null) existing.setMessage(lead.getMessage());
//        existing.setUpdatedAt(new Date());
//
//        String updateQuery = "UPDATE app_leads SET name=?, email=?, mobile_number=?, source=?, status=?, course=?, message=?, updated_at=? WHERE id=?";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(updateQuery)) {
//
//            ps.setString(1, existing.getName());
//            ps.setString(2, existing.getEmail());
//            ps.setString(3, existing.getMobileNumber());
//            ps.setString(4, existing.getSource());
//            ps.setString(5, existing.getStatus());
//            ps.setString(6, existing.getCourse());
//            ps.setString(7, existing.getMessage());
//            ps.setTimestamp(8, new java.sql.Timestamp(existing.getUpdatedAt().getTime()));
//            ps.setLong(9, id);
//
//            ps.executeUpdate();
//        }
//        return existing;
//    }
//
//    // Delete Lead
//    public boolean deleteLead(Long id) throws SQLException {
//        String deleteQuery = "DELETE FROM app_leads WHERE id=?";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
//            ps.setLong(1, id);
//            return ps.executeUpdate() > 0;
//        }
//    }
//
//    // Get Lead by ID
//    public LeadApp getLeadById(Long id) throws SQLException {
//        String query = "SELECT * FROM app_leads WHERE id=?";
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(query)) {
//            ps.setLong(1, id);
//            ResultSet rs = ps.executeQuery();
//            if (!rs.next()) return null;
//
//            LeadApp lead = new LeadApp();
//            lead.setId(rs.getLong("id"));
//            lead.setBusinessNumber(rs.getLong("business_number"));
//            lead.setName(rs.getString("name"));
//            lead.setEmail(rs.getString("email"));
//            lead.setMobileNumber(rs.getString("mobile_number"));
//            lead.setSource(rs.getString("source"));
//            lead.setStatus(rs.getString("status"));
//            lead.setCourse(rs.getString("course"));
//            lead.setMessage(rs.getString("message"));
//            lead.setCreatedAt(rs.getDate("created_at"));
//            lead.setUpdatedAt(rs.getDate("updated_at"));
//            return lead;
//        }
//    }
//
//    // Get All Leads
//    public List<LeadApp> getAllLeads() throws SQLException {
//        String query = "SELECT * FROM app_leads ORDER BY business_number ASC";
//        List<LeadApp> leads = new ArrayList<>();
//        try (Connection conn = getConnection();
//             PreparedStatement ps = conn.prepareStatement(query);
//             ResultSet rs = ps.executeQuery()) {
//
//            while (rs.next()) {
//                LeadApp lead = new LeadApp();
//                lead.setId(rs.getLong("id"));
//                lead.setBusinessNumber(rs.getLong("business_number"));
//                lead.setName(rs.getString("name"));
//                lead.setEmail(rs.getString("email"));
//                lead.setMobileNumber(rs.getString("mobile_number"));
//                lead.setSource(rs.getString("source"));
//                lead.setStatus(rs.getString("status"));
//                lead.setCourse(rs.getString("course"));
//                lead.setMessage(rs.getString("message"));
//                lead.setCreatedAt(rs.getDate("created_at"));
//                lead.setUpdatedAt(rs.getDate("updated_at"));
//                leads.add(lead);
//            }
//        }
//        return leads;
//    }
//}
//

package com.academy.management.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.academy.management.model.LeadApp;

@Repository
public class LeadAppDao {

    // PostgreSQL connection
	@Autowired
	private DataSource dataSource;

	private Connection getConnection() throws SQLException {
	    return dataSource.getConnection();
	}


    // Generate next business number
    private Long getNextBusinessNumber() throws SQLException {
        String query = "SELECT COALESCE(MAX(business_number), 0) + 1 AS next_bn FROM app_leads";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getLong("next_bn");
        }
        return 1L;
    }

    // Save Lead (POST)
    public LeadApp saveLead(LeadApp lead) throws SQLException {
        String insertQuery = "INSERT INTO app_leads " +
                "(business_number, name, email, mobile_number, source, status, course, message, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(insertQuery, new String[]{"id"})) {

            lead.setBusinessNumber(getNextBusinessNumber());

            if (lead.getStatus() == null) lead.setStatus("NEW");
            if (lead.getCreatedAt() == null) lead.setCreatedAt(new Date());

            ps.setLong(1, lead.getBusinessNumber());
            ps.setString(2, lead.getName());
            ps.setString(3, lead.getEmail());
            ps.setString(4, lead.getMobileNumber());
            ps.setString(5, lead.getSource());
            ps.setString(6, lead.getStatus());
            ps.setString(7, lead.getCourse());
            ps.setString(8, lead.getMessage());
            ps.setTimestamp(9, new java.sql.Timestamp(lead.getCreatedAt().getTime()));

            ps.executeUpdate();

            try (ResultSet rsKeys = ps.getGeneratedKeys()) {
                if (rsKeys.next()) lead.setId(rsKeys.getLong(1));
            }

            return lead;
        }
    }

    // Full Update (PUT)
    public LeadApp updateLead(Long id, LeadApp lead) throws SQLException {
        String updateQuery = "UPDATE app_leads SET name=?, email=?, mobile_number=?, source=?, status=?, course=?, message=?, updated_at=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {

            if (lead.getStatus() == null) lead.setStatus("NEW");
            lead.setUpdatedAt(new Date());

            ps.setString(1, lead.getName());
            ps.setString(2, lead.getEmail());
            ps.setString(3, lead.getMobileNumber());
            ps.setString(4, lead.getSource());
            ps.setString(5, lead.getStatus());
            ps.setString(6, lead.getCourse());
            ps.setString(7, lead.getMessage());
            ps.setTimestamp(8, new java.sql.Timestamp(lead.getUpdatedAt().getTime()));
            ps.setLong(9, id);

            ps.executeUpdate();
        }
        return lead;
    }

    // Partial Update (PATCH)
    public LeadApp patchLead(Long id, LeadApp lead) throws SQLException {
        LeadApp existing = getLeadById(id);
        if (existing == null) return null;

        if (lead.getName() != null) existing.setName(lead.getName());
        if (lead.getEmail() != null) existing.setEmail(lead.getEmail());
        if (lead.getMobileNumber() != null) existing.setMobileNumber(lead.getMobileNumber());
        if (lead.getSource() != null) existing.setSource(lead.getSource());
        if (lead.getStatus() != null) existing.setStatus(lead.getStatus());
        if (lead.getCourse() != null) existing.setCourse(lead.getCourse());
        if (lead.getMessage() != null) existing.setMessage(lead.getMessage());
        existing.setUpdatedAt(new Date());

        String updateQuery = "UPDATE app_leads SET name=?, email=?, mobile_number=?, source=?, status=?, course=?, message=?, updated_at=? WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(updateQuery)) {

            ps.setString(1, existing.getName());
            ps.setString(2, existing.getEmail());
            ps.setString(3, existing.getMobileNumber());
            ps.setString(4, existing.getSource());
            ps.setString(5, existing.getStatus());
            ps.setString(6, existing.getCourse());
            ps.setString(7, existing.getMessage());
            ps.setTimestamp(8, new java.sql.Timestamp(existing.getUpdatedAt().getTime()));
            ps.setLong(9, id);

            ps.executeUpdate();
        }
        return existing;
    }

    // Delete Lead
    public boolean deleteLead(Long id) throws SQLException {
        String deleteQuery = "DELETE FROM app_leads WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(deleteQuery)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    // Get Lead by ID
    public LeadApp getLeadById(Long id) throws SQLException {
        String query = "SELECT * FROM app_leads WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;

            LeadApp lead = new LeadApp();
            lead.setId(rs.getLong("id"));
            lead.setBusinessNumber(rs.getLong("business_number"));
            lead.setName(rs.getString("name"));
            lead.setEmail(rs.getString("email"));
            lead.setMobileNumber(rs.getString("mobile_number"));
            lead.setSource(rs.getString("source"));
            lead.setStatus(rs.getString("status"));
            lead.setCourse(rs.getString("course"));
            lead.setMessage(rs.getString("message"));
            lead.setCreatedAt(rs.getDate("created_at"));
            lead.setUpdatedAt(rs.getDate("updated_at"));
            return lead;
        }
    }

    // Get All Leads
    public List<LeadApp> getAllLeads() throws SQLException {
        String query = "SELECT * FROM public.app_leads ORDER BY business_number ASC";
        List<LeadApp> leads = new ArrayList<>();
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                LeadApp lead = new LeadApp();
                lead.setId(rs.getLong("id"));
                lead.setBusinessNumber(rs.getLong("business_number"));
                lead.setName(rs.getString("name"));
                lead.setEmail(rs.getString("email"));
                lead.setMobileNumber(rs.getString("mobile_number"));
                lead.setSource(rs.getString("source"));
                lead.setStatus(rs.getString("status"));
                lead.setCourse(rs.getString("course"));
                lead.setMessage(rs.getString("message"));
                lead.setCreatedAt(rs.getDate("created_at"));
                lead.setUpdatedAt(rs.getDate("updated_at"));
                leads.add(lead);
            }
        }
        return leads;
    }
}
