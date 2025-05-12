package com.example.pft.service;



import com.example.pft.dto.DashboardSummaryDto;
import com.example.pft.dto.MonthlyChartDto;
import com.example.pft.model.Transaction;
import com.example.pft.model.User;
import com.example.pft.repository.TransactionRepository;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    

    public DashboardSummaryDto getDashboardSummary(User user) {
        List<Transaction> transactions = transactionRepository.findByUser(user);

        BigDecimal totalIncome = transactions.stream()
    .filter(t -> t.getType().equalsIgnoreCase("INCOME"))
    .map(t -> t.getAmount())
    .reduce(BigDecimal.ZERO, BigDecimal::add);

BigDecimal totalExpense = transactions.stream()
    .filter(t -> t.getType().equalsIgnoreCase("EXPENSE"))
    .map(t -> t.getAmount())
    .reduce(BigDecimal.ZERO, BigDecimal::add);

BigDecimal balance = totalIncome.subtract(totalExpense);

return new DashboardSummaryDto(totalIncome, totalExpense, balance);

}

public List<MonthlyChartDto> getMonthlyChartData(User user) {
    List<Transaction> transactions = transactionRepository.findByUser(user);

    Map<YearMonth, List<Transaction>> grouped = transactions.stream()
        .collect(Collectors.groupingBy(t -> YearMonth.from(t.getDate())));

    List<MonthlyChartDto> monthlyData = new ArrayList<>();

    grouped.forEach((month, txns) -> {
        BigDecimal income = txns.stream()
            .filter(t -> t.getType().equalsIgnoreCase("INCOME"))
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal expense = txns.stream()
            .filter(t -> t.getType().equalsIgnoreCase("EXPENSE"))
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        monthlyData.add(new MonthlyChartDto(month.toString(), income, expense));
    });

    // Sort by month
    monthlyData.sort(Comparator.comparing(MonthlyChartDto::getMonth));
    return monthlyData;
}

}