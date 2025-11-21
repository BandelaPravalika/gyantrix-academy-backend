package com.academy.management.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.academy.management.model.LeadApp;
import com.academy.management.service.LeadAppService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/leads")
public class LeadAppController {

    @Autowired
    private LeadAppService leadAppService;

    @PostMapping
    public ResponseEntity<LeadApp> createLead(@RequestBody LeadApp lead) {
        try { return ResponseEntity.ok(leadAppService.createLead(lead)); }
        catch (SQLException e) { e.printStackTrace(); return ResponseEntity.status(500).build(); }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LeadApp> updateLead(@PathVariable Long id, @RequestBody LeadApp lead) {
        try {
            LeadApp updated = leadAppService.updateLead(id, lead);
            if (updated == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(updated);
        } catch (SQLException e) { e.printStackTrace(); return ResponseEntity.status(500).build(); }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LeadApp> patchLead(@PathVariable Long id, @RequestBody LeadApp lead) {
        try {
            LeadApp patched = leadAppService.patchLead(id, lead);
            if (patched == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(patched);
        } catch (SQLException e) { e.printStackTrace(); return ResponseEntity.status(500).build(); }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteLead(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            boolean deleted = leadAppService.deleteLead(id);
            if (deleted) {
                response.put("message", "Lead with ID " + id + " deleted successfully.");
                return ResponseEntity.ok(response);
            } else {
                response.put("message", "Lead with ID " + id + " not found.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            response.put("message", "Error occurred while deleting lead: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LeadApp> getLeadById(@PathVariable Long id) {
        try {
            LeadApp lead = leadAppService.getLeadById(id);
            if (lead == null) return ResponseEntity.notFound().build();
            return ResponseEntity.ok(lead);
        } catch (SQLException e) { e.printStackTrace(); return ResponseEntity.status(500).build(); }
    }

    @GetMapping
    public ResponseEntity<List<LeadApp>> getAllLeads() {
        try { return ResponseEntity.ok(leadAppService.getAllLeads()); }
        catch (SQLException e) { e.printStackTrace(); return ResponseEntity.status(500).build(); }
    }
    @GetMapping("/test")
    public String test() {
        return "API is working!";
    }

    @Autowired
    private DataSource dataSource;

    @GetMapping("/db-test")
    public String dbTest() {
        try (Connection conn = dataSource.getConnection()) {
            return "DB connected: " + conn.getCatalog();
        } catch (Exception e) {
            return "DB connection failed: " + e.getMessage();
        }
    }
    
    @GetMapping("/db-test-lead")
    public String testLead() {
        String sql = "SELECT COUNT(*) AS total FROM public.app_leads"; // correct schema + table
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return "Total rows in app_leads = " + rs.getInt("total");
            }
            return "Query executed but no rows found.";
        } catch (Exception e) {
            return "DB ERROR: " + e.getMessage();
        }
    }




} 