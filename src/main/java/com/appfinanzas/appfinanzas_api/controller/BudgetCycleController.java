package com.appfinanzas.appfinanzas_api.controller;

import com.appfinanzas.appfinanzas_api.dto.request.CreateBudgetCycleRequest;
import com.appfinanzas.appfinanzas_api.dto.request.UpdateBudgetCycleRequest;
import com.appfinanzas.appfinanzas_api.dto.response.BudgetCycleResponse;
import com.appfinanzas.appfinanzas_api.service.BudgetCycleService;
import com.appfinanzas.appfinanzas_api.util.Constants;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(Constants.BUDGET_CYCLES_PATH)
public class BudgetCycleController {

    private final BudgetCycleService budgetCycleService;

    public BudgetCycleController(BudgetCycleService budgetCycleService) {
        this.budgetCycleService = budgetCycleService;
    }

    @PostMapping
    public ResponseEntity<BudgetCycleResponse> createBudgetCycle(@Valid @RequestBody CreateBudgetCycleRequest request) {
        BudgetCycleResponse response = budgetCycleService.createBudgetCycle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BudgetCycleResponse>> getAllBudgetCycles() {
        List<BudgetCycleResponse> response = budgetCycleService.getAllBudgetCycles();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BudgetCycleResponse> getBudgetCycleById(@PathVariable Long id) {
        BudgetCycleResponse response = budgetCycleService.getBudgetCycleById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BudgetCycleResponse>> getBudgetCyclesByUserId(@PathVariable Long userId) {
        List<BudgetCycleResponse> response = budgetCycleService.getBudgetCyclesByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BudgetCycleResponse> updateBudgetCycle(
            @PathVariable Long id,
            @Valid @RequestBody UpdateBudgetCycleRequest request) {

        BudgetCycleResponse response = budgetCycleService.updateBudgetCycle(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBudgetCycle(@PathVariable Long id) {
        budgetCycleService.deleteBudgetCycle(id);
        return ResponseEntity.noContent().build();
    }
}