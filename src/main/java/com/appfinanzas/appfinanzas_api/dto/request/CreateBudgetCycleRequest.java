package com.appfinanzas.appfinanzas_api.dto.request;

import com.appfinanzas.appfinanzas_api.enums.BudgetCycleType;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class CreateBudgetCycleRequest {

    @NotNull(message = "El id del usuario es obligatorio")
    private Long userId;

    @NotNull(message = "El tipo de ciclo es obligatorio")
    private BudgetCycleType type;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDate startDate;

    @NotNull(message = "La fecha final es obligatoria")
    private LocalDate endDate;

    private Long clonedFromCycleId;

    public CreateBudgetCycleRequest() {
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public BudgetCycleType getType() {
        return type;
    }

    public void setType(BudgetCycleType type) {
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

    public Long getClonedFromCycleId() {
        return clonedFromCycleId;
    }

    public void setClonedFromCycleId(Long clonedFromCycleId) {
        this.clonedFromCycleId = clonedFromCycleId;
    }
}