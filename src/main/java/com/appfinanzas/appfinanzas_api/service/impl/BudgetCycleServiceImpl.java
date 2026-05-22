package com.appfinanzas.appfinanzas_api.service.impl;

import com.appfinanzas.appfinanzas_api.dto.request.CreateBudgetCycleRequest;
import com.appfinanzas.appfinanzas_api.dto.request.UpdateBudgetCycleRequest;
import com.appfinanzas.appfinanzas_api.dto.response.BudgetCycleResponse;
import com.appfinanzas.appfinanzas_api.entity.BudgetCycle;
import com.appfinanzas.appfinanzas_api.entity.User;
import com.appfinanzas.appfinanzas_api.exception.BusinessException;
import com.appfinanzas.appfinanzas_api.exception.ResourceNotFoundException;
import com.appfinanzas.appfinanzas_api.repository.BudgetCycleRepository;
import com.appfinanzas.appfinanzas_api.repository.UserRepository;
import com.appfinanzas.appfinanzas_api.service.BudgetCycleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class BudgetCycleServiceImpl implements BudgetCycleService {

    private final BudgetCycleRepository budgetCycleRepository;
    private final UserRepository userRepository;

    public BudgetCycleServiceImpl(BudgetCycleRepository budgetCycleRepository,
                                  UserRepository userRepository) {
        this.budgetCycleRepository = budgetCycleRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public BudgetCycleResponse createBudgetCycle(CreateBudgetCycleRequest request) {

        validateDates(request.getStartDate(), request.getEndDate());

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + request.getUserId()));

        boolean existsCycle = budgetCycleRepository.existsByUserIdAndStartDateAndEndDateAndActiveTrue(
                request.getUserId(),
                request.getStartDate(),
                request.getEndDate()
        );

        if (existsCycle) {
            throw new BusinessException("Ya existe un ciclo presupuestal activo para ese periodo");
        }

        BudgetCycle clonedFromCycle = null;

        if (request.getClonedFromCycleId() != null) {
            clonedFromCycle = budgetCycleRepository.findById(request.getClonedFromCycleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ciclo origen no encontrado con id: " + request.getClonedFromCycleId()));
        }

        BudgetCycle budgetCycle = new BudgetCycle();
        budgetCycle.setUser(user);
        budgetCycle.setType(request.getType());
        budgetCycle.setStartDate(request.getStartDate());
        budgetCycle.setEndDate(request.getEndDate());
        budgetCycle.setActive(true);
        budgetCycle.setClonedFromCycle(clonedFromCycle);

        BudgetCycle savedBudgetCycle = budgetCycleRepository.save(budgetCycle);

        return mapToResponse(savedBudgetCycle);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetCycleResponse> getAllBudgetCycles() {

        List<BudgetCycle> cycles = budgetCycleRepository.findAll();
        List<BudgetCycleResponse> response = new ArrayList<>();

        for (BudgetCycle cycle : cycles) {
            response.add(mapToResponse(cycle));
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BudgetCycleResponse> getBudgetCyclesByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + userId);
        }

        List<BudgetCycle> cycles = budgetCycleRepository.findByUserIdAndActiveTrue(userId);
        List<BudgetCycleResponse> response = new ArrayList<>();

        for (BudgetCycle cycle : cycles) {
            response.add(mapToResponse(cycle));
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public BudgetCycleResponse getBudgetCycleById(Long id) {

        BudgetCycle cycle = budgetCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciclo presupuestal no encontrado con id: " + id));

        return mapToResponse(cycle);
    }

    @Override
    @Transactional
    public BudgetCycleResponse updateBudgetCycle(Long id, UpdateBudgetCycleRequest request) {

        validateDates(request.getStartDate(), request.getEndDate());

        BudgetCycle cycle = budgetCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciclo presupuestal no encontrado con id: " + id));

        cycle.setType(request.getType());
        cycle.setStartDate(request.getStartDate());
        cycle.setEndDate(request.getEndDate());

        if (request.getActive() != null) {
            cycle.setActive(request.getActive());
        }

        BudgetCycle updatedCycle = budgetCycleRepository.save(cycle);

        return mapToResponse(updatedCycle);
    }

    @Override
    @Transactional
    public void deleteBudgetCycle(Long id) {

        BudgetCycle cycle = budgetCycleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ciclo presupuestal no encontrado con id: " + id));

        cycle.setActive(false);
        budgetCycleRepository.save(cycle);
    }

    private void validateDates(java.time.LocalDate startDate, java.time.LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new BusinessException("Las fechas del ciclo son obligatorias");
        }

        if (endDate.isBefore(startDate)) {
            throw new BusinessException("La fecha final no puede ser menor que la fecha inicial");
        }
    }

    private BudgetCycleResponse mapToResponse(BudgetCycle cycle) {

        Long clonedFromCycleId = null;

        if (cycle.getClonedFromCycle() != null) {
            clonedFromCycleId = cycle.getClonedFromCycle().getId();
        }

        return new BudgetCycleResponse(
                cycle.getId(),
                cycle.getUser().getId(),
                cycle.getUser().getName(),
                cycle.getType().name(),
                cycle.getStartDate(),
                cycle.getEndDate(),
                cycle.getActive(),
                clonedFromCycleId,
                cycle.getCreatedAt(),
                cycle.getUpdatedAt()
        );
    }
}