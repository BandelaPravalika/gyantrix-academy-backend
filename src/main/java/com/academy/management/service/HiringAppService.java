package com.academy.management.service;

import com.academy.management.dao.HiringAppDao;
import com.academy.management.model.HiringApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.sql.SQLException;
import java.util.List;

@Service
public class HiringAppService {

    @Autowired
    private HiringAppDao dao;

    public String addHiring(HiringApp hiring) throws SQLException {
        return dao.addHiring(hiring);
    }

    public List<HiringApp> getAllHirings() throws SQLException {
        return dao.getAllHirings();
    }

    public HiringApp getHiringById(int id) throws SQLException {
        return dao.getHiringById(id);
    }

    public String updateHiring(int id, HiringApp hiring) throws SQLException {
        return dao.updateHiring(id, hiring);
    }
    
    public String patchHiring(int id, HiringApp hiring) throws SQLException {
        return dao.patchHiring(id, hiring);
    }


    public boolean deleteHiring(int id) throws SQLException {
        return dao.deleteHiring(id);
    }
}
