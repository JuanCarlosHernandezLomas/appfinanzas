package com.appfinanzas.appfinanzas_api.controller;

import com.appfinanzas.appfinanzas_api.dto.request.CreateIncomeRequest;
import com.appfinanzas.appfinanzas_api.dto.request.UpdateIncomeRequest;
import com.appfinanzas.appfinanzas_api.dto.response.IncomeResponse;
import com.appfinanzas.appfinanzas_api.service.IncomeService;
import com.appfinanzas.appfinanzas_api.util.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.INCOMES_PATH)
public class IncomeController {

    private final IncomeService incomeService;

    public IncomeController(IncomeService incomeService) {
        this.incomeService = incomeService;
    }

    @PostMapping
    public ResponseEntity<IncomeResponse> createIncome(@Valid @RequestBody CreateIncomeRequest request) {
        IncomeResponse response = incomeService.createIncome(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<IncomeResponse>> getAllIncomes() {
        List<IncomeResponse> response = incomeService.getAllIncomes();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncomeResponse> getIncomeById(@PathVariable Long id) {
        IncomeResponse response = incomeService.getIncomeById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<IncomeResponse>> getIncomesByUserId(@PathVariable Long userId) {
        List<IncomeResponse> response = incomeService.getIncomesByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/budget-cycle/{budgetCycleId}")
    public ResponseEntity<List<IncomeResponse>> getIncomesByBudgetCycleId(@PathVariable Long budgetCycleId) {
        List<IncomeResponse> response = incomeService.getIncomesByBudgetCycleId(budgetCycleId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<IncomeResponse> updateIncome(
            @PathVariable Long id,
            @Valid @RequestBody UpdateIncomeRequest request) {

        IncomeResponse response = incomeService.updateIncome(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteIncome(@PathVariable Long id) {
        incomeService.deleteIncome(id);
        return ResponseEntity.noContent().build();
    }
}