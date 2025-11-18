package com.academy.management.dao;

import com.academy.management.model.BatchApp;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BatchAppDao {

    private final JdbcTemplate jdbcTemplate;

    public BatchAppDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<BatchApp> rowMapper = (ResultSet rs, int rowNum) -> {
        BatchApp batch = new BatchApp();
        batch.setId(rs.getLong("id"));
        batch.setBusinessNumber(rs.getLong("business_number"));
        batch.setCourseId(rs.getLong("course_id"));
        batch.setBatchName(rs.getString("batch_name"));
        batch.setStartDate(rs.getDate("start_date"));
        batch.setEndDate(rs.getDate("end_date"));
        batch.setStatus(rs.getString("status"));
        batch.setCreatedAt(rs.getDate("created_at"));
        batch.setUpdatedAt(rs.getDate("updated_at"));
        return batch;
    };

    // Generate next business number
    private long getNextBusinessNumber() {
        String sql = "SELECT NVL(MAX(business_number), 0) + 1 FROM APP_BATCHES";
        return jdbcTemplate.queryForObject(sql, Long.class);
    }

    // Get all batches
    public List<BatchApp> findAll() {
        String sql = "SELECT * FROM app_batches ORDER BY id ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // Get batch by ID
    public BatchApp findById(Long id) {
        String sql = "SELECT * FROM app_batches WHERE id = ?";
        return jdbcTemplate.queryForObject(sql, new Object[]{id}, rowMapper);
    }

    // Create batch with auto businessNumber
    public BatchApp save(BatchApp batch) {
        if (batch.getBusinessNumber() == null) {
            batch.setBusinessNumber(getNextBusinessNumber());
        }

        boolean hasStartDate = batch.getStartDate() != null;
        boolean hasEndDate = batch.getEndDate() != null;

        String sql;
        Object[] params;

        if (hasStartDate && hasEndDate) {
            sql = "INSERT INTO app_batches (business_number, course_id, batch_name, start_date, end_date, status) VALUES (?, ?, ?, ?, ?, ?)";
            params = new Object[]{
                    batch.getBusinessNumber(),
                    batch.getCourseId(),
                    batch.getBatchName(),
                    new java.sql.Date(batch.getStartDate().getTime()),
                    new java.sql.Date(batch.getEndDate().getTime()),
                    batch.getStatus() != null ? batch.getStatus() : "ACTIVE"
            };
        } else if (hasStartDate) {
            sql = "INSERT INTO app_batches (business_number, course_id, batch_name, start_date, status) VALUES (?, ?, ?, ?, ?)";
            params = new Object[]{
                    batch.getBusinessNumber(),
                    batch.getCourseId(),
                    batch.getBatchName(),
                    new java.sql.Date(batch.getStartDate().getTime()),
                    batch.getStatus() != null ? batch.getStatus() : "ACTIVE"
            };
        } else if (hasEndDate) {
            sql = "INSERT INTO app_batches (business_number, course_id, batch_name, end_date, status) VALUES (?, ?, ?, ?, ?)";
            params = new Object[]{
                    batch.getBusinessNumber(),
                    batch.getCourseId(),
                    batch.getBatchName(),
                    new java.sql.Date(batch.getEndDate().getTime()),
                    batch.getStatus() != null ? batch.getStatus() : "ACTIVE"
            };
        } else {
            sql = "INSERT INTO app_batches (business_number, course_id, batch_name, status) VALUES (?, ?, ?, ?)";
            params = new Object[]{
                    batch.getBusinessNumber(),
                    batch.getCourseId(),
                    batch.getBatchName(),
                    batch.getStatus() != null ? batch.getStatus() : "ACTIVE"
            };
        }

        jdbcTemplate.update(sql, params);
        return batch;
    }


    // Update batch (PUT-like)
    public BatchApp update(BatchApp batch) {
        BatchApp existing = findById(batch.getId());

        String sql = "UPDATE app_batches SET course_id=?, batch_name=?, start_date=?, end_date=?, status=?, updated_at=SYSDATE WHERE id=?";
        jdbcTemplate.update(sql,
                batch.getCourseId() != null ? batch.getCourseId() : existing.getCourseId(),
                batch.getBatchName() != null ? batch.getBatchName() : existing.getBatchName(),
                batch.getStartDate() != null ? new java.sql.Date(batch.getStartDate().getTime()) : existing.getStartDate(),
                batch.getEndDate() != null ? new java.sql.Date(batch.getEndDate().getTime()) : existing.getEndDate(),
                batch.getStatus() != null ? batch.getStatus() : existing.getStatus(),
                batch.getId()
        );
        return findById(batch.getId());
    }

    // Partial update (PATCH)
    public BatchApp patch(BatchApp batch) {
        BatchApp existing = findById(batch.getId());

        if(batch.getCourseId() != null) existing.setCourseId(batch.getCourseId());
        if(batch.getBatchName() != null) existing.setBatchName(batch.getBatchName());
        if(batch.getStartDate() != null) existing.setStartDate(batch.getStartDate());
        if(batch.getEndDate() != null) existing.setEndDate(batch.getEndDate());
        if(batch.getStatus() != null) existing.setStatus(batch.getStatus());

        String sql = "UPDATE app_batches SET course_id=?, batch_name=?, start_date=?, end_date=?, status=?, updated_at=SYSDATE WHERE id=?";
        jdbcTemplate.update(sql,
                existing.getCourseId(),
                existing.getBatchName(),
                existing.getStartDate() != null ? new java.sql.Date(existing.getStartDate().getTime()) : null,
                existing.getEndDate() != null ? new java.sql.Date(existing.getEndDate().getTime()) : null,
                existing.getStatus(),
                existing.getId()
        );

        return findById(existing.getId());
    }

    // Delete batch

    public boolean delete(Long id) {
        String sql = "DELETE FROM app_batches WHERE id = ?";
        int rowsAffected = jdbcTemplate.update(sql, id);
        return rowsAffected > 0; // true if a record was deleted
    }

}
