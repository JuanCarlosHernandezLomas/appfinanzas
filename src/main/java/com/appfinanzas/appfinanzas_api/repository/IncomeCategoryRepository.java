package com.appfinanzas.appfinanzas_api.repository;

import com.appfinanzas.appfinanzas_api.entity.IncomeCategory;
import com.appfinanzas.appfinanzas_api.enums.MovementType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncomeCategoryRepository extends JpaRepository<IncomeCategory, Long> {

    List<IncomeCategory> findByActiveTrue();

    List<IncomeCategory> findByTypeAndActiveTrue(MovementType type);

    boolean existsByNameIgnoreCaseAndType(String name, MovementType type);
}