package com.academy.management.service;

import com.academy.management.dao.FeeAppDao;
import com.academy.management.model.FeeApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class FeeAppService {

    @Autowired
    private FeeAppDao dao;

    public FeeApp createFee(FeeApp fee) throws SQLException {
        return dao.save(fee);
    }

    public FeeApp updateFee(Long id, FeeApp fee) throws SQLException {
        return dao.update(id, fee);
    }

    public List<FeeApp> getAllFees() throws SQLException {
        return dao.getAll();
    }

    public FeeApp getFeeById(Long id) throws SQLException {
        return dao.getById(id);
    }
 // Patch Fee
    public String patchFee(Long id, FeeApp fee) {
        return dao.patch(id, fee);
    }


    public String deleteFee(Long id) {
        try {
            return dao.delete(id); // DAO may throw SQLException
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting fee: " + e.getMessage(), e);
        }
    }
}
