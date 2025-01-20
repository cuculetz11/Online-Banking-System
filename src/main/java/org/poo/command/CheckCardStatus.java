package org.poo.command;

import org.poo.entities.Bank;
import org.poo.entities.card.Card;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.ErrorManager;
import org.poo.utils.TransactionManager;

public class CheckCardStatus implements Command {
    /**
     * Verifica status-ul unui card
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        try {
            Card card = Bank.getInstance().getCards().get(input.getCardNumber());
            if (card == null) {
                ErrorManager.notFound(Constants.CARD_NOT_FOUND, input.getCommand(),
                        input.getTimestamp());
                throw new IllegalArgumentException("Nu exista cardul: " + input.getCardNumber());
            }
            if (card.getStatus().equals(Constants.FROZEN) && card.getAccount().verifyBalance()) {
                throw new IllegalArgumentException("Este deja inghetat: " + input.getCardNumber());
            }
            if (card.getAccount().verifyBalance()) {
                card.setStatus(Constants.FROZEN);

                DatesForTransaction datesForTransaction =
                        new DatesForTransaction.Builder(Constants.THE_CARD_WILL_BE_FROZEN,
                                input.getTimestamp())
                                .transactionName(Constants.CHECK_CARD_TRANSACTION)
                                .userEmail(card.getAccount().getUser().getEmail())
                                .build();
                TransactionManager.generateAndAddTransaction(datesForTransaction);
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }
    }
}
