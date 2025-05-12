package com.example.pft.dto;

import java.math.BigDecimal;

public class CategorySummaryDto {
    private String category;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;

    public CategorySummaryDto(String category, BigDecimal totalIncome, BigDecimal totalExpense) {
        this.category = category;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

    public String getCategory() {
        return category;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }
}
