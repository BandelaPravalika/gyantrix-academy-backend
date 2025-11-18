package com.academy.management.model;

import java.util.Date;

public class LeadApp {

    private Long id; // Primary key
    private Long businessNumber; // Auto incremented
    private String name;
    private String email;        // NOT NULL, UNIQUE
    private String mobileNumber; // NOT NULL, UNIQUE
    private String source;
    private String status = "NEW"; // default
    private String course; // optional, nullable
    private String message; // optional, nullable
    private Date createdAt;
    private Date updatedAt;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBusinessNumber() { return businessNumber; }
    public void setBusinessNumber(Long businessNumber) { this.businessNumber = businessNumber; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
