package com.banking.service;

import com.banking.entity.Account;
import com.banking.entity.Transaction;
import com.banking.entity.User;
import com.banking.repository.AccountRepository;
import com.banking.repository.TransactionRepository;
import com.banking.util.CsvUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final CsvUtility csvUtil;

    public Transaction deposit(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance() + amount);
        Transaction transaction = new Transaction(null, amount, "DEPOSIT", LocalDateTime.now(), account, null);
        return transactionRepository.save(transaction);
    }

    public Transaction withdraw(String accountNumber, double amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        if (account.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }
        account.setBalance(account.getBalance() - amount);
        Transaction transaction = new Transaction(null, amount, "WITHDRAW", LocalDateTime.now(), account, null);
        return transactionRepository.save(transaction);
    }

    // Transfer money between accounts
    public Transaction transfer(String fromAccountNumber, String toAccountNumber, double amount) {
        Account sender = accountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new RuntimeException("Sender account not found"));

        Account receiver = accountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new RuntimeException("Receiver account not found"));

        if (sender.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        sender.setBalance(sender.getBalance() - amount);
        receiver.setBalance(receiver.getBalance() + amount);

        Transaction transaction = new Transaction();
        transaction.setFromAccount(sender);
        transaction.setToAccount(receiver);
        transaction.setAmount(amount);
        transaction.setType("TRANSFER");
        transaction.setTimestamp(LocalDateTime.now());

        accountRepository.save(sender);
        accountRepository.save(receiver);
        return transactionRepository.save(transaction);
    }

    // Export transactions as CSV
    public ByteArrayInputStream getTransactionsAsCSV(User user) {
        List<Transaction> transactions = transactionRepository.findByFromAccount_User(user.getId());
        return csvUtil.transactionsToCSV(transactions);
    }
}

