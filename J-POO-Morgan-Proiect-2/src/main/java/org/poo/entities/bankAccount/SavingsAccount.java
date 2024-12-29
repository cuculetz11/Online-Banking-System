package org.poo.entities.bankAccount;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.DatesForTransaction;
import org.poo.utils.ErrorManager;
import org.poo.utils.TransactionManager;

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
                        .build();
        TransactionManager.generateAndAddTransaction(datesForTransaction);
    }

    /**
     * {@inheritDoc}
     * @param input datale necesare
     */
    @Override
    public void spendingReport(final CommandInput input) {
        ErrorManager.wrongType(input.getCommand(), input.getTimestamp());
    }
}
