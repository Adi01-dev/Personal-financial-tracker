package com.example.pft.repository;

import com.example.pft.model.MonthlyBudget;
import com.example.pft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MonthlyBudgetRepository extends JpaRepository<MonthlyBudget, Long> {
    List<MonthlyBudget> findByUserAndMonth(User user, String month);
    MonthlyBudget findByUserAndCategoryAndMonth(User user, String category, String month);
}
