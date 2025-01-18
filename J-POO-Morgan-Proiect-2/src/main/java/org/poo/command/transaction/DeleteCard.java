package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.card.Card;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.ErrorManager;
import org.poo.utils.TransactionManager;

public class DeleteCard implements Command {
    /**
     * Sterge cardul selectat
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
        try {
            if (Bank.getInstance().getCardDeletedHistory().containsKey(input.getCardNumber())) {
                throw new Exception("Acest card a mai fost sters: " + input.getCardNumber());
            }
            Card card = Bank.getInstance().getCards().get(input.getCardNumber());
            if (card == null) {
                ErrorManager.notFound(input.getCardNumber() + " " + input.getEmail(),
                        input.getCommand(), input.getTimestamp());
                return;
            }

            Account cardHolder = card.getAccount();
            if (cardHolder == null) {
                throw new IllegalArgumentException("Contul nu exista: " + input.getCardNumber());
            }
            if(!cardHolder.checkPropriety(input.getEmail())) {
                System.out.println("proprietate proasta");
                return;
            }
            cardHolder.deleteCard(input);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
