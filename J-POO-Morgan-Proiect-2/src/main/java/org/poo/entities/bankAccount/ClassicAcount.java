package org.poo.entities.bankAccount;

import org.poo.command.debug.dto.AccountMerchantsDTO;
import org.poo.command.debug.dto.DebugActionsDTO;
import org.poo.entities.Merchant;
import org.poo.entities.transaction.CardPayment;
import org.poo.entities.transaction.Transaction;
import org.poo.fileio.CommandInput;
import org.poo.utils.Constants;
import org.poo.utils.ErrorManager;
import org.poo.utils.JsonOutManager;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ClassicAcount extends Account {
    public ClassicAcount(final CommandInput input) {
        super(0, input.getCurrency(), "classic");
    }

    /**
     * {@inheritDoc}
     * @param input datale necesare
     */
    @Override
    public void addInterest(final CommandInput input) {
        ErrorManager.notFound(Constants.NOT_SAVINGS_ACCOUNT, input.getCommand(),
                input.getTimestamp());
    }

    /**
     * {@inheritDoc}
     * @param input datele necesare
     */
    @Override
    public void changeInterestRate(final CommandInput input) {
        ErrorManager.notFound(Constants.NOT_SAVINGS_ACCOUNT, input.getCommand(),
                input.getTimestamp());
    }

    /**
     * {@inheritDoc}
     * @param input datale necesare
     */
    @Override
    public void spendingReport(final CommandInput input) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        LinkedHashMap<String, Merchant> merchants = new LinkedHashMap<>();


        for (Transaction transaction : this.getTransactionsHistory()) {
            if (transaction.getTimestamp() > input.getEndTimestamp()) {
                break;
            }
            if (transaction.getTimestamp() >= input.getStartTimestamp()
                    && transaction.getDescription().equals(Constants.CARD_PAYMENT)) {
                transactions.add(transaction);
                CardPayment cardPayment = (CardPayment) transaction;
                if (!merchants.containsKey(cardPayment.getCommerciant())) {
                    Merchant merchant = new Merchant(cardPayment.getCommerciant(),
                            cardPayment.getAmount());
                    merchants.put(cardPayment.getCommerciant(), merchant);
                } else {
                    merchants.get(cardPayment.getCommerciant()).setTotal(merchants.get(cardPayment
                            .getCommerciant()).getTotal() + cardPayment.getAmount());
                }
            }
        }
        ArrayList<Merchant> merchantsList = new ArrayList<>(merchants.values());
        merchantsList.sort((c2, c1) -> c2.getCommerciant().compareTo(c1.getCommerciant()));
        AccountMerchantsDTO accountCommerciantsDTO = new AccountMerchantsDTO(input.getAccount(),
                this.getBalance(), this.getCurrency(), transactions, merchantsList);
        DebugActionsDTO<AccountMerchantsDTO> spendingReports =
                new DebugActionsDTO<>(input.getCommand(), accountCommerciantsDTO,
                        input.getTimestamp());
        JsonOutManager.getInstance().addToOutput(spendingReports);
    }
}
