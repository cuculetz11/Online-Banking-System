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

            if (!card.getAccount().getUser().getEmail().equals(input.getEmail())) {
                throw new Exception("Acest card nu este a user-ului ce vrea sa-l stearga: "
                        + input.getCardNumber());
            }

            Account account = Bank.getInstance().getCards().get(input.getCardNumber()).getAccount();
            if (account == null) {
                throw new IllegalArgumentException("Contul nu exista: " + input.getCardNumber());
            }
            String iban = Bank.getInstance().getCards().get(input.getCardNumber()).getAccount()
                    .getIban();

            BANKING_SERVICES.deleteCard(input.getEmail(), input.getCardNumber());
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.THE_CARD_HAS_BEEN_DESTROYED,
                            input.getTimestamp())
                            .transactionName(Constants.DELETE_CARD_TRANSACTION)
                            .cardNumber(input.getCardNumber())
                            .userEmail(input.getEmail())
                            .iban(iban)
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }
}
