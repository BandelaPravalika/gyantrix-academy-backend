package com.academy.management.model;

import java.sql.Date;

public class HiringApp {

    private int hiringId;
    private int businessNumber;
    private String email;
    private String title;
    private String description;
    private String domain;
    private String location;
    private Date deadline;
    private Date postedDate; // should map to SYSDATE if not provided
    private String salary;
    private String educationDetails;
    private String experience;
    private String skills;
    private int shortlistedCount = 0;
    private int applicationCount = 0;

    // Getters and Setters
    public int getHiringId() { return hiringId; }
    public void setHiringId(int hiringId) { this.hiringId = hiringId; }

    public int getBusinessNumber() { return businessNumber; }
    public void setBusinessNumber(int businessNumber) { this.businessNumber = businessNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDomain() { return domain; }
    public void setDomain(String domain) { this.domain = domain; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Date getDeadline() { return deadline; }
    public void setDeadline(Date deadline) { this.deadline = deadline; }

    public Date getPostedDate() { return postedDate; }
    public void setPostedDate(Date postedDate) { this.postedDate = postedDate; }

    public String getSalary() { return salary; }
    public void setSalary(String salary) { this.salary = salary; }

    public String getEducationDetails() { return educationDetails; }
    public void setEducationDetails(String educationDetails) { this.educationDetails = educationDetails; }

    public String getExperience() { return experience; }
    public void setExperience(String experience) { this.experience = experience; }

    public String getSkills() { return skills; }
    public void setSkills(String skills) { this.skills = skills; }

    public int getShortlistedCount() { return shortlistedCount; }
    public void setShortlistedCount(int shortlistedCount) { this.shortlistedCount = shortlistedCount; }

    public int getApplicationCount() { return applicationCount; }
    public void setApplicationCount(int applicationCount) { this.applicationCount = applicationCount; }
}
