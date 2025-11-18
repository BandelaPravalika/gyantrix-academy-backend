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

import com.academy.management.model.FeeApp;
import com.academy.management.service.FeeAppService;

@RestController
@RequestMapping("/api/fees")
@CrossOrigin(origins = "*")
public class FeeAppController {

    @Autowired
    private FeeAppService service;

    @PostMapping
    public FeeApp createFee(@RequestBody FeeApp fee) throws SQLException {
        return service.createFee(fee);
    }

    @PutMapping("/{id}")
    public FeeApp updateFee(@PathVariable Long id, @RequestBody FeeApp fee) throws SQLException {
        return service.updateFee(id, fee);
    }
 // Partial update (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<String> patchFee(@PathVariable Long id, @RequestBody FeeApp fee) {
        try {
            String message = service.patchFee(id, fee);
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error patching fee: " + e.getMessage());
        }
    }


    @GetMapping
    public List<FeeApp> getAllFees() throws SQLException {
        return service.getAllFees();
    }

    @GetMapping("/{id}")
    public FeeApp getFeeById(@PathVariable Long id) throws SQLException {
        return service.getFeeById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFee(@PathVariable Long id) {
        try {
            // Capture the message returned by the service
            String message = service.deleteFee(id);  
            return ResponseEntity.ok(message);  // return it in the API response
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error deleting fee: " + e.getMessage());
        }
    }

}
