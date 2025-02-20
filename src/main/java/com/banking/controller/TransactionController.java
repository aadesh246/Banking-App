package com.banking.controller;

import com.banking.entity.Transaction;
import com.banking.entity.User;
import com.banking.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
public class TransactionController {
    private final TransactionService transactionService;

    // Transfer money
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> transfer(
            @RequestParam String fromAccount,
            @RequestParam String toAccount,
            @RequestParam double amount) {
        return ResponseEntity.ok(transactionService.transfer(fromAccount, toAccount, amount));
    }

    // Export transactions as CSV
    @GetMapping("/export-csv")
    public ResponseEntity<InputStreamResource> exportTransactionsToCSV(@AuthenticationPrincipal User user) {
        ByteArrayInputStream csvData = transactionService.getTransactionsAsCSV(user);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions.csv")
                .contentType(MediaType.parseMediaType("text/csv"))
                .body(new InputStreamResource(csvData));
    }

    @PostMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestParam String accountNumber, @RequestParam double amount) {
        return ResponseEntity.ok(transactionService.deposit(accountNumber, amount));
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestParam String accountNumber, @RequestParam double amount) {
        return ResponseEntity.ok(transactionService.withdraw(accountNumber, amount));
    }
}
