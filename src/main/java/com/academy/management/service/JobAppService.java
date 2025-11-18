package com.academy.management.service;

import com.academy.management.dao.JobAppDao;
import com.academy.management.model.JobApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class JobAppService {

    @Autowired
    private JobAppDao dao;

    public String addJob(JobApp job) throws SQLException {
        return dao.addJob(job);
    }

    public List<JobApp> getAllJobs() throws SQLException {
        return dao.getAllJobs();
    }

    public JobApp getJobById(int id) throws SQLException {
        return dao.getJobById(id);
    }

    public String updateJob(int id, JobApp job) throws SQLException {
        return dao.updateJob(id, job);
    }

    public String patchJobStatus(int id, String status) throws SQLException {
        return dao.patchJobStatus(id, status);
    }

    public boolean deleteJob(int id) throws SQLException {
        return dao.deleteJob(id);
    }
}
