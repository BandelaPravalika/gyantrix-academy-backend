package com.academy.management.model;

import java.util.Date;

public class PermissionApp {
    private Long id;
    private Long businessNumber;
    private Long userId;
    private String permissionKey;
    private String status = "ENABLED"; // default
    private Date createdAt;
    private Date updatedAt;

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBusinessNumber() { return businessNumber; }
    public void setBusinessNumber(Long businessNumber) { this.businessNumber = businessNumber; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getPermissionKey() { return permissionKey; }
    public void setPermissionKey(String permissionKey) { this.permissionKey = permissionKey; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }
}
