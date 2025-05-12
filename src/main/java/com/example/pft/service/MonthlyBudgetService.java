package com.example.pft.service;

import com.example.pft.dto.BudgetComparisonDto;
import com.example.pft.dto.MonthlyBudgetDto;
import com.example.pft.model.MonthlyBudget;
import com.example.pft.model.User;
import com.example.pft.repository.MonthlyBudgetRepository;
import com.example.pft.repository.TransactionRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MonthlyBudgetService {

    @Autowired
    private MonthlyBudgetRepository budgetRepository;
    @Autowired
private TransactionRepository transactionRepository;


    public void setBudget(User user, MonthlyBudgetDto dto) {
        MonthlyBudget existing = budgetRepository.findByUserAndCategoryAndMonth(user, dto.getCategory(), dto.getMonth());
        if (existing != null) {
            existing.setAmount(dto.getAmount());
            budgetRepository.save(existing);
        } else {
            MonthlyBudget newBudget = new MonthlyBudget();
            newBudget.setUser(user);
            newBudget.setCategory(dto.getCategory());
            newBudget.setMonth(dto.getMonth());
            newBudget.setAmount(dto.getAmount());
            budgetRepository.save(newBudget);
        }
    }

    public List<MonthlyBudget> getBudgets(User user, String month) {
        return budgetRepository.findByUserAndMonth(user, month);
    }

    public List<BudgetComparisonDto> compareBudgetVsActual(User user, String month) {
    List<MonthlyBudget> budgets = budgetRepository.findByUserAndMonth(user, month);

    // Get actual spending by category for the month
    List<Object[]> actuals = transactionRepository.findActualSpendingByUserAndMonth(user, month);

    Map<String, BigDecimal> actualMap = new HashMap<>();
    for (Object[] row : actuals) {
        actualMap.put((String) row[0], (BigDecimal) row[1]);
    }

    List<BudgetComparisonDto> result = new ArrayList<>();
    for (MonthlyBudget budget : budgets) {
        BigDecimal spent = actualMap.getOrDefault(budget.getCategory(), BigDecimal.ZERO);
        result.add(new BudgetComparisonDto(budget.getCategory(), budget.getAmount(), spent));
    }

    return result;
}

}
