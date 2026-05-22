package com.appfinanzas.appfinanzas_api.service;

import com.appfinanzas.appfinanzas_api.dto.request.CreateIncomeRequest;
import com.appfinanzas.appfinanzas_api.dto.request.UpdateIncomeRequest;
import com.appfinanzas.appfinanzas_api.dto.response.IncomeResponse;

import java.util.List;

public interface IncomeService {

    IncomeResponse createIncome(CreateIncomeRequest request);

    List<IncomeResponse> getAllIncomes();

    IncomeResponse getIncomeById(Long id);

    List<IncomeResponse> getIncomesByUserId(Long userId);

    List<IncomeResponse> getIncomesByBudgetCycleId(Long budgetCycleId);

    IncomeResponse updateIncome(Long id, UpdateIncomeRequest request);

    void deleteIncome(Long id);
}