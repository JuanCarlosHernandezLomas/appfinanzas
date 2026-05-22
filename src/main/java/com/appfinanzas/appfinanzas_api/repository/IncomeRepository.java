package com.appfinanzas.appfinanzas_api.repository;

import com.appfinanzas.appfinanzas_api.entity.Income;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeRepository extends JpaRepository<Income, Long> {

    List<Income> findByUserId(Long userId);

    List<Income> findByUserIdAndActiveTrue(Long userId);

    List<Income> findByBudgetCycleIdAndActiveTrue(Long budgetCycleId);
}