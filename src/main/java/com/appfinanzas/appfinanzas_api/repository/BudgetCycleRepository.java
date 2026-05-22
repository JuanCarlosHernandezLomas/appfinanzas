package com.appfinanzas.appfinanzas_api.repository;

import com.appfinanzas.appfinanzas_api.entity.BudgetCycle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetCycleRepository extends JpaRepository<BudgetCycle, Long> {

    List<BudgetCycle> findByUserId(Long userId);

    List<BudgetCycle> findByUserIdAndActiveTrue(Long userId);

    boolean existsByUserIdAndStartDateAndEndDateAndActiveTrue(
            Long userId,
            java.time.LocalDate startDate,
            java.time.LocalDate endDate
    );
}