package com.academy.management.controller;

import com.academy.management.model.BatchApp;
import com.academy.management.service.BatchAppService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
public class BatchAppController {

    private final BatchAppService batchAppService;

    public BatchAppController(BatchAppService batchAppService) {
        this.batchAppService = batchAppService;
    }

    @GetMapping
    public ResponseEntity<List<BatchApp>> getAllBatches() {
        return ResponseEntity.ok(batchAppService.getAllBatches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BatchApp> getBatchById(@PathVariable Long id) {
        return ResponseEntity.ok(batchAppService.getBatchById(id));
    }

    @PostMapping
    public ResponseEntity<BatchApp> createBatch(@RequestBody BatchApp batch) {
        return ResponseEntity.ok(batchAppService.createBatch(batch));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BatchApp> updateBatch(@PathVariable Long id, @RequestBody BatchApp batch) {
        batch.setId(id);
        return ResponseEntity.ok(batchAppService.updateBatch(batch));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BatchApp> patchBatch(@PathVariable Long id, @RequestBody BatchApp batch) {
        batch.setId(id);
        return ResponseEntity.ok(batchAppService.patchBatch(batch));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBatch(@PathVariable Long id) {
        boolean deleted = batchAppService.deleteBatch(id); // call service
        if (deleted) {
            return ResponseEntity.ok("Batch deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Batch not found");
        }
    }

}
