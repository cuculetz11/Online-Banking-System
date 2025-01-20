package org.poo.services.payment;

import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.commerciant.Commerciant;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.ErrorManager;
import org.poo.utils.TransactionManager;

public class BankTransferStrategy implements PaymentStrategy {
    private Account sender;
    private Account receiver;
    private double amount;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkForErrors(final CommandInput input) {
        try {
            Account senderAccount = Bank.getInstance().getAccounts().get(input.getAccount());
            if (senderAccount == null) {
                ErrorManager.notFound(Constants.USER_NOT_FOUND, input.getCommand(),
                        input.getTimestamp());
                return true;
            }
            Account receiverAccount = Bank.getInstance().getAccounts().get(input.getReceiver());
            if (receiverAccount == null && !Bank.getInstance().getCommerciants().containsKey(
                    input.getReceiver())) {
                ErrorManager.notFound(Constants.USER_NOT_FOUND, input.getCommand(),
                        input.getTimestamp());
                return true;
            }
            if (input.getAccount().chars().allMatch(Character::isLetter)) {
                throw new IllegalArgumentException("Sender-ul nu trebuie sa fie alias: "
                        + input.getAccount());
            }
            if (senderAccount.isTransferPossible(input.getAmount())
                    .equals(Constants.TRANSACTION_IMPOSSIBLE)) {
                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder(Constants.INSUFFICIENT_FUNDS,
                                input.getTimestamp())
                                .transactionName(Constants.INSUFFICIENT_FUNDS_TRANSFER_TRANSACTION)
                                .iban(senderAccount.getIban())
                                .userEmail(input.getEmail())
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
                return true;
            }
            if (senderAccount.isTransferPossible(input.getAmount())
                    .equals(Constants.LIMIT_EXCEEDED)) {
                return true;
            }

            this.receiver = receiverAccount;
            this.sender = senderAccount;
            this.amount = input.getAmount();

            if (Bank.getInstance().getCommerciants().containsKey(input.getReceiver())) {
                Commerciant commerciant = Bank.getInstance().getCommerciants()
                        .get(input.getReceiver());
                sender.getUser().getPlan().createCashBackContext(commerciant, sender, amount);
                sender.getUser().getPlan().performCashback();
            }

            String money = input.getAmount() + " " + sender.getCurrency();
            DatesForTransaction datesForTransactionSender =
                    new DatesForTransaction.Builder(input.getDescription(), input.getTimestamp())
                            .transactionName(Constants.TRANSFER_TRANSACTION)
                            .iban(sender.getIban())
                            .senderIban(sender.getIban())
                            .receiverIban(input.getReceiver())
                            .userEmail(sender.getUser().getEmail())
                            .money(money)
                            .transferType(Constants.SENT)
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransactionSender);
            double exchangedAmount = amount;

            if (receiverAccount != null) {
                exchangedAmount = CURRENCY_EXCHANGE_SERVICE.exchangeCurrency(new
                        CurrencyPair(sender.getCurrency(), receiver.getCurrency()), amount);
                String exchangedMoney = exchangedAmount + " " + receiver.getCurrency();

                DatesForTransaction datesForTransactionReceiver =
                        new DatesForTransaction.Builder(input.getDescription(),
                                input.getTimestamp())
                                .transactionName(Constants.TRANSFER_TRANSACTION)
                                .iban(receiver.getIban())
                                .senderIban(sender.getIban())
                                .receiverIban(receiver.getIban())
                                .userEmail(receiver.getUser().getEmail())
                                .money(exchangedMoney)
                                .transferType(Constants.RECEIVED)
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransactionReceiver);
            }
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    /**
     * Transferul bancar
     */
    @Override
    public void pay() {
        sender.transfer(receiver, amount);
    }
}
