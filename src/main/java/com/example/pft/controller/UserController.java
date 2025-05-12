package com.example.pft.controller;


import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.pft.dto.LoginRequestDto;
import com.example.pft.dto.UserRegistrationDto;
import com.example.pft.model.Transaction;
import com.example.pft.model.User;
import com.example.pft.repository.TransactionRepository;
import com.example.pft.service.UserService;
import com.example.pft.util.JwtUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

@Autowired
private JwtUtil jwtUtil;


    @Autowired
    private UserService userService;

    @Autowired
private TransactionRepository transactionRepository;


    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody UserRegistrationDto dto) {
        User createdUser = userService.registerUser(dto.getName(), dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(createdUser);
    }

    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequestDto dto) {
    try {
        String token = userService.loginUser(dto.getEmail(), dto.getPassword());
        return ResponseEntity.ok(Collections.singletonMap("token", token));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Collections.singletonMap("error", e.getMessage()));
    }
}

@GetMapping("/analytics")
public ResponseEntity<?> getAnalytics(@RequestHeader("Authorization") String authHeader) {
    String email = getEmailFromToken(authHeader);
    User user = userService.findByEmail(email);

    List<Transaction> transactions = transactionRepository.findByUser(user);

    // Monthly totals
    Map<String, BigDecimal> monthlyIncome = new HashMap<>();
    Map<String, BigDecimal> monthlyExpense = new HashMap<>();

    // Category totals
    Map<String, BigDecimal> categoryTotals = new HashMap<>();

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");

    for (Transaction t : transactions) {
        String month = t.getDate().format(formatter);
        categoryTotals.merge(t.getCategory(), t.getAmount(), BigDecimal::add);

        if ("income".equalsIgnoreCase(t.getType())) {
            monthlyIncome.merge(month, t.getAmount(), BigDecimal::add);
        } else if ("expense".equalsIgnoreCase(t.getType())) {
            monthlyExpense.merge(month, t.getAmount(), BigDecimal::add);
        }
    }

    Map<String, Object> response = new HashMap<>();
    response.put("monthlyIncome", monthlyIncome);
    response.put("monthlyExpense", monthlyExpense);
    response.put("categoryTotals", categoryTotals);

    return ResponseEntity.ok(response);
}

private String getEmailFromToken(String authHeader) {
    // Remove "Bearer " prefix if present
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        return jwtUtil.extractEmail(token);
    }
    throw new RuntimeException("Invalid Authorization header");
}



}


