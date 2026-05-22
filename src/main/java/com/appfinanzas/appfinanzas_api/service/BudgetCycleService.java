package com.appfinanzas.appfinanzas_api.service;


import com.appfinanzas.appfinanzas_api.dto.request.CreateBudgetCycleRequest;
import com.appfinanzas.appfinanzas_api.dto.request.UpdateBudgetCycleRequest;
import com.appfinanzas.appfinanzas_api.dto.response.BudgetCycleResponse;

import java.util.List;

public interface BudgetCycleService {

    BudgetCycleResponse createBudgetCycle(CreateBudgetCycleRequest request);

    List<BudgetCycleResponse> getAllBudgetCycles();

    List<BudgetCycleResponse> getBudgetCyclesByUserId(Long userId);

    BudgetCycleResponse getBudgetCycleById(Long id);

    BudgetCycleResponse updateBudgetCycle(Long id, UpdateBudgetCycleRequest request);

    void deleteBudgetCycle(Long id);
}