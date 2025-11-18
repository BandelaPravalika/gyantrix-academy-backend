package com.academy.management.controller;

import com.academy.management.model.JobApp;
import com.academy.management.service.JobAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/jobs")
public class JobAppController {

    @Autowired
    private JobAppService service;

    @PostMapping
    public ResponseEntity<String> addJob(@RequestBody JobApp job) {
        try {
            String result = service.addJob(job);
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding job: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<JobApp>> getAllJobs() {
        try {
            List<JobApp> jobs = service.getAllJobs();
            return ResponseEntity.ok(jobs);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApp> getJobById(@PathVariable int id) {
        try {
            JobApp job = service.getJobById(id);
            if (job != null) return ResponseEntity.ok(job);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateJob(@PathVariable int id, @RequestBody JobApp job) {
        try {
            String result = service.updateJob(id, job);
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating job: " + e.getMessage());
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<String> patchJobStatus(@PathVariable int id, @RequestBody JobApp job) {
        try {
            String result = service.patchJobStatus(id, job.getStatus());
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating job status: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteJob(@PathVariable int id) {
        try {
            boolean deleted = service.deleteJob(id);
            return deleted ? ResponseEntity.ok("Job deleted successfully!")
                    : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Job not found!");
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting job: " + e.getMessage());
        }
    }
}
