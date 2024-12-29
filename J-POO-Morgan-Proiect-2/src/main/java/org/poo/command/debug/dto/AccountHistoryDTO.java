package org.poo.command.debug.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.poo.entities.transaction.Transaction;

import java.util.ArrayList;

public record AccountHistoryDTO(@JsonGetter("IBAN") String IBAN, double balance, String currency,
                                ArrayList<Transaction> transactions) {
}
