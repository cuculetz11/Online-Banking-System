package org.poo.services.withdraw;

import org.poo.entities.Bank;
import org.poo.entities.CurrencyPair;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.card.Card;
import org.poo.fileio.CommandInput;
import org.poo.services.payment.PaymentStrategy;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.ErrorManager;
import org.poo.utils.TransactionManager;

public class WithdrawCashAction implements PaymentStrategy {
    private Account cardAccount;
    private double amount;
    @Override
    public void pay() {
        cardAccount.pay(amount);
    }

    @Override
    public boolean checkForErrors(CommandInput input) {
        try {
            Card card = Bank.getInstance().getCards().get(input.getCardNumber());
            if (card == null) {
                ErrorManager.notFound(Constants.CARD_NOT_FOUND, input.getCommand(),
                        input.getTimestamp());
                return true;
            }
            this.cardAccount = card.getAccount();
            if(!card.getAccount().getUser().getEmail().equals(input.getEmail())) {
                System.out.println("problem");
                return true;
            }
            if(card.getStatus().equals("frozen")) {
                System.out.println("problem");
                return true;
            }
            this.amount = CURRENCY_EXCHANGE_SERVICE.exchangeCurrency(new CurrencyPair("RON", cardAccount.getCurrency()), input.getAmount());
            if(this.cardAccount.getMinimumBalance() >= cardAccount.getBalance() - this.amount - cardAccount.getUser().getPlan().getCommissionPlan().commission(amount, cardAccount.getCurrency())) {
                System.out.println("problem");
                return true;
            }
            if(cardAccount.isTransferPossible(amount)) {
                System.out.println("problem");
                return true;
            }
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder("Cash withdrawal of " + String.valueOf(input.getAmount()),
                            input.getTimestamp())
                            .transactionName(Constants.CASH_WITHDRAWAL_TRANSACTION)
                            .userEmail(input.getEmail())
                            .amount(input.getAmount())
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);

            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
