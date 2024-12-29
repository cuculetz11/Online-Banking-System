package org.poo.entities.card;

import org.poo.command.transaction.CreateCard;
import org.poo.command.transaction.DeleteCard;
import org.poo.entities.Bank;
import org.poo.fileio.CommandInput;

public class OneTimePayCard extends Card {
    public OneTimePayCard() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pay(final double amount) {

        super.getAccount().setBalance(super.getAccount().getBalance() - amount);
        String userEmail = super.getAccount().getUser().getEmail();
        String iban = super.getAccount().getIban();

        CommandInput inputCreate = new CommandInput();
        inputCreate.setAccount(iban);
        inputCreate.setEmail(userEmail);
        inputCreate.setCommand("createOneTimeCard");
        inputCreate.setTimestamp(Bank.getInstance().getCurrentTimestamp());

        CommandInput inputDelete = new CommandInput();
        inputDelete.setCardNumber(super.getCardNumber());
        inputDelete.setEmail(userEmail);
        inputDelete.setTimestamp(Bank.getInstance().getCurrentTimestamp());

        DeleteCard deleteCard = new DeleteCard();
        deleteCard.execute(inputDelete);

        CreateCard createCard = new CreateCard();
        createCard.execute(inputCreate);

    }
}
