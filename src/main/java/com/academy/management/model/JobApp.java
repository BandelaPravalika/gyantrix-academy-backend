package com.academy.management.model;

import java.sql.Date;

public class JobApp {
    private int jobId;
    private int businessNumber;
    private int hiringId;
    private String name;
    private String email;
    private String mobileNumber;
    private String resume;
    private Date appliedDate;
    private Date updatedDate;
    private String status = "Applied";

    // Getters and Setters
    public int getJobId() { return jobId; }
    public void setJobId(int jobId) { this.jobId = jobId; }

    public int getBusinessNumber() { return businessNumber; }
    public void setBusinessNumber(int businessNumber) { this.businessNumber = businessNumber; }

    public int getHiringId() { return hiringId; }
    public void setHiringId(int hiringId) { this.hiringId = hiringId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getResume() { return resume; }
    public void setResume(String resume) { this.resume = resume; }

    public Date getAppliedDate() { return appliedDate; }
    public void setAppliedDate(Date appliedDate) { this.appliedDate = appliedDate; }

    public Date getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(Date updatedDate) { this.updatedDate = updatedDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
