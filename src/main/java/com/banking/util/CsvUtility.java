package com.banking.util;

import com.banking.entity.Transaction;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Component
public class CsvUtility {
    public ByteArrayInputStream transactionsToCSV(List<Transaction> transactions) {
        final CSVFormat format = CSVFormat.DEFAULT.withHeader("ID", "Amount", "Type", "Timestamp", "From Account", "To Account");
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {

            for (Transaction transaction : transactions) {
                csvPrinter.printRecord(
                        transaction.getId(),
                        transaction.getAmount(),
                        transaction.getType(),
                        transaction.getTimestamp(),
                        transaction.getFromAccount().getAccountNumber(),
                        transaction.getToAccount() != null ? transaction.getToAccount().getAccountNumber() : "N/A"
                );
            }
            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Error generating CSV", e);
        }
    }
}

