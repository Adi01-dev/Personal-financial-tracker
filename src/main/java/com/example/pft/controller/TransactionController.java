package com.example.pft.controller;

import com.example.pft.dto.CategorySummaryDto;
import com.example.pft.dto.DashboardSummaryDto;
import com.example.pft.dto.MonthlyChartDto;
import com.example.pft.dto.MonthlySummaryDto;
import com.example.pft.model.Transaction;
import com.example.pft.model.User;
import com.example.pft.repository.TransactionRepository;
import com.example.pft.repository.UserRepository;
import com.example.pft.service.TransactionService;
import com.example.pft.service.UserService;
import com.example.pft.util.JwtUtil;

import java.util.Collections;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private String getEmailFromToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return jwtUtil.extractEmail(authHeader.substring(7));
        }
        return null;
    }

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction transaction,
            @RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        transaction.setUser(user);
        return ResponseEntity.ok(transactionRepository.save(transaction));
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getAllTransactions(@RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        return ResponseEntity.ok(transactionRepository.findByUser(user));
    }

    @GetMapping("/summary/monthly")
    public ResponseEntity<List<MonthlySummaryDto>> getMonthlySummary(
            @RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));
        List<MonthlySummaryDto> summary = transactionRepository.findMonthlySummaryByUser(user);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/summary/category")
    public ResponseEntity<List<CategorySummaryDto>> getCategorySummary(@AuthenticationPrincipal User user) {
        List<CategorySummaryDto> summary = transactionRepository.getCategoryWiseSummary(user);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboardData(@RequestHeader("Authorization") String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "Missing or invalid Authorization header"));
            }

            String token = authHeader.substring(7);
            String email = jwtUtil.extractEmail(token);

            if (email == null || email.isBlank()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "Invalid token: email could not be extracted"));
            }

            User user = userService.findByEmail(email);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Collections.singletonMap("error", "User not found for the given token"));
            }

            DashboardSummaryDto dashboardSummary = transactionService.getDashboardSummary(user);
            return ResponseEntity.ok(dashboardSummary);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "An unexpected error occurred"));
        }
    }

    @GetMapping("/chart/monthly")
public ResponseEntity<List<MonthlyChartDto>> getMonthlyChartData(@RequestHeader("Authorization") String authHeader) {
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    String token = authHeader.substring(7);
    String email = jwtUtil.extractEmail(token);
    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    List<MonthlyChartDto> chartData = transactionService.getMonthlyChartData(user);
    
    return ResponseEntity.ok(chartData);

}


    @Autowired
    private UserService userService;

    @GetMapping("/export")
    public void exportToCsv(HttpServletResponse response, Principal principal) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=transactions.csv");

        User user = userService.getUserByEmail(principal.getName());
        List<Transaction> transactions = transactionRepository.findByUser(user);

        PrintWriter writer = response.getWriter();
        writer.println("Date,Type,Amount,Category,Description");

        for (Transaction t : transactions) {
            writer.printf("%s,%s,%.2f,%s,%s%n",
                    t.getDate(),
                    t.getType(),
                    t.getAmount(),
                    t.getCategory(),
                    t.getDescription());
        }

        writer.flush();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTransaction(@PathVariable Long id,
            @RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!transaction.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        transactionRepository.delete(transaction);
        return ResponseEntity.ok("Deleted");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTransaction(@PathVariable Long id,
            @RequestBody Transaction updatedTransaction,
            @RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromToken(authHeader);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        if (!existing.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).body("Not allowed");
        }

        // Update fields
        existing.setAmount(updatedTransaction.getAmount());
        existing.setCategory(updatedTransaction.getCategory());
        existing.setDescription(updatedTransaction.getDescription());
        existing.setDate(updatedTransaction.getDate());
        existing.setType(updatedTransaction.getType());

        transactionRepository.save(existing);

        return ResponseEntity.ok(existing);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Transaction>> filterTransactions(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        String email = getEmailFromToken(authHeader);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Transaction> filtered = transactionRepository.findFilteredTransactions(
                user, category, type, startDate, endDate);

        return ResponseEntity.ok(filtered);
    }

}
