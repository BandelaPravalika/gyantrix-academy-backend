package com.academy.management.service;

import com.academy.management.dao.BatchAppDao;
import com.academy.management.model.BatchApp;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BatchAppService {

    private final BatchAppDao batchAppDao;

    public BatchAppService(BatchAppDao batchAppDao) {
        this.batchAppDao = batchAppDao;
    }

    public List<BatchApp> getAllBatches() {
        return batchAppDao.findAll();
    }

    public BatchApp getBatchById(Long id) {
        return batchAppDao.findById(id);
    }

    public BatchApp createBatch(BatchApp batch) {
        return batchAppDao.save(batch);
    }

    public BatchApp updateBatch(BatchApp batch) {
        return batchAppDao.update(batch);
    }

    public BatchApp patchBatch(BatchApp batch) {
        return batchAppDao.patch(batch);
    }

    public boolean deleteBatch(Long id) {
        return batchAppDao.delete(id); // DAO returns true/false
    }

}
