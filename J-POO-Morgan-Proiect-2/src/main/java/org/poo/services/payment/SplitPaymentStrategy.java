package org.poo.services.payment;

import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.TransactionManager;

import java.util.ArrayList;

public class SplitPaymentStrategy implements PaymentStrategy {
    private final ArrayList<Account> accounts = new ArrayList<>();
    private final ArrayList<Double> amounts = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkForErrors(final CommandInput input) {

        double amountPerUser = input.getAmount() / input.getAccounts().size();
        boolean insufficientMoney = false;
        String accountWithLowMoney = null;
        for (String iban : input.getAccounts()) {
            if (iban.chars().allMatch(Character::isLetter)) {
                return true;
            }
            Account account = Bank.getInstance().getAccounts().get(iban);
            if (account == null) {
                return true;
            }
            accounts.add(account);
            double amountPay =
                    CURRENCY_EXCHANGE_SERVICE.exchangeCurrency(new CurrencyPair(input.getCurrency(),
                            account.getCurrency()), amountPerUser);
            amounts.add(amountPay);
            if (account.isTransferPossible(amountPay)) {
                insufficientMoney = true;
                accountWithLowMoney = iban;
            }
        }
        String description =
                "Split payment of " + String.format("%.2f",
                        input.getAmount()) + " " + input.getCurrency();
        String error =
                "Account " + accountWithLowMoney + " has insufficient funds for a split payment.";
        if (insufficientMoney) {
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(description, input.getTimestamp())
                            .transactionName(Constants.SPLIT_PAYMENT_FAILED_TRANSACTION)
                            .accountsList(accounts)
                            .accounts(input.getAccounts())
                            .currency(input.getCurrency())
                            .amount(amountPerUser)
                            .errorMessage(error)
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            return true;
        }

        DatesForTransaction datesForTransaction =
                new DatesForTransaction.Builder(description, input.getTimestamp())
                        .transactionName(Constants.SPLIT_PAYMENT_TRANSFER)
                        .currency(input.getCurrency())
                        .amount(amountPerUser)
                        .accounts(input.getAccounts())
                        .accountsList(accounts)
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);
        return false;

    }

    /**
     * Se efectuaza plata pentru fiecare cont din lista
     */
    @Override
    public void pay() {
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            account.pay(amounts.get(i));
        }
    }
}
