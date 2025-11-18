package com.academy.management.controller;

import com.academy.management.model.EnrollmentApp;
import com.academy.management.service.EnrollmentAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@CrossOrigin(origins = "*")
public class EnrollmentAppController {

    private final EnrollmentAppService service;

    public EnrollmentAppController(EnrollmentAppService service) {
        this.service = service;
    }

    @GetMapping
    public List<EnrollmentApp> getAll() {
        return service.getAllEnrollments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnrollmentApp> getById(@PathVariable Long id) {
        EnrollmentApp enrollment = service.getEnrollmentById(id);
        return enrollment != null ? ResponseEntity.ok(enrollment) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<EnrollmentApp> create(@RequestBody EnrollmentApp enrollment) {
        EnrollmentApp created = service.createEnrollment(enrollment);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EnrollmentApp> update(@PathVariable Long id, @RequestBody EnrollmentApp enrollment) {
        enrollment.setId(id);
        EnrollmentApp updated = service.updateEnrollment(enrollment);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EnrollmentApp> patch(@PathVariable Long id, @RequestBody EnrollmentApp enrollment) {
        enrollment.setId(id);
        EnrollmentApp patched = service.patchEnrollment(enrollment);
        return patched != null ? ResponseEntity.ok(patched) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean deleted = service.deleteEnrollment(id);
        return deleted ? ResponseEntity.ok("Deleted successfully") : ResponseEntity.notFound().build();
    }
}
