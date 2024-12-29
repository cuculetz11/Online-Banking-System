package org.poo.command.debug.dto;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.poo.entities.Merchant;
import org.poo.entities.transaction.Transaction;

import java.util.ArrayList;
import java.util.List;

public record AccountMerchantsDTO(@JsonGetter("IBAN") String iban, double balance, String currency,
                                  ArrayList<Transaction> transactions,
                                  List<Merchant> commerciants) {
}
