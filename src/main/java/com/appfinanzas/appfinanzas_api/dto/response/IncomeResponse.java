package com.appfinanzas.appfinanzas_api.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class IncomeResponse {

    private Long id;

    private Long userId;
    private String userName;

    private Long budgetCycleId;
    private String budgetCycleType;

    private Long incomeCategoryId;
    private String incomeCategoryName;

    private String name;
    private String type;
    private BigDecimal amount;
    private LocalDate incomeDate;
    private Boolean recurring;
    private String observations;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public IncomeResponse() {
    }

    public IncomeResponse(Long id,
                          Long userId,
                          String userName,
                          Long budgetCycleId,
                          String budgetCycleType,
                          Long incomeCategoryId,
                          String incomeCategoryName,
                          String name,
                          String type,
                          BigDecimal amount,
                          LocalDate incomeDate,
                          Boolean recurring,
                          String observations,
                          Boolean active,
                          LocalDateTime createdAt,
                          LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.budgetCycleId = budgetCycleId;
        this.budgetCycleType = budgetCycleType;
        this.incomeCategoryId = incomeCategoryId;
        this.incomeCategoryName = incomeCategoryName;
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.incomeDate = incomeDate;
        this.recurring = recurring;
        this.observations = observations;
        this.active = active;
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

    public Long getBudgetCycleId() {
        return budgetCycleId;
    }

    public void setBudgetCycleId(Long budgetCycleId) {
        this.budgetCycleId = budgetCycleId;
    }

    public String getBudgetCycleType() {
        return budgetCycleType;
    }

    public void setBudgetCycleType(String budgetCycleType) {
        this.budgetCycleType = budgetCycleType;
    }

    public Long getIncomeCategoryId() {
        return incomeCategoryId;
    }

    public void setIncomeCategoryId(Long incomeCategoryId) {
        this.incomeCategoryId = incomeCategoryId;
    }

    public String getIncomeCategoryName() {
        return incomeCategoryName;
    }

    public void setIncomeCategoryName(String incomeCategoryName) {
        this.incomeCategoryName = incomeCategoryName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDate getIncomeDate() {
        return incomeDate;
    }

    public void setIncomeDate(LocalDate incomeDate) {
        this.incomeDate = incomeDate;
    }

    public Boolean getRecurring() {
        return recurring;
    }

    public void setRecurring(Boolean recurring) {
        this.recurring = recurring;
    }

    public String getObservations() {
        return observations;
    }

    public void setObservations(String observations) {
        this.observations = observations;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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