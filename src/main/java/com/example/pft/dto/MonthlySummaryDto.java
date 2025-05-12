package com.example.pft.dto;

import java.math.BigDecimal;

public class MonthlySummaryDto {
    private String month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;

    // ✅ Required constructor
    public MonthlySummaryDto(String month, BigDecimal totalIncome, BigDecimal totalExpense) {
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

    // Getters
    public String getMonth() {
        return month;
    }

    public BigDecimal getTotalIncome() {
        return totalIncome;
    }

    public BigDecimal getTotalExpense() {
        return totalExpense;
    }
}
