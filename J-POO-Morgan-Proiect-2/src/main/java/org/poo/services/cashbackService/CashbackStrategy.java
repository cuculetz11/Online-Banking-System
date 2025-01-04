package org.poo.services.cashbackService;

import org.poo.entities.bankAccount.Account;
import org.poo.entities.commerciant.Commerciant;
import org.poo.fileio.CommandInput;

public interface CashbackStrategy {
    boolean check(Commerciant commerciant, Account account);
    double getCashback();
    void updateCashback(Account account);
}
