package org.poo.services.payment;

import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.card.Card;
import org.poo.entities.transaction.CardPayment;
import org.poo.entities.transaction.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.ErrorManager;
import org.poo.utils.TransactionManager;

public class CardPaymentStrategy implements PaymentStrategy {
    private Card card;
    private double amountToPay;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean checkForErrors(final CommandInput input) {
        Card cardInput = Bank.getInstance().getCards().get(input.getCardNumber());
        if (cardInput == null) {
            ErrorManager.notFound(Constants.CARD_NOT_FOUND, input.getCommand(),
                    input.getTimestamp());
            return true;
        }
        this.card = cardInput;
        if (cardInput.getStatus().equals(Constants.FROZEN)) {
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.THE_CARD_IS_FROZEN,
                            input.getTimestamp())
                            .transactionName(Constants.FROZEN_CARD_TRANSACTION)
                            .userEmail(input.getEmail())
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            return true;
        }

        double amount =
                CURRENCY_EXCHANGE_SERVICE.exchangeCurrency(new CurrencyPair(input.getCurrency(),
                        cardInput.getAccount().getCurrency()), input.getAmount());
        if (cardInput.getAccount().isTransferPossible(amount)) {
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.INSUFFICIENT_FUNDS,
                            input.getTimestamp())
                            .transactionName(Constants.INSUFFICIENT_FUNDS_PAY_ONLINE_TRANSACTION)
                            .userEmail(input.getEmail())
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            return true;
        }
        this.amountToPay = amount;
        DatesForTransaction datesForTransaction =
                new DatesForTransaction.Builder(Constants.CARD_PAYMENT, input.getTimestamp())
                        .transactionName(Constants.CARD_PAYMENT_TRANSACTION)
                        .commerciant(input.getCommerciant())
                        .userEmail(input.getEmail())
                        .iban(cardInput.getAccount().getIban())
                        .amount(amount)
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);
        Transaction transaction = new CardPayment(amount, input.getCommerciant(),
                input.getTimestamp(), Constants.CARD_PAYMENT);
        return false;
    }

    /**
     * Reprezinta o plata cu cardul
     */
    @Override
    public void pay() {
        card.pay(amountToPay);
    }
}
