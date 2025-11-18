package com.academy.management.service;

import com.academy.management.dao.LeadAppDao;
import com.academy.management.model.LeadApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class LeadAppService {

    @Autowired
    private LeadAppDao leadAppDao;

    public LeadApp createLead(LeadApp lead) throws SQLException { return leadAppDao.saveLead(lead); }
    public LeadApp updateLead(Long id, LeadApp lead) throws SQLException { return leadAppDao.updateLead(id, lead); }
    public LeadApp patchLead(Long id, LeadApp lead) throws SQLException { return leadAppDao.patchLead(id, lead); }
    public boolean deleteLead(Long id) throws SQLException { return leadAppDao.deleteLead(id); }
    public LeadApp getLeadById(Long id) throws SQLException { return leadAppDao.getLeadById(id); }
    public List<LeadApp> getAllLeads() throws SQLException { return leadAppDao.getAllLeads(); }
}
