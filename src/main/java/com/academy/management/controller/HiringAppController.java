package com.academy.management.controller;

import java.sql.SQLException;
import java.util.List;

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

import com.academy.management.model.HiringApp;
import com.academy.management.service.HiringAppService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/hiring")
public class HiringAppController {

    @Autowired
    private HiringAppService service;

    @PostMapping
    public String addHiring(@RequestBody HiringApp hiring) throws SQLException {
        return service.addHiring(hiring);
    }

    @GetMapping
    public List<HiringApp> getAllHirings() throws SQLException {
        return service.getAllHirings();
    }

    @GetMapping("/{id}")
    public HiringApp getHiringById(@PathVariable int id) throws SQLException {
        return service.getHiringById(id);
    }

    @PutMapping("/{id}")
    public String updateHiring(@PathVariable int id, @RequestBody HiringApp hiring) throws SQLException {
        return service.updateHiring(id, hiring);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<String> patchHiring(@PathVariable int id, @RequestBody HiringApp hiring) {
        try {
            String result = service.patchHiring(id, hiring);
            return ResponseEntity.ok(result);
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error patching hiring: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public boolean deleteHiring(@PathVariable int id) throws SQLException {
        return service.deleteHiring(id);
    }
}
