package com.example.pft.repository;

import com.example.pft.dto.CategorySummaryDto;
import com.example.pft.dto.MonthlyChartDto;
import com.example.pft.dto.MonthlySummaryDto;
import com.example.pft.model.Transaction;
import com.example.pft.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
        List<Transaction> findByUser(User user);

        @Query("SELECT new com.example.pft.dto.MonthlySummaryDto(" +
                        "CONCAT(YEAR(t.date), '-', MONTH(t.date)), " +
                        "SUM(CASE WHEN t.type = 'income' THEN t.amount ELSE 0 END), " +
                        "SUM(CASE WHEN t.type = 'expense' THEN t.amount ELSE 0 END)) " +
                        "FROM Transaction t " +
                        "WHERE t.user = :user " +
                        "GROUP BY YEAR(t.date), MONTH(t.date) " +
                        "ORDER BY YEAR(t.date), MONTH(t.date)")
        List<MonthlySummaryDto> findMonthlySummaryByUser(@Param("user") User user);

        @Query("SELECT new com.example.pft.dto.CategorySummaryDto(" +
                        "t.category, " +
                        "SUM(CASE WHEN t.type = 'income' THEN t.amount ELSE 0 END), " +
                        "SUM(CASE WHEN t.type = 'expense' THEN t.amount ELSE 0 END)) " +
                        "FROM Transaction t WHERE t.user = :user " +
                        "GROUP BY t.category")
        List<CategorySummaryDto> getCategoryWiseSummary(@Param("user") User user);

        @Query("SELECT " +
                        "SUM(CASE WHEN t.type = 'income' THEN t.amount ELSE 0 END), " +
                        "SUM(CASE WHEN t.type = 'expense' THEN t.amount ELSE 0 END) " +
                        "FROM Transaction t WHERE t.user = :user")
        Object[] getDashboardSummary(@Param("user") User user);

        @Query("SELECT new com.example.pft.dto.MonthlyChartDto(" +
                        "CONCAT(YEAR(t.date), '-', MONTH(t.date)), " +
                        "SUM(CASE WHEN t.type = 'income' THEN t.amount ELSE 0 END), " +
                        "SUM(CASE WHEN t.type = 'expense' THEN t.amount ELSE 0 END)) " +
                        "FROM Transaction t " +
                        "WHERE t.user = :user " +
                        "GROUP BY YEAR(t.date), MONTH(t.date) " +
                        "ORDER BY YEAR(t.date), MONTH(t.date)")
        List<MonthlyChartDto> findMonthlyChartDataByUser(@Param("user") User user);

        @Query("SELECT t.category, SUM(t.amount) FROM Transaction t " +
                        "WHERE t.user = :user AND t.type = 'expense' AND FUNCTION('DATE_FORMAT', t.date, '%Y-%m') = :month "
                        +
                        "GROUP BY t.category")
        List<Object[]> findActualSpendingByUserAndMonth(@Param("user") User user, @Param("month") String month);

        @Query("SELECT t FROM Transaction t WHERE t.user = :user " +
                        "AND (:category IS NULL OR t.category = :category) " +
                        "AND (:type IS NULL OR t.type = :type) " +
                        "AND (:startDate IS NULL OR t.date >= :startDate) " +
                        "AND (:endDate IS NULL OR t.date <= :endDate)")
        List<Transaction> findFilteredTransactions(
                        @Param("user") User user,
                        @Param("category") String category,
                        @Param("type") String type,
                        @Param("startDate") java.time.LocalDate startDate,
                        @Param("endDate") java.time.LocalDate endDate);

}
