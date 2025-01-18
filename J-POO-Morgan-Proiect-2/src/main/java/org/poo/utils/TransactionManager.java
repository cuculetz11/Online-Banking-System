package org.poo.utils;

import org.poo.command.transaction.UpgradePlan;
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
            case Constants.ADD_ACCOUNT_TRANSACTION,
                 Constants.CHANGE_INTEREST_RATE_TRANSACTION:
                transaction = new Transaction(datesForTransaction.getTimestamp(),
                        datesForTransaction.getDescription());
                transaction.addToAccountHistory(datesForTransaction.getIban());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                break;

            case Constants.FROZEN_CARD_TRANSACTION,
                 Constants.INSUFFICIENT_FUNDS_PAY_ONLINE_TRANSACTION,
                 Constants.DELETE_ACCOUNT_FAIL_TRANSACTION,
                 Constants.CHECK_CARD_TRANSACTION,
                 Constants.MIN_AGE_TRANSACTION:
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
            case Constants.INSUFFICIENT_FUNDS_TRANSFER_TRANSACTION,
                 Constants.DONT_HAVE_CLASSIC_TRANSACTION,
                 Constants.PLAN_ALREADY_TRANSACTION:
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
                if(datesForTransaction.getSplitPaymentType().equals("custom")) {
                    transaction = new ErrorSplitPayment(datesForTransaction.getTimestamp(),
                            datesForTransaction.getCurrency(), datesForTransaction.getAccounts(),
                            datesForTransaction.getDescription(), datesForTransaction.getSplitPaymentType(),
                            datesForTransaction.getAmountForUsers(), datesForTransaction.getErrorMessage());
                } else {
                    transaction = new ErrorSplitPaymentEq(datesForTransaction.getTimestamp(),
                            datesForTransaction.getCurrency(),datesForTransaction.getAccounts(),
                            datesForTransaction.getDescription(), datesForTransaction.getSplitPaymentType(),
                            Math.round(datesForTransaction.getAmountForUsers().get(0) * 100) / 100.0, datesForTransaction.getErrorMessage());
                }

                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                transaction.addToAccountHistory(datesForTransaction.getIban());
                break;
            case Constants.SPLIT_PAYMENT_TRANSACTION:
                if(datesForTransaction.getSplitPaymentType().equals("custom")) {
                    transaction = new SplitPayment(datesForTransaction.getTimestamp(),
                            datesForTransaction.getCurrency(),
                            datesForTransaction.getAccounts(), datesForTransaction.getDescription(),
                            datesForTransaction.getSplitPaymentType(), datesForTransaction.getAmountForUsers());
                } else {
                    transaction = new SplitPaymentEq(datesForTransaction.getTimestamp(),
                            datesForTransaction.getCurrency(), datesForTransaction.getAccounts(),
                            datesForTransaction.getDescription(), datesForTransaction.getSplitPaymentType(),
                            Math.round(datesForTransaction.getAmountForUsers().get(0) * 100) / 100.0);
                }
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                transaction.addToAccountHistory(datesForTransaction.getIban());
                break;
            case Constants.UPGRADE_PLAN_TRANSACTION:
                transaction = new NewPlan(datesForTransaction.getDescription(),
                        datesForTransaction.getTimestamp(), datesForTransaction.getIban(),
                        datesForTransaction.getNewPlanType());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                transaction.addToAccountHistory(datesForTransaction.getIban());
                break;
            case Constants.CASH_WITHDRAWAL_TRANSACTION:
                transaction = new CashWithdrawal(datesForTransaction.getDescription(),
                        datesForTransaction.getTimestamp(), datesForTransaction.getAmount());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                break;
            case Constants.INTEREST_RATE_TRANSACTION:
                transaction = new InterestRate(datesForTransaction.getDescription(),
                        datesForTransaction.getTimestamp(), datesForTransaction.getAmount(),
                        datesForTransaction.getCurrency());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
                transaction.addToAccountHistory(datesForTransaction.getIban());
                break;
            case Constants.Savings_WITHDRAWAL_TRANSACTION:
                transaction = new SavingsWithdrawal(datesForTransaction.getAccounts().get(0),
                        datesForTransaction.getDescription(),
                        datesForTransaction.getAccounts().get(1),
                        datesForTransaction.getAmount(), datesForTransaction.getTimestamp());
                transaction.addToUserHistory(datesForTransaction.getUserEmail());
            default:
                break;
        }

    }
}
