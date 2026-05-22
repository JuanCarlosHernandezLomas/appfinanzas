package com.appfinanzas.appfinanzas_api.repository;

import com.appfinanzas.appfinanzas_api.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findByUserId(Long userId);

    List<Expense> findByUserIdAndActiveTrue(Long userId);

    List<Expense> findByBudgetCycleIdAndActiveTrue(Long budgetCycleId);

    List<Expense> findByBudgetCycleIdAndPaidFalseAndActiveTrue(Long budgetCycleId);
}