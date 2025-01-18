package org.poo.services.splitPayment;

import lombok.Getter;
import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.services.CurrencyExchangeService;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.TransactionManager;

import java.lang.constant.Constable;
import java.util.ArrayList;
import java.util.HashSet;

import java.util.List;

@Getter
public class WaitingSplitPayment {
    private HashSet<String> remainedPayments;
    private CommandInput commandInput;
    private List<Double> amounts;

    public WaitingSplitPayment(CommandInput commandInput, HashSet<String> remainedPayments) {
        this.commandInput = commandInput;
        this.remainedPayments = remainedPayments;
    }
    public boolean check() {
        if(remainedPayments.isEmpty()) {
            return true;
        }
        return false;
    }
    public String checkEverybodyMoney() {
         List<Double> amountsPerUser = new ArrayList<>();
        if(commandInput.getSplitPaymentType().equals("equal")) {
            for(int i = 0 ; i < commandInput.getAccounts().size(); i++) {
                amountsPerUser.add(commandInput.getAmount() / commandInput.getAccounts().size());
            }
        } else if(commandInput.getSplitPaymentType().equals("custom")) {
            amountsPerUser = commandInput.getAmountForUsers();
        }
        amounts = amountsPerUser;
        for(int i = 0; i < amountsPerUser.size(); i++) {
           Account account = Bank.getInstance().getAccounts().get(commandInput.getAccounts().get(i));
           double amount = amountsPerUser.get(i);
           CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
           amount = currencyExchangeService.exchangeCurrency(new CurrencyPair(commandInput.getCurrency(), account.getCurrency()), amount);
           if (!account.isTransferPossibleWhCommision(amount)) {
               return account.getIban();
           }
       }
        return null;
    }

    public void processPayment() {

        String accountWithNoMoney = checkEverybodyMoney();
        if(accountWithNoMoney != null) {
            for(int i = 0; i < amounts.size(); i++) {
                Account account = Bank.getInstance().getAccounts().get(commandInput.getAccounts().get(i));
                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder("Split payment of " + String.format("%.2f", commandInput.getAmount()) + " " + commandInput.getCurrency(), commandInput.getTimestamp())
                                .transactionName(Constants.SPLIT_PAYMENT_FAILED_TRANSACTION)
                                .currency(commandInput.getCurrency())
                                .accounts(commandInput.getAccounts())
                                .amountForUsers(amounts)
                                .splitPaymentType(commandInput.getSplitPaymentType())
                                .userEmail(account.getUser().getEmail())
                                .iban(account.getIban())
                                .errorMessage("Account " + accountWithNoMoney + " has insufficient funds for a split payment.")
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
            }
        } else {
            for (int i = 0; i < amounts.size(); i++) {
                Account account = Bank.getInstance().getAccounts().get(commandInput.getAccounts().get(i));
                double amount = amounts.get(i);
                CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
                amount = currencyExchangeService.exchangeCurrency(new CurrencyPair(commandInput.getCurrency(), account.getCurrency()), amount);
                account.paySplit(amount);
                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder("Split payment of " + String.format("%.2f", commandInput.getAmount()) + " " + commandInput.getCurrency(), commandInput.getTimestamp())
                                .transactionName(Constants.SPLIT_PAYMENT_TRANSACTION)
                                .currency(commandInput.getCurrency())
                                .accounts(commandInput.getAccounts())
                                .amountForUsers(amounts)
                                .splitPaymentType(commandInput.getSplitPaymentType())
                                .userEmail(account.getUser().getEmail())
                                .iban(account.getIban())
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
            }
        }


    }
}
