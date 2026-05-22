package com.appfinanzas.appfinanzas_api.service.impl;

import com.appfinanzas.appfinanzas_api.dto.request.CreateIncomeRequest;
import com.appfinanzas.appfinanzas_api.dto.request.UpdateIncomeRequest;
import com.appfinanzas.appfinanzas_api.dto.response.IncomeResponse;
import com.appfinanzas.appfinanzas_api.entity.BudgetCycle;
import com.appfinanzas.appfinanzas_api.entity.Income;
import com.appfinanzas.appfinanzas_api.entity.IncomeCategory;
import com.appfinanzas.appfinanzas_api.entity.User;
import com.appfinanzas.appfinanzas_api.exception.BusinessException;
import com.appfinanzas.appfinanzas_api.exception.ResourceNotFoundException;
import com.appfinanzas.appfinanzas_api.repository.BudgetCycleRepository;
import com.appfinanzas.appfinanzas_api.repository.IncomeCategoryRepository;
import com.appfinanzas.appfinanzas_api.repository.IncomeRepository;
import com.appfinanzas.appfinanzas_api.repository.UserRepository;
import com.appfinanzas.appfinanzas_api.service.IncomeService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class IncomeServiceImpl implements IncomeService {

    private final IncomeRepository incomeRepository;
    private final UserRepository userRepository;
    private final BudgetCycleRepository budgetCycleRepository;
    private final IncomeCategoryRepository incomeCategoryRepository;

    public IncomeServiceImpl(IncomeRepository incomeRepository,
                             UserRepository userRepository,
                             BudgetCycleRepository budgetCycleRepository,
                             IncomeCategoryRepository incomeCategoryRepository) {
        this.incomeRepository = incomeRepository;
        this.userRepository = userRepository;
        this.budgetCycleRepository = budgetCycleRepository;
        this.incomeCategoryRepository = incomeCategoryRepository;
    }

    @Override
    @Transactional
    public IncomeResponse createIncome(CreateIncomeRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + request.getUserId()));

        BudgetCycle budgetCycle = budgetCycleRepository.findById(request.getBudgetCycleId())
                .orElseThrow(() -> new ResourceNotFoundException("Ciclo presupuestal no encontrado con id: " + request.getBudgetCycleId()));

        validateBudgetCycleBelongsToUser(budgetCycle, user.getId());

        IncomeCategory incomeCategory = null;

        if (request.getIncomeCategoryId() != null) {
            incomeCategory = incomeCategoryRepository.findById(request.getIncomeCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría de ingreso no encontrada con id: " + request.getIncomeCategoryId()));

            if (!incomeCategory.getType().equals(request.getType())) {
                throw new BusinessException("El tipo de la categoría no coincide con el tipo del ingreso");
            }
        }

        Income income = new Income();
        income.setUser(user);
        income.setBudgetCycle(budgetCycle);
        income.setIncomeCategory(incomeCategory);
        income.setName(request.getName());
        income.setType(request.getType());
        income.setAmount(request.getAmount());
        income.setIncomeDate(request.getIncomeDate());
        income.setRecurring(request.getRecurring() != null ? request.getRecurring() : false);
        income.setObservations(request.getObservations());
        income.setActive(true);

        Income savedIncome = incomeRepository.save(income);

        return mapToResponse(savedIncome);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeResponse> getAllIncomes() {

        List<Income> incomes = incomeRepository.findAll();
        List<IncomeResponse> response = new ArrayList<>();

        for (Income income : incomes) {
            response.add(mapToResponse(income));
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public IncomeResponse getIncomeById(Long id) {

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso no encontrado con id: " + id));

        return mapToResponse(income);
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeResponse> getIncomesByUserId(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Usuario no encontrado con id: " + userId);
        }

        List<Income> incomes = incomeRepository.findByUserIdAndActiveTrue(userId);
        List<IncomeResponse> response = new ArrayList<>();

        for (Income income : incomes) {
            response.add(mapToResponse(income));
        }

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public List<IncomeResponse> getIncomesByBudgetCycleId(Long budgetCycleId) {

        if (!budgetCycleRepository.existsById(budgetCycleId)) {
            throw new ResourceNotFoundException("Ciclo presupuestal no encontrado con id: " + budgetCycleId);
        }

        List<Income> incomes = incomeRepository.findByBudgetCycleIdAndActiveTrue(budgetCycleId);
        List<IncomeResponse> response = new ArrayList<>();

        for (Income income : incomes) {
            response.add(mapToResponse(income));
        }

        return response;
    }

    @Override
    @Transactional
    public IncomeResponse updateIncome(Long id, UpdateIncomeRequest request) {

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso no encontrado con id: " + id));

        IncomeCategory incomeCategory = null;

        if (request.getIncomeCategoryId() != null) {
            incomeCategory = incomeCategoryRepository.findById(request.getIncomeCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Categoría de ingreso no encontrada con id: " + request.getIncomeCategoryId()));

            if (!incomeCategory.getType().equals(request.getType())) {
                throw new BusinessException("El tipo de la categoría no coincide con el tipo del ingreso");
            }
        }

        income.setIncomeCategory(incomeCategory);
        income.setName(request.getName());
        income.setType(request.getType());
        income.setAmount(request.getAmount());
        income.setIncomeDate(request.getIncomeDate());
        income.setRecurring(request.getRecurring() != null ? request.getRecurring() : false);
        income.setObservations(request.getObservations());

        if (request.getActive() != null) {
            income.setActive(request.getActive());
        }

        Income updatedIncome = incomeRepository.save(income);

        return mapToResponse(updatedIncome);
    }

    @Override
    @Transactional
    public void deleteIncome(Long id) {

        Income income = incomeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ingreso no encontrado con id: " + id));

        income.setActive(false);
        incomeRepository.save(income);
    }

    private void validateBudgetCycleBelongsToUser(BudgetCycle budgetCycle, Long userId) {

        if (!budgetCycle.getUser().getId().equals(userId)) {
            throw new BusinessException("El ciclo presupuestal no pertenece al usuario indicado");
        }

        if (!budgetCycle.getActive()) {
            throw new BusinessException("No se puede registrar ingreso en un ciclo presupuestal inactivo");
        }
    }

    private IncomeResponse mapToResponse(Income income) {

        Long incomeCategoryId = null;
        String incomeCategoryName = null;

        if (income.getIncomeCategory() != null) {
            incomeCategoryId = income.getIncomeCategory().getId();
            incomeCategoryName = income.getIncomeCategory().getName();
        }

        return new IncomeResponse(
                income.getId(),
                income.getUser().getId(),
                income.getUser().getName(),
                income.getBudgetCycle().getId(),
                income.getBudgetCycle().getType().name(),
                incomeCategoryId,
                incomeCategoryName,
                income.getName(),
                income.getType().name(),
                income.getAmount(),
                income.getIncomeDate(),
                income.getRecurring(),
                income.getObservations(),
                income.getActive(),
                income.getCreatedAt(),
                income.getUpdatedAt()
        );
    }
}