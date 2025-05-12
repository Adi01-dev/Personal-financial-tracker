package com.example.pft.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "monthly_budgets", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "category", "month"})
})
public class MonthlyBudget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String category;

    private String month; // format YYYY-MM

    private BigDecimal amount;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
