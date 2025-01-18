package org.poo.entities.card;

import org.poo.command.transaction.CreateCard;
import org.poo.command.transaction.DeleteCard;
import org.poo.entities.Bank;
import org.poo.fileio.CommandInput;
import org.poo.services.BankingServices;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.TransactionManager;

public class OneTimePayCard extends Card {
    public OneTimePayCard() {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pay(final double amount) {

        super.getAccount().pay(amount);
        String userEmail = super.getAccount().getUser().getEmail();
        String iban = super.getAccount().getIban();

        CommandInput inputCreate = new CommandInput();
        inputCreate.setAccount(iban);
        inputCreate.setEmail(userEmail);
        inputCreate.setCommand("createOneTimeCard");
        inputCreate.setTimestamp(Bank.getInstance().getCurrentInput().getTimestamp());

        CommandInput inputDelete = new CommandInput();
        inputDelete.setCardNumber(super.getCardNumber());
        inputDelete.setEmail(userEmail);
        inputDelete.setTimestamp(Bank.getInstance().getCurrentInput().getTimestamp());


//        DeleteCard deleteCard = new DeleteCard();
//        deleteCard.execute(inputDelete);
        CommandInput input = Bank.getInstance().getCurrentInput();
        BankingServices bankingServices = new BankingServices();
        bankingServices.deleteCard(this.getAccount(), input.getCardNumber());

        DatesForTransaction datesForTransaction =
                new DatesForTransaction.Builder(Constants.THE_CARD_HAS_BEEN_DESTROYED,
                        input.getTimestamp())
                        .transactionName(Constants.DELETE_CARD_TRANSACTION)
                        .cardNumber(input.getCardNumber())
                        .userEmail(input.getEmail())
                        .iban(iban)
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);

        CreateCard createCard = new CreateCard();
        createCard.execute(inputCreate);

    }
}
