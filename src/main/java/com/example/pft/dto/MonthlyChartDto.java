package com.example.pft.dto;

import java.math.BigDecimal;

public class MonthlyChartDto {
    private String month;
    private BigDecimal totalIncome;
    private BigDecimal totalExpense;

    public MonthlyChartDto(String month, BigDecimal totalIncome, BigDecimal totalExpense) {
        this.month = month;
        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
    }

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
