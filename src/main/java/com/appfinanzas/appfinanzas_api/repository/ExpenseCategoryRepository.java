package com.appfinanzas.appfinanzas_api.repository;

import com.appfinanzas.appfinanzas_api.entity.ExpenseCategory;
import com.appfinanzas.appfinanzas_api.enums.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseCategoryRepository extends JpaRepository<ExpenseCategory, Long> {

    List<ExpenseCategory> findByActiveTrue();

    List<ExpenseCategory> findByTypeAndActiveTrue(MovementType type);

    boolean existsByNameIgnoreCaseAndType(String name, MovementType type);
}