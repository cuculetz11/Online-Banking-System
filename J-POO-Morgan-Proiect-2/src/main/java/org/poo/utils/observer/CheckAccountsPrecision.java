package org.poo.utils.observer;

import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.services.BankMethods;

public class CheckAccountsPrecision implements BankMethods {
    @Override
    public void visit(Bank bank) {
        for(Account account : bank.getAccounts().values()) {
            account.checkBalancePrecision();
        }
    }
}
