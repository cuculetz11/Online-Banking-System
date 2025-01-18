package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.services.CurrencyExchangeService;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.ErrorManager;
import org.poo.utils.TransactionManager;

import java.util.HashMap;
import java.util.Map;

public class UpgradePlan implements Command {
    private Map<String, Integer> planToValue = Map.of(
            "standard", 0,
            "student", 0,
            "silver", 1,
            "gold", 2
    );
    @Override
    public void execute(CommandInput input) {
        Account account = Bank.getInstance().getAccounts().get(input.getAccount());
        if(account == null) {
            ErrorManager.notFound(Constants.ACCOUNT_NOT_FOUND, input.getCommand(), input.getTimestamp());
            return;
        }
        if(planToValue.get(account.getUser().getPlan().getPlanType()) == planToValue.get(input.getNewPlanType())) {
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder("The user already has the " + input.getNewPlanType()
                            + " plan.", input.getTimestamp())
                            .transactionName(Constants.PLAN_ALREADY_TRANSACTION)
                            .userEmail(account.getUser().getEmail())
                            .iban(account.getIban())
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            return;
        }
        if(planToValue.get(account.getUser().getPlan().getPlanType()) > planToValue.get(input.getNewPlanType())) {
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder("You cannot downgrade your plan.", input.getTimestamp())
                            .transactionName(Constants.PLAN_ALREADY_TRANSACTION)
                            .userEmail(account.getUser().getEmail())
                            .iban(account.getIban())
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            return;
        }
        double amountToPay = 0;
        if(planToValue.get(input.getNewPlanType()) - planToValue.get(account.getUser().getPlan().getPlanType()) == 2) {
            amountToPay = 350;
        } else if(planToValue.get(input.getNewPlanType()) - planToValue.get(account.getUser().getPlan().getPlanType()) == 1) {
            if(planToValue.get(account.getUser().getPlan().getPlanType()) == 0) {
                amountToPay = 100;
            }
            if(planToValue.get(account.getUser().getPlan().getPlanType()) == 1) {
                amountToPay = 250;
            }
        }
        CurrencyExchangeService currencyExchangeService = new CurrencyExchangeService();
        amountToPay = currencyExchangeService.exchangeCurrency(new CurrencyPair("RON", account.getCurrency()),amountToPay);

        if(account.getBalance() < amountToPay) {
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.INSUFFICIENT_FUNDS, input.getTimestamp())
                            .transactionName(Constants.INSUFFICIENT_FUNDS_PAY_ONLINE_TRANSACTION)
                            .userEmail(account.getUser().getEmail())
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            return;
        }
        account.setBalance(account.getBalance() - amountToPay);
        account.getUser().getPlan().update(input.getNewPlanType());
        DatesForTransaction datesForTransaction =
                new DatesForTransaction.Builder(Constants.UPGRADE_PLAN_SENTENCE,
                        input.getTimestamp())
                        .transactionName(Constants.UPGRADE_PLAN_TRANSACTION)
                        .userEmail(account.getUser().getEmail())
                        .newPlanType(input.getNewPlanType())
                        .iban(account.getIban())
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);
    }
}
