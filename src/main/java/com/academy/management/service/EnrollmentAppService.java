package com.academy.management.service;

import com.academy.management.dao.EnrollmentAppDao;
import com.academy.management.model.EnrollmentApp;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

@Service
public class EnrollmentAppService {

    private final EnrollmentAppDao dao;

    public EnrollmentAppService(EnrollmentAppDao dao) {
        this.dao = dao;
    }

    public List<EnrollmentApp> getAllEnrollments() {
        try { return dao.findAll(); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public EnrollmentApp getEnrollmentById(Long id) {
        try { return dao.findById(id); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    public EnrollmentApp createEnrollment(EnrollmentApp e) {
        assignDefaultValues(e);
        try { return dao.save(e); }
        catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    public EnrollmentApp updateEnrollment(EnrollmentApp e) {
        assignDefaultValues(e);
        try { return dao.update(e); }
        catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    public EnrollmentApp patchEnrollment(EnrollmentApp e) {
        try { return dao.patch(e); }
        catch (SQLException ex) { throw new RuntimeException(ex); }
    }

    public boolean deleteEnrollment(Long id) {
        try { return dao.delete(id); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

    // Assign default values if null
    private void assignDefaultValues(EnrollmentApp e) {
        if (e.getProgress() == null) e.setProgress(0.0);
        if (e.getStatus() == null) e.setStatus("ENROLLED");
        if (e.getEnrolledDate() == null) e.setEnrolledDate(new Date());
    }
}
