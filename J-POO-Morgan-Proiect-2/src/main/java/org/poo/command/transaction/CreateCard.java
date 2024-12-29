package org.poo.command.transaction;

import org.poo.command.Command;
import org.poo.entities.Bank;
import org.poo.entities.bankAccount.Account;
import org.poo.entities.card.Card;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.TransactionManager;
import org.poo.utils.factory.CardFactory;

public class CreateCard implements Command {
    /**
     * Creeaza un card nou
     * @param input obiectul ce contine informatiile ncesare pentru a efectua comanda
     */
    @Override
    public void execute(final CommandInput input) {
    try {
        if (Bank.getInstance().getUsers().get(input.getEmail()) == null) {
           throw new IllegalArgumentException("Utilizatorul nu exista: " + input.getEmail());
        }
        Account account = Bank.getInstance().getUsers().get(input.getEmail()).getAccounts()
                .get(input.getAccount());
        if (account == null) {
            throw new IllegalArgumentException("Contul nu exista: " + input.getAccount());
        }


        Card card = CardFactory.getCard(input);
        if (card == null) {
            throw new IllegalArgumentException("Nu se poate crea cardul dupa comanda data: "
                    + input.getCommand());
        }
        BANKING_SERVICES.addCard(card, account);
        DatesForTransaction datesForTransaction =
                new DatesForTransaction.Builder(Constants.NEW_CARD_CREATED, input.getTimestamp())
                        .transactionName(Constants.ADD_CARD_TRANSACTION)
                        .cardNumber(card.getCardNumber())
                        .iban(input.getAccount())
                        .userEmail(input.getEmail())
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);
        } catch (IllegalArgumentException e) {
            System.err.println("Erroare: " + e.getMessage());
        }
    }
}
