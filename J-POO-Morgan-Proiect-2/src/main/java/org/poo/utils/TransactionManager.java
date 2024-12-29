package org.poo.utils;

import org.poo.entities.bankAccount.Account;
import org.poo.entities.transaction.*;

public final class TransactionManager {
    private TransactionManager() { }

    /**
     * Reprezinta un factory pentru fiecare trnzactie ceruta
     * @param datesForTransaction datele necesare tranzactiei
     */
    public static void generateAndAddTransaction(final DatesForTransaction datesForTransaction) {
        Transaction transaction;
        switch (datesForTransaction.getTransactionName()) {
            case Constants.ADD_ACCOUNT_TRANSACTION:
                transaction = new Transaction(datesForTransaction.getTimestamp(),
                        datesForTransaction.getDescription());
                transaction.addToAccountHistory(datesForTransaction.getIban());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                break;

            case Constants.CHANGE_INTEREST_RATE_TRANSACTION,
                 Constants.FROZEN_CARD_TRANSACTION,
                 Constants.INSUFFICIENT_FUNDS_PAY_ONLINE_TRANSACTION,
                 Constants.DELETE_ACCOUNT_FAIL_TRANSACTION,
                 Constants.CHECK_CARD_TRANSACTION:
                transaction = new Transaction(datesForTransaction.getTimestamp(),
                        datesForTransaction.getDescription());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                break;

            case Constants.DELETE_CARD_TRANSACTION:
                transaction = new LifeOfACard(datesForTransaction.getDescription(),
                        datesForTransaction.getCardNumber(), datesForTransaction.getUserEmail(),
                        datesForTransaction.getTimestamp(), datesForTransaction.getIban());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                break;

            case Constants.ADD_CARD_TRANSACTION:
                transaction = new LifeOfACard(datesForTransaction.getDescription(),
                        datesForTransaction.getCardNumber(), datesForTransaction.getUserEmail(),
                        datesForTransaction.getTimestamp(), datesForTransaction.getIban());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                transaction.addToAccountHistory(datesForTransaction.getIban());
                break;
            case Constants.CARD_PAYMENT_TRANSACTION:
                transaction = new CardPayment(datesForTransaction.getAmount(),
                        datesForTransaction.getCommerciant(), datesForTransaction.getTimestamp(),
                        datesForTransaction.getDescription());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                transaction.addToAccountHistory(datesForTransaction.getIban());
                break;
            case Constants.INSUFFICIENT_FUNDS_TRANSFER_TRANSACTION:
                transaction = new Transaction(datesForTransaction.getTimestamp(),
                        datesForTransaction.getDescription());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                transaction.addToAccountHistory(datesForTransaction.getIban());
                break;
            case Constants.TRANSFER_TRANSACTION:
                transaction = new Transfer(datesForTransaction.getTimestamp(),
                        datesForTransaction.getDescription(), datesForTransaction.getSenderIban(),
                        datesForTransaction.getReceiverIban(), datesForTransaction.getMoney(),
                        datesForTransaction.getTransferType());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                transaction.addToAccountHistory(datesForTransaction.getIban());
                break;
            case Constants.SPLIT_PAYMENT_FAILED_TRANSACTION:
                transaction = new ErrorSplitPayment(datesForTransaction.getTimestamp(),
                        datesForTransaction.getCurrency(), datesForTransaction.getAmount(),
                        datesForTransaction.getAccounts(), datesForTransaction.getDescription(),
                        datesForTransaction.getErrorMessage());
                for (Account account : datesForTransaction.getAccountsList()) {
                    transaction.addToAccountHistory(account.getIban());
                    transaction.addToUserHistory(account.getUser().getEmail());
                }
                break;
            case Constants.SPLIT_PAYMENT_TRANSFER:
                transaction = new SplitPayment(datesForTransaction.getTimestamp(),
                        datesForTransaction.getCurrency(), datesForTransaction.getAmount(),
                        datesForTransaction.getAccounts(), datesForTransaction.getDescription());
                for (Account account : datesForTransaction.getAccountsList()) {
                    transaction.addToAccountHistory(account.getIban());
                    transaction.addToUserHistory(account.getUser().getEmail());
                }
                break;
            default:
                break;
        }

    }
}
