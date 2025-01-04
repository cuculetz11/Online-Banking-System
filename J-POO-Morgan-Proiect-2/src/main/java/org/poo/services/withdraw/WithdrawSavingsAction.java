package org.poo.services.withdraw;

import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.services.payment.PaymentStrategy;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.TransactionManager;

public class WithdrawSavingsAction implements PaymentStrategy {
    private Account account;
    private double exchangedAmount;

    @Override
    public void pay() {
        account.pay(exchangedAmount);
    }

    @Override
    public boolean checkForErrors(CommandInput input) {
        try {
            this.account = Bank.getInstance().getAccounts().get(input.getAccount());
            if(this.account == null){
                throw new IllegalArgumentException("Contul nu exitsa: " + input.getAccount());
            }
            if(Integer.parseInt(this.account.getUser().getBirthDate().split("-")[0]) < 2024) {
                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder(Constants.DONT_HAVE_MIN_AGE,
                                input.getTimestamp())
                                .transactionName(Constants.MIN_AGE_TRANSACTION)
                                .userEmail(account.getUser().getEmail())
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
                return true;
            }
            this.exchangedAmount = CURRENCY_EXCHANGE_SERVICE.exchangeCurrency(new CurrencyPair(input.getCurrency(), this.account.getCurrency()), input.getAmount());
            if (this.account.isTransferPossible(this.exchangedAmount)) {
                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder(Constants.INSUFFICIENT_FUNDS,
                                input.getTimestamp())
                                .transactionName(Constants.INSUFFICIENT_FUNDS_TRANSFER_TRANSACTION)
                                .iban(this.account.getIban())
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
            return true;
        }
    }
}
