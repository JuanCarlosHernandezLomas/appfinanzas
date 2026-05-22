package com.appfinanzas.appfinanzas_api.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class BudgetCycleResponse {

    private Long id;
    private Long userId;
    private String userName;
    private String type;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean active;
    private Long clonedFromCycleId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BudgetCycleResponse() {
    }

    public BudgetCycleResponse(Long id,
                               Long userId,
                               String userName,
                               String type,
                               LocalDate startDate,
                               LocalDate endDate,
                               Boolean active,
                               Long clonedFromCycleId,
                               LocalDateTime createdAt,
                               LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.active = active;
        this.clonedFromCycleId = clonedFromCycleId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Long getClonedFromCycleId() {
        return clonedFromCycleId;
    }

    public void setClonedFromCycleId(Long clonedFromCycleId) {
        this.clonedFromCycleId = clonedFromCycleId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}