package org.poo.services.payment;

import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.card.Card;
import org.poo.entities.commerciant.Commerciant;
import org.poo.entities.transaction.CardPayment;
import org.poo.entities.transaction.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.services.cashbackService.CashBackContext;
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
        //System.out.println("cardul e: "+  cardInput + " proprietarul e: " + cardInput.getCardOwnerEmail() + " emailul din comanda: " + input.getEmail() );
        if(input.getAmount() == 0) {
            return true;
        }
        if (cardInput == null) {
            ErrorManager.notFound(Constants.CARD_NOT_FOUND, input.getCommand(),
                    input.getTimestamp());
            return true;
        }
        if(!cardInput.getAccount().getType().equals("business") && !input.getEmail().equals(cardInput.getCardOwnerEmail())) {
            ErrorManager.notFound(Constants.CARD_NOT_FOUND, input.getCommand(),
                    input.getTimestamp());
            System.out.println("aici");
            return true;
        }
        if(!cardInput.getAccount().checkPropriety(input.getEmail())) {
            ErrorManager.notFound(Constants.CARD_NOT_FOUND, input.getCommand(),
                    input.getTimestamp());
            System.out.println("aici");
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
        if(Bank.getInstance().getCommerciants().get(input.getCommerciant()) == null) {
            System.out.println("comercinat invalid");
            return true;
        }
        double amount =
                CURRENCY_EXCHANGE_SERVICE.exchangeCurrency(new CurrencyPair(input.getCurrency(),
                        cardInput.getAccount().getCurrency()), input.getAmount());
        if (cardInput.getAccount().isTransferPossible(amount).equals(Constants.TRANSACTION_IMPOSSIBLE)) {
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.INSUFFICIENT_FUNDS,
                            input.getTimestamp())
                            .transactionName(Constants.INSUFFICIENT_FUNDS_PAY_ONLINE_TRANSACTION)
                            .userEmail(input.getEmail())
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            return true;
        }

        if (cardInput.getAccount().isTransferPossible(amount).equals(Constants.LIMIT_EXCEEDED)) {
            System.out.println("limita cardului exceeed");
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
        Commerciant commerciant = Bank.getInstance().getCommerciants().get(input.getCommerciant());
        card.getAccount().getUser().getPlan().checkUpdate(amount,card.getAccount().getCurrency(), card.getAccount());
        card.getAccount().getUser().getPlan().setCashBackContext(new CashBackContext(commerciant, card.getAccount(), amountToPay));
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
