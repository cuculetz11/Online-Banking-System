package org.poo.entities.bankAccount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.command.debug.dto.AccountDeleteInfo;
import org.poo.command.debug.dto.DebugActionsDTO;
import org.poo.command.debug.dto.DeleteAccountDTO;
import org.poo.command.debug.dto.ErrorPrint;
import org.poo.entities.Bank;
import org.poo.entities.card.Card;
import org.poo.fileio.CommandInput;
import org.poo.services.BankingServices;
import org.poo.utils.*;

@Setter
@Getter
public class SavingsAccount extends Account {
    @JsonIgnore
    private double interestRate;

    public SavingsAccount(final CommandInput input) {
        super(0, input.getCurrency(), "savings");
        this.interestRate = input.getInterestRate();
    }


    /**
     * {@inheritDoc}
     * @param input datale necesare
     */
    @Override
    public void addInterest(final CommandInput input) {
        DatesForTransaction datesForTransaction =
                new DatesForTransaction.Builder(Constants.INTEREST_RATE_INCOME,
                        input.getTimestamp())
                        .transactionName(Constants.INTEREST_RATE_TRANSACTION)
                        .userEmail(this.getUser().getEmail())
                        .amount(this.getBalance() * this.getInterestRate())
                        .currency(this.getCurrency())
                        .iban(this.getIban())
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);

        this.setBalance(this.getBalance() + this.getBalance()
                * this.getInterestRate());
    }

    /**
     * {@inheritDoc}
     * @param input datele necesare
     */
    @Override
    public void changeInterestRate(final CommandInput input) {
        this.setInterestRate(input.getInterestRate());
        DatesForTransaction datesForTransaction =
                new DatesForTransaction.Builder(
                        "Interest rate of the account changed to "
                                + input.getInterestRate(), input.getTimestamp())
                        .transactionName(Constants.CHANGE_INTEREST_RATE_TRANSACTION)
                        .userEmail(this.getUser().getEmail())
                        .iban(this.getIban())
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);
    }

    /**
     * {@inheritDoc}
     * @param input datale necesare
     */
    @Override
    public void spendingReport(final CommandInput input) {
        if(input.getCommand().equals(Constants.BUSINESS_REPORT)) {
            System.out.println("nu e const business");
            return;
        }
        ErrorManager.wrongType(input.getCommand(), input.getTimestamp());
    }

    @Override
    public void deposit(CommandInput input) {
        if(!input.getEmail().equals(this.getUser().getEmail()))
            return;
        setBalance(getBalance() + input.getAmount());
    }

    @Override
    public void setMinBalance(CommandInput input) {
        setMinimumBalance(input.getAmount());
    }

    @Override
    public void deleteCard(CommandInput input) {
        if (!this.getUser().getEmail().equals(input.getEmail())) {
            System.out.println("acest card nu e a userului dat in comanda");
        }
        String iban = Bank.getInstance().getCards().get(input.getCardNumber()).getAccount()
                .getIban();
        BankingServices bankingServices = new BankingServices();
        if(this.getBalance() != 0)
            return;
        bankingServices.deleteCard(this, input.getCardNumber());

        DatesForTransaction datesForTransaction =
                new DatesForTransaction.Builder(Constants.THE_CARD_HAS_BEEN_DESTROYED,
                        input.getTimestamp())
                        .transactionName(Constants.DELETE_CARD_TRANSACTION)
                        .cardNumber(input.getCardNumber())
                        .userEmail(input.getEmail())
                        .iban(iban)
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);
    }

    @Override
    public void deleteAccount(CommandInput input) {
        AccountDeleteInfo data = null;
        if(this.getBalance() > 0 || !this.getUser().getEmail().equals(input.getEmail())) {
            DatesForTransaction datesForTransaction =
                    new DatesForTransaction.Builder(Constants.ACCOUNT_CANT_BE_DELETED_FUNDS,
                            input.getTimestamp())
                            .transactionName(Constants.DELETE_ACCOUNT_FAIL_TRANSACTION)
                            .userEmail(input.getEmail())
                            .build();
            TransactionManager.generateAndAddTransaction(datesForTransaction);
            data = new ErrorPrint(Constants.ACCOUNT_CANT_BE_DELETED, input.getTimestamp());
            DebugActionsDTO<AccountDeleteInfo> wasAccountDeleted =
                    new DebugActionsDTO<>(input.getCommand(), data, input.getTimestamp());
            JsonOutManager.getInstance().addToOutput(wasAccountDeleted);
            return;
        }

        for (Card c : this.getCards().values()) {
            Bank.getInstance().getCards().remove(c.getCardNumber());
        }
        this.getCards().clear();
        Bank.getInstance().getUsers().get(input.getEmail()).getAccounts().remove(this.getIban());
        Bank.getInstance().getAccounts().remove(this.getIban());

        data = new DeleteAccountDTO(Constants.ACCOUNT_DELETED, input.getTimestamp());
        DebugActionsDTO<AccountDeleteInfo> wasAccountDeleted =
                new DebugActionsDTO<>(input.getCommand(), data, input.getTimestamp());
        JsonOutManager.getInstance().addToOutput(wasAccountDeleted);
    }

    @Override
   public void withdrawMoney(final double money) {
        setBalance(getBalance() - money);
    }
}
