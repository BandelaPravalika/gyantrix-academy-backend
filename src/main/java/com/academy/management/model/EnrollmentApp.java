package com.academy.management.model;

import java.util.Date;

public class EnrollmentApp {
    private Long id;
    private Long businessNumber;
    private Long studentId;
    private Long courseId;
    private Double progress = 0.0;
    private Date enrolledDate;
    private String status = "ENROLLED";
    private Date createdAt;
    private Date updatedAt;

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBusinessNumber() { return businessNumber; }
    public void setBusinessNumber(Long businessNumber) { this.businessNumber = businessNumber; }

    public Long getStudentId() { return studentId; }
    public void setStudentId(Long studentId) { this.studentId = studentId; }

    public Long getCourseId() { return courseId; }
    public void setCourseId(Long courseId) { this.courseId = courseId; }

    public Double getProgress() { return progress; }
    public void setProgress(Double progress) { this.progress = progress; }

    public Date getEnrolledDate() { return enrolledDate; }
    public void setEnrolledDate(Date enrolledDate) { this.enrolledDate = enrolledDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
