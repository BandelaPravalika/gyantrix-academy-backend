package com.academy.management.dao;

import com.academy.management.model.JobApp;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JobAppDao {

    private static final String URL = "jdbc:oracle:thin:@localhost:1521:orcl";
    private static final String USER = "C##Pravalika";
    private static final String PASSWORD = "1234";

    // Get next business_number starting from 1
    private int getNextBusinessNumber(Connection conn) throws SQLException {
        String query = "SELECT NVL(MAX(business_number), 0) + 1 AS next_number FROM app_jobs";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) return rs.getInt("next_number");
        }
        return 1;
    }

    // POST - Add job application
    public String addJob(JobApp job) throws SQLException {
        String query = "INSERT INTO app_jobs (business_number, hiring_id, name, email, mobile_number, resume, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            int nextBusinessNumber = getNextBusinessNumber(conn);

            ps.setInt(1, nextBusinessNumber);
            ps.setInt(2, job.getHiringId());
            ps.setString(3, job.getName());
            ps.setString(4, job.getEmail());
            ps.setString(5, job.getMobileNumber());
            ps.setString(6, job.getResume());
            ps.setString(7, job.getStatus() == null ? "Applied" : job.getStatus());

            int rows = ps.executeUpdate();
            return rows > 0 ? "Job application added successfully. Business number: " + nextBusinessNumber
                    : "Failed to add job application.";
        }
    }

    // GET - all job applications
    public List<JobApp> getAllJobs() throws SQLException {
        List<JobApp> list = new ArrayList<>();
        String query = "SELECT * FROM app_jobs ORDER BY job_id ASC";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                JobApp job = new JobApp();
                job.setJobId(rs.getInt("job_id"));
                job.setBusinessNumber(rs.getInt("business_number"));
                job.setHiringId(rs.getInt("hiring_id"));
                job.setName(rs.getString("name"));
                job.setEmail(rs.getString("email"));
                job.setMobileNumber(rs.getString("mobile_number"));
                job.setResume(rs.getString("resume"));
                job.setAppliedDate(rs.getDate("applied_date"));
                job.setUpdatedDate(rs.getDate("updated_date"));
                job.setStatus(rs.getString("status"));
                list.add(job);
            }
        }
        return list;
    }

    // GET by job_id
    public JobApp getJobById(int id) throws SQLException {
        String query = "SELECT * FROM app_jobs WHERE job_id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                JobApp job = new JobApp();
                job.setJobId(rs.getInt("job_id"));
                job.setBusinessNumber(rs.getInt("business_number"));
                job.setHiringId(rs.getInt("hiring_id"));
                job.setName(rs.getString("name"));
                job.setEmail(rs.getString("email"));
                job.setMobileNumber(rs.getString("mobile_number"));
                job.setResume(rs.getString("resume"));
                job.setAppliedDate(rs.getDate("applied_date"));
                job.setUpdatedDate(rs.getDate("updated_date"));
                job.setStatus(rs.getString("status"));
                return job;
            }
        }
        return null;
    }

    // PUT - update entire job application
    public String updateJob(int id, JobApp job) throws SQLException {
        String query = "UPDATE app_jobs SET hiring_id=?, name=?, email=?, mobile_number=?, resume=?, status=?, updated_date=SYSDATE WHERE job_id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, job.getHiringId());
            ps.setString(2, job.getName());
            ps.setString(3, job.getEmail());
            ps.setString(4, job.getMobileNumber());
            ps.setString(5, job.getResume());
            ps.setString(6, job.getStatus());
            ps.setInt(7, id);

            int rows = ps.executeUpdate();
            return rows > 0 ? "Job application updated successfully!" : "Job application not found!";
        }
    }

    // PATCH - update status only
    public String patchJobStatus(int id, String status) throws SQLException {
        String query = "UPDATE app_jobs SET status=?, updated_date=SYSDATE WHERE job_id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, status);
            ps.setInt(2, id);

            int rows = ps.executeUpdate();
            return rows > 0 ? "Job status updated successfully!" : "Job application not found!";
        }
    }

    // DELETE
    public boolean deleteJob(int id) throws SQLException {
        String query = "DELETE FROM app_jobs WHERE job_id=?";
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
