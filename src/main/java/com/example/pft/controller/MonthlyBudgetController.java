package com.example.pft.controller;

import com.example.pft.dto.BudgetComparisonDto;
import com.example.pft.dto.MonthlyBudgetDto;
import com.example.pft.dto.MonthlyChartDto;
import com.example.pft.model.MonthlyBudget;
import com.example.pft.model.User;
import com.example.pft.repository.TransactionRepository;
import com.example.pft.service.MonthlyBudgetService;
import com.example.pft.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/budget")
public class MonthlyBudgetController {

    @Autowired
    private MonthlyBudgetService budgetService;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @PostMapping("/set")
    public void setBudget(@RequestBody MonthlyBudgetDto dto, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        budgetService.setBudget(user, dto);
    }

    @GetMapping("/view")
    public List<MonthlyBudget> viewBudget(@RequestParam String month, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        return budgetService.getBudgets(user, month);
    }
    @GetMapping("/compare")
public List<BudgetComparisonDto> compareBudget(@RequestParam String month, Principal principal) {
    User user = userService.getUserByEmail(principal.getName());
    return budgetService.compareBudgetVsActual(user, month);
}
@GetMapping("/chart/monthly")
public ResponseEntity<List<MonthlyChartDto>> getMonthlyChartData(
        @RequestHeader("Authorization") String authHeader) {
    
    User user = userService.getUserFromToken(authHeader);
    List<MonthlyChartDto> chartData = transactionRepository.findMonthlyChartDataByUser(user);
    return ResponseEntity.ok(chartData);
}


}
