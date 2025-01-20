package org.poo.services.withdraw;

import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.fileio.CommandInput;
import org.poo.services.payment.PaymentStrategy;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.TransactionManager;

import java.time.LocalDate;
import java.time.Period;
import java.util.Arrays;

public class WithdrawSavingsAction implements PaymentStrategy {
    private Account account;
    private double exchangedAmount;
    private Account classicAccount;

    /**
     * Scoatem banii de pe card fara comision si ii adugam in contul clasic
     */
    @Override
    public void pay() {
        account.withdrawMoney(exchangedAmount);
        classicAccount.addMoney(exchangedAmount, account.getCurrency());
    }

    /**
     * Verificam daca se poate efectua scoaterea
     * @param input data necesare pentru verificare
     * @return fals daca se poate face, adevarat altfel
     */
    @Override
    public boolean checkForErrors(final CommandInput input) {
        try {
            this.account = Bank.getInstance().getAccounts().get(input.getAccount());
            if (this.account == null) {
                throw new IllegalArgumentException("Contul nu exitsa: " + input.getAccount());
            }
            boolean hasClassic = false;

            for (Account a : account.getUser().getAccounts().values()) {
                if (a.getType().equals("classic") && a.getCurrency().equals(input.getCurrency())) {
                    hasClassic = true;
                    classicAccount = a;
                    break;
                }
            }
            if (!hasClassic) {
                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder(Constants.DONT_HAVE_CLASSIC,
                                input.getTimestamp())
                                .transactionName(Constants.DONT_HAVE_CLASSIC_TRANSACTION)
                                .userEmail(account.getUser().getEmail())
                                .iban(account.getIban())
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
                return true;
            }
            int ages = Period.between(LocalDate.parse(this.account.getUser().getBirthDate()),
                    LocalDate.now()).getYears();
            if (ages < Constants.MINIMUM_AGE) {
                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder(Constants.DONT_HAVE_MIN_AGE,
                                input.getTimestamp())
                                .transactionName(Constants.MIN_AGE_TRANSACTION)
                                .userEmail(account.getUser().getEmail())
                                .iban(account.getIban())
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
                return true;
            }
            this.exchangedAmount = CURRENCY_EXCHANGE_SERVICE.exchangeCurrency(
                    new CurrencyPair(input.getCurrency(),
                            this.account.getCurrency()), input.getAmount());
            if (this.account.isTransferPossibleWhCommision(this.exchangedAmount)) {
                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder(Constants.INSUFFICIENT_FUNDS,
                                input.getTimestamp())
                                .transactionName(Constants.INSUFFICIENT_FUNDS_TRANSFER_TRANSACTION)
                                .iban(this.account.getIban())
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
                return true;
            }
            if (this.account.verifyBalance()) {
                return true;
            }

            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.SAVINGS_WITHDRAWAL,
                            input.getTimestamp())
                            .transactionName(Constants.SAVINGS_WITHDRAWAL_TRANSACTION)
                            .accounts(Arrays.asList(classicAccount.getIban(), account.getIban()))
                            .userEmail(account.getUser().getEmail())
                            .amount(this.exchangedAmount)
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            datesForTransaction =
                    new DatesForTransaction.Builder(Constants.SAVINGS_WITHDRAWAL,
                            input.getTimestamp())
                            .transactionName(Constants.SAVINGS_WITHDRAWAL_TRANSACTION)
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
