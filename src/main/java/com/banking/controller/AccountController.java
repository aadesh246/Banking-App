package com.banking.controller;

import com.banking.entity.Account;
import com.banking.entity.User;
import com.banking.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    // Create a new account for the logged-in user
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(
            @AuthenticationPrincipal User user,
            @RequestParam double initialBalance) {

        Account account = accountService.createAccount(user, initialBalance);
        return ResponseEntity.ok(account);
    }

    // Get account details by account number
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountDetails(@PathVariable String accountNumber) {
        Account account = accountService.getAccountByNumber(accountNumber);
        return ResponseEntity.ok(account);
    }
}

