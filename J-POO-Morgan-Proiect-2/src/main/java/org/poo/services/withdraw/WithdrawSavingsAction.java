package org.poo.services.withdraw;

import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.services.payment.PaymentStrategy;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.TransactionManager;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class WithdrawSavingsAction implements PaymentStrategy {
    private Account account;
    private double exchangedAmount;
    private Account classicAccount;

    @Override
    public void pay() {
        account.withdrawMoney(exchangedAmount);
        classicAccount.addMoney(exchangedAmount, account.getCurrency());
        //System.out.println("am adugat bani din contul de savings: " + account.getIban() + " in contul classic: " + classicAccount.getIban() + " user: " + classicAccount.getUser().getEmail());
    }

    @Override
    public boolean checkForErrors(CommandInput input) {
        try {
            this.account = Bank.getInstance().getAccounts().get(input.getAccount());
            if(this.account == null){
                throw new IllegalArgumentException("Contul nu exitsa: " + input.getAccount());
            }
            boolean hasClassic = false;
            //System.out.println("userul: " + account.getUser().getEmail() + " accounts:" + account.getUser().getAccounts());
            for(Account a : account.getUser().getAccounts().values()) {
                if(a.getType().equals("classic") && a.getCurrency().equals(input.getCurrency())) {
                    hasClassic = true;
                    classicAccount = a;
                    break;
                }
            }
            if(!hasClassic){
                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder(Constants.DONT_HAVE_CLASSIC,
                                input.getTimestamp())
                                .transactionName(Constants.DONT_HAVE_CLASSIC_TRANSACTION)
                                .userEmail(account.getUser().getEmail())
                                .iban(account.getIban())
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
                //System.out.println("aici1");
                return true;
            }
            //sa pun data reala
            if(2024 - Integer.parseInt(this.account.getUser().getBirthDate().split("-")[0]) < 21) {
                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder(Constants.DONT_HAVE_MIN_AGE,
                                input.getTimestamp())
                                .transactionName(Constants.MIN_AGE_TRANSACTION)
                                .userEmail(account.getUser().getEmail())
                                .iban(account.getIban())
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
                //System.out.println("aici2");
                return true;
            }
            this.exchangedAmount = CURRENCY_EXCHANGE_SERVICE.exchangeCurrency(new CurrencyPair(input.getCurrency(), this.account.getCurrency()), input.getAmount());
            if (!this.account.isTransferPossibleWhCommision(this.exchangedAmount)) {
                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder(Constants.INSUFFICIENT_FUNDS,
                                input.getTimestamp())
                                .transactionName(Constants.INSUFFICIENT_FUNDS_TRANSFER_TRANSACTION)
                                .iban(this.account.getIban())
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
                //System.out.println("aici3");
                return true;
            }
//            if(this.account.verifyBalance() {
//                System.out.println("withdrow limit");
//                return true;
//            }

            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.SAVINGS_WITHDRAWAL,
                            input.getTimestamp())
                            .transactionName(Constants.Savings_WITHDRAWAL_TRANSACTION)
                            .accounts(Arrays.asList(classicAccount.getIban(), account.getIban()))
                            .userEmail(account.getUser().getEmail())
                            .amount(this.exchangedAmount)
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            datesForTransaction =
                    new DatesForTransaction.Builder(Constants.SAVINGS_WITHDRAWAL,
                            input.getTimestamp())
                            .transactionName(Constants.Savings_WITHDRAWAL_TRANSACTION)
                            .accounts(Arrays.asList(classicAccount.getIban(), account.getIban()))
                            .userEmail(classicAccount.getUser().getEmail())
                            .amount(this.exchangedAmount)
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return true;
        }
    }
}
