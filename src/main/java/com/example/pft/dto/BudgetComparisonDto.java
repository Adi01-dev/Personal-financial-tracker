package com.example.pft.dto;

import java.math.BigDecimal;

public class BudgetComparisonDto {
    private String category;
    private BigDecimal budgetedAmount;
    private BigDecimal actualSpent;

    public BudgetComparisonDto(String category, BigDecimal budgetedAmount, BigDecimal actualSpent) {
        this.category = category;
        this.budgetedAmount = budgetedAmount;
        this.actualSpent = actualSpent;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getBudgetedAmount() {
        return budgetedAmount;
    }

    public void setBudgetedAmount(BigDecimal budgetedAmount) {
        this.budgetedAmount = budgetedAmount;
    }

    public BigDecimal getActualSpent() {
        return actualSpent;
    }

    public void setActualSpent(BigDecimal actualSpent) {
        this.actualSpent = actualSpent;
    }
}
