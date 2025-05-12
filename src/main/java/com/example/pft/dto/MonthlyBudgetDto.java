package com.example.pft.dto;

import java.math.BigDecimal;

public class MonthlyBudgetDto {
    private String category;
    private String month; // Format: YYYY-MM
    private BigDecimal amount;

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
